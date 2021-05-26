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

@Named("tradeconfig")
@RequestScoped
public class TradeConfigJSF {

    @Inject
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
        String currentConfigStr = "\n\n########## Trade configuration update. Current config:\n\n";
        String runTimeModeStr = this.runtimeMode;
        if (runTimeModeStr != null) {
            try {
                for (int i = 0; i < runtimeModeList.length; i++) {
                    if (runTimeModeStr.equals(runtimeModeList[i])) {
                        TradeConfig.setRunTimeMode(i);
                    }
                }
            } catch (Exception e) {

                Log.error(e, "TradeConfigJSF.updateConfig(..): minor exception caught", "trying to set runtimemode to " + runTimeModeStr,
                        "reverting to current value");

            } // If the value is bad, simply revert to current
        }
        currentConfigStr += "\t\tRunTimeMode:\t\t\t" + TradeConfig.runTimeModeNames[TradeConfig.getRunTimeMode()] + "\n";

        TradeConfig.setUseRemoteEJBInterface(useRemoteEJBInterface);
        currentConfigStr += "\t\tUse Remote EJB Interface:\t" + TradeConfig.useRemoteEJBInterface() + "\n";
        
        String orderProcessingModeStr = this.orderProcessingMode;
        if (orderProcessingModeStr != null) {
            try {
                for (int i = 0; i < orderProcessingModeList.length; i++) {
                    if (orderProcessingModeStr.equals(orderProcessingModeList[i])) {
                        TradeConfig.orderProcessingMode = i;
                    }
                }
            } catch (Exception e) {
                Log.error(e, "TradeConfigJSF.updateConfig(..): minor exception caught", "trying to set orderProcessing to " + orderProcessingModeStr,
                        "reverting to current value");

            } // If the value is bad, simply revert to current
        }
        currentConfigStr += "\t\tOrderProcessingMode:\t\t" + TradeConfig.orderProcessingModeNames[TradeConfig.orderProcessingMode] + "\n";

        /*
        String cachingTypeStr = this.cachingType;
        if (cachingTypeStr != null) {
            try {
                for (int i = 0; i < cachingTypeList.length; i++) {
                    if (cachingTypeStr.equals(cachingTypeList[i])) {
                        TradeConfig.cachingType = i;
                    }
                }
            } catch (Exception e) {
                Log.error(e, "TradeConfigJSF.updateConfig(..): minor exception caught", "trying to set cachingType to " + cachingTypeStr,
                        "reverting to current value");

            } // If the value is bad, simply revert to current
        }
        currentConfigStr += "\t\tCachingType:\t\t\t" + TradeConfig.cachingTypeNames[TradeConfig.cachingType] + "\n";

        int distMapCacheSize = this.distributedMapCacheSize;

        try {
            TradeConfig.setDistributedMapCacheSize(distMapCacheSize);
        } catch (Exception e) {
            Log.error(e, "TradeConfigJSF.updateConfig(..): minor exception caught", "trying to set distributedMapCacheSize", "reverting to current value");

        } // If the value is bad, simply revert to current

        currentConfigStr += "\t\tDMapCacheSize:\t\t\t" + TradeConfig.getDistributedMapCacheSize() + "\n";
		*/
        
        String webInterfaceStr = webInterface;
        if (webInterfaceStr != null) {
            try {
                for (int i = 0; i < webInterfaceList.length; i++) {
                    if (webInterfaceStr.equals(webInterfaceList[i])) {
                        TradeConfig.webInterface = i;
                    }
                }
            } catch (Exception e) {
                Log.error(e, "TradeConfigJSF.updateConfig(..): minor exception caught", "trying to set WebInterface to " + webInterfaceStr,
                        "reverting to current value");

            } // If the value is bad, simply revert to current
        }
        currentConfigStr += "\t\tWeb Interface:\t\t\t" + TradeConfig.webInterfaceNames[TradeConfig.webInterface] + "\n";

        TradeConfig.setMAX_USERS(maxUsers);
        TradeConfig.setMAX_QUOTES(maxQuotes);

        currentConfigStr += "\t\tTrade  Users:\t\t\t" + TradeConfig.getMAX_USERS() + "\n";
        currentConfigStr += "\t\tTrade Quotes:\t\t\t" + TradeConfig.getMAX_QUOTES() + "\n";

