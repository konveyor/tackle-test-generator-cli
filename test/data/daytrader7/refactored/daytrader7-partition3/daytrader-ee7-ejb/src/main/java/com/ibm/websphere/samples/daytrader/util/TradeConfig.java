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
package com.ibm.websphere.samples.daytrader.util;

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
import java.util.ArrayList;
import java.util.Random;

/**
 * TradeConfig is a JavaBean holding all configuration and runtime parameters
 * for the Trade application TradeConfig sets runtime parameters such as the
 * RunTimeMode (EJB, JDBC, EJB_ALT)
 *
 */

public class TradeConfig implements KluInterface {
    
    public static final int EJB3 = 0;
    
    public static final int DIRECT = 1;
    
    public static final int SYNCH = 0;
    
    public static final int ASYNCH_2PHASE = 1;
    
    public static final int ASYNCH_MANAGEDTHREAD = 2;
    
    public static final int STANDARD = 0;
    
    public static final int JSP = 0;
    
    public static final int JSP_Images = 1;
    
    public static final String newUserPrefix = "ru:";
    
    public static final int verifyPercent = 5;
    

    /*
     * Trade Scenario actions mixes. Each of the array rows represents a
     * specific Trade Scenario Mix. The columns give the percentages for each
     * action in the column header. Note: "login" is always 0. logout represents
     * both login and logout (because each logout operation will cause a new
     * login when the user context attempts the next action.
     */
    /* Trade Scenario Workload parameters */
    public static final int HOME_OP = 0;
    
    public static final int QUOTE_OP = 1;
    
    public static final int LOGIN_OP = 2;
    
    public static final int LOGOUT_OP = 3;
    
    public static final int REGISTER_OP = 4;
    
    public static final int ACCOUNT_OP = 5;
    
    public static final int PORTFOLIO_OP = 6;
    
    public static final int BUY_OP = 7;
    
    public static final int SELL_OP = 8;
    
    public static final int UPDATEACCOUNT_OP = 9;
    
    // Tracks the number of buys over sell when a users portfolio is empty
    // Used to maintain the correct ratio of buys/sells

    /* JSP pages for all Trade Actions */

    public static final int WELCOME_PAGE = 0;
    
    public static final int REGISTER_PAGE = 1;
    
    public static final int PORTFOLIO_PAGE = 2;
    
    public static final int QUOTE_PAGE = 3;
    
    public static final int HOME_PAGE = 4;
    
    public static final int ACCOUNT_PAGE = 5;
    
    public static final int ORDER_PAGE = 6;
    
    public static final int CONFIG_PAGE = 7;
    
    public static final int STATS_PAGE = 8;
    
    public static final int MARKET_SUMMARY_PAGE = 9;

    private String klu__referenceID = "";
    private static String serviceURI;
    private static Client client;
    private static final Logger logger = CardinalLogger.getLogger(TradeConfig.class);

    static {
        client = ClientBuilder.newClient();

        logger.info("Static initializer of TradeConfig of cluster partition_1");

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
        serviceURI += "TradeConfigService";

        try {
            java.net.URI uri = java.net.URI.create(serviceURI);
        }
        catch (Exception e) {
            throw new RuntimeException("Invalid URI for partition partition_1, "+
                "service TradeConfigService: "+serviceURI, e);
        }

        logger.info("partition_1 TradeConfigService URI = " + serviceURI);
    }

