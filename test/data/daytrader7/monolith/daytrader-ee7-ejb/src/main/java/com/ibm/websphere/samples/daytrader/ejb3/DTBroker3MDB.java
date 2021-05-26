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

@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
@MessageDriven(activationConfig = { @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "TradeBrokerQueue"),
        @ActivationConfigProperty(propertyName = "subscriptionDurability", propertyValue = "NonDurable") })
public class DTBroker3MDB implements MessageListener {
    private final MDBStats mdbStats;
    private int statInterval = 10000;

    
    // TODO: Using local interface, make it configurable to use remote?
    @EJB
    private TradeSLSBLocal tradeSLSB;

    @Resource
    public MessageDrivenContext mdc;

    public DTBroker3MDB() {
        if (Log.doTrace()) {
            Log.trace("DTBroker3MDB:DTBroker3MDB()");
        }
        if (statInterval <= 0) {
            statInterval = 10000;
        }
        mdbStats = MDBStats.getInstance();
    }

    @Override
    public void onMessage(Message message) {
        try {
            if (Log.doTrace()) {
                Log.trace("TradeBroker:onMessage -- received message -->" + ((TextMessage) message).getText() + "command-->"
                        + message.getStringProperty("command") + "<--");
            }

            if (message.getJMSRedelivered()) {
                Log.log("DTBroker3MDB: The following JMS message was redelivered due to a rollback:\n" + ((TextMessage) message).getText());
                // Order has been cancelled -- ignore returned messages
                return;
            }
            String command = message.getStringProperty("command");
            if (command == null) {
                Log.debug("DTBroker3MDB:onMessage -- received message with null command. Message-->" + message);
                return;
            }
            if (command.equalsIgnoreCase("neworder")) {
                /* Get the Order ID and complete the Order */
                Integer orderID = new Integer(message.getIntProperty("orderID"));
                boolean twoPhase = message.getBooleanProperty("twoPhase");
                boolean direct = message.getBooleanProperty("direct");
                long publishTime = message.getLongProperty("publishTime");
                long receiveTime = System.currentTimeMillis();

                TradeServices trade = null;

                try {
                    trade = getTrade(direct);

                    if (Log.doTrace()) {
                        Log.trace("DTBroker3MDB:onMessage - completing order " + orderID + " twoPhase=" + twoPhase + " direct=" + direct);
                    }

                    trade.completeOrder(orderID, twoPhase);

                    TimerStat currentStats = mdbStats.addTiming("DTBroker3MDB:neworder", publishTime, receiveTime);

                    if ((currentStats.getCount() % statInterval) == 0) {
                        Log.log(" DTBroker3MDB: processed " + statInterval + " stock trading orders." +
                                " Total NewOrders process = " + currentStats.getCount() +
                                "Time (in seconds):" +
                                " min: " +currentStats.getMinSecs()+
                                " max: " +currentStats.getMaxSecs()+
                                " avg: " +currentStats.getAvgSecs());
                    }
                } catch (Exception e) {
                    Log.error("DTBroker3MDB:onMessage Exception completing order: " + orderID + "\n", e);
                    mdc.setRollbackOnly();
                    /*
                     * UPDATE - order is cancelled in trade if an error is
                     * caught try { trade.cancelOrder(orderID, twoPhase); }
                     * catch (Exception e2) { Log.error("order cancel failed",
                     * e); }
                     */
                }
            } else if (command.equalsIgnoreCase("ping")) {
                if (Log.doTrace()) {
                    Log.trace("DTBroker3MDB:onMessage  received test command -- message: " + ((TextMessage) message).getText());
                }

                long publishTime = message.getLongProperty("publishTime");
                long receiveTime = System.currentTimeMillis();

                TimerStat currentStats = mdbStats.addTiming("DTBroker3MDB:ping", publishTime, receiveTime);

                if ((currentStats.getCount() % statInterval) == 0) {
                    Log.log(" DTBroker3MDB: received " + statInterval + " ping messages." +
                            " Total ping message count = " + currentStats.getCount() +
                            " Time (in seconds):" +
                            " min: " +currentStats.getMinSecs()+
                            " max: " +currentStats.getMaxSecs()+
                            " avg: " +currentStats.getAvgSecs());
                }
            } else {
                Log.error("DTBroker3MDB:onMessage - unknown message request command-->" + command + "<-- message=" + ((TextMessage) message).getText());
            }
        } catch (Throwable t) {
            // JMS onMessage should handle all exceptions
            Log.error("DTBroker3MDB: Error rolling back transaction", t);
            mdc.setRollbackOnly();
        }
    }

    private TradeServices getTrade(boolean direct) throws Exception {
        TradeServices trade;
        if (direct) {
            trade = new TradeDirect();
        } else {
            trade = tradeSLSB;
        }

        return trade;
    }

}
