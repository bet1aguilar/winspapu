/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package winspapus;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;



/**
 *
 * @author Betmart
 */
public class consulta extends Thread{
    private int cont = 0 ;
    ResultSet propio;
    Connection conex=null;
    boolean sigue=false;
    private String codicove="", numero, numegrup, refere,mbdat_id, porcgad, porcpre, porcutil;
   private String precasu, precunit, redondeo, status, mtabus_id,cantidad, descri, rendimi, unidad, tabnuevo;
   private String sql="", mtabuid;
   Statement st = null, stmt=null;
   Principal obj;
  

    
    public consulta(ResultSet rs, int tiempo, Connection conex, String tab, String tab2, Principal obj){
        propio=rs;
       
        this.obj = obj;
        mtabuid = tab2;
        tabnuevo = tab;
    
             
            
        this.conex = conex;
        try {
            st = (Statement) conex.createStatement();
            stmt =(Statement) conex.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(consulta.class.getName()).log(Level.SEVERE, null, ex);
        }
        sigue = true;
    }
    
    private void insertmat() throws SQLException{
         Statement s = (Statement) conex.createStatement();
      
        ResultSet rsmat = st.executeQuery("SELECT mmtab_id, cantidad, precio FROM dmtabs WHERE mtabus_id = '"+mtabuid+"' AND numepart="+numero);
        String mmtab_id, precio;
             
             while (rsmat.next()) {
                  mmtab_id =rsmat.getObject(1).toString();
                  cantidad = rsmat.getObject(2).toString();
                  precio = rsmat.getObject(3).toString();
                  
                  sql = "INSERT INTO dmtabs VALUES ('"+tabnuevo+"', '"+codicove+"', " + 

                                                        "'"+mmtab_id+"'," + 

                                                        ""+numero+", " + 

                                                        ""+cantidad+", " +

                                                        ""+precio+", " + 
                                                                                                                           
                                                        "1);";
        try {
                s.execute(sql);
            } catch (SQLException ex) {
                Logger.getLogger(tab.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    }
    
     private void insertaequip() throws SQLException{
          Statement s = (Statement) conex.createStatement();
        ResultSet rse = st.executeQuery("SELECT metab_id, cantidad, precio FROM deptabs WHERE mtabus_id = '"+mtabuid+"' AND numero="+numero);
        String metab_id, precio;
             
             while (rse.next()) {
                  metab_id =rse.getObject(1).toString();
                  cantidad = rse.getObject(2).toString();
                  precio = rse.getObject(3).toString();
                  
                  sql = "INSERT INTO deptabs VALUES ('"+tabnuevo+"', '"+codicove+"', " + 

                                                        "'"+metab_id+"'," + 

                                                        ""+numero+", " + 

                                                        ""+cantidad+", " +

                                                        ""+precio+", " + 
                                                                                                                           
                                                        "1);";
        try {
                stmt.execute(sql);
            } catch (SQLException ex) {
                Logger.getLogger(tab.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    }
     
      private void insertamano() throws SQLException{
           Statement s = (Statement) conex.createStatement();
        ResultSet rsma = st.executeQuery("SELECT mmotab_id, cantidad, bono, salario, subsidi FROM dmoptabs WHERE mtabus_id = '"+mtabuid+"' AND numero="+numero);
        String mmoptab_id, bono, salario, subsidi;
             
             while (rsma.next()) {
                  mmoptab_id =rsma.getObject(1).toString();
                  cantidad = rsma.getObject(2).toString();
                  bono = rsma.getObject(3).toString();
                  salario = rsma.getObject(4).toString();
                  subsidi = rsma.getObject(5).toString();
                  sql = "INSERT INTO dmoptabs VALUES ('"+tabnuevo+"', " + 

                                                        "'"+mmoptab_id+"'," + 

                                                        ""+numero+", " + 

                                                        ""+cantidad+", " +

                                                        ""+bono+", " +
                                                        
                                                        ""+salario+", " +
                                                        
                                                        ""+subsidi+", " +
                          
                                                        "1,'"+codicove+"');";
        try {
                s.execute(sql);
            } catch (SQLException ex) {
                Logger.getLogger(tab.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
             rsma.close();
    }
    @Override
    public void run(){
        try {
       
            Statement rst = (Statement) conex.createStatement(), mat=(Statement) conex.createStatement(), equip=(Statement) conex.createStatement();
            Statement mano = (Statement) conex.createStatement();
            String sqlm = "INSERT INTO mptabs (codicove, numero, numegrup, descri, mbdat_id, porcgad, porcpre,porcutil, "
                        + "precasu, precunit, rendimi, unidad, status, mtabus_id, cantidad )"+
                         "SELECT DISTINCT mp.codicove as codicove, mp.numero as numero, mp.numegrup as numegrup, mp.descri as descri, "
                        + "mp.mbdat_id as mbdat_id, mp.porcgad as porcgad, mp.porcpre as porcpre,"+
                         "mp.porcutil as porcutil, mp.precasu as precasu, mp.precunit as precunit, mp.rendimi as rendimi, mp.unidad as unidad, "
                        + "mp.status as status, tb.id as mtabus_id, mp.cantidad as cantidad "+
                         "FROM mptabs as mp, mtabus as tb WHERE tb.id='"+tabnuevo+"' AND mp.mtabus_id='"+mtabuid+"'";
            System.out.println("+insertar partida en copiar tabulador "+sqlm);
            rst.execute(sqlm);
            
            sqlm="INSERT INTO dmtabs (mtabus_id, mptab_id, mmtab_id, numepart, cantidad, precio, status)"+
                "SELECT DISTINCT tb.id as mtabus_id, mp.codicove as mptab_id, mm.mmtab_id as mmtab_id, mp.numero as numepart,"+
                "mm.cantidad as cantidad, mm.precio as precio, mm.status as status FROM mtabus as tb, mptabs as mp,"
                    + "dmtabs as mm WHERE tb.id='"+tabnuevo+"'  AND mm.mtabus_id=mp.mtabus_id AND mp.mtabus_id='"+mtabuid+"' AND mm.numepart=mp.numero"
                    + " GROUP BY tb.id, mp.codicove, mm.mmtab_id, mm.numepart";
         System.out.println("+insertar detalle de material en copiar tabulador "+sqlm);
         
            mat.execute(sqlm);
             
            sqlm="INSERT INTO deptabs (mtabus_id, mptab_id, metab_id, numero, cantidad, precio, status)"
                    + "SELECT DISTINCT tb.id as mtabus_id, mp.codicove as mptab_id, me.metab_id as metab_id, mp.numero as numero,"
                    + "me.cantidad as cantidad, me.precio as precio, me.status as status FROM mtabus as tb, mptabs as mp, "
                    + "deptabs as me WHERE tb.id='"+tabnuevo+"'  AND me.mtabus_id=mp.mtabus_id AND mp.mtabus_id='"+mtabuid+"' "
                    + "AND me.numero=mp.numero"
                    + " GROUP BY tb.id, mp.codicove, me.metab_id, mp.numero";
             System.out.println("+insertar detalle de equipo en copiar tabulador "+sqlm);
            equip.execute(sqlm);
            
            sqlm="INSERT INTO dmoptabs (mtabus_id, mptab_id, mmotab_id, numero, cantidad, bono, salario, subsidi, status)"
                    + "SELECT DISTINCT tb.id as mtabus_id, mp.codicove as mptab_id, mmo.mmotab_id as mmotab_id, mp.numero as numero,"
                    + "mmo.cantidad as cantidad, mmo.bono as bono,"
                    + "mmo.salario as salario, mmo.subsidi as subsidi, mmo.status as status "
                    + "FROM mtabus as tb, mptabs as mp, dmoptabs as mmo"
                    + " WHERE tb.id='"+tabnuevo+"'  AND mmo.mtabus_id=mp.mtabus_id AND mp.mtabus_id='"+mtabuid+"' AND mmo.numero=mp.numero"
                    + " GROUP BY tb.id, mp.codicove, mmo.mmotab_id, mp.numero";
            System.out.println("+insertar detalle de mano de obra en copia tabulador "+sqlm);
            mano.execute(sqlm);
            
            JOptionPane.showMessageDialog(null, "Se ha copiado el tabulador");
        } catch (SQLException ex) {
             JOptionPane.showMessageDialog(null, "No Se ha copiado el tabulador");
            Logger.getLogger(consulta.class.getName()).log(Level.SEVERE, null, ex);
        }
        
            
             
             
             obj.buscatab();
        
        
    }
   
}
