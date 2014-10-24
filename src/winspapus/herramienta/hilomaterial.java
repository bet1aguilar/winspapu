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
public class hilomaterial extends Thread{
    private Connection conexion;
    private java.sql.Connection conn;
    RecuperarTab recup;
    Principal prin;
 public static final int RET_OK = 1;
    hilomaterial(java.sql.Connection conexion, java.sql.Connection conn, RecuperarTab recup, Principal prin) {
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
            
            
              //MATERIALES
            Statement carga6= (Statement) conn.createStatement();
            Statement escribir6 = (Statement) conexion.createStatement();
            sql = "Select * from materiales";
            ResultSet rst6 = carga6.executeQuery(sql); 
             while(rst6.next()){
                 descri=rst6.getString("Descripcion");
                 descri=descri.replace("'", "º");
                 sql= "INSERT INTO mmtabs VALUES ('"+mtabu+"',"
                                              + "'"+rst6.getString("IDMateriales")+"',"
                                              + "'"+descri+"',"
                                              + rst6.getFloat("Desperdicio")+","                                                         
                                              + rst6.getFloat("Precio")+","                                                          
                                              + "'"+rst6.getString("Unidad")+"',"
                                              + "1)";
                  escribir6.execute(sql); 

            }
                                        
        } catch (SQLException ex) {
            Logger.getLogger(RecuperarTab.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
