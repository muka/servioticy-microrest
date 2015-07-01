/*
 * Copyright 2015 CREATE-NET <http://create-net.org>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.createnet.servioticy.microrest.coap;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.coap.Request;
import org.eclipse.californium.core.server.resources.CoapExchange;

/**
 *
 * @author Luca Capra <luca.capra@gmail.com>
 */
public class StreamResource extends CoapResource {

    public StreamResource() {
        super("ServiceObject.Stream");
        getAttributes().setTitle("Resource managing streams for service objects");
    }

    @Override
    public void handleGET(CoapExchange exchange) {

//        exchange.accept(); // make it a separate response
//
//        if (exchange.getRequestOptions()....) {
//            // do something specific to the request options
//        }
//        exchange.respond(CREATED); // reply with response code only (shortcut)
        
        
        // get request to read out details
        Request request = exchange.advanced().getRequest();

        StringBuilder payload = new StringBuilder();
        payload.append(String.format("Type: %d (%s)\nCode: %d (%s)\nMID: %d\n",
                request.getType().value,
                request.getType(),
                request.getCode().value,
                request.getCode(),
                request.getMID()
        ));

        payload.append("?").append(request.getOptions().getUriQueryString());
        if (payload.length() > 64) {
            payload.delete(63, payload.length());
            payload.append('»');
        }

        // complete the request
        exchange.respond(CoAP.ResponseCode.CONTENT, payload.toString(), MediaTypeRegistry.TEXT_PLAIN);
    }

}
