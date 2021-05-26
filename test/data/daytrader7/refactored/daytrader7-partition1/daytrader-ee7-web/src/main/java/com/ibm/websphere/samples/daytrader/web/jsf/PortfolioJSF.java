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
import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.context.ExternalContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import com.ibm.websphere.samples.daytrader.TradeAction;
import com.ibm.websphere.samples.daytrader.entities.HoldingDataBean;
import com.ibm.websphere.samples.daytrader.entities.OrderDataBean;
import com.ibm.websphere.samples.daytrader.entities.QuoteDataBean;
import com.ibm.websphere.samples.daytrader.util.FinancialUtils;
import com.ibm.websphere.samples.daytrader.util.TradeConfig;



public class PortfolioJSF {
    
    private ExternalContext facesExternalContext;

    
    private TradeAction tradeAction;

    private BigDecimal balance;
    private BigDecimal openBalance;
    private Integer numberHoldings;
    private BigDecimal holdingsTotal;
    private BigDecimal sumOfCashHoldings;
    private BigDecimal totalGain = new BigDecimal(0.0);
    private BigDecimal totalValue = new BigDecimal(0.0);
    private BigDecimal totalBasis = new BigDecimal(0.0);
    private BigDecimal totalGainPercent = new BigDecimal(0.0);
    private ArrayList<HoldingData> holdingDatas;
    private HtmlDataTable dataTable;

    
    public void getPortfolio() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/PortfolioJSF.java:PortfolioJSF:getPortfolio");
    }

    public String sell() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/PortfolioJSF.java:PortfolioJSF:sell");
    }

    public void setDataTable(HtmlDataTable dataTable) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/PortfolioJSF.java:PortfolioJSF:setDataTable");
    }

    public HtmlDataTable getDataTable() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/PortfolioJSF.java:PortfolioJSF:getDataTable");
    }

    public void setBalance(BigDecimal balance) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/PortfolioJSF.java:PortfolioJSF:setBalance");
    }

    public BigDecimal getBalance() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/PortfolioJSF.java:PortfolioJSF:getBalance");
    }

    public void setOpenBalance(BigDecimal openBalance) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/PortfolioJSF.java:PortfolioJSF:setOpenBalance");
    }

    public BigDecimal getOpenBalance() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/PortfolioJSF.java:PortfolioJSF:getOpenBalance");
    }

    public void setHoldingsTotal(BigDecimal holdingsTotal) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/PortfolioJSF.java:PortfolioJSF:setHoldingsTotal");
    }

    public BigDecimal getHoldingsTotal() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/PortfolioJSF.java:PortfolioJSF:getHoldingsTotal");
    }

    public void setSumOfCashHoldings(BigDecimal sumOfCashHoldings) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/PortfolioJSF.java:PortfolioJSF:setSumOfCashHoldings");
    }

    public BigDecimal getSumOfCashHoldings() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/PortfolioJSF.java:PortfolioJSF:getSumOfCashHoldings");
    }

    public void setNumberHoldings(Integer numberHoldings) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/PortfolioJSF.java:PortfolioJSF:setNumberHoldings");
    }

    public Integer getNumberHoldings() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/PortfolioJSF.java:PortfolioJSF:getNumberHoldings");
    }

    public void setTotalGain(BigDecimal totalGain) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/PortfolioJSF.java:PortfolioJSF:setTotalGain");
    }

    public BigDecimal getTotalGain() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/PortfolioJSF.java:PortfolioJSF:getTotalGain");
    }

    public void setTotalValue(BigDecimal totalValue) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/PortfolioJSF.java:PortfolioJSF:setTotalValue");
    }

    public BigDecimal getTotalValue() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/PortfolioJSF.java:PortfolioJSF:getTotalValue");
    }

    public void setTotalBasis(BigDecimal totalBasis) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/PortfolioJSF.java:PortfolioJSF:setTotalBasis");
    }

    public BigDecimal getTotalBasis() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/PortfolioJSF.java:PortfolioJSF:getTotalBasis");
    }

    public void setHoldingDatas(ArrayList<HoldingData> holdingDatas) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/PortfolioJSF.java:PortfolioJSF:setHoldingDatas");
    }

    public ArrayList<HoldingData> getHoldingDatas() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/PortfolioJSF.java:PortfolioJSF:getHoldingDatas");
    }

    public void setTotalGainPercent(BigDecimal totalGainPercent) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/PortfolioJSF.java:PortfolioJSF:setTotalGainPercent");
    }

    public BigDecimal getTotalGainPercent() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/PortfolioJSF.java:PortfolioJSF:getTotalGainPercent");
    }

    public String getTotalGainPercentHTML() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/PortfolioJSF.java:PortfolioJSF:getTotalGainPercentHTML");
    }
}
