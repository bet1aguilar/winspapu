/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parametros;


import com.mysql.jdbc.Statement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Betmart
 */
public class bloqueo {
    private Connection conex;
    public bloqueo(Connection conex){
        this.conex=conex;
    }
    public boolean isbloqued(){
        boolean bloq=false;
        String select = "SELECT bloqueado FROM instalacion";
        try {
            Statement st = (Statement) conex.createStatement();
            ResultSet rst = st.executeQuery(select);
            while(rst.next())
            {
               
                if(rst.getInt(1)==1){
                    bloq=true;
                }else{
                    bloq=false;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(bloqueo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bloq;
    }
    public void actualiza(int bloquea){
        String update = "UPDATE instalacion SET bloqueado="+bloquea+"";
        try {
            Statement st = (Statement) conex.createStatement();
            st.execute(update);
        } catch (SQLException ex) {
            Logger.getLogger(bloqueo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
