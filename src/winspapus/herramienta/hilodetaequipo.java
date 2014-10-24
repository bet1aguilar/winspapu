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
public class hilodetaequipo extends Thread{
    private Connection conexion;
    private java.sql.Connection conn;
    RecuperarTab recup;
    Principal prin;
 public static final int RET_OK = 1;
    hilodetaequipo(java.sql.Connection conexion, java.sql.Connection conn, RecuperarTab recup, Principal prin) {
        this.conexion=(Connection) conexion;
        this.conn= conn;
        this.recup=recup;
        this.prin=prin;
    }
    public void conectar(){
        
    }
    @Override
    public void run() {
         try {
            
            Statement carga= (Statement) conn.createStatement();
            Statement escribir = (Statement) conexion.createStatement();
            String sql = "Select * from Config";
            ResultSet rst = carga.executeQuery(sql);
            String mtabu;
            rst.next();
            mtabu = rst.getString("IDConfig");
          
            // PARTIDAS
            String grupo,descri;
                        
           //EQUIPOS POR PARTIDAS
            Statement carga3= (Statement) conn.createStatement();
            Statement escribir3 = (Statement) conexion.createStatement();
            sql = "Select * from equipospartida";
            ResultSet rst3 = carga3.executeQuery(sql); 
             while(rst3.next()){

                 sql= "INSERT INTO deptabs VALUES ('"+mtabu+"',"
                                              + "'"+rst3.getString("IDPartidas")+"',"
                                              + "'"+rst3.getString("IDEquipos")+"',"
                                              + rst3.getString("Numero")+","
                                              + rst3.getFloat("cantidad")+","                                                         
                                              + rst3.getFloat("precio")+","                                                          
                                              + "1)";                   
                   escribir3.execute(sql); 

            }             
            
            
        } catch (SQLException ex) {
            Logger.getLogger(RecuperarTab.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
