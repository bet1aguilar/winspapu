/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package winspapus.herramienta.Tabulador;

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
public class ThreadMptabs extends Thread{
    private Connection conex;
    private String mtabu;
    RespaldarTabulador respaldo;
    private String temp="";
    public ThreadMptabs(Connection conex, String mtabu, RespaldarTabulador respaldo)
    {
        this.conex=conex;
        this.mtabu = mtabu;
        this.respaldo=respaldo;
    }

    
    
    @Override
    public void run(){
        int cont=0;
        String select = "SELECT codicove, numero, numegrup, IFNULL(descri,'') as descri, IFNULL(refere,0) as refere, mbdat_id, "        
                + "IFNULL(porcgad,0) as porcgad, IFNULL(porcpre,0) as porcpre, IFNULL(porcutil,0) as porcutil, "
                + "IFNULL(precasu,0) as precasu, IFNULL(precunit,0) as precunit, IFNULL(rendimi,0) as rendimi, "
                + "IFNULL(unidad,'') as unidad, redondeo, mtabus_id, IFNULL(cantidad,0) as cantidad, IFNULL(capitulo,0) as capitulo,"
                + "IFNULL(numenletra,'') as numenletra, "
                + "IFNULL(status,'') as status "
                + "FROM mptabs WHERE mtabus_id ='"+mtabu+"'";
        
        try {
            Statement st = conex.createStatement();
            ResultSet rst = st.executeQuery(select);
            while(rst.next())
            {
                if(cont>0)
                    temp = temp+",\n";
                if(cont==0)
                    temp = "REPLACE INTO mptabs (codicove, numero, numegrup, descri, refere, mbdat_id, porcgad, porcpre, porcutil, "
                            + "precasu, precunit, rendimi, unidad, redondeo, mtabus_id, cantidad, capitulo, numenletra, status) VALUES \n";
                temp = temp+"('"+rst.getString("mtabus_id") +"', "+rst.getString("numero") +", "
                        + ""+rst.getString("numegrup") +", '"+rst.getString("descri").replace(";",",") +"', "
                        + "'"+rst.getString("refere") +"',"+rst.getString("mbdat_id") +","
                        + ""+rst.getString("porcgad")+", "+rst.getString("porcpre")+","
                        + ""+rst.getString("porcutil")+", "+rst.getString("precasu")+","
                        + ""+rst.getString("precunit")+", "+rst.getString("rendimi")+", "
                        + "'"+rst.getString("unidad")+"', '"+rst.getString("redondeo") +"', "
                        + "'"+rst.getString("mtabus_id") +"', "+rst.getString("cantidad")+", "
                        + ""+rst.getString("capitulo")+", "
                        + "'"+rst.getString("numenletra")+"', '"+rst.getString("status")+"')";                
                cont++;
            }
            if(cont>0)
            temp=temp+";\n";
        } catch (SQLException ex) {
            Logger.getLogger(ThreadMptabs.class.getName()).log(Level.SEVERE, null, ex);
        }
        respaldo.mptabs = temp;
        respaldo.verificaHilos();
    }
}
