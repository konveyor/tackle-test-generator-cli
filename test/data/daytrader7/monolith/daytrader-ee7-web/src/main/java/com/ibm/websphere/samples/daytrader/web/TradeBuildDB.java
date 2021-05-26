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
package com.ibm.websphere.samples.daytrader.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;

import com.ibm.websphere.samples.daytrader.direct.TradeDirect;
import com.ibm.websphere.samples.daytrader.entities.AccountDataBean;
import com.ibm.websphere.samples.daytrader.util.Log;
import com.ibm.websphere.samples.daytrader.util.TradeConfig;

/**
 * TradeBuildDB uses operations provided by the TradeApplication to (a) create the Database tables
 * (b)populate a DayTrader database without creating the tables. Specifically, a
 * new DayTrader User population is created using UserIDs of the form "uid:xxx"
 * where xxx is a sequential number (e.g. uid:0, uid:1, etc.). New stocks are also created of the
 * form "s:xxx", again where xxx represents sequential numbers (e.g. s:1, s:2, etc.)
 */
public class TradeBuildDB {

    /**
     * Populate a Trade DB using standard out as a log
     */
    public TradeBuildDB() throws Exception {
        this(new java.io.PrintWriter(System.out), null);
    }

    /**
     * Re-create the DayTrader db tables and populate them OR just populate a DayTrader DB, logging to the provided output stream
     */
    public TradeBuildDB(java.io.PrintWriter out, InputStream ddlFile) throws Exception {
        String symbol, companyName;
        int errorCount = 0; // Give up gracefully after 10 errors
        
        // Build db in direct mode because it is faster
        TradeDirect tradeDirect = new TradeDirect();

        //  TradeStatistics.statisticsEnabled=false;  // disable statistics
        out.println("<HEAD><BR><EM> TradeBuildDB: Building DayTrader Database...</EM><BR> This operation will take several minutes. Please wait...</HEAD>");
        out.println("<BODY>");

        if (ddlFile != null) {
            //out.println("<BR>TradeBuildDB: **** warPath= "+warPath+" ****</BR></BODY>");

        	boolean success = false;

            Object[] sqlBuffer = null;

            //parse the DDL file and fill the SQL commands into a buffer
            try {
                sqlBuffer = parseDDLToBuffer(ddlFile);
            } catch (Exception e) {
                Log.error(e, "TradeBuildDB: Unable to parse DDL file");
                out.println("<BR>TradeBuildDB: **** Unable to parse DDL file for the specified database ****</BR></BODY>");
                return;
            }
            if ((sqlBuffer == null) || (sqlBuffer.length == 0)) {
                out.println("<BR>TradeBuildDB: **** Parsing DDL file returned empty buffer, please check that a valid DB specific DDL file is available and retry ****</BR></BODY>");
                return;
            }

            // send the sql commands buffer to drop and recreate the Daytrader tables
            out.println("<BR>TradeBuildDB: **** Dropping and Recreating the DayTrader tables... ****</BR>");
            try {
                success = tradeDirect.recreateDBTables(sqlBuffer, out);
            } catch (Exception e) {
                Log.error(e, "TradeBuildDB: Unable to drop and recreate DayTrader Db Tables, please check for database consistency before continuing");
                out.println("TradeBuildDB: Unable to drop and recreate DayTrader Db Tables, please check for database consistency before continuing");
                return;
            }
            if (!success) {
                out.println("<BR>TradeBuildDB: **** Unable to drop and recreate DayTrader Db Tables, please check for database consistency before continuing ****</BR></BODY>");
                return;
            }
            out.println("<BR>TradeBuildDB: **** DayTrader tables successfully created! ****</BR><BR><b> Please Stop and Re-start your Daytrader application (or your application server) and then use the \"Repopulate Daytrader Database\" link to populate your database.</b></BR><BR><BR></BODY>");
            return;
        } // end of createDBTables

        out.println("<BR>TradeBuildDB: **** Creating " + TradeConfig.getMAX_QUOTES() + " Quotes ****</BR>");
        //Attempt to delete all of the Trade users and Trade Quotes first
        try {
            tradeDirect.resetTrade(true);
        } catch (Exception e) {
            Log.error(e, "TradeBuildDB: Unable to delete Trade users (uid:0, uid:1, ...) and Trade Quotes (s:0, s:1, ...)");
        }
        for (int i = 0; i < TradeConfig.getMAX_QUOTES(); i++) {
            symbol = "s:" + i;
            companyName = "S" + i + " Incorporated";
            try {
                tradeDirect.createQuote(symbol, companyName, new java.math.BigDecimal(TradeConfig.rndPrice()));
                if (i % 10 == 0) {
                    out.print("....." + symbol);
                    if (i % 100 == 0) {
                        out.println(" -<BR>");
                        out.flush();
                    }
                }
            } catch (Exception e) {
                if (errorCount++ >= 10) {
                    String error = "Populate Trade DB aborting after 10 create quote errors. Check the EJB datasource configuration. Check the log for details <BR><BR> Exception is: <BR> "
                            + e.toString();
                    Log.error(e, error);
                    throw e;
                }
            }
        }
        out.println("<BR>");
        out.println("<BR>**** Registering " + TradeConfig.getMAX_USERS() + " Users **** ");
        errorCount = 0; //reset for user registrations

        // Registration is a formal operation in Trade 2.
        for (int i = 0; i < TradeConfig.getMAX_USERS(); i++) {
            String userID = "uid:" + i;
            String fullname = TradeConfig.rndFullName();
            String email = TradeConfig.rndEmail(userID);
            String address = TradeConfig.rndAddress();
            String creditcard = TradeConfig.rndCreditCard();
            double initialBalance = (double) (TradeConfig.rndInt(100000)) + 200000;
            if (i == 0) {
                initialBalance = 1000000; // uid:0 starts with a cool million.
            }
            try {
                AccountDataBean accountData = tradeDirect.register(userID, "xxx", fullname, address, email, creditcard, new BigDecimal(initialBalance));

                if (accountData != null) {
                    if (i % 50 == 0) {
                        out.print("<BR>Account# " + accountData.getAccountID() + " userID=" + userID);
                    } // end-if

                    int holdings = TradeConfig.rndInt(TradeConfig.getMAX_HOLDINGS() + 1); // 0-MAX_HOLDING (inclusive), avg holdings per user = (MAX-0)/2
                    double quantity = 0;

                    for (int j = 0; j < holdings; j++) {
                        symbol = TradeConfig.rndSymbol();
                        quantity = TradeConfig.rndQuantity();
                        tradeDirect.buy(userID, symbol, quantity, TradeConfig.orderProcessingMode);
                    } // end-for
                    if (i % 50 == 0) {
                        out.println(" has " + holdings + " holdings.");
                        out.flush();
                    } // end-if
                } else {
                    out.println("<BR>UID " + userID + " already registered.</BR>");
                    out.flush();
                } // end-if

            } catch (Exception e) {
                if (errorCount++ >= 10) {
                    String error = "Populate Trade DB aborting after 10 user registration errors. Check the log for details. <BR><BR> Exception is: <BR>"
                            + e.toString();
                    Log.error(e, error);
                    throw e;
                }
            }
        } // end-for
        out.println("</BODY>");
    }

