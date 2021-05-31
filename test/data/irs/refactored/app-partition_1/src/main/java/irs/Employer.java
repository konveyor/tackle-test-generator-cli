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
import java.util.logging.Logger;

import com.ibm.cardinal.util.CardinalException;
import com.ibm.cardinal.util.CardinalLogger;
import com.ibm.cardinal.util.CardinalString;
import com.ibm.cardinal.util.ClusterObjectManager;
import com.ibm.cardinal.util.KluInterface;
import com.ibm.cardinal.util.SerializationUtil;

import java.util.ArrayList;
import java.util.List;

public class Employer implements KluInterface {
    private String klu__referenceID = "";
    private static String klu__serviceURI;
    private static Client klu__client;
    private static final Logger klu__logger = CardinalLogger.getLogger(Employer.class);

    static {
        klu__client = ClientBuilder.newClient();

        klu__logger.info("Static initializer of Employer of cluster partition_2");

        klu__serviceURI = System.getenv().get("IRS_PARTITION_2_REST_URL");
        if (klu__serviceURI == null) {
            throw new java.lang.RuntimeException("Environment variable "+
                "IRS_PARTITION_2_REST_URL not set\n"+
                "Please set IRS_PARTITION_2_REST_URL to "+
                "partition_2 host:port"
            );
        }

        if (!klu__serviceURI.endsWith("/")) {
            klu__serviceURI += "/";
        }
        klu__serviceURI += "EmployerService";

        try {
            java.net.URI uri = java.net.URI.create(klu__serviceURI);
        }
        catch (java.lang.Exception e) {
            throw new java.lang.RuntimeException("Invalid URI for partition partition_2, "+
                "service EmployerService: "+klu__serviceURI, e);
        }

        klu__logger.info("partition_2 EmployerService URI = " + klu__serviceURI);
    }




