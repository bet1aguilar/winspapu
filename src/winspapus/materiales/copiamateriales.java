/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package winspapus.materiales;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Betmart
 */
public class copiamateriales extends Thread{
    private Connection conex;
    String mtabus, numpartida, codicove;
    private String sql, tabnuevo;
    private Statement st ;
    private ResultSet rs;
    public copiamateriales(Connection conexion, String mtabu, String tabnuevo, String num, String codicove){
        this.conex = conexion;
        this.mtabus = mtabu;
        this.tabnuevo = tabnuevo;
        this.numpartida = num;
        try {
            st = (Statement) conex.createStatement();
            sql= "SELECT mmtab_id, cantidad, precio FROM dmtabs WHERE mtabus_id = '"+mtabus+"' AND numepart="+numpartida;
            this.codicove = codicove;
            rs = st.executeQuery(sql);
        } catch (SQLException ex) {
            Logger.getLogger(copiamateriales.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    public void run(){
        try {
            while(rs.next()){
                Statement s = (Statement) conex.createStatement();
                 String mmtab_id =rs.getObject(1).toString();
                  String cantidad = rs.getObject(2).toString();
                  String precio = rs.getObject(3).toString();
                  
                  String sql1 = "INSERT INTO dmtabs VALUES ('"+tabnuevo+"', '"+codicove+"', " + 

                                                        "'"+mmtab_id+"'," + 

                                                        ""+numpartida+", " + 

                                                        ""+cantidad+", " +

                                                        ""+precio+", " + 
                                                                                                                           
                                                        "1);";
      
                s.execute(sql1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(copiamateriales.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
