/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package presupuestos;
import java.sql.Connection;
import com.mysql.jdbc.Statement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Betmart
 */
public class reordena1 extends Thread{
    String [] coldata;
    int i=0;
    int [] numeros;
    String cadena;
    private Connection conexion;
   int muestra;
    Presupuesto obj;
    public reordena1(Connection conex, String[] coldata, int[] numeros, String id, Presupuesto pres) {
        this.coldata = coldata;
        this.numeros = numeros;
        cadena = id;
        muestra=0;
        conexion = conex;
        this.obj = pres;
    }
    public reordena1(Connection conex, String[] coldata, int[] numeros, String id, Presupuesto pres,int muestra) {
        this.coldata = coldata;
        this.numeros = numeros;
        cadena = id;
        conexion = conex;
        muestra=1;
        this.obj = pres;
    }
    @Override
    public void run(){
        while(i<coldata.length){
         
            
               
                Statement st = null;
            try {
                st = (Statement) conexion.createStatement();
            } catch (SQLException ex) {
                Logger.getLogger(reordena1.class.getName()).log(Level.SEVERE, null, ex);
            }
           
                String sql = "UPDATE Mppres set numegrup="+(i+1)+" "
                        + "WHERE numero="+numeros[i]+" AND (mpre_id='"+cadena+"' "
                        + "OR mpre_id IN (SELECT id FROM mpres WHERE "
                        + "mpres_id='"+cadena+"' GROUP BY id))";
            try {
                st.execute(sql);
            } catch (SQLException ex) {
                Logger.getLogger(reordena1.class.getName()).log(Level.SEVERE, null, ex);
            }
            i++;
            }
        
        try {
            obj.buscapartida1();
        } catch (SQLException ex) {
            Logger.getLogger(reordena1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    }

