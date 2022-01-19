package com.acme.anvil.service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.InvalidTransactionException;
import javax.transaction.SystemException;

import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.TransactionTimeout;

import com.acme.anvil.service.jms.LogEventPublisher;
import com.acme.anvil.vo.Item;
import com.acme.anvil.vo.LogEvent;

public class ItemLookupBean {

	private static final Logger LOG = Logger.getLogger(ItemLookup.class);
	
	@Inject
	LogEventPublisher logEventPublisher;
	
	public Item lookupItem(long id) throws SystemException, InvalidTransactionException {
		LOG.info("Calling lookupItem.");
		
		//stubbed out.
		Item item = new Item();
		item.setId(id);
		
		final LogEvent le = new LogEvent(new Date(), "Returning Item: "+id); 
		logEventPublisher.publishLogEvent(le);
		
		return item;
	}
}
