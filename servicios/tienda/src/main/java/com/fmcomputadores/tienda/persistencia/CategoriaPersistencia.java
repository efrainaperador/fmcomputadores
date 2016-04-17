/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fmcomputadores.tienda.persistencia;

import com.fmcomputadores.tienda.dto.Categoria;
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
public class CategoriaPersistencia {
    
    private final static String SQL_CONSULTA_TODOS="select * from Categoria order by nombre";
    private final static String SQL_CONSULTA="select * from Categoria where id=?";
    private final static String SQL_INSERT="insert into Categoria(nombre,icono) values(?,?)";
    private final static String SQL_UPDATE="update Categoria set nombre=?, icono=? where id=?";
    
    public List<Categoria> getCategorias(){
        Connection con=Conexion.getConnection();
        PreparedStatement ps=null;
        ResultSet rs=null;
        List<Categoria> categorias=new ArrayList<>();
        try {
            ps=con.prepareStatement(SQL_CONSULTA_TODOS);
            rs=ps.executeQuery();
            while(rs.next()){
                categorias.add(parseCategoria(rs));
            }
            return categorias;
        } catch (SQLException ex) {
            Logger.getLogger(CategoriaPersistencia.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            Conexion.close(rs);
            Conexion.close(ps);
            Conexion.close(con);
        }
        return null;
    }
    
    public Categoria getCategoria(int id){
        Connection con=Conexion.getConnection();
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            ps=con.prepareStatement(SQL_CONSULTA);
            ps.setInt(1, id);
            rs=ps.executeQuery();
            if(rs.next()){
                return parseCategoria(rs);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CategoriaPersistencia.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            Conexion.close(rs);
            Conexion.close(ps);
            Conexion.close(con);
        }
        return null;
    }
    
    public Categoria insertCategoria(Categoria c){
        Connection con=Conexion.getConnection();
        PreparedStatement ps=null, psId=null;
        ResultSet rs=null;
        try {
            ps=con.prepareStatement(SQL_INSERT);
            ps.setString(1, c.getNombre());
            ps.setString(2, c.getIcono());
            ps.executeUpdate();
            psId=con.prepareStatement("select LAST_INSERT_ID()");
            rs=psId.executeQuery();
            if(rs.next()){
                c.setId(rs.getInt(1));
                return c;
            }
        } catch (SQLException ex) {
            Logger.getLogger(CategoriaPersistencia.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            Conexion.close(rs);
            Conexion.close(ps);
            Conexion.close(psId);
            Conexion.close(con);
        }
        throw new RuntimeException("Error al insertar la entidad");
    }
    
    private Categoria parseCategoria(ResultSet rs) throws SQLException{
        Categoria m=new Categoria();
        m.setId(rs.getInt("id"));
        m.setNombre(rs.getString("nombre"));
        m.setIcono(rs.getString("icono"));
        return m;
    }
    
    public void updateCategoria(Categoria m){
        Connection con=Conexion.getConnection();
        PreparedStatement ps=null;
        try {
            ps=con.prepareStatement(SQL_UPDATE);
            ps.setString(1, m.getNombre());
            ps.setString(2, m.getIcono());
            ps.setInt(3, m.getId());
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(CategoriaPersistencia.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException("Error al update la entidad");
        } finally {
            Conexion.close(ps);
            Conexion.close(con);
        }
    }
    
    public static void main(String[] args) {
        CategoriaPersistencia persistencia=new CategoriaPersistencia();
        Categoria c=new Categoria();
        c.setNombre("Portatiles");
        c.setIcono("http://comprarportatil.info/wp-content/uploads/2015/05/portatilesj.png");
        c=persistencia.insertCategoria(c);
        System.out.println(c.getId());
        System.out.println(c.getNombre());
        System.out.println(c.getIcono());
    }
    
}
