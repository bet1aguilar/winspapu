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
/**
 *
 * @author Spapu
 */
public class hilopartida1 extends Thread{
    private Connection conexion;
    private java.sql.Connection conn;
    RecuperarTab recup;
 public static final int RET_OK = 1;
    hilopartida1(java.sql.Connection conexion, java.sql.Connection conn, RecuperarTab recup) {
        this.conexion=(Connection) conexion;
        this.conn= conn;
        this.recup=recup;
    }
    public void conectar(){
        
    }
    @Override
    public void run() {
       
         try {
           
            Statement carga= (Statement) conn.createStatement();
            Statement escribir = (Statement) conexion.createStatement();
            String sql = "Select * from Config";
            ResultSet rst = carga.executeQuery(sql);
            String mtabu;
            rst.next();
            mtabu = rst.getString("IDConfig");
            // PARTIDAS
            String grupo,descri;
            Statement car0= (Statement) conn.createStatement();
            Statement esc0 = (Statement) conexion.createStatement();
            sql = "Select * from Partidas";
            
            ResultSet rst0 = car0.executeQuery(sql); 
             while(rst0.next()){
                 grupo=rst0.getString("NroPartida");
             
                 descri=rst0.getString("Descripcion");
                 descri=descri.replace("'", "º");
                 sql= "INSERT INTO mptabs VALUES ('"+rst0.getString("CodCovenin")+"',"
                                              + "'"+grupo+"',"
                                              + "'"+grupo+"',"
                                              + "'"+descri+"',"
                                              + "'"+rst0.getString("Referencia")+"',"
                                              + "'"+rst0.getString("IDBancoDatos")+"',"
                                              + "'"+rst0.getFloat("PAdminYGastos")+"',"
                                              + "'"+rst0.getFloat("PPrestaciones")+"',"                                              
                                              + "'"+rst0.getFloat("PUtilidad")+"',"
                                              + "'"+rst0.getFloat("PrecioAsumido")+"',"
                                              + "'"+rst0.getFloat("PrecioUnitario")+"',"                                              
                                              + "'"+rst0.getFloat("Rendimiento")+"',"
                                              + "'"+rst0.getString("Unidad")+"',"
                                              + +rst0.getInt("Redondeo")+","
                                              + "'1',"                                              
                                              + "'"+mtabu+"',"
                                              + +rst0.getFloat("Cantidad")+","
                                              + "0,'')";                   
                   esc0.execute(sql); 

            }
                        
    
            
        } catch (SQLException ex) {
            Logger.getLogger(RecuperarTab.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
       
}
