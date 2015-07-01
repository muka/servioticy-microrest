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
package org.createnet.servioticy.microrest.rest;

import java.io.IOException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.createnet.compose.Compose;
import org.createnet.compose.exception.HttpException;
import org.createnet.compose.exception.RecordsetException;
import org.createnet.compose.exception.RestClientException;
import org.createnet.compose.object.Channel;
import org.createnet.compose.object.ResultSet;
import org.createnet.compose.object.Stream;
import org.createnet.compose.recordset.IRecord;
import org.createnet.compose.recordset.RecordSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author luca capra <luca.capra@gmail.com>
 */
@Path("/stream")
public class StreamRest {

    Logger logger = LoggerFactory.getLogger(StreamRest.class);    
    
    protected String uri = "http://servioticy.local";
    
    @Context
    UriInfo uriInfo;

    @Context
    ServletContext servletContext;

    @Context
    private transient HttpServletRequest servletRequest;
    
    @GET @Path("/{stream}/{channel}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response read(
        @Context HttpHeaders headers, 
        @PathParam("stream") String streamName, 
        @PathParam("channel") String channelName
    ) {
        
        String authToken = headers.getHeaderString("Authorization");
        String soId = headers.getHeaderString("Object-Id");
        
        Compose compose = new Compose(authToken, uri);
        
        try {

            Channel channel = compose.createChannel(soId, streamName, channelName);
            
            ResultSet results = channel.getStream().pull(true);
            if(results == null || results.isEmpty()) {
                return Response.status(Response.Status.NO_CONTENT).build();
            }
            
            RecordSet recordSet = results.get(0);
            
            IRecord record = recordSet.getByChannelName(channelName);
            
            if(record == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("Channel data not found").build();
            }
            
            return Response.status(Response.Status.OK).entity(record.getValue()).build();
            
        } catch (HttpException ex) {
            return Response.status(ex.getStatus()).entity(ex.getStatusText()).build();
        } catch (RestClientException ex) {
            logger.error("Error on pull request", ex);
            return Response.serverError().entity("Error performing request").build();
        } catch (RecordsetException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }

    }
    
    @PUT @Path("/{stream}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response saveJson(
        @Context HttpHeaders headers,
        @PathParam("stream") String streamName, 
        String body
    ) {

        String authToken = headers.getHeaderString("Authorization");
        String soId = headers.getHeaderString("Object-Id");
        
        Compose compose = new Compose(authToken, uri);        
        Stream stream = compose.createStream(soId, streamName);
        try {
            
            RecordSet recordset = new RecordSet(stream, body);
            stream.push(recordset);

            return Response.ok().build();            
            
        } catch (HttpException ex) {
            return Response.status(ex.getStatus()).entity(ex.getStatusText()).build();
        } catch (RecordsetException ex) {
            logger.error("Error on push request", ex);
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } catch (RestClientException | IOException ex) {
            logger.error("Error on push request", ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Internal Server Error").build();
        }

    }

}
