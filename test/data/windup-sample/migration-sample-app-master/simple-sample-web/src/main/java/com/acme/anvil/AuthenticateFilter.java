package com.acme.anvil;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.time.DateUtils;

public class AuthenticateFilter implements Filter {

	private static final Logger LOG = Logger.getLogger("AuthenticateFilter");
	
	public void destroy() {
		LOG.fine("AuthenticateFilter destroy.");
	}

	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
	    HttpServletRequest request = (HttpServletRequest)req;
	    HttpSession session = request.getSession();
	    
		LOG.info("AuthenticateFilter doFilter.");
		if(req.getAttribute("cancelSession") != null) {
			LOG.info("Cancelled session due to cancelSession attribute.");
			request.logout();
			//ServletAuthentication.invalidateAll(request);
		}
		else if(session != null) {
			Date fiveMinutesAgo = DateUtils.addMinutes(new Date(), -5);
			//check that the time the session was last accessed was after 5 minutes ago..
			Date timeLastAccessed = new Date(session.getLastAccessedTime());
			
			if(timeLastAccessed.before(fiveMinutesAgo)) {
				LOG.info("Cancelled session due to age");
				session.invalidate();
				//make the user log back in.
				request.logout();
				//ServletAuthentication.invalidateAll(request);
			}
		} 
		chain.doFilter(req, resp);
	}

	public void init(FilterConfig config) throws ServletException {
		LOG.info("AuthenticateFilter init.");
	}

}