        TradeConfig.setMarketSummaryInterval(marketSummaryInterval);

        currentConfigStr += "\t\tMarket Summary Interval:\t" + TradeConfig.getMarketSummaryInterval() + "\n";

        TradeConfig.setPrimIterations(primIterations);

        currentConfigStr += "\t\tPrimitive Iterations:\t\t" + TradeConfig.getPrimIterations() + "\n";

        TradeConfig.setPublishQuotePriceChange(publishQuotePriceChange);
        currentConfigStr += "\t\tTradeStreamer MDB Enabled:\t" + TradeConfig.getPublishQuotePriceChange() + "\n";

        TradeConfig.setPercentSentToWebsocket(percentSentToWebsocket);
        currentConfigStr += "\t\t% of trades on Websocket:\t" + TradeConfig.getPercentSentToWebsocket() + "\n";
        
        TradeConfig.setLongRun(longRun);
        currentConfigStr += "\t\tLong Run Enabled:\t\t" + TradeConfig.getLongRun() + "\n";

        TradeConfig.setDisplayOrderAlerts(displayOrderAlerts);
        currentConfigStr += "\t\tDisplay Order Alerts:\t\t" + TradeConfig.getDisplayOrderAlerts() + "\n";

        Log.setTrace(trace);
        currentConfigStr += "\t\tTrace Enabled:\t\t\t" + TradeConfig.getTrace() + "\n";

        Log.setActionTrace(actionTrace);
        currentConfigStr += "\t\tAction Trace Enabled:\t\t" + TradeConfig.getActionTrace() + "\n";

