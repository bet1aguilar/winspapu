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
public class ThreadDmpres extends Thread{
    private Connection conex;
    private String mpres;
    RespaldarPresupuesto respaldo;
    private String temp="";
    public ThreadDmpres(Connection conex, String mpres, RespaldarPresupuesto respaldo)
    {
        this.conex=conex;
        this.mpres = mpres;
        this.respaldo=respaldo;
    }

    
    
    @Override
    public void run(){
        int cont=0;
        String select = "SELECT * FROM dmpres WHERE mpre_id ='"+mpres+"' OR mpre_id IN "
                + "(SELECT id FROM mpres WHERE mpres_id='"+mpres+"')";
        
        try {
            Statement st = conex.createStatement();
            ResultSet rst = st.executeQuery(select);
            while(rst.next())
            {
                if(cont>0)
                    temp = temp+",\n";
                if(cont==0)
                    temp = "REPLACE INTO dmpres (mpre_id, mppre_id, mmpre_id, numepart, cantidad, precio, status) VALUES \n";
                temp = temp+"('"+rst.getString("mpre_id") +"', '"+rst.getString("mppre_id") +"', "
                        + "'"+rst.getString("mmpre_id") +"', "+rst.getString("numepart") +", "
                        + ""+rst.getString("cantidad") +", "+rst.getString("precio") +", "
                        + "'"+rst.getString("status") +"')";                
                cont++;
            }
            if(cont>0)
            temp=temp+";\n";
        } catch (SQLException ex) {
            Logger.getLogger(ThreadDmpres.class.getName()).log(Level.SEVERE, null, ex);
        }
        respaldo.dmpres = temp;
        respaldo.verificaHilos();
    }
}
