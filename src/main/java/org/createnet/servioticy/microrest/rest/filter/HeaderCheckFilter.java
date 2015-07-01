/*
 * Copyright 2014 CREATE-NET <http://create-net.org>
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 *               http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.createnet.servioticy.microrest.rest.filter;

import java.io.IOException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author Luca Capra <luca.capra@gmail.com>
 */
@Provider
public class HeaderCheckFilter implements ContainerRequestFilter {
    
    @Override
    public void filter(ContainerRequestContext crc) throws IOException {
        
        if(!crc.getHeaders().containsKey("Authorization")) {
            throw new NotAuthorizedException("`Authorization` header missing");
        }
        
        if(!crc.getHeaders().containsKey("Object-Id")) {
            throw new NotAuthorizedException("`Object-Id` header missing");
        }
    }
    
}