/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fmcomputadores.tienda;

import com.fmcomputadores.tienda.dto.Marca;
import com.fmcomputadores.tienda.persistencia.MarcaPersistencia;
import com.google.gson.Gson;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Efrai
 */
@Path("marca")
@Consumes(MediaType.TEXT_PLAIN)
@Produces(MediaType.TEXT_PLAIN)
public class MarcaServicio {
    
    private MarcaPersistencia marcaPersistencia=new MarcaPersistencia();
    private Gson gson=new Gson();
    
    @GET
    public String getMarcas(){
        return gson.toJson(marcaPersistencia.getMarcas());
    }
    
    @GET
    @Path("{id}")
    public String getMarca(@PathParam("id") int id){
        return gson.toJson(marcaPersistencia.getMarca(id));
    }
    
    @POST
    public String insertMarca(String marca){
        return gson.toJson(
                marcaPersistencia.insertMarca(
                        gson.fromJson(marca, Marca.class)));
    }
    
    @PUT
    public void updateMarca(String marca){
        marcaPersistencia.updateMarca(gson.fromJson(marca, Marca.class));
    }
    
}
