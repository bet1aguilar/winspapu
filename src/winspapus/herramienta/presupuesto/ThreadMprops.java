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
public class ThreadMprops extends Thread{
    private Connection conex;
    private String mpres;
    RespaldarPresupuesto respaldo;
    private String temp="";
    public ThreadMprops(Connection conex, String mpres, RespaldarPresupuesto respaldo)
    {
        this.conex=conex;
        this.mpres = mpres;
        this.respaldo=respaldo;
    }

    
    
    @Override
    public void run(){
        int cont=0;
        String select = "SELECT mp.id, mp.nombre, IFNULL(telefo,'') as telefo, IFNULL(pagina, '') as pagina,"
                + "IFNULL(email,'') as email, IFNULL(logo,'') as logo, IFNULL(mp.status,'') as status"
                + " FROM mprops as mp, mpres as m WHERE mp.id=codpro AND (m.id ='"+mpres+"' OR m.id IN "
                + "(SELECT id FROM mpres WHERE mpres_id='"+mpres+"'))";
        
        try {
            Statement st = conex.createStatement();
            ResultSet rst = st.executeQuery(select);
            while(rst.next())
            {
                if(cont>0)
                    temp = temp+",\n";
                if(cont==0)
                    temp = "REPLACE INTO mprops (id, nombre, telefo, pagina, email, status) VALUES \n";
                temp = temp+"('"+rst.getString("mp.id") +"', '"+rst.getString("mp.nombre") +"', "
                        + "'"+rst.getString("telefo") +"', "
                        + "'"+rst.getString("pagina") +"', '"+rst.getString("email") +"',"
                        + " '"+rst.getString("status") +"')";                
                cont++;
            }
            if(cont>0)
            temp=temp+";\n";
        } catch (SQLException ex) {
            Logger.getLogger(ThreadMprops.class.getName()).log(Level.SEVERE, null, ex);
        }
        respaldo.mprops = temp;
        respaldo.verificaHilos();
    }
}
