/**
 * This is a Cardinal generated proxy
 */

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
package com.ibm.websphere.samples.daytrader;

import java.io.StringReader;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

import com.ibm.cardinal.util.CardinalException;
import com.ibm.cardinal.util.CardinalLogger;
import com.ibm.cardinal.util.CardinalString;
import com.ibm.cardinal.util.ClusterObjectManager;
import com.ibm.cardinal.util.KluInterface;
import com.ibm.cardinal.util.SerializationUtil;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Collection;
//import java.util.Properties;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

import javax.naming.InitialContext;

import com.ibm.websphere.samples.daytrader.beans.MarketSummaryDataBean;
import com.ibm.websphere.samples.daytrader.beans.RunStatsDataBean;
import com.ibm.websphere.samples.daytrader.direct.TradeDirect;
import com.ibm.websphere.samples.daytrader.ejb3.TradeSLSBLocal;
import com.ibm.websphere.samples.daytrader.ejb3.TradeSLSBRemote;
import com.ibm.websphere.samples.daytrader.entities.AccountDataBean;
import com.ibm.websphere.samples.daytrader.entities.AccountProfileDataBean;
import com.ibm.websphere.samples.daytrader.entities.HoldingDataBean;
import com.ibm.websphere.samples.daytrader.entities.OrderDataBean;
import com.ibm.websphere.samples.daytrader.entities.QuoteDataBean;
import com.ibm.websphere.samples.daytrader.util.FinancialUtils;
import com.ibm.websphere.samples.daytrader.util.Log;
import com.ibm.websphere.samples.daytrader.util.TradeConfig;

/**
 * The TradeAction class provides the generic client side access to each of the
 * Trade brokerage user operations. These include login, logout, buy, sell,
 * getQuote, etc. The TradeAction class does not handle user interface
 * processing and should be used by a class that is UI specific. For example,
 * {trade_client.TradeServletAction}manages a web interface to Trade, making
 * calls to TradeAction methods to actually performance each operation.
 */
public class TradeAction implements KluInterface, TradeServices {
    private String klu__referenceID = "";
    private static String serviceURI;
    private static Client client;
    private static final Logger logger = CardinalLogger.getLogger(TradeAction.class);

    static {
        client = ClientBuilder.newClient();

        logger.info("Static initializer of TradeAction of cluster partition_1");

        serviceURI = System.getenv().get("APPLICATION_PARTITION_1_REST_URL");
        if (serviceURI == null) {
            throw new RuntimeException("Environment variable "+
                "APPLICATION_PARTITION_1_REST_URL not set\n"+
                "Please set APPLICATION_PARTITION_1_REST_URL to "+
                "partition_1 host:port"
            );
        }

        if (!serviceURI.endsWith("/")) {
            serviceURI += "/";
        }
        serviceURI += "TradeActionService";

        try {
            java.net.URI uri = java.net.URI.create(serviceURI);
        }
        catch (Exception e) {
            throw new RuntimeException("Invalid URI for partition partition_1, "+
                "service TradeActionService: "+serviceURI, e);
        }

        logger.info("partition_1 TradeActionService URI = " + serviceURI);
    }




    // constructor for creating proxy instances of this class using reference ID
    public TradeAction(CardinalString referenceId) {
        setKlu__referenceID(referenceId.getString());
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        client.target(serviceURI)
            .path("incObjectCount")
            .request()
            .post(Entity.form(form));
	}



    public String getKlu__referenceID() {
        return this.klu__referenceID;
    }

    public void setKlu__referenceID(String referenceId) {
        this.klu__referenceID = referenceId;
    }

    // decrement object reference count when this object is garbage collected
    public void finalize() {
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        client.target(serviceURI)
            .path("decObjectCount")
            .request()
            .post(Entity.form(form));
    }






