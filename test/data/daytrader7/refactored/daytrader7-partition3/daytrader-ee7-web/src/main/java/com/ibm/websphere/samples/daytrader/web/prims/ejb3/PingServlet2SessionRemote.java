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
package com.ibm.websphere.samples.daytrader.web.prims.ejb3;

import com.ibm.cardinal.util.*;

import java.io.IOException;

import javax.ejb.EJB;
import javax.naming.InitialContext;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.websphere.samples.daytrader.ejb3.TradeSLSBRemote;
import com.ibm.websphere.samples.daytrader.util.Log;
import com.ibm.websphere.samples.daytrader.util.TradeConfig;

/**
 *
 * This primitive is designed to run inside the TradeApplication and relies upon
 * the { trade_client.TradeConfig} class to set configuration parameters.
 * PingServlet2SessionEJB tests key functionality of a servlet call to a
 * stateless SessionEJB. This servlet makes use of the Stateless Session EJB
 * { trade.Trade} by calling calculateInvestmentReturn with three random
 * numbers.
 *
 */

public class PingServlet2SessionRemote extends HttpServlet {

    private static final long serialVersionUID = -6328388347808212784L;

    private static String initTime;

    private static int hitCount;

    
    private TradeSLSBRemote tradeSLSBRemote;

    
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/prims/ejb3/PingServlet2SessionRemote.java:PingServlet2SessionRemote:doPost");
    }

    
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/prims/ejb3/PingServlet2SessionRemote.java:PingServlet2SessionRemote:doGet");
    }

    
    public String getServletInfo() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/prims/ejb3/PingServlet2SessionRemote.java:PingServlet2SessionRemote:getServletInfo");
    }

    
    public void init(ServletConfig config) throws ServletException {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/prims/ejb3/PingServlet2SessionRemote.java:PingServlet2SessionRemote:init");
    }
}