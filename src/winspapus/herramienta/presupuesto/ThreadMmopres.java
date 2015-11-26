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
public class ThreadMmopres extends Thread{
    private Connection conex;
    private String mpres;
    RespaldarPresupuesto respaldo;
    private String temp="";
    public ThreadMmopres(Connection conex, String mpres, RespaldarPresupuesto respaldo)
    {
        this.conex=conex;
        this.mpres = mpres;
        this.respaldo=respaldo;
    }

    
    
    @Override
    public void run(){
        int cont=0;
        String select = "SELECT * FROM mmopres WHERE mpre_id ='"+mpres+"' OR mpre_id IN "
                + "(SELECT id FROM mpres WHERE mpres_id='"+mpres+"')";
        
        try {
            Statement st = conex.createStatement();
            ResultSet rst = st.executeQuery(select);
            while(rst.next())
            {
                if(cont>0)
                    temp = temp+",\n";
                if(cont==0)
                    temp = "REPLACE INTO mmopres (mpre_id, id, descri, bono, salario, subsid, status) VALUES \n";
                temp = temp+"('"+rst.getString("mpre_id") +"', '"+rst.getString("id") +"', "
                        + "'"+rst.getString("descri") +"', "+rst.getString("bono") +", "
                        + ""+rst.getString("salario") +", "+rst.getString("subsid") +","
                        + "'"+rst.getString("status") +"')";
                cont++;
            }
            if(cont>0)
            temp=temp+";\n";
        } catch (SQLException ex) {
            Logger.getLogger(ThreadMmopres.class.getName()).log(Level.SEVERE, null, ex);
        }
        respaldo.mmopres = temp;
        respaldo.verificaHilos();
    }
}
