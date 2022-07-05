/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.agenda.factory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author loboMal
 */
public class ConnectionFactory {
    
    //Nome do usuário do mysql   
    private static final String USERNAME = "root";
    
    //Senha do db
    private static final String PASSWORD = "95991374293";
    
    //Caminho do db, porta
    private static final String DATABASE_URL = "jdbc:mysql://10.0.0.139:3306/agenda";
    
    /*
     * CONEXÃO COM O BANCO DE DADOS!
     */
    public static Connection createConnectionToMysql () throws Exception {
        
        //Faz com que a classe (Driver) seja carregada pela JVM
        Class.forName("com.mysql.jdbc.Driver");
        
        //CRIA A CONEXÃO COM O DB
        Connection connection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
        
        return connection;
    }
    
    public static void main (String[] args) {
        
        //Recuperar uma conexão com o banco de dados
        Connection con = null;
        try {
            con = createConnectionToMysql();
        } catch (Exception ex) {
            Logger.getLogger(ConnectionFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //Testar se conexão é nula ou não
        if ( con != null ) {
            System.out.println("Conexão obtida com sucesso!");
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(ConnectionFactory.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
}
