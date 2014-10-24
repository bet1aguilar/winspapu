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

/**
 *
 * @author Betmart
 */
public class copiaequipos extends Thread{
    private Connection conex;
    String mtabus, numpartida, codicove, tabnuevo;
    private String sql;
    private Statement st ;
    private ResultSet rse;
    public copiaequipos(Connection conexion, String mtabu, String tabnuevo, String num, String codicove){
        this.conex = conexion;
        this.mtabus = mtabu;
        this.numpartida = num;
        this.tabnuevo = tabnuevo;
        try {
            st = (Statement) conex.createStatement();
            sql= "SELECT metab_id, cantidad, precio FROM deptabs WHERE mtabus_id = '"+mtabus+"' AND numero="+numpartida;
            this.codicove = codicove;
            rse = st.executeQuery(sql);
        } catch (SQLException ex) {
            Logger.getLogger(copiaequipos.class.getName()).log(Level.SEVERE, null, ex);
    }
    }
    @Override
    public void run(){
        try {
            while (rse.next()) {
                Statement stmt= (Statement) conex.createStatement();
                      String metab_id =rse.getObject(1).toString();
                      String cantidad = rse.getObject(2).toString();
                      String precio = rse.getObject(3).toString();
                      
                      sql = "INSERT INTO deptabs VALUES ('"+tabnuevo+"', '"+codicove+"', " + 

                                                            "'"+metab_id+"'," + 

                                                            ""+numpartida+", " + 

                                                            ""+cantidad+", " +

                                                            ""+precio+", " + 
                                                                                                                               
                                                            "1);";
            try {
                    stmt.execute(sql);
                } catch (SQLException ex) {
                    Logger.getLogger(copiaequipos.class.getName()).log(Level.SEVERE, null, ex);
                }
        }
        } catch (SQLException ex) {
            Logger.getLogger(copiaequipos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
