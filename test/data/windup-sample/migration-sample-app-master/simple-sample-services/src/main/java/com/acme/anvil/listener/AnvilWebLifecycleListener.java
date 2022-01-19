package com.acme.anvil.listener;

import java.util.Properties;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.log4j.Logger;

import com.acme.anvil.management.AnvilInvokeBeanImpl;

/**
 * See more information on registering MBeans in Weblogic at:
 * 	http://docs.oracle.com/cd/E14571_01/web.1111/e13729/designapp.htm
 * 
 * This serves as an example on how to get Application Context information and register MBeans.
 * 
 * @author bradsdavis
 *
 */
@WebListener
public class AnvilWebLifecycleListener implements ServletContextListener {

	private static Logger LOG = Logger.getLogger(AnvilWebLifecycleListener.class);
	private static final String MBEAN_NAME = "com.acme:Name=anvil,Type=com.acme.anvil.management.AnvilInvokeBeanApplicationLifecycleListener";
	
	
	public void contextInitialized(ServletContextEvent event) {
		String appName = event.getServletContext().getContextPath();
		LOG.info("Initialized Application["+appName+"]");
		registerMBean();
	}
	
	public void contextDestroyed(ServletContextEvent event) {
		String appName = event.getServletContext().getContextPath();
		LOG.info("Post Stop Application["+appName+"]");
		unregisterMBean();
	}
	
	private MBeanServer getMBeanServer() throws NamingException {
		Context context = new InitialContext();
		
		//get reference to the MBean Server...
		MBeanServer server = (MBeanServer) context.lookup("java:comp/jmx/runtime");
		return server;
	}
	
	private void registerMBean() {
		LOG.info("Registering MBeans.");
		
		MBeanServer server;
		try {
			server = getMBeanServer();
			server.registerMBean(new AnvilInvokeBeanImpl(), new ObjectName(MBEAN_NAME));
			LOG.info("Registered MBean["+MBEAN_NAME+"]");
		} catch (Exception e) {
			LOG.error("Exception while registering MBean["+MBEAN_NAME+"]");
		}
	}
	
	private void unregisterMBean() {
		LOG.info("Unregistering MBeans.");
		
		MBeanServer server;
		try {
			server = getMBeanServer();
			server.unregisterMBean(new ObjectName(MBEAN_NAME));
			LOG.info("Unregistered MBean["+MBEAN_NAME+"]");
		} catch (Exception e) {
			LOG.error("Exception while unregistering MBean["+MBEAN_NAME+"]");
		}
	}
}
