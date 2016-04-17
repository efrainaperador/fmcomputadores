/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fmcomputadores.tienda.persistencia;

import com.fmcomputadores.tienda.CategoriaServicio;
import com.fmcomputadores.tienda.MarcaServicio;
import com.fmcomputadores.tienda.dto.Categoria;
import com.fmcomputadores.tienda.dto.Marca;
import com.fmcomputadores.tienda.dto.Producto;
import com.google.gson.Gson;
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
public class ProductoPersistencia {
    
    private final static String SQL_CONSULTA_TODOS="select * from Producto order by nombre";
    private final static String SQL_CONSULTA_CATEGORIA="select * from Producto where categoria=? order by nombre";
    private final static String SQL_CONSULTA="select * from Producto where id=?";
    private final static String SQL_INSERT="insert into Producto(nombre,descripcion,especificaciones,precio,estado,categoria,marca)"
            + " values(?,?,?,?,?,?,?) ";
    private final static String SQL_UPDATE="update Producto "
            + "set nombre=?, "
            + "descripcion=?,"
            + "especificaciones=?,"
            + "precio=?,"
            + "estado=?,"
            + "categoria=?,"
            + "marca=? "
            + "where id=?";

    public List<Producto> getProductos(){
        Connection con=Conexion.getConnection();
        PreparedStatement ps=null;
        ResultSet rs=null;
        List<Producto> productos=new ArrayList<>();
        try {
            ps=con.prepareStatement(SQL_CONSULTA_TODOS);
            rs=ps.executeQuery();
            while(rs.next()){
                productos.add(parseProducto(rs));
            }
            return productos;
        } catch (SQLException ex) {
            Logger.getLogger(ProductoPersistencia.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            Conexion.close(rs);
            Conexion.close(ps);
            Conexion.close(con);
        }
        return null;
    }
    
    public List<Producto> getProductosCategoria(int categoria){
        Connection con=Conexion.getConnection();
        PreparedStatement ps=null;
        ResultSet rs=null;
        List<Producto> productos=new ArrayList<>();
        try {
            ps=con.prepareStatement(SQL_CONSULTA_CATEGORIA);
            ps.setInt(1, categoria);
            rs=ps.executeQuery();
            while(rs.next()){
                productos.add(parseProducto(rs));
            }
            return productos;
        } catch (SQLException ex) {
            Logger.getLogger(ProductoPersistencia.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            Conexion.close(rs);
            Conexion.close(ps);
            Conexion.close(con);
        }
        return null;
    }
    
    public Producto getProducto(int id){
        Connection con=Conexion.getConnection();
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            ps=con.prepareStatement(SQL_CONSULTA);
            ps.setInt(1, id);
            rs=ps.executeQuery();
            if(rs.next()){
                return parseProducto(rs);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductoPersistencia.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            Conexion.close(rs);
            Conexion.close(ps);
            Conexion.close(con);
        }
        return null;
    }
    
    public Producto insertProducto(Producto c){
        Connection con=Conexion.getConnection();
        PreparedStatement ps=null, psId=null;
        ResultSet rs=null;
        try {
            con.setAutoCommit(false);
            ps=con.prepareStatement(SQL_INSERT);
            ps.setString(1, c.getNombre());
            ps.setString(2, c.getDescripcion());
            ps.setString(3, c.getEspecificacion());
            ps.setDouble(4, c.getPrecio());
            ps.setBoolean(5, c.isEstado());
            ps.setInt(6, c.getCategoria().getId());
            ps.setInt(7, c.getMarca().getId());
            ps.executeUpdate();
            psId=con.prepareStatement("select LAST_INSERT_ID()");
            rs=psId.executeQuery();
            if(rs.next()){
                c.setId(rs.getInt(1));
                //insertar imagenes
                new ImagenPersistencia().insertImagenes(c.getImagenes(), c.getId());
                con.commit();
                return c;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductoPersistencia.class.getName()).log(Level.SEVERE, null, ex);
            if(con!=null){
                try {
                    con.rollback();
                } catch (SQLException ex1) {
                    Logger.getLogger(ProductoPersistencia.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
        } finally {
            Conexion.close(rs);
            Conexion.close(ps);
            Conexion.close(psId);
            Conexion.close(con);
        }
        throw new RuntimeException("Error al insertar la entidad");
    }
    
    private Producto parseProducto(ResultSet rs) throws SQLException{
        Producto m=new Producto();
        m.setId(rs.getInt("id"));
        m.setNombre(rs.getString("nombre"));
        m.setCategoria(new CategoriaPersistencia().getCategoria(rs.getInt("categoria")));
        m.setDescripcion(rs.getString("descripcion"));
        m.setEspecificacion(rs.getString("especificaciones"));
        m.setEstado(rs.getBoolean("estado"));
        m.setMarca(new MarcaPersistencia().getMarca(rs.getInt("marca")));
        m.setPrecio(rs.getDouble("precio"));
        m.setImagenes(new ImagenPersistencia().getImagenProducto(m.getId()));
        return m;
    }
    
    public void updateProducto(Producto p){
        Connection con=Conexion.getConnection();
        PreparedStatement ps=null;
        try {
            ps=con.prepareStatement(SQL_UPDATE);
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getDescripcion());
            ps.setString(3, p.getEspecificacion());
            ps.setDouble(4, p.getPrecio());
            ps.setBoolean(5, p.isEstado());
            ps.setInt(6, p.getCategoria().getId());
            ps.setInt(7, p.getMarca().getId());
            ps.setInt(8, p.getId());
            ps.executeUpdate();
            //actualizar imagenes
            new ImagenPersistencia().insertImagenes(p.getImagenes(), p.getId());
        } catch (SQLException ex) {
            Logger.getLogger(ProductoPersistencia.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException("Error al update la entidad");
        } finally {
            Conexion.close(ps);
            Conexion.close(con);
        }
    }
    
    public static void main(String[] args) {
        Producto p = new Producto();
        p.setCategoria(new Gson().fromJson(new CategoriaServicio().getCategoria(1),Categoria.class));
        p.setMarca(new Gson().fromJson(new MarcaServicio().getMarca(1), Marca.class));
        p.setDescripcion("Descripción de producto 1: Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum");
        p.setEspecificacion("Especificación producto 1: \\n Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean in tristique diam, at ullamcorper dolor. Fusce hendrerit quis lectus ac mattis. Pellentesque semper nulla porta posuere finibus. Duis dolor metus, malesuada eget ligula sed, congue dapibus ante. Sed condimentum efficitur ipsum commodo vestibulum. Vestibulum semper eros ut eros venenatis volutpat. Suspendisse porttitor erat egestas quam iaculis, eu mollis tortor finibus. Proin eget vehicula eros, sit amet fermentum lacus. Nam molestie urna maximus, tincidunt elit vitae, pharetra felis. Donec sagittis massa ac diam lobortis ullamcorper. Pellentesque gravida fringilla mauris, eu rhoncus lectus posuere sit amet.\n"
                + "\\nNunc velit quam, porta id ex in, fermentum hendrerit dui. Cras venenatis viverra erat, vitae iaculis ipsum convallis sit amet. Nulla eros libero, eleifend in imperdiet quis, lobortis sed massa. In odio lacus, pharetra ut elit in, eleifend luctus metus. Morbi eget scelerisque mi, nec tincidunt mi. Nunc luctus dignissim bibendum. Duis tempor porta velit eu maximus. Vestibulum purus leo, mollis nec auctor ac, commodo id lacus. Donec felis elit, congue eget dolor rutrum, scelerisque tristique lorem. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Morbi quis erat ut ex laoreet viverra vel quis lorem. Mauris a leo leo. Morbi elementum augue vel vehicula consequat. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Nunc commodo elit vel varius tristique. \n"
                + "\\nAliquam et dui venenatis, feugiat nulla at, mattis sapien. Quisque at interdum nulla. Curabitur elit magna, pulvinar vitae metus sed, dapibus dictum nisi. Sed tortor dui, vestibulum ut tortor a, malesuada blandit nulla. Proin a scelerisque orci, in mollis justo. Morbi vulputate arcu laoreet, lobortis neque et, ultricies turpis. Mauris auctor pharetra molestie. Vestibulum faucibus tristique neque. Suspendisse cursus sagittis imperdiet. Ut congue blandit lorem, vel accumsan lacus luctus id. Proin turpis justo, aliquet a eros eget, consectetur blandit magna. \n"
                + "\\nVestibulum lacus tellus, egestas et felis non, laoreet dignissim lacus. Integer faucibus, enim porta rutrum aliquam, sapien augue congue libero, a porttitor urna ante sit amet dui. Cras non sapien enim. In sodales fringilla nisl, eget hendrerit metus rhoncus id. Vivamus pellentesque in turpis semper ornare. Sed dui dolor, dignissim quis fringilla in, egestas sit amet neque. Pellentesque quis quam risus. Mauris neque nunc, volutpat quis porta vitae, ultricies vitae massa. \n"
                + "\\nQuisque eu viverra dolor. Fusce finibus nisi ut turpis tempor volutpat eget eu ipsum. Donec est enim, aliquam id ullamcorper non, pretium nec augue. Etiam ultrices velit erat, at consequat leo commodo vel. Morbi lobortis ullamcorper felis, commodo lacinia lectus tincidunt eu. Morbi id lectus dignissim, iaculis lacus eget, congue enim. Sed posuere elementum metus non vehicula. Phasellus felis dolor, placerat et vestibulum a, mattis nec est. Praesent a ornare metus. ");
        p.setNombre("Producto 1");
        p.setPrecio(1000000);
        p.setImagenes(new ArrayList<String>());
        p.getImagenes().add("http://www.solutekcolombia.com/images/computadores_escritorio_hp.jpeg");
        p.getImagenes().add("http://www.solutekpartes.com.co/wp-content/uploads/2014/03/venta-al-por-mayor-de-computadores-portatiles-toshiba-consumo-940x460.jpg");
        ProductoPersistencia persistencia=new ProductoPersistencia();
        persistencia.insertProducto(p);
        
    }
}
