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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import presupuestos.Presupuesto;
import presupuestos.tabpresupuesto;
import winspapus.Principal;
/**
 *
 * @author Spapu
 */
public class hilorecuperapre extends Thread{
    private Connection conexion;
    JProgressBar mat, eq, mano, detmat, detmano, deteq, presu,partida;
    private java.sql.Connection conn,conn2;
    RecuperarPre recup;
    Principal prin;
    Presupuesto p;
    String prop, cont;
    porcentajesPres porc;
 public static final int RET_OK = 1;
    hilorecuperapre(java.sql.Connection conexion, java.sql.Connection conn,java.sql.Connection conn2, RecuperarPre recup, Principal prin, porcentajesPres pres, Presupuesto p) {
        this.conexion=(Connection) conexion;
        this.conn= conn;
        this.porc=pres;
        mat = porc.getMateriales();
        eq = porc.getEquipos();
        mano = porc.getMano();
        detmat = porc.getDetMat();
        detmano = porc.getDetMano();
        deteq = porc.getDetEq();
        presu = porc.getPres();
        partida = porc.getPartida();
        this.conn2= conn2;
        this.recup=recup;
        this.p = p;
        this.prin=prin;
    }

     private float getdeteq(int numero){
       float cant=0;
        String sql = "Select COUNT(*)"
                   + "from EquiposPartida de, equipos e where de.idequipos=e.idequipos AND de.Numero="+numero+"";
        try {
            Statement st = conn.createStatement();
            ResultSet rst = st.executeQuery(sql);
            while(rst.next()){
                cant=rst.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(hilorecuperapre.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cant;
    }
    private float getmat(){
        float cant=0;
        String sql = "Select COUNT(*) from Materiales";
        try {
            Statement st = conn.createStatement();
            ResultSet rst = st.executeQuery(sql);
            while(rst.next()){
                cant=rst.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(hilorecuperapre.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cant;
    }
    private float getdetmano(int numero){
        float cant=0;
        String sql = "Select COUNT(*)"
                    + " from ManoObraPartida mo, manoobra m where "
                    + "m.idmanoobra=mo.idmanoobra AND mo.Numero="+numero+"";
        try {
            Statement st = conn.createStatement();
            ResultSet rst = st.executeQuery(sql);
            while(rst.next()){
                cant=rst.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(hilorecuperapre.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cant;
    }
     private float getdetmat(int numero){
        float cant=0;
      
        String sql = "Select COUNT(*) "
                    + "from MaterialesPartida"
                    + " dm, materiales m where dm.idmateriales=m.idmateriales AND dm.Numero="+numero+"" ;
        try {
            Statement st = conn.createStatement();
            ResultSet rst = st.executeQuery(sql);
            while(rst.next()){
                cant=rst.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(hilorecuperapre.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cant;
    }
    private float getmano(){
       float cant=0;
        String sql = "Select COUNT(*) from ManoObra";
        try {
            Statement st = conn.createStatement();
            ResultSet rst = st.executeQuery(sql);
            while(rst.next()){
                cant=rst.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(hilorecuperapre.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cant;
    }
     private float getequipo(){
        float cant=0;
        String sql = "Select COUNT(*) from Equipos";
        try {
            Statement st = conn.createStatement();
            ResultSet rst = st.executeQuery(sql);
            while(rst.next()){
                cant=rst.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(hilorecuperapre.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cant;
    }
     private float getpartida(){
        float cant=0;
        String sql = "Select COUNT(*) from Partidas";
        try {
            Statement st = conn.createStatement();
            ResultSet rst = st.executeQuery(sql);
            while(rst.next()){
                cant=rst.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(hilorecuperapre.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cant;
    }
     public void borrarpres (String id){
         String select = "SELECT COUNT(*) FROM mpres WHERE id='"+id+"'";
         int cont=0;
        try {
            Statement st = conexion.createStatement();
            ResultSet rst = st.executeQuery(select);
            while(rst.next())
            {
             cont = rst.getInt(1);   
            }
        } catch (SQLException ex) {
            Logger.getLogger(hilorecuperapre.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(cont>0)
        {
            int op = JOptionPane.showConfirmDialog(null, "¿Desea Eliminar este Presupuesto?", "Borrar Presupuesto", JOptionPane.YES_NO_OPTION);
    if(op==JOptionPane.YES_OPTION){
            try {
                
                String borrarpartidas = "DELETE FROM mppres WHERE mpre_id='"+id+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+id+"')";
                String borrarmats = "DELETE FROM deppres WHERE mpre_id='"+id+"' OR mpre_id IN (SELECT id FROM mpres WHERE  mpres_id='"+id+"')";
                String borrarequips = "DELETE FROM dmpres WHERE mpre_id='"+id+"' OR mpre_id IN (SELECT id FROM mpres WHERE  mpres_id='"+id+"')";
                String borrarmates = "DELETE FROM mmpres WHERE mpre_id='"+id+"' OR mpre_id IN (SELECT id FROM mpres WHERE  mpres_id='"+id+"')";
                String borrarequipos = "DELETE FROM mepres WHERE mpre_id='"+id+"' OR mpre_id IN (SELECT id FROM mpres WHERE  mpres_id='"+id+"')";
                String borrarmanos = "DELETE FROM mmopres WHERE mpre_id='"+id+"' OR mpre_id IN (SELECT id FROM mpres WHERE  mpres_id='"+id+"')";
                String borrardmanos = "DELETE FROM dmoppres WHERE mpre_id='"+id+"' OR mpre_id IN (SELECT id FROM mpres WHERE  mpres_id='"+id+"')";
                String detvalus = "DELETE FROM dvalus WHERE mpre_id='"+id+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+id+"')";
                String mvalus = "DELETE FROM mvalus WHERE mpre_id='"+id+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+id+"')";
                String admppres = "DELETE FROM admppres WHERE mpre_id='"+id+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+id+"')";
                String pays = "DELETE FROM pays WHERE mpre_id='"+id+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+id+"')";
                String cmpres = "DELETE FROM cmpres WHERE mpre_id='"+id+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+id+"')";
                String rcppres = "DELETE FROM rcppres WHERE mpre_id='"+id+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+id+"')";
                String borrar = "DELETE FROM mpres where id='"+id+"'";
                String borrarNP =  "DELETE FROM mpres WHERE mpres_id='"+id+"'";
                Statement stpres = (Statement) conexion.createStatement();
                Statement stppres = (Statement) conexion.createStatement();
                Statement stmats= (Statement) conexion.createStatement();
                Statement stequipo = (Statement) conexion.createStatement();
                Statement stmano = (Statement) conexion.createStatement();
                Statement stpresNP = (Statement) conexion.createStatement();
                
                
                stppres.execute(borrarpartidas);
                stppres.execute(borrarmats);
                stppres.execute(borrarequips);
                stppres.execute(rcppres);
                stppres.execute(borrardmanos);
                stppres.execute(borrarmates);
                stppres.execute(borrarequipos);
                stppres.execute(borrarmanos);
                stppres.execute(detvalus);
                stppres.execute(mvalus);
                stppres.execute(admppres);
                stppres.execute(pays);
                stpres.execute(cmpres);
                stpres.execute(borrar);
                stpresNP.execute(borrarNP);
                JOptionPane.showMessageDialog(null, "El presupuesto ha sido eliminado!!");
                
               }
            // TODO add your handling code here:
            catch (SQLException ex) {
                Logger.getLogger(Presupuesto.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
        }
    }
    @Override
    public void run() {
        String descri;
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
           
            borrarpres(mpres);
            
            presu.setValue(7);
            porc.repaint();
                     
              //MATERIALES
            Statement car2= (Statement) conn.createStatement();
            Statement esc2 = (Statement) conexion.createStatement();
            sql = "Select * from Materiales";
            ResultSet rst2 = car2.executeQuery(sql);          
            float div = 100/getmat();
            float suma = div+1;
           
             while(rst2.next()){
                 int valor=0;
                 if(suma>1){
                  valor=(int) Math.rint(suma);
                 }
                 mat.setValue(valor);
                 porc.repaint();
                suma = suma+div;
                 descri=rst2.getString("Descripcion");
                 descri=descri.replace("'", "º");
                 sql= "REPLACE INTO mmpres VALUES ('"+mpres+"',"
                                              + "'"+rst2.getString("IDMateriales")+"',"
                                              + "'"+descri+"', IFNULL("
                                              + rst2.getBigDecimal("Desperdicio")+",0), IFNULL("                                                         
                                              + rst2.getBigDecimal("Precio")+",0),"                                                          
                                              + "'"+rst2.getString("Unidad")+"',"
                                              + "'1')";
                  esc2.execute(sql); 

            }
             mat.setValue(100);
             presu.setValue(14);
             porc.repaint();
              //MANO DE OBRA
            Statement car1= conn.createStatement();
           
            sql = "Select * from ManoObra";
            ResultSet rst1 = car1.executeQuery(sql);
           
           float rows = 100/getmano();
            suma=rows+1;
           
              while(rst1.next()){
                  int valor=0;
                 if(suma>1){
                  valor=(int) Math.rint(suma);
                 }
                 mano.setValue(valor);
                 porc.repaint();
                 suma=suma+rows;
                  Statement esc1 = (Statement) conexion.createStatement();
                String sql1= "REPLACE INTO mmopres VALUES ('"+mpres+"',"
                                              + "'"+rst1.getString("IDManoObra")+"',"
                                              + "'"+rst1.getString("Descripcion")+"', IFNULL("
                                              + rst1.getBigDecimal("Bono")+",0), IFNULL("                                                         
                                              + rst1.getBigDecimal("Salario")+",0), IFNULL("                                                         
                                              + rst1.getBigDecimal("Subsidio")+",0),"                                                                                                 
                                              + "'1')";
                System.out.println("mano de obra "+sql1);
                 
                   esc1.execute(sql1); 

            }
             presu.setValue(21);
             mano.setValue(100);
             porc.repaint();
              //EQUIPOS
            Statement car4= (Statement) conn.createStatement();
            Statement esc4 = (Statement) conexion.createStatement();
            sql = "Select * from Equipos";
            ResultSet rst4 = car4.executeQuery(sql); 
            div = 100/getequipo();
            suma=div+1;
             while(rst4.next()){
                 int valor=0;
                 if(suma>1){
                  valor=(int) Math.rint(suma);
                 }
                 eq.setValue(valor);
                 porc.repaint();
                 suma=suma+div;
                 sql= "REPLACE INTO mepres VALUES ('"+mpres+"',"
                                              + "'"+rst4.getString("IDEquipos")+"',"
                                              + "'"+rst4.getString("Descripcion")+"', IFNULL("
                                              + rst4.getBigDecimal("Depreciacion")+",0), IFNULL("                                                         
                                              + rst4.getBigDecimal("Precio")+",0),"                                                          
                                              + "1)";                 
                  esc4.execute(sql); 

            }
             eq.setValue(100);
             presu.setValue(28);
             porc.repaint();
             car4.close();
            //RECUPERAR PRESUPUESTOS
            Statement car0= (Statement) conn.createStatement();
            Statement esc0 = (Statement) conexion.createStatement();
            sql = "Select * from presupuesto";
            ResultSet rst0 = car0.executeQuery(sql); 
            rst0.next();
            prop = rst0.getString("IDPropietario");
            cont = rst0.getString("IDContratista");
            sql = "REPLACE INTO mpres (id,nomabr,nombre,ubicac,fecini,fecfin,porgam,porcfi,"
                    + "porimp,poripa,porpre,poruti,codpro,codcon,status) VALUES "
                                                         + "('"+mpres+"',"
                                                         + "'"+rst0.getString("NombreAbrev")+"',"
                                                         + "'"+rst0.getString("Descripcion")+"',"
                                                         + "'"+rst0.getString("Ubicacion")+"',"
                                                         + "'"+rst0.getDate("FechaInicio")+"',"                                                         
                                                         + "'"+rst0.getDate("FechaFin")+"',"
                                                         + " IFNULL("+rst0.getBigDecimal("PAdminYGastos")+",0),"
                                                         + " IFNULL("+rst0.getBigDecimal("PCostosFinancieros")+",0),"
                                                         + " IFNULL("+rst0.getBigDecimal("PImpuestoGeneral")+",0),"                                                         
                                                         + " IFNULL("+rst0.getBigDecimal("PImpuestoPartidas")+",0),"                                                         
                                                         + " IFNULL("+rst0.getBigDecimal("PPrestaciones")+",0),"                                                         
                                                         + " IFNULL("+rst0.getBigDecimal("PUtilidad")+",0),"                                                         
                                                         + "'"+prop+"',"                                                         
                                                         + "'"+cont+"',"                                                         
                                                         + "1)";
             esc0.execute(sql);
             presu.setValue(35);
             //GUARDAR CONTRATISTA
            Statement car00= (Statement) conn2.createStatement();
            Statement esc00 = (Statement) conexion.createStatement();
            sql = "Select * from Contratistas where IDContratista='"+cont+"'";
            
            ResultSet rst00 = car00.executeQuery(sql); 
            
            int count=rst00.getFetchSize();
           
            if(count>0)
            {
                
                String contexiste = "SELECT * FROM mconts WHERE id='"+cont+"'";
                Statement existe= conexion.createStatement();
                ResultSet rexiste = existe.executeQuery(contexiste);
                int cuenta =rexiste.getFetchSize();
                int op=0;
            if(cuenta>0){
                op = JOptionPane.showConfirmDialog(detmat, "Contratista ya existe, ¿Desea sustituirlo?", "¿Sustituir Contratista?", JOptionPane.YES_NO_OPTION);
            }
            if((op==JOptionPane.YES_OPTION||cuenta==0) &&  rst00.next()){
            sql= "REPLACE INTO mconts (id,nombre,direcc,telefo,fax,email,"
                    + " ingres,cedres,civres,ingins,repleg,cedrep,cedins,civins,encabe,rif,nit,status) VALUES "
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
            }
            }
            presu.setValue(42);
            porc.repaint();
             //GUARDAR PROPIETARIO
            Statement car01= (Statement) conn2.createStatement();
            Statement esc01 = (Statement) conexion.createStatement();
            sql = "Select * from Propietarios where IDPropietario='"+prop+"'";
            ResultSet rst01 = car01.executeQuery(sql);
            
            int conta = rst01.getFetchSize();
           
            if(conta>0){
            String existeprop = "SELECT * FROM mprops WHERE id='"+prop+"'";
            ResultSet rstprop = conexion.createStatement().executeQuery(existeprop);
            int cuenta = rstprop.getFetchSize();
            int op=0;
            if(cuenta>0){
                JOptionPane.showConfirmDialog(detmat, "Propietario ya existe, ¿Desea sustituirlo?","¿Sustituir Propietario?", JOptionPane.YES_NO_OPTION);
            }
            if((op==JOptionPane.YES_OPTION||cuenta==0)&& rst01.next()){
            sql= "REPLACE INTO mprops (id,nombre,telefo,pagina,status) VALUES "
                                                         + "('"+rst01.getString("IDPropietario")+"',"
                                                         + "'"+rst01.getString("Nombre")+"',"
                                                         + "'"+rst01.getString("Telefono")+"',"
                                                         + "'"+rst01.getString("URL")+"',"                                                                                                                  
                                                         + "'1')";
             esc01.execute(sql);
            }
            }
           
            presu.setValue(49);
            porc.repaint();
             //PARTIDAS
            String grupo, numero,tipop,status;
            Statement car_0= (Statement) conn.createStatement();
            Statement esc_0 = (Statement) conexion.createStatement();
            sql = "Select * from Partidas ORDER BY NroPartida";
            String nrocuadro="0", mppre_id=null, tiporec=null;
            String nrocapitulo="0";
             String tiponp=null;
            String nropresupuesto="0";
            ResultSet rst_0 = car_0.executeQuery(sql);          
            div = 100/getpartida();
            suma=div+1;
             while(rst_0.next()){
                 int valor=0;
                 tiporec="";
                 tipop="";
                 tiponp="";
                 if(suma>1){
                  valor=(int) Math.rint(suma);
                 }
                 partida.setValue(valor);
                 suma=suma+div;
                 
                 porc.repaint();
                 suma=suma+div;
                 numero=rst_0.getString("NroPartida");
                              
                 descri=rst_0.getString("Descripcion");
                 status=rst_0.getString("Status");
                 String auxpres = mpres;
                
                 if(status.equals("0")){
                     tipop="Org";  
                 }
                 else {
                     
                     if(status.equals("1")){
                        tipop="NP";
                        tiponp="NP";
                        auxpres=mpres;
                         nropresupuesto = rst_0.getString("Cuadro");
                        mpres = mpres+tipop+nropresupuesto;
                        String insertpres = "REPLACE INTO mpres "
                                + "SELECT '"+mpres+"', nomabr, nombre, ubicac, fecini, fecfin, feccon, fecimp, porgam, "
                                + "porcfi, porimp, poripa, porpre, poruti, codpro,codcon, parpre, nrocon, nroctr, fecapr, "
                                + "nrolic, status, '"+auxpres+"', memo, timemo, fecmemo, seleccionado,  partidapres, CURDATE(), "
                                + "null FROM mpres WHERE id='"+auxpres+"'";
                        Statement stinsert = conexion.createStatement();
                        stinsert.execute(insertpres);
                        //CREAR PRESUPUESTO DE NP
                     }
                      if(status.equals("2")){
                         mppre_id=rst_0.getString("Numero");
                         nrocuadro = rst_0.getString("Cuadro");
                        tipop="VP";  
                        tiporec="VP-"+mppre_id;
                        tiponp=null;
                        auxpres=mpres;
                         nropresupuesto = nrocuadro;
                        mpres = mpres+" "+tipop+"-"+nrocuadro;
                        String insertpres = "REPLACE INTO mpres "
                                + "SELECT '"+mpres+"', nomabr, nombre, ubicac, fecini, fecfin, feccon, fecimp, porgam, "
                                + "porcfi, porimp, poripa, porpre, poruti, codpro,codcon, parpre, nrocon, nroctr, fecapr, "
                                + "nrolic, status, '"+auxpres+"', memo, timemo, fecmemo, seleccionado,  partidapres, CURDATE(), "
                                + "'"+nrocuadro+"' FROM mpres WHERE id='"+auxpres+"'";
                        Statement stinsert = conexion.createStatement();
                        stinsert.execute(insertpres);
                  
                 }
                 }
                
                 porc.repaint();
                 String selectcapitulo = "SELECT TOP 1 NroCapitulo, Descripcion FROM Capitulos WHERE "
                         + "NroPartida>"+numero+"";
                 Statement stcap = conn.createStatement();
                 ResultSet rstcap = stcap.executeQuery(selectcapitulo);
                 while(rstcap.next()){
                     nrocapitulo=rstcap.getString(1);
                     String insertcap = "REPLACE INTO cmpres (descri, codigo, mpre_id) "
                             + "VALUES ('"+nrocapitulo+"', '"+rstcap.getString(2) +"','"+mpres+"')";
                     Statement stinsert = conexion.createStatement();
                     stinsert.execute(insertcap);
                     String consultaultimoid = "SELECT id FROM cmpres ORDER BY id DESC LIMIT 1";
                     Statement stultimo = conexion.createStatement();
                     ResultSet rstultimo = stultimo.executeQuery(consultaultimoid);
                     while(rstultimo.next()){
                         nrocapitulo=rstultimo.getString(1);
                     }
                 }
                 
                 descri=descri.replace("'", "º");
                 sql= "REPLACE INTO mppres (mpre_id,id,numero,numegrup,descri,idband,porcgad,porcpre,porcutil," 
                 +"precasu,precunit,rendimi,unidad,redondeo,status,cantidad,tipo,nropresupuesto,nrocuadro,mppre_id,"
                 +"tiporec,refere,fechaini,fechafin,cron,rango,lapso,capitulo,tiponp) "
                 +"VALUES ('" +mpres+ "','"+rst_0.getString("CodCovenin")+"',"
                                              + "'"+numero+"',"
                                              + "'"+numero+"',"
                                              + "'"+descri+"',"                                              
                                              + "'"+rst_0.getString("IDBancoDatos")+"',"
                                              + "'"+rst_0.getBigDecimal("PAdminYGastos")+"',"
                                              + "'"+rst_0.getBigDecimal("PPrestaciones")+"',"                                                                                            
                                              + "'"+rst_0.getBigDecimal("PUtilidad")+"',"
                                              + "'"+rst_0.getBigDecimal("PrecioAsumido")+"',"
                                              + "'"+rst_0.getBigDecimal("PrecioUnitario")+"',"
                                              + "'"+rst_0.getBigDecimal("Rendimiento")+"',"
                                              + "'"+rst_0.getString("Unidad")+"', IFNULL("
                                              + +rst_0.getInt("Redondeo")+",0),"
                                              + "'1', IFNULL("                                              
                                              +rst_0.getBigDecimal("Cantidad")+",0),"
                                              + "'" + tipop + "','"+nropresupuesto+"','"+nrocuadro+"',"
                                               + "'"+mppre_id+"','"+tiporec+"',null,null,null,0,0,0,'"+nrocapitulo+"','"+tiponp+"')";                   
                   esc_0.execute(sql); 
                    //MANO DE OBRA POR PARTIDAS
            Statement carga5= (Statement) conn.createStatement();
            Statement escribir5 = (Statement) conexion.createStatement();
            sql = "Select mo.IDPartidas,mo.IDManoObra,mo.Cantidad,mo.Numero,mo.Nuevo,m.Bono,"
                    + "m.Salario,m.Subsidio from ManoObraPartida mo, manoobra m where "
                    + "m.idmanoobra=mo.idmanoobra AND mo.Numero="+numero+"";
            ResultSet rst5 = carga5.executeQuery(sql); 
            
            div = 100/getdetmano(Integer.parseInt(numero));
            suma=div+1;
            presu.setValue(63);
             while(rst5.next()){
                 int valora=0;
                 if(suma>1){
                  valora=(int) Math.rint(suma);
                 }
                 detmano.setValue(valora);
                 porc.repaint();
                 suma=suma+div;
                Statement bd1= (Statement) conexion.createStatement(); 
                String sPart=rst5.getString("IDPartidas");
                String sDeta=rst5.getString("IDManoObra");
                 sql= "REPLACE INTO dmoppres VALUES ('"+mpres+"',"                                              
                                              + "'"+sDeta+"',"
                                              + "'"+sPart+"',"
                                              + numero+","
                                              + rst5.getBigDecimal("cantidad")+","                                                         
                                              + rst5.getBigDecimal("bono")+","                                                          
                                              + rst5.getBigDecimal("salario")+","                                                          
                                              +  rst5.getBigDecimal("subsidio")+","                                                          
                                              + "'1')";
                   System.out.println(sql);                   
                   escribir5.execute(sql); 
                 String consultamano = "SELECT * FROM ManoObra WHERE IDManoObra='"+sDeta+"'";
                 Statement stmano = conn.createStatement();
                 ResultSet rstmano = stmano.executeQuery(consultamano); 
                 
                 while(rstmano.next())
                 {
                     String inserta = "REPLACE INTO mmopres VALUES "
                             + "('"+mpres+"', '"+sDeta+"', '"+rstmano.getString("Descripcion") +"',"
                             + " IFNULL("+rstmano.getDouble("Bono") +",0),  IFNULL("+rstmano.getDouble("Salario") +",0), "
                             + " IFNULL("+rstmano.getString("Subsidio")+",0),1)";
                     Statement stinserta = conexion.createStatement();
                     System.out.println("mano de obra "+inserta);
                     stinserta.execute(inserta);
                 }
                 stmano.close();
            }   
             detmano.setValue(100);
            
             porc.repaint();
            //MATERIALES POR PARTIDAS
            Statement car3= (Statement) conn.createStatement();
            Statement esc3 = (Statement) conexion.createStatement();
            sql = "Select dm.IDpartidas,dm.IDMateriales,dm.Cantidad,dm.Numero,dm.Nuevo,m.Precio from MaterialesPartida"
                    + " dm, materiales m where dm.idmateriales=m.idmateriales AND dm.Numero="+numero+"" ;
            ResultSet rst3 = car3.executeQuery(sql); 
            div = 100/getdetmat(Integer.parseInt(numero));
            suma=div+1;
               while(rst3.next()){     
                    int valora=0;
                 if(suma>1){
                  valora=(int) Math.rint(suma);
                 }
                 detmat.setValue(valora);
                 porc.repaint();
                 suma=suma+div;
                Statement bd2= (Statement) conexion.createStatement(); 
                String sPart=rst3.getString("IDPartidas");
                String sDeta=rst3.getString("IDMateriales");
               
                 sql= "REPLACE INTO dmpres VALUES ('"+mpres+"',"
                                              + "'"+sPart+"',"
                                              + "'"+sDeta+"',"
                                              + rst3.getInt("Numero")+", IFNULL("                                                         
                                              + rst3.getBigDecimal("Cantidad")+",0), IFNULL("
                                              + rst3.getBigDecimal("precio")+",0),"                                                          
                                              + "'1')";
                 esc3.execute(sql); 
                 String consultamat = "SELECT * FROM Materiales WHERE IDMateriales='"+sDeta+"'";
                 Statement stmat = conn.createStatement();
                 ResultSet rstmat = stmat.executeQuery(consultamat); 
                 
                 while(rstmat.next())
                 {
                     descri=rstmat.getString("Descripcion");
                     descri=descri.replace("'", "°");
                     String inserta = "REPLACE INTO mmpres VALUES "
                             + "('"+mpres+"', '"+sDeta+"', '"+ descri+"',"
                             + "IFNULL("+rstmat.getDouble("Desperdicio") +",0), IFNULL("+rstmat.getDouble("Precio") +",0), "
                             + "'"+rstmat.getString("Unidad") +"',1)";
                     Statement stinserta = conexion.createStatement();
                     stinserta.execute(inserta);
                 }
                  
                  stmat.close();
            }
             detmat.setValue(100);
           porc.repaint();
            //EQUIPOS POR PARTIDAS
            Statement car5= (Statement) conn.createStatement();
            Statement esc5 = (Statement) conexion.createStatement();
            sql = "Select de.IDPartidas,de.IDEquipos,de.Cantidad,de.Numero,de.Nuevo,e.Precio "
                    + "from EquiposPartida de, equipos e where de.idequipos=e.idequipos AND de.Numero="+numero+"";
            ResultSet rst6 = car5.executeQuery(sql); 
            div=100/getdeteq(Integer.parseInt(numero));
            suma=div+1;
             while(rst6.next()){
                  int valora=0;
                 if(suma>1){
                  valora=(int) Math.rint(suma);
                 }
                 deteq.setValue(valora);
                 porc.repaint();
                 suma=suma+div;
               
                String sPart=rst6.getString("IDPartidas");
                String sDeta=rst6.getString("IDEquipos");
                 
                 sql= "REPLACE INTO deppres VALUES ('"+mpres+"',"
                                              + "'"+sPart+"',"
                                              + "'"+sDeta+"',"
                                              + numero+","
                                              + rst6.getBigDecimal("cantidad")+","                                                         
                                              + rst6.getBigDecimal("precio")+","                                                          
                                              + "1)";                   
                   esc5.execute(sql); 
                   String consultaeq = "SELECT * FROM Equipos WHERE IDEquipos='"+sDeta+"'";
                 Statement steq = conn.createStatement();
                 ResultSet rsteq = steq.executeQuery(consultaeq); 
                 
                 while(rsteq.next())
                 {
                     String inserta = "REPLACE INTO mepres VALUES "
                             + "('"+mpres+"', '"+sDeta+"', '"+rsteq.getString("Descripcion") +"',"
                             + ""+rsteq.getDouble("Depreciacion") +", "+rsteq.getDouble("Precio") +",1)";
                     Statement stinserta = conexion.createStatement();
                     stinserta.execute(inserta);
                 }
                 steq.close();
                
            } 
                   mpres=auxpres;
                   deteq.setValue(100);
                   porc.repaint();
            } 
             partida.setValue(100);
             presu.setValue(77);
             porc.repaint();
           

                       
          
             //VALUACIONES
            Statement car10= (Statement) conn.createStatement();
            Statement esc10 = (Statement) conexion.createStatement();
            sql = "Select * from Valuaciones";
            ResultSet rst10 = car10.executeQuery(sql); 
             while(rst10.next()){
                 String fecha1,fecha2;                                  
                 SimpleDateFormat formatofecha=new SimpleDateFormat("yyyy-MM-dd");
                 fecha1=formatofecha.format(rst10.getDate("desde"));    
                 fecha2=formatofecha.format(rst10.getDate("hasta"));                                      
                 sql= "REPLACE INTO mvalus (id,desde,hasta,status,mpre_id,tipo,lapso) "
                       +"VALUES ('"+rst10.getString("numero")+"',"
                                              + "'"+fecha1+"',"
                                              + "'"+fecha2+"','1',"
                                              + "'"+mpres+"','Parcial',1)" ;                                 
                 esc10.execute(sql); 

            }
             //DETALLE DE VALUACIONES
            Statement car11= (Statement) conn.createStatement();
            Statement esc11 = (Statement) conexion.createStatement();
            sql = "Select * from Detalle";
            ResultSet rst11 = car11.executeQuery(sql); 
             while(rst11.next()){                 
                 sql= "REPLACE INTO dvalus (mpre_id,mvalu_id,mppre_id,cantidad,precio,numepart,status,aumento) "
                       +"VALUES ('"+ mpres + "', "+rst11.getString("numero")+","
                                              + "'"+rst11.getString("Partida")+"',"
                                              + "'"+rst11.getBigDecimal("cantidad")+"',"
                                              + " IFNULL('"+rst11.getBigDecimal("precio")+"',0),"
                                              + "'"+rst11.getInt("nropartida")+"','1',0)";                                              
                  System.out.println(sql);   
                  esc11.execute(sql); 

            }
             presu.setValue(84);
             porc.repaint();
           //AUMENTOS
            String selectaumento = "SELECT * FROM Aumentos";
            Statement staumento = conn.createStatement();
            ResultSet rstaumento = staumento.executeQuery(selectaumento);
            String valua="", aux;
            while(rstaumento.next()){
                valua="";
                aux=rstaumento.getString("Valua");
                for(int i=0; i<aux.length();i++)
                {
                    if(Character.isDigit(aux.charAt(i))){
                        valua+=aux.charAt(i);
                    }
                }
                System.out.println("valua "+valua);
                String insertaum = "REPLACE INTO admppres (payd_id, mpre_id, numepart, mvalu_id, aumento, disminucion)"
                        + " VALUES ('"+rstaumento.getString("Correla")+"', '"+mpres+"', '"+rstaumento.getString("Numero")+"',"
                        + "'"+valua+"', '"+rstaumento.getString("Aumento")+"','"+rstaumento.getString("Disminu") +"')";
                Statement staum = conexion.createStatement();
                staum.execute(insertaum);
                
            }
             presu.setValue(100);
             porc.repaint();
            //---------------------------
     JOptionPane.showMessageDialog(recup, "Recuperación de datos del Presupuesto Completado..");
     String consultacont = "SELECT COUNT(*) FROM mconts WHERE id='"+cont+"'";
     Statement st = conexion.createStatement();
     ResultSet rstcont = st.executeQuery(consultacont);
     int numcont=0, numprop=0;
     while(rstcont.next()){
         numcont=rstcont.getInt(1);
     }
     if(numcont==0){
        Statement esccont = conexion.createStatement();
        sql = "REPLACE INTO mconts (id,nombre) VALUES ('"+cont+"','"+cont+"')";
        esccont.execute(sql);
        JOptionPane.showMessageDialog(recup, "La contratista "+cont+" no se encuentra registrada, modifique valores en Parametros/Contratista");
            
     }
     String consultaprop = "SELECT COUNT(*) FROM mprops WHERE id='"+prop+"'";
     Statement stprop = conexion.createStatement();
     ResultSet rstprop = st.executeQuery(consultaprop);
     while(rstprop.next()){
         numprop=rstprop.getInt(1);
     }
     if(numprop==0){
         Statement escprop=conexion.createStatement();
        sql= "REPLACE INTO mprops (id,nombre) VALUES ('"+prop+"','"+prop+"')";
        escprop.execute(sql);
        JOptionPane.showMessageDialog(recup, "El propietario "+prop+" no se encuentra registrado, modifique valores en Parametros/Propietario");
     }
     prin.setavalible(true);
     porc.doClose(RET_OK);
     recup.doClose(RET_OK);
     prin.entro=0;
     prin.entro();
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(recup, "Error al subir presupuesto "+ex.getMessage());
            recup.doClose(RET_OK);
            
            Logger.getLogger(RecuperarTab.class.getName()).log(Level.SEVERE, null, ex);
             this.stop();
        }
    }
}
