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

public class Salary implements KluInterface {
    private String klu__referenceID = "";
    private static String klu__serviceURI;
    private static Client klu__client;
    private static final Logger klu__logger = CardinalLogger.getLogger(Salary.class);

    static {
        klu__client = ClientBuilder.newClient();

        klu__logger.info("Static initializer of Salary of cluster partition_2");

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
        klu__serviceURI += "SalaryService";

        try {
            java.net.URI uri = java.net.URI.create(klu__serviceURI);
        }
        catch (java.lang.Exception e) {
            throw new java.lang.RuntimeException("Invalid URI for partition partition_2, "+
                "service SalaryService: "+klu__serviceURI, e);
        }

        klu__logger.info("partition_2 SalaryService URI = " + klu__serviceURI);
    }




    // constructor for creating proxy instances of this class using reference ID
    public Salary(CardinalString referenceId) {
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




public Salary() {
        // create form for service request
        Form form = new Form();
        klu__logger.info("[Salary] Calling service "+klu__serviceURI+
            "/Salary_001 with form: "+form.asMap());

        // call constructor service and store ref ID
        Response svc_response;
        try {
            svc_response = 
                klu__client.target(klu__serviceURI) 
                .path("Salary_001") 
                .request(MediaType.APPLICATION_JSON) 
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            java.lang.Throwable cause = wae.getCause();
            klu__logger.warning("[Salary] Exception thrown in service call: "+wae.getMessage());
            throw (java.lang.RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        klu__logger.info("[Salary] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        setKlu__referenceID(response_json.getString("return_value"));
        svc_response.close();
    }
	
    public Salary(int employerId, int employeeId, double salary) {
        // create form for service request
        Form form = new Form();
        form.param("employerId", String.valueOf(employerId)); 

        form.param("employeeId", String.valueOf(employeeId)); 

        form.param("salary", String.valueOf(salary)); 

        klu__logger.info("[Salary] Calling service "+klu__serviceURI+
            "/Salary_002 with form: "+form.asMap());

        // call constructor service and store ref ID
        Response svc_response;
        try {
            svc_response = 
                klu__client.target(klu__serviceURI) 
                .path("Salary_002") 
                .request(MediaType.APPLICATION_JSON) 
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            java.lang.Throwable cause = wae.getCause();
            klu__logger.warning("[Salary] Exception thrown in service call: "+wae.getMessage());
            throw (java.lang.RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        klu__logger.info("[Salary] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        setKlu__referenceID(response_json.getString("return_value"));
        svc_response.close();
    }
    
	public int getEmployerId() {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        klu__logger.info("[Salary] Calling service "+klu__serviceURI+
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
            klu__logger.warning("[Salary] Exception thrown in service call: "+wae.getMessage());
            throw (java.lang.RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        klu__logger.info("[Salary] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return Integer.parseInt(response);
    }

	public void setEmployerId(int employerId) {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        form.param("employerId", String.valueOf(employerId)); 

        klu__logger.info("[Salary] Calling service "+klu__serviceURI+
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
            klu__logger.warning("[Salary] Exception thrown in service call: "+wae.getMessage());
            throw (java.lang.RuntimeException)cause;
        }

    }

	public int getEmployeeId() {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        klu__logger.info("[Salary] Calling service "+klu__serviceURI+
            "/getEmployeeId with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = klu__client.target(klu__serviceURI)
                .path("getEmployeeId")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            java.lang.Throwable cause = wae.getCause();
            klu__logger.warning("[Salary] Exception thrown in service call: "+wae.getMessage());
            throw (java.lang.RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        klu__logger.info("[Salary] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return Integer.parseInt(response);
    }

	public void setEmployeeId(int employeeId) {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        form.param("employeeId", String.valueOf(employeeId)); 

        klu__logger.info("[Salary] Calling service "+klu__serviceURI+
            "/setEmployeeId with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            klu__client.target(klu__serviceURI)
                .path("setEmployeeId")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            java.lang.Throwable cause = wae.getCause();
            klu__logger.warning("[Salary] Exception thrown in service call: "+wae.getMessage());
            throw (java.lang.RuntimeException)cause;
        }

    }

	public double getSalary() {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        klu__logger.info("[Salary] Calling service "+klu__serviceURI+
            "/getSalary with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = klu__client.target(klu__serviceURI)
                .path("getSalary")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            java.lang.Throwable cause = wae.getCause();
            klu__logger.warning("[Salary] Exception thrown in service call: "+wae.getMessage());
            throw (java.lang.RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        klu__logger.info("[Salary] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return Double.parseDouble(response);
    }

	public void setSalary(double salary) {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        form.param("salary", String.valueOf(salary)); 

        klu__logger.info("[Salary] Calling service "+klu__serviceURI+
            "/setSalary with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            klu__client.target(klu__serviceURI)
                .path("setSalary")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            java.lang.Throwable cause = wae.getCause();
            klu__logger.warning("[Salary] Exception thrown in service call: "+wae.getMessage());
            throw (java.lang.RuntimeException)cause;
        }

    }

}

