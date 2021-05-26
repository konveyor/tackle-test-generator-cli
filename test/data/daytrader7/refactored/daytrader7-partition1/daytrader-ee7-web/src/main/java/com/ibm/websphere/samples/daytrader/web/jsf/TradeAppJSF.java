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

import java.io.Serializable;
import java.math.BigDecimal;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.ibm.websphere.samples.daytrader.TradeAction;
import com.ibm.websphere.samples.daytrader.entities.AccountDataBean;
import com.ibm.websphere.samples.daytrader.entities.AccountProfileDataBean;
import com.ibm.websphere.samples.daytrader.util.Log;



public class TradeAppJSF implements Serializable {
	
    
    private ExternalContext facesExternalContext;

    
    private TradeAction tradeAction;

    private static final long serialVersionUID = 2L;
    private String userID = "uid:0";
    private String password = "xxx";
    private String cpassword;
    private String results;
    private String fullname;
    private String address;
    private String email;
    private String ccn;
    private String money;

    public String login() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/TradeAppJSF.java:TradeAppJSF:login");
    }

    public String register() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/TradeAppJSF.java:TradeAppJSF:register");
    }

    public String updateProfile() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/TradeAppJSF.java:TradeAppJSF:updateProfile");
    }

    public String logout() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/TradeAppJSF.java:TradeAppJSF:logout");
    }

    public String getUserID() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/TradeAppJSF.java:TradeAppJSF:getUserID");
    }

    public void setUserID(String userID) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/TradeAppJSF.java:TradeAppJSF:setUserID");
    }

    public String getPassword() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/TradeAppJSF.java:TradeAppJSF:getPassword");
    }

    public void setPassword(String password) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/TradeAppJSF.java:TradeAppJSF:setPassword");
    }

    public String getCpassword() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/TradeAppJSF.java:TradeAppJSF:getCpassword");
    }

    public void setCpassword(String cpassword) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/TradeAppJSF.java:TradeAppJSF:setCpassword");
    }

    public String getFullname() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/TradeAppJSF.java:TradeAppJSF:getFullname");
    }

    public void setFullname(String fullname) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/TradeAppJSF.java:TradeAppJSF:setFullname");
    }

    public String getResults() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/TradeAppJSF.java:TradeAppJSF:getResults");
    }

    public void setResults(String results) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/TradeAppJSF.java:TradeAppJSF:setResults");
    }

    public String getAddress() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/TradeAppJSF.java:TradeAppJSF:getAddress");
    }

    public void setAddress(String address) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/TradeAppJSF.java:TradeAppJSF:setAddress");
    }

    public String getEmail() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/TradeAppJSF.java:TradeAppJSF:getEmail");
    }

    public void setEmail(String email) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/TradeAppJSF.java:TradeAppJSF:setEmail");
    }

    public String getCcn() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/TradeAppJSF.java:TradeAppJSF:getCcn");
    }

    public void setCcn(String ccn) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/TradeAppJSF.java:TradeAppJSF:setCcn");
    }

    public String getMoney() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/TradeAppJSF.java:TradeAppJSF:getMoney");
    }

    public void setMoney(String money) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/TradeAppJSF.java:TradeAppJSF:setMoney");
    }
};
