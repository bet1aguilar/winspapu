/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package winspapus.herramienta;

import com.mysql.jdbc.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import winspapus.Principal;

/**
 *
 * @author Betmart
 */
public class hilodetmano extends Thread{
    
    private Connection conexion;
    private java.sql.Connection conn;
    RecuperarTab recup;
    String mtabu;
    Principal prin;    
    public static final int RET_OK = 1;
    hilodetmano(java.sql.Connection conexion, java.sql.Connection conn, RecuperarTab recup, Principal prin, String mtabu) {
        this.conexion=(Connection) conexion;
        this.conn= conn;
        this.recup=recup;
        this.mtabu = mtabu;
        this.prin=prin;
    }
    @Override
    public void run() {
         try {
             
           //MANO DE OBRA POR PARTIDAS
            Statement carga5= (Statement) conn.createStatement();
            Statement escribir5 = (Statement) conexion.createStatement();
            String sql = "Select * from manoobrapartida";
            ResultSet rst5 = carga5.executeQuery(sql); 
            while(rst5.next()){ 
                 sql= "REPLACE INTO dmoptabs "
                         + "(mtabus_id, mmotab_id, numero, cantidad,status, mptab_id) "
                         + "VALUES ('"+mtabu+"',"                                              
                                              + "'"+rst5.getString("IDManoObra")+"',"
                                              + rst5.getInt("Numero")+","
                                              + rst5.getFloat("Cantidad")+","                                                                                             
                                              + "'1',"
                                              + "'"+rst5.getString("IDPartidas")+"')";                   
                   escribir5.execute(sql); 
            }
            JOptionPane.showMessageDialog(recup, "Recuperación de datos del Tabulador Completado..");
            recup.doClose(RET_OK);
         }catch (SQLException ex) {
            Logger.getLogger(RecuperarTab.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
