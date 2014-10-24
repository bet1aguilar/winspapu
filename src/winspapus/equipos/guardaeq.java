/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package winspapus.equipos;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JProgressBar;
import winspapus.Principal;
import winspapus.consulta;
import winspapus.tab;

/**
 *
 * @author Betmart
 */
public class guardaeq extends Thread{
  private int cont = 0 ;
 ResultSet propio;
    Connection conex=null;
    boolean sigue=false;
  
    private String id, descri, desperdi, precio, unidad, status, tabnuevo;
  
   private String sql="", mtabuid, deprecia;
   Statement st = null, stmt=null;
   Principal obj;
   JProgressBar barra;

   
    public guardaeq(ResultSet rs, Connection conex, String tab, String tab2, Principal obj, JProgressBar barrita) throws SQLException{
        propio =rs;
        barra = barrita;
        st = (Statement) conex.createStatement();
        this.obj = obj;
        mtabuid = tab;
        this.conex = conex;
        tabnuevo = tab2;        
           this.descri=descri; 
        this.conex = conex;
        try {
            st = (Statement) conex.createStatement();
            stmt =(Statement) conex.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(consulta.class.getName()).log(Level.SEVERE, null, ex);
        }
        sigue = true;
    }
    
     public void run(){
        try {
            while (propio.next()) {
                     Statement str = (Statement) conex.createStatement();
                  
                  
                        id=propio.getObject(1).toString();
                      descri = propio.getObject(2).toString();
                      deprecia = propio.getObject(3).toString();
                      precio = propio.getObject(4).toString();
                      status= propio.getObject(5).toString();
                     
                      
                      sql = "INSERT INTO Metabs VALUES ('"+tabnuevo+"', '"+id+"', " + 

                                                            "'"+descri+"'," + 

                                                            ""+deprecia+", " + 
                                                            
                                                            ""+precio+", " +
                                                            
                                                            "'"+status+"' );";
            try {
                    str.execute(sql);
                } catch (SQLException ex) {
                    Logger.getLogger(tab.class.getName()).log(Level.SEVERE, null, ex);
                }
                
               }
             
             
        } catch (SQLException ex) {
            Logger.getLogger(consulta.class.getName()).log(Level.SEVERE, null, ex);
        }
        
     }
}

