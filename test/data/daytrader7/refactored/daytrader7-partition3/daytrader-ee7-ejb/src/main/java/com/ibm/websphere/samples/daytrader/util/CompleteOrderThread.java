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
package com.ibm.websphere.samples.daytrader.util;

import com.ibm.cardinal.util.*;

import javax.ejb.EJBException;
import javax.naming.InitialContext;
import javax.transaction.UserTransaction;

import com.ibm.websphere.samples.daytrader.TradeServices;
import com.ibm.websphere.samples.daytrader.direct.TradeDirect;
import com.ibm.websphere.samples.daytrader.ejb3.TradeSLSBBean;

public class CompleteOrderThread implements Runnable {

        final Integer orderID;
        boolean twoPhase;
        
        
        public CompleteOrderThread (Integer id, boolean twoPhase) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-ejb/src/main/java/com/ibm/websphere/samples/daytrader/util/CompleteOrderThread.java:CompleteOrderThread:CompleteOrderThread");
    }
        
        
        public void run() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-ejb/src/main/java/com/ibm/websphere/samples/daytrader/util/CompleteOrderThread.java:CompleteOrderThread:run");
    }
}
