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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class PingReentryServlet extends HttpServlet {

    private static final long serialVersionUID = -2536027021580175706L;
    
    
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/prims/PingReentryServlet.java:PingReentryServlet:doPost");
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
    
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/prims/PingReentryServlet.java:PingReentryServlet:doGet");
    }

    /**
     * returns a string of information about the servlet
     *
     *  info String: contains info about the servlet
     **/
    
    public String getServletInfo() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/prims/PingReentryServlet.java:PingReentryServlet:getServletInfo");
    }

    /**
     * called when the class is loaded to initialize the servlet
     *
     *  config
     *            ServletConfig:
     **/
    
    public void init(ServletConfig config) throws ServletException {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/prims/PingReentryServlet.java:PingReentryServlet:init");
    }
}
