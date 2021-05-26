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

import com.ibm.cardinal.util.*;

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




public class TradeSLSBBean implements TradeSLSBRemote, TradeSLSBLocal {
	
    
    private QueueConnectionFactory queueConnectionFactory;

    
    private TopicConnectionFactory topicConnectionFactory;

    
    private Topic tradeStreamerTopic;

    
    private Queue tradeBrokerQueue;
    
     
    private ManagedThreadFactory managedThreadFactory;
	
    /* JBoss 
    
    private QueueConnectionFactory queueConnectionFactory;

    
    private TopicConnectionFactory topicConnectionFactory;

    
    private Topic tradeStreamerTopic;
        
    
    private Queue tradeBrokerQueue;
    */
    
    
    private EntityManager entityManager;

    
    private SessionContext context;
    
    
    MarketSummarySingleton marketSummarySingleton;

    /** Creates a new instance of TradeSLSBBean */
    public TradeSLSBBean() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-ejb/src/main/java/com/ibm/websphere/samples/daytrader/ejb3/TradeSLSBBean.java:TradeSLSBBean:TradeSLSBBean");
    }

    
    public MarketSummaryDataBean getMarketSummary() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-ejb/src/main/java/com/ibm/websphere/samples/daytrader/ejb3/TradeSLSBBean.java:TradeSLSBBean:getMarketSummary");
    }

    
    public OrderDataBean buy(String userID, String symbol, double quantity, int orderProcessingMode) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-ejb/src/main/java/com/ibm/websphere/samples/daytrader/ejb3/TradeSLSBBean.java:TradeSLSBBean:buy");
    }

    
    public OrderDataBean sell(final String userID, final Integer holdingID, int orderProcessingMode) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-ejb/src/main/java/com/ibm/websphere/samples/daytrader/ejb3/TradeSLSBBean.java:TradeSLSBBean:sell");
    }

    
    public void queueOrder(Integer orderID, boolean twoPhase) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-ejb/src/main/java/com/ibm/websphere/samples/daytrader/ejb3/TradeSLSBBean.java:TradeSLSBBean:queueOrder");
    }

    
    public OrderDataBean completeOrder(Integer orderID, boolean twoPhase) throws Exception {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-ejb/src/main/java/com/ibm/websphere/samples/daytrader/ejb3/TradeSLSBBean.java:TradeSLSBBean:completeOrder");
    }

    
    public void cancelOrder(Integer orderID, boolean twoPhase) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-ejb/src/main/java/com/ibm/websphere/samples/daytrader/ejb3/TradeSLSBBean.java:TradeSLSBBean:cancelOrder");
    }

    
    public void orderCompleted(String userID, Integer orderID) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-ejb/src/main/java/com/ibm/websphere/samples/daytrader/ejb3/TradeSLSBBean.java:TradeSLSBBean:orderCompleted");
    }

    
    public Collection<OrderDataBean> getOrders(String userID) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-ejb/src/main/java/com/ibm/websphere/samples/daytrader/ejb3/TradeSLSBBean.java:TradeSLSBBean:getOrders");
    }

    
    public Collection<OrderDataBean> getClosedOrders(String userID) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-ejb/src/main/java/com/ibm/websphere/samples/daytrader/ejb3/TradeSLSBBean.java:TradeSLSBBean:getClosedOrders");
    }

    
    public QuoteDataBean createQuote(String symbol, String companyName, BigDecimal price) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-ejb/src/main/java/com/ibm/websphere/samples/daytrader/ejb3/TradeSLSBBean.java:TradeSLSBBean:createQuote");
    }

    
    public QuoteDataBean getQuote(String symbol) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-ejb/src/main/java/com/ibm/websphere/samples/daytrader/ejb3/TradeSLSBBean.java:TradeSLSBBean:getQuote");
    }

    
    public Collection<QuoteDataBean> getAllQuotes() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-ejb/src/main/java/com/ibm/websphere/samples/daytrader/ejb3/TradeSLSBBean.java:TradeSLSBBean:getAllQuotes");
    }

    
    public QuoteDataBean updateQuotePriceVolume(String symbol, BigDecimal changeFactor, double sharesTraded) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-ejb/src/main/java/com/ibm/websphere/samples/daytrader/ejb3/TradeSLSBBean.java:TradeSLSBBean:updateQuotePriceVolume");
    }

    
    public Collection<HoldingDataBean> getHoldings(String userID) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-ejb/src/main/java/com/ibm/websphere/samples/daytrader/ejb3/TradeSLSBBean.java:TradeSLSBBean:getHoldings");
    }

    
    public HoldingDataBean getHolding(Integer holdingID) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-ejb/src/main/java/com/ibm/websphere/samples/daytrader/ejb3/TradeSLSBBean.java:TradeSLSBBean:getHolding");
    }

    
    public AccountDataBean getAccountData(String userID) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-ejb/src/main/java/com/ibm/websphere/samples/daytrader/ejb3/TradeSLSBBean.java:TradeSLSBBean:getAccountData");
    }

    
    public AccountProfileDataBean getAccountProfileData(String userID) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-ejb/src/main/java/com/ibm/websphere/samples/daytrader/ejb3/TradeSLSBBean.java:TradeSLSBBean:getAccountProfileData");
    }

    
    public AccountProfileDataBean updateAccountProfile(AccountProfileDataBean profileData) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-ejb/src/main/java/com/ibm/websphere/samples/daytrader/ejb3/TradeSLSBBean.java:TradeSLSBBean:updateAccountProfile");
    }

    
    public AccountDataBean login(String userID, String password) throws RollbackException {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-ejb/src/main/java/com/ibm/websphere/samples/daytrader/ejb3/TradeSLSBBean.java:TradeSLSBBean:login");
    }

    
    public void logout(String userID) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-ejb/src/main/java/com/ibm/websphere/samples/daytrader/ejb3/TradeSLSBBean.java:TradeSLSBBean:logout");
    }

    
    public AccountDataBean register(String userID, String password, String fullname, String address, String email, String creditcard, BigDecimal openBalance) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-ejb/src/main/java/com/ibm/websphere/samples/daytrader/ejb3/TradeSLSBBean.java:TradeSLSBBean:register");
    }

    
    
    public RunStatsDataBean resetTrade(boolean deleteAll) throws Exception {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-ejb/src/main/java/com/ibm/websphere/samples/daytrader/ejb3/TradeSLSBBean.java:TradeSLSBBean:resetTrade");
    }

    
    public void publishQuotePriceChange(QuoteDataBean quote, BigDecimal oldPrice, BigDecimal changeFactor, double sharesTraded) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-ejb/src/main/java/com/ibm/websphere/samples/daytrader/ejb3/TradeSLSBBean.java:TradeSLSBBean:publishQuotePriceChange");
    }

    private OrderDataBean createOrder(AccountDataBean account, QuoteDataBean quote, HoldingDataBean holding, String orderType, double quantity) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-ejb/src/main/java/com/ibm/websphere/samples/daytrader/ejb3/TradeSLSBBean.java:TradeSLSBBean:createOrder");
    }

    private HoldingDataBean createHolding(AccountDataBean account, QuoteDataBean quote, double quantity, BigDecimal purchasePrice) throws Exception {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-ejb/src/main/java/com/ibm/websphere/samples/daytrader/ejb3/TradeSLSBBean.java:TradeSLSBBean:createHolding");
    }

    public double investmentReturn(double investment, double NetValue) throws Exception {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-ejb/src/main/java/com/ibm/websphere/samples/daytrader/ejb3/TradeSLSBBean.java:TradeSLSBBean:investmentReturn");
    }

    public QuoteDataBean pingTwoPhase(String symbol) throws Exception {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-ejb/src/main/java/com/ibm/websphere/samples/daytrader/ejb3/TradeSLSBBean.java:TradeSLSBBean:pingTwoPhase");
    } 
    
    class quotePriceComparator implements Comparator<Object> {

        
        public int compare(Object quote1, Object quote2) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-ejb/src/main/java/com/ibm/websphere/samples/daytrader/ejb3/TradeSLSBBean.java:TradeSLSBBean::quotePriceComparator:compare");
    }
    }

    
    public void postConstruct() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-ejb/src/main/java/com/ibm/websphere/samples/daytrader/ejb3/TradeSLSBBean.java:TradeSLSBBean:postConstruct");
    }
}
