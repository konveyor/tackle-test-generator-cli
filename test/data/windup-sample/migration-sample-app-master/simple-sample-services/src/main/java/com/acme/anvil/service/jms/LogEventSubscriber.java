package com.acme.anvil.service.jms;


import java.text.SimpleDateFormat;

import javax.ejb.MessageDriven;
import javax.annotation.security.PermitAll;
import javax.ejb.ActivationConfigProperty;
import javax.jms.JMSDestinationDefinition;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.apache.log4j.Logger;

import com.acme.anvil.vo.LogEvent;

@JMSDestinationDefinition(
	    name = LogEventPublisher.QUEUE_JNDI_NAME,
	    destinationName="LogEventQueue", 
	    description="Log Event Queue", 
	    interfaceName = "javax.jms.Queue") 
@MessageDriven(
	activationConfig = {
	    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
	    @ActivationConfigProperty(propertyName = "destination", propertyValue = "LogEventQueue"),
	}
)
public class LogEventSubscriber implements MessageListener {

	private static final Logger LOG = Logger.getLogger(LogEventSubscriber.class);
	private static final SimpleDateFormat SDF = new SimpleDateFormat("MM/dd/yyyy 'at' HH:mm:ss z");
	
	@PermitAll
	public void onMessage(Message msg) {
		ObjectMessage om = (ObjectMessage)msg;
		Object obj;
		try {
			obj = om.getObject();
			
			if(obj instanceof LogEvent) {
				LogEvent event = (LogEvent)obj;
				LOG.info("Log Event ["+SDF.format(event.getDate())+"] : "+event.getMessage());
			}
		} catch (JMSException e) {
			LOG.error("Exception reading message.", e);
		}
	}
}
