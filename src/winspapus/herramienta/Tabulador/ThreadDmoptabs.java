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
public class ThreadDmoptabs extends Thread{
    private Connection conex;
    private String mtabu;
    RespaldarTabulador respaldo;
    private String temp="";
    public ThreadDmoptabs(Connection conex, String mtabu, RespaldarTabulador respaldo)
    {
        this.conex=conex;
        this.mtabu = mtabu;
        this.respaldo=respaldo;
    }

    
    
    @Override
    public void run(){
        int cont=0;
        String select = "SELECT mtabus_id, mmotab_id, numero, IFNULL(cantidad,0) as cantidad, "
                + "IFNULL(bono,0) as bono, IFNULL(salario,0) as salario, "
                + "IFNULL(subsidi,0) as subsidi, IFNULL(status,'') as status, IFNULL(mptab_id,'') as mptab_id "
                + "FROM dmoptabs WHERE mtabus_id ='"+mtabu+"'";
        
        try {
            Statement st = conex.createStatement();
            ResultSet rst = st.executeQuery(select);
            while(rst.next())
            {
                if(cont>0)
                    temp = temp+",\n";
                if(cont==0)
                    temp = "REPLACE INTO dmoptabs (mtabus_id, mmotab_id, numero, cantidad, bono, salario, subsidi, status,"
                            + "mptab_id) VALUES \n";
                temp = temp+"('"+rst.getString("mtabus_id") +"', '"+rst.getString("mmotab_id") +"', "
                        + ""+rst.getString("numero") +", "+rst.getString("cantidad") +", "
                        + ""+rst.getString("bono") +","+rst.getString("salario") +","
                        + ""+rst.getString("subsidi") +", '"+rst.getString("status") +"',"
                        + "'"+rst.getString("mptab_id") +"')";                
                cont++;
            }
            if(cont>0)
            temp=temp+";\n";
        } catch (SQLException ex) {
            Logger.getLogger(ThreadDmoptabs.class.getName()).log(Level.SEVERE, null, ex);
        }
        respaldo.dmoptabs = temp;
        respaldo.verificaHilos();
    }
}
