package irs;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

import com.ibm.cardinal.util.CardinalException;
import com.ibm.cardinal.util.CardinalLogger;
import com.ibm.cardinal.util.CardinalString;
import com.ibm.cardinal.util.ClusterObjectManager;
import com.ibm.cardinal.util.SerializationUtil;

/**
 * Service class for Employee - Generated by Cardinal
 */

@Path("/EmployeeService")
public class EmployeeService {
    private static final Logger klu__logger = CardinalLogger.getLogger(EmployeeService.class);




    // health check service
    @GET 
    @Path("/health") 
    @Produces(MediaType.TEXT_HTML) 
    public String getHealth() { 
        klu__logger.info("[Employee] getHealth() called");
        return "EmployeeService::Health OK"; 
    }



    // service for incrementing object reference count
    @POST
    @Path("/incObjectCount")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void incObjectCount(@FormParam("klu__referenceID") String klu__referenceID) {
        klu__logger.info("[EmployeeService] incObjectCount() called with ref: "+klu__referenceID);
        ClusterObjectManager.incObjectCount(klu__referenceID);
    }



    // service for decrementing object reference count
    @POST
    @Path("/decObjectCount")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void decObjectCount(@FormParam("klu__referenceID") String klu__referenceID) {
        klu__logger.info("[Employee] decObjectCount() called with ref: "+klu__referenceID);
        ClusterObjectManager.decObjectCount(klu__referenceID);
    }





    @POST
    @Path("/getLevel")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLevel(
        @FormParam("klu__referenceID") String klu__referenceID,
        @Context HttpServletResponse servletResponse
    ) {

        String response;

        // dynamically dispatched method: using reference ID, get object from cluster object manager and
        // call method on the object
        Employee instEmployee = (Employee)ClusterObjectManager.getObject(klu__referenceID);

        try {
            response = instEmployee.getLevel();
        }
        catch (java.lang.Throwable t) {
            String msg = "Call to method getLevel() of Employee raised exception: "+t.getMessage();
            klu__logger.warning(msg);
            throw new WebApplicationException(msg, t, CardinalException.APPLICATION_EXCEPTION);
        }
        JsonObjectBuilder jsonresp = Json.createObjectBuilder();
        JsonObject jsonobj = jsonresp.add("return_value", response).build();
        klu__logger.info("[Employee] Returning JSON object: "+jsonobj.toString());
        return Response
            .status(Response.Status.OK)
            .entity(jsonobj)
            .build();

    }

    @POST
    @Path("/setLevel")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void setLevel(
        @FormParam("klu__referenceID") String klu__referenceID,
        @FormParam("level") String level,
        @Context HttpServletResponse servletResponse
    ) {

        String level_fpar = level;

        // dynamically dispatched method: using reference ID, get object from cluster object manager and
        // call method on the object
        Employee instEmployee = (Employee)ClusterObjectManager.getObject(klu__referenceID);

        try {
            instEmployee.setLevel(level_fpar);
        }
        catch (java.lang.Throwable t) {
            String msg = "Call to method setLevel() of Employee raised exception: "+t.getMessage();
            klu__logger.warning(msg);
            throw new WebApplicationException(msg, t, CardinalException.APPLICATION_EXCEPTION);
        }

    }

    @POST
    @Path("/Employee_001")
    @Produces(MediaType.APPLICATION_JSON)
    public Response Employee_001(
        @Context HttpServletResponse servletResponse
    ) {

        // call constructor, add created object to cluster object manager, and return ref ID
        Employee instEmployee;
        try {
            instEmployee = new Employee();
        }
        catch (java.lang.Throwable t) {
            String msg = "Call to constructor Employee() raised exception: "+t.getMessage();
            klu__logger.warning(msg);
            throw new WebApplicationException(msg, t, CardinalException.APPLICATION_EXCEPTION);
        }
        String refid = ClusterObjectManager.putObject(instEmployee);
        instEmployee.setKlu__referenceID(refid);
        JsonObject jsonobj = Json
            .createObjectBuilder()
            .add("return_value", refid)
            .build();
        return Response
            .status(Response.Status.OK)
            .entity(jsonobj)
            .build();
    }

