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
package com.ibm.websphere.samples.daytrader.web.prims;

import com.ibm.cardinal.util.*;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ibm.websphere.samples.daytrader.util.Log;

/**
 *
 * PingHTTPSession3 tests the servers ability to manage and persist large
 * HTTPSession data objects. The servlet creates the large custom java object
 * { PingSession3Object}. This large session object is retrieved and stored
 * to the session on each user request. The default settings result in approx
 * 2024 bits being retrieved and stored upon each request.
 *
 */

public class PingSession3 extends HttpServlet {
    private static final long serialVersionUID = -6129599971684210414L;
    private static int NUM_OBJECTS = 2;
    private static String initTime = null;
    private static int hitCount = 0;

    /**
     * forwards post requests to the doGet method Creation date: (11/6/2000
     * 10:52:39 AM)
     *
     *  res
     *            javax.servlet.http.HttpServletRequest
     *  res2
     *            javax.servlet.http.HttpServletResponse
     */
    
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/prims/PingSession3.java:PingSession3:doPost");
    }

    /**
     * this is the main method of the servlet that will service all get
     * requests.
     *
     *  request
     *            HttpServletRequest
     *  responce
     *            HttpServletResponce
     **/
    
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/prims/PingSession3.java:PingSession3:doGet");
    }

    /**
     * returns a string of information about the servlet
     *
     *  info String: contains info about the servlet
     **/
    
    public String getServletInfo() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/prims/PingSession3.java:PingSession3:getServletInfo");
    }

    /**
     * called when the class is loaded to initialize the servlet
     *
     *  config
     *            ServletConfig:
     **/
    
    public void init(ServletConfig config) throws ServletException {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/prims/PingSession3.java:PingSession3:init");
    }
}