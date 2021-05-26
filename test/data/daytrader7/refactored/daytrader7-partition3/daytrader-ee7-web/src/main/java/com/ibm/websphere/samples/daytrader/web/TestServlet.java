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
import java.math.BigDecimal;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.websphere.samples.daytrader.TradeAction;
import com.ibm.websphere.samples.daytrader.util.Log;
import com.ibm.websphere.samples.daytrader.util.TradeConfig;


public class TestServlet extends HttpServlet {

    private static final long serialVersionUID = -2927579146688173127L;

    
    public void init(ServletConfig config) throws ServletException {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/TestServlet.java:TestServlet:init");
    }

    /**
     * Process incoming HTTP GET requests
     *
     *  request
     *            Object that encapsulates the request to the servlet
     *  response
     *            Object that encapsulates the response from the servlet
     */
    
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/TestServlet.java:TestServlet:doGet");
    }

    /**
     * Process incoming HTTP POST requests
     *
     *  request
     *            Object that encapsulates the request to the servlet
     *  response
     *            Object that encapsulates the response from the servlet
     */
    
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/TestServlet.java:TestServlet:doPost");
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
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/TestServlet.java:TestServlet:performTask");
    }
}