    // constructor for creating proxy instances of this class using reference ID
    public Employer(CardinalString referenceId) {
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



    // getter method for field "employerName" (generated)
    public  String get__employerName() {
        // create form for service request to pass reference ID
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        klu__logger.info("[Employer] Calling service "+klu__serviceURI+
            "/get__employerName with form: "+form.asMap());
        // call getter service and get encoded response from response JSON (for collection return type)
        Response svc_response;
        try {
            svc_response = klu__client.target(klu__serviceURI)
                .path("get__employerName")
                .request(MediaType.APPLICATION_JSON)
                .method(javax.ws.rs.HttpMethod.GET, Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            java.lang.Throwable cause = wae.getCause();
            klu__logger.warning("Employer] Exception thrown in service call: "+wae.getMessage());
            throw (java.lang.RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        klu__logger.info("[Employer] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return response;
    }



    // setter method for field "employerName" (generated)
    public  void set__employerName(String employerName) {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        form.param("employerName", employerName); 
        klu__logger.info("[Employer] Calling service "+klu__serviceURI+
            "/set__employerName with form: "+form.asMap());

        // call setter service (no response for "void" return type)
        try {
            klu__client.target(klu__serviceURI)
                .path("set__employerName")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            java.lang.Throwable cause = wae.getCause();
            throw (java.lang.RuntimeException)cause;
        }
    }

public Employer() {
        // create form for service request
        Form form = new Form();
        klu__logger.info("[Employer] Calling service "+klu__serviceURI+
            "/Employer_001 with form: "+form.asMap());

        // call constructor service and store ref ID
        Response svc_response;
        try {
            svc_response = 
                klu__client.target(klu__serviceURI) 
                .path("Employer_001") 
                .request(MediaType.APPLICATION_JSON) 
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            java.lang.Throwable cause = wae.getCause();
            klu__logger.warning("[Employer] Exception thrown in service call: "+wae.getMessage());
            throw (java.lang.RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        klu__logger.info("[Employer] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        setKlu__referenceID(response_json.getString("return_value"));
        svc_response.close();
    }
	   
	protected Employer(int employerId, String employerName, List<Employee> employees){
        // create form for service request
        Form form = new Form();
        form.param("employerId", String.valueOf(employerId)); 

        form.param("employerName", employerName); 

        
        // convert physical/proxy object(s) referenced by "employees" to reference ID(s)
        String employees_fpar = SerializationUtil.encodeWithDynamicTypeCheck(employees);
        form.param("employees", employees_fpar);

        klu__logger.info("[Employer] Calling service "+klu__serviceURI+
            "/Employer_002 with form: "+form.asMap());

        // call constructor service and store ref ID
        Response svc_response;
        try {
            svc_response = 
                klu__client.target(klu__serviceURI) 
                .path("Employer_002") 
                .request(MediaType.APPLICATION_JSON) 
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            java.lang.Throwable cause = wae.getCause();
            klu__logger.warning("[Employer] Exception thrown in service call: "+wae.getMessage());
            throw (java.lang.RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        klu__logger.info("[Employer] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        setKlu__referenceID(response_json.getString("return_value"));
        svc_response.close();
    }

	protected int getEmployerId() {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        klu__logger.info("[Employer] Calling service "+klu__serviceURI+
            "/getEmployerId with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = klu__client.target(klu__serviceURI)
                .path("getEmployerId")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            java.lang.Throwable cause = wae.getCause();
            klu__logger.warning("[Employer] Exception thrown in service call: "+wae.getMessage());
            throw (java.lang.RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        klu__logger.info("[Employer] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return Integer.parseInt(response);
    }

	protected void setEmployerId(int employerId) {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        form.param("employerId", String.valueOf(employerId)); 

        klu__logger.info("[Employer] Calling service "+klu__serviceURI+
            "/setEmployerId with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            klu__client.target(klu__serviceURI)
                .path("setEmployerId")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            java.lang.Throwable cause = wae.getCause();
            klu__logger.warning("[Employer] Exception thrown in service call: "+wae.getMessage());
            throw (java.lang.RuntimeException)cause;
        }

    }

	protected void setEmployerAttributes(int employerId, String employerName, List<Employee> employees) {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        form.param("employerId", String.valueOf(employerId)); 

        form.param("employerName", employerName); 

        
        // convert physical/proxy object(s) referenced by "employees" to reference ID(s)
        String employees_fpar = SerializationUtil.encodeWithDynamicTypeCheck(employees);
        form.param("employees", employees_fpar);

        klu__logger.info("[Employer] Calling service "+klu__serviceURI+
            "/setEmployerAttributes with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            klu__client.target(klu__serviceURI)
                .path("setEmployerAttributes")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            java.lang.Throwable cause = wae.getCause();
            klu__logger.warning("[Employer] Exception thrown in service call: "+wae.getMessage());
            throw (java.lang.RuntimeException)cause;
        }

    }

	public void addEmployees(Employee... emps) {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        
        // convert physical/proxy object(s) referenced by "emps" to reference ID(s)
        String emps_fpar = SerializationUtil.encodeWithDynamicTypeCheck(emps);
        form.param("emps", emps_fpar);

        klu__logger.info("[Employer] Calling service "+klu__serviceURI+
            "/addEmployees with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            klu__client.target(klu__serviceURI)
                .path("addEmployees")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            java.lang.Throwable cause = wae.getCause();
            klu__logger.warning("[Employer] Exception thrown in service call: "+wae.getMessage());
            throw (java.lang.RuntimeException)cause;
        }

    }

	public String getEmployerName() {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        klu__logger.info("[Employer] Calling service "+klu__serviceURI+
            "/getEmployerName with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = klu__client.target(klu__serviceURI)
                .path("getEmployerName")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            java.lang.Throwable cause = wae.getCause();
            klu__logger.warning("[Employer] Exception thrown in service call: "+wae.getMessage());
            throw (java.lang.RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        klu__logger.info("[Employer] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return response;
    }

	public void setEmployerName(String employerName) {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        form.param("employerName", employerName); 

        klu__logger.info("[Employer] Calling service "+klu__serviceURI+
            "/setEmployerName with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            klu__client.target(klu__serviceURI)
                .path("setEmployerName")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            java.lang.Throwable cause = wae.getCause();
            klu__logger.warning("[Employer] Exception thrown in service call: "+wae.getMessage());
            throw (java.lang.RuntimeException)cause;
        }

    }

	public List<Employee> getEmployees() {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        klu__logger.info("[Employer] Calling service "+klu__serviceURI+
            "/getEmployees with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = klu__client.target(klu__serviceURI)
                .path("getEmployees")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            java.lang.Throwable cause = wae.getCause();
            klu__logger.warning("[Employer] Exception thrown in service call: "+wae.getMessage());
            throw (java.lang.RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        klu__logger.info("[Employer] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        
        // convert reference ID(s) stored in "response" to physical/proxy object(s)
        List<Employee> response_obj = (List<Employee>)SerializationUtil.decodeWithDynamicTypeCheck(response);
        return response_obj;

    }

	public void setEmployees(List<Employee> employees) {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        
        // convert physical/proxy object(s) referenced by "employees" to reference ID(s)
        String employees_fpar = SerializationUtil.encodeWithDynamicTypeCheck(employees);
        form.param("employees", employees_fpar);

        klu__logger.info("[Employer] Calling service "+klu__serviceURI+
            "/setEmployees with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            klu__client.target(klu__serviceURI)
                .path("setEmployees")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            java.lang.Throwable cause = wae.getCause();
            klu__logger.warning("[Employer] Exception thrown in service call: "+wae.getMessage());
            throw (java.lang.RuntimeException)cause;
        }

    }

}

