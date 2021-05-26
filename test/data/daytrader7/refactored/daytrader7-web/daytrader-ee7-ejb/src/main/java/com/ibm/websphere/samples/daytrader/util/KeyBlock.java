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

import java.util.AbstractSequentialList;
import java.util.ListIterator;

public class KeyBlock extends AbstractSequentialList<Object> {

    // min and max provide range of valid primary keys for this KeyBlock
    private int min = 0;
    private int max = 0;
    private int index = 0;

    /**
     * Constructor for KeyBlock
     */
    public KeyBlock() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-ejb/src/main/java/com/ibm/websphere/samples/daytrader/util/KeyBlock.java:KeyBlock:KeyBlock [overloaded_#001]");
    }

    /**
     * Constructor for KeyBlock
     */
    public KeyBlock(int min, int max) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-ejb/src/main/java/com/ibm/websphere/samples/daytrader/util/KeyBlock.java:KeyBlock:KeyBlock [overloaded_#002]");
    }

    /**
     *  AbstractCollection#size()
     */
    
    public int size() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-ejb/src/main/java/com/ibm/websphere/samples/daytrader/util/KeyBlock.java:KeyBlock:size");
    }

    /**
     *  AbstractSequentialList#listIterator(int)
     */
    
    public ListIterator<Object> listIterator(int arg0) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-ejb/src/main/java/com/ibm/websphere/samples/daytrader/util/KeyBlock.java:KeyBlock:listIterator");
    }

    class KeyBlockIterator implements ListIterator<Object> {

        /**
         *  ListIterator#hasNext()
         */
        
        public boolean hasNext() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-ejb/src/main/java/com/ibm/websphere/samples/daytrader/util/KeyBlock.java:KeyBlock::KeyBlockIterator:hasNext");
    }

        /**
         *  ListIterator#next()
         */
        
        public synchronized Object next() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-ejb/src/main/java/com/ibm/websphere/samples/daytrader/util/KeyBlock.java:KeyBlock::KeyBlockIterator:next");
    }

        /**
         *  ListIterator#hasPrevious()
         */
        
        public boolean hasPrevious() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-ejb/src/main/java/com/ibm/websphere/samples/daytrader/util/KeyBlock.java:KeyBlock::KeyBlockIterator:hasPrevious");
    }

        /**
         *  ListIterator#previous()
         */
        
        public Object previous() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-ejb/src/main/java/com/ibm/websphere/samples/daytrader/util/KeyBlock.java:KeyBlock::KeyBlockIterator:previous");
    }

        /**
         *  ListIterator#nextIndex()
         */
        
        public int nextIndex() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-ejb/src/main/java/com/ibm/websphere/samples/daytrader/util/KeyBlock.java:KeyBlock::KeyBlockIterator:nextIndex");
    }

        /**
         *  ListIterator#previousIndex()
         */
        
        public int previousIndex() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-ejb/src/main/java/com/ibm/websphere/samples/daytrader/util/KeyBlock.java:KeyBlock::KeyBlockIterator:previousIndex");
    }

        /**
         *  ListIterator#add()
         */
        
        public void add(Object o) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-ejb/src/main/java/com/ibm/websphere/samples/daytrader/util/KeyBlock.java:KeyBlock::KeyBlockIterator:add");
    }

        /**
         *  ListIterator#remove()
         */
        
        public void remove() {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-ejb/src/main/java/com/ibm/websphere/samples/daytrader/util/KeyBlock.java:KeyBlock::KeyBlockIterator:remove");
    }

        /**
         *  ListIterator#set(Object)
         */
        
        public void set(Object arg0) {
		throw new CardinalException("ERROR: dummy function called at daytrader-ee7-ejb/src/main/java/com/ibm/websphere/samples/daytrader/util/KeyBlock.java:KeyBlock::KeyBlockIterator:set");
    }
    }
}