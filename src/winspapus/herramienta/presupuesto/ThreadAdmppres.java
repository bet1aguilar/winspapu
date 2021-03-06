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
public class ThreadAdmppres extends Thread{
    private Connection conex;
    private String mpres;
    RespaldarPresupuesto respaldo;
    private String temp="";
    public ThreadAdmppres(Connection conex, String mpres, RespaldarPresupuesto respaldo)
    {
        this.conex=conex;
        this.mpres = mpres;
        this.respaldo=respaldo;
    }

    
    
    @Override
    public void run(){
        int cont=0;
        String select = "SELECT payd_id, mpre_id, numepart, mvalu_id, IFNULL(aumento,0) as aumento, "
                + "IFNULL(disminucion,0) as disminucion FROM admppres WHERE mpre_id ='"+mpres+"' OR mpre_id IN "
                + "(SELECT id FROM mpres WHERE mpres_id='"+mpres+"')";
        
        try {
            Statement st = conex.createStatement();
            ResultSet rst = st.executeQuery(select);
            while(rst.next())
            {
                if(cont>0)
                    temp = temp+",\n";
                if(cont==0)
                    temp = "REPLACE INTO admppres (payd_id, mpre_id, numepart, mvalu_id, aumento, disminucion) VALUES \n";
                temp = temp+"("+rst.getString("payd_id") +", '"+rst.getString("mpre_id") +"', "
                        + "'"+rst.getString("numepart") +"', '"+rst.getString("mvalu_id") +"', "
                        + ""+rst.getString("aumento") +", "+rst.getString("disminucion") +")";
                cont++;                
            }
            if(cont>0)
            temp=temp+";\n";
        } catch (SQLException ex) {
            Logger.getLogger(ThreadAdmppres.class.getName()).log(Level.SEVERE, null, ex);
        }
        respaldo.admppres = temp;
        respaldo.verificaHilos();
    }
}
