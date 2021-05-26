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
        
        stockChanges.add(0, message);
        
        // Add stock, remove if needed
        if(stockChanges.size() > 5) {
            stockChanges.remove(5);
        }
    }
    
    public static JsonObject stockChangesInJSON() {
        
        JsonObjectBuilder jObjectBuilder = Json.createObjectBuilder();
        
        try {
            int i = 1;
            
            List<Message> temp = new LinkedList<Message>(stockChanges);
                        
            for (Iterator<Message> iterator = temp.iterator(); iterator.hasNext();) {
                Message message = iterator.next();
                            
                jObjectBuilder.add("change" + i + "_stock", message.getStringProperty("symbol"));
                jObjectBuilder.add("change" + i + "_price","$" + message.getStringProperty("price"));          
                            
                BigDecimal change = new BigDecimal(message.getStringProperty("price")).subtract(new BigDecimal(message.getStringProperty("oldPrice")));
                change.setScale(2, RoundingMode.HALF_UP);
                
                jObjectBuilder.add("change" + i + "_change", change.toString());
                
                i++;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return jObjectBuilder.build();
    }
    
    public static boolean isEmpty() {
        return stockChanges.isEmpty();
    }        

}
