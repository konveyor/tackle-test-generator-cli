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
package com.ibm.websphere.samples.daytrader.web.jsf;

import com.ibm.cardinal.util.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import com.ibm.websphere.samples.daytrader.util.FinancialUtils;



public class HoldingData implements Serializable {

    private static final long serialVersionUID = -4760036695773749721L;

    private Integer holdingID;
    private double quantity;
    private BigDecimal purchasePrice;
    private Date purchaseDate;
    private String quoteID;
    private BigDecimal price;
    private BigDecimal basis;
    private BigDecimal marketValue;
    private BigDecimal gain;

    public void setHoldingID(Integer holdingID) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/HoldingData.java:HoldingData:setHoldingID");
    }

    public Integer getHoldingID() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/HoldingData.java:HoldingData:getHoldingID");
    }

    public void setQuantity(double quantity) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/HoldingData.java:HoldingData:setQuantity");
    }

    public double getQuantity() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/HoldingData.java:HoldingData:getQuantity");
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/HoldingData.java:HoldingData:setPurchasePrice");
    }

    public BigDecimal getPurchasePrice() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/HoldingData.java:HoldingData:getPurchasePrice");
    }

    public void setPurchaseDate(Date purchaseDate) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/HoldingData.java:HoldingData:setPurchaseDate");
    }

    public Date getPurchaseDate() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/HoldingData.java:HoldingData:getPurchaseDate");
    }

    public void setQuoteID(String quoteID) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/HoldingData.java:HoldingData:setQuoteID");
    }

    public String getQuoteID() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/HoldingData.java:HoldingData:getQuoteID");
    }

    public void setPrice(BigDecimal price) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/HoldingData.java:HoldingData:setPrice");
    }

    public BigDecimal getPrice() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/HoldingData.java:HoldingData:getPrice");
    }

    public void setBasis(BigDecimal basis) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/HoldingData.java:HoldingData:setBasis");
    }

    public BigDecimal getBasis() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/HoldingData.java:HoldingData:getBasis");
    }

    public void setMarketValue(BigDecimal marketValue) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/HoldingData.java:HoldingData:setMarketValue");
    }

    public BigDecimal getMarketValue() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/HoldingData.java:HoldingData:getMarketValue");
    }

    public void setGain(BigDecimal gain) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/HoldingData.java:HoldingData:setGain");
    }

    public BigDecimal getGain() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/HoldingData.java:HoldingData:getGain");
    }

    public String getGainHTML() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-web/src/main/java/com/ibm/websphere/samples/daytrader/web/jsf/HoldingData.java:HoldingData:getGainHTML");
    }
}
