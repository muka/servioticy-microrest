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

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.createnet.servioticy.microrest.rest.StreamRest;
import org.eclipse.californium.core.CoapServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Luca Capra <luca.capra@gmail.com>
 */
@WebListener
public class CoapService implements ServletContextListener
{

    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        this.start();
    }    
    
    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        this.stop();
    }

    Logger logger = LoggerFactory.getLogger(CoapService.class);
    
    final protected CoapServer server;
    
    public CoapService() {
        
        logger.info("Initializing CoAP server");
        
        server = new CoapServer();
        server.add(new StreamResource());        
    }
    
    public void start() {
        logger.info("Starting CoAP server");
        server.start();
    }
    
    public void stop() {
        logger.info("Stopping CoAP server");
        server.stop();
    }
    
    public CoapServer getServer() {
        return server;
    }

}
