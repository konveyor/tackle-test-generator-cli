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

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.context.ExternalContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import com.ibm.websphere.samples.daytrader.TradeAction;
import com.ibm.websphere.samples.daytrader.entities.OrderDataBean;
import com.ibm.websphere.samples.daytrader.entities.QuoteDataBean;
import com.ibm.websphere.samples.daytrader.util.Log;
import com.ibm.websphere.samples.daytrader.util.TradeConfig;



public class QuoteJSF {

    
    private ExternalContext facesExternalContext;

    
    private TradeAction tradeAction;

    private QuoteData[] quotes;
    private String symbols = null;
    private HtmlDataTable dataTable;
    private Integer quantity = 100;

    
    public void getAllQuotes() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/QuoteJSF.java:QuoteJSF:getAllQuotes");
    }

    public String getQuotesBySymbols() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/QuoteJSF.java:QuoteJSF:getQuotesBySymbols");
    }

    public String buy() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/QuoteJSF.java:QuoteJSF:buy");
    }

    public void setQuotes(QuoteData[] quotes) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/QuoteJSF.java:QuoteJSF:setQuotes");
    }

    public QuoteData[] getQuotes() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/QuoteJSF.java:QuoteJSF:getQuotes");
    }

    public void setSymbols(String symbols) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/QuoteJSF.java:QuoteJSF:setSymbols");
    }

    public String getSymbols() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/QuoteJSF.java:QuoteJSF:getSymbols");
    }

    public void setDataTable(HtmlDataTable dataTable) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/QuoteJSF.java:QuoteJSF:setDataTable");
    }

    public HtmlDataTable getDataTable() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/QuoteJSF.java:QuoteJSF:getDataTable");
    }

    public void setQuantity(Integer quantity) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/QuoteJSF.java:QuoteJSF:setQuantity");
    }

    public Integer getQuantity() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/QuoteJSF.java:QuoteJSF:getQuantity");
    }
}
