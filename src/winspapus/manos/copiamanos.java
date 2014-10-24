/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package winspapus.manos;

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
public class copiamanos extends Thread{
     private Connection conex;
    String mtabus, numpartida, codicove, tabnuevo;
    private String sql;
    private Statement st ;
    private ResultSet rsma;
    public copiamanos(Connection conexion, String mtabu, String tabnuevo, String num, String codicove){
         this.conex = conexion;
        this.mtabus = mtabu;
        this.numpartida = num;
        this.tabnuevo = tabnuevo;
        try {
            st = (Statement) conex.createStatement();
            sql= "SELECT mmotab_id, cantidad, bono, salario, subsidi FROM dmoptabs WHERE mtabus_id = '"+mtabus+"' AND numero="+numpartida;
            this.codicove = codicove;
            rsma = st.executeQuery(sql);
        } catch (SQLException ex) {
            Logger.getLogger(copiamanos.class.getName()).log(Level.SEVERE, null, ex);
    }
    }
    
    @Override
    public void run(){
        try {
            while (rsma.next()) {
                Statement s = (Statement) conex.createStatement();
                      String mmoptab_id =rsma.getObject(1).toString();
                      String cantidad = rsma.getObject(2).toString();
                      String bono = rsma.getObject(3).toString();
                      String salario = rsma.getObject(4).toString();
                      String subsidi = rsma.getObject(5).toString();
                      sql = "INSERT INTO dmoptabs VALUES ('"+tabnuevo+"', " + 

                                                            "'"+mmoptab_id+"'," + 

                                                            ""+numpartida+", " + 

                                                            ""+cantidad+", " +

                                                            ""+bono+", " +
                                                            
                                                            ""+salario+", " +
                                                            
                                                            ""+subsidi+", " +
                              
                                                            "1,'"+codicove+"');";
            try {
                    s.execute(sql);
                } catch (SQLException ex) {
                    Logger.getLogger(copiamanos.class.getName()).log(Level.SEVERE, null, ex);
                }
        }
        } catch (SQLException ex) {
            Logger.getLogger(copiamanos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
