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

import javax.inject.Inject;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.websphere.samples.daytrader.web.prims.PingCDIBean;


public class PingServletCDIBeanManagerViaCDICurrent extends HttpServlet {

    private static final long serialVersionUID = -1803544618879689949L;
    private static String initTime;

    
    PingCDIBean cdiBean;

    
    
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/prims/PingServletCDIBeanManagerViaCDICurrent.java:PingServletCDIBeanManagerViaCDICurrent:doGet");
    }

    /**
     * called when the class is loaded to initialize the servlet
     * 
     *  config
     *            ServletConfig:
     **/
    
    public void init(ServletConfig config) throws ServletException {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/prims/PingServletCDIBeanManagerViaCDICurrent.java:PingServletCDIBeanManagerViaCDICurrent:init");
    }
}
