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

import com.ibm.cardinal.util.CardinalException;
import com.ibm.cardinal.util.CardinalLogger;
import com.ibm.cardinal.util.CardinalString;
import com.ibm.cardinal.util.ClusterObjectManager;
import com.ibm.cardinal.util.KluInterface;
import com.ibm.cardinal.util.SerializationUtil;

import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;



public class Log implements KluInterface {
    private String klu__referenceID = "";
    private static String serviceURI;
    private static Client client;
    private static final Logger logger = CardinalLogger.getLogger(Log.class);

    static {
        client = ClientBuilder.newClient();

        logger.info("Static initializer of Log of cluster partition_1");

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
        serviceURI += "LogService";

        try {
            java.net.URI uri = java.net.URI.create(serviceURI);
        }
        catch (Exception e) {
            throw new RuntimeException("Invalid URI for partition partition_1, "+
                "service LogService: "+serviceURI, e);
        }

        logger.info("partition_1 LogService URI = " + serviceURI);
    }

    // default constructor (generated)
    public Log() {
        Response svc_response =
            client.target(serviceURI) 
            .path("Log_default_ctor") 
            .request(MediaType.APPLICATION_JSON) 
            .post(Entity.text(""), Response.class);
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[Log()] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        setKlu__referenceID(response_json.getString("return_value"));
        svc_response.close();

    }



