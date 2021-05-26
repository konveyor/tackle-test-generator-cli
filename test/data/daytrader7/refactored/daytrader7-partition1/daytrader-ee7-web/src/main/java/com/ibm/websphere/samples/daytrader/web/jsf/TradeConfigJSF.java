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

import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import com.ibm.websphere.samples.daytrader.TradeAction;
import com.ibm.websphere.samples.daytrader.beans.RunStatsDataBean;
import com.ibm.websphere.samples.daytrader.direct.TradeDirect;
import com.ibm.websphere.samples.daytrader.util.Log;
import com.ibm.websphere.samples.daytrader.util.TradeConfig;
import com.ibm.websphere.samples.daytrader.web.TradeBuildDB;



public class TradeConfigJSF {

    
    private ExternalContext facesExternalContext;

    private String runtimeMode = TradeConfig.runTimeModeNames[TradeConfig.getRunTimeMode()];
    private String orderProcessingMode = TradeConfig.orderProcessingModeNames[TradeConfig.getOrderProcessingMode()];
    //private String cachingType = TradeConfig.cachingTypeNames[TradeConfig.getCachingType()];
    //private int distributedMapCacheSize = TradeConfig.getDistributedMapCacheSize();
    private int maxUsers = TradeConfig.getMAX_USERS();
    private int maxQuotes = TradeConfig.getMAX_QUOTES();
    private int marketSummaryInterval = TradeConfig.getMarketSummaryInterval();
    private String webInterface = TradeConfig.webInterfaceNames[TradeConfig.getWebInterface()];
    private int primIterations = TradeConfig.getPrimIterations();
    private int percentSentToWebsocket = TradeConfig.getPercentSentToWebsocket();
    private boolean publishQuotePriceChange = TradeConfig.getPublishQuotePriceChange();
    private boolean longRun = TradeConfig.getLongRun();
    private boolean displayOrderAlerts = TradeConfig.getDisplayOrderAlerts();
    private boolean useRemoteEJBInterface = TradeConfig.useRemoteEJBInterface();
    private boolean actionTrace = TradeConfig.getActionTrace();
    private boolean trace = TradeConfig.getTrace();
    private String[] runtimeModeList = TradeConfig.runTimeModeNames;
    private String[] orderProcessingModeList = TradeConfig.orderProcessingModeNames;
    //private String[] cachingTypeList = TradeConfig.cachingTypeNames;
    private String[] webInterfaceList = TradeConfig.webInterfaceNames;
    private String result = "";

