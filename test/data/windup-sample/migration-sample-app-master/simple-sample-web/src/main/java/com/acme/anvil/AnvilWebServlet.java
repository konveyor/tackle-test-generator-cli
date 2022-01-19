package com.acme.anvil;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.acme.anvil.service.ItemLookupLocal;
import com.acme.anvil.service.ItemLookupLocalHome;
import com.acme.anvil.vo.Item;

public class AnvilWebServlet extends HttpServlet {

	private static final Logger LOG = Logger.getLogger(AnvilWebServlet.class.getName());
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LOG.info("Started AnvilWebServlet request");
		LOG.info("User principal: " + req.getUserPrincipal());
		LOG.info("Is in acme_user role? " + req.isUserInRole("acme_user"));
		
		InitialContext ic;
		ItemLookupLocal local;
		try {
			ic = new InitialContext();
			LOG.info("Retrieved initial context");
			local  = (ItemLookupLocal)ic.lookup("java:global/simple-sample-app/simple-sample-services/ItemLookupBean!com.acme.anvil.service.ItemLookupLocal");
			LOG.info("ItemLookupLocal: " + local);
			
			String itemId = req.getParameter("id");
			if(StringUtils.isNotBlank(itemId)) {
				Long id = Long.parseLong(itemId);
				Item item = local.lookupItem(id);
				LOG.info("For id: " + id + ", found item: " + item);
				renderResult(resp, item);
			} else {
				resp.getWriter().write("'id' parameter must be specified");
			}
		} catch (EJBException e) {
			LOG.log(Level.SEVERE, "Exception creating EJB.", e);
		} catch (NamingException e) {
			LOG.log(Level.SEVERE, "Exception looking up EJB LocalHome.", e);
		}
	}
	
	private void renderResult(HttpServletResponse resp, Item item) throws IOException {
		resp.getWriter().write("<html><head><title>Lookup Result</title></head><body>");
		resp.getWriter().write("<h2>Result is: " + item.getId() + "</h2>");
		resp.getWriter().write("<a href='index.html'>Go back</a>");
		resp.getWriter().write("</body></html>");
	}
}
