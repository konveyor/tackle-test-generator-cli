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
import java.util.*;

/**
 * Service class for IRS - Generated by Cardinal
 */

@Path("/IRSService")
public class IRSService {
    private static final Logger klu__logger = CardinalLogger.getLogger(IRSService.class);

    // default constructor service
    @POST
    @Path("/IRS_default_ctor")
    @Produces(MediaType.APPLICATION_JSON) 
    public Response IRS_default_ctor() {
        IRS instIRS = new IRS();
        String refid = ClusterObjectManager.putObject(instIRS);
        instIRS.setKlu__referenceID(refid);
        JsonObject jsonobj = Json
            .createObjectBuilder()
            .add("return_value", refid)
            .build();
        klu__logger.info("[IRSService] Returning JSON object: "+jsonobj.toString());
        return Response
            .status(Response.Status.OK)
            .entity(jsonobj)
            .build();
    }



    // health check service
    @GET 
    @Path("/health") 
    @Produces(MediaType.TEXT_HTML) 
    public String getHealth() { 
        klu__logger.info("[IRS] getHealth() called");
        return "IRSService::Health OK"; 
    }



    // service for incrementing object reference count
    @POST
    @Path("/incObjectCount")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void incObjectCount(@FormParam("klu__referenceID") String klu__referenceID) {
        klu__logger.info("[IRSService] incObjectCount() called with ref: "+klu__referenceID);
        ClusterObjectManager.incObjectCount(klu__referenceID);
    }



    // service for decrementing object reference count
    @POST
    @Path("/decObjectCount")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void decObjectCount(@FormParam("klu__referenceID") String klu__referenceID) {
        klu__logger.info("[IRS] decObjectCount() called with ref: "+klu__referenceID);
        ClusterObjectManager.decObjectCount(klu__referenceID);
    }





    @POST
    @Path("/getYear")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getYear(
        @FormParam("klu__referenceID") String klu__referenceID,
        @Context HttpServletResponse servletResponse
    ) {

        int response;

        // dynamically dispatched method: using reference ID, get object from cluster object manager and
        // call method on the object
        IRS instIRS = (IRS)ClusterObjectManager.getObject(klu__referenceID);

        try {
            response = instIRS.getYear();
        }
        catch (java.lang.Throwable t) {
            String msg = "Call to method getYear() of IRS raised exception: "+t.getMessage();
            klu__logger.warning(msg);
            throw new WebApplicationException(msg, t, CardinalException.APPLICATION_EXCEPTION);
        }
        JsonObjectBuilder jsonresp = Json.createObjectBuilder();
        JsonObject jsonobj = jsonresp.add("return_value", String.valueOf(response)).build();
        klu__logger.info("[IRS] Returning JSON object: "+jsonobj.toString());
        return Response
            .status(Response.Status.OK)
            .entity(jsonobj)
            .build();

    }

    @POST
    @Path("/setYear")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void setYear(
        @FormParam("klu__referenceID") String klu__referenceID,
        @FormParam("yearr") String yearr,
        @Context HttpServletResponse servletResponse
    ) {

        int yearr_fpar = Integer.parseInt(yearr);

        // dynamically dispatched method: using reference ID, get object from cluster object manager and
        // call method on the object
        IRS instIRS = (IRS)ClusterObjectManager.getObject(klu__referenceID);

        try {
            instIRS.setYear(yearr_fpar);
        }
        catch (java.lang.Throwable t) {
            String msg = "Call to method setYear() of IRS raised exception: "+t.getMessage();
            klu__logger.warning(msg);
            throw new WebApplicationException(msg, t, CardinalException.APPLICATION_EXCEPTION);
        }

    }

    @POST
    @Path("/getSalaryList")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSalaryList(
        @FormParam("klu__referenceID") String klu__referenceID,
        @Context HttpServletResponse servletResponse
    ) {

        List<Salary> response;

        // dynamically dispatched method: using reference ID, get object from cluster object manager and
        // call method on the object
        IRS instIRS = (IRS)ClusterObjectManager.getObject(klu__referenceID);

        try {
            response = instIRS.getSalaryList();
        }
        catch (java.lang.Throwable t) {
            String msg = "Call to method getSalaryList() of IRS raised exception: "+t.getMessage();
            klu__logger.warning(msg);
            throw new WebApplicationException(msg, t, CardinalException.APPLICATION_EXCEPTION);
        }
        JsonObjectBuilder jsonresp = Json.createObjectBuilder();
        
        // convert physical/proxy object(s) referenced by "response" to reference ID(s)
        String response_obj = SerializationUtil.encodeWithDynamicTypeCheck(response);
        JsonObject jsonobj = jsonresp.add("return_value", response_obj).build();

        klu__logger.info("[IRS] Returning JSON object: "+jsonobj.toString());
        return Response
            .status(Response.Status.OK)
            .entity(jsonobj)
            .build();

    }

    @POST
    @Path("/setSalaryList")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void setSalaryList(
        @FormParam("klu__referenceID") String klu__referenceID,
        @FormParam("salaryList") String salaryList,
        @Context HttpServletResponse servletResponse
    ) {

        
        // convert reference ID(s) stored in "salaryList" to physical/proxy object(s)
        List<Salary> salaryList_fpar = (List<Salary>)SerializationUtil.decodeWithDynamicTypeCheck(salaryList);

        // dynamically dispatched method: using reference ID, get object from cluster object manager and
        // call method on the object
        IRS instIRS = (IRS)ClusterObjectManager.getObject(klu__referenceID);

        try {
            instIRS.setSalaryList(salaryList_fpar);
        }
        catch (java.lang.Throwable t) {
            String msg = "Call to method setSalaryList() of IRS raised exception: "+t.getMessage();
            klu__logger.warning(msg);
            throw new WebApplicationException(msg, t, CardinalException.APPLICATION_EXCEPTION);
        }

    }