    public void updateConfig() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/TradeConfigJSF.java:TradeConfigJSF:updateConfig");
    }

    public String resetTrade() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/TradeConfigJSF.java:TradeConfigJSF:resetTrade");
    }

    public String populateDatabase() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/TradeConfigJSF.java:TradeConfigJSF:populateDatabase");
    }

    public String buildDatabaseTables() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/TradeConfigJSF.java:TradeConfigJSF:buildDatabaseTables");
    }

    public void setRuntimeMode(String runtimeMode) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/TradeConfigJSF.java:TradeConfigJSF:setRuntimeMode");
    }

    public String getRuntimeMode() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/TradeConfigJSF.java:TradeConfigJSF:getRuntimeMode");
    }

    public void setOrderProcessingMode(String orderProcessingMode) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/TradeConfigJSF.java:TradeConfigJSF:setOrderProcessingMode");
    }

    public String getOrderProcessingMode() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/TradeConfigJSF.java:TradeConfigJSF:getOrderProcessingMode");
    }
    /*
    public void setCachingType(String cachingType) {
        this.cachingType = cachingType;
    }

    public String getCachingType() {
        return cachingType;
    }

    public void setDistributedMapCacheSize(int distributedMapCacheSize) {
        this.distributedMapCacheSize = distributedMapCacheSize;
    }

    public int getDistributedMapCacheSize() {
        return distributedMapCacheSize;
    }*/

    public void setMaxUsers(int maxUsers) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/TradeConfigJSF.java:TradeConfigJSF:setMaxUsers");
    }

    public int getMaxUsers() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/TradeConfigJSF.java:TradeConfigJSF:getMaxUsers");
    }

    public void setmaxQuotes(int maxQuotes) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/TradeConfigJSF.java:TradeConfigJSF:setmaxQuotes");
    }

    public int getMaxQuotes() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/TradeConfigJSF.java:TradeConfigJSF:getMaxQuotes");
    }

    public void setMarketSummaryInterval(int marketSummaryInterval) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/TradeConfigJSF.java:TradeConfigJSF:setMarketSummaryInterval");
    }

    public int getMarketSummaryInterval() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/TradeConfigJSF.java:TradeConfigJSF:getMarketSummaryInterval");
    }

    public void setPrimIterations(int primIterations) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/TradeConfigJSF.java:TradeConfigJSF:setPrimIterations");
    }

    public int getPrimIterations() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/TradeConfigJSF.java:TradeConfigJSF:getPrimIterations");
    }

    public void setPublishQuotePriceChange(boolean publishQuotePriceChange) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/TradeConfigJSF.java:TradeConfigJSF:setPublishQuotePriceChange");
    }

    public boolean isPublishQuotePriceChange() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/TradeConfigJSF.java:TradeConfigJSF:isPublishQuotePriceChange");
    }
    
    public void setPercentSentToWebsocket(int percentSentToWebsocket) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/TradeConfigJSF.java:TradeConfigJSF:setPercentSentToWebsocket");
    }

    public int getPercentSentToWebsocket() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/TradeConfigJSF.java:TradeConfigJSF:getPercentSentToWebsocket");
    }

    public void setDisplayOrderAlerts(boolean displayOrderAlerts) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/TradeConfigJSF.java:TradeConfigJSF:setDisplayOrderAlerts");
    }

    public boolean isDisplayOrderAlerts() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/TradeConfigJSF.java:TradeConfigJSF:isDisplayOrderAlerts");
    }
    
    public void setUseRemoteEJBInterface(boolean useRemoteEJBInterface) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/TradeConfigJSF.java:TradeConfigJSF:setUseRemoteEJBInterface");
    }

    public boolean isUseRemoteEJBInterface() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/TradeConfigJSF.java:TradeConfigJSF:isUseRemoteEJBInterface");
    }

    public void setLongRun(boolean longRun) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/TradeConfigJSF.java:TradeConfigJSF:setLongRun");
    }

    public boolean isLongRun() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/TradeConfigJSF.java:TradeConfigJSF:isLongRun");
    }

    public void setTrace(boolean trace) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/TradeConfigJSF.java:TradeConfigJSF:setTrace");
    }

    public boolean isTrace() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/TradeConfigJSF.java:TradeConfigJSF:isTrace");
    }

    public void setRuntimeModeList(String[] runtimeModeList) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/TradeConfigJSF.java:TradeConfigJSF:setRuntimeModeList");
    }

    public String[] getRuntimeModeList() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/TradeConfigJSF.java:TradeConfigJSF:getRuntimeModeList");
    }

    public void setOrderProcessingModeList(String[] orderProcessingModeList) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/TradeConfigJSF.java:TradeConfigJSF:setOrderProcessingModeList");
    }

    public String[] getOrderProcessingModeList() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/TradeConfigJSF.java:TradeConfigJSF:getOrderProcessingModeList");
    }

    /*public void setCachingTypeList(String[] cachingTypeList) {
        this.cachingTypeList = cachingTypeList;
    }

    public String[] getCachingTypeList() {
        return cachingTypeList;
    }*/

    public void setWebInterface(String webInterface) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/TradeConfigJSF.java:TradeConfigJSF:setWebInterface");
    }

    public String getWebInterface() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/TradeConfigJSF.java:TradeConfigJSF:getWebInterface");
    }

    public void setWebInterfaceList(String[] webInterfaceList) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/TradeConfigJSF.java:TradeConfigJSF:setWebInterfaceList");
    }

    public String[] getWebInterfaceList() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/TradeConfigJSF.java:TradeConfigJSF:getWebInterfaceList");
    }

    public void setActionTrace(boolean actionTrace) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/TradeConfigJSF.java:TradeConfigJSF:setActionTrace");
    }

    public boolean isActionTrace() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/TradeConfigJSF.java:TradeConfigJSF:isActionTrace");
    }

    public void setResult(String result) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/TradeConfigJSF.java:TradeConfigJSF:setResult");
    }

    public String getResult() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/TradeConfigJSF.java:TradeConfigJSF:getResult");
    }

}
