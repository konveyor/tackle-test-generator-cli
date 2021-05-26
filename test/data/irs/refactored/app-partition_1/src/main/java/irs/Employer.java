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
    private static String serviceURI;
    private static Client client;
    private static final Logger logger = CardinalLogger.getLogger(Employer.class);

    static {
        client = ClientBuilder.newClient();

        logger.info("Static initializer of Employer of cluster partition_2");

        serviceURI = System.getenv().get("IRS_PARTITION_2_REST_URL");
        if (serviceURI == null) {
            throw new RuntimeException("Environment variable "+
                "IRS_PARTITION_2_REST_URL not set\n"+
                "Please set IRS_PARTITION_2_REST_URL to "+
                "partition_2 host:port"
            );
        }

        if (!serviceURI.endsWith("/")) {
            serviceURI += "/";
        }
        serviceURI += "EmployerService";

        try {
            java.net.URI uri = java.net.URI.create(serviceURI);
        }
        catch (Exception e) {
            throw new RuntimeException("Invalid URI for partition partition_2, "+
                "service EmployerService: "+serviceURI, e);
        }

        logger.info("partition_2 EmployerService URI = " + serviceURI);
    }




    // constructor for creating proxy instances of this class using reference ID
    public Employer(CardinalString referenceId) {
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



    // getter method for field "employerName" (generated)
    public  String get__employerName() {
        // create form for service request to pass reference ID
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        logger.info("[Employer] Calling service "+serviceURI+
            "/get__employerName with form: "+form.asMap());
        // call getter service and get encoded response from response JSON (for collection return type)
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("get__employerName")
                .request(MediaType.APPLICATION_JSON)
                .method(javax.ws.rs.HttpMethod.GET, Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("Employer] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[Employer] Response JSON string: "+response_json_str);
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
        logger.info("[Employer] Calling service "+serviceURI+
            "/set__employerName with form: "+form.asMap());

        // call setter service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("set__employerName")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            throw (RuntimeException)cause;
        }
    }


	
	public Employer() {
        // create form for service request
        Form form = new Form();
        logger.info("[Employer] Calling service "+serviceURI+
            "/Employer_001 with form: "+form.asMap());

        // call constructor service and store ref ID
        Response svc_response;
        try {
            svc_response = 
                client.target(serviceURI) 
                .path("Employer_001") 
                .request(MediaType.APPLICATION_JSON) 
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[Employer] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[Employer] Response JSON string: "+response_json_str);
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

        logger.info("[Employer] Calling service "+serviceURI+
            "/Employer_002 with form: "+form.asMap());

        // call constructor service and store ref ID
        Response svc_response;
        try {
            svc_response = 
                client.target(serviceURI) 
                .path("Employer_002") 
                .request(MediaType.APPLICATION_JSON) 
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[Employer] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[Employer] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        setKlu__referenceID(response_json.getString("return_value"));
        svc_response.close();
    }

	protected int getEmployerId() {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        logger.info("[Employer] Calling service "+serviceURI+
            "/getEmployerId with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("getEmployerId")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[Employer] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[Employer] Response JSON string: "+response_json_str);
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

        logger.info("[Employer] Calling service "+serviceURI+
            "/setEmployerId with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("setEmployerId")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[Employer] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
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

        logger.info("[Employer] Calling service "+serviceURI+
            "/setEmployerAttributes with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("setEmployerAttributes")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[Employer] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }

    }

	public void addEmployees(Employee... emps) {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        
        // convert physical/proxy object(s) referenced by "emps" to reference ID(s)
        String emps_fpar = SerializationUtil.encodeWithDynamicTypeCheck(emps);
        form.param("emps", emps_fpar);

        logger.info("[Employer] Calling service "+serviceURI+
            "/addEmployees with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("addEmployees")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[Employer] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }

    }

	public String getEmployerName() {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        logger.info("[Employer] Calling service "+serviceURI+
            "/getEmployerName with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("getEmployerName")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[Employer] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[Employer] Response JSON string: "+response_json_str);
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

        logger.info("[Employer] Calling service "+serviceURI+
            "/setEmployerName with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("setEmployerName")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[Employer] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }

    }

	public List<Employee> getEmployees() {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        logger.info("[Employer] Calling service "+serviceURI+
            "/getEmployees with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = client.target(serviceURI)
                .path("getEmployees")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[Employer] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        logger.info("[Employer] Response JSON string: "+response_json_str);
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

        logger.info("[Employer] Calling service "+serviceURI+
            "/setEmployees with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            client.target(serviceURI)
                .path("setEmployees")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            Throwable cause = wae.getCause();
            logger.warning("[Employer] Exception thrown in service call: "+wae.getMessage());
            throw (RuntimeException)cause;
        }

    }

}
