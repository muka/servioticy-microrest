/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.createnet.servioticy.microrest.rest;

import javax.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;

/**
 *
 * @author Luca Capra <luca.capra@gmail.com>
 */
@ApplicationPath("/micro")
public class AppConfig extends ResourceConfig {
    public AppConfig() {
        packages("org.createnet.servioticy.microrest.rest");
    }
}