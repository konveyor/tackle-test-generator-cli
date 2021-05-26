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
package com.ibm.websphere.samples.daytrader.entities;

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

import java.io.Serializable;
import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import com.ibm.websphere.samples.daytrader.util.Log;
import com.ibm.websphere.samples.daytrader.util.TradeConfig;





public class QuoteDataBean implements KluInterface, Serializable {
    private String klu__referenceID = "";
    private static String serviceURI;
    private static Client client;
    private static final Logger logger = CardinalLogger.getLogger(QuoteDataBean.class);

    static {
        client = ClientBuilder.newClient();

        logger.info("Static initializer of QuoteDataBean of cluster partition_1");

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
        serviceURI += "QuoteDataBeanService";

        try {
            java.net.URI uri = java.net.URI.create(serviceURI);
        }
        catch (Exception e) {
            throw new RuntimeException("Invalid URI for partition partition_1, "+
                "service QuoteDataBeanService: "+serviceURI, e);
        }

        logger.info("partition_1 QuoteDataBeanService URI = " + serviceURI);
    }




    // constructor for creating proxy instances of this class using reference ID
    public QuoteDataBean(CardinalString referenceId) {
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




 /* price change */

    /* Accessor methods for relationship fields are not kept in the DataBean */

    public QuoteDataBean() {
        // create form for service request
        Form form = new Form();
        logger.info("[QuoteDataBean] Calling service "+serviceURI+
            "/QuoteDataBean_001 with form: "+form.asMap());

        // call constructor service and store ref ID
        Response svc_response;
        try {
            svc_response = 
                client.target(serviceURI) 
                .path("QuoteDataBean_001") 
                .request(MediaType.APPLICATION_JSON) 
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[QuoteDataBean] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[QuoteDataBean] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        setKlu__referenceID(response_json.getString("return_value"));
        svc_response.close();
    }

    public QuoteDataBean(String symbol, String companyName, double volume, BigDecimal price, BigDecimal open, BigDecimal low, BigDecimal high, double change) {
        // create form for service request
        Form form = new Form();
        form.param("symbol", symbol); 

        form.param("companyName", companyName); 

        form.param("volume", String.valueOf(volume)); 

        
        // convert physical/proxy object(s) referenced by "price" to reference ID(s)
        String price_fpar = SerializationUtil.encodeWithDynamicTypeCheck(price);
        form.param("price", price_fpar);

        
        // convert physical/proxy object(s) referenced by "open" to reference ID(s)
        String open_fpar = SerializationUtil.encodeWithDynamicTypeCheck(open);
        form.param("open", open_fpar);

        
        // convert physical/proxy object(s) referenced by "low" to reference ID(s)
        String low_fpar = SerializationUtil.encodeWithDynamicTypeCheck(low);
        form.param("low", low_fpar);

        
        // convert physical/proxy object(s) referenced by "high" to reference ID(s)
        String high_fpar = SerializationUtil.encodeWithDynamicTypeCheck(high);
        form.param("high", high_fpar);

        form.param("change", String.valueOf(change)); 

        logger.info("[QuoteDataBean] Calling service "+serviceURI+
            "/QuoteDataBean_002 with form: "+form.asMap());

        // call constructor service and store ref ID
        Response svc_response;
        try {
            svc_response = 
                client.target(serviceURI) 
                .path("QuoteDataBean_002") 
                .request(MediaType.APPLICATION_JSON) 
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[QuoteDataBean] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[QuoteDataBean] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        setKlu__referenceID(response_json.getString("return_value"));
        svc_response.close();
    }

    public static QuoteDataBean getRandomInstance() {
        // create form for service request
        Form form = new Form();
        logger.info("[QuoteDataBean] Calling service "+serviceURI+
            "/getRandomInstance with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("getRandomInstance")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[QuoteDataBean] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[QuoteDataBean] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        
        // convert reference ID(s) stored in "response" to physical/proxy object(s)
        QuoteDataBean response_obj = (QuoteDataBean)SerializationUtil.decodeWithDynamicTypeCheck(response);
        return response_obj;

    }

    // Create a "zero" value quoteDataBean for the given symbol
    public QuoteDataBean(String symbol) {
        // create form for service request
        Form form = new Form();
        form.param("symbol", symbol); 

        logger.info("[QuoteDataBean] Calling service "+serviceURI+
            "/QuoteDataBean_003 with form: "+form.asMap());

        // call constructor service and store ref ID
        Response svc_response;
        try {
            svc_response = 
                client.target(serviceURI) 
                .path("QuoteDataBean_003") 
                .request(MediaType.APPLICATION_JSON) 
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[QuoteDataBean] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[QuoteDataBean] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        setKlu__referenceID(response_json.getString("return_value"));
        svc_response.close();
    }

    @Override
    public String toString() {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        logger.info("[QuoteDataBean] Calling service "+serviceURI+
            "/toString with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("toString")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[QuoteDataBean] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[QuoteDataBean] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return response;
    }

    public String toHTML() {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        logger.info("[QuoteDataBean] Calling service "+serviceURI+
            "/toHTML with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("toHTML")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[QuoteDataBean] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[QuoteDataBean] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return response;
    }

    public void print() {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        logger.info("[QuoteDataBean] Calling service "+serviceURI+
            "/print with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("print")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[QuoteDataBean] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }

    }

    public String getSymbol() {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        logger.info("[QuoteDataBean] Calling service "+serviceURI+
            "/getSymbol with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("getSymbol")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[QuoteDataBean] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[QuoteDataBean] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return response;
    }

    public void setSymbol(String symbol) {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        form.param("symbol", symbol); 

        logger.info("[QuoteDataBean] Calling service "+serviceURI+
            "/setSymbol with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("setSymbol")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[QuoteDataBean] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }

    }

    public String getCompanyName() {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        logger.info("[QuoteDataBean] Calling service "+serviceURI+
            "/getCompanyName with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("getCompanyName")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[QuoteDataBean] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[QuoteDataBean] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return response;
    }

    public void setCompanyName(String companyName) {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        form.param("companyName", companyName); 

        logger.info("[QuoteDataBean] Calling service "+serviceURI+
            "/setCompanyName with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("setCompanyName")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[QuoteDataBean] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }

    }

    public BigDecimal getPrice() {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        logger.info("[QuoteDataBean] Calling service "+serviceURI+
            "/getPrice with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("getPrice")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[QuoteDataBean] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[QuoteDataBean] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        
        // convert reference ID(s) stored in "response" to physical/proxy object(s)
        BigDecimal response_obj = (BigDecimal)SerializationUtil.decodeWithDynamicTypeCheck(response);
        return response_obj;

    }

    public void setPrice(BigDecimal price) {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        
        // convert physical/proxy object(s) referenced by "price" to reference ID(s)
        String price_fpar = SerializationUtil.encodeWithDynamicTypeCheck(price);
        form.param("price", price_fpar);

        logger.info("[QuoteDataBean] Calling service "+serviceURI+
            "/setPrice with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("setPrice")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[QuoteDataBean] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }

    }

    public BigDecimal getOpen() {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        logger.info("[QuoteDataBean] Calling service "+serviceURI+
            "/getOpen with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("getOpen")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[QuoteDataBean] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[QuoteDataBean] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        
        // convert reference ID(s) stored in "response" to physical/proxy object(s)
        BigDecimal response_obj = (BigDecimal)SerializationUtil.decodeWithDynamicTypeCheck(response);
        return response_obj;

    }

    public void setOpen(BigDecimal open) {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        
        // convert physical/proxy object(s) referenced by "open" to reference ID(s)
        String open_fpar = SerializationUtil.encodeWithDynamicTypeCheck(open);
        form.param("open", open_fpar);

        logger.info("[QuoteDataBean] Calling service "+serviceURI+
            "/setOpen with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("setOpen")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[QuoteDataBean] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }

    }

    public BigDecimal getLow() {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        logger.info("[QuoteDataBean] Calling service "+serviceURI+
            "/getLow with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("getLow")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[QuoteDataBean] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[QuoteDataBean] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        
        // convert reference ID(s) stored in "response" to physical/proxy object(s)
        BigDecimal response_obj = (BigDecimal)SerializationUtil.decodeWithDynamicTypeCheck(response);
        return response_obj;

    }

    public void setLow(BigDecimal low) {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        
        // convert physical/proxy object(s) referenced by "low" to reference ID(s)
        String low_fpar = SerializationUtil.encodeWithDynamicTypeCheck(low);
        form.param("low", low_fpar);

        logger.info("[QuoteDataBean] Calling service "+serviceURI+
            "/setLow with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("setLow")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[QuoteDataBean] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }

    }

    public BigDecimal getHigh() {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        logger.info("[QuoteDataBean] Calling service "+serviceURI+
            "/getHigh with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("getHigh")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[QuoteDataBean] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[QuoteDataBean] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        
        // convert reference ID(s) stored in "response" to physical/proxy object(s)
        BigDecimal response_obj = (BigDecimal)SerializationUtil.decodeWithDynamicTypeCheck(response);
        return response_obj;

    }

    public void setHigh(BigDecimal high) {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        
        // convert physical/proxy object(s) referenced by "high" to reference ID(s)
        String high_fpar = SerializationUtil.encodeWithDynamicTypeCheck(high);
        form.param("high", high_fpar);

        logger.info("[QuoteDataBean] Calling service "+serviceURI+
            "/setHigh with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("setHigh")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[QuoteDataBean] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }

    }

    public double getChange() {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        logger.info("[QuoteDataBean] Calling service "+serviceURI+
            "/getChange with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("getChange")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[QuoteDataBean] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[QuoteDataBean] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return Double.parseDouble(response);
    }

    public void setChange(double change) {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        form.param("change", String.valueOf(change)); 

        logger.info("[QuoteDataBean] Calling service "+serviceURI+
            "/setChange with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("setChange")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[QuoteDataBean] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }

    }

    public double getVolume() {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        logger.info("[QuoteDataBean] Calling service "+serviceURI+
            "/getVolume with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("getVolume")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[QuoteDataBean] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[QuoteDataBean] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return Double.parseDouble(response);
    }

    public void setVolume(double volume) {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        form.param("volume", String.valueOf(volume)); 

        logger.info("[QuoteDataBean] Calling service "+serviceURI+
            "/setVolume with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("setVolume")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[QuoteDataBean] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }

    }

    @Override
    public int hashCode() {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        logger.info("[QuoteDataBean] Calling service "+serviceURI+
            "/hashCode with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("hashCode")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[QuoteDataBean] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[QuoteDataBean] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return Integer.parseInt(response);
    }

    @Override
    public boolean equals(Object object) {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        
        // convert physical/proxy object(s) referenced by "object" to reference ID(s)
        String object_fpar = SerializationUtil.encodeWithDynamicTypeCheck(object);
        form.param("object", object_fpar);

        logger.info("[QuoteDataBean] Calling service "+serviceURI+
            "/equals with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("equals")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[QuoteDataBean] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[QuoteDataBean] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return Boolean.parseBoolean(response);
    }
}