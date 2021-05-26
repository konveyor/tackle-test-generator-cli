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

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

import com.ibm.websphere.samples.daytrader.util.Log;

// This is coded to be a Text type decoder expecting JSON format. 
// It will decode incoming messages into object of type String
public class ActionDecoder implements Decoder.Text<ActionMessage> {

    public ActionDecoder() {
    }
    
    @Override
    public void destroy() {
    }

    @Override
    public void init(EndpointConfig config) {
    }

    @Override
    public ActionMessage decode(String jsonText) throws DecodeException {
       
        if (Log.doTrace()) {
            Log.trace("ActionDecoder:decode -- received -->" + jsonText + "<--");
        }

        ActionMessage actionMessage = new ActionMessage();
        actionMessage.doDecoding(jsonText);
        return actionMessage;

    }

    @Override
    public boolean willDecode(String s) {
        return true;
    }

}
