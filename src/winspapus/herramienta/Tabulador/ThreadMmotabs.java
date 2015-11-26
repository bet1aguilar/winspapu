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
public class ThreadMmotabs extends Thread{
    private Connection conex;
    private String mtabu;
    RespaldarTabulador respaldo;
    private String temp="";
    public ThreadMmotabs(Connection conex, String mtabu, RespaldarTabulador respaldo)
    {
        this.conex=conex;
        this.mtabu = mtabu;
        this.respaldo=respaldo;
    }

    
    
    @Override
    public void run(){
        int cont=0;
        String select = "SELECT mtabus_id, id, descri, IFNULL(bono,0) as bono, "        
                + "IFNULL(salario,0) as salario, IFNULL(subsid,0) as subsid, IFNULL(status,'') as status "
                + "FROM mmotabs WHERE mtabus_id ='"+mtabu+"'";
        
        try {
            Statement st = conex.createStatement();
            ResultSet rst = st.executeQuery(select);
            while(rst.next())
            {
                if(cont>0)
                    temp = temp+",\n";
                if(cont==0)
                    temp = "REPLACE INTO mmotabs (mtabus_id, id, descri, bono, salario, subsid, status) VALUES \n";
                temp = temp+"('"+rst.getString("mtabus_id") +"', '"+rst.getString("id") +"', "
                        + "'"+rst.getString("descri") +"', "+rst.getString("bono") +", "
                        + ""+rst.getString("salario") +","+rst.getString("subsid") +","
                        + "'"+rst.getString("status") +"')";                
                cont++;
            }
            if(cont>0)
            temp=temp+";\n";
        } catch (SQLException ex) {
            Logger.getLogger(ThreadMmotabs.class.getName()).log(Level.SEVERE, null, ex);
        }
        respaldo.mmotabs = temp;
        respaldo.verificaHilos();
    }
}
