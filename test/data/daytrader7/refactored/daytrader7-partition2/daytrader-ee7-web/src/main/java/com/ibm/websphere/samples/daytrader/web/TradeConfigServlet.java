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

import com.ibm.cardinal.util.*;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.websphere.samples.daytrader.TradeAction;
import com.ibm.websphere.samples.daytrader.beans.RunStatsDataBean;
import com.ibm.websphere.samples.daytrader.direct.TradeDirect;
import com.ibm.websphere.samples.daytrader.util.Log;
import com.ibm.websphere.samples.daytrader.util.TradeConfig;

/**
 * TradeConfigServlet provides a servlet interface to adjust DayTrader runtime parameters.
 * TradeConfigServlet updates values in the { com.ibm.websphere.samples.daytrader.web.TradeConfig} JavaBean holding
 * all configuration and runtime parameters for the Trade application
 *
 */

public class TradeConfigServlet extends HttpServlet {

    private static final long serialVersionUID = -1910381529792500095L;

    /**
     * Servlet initialization method.
     */
    
    public void init(ServletConfig config) throws ServletException {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/TradeConfigServlet.java:TradeConfigServlet:init");
    }

    /**
     * Create the TradeConfig bean and pass it the config.jsp page
     * to display the current Trade runtime configuration
     * Creation date: (2/8/2000 3:43:59 PM)
     */
    void doConfigDisplay(HttpServletRequest req, HttpServletResponse resp, String results) throws Exception {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/TradeConfigServlet.java:TradeConfigServlet:doConfigDisplay");
    }

    void doResetTrade(HttpServletRequest req, HttpServletResponse resp, String results) throws Exception {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/TradeConfigServlet.java:TradeConfigServlet:doResetTrade");
    }

    /**
     * Update Trade runtime configuration paramaters
     * Creation date: (2/8/2000 3:44:24 PM)
     */
    void doConfigUpdate(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/TradeConfigServlet.java:TradeConfigServlet:doConfigUpdate");
    }

    
    public void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/TradeConfigServlet.java:TradeConfigServlet:service");
    }
}