    @POST
    @Path("/Employee_002")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response Employee_002(
        @FormParam("employeeId") String employeeId,
        @FormParam("name") String name,
        @FormParam("hours") String hours,
        @FormParam("rate") String rate,
        @Context HttpServletResponse servletResponse
    ) {

        int employeeId_fpar = Integer.parseInt(employeeId);

        String name_fpar = name;

        int hours_fpar = Integer.parseInt(hours);

        double rate_fpar = Double.parseDouble(rate);

        // call constructor, add created object to cluster object manager, and return ref ID
        Employee instEmployee;
        try {
            instEmployee = new Employee(employeeId_fpar, name_fpar, hours_fpar, rate_fpar);
        }
        catch (java.lang.Throwable t) {
            String msg = "Call to constructor Employee() raised exception: "+t.getMessage();
            klu__logger.warning(msg);
            throw new WebApplicationException(msg, t, CardinalException.APPLICATION_EXCEPTION);
        }
        String refid = ClusterObjectManager.putObject(instEmployee);
        instEmployee.setKlu__referenceID(refid);
        JsonObject jsonobj = Json
            .createObjectBuilder()
            .add("return_value", refid)
            .build();
        return Response
            .status(Response.Status.OK)
            .entity(jsonobj)
            .build();
    }

    @POST
    @Path("/getHours")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getHours(
        @FormParam("klu__referenceID") String klu__referenceID,
        @Context HttpServletResponse servletResponse
    ) {

        int response;

        // dynamically dispatched method: using reference ID, get object from cluster object manager and
        // call method on the object
        Employee instEmployee = (Employee)ClusterObjectManager.getObject(klu__referenceID);

        try {
            response = instEmployee.getHours();
        }
        catch (java.lang.Throwable t) {
            String msg = "Call to method getHours() of Employee raised exception: "+t.getMessage();
            klu__logger.warning(msg);
            throw new WebApplicationException(msg, t, CardinalException.APPLICATION_EXCEPTION);
        }
        JsonObjectBuilder jsonresp = Json.createObjectBuilder();
        JsonObject jsonobj = jsonresp.add("return_value", String.valueOf(response)).build();
        klu__logger.info("[Employee] Returning JSON object: "+jsonobj.toString());
        return Response
            .status(Response.Status.OK)
            .entity(jsonobj)
            .build();

    }

    @POST
    @Path("/setHours")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void setHours(
        @FormParam("klu__referenceID") String klu__referenceID,
        @FormParam("hours") String hours,
        @Context HttpServletResponse servletResponse
    ) {

        int hours_fpar = Integer.parseInt(hours);

        // dynamically dispatched method: using reference ID, get object from cluster object manager and
        // call method on the object
        Employee instEmployee = (Employee)ClusterObjectManager.getObject(klu__referenceID);

        try {
            instEmployee.setHours(hours_fpar);
        }
        catch (java.lang.Throwable t) {
            String msg = "Call to method setHours() of Employee raised exception: "+t.getMessage();
            klu__logger.warning(msg);
            throw new WebApplicationException(msg, t, CardinalException.APPLICATION_EXCEPTION);
        }

    }

    @POST
    @Path("/getRate")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRate(
        @FormParam("klu__referenceID") String klu__referenceID,
        @Context HttpServletResponse servletResponse
    ) {

        double response;

        // dynamically dispatched method: using reference ID, get object from cluster object manager and
        // call method on the object
        Employee instEmployee = (Employee)ClusterObjectManager.getObject(klu__referenceID);

        try {
            response = instEmployee.getRate();
        }
        catch (java.lang.Throwable t) {
            String msg = "Call to method getRate() of Employee raised exception: "+t.getMessage();
            klu__logger.warning(msg);
            throw new WebApplicationException(msg, t, CardinalException.APPLICATION_EXCEPTION);
        }
        JsonObjectBuilder jsonresp = Json.createObjectBuilder();
        JsonObject jsonobj = jsonresp.add("return_value", String.valueOf(response)).build();
        klu__logger.info("[Employee] Returning JSON object: "+jsonobj.toString());
        return Response
            .status(Response.Status.OK)
            .entity(jsonobj)
            .build();

    }