    // constructor for creating proxy instances of this class using reference ID
    public Log(CardinalString referenceId) {
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





    

    // A general purpose, high performance logging, tracing, statistic service

    public static void log(String message) {
        // create form for service request
        Form form = new Form();
        form.param("message", message); 

        logger.info("[Log] Calling service "+serviceURI+
            "/log_001 with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("log_001")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[Log] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }

    }

    public static void log(String msg1, String msg2) {
        // create form for service request
        Form form = new Form();
        form.param("msg1", msg1); 

        form.param("msg2", msg2); 

        logger.info("[Log] Calling service "+serviceURI+
            "/log_002 with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("log_002")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[Log] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }

    }

    public static void log(String msg1, String msg2, String msg3) {
        // create form for service request
        Form form = new Form();
        form.param("msg1", msg1); 

        form.param("msg2", msg2); 

        form.param("msg3", msg3); 

        logger.info("[Log] Calling service "+serviceURI+
            "/log_003 with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("log_003")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[Log] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }

    }

    public static void error(String message) {
        // create form for service request
        Form form = new Form();
        form.param("message", message); 

        logger.info("[Log] Calling service "+serviceURI+
            "/error_004 with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("error_004")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[Log] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }

    }

    public static void error(String message, Throwable e) {
        // create form for service request
        Form form = new Form();
        form.param("message", message); 

        
        // convert physical/proxy object(s) referenced by "e" to reference ID(s)
        String e_fpar = SerializationUtil.encodeWithDynamicTypeCheck(e);
        form.param("e", e_fpar);

        logger.info("[Log] Calling service "+serviceURI+
            "/error_005 with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("error_005")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[Log] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }

    }

    public static void error(String msg1, String msg2, Throwable e) {
        // create form for service request
        Form form = new Form();
        form.param("msg1", msg1); 

        form.param("msg2", msg2); 

        
        // convert physical/proxy object(s) referenced by "e" to reference ID(s)
        String e_fpar = SerializationUtil.encodeWithDynamicTypeCheck(e);
        form.param("e", e_fpar);

        logger.info("[Log] Calling service "+serviceURI+
            "/error_006 with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("error_006")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[Log] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }

    }

    public static void error(String msg1, String msg2, String msg3, Throwable e) {
        // create form for service request
        Form form = new Form();
        form.param("msg1", msg1); 

        form.param("msg2", msg2); 

        form.param("msg3", msg3); 

        
        // convert physical/proxy object(s) referenced by "e" to reference ID(s)
        String e_fpar = SerializationUtil.encodeWithDynamicTypeCheck(e);
        form.param("e", e_fpar);

        logger.info("[Log] Calling service "+serviceURI+
            "/error_007 with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("error_007")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[Log] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }

    }

    public static void error(Throwable e, String message) {
        // create form for service request
        Form form = new Form();
        
        // convert physical/proxy object(s) referenced by "e" to reference ID(s)
        String e_fpar = SerializationUtil.encodeWithDynamicTypeCheck(e);
        form.param("e", e_fpar);

        form.param("message", message); 

        logger.info("[Log] Calling service "+serviceURI+
            "/error_008 with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("error_008")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[Log] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }

    }

    public static void error(Throwable e, String msg1, String msg2) {
        // create form for service request
        Form form = new Form();
        
        // convert physical/proxy object(s) referenced by "e" to reference ID(s)
        String e_fpar = SerializationUtil.encodeWithDynamicTypeCheck(e);
        form.param("e", e_fpar);

        form.param("msg1", msg1); 

        form.param("msg2", msg2); 

        logger.info("[Log] Calling service "+serviceURI+
            "/error_009 with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("error_009")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[Log] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }

    }

    public static void error(Throwable e, String msg1, String msg2, String msg3) {
        // create form for service request
        Form form = new Form();
        
        // convert physical/proxy object(s) referenced by "e" to reference ID(s)
        String e_fpar = SerializationUtil.encodeWithDynamicTypeCheck(e);
        form.param("e", e_fpar);

        form.param("msg1", msg1); 

        form.param("msg2", msg2); 

        form.param("msg3", msg3); 

        logger.info("[Log] Calling service "+serviceURI+
            "/error_0010 with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("error_0010")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[Log] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }

    }

    public static void trace(String message) {
        // create form for service request
        Form form = new Form();
        form.param("message", message); 

        logger.info("[Log] Calling service "+serviceURI+
            "/trace_0011 with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("trace_0011")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[Log] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }

    }

    public static void trace(String message, Object parm1) {
        // create form for service request
        Form form = new Form();
        form.param("message", message); 

        
        // convert physical/proxy object(s) referenced by "parm1" to reference ID(s)
        String parm1_fpar = SerializationUtil.encodeWithDynamicTypeCheck(parm1);
        form.param("parm1", parm1_fpar);

        logger.info("[Log] Calling service "+serviceURI+
            "/trace_0012 with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("trace_0012")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[Log] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }

    }

    public static void trace(String message, Object parm1, Object parm2) {
        // create form for service request
        Form form = new Form();
        form.param("message", message); 

        
        // convert physical/proxy object(s) referenced by "parm1" to reference ID(s)
        String parm1_fpar = SerializationUtil.encodeWithDynamicTypeCheck(parm1);
        form.param("parm1", parm1_fpar);

        
        // convert physical/proxy object(s) referenced by "parm2" to reference ID(s)
        String parm2_fpar = SerializationUtil.encodeWithDynamicTypeCheck(parm2);
        form.param("parm2", parm2_fpar);

        logger.info("[Log] Calling service "+serviceURI+
            "/trace_0013 with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("trace_0013")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[Log] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }

    }

    public static void trace(String message, Object parm1, Object parm2, Object parm3) {
        // create form for service request
        Form form = new Form();
        form.param("message", message); 

        
        // convert physical/proxy object(s) referenced by "parm1" to reference ID(s)
        String parm1_fpar = SerializationUtil.encodeWithDynamicTypeCheck(parm1);
        form.param("parm1", parm1_fpar);

        
        // convert physical/proxy object(s) referenced by "parm2" to reference ID(s)
        String parm2_fpar = SerializationUtil.encodeWithDynamicTypeCheck(parm2);
        form.param("parm2", parm2_fpar);

        
        // convert physical/proxy object(s) referenced by "parm3" to reference ID(s)
        String parm3_fpar = SerializationUtil.encodeWithDynamicTypeCheck(parm3);
        form.param("parm3", parm3_fpar);

        logger.info("[Log] Calling service "+serviceURI+
            "/trace_0014 with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("trace_0014")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[Log] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }

    }

    public static void trace(String message, Object parm1, Object parm2, Object parm3, Object parm4) {
        // create form for service request
        Form form = new Form();
        form.param("message", message); 

        
        // convert physical/proxy object(s) referenced by "parm1" to reference ID(s)
        String parm1_fpar = SerializationUtil.encodeWithDynamicTypeCheck(parm1);
        form.param("parm1", parm1_fpar);

        
        // convert physical/proxy object(s) referenced by "parm2" to reference ID(s)
        String parm2_fpar = SerializationUtil.encodeWithDynamicTypeCheck(parm2);
        form.param("parm2", parm2_fpar);

        
        // convert physical/proxy object(s) referenced by "parm3" to reference ID(s)
        String parm3_fpar = SerializationUtil.encodeWithDynamicTypeCheck(parm3);
        form.param("parm3", parm3_fpar);

        
        // convert physical/proxy object(s) referenced by "parm4" to reference ID(s)
        String parm4_fpar = SerializationUtil.encodeWithDynamicTypeCheck(parm4);
        form.param("parm4", parm4_fpar);

        logger.info("[Log] Calling service "+serviceURI+
            "/trace_0015 with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("trace_0015")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[Log] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }

    }

    public static void trace(String message, Object parm1, Object parm2, Object parm3, Object parm4, Object parm5) {
        // create form for service request
        Form form = new Form();
        form.param("message", message); 

        
        // convert physical/proxy object(s) referenced by "parm1" to reference ID(s)
        String parm1_fpar = SerializationUtil.encodeWithDynamicTypeCheck(parm1);
        form.param("parm1", parm1_fpar);

        
        // convert physical/proxy object(s) referenced by "parm2" to reference ID(s)
        String parm2_fpar = SerializationUtil.encodeWithDynamicTypeCheck(parm2);
        form.param("parm2", parm2_fpar);

        
        // convert physical/proxy object(s) referenced by "parm3" to reference ID(s)
        String parm3_fpar = SerializationUtil.encodeWithDynamicTypeCheck(parm3);
        form.param("parm3", parm3_fpar);

        
        // convert physical/proxy object(s) referenced by "parm4" to reference ID(s)
        String parm4_fpar = SerializationUtil.encodeWithDynamicTypeCheck(parm4);
        form.param("parm4", parm4_fpar);

        
        // convert physical/proxy object(s) referenced by "parm5" to reference ID(s)
        String parm5_fpar = SerializationUtil.encodeWithDynamicTypeCheck(parm5);
        form.param("parm5", parm5_fpar);

        logger.info("[Log] Calling service "+serviceURI+
            "/trace_0016 with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("trace_0016")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[Log] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }

    }

    public static void trace(String message, Object parm1, Object parm2, Object parm3, Object parm4, Object parm5, Object parm6) {
        // create form for service request
        Form form = new Form();
        form.param("message", message); 

        
        // convert physical/proxy object(s) referenced by "parm1" to reference ID(s)
        String parm1_fpar = SerializationUtil.encodeWithDynamicTypeCheck(parm1);
        form.param("parm1", parm1_fpar);

        
        // convert physical/proxy object(s) referenced by "parm2" to reference ID(s)
        String parm2_fpar = SerializationUtil.encodeWithDynamicTypeCheck(parm2);
        form.param("parm2", parm2_fpar);

        
        // convert physical/proxy object(s) referenced by "parm3" to reference ID(s)
        String parm3_fpar = SerializationUtil.encodeWithDynamicTypeCheck(parm3);
        form.param("parm3", parm3_fpar);

        
        // convert physical/proxy object(s) referenced by "parm4" to reference ID(s)
        String parm4_fpar = SerializationUtil.encodeWithDynamicTypeCheck(parm4);
        form.param("parm4", parm4_fpar);

        
        // convert physical/proxy object(s) referenced by "parm5" to reference ID(s)
        String parm5_fpar = SerializationUtil.encodeWithDynamicTypeCheck(parm5);
        form.param("parm5", parm5_fpar);

        
        // convert physical/proxy object(s) referenced by "parm6" to reference ID(s)
        String parm6_fpar = SerializationUtil.encodeWithDynamicTypeCheck(parm6);
        form.param("parm6", parm6_fpar);

        logger.info("[Log] Calling service "+serviceURI+
            "/trace_0017 with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("trace_0017")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[Log] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }

    }

    public static void trace(String message, Object parm1, Object parm2, Object parm3, Object parm4, Object parm5, Object parm6, Object parm7) {
        // create form for service request
        Form form = new Form();
        form.param("message", message); 

        
        // convert physical/proxy object(s) referenced by "parm1" to reference ID(s)
        String parm1_fpar = SerializationUtil.encodeWithDynamicTypeCheck(parm1);
        form.param("parm1", parm1_fpar);

        
        // convert physical/proxy object(s) referenced by "parm2" to reference ID(s)
        String parm2_fpar = SerializationUtil.encodeWithDynamicTypeCheck(parm2);
        form.param("parm2", parm2_fpar);

        
        // convert physical/proxy object(s) referenced by "parm3" to reference ID(s)
        String parm3_fpar = SerializationUtil.encodeWithDynamicTypeCheck(parm3);
        form.param("parm3", parm3_fpar);

        
        // convert physical/proxy object(s) referenced by "parm4" to reference ID(s)
        String parm4_fpar = SerializationUtil.encodeWithDynamicTypeCheck(parm4);
        form.param("parm4", parm4_fpar);

        
        // convert physical/proxy object(s) referenced by "parm5" to reference ID(s)
        String parm5_fpar = SerializationUtil.encodeWithDynamicTypeCheck(parm5);
        form.param("parm5", parm5_fpar);

        
        // convert physical/proxy object(s) referenced by "parm6" to reference ID(s)
        String parm6_fpar = SerializationUtil.encodeWithDynamicTypeCheck(parm6);
        form.param("parm6", parm6_fpar);

        
        // convert physical/proxy object(s) referenced by "parm7" to reference ID(s)
        String parm7_fpar = SerializationUtil.encodeWithDynamicTypeCheck(parm7);
        form.param("parm7", parm7_fpar);

        logger.info("[Log] Calling service "+serviceURI+
            "/trace_0018 with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("trace_0018")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[Log] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }

    }

    public static void traceEnter(String message) {
        // create form for service request
        Form form = new Form();
        form.param("message", message); 

        logger.info("[Log] Calling service "+serviceURI+
            "/traceEnter with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("traceEnter")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[Log] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }

    }

    public static void traceExit(String message) {
        // create form for service request
        Form form = new Form();
        form.param("message", message); 

        logger.info("[Log] Calling service "+serviceURI+
            "/traceExit with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("traceExit")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[Log] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }

    }

    public static void stat(String message) {
        // create form for service request
        Form form = new Form();
        form.param("message", message); 

        logger.info("[Log] Calling service "+serviceURI+
            "/stat with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("stat")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[Log] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }

    }

    public static void debug(String message) {
        // create form for service request
        Form form = new Form();
        form.param("message", message); 

        logger.info("[Log] Calling service "+serviceURI+
            "/debug with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("debug")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[Log] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }

    }

    public static void print(String message) {
        // create form for service request
        Form form = new Form();
        form.param("message", message); 

        logger.info("[Log] Calling service "+serviceURI+
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
            logger.warning("[Log] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }

    }

    public static void printObject(Object o) {
        // create form for service request
        Form form = new Form();
        
        // convert physical/proxy object(s) referenced by "o" to reference ID(s)
        String o_fpar = SerializationUtil.encodeWithDynamicTypeCheck(o);
        form.param("o", o_fpar);

        logger.info("[Log] Calling service "+serviceURI+
            "/printObject with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("printObject")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[Log] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }

    }

    public static void printCollection(Collection<?> c) {
        // create form for service request
        Form form = new Form();
        
        // convert physical/proxy object(s) referenced by "c" to reference ID(s)
        String c_fpar = SerializationUtil.encodeWithDynamicTypeCheck(c);
        form.param("c", c_fpar);

        logger.info("[Log] Calling service "+serviceURI+
            "/printCollection_0019 with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("printCollection_0019")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[Log] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }

    }

    public static void printCollection(String message, Collection<?> c) {
        // create form for service request
        Form form = new Form();
        form.param("message", message); 

        
        // convert physical/proxy object(s) referenced by "c" to reference ID(s)
        String c_fpar = SerializationUtil.encodeWithDynamicTypeCheck(c);
        form.param("c", c_fpar);

        logger.info("[Log] Calling service "+serviceURI+
            "/printCollection_0020 with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("printCollection_0020")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[Log] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }

    }

    public static boolean doActionTrace() {
        // create form for service request
        Form form = new Form();
        logger.info("[Log] Calling service "+serviceURI+
            "/doActionTrace with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("doActionTrace")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[Log] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[Log] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return Boolean.parseBoolean(response);
    }

    public static boolean doTrace() {
        // create form for service request
        Form form = new Form();
        logger.info("[Log] Calling service "+serviceURI+
            "/doTrace with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("doTrace")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[Log] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[Log] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return Boolean.parseBoolean(response);
    }

    public static boolean doDebug() {
        // create form for service request
        Form form = new Form();
        logger.info("[Log] Calling service "+serviceURI+
            "/doDebug with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("doDebug")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[Log] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[Log] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return Boolean.parseBoolean(response);
    }

    public static boolean doStat() {
        // create form for service request
        Form form = new Form();
        logger.info("[Log] Calling service "+serviceURI+
            "/doStat with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("doStat")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[Log] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[Log] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return Boolean.parseBoolean(response);
    }

    /**
     * Gets the trace
     *
     * @return Returns a boolean
     */
    public static boolean getTrace() {
        // create form for service request
        Form form = new Form();
        logger.info("[Log] Calling service "+serviceURI+
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
            logger.warning("[Log] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[Log] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return Boolean.parseBoolean(response);
    }

    /**
     * Gets the trace value for Trade actions only
     *
     * @return Returns a boolean
     */
    public static boolean getActionTrace() {
        // create form for service request
        Form form = new Form();
        logger.info("[Log] Calling service "+serviceURI+
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
            logger.warning("[Log] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[Log] Response JSON string: "+response_json_str);
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

        logger.info("[Log] Calling service "+serviceURI+
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
            logger.warning("[Log] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }

    }

    /**
     * Sets the trace value for Trade actions only
     *
     * @param trace
     *            The trace to set
     */
    public static void setActionTrace(boolean traceValue) {
        // create form for service request
        Form form = new Form();
        form.param("traceValue", String.valueOf(traceValue)); 

        logger.info("[Log] Calling service "+serviceURI+
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
            logger.warning("[Log] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }

    }
}
