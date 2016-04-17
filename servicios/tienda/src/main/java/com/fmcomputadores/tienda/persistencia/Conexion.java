/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fmcomputadores.tienda.persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Efrai
 */
public class Conexion {

    private static final String URL="jdbc:mysql://52.38.92.137:3306/tienda";
    private static final String USUARIO="root";
    private static final String PASSWORD="123456";
    
    public static Connection getConnection(){
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection conn = DriverManager.getConnection(URL,USUARIO,PASSWORD);
            return conn;
        } catch (ClassNotFoundException | SQLException | InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
        throw new RuntimeException("No se pudo realizar la conexion a la bd");
    }
    
    public static void close(Connection con){
        try {
            if(con!=null){
                con.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void close(PreparedStatement con){
        try {
            if(con!=null){
                con.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void close(ResultSet con){
        try {
            if(con!=null){
                con.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
