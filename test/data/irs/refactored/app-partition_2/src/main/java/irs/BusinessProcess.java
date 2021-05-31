/**
 * This is a Cardinal generated proxy
 */

package irs;

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

import java.util.*;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BusinessProcess implements KluInterface {
    private String klu__referenceID = "";
    private static String klu__serviceURI;
    private static Client klu__client;
    private static final Logger klu__logger = CardinalLogger.getLogger(BusinessProcess.class);

    static {
        klu__client = ClientBuilder.newClient();

        klu__logger.info("Static initializer of BusinessProcess of cluster partition_1");

        klu__serviceURI = System.getenv().get("IRS_PARTITION_1_REST_URL");
        if (klu__serviceURI == null) {
            throw new java.lang.RuntimeException("Environment variable "+
                "IRS_PARTITION_1_REST_URL not set\n"+
                "Please set IRS_PARTITION_1_REST_URL to "+
                "partition_1 host:port"
            );
        }

        if (!klu__serviceURI.endsWith("/")) {
            klu__serviceURI += "/";
        }
        klu__serviceURI += "BusinessProcessService";

        try {
            java.net.URI uri = java.net.URI.create(klu__serviceURI);
        }
        catch (java.lang.Exception e) {
            throw new java.lang.RuntimeException("Invalid URI for partition partition_1, "+
                "service BusinessProcessService: "+klu__serviceURI, e);
        }

        klu__logger.info("partition_1 BusinessProcessService URI = " + klu__serviceURI);
    }

    // default constructor (generated)
    public BusinessProcess() {
        Response svc_response =
            klu__client.target(klu__serviceURI) 
            .path("BusinessProcess_default_ctor") 
            .request(MediaType.APPLICATION_JSON) 
            .post(Entity.text(""), Response.class);
        String response_json_str = svc_response.readEntity(String.class);
        klu__logger.info("[BusinessProcess()] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        setKlu__referenceID(response_json.getString("return_value"));
        svc_response.close();

    }



    // constructor for creating proxy instances of this class using reference ID
    public BusinessProcess(CardinalString referenceId) {
        setKlu__referenceID(referenceId.getString());
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        klu__client.target(klu__serviceURI)
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
        klu__client.target(klu__serviceURI)
            .path("decObjectCount")
            .request()
            .post(Entity.form(form));
    }






	public List<Employer> getAllEmployers() {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        klu__logger.info("[BusinessProcess] Calling service "+klu__serviceURI+
            "/getAllEmployers with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = klu__client.target(klu__serviceURI)
                .path("getAllEmployers")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            java.lang.Throwable cause = wae.getCause();
            klu__logger.warning("[BusinessProcess] Exception thrown in service call: "+wae.getMessage());
            throw (java.lang.RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        klu__logger.info("[BusinessProcess] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        
        // convert reference ID(s) stored in "response" to physical/proxy object(s)
        List<Employer> response_obj = (List<Employer>)SerializationUtil.decodeWithDynamicTypeCheck(response);
        return response_obj;

    }
	
	public void genSalarySlip(IRS irsInst) {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        
        // convert physical/proxy object(s) referenced by "irsInst" to reference ID(s)
        String irsInst_fpar = SerializationUtil.encodeWithDynamicTypeCheck(irsInst);
        form.param("irsInst", irsInst_fpar);

        klu__logger.info("[BusinessProcess] Calling service "+klu__serviceURI+
            "/genSalarySlip with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            klu__client.target(klu__serviceURI)
                .path("genSalarySlip")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            java.lang.Throwable cause = wae.getCause();
            klu__logger.warning("[BusinessProcess] Exception thrown in service call: "+wae.getMessage());
            throw (java.lang.RuntimeException)cause;
        }

    }
	
	public static void main(String[] args) {
        // create form for service request
        Form form = new Form();
        
        // serialize primitive array to string
        String args_fpar = SerializationUtil.encode(args, null);
        form.param("args", args_fpar);

        klu__logger.info("[BusinessProcess] Calling service "+klu__serviceURI+
            "/main with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            klu__client.target(klu__serviceURI)
                .path("main")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            java.lang.Throwable cause = wae.getCause();
            klu__logger.warning("[BusinessProcess] Exception thrown in service call: "+wae.getMessage());
            throw (java.lang.RuntimeException)cause;
        }

    }
	
	public static void test_irs() {
        // create form for service request
        Form form = new Form();
        klu__logger.info("[BusinessProcess] Calling service "+klu__serviceURI+
            "/test_irs with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            klu__client.target(klu__serviceURI)
                .path("test_irs")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            java.lang.Throwable cause = wae.getCause();
            klu__logger.warning("[BusinessProcess] Exception thrown in service call: "+wae.getMessage());
            throw (java.lang.RuntimeException)cause;
        }

    }

	public static void test_employee() {
        // create form for service request
        Form form = new Form();
        klu__logger.info("[BusinessProcess] Calling service "+klu__serviceURI+
            "/test_employee with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            klu__client.target(klu__serviceURI)
                .path("test_employee")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            java.lang.Throwable cause = wae.getCause();
            klu__logger.warning("[BusinessProcess] Exception thrown in service call: "+wae.getMessage());
            throw (java.lang.RuntimeException)cause;
        }

    }

	public static List<Employee> test_employer() {
        // create form for service request
        Form form = new Form();
        klu__logger.info("[BusinessProcess] Calling service "+klu__serviceURI+
            "/test_employer with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = klu__client.target(klu__serviceURI)
                .path("test_employer")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            java.lang.Throwable cause = wae.getCause();
            klu__logger.warning("[BusinessProcess] Exception thrown in service call: "+wae.getMessage());
            throw (java.lang.RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        klu__logger.info("[BusinessProcess] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        
        // convert reference ID(s) stored in "response" to physical/proxy object(s)
        List<Employee> response_obj = (List<Employee>)SerializationUtil.decodeWithDynamicTypeCheck(response);
        return response_obj;

    }

	public static Employer test_employer2() {
        // create form for service request
        Form form = new Form();
        klu__logger.info("[BusinessProcess] Calling service "+klu__serviceURI+
            "/test_employer2 with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = klu__client.target(klu__serviceURI)
                .path("test_employer2")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            java.lang.Throwable cause = wae.getCause();
            klu__logger.warning("[BusinessProcess] Exception thrown in service call: "+wae.getMessage());
            throw (java.lang.RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        klu__logger.info("[BusinessProcess] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        
        // convert reference ID(s) stored in "response" to physical/proxy object(s)
        Employer response_obj = (Employer)SerializationUtil.decodeWithDynamicTypeCheck(response);
        return response_obj;

    }

	public static int test_employer3() {
        // create form for service request
        Form form = new Form();
        klu__logger.info("[BusinessProcess] Calling service "+klu__serviceURI+
            "/test_employer3 with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = klu__client.target(klu__serviceURI)
                .path("test_employer3")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            java.lang.Throwable cause = wae.getCause();
            klu__logger.warning("[BusinessProcess] Exception thrown in service call: "+wae.getMessage());
            throw (java.lang.RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        klu__logger.info("[BusinessProcess] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return Integer.parseInt(response);
    }

	public static void test_salary() {
        // create form for service request
        Form form = new Form();
        klu__logger.info("[BusinessProcess] Calling service "+klu__serviceURI+
            "/test_salary with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            klu__client.target(klu__serviceURI)
                .path("test_salary")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            java.lang.Throwable cause = wae.getCause();
            klu__logger.warning("[BusinessProcess] Exception thrown in service call: "+wae.getMessage());
            throw (java.lang.RuntimeException)cause;
        }

    }

	public static void test_irs_salary_set_map() {
        // create form for service request
        Form form = new Form();
        klu__logger.info("[BusinessProcess] Calling service "+klu__serviceURI+
            "/test_irs_salary_set_map with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            klu__client.target(klu__serviceURI)
                .path("test_irs_salary_set_map")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            java.lang.Throwable cause = wae.getCause();
            klu__logger.warning("[BusinessProcess] Exception thrown in service call: "+wae.getMessage());
            throw (java.lang.RuntimeException)cause;
        }

    }

	public void test_2(){
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        klu__logger.info("[BusinessProcess] Calling service "+klu__serviceURI+
            "/test_2 with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            klu__client.target(klu__serviceURI)
                .path("test_2")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            java.lang.Throwable cause = wae.getCause();
            klu__logger.warning("[BusinessProcess] Exception thrown in service call: "+wae.getMessage());
            throw (java.lang.RuntimeException)cause;
        }

    }
	
}

