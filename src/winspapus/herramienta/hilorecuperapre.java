/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package winspapus.herramienta;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import winspapus.Principal;
/**
 *
 * @author Spapu
 */
public class hilorecuperapre extends Thread{
    private Connection conexion;
    private java.sql.Connection conn,conn2;
    RecuperarPre recup;
    Principal prin;
 public static final int RET_OK = 1;
    hilorecuperapre(java.sql.Connection conexion, java.sql.Connection conn,java.sql.Connection conn2, RecuperarPre recup, Principal prin) {
        this.conexion=(Connection) conexion;
        this.conn= conn;
        this.conn2= conn2;
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
            String sql = "Select * from Presupuesto";
            ResultSet rst = carga.executeQuery(sql);
            String mpres,sTabu;
            sTabu="ND";
            rst.next();
            mpres = rst.getString("IDPresupuesto");
            //BORRAR ANTERIOR
            Statement borra1=(Statement) conexion.createStatement() ;
            sql="delete from mpres where id='" +mpres+"'";
            borra1.execute(sql);
            Statement borra2=(Statement) conexion.createStatement() ;
            sql="delete from mppres where mpre_id='" +mpres+"'";
            borra2.execute(sql);
            Statement borra3=(Statement) conexion.createStatement() ;
            sql="delete from deppres where mpre_id='" +mpres+"'";
            borra3.execute(sql);
            Statement borra4=(Statement) conexion.createStatement() ;
            sql="delete from mepres where mpre_id='" +mpres+"'";
            borra4.execute(sql);
            Statement borra5=(Statement) conexion.createStatement() ;
            sql="delete from dmpres where mpre_id='" +mpres+"'";
            borra5.execute(sql);            
            Statement borra6=(Statement) conexion.createStatement() ;
            sql="delete from mmpres where mpre_id='" +mpres+"'";
            borra6.execute(sql);
            Statement borra7=(Statement) conexion.createStatement() ;
            sql="delete from dmoppres where mpre_id='" +mpres+"'";
            borra7.execute(sql);
            Statement borra8=(Statement) conexion.createStatement() ;
            sql="delete from mmopres where mpre_id='" +mpres+"'";
            borra8.execute(sql);
            Statement borra9=(Statement) conexion.createStatement() ;
            sql="delete from mvalus where mpre_id='" +mpres+"'";
            borra9.execute(sql);
            Statement borra10=(Statement) conexion.createStatement() ;
            sql="delete from dvalus where mpre_id='" +mpres+"'";
            borra10.execute(sql);
            //            
            //RECUPERAR PRESUPUESTOS
            Statement car0= (Statement) conn.createStatement();
            Statement esc0 = (Statement) conexion.createStatement();
            sql = "Select * from presupuesto";
            ResultSet rst0 = car0.executeQuery(sql); 
            rst0.next();
            sql= "INSERT INTO mpres (id,nomabr,nombre,ubicac,fecini,fecfin,porgam,porcfi,porimp,poripa,porpre,poruti,codpro,codcon,status) VALUES "
                                                         + "('"+mpres+"',"
                                                         + "'"+rst0.getString("NombreAbrev")+"',"
                                                         + "'"+rst0.getString("Descripcion")+"',"
                                                         + "'"+rst0.getString("Ubicacion")+"',"
                                                         + "'"+rst0.getDate("FechaInicio")+"',"                                                         
                                                         + "'"+rst0.getDate("FechaFin")+"',"
                                                         + ""+rst0.getFloat("PAdminYGastos")+","
                                                         + ""+rst0.getFloat("PCostosFinancieros")+","
                                                         + ""+rst0.getFloat("PImpuestoGeneral")+","                                                         
                                                         + ""+rst0.getFloat("PImpuestoPartidas")+","                                                         
                                                         + ""+rst0.getFloat("PPrestaciones")+","                                                         
                                                         + ""+rst0.getFloat("PUtilidad")+","                                                         
                                                         + "'"+rst0.getString("IDPropietario")+"',"                                                         
                                                         + "'"+rst0.getString("IDContratista")+"',"                                                         
                                                         + "1)";
             esc0.execute(sql);
             //GUARDAR CONTRATISTA
            Statement car00= (Statement) conn2.createStatement();
            Statement esc00 = (Statement) conexion.createStatement();
            sql = "Select * from contratistas";
            ResultSet rst00 = car00.executeQuery(sql); 
            rst00.next();
            sql= "INSERT INTO mconts (id,nombre,direcc,telefo,fax,email,ingres,cedres,civres,ingins,repleg,cedrep,cedins,civins,encabe,rif,nit,status) VALUES "
                                                         + "('"+rst00.getString("IDContratista")+"',"
                                                         + "'"+rst00.getString("Nombre")+"',"
                                                         + "'"+rst00.getString("Direccion")+"',"
                                                         + "'"+rst00.getString("Telefono")+"',"                                                         
                                                         + "'"+rst00.getString("Fax")+"',"
                                                         + "'"+rst00.getString("EMail")+"',"
                                                         + "'"+rst00.getString("Ingeniero")+"',"
                                                         + "'"+rst00.getString("CIReside")+"',"                                                         
                                                         + "'"+rst00.getString("CIVReside")+"',"                                                         
                                                         + "'"+rst00.getString("Inspector")+"',"                                                                      
                                                         + "'"+rst00.getString("Representante")+"',"                    
                                                         + "'"+rst00.getString("CIRepre")+"',"                                                         
                                                         + "'"+rst00.getString("CIInspe")+"',"                    
                                                         + "'"+rst00.getString("CIVInspe")+"',"                                                                        
                                                         + "'"+rst00.getString("Encabe")+"',"                                                         
                                                         + "'"+rst00.getString("RIF")+"',"                                                                        
                                                         + "'"+rst00.getString("NIT")+"',"                                                         
                                                         + "'1')";
             esc00.execute(sql);
             //GUARDAR PROPIETARIO
            Statement car01= (Statement) conn2.createStatement();
            Statement esc01 = (Statement) conexion.createStatement();
            sql = "Select * from propietarios";
            ResultSet rst01 = car01.executeQuery(sql); 
            rst01.next();
            sql= "INSERT INTO mprops (id,nombre,telefo,pagina,status) VALUES "
                                                         + "('"+rst01.getString("IDPropietario")+"',"
                                                         + "'"+rst01.getString("Nombre")+"',"
                                                         + "'"+rst01.getString("Telefono")+"',"
                                                         + "'"+rst01.getString("URL")+"',"                                                                                                                  
                                                         + "'1')";
             esc01.execute(sql);
             // PARTIDAS
            String grupo,descri,tipop,status;
            Statement car_0= (Statement) conn.createStatement();
            Statement esc_0 = (Statement) conexion.createStatement();
            sql = "Select * from Partidas";
            
            ResultSet rst_0 = car_0.executeQuery(sql); 
             while(rst_0.next()){
                 grupo=rst_0.getString("NroPartida");
                 descri=rst_0.getString("Descripcion");
                 status=rst_0.getString("Status");
                 if(status.equals("0")){
                     tipop="Org";  
                 }
                 else 
                     if(status.equals("1")){
                        tipop="NP";
                     }
                     else{
                        tipop="VP";                        
                  
                 }
                 descri=descri.replace("'", "º");
                 sql= "INSERT INTO mppres (mpre_id,id,numero,numegrup,descri,idband,porcgad,porcpre,porcutil," 
                 +"precasu,precunit,rendimi,unidad,redondeo,status,cantidad,tipo,nropresupuesto,nrocuadro,mppre_id,"
                 +"tiporec,refere,fechaini,fechafin,cron,rango,lapso,ctabs_id) "
                 +"VALUES ('" +mpres+ "','"+rst_0.getString("CodCovenin")+"',"
                                              + "'"+grupo+"',"
                                              + "'"+grupo+"',"
                                              + "'"+descri+"',"                                              
                                              + "'"+rst_0.getString("IDBancoDatos")+"',"
                                              + "'"+rst_0.getFloat("PAdminYGastos")+"',"
                                              + "'"+rst_0.getFloat("PPrestaciones")+"',"                                                                                            
                                              + "'"+rst_0.getFloat("PUtilidad")+"',"
                                              + "'"+rst_0.getFloat("PrecioAsumido")+"',"
                                              + "'"+rst_0.getFloat("PrecioUnitario")+"',"
                                              + "'"+rst_0.getFloat("Rendimiento")+"',"
                                              + "'"+rst_0.getString("Unidad")+"',"
                                              + +rst_0.getInt("Redondeo")+","
                                              + "'1',"                                              
                                              +rst_0.getFloat("Cantidad")+","
                                              + "'" + tipop + "',0,0,null,null,null,null,null,0,0,0,0)";                   
                   esc_0.execute(sql); 

            }             
            
             //MANO DE OBRA
            Statement car1= (Statement) conn.createStatement();
            Statement esc1 = (Statement) conexion.createStatement();
            sql = "Select * from manoobra";
            ResultSet rst1 = car1.executeQuery(sql); 
             while(rst1.next()){

                 sql= "INSERT INTO mmopres VALUES ('"+mpres+"',"
                                              + "'"+rst1.getString("IDManoObra")+"',"
                                              + "'"+rst1.getString("Descripcion")+"',"
                                              + rst1.getFloat("Bono")+","                                                         
                                              + rst1.getFloat("Salario")+","                                                         
                                              + rst1.getFloat("Subsidio")+","                                                                                                 
                                              + "'1')";
                 
                   esc1.execute(sql); 

            }            
           //MANO DE OBRA POR PARTIDAS
            Statement carga5= (Statement) conn.createStatement();
            Statement escribir5 = (Statement) conexion.createStatement();
            sql = "Select mo.IDPartidas,mo.IDManoObra,mo.Cantidad,mo.Numero,mo.Nuevo,m.Bono,m.Salario,m.Subsidio from manoobrapartida mo, manoobra m where m.idmanoobra=mo.idmanoobra ";
            ResultSet rst5 = carga5.executeQuery(sql); 
             while(rst5.next()){
                Statement bd1= (Statement) conexion.createStatement(); 
                String sPart=rst5.getString("IDPartidas");
                String sDeta=rst5.getString("IDManoObra");
                sql = "Select * from dmoppres where mpre_id='"+mpres+"' and mmopre_id='" + sDeta + "' and mppre_id='"+ sPart +"'";
                ResultSet rstbd = bd1.executeQuery(sql); 
                
                rstbd.last();
                int regis = rstbd.getRow();
                System.out.println(regis);                   
                if (regis==0){
                    
                 sql= "INSERT INTO dmoppres VALUES ('"+mpres+"',"                                              
                                              + "'"+sDeta+"',"
                                              + "'"+sPart+"',"
                                              + rst5.getInt("numero")+","
                                              + rst5.getFloat("cantidad")+","                                                         
                                              + rst5.getFloat("bono")+","                                                          
                                              + rst5.getFloat("salario")+","                                                          
                                              +  rst5.getFloat("subsidio")+","                                                          
                                              + "'1')";
                   System.out.println(sql);                   
                   escribir5.execute(sql); 
                }
                

            }   
               //MATERIALES
            Statement car2= (Statement) conn.createStatement();
            Statement esc2 = (Statement) conexion.createStatement();
            sql = "Select * from materiales";
            ResultSet rst2 = car2.executeQuery(sql); 
             while(rst2.next()){
                 descri=rst2.getString("Descripcion");
                 descri=descri.replace("'", "º");
                 sql= "INSERT INTO mmpres VALUES ('"+mpres+"',"
                                              + "'"+rst2.getString("IDMateriales")+"',"
                                              + "'"+descri+"',"
                                              + rst2.getFloat("Desperdicio")+","                                                         
                                              + rst2.getFloat("Precio")+","                                                          
                                              + "'"+rst2.getString("Unidad")+"',"
                                              + "'1')";
                  esc2.execute(sql); 

            }
            //MATERIALES POR PARTIDAS
            Statement car3= (Statement) conn.createStatement();
            Statement esc3 = (Statement) conexion.createStatement();
            sql = "Select dm.IDpartidas,dm.IDMateriales,dm.Cantidad,dm.Numero,dm.Nuevo,m.Precio from materialespartida dm, materiales m where dm.idmateriales=m.idmateriales" ;
            ResultSet rst3 = car3.executeQuery(sql); 
             while(rst3.next()){              
                Statement bd2= (Statement) conexion.createStatement(); 
                String sPart=rst3.getString("IDPartidas");
                String sDeta=rst3.getString("IDMateriales");
                sql = "Select * from dmpres where mpre_id='"+mpres+"' and mmpre_id='" + sDeta + "' and mppre_id='"+ sPart +"'";
                ResultSet rstbd = bd2.executeQuery(sql); 
                
                rstbd.last();
                int regis = rstbd.getRow();
                System.out.println(regis);                   
                if (regis==0){
                 sql= "INSERT INTO dmpres VALUES ('"+mpres+"',"
                                              + "'"+sPart+"',"
                                              + "'"+sDeta+"',"
                                              + rst3.getInt("Numero")+","                                                         
                                              + rst3.getFloat("Cantidad")+","
                                              + rst3.getFloat("precio")+","                                                          
                                              + "'1')";
                   esc3.execute(sql); 
                }    

            }  
            //EQUIPOS
            Statement car4= (Statement) conn.createStatement();
            Statement esc4 = (Statement) conexion.createStatement();
            sql = "Select * from equipos";
            ResultSet rst4 = car4.executeQuery(sql); 
             while(rst4.next()){

                 sql= "INSERT INTO mepres VALUES ('"+mpres+"',"
                                              + "'"+rst4.getString("IDEquipos")+"',"
                                              + "'"+rst4.getString("Descripcion")+"',"
                                              + rst4.getFloat("Depreciacion")+","                                                         
                                              + rst4.getFloat("Precio")+","                                                          
                                              + "1)";                 
                  esc4.execute(sql); 

            }
            //EQUIPOS POR PARTIDAS
            Statement car5= (Statement) conn.createStatement();
            Statement esc5 = (Statement) conexion.createStatement();
            sql = "Select de.IDPartidas,de.IDEquipos,de.Cantidad,de.Numero,de.Nuevo,e.Precio from equipospartida de, equipos e where de.idequipos=e.idequipos";
            ResultSet rst6 = car5.executeQuery(sql); 
             while(rst6.next()){
                Statement bd3= (Statement) conexion.createStatement(); 
                String sPart=rst6.getString("IDPartidas");
                String sDeta=rst6.getString("IDEquipos");
                sql = "Select * from deppres where mpre_id='"+mpres+"' and mepre_id='" + sDeta + "' and mppre_id='"+ sPart +"'";
                ResultSet rstbd = bd3.executeQuery(sql); 
                
                rstbd.last();
                int regis = rstbd.getRow();
                System.out.println(regis);                   
                if (regis==0){   
                 sql= "INSERT INTO deppres VALUES ('"+mpres+"',"
                                              + "'"+sPart+"',"
                                              + "'"+sDeta+"',"
                                              + rst6.getString("Numero")+","
                                              + rst6.getFloat("cantidad")+","                                                         
                                              + rst6.getFloat("precio")+","                                                          
                                              + "1)";                   
                   esc5.execute(sql); 
                }
            } 
             //VALUACIONES
            Statement car10= (Statement) conn.createStatement();
            Statement esc10 = (Statement) conexion.createStatement();
            sql = "Select * from valuaciones";
            ResultSet rst10 = car10.executeQuery(sql); 
             while(rst10.next()){
                 String fecha1,fecha2;                                  
                 SimpleDateFormat formatofecha=new SimpleDateFormat("yyyy-MM-dd");
                 fecha1=formatofecha.format(rst10.getDate("desde"));    
                 fecha2=formatofecha.format(rst10.getDate("hasta"));                                      
                 sql= "INSERT INTO mvalus (id,desde,hasta,status,mpre_id,tipo,lapso) "
                       +"VALUES ('"+rst10.getString("numero")+"',"
                                              + "'"+fecha1+"',"
                                              + "'"+fecha2+"','1',"
                                              + "'"+mpres+"','Parcial',1)" ;                                 
                 esc10.execute(sql); 

            }
             //DETALLE DE VALUACIONES
            Statement car11= (Statement) conn.createStatement();
            Statement esc11 = (Statement) conexion.createStatement();
            sql = "Select * from detalle";
            ResultSet rst11 = car11.executeQuery(sql); 
             while(rst11.next()){                 
                 sql= "INSERT INTO dvalus (mpre_id,mvalu_id,mppre_id,cantidad,precio,numepart,status,aumento) "
                       +"VALUES ('"+ mpres + "', "+rst11.getString("numero")+","
                                              + "'"+rst11.getString("Partida")+"',"
                                              + "'"+rst11.getFloat("cantidad")+"',"
                                              + "'"+rst11.getFloat("precio")+"',"
                                              + "'"+rst11.getInt("nropartida")+"','1',0)";                                              
                  System.out.println(sql);   
                  esc11.execute(sql); 

            } 
     JOptionPane.showMessageDialog(recup, "Recuperación de datos del Presupuesto Completado..");
     recup.doClose(RET_OK);
            
        } catch (SQLException ex) {
            Logger.getLogger(RecuperarTab.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
