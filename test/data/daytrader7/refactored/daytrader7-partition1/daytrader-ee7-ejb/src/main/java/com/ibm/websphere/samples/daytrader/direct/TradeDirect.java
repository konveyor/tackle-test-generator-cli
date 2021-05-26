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
package com.ibm.websphere.samples.daytrader.direct;

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

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;

import javax.enterprise.concurrent.ManagedThreadFactory;
import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import javax.transaction.UserTransaction;

import com.ibm.websphere.samples.daytrader.TradeAction;
import com.ibm.websphere.samples.daytrader.TradeServices;
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
import com.ibm.websphere.samples.daytrader.util.MDBStats;
import com.ibm.websphere.samples.daytrader.util.TradeConfig;

/**
 * TradeDirect uses direct JDBC and JMS access to a
 * <code>javax.sql.DataSource</code> to implement the business methods of the
 * Trade online broker application. These business methods represent the
 * features and operations that can be performed by customers of the brokerage
 * such as login, logout, get a stock quote, buy or sell a stock, etc. and are
 * specified in the {@link com.ibm.websphere.samples.daytrader.TradeServices}
 * interface
 *
 * Note: In order for this class to be thread-safe, a new TradeJDBC must be
 * created for each call to a method from the TradeInterface interface.
 * Otherwise, pooled connections may not be released.
 *
 * @see com.ibm.websphere.samples.daytrader.TradeServices
 *
 */

public class TradeDirect implements KluInterface, TradeServices {
    private String klu__referenceID = "";
    private static String serviceURI;
    private static Client client;
    private static final Logger logger = CardinalLogger.getLogger(TradeDirect.class);

    static {
        client = ClientBuilder.newClient();

        logger.info("Static initializer of TradeDirect of cluster partition_2");

        serviceURI = System.getenv().get("APPLICATION_PARTITION_2_REST_URL");
        if (serviceURI == null) {
            throw new RuntimeException("Environment variable "+
                "APPLICATION_PARTITION_2_REST_URL not set\n"+
                "Please set APPLICATION_PARTITION_2_REST_URL to "+
                "partition_2 host:port"
            );
        }

        if (!serviceURI.endsWith("/")) {
            serviceURI += "/";
        }
        serviceURI += "TradeDirectService";

        try {
            java.net.URI uri = java.net.URI.create(serviceURI);
        }
        catch (Exception e) {
            throw new RuntimeException("Invalid URI for partition partition_2, "+
                "service TradeDirectService: "+serviceURI, e);
        }

        logger.info("partition_2 TradeDirectService URI = " + serviceURI);
    }




    // constructor for creating proxy instances of this class using reference ID
    public TradeDirect(CardinalString referenceId) {
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





   
    /**
     * Zero arg constructor for TradeDirect
     */
    public TradeDirect() {
        // create form for service request
        Form form = new Form();
        logger.info("[TradeDirect] Calling service "+serviceURI+
            "/TradeDirect_001 with form: "+form.asMap());

        // call constructor service and store ref ID
        Response svc_response;
        try {
            svc_response = 
                client.target(serviceURI) 
                .path("TradeDirect_001") 
                .request(MediaType.APPLICATION_JSON) 
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeDirect] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeDirect] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        setKlu__referenceID(response_json.getString("return_value"));
        svc_response.close();
    }