    @POST
    @Path("/setRate")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void setRate(
        @FormParam("klu__referenceID") String klu__referenceID,
        @FormParam("rate") String rate,
        @Context HttpServletResponse servletResponse
    ) {

        double rate_fpar = Double.parseDouble(rate);

        // dynamically dispatched method: using reference ID, get object from cluster object manager and
        // call method on the object
        Employee instEmployee = (Employee)ClusterObjectManager.getObject(klu__referenceID);

        try {
            instEmployee.setRate(rate_fpar);
        }
        catch (java.lang.Throwable t) {
            String msg = "Call to method setRate() of Employee raised exception: "+t.getMessage();
            klu__logger.warning(msg);
            throw new WebApplicationException(msg, t, CardinalException.APPLICATION_EXCEPTION);
        }

    }

    @POST
    @Path("/getEmployeeId")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEmployeeId(
        @FormParam("klu__referenceID") String klu__referenceID,
        @Context HttpServletResponse servletResponse
    ) {

        int response;

        // dynamically dispatched method: using reference ID, get object from cluster object manager and
        // call method on the object
        Employee instEmployee = (Employee)ClusterObjectManager.getObject(klu__referenceID);

        try {
            response = instEmployee.getEmployeeId();
        }
        catch (java.lang.Throwable t) {
            String msg = "Call to method getEmployeeId() of Employee raised exception: "+t.getMessage();
            klu__logger.warning(msg);
            throw new WebApplicationException(msg, t, CardinalException.APPLICATION_EXCEPTION);
        }
        JsonObjectBuilder jsonresp = Json.createObjectBuilder();
        JsonObject jsonobj = jsonresp.add("return_value", String.valueOf(response)).build();
        klu__logger.info("[Employee] Returning JSON object: "+jsonobj.toString());
        return Response
            .status(Response.Status.OK)
            .entity(jsonobj)
            .build();

    }

    @POST
    @Path("/setEmployeeId")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void setEmployeeId(
        @FormParam("klu__referenceID") String klu__referenceID,
        @FormParam("employeeId") String employeeId,
        @Context HttpServletResponse servletResponse
    ) {

        int employeeId_fpar = Integer.parseInt(employeeId);

        // dynamically dispatched method: using reference ID, get object from cluster object manager and
        // call method on the object
        Employee instEmployee = (Employee)ClusterObjectManager.getObject(klu__referenceID);

        try {
            instEmployee.setEmployeeId(employeeId_fpar);
        }
        catch (java.lang.Throwable t) {
            String msg = "Call to method setEmployeeId() of Employee raised exception: "+t.getMessage();
            klu__logger.warning(msg);
            throw new WebApplicationException(msg, t, CardinalException.APPLICATION_EXCEPTION);
        }

    }

    @POST
    @Path("/getName")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getName(
        @FormParam("klu__referenceID") String klu__referenceID,
        @Context HttpServletResponse servletResponse
    ) {

        String response;

        // dynamically dispatched method: using reference ID, get object from cluster object manager and
        // call method on the object
        Employee instEmployee = (Employee)ClusterObjectManager.getObject(klu__referenceID);

        try {
            response = instEmployee.getName();
        }
        catch (java.lang.Throwable t) {
            String msg = "Call to method getName() of Employee raised exception: "+t.getMessage();
            klu__logger.warning(msg);
            throw new WebApplicationException(msg, t, CardinalException.APPLICATION_EXCEPTION);
        }
        JsonObjectBuilder jsonresp = Json.createObjectBuilder();
        JsonObject jsonobj = jsonresp.add("return_value", response).build();
        klu__logger.info("[Employee] Returning JSON object: "+jsonobj.toString());
        return Response
            .status(Response.Status.OK)
            .entity(jsonobj)
            .build();

    }

    @POST
    @Path("/setName")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void setName(
        @FormParam("klu__referenceID") String klu__referenceID,
        @FormParam("name") String name,
        @Context HttpServletResponse servletResponse
    ) {

        String name_fpar = name;

        // dynamically dispatched method: using reference ID, get object from cluster object manager and
        // call method on the object
        Employee instEmployee = (Employee)ClusterObjectManager.getObject(klu__referenceID);

        try {
            instEmployee.setName(name_fpar);
        }
        catch (java.lang.Throwable t) {
            String msg = "Call to method setName() of Employee raised exception: "+t.getMessage();
            klu__logger.warning(msg);
            throw new WebApplicationException(msg, t, CardinalException.APPLICATION_EXCEPTION);
        }

    }

}