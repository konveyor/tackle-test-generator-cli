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
package com.ibm.websphere.samples.daytrader.ejb3;

import com.ibm.cardinal.util.*;

import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenContext;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import com.ibm.websphere.samples.daytrader.TradeServices;
import com.ibm.websphere.samples.daytrader.direct.TradeDirect;
import com.ibm.websphere.samples.daytrader.util.Log;
import com.ibm.websphere.samples.daytrader.util.MDBStats;
import com.ibm.websphere.samples.daytrader.util.TimerStat;




public class DTBroker3MDB implements MessageListener {
    private final MDBStats mdbStats;
    private int statInterval = 10000;

    
    // TODO: Using local interface, make it configurable to use remote?
    
    private TradeSLSBLocal tradeSLSB;

    
    public MessageDrivenContext mdc;

    public DTBroker3MDB() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-ejb/src/main/java/com/ibm/websphere/samples/daytrader/ejb3/DTBroker3MDB.java:DTBroker3MDB:DTBroker3MDB");
    }

    
    public void onMessage(Message message) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-ejb/src/main/java/com/ibm/websphere/samples/daytrader/ejb3/DTBroker3MDB.java:DTBroker3MDB:onMessage");
    }

    private TradeServices getTrade(boolean direct) throws Exception {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-ejb/src/main/java/com/ibm/websphere/samples/daytrader/ejb3/DTBroker3MDB.java:DTBroker3MDB:getTrade");
    }

}
