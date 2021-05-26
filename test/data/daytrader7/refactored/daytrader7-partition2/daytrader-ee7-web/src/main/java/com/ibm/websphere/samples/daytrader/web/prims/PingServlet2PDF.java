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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.websphere.samples.daytrader.util.Log;

/**
 *
 * PingServlet2PDF tests a call to a servlet which then loads a PDF document.
 *
 */

public class PingServlet2PDF extends HttpServlet {

    private static final long serialVersionUID = -1321793174442755868L;
    private static int hitCount = 0;
    private static final int BUFFER_SIZE = 1024 * 8; // 8 KB

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
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/prims/PingServlet2PDF.java:PingServlet2PDF:doPost");
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
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/prims/PingServlet2PDF.java:PingServlet2PDF:doGet");
    }
}