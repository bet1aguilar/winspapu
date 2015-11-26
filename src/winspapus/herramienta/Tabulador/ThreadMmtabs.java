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
public class ThreadMmtabs extends Thread{
    private Connection conex;
    private String mtabu;
    RespaldarTabulador respaldo;
    private String temp="";
    public ThreadMmtabs(Connection conex, String mtabu, RespaldarTabulador respaldo)
    {
        this.conex=conex;
        this.mtabu = mtabu;
        this.respaldo=respaldo;
    }

    
    
    @Override
    public void run(){
        int cont=0;
        String select = "SELECT mtabus_id, id, descri, IFNULL(desperdi,0) as desperdi, "        
                + "IFNULL(precio,0) as precio, IFNULL(unidad,'') as unidad, IFNULL(status,'') as status "
                + "FROM mmtabs WHERE mtabus_id ='"+mtabu+"'";
        
        try {
            Statement st = conex.createStatement();
            ResultSet rst = st.executeQuery(select);
            while(rst.next())
            {
                if(cont>0)
                    temp = temp+",\n";
                if(cont==0)
                    temp = "REPLACE INTO mmtabs (mtabus_id, id, descri, desperdi, precio, unidad, status) VALUES \n";
                temp = temp+"('"+rst.getString("mtabus_id") +"', '"+rst.getString("id") +"', "
                        + "'"+rst.getString("descri") +"', "+rst.getString("desperdi") +", "
                        + ""+rst.getString("precio") +",'"+rst.getString("unidad") +"',"
                        + "'"+rst.getString("status") +"')";                
                cont++;
            }
            if(cont>0)
            temp=temp+";\n";
        } catch (SQLException ex) {
            Logger.getLogger(ThreadMmtabs.class.getName()).log(Level.SEVERE, null, ex);
        }
        respaldo.mmtabs = temp;
        respaldo.verificaHilos();
    }
}
