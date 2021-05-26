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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.ibm.websphere.samples.daytrader.beans.MarketSummaryDataBean;
import com.ibm.websphere.samples.daytrader.entities.QuoteDataBean;
import com.ibm.websphere.samples.daytrader.util.FinancialUtils;
import com.ibm.websphere.samples.daytrader.util.Log;
import com.ibm.websphere.samples.daytrader.util.TradeConfig;


public class MarketSummarySingleton {
    
    private MarketSummaryDataBean marketSummaryDataBean;
    
    
    private EntityManager entityManager;
    
     
    private void setup () {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-ejb/src/main/java/com/ibm/websphere/samples/daytrader/ejb3/MarketSummarySingleton.java:MarketSummarySingleton:setup");
    }
    
    /* Update Market Summary every 20 seconds */
    
    private void updateMarketSummary() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-ejb/src/main/java/com/ibm/websphere/samples/daytrader/ejb3/MarketSummarySingleton.java:MarketSummarySingleton:updateMarketSummary");
    }

    
    public MarketSummaryDataBean getMarketSummaryDataBean() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-ejb/src/main/java/com/ibm/websphere/samples/daytrader/ejb3/MarketSummarySingleton.java:MarketSummarySingleton:getMarketSummaryDataBean");
    }

    
    public void setMarketSummaryDataBean(MarketSummaryDataBean marketSummaryDataBean) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-ejb/src/main/java/com/ibm/websphere/samples/daytrader/ejb3/MarketSummarySingleton.java:MarketSummarySingleton:setMarketSummaryDataBean");
    }

}
