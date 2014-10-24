/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package winspapus;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

/**
 *
 * @author Betmart
 */
public class reordena extends Thread{
    String [] coldata;
    int i=0;
    int [] numeros;
    String cadena;
    Connection conexion;
    JProgressBar barrita;
    JLabel ordena;
    Principal obj;
    public reordena(Connection conex, String[] obj, int [] num, String mtabu, Principal spapu){
        coldata= obj;
        numeros = num;
        cadena = mtabu;
        conexion = conex;
        this.obj = spapu;
    }
    @Override
    public void run(){
        while(i<coldata.length){
         
            
                System.out.println("i="+i);
                Statement st = null;
            try {
                st = (Statement) conexion.createStatement();
            } catch (SQLException ex) {
                Logger.getLogger(reordena.class.getName()).log(Level.SEVERE, null, ex);
            }
                String sql = "UPDATE Mptabs set numegrup="+(i+1)+" WHERE numero="+numeros[i]+" AND mtabus_id='"+cadena+"'";
            try {
                st.execute(sql);
            } catch (SQLException ex) {
                Logger.getLogger(reordena.class.getName()).log(Level.SEVERE, null, ex);
            }
            i++;
            }
        JOptionPane.showMessageDialog(null, "Se han reordenado las partidas");
      
        obj.partida();
    }
}
