/**
 * (C) Copyright IBM Corporation 2015.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ibm.websphere.samples.daytrader.web.jsf;

import com.ibm.cardinal.util.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import com.ibm.websphere.samples.daytrader.TradeAction;
import com.ibm.websphere.samples.daytrader.entities.AccountDataBean;
import com.ibm.websphere.samples.daytrader.entities.OrderDataBean;
import com.ibm.websphere.samples.daytrader.util.FinancialUtils;
import com.ibm.websphere.samples.daytrader.util.TradeConfig;



public class AccountDataJSF {
    
    private ExternalContext facesExternalContext;

    
    private TradeAction tradeAction;

    private Date sessionCreationDate;
    private Date currentTime;
    private String profileID;
    private Integer accountID;
    private Date creationDate;
    private int loginCount;
    private Date lastLogin;
    private int logoutCount;
    private BigDecimal balance;
    private BigDecimal openBalance;
    private Integer numberHoldings;
    private BigDecimal holdingsTotal;
    private BigDecimal sumOfCashHoldings;
    private BigDecimal gain;
    private BigDecimal gainPercent;

    private OrderData[] closedOrders;
    private OrderData[] allOrders;  
    
    private Integer numberOfOrders = 0;
	private Integer numberOfOrderRows = 5;
	    
	public void toggleShowAllRows() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/AccountDataJSF.java:AccountDataJSF:toggleShowAllRows");
    }
	
    
    public void home() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/AccountDataJSF.java:AccountDataJSF:home");
    }

    private void doAccountData(AccountDataBean accountData, Collection<?> holdingDataBeans) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/AccountDataJSF.java:AccountDataJSF:doAccountData");
    }

    public Date getSessionCreationDate() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/AccountDataJSF.java:AccountDataJSF:getSessionCreationDate");
    }

    public void setSessionCreationDate(Date sessionCreationDate) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/AccountDataJSF.java:AccountDataJSF:setSessionCreationDate");
    }

    public Date getCurrentTime() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/AccountDataJSF.java:AccountDataJSF:getCurrentTime");
    }

    public void setCurrentTime(Date currentTime) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/AccountDataJSF.java:AccountDataJSF:setCurrentTime");
    }

    public String getProfileID() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/AccountDataJSF.java:AccountDataJSF:getProfileID");
    }

    public void setProfileID(String profileID) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/AccountDataJSF.java:AccountDataJSF:setProfileID");
    }

    public void setAccountID(Integer accountID) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/AccountDataJSF.java:AccountDataJSF:setAccountID");
    }

    public Integer getAccountID() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/AccountDataJSF.java:AccountDataJSF:getAccountID");
    }

    public void setCreationDate(Date creationDate) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/AccountDataJSF.java:AccountDataJSF:setCreationDate");
    }

    public Date getCreationDate() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/AccountDataJSF.java:AccountDataJSF:getCreationDate");
    }

    public void setLoginCount(int loginCount) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/AccountDataJSF.java:AccountDataJSF:setLoginCount");
    }

    public int getLoginCount() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/AccountDataJSF.java:AccountDataJSF:getLoginCount");
    }

    public void setBalance(BigDecimal balance) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/AccountDataJSF.java:AccountDataJSF:setBalance");
    }

    public BigDecimal getBalance() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/AccountDataJSF.java:AccountDataJSF:getBalance");
    }

    public void setOpenBalance(BigDecimal openBalance) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/AccountDataJSF.java:AccountDataJSF:setOpenBalance");
    }

    public BigDecimal getOpenBalance() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/AccountDataJSF.java:AccountDataJSF:getOpenBalance");
    }

    public void setHoldingsTotal(BigDecimal holdingsTotal) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/AccountDataJSF.java:AccountDataJSF:setHoldingsTotal");
    }

    public BigDecimal getHoldingsTotal() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/AccountDataJSF.java:AccountDataJSF:getHoldingsTotal");
    }

    public void setSumOfCashHoldings(BigDecimal sumOfCashHoldings) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/AccountDataJSF.java:AccountDataJSF:setSumOfCashHoldings");
    }

    public BigDecimal getSumOfCashHoldings() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/AccountDataJSF.java:AccountDataJSF:getSumOfCashHoldings");
    }

    public void setGain(BigDecimal gain) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/AccountDataJSF.java:AccountDataJSF:setGain");
    }

    public BigDecimal getGain() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/AccountDataJSF.java:AccountDataJSF:getGain");
    }

    public void setGainPercent(BigDecimal gainPercent) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/AccountDataJSF.java:AccountDataJSF:setGainPercent");
    }

    public BigDecimal getGainPercent() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/AccountDataJSF.java:AccountDataJSF:getGainPercent");
    }

    public void setNumberHoldings(Integer numberHoldings) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/AccountDataJSF.java:AccountDataJSF:setNumberHoldings");
    }

    public Integer getNumberHoldings() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/AccountDataJSF.java:AccountDataJSF:getNumberHoldings");
    }

    public OrderData[] getClosedOrders() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/AccountDataJSF.java:AccountDataJSF:getClosedOrders");
    }

    public void setClosedOrders(OrderData[] closedOrders) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/AccountDataJSF.java:AccountDataJSF:setClosedOrders");
    }

    public void setLastLogin(Date lastLogin) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/AccountDataJSF.java:AccountDataJSF:setLastLogin");
    }

    public Date getLastLogin() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/AccountDataJSF.java:AccountDataJSF:getLastLogin");
    }

    public void setLogoutCount(int logoutCount) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/AccountDataJSF.java:AccountDataJSF:setLogoutCount");
    }

    public int getLogoutCount() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/AccountDataJSF.java:AccountDataJSF:getLogoutCount");
    }

    public void setAllOrders(OrderData[] allOrders) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/AccountDataJSF.java:AccountDataJSF:setAllOrders");
    }

    public OrderData[] getAllOrders() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/AccountDataJSF.java:AccountDataJSF:getAllOrders");
    }

    public String getGainHTML() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/AccountDataJSF.java:AccountDataJSF:getGainHTML");
    }

    public String getGainPercentHTML() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/AccountDataJSF.java:AccountDataJSF:getGainPercentHTML");
    }

	public Integer getNumberOfOrderRows() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/AccountDataJSF.java:AccountDataJSF:getNumberOfOrderRows");
    }

	public void setNumberOfOrderRows(Integer numberOfOrderRows) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/AccountDataJSF.java:AccountDataJSF:setNumberOfOrderRows");
    }

    public Integer getNumberOfOrders() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/AccountDataJSF.java:AccountDataJSF:getNumberOfOrders");
    }

    public void setNumberOfOrders(Integer numberOfOrders) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/AccountDataJSF.java:AccountDataJSF:setNumberOfOrders");
    }
}