    public TradeDirect(boolean inSession) {
        // create form for service request
        Form form = new Form();
        form.param("inSession", String.valueOf(inSession)); 

        logger.info("[TradeDirect] Calling service "+serviceURI+
            "/TradeDirect_002 with form: "+form.asMap());

        // call constructor service and store ref ID
        Response svc_response;
        try {
            svc_response = 
                client.target(serviceURI) 
                .path("TradeDirect_002") 
                .request(MediaType.APPLICATION_JSON) 
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeDirect] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeDirect] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        setKlu__referenceID(response_json.getString("return_value"));
        svc_response.close();
    }

    /**
     * @see TradeServices#getMarketSummary()
     */
    @Override
    public MarketSummaryDataBean getMarketSummary() throws Exception {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        logger.info("[TradeDirect] Calling service "+serviceURI+
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
            logger.warning("[TradeDirect] Exception thrown in service call: "+wae.getMessage());
            if (wae.getResponse().getStatus() == CardinalException.APPLICATION_EXCEPTION) {
                logger.warning("[TradeDirect] Re-throwing wrapped application exception: ");
                // typecast to declared exception types
                if (cause instanceof Exception) {
                    throw (Exception)cause;
                }
            }
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeDirect] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        
        // convert reference ID(s) stored in "response" to physical/proxy object(s)
        MarketSummaryDataBean response_obj = (MarketSummaryDataBean)SerializationUtil.decodeWithDynamicTypeCheck(response);
        return response_obj;

    }

    /**
     * @see TradeServices#buy(String, String, double)
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

        logger.info("[TradeDirect] Calling service "+serviceURI+
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
            logger.warning("[TradeDirect] Exception thrown in service call: "+wae.getMessage());
            if (wae.getResponse().getStatus() == CardinalException.APPLICATION_EXCEPTION) {
                logger.warning("[TradeDirect] Re-throwing wrapped application exception: ");
                // typecast to declared exception types
                if (cause instanceof Exception) {
                    throw (Exception)cause;
                }
            }
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeDirect] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        
        // convert reference ID(s) stored in "response" to physical/proxy object(s)
        OrderDataBean response_obj = (OrderDataBean)SerializationUtil.decodeWithDynamicTypeCheck(response);
        return response_obj;

    }

    /**
     * @see TradeServices#sell(String, Integer)
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

        logger.info("[TradeDirect] Calling service "+serviceURI+
            "/sell with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("sell")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeDirect] Exception thrown in service call: "+wae.getMessage());
            if (wae.getResponse().getStatus() == CardinalException.APPLICATION_EXCEPTION) {
                logger.warning("[TradeDirect] Re-throwing wrapped application exception: ");
                // typecast to declared exception types
                if (cause instanceof Exception) {
                    throw (Exception)cause;
                }
            }
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeDirect] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        
        // convert reference ID(s) stored in "response" to physical/proxy object(s)
        OrderDataBean response_obj = (OrderDataBean)SerializationUtil.decodeWithDynamicTypeCheck(response);
        return response_obj;

    }

    /**
     * @see TradeServices#queueOrder(Integer)
     */
    @Override
    public void queueOrder(Integer orderID, boolean twoPhase) throws Exception {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        
        // convert physical/proxy object(s) referenced by "orderID" to reference ID(s)
        String orderID_fpar = SerializationUtil.encodeWithDynamicTypeCheck(orderID);
        form.param("orderID", orderID_fpar);

        form.param("twoPhase", String.valueOf(twoPhase)); 

        logger.info("[TradeDirect] Calling service "+serviceURI+
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
            logger.warning("[TradeDirect] Exception thrown in service call: "+wae.getMessage());
            if (wae.getResponse().getStatus() == CardinalException.APPLICATION_EXCEPTION) {
                logger.warning("[TradeDirect] Re-throwing wrapped application exception: ");
                // typecast to declared exception types
                if (cause instanceof Exception) {
                    throw (Exception)cause;
                }
            }
            throw (RuntimeException)cause;
        }

    }

    /**
     * @see TradeServices#completeOrder(Integer)
     */
    @Override
    public OrderDataBean completeOrder(Integer orderID, boolean twoPhase) throws Exception {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        
        // convert physical/proxy object(s) referenced by "orderID" to reference ID(s)
        String orderID_fpar = SerializationUtil.encodeWithDynamicTypeCheck(orderID);
        form.param("orderID", orderID_fpar);

        form.param("twoPhase", String.valueOf(twoPhase)); 

        logger.info("[TradeDirect] Calling service "+serviceURI+
            "/completeOrder_003 with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("completeOrder_003")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeDirect] Exception thrown in service call: "+wae.getMessage());
            if (wae.getResponse().getStatus() == CardinalException.APPLICATION_EXCEPTION) {
                logger.warning("[TradeDirect] Re-throwing wrapped application exception: ");
                // typecast to declared exception types
                if (cause instanceof Exception) {
                    throw (Exception)cause;
                }
            }
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeDirect] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        
        // convert reference ID(s) stored in "response" to physical/proxy object(s)
        OrderDataBean response_obj = (OrderDataBean)SerializationUtil.decodeWithDynamicTypeCheck(response);
        return response_obj;

    }

    /**
     * @see TradeServices#cancelOrder(Integer, boolean)
     */
    @Override
    public void cancelOrder(Integer orderID, boolean twoPhase) throws Exception {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        
        // convert physical/proxy object(s) referenced by "orderID" to reference ID(s)
        String orderID_fpar = SerializationUtil.encodeWithDynamicTypeCheck(orderID);
        form.param("orderID", orderID_fpar);

        form.param("twoPhase", String.valueOf(twoPhase)); 

        logger.info("[TradeDirect] Calling service "+serviceURI+
            "/cancelOrder_005 with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("cancelOrder_005")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeDirect] Exception thrown in service call: "+wae.getMessage());
            if (wae.getResponse().getStatus() == CardinalException.APPLICATION_EXCEPTION) {
                logger.warning("[TradeDirect] Re-throwing wrapped application exception: ");
                // typecast to declared exception types
                if (cause instanceof Exception) {
                    throw (Exception)cause;
                }
            }
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

        logger.info("[TradeDirect] Calling service "+serviceURI+
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
            logger.warning("[TradeDirect] Exception thrown in service call: "+wae.getMessage());
            if (wae.getResponse().getStatus() == CardinalException.APPLICATION_EXCEPTION) {
                logger.warning("[TradeDirect] Re-throwing wrapped application exception: ");
                // typecast to declared exception types
                if (cause instanceof Exception) {
                    throw (Exception)cause;
                }
            }
            throw (RuntimeException)cause;
        }

    }

    /**
     * @see TradeServices#getOrders(String)
     */
    @Override
    public Collection<OrderDataBean> getOrders(String userID) throws Exception {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        form.param("userID", userID); 

        logger.info("[TradeDirect] Calling service "+serviceURI+
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
            logger.warning("[TradeDirect] Exception thrown in service call: "+wae.getMessage());
            if (wae.getResponse().getStatus() == CardinalException.APPLICATION_EXCEPTION) {
                logger.warning("[TradeDirect] Re-throwing wrapped application exception: ");
                // typecast to declared exception types
                if (cause instanceof Exception) {
                    throw (Exception)cause;
                }
            }
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeDirect] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        
        // convert reference ID(s) stored in "response" to physical/proxy object(s)
        Collection<OrderDataBean> response_obj = (Collection<OrderDataBean>)SerializationUtil.decodeWithDynamicTypeCheck(response);
        return response_obj;

    }

    /**
     * @see TradeServices#getClosedOrders(String)
     */
    @Override
    public Collection<OrderDataBean> getClosedOrders(String userID) throws Exception {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        form.param("userID", userID); 

        logger.info("[TradeDirect] Calling service "+serviceURI+
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
            logger.warning("[TradeDirect] Exception thrown in service call: "+wae.getMessage());
            if (wae.getResponse().getStatus() == CardinalException.APPLICATION_EXCEPTION) {
                logger.warning("[TradeDirect] Re-throwing wrapped application exception: ");
                // typecast to declared exception types
                if (cause instanceof Exception) {
                    throw (Exception)cause;
                }
            }
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeDirect] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        
        // convert reference ID(s) stored in "response" to physical/proxy object(s)
        Collection<OrderDataBean> response_obj = (Collection<OrderDataBean>)SerializationUtil.decodeWithDynamicTypeCheck(response);
        return response_obj;

    }

    /**
     * @see TradeServices#createQuote(String, String, BigDecimal)
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

        logger.info("[TradeDirect] Calling service "+serviceURI+
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
            logger.warning("[TradeDirect] Exception thrown in service call: "+wae.getMessage());
            if (wae.getResponse().getStatus() == CardinalException.APPLICATION_EXCEPTION) {
                logger.warning("[TradeDirect] Re-throwing wrapped application exception: ");
                // typecast to declared exception types
                if (cause instanceof Exception) {
                    throw (Exception)cause;
                }
            }
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeDirect] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        
        // convert reference ID(s) stored in "response" to physical/proxy object(s)
        QuoteDataBean response_obj = (QuoteDataBean)SerializationUtil.decodeWithDynamicTypeCheck(response);
        return response_obj;

    }

    /**
     * @see TradeServices#getQuote(String)
     */

    @Override
    public QuoteDataBean getQuote(String symbol) throws Exception {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        form.param("symbol", symbol); 

        logger.info("[TradeDirect] Calling service "+serviceURI+
            "/getQuote_007 with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("getQuote_007")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeDirect] Exception thrown in service call: "+wae.getMessage());
            if (wae.getResponse().getStatus() == CardinalException.APPLICATION_EXCEPTION) {
                logger.warning("[TradeDirect] Re-throwing wrapped application exception: ");
                // typecast to declared exception types
                if (cause instanceof Exception) {
                    throw (Exception)cause;
                }
            }
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeDirect] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        
        // convert reference ID(s) stored in "response" to physical/proxy object(s)
        QuoteDataBean response_obj = (QuoteDataBean)SerializationUtil.decodeWithDynamicTypeCheck(response);
        return response_obj;

    }

    /**
     * @see TradeServices#getAllQuotes(String)
     */
    @Override
    public Collection<QuoteDataBean> getAllQuotes() throws Exception {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        logger.info("[TradeDirect] Calling service "+serviceURI+
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
            logger.warning("[TradeDirect] Exception thrown in service call: "+wae.getMessage());
            if (wae.getResponse().getStatus() == CardinalException.APPLICATION_EXCEPTION) {
                logger.warning("[TradeDirect] Re-throwing wrapped application exception: ");
                // typecast to declared exception types
                if (cause instanceof Exception) {
                    throw (Exception)cause;
                }
            }
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeDirect] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        
        // convert reference ID(s) stored in "response" to physical/proxy object(s)
        Collection<QuoteDataBean> response_obj = (Collection<QuoteDataBean>)SerializationUtil.decodeWithDynamicTypeCheck(response);
        return response_obj;

    }

    /**
     * @see TradeServices#getHoldings(String)
     */
    @Override
    public Collection<HoldingDataBean> getHoldings(String userID) throws Exception {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        form.param("userID", userID); 

        logger.info("[TradeDirect] Calling service "+serviceURI+
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
            logger.warning("[TradeDirect] Exception thrown in service call: "+wae.getMessage());
            if (wae.getResponse().getStatus() == CardinalException.APPLICATION_EXCEPTION) {
                logger.warning("[TradeDirect] Re-throwing wrapped application exception: ");
                // typecast to declared exception types
                if (cause instanceof Exception) {
                    throw (Exception)cause;
                }
            }
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeDirect] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        
        // convert reference ID(s) stored in "response" to physical/proxy object(s)
        Collection<HoldingDataBean> response_obj = (Collection<HoldingDataBean>)SerializationUtil.decodeWithDynamicTypeCheck(response);
        return response_obj;

    }

    /**
     * @see TradeServices#getHolding(Integer)
     */
    @Override
    public HoldingDataBean getHolding(Integer holdingID) throws Exception {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        
        // convert physical/proxy object(s) referenced by "holdingID" to reference ID(s)
        String holdingID_fpar = SerializationUtil.encodeWithDynamicTypeCheck(holdingID);
        form.param("holdingID", holdingID_fpar);

        logger.info("[TradeDirect] Calling service "+serviceURI+
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
            logger.warning("[TradeDirect] Exception thrown in service call: "+wae.getMessage());
            if (wae.getResponse().getStatus() == CardinalException.APPLICATION_EXCEPTION) {
                logger.warning("[TradeDirect] Re-throwing wrapped application exception: ");
                // typecast to declared exception types
                if (cause instanceof Exception) {
                    throw (Exception)cause;
                }
            }
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeDirect] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        
        // convert reference ID(s) stored in "response" to physical/proxy object(s)
        HoldingDataBean response_obj = (HoldingDataBean)SerializationUtil.decodeWithDynamicTypeCheck(response);
        return response_obj;

    }

    /**
     * @see TradeServices#getAccountData(String)
     */
    @Override
    public AccountDataBean getAccountData(String userID) throws Exception {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        form.param("userID", userID); 

        logger.info("[TradeDirect] Calling service "+serviceURI+
            "/getAccountData_009 with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("getAccountData_009")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeDirect] Exception thrown in service call: "+wae.getMessage());
            if (wae.getResponse().getStatus() == CardinalException.APPLICATION_EXCEPTION) {
                logger.warning("[TradeDirect] Re-throwing wrapped application exception: ");
                // typecast to declared exception types
                if (cause instanceof Exception) {
                    throw (Exception)cause;
                }
            }
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeDirect] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        
        // convert reference ID(s) stored in "response" to physical/proxy object(s)
        AccountDataBean response_obj = (AccountDataBean)SerializationUtil.decodeWithDynamicTypeCheck(response);
        return response_obj;

    }

    /*private AccountDataBean getAccountDataForUpdate(Connection conn,
            String userID) throws Exception {
        PreparedStatement stmt = getStatement(conn,
                getAccountForUserForUpdateSQL);
        stmt.setString(1, userID);
        ResultSet rs = stmt.executeQuery();
        AccountDataBean accountData = getAccountDataFromResultSet(rs);
        stmt.close();
        return accountData;
    }*/

    /**
     * @see TradeServices#getAccountData(String)
     */
    public AccountDataBean getAccountData(int accountID) throws Exception {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        form.param("accountID", String.valueOf(accountID)); 

        logger.info("[TradeDirect] Calling service "+serviceURI+
            "/getAccountData_0011 with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("getAccountData_0011")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeDirect] Exception thrown in service call: "+wae.getMessage());
            if (wae.getResponse().getStatus() == CardinalException.APPLICATION_EXCEPTION) {
                logger.warning("[TradeDirect] Re-throwing wrapped application exception: ");
                // typecast to declared exception types
                if (cause instanceof Exception) {
                    throw (Exception)cause;
                }
            }
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeDirect] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        
        // convert reference ID(s) stored in "response" to physical/proxy object(s)
        AccountDataBean response_obj = (AccountDataBean)SerializationUtil.decodeWithDynamicTypeCheck(response);
        return response_obj;

    }

    /**
     * @see TradeServices#getAccountProfileData(String)
     */
    @Override
    public AccountProfileDataBean getAccountProfileData(String userID) throws Exception {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        form.param("userID", userID); 

        logger.info("[TradeDirect] Calling service "+serviceURI+
            "/getAccountProfileData_0015 with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("getAccountProfileData_0015")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeDirect] Exception thrown in service call: "+wae.getMessage());
            if (wae.getResponse().getStatus() == CardinalException.APPLICATION_EXCEPTION) {
                logger.warning("[TradeDirect] Re-throwing wrapped application exception: ");
                // typecast to declared exception types
                if (cause instanceof Exception) {
                    throw (Exception)cause;
                }
            }
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeDirect] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        
        // convert reference ID(s) stored in "response" to physical/proxy object(s)
        AccountProfileDataBean response_obj = (AccountProfileDataBean)SerializationUtil.decodeWithDynamicTypeCheck(response);
        return response_obj;

    }

    /**
     * @see TradeServices#updateAccountProfile(AccountProfileDataBean)
     */
    @Override
    public AccountProfileDataBean updateAccountProfile(AccountProfileDataBean profileData) throws Exception {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        
        // convert physical/proxy object(s) referenced by "profileData" to reference ID(s)
        String profileData_fpar = SerializationUtil.encodeWithDynamicTypeCheck(profileData);
        form.param("profileData", profileData_fpar);

        logger.info("[TradeDirect] Calling service "+serviceURI+
            "/updateAccountProfile_0018 with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("updateAccountProfile_0018")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeDirect] Exception thrown in service call: "+wae.getMessage());
            if (wae.getResponse().getStatus() == CardinalException.APPLICATION_EXCEPTION) {
                logger.warning("[TradeDirect] Re-throwing wrapped application exception: ");
                // typecast to declared exception types
                if (cause instanceof Exception) {
                    throw (Exception)cause;
                }
            }
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeDirect] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        
        // convert reference ID(s) stored in "response" to physical/proxy object(s)
        AccountProfileDataBean response_obj = (AccountProfileDataBean)SerializationUtil.decodeWithDynamicTypeCheck(response);
        return response_obj;

    }

    /*
    private void updateQuoteVolume(Connection conn, QuoteDataBean quoteData,
            double quantity) throws Exception {
        PreparedStatement stmt = getStatement(conn, updateQuoteVolumeSQL);

        stmt.setDouble(1, quantity);
        stmt.setString(2, quoteData.getSymbol());

        stmt.executeUpdate();
        stmt.close();
    }
     */

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

        logger.info("[TradeDirect] Calling service "+serviceURI+
            "/updateQuotePriceVolume_0020 with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("updateQuotePriceVolume_0020")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeDirect] Exception thrown in service call: "+wae.getMessage());
            if (wae.getResponse().getStatus() == CardinalException.APPLICATION_EXCEPTION) {
                logger.warning("[TradeDirect] Re-throwing wrapped application exception: ");
                // typecast to declared exception types
                if (cause instanceof Exception) {
                    throw (Exception)cause;
                }
            }
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeDirect] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        
        // convert reference ID(s) stored in "response" to physical/proxy object(s)
        QuoteDataBean response_obj = (QuoteDataBean)SerializationUtil.decodeWithDynamicTypeCheck(response);
        return response_obj;

    }

    /**
     * Update a quote's price and volume
     *
     * @param symbol
     *            The PK of the quote
     * @param changeFactor
     *            the percent to change the old price by (between 50% and 150%)
     * @param sharedTraded
     *            the ammount to add to the current volume
     * @param publishQuotePriceChange
     *            used by the PingJDBCWrite Primitive to ensure no JMS is used,
     *            should be true for all normal calls to this API
     */
    public QuoteDataBean updateQuotePriceVolumeInt(String symbol, BigDecimal changeFactor, double sharesTraded, boolean publishQuotePriceChange)
            throws Exception {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        form.param("symbol", symbol); 

        
        // convert physical/proxy object(s) referenced by "changeFactor" to reference ID(s)
        String changeFactor_fpar = SerializationUtil.encodeWithDynamicTypeCheck(changeFactor);
        form.param("changeFactor", changeFactor_fpar);

        form.param("sharesTraded", String.valueOf(sharesTraded)); 

        form.param("publishQuotePriceChange", String.valueOf(publishQuotePriceChange)); 

        logger.info("[TradeDirect] Calling service "+serviceURI+
            "/updateQuotePriceVolumeInt with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("updateQuotePriceVolumeInt")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeDirect] Exception thrown in service call: "+wae.getMessage());
            if (wae.getResponse().getStatus() == CardinalException.APPLICATION_EXCEPTION) {
                logger.warning("[TradeDirect] Re-throwing wrapped application exception: ");
                // typecast to declared exception types
                if (cause instanceof Exception) {
                    throw (Exception)cause;
                }
            }
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeDirect] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        
        // convert reference ID(s) stored in "response" to physical/proxy object(s)
        QuoteDataBean response_obj = (QuoteDataBean)SerializationUtil.decodeWithDynamicTypeCheck(response);
        return response_obj;

    }

    /**
     * @see TradeServices#login(String, String)
     */

    @Override
    public AccountDataBean login(String userID, String password) throws Exception {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        form.param("userID", userID); 

        form.param("password", password); 

        logger.info("[TradeDirect] Calling service "+serviceURI+
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
            logger.warning("[TradeDirect] Exception thrown in service call: "+wae.getMessage());
            if (wae.getResponse().getStatus() == CardinalException.APPLICATION_EXCEPTION) {
                logger.warning("[TradeDirect] Re-throwing wrapped application exception: ");
                // typecast to declared exception types
                if (cause instanceof Exception) {
                    throw (Exception)cause;
                }
            }
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeDirect] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        
        // convert reference ID(s) stored in "response" to physical/proxy object(s)
        AccountDataBean response_obj = (AccountDataBean)SerializationUtil.decodeWithDynamicTypeCheck(response);
        return response_obj;

    }

    /**
     * @see TradeServices#logout(String)
     */
    @Override
    public void logout(String userID) throws Exception {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        form.param("userID", userID); 

        logger.info("[TradeDirect] Calling service "+serviceURI+
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
            logger.warning("[TradeDirect] Exception thrown in service call: "+wae.getMessage());
            if (wae.getResponse().getStatus() == CardinalException.APPLICATION_EXCEPTION) {
                logger.warning("[TradeDirect] Re-throwing wrapped application exception: ");
                // typecast to declared exception types
                if (cause instanceof Exception) {
                    throw (Exception)cause;
                }
            }
            throw (RuntimeException)cause;
        }

    }

    /**
     * @see TradeServices#register(String, String, String, String, String,
     *      String, BigDecimal, boolean)
     */

    @Override
    public AccountDataBean register(String userID, String password, String fullname, String address, String email, String creditcard, BigDecimal openBalance)
            throws Exception {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        form.param("userID", userID); 

        form.param("password", password); 

        form.param("fullname", fullname); 

        form.param("address", address); 

        form.param("email", email); 

        form.param("creditcard", creditcard); 

        
        // convert physical/proxy object(s) referenced by "openBalance" to reference ID(s)
        String openBalance_fpar = SerializationUtil.encodeWithDynamicTypeCheck(openBalance);
        form.param("openBalance", openBalance_fpar);

        logger.info("[TradeDirect] Calling service "+serviceURI+
            "/register with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("register")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeDirect] Exception thrown in service call: "+wae.getMessage());
            if (wae.getResponse().getStatus() == CardinalException.APPLICATION_EXCEPTION) {
                logger.warning("[TradeDirect] Re-throwing wrapped application exception: ");
                // typecast to declared exception types
                if (cause instanceof Exception) {
                    throw (Exception)cause;
                }
            }
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeDirect] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        
        // convert reference ID(s) stored in "response" to physical/proxy object(s)
        AccountDataBean response_obj = (AccountDataBean)SerializationUtil.decodeWithDynamicTypeCheck(response);
        return response_obj;

    }

    public String checkDBProductName() throws Exception {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        logger.info("[TradeDirect] Calling service "+serviceURI+
            "/checkDBProductName with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("checkDBProductName")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeDirect] Exception thrown in service call: "+wae.getMessage());
            if (wae.getResponse().getStatus() == CardinalException.APPLICATION_EXCEPTION) {
                logger.warning("[TradeDirect] Re-throwing wrapped application exception: ");
                // typecast to declared exception types
                if (cause instanceof Exception) {
                    throw (Exception)cause;
                }
            }
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeDirect] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return response;
    }

    public boolean recreateDBTables(Object[] sqlBuffer, java.io.PrintWriter out) throws Exception {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        
        // convert physical/proxy object(s) referenced by "sqlBuffer" to reference ID(s)
        String sqlBuffer_fpar = SerializationUtil.encodeWithDynamicTypeCheck(sqlBuffer);
        form.param("sqlBuffer", sqlBuffer_fpar);

        
        // convert physical/proxy object(s) referenced by "out" to reference ID(s)
        String out_fpar = SerializationUtil.encodeWithDynamicTypeCheck(out);
        form.param("out", out_fpar);

        logger.info("[TradeDirect] Calling service "+serviceURI+
            "/recreateDBTables with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("recreateDBTables")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeDirect] Exception thrown in service call: "+wae.getMessage());
            if (wae.getResponse().getStatus() == CardinalException.APPLICATION_EXCEPTION) {
                logger.warning("[TradeDirect] Re-throwing wrapped application exception: ");
                // typecast to declared exception types
                if (cause instanceof Exception) {
                    throw (Exception)cause;
                }
            }
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeDirect] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return Boolean.parseBoolean(response);
    }

    @Override
    public RunStatsDataBean resetTrade(boolean deleteAll) throws Exception {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        form.param("deleteAll", String.valueOf(deleteAll)); 

        logger.info("[TradeDirect] Calling service "+serviceURI+
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
            logger.warning("[TradeDirect] Exception thrown in service call: "+wae.getMessage());
            if (wae.getResponse().getStatus() == CardinalException.APPLICATION_EXCEPTION) {
                logger.warning("[TradeDirect] Re-throwing wrapped application exception: ");
                // typecast to declared exception types
                if (cause instanceof Exception) {
                    throw (Exception)cause;
                }
            }
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeDirect] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        
        // convert reference ID(s) stored in "response" to physical/proxy object(s)
        RunStatsDataBean response_obj = (RunStatsDataBean)SerializationUtil.decodeWithDynamicTypeCheck(response);
        return response_obj;

    }

    public Connection getConnPublic() throws Exception {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        logger.info("[TradeDirect] Calling service "+serviceURI+
            "/getConnPublic with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("getConnPublic")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeDirect] Exception thrown in service call: "+wae.getMessage());
            if (wae.getResponse().getStatus() == CardinalException.APPLICATION_EXCEPTION) {
                logger.warning("[TradeDirect] Re-throwing wrapped application exception: ");
                // typecast to declared exception types
                if (cause instanceof Exception) {
                    throw (Exception)cause;
                }
            }
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeDirect] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        
        // convert reference ID(s) stored in "response" to physical/proxy object(s)
        Connection response_obj = (Connection)SerializationUtil.decodeWithDynamicTypeCheck(response);
        return response_obj;

    }

    public static synchronized void init() {
        // create form for service request
        Form form = new Form();
        logger.info("[TradeDirect] Calling service "+serviceURI+
            "/init with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("init")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeDirect] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }

    }

    public static void destroy() {
        // create form for service request
        Form form = new Form();
        logger.info("[TradeDirect] Calling service "+serviceURI+
            "/destroy with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("destroy")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeDirect] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }

    }

}