    @POST
    @Path("/getSalaryfromIRS")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSalaryfromIRS(
        @FormParam("klu__referenceID") String klu__referenceID,
        @FormParam("employeeId") String employeeId,
        @Context HttpServletResponse servletResponse
    ) {

        int employeeId_fpar = Integer.parseInt(employeeId);

        Salary response;

        // dynamically dispatched method: using reference ID, get object from cluster object manager and
        // call method on the object
        IRS instIRS = (IRS)ClusterObjectManager.getObject(klu__referenceID);

        try {
            response = instIRS.getSalaryfromIRS(employeeId_fpar);
        }
        catch (java.lang.Throwable t) {
            String msg = "Call to method getSalaryfromIRS() of IRS raised exception: "+t.getMessage();
            klu__logger.warning(msg);
            throw new WebApplicationException(msg, t, CardinalException.APPLICATION_EXCEPTION);
        }
        JsonObjectBuilder jsonresp = Json.createObjectBuilder();
        
        // convert physical/proxy object(s) referenced by "response" to reference ID(s)
        String response_obj = SerializationUtil.encodeWithDynamicTypeCheck(response);
        JsonObject jsonobj = jsonresp.add("return_value", response_obj).build();

        klu__logger.info("[IRS] Returning JSON object: "+jsonobj.toString());
        return Response
            .status(Response.Status.OK)
            .entity(jsonobj)
            .build();

    }

    @POST
    @Path("/getSalarySet")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSalarySet(
        @FormParam("klu__referenceID") String klu__referenceID,
        @FormParam("employeeId") String employeeId,
        @Context HttpServletResponse servletResponse
    ) {

        int employeeId_fpar = Integer.parseInt(employeeId);

        Set<Salary> response;

        // dynamically dispatched method: using reference ID, get object from cluster object manager and
        // call method on the object
        IRS instIRS = (IRS)ClusterObjectManager.getObject(klu__referenceID);

        try {
            response = instIRS.getSalarySet(employeeId_fpar);
        }
        catch (java.lang.Throwable t) {
            String msg = "Call to method getSalarySet() of IRS raised exception: "+t.getMessage();
            klu__logger.warning(msg);
            throw new WebApplicationException(msg, t, CardinalException.APPLICATION_EXCEPTION);
        }
        JsonObjectBuilder jsonresp = Json.createObjectBuilder();
        
        // convert physical/proxy object(s) referenced by "response" to reference ID(s)
        String response_obj = SerializationUtil.encodeWithDynamicTypeCheck(response);
        JsonObject jsonobj = jsonresp.add("return_value", response_obj).build();

        klu__logger.info("[IRS] Returning JSON object: "+jsonobj.toString());
        return Response
            .status(Response.Status.OK)
            .entity(jsonobj)
            .build();

    }

    @POST
    @Path("/getAllSalarySets")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllSalarySets(
        @FormParam("klu__referenceID") String klu__referenceID,
        @Context HttpServletResponse servletResponse
    ) {

        Map<Integer, Set<Salary>> response;

        // dynamically dispatched method: using reference ID, get object from cluster object manager and
        // call method on the object
        IRS instIRS = (IRS)ClusterObjectManager.getObject(klu__referenceID);

        try {
            response = instIRS.getAllSalarySets();
        }
        catch (java.lang.Throwable t) {
            String msg = "Call to method getAllSalarySets() of IRS raised exception: "+t.getMessage();
            klu__logger.warning(msg);
            throw new WebApplicationException(msg, t, CardinalException.APPLICATION_EXCEPTION);
        }
        JsonObjectBuilder jsonresp = Json.createObjectBuilder();
        
        // convert physical/proxy object(s) referenced by "response" to reference ID(s)
        String response_obj = SerializationUtil.encodeWithDynamicTypeCheck(response);
        JsonObject jsonobj = jsonresp.add("return_value", response_obj).build();

        klu__logger.info("[IRS] Returning JSON object: "+jsonobj.toString());
        return Response
            .status(Response.Status.OK)
            .entity(jsonobj)
            .build();

    }

    @POST
    @Path("/setAllSalarySets")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void setAllSalarySets(
        @FormParam("klu__referenceID") String klu__referenceID,
        @FormParam("employerSalarySet") String employerSalarySet,
        @Context HttpServletResponse servletResponse
    ) {

        
        // convert reference ID(s) stored in "employerSalarySet" to physical/proxy object(s)
        Map<Integer, Set<Salary>> employerSalarySet_fpar = (Map<Integer, Set<Salary>>)SerializationUtil.decodeWithDynamicTypeCheck(employerSalarySet);

        // dynamically dispatched method: using reference ID, get object from cluster object manager and
        // call method on the object
        IRS instIRS = (IRS)ClusterObjectManager.getObject(klu__referenceID);

        try {
            instIRS.setAllSalarySets(employerSalarySet_fpar);
        }
        catch (java.lang.Throwable t) {
            String msg = "Call to method setAllSalarySets() of IRS raised exception: "+t.getMessage();
            klu__logger.warning(msg);
            throw new WebApplicationException(msg, t, CardinalException.APPLICATION_EXCEPTION);
        }

    }

}