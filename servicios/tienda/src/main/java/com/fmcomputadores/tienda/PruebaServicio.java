/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fmcomputadores.tienda;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 *
 * @author Efrai
 */
@Path("prueba")
public class PruebaServicio {
    
    @GET
    public String holaMundo(){
        return "hola mundo";
    }
    
}
