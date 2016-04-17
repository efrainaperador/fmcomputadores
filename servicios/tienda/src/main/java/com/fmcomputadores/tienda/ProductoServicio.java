/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fmcomputadores.tienda;

import com.fmcomputadores.tienda.dto.Producto;
import com.fmcomputadores.tienda.persistencia.ProductoPersistencia;
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
@Path("producto")
@Consumes(MediaType.TEXT_PLAIN)
@Produces(MediaType.TEXT_PLAIN)
public class ProductoServicio {
    
    private ProductoPersistencia productoPersistencia=new ProductoPersistencia();
    private Gson gson=new Gson();
        
    @GET
    public String getProductos() {
        return gson.toJson(productoPersistencia.getProductos());
    }
    
    @GET
    @Path("categoria/{id}")
    public String getProductosCategoria(@PathParam("id") int categoria) {
        return gson.toJson(productoPersistencia.getProductosCategoria(categoria));
    }
    
    @GET
    @Path("{id}")
    public String getProducto(@PathParam("id") int id) {
        return gson.toJson(productoPersistencia.getProducto(id));
    }
    
    @POST
    public String insertProducto(String producto) {
        return gson.toJson(
                productoPersistencia.insertProducto(
                        gson.fromJson(producto, Producto.class)));
    }
    
    @PUT
    public void updateProducto(String producto) {
        productoPersistencia.updateProducto(
                gson.fromJson(producto, Producto.class));
    }
}
