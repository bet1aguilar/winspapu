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
public class ThreadDeptabs extends Thread{
    private Connection conex;
    private String mtabu;
    RespaldarTabulador respaldo;
    private String temp="";
    public ThreadDeptabs(Connection conex, String mtabu, RespaldarTabulador respaldo)
    {
        this.conex=conex;
        this.mtabu = mtabu;
        this.respaldo=respaldo;
    }

    
    
    @Override
    public void run(){
        int cont=0;
        String select = "SELECT mtabus_id, mptab_id, metab_id, numero, IFNULL(cantidad,0) as cantidad, "
                + "IFNULL(precio,0) as precio, IFNULL(status,'') as status FROM deptabs WHERE mtabus_id ='"+mtabu+"'";
        
        try {
            Statement st = conex.createStatement();
            ResultSet rst = st.executeQuery(select);
            while(rst.next())
            {
                if(cont>0)
                    temp = temp+",\n";
                if(cont==0)
                    temp = "REPLACE INTO deptabs (mtabus_id, mptab_id, metab_id, numero, cantidad) VALUES \n";
                temp = temp+"('"+rst.getString("mtabus_id") +"', '"+rst.getString("mptab_id") +"', "
                        + "'"+rst.getString("metab_id") +"', "+rst.getString("numero") +", "
                        + ""+rst.getString("cantidad") +")";                
                cont++;
            }
            if(cont>0)
            temp=temp+";\n";
        } catch (SQLException ex) {
            Logger.getLogger(ThreadDeptabs.class.getName()).log(Level.SEVERE, null, ex);
        }
        respaldo.deptabs = temp;
        respaldo.verificaHilos();
    }
}
