/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package winspapus.herramienta;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import winspapus.Principal;
/**
 *
 * @author Spapu
 */
public class hilomanoobra extends Thread{
    private Connection conexion;
    private java.sql.Connection conn;
    RecuperarTab recup;
    Principal prin;
    hilomanoobra(java.sql.Connection conexion, java.sql.Connection conn, RecuperarTab recup, Principal prin) {
        this.conexion=(Connection) conexion;
        this.conn= conn;
        this.recup=recup;
        this.prin=prin;
    }
    
    @Override
    public void run() {
         try {
            
            Statement carga= (Statement) conn.createStatement();
          
            String sql = "Select * from Config";
            ResultSet rst = carga.executeQuery(sql);
            String mtabu;
            rst.next();
            mtabu = rst.getString("IDConfig");
         
            //MANO DE OBRA
            Statement carga4= (Statement) conn.createStatement();
            Statement escribir4 = (Statement) conexion.createStatement();
            sql = "Select * from manoobra";
            ResultSet rst4 = carga4.executeQuery(sql); 
             while(rst4.next()){

                 sql= "REPLACE INTO mmotabs VALUES ('"+mtabu+"',"
                                              + "'"+rst4.getString("IDManoObra")+"',"
                                              + "'"+rst4.getString("Descripcion")+"',"
                                              + rst4.getFloat("Bono")+","                                                         
                                              + rst4.getFloat("Salario")+","                                                         
                                              + rst4.getFloat("Subsidio")+","                                                                                                 
                                              + "1)";                   
                   escribir4.execute(sql); 

            }
          
            
        } catch (SQLException ex) {
            Logger.getLogger(RecuperarTab.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
