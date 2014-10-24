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
public class hiloequipos extends Thread{
    private Connection conexion;
    private java.sql.Connection conn;
    RecuperarTab recup;
    Principal prin;
 public static final int RET_OK = 1;
    hiloequipos(java.sql.Connection conexion, java.sql.Connection conn, RecuperarTab recup, Principal prin) {
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
           
            String grupo,descri;
            
             //EQUIPOS
            Statement carga2= (Statement) conn.createStatement();
            Statement escribir2 = (Statement) conexion.createStatement();
            sql = "Select * from equipos";
            ResultSet rst2 = carga2.executeQuery(sql); 
             while(rst2.next()){

                 sql= "INSERT INTO metabs VALUES ('"+mtabu+"',"
                                              + "'"+rst2.getString("IDEquipos")+"',"
                                              + "'"+rst2.getString("Descripcion")+"',"
                                              + rst2.getFloat("Depreciacion")+","                                                         
                                              + rst2.getFloat("Precio")+","                                                          
                                              + "1)";                 
                  escribir2.execute(sql); 

            }
         
            
        } catch (SQLException ex) {
            Logger.getLogger(RecuperarTab.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