        System.out.println(currentConfigStr);
        setResult("DayTrader Configuration Updated");
    }

    public String resetTrade() {
        RunStatsDataBean runStatsData = new RunStatsDataBean();
        TradeConfig currentConfig = new TradeConfig();
        HttpSession session = (HttpSession) facesExternalContext.getSession(true);
        try {
        	// Do not inject TradeAction on this class because we dont want the 
        	// config to initialiaze at startup. 
            TradeAction tradeAction = new TradeAction();
            runStatsData = tradeAction.resetTrade(false);
            session.setAttribute("runStatsData", runStatsData);
            session.setAttribute("tradeConfig", currentConfig);
            result += "Trade Reset completed successfully";

        } catch (Exception e) {
            result += "Trade Reset Error  - see log for details";
            session.setAttribute("result", result);
            Log.error(e, result);
        }

        return "stats";
    }

    public String populateDatabase() {

        try {
            new TradeBuildDB(new java.io.PrintWriter(System.out), null);
        } catch (Exception e) {
            e.printStackTrace();
        }       

        result = "TradeBuildDB: **** DayTrader Database Built - " + TradeConfig.getMAX_USERS() + " users created, " + TradeConfig.getMAX_QUOTES()
                + " quotes created. ****<br/>";
        result += "TradeBuildDB: **** Check System.Out for any errors. ****<br/>";

        return "database";
    }

    public String buildDatabaseTables() {
        try {

            //Find out the Database being used
            TradeDirect tradeDirect = new TradeDirect();

            String dbProductName = null;
            try {
                dbProductName = tradeDirect.checkDBProductName();
            } catch (Exception e) {
                Log.error(e, "TradeBuildDB: Unable to check DB Product name");
            }
            if (dbProductName == null) {
                result += "TradeBuildDB: **** Unable to check DB Product name, please check Database/AppServer configuration and retry ****<br/>";
                return "database";
            }

            String ddlFile = null;
            //Locate DDL file for the specified database
            try {
                result = result + "TradeBuildDB: **** Database Product detected: " + dbProductName + " ****<br/>";
                if (dbProductName.startsWith("DB2/")) { // if db is DB2
                    ddlFile = "/dbscripts/db2/Table.ddl";
                } else if (dbProductName.startsWith("Apache Derby")) { //if db is Derby
                    ddlFile = "/dbscripts/derby/Table.ddl";
                } else if (dbProductName.startsWith("Oracle")) { // if the Db is Oracle
                    ddlFile = "/dbscripts/oracle/Table.ddl";
                } else { // Unsupported "Other" Database
                    ddlFile = "/dbscripts/derby/Table.ddl";
                    result = result + "TradeBuildDB: **** This Database is unsupported/untested use at your own risk ****<br/>";
                }

                result = result + "TradeBuildDB: **** The DDL file at path" + ddlFile + " will be used ****<br/>";
            } catch (Exception e) {
                Log.error(e, "TradeBuildDB: Unable to locate DDL file for the specified database");
                result = result + "TradeBuildDB: **** Unable to locate DDL file for the specified database ****<br/>";
                return "database";
            }

            new TradeBuildDB(new java.io.PrintWriter(System.out), facesExternalContext.getResourceAsStream(ddlFile));

            result = result + "TradeBuildDB: **** DayTrader Database Created, Check System.Out for any errors. ****<br/>";

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Go to configure.xhtml
        return "database";
    }

    public void setRuntimeMode(String runtimeMode) {
        this.runtimeMode = runtimeMode;
    }

    public String getRuntimeMode() {
        return runtimeMode;
    }

    public void setOrderProcessingMode(String orderProcessingMode) {
        this.orderProcessingMode = orderProcessingMode;
    }

    public String getOrderProcessingMode() {
        return orderProcessingMode;
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
        this.maxUsers = maxUsers;
    }

    public int getMaxUsers() {
        return maxUsers;
    }

    public void setmaxQuotes(int maxQuotes) {
        this.maxQuotes = maxQuotes;
    }

    public int getMaxQuotes() {
        return maxQuotes;
    }

    public void setMarketSummaryInterval(int marketSummaryInterval) {
        this.marketSummaryInterval = marketSummaryInterval;
    }

    public int getMarketSummaryInterval() {
        return marketSummaryInterval;
    }

    public void setPrimIterations(int primIterations) {
        this.primIterations = primIterations;
    }

    public int getPrimIterations() {
        return primIterations;
    }

    public void setPublishQuotePriceChange(boolean publishQuotePriceChange) {
        this.publishQuotePriceChange = publishQuotePriceChange;
    }

    public boolean isPublishQuotePriceChange() {
        return publishQuotePriceChange;
    }
    
    public void setPercentSentToWebsocket(int percentSentToWebsocket) {
        this. percentSentToWebsocket =  percentSentToWebsocket;
    }

    public int getPercentSentToWebsocket() {
        return  percentSentToWebsocket;
    }

    public void setDisplayOrderAlerts(boolean displayOrderAlerts) {
        this.displayOrderAlerts = displayOrderAlerts;
    }

    public boolean isDisplayOrderAlerts() {
        return displayOrderAlerts;
    }
    
    public void setUseRemoteEJBInterface(boolean useRemoteEJBInterface) {
        this.useRemoteEJBInterface = useRemoteEJBInterface;
    }

    public boolean isUseRemoteEJBInterface() {
        return useRemoteEJBInterface;
    }

    public void setLongRun(boolean longRun) {
        this.longRun = longRun;
    }

    public boolean isLongRun() {
        return longRun;
    }

    public void setTrace(boolean trace) {
        this.trace = trace;
    }

    public boolean isTrace() {
        return trace;
    }

    public void setRuntimeModeList(String[] runtimeModeList) {
        this.runtimeModeList = runtimeModeList;
    }

    public String[] getRuntimeModeList() {
        return runtimeModeList;
    }

    public void setOrderProcessingModeList(String[] orderProcessingModeList) {
        this.orderProcessingModeList = orderProcessingModeList;
    }

    public String[] getOrderProcessingModeList() {
        return orderProcessingModeList;
    }

    /*public void setCachingTypeList(String[] cachingTypeList) {
        this.cachingTypeList = cachingTypeList;
    }

    public String[] getCachingTypeList() {
        return cachingTypeList;
    }*/

    public void setWebInterface(String webInterface) {
        this.webInterface = webInterface;
    }

    public String getWebInterface() {
        return webInterface;
    }

    public void setWebInterfaceList(String[] webInterfaceList) {
        this.webInterfaceList = webInterfaceList;
    }

    public String[] getWebInterfaceList() {
        return webInterfaceList;
    }

    public void setActionTrace(boolean actionTrace) {
        this.actionTrace = actionTrace;
    }

    public boolean isActionTrace() {
        return actionTrace;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }

}
