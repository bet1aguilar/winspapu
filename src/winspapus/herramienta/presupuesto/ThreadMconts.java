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
public class ThreadMconts extends Thread{
    private Connection conex;
    private String mpres;
    RespaldarPresupuesto respaldo;
    private String temp="";
    public ThreadMconts(Connection conex, String mpres, RespaldarPresupuesto respaldo)
    {
        this.conex=conex;
        this.mpres = mpres;
        this.respaldo=respaldo;
    }

    
    
    @Override
    public void run(){
        int cont=0;
        String select = "SELECT mc.id, mc.nombre, IFNULL(direcc,'') as direcc, IFNULL(telefo,'') as telefo, "
                + "IFNULL(fax,'') as fax, IFNULL(email,'') as email, IFNULL(ingres,'') as ingres, "
                + "IFNULL(cedres,'') as cedres, IFNULL(civres,'') as civres, "
                + "IFNULL(ingins,'') as ingins, IFNULL(cedins,'') as cedins, IFNULL(civins,'') as civins, "
                + "IFNULL(repleg, '') as repleg, IFNULL(cedrep,'') as cedrep, IFNULL(rif, '') as rif, "
                + "IFNULL(nit,'') as nit, IFNULL(logo,'') as logo, IFNULL(encabe,'') as encabe, "
                + "IFNULL(mc.status,'') as status,"
                + "IFNULL(pagina,'') as pagina"
                + " FROM mconts as mc, mpres as mp WHERE mc.id = mp.codcon AND (mp.id ='"+mpres+"' OR mp.id IN "
                + "(SELECT id FROM mpres WHERE mpres_id='"+mpres+"'))";
       
        try {
            Statement st = conex.createStatement();
            ResultSet rst = st.executeQuery(select);
            while(rst.next())
            {
                
                if(cont>0)
                    temp = temp+",\n";
                if(cont==0)
                     temp = "REPLACE INTO mconts (id, nombre, direcc, telefo, fax, email, ingres, "
                             + "cedres, civres, ingins, cedins, "
                + "civins, repleg, cedrep, rif, nit, encabe, status, pagina) VALUES \n";
                temp = temp+"('"+rst.getString("mc.id") +"', '"+rst.getString("mc.nombre") +"', "
                        + "'"+rst.getString("direcc") +"', '"+rst.getString("telefo") +"', "
                        + "'"+rst.getString("fax") +"', '"+rst.getString("email") +"',"
                        + "'"+rst.getString("ingres") +"', '"+rst.getString("cedres") +"',"
                        + "'"+rst.getString("civres") +"', '"+rst.getString("ingins") +"',"
                        + "'"+rst.getString("cedins") +"', '"+rst.getString("civins") +"', "
                        + "'"+rst.getString("repleg") +"', '"+rst.getString("cedrep") +"',"
                        + "'"+rst.getString("rif") +"', '"+rst.getString("nit") +"',"
                        + " '"+rst.getString("encabe") +"',"
                        + "'"+rst.getString("status") +"','"+rst.getString("pagina") +"')";                
                cont++;
            }
            if(cont>0)
            temp=temp+";\n";
        } catch (SQLException ex) {
            Logger.getLogger(ThreadMconts.class.getName()).log(Level.SEVERE, null, ex);
        }
        respaldo.mconts = temp;
        respaldo.verificaHilos();
    }
}
