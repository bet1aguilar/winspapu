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
public class ThreadMvalus extends Thread{
    private Connection conex;
    private String mpres;
    RespaldarPresupuesto respaldo;
    private String temp="";
    public ThreadMvalus(Connection conex, String mpres, RespaldarPresupuesto respaldo)
    {
        this.conex=conex;
        this.mpres = mpres;
        this.respaldo=respaldo;
    }

    
    
    @Override
    public void run(){
        int cont=0;
        String select = "SELECT *, IFNULL(status,1) FROM mvalus WHERE mpre_id ='"+mpres+"' OR mpre_id IN "
                + "(SELECT id FROM mpres WHERE mpres_id='"+mpres+"')";
        
        try {
            Statement st = conex.createStatement();
            ResultSet rst = st.executeQuery(select);
            while(rst.next())
            {
                String status = rst.getString("status");
                if(cont>0)
                    temp = temp+",\n";
                if(cont==0)
                    temp = "REPLACE INTO mvalus (id, desde, hasta, status, mpre_id, tipo, lapso) VALUES \n";
                temp = temp+"("+rst.getString("id") +", '"+rst.getString("desde") +"', "
                        + "'"+rst.getString("hasta") +"', IF('"+status+"'='null',NULL,IFNULL("+status+",1)), "
                        + "'"+rst.getString("mpre_id") +"', '"+rst.getString("tipo") +"',"
                        + ""+rst.getString("lapso") +")";
                cont++;
            }
            if(cont>0)
            temp=temp+";\n";
        } catch (SQLException ex) {
            Logger.getLogger(ThreadMvalus.class.getName()).log(Level.SEVERE, null, ex);
        }
        respaldo.mvalus = temp;
        respaldo.verificaHilos();
    }
}
