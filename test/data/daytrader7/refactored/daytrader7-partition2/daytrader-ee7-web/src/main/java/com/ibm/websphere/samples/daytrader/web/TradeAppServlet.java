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
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ibm.websphere.samples.daytrader.util.Log;
import com.ibm.websphere.samples.daytrader.util.TradeConfig;

/**
 *
 * TradeAppServlet provides the standard web interface to Trade and can be
 * accessed with the Go Trade! link. Driving benchmark load using this interface
 * requires a sophisticated web load generator that is capable of filling HTML
 * forms and posting dynamic data.
 */


public class TradeAppServlet extends HttpServlet {

    private static final long serialVersionUID = 481530522846648373L;

    /**
     * Servlet initialization method.
     */
    
    public void init(ServletConfig config) throws ServletException {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/TradeAppServlet.java:TradeAppServlet:init");
    }

    /**
     * Returns a string that contains information about TradeScenarioServlet
     *
     *  The servlet information
     */
    
    public java.lang.String getServletInfo() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/TradeAppServlet.java:TradeAppServlet:getServletInfo");
    }

    /**
     * Process incoming HTTP GET requests
     *
     *  request
     *            Object that encapsulates the request to the servlet
     *  response
     *            Object that encapsulates the response from the servlet
     */
    
    public void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws ServletException, IOException {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/TradeAppServlet.java:TradeAppServlet:doGet");
    }

    /**
     * Process incoming HTTP POST requests
     *
     *  request
     *            Object that encapsulates the request to the servlet
     *  response
     *            Object that encapsulates the response from the servlet
     */
    
    public void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws ServletException, IOException {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/TradeAppServlet.java:TradeAppServlet:doPost");
    }

    /**
     * Main service method for TradeAppServlet
     *
     *  request
     *            Object that encapsulates the request to the servlet
     *  response
     *            Object that encapsulates the response from the servlet
     */
    public void performTask(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/TradeAppServlet.java:TradeAppServlet:performTask");
    }

}