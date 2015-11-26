/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package winspapus.herramienta;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import winspapus.Principal;
/**
 *
 * @author Spapu
 */
public class hilorecuperar extends Thread{
    private Connection conexion;
    private java.sql.Connection conn;
    RecuperarTab recup;
    Principal prin;
 public static final int RET_OK = 1;
    hilorecuperar(java.sql.Connection conexion, java.sql.Connection conn, RecuperarTab recup, Principal prin) {
        this.conexion=(Connection) conexion;
        this.conn= conn;
        this.recup=recup;
        this.prin=prin;
    }
    public void conectar(){
        
    }
    @Override
    public void run() {
         try {
             
            Statement carga= (Statement) conn.createStatement();
            Statement escribir = (Statement) conexion.createStatement();
            String sql = "Select * from Config";
                        System.out.println(conn.getMetaData().getURL());
            ResultSet rst = carga.executeQuery(sql);

            String mtabu;
            rst.next();
            mtabu = rst.getString("IDConfig");
           
            //            
            //RECUPERAR TABULADORES
            sql= "REPLACE INTO mtabus "
                    + "(id, descri, vigencia, padyga, pcosfin, pimpue,pprest,putild,status,seleccionado) "
                    + "VALUES "
                    + "('"+mtabu+"',"
                                                         + "'"+rst.getString("Descripcion")+"',"
                                                         + "'"+rst.getDate("Vigencia")+"',"
                                                         + ""+rst.getFloat("PAdminYGastos")+","
                                                         + ""+rst.getFloat("PCostosFinancieros")+","
                                                         + ""+rst.getFloat("PImpuesto")+","                                                         
                                                         + ""+rst.getFloat("PPrestaciones")+","                                                         
                                                         + ""+rst.getFloat("PUtilidad")+","                                                         
                                                         + "1,0)";
             escribir.execute(sql);
             
            // BANCO DE DATOS            
            Statement carga1= (Statement) conn.createStatement();
            Statement escribir1 = (Statement) conexion.createStatement();
            sql = "Select * from bancodatos";
            ResultSet rst1 = carga1.executeQuery(sql); 
             while(rst1.next()){
                Statement bd1= (Statement) conexion.createStatement(); 
                String strBanco=rst1.getString("IDBancoDatos");
                sql = "Select * from mbdats where id='"+strBanco +"'";
                ResultSet rstbd = bd1.executeQuery(sql); 
                rstbd.last();
                int regis = rstbd.getRow();
                if (regis==0)
                {
                   sql= "REPLACE INTO mbdats VALUES ('"+strBanco+"',"
                                              + "'"+rst1.getString("Descripcion")+"',"
                                              + rst1.getInt("Numero")+","                                                         
                                              + "1)";
                   escribir1.execute(sql); 
                }    
            }
                
          
                
             
    // recup.doClose(RET_OK);
     prin.buscatab();
     
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(recup, "Ocurrió un error al cargar el tabulador "+ex.getMessage());
            recup.doClose(RET_OK);
            Logger.getLogger(RecuperarTab.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
