/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Al$x
 */
public class ConexionBD {


    public static Connection getConnection() throws SQLException, ClassNotFoundException {
String usuario = "username";
String contrasena = "password";
String connectionUrl = "yourdb or localhost" + usuario + ";password=" + 
        contrasena+";encrypt=false;trustServerCertificate=false";
        Connection connection = DriverManager.getConnection(connectionUrl);
         System.out.println("a");
        return connection;
       
    }

}