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

import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpUpgradeHandler;
import javax.servlet.http.WebConnection;
import javax.servlet.annotation.WebServlet;

import com.ibm.websphere.samples.daytrader.util.Log;


public class PingUpgradeServlet extends HttpServlet {
    private static final long serialVersionUID = -6955518532146927509L;
   

    
    protected void doGet(final HttpServletRequest req, final HttpServletResponse res) throws ServletException, IOException {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/prims/PingUpgradeServlet.java:PingUpgradeServlet:doGet");
    }
    
    
    protected void doPost(final HttpServletRequest req, final HttpServletResponse res) throws ServletException, IOException {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/prims/PingUpgradeServlet.java:PingUpgradeServlet:doPost");
    }

    public static class Handler implements HttpUpgradeHandler {
    
        
        public void init(final WebConnection wc) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/prims/PingUpgradeServlet.java:PingUpgradeServlet::Handler:init");
    }

        
        public void destroy() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/prims/PingUpgradeServlet.java:PingUpgradeServlet::Handler:destroy");
    }
    }

    private static class Listener implements ReadListener {
        private final WebConnection connection;
        private ServletInputStream input = null;
        private ServletOutputStream output = null;
        
        private Listener(final WebConnection connection) throws IOException  {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/prims/PingUpgradeServlet.java:PingUpgradeServlet::Listener:Listener");
    }

        
        public void onDataAvailable() throws IOException {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/prims/PingUpgradeServlet.java:PingUpgradeServlet::Listener:onDataAvailable");
    }

        private void closeConnection() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/prims/PingUpgradeServlet.java:PingUpgradeServlet::Listener:closeConnection");
    }
        
        
        
        public void onAllDataRead() throws IOException {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/prims/PingUpgradeServlet.java:PingUpgradeServlet::Listener:onAllDataRead");
    }

        
        public void onError(final Throwable t) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/prims/PingUpgradeServlet.java:PingUpgradeServlet::Listener:onError");
    }
    }
}