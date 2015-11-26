/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package winspapus.herramienta.presupuesto;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Betmart
 * En esta clase se respaldan las partidas (REPLACE INTO mppres)
 */
public class ThreadRcppres extends Thread{
    private Connection conex;
    private String mpres;
    RespaldarPresupuesto respaldo;
    private String temp="";
    public ThreadRcppres(Connection conex, String mpres, RespaldarPresupuesto respaldo)
    {
        this.conex=conex;
        this.mpres = mpres;
        this.respaldo=respaldo;
    }

    
    
    @Override
    public void run(){
        int cont=0;
        String select = "SELECT * FROM rcppres WHERE mpre_id ='"+mpres+"' OR mpre_id IN "
                + "(SELECT id FROM mpres WHERE mpres_id='"+mpres+"')";
        
        try {
            Statement st = conex.createStatement();
            ResultSet rst = st.executeQuery(select);
            while(rst.next())
            {
                if(cont>0)
                    temp = temp+",\n";
                if(cont==0)
                    temp = "REPLACE INTO rcppres (id, mppres_id, fechaini, fechafin, mpre_id) VALUES \n";
                temp = temp+"("+rst.getString("id") +", '"+rst.getString("mppres_id") +"', "
                        + "'"+rst.getString("fechaini") +"', '"+rst.getString("fechafin") +"', "
                        + "'"+rst.getString("mpre_id") +"')";                
                cont++;
            }
            if(cont>0)
            temp=temp+";\n";
        } catch (SQLException ex) {
            Logger.getLogger(ThreadRcppres.class.getName()).log(Level.SEVERE, null, ex);
        }
        respaldo.rcppres = temp;
        respaldo.verificaHilos();
    }
}
