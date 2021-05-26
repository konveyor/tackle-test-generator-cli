/**
 * (C) Copyright IBM Corporation 2015.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ibm.websphere.samples.daytrader.ejb3;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBException;
//import javax.ejb.LocalBean;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.enterprise.concurrent.ManagedThreadFactory;
import javax.jms.JMSContext;
import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnectionFactory;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.RollbackException;

import com.ibm.websphere.samples.daytrader.TradeAction;
//import com.ibm.websphere.samples.daytrader.TradeServices;
import com.ibm.websphere.samples.daytrader.beans.MarketSummaryDataBean;
import com.ibm.websphere.samples.daytrader.beans.RunStatsDataBean;
import com.ibm.websphere.samples.daytrader.entities.AccountDataBean;
import com.ibm.websphere.samples.daytrader.entities.AccountProfileDataBean;
import com.ibm.websphere.samples.daytrader.entities.HoldingDataBean;
import com.ibm.websphere.samples.daytrader.entities.OrderDataBean;
import com.ibm.websphere.samples.daytrader.entities.QuoteDataBean;
import com.ibm.websphere.samples.daytrader.util.CompleteOrderThread;
import com.ibm.websphere.samples.daytrader.util.FinancialUtils;
import com.ibm.websphere.samples.daytrader.util.Log;
import com.ibm.websphere.samples.daytrader.util.TradeConfig;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TradeSLSBBean implements TradeSLSBRemote, TradeSLSBLocal {
	
    @Resource(name = "jms/QueueConnectionFactory", authenticationType = javax.annotation.Resource.AuthenticationType.APPLICATION)
    private QueueConnectionFactory queueConnectionFactory;

    @Resource(name = "jms/TopicConnectionFactory", authenticationType = javax.annotation.Resource.AuthenticationType.APPLICATION)
    private TopicConnectionFactory topicConnectionFactory;

    @Resource(lookup = "jms/TradeStreamerTopic")
    private Topic tradeStreamerTopic;

    @Resource(lookup = "jms/TradeBrokerQueue")
    private Queue tradeBrokerQueue;
    
    @Resource 
    private ManagedThreadFactory managedThreadFactory;
	
    /* JBoss 
    @Resource(name = "java:/jms/QueueConnectionFactory", authenticationType = javax.annotation.Resource.AuthenticationType.APPLICATION)
    private QueueConnectionFactory queueConnectionFactory;

    @Resource(name = "java:/jms/TopicConnectionFactory", authenticationType = javax.annotation.Resource.AuthenticationType.APPLICATION)
    private TopicConnectionFactory topicConnectionFactory;

    @Resource(lookup = "java:/jms/TradeStreamerTopic")
    private Topic tradeStreamerTopic;
        
    @Resource(lookup = "java:/jms/TradeBrokerQueue")
    private Queue tradeBrokerQueue;
    */
    
    @PersistenceContext
    private EntityManager entityManager;

    @Resource
    private SessionContext context;
    
    @EJB
    MarketSummarySingleton marketSummarySingleton;

    /** Creates a new instance of TradeSLSBBean */
    public TradeSLSBBean() {
        if (Log.doTrace()) {
            Log.trace("TradeSLSBBean:ejbCreate  -- JNDI lookups of EJB and JMS resources");
        }
    }

    @Override
    public MarketSummaryDataBean getMarketSummary() {

        if (Log.doTrace()) {
            Log.trace("TradeSLSBBean:getMarketSummary -- getting market summary");
        }

        return marketSummarySingleton.getMarketSummaryDataBean();
    }

    @Override
    public OrderDataBean buy(String userID, String symbol, double quantity, int orderProcessingMode) {
        OrderDataBean order;
        BigDecimal total;
        try {
            if (Log.doTrace()) {
                Log.trace("TradeSLSBBean:buy", userID, symbol, quantity, orderProcessingMode);
            }
            
            AccountProfileDataBean profile = entityManager.find(AccountProfileDataBean.class, userID);
            AccountDataBean account = profile.getAccount();
            QuoteDataBean quote = entityManager.find(QuoteDataBean.class, symbol);
            HoldingDataBean holding = null; // The holding will be created by
            // this buy order

            order = createOrder(account, quote, holding, "buy", quantity);
                      
            // UPDATE - account should be credited during completeOrder
            BigDecimal price = quote.getPrice();
            BigDecimal orderFee = order.getOrderFee();
            BigDecimal balance = account.getBalance();
            total = (new BigDecimal(quantity).multiply(price)).add(orderFee);
            account.setBalance(balance.subtract(total));
            final Integer orderID=order.getOrderID(); 
            
            if (orderProcessingMode == TradeConfig.SYNCH) {
                completeOrder(orderID, false);
            } else {
                entityManager.flush();
                queueOrder(orderID, true);
            }
        } catch (Exception e) {
            Log.error("TradeSLSBBean:buy(" + userID + "," + symbol + "," + quantity + ") --> failed", e);
            /* On exception - cancel the order */
            // TODO figure out how to do this with JPA
            // if (order != null) order.cancel();
            throw new EJBException(e);
        }
        return order;
    }

    @Override
    public OrderDataBean sell(final String userID, final Integer holdingID, int orderProcessingMode) {
        OrderDataBean order;
        BigDecimal total;
        try {
            if (Log.doTrace()) {
                Log.trace("TradeSLSBBean:sell", userID, holdingID, orderProcessingMode);
            }
            
            AccountProfileDataBean profile = entityManager.find(AccountProfileDataBean.class, userID);
            AccountDataBean account = profile.getAccount();

            HoldingDataBean holding = entityManager.find(HoldingDataBean.class, holdingID);
            
            if (holding == null) {
                Log.error("TradeSLSBBean:sell User " + userID + " attempted to sell holding " + holdingID + " which has already been sold");

                OrderDataBean orderData = new OrderDataBean();
                orderData.setOrderStatus("cancelled");
                entityManager.persist(orderData);

                return orderData;
            }

            QuoteDataBean quote = holding.getQuote();
            double quantity = holding.getQuantity();
            order = createOrder(account, quote, holding, "sell", quantity);

            // UPDATE the holding purchase data to signify this holding is
            // "inflight" to be sold
            // -- could add a new holdingStatus attribute to holdingEJB
            holding.setPurchaseDate(new java.sql.Timestamp(0));

            // UPDATE - account should be credited during completeOrder
            BigDecimal price = quote.getPrice();
            BigDecimal orderFee = order.getOrderFee();
            BigDecimal balance = account.getBalance();
            total = (new BigDecimal(quantity).multiply(price)).subtract(orderFee);
            account.setBalance(balance.add(total));
            final Integer orderID=order.getOrderID();

            if (orderProcessingMode == TradeConfig.SYNCH) {
                completeOrder(orderID, false);
            } else {
                entityManager.flush();
                queueOrder(orderID, true);
            }

        } catch (Exception e) {
            Log.error("TradeSLSBBean:sell(" + userID + "," + holdingID + ") --> failed", e);
            // if (order != null) order.cancel();
            // UPDATE - handle all exceptions like:
            throw new EJBException("TradeSLSBBean:sell(" + userID + "," + holdingID + ")", e);
        }
        return order;
    }

    @Override
    public void queueOrder(Integer orderID, boolean twoPhase) {
        if (Log.doTrace()) {
            Log.trace("TradeSLSBBean:queueOrder", orderID);
        }
                	
        if (TradeConfig.getOrderProcessingMode() == TradeConfig.ASYNCH_MANAGEDTHREAD) {
        
            Thread thread = managedThreadFactory.newThread(new CompleteOrderThread(orderID, twoPhase));
            
            thread.start();
        
        } else {
        
            try (JMSContext queueContext = queueConnectionFactory.createContext();) {
                TextMessage message = queueContext.createTextMessage();

                message.setStringProperty("command", "neworder");
                message.setIntProperty("orderID", orderID);
                message.setBooleanProperty("twoPhase", twoPhase);
                message.setText("neworder: orderID=" + orderID + " runtimeMode=EJB twoPhase=" + twoPhase);
                message.setLongProperty("publishTime", System.currentTimeMillis());
        		        		
                queueContext.createProducer().send(tradeBrokerQueue, message);
        		
            } catch (Exception e) {
                throw new EJBException(e.getMessage(), e); // pass the exception
            }
        }
    }

    @Override
    public OrderDataBean completeOrder(Integer orderID, boolean twoPhase) throws Exception {
        if (Log.doTrace()) {
            Log.trace("TradeSLSBBean:completeOrder", orderID + " twoPhase=" + twoPhase);
        }  
              
        OrderDataBean order = entityManager.find(OrderDataBean.class, orderID);
        
        if (order == null) {
            throw new EJBException("Error: attempt to complete Order that is null\n" + order);
        }
        
        order.getQuote();

        if (order.isCompleted()) {
            throw new EJBException("Error: attempt to complete Order that is already completed\n" + order);
        }

        AccountDataBean account = order.getAccount();
        QuoteDataBean quote = order.getQuote();
        HoldingDataBean holding = order.getHolding();
        BigDecimal price = order.getPrice();
        double quantity = order.getQuantity();

        String userID = account.getProfile().getUserID();

        if (Log.doTrace()) {
            Log.trace("TradeSLSBBeanInternal:completeOrder--> Completing Order " + order.getOrderID() + "\n\t Order info: " + order + "\n\t Account info: "
                    + account + "\n\t Quote info: " + quote + "\n\t Holding info: " + holding);
        }

        if (order.isBuy()) {
            /*
             * Complete a Buy operation - create a new Holding for the Account -
             * deduct the Order cost from the Account balance
             */

            HoldingDataBean newHolding = createHolding(account, quote, quantity, price);
            order.setHolding(newHolding);
        }

        if (order.isSell()) {
            /*
             * Complete a Sell operation - remove the Holding from the Account -
             * deposit the Order proceeds to the Account balance
             */
            if (holding == null) {
                //Log.error("TradeSLSBBean:completeOrder -- Unable to sell order " + order.getOrderID() + " holding already sold");
                order.cancel();
                throw new EJBException("TradeSLSBBean:completeOrder -- Unable to sell order " + order.getOrderID() + " holding already sold");
            } else {
                entityManager.remove(holding);
                order.setHolding(null);
            }
            
            
        }
        order.setOrderStatus("closed");

        order.setCompletionDate(new java.sql.Timestamp(System.currentTimeMillis()));

        if (Log.doTrace()) {
            Log.trace("TradeSLSBBean:completeOrder--> Completed Order " + order.getOrderID() + "\n\t Order info: " + order + "\n\t Account info: " + account
                    + "\n\t Quote info: " + quote + "\n\t Holding info: " + holding);
        }
        // if (Log.doTrace())
        // Log.trace("Calling TradeAction:orderCompleted from Session EJB using Session Object");
        // FUTURE All getEJBObjects could be local -- need to add local I/F

        TradeAction tradeAction = new TradeAction();
        tradeAction.orderCompleted(userID, orderID);

       
        
        return order;
    }

    @Override
    public void cancelOrder(Integer orderID, boolean twoPhase) {
        if (Log.doTrace()) {
            Log.trace("TradeSLSBBean:cancelOrder", orderID + " twoPhase=" + twoPhase);
        }

        OrderDataBean order = entityManager.find(OrderDataBean.class, orderID);
        order.cancel();
    }

    @Override
    public void orderCompleted(String userID, Integer orderID) {
        throw new UnsupportedOperationException("TradeSLSBBean:orderCompleted method not supported");
    }

    @Override
    public Collection<OrderDataBean> getOrders(String userID) {
        if (Log.doTrace()) {
            Log.trace("TradeSLSBBean:getOrders", userID);
        }

        AccountProfileDataBean profile = entityManager.find(AccountProfileDataBean.class, userID);
        AccountDataBean account = profile.getAccount();
        return account.getOrders();
    }

    @Override
    public Collection<OrderDataBean> getClosedOrders(String userID) {
        if (Log.doTrace()) {
            Log.trace("TradeSLSBBean:getClosedOrders", userID);
        }

        try {
            /* I want to do a CriteriaUpdate here, but there are issues with JBoss/Hibernate */
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<OrderDataBean> criteriaQuery = criteriaBuilder.createQuery(OrderDataBean.class);
            Root<OrderDataBean> orders = criteriaQuery.from(OrderDataBean.class);
            criteriaQuery.where(criteriaBuilder.equal(orders.get("orderStatus"), "closed"),criteriaBuilder.equal(orders.get("account").get("profile").get("userID"),userID));
            criteriaQuery.select(orders);
            TypedQuery<OrderDataBean> q = entityManager.createQuery(criteriaQuery);
            List<OrderDataBean> results = q.getResultList();
            
            Iterator<OrderDataBean> itr = results.iterator();
            
            // Spin through the orders to remove or mark completed
            while (itr.hasNext()) {
                OrderDataBean order = itr.next();
                // TODO: Investigate ConncurrentModification Exceptions                                
                if (TradeConfig.getLongRun()) {
                    //Added this for Longruns (to prevent orderejb growth)
                    entityManager.remove(order); 
                }
                else {
                    order.setOrderStatus("completed");
                }
            }

            return results;
            
        } catch (Exception e) {
            Log.error("TradeSLSBBean.getClosedOrders", e);
            throw new EJBException("TradeSLSBBean.getClosedOrders - error", e);
        }
    }

    @Override
    public QuoteDataBean createQuote(String symbol, String companyName, BigDecimal price) {
        try {
            QuoteDataBean quote = new QuoteDataBean(symbol, companyName, 0, price, price, price, price, 0);
            entityManager.persist(quote);
            if (Log.doTrace()) {
                Log.trace("TradeSLSBBean:createQuote-->" + quote);
            }
            return quote;
        } catch (Exception e) {
            Log.error("TradeSLSBBean:createQuote -- exception creating Quote", e);
            throw new EJBException(e);
        }
    }

    @Override
    public QuoteDataBean getQuote(String symbol) {
        if (Log.doTrace()) {
            Log.trace("TradeSLSBBean:getQuote", symbol);
        }

        return entityManager.find(QuoteDataBean.class, symbol);
    }

    @Override
    public Collection<QuoteDataBean> getAllQuotes() {
        if (Log.doTrace()) {
            Log.trace("TradeSLSBBean:getAllQuotes");
        }

        TypedQuery<QuoteDataBean> query = entityManager.createNamedQuery("quoteejb.allQuotes",QuoteDataBean.class);
        return query.getResultList();
    }

    @Override
    public QuoteDataBean updateQuotePriceVolume(String symbol, BigDecimal changeFactor, double sharesTraded) {
        if (!TradeConfig.getUpdateQuotePrices()) {
            return new QuoteDataBean();
        }

        if (Log.doTrace()) {
            Log.trace("TradeSLSBBean:updateQuote", symbol, changeFactor);
        }

        TypedQuery<QuoteDataBean> q = entityManager.createNamedQuery("quoteejb.quoteForUpdate",QuoteDataBean.class);
        q.setParameter(1, symbol);
        QuoteDataBean quote = q.getSingleResult();

        BigDecimal oldPrice = quote.getPrice();
        BigDecimal openPrice = quote.getOpen();

        if (oldPrice.equals(TradeConfig.PENNY_STOCK_PRICE)) {
            changeFactor = TradeConfig.PENNY_STOCK_RECOVERY_MIRACLE_MULTIPLIER;
        } else if (oldPrice.compareTo(TradeConfig.MAXIMUM_STOCK_PRICE) > 0) {
            changeFactor = TradeConfig.MAXIMUM_STOCK_SPLIT_MULTIPLIER;
        }

        BigDecimal newPrice = changeFactor.multiply(oldPrice).setScale(2, BigDecimal.ROUND_HALF_UP);

        quote.setPrice(newPrice);
        quote.setChange(newPrice.subtract(openPrice).doubleValue());
        quote.setVolume(quote.getVolume() + sharesTraded);
        entityManager.merge(quote);

        context.getBusinessObject(TradeSLSBLocal.class).publishQuotePriceChange(quote, oldPrice, changeFactor, sharesTraded);
       
        return quote;
    }

    @Override
    public Collection<HoldingDataBean> getHoldings(String userID) {
        if (Log.doTrace()) {
            Log.trace("TradeSLSBBean:getHoldings", userID);
        }

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<HoldingDataBean> criteriaQuery = criteriaBuilder.createQuery(HoldingDataBean.class);
        Root<HoldingDataBean> holdings = criteriaQuery.from(HoldingDataBean.class);
        criteriaQuery.where(criteriaBuilder.equal(holdings.get("account").get("profile").get("userID"), userID));
        criteriaQuery.select(holdings);
        TypedQuery<HoldingDataBean> typedQuery = entityManager.createQuery(criteriaQuery);
               
        return typedQuery.getResultList();
    }

    @Override
    public HoldingDataBean getHolding(Integer holdingID) {
        if (Log.doTrace()) {
            Log.trace("TradeSLSBBean:getHolding", holdingID);
        }
        return entityManager.find(HoldingDataBean.class, holdingID);
    }

    @Override
    public AccountDataBean getAccountData(String userID) {
        if (Log.doTrace()) {
            Log.trace("TradeSLSBBean:getAccountData", userID);
        }

        AccountProfileDataBean profile = entityManager.find(AccountProfileDataBean.class, userID);
        AccountDataBean account = profile.getAccount();

        // Added to populate transient field for account
        account.setProfileID(profile.getUserID());
        
        return account;
    }

    @Override
    public AccountProfileDataBean getAccountProfileData(String userID) {
        if (Log.doTrace()) {
            Log.trace("TradeSLSBBean:getProfileData", userID);
        }

        return entityManager.find(AccountProfileDataBean.class, userID);
    }

    @Override
    public AccountProfileDataBean updateAccountProfile(AccountProfileDataBean profileData) {
        if (Log.doTrace()) {
            Log.trace("TradeSLSBBean:updateAccountProfileData", profileData);
        }
             
        AccountProfileDataBean temp = entityManager.find(AccountProfileDataBean.class, profileData.getUserID());
        temp.setAddress(profileData.getAddress());
        temp.setPassword(profileData.getPassword());
        temp.setFullName(profileData.getFullName());
        temp.setCreditCard(profileData.getCreditCard());
        temp.setEmail(profileData.getEmail());

        entityManager.merge(temp);

        return temp;
    }

    @Override
    public AccountDataBean login(String userID, String password) throws RollbackException {
        AccountProfileDataBean profile = entityManager.find(AccountProfileDataBean.class, userID);

        if (profile == null) {
            throw new EJBException("No such user: " + userID);
        }
        entityManager.merge(profile);
        AccountDataBean account = profile.getAccount();

        if (Log.doTrace()) {
            Log.trace("TradeSLSBBean:login", userID, password);
        }
        account.login(password);
        if (Log.doTrace()) {
            Log.trace("TradeSLSBBean:login(" + userID + "," + password + ") success" + account);
        }
        
        return account;
    }

    @Override
    public void logout(String userID) {
        if (Log.doTrace()) {
            Log.trace("TradeSLSBBean:logout", userID);
        }

        AccountProfileDataBean profile = entityManager.find(AccountProfileDataBean.class, userID);
        AccountDataBean account = profile.getAccount();

        account.logout();

        if (Log.doTrace()) {
            Log.trace("TradeSLSBBean:logout(" + userID + ") success");
        }
        
    }

    @Override
    public AccountDataBean register(String userID, String password, String fullname, String address, String email, String creditcard, BigDecimal openBalance) {
        AccountDataBean account = null;
        AccountProfileDataBean profile = null;

        if (Log.doTrace()) {
            Log.trace("TradeSLSBBean:register", userID, password, fullname, address, email, creditcard, openBalance);
        }

        // Check to see if a profile with the desired userID already exists
        profile = entityManager.find(AccountProfileDataBean.class, userID);

        if (profile != null) {
            Log.error("Failed to register new Account - AccountProfile with userID(" + userID + ") already exists");
            return null;
        } else {
            profile = new AccountProfileDataBean(userID, password, fullname, address, email, creditcard);
            account = new AccountDataBean(0, 0, null, new Timestamp(System.currentTimeMillis()), openBalance, openBalance, userID);

            profile.setAccount(account);
            account.setProfile(profile);

            entityManager.persist(profile);
            entityManager.persist(account);
        }

        return account;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public RunStatsDataBean resetTrade(boolean deleteAll) throws Exception {
        if (Log.doTrace()) {
            Log.trace("TradeSLSBBean:resetTrade", deleteAll);
        }

        return new com.ibm.websphere.samples.daytrader.direct.TradeDirect(false).resetTrade(deleteAll);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void publishQuotePriceChange(QuoteDataBean quote, BigDecimal oldPrice, BigDecimal changeFactor, double sharesTraded) {
        if (!TradeConfig.getPublishQuotePriceChange()) {
            return;
        }
        if (Log.doTrace()) {
            Log.trace("TradeSLSBBean:publishQuotePricePublishing -- quoteData = " + quote);
        }

        try (JMSContext topicContext = topicConnectionFactory.createContext();) {
    		TextMessage message = topicContext.createTextMessage();

    		message.setStringProperty("command", "updateQuote");
            message.setStringProperty("symbol", quote.getSymbol());
            message.setStringProperty("company", quote.getCompanyName());
            message.setStringProperty("price", quote.getPrice().toString());
            message.setStringProperty("oldPrice", oldPrice.toString());
            message.setStringProperty("open", quote.getOpen().toString());
            message.setStringProperty("low", quote.getLow().toString());
            message.setStringProperty("high", quote.getHigh().toString());
            message.setDoubleProperty("volume", quote.getVolume());
            message.setStringProperty("changeFactor", changeFactor.toString());
            message.setDoubleProperty("sharesTraded", sharesTraded);
            message.setLongProperty("publishTime", System.currentTimeMillis());
            message.setText("Update Stock price for " + quote.getSymbol() + " old price = " + oldPrice + " new price = " + quote.getPrice());
    		        		
    		topicContext.createProducer().send(tradeStreamerTopic, message);
    	} catch (Exception e) {
    		 throw new EJBException(e.getMessage(), e); // pass the exception
    	}
        
        
        
    }

    private OrderDataBean createOrder(AccountDataBean account, QuoteDataBean quote, HoldingDataBean holding, String orderType, double quantity) {

        OrderDataBean order;

        if (Log.doTrace()) {
            Log.trace("TradeSLSBBean:createOrder(orderID=" + " account=" + ((account == null) ? null : account.getAccountID()) + " quote="
                    + ((quote == null) ? null : quote.getSymbol()) + " orderType=" + orderType + " quantity=" + quantity);
        }
        try {
            order = new OrderDataBean(orderType, "open", new Timestamp(System.currentTimeMillis()), null, quantity, quote.getPrice().setScale(
                    FinancialUtils.SCALE, FinancialUtils.ROUND), TradeConfig.getOrderFee(orderType), account, quote, holding);
            entityManager.persist(order);
        } catch (Exception e) {
            Log.error("TradeSLSBBean:createOrder -- failed to create Order. The stock/quote may not exist in the database.", e);
            throw new EJBException("TradeSLSBBean:createOrder -- failed to create Order. Check that the symbol exists in the database.", e);
        }
        return order;
    }

    private HoldingDataBean createHolding(AccountDataBean account, QuoteDataBean quote, double quantity, BigDecimal purchasePrice) throws Exception {
        HoldingDataBean newHolding = new HoldingDataBean(quantity, purchasePrice, new Timestamp(System.currentTimeMillis()), account, quote);
        entityManager.persist(newHolding);
        return newHolding;
    }

    public double investmentReturn(double investment, double NetValue) throws Exception {
        if (Log.doTrace()) {
            Log.trace("TradeSLSBBean:investmentReturn");
        }

        double diff = NetValue - investment;
        double ir = diff / investment;
        return ir;
    }

    public QuoteDataBean pingTwoPhase(String symbol) throws Exception {
      
    	if (Log.doTrace()) {
    		Log.trace("TradeSLSBBean:pingTwoPhase", symbol);
    	}
                     
    	QuoteDataBean quoteData = null;
            
    	try (JMSContext queueContext = queueConnectionFactory.createContext();) {
    		// Get a Quote and send a JMS message in a 2-phase commit
    		quoteData = entityManager.find(QuoteDataBean.class, symbol);
                		    		
    		TextMessage message = queueContext.createTextMessage();

    		message.setStringProperty("command", "ping");
    		message.setLongProperty("publishTime", System.currentTimeMillis());
    		message.setText("Ping message for queue java:comp/env/jms/TradeBrokerQueue sent from TradeSLSBBean:pingTwoPhase at " + new java.util.Date());
    		queueContext.createProducer().send(tradeBrokerQueue, message);
    	} catch (Exception e) {
    		Log.error("TradeSLSBBean:pingTwoPhase -- exception caught", e);
    	}
            	
    	return quoteData;
    } 
    
    class quotePriceComparator implements Comparator<Object> {

        @Override
        public int compare(Object quote1, Object quote2) {
            double change1 = ((QuoteDataBean) quote1).getChange();
            double change2 = ((QuoteDataBean) quote2).getChange();
            return new Double(change2).compareTo(change1);
        }
    }

    @PostConstruct
    public void postConstruct() {
               
        if (Log.doTrace()) {
            Log.trace("updateQuotePrices: " + TradeConfig.getUpdateQuotePrices());
            Log.trace("publishQuotePriceChange: " + TradeConfig.getPublishQuotePriceChange());
        }
    }
}
