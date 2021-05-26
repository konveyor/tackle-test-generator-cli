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

/**
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates. To enable and disable the creation of type
 * comments go to Window>Preferences>Java>Code Generation.
 */
public class MDBStats extends java.util.HashMap<String, TimerStat> implements KluInterface {
    private String klu__referenceID = "";
    private static String serviceURI;
    private static Client client;
    private static final Logger logger = CardinalLogger.getLogger(MDBStats.class);

    static {
        client = ClientBuilder.newClient();

        logger.info("Static initializer of MDBStats of cluster partition_3");

        serviceURI = System.getenv().get("APPLICATION_PARTITION_3_REST_URL");
        if (serviceURI == null) {
            throw new RuntimeException("Environment variable "+
                "APPLICATION_PARTITION_3_REST_URL not set\n"+
                "Please set APPLICATION_PARTITION_3_REST_URL to "+
                "partition_3 host:port"
            );
        }

        if (!serviceURI.endsWith("/")) {
            serviceURI += "/";
        }
        serviceURI += "MDBStatsService";

        try {
            java.net.URI uri = java.net.URI.create(serviceURI);
        }
        catch (Exception e) {
            throw new RuntimeException("Invalid URI for partition partition_3, "+
                "service MDBStatsService: "+serviceURI, e);
        }

        logger.info("partition_3 MDBStatsService URI = " + serviceURI);
    }




    // constructor for creating proxy instances of this class using reference ID
    public MDBStats(CardinalString referenceId) {
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






    public static synchronized MDBStats getInstance() {
        // create form for service request
        Form form = new Form();
        logger.info("[MDBStats] Calling service "+serviceURI+
            "/getInstance with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("getInstance")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[MDBStats] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[MDBStats] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        
        // convert reference ID(s) stored in "response" to physical/proxy object(s)
        MDBStats response_obj = (MDBStats)SerializationUtil.decodeWithDynamicTypeCheck(response);
        return response_obj;

    }

    public TimerStat addTiming(String type, long sendTime, long recvTime) {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        form.param("type", type); 

        form.param("sendTime", String.valueOf(sendTime)); 

        form.param("recvTime", String.valueOf(recvTime)); 

        logger.info("[MDBStats] Calling service "+serviceURI+
            "/addTiming with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("addTiming")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[MDBStats] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[MDBStats] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        
        // convert reference ID(s) stored in "response" to physical/proxy object(s)
        TimerStat response_obj = (TimerStat)SerializationUtil.decodeWithDynamicTypeCheck(response);
        return response_obj;

    }

    public synchronized void reset() {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        logger.info("[MDBStats] Calling service "+serviceURI+
            "/reset with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("reset")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[MDBStats] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }

    }

}
