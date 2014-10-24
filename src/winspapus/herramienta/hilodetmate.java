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
import winspapus.Principal;
/**
 *
 * @author Spapu
 */
public class hilodetmate extends Thread{
    private Connection conexion;
    private java.sql.Connection conn;
    RecuperarTab recup;
    Principal prin;
 public static final int RET_OK = 1;
    hilodetmate(java.sql.Connection conexion, java.sql.Connection conn, RecuperarTab recup, Principal prin) {
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
          
             //MATERIALES POR PARTIDAS
            Statement carga7= (Statement) conn.createStatement();
            Statement escribir7 = (Statement) conexion.createStatement();
            sql = "Select * from materialespartida";
            ResultSet rst7 = carga7.executeQuery(sql); 
             while(rst7.next()){                 
                 sql= "INSERT INTO dmtabs VALUES ('"+mtabu+"',"
                                              + "'"+rst7.getString("IDPartidas")+"',"
                                              + "'"+rst7.getString("IDMateriales")+"',"
                                              + rst7.getInt("Numero")+","                                                         
                                              + rst7.getFloat("Cantidad")+","
                                              + rst7.getFloat("precio")+","                                                          
                                              + "1)";                   
                   escribir7.execute(sql); 

            }       
   
            
        } catch (SQLException ex) {
            Logger.getLogger(RecuperarTab.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