    public Object[] parseDDLToBuffer(InputStream ddlFile) throws Exception {
        BufferedReader br = null;
        ArrayList<String> sqlBuffer = new ArrayList<String>(30); //initial capacity 30 assuming we have 30 ddl-sql statements to read

        try {
            if (Log.doTrace())
                Log.traceEnter("TradeBuildDB:parseDDLToBuffer - " + ddlFile);

            br = new BufferedReader(new InputStreamReader(ddlFile));
            String s;
            String sql = new String();
            while ((s = br.readLine()) != null) {
                s = s.trim();
                if ((s.length() != 0) && (s.charAt(0) != '#')) // Empty lines or lines starting with "#" are ignored
                {
                    sql = sql + " " + s;
                    if (s.endsWith(";")) { // reached end of sql statement
                        sql = sql.replace(';', ' '); //remove the semicolon
                        sqlBuffer.add(sql);
                        sql = "";
                    }
                }
            }
        } catch (IOException ex) {
            Log.error("TradeBuildDB:parseDDLToBuffer Exeception during open/read of File: " + ddlFile, ex);
            throw ex;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ex) {
                    Log.error("TradeBuildDB:parseDDLToBuffer Failed to close BufferedReader", ex);
                }
            }
        }
        return sqlBuffer.toArray();
    }

    public static void main(String[] args) throws Exception {
        new TradeBuildDB();

    }
}
