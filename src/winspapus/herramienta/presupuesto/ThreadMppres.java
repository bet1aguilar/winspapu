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
public class ThreadMppres extends Thread{
    private Connection conex;
    private String mpres;
    RespaldarPresupuesto respaldo;
    private String temp="";
    public ThreadMppres(Connection conex, String mpres, RespaldarPresupuesto respaldo)
    {
        this.conex=conex;
        this.mpres = mpres;
        this.respaldo=respaldo;
    }

    
    
    @Override
    public void run(){
        int cont=0;
        String select = "SELECT mpre_id, id, numero, IFNULL(numegrup,0) as numegrup, IFNULL(descri, '') as descri, "
                + "IFNULL(idband, '') as idband, IFNULL(porcgad,0) as porcgad, IFNULL(porcpre,0) as porcpre, "
                + "IFNULL(porcutil,0) as porcutil, precasu, precunit, IFNULL(rendimi,0) as rendimi, "
                + "IFNULL(unidad,'') as unidad, IFNULL(redondeo,'') as redondeo, IFNULL(status,'') as status, "
                + "IFNULL(cantidad,0) as cantidad, tipo, nropresupuesto, IFNULL(nrocuadro,0) as nrocuadro, "
                + "IFNULL(mppre_id,'') as mppre_id, IFNULL(tiporec, '') as tiporec, IFNULL(refere,'') as refere, "
                + "date_format(fechaini,'%Y-%m-%d') as fechaini, date_format(fechafin,'%Y-%m-%d') as fechafin, cron, rango, lapso, capitulo,"
                + "IF(tiponp='null','',IFNULL(tiponp,'')) as tiponp"
                + " FROM mppres WHERE mpre_id ='"+mpres+"' OR mpre_id IN "
                + "(SELECT id FROM mpres WHERE mpres_id='"+mpres+"')";
        
        try {
            Statement st = conex.createStatement();
            ResultSet rst = st.executeQuery(select);
            while(rst.next())
            {
                if(cont>0)
                    temp = temp+",\n";
                if(cont==0)
                    temp = "REPLACE INTO mppres (mpre_id, id, numero, numegrup, descri, idband, porcgad, porcpre, porcutil, "
                + "precasu, precunit, rendimi, unidad, redondeo, status, cantidad, tipo, nropresupuesto, nrocuadro, "
                + "mppre_id, tiporec, refere, fechaini, fechafin, cron, rango, lapso, capitulo, tiponp) VALUES \n";
                String fechaini = rst.getString("fechaini");
                String fechafin = rst.getString("fechafin");
                temp = temp+"('"+rst.getString("mpre_id") +"', '"+rst.getString("id") +"', "
                        + ""+rst.getString("numero") +", "+rst.getString("numegrup") +", "
                        + "'"+rst.getString("descri") +"', '"+rst.getString("idband") +"', "
                        + ""+rst.getString("porcgad") +", "+rst.getString("porcpre") +","
                        + ""+rst.getString("porcutil") +", "+rst.getString("precasu") +","
                        + ""+rst.getString("precunit") +", "+rst.getString("rendimi") +","
                        + "'"+rst.getString("unidad") +"', '"+rst.getString("redondeo") +"',"
                        + "'"+rst.getString("status") +"', "+rst.getString("cantidad") +","
                        + "'"+rst.getString("tipo") +"', "+rst.getString("nropresupuesto") +","
                        + ""+rst.getString("nrocuadro") +", '"+rst.getString("mppre_id") +"',"
                        + "'"+rst.getString("tiporec") +"', '"+rst.getString("refere") +"', "
                        + "IF("+fechaini+" IS NULL,NULL,'"+fechaini+"'),IF("+fechafin+" IS NULL,NULL,'"+fechafin +"'),"
                        + ""+rst.getString("cron") +","+rst.getString("rango") +","
                        + ""+rst.getString("lapso") +", "+rst.getString("capitulo") +","
                        + "'"+rst.getString("tiponp") +"')";
                cont++;
            }
            if(cont>0)
            temp=temp+";\n";
        } catch (SQLException ex) {
            Logger.getLogger(ThreadMppres.class.getName()).log(Level.SEVERE, null, ex);
        }
        respaldo.mppres = temp;
        respaldo.verificaHilos();
    }
}