    // default constructor (generated)
    public TradeConfig() {
        Response svc_response =
            client.target(serviceURI) 
            .path("TradeConfig_default_ctor") 
            .request(MediaType.APPLICATION_JSON) 
            .post(Entity.text(""), Response.class);
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeConfig()] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        setKlu__referenceID(response_json.getString("return_value"));
        svc_response.close();

    }



    // constructor for creating proxy instances of this class using reference ID
    public TradeConfig(CardinalString referenceId) {
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



    // getter method for field "PENNY_STOCK_PRICE" (generated)
    public static BigDecimal get__PENNY_STOCK_PRICE() {
        // call getter service and get encoded response from response JSON (for collection return type)
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("get__PENNY_STOCK_PRICE")
                .request(MediaType.APPLICATION_JSON)
                .method(javax.ws.rs.HttpMethod.GET, Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("TradeConfig] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeConfig] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        
        // convert reference ID(s) stored in "response" to physical/proxy object(s)
        BigDecimal response_obj = (BigDecimal)SerializationUtil.decodeWithDynamicTypeCheck(response);
        return response_obj;

    }



    // setter method for field "PENNY_STOCK_PRICE" (generated)
    public static void set__PENNY_STOCK_PRICE(BigDecimal PENNY_STOCK_PRICE) {
        // create form for service request
        Form form = new Form();
        
        // convert physical/proxy object(s) referenced by "PENNY_STOCK_PRICE" to reference ID(s)
        String PENNY_STOCK_PRICE_fpar = SerializationUtil.encodeWithDynamicTypeCheck(PENNY_STOCK_PRICE);
        form.param("PENNY_STOCK_PRICE", PENNY_STOCK_PRICE_fpar);


        logger.info("[TradeConfig] Calling service "+serviceURI+
            "/set__PENNY_STOCK_PRICE with form: "+form.asMap());

        // call setter service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("set__PENNY_STOCK_PRICE")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            throw (RuntimeException)cause;
        }
    }
    // getter method for field "PENNY_STOCK_RECOVERY_MIRACLE_MULTIPLIER" (generated)
    public static BigDecimal get__PENNY_STOCK_RECOVERY_MIRACLE_MULTIPLIER() {
        // call getter service and get encoded response from response JSON (for collection return type)
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("get__PENNY_STOCK_RECOVERY_MIRACLE_MULTIPLIER")
                .request(MediaType.APPLICATION_JSON)
                .method(javax.ws.rs.HttpMethod.GET, Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("TradeConfig] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeConfig] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        
        // convert reference ID(s) stored in "response" to physical/proxy object(s)
        BigDecimal response_obj = (BigDecimal)SerializationUtil.decodeWithDynamicTypeCheck(response);
        return response_obj;

    }



    // setter method for field "PENNY_STOCK_RECOVERY_MIRACLE_MULTIPLIER" (generated)
    public static void set__PENNY_STOCK_RECOVERY_MIRACLE_MULTIPLIER(BigDecimal PENNY_STOCK_RECOVERY_MIRACLE_MULTIPLIER) {
        // create form for service request
        Form form = new Form();
        
        // convert physical/proxy object(s) referenced by "PENNY_STOCK_RECOVERY_MIRACLE_MULTIPLIER" to reference ID(s)
        String PENNY_STOCK_RECOVERY_MIRACLE_MULTIPLIER_fpar = SerializationUtil.encodeWithDynamicTypeCheck(PENNY_STOCK_RECOVERY_MIRACLE_MULTIPLIER);
        form.param("PENNY_STOCK_RECOVERY_MIRACLE_MULTIPLIER", PENNY_STOCK_RECOVERY_MIRACLE_MULTIPLIER_fpar);


        logger.info("[TradeConfig] Calling service "+serviceURI+
            "/set__PENNY_STOCK_RECOVERY_MIRACLE_MULTIPLIER with form: "+form.asMap());

        // call setter service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("set__PENNY_STOCK_RECOVERY_MIRACLE_MULTIPLIER")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            throw (RuntimeException)cause;
        }
    }
    // getter method for field "MAXIMUM_STOCK_PRICE" (generated)
    public static BigDecimal get__MAXIMUM_STOCK_PRICE() {
        // call getter service and get encoded response from response JSON (for collection return type)
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("get__MAXIMUM_STOCK_PRICE")
                .request(MediaType.APPLICATION_JSON)
                .method(javax.ws.rs.HttpMethod.GET, Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("TradeConfig] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeConfig] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        
        // convert reference ID(s) stored in "response" to physical/proxy object(s)
        BigDecimal response_obj = (BigDecimal)SerializationUtil.decodeWithDynamicTypeCheck(response);
        return response_obj;

    }



    // setter method for field "MAXIMUM_STOCK_PRICE" (generated)
    public static void set__MAXIMUM_STOCK_PRICE(BigDecimal MAXIMUM_STOCK_PRICE) {
        // create form for service request
        Form form = new Form();
        
        // convert physical/proxy object(s) referenced by "MAXIMUM_STOCK_PRICE" to reference ID(s)
        String MAXIMUM_STOCK_PRICE_fpar = SerializationUtil.encodeWithDynamicTypeCheck(MAXIMUM_STOCK_PRICE);
        form.param("MAXIMUM_STOCK_PRICE", MAXIMUM_STOCK_PRICE_fpar);


        logger.info("[TradeConfig] Calling service "+serviceURI+
            "/set__MAXIMUM_STOCK_PRICE with form: "+form.asMap());

        // call setter service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("set__MAXIMUM_STOCK_PRICE")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            throw (RuntimeException)cause;
        }
    }
    // getter method for field "MAXIMUM_STOCK_SPLIT_MULTIPLIER" (generated)
    public static BigDecimal get__MAXIMUM_STOCK_SPLIT_MULTIPLIER() {
        // call getter service and get encoded response from response JSON (for collection return type)
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("get__MAXIMUM_STOCK_SPLIT_MULTIPLIER")
                .request(MediaType.APPLICATION_JSON)
                .method(javax.ws.rs.HttpMethod.GET, Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("TradeConfig] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeConfig] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        
        // convert reference ID(s) stored in "response" to physical/proxy object(s)
        BigDecimal response_obj = (BigDecimal)SerializationUtil.decodeWithDynamicTypeCheck(response);
        return response_obj;

    }



    // setter method for field "MAXIMUM_STOCK_SPLIT_MULTIPLIER" (generated)
    public static void set__MAXIMUM_STOCK_SPLIT_MULTIPLIER(BigDecimal MAXIMUM_STOCK_SPLIT_MULTIPLIER) {
        // create form for service request
        Form form = new Form();
        
        // convert physical/proxy object(s) referenced by "MAXIMUM_STOCK_SPLIT_MULTIPLIER" to reference ID(s)
        String MAXIMUM_STOCK_SPLIT_MULTIPLIER_fpar = SerializationUtil.encodeWithDynamicTypeCheck(MAXIMUM_STOCK_SPLIT_MULTIPLIER);
        form.param("MAXIMUM_STOCK_SPLIT_MULTIPLIER", MAXIMUM_STOCK_SPLIT_MULTIPLIER_fpar);


        logger.info("[TradeConfig] Calling service "+serviceURI+
            "/set__MAXIMUM_STOCK_SPLIT_MULTIPLIER with form: "+form.asMap());

        // call setter service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("set__MAXIMUM_STOCK_SPLIT_MULTIPLIER")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            throw (RuntimeException)cause;
        }
    }
    // getter method for field "runTimeModeNames" (generated)
    public static String[] get__runTimeModeNames() {
        // call getter service and get encoded response from response JSON (for collection return type)
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("get__runTimeModeNames")
                .request(MediaType.APPLICATION_JSON)
                .method(javax.ws.rs.HttpMethod.GET, Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("TradeConfig] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeConfig] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        
        // deserialize string to primitive array
        String[] response_obj = (String[])SerializationUtil.decode(response);
        return response_obj;

    }



    // setter method for field "runTimeModeNames" (generated)
    public static void set__runTimeModeNames(String[] runTimeModeNames) {
        // create form for service request
        Form form = new Form();
        
        // serialize primitive array to string
        String runTimeModeNames_fpar = SerializationUtil.encode(runTimeModeNames, null);
        form.param("runTimeModeNames", runTimeModeNames_fpar);


        logger.info("[TradeConfig] Calling service "+serviceURI+
            "/set__runTimeModeNames with form: "+form.asMap());

        // call setter service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("set__runTimeModeNames")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            throw (RuntimeException)cause;
        }
    }
    // getter method for field "runTimeMode" (generated)
    public static int get__runTimeMode() {
        // call getter service and get encoded response from response JSON (for collection return type)
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("get__runTimeMode")
                .request(MediaType.APPLICATION_JSON)
                .method(javax.ws.rs.HttpMethod.GET, Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("TradeConfig] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeConfig] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return Integer.parseInt(response);
    }



    // setter method for field "runTimeMode" (generated)
    public static void set__runTimeMode(int runTimeMode) {
        // create form for service request
        Form form = new Form();
        form.param("runTimeMode", String.valueOf(runTimeMode)); 
        logger.info("[TradeConfig] Calling service "+serviceURI+
            "/set__runTimeMode with form: "+form.asMap());

        // call setter service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("set__runTimeMode")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            throw (RuntimeException)cause;
        }
    }
    // getter method for field "orderProcessingModeNames" (generated)
    public static String[] get__orderProcessingModeNames() {
        // call getter service and get encoded response from response JSON (for collection return type)
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("get__orderProcessingModeNames")
                .request(MediaType.APPLICATION_JSON)
                .method(javax.ws.rs.HttpMethod.GET, Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("TradeConfig] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeConfig] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        
        // deserialize string to primitive array
        String[] response_obj = (String[])SerializationUtil.decode(response);
        return response_obj;

    }



    // setter method for field "orderProcessingModeNames" (generated)
    public static void set__orderProcessingModeNames(String[] orderProcessingModeNames) {
        // create form for service request
        Form form = new Form();
        
        // serialize primitive array to string
        String orderProcessingModeNames_fpar = SerializationUtil.encode(orderProcessingModeNames, null);
        form.param("orderProcessingModeNames", orderProcessingModeNames_fpar);


        logger.info("[TradeConfig] Calling service "+serviceURI+
            "/set__orderProcessingModeNames with form: "+form.asMap());

        // call setter service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("set__orderProcessingModeNames")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            throw (RuntimeException)cause;
        }
    }
    // getter method for field "orderProcessingMode" (generated)
    public static int get__orderProcessingMode() {
        // call getter service and get encoded response from response JSON (for collection return type)
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("get__orderProcessingMode")
                .request(MediaType.APPLICATION_JSON)
                .method(javax.ws.rs.HttpMethod.GET, Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("TradeConfig] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeConfig] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return Integer.parseInt(response);
    }



    // setter method for field "orderProcessingMode" (generated)
    public static void set__orderProcessingMode(int orderProcessingMode) {
        // create form for service request
        Form form = new Form();
        form.param("orderProcessingMode", String.valueOf(orderProcessingMode)); 
        logger.info("[TradeConfig] Calling service "+serviceURI+
            "/set__orderProcessingMode with form: "+form.asMap());

        // call setter service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("set__orderProcessingMode")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            throw (RuntimeException)cause;
        }
    }
    // getter method for field "accessModeNames" (generated)
    public static String[] get__accessModeNames() {
        // call getter service and get encoded response from response JSON (for collection return type)
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("get__accessModeNames")
                .request(MediaType.APPLICATION_JSON)
                .method(javax.ws.rs.HttpMethod.GET, Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("TradeConfig] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeConfig] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        
        // deserialize string to primitive array
        String[] response_obj = (String[])SerializationUtil.decode(response);
        return response_obj;

    }



    // setter method for field "accessModeNames" (generated)
    public static void set__accessModeNames(String[] accessModeNames) {
        // create form for service request
        Form form = new Form();
        
        // serialize primitive array to string
        String accessModeNames_fpar = SerializationUtil.encode(accessModeNames, null);
        form.param("accessModeNames", accessModeNames_fpar);


        logger.info("[TradeConfig] Calling service "+serviceURI+
            "/set__accessModeNames with form: "+form.asMap());

        // call setter service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("set__accessModeNames")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            throw (RuntimeException)cause;
        }
    }
    // getter method for field "webInterfaceNames" (generated)
    public static String[] get__webInterfaceNames() {
        // call getter service and get encoded response from response JSON (for collection return type)
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("get__webInterfaceNames")
                .request(MediaType.APPLICATION_JSON)
                .method(javax.ws.rs.HttpMethod.GET, Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("TradeConfig] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeConfig] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        
        // deserialize string to primitive array
        String[] response_obj = (String[])SerializationUtil.decode(response);
        return response_obj;

    }



    // setter method for field "webInterfaceNames" (generated)
    public static void set__webInterfaceNames(String[] webInterfaceNames) {
        // create form for service request
        Form form = new Form();
        
        // serialize primitive array to string
        String webInterfaceNames_fpar = SerializationUtil.encode(webInterfaceNames, null);
        form.param("webInterfaceNames", webInterfaceNames_fpar);


        logger.info("[TradeConfig] Calling service "+serviceURI+
            "/set__webInterfaceNames with form: "+form.asMap());

        // call setter service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("set__webInterfaceNames")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            throw (RuntimeException)cause;
        }
    }
    // getter method for field "webInterface" (generated)
    public static int get__webInterface() {
        // call getter service and get encoded response from response JSON (for collection return type)
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("get__webInterface")
                .request(MediaType.APPLICATION_JSON)
                .method(javax.ws.rs.HttpMethod.GET, Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("TradeConfig] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeConfig] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return Integer.parseInt(response);
    }



    // setter method for field "webInterface" (generated)
    public static void set__webInterface(int webInterface) {
        // create form for service request
        Form form = new Form();
        form.param("webInterface", String.valueOf(webInterface)); 
        logger.info("[TradeConfig] Calling service "+serviceURI+
            "/set__webInterface with form: "+form.asMap());

        // call setter service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("set__webInterface")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            throw (RuntimeException)cause;
        }
    }
    // getter method for field "JDBC_UID" (generated)
    public static String get__JDBC_UID() {
        // call getter service and get encoded response from response JSON (for collection return type)
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("get__JDBC_UID")
                .request(MediaType.APPLICATION_JSON)
                .method(javax.ws.rs.HttpMethod.GET, Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("TradeConfig] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeConfig] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return response;
    }



    // setter method for field "JDBC_UID" (generated)
    public static void set__JDBC_UID(String JDBC_UID) {
        // create form for service request
        Form form = new Form();
        form.param("JDBC_UID", JDBC_UID); 
        logger.info("[TradeConfig] Calling service "+serviceURI+
            "/set__JDBC_UID with form: "+form.asMap());

        // call setter service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("set__JDBC_UID")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            throw (RuntimeException)cause;
        }
    }
    // getter method for field "JDBC_PWD" (generated)
    public static String get__JDBC_PWD() {
        // call getter service and get encoded response from response JSON (for collection return type)
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("get__JDBC_PWD")
                .request(MediaType.APPLICATION_JSON)
                .method(javax.ws.rs.HttpMethod.GET, Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("TradeConfig] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeConfig] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return response;
    }



    // setter method for field "JDBC_PWD" (generated)
    public static void set__JDBC_PWD(String JDBC_PWD) {
        // create form for service request
        Form form = new Form();
        form.param("JDBC_PWD", JDBC_PWD); 
        logger.info("[TradeConfig] Calling service "+serviceURI+
            "/set__JDBC_PWD with form: "+form.asMap());

        // call setter service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("set__JDBC_PWD")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            throw (RuntimeException)cause;
        }
    }
    // getter method for field "DS_NAME" (generated)
    public static String get__DS_NAME() {
        // call getter service and get encoded response from response JSON (for collection return type)
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("get__DS_NAME")
                .request(MediaType.APPLICATION_JSON)
                .method(javax.ws.rs.HttpMethod.GET, Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("TradeConfig] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeConfig] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return response;
    }



    // setter method for field "DS_NAME" (generated)
    public static void set__DS_NAME(String DS_NAME) {
        // create form for service request
        Form form = new Form();
        form.param("DS_NAME", DS_NAME); 
        logger.info("[TradeConfig] Calling service "+serviceURI+
            "/set__DS_NAME with form: "+form.asMap());

        // call setter service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("set__DS_NAME")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            throw (RuntimeException)cause;
        }
    }
    // getter method for field "JDBCDriverNeedsGlobalTransation" (generated)
    public static boolean get__JDBCDriverNeedsGlobalTransation() {
        // call getter service and get encoded response from response JSON (for collection return type)
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("get__JDBCDriverNeedsGlobalTransation")
                .request(MediaType.APPLICATION_JSON)
                .method(javax.ws.rs.HttpMethod.GET, Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("TradeConfig] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeConfig] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return Boolean.parseBoolean(response);
    }



    // setter method for field "JDBCDriverNeedsGlobalTransation" (generated)
    public static void set__JDBCDriverNeedsGlobalTransation(boolean JDBCDriverNeedsGlobalTransation) {
        // create form for service request
        Form form = new Form();
        form.param("JDBCDriverNeedsGlobalTransation", String.valueOf(JDBCDriverNeedsGlobalTransation)); 
        logger.info("[TradeConfig] Calling service "+serviceURI+
            "/set__JDBCDriverNeedsGlobalTransation with form: "+form.asMap());

        // call setter service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("set__JDBCDriverNeedsGlobalTransation")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            throw (RuntimeException)cause;
        }
    }
    // getter method for field "DATASOURCE" (generated)
    public static String get__DATASOURCE() {
        // call getter service and get encoded response from response JSON (for collection return type)
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("get__DATASOURCE")
                .request(MediaType.APPLICATION_JSON)
                .method(javax.ws.rs.HttpMethod.GET, Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("TradeConfig] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeConfig] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return response;
    }



    // setter method for field "DATASOURCE" (generated)
    public static void set__DATASOURCE(String DATASOURCE) {
        // create form for service request
        Form form = new Form();
        form.param("DATASOURCE", DATASOURCE); 
        logger.info("[TradeConfig] Calling service "+serviceURI+
            "/set__DATASOURCE with form: "+form.asMap());

        // call setter service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("set__DATASOURCE")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            throw (RuntimeException)cause;
        }
    }
    // getter method for field "KEYBLOCKSIZE" (generated)
    public static int get__KEYBLOCKSIZE() {
        // call getter service and get encoded response from response JSON (for collection return type)
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("get__KEYBLOCKSIZE")
                .request(MediaType.APPLICATION_JSON)
                .method(javax.ws.rs.HttpMethod.GET, Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("TradeConfig] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeConfig] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return Integer.parseInt(response);
    }



    // setter method for field "KEYBLOCKSIZE" (generated)
    public static void set__KEYBLOCKSIZE(int KEYBLOCKSIZE) {
        // create form for service request
        Form form = new Form();
        form.param("KEYBLOCKSIZE", String.valueOf(KEYBLOCKSIZE)); 
        logger.info("[TradeConfig] Calling service "+serviceURI+
            "/set__KEYBLOCKSIZE with form: "+form.asMap());

        // call setter service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("set__KEYBLOCKSIZE")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            throw (RuntimeException)cause;
        }
    }
    // getter method for field "QUOTES_PER_PAGE" (generated)
    public static int get__QUOTES_PER_PAGE() {
        // call getter service and get encoded response from response JSON (for collection return type)
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("get__QUOTES_PER_PAGE")
                .request(MediaType.APPLICATION_JSON)
                .method(javax.ws.rs.HttpMethod.GET, Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("TradeConfig] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeConfig] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return Integer.parseInt(response);
    }



    // setter method for field "QUOTES_PER_PAGE" (generated)
    public static void set__QUOTES_PER_PAGE(int QUOTES_PER_PAGE) {
        // create form for service request
        Form form = new Form();
        form.param("QUOTES_PER_PAGE", String.valueOf(QUOTES_PER_PAGE)); 
        logger.info("[TradeConfig] Calling service "+serviceURI+
            "/set__QUOTES_PER_PAGE with form: "+form.asMap());

        // call setter service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("set__QUOTES_PER_PAGE")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            throw (RuntimeException)cause;
        }
    }
    // getter method for field "RND_USER" (generated)
    public static boolean get__RND_USER() {
        // call getter service and get encoded response from response JSON (for collection return type)
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("get__RND_USER")
                .request(MediaType.APPLICATION_JSON)
                .method(javax.ws.rs.HttpMethod.GET, Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("TradeConfig] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeConfig] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return Boolean.parseBoolean(response);
    }



    // setter method for field "RND_USER" (generated)
    public static void set__RND_USER(boolean RND_USER) {
        // create form for service request
        Form form = new Form();
        form.param("RND_USER", String.valueOf(RND_USER)); 
        logger.info("[TradeConfig] Calling service "+serviceURI+
            "/set__RND_USER with form: "+form.asMap());

        // call setter service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("set__RND_USER")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            throw (RuntimeException)cause;
        }
    }
    // getter method for field "webUI" (generated)
    public static String[][] get__webUI() {
        // call getter service and get encoded response from response JSON (for collection return type)
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("get__webUI")
                .request(MediaType.APPLICATION_JSON)
                .method(javax.ws.rs.HttpMethod.GET, Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("TradeConfig] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeConfig] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        
        // deserialize string to primitive array
        String[][] response_obj = (String[][])SerializationUtil.decode(response);
        return response_obj;

    }



    // setter method for field "webUI" (generated)
    public static void set__webUI(String[][] webUI) {
        // create form for service request
        Form form = new Form();
        
        // serialize primitive array to string
        String webUI_fpar = SerializationUtil.encode(webUI, null);
        form.param("webUI", webUI_fpar);


        logger.info("[TradeConfig] Calling service "+serviceURI+
            "/set__webUI with form: "+form.asMap());

        // call setter service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("set__webUI")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            throw (RuntimeException)cause;
        }
    }



    /**
     * Return a Trade UI Web page based on the current configuration This may
     * return a JSP page or a Servlet page Creation date: (3/14/2000 9:08:34 PM)
     */

    public static String getPage(int pageNumber) {
        // create form for service request
        Form form = new Form();
        form.param("pageNumber", String.valueOf(pageNumber)); 

        logger.info("[TradeConfig] Calling service "+serviceURI+
            "/getPage with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("getPage")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeConfig] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeConfig] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return response;
    }

    /**
     * Return the list of run time mode names Creation date: (3/8/2000 5:58:34
     * PM)
     *
     * @return java.lang.String[]
     */
    public static java.lang.String[] getRunTimeModeNames() {
        // create form for service request
        Form form = new Form();
        logger.info("[TradeConfig] Calling service "+serviceURI+
            "/getRunTimeModeNames with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("getRunTimeModeNames")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeConfig] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeConfig] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        
        // convert reference ID(s) stored in "response" to physical/proxy object(s)
        java.lang.String[] response_obj = (java.lang.String[])SerializationUtil.decodeWithDynamicTypeCheck(response);
        return response_obj;

    }

    /**
     * Return a Trade Scenario Operation based on the setting of the current mix
     * (TradeScenarioMix) Creation date: (2/10/2000 9:08:34 PM)
     */

    public static char getScenarioAction(boolean newUser) {
        // create form for service request
        Form form = new Form();
        form.param("newUser", String.valueOf(newUser)); 

        logger.info("[TradeConfig] Calling service "+serviceURI+
            "/getScenarioAction with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("getScenarioAction")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeConfig] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeConfig] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return response.charAt(0);
    }

    public static String getUserID() {
        // create form for service request
        Form form = new Form();
        logger.info("[TradeConfig] Calling service "+serviceURI+
            "/getUserID with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("getUserID")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeConfig] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeConfig] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return response;
    }

    public static BigDecimal getOrderFee(String orderType) {
        // create form for service request
        Form form = new Form();
        form.param("orderType", orderType); 

        logger.info("[TradeConfig] Calling service "+serviceURI+
            "/getOrderFee with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("getOrderFee")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeConfig] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeConfig] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        
        // convert reference ID(s) stored in "response" to physical/proxy object(s)
        BigDecimal response_obj = (BigDecimal)SerializationUtil.decodeWithDynamicTypeCheck(response);
        return response_obj;

    }

    /**
     * Increment the sell deficit counter Creation date: (6/21/2000 11:33:45 AM)
     */
    public static synchronized void incrementSellDeficit() {
        // create form for service request
        Form form = new Form();
        logger.info("[TradeConfig] Calling service "+serviceURI+
            "/incrementSellDeficit with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("incrementSellDeficit")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeConfig] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }

    }

    public static String nextUserID() {
        // create form for service request
        Form form = new Form();
        logger.info("[TradeConfig] Calling service "+serviceURI+
            "/nextUserID with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("nextUserID")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeConfig] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeConfig] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return response;
    }

    public static double random() {
        // create form for service request
        Form form = new Form();
        logger.info("[TradeConfig] Calling service "+serviceURI+
            "/random with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("random")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeConfig] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeConfig] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return Double.parseDouble(response);
    }

    public static String rndAddress() {
        // create form for service request
        Form form = new Form();
        logger.info("[TradeConfig] Calling service "+serviceURI+
            "/rndAddress with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("rndAddress")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeConfig] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeConfig] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return response;
    }

    public static String rndBalance() {
        // create form for service request
        Form form = new Form();
        logger.info("[TradeConfig] Calling service "+serviceURI+
            "/rndBalance with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("rndBalance")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeConfig] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeConfig] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return response;
    }

    public static String rndCreditCard() {
        // create form for service request
        Form form = new Form();
        logger.info("[TradeConfig] Calling service "+serviceURI+
            "/rndCreditCard with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("rndCreditCard")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeConfig] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeConfig] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return response;
    }

    public static String rndEmail(String userID) {
        // create form for service request
        Form form = new Form();
        form.param("userID", userID); 

        logger.info("[TradeConfig] Calling service "+serviceURI+
            "/rndEmail with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("rndEmail")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeConfig] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeConfig] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return response;
    }

    public static String rndFullName() {
        // create form for service request
        Form form = new Form();
        logger.info("[TradeConfig] Calling service "+serviceURI+
            "/rndFullName with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("rndFullName")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeConfig] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeConfig] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return response;
    }

    public static int rndInt(int i) {
        // create form for service request
        Form form = new Form();
        form.param("i", String.valueOf(i)); 

        logger.info("[TradeConfig] Calling service "+serviceURI+
            "/rndInt with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("rndInt")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeConfig] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeConfig] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return Integer.parseInt(response);
    }

    public static float rndFloat(int i) {
        // create form for service request
        Form form = new Form();
        form.param("i", String.valueOf(i)); 

        logger.info("[TradeConfig] Calling service "+serviceURI+
            "/rndFloat with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("rndFloat")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeConfig] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeConfig] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return Float.parseFloat(response);
    }

    public static BigDecimal rndBigDecimal(float f) {
        // create form for service request
        Form form = new Form();
        form.param("f", String.valueOf(f)); 

        logger.info("[TradeConfig] Calling service "+serviceURI+
            "/rndBigDecimal with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("rndBigDecimal")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeConfig] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeConfig] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        
        // convert reference ID(s) stored in "response" to physical/proxy object(s)
        BigDecimal response_obj = (BigDecimal)SerializationUtil.decodeWithDynamicTypeCheck(response);
        return response_obj;

    }

    public static boolean rndBoolean() {
        // create form for service request
        Form form = new Form();
        logger.info("[TradeConfig] Calling service "+serviceURI+
            "/rndBoolean with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("rndBoolean")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeConfig] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeConfig] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return Boolean.parseBoolean(response);
    }

    /**
     * Returns a new Trade user Creation date: (2/16/2000 8:50:35 PM)
     */
    public static synchronized String rndNewUserID() {
        // create form for service request
        Form form = new Form();
        logger.info("[TradeConfig] Calling service "+serviceURI+
            "/rndNewUserID with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("rndNewUserID")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeConfig] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeConfig] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return response;
    }

    public static float rndPrice() {
        // create form for service request
        Form form = new Form();
        logger.info("[TradeConfig] Calling service "+serviceURI+
            "/rndPrice with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("rndPrice")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeConfig] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeConfig] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return Float.parseFloat(response);
    }
	
    public static BigDecimal getRandomPriceChangeFactor() {
        // create form for service request
        Form form = new Form();
        logger.info("[TradeConfig] Calling service "+serviceURI+
            "/getRandomPriceChangeFactor with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("getRandomPriceChangeFactor")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeConfig] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeConfig] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        
        // convert reference ID(s) stored in "response" to physical/proxy object(s)
        BigDecimal response_obj = (BigDecimal)SerializationUtil.decodeWithDynamicTypeCheck(response);
        return response_obj;

    }

    public static float rndQuantity() {
        // create form for service request
        Form form = new Form();
        logger.info("[TradeConfig] Calling service "+serviceURI+
            "/rndQuantity with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("rndQuantity")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeConfig] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeConfig] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return Float.parseFloat(response);
    }

    public static String rndSymbol() {
        // create form for service request
        Form form = new Form();
        logger.info("[TradeConfig] Calling service "+serviceURI+
            "/rndSymbol with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("rndSymbol")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeConfig] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeConfig] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return response;
    }

    public static String rndSymbols() {
        // create form for service request
        Form form = new Form();
        logger.info("[TradeConfig] Calling service "+serviceURI+
            "/rndSymbols with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("rndSymbols")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeConfig] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeConfig] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return response;
    }

    public static String rndUserID() {
        // create form for service request
        Form form = new Form();
        logger.info("[TradeConfig] Calling service "+serviceURI+
            "/rndUserID with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("rndUserID")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeConfig] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeConfig] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return response;
    }
	
    /**
     * Set the list of run time mode names Creation date: (3/8/2000 5:58:34 PM)
     *
     * @param newRunTimeModeNames
     *            java.lang.String[]
     */
    public static void setRunTimeModeNames(java.lang.String[] newRunTimeModeNames) {
        // create form for service request
        Form form = new Form();
        
        // convert physical/proxy object(s) referenced by "newRunTimeModeNames" to reference ID(s)
        String newRunTimeModeNames_fpar = SerializationUtil.encodeWithDynamicTypeCheck(newRunTimeModeNames);
        form.param("newRunTimeModeNames", newRunTimeModeNames_fpar);

        logger.info("[TradeConfig] Calling service "+serviceURI+
            "/setRunTimeModeNames with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("setRunTimeModeNames")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeConfig] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }

    }

    /**
     * This is a convenience method for servlets to set Trade configuration
     * parameters from servlet initialization parameters. The servlet provides
     * the init param and its value as strings. This method then parses the
     * parameter, converts the value to the correct type and sets the
     * corresponding TradeConfig parameter to the converted value
     *
     */
    public static void setConfigParam(String parm, String value) {
        // create form for service request
        Form form = new Form();
        form.param("parm", parm); 

        form.param("value", value); 

        logger.info("[TradeConfig] Calling service "+serviceURI+
            "/setConfigParam with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("setConfigParam")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeConfig] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }

    }

    /**
     * Gets the orderProcessingModeNames
     *
     * @return Returns a String[]
     */
    public static String[] getOrderProcessingModeNames() {
        // create form for service request
        Form form = new Form();
        logger.info("[TradeConfig] Calling service "+serviceURI+
            "/getOrderProcessingModeNames with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("getOrderProcessingModeNames")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeConfig] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeConfig] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        
        // deserialize string to primitive array
        String[] response_obj = (String[])SerializationUtil.decode(response);
        return response_obj;

    }

    /**
     * Gets the webInterfaceNames
     *
     * @return Returns a String[]
     */
    public static String[] getWebInterfaceNames() {
        // create form for service request
        Form form = new Form();
        logger.info("[TradeConfig] Calling service "+serviceURI+
            "/getWebInterfaceNames with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("getWebInterfaceNames")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeConfig] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeConfig] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        
        // deserialize string to primitive array
        String[] response_obj = (String[])SerializationUtil.decode(response);
        return response_obj;

    }

    /**
     * Gets the webInterfaceNames
     *
     * @return Returns a String[]
     */
    /*public static String[] getCachingTypeNames() {
        return cachingTypeNames;
    }*/

    /**
     * Gets the scenarioMixes
     *
     * @return Returns a int[][]
     */
    public static int[][] getScenarioMixes() {
        // create form for service request
        Form form = new Form();
        logger.info("[TradeConfig] Calling service "+serviceURI+
            "/getScenarioMixes with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("getScenarioMixes")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeConfig] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeConfig] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        
        // deserialize string to primitive array
        int[][] response_obj = (int[][])SerializationUtil.decode(response);
        return response_obj;

    }

    /**
     * Gets the trace
     *
     * @return Returns a boolean
     */
    public static boolean getTrace() {
        // create form for service request
        Form form = new Form();
        logger.info("[TradeConfig] Calling service "+serviceURI+
            "/getTrace with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("getTrace")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeConfig] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeConfig] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return Boolean.parseBoolean(response);
    }

    /**
     * Sets the trace
     *
     * @param trace
     *            The trace to set
     */
    public static void setTrace(boolean traceValue) {
        // create form for service request
        Form form = new Form();
        form.param("traceValue", String.valueOf(traceValue)); 

        logger.info("[TradeConfig] Calling service "+serviceURI+
            "/setTrace with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("setTrace")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeConfig] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }

    }

    /**
     * Gets the mAX_USERS.
     *
     * @return Returns a int
     */
    public static int getMAX_USERS() {
        // create form for service request
        Form form = new Form();
        logger.info("[TradeConfig] Calling service "+serviceURI+
            "/getMAX_USERS with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("getMAX_USERS")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeConfig] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeConfig] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return Integer.parseInt(response);
    }

    /**
     * Sets the mAX_USERS.
     *
     * @param mAX_USERS
     *            The mAX_USERS to set
     */
    public static void setMAX_USERS(int mAX_USERS) {
        // create form for service request
        Form form = new Form();
        form.param("mAX_USERS", String.valueOf(mAX_USERS)); 

        logger.info("[TradeConfig] Calling service "+serviceURI+
            "/setMAX_USERS with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("setMAX_USERS")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeConfig] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }

    }

    /**
     * Gets the mAX_QUOTES.
     *
     * @return Returns a int
     */
    public static int getMAX_QUOTES() {
        // create form for service request
        Form form = new Form();
        logger.info("[TradeConfig] Calling service "+serviceURI+
            "/getMAX_QUOTES with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("getMAX_QUOTES")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeConfig] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeConfig] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return Integer.parseInt(response);
    }

    /**
     * Sets the mAX_QUOTES.
     *
     * @param mAX_QUOTES
     *            The mAX_QUOTES to set
     */
    public static void setMAX_QUOTES(int mAX_QUOTES) {
        // create form for service request
        Form form = new Form();
        form.param("mAX_QUOTES", String.valueOf(mAX_QUOTES)); 

        logger.info("[TradeConfig] Calling service "+serviceURI+
            "/setMAX_QUOTES with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("setMAX_QUOTES")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeConfig] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }

    }

    /**
     * Gets the mAX_HOLDINGS.
     *
     * @return Returns a int
     */
    public static int getMAX_HOLDINGS() {
        // create form for service request
        Form form = new Form();
        logger.info("[TradeConfig] Calling service "+serviceURI+
            "/getMAX_HOLDINGS with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("getMAX_HOLDINGS")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeConfig] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeConfig] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return Integer.parseInt(response);
    }

    /**
     * Sets the mAX_HOLDINGS.
     *
     * @param mAX_HOLDINGS
     *            The mAX_HOLDINGS to set
     */
    public static void setMAX_HOLDINGS(int mAX_HOLDINGS) {
        // create form for service request
        Form form = new Form();
        form.param("mAX_HOLDINGS", String.valueOf(mAX_HOLDINGS)); 

        logger.info("[TradeConfig] Calling service "+serviceURI+
            "/setMAX_HOLDINGS with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("setMAX_HOLDINGS")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeConfig] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }

    }

    /**
     * Gets the actionTrace.
     *
     * @return Returns a boolean
     */
    public static boolean getActionTrace() {
        // create form for service request
        Form form = new Form();
        logger.info("[TradeConfig] Calling service "+serviceURI+
            "/getActionTrace with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("getActionTrace")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeConfig] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeConfig] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return Boolean.parseBoolean(response);
    }

    /**
     * Sets the actionTrace.
     *
     * @param actionTrace
     *            The actionTrace to set
     */
    public static void setActionTrace(boolean actionTrace) {
        // create form for service request
        Form form = new Form();
        form.param("actionTrace", String.valueOf(actionTrace)); 

        logger.info("[TradeConfig] Calling service "+serviceURI+
            "/setActionTrace with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("setActionTrace")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeConfig] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }

    }

    /**
     * Gets the scenarioCount.
     *
     * @return Returns a int
     */
    public static int getScenarioCount() {
        // create form for service request
        Form form = new Form();
        logger.info("[TradeConfig] Calling service "+serviceURI+
            "/getScenarioCount with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("getScenarioCount")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeConfig] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeConfig] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return Integer.parseInt(response);
    }

    /**
     * Sets the scenarioCount.
     *
     * @param scenarioCount
     *            The scenarioCount to set
     */
    public static void setScenarioCount(int scenarioCount) {
        // create form for service request
        Form form = new Form();
        form.param("scenarioCount", String.valueOf(scenarioCount)); 

        logger.info("[TradeConfig] Calling service "+serviceURI+
            "/setScenarioCount with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("setScenarioCount")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeConfig] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }

    }

    public static synchronized void incrementScenarioCount() {
        // create form for service request
        Form form = new Form();
        logger.info("[TradeConfig] Calling service "+serviceURI+
            "/incrementScenarioCount with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("incrementScenarioCount")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeConfig] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }

    }

    /**
     * Gets the jdbc driver needs global transaction Some XA Drivers require a
     * global transaction to be started for all SQL calls. To work around this,
     * set this to true to cause the direct mode to start a user transaction.
     *
     * @return Returns a boolean
     */
    public static boolean getJDBCDriverNeedsGlobalTransation() {
        // create form for service request
        Form form = new Form();
        logger.info("[TradeConfig] Calling service "+serviceURI+
            "/getJDBCDriverNeedsGlobalTransation with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("getJDBCDriverNeedsGlobalTransation")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeConfig] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeConfig] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return Boolean.parseBoolean(response);
    }

    /**
     * Sets the jdbc driver needs global transaction
     *
     * @param JDBCDriverNeedsGlobalTransationVal
     *            the value
     */
    public static void setJDBCDriverNeedsGlobalTransation(boolean JDBCDriverNeedsGlobalTransationVal) {
        // create form for service request
        Form form = new Form();
        form.param("JDBCDriverNeedsGlobalTransationVal", String.valueOf(JDBCDriverNeedsGlobalTransationVal)); 

        logger.info("[TradeConfig] Calling service "+serviceURI+
            "/setJDBCDriverNeedsGlobalTransation with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("setJDBCDriverNeedsGlobalTransation")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeConfig] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }

    }

    /**
     * Gets the updateQuotePrices.
     *
     * @return Returns a boolean
     */
    public static boolean getUpdateQuotePrices() {
        // create form for service request
        Form form = new Form();
        logger.info("[TradeConfig] Calling service "+serviceURI+
            "/getUpdateQuotePrices with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("getUpdateQuotePrices")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeConfig] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeConfig] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return Boolean.parseBoolean(response);
    }

    /**
     * Sets the updateQuotePrices.
     *
     * @param updateQuotePrices
     *            The updateQuotePrices to set
     */
    public static void setUpdateQuotePrices(boolean updateQuotePrices) {
        // create form for service request
        Form form = new Form();
        form.param("updateQuotePrices", String.valueOf(updateQuotePrices)); 

        logger.info("[TradeConfig] Calling service "+serviceURI+
            "/setUpdateQuotePrices with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("setUpdateQuotePrices")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeConfig] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }

    }

    public static int getPrimIterations() {
        // create form for service request
        Form form = new Form();
        logger.info("[TradeConfig] Calling service "+serviceURI+
            "/getPrimIterations with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("getPrimIterations")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeConfig] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeConfig] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return Integer.parseInt(response);
    }

    public static void setPrimIterations(int iter) {
        // create form for service request
        Form form = new Form();
        form.param("iter", String.valueOf(iter)); 

        logger.info("[TradeConfig] Calling service "+serviceURI+
            "/setPrimIterations with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("setPrimIterations")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeConfig] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }

    }

    public static boolean getLongRun() {
        // create form for service request
        Form form = new Form();
        logger.info("[TradeConfig] Calling service "+serviceURI+
            "/getLongRun with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("getLongRun")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeConfig] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeConfig] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return Boolean.parseBoolean(response);
    }

    public static void setLongRun(boolean longRun) {
        // create form for service request
        Form form = new Form();
        form.param("longRun", String.valueOf(longRun)); 

        logger.info("[TradeConfig] Calling service "+serviceURI+
            "/setLongRun with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("setLongRun")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeConfig] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }

    }

    public static void setPublishQuotePriceChange(boolean publishQuotePriceChange) {
        // create form for service request
        Form form = new Form();
        form.param("publishQuotePriceChange", String.valueOf(publishQuotePriceChange)); 

        logger.info("[TradeConfig] Calling service "+serviceURI+
            "/setPublishQuotePriceChange with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("setPublishQuotePriceChange")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeConfig] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }

    }

    public static boolean getPublishQuotePriceChange() {
        // create form for service request
        Form form = new Form();
        logger.info("[TradeConfig] Calling service "+serviceURI+
            "/getPublishQuotePriceChange with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("getPublishQuotePriceChange")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeConfig] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeConfig] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return Boolean.parseBoolean(response);
    }

    public static void setMarketSummaryInterval(int seconds) {
        // create form for service request
        Form form = new Form();
        form.param("seconds", String.valueOf(seconds)); 

        logger.info("[TradeConfig] Calling service "+serviceURI+
            "/setMarketSummaryInterval with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("setMarketSummaryInterval")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeConfig] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }

    }

    public static int getMarketSummaryInterval() {
        // create form for service request
        Form form = new Form();
        logger.info("[TradeConfig] Calling service "+serviceURI+
            "/getMarketSummaryInterval with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("getMarketSummaryInterval")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeConfig] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeConfig] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return Integer.parseInt(response);
    }

    public static void setRunTimeMode(int value) {
        // create form for service request
        Form form = new Form();
        form.param("value", String.valueOf(value)); 

        logger.info("[TradeConfig] Calling service "+serviceURI+
            "/setRunTimeMode with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("setRunTimeMode")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeConfig] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }

    }

    public static int getRunTimeMode() {
        // create form for service request
        Form form = new Form();
        logger.info("[TradeConfig] Calling service "+serviceURI+
            "/getRunTimeMode with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("getRunTimeMode")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeConfig] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeConfig] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return Integer.parseInt(response);
    }

    public static void setOrderProcessingMode(int value) {
        // create form for service request
        Form form = new Form();
        form.param("value", String.valueOf(value)); 

        logger.info("[TradeConfig] Calling service "+serviceURI+
            "/setOrderProcessingMode with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("setOrderProcessingMode")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeConfig] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }

    }

    public static int getOrderProcessingMode() {
        // create form for service request
        Form form = new Form();
        logger.info("[TradeConfig] Calling service "+serviceURI+
            "/getOrderProcessingMode with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("getOrderProcessingMode")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeConfig] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeConfig] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return Integer.parseInt(response);
    }

    public static void setAccessMode(int value) {
        // create form for service request
        Form form = new Form();
        form.param("value", String.valueOf(value)); 

        logger.info("[TradeConfig] Calling service "+serviceURI+
            "/setAccessMode with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("setAccessMode")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeConfig] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }

    }

    public static int getAccessMode() {
        // create form for service request
        Form form = new Form();
        logger.info("[TradeConfig] Calling service "+serviceURI+
            "/getAccessMode with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("getAccessMode")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeConfig] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeConfig] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return Integer.parseInt(response);
    }

    public static void setWebInterface(int value) {
        // create form for service request
        Form form = new Form();
        form.param("value", String.valueOf(value)); 

        logger.info("[TradeConfig] Calling service "+serviceURI+
            "/setWebInterface with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("setWebInterface")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeConfig] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }

    }

    public static int getWebInterface() {
        // create form for service request
        Form form = new Form();
        logger.info("[TradeConfig] Calling service "+serviceURI+
            "/getWebInterface with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("getWebInterface")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeConfig] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeConfig] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return Integer.parseInt(response);
    }

    /*public static void setCachingType(int value) {
        cachingType = value;
    }

    public static int getCachingType() {
        return cachingType;
    }
	*/
    public static void setDisplayOrderAlerts(boolean value) {
        // create form for service request
        Form form = new Form();
        form.param("value", String.valueOf(value)); 

        logger.info("[TradeConfig] Calling service "+serviceURI+
            "/setDisplayOrderAlerts with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("setDisplayOrderAlerts")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeConfig] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }

    }

    public static boolean getDisplayOrderAlerts() {
        // create form for service request
        Form form = new Form();
        logger.info("[TradeConfig] Calling service "+serviceURI+
            "/getDisplayOrderAlerts with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("getDisplayOrderAlerts")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeConfig] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeConfig] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return Boolean.parseBoolean(response);
    }
    /*
    public static void setDistributedMapCacheSize(int value) {
        distributedMapCacheSize = value;
    }

    public static int getDistributedMapCacheSize() {
        return distributedMapCacheSize;
    }*/

    public static void setPercentSentToWebsocket(int value) {
        // create form for service request
        Form form = new Form();
        form.param("value", String.valueOf(value)); 

        logger.info("[TradeConfig] Calling service "+serviceURI+
            "/setPercentSentToWebsocket with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("setPercentSentToWebsocket")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeConfig] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }

    }
    
	public static int getPercentSentToWebsocket() {
        // create form for service request
        Form form = new Form();
        logger.info("[TradeConfig] Calling service "+serviceURI+
            "/getPercentSentToWebsocket with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("getPercentSentToWebsocket")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeConfig] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeConfig] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return Integer.parseInt(response);
    }
	
	public static void setUseRemoteEJBInterface(boolean value) {
        // create form for service request
        Form form = new Form();
        form.param("value", String.valueOf(value)); 

        logger.info("[TradeConfig] Calling service "+serviceURI+
            "/setUseRemoteEJBInterface with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("setUseRemoteEJBInterface")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeConfig] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }

    }

	public static boolean useRemoteEJBInterface() {
        // create form for service request
        Form form = new Form();
        logger.info("[TradeConfig] Calling service "+serviceURI+
            "/useRemoteEJBInterface with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("useRemoteEJBInterface")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[TradeConfig] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[TradeConfig] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return Boolean.parseBoolean(response);
    }	
}
