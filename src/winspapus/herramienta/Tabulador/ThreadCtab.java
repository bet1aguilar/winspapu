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
public class ThreadCtab extends Thread{
    private Connection conex;
    private String mtabu;
    RespaldarTabulador respaldo;
    private String temp="";
    public ThreadCtab(Connection conex, String mtabu, RespaldarTabulador respaldo)
    {
        this.conex=conex;
        this.mtabu = mtabu;
        this.respaldo=respaldo;
    }

    
    
    @Override
    public void run(){
        int cont=0;
        String select = "SELECT id, descri, IFNULL(codigo,'') as codigo, IFNULL(ctabs_id,0) as ctabs_id,"
                + "mtabus_id FROM ctabs WHERE mtabus_id ='"+mtabu+"'";
        
        try {
            Statement st = conex.createStatement();
            ResultSet rst = st.executeQuery(select);
            while(rst.next())
            {
                if(cont>0)
                    temp = temp+",\n";
                if(cont==0)
                    temp = "REPLACE INTO ctabs (id, descri,codigo, ctabs_id, mtabus_id) VALUES \n";
                temp = temp+"('"+rst.getString("id") +"', '"+rst.getString("descri") +"', "
                        + "'"+rst.getString("codigo") +"', "+rst.getString("ctabs_id") +", "
                        + "'"+rst.getString("mtabus_id") +"')";                
                cont++;
            }
            if(cont>0)
            temp=temp+";\n";
        } catch (SQLException ex) {
            Logger.getLogger(ThreadCtab.class.getName()).log(Level.SEVERE, null, ex);
        }
        respaldo.ctabs = temp;
        respaldo.verificaHilos();
    }
}
