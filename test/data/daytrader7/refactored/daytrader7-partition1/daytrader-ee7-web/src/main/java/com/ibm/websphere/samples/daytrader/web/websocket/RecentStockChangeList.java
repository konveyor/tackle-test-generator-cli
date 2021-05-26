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
package com.ibm.websphere.samples.daytrader.web.websocket;

import com.ibm.cardinal.util.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.jms.Message;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;


/** This class is a holds the last 5 stock changes, used by the MarketSummary WebSocket
 * */

public class RecentStockChangeList {

    private static List<Message> stockChanges = Collections.synchronizedList(new LinkedList<Message>());
       
    public static void addStockChange(Message message) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/websocket/RecentStockChangeList.java:RecentStockChangeList:addStockChange");
    }
    
    public static JsonObject stockChangesInJSON() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/websocket/RecentStockChangeList.java:RecentStockChangeList:stockChangesInJSON");
    }
    
    public static boolean isEmpty() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/websocket/RecentStockChangeList.java:RecentStockChangeList:isEmpty");
    }        

}
