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

public class Employee implements KluInterface {
    private String klu__referenceID = "";
    private static String klu__serviceURI;
    private static Client klu__client;
    private static final Logger klu__logger = CardinalLogger.getLogger(Employee.class);

    static {
        klu__client = ClientBuilder.newClient();

        klu__logger.info("Static initializer of Employee of cluster partition_2");

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
        klu__serviceURI += "EmployeeService";

        try {
            java.net.URI uri = java.net.URI.create(klu__serviceURI);
        }
        catch (java.lang.Exception e) {
            throw new java.lang.RuntimeException("Invalid URI for partition partition_2, "+
                "service EmployeeService: "+klu__serviceURI, e);
        }

        klu__logger.info("partition_2 EmployeeService URI = " + klu__serviceURI);
    }




    // constructor for creating proxy instances of this class using reference ID
    public Employee(CardinalString referenceId) {
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




// 	static {
// 		level = "XYZ";
// 		System.out.println("In Employee static block: level "+level);
// 	}
	
	public String getLevel() {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        klu__logger.info("[Employee] Calling service "+klu__serviceURI+
            "/getLevel with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = klu__client.target(klu__serviceURI)
                .path("getLevel")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            java.lang.Throwable cause = wae.getCause();
            klu__logger.warning("[Employee] Exception thrown in service call: "+wae.getMessage());
            throw (java.lang.RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        klu__logger.info("[Employee] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return response;
    }

	public void setLevel(String level) {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        form.param("level", level); 

        klu__logger.info("[Employee] Calling service "+klu__serviceURI+
            "/setLevel with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            klu__client.target(klu__serviceURI)
                .path("setLevel")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            java.lang.Throwable cause = wae.getCause();
            klu__logger.warning("[Employee] Exception thrown in service call: "+wae.getMessage());
            throw (java.lang.RuntimeException)cause;
        }

    }
	
	public Employee() {
        // create form for service request
        Form form = new Form();
        klu__logger.info("[Employee] Calling service "+klu__serviceURI+
            "/Employee_001 with form: "+form.asMap());

        // call constructor service and store ref ID
        Response svc_response;
        try {
            svc_response = 
                klu__client.target(klu__serviceURI) 
                .path("Employee_001") 
                .request(MediaType.APPLICATION_JSON) 
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            java.lang.Throwable cause = wae.getCause();
            klu__logger.warning("[Employee] Exception thrown in service call: "+wae.getMessage());
            throw (java.lang.RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        klu__logger.info("[Employee] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        setKlu__referenceID(response_json.getString("return_value"));
        svc_response.close();
    }
	
	public Employee(int employeeId, String name, int hours, double rate){
        // create form for service request
        Form form = new Form();
        form.param("employeeId", String.valueOf(employeeId)); 

        form.param("name", name); 

        form.param("hours", String.valueOf(hours)); 

        form.param("rate", String.valueOf(rate)); 

        klu__logger.info("[Employee] Calling service "+klu__serviceURI+
            "/Employee_002 with form: "+form.asMap());

        // call constructor service and store ref ID
        Response svc_response;
        try {
            svc_response = 
                klu__client.target(klu__serviceURI) 
                .path("Employee_002") 
                .request(MediaType.APPLICATION_JSON) 
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            java.lang.Throwable cause = wae.getCause();
            klu__logger.warning("[Employee] Exception thrown in service call: "+wae.getMessage());
            throw (java.lang.RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        klu__logger.info("[Employee] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        setKlu__referenceID(response_json.getString("return_value"));
        svc_response.close();
    }

	public int getHours() {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        klu__logger.info("[Employee] Calling service "+klu__serviceURI+
            "/getHours with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = klu__client.target(klu__serviceURI)
                .path("getHours")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            java.lang.Throwable cause = wae.getCause();
            klu__logger.warning("[Employee] Exception thrown in service call: "+wae.getMessage());
            throw (java.lang.RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        klu__logger.info("[Employee] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return Integer.parseInt(response);
    }

	public void setHours(int hours) {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        form.param("hours", String.valueOf(hours)); 

        klu__logger.info("[Employee] Calling service "+klu__serviceURI+
            "/setHours with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            klu__client.target(klu__serviceURI)
                .path("setHours")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            java.lang.Throwable cause = wae.getCause();
            klu__logger.warning("[Employee] Exception thrown in service call: "+wae.getMessage());
            throw (java.lang.RuntimeException)cause;
        }

    }

	public double getRate() {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        klu__logger.info("[Employee] Calling service "+klu__serviceURI+
            "/getRate with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = klu__client.target(klu__serviceURI)
                .path("getRate")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            java.lang.Throwable cause = wae.getCause();
            klu__logger.warning("[Employee] Exception thrown in service call: "+wae.getMessage());
            throw (java.lang.RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        klu__logger.info("[Employee] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return Double.parseDouble(response);
    }

	public void setRate(double rate) {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        form.param("rate", String.valueOf(rate)); 

        klu__logger.info("[Employee] Calling service "+klu__serviceURI+
            "/setRate with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            klu__client.target(klu__serviceURI)
                .path("setRate")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            java.lang.Throwable cause = wae.getCause();
            klu__logger.warning("[Employee] Exception thrown in service call: "+wae.getMessage());
            throw (java.lang.RuntimeException)cause;
        }

    }

	public int getEmployeeId() {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        klu__logger.info("[Employee] Calling service "+klu__serviceURI+
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
            klu__logger.warning("[Employee] Exception thrown in service call: "+wae.getMessage());
            throw (java.lang.RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        klu__logger.info("[Employee] Response JSON string: "+response_json_str);
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

        klu__logger.info("[Employee] Calling service "+klu__serviceURI+
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
            klu__logger.warning("[Employee] Exception thrown in service call: "+wae.getMessage());
            throw (java.lang.RuntimeException)cause;
        }

    }

    public String getName() {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        klu__logger.info("[Employee] Calling service "+klu__serviceURI+
            "/getName with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = klu__client.target(klu__serviceURI)
                .path("getName")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            java.lang.Throwable cause = wae.getCause();
            klu__logger.warning("[Employee] Exception thrown in service call: "+wae.getMessage());
            throw (java.lang.RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        klu__logger.info("[Employee] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return response;
    }

    public void setName(String name) {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        form.param("name", name); 

        klu__logger.info("[Employee] Calling service "+klu__serviceURI+
            "/setName with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            klu__client.target(klu__serviceURI)
                .path("setName")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            java.lang.Throwable cause = wae.getCause();
            klu__logger.warning("[Employee] Exception thrown in service call: "+wae.getMessage());
            throw (java.lang.RuntimeException)cause;
        }

    }
	
}

