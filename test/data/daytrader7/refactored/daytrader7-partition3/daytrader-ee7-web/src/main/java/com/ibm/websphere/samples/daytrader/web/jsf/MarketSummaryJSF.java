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
import java.math.RoundingMode;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.ibm.websphere.samples.daytrader.TradeAction;
import com.ibm.websphere.samples.daytrader.beans.MarketSummaryDataBean;
import com.ibm.websphere.samples.daytrader.entities.QuoteDataBean;
import com.ibm.websphere.samples.daytrader.util.FinancialUtils;



public class MarketSummaryJSF {
    
    private TradeAction tradeAction;

    private BigDecimal TSIA;
    private BigDecimal openTSIA;
    private double volume;
    private QuoteData[] topGainers;
    private QuoteData[] topLosers;
    private Date summaryDate;

    // cache the gainPercent once computed for this bean
    private BigDecimal gainPercent = null;

    
    public void getMarketSummary() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/MarketSummaryJSF.java:MarketSummaryJSF:getMarketSummary");
    }

    public void setTSIA(BigDecimal tSIA) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/MarketSummaryJSF.java:MarketSummaryJSF:setTSIA");
    }

    public BigDecimal getTSIA() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/MarketSummaryJSF.java:MarketSummaryJSF:getTSIA");
    }

    public void setOpenTSIA(BigDecimal openTSIA) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/MarketSummaryJSF.java:MarketSummaryJSF:setOpenTSIA");
    }

    public BigDecimal getOpenTSIA() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/MarketSummaryJSF.java:MarketSummaryJSF:getOpenTSIA");
    }

    public void setVolume(double volume) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/MarketSummaryJSF.java:MarketSummaryJSF:setVolume");
    }

    public double getVolume() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/MarketSummaryJSF.java:MarketSummaryJSF:getVolume");
    }

    public void setTopGainers(QuoteData[] topGainers) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/MarketSummaryJSF.java:MarketSummaryJSF:setTopGainers");
    }

    public QuoteData[] getTopGainers() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/MarketSummaryJSF.java:MarketSummaryJSF:getTopGainers");
    }

    public void setTopLosers(QuoteData[] topLosers) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/MarketSummaryJSF.java:MarketSummaryJSF:setTopLosers");
    }

    public QuoteData[] getTopLosers() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/MarketSummaryJSF.java:MarketSummaryJSF:getTopLosers");
    }

    public void setSummaryDate(Date summaryDate) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/MarketSummaryJSF.java:MarketSummaryJSF:setSummaryDate");
    }

    public Date getSummaryDate() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/MarketSummaryJSF.java:MarketSummaryJSF:getSummaryDate");
    }

    public void setGainPercent(BigDecimal gainPercent) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/MarketSummaryJSF.java:MarketSummaryJSF:setGainPercent");
    }

    public BigDecimal getGainPercent() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/MarketSummaryJSF.java:MarketSummaryJSF:getGainPercent");
    }

    public String getGainPercentHTML() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/MarketSummaryJSF.java:MarketSummaryJSF:getGainPercentHTML");
    }

}
