/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fmcomputadores.tienda;

import com.fmcomputadores.tienda.dto.Categoria;
import com.fmcomputadores.tienda.persistencia.CategoriaPersistencia;
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
@Path("categoria")
@Consumes(MediaType.TEXT_PLAIN)
@Produces(MediaType.TEXT_PLAIN)
public class CategoriaServicio {
    
    private CategoriaPersistencia categoriaPersistencia=new CategoriaPersistencia();
    private Gson gson=new Gson();
    
    @GET
    public String getCategorias(){
        return gson.toJson(categoriaPersistencia.getCategorias());
    }
    
    @GET
    @Path("{id}")
    public String getCategoria(@PathParam("id") int id){
        return gson.toJson(categoriaPersistencia.getCategoria(id));
    }
    
    @POST
    public String insertCategoria(String categoria){
        return gson.toJson(
                categoriaPersistencia.insertCategoria(
                        gson.fromJson(categoria, Categoria.class)));
    }
    
    @PUT
    public void updateCategoria(String categoria){
        categoriaPersistencia.updateCategoria(
                gson.fromJson(categoria, Categoria.class));
    }
    
}
