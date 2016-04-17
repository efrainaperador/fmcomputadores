/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fmcomputadores.tienda.persistencia;

import com.fmcomputadores.tienda.dto.Marca;
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
public class MarcaPersistencia {
    
    private final static String SQL_CONSULTA_TODOS="select * from Marca order by nombre";
    private final static String SQL_CONSULTA="select * from Marca where id=?";
    private final static String SQL_INSERT="insert into Marca(nombre) values(?)";
    private final static String SQL_UPDATE="update Marca set nombre=? where id=?";
    
    public List<Marca> getMarcas(){
        Connection con=Conexion.getConnection();
        PreparedStatement ps=null;
        ResultSet rs=null;
        List<Marca> marcas=new ArrayList<>();
        try {
            ps=con.prepareStatement(SQL_CONSULTA_TODOS);
            rs=ps.executeQuery();
            while(rs.next()){
                marcas.add(parseMarca(rs));
            }
            return marcas;
        } catch (SQLException ex) {
            Logger.getLogger(MarcaPersistencia.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            Conexion.close(rs);
            Conexion.close(ps);
            Conexion.close(con);
        }
        return null;
    }
    
    public Marca getMarca(int id){
        Connection con=Conexion.getConnection();
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            ps=con.prepareStatement(SQL_CONSULTA);
            ps.setInt(1, id);
            rs=ps.executeQuery();
            if(rs.next()){
                return parseMarca(rs);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MarcaPersistencia.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            Conexion.close(rs);
            Conexion.close(ps);
            Conexion.close(con);
        }
        return null;
    }
    
    public Marca insertMarca(Marca m){
        Connection con=Conexion.getConnection();
        PreparedStatement ps=null, psId=null;
        ResultSet rs=null;
        try {
            ps=con.prepareStatement(SQL_INSERT);
            ps.setString(1, m.getNombre());
            ps.executeUpdate();
            psId=con.prepareStatement("select LAST_INSERT_ID()");
            rs=psId.executeQuery();
            if(rs.next()){
                m.setId(rs.getInt(1));
                return m;
            }
        } catch (SQLException ex) {
            Logger.getLogger(MarcaPersistencia.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            Conexion.close(rs);
            Conexion.close(ps);
            Conexion.close(psId);
            Conexion.close(con);
        }
        throw new RuntimeException("Error al insertar la entidad");
    }
    
    private Marca parseMarca(ResultSet rs) throws SQLException{
        Marca m=new Marca();
        m.setId(rs.getInt("id"));
        m.setNombre(rs.getString("nombre"));
        return m;
    }
    
    public void updateMarca(Marca m){
        Connection con=Conexion.getConnection();
        PreparedStatement ps=null;
        try {
            ps=con.prepareStatement(SQL_UPDATE);
            ps.setString(1, m.getNombre());
            ps.setInt(2, m.getId());
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(MarcaPersistencia.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException("Error al update la entidad");
        } finally {
            Conexion.close(ps);
            Conexion.close(con);
        }
    }
    
    public static void main(String[] args) {
        MarcaPersistencia persistencia=new MarcaPersistencia();
        /*Marca m=new Marca();
        m.setNombre("Dell");
        m=persistencia.insertMarca(m);
        System.out.println(m);
        System.out.println(m.getId());
        System.out.println(m.getNombre());*/
        List<Marca> marcas = persistencia.getMarcas();
        System.out.println(marcas.size());
        System.out.println(marcas.get(0));
        System.out.println(marcas.get(0).getId());
        System.out.println(marcas.get(0).getNombre());
    }
    
}
