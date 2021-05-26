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

import com.ibm.cardinal.util.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ibm.websphere.samples.daytrader.TradeAction;
import com.ibm.websphere.samples.daytrader.TradeServices;
import com.ibm.websphere.samples.daytrader.entities.AccountDataBean;
import com.ibm.websphere.samples.daytrader.entities.AccountProfileDataBean;
import com.ibm.websphere.samples.daytrader.entities.HoldingDataBean;
import com.ibm.websphere.samples.daytrader.entities.OrderDataBean;
import com.ibm.websphere.samples.daytrader.entities.QuoteDataBean;
import com.ibm.websphere.samples.daytrader.util.Log;
import com.ibm.websphere.samples.daytrader.util.TradeConfig;

/**
 * TradeServletAction provides servlet specific client side access to each of
 * the Trade brokerage user operations. These include login, logout, buy, sell,
 * getQuote, etc. TradeServletAction manages a web interface to Trade handling
 * HttpRequests/HttpResponse objects and forwarding results to the appropriate
 * JSP page for the web interface. TradeServletAction invokes
 * { TradeAction} methods to actually perform each trading operation.
 *
 */
public class TradeServletAction {

    private TradeServices tAction = null;

    TradeServletAction() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/TradeServletAction.java:TradeServletAction:TradeServletAction");
    }

    /**
     * Display User Profile information such as address, email, etc. for the
     * given Trader Dispatch to the Trade Account JSP for display
     *
     *  userID
     *            The User to display profile info
     *  ctx
     *            the servlet context
     *  req
     *            the HttpRequest object
     *  resp
     *            the HttpResponse object
     *  results
     *            A short description of the results/success of this web request
     *            provided on the web page
     *  javax.servlet.ServletException
     *                If a servlet specific exception is encountered
     *  javax.io.IOException
     *                If an exception occurs while writing results back to the
     *                user
     *
     */
    void doAccount(ServletContext ctx, HttpServletRequest req, HttpServletResponse resp, String userID, String results) throws javax.servlet.ServletException,
            java.io.IOException {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/TradeServletAction.java:TradeServletAction:doAccount");
    }

    /**
     * Update User Profile information such as address, email, etc. for the
     * given Trader Dispatch to the Trade Account JSP for display If any in put
     * is incorrect revert back to the account page w/ an appropriate message
     *
     *  userID
     *            The User to upddate profile info
     *  password
     *            The new User password
     *  cpassword
     *            Confirm password
     *  fullname
     *            The new User fullname info
     *  address
     *            The new User address info
     *  cc
     *            The new User credit card info
     *  email
     *            The new User email info
     *  ctx
     *            the servlet context
     *  req
     *            the HttpRequest object
     *  resp
     *            the HttpResponse object
     *  javax.servlet.ServletException
     *                If a servlet specific exception is encountered
     *  javax.io.IOException
     *                If an exception occurs while writing results back to the
     *                user
     *
     */
    void doAccountUpdate(ServletContext ctx, HttpServletRequest req, HttpServletResponse resp, String userID, String password, String cpassword,
            String fullName, String address, String creditcard, String email) throws javax.servlet.ServletException, java.io.IOException {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/TradeServletAction.java:TradeServletAction:doAccountUpdate");
    }

    /**
     * Buy a new holding of shares for the given trader Dispatch to the Trade
     * Portfolio JSP for display
     *
     *  userID
     *            The User buying shares
     *  symbol
     *            The stock to purchase
     *  amount
     *            The quantity of shares to purchase
     *  ctx
     *            the servlet context
     *  req
     *            the HttpRequest object
     *  resp
     *            the HttpResponse object
     *  javax.servlet.ServletException
     *                If a servlet specific exception is encountered
     *  javax.io.IOException
     *                If an exception occurs while writing results back to the
     *                user
     *
     */
    void doBuy(ServletContext ctx, HttpServletRequest req, HttpServletResponse resp, String userID, String symbol, String quantity) throws ServletException,
            IOException {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/TradeServletAction.java:TradeServletAction:doBuy");
    }

    /**
     * Create the Trade Home page with personalized information such as the
     * traders account balance Dispatch to the Trade Home JSP for display
     *
     *  ctx
     *            the servlet context
     *  req
     *            the HttpRequest object
     *  resp
     *            the HttpResponse object
     *  results
     *            A short description of the results/success of this web request
     *            provided on the web page
     *  javax.servlet.ServletException
     *                If a servlet specific exception is encountered
     *  javax.io.IOException
     *                If an exception occurs while writing results back to the
     *                user
     *
     */
    void doHome(ServletContext ctx, HttpServletRequest req, HttpServletResponse resp, String userID, String results) throws javax.servlet.ServletException,
            java.io.IOException {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/TradeServletAction.java:TradeServletAction:doHome");
    }

    /**
     * Login a Trade User. Dispatch to the Trade Home JSP for display
     *
     *  userID
     *            The User to login
     *  passwd
     *            The password supplied by the trader used to authenticate
     *  ctx
     *            the servlet context
     *  req
     *            the HttpRequest object
     *  resp
     *            the HttpResponse object
     *  results
     *            A short description of the results/success of this web request
     *            provided on the web page
     *  javax.servlet.ServletException
     *                If a servlet specific exception is encountered
     *  javax.io.IOException
     *                If an exception occurs while writing results back to the
     *                user
     *
     */
    void doLogin(ServletContext ctx, HttpServletRequest req, HttpServletResponse resp, String userID, String passwd) throws javax.servlet.ServletException,
            java.io.IOException {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/TradeServletAction.java:TradeServletAction:doLogin");
    }

    /**
     * Logout a Trade User Dispatch to the Trade Welcome JSP for display
     *
     *  userID
     *            The User to logout
     *  ctx
     *            the servlet context
     *  req
     *            the HttpRequest object
     *  resp
     *            the HttpResponse object
     *  results
     *            A short description of the results/success of this web request
     *            provided on the web page
     *  javax.servlet.ServletException
     *                If a servlet specific exception is encountered
     *  javax.io.IOException
     *                If an exception occurs while writing results back to the
     *                user
     *
     */
    void doLogout(ServletContext ctx, HttpServletRequest req, HttpServletResponse resp, String userID) throws ServletException, IOException {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/TradeServletAction.java:TradeServletAction:doLogout");
    }

    /**
     * Retrieve the current portfolio of stock holdings for the given trader
     * Dispatch to the Trade Portfolio JSP for display
     *
     *  userID
     *            The User requesting to view their portfolio
     *  ctx
     *            the servlet context
     *  req
     *            the HttpRequest object
     *  resp
     *            the HttpResponse object
     *  results
     *            A short description of the results/success of this web request
     *            provided on the web page
     *  javax.servlet.ServletException
     *                If a servlet specific exception is encountered
     *  javax.io.IOException
     *                If an exception occurs while writing results back to the
     *                user
     *
     */
    void doPortfolio(ServletContext ctx, HttpServletRequest req, HttpServletResponse resp, String userID, String results) throws ServletException, IOException {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/TradeServletAction.java:TradeServletAction:doPortfolio");
    }

    /**
     * Retrieve the current Quote for the given stock symbol Dispatch to the
     * Trade Quote JSP for display
     *
     *  userID
     *            The stock symbol used to get the current quote
     *  ctx
     *            the servlet context
     *  req
     *            the HttpRequest object
     *  resp
     *            the HttpResponse object
     *  javax.servlet.ServletException
     *                If a servlet specific exception is encountered
     *  javax.io.IOException
     *                If an exception occurs while writing results back to the
     *                user
     *
     */
    void doQuotes(ServletContext ctx, HttpServletRequest req, HttpServletResponse resp, String userID, String symbols) throws ServletException, IOException {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/TradeServletAction.java:TradeServletAction:doQuotes");
    }

    /**
     * Register a new trader given the provided user Profile information such as
     * address, email, etc. Dispatch to the Trade Home JSP for display
     *
     *  userID
     *            The User to create
     *  passwd
     *            The User password
     *  fullname
     *            The new User fullname info
     *  ccn
     *            The new User credit card info
     *  money
     *            The new User opening account balance
     *  address
     *            The new User address info
     *  email
     *            The new User email info
     *  The userID of the new trader
     *  ctx
     *            the servlet context
     *  req
     *            the HttpRequest object
     *  resp
     *            the HttpResponse object
     *  javax.servlet.ServletException
     *                If a servlet specific exception is encountered
     *  javax.io.IOException
     *                If an exception occurs while writing results back to the
     *                user
     *
     */
    void doRegister(ServletContext ctx, HttpServletRequest req, HttpServletResponse resp, String userID, String passwd, String cpasswd, String fullname,
            String ccn, String openBalanceString, String email, String address) throws ServletException, IOException {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/TradeServletAction.java:TradeServletAction:doRegister");
    }

    /**
     * Sell a current holding of stock shares for the given trader. Dispatch to
     * the Trade Portfolio JSP for display
     *
     *  userID
     *            The User buying shares
     *  symbol
     *            The stock to sell
     *  indx
     *            The unique index identifying the users holding to sell
     *  ctx
     *            the servlet context
     *  req
     *            the HttpRequest object
     *  resp
     *            the HttpResponse object
     *  javax.servlet.ServletException
     *                If a servlet specific exception is encountered
     *  javax.io.IOException
     *                If an exception occurs while writing results back to the
     *                user
     *
     */
    void doSell(ServletContext ctx, HttpServletRequest req, HttpServletResponse resp, String userID, Integer holdingID) throws ServletException, IOException {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/TradeServletAction.java:TradeServletAction:doSell");
    }

    void doWelcome(ServletContext ctx, HttpServletRequest req, HttpServletResponse resp, String status) throws ServletException, IOException {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/TradeServletAction.java:TradeServletAction:doWelcome");
    }

    private void requestDispatch(ServletContext ctx, HttpServletRequest req, HttpServletResponse resp, String userID, String page) throws ServletException,
            IOException {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/TradeServletAction.java:TradeServletAction:requestDispatch");
    }

    void doMarketSummary(ServletContext ctx, HttpServletRequest req, HttpServletResponse resp, String userID) throws ServletException, IOException {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/TradeServletAction.java:TradeServletAction:doMarketSummary");
    }
}