    public TradeAction() {
        // create form for service request
        Form form = new Form();
        logger.info("[TradeAction] Calling service "+serviceURI+
            "/TradeAction_001 with form: "+form.asMap());

        // call constructor service and store ref ID
        Response svc_response;
        try {
            svc_response = 
                client.target(serviceURI) 
                .path("TradeAction_001") 
                .request(MediaType.APPLICATION_JSON) 
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeAction] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeAction] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        setKlu__referenceID(response_json.getString("return_value"));
        svc_response.close();
    }

    public TradeAction(TradeServices trade) {
        // create form for service request
        Form form = new Form();
        
        // convert physical/proxy object(s) referenced by "trade" to reference ID(s)
        String trade_fpar = SerializationUtil.encodeWithDynamicTypeCheck(trade);
        form.param("trade", trade_fpar);

        logger.info("[TradeAction] Calling service "+serviceURI+
            "/TradeAction_002 with form: "+form.asMap());

        // call constructor service and store ref ID
        Response svc_response;
        try {
            svc_response = 
                client.target(serviceURI) 
                .path("TradeAction_002") 
                .request(MediaType.APPLICATION_JSON) 
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeAction] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeAction] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        setKlu__referenceID(response_json.getString("return_value"));
        svc_response.close();
    }

    /**
     * Market Summary is inherently a heavy database operation. For servers that
     * have a caching story this is a great place to cache data that is good for
     * a period of time. In order to provide a flexible framework for this we
     * allow the market summary operation to be invoked on every transaction,
     * time delayed or never. This is configurable in the configuration panel.
     *
     * @return An instance of the market summary
     */
    @Override
    public MarketSummaryDataBean getMarketSummary() throws Exception {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        logger.info("[TradeAction] Calling service "+serviceURI+
            "/getMarketSummary with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("getMarketSummary")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeAction] Exception thrown in service call: "+wae.getMessage());
            if (wae.getResponse().getStatus() == CardinalException.APPLICATION_EXCEPTION) {
                logger.warning("[TradeAction] Re-throwing wrapped application exception: ");
                // typecast to declared exception types
                if (cause instanceof Exception) {
                    throw (Exception)cause;
                }
            }
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeAction] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        
        // convert reference ID(s) stored in "response" to physical/proxy object(s)
        MarketSummaryDataBean response_obj = (MarketSummaryDataBean)SerializationUtil.decodeWithDynamicTypeCheck(response);
        return response_obj;

    }

    /**
     * Compute and return a snapshot of the current market conditions This
     * includes the TSIA - an index of the price of the top 100 Trade stock
     * quotes The openTSIA ( the index at the open) The volume of shares traded,
     * Top Stocks gain and loss
     *
     * @return A snapshot of the current market summary
     */
    public MarketSummaryDataBean getMarketSummaryInternal() throws Exception {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        logger.info("[TradeAction] Calling service "+serviceURI+
            "/getMarketSummaryInternal with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("getMarketSummaryInternal")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeAction] Exception thrown in service call: "+wae.getMessage());
            if (wae.getResponse().getStatus() == CardinalException.APPLICATION_EXCEPTION) {
                logger.warning("[TradeAction] Re-throwing wrapped application exception: ");
                // typecast to declared exception types
                if (cause instanceof Exception) {
                    throw (Exception)cause;
                }
            }
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeAction] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        
        // convert reference ID(s) stored in "response" to physical/proxy object(s)
        MarketSummaryDataBean response_obj = (MarketSummaryDataBean)SerializationUtil.decodeWithDynamicTypeCheck(response);
        return response_obj;

    }

    /**
     * Purchase a stock and create a new holding for the given user. Given a
     * stock symbol and quantity to purchase, retrieve the current quote price,
     * debit the user's account balance, and add holdings to user's portfolio.
     *
     * @param userID
     *            the customer requesting the stock purchase
     * @param symbol
     *            the symbol of the stock being purchased
     * @param quantity
     *            the quantity of shares to purchase
     * @return OrderDataBean providing the status of the newly created buy order
     */
    @Override
    public OrderDataBean buy(String userID, String symbol, double quantity, int orderProcessingMode) throws Exception {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        form.param("userID", userID); 

        form.param("symbol", symbol); 

        form.param("quantity", String.valueOf(quantity)); 

        form.param("orderProcessingMode", String.valueOf(orderProcessingMode)); 

        logger.info("[TradeAction] Calling service "+serviceURI+
            "/buy with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("buy")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeAction] Exception thrown in service call: "+wae.getMessage());
            if (wae.getResponse().getStatus() == CardinalException.APPLICATION_EXCEPTION) {
                logger.warning("[TradeAction] Re-throwing wrapped application exception: ");
                // typecast to declared exception types
                if (cause instanceof Exception) {
                    throw (Exception)cause;
                }
            }
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeAction] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        
        // convert reference ID(s) stored in "response" to physical/proxy object(s)
        OrderDataBean response_obj = (OrderDataBean)SerializationUtil.decodeWithDynamicTypeCheck(response);
        return response_obj;

    }

    /**
     * Sell(SOAP 2.2 Wrapper converting int to Integer) a stock holding and
     * removed the holding for the given user. Given a Holding, retrieve current
     * quote, credit user's account, and reduce holdings in user's portfolio.
     *
     * @param userID
     *            the customer requesting the sell
     * @param holdingID
     *            the users holding to be sold
     * @return OrderDataBean providing the status of the newly created sell
     *         order
     */
    public OrderDataBean sell(String userID, int holdingID, int orderProcessingMode) throws Exception {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        form.param("userID", userID); 

        form.param("holdingID", String.valueOf(holdingID)); 

        form.param("orderProcessingMode", String.valueOf(orderProcessingMode)); 

        logger.info("[TradeAction] Calling service "+serviceURI+
            "/sell_003 with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("sell_003")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeAction] Exception thrown in service call: "+wae.getMessage());
            if (wae.getResponse().getStatus() == CardinalException.APPLICATION_EXCEPTION) {
                logger.warning("[TradeAction] Re-throwing wrapped application exception: ");
                // typecast to declared exception types
                if (cause instanceof Exception) {
                    throw (Exception)cause;
                }
            }
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeAction] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        
        // convert reference ID(s) stored in "response" to physical/proxy object(s)
        OrderDataBean response_obj = (OrderDataBean)SerializationUtil.decodeWithDynamicTypeCheck(response);
        return response_obj;

    }

    /**
     * Sell a stock holding and removed the holding for the given user. Given a
     * Holding, retrieve current quote, credit user's account, and reduce
     * holdings in user's portfolio.
     *
     * @param userID
     *            the customer requesting the sell
     * @param holdingID
     *            the users holding to be sold
     * @return OrderDataBean providing the status of the newly created sell
     *         order
     */
    @Override
    public OrderDataBean sell(String userID, Integer holdingID, int orderProcessingMode) throws Exception {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        form.param("userID", userID); 

        
        // convert physical/proxy object(s) referenced by "holdingID" to reference ID(s)
        String holdingID_fpar = SerializationUtil.encodeWithDynamicTypeCheck(holdingID);
        form.param("holdingID", holdingID_fpar);

        form.param("orderProcessingMode", String.valueOf(orderProcessingMode)); 

        logger.info("[TradeAction] Calling service "+serviceURI+
            "/sell_004 with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("sell_004")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeAction] Exception thrown in service call: "+wae.getMessage());
            if (wae.getResponse().getStatus() == CardinalException.APPLICATION_EXCEPTION) {
                logger.warning("[TradeAction] Re-throwing wrapped application exception: ");
                // typecast to declared exception types
                if (cause instanceof Exception) {
                    throw (Exception)cause;
                }
            }
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeAction] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        
        // convert reference ID(s) stored in "response" to physical/proxy object(s)
        OrderDataBean response_obj = (OrderDataBean)SerializationUtil.decodeWithDynamicTypeCheck(response);
        return response_obj;

    }

    /**
     * Queue the Order identified by orderID to be processed
     * <p/>
     * Orders are submitted through JMS to a Trading Broker and completed
     * asynchronously. This method queues the order for processing
     * <p/>
     * The boolean twoPhase specifies to the server implementation whether or
     * not the method is to participate in a global transaction
     *
     * @param orderID
     *            the Order being queued for processing
     */
    @Override
    public void queueOrder(Integer orderID, boolean twoPhase) {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        
        // convert physical/proxy object(s) referenced by "orderID" to reference ID(s)
        String orderID_fpar = SerializationUtil.encodeWithDynamicTypeCheck(orderID);
        form.param("orderID", orderID_fpar);

        form.param("twoPhase", String.valueOf(twoPhase)); 

        logger.info("[TradeAction] Calling service "+serviceURI+
            "/queueOrder with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("queueOrder")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeAction] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }

    }

    /**
     * Complete the Order identefied by orderID Orders are submitted through JMS
     * to a Trading agent and completed asynchronously. This method completes
     * the order For a buy, the stock is purchased creating a holding and the
     * users account is debited For a sell, the stock holding is removed and the
     * users account is credited with the proceeds
     * <p/>
     * The boolean twoPhase specifies to the server implementation whether or
     * not the method is to participate in a global transaction
     *
     * @param orderID
     *            the Order to complete
     * @return OrderDataBean providing the status of the completed order
     */
    @Override
    public OrderDataBean completeOrder(Integer orderID, boolean twoPhase) {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        
        // convert physical/proxy object(s) referenced by "orderID" to reference ID(s)
        String orderID_fpar = SerializationUtil.encodeWithDynamicTypeCheck(orderID);
        form.param("orderID", orderID_fpar);

        form.param("twoPhase", String.valueOf(twoPhase)); 

        logger.info("[TradeAction] Calling service "+serviceURI+
            "/completeOrder with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("completeOrder")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeAction] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeAction] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        
        // convert reference ID(s) stored in "response" to physical/proxy object(s)
        OrderDataBean response_obj = (OrderDataBean)SerializationUtil.decodeWithDynamicTypeCheck(response);
        return response_obj;

    }

    /**
     * Cancel the Order identified by orderID
     * <p/>
     * Orders are submitted through JMS to a Trading Broker and completed
     * asynchronously. This method queues the order for processing
     * <p/>
     * The boolean twoPhase specifies to the server implementation whether or
     * not the method is to participate in a global transaction
     *
     * @param orderID
     *            the Order being queued for processing
     */
    @Override
    public void cancelOrder(Integer orderID, boolean twoPhase) {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        
        // convert physical/proxy object(s) referenced by "orderID" to reference ID(s)
        String orderID_fpar = SerializationUtil.encodeWithDynamicTypeCheck(orderID);
        form.param("orderID", orderID_fpar);

        form.param("twoPhase", String.valueOf(twoPhase)); 

        logger.info("[TradeAction] Calling service "+serviceURI+
            "/cancelOrder with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("cancelOrder")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeAction] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }

    }

    @Override
    public void orderCompleted(String userID, Integer orderID) throws Exception {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        form.param("userID", userID); 

        
        // convert physical/proxy object(s) referenced by "orderID" to reference ID(s)
        String orderID_fpar = SerializationUtil.encodeWithDynamicTypeCheck(orderID);
        form.param("orderID", orderID_fpar);

        logger.info("[TradeAction] Calling service "+serviceURI+
            "/orderCompleted with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("orderCompleted")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeAction] Exception thrown in service call: "+wae.getMessage());
            if (wae.getResponse().getStatus() == CardinalException.APPLICATION_EXCEPTION) {
                logger.warning("[TradeAction] Re-throwing wrapped application exception: ");
                // typecast to declared exception types
                if (cause instanceof Exception) {
                    throw (Exception)cause;
                }
            }
            throw (RuntimeException)cause;
        }

    }

    /**
     * Get the collection of all orders for a given account
     *
     * @param userID
     *            the customer account to retrieve orders for
     * @return Collection OrderDataBeans providing detailed order information
     */
    @Override
    public Collection<?> getOrders(String userID) throws Exception {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        form.param("userID", userID); 

        logger.info("[TradeAction] Calling service "+serviceURI+
            "/getOrders with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("getOrders")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeAction] Exception thrown in service call: "+wae.getMessage());
            if (wae.getResponse().getStatus() == CardinalException.APPLICATION_EXCEPTION) {
                logger.warning("[TradeAction] Re-throwing wrapped application exception: ");
                // typecast to declared exception types
                if (cause instanceof Exception) {
                    throw (Exception)cause;
                }
            }
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeAction] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        
        // convert reference ID(s) stored in "response" to physical/proxy object(s)
        Collection<?> response_obj = (Collection<?>)SerializationUtil.decodeWithDynamicTypeCheck(response);
        return response_obj;

    }

    /**
     * Get the collection of completed orders for a given account that need to
     * be alerted to the user
     *
     * @param userID
     *            the customer account to retrieve orders for
     * @return Collection OrderDataBeans providing detailed order information
     */
    @Override
    public Collection<?> getClosedOrders(String userID) throws Exception {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        form.param("userID", userID); 

        logger.info("[TradeAction] Calling service "+serviceURI+
            "/getClosedOrders with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("getClosedOrders")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeAction] Exception thrown in service call: "+wae.getMessage());
            if (wae.getResponse().getStatus() == CardinalException.APPLICATION_EXCEPTION) {
                logger.warning("[TradeAction] Re-throwing wrapped application exception: ");
                // typecast to declared exception types
                if (cause instanceof Exception) {
                    throw (Exception)cause;
                }
            }
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeAction] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        
        // convert reference ID(s) stored in "response" to physical/proxy object(s)
        Collection<?> response_obj = (Collection<?>)SerializationUtil.decodeWithDynamicTypeCheck(response);
        return response_obj;

    }

    /**
     * Given a market symbol, price, and details, create and return a new
     * {@link QuoteDataBean}
     *
     * @param symbol
     *            the symbol of the stock
     * @param price
     *            the current stock price
     * @return a new QuoteDataBean or null if Quote could not be created
     */
    @Override
    public QuoteDataBean createQuote(String symbol, String companyName, BigDecimal price) throws Exception {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        form.param("symbol", symbol); 

        form.param("companyName", companyName); 

        
        // convert physical/proxy object(s) referenced by "price" to reference ID(s)
        String price_fpar = SerializationUtil.encodeWithDynamicTypeCheck(price);
        form.param("price", price_fpar);

        logger.info("[TradeAction] Calling service "+serviceURI+
            "/createQuote with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("createQuote")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeAction] Exception thrown in service call: "+wae.getMessage());
            if (wae.getResponse().getStatus() == CardinalException.APPLICATION_EXCEPTION) {
                logger.warning("[TradeAction] Re-throwing wrapped application exception: ");
                // typecast to declared exception types
                if (cause instanceof Exception) {
                    throw (Exception)cause;
                }
            }
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeAction] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        
        // convert reference ID(s) stored in "response" to physical/proxy object(s)
        QuoteDataBean response_obj = (QuoteDataBean)SerializationUtil.decodeWithDynamicTypeCheck(response);
        return response_obj;

    }

    /**
     * Return a collection of {@link QuoteDataBean}describing all current quotes
     *
     * @return the collection of QuoteDataBean
     */
    @Override
    public Collection<?> getAllQuotes() throws Exception {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        logger.info("[TradeAction] Calling service "+serviceURI+
            "/getAllQuotes with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("getAllQuotes")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeAction] Exception thrown in service call: "+wae.getMessage());
            if (wae.getResponse().getStatus() == CardinalException.APPLICATION_EXCEPTION) {
                logger.warning("[TradeAction] Re-throwing wrapped application exception: ");
                // typecast to declared exception types
                if (cause instanceof Exception) {
                    throw (Exception)cause;
                }
            }
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeAction] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        
        // convert reference ID(s) stored in "response" to physical/proxy object(s)
        Collection<?> response_obj = (Collection<?>)SerializationUtil.decodeWithDynamicTypeCheck(response);
        return response_obj;

    }

    /**
     * Return a {@link QuoteDataBean}describing a current quote for the given
     * stock symbol
     *
     * @param symbol
     *            the stock symbol to retrieve the current Quote
     * @return the QuoteDataBean
     */
    @Override
    public QuoteDataBean getQuote(String symbol) throws Exception {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        form.param("symbol", symbol); 

        logger.info("[TradeAction] Calling service "+serviceURI+
            "/getQuote with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("getQuote")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeAction] Exception thrown in service call: "+wae.getMessage());
            if (wae.getResponse().getStatus() == CardinalException.APPLICATION_EXCEPTION) {
                logger.warning("[TradeAction] Re-throwing wrapped application exception: ");
                // typecast to declared exception types
                if (cause instanceof Exception) {
                    throw (Exception)cause;
                }
            }
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeAction] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        
        // convert reference ID(s) stored in "response" to physical/proxy object(s)
        QuoteDataBean response_obj = (QuoteDataBean)SerializationUtil.decodeWithDynamicTypeCheck(response);
        return response_obj;

    }

    /**
     * Update the stock quote price for the specified stock symbol
     *
     * @param symbol
     *            for stock quote to update
     * @return the QuoteDataBean describing the stock
     */
    /* avoid data collision with synch */
    @Override
    public QuoteDataBean updateQuotePriceVolume(String symbol, BigDecimal changeFactor, double sharesTraded) throws Exception {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        form.param("symbol", symbol); 

        
        // convert physical/proxy object(s) referenced by "changeFactor" to reference ID(s)
        String changeFactor_fpar = SerializationUtil.encodeWithDynamicTypeCheck(changeFactor);
        form.param("changeFactor", changeFactor_fpar);

        form.param("sharesTraded", String.valueOf(sharesTraded)); 

        logger.info("[TradeAction] Calling service "+serviceURI+
            "/updateQuotePriceVolume with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("updateQuotePriceVolume")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeAction] Exception thrown in service call: "+wae.getMessage());
            if (wae.getResponse().getStatus() == CardinalException.APPLICATION_EXCEPTION) {
                logger.warning("[TradeAction] Re-throwing wrapped application exception: ");
                // typecast to declared exception types
                if (cause instanceof Exception) {
                    throw (Exception)cause;
                }
            }
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeAction] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        
        // convert reference ID(s) stored in "response" to physical/proxy object(s)
        QuoteDataBean response_obj = (QuoteDataBean)SerializationUtil.decodeWithDynamicTypeCheck(response);
        return response_obj;

    }

    /**
     * Return the portfolio of stock holdings for the specified customer as a
     * collection of HoldingDataBeans
     *
     * @param userID
     *            the customer requesting the portfolio
     * @return Collection of the users portfolio of stock holdings
     */
    @Override
    public Collection<?> getHoldings(String userID) throws Exception {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        form.param("userID", userID); 

        logger.info("[TradeAction] Calling service "+serviceURI+
            "/getHoldings with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("getHoldings")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeAction] Exception thrown in service call: "+wae.getMessage());
            if (wae.getResponse().getStatus() == CardinalException.APPLICATION_EXCEPTION) {
                logger.warning("[TradeAction] Re-throwing wrapped application exception: ");
                // typecast to declared exception types
                if (cause instanceof Exception) {
                    throw (Exception)cause;
                }
            }
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeAction] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        
        // convert reference ID(s) stored in "response" to physical/proxy object(s)
        Collection<?> response_obj = (Collection<?>)SerializationUtil.decodeWithDynamicTypeCheck(response);
        return response_obj;

    }

    /**
     * Return a specific user stock holding identifed by the holdingID
     *
     * @param holdingID
     *            the holdingID to return
     * @return a HoldingDataBean describing the holding
     */
    @Override
    public HoldingDataBean getHolding(Integer holdingID) throws Exception {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        
        // convert physical/proxy object(s) referenced by "holdingID" to reference ID(s)
        String holdingID_fpar = SerializationUtil.encodeWithDynamicTypeCheck(holdingID);
        form.param("holdingID", holdingID_fpar);

        logger.info("[TradeAction] Calling service "+serviceURI+
            "/getHolding with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("getHolding")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeAction] Exception thrown in service call: "+wae.getMessage());
            if (wae.getResponse().getStatus() == CardinalException.APPLICATION_EXCEPTION) {
                logger.warning("[TradeAction] Re-throwing wrapped application exception: ");
                // typecast to declared exception types
                if (cause instanceof Exception) {
                    throw (Exception)cause;
                }
            }
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeAction] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        
        // convert reference ID(s) stored in "response" to physical/proxy object(s)
        HoldingDataBean response_obj = (HoldingDataBean)SerializationUtil.decodeWithDynamicTypeCheck(response);
        return response_obj;

    }

    /**
     * Return an AccountDataBean object for userID describing the account
     *
     * @param userID
     *            the account userID to lookup
     * @return User account data in AccountDataBean
     */
    @Override
    public AccountDataBean getAccountData(String userID) throws Exception {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        form.param("userID", userID); 

        logger.info("[TradeAction] Calling service "+serviceURI+
            "/getAccountData with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("getAccountData")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeAction] Exception thrown in service call: "+wae.getMessage());
            if (wae.getResponse().getStatus() == CardinalException.APPLICATION_EXCEPTION) {
                logger.warning("[TradeAction] Re-throwing wrapped application exception: ");
                // typecast to declared exception types
                if (cause instanceof Exception) {
                    throw (Exception)cause;
                }
            }
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeAction] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        
        // convert reference ID(s) stored in "response" to physical/proxy object(s)
        AccountDataBean response_obj = (AccountDataBean)SerializationUtil.decodeWithDynamicTypeCheck(response);
        return response_obj;

    }

    /**
     * Return an AccountProfileDataBean for userID providing the users profile
     *
     * @param userID
     *            the account userID to lookup
     */
    @Override
    public AccountProfileDataBean getAccountProfileData(String userID) throws Exception {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        form.param("userID", userID); 

        logger.info("[TradeAction] Calling service "+serviceURI+
            "/getAccountProfileData with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("getAccountProfileData")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeAction] Exception thrown in service call: "+wae.getMessage());
            if (wae.getResponse().getStatus() == CardinalException.APPLICATION_EXCEPTION) {
                logger.warning("[TradeAction] Re-throwing wrapped application exception: ");
                // typecast to declared exception types
                if (cause instanceof Exception) {
                    throw (Exception)cause;
                }
            }
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeAction] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        
        // convert reference ID(s) stored in "response" to physical/proxy object(s)
        AccountProfileDataBean response_obj = (AccountProfileDataBean)SerializationUtil.decodeWithDynamicTypeCheck(response);
        return response_obj;

    }

    /**
     * Update userID's account profile information using the provided
     * AccountProfileDataBean object
     *
     * @param accountProfileData
     *            account profile data in AccountProfileDataBean
     */
    @Override
    public AccountProfileDataBean updateAccountProfile(AccountProfileDataBean accountProfileData) throws Exception {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        
        // convert physical/proxy object(s) referenced by "accountProfileData" to reference ID(s)
        String accountProfileData_fpar = SerializationUtil.encodeWithDynamicTypeCheck(accountProfileData);
        form.param("accountProfileData", accountProfileData_fpar);

        logger.info("[TradeAction] Calling service "+serviceURI+
            "/updateAccountProfile with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("updateAccountProfile")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeAction] Exception thrown in service call: "+wae.getMessage());
            if (wae.getResponse().getStatus() == CardinalException.APPLICATION_EXCEPTION) {
                logger.warning("[TradeAction] Re-throwing wrapped application exception: ");
                // typecast to declared exception types
                if (cause instanceof Exception) {
                    throw (Exception)cause;
                }
            }
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeAction] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        
        // convert reference ID(s) stored in "response" to physical/proxy object(s)
        AccountProfileDataBean response_obj = (AccountProfileDataBean)SerializationUtil.decodeWithDynamicTypeCheck(response);
        return response_obj;

    }

    /**
     * Attempt to authenticate and login a user with the given password
     *
     * @param userID
     *            the customer to login
     * @param password
     *            the password entered by the customer for authentication
     * @return User account data in AccountDataBean
     */
    @Override
    public AccountDataBean login(String userID, String password) throws Exception {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        form.param("userID", userID); 

        form.param("password", password); 

        logger.info("[TradeAction] Calling service "+serviceURI+
            "/login with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("login")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeAction] Exception thrown in service call: "+wae.getMessage());
            if (wae.getResponse().getStatus() == CardinalException.APPLICATION_EXCEPTION) {
                logger.warning("[TradeAction] Re-throwing wrapped application exception: ");
                // typecast to declared exception types
                if (cause instanceof Exception) {
                    throw (Exception)cause;
                }
            }
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeAction] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        
        // convert reference ID(s) stored in "response" to physical/proxy object(s)
        AccountDataBean response_obj = (AccountDataBean)SerializationUtil.decodeWithDynamicTypeCheck(response);
        return response_obj;

    }

    /**
     * Logout the given user
     *
     * @param userID
     *            the customer to logout
     */
    @Override
    public void logout(String userID) throws Exception {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        form.param("userID", userID); 

        logger.info("[TradeAction] Calling service "+serviceURI+
            "/logout with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("logout")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeAction] Exception thrown in service call: "+wae.getMessage());
            if (wae.getResponse().getStatus() == CardinalException.APPLICATION_EXCEPTION) {
                logger.warning("[TradeAction] Re-throwing wrapped application exception: ");
                // typecast to declared exception types
                if (cause instanceof Exception) {
                    throw (Exception)cause;
                }
            }
            throw (RuntimeException)cause;
        }

    }

    /**
     * Register a new Trade customer. Create a new user profile, user registry
     * entry, account with initial balance, and empty portfolio.
     *
     * @param userID
     *            the new customer to register
     * @param password
     *            the customers password
     * @param fullname
     *            the customers fullname
     * @param address
     *            the customers street address
     * @param email
     *            the customers email address
     * @param creditCard
     *            the customers creditcard number
     * @param openBalance
     *            the amount to charge to the customers credit to open the
     *            account and set the initial balance
     * @return the userID if successful, null otherwise
     */
    @Override
    public AccountDataBean register(String userID, String password, String fullname, String address, String email, String creditCard, BigDecimal openBalance)
            throws Exception {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        form.param("userID", userID); 

        form.param("password", password); 

        form.param("fullname", fullname); 

        form.param("address", address); 

        form.param("email", email); 

        form.param("creditCard", creditCard); 

        
        // convert physical/proxy object(s) referenced by "openBalance" to reference ID(s)
        String openBalance_fpar = SerializationUtil.encodeWithDynamicTypeCheck(openBalance);
        form.param("openBalance", openBalance_fpar);

        logger.info("[TradeAction] Calling service "+serviceURI+
            "/register_005 with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("register_005")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeAction] Exception thrown in service call: "+wae.getMessage());
            if (wae.getResponse().getStatus() == CardinalException.APPLICATION_EXCEPTION) {
                logger.warning("[TradeAction] Re-throwing wrapped application exception: ");
                // typecast to declared exception types
                if (cause instanceof Exception) {
                    throw (Exception)cause;
                }
            }
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeAction] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        
        // convert reference ID(s) stored in "response" to physical/proxy object(s)
        AccountDataBean response_obj = (AccountDataBean)SerializationUtil.decodeWithDynamicTypeCheck(response);
        return response_obj;

    }

    public AccountDataBean register(String userID, String password, String fullname, String address, String email, String creditCard, String openBalanceString)
            throws Exception {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        form.param("userID", userID); 

        form.param("password", password); 

        form.param("fullname", fullname); 

        form.param("address", address); 

        form.param("email", email); 

        form.param("creditCard", creditCard); 

        form.param("openBalanceString", openBalanceString); 

        logger.info("[TradeAction] Calling service "+serviceURI+
            "/register_006 with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("register_006")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeAction] Exception thrown in service call: "+wae.getMessage());
            if (wae.getResponse().getStatus() == CardinalException.APPLICATION_EXCEPTION) {
                logger.warning("[TradeAction] Re-throwing wrapped application exception: ");
                // typecast to declared exception types
                if (cause instanceof Exception) {
                    throw (Exception)cause;
                }
            }
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeAction] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        
        // convert reference ID(s) stored in "response" to physical/proxy object(s)
        AccountDataBean response_obj = (AccountDataBean)SerializationUtil.decodeWithDynamicTypeCheck(response);
        return response_obj;

    }

    /**
     * Reset the TradeData by - removing all newly registered users by scenario
     * servlet (i.e. users with userID's beginning with "ru:") * - removing all
     * buy/sell order pairs - setting logoutCount = loginCount
     *
     * return statistics for this benchmark run
     */
    @Override
    public RunStatsDataBean resetTrade(boolean deleteAll) throws Exception {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        form.param("deleteAll", String.valueOf(deleteAll)); 

        logger.info("[TradeAction] Calling service "+serviceURI+
            "/resetTrade with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("resetTrade")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeAction] Exception thrown in service call: "+wae.getMessage());
            if (wae.getResponse().getStatus() == CardinalException.APPLICATION_EXCEPTION) {
                logger.warning("[TradeAction] Re-throwing wrapped application exception: ");
                // typecast to declared exception types
                if (cause instanceof Exception) {
                    throw (Exception)cause;
                }
            }
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeAction] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        
        // convert reference ID(s) stored in "response" to physical/proxy object(s)
        RunStatsDataBean response_obj = (RunStatsDataBean)SerializationUtil.decodeWithDynamicTypeCheck(response);
        return response_obj;

    }
}
