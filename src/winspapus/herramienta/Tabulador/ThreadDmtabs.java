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
public class ThreadDmtabs extends Thread{
    private Connection conex;
    private String mtabu;
    RespaldarTabulador respaldo;
    private String temp="";
    public ThreadDmtabs(Connection conex, String mtabu, RespaldarTabulador respaldo)
    {
        this.conex=conex;
        this.mtabu = mtabu;
        this.respaldo=respaldo;
    }

    
    
    @Override
    public void run(){
        int cont=0;
        String select = "SELECT mtabus_id, mptab_id, mmtab_id, numepart, IFNULL(cantidad,0) as cantidad, "        
                + "IFNULL(precio,0) as precio, IFNULL(status,'') as status "
                + "FROM dmtabs WHERE mtabus_id ='"+mtabu+"'";
        
        try {
            Statement st = conex.createStatement();
            ResultSet rst = st.executeQuery(select);
            while(rst.next())
            {
                if(cont>0)
                    temp = temp+",\n";
                if(cont==0)
                    temp = "REPLACE INTO dmtabs (mtabus_id, mptab_id, mmtab_id, numepart, cantidad, precio, status) VALUES \n";
                temp = temp+"('"+rst.getString("mtabus_id") +"', '"+rst.getString("mptab_id") +"', "
                        + "'"+rst.getString("mmtab_id") +"', "+rst.getString("numepart") +", "
                        + ""+rst.getString("cantidad") +","+rst.getString("precio") +","
                        + "'"+rst.getString("status") +"')";                
                cont++;
            }
            if(cont>0)
            temp=temp+";\n";
        } catch (SQLException ex) {
            Logger.getLogger(ThreadDmtabs.class.getName()).log(Level.SEVERE, null, ex);
        }
        respaldo.dmtabs = temp;
        respaldo.verificaHilos();
    }
}
