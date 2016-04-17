/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fmcomputadores.tienda.persistencia;

import com.fmcomputadores.tienda.dto.Producto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Efrai
 */
public class ImagenPersistencia {
    
    private final static String SQL_CONSULTA_TODOS="select * from Imagen where producto=? order by id";
    private final static String SQL_DELETE="delete from Imagen where producto=?";
    private final static String SQL_INSERT="insert into Imagen(url, producto) values(?,?) ";
    
    public List<String> getImagenProducto(int producto){
        Connection con=Conexion.getConnection();
        PreparedStatement ps=null;
        ResultSet rs=null;
        List<String> imagenes=new ArrayList<>();
        try {
            ps=con.prepareStatement(SQL_CONSULTA_TODOS);
            ps.setInt(1, producto);
            rs=ps.executeQuery();
            while(rs.next()){
                imagenes.add(rs.getString("url"));
            }
            return imagenes;
        } catch (SQLException ex) {
            Logger.getLogger(ProductoPersistencia.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            Conexion.close(rs);
            Conexion.close(ps);
            Conexion.close(con);
        }
        return null;
    }
    
    public void insertImagenes(List<String> imagenes, int producto){
        Connection con=Conexion.getConnection();
        PreparedStatement ps=null, psDel=null;
        try {
            psDel=con.prepareStatement(SQL_DELETE);
            psDel.setInt(1, producto);
            psDel.executeUpdate();
            for(String url:imagenes){
                ps=con.prepareStatement(SQL_INSERT);
                ps.setString(1, url);
                ps.setInt(2, producto);
                ps.executeUpdate();
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductoPersistencia.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            Conexion.close(psDel);
            Conexion.close(ps);
            Conexion.close(con);
        }
    }
}
