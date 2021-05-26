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
package com.ibm.websphere.samples.daytrader.web;

import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.ibm.websphere.samples.daytrader.direct.TradeDirect;
import com.ibm.websphere.samples.daytrader.util.Log;
import com.ibm.websphere.samples.daytrader.util.TradeConfig;

@WebListener()
public class TradeWebContextListener implements ServletContextListener {

    // receieve trade web app startup/shutown events to start(initialized)/stop
    // TradeDirect
    @Override
    public void contextInitialized(ServletContextEvent event) {
        Log.trace("TradeWebContextListener contextInitialized -- initializing TradeDirect");
        
        // Load settings from properties file (if it exists)
        Properties prop = new Properties();
        InputStream stream =  event.getServletContext().getResourceAsStream("/properties/daytrader.properties");
        
        try {
            prop.load(stream);
            System.out.println("Settings from daytrader.properties: " + prop);
            TradeConfig.setRunTimeMode(Integer.parseInt(prop.getProperty("runtimeMode")));
            TradeConfig.setUseRemoteEJBInterface(Boolean.parseBoolean(prop.getProperty("useRemoteEJBInterface")));
            TradeConfig.setOrderProcessingMode(Integer.parseInt(prop.getProperty("orderProcessingMode")));
            TradeConfig.setWebInterface(Integer.parseInt(prop.getProperty("webInterface")));
            //TradeConfig.setCachingType(Integer.parseInt(prop.getProperty("cachingType")));
            //TradeConfig.setDistributedMapCacheSize(Integer.parseInt(prop.getProperty("cacheSize")));
            TradeConfig.setMAX_USERS(Integer.parseInt(prop.getProperty("maxUsers")));
            TradeConfig.setMAX_QUOTES(Integer.parseInt(prop.getProperty("maxQuotes")));
            TradeConfig.setMarketSummaryInterval(Integer.parseInt(prop.getProperty("marketSummaryInterval")));
            TradeConfig.setPrimIterations(Integer.parseInt(prop.getProperty("primIterations")));
            TradeConfig.setPublishQuotePriceChange(Boolean.parseBoolean(prop.getProperty("publishQuotePriceChange")));
            TradeConfig.setPercentSentToWebsocket(Integer.parseInt(prop.getProperty("percentSentToWebsocket")));
            TradeConfig.setDisplayOrderAlerts(Boolean.parseBoolean(prop.getProperty("displayOrderAlerts")));
            TradeConfig.setLongRun(Boolean.parseBoolean(prop.getProperty("longRun")));
            TradeConfig.setTrace(Boolean.parseBoolean(prop.getProperty("trace")));
            TradeConfig.setActionTrace(Boolean.parseBoolean(prop.getProperty("actionTrace")));
        } catch (Exception e) {
            System.out.println("daytrader.properties not found");
        }
        
        TradeDirect.init();
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        Log.trace("TradeWebContextListener  contextDestroy calling TradeDirect:destroy()");
        // TradeDirect.destroy();
    }

}
