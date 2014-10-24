/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * reporte.java
 *
 * Created on 11/08/2014, 12:16:29 PM
 */
package valuaciones;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import reportes.reportepresupuesto;
import winspapus.denumeroaletra1;

/**
 *
 * @author Betmart
 */
public class reporte extends javax.swing.JDialog {

    /** A return status code - returned if Cancel button has been pressed */
    public static final int RET_CANCEL = 0;
    /** A return status code - returned if OK button has been pressed */
    public static final int RET_OK = 1;
    String ruta="";
    private FileInputStream input;
    private Connection conex;
    String mpres;
    String mvalu;
    Date date=new Date();
    String fecha;
    String tipovalu;
     SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
    /** Creates new form reporte */
    public reporte(java.awt.Frame parent, boolean modal, Connection conex, String mpres, String mvalu, String tipovalu) {
        super(parent, false);
        initComponents();
        this.conex=conex;
        this.tipovalu=tipovalu;
        this.mpres = mpres;
        this.mvalu = mvalu;
        jDateChooser1.setDate(date);
        // Close the dialog when Esc is pressed
        String cancelName = "cancel";
        InputMap inputMap = getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), cancelName);
        ActionMap actionMap = getRootPane().getActionMap();
        actionMap.put(cancelName, new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                doClose(RET_CANCEL);
            }
        });
    }
    
    public void generarreporte()
    {
        try {
            JasperPrint print=null;
         
          Map parameters = new HashMap();
            try {
                input = new FileInputStream(new File("valuacion.jrxml"));
                 String borra = "TRUNCATE TABLE reportevaluacion";
                  Statement truncate;
                try {
                    truncate = (Statement) conex.createStatement();
                      truncate.execute(borra);
                      String consultaoriginal= "INSERT INTO reportevaluacion "
                              + "SELECT mp.numegrup as nro, mp.id as codigo, mp.descri as descri, mp.unidad as unidad,"
                              + "IFNULL((SELECT SUM(cantidad) as cantidades "
                              + "FROM dvalus WHERE numepart=mp.numero AND (mpre_id='"+mpres+"' OR mpre_id IN "
                              + "(SELECT id FROM mpres WHERE mpres_id='"+mpres+"')) AND "
                              + "mvalu_id<"+mvalu+"),0) as cantidadanterior,IFNULL((SELECT SUM(cantidad) as cantidades"
                              + " FROM dvalus WHERE numepart=mp.numero AND (mpre_id='"+mpres+"' OR mpre_id IN "
                              + "(SELECT id FROM mpres WHERE mpres_id='"+mpres+"')) AND "
                              + "mvalu_id<="+mvalu+"),0) as cantidadactual, IF(mp.precasu=0,mp.precunit,mp.precasu)"
                              + " as preciounitario,"
                              + "ROUND(IFNULL((SELECT SUM(cantidad) as cantidad "
                              + "FROM dvalus WHERE numepart=mp.numero AND mpre_id='"+mpres+"' AND "
                              + "mvalu_id<="+mvalu+")*IF(mp.precasu=0,mp.precunit,mp.precasu),0),2) as total,"+mvalu+",'"+mpres+"'"
                              + " FROM mppres as mp, dvalus as dv, mvalus as mv WHERE mp.tipo='Org' AND"
                              + " mp.numero = dv.numepart AND dv.mpre_id=mp.mpre_id AND "
                              + "dv.mvalu_id<=mv.id AND mv.id="+mvalu+" AND"
                              + " (mv.mpre_id = '"+mpres+"' OR mv.mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+mpres+"')) "
                              + "AND (dv.mpre_id='"+mpres+"' "
                              + "OR dv.mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+mpres+"'))"
                              + " AND (mp.mpre_id='"+mpres+"' "
                              + "OR mp.mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+mpres+"')) "
                              + "GROUP BY dv.numepart ORDER BY mp.numegrup";
                      Statement original = (Statement) conex.createStatement();
                      original.execute(consultaoriginal);
                      
                      //------------NO Prevista------------------------------------------
                      String cuenta = "SELECT COUNT(*) FROM mppres as mp, dvalus as dv, mvalus as mv "
                              + "WHERE dv.mvalu_id<=mv.id AND mv.id = "+mvalu+" AND (mp.mpre_id='"+mpres+"' OR mp.mpre_id IN "
                              + "(SELECT id FROM mpres WHERE mpres_id='"+mpres+"')) AND (dv.mpre_id ='"+mpres+"' OR "
                              + "dv.mpre_id IN (SELECT id FROM mpres WHERE mpres_id = '"+mpres+"'))AND mp.numero = dv.numepart"
                              + " AND (mv.mpre_id='"+mpres+"' "
                              + "OR mv.mpre_id IN (SELECT id FROM mpres WHERE mpres_id ='"+mpres+"')) AND mp.tipo='NP' "
                              + "AND mp.tiponp='NP'";
                      Statement cuantosnp= (Statement) conex.createStatement();
                      ResultSet rscuantosnp = cuantosnp.executeQuery(cuenta);
                      int cuentas=0;
                      while(rscuantosnp.next()){
                          cuentas = rscuantosnp.getInt(1);
                      }
                      if(cuentas>0)
                      {
                          String insertatit = "INSERT INTO reportevaluacion (codigo,descri,mvalu,mpre)"
                                        + "VALUES ('','PARTIDAS NO PREVISTAS',"+mvalu+",'"+mpres+"')";
                                Statement ins = (Statement) conex.createStatement();
                                ins.execute(insertatit);
                                String consultanp= "INSERT INTO reportevaluacion "
                              + " SELECT mp.numegrup as nro, mp.id as codigo, mp.descri as descri, mp.unidad as unidad,"
                              + " IFNULL((SELECT SUM(cantidad) as cantidades "
                              + " FROM dvalus WHERE numepart=mp.numero AND (mpre_id='"+mpres+"' OR mpre_id IN "
                              + "(SELECT id FROM mpres WHERE mpres_id='"+mpres+"')) AND "
                              + " mvalu_id<"+mvalu+"),0) as cantidadanterior,IFNULL((SELECT SUM(cantidad) as cantidades"
                              + " FROM dvalus WHERE numepart=mp.numero AND (mpre_id='"+mpres+"' OR mpre_id IN "
                              + "(SELECT id FROM mpres WHERE mpres_id='"+mpres+"')) AND "
                              + " mvalu_id<="+mvalu+"),0) as cantidadactual, IF(mp.precasu=0,mp.precunit,mp.precasu) as preciounitario,"
                              + " ROUND(IFNULL((SELECT SUM(cantidad) as cantidad "
                              + " FROM dvalus WHERE numepart=mp.numero AND mpre_id='"+mpres+"' AND "
                              + " mvalu_id<="+mvalu+")*IF(mp.precasu=0,mp.precunit,mp.precasu),0),2) as total,"+mvalu+",'"+mpres+"'"
                              + " FROM mppres as mp, dvalus as dv, mvalus as mv WHERE mp.tipo='NP' AND mp.tiponp='NP' AND"
                              + " mp.numero = dv.numepart AND"
                              + " dv.mvalu_id<=mv.id AND mv.id="+mvalu+" AND"
                              + " (mv.mpre_id = '"+mpres+"' OR mv.mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+mpres+"')) "
                              + " AND (dv.mpre_id='"+mpres+"'"
                              + " OR dv.mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+mpres+"'))"
                              + " AND (mp.mpre_id='"+mpres+"'"
                              + " OR mp.mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+mpres+"'))"
                              + " GROUP BY dv.numepart ORDER BY mp.numegrup";
                                System.out.println("consultanp "+consultanp);
                                Statement noprevista = (Statement) conex.createStatement();
                                noprevista.execute(consultanp);
                                
                      }
                      //------------------Obras extras------------------
                      String cuentaoe = "SELECT COUNT(*) FROM mppres as mp, dvalus as dv, mvalus as mv "
                              + "WHERE dv.mvalu_id<=mv.id AND mv.id = "+mvalu+" AND (mp.mpre_id='"+mpres+"' OR mp.mpre_id IN "
                              + "(SELECT id FROM mpres WHERE mpres_id='"+mpres+"')) AND (dv.mpre_id ='"+mpres+"' OR "
                              + "dv.mpre_id IN (SELECT id FROM mpres WHERE mpres_id = '"+mpres+"'))AND mp.numero = dv.numepart"
                              + " AND (mv.mpre_id='"+mpres+"' "
                              + "OR mv.mpre_id IN (SELECT id FROM mpres WHERE mpres_id ='"+mpres+"')) AND mp.tipo='NP' "
                              + "AND mp.tiponp='OE'";
                       Statement cuantosoe= (Statement) conex.createStatement();
                      ResultSet rscuantosoe = cuantosoe.executeQuery(cuentaoe);
                      int cuentasoe=0;
                      while(rscuantosoe.next()){
                          cuentasoe = rscuantosoe.getInt(1);
                      }
                      if(cuentasoe>0){
                          String insertatit = "INSERT INTO reportevaluacion (codigo,descri,mvalu,mpre)"
                                        + "VALUES ('','OBRAS EXTRAS',"+mvalu+",'"+mpres+"')";
                                Statement ins = (Statement) conex.createStatement();
                                ins.execute(insertatit);
                                String consultaoe= "INSERT INTO reportevaluacion "
                              + " SELECT mp.numegrup as nro, mp.id as codigo, mp.descri as descri, mp.unidad as unidad,"
                              + " IFNULL((SELECT SUM(cantidad) as cantidades "
                              + " FROM dvalus WHERE numepart=mp.numero AND (mpre_id='"+mpres+"' OR mpre_id IN "
                              + " (SELECT id FROM mpres WHERE mpres_id='"+mpres+"')) AND "
                              + " mvalu_id<"+mvalu+"),0) as cantidadanterior,IFNULL((SELECT SUM(cantidad) as cantidades"
                              + " FROM dvalus WHERE numepart=mp.numero AND (mpre_id='"+mpres+"' OR mpre_id IN "
                              + " (SELECT id FROM mpres WHERE mpres_id='"+mpres+"')) AND "
                              + " mvalu_id<="+mvalu+"),0) as cantidadactual, IF(mp.precasu=0,mp.precunit,mp.precasu) as preciounitario,"
                              + " ROUND(IFNULL((SELECT SUM(cantidad) as cantidad "
                              + " FROM dvalus WHERE numepart=mp.numero AND mpre_id='"+mpres+"' AND "
                              + " mvalu_id<="+mvalu+")*IF(mp.precasu=0,mp.precunit,mp.precasu),0),2) as total,"+mvalu+",'"+mpres+"'"
                              + " FROM mppres as mp, dvalus as dv, mvalus as mv WHERE mp.tipo='NP' AND mp.tiponp='OE' AND"
                              + " mp.numero = dv.numepart AND "
                              + " dv.mvalu_id<=mv.id AND mv.id="+mvalu+" AND"
                              + " (mv.mpre_id = '"+mpres+"' OR mv.mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+mpres+"')) "
                              + " AND (dv.mpre_id='"+mpres+"'"
                              + " OR dv.mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+mpres+"'))"
                              + " AND (mp.mpre_id='"+mpres+"'"
                              + " OR mp.mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+mpres+"'))"
                              + " GROUP BY dv.numepart ORDER BY mp.numegrup";
                                Statement obraextra= (Statement) conex.createStatement();
                                obraextra.execute(consultaoe);
                      }
                      //------------Obras Adicionales--------------------------------------------------------
                      String cuentaoa = "SELECT COUNT(*) FROM mppres as mp, dvalus as dv, mvalus as mv "
                              + "WHERE dv.mvalu_id<=mv.id AND mv.id = "+mvalu+" AND (mp.mpre_id='"+mpres+"' OR mp.mpre_id IN "
                              + "(SELECT id FROM mpres WHERE mpres_id='"+mpres+"')) AND (dv.mpre_id ='"+mpres+"' OR "
                              + "dv.mpre_id IN (SELECT id FROM mpres WHERE mpres_id = '"+mpres+"'))AND mp.numero = dv.numepart"
                              + " AND (mv.mpre_id='"+mpres+"' "
                              + "OR mv.mpre_id IN (SELECT id FROM mpres WHERE mpres_id ='"+mpres+"')) AND mp.tipo='NP' "
                              + "AND mp.tiponp='OA'";
                       Statement cuantosoa= (Statement) conex.createStatement();
                      ResultSet rscuantosoa = cuantosoa.executeQuery(cuentaoa);
                      int cuentasoa=0;
                      while(rscuantosoa.next()){
                          cuentasoa = rscuantosoa.getInt(1);
                      }
                      if(cuentasoa>0){
                          String insertatit = "INSERT INTO reportevaluacion (codigo,descri,mvalu,mpre)"
                                        + "VALUES ('','OBRAS ADICIONALES',"+mvalu+",'"+mpres+"')";
                                Statement ins = (Statement) conex.createStatement();
                                ins.execute(insertatit);
                                String consultaAD= "INSERT INTO reportevaluacion "
                              + " SELECT mp.numegrup as nro, mp.id as codigo, mp.descri as descri, mp.unidad as unidad,"
                              + " IFNULL((SELECT SUM(cantidad) as cantidades "
                              + " FROM dvalus WHERE numepart=mp.numero AND (mpre_id='"+mpres+"' OR mpre_id IN "
                              + " (SELECT id FROM mpres WHERE mpres_id='"+mpres+"')) AND "
                              + " mvalu_id<"+mvalu+"),0) as cantidadanterior,IFNULL((SELECT SUM(cantidad) as cantidades"
                              + " FROM dvalus WHERE numepart=mp.numero AND (mpre_id='"+mpres+"' OR mpre_id IN "
                              + " (SELECT id FROM mpres WHERE mpres_id='"+mpres+"')) AND "
                              + " mvalu_id<="+mvalu+"),0) as cantidadactual, IF(mp.precasu=0,mp.precunit,mp.precasu) as preciounitario,"
                              + " ROUND(IFNULL((SELECT SUM(cantidad) as cantidad "
                              + " FROM dvalus WHERE numepart=mp.numero AND mpre_id='"+mpres+"' AND "
                              + " mvalu_id<="+mvalu+")*IF(mp.precasu=0,mp.precunit,mp.precasu),0),2) as total,"+mvalu+",'"+mpres+"'"
                              + " FROM mppres as mp, dvalus as dv, mvalus as mv WHERE mp.tipo='NP' AND mp.tiponp='OA' AND"
                              + " mp.numero = dv.numepart AND "
                              + " dv.mvalu_id<=mv.id AND mv.id="+mvalu+" AND"
                              + " (mv.mpre_id = '"+mpres+"' OR mv.mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+mpres+"')) "
                              + " AND (dv.mpre_id='"+mpres+"'"
                              + " OR dv.mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+mpres+"'))"
                              + " AND (mp.mpre_id='"+mpres+"'"
                              + " OR mp.mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+mpres+"'))"
                              + " GROUP BY dv.numepart ORDER BY mp.numegrup";
                                Statement obraAD= (Statement) conex.createStatement();
                                obraAD.execute(consultaAD);
                      }
                      //------------Obras COMPLEMENTARIAS--------------------------------------------------------
                      String cuentaoc = "SELECT COUNT(*) FROM mppres as mp, dvalus as dv, mvalus as mv "
                              + "WHERE dv.mvalu_id<=mv.id AND mv.id = "+mvalu+" AND (mp.mpre_id='"+mpres+"' OR mp.mpre_id IN "
                              + "(SELECT id FROM mpres WHERE mpres_id='"+mpres+"')) AND (dv.mpre_id ='"+mpres+"' OR "
                              + "dv.mpre_id IN (SELECT id FROM mpres WHERE mpres_id = '"+mpres+"'))AND mp.numero = dv.numepart"
                              + " AND (mv.mpre_id='"+mpres+"' "
                              + "OR mv.mpre_id IN (SELECT id FROM mpres WHERE mpres_id ='"+mpres+"')) AND mp.tipo='NP' "
                              + "AND mp.tiponp='OC'";
                       Statement cuantosoc= (Statement) conex.createStatement();
                      ResultSet rscuantosoc = cuantosoc.executeQuery(cuentaoc);
                      int cuentasoc=0;
                      while(rscuantosoc.next()){
                          cuentasoc = rscuantosoc.getInt(1);
                      }
                      if(cuentasoc>0){
                          String insertatit = "INSERT INTO reportevaluacion (codigo,descri,mvalu,mpre)"
                                        + "VALUES ('','OBRAS COMPLEMENTARIAS',"+mvalu+",'"+mpres+"'))";
                                Statement ins = (Statement) conex.createStatement();
                                ins.execute(insertatit);
                                String consultaoc= "INSERT INTO reportevaluacion "
                              + " SELECT mp.numegrup as nro, mp.id as codigo, mp.descri as descri, mp.unidad as unidad,"
                              + " IFNULL((SELECT SUM(cantidad) as cantidades "
                              + " FROM dvalus WHERE numepart=mp.numero AND (mpre_id='"+mpres+"' OR mpre_id IN "
                              + "(SELECT id FROM mpres WHERE mpres_id='"+mpres+"')) AND "
                              + " mvalu_id<"+mvalu+"),0) as cantidadanterior,IFNULL((SELECT SUM(cantidad) as cantidades"
                              + " FROM dvalus WHERE numepart=mp.numero AND (mpre_id='"+mpres+"' OR mpre_id IN "
                              + " (SELECT id FROM mpres WHERE mpres_id='"+mpres+"')) AND "
                              + " mvalu_id<="+mvalu+"),0) as cantidadactual, IF(mp.precasu=0,mp.precunit,mp.precasu) as preciounitario,"
                              + " ROUND(IFNULL((SELECT SUM(cantidad) as cantidad "
                              + " FROM dvalus WHERE numepart=mp.numero AND mpre_id='"+mpres+"' AND "
                              + " mvalu_id<="+mvalu+")*IF(mp.precasu=0,mp.precunit,mp.precasu),0),2) as total,"+mvalu+",'"+mpres+"'"
                              + " FROM mppres as mp, dvalus as dv, mvalus as mv WHERE mp.tipo='NP' AND mp.tiponp='OC' AND"
                              + " mp.numero = dv.numepart AND "
                              + " dv.mvalu_id<=mv.id AND mv.id="+mvalu+" AND"
                              + " (mv.mpre_id = '"+mpres+"' OR mv.mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+mpres+"')) "
                              + " AND (dv.mpre_id='"+mpres+"'"
                              + " OR dv.mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+mpres+"'))"
                              + " AND (mp.mpre_id='"+mpres+"'"
                              + " OR mp.mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+mpres+"'))"
                              + " GROUP BY dv.numepart ORDER BY mp.numegrup";
                                Statement obraoc= (Statement) conex.createStatement();
                                obraoc.execute(consultaoc);
                      }
                      
                      //-----------VARIACIÓN DE PRECIOS----------------------------------------------
                      String cuentavp = "SELECT COUNT(*) FROM mppres as mp, dvalus as dv, mvalus as mv "
                              + "WHERE dv.mvalu_id<=mv.id AND mv.id = "+mvalu+" AND"
                              + " (mp.mpre_id='"+mpres+"' OR mp.mpre_id IN "
                              + "(SELECT id FROM mpres WHERE mpres_id='"+mpres+"')) AND (dv.mpre_id ='"+mpres+"' OR "
                              + "dv.mpre_id IN (SELECT id FROM mpres WHERE mpres_id = '"+mpres+"'))AND mp.numero = dv.numepart"
                              + " AND (mv.mpre_id='"+mpres+"' "
                              + "OR mv.mpre_id IN (SELECT id FROM mpres WHERE mpres_id ='"+mpres+"')) AND mp.tipo='VP'";
                       Statement cuantosvp= (Statement) conex.createStatement();
                      ResultSet rscuantosvp = cuantosvp.executeQuery(cuentavp);
                      int cuentasvp=0;
                      while(rscuantosvp.next()){
                          cuentasvp= rscuantosvp.getInt(1);
                      }
                      if(cuentasvp>0){
                           String deltas="IF((mp.precunit-(SELECT m.precunit FROM "
                    + "mppres as m WHERE (m.mpre_id='"+mpres+"' OR m.mpre_id IN "
                    + " (SELECT id FROM mpres WHERE mpres_id='"+mpres+"')) AND m.numero=mp.mppre_id))<0,0,"
                    + "mp.precunit-(SELECT m.precunit FROM "
                    + "mppres as m WHERE (m.mpre_id='"+mpres+"' OR m.mpre_id IN "
                    + " (SELECT id FROM mpres WHERE mpres_id='"+mpres+"')) AND m.numero=mp.mppre_id))";
                          String insertatit = "INSERT INTO reportevaluacion (codigo,descri,mvalu,mpre)"
                                        + "VALUES ('','VARIACIONES DE PRECIO',"+mvalu+",'"+mpres+"')";
                                Statement ins = (Statement) conex.createStatement();
                                ins.execute(insertatit);
                                String consultavp= "INSERT INTO reportevaluacion "
                              + " SELECT mp.numegrup as nro, mp.id as codigo, mp.descri as descri, mp.unidad as unidad,"
                              + " IFNULL((SELECT SUM(cantidad) as cantidades "
                              + " FROM dvalus WHERE numepart=mp.numero AND (mpre_id='"+mpres+"' OR mpre_id IN "
                              + " (SELECT id FROM mpres WHERE mpres_id='"+mpres+"')) AND "
                              + " mvalu_id<"+mvalu+"),0) as cantidadanterior,IFNULL((SELECT SUM(cantidad) as cantidades"
                              + " FROM dvalus WHERE numepart=mp.numero AND (mpre_id='"+mpres+"' OR mpre_id IN "
                              + " (SELECT id FROM mpres WHERE mpres_id='"+mpres+"')) AND "
                              + " mvalu_id<="+mvalu+"),0) as cantidadactual, "
                                        + ""+deltas+" as preciounitario,"
                              + " ROUND(IFNULL((SELECT SUM(cantidad) as cantidad "
                              + " FROM dvalus WHERE numepart=mp.numero AND mpre_id='"+mpres+"' AND "
                              + " mvalu_id<="+mvalu+")*"+deltas+",0),2) as total,"+mvalu+",'"+mpres+"'"
                              + " FROM mppres as mp, dvalus as dv, mvalus as mv WHERE mp.tipo='VP' AND"
                              + " mp.numero = dv.numepart AND"
                              + " dv.mvalu_id<=mv.id AND mv.id="+mvalu+" AND"
                              + " (mv.mpre_id = '"+mpres+"' OR mv.mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+mpres+"')) "
                              + " AND (dv.mpre_id='"+mpres+"' "
                              + " OR dv.mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+mpres+"'))"
                              + " AND (mp.mpre_id='"+mpres+"' "
                              + " OR mp.mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+mpres+"')) "
                              + " GROUP BY dv.numepart ORDER BY mp.numegrup";
                                System.out.println("consulta vp "+consultavp);
                                Statement obraoc= (Statement) conex.createStatement();
                                obraoc.execute(consultavp);
                      }
                } catch (SQLException ex) {
                    Logger.getLogger(reporte.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                          
            } catch (FileNotFoundException ex) {
                JOptionPane.showMessageDialog(null, "No se encuentra el archivo del reporte "+ex.getMessage());
                Logger.getLogger(reporte.class.getName()).log(Level.SEVERE, null, ex);
            }
            JasperDesign design = JRXmlLoader.load(input); 
                JasperReport report = JasperCompileManager.compileReport(design);
               
                denumeroaletra1 nume = new denumeroaletra1();
              String letras="";
              int decimalPlaces = 2; 
              BigDecimal bd = new BigDecimal(mvalu); 
              bd = bd.setScale(decimalPlaces, BigDecimal.ROUND_HALF_EVEN); 
              String titulo = jTextField1.getText();
            
              letras="("+nume.Convertir(String.valueOf(mvalu), true)+")";
              if(!tipovalu.equals("VALUACIÓN UNICA")){
                  tipovalu = tipovalu+" "+letras;
              }
              
              parameters.put("mpres", mpres);
              parameters.put("mvalu", mvalu);
              parameters.put("tmvalu", tipovalu);
              parameters.put("enletra", letras);
              parameters.put("titulo", titulo);
              parameters.put("fecha", fecha);
              print = JasperFillManager.fillReport(report, parameters, conex);
           
            FileOutputStream output=null;
            String auxruta=ruta;
            
            if(jCheckBox1.isSelected()){
                ruta= ruta+".pdf";
                try {
                output = new FileOutputStream(new File(ruta));
            } catch (FileNotFoundException ex) {
                JOptionPane.showMessageDialog(null, "No se genero el reporte en pdf "+ex.getMessage());
                Logger.getLogger(reporte.class.getName()).log(Level.SEVERE, null, ex);
            }
                JasperExportManager.exportReportToPdfStream(print, output);
                
            }
            if(jCheckBox2.isSelected()){
                 ruta= auxruta+".xls";
                  ByteArrayOutputStream outputByteArray = new ByteArrayOutputStream();
                try {
                    
                output = new FileOutputStream(new File(ruta));
            } catch (FileNotFoundException ex) {
                JOptionPane.showMessageDialog(null, "No se genero el reporte en excel "+ex.getMessage());
                Logger.getLogger(reportepresupuesto.class.getName()).log(Level.SEVERE, null, ex);
            }
                 JRXlsExporter exporterXLS = new JRXlsExporter();
            exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT, print);
         exporterXLS.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, outputByteArray);
         exporterXLS.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
         exporterXLS.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
         exporterXLS.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
         exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
         exporterXLS.exportReport();
                try {
                    output.write(outputByteArray.toByteArray());
                } catch (IOException ex) {
                    Logger.getLogger(reporte.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            JasperViewer.viewReport(print, false);
        } catch (JRException ex) {
            Logger.getLogger(reporte.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /** @return the return status of this dialog - one of RET_OK or RET_CANCEL */
    public int getReturnStatus() {
        return returnStatus;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jCheckBox3 = new javax.swing.JCheckBox();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        okButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/selecc.fw.png"))); // NOI18N
        okButton.setToolTipText("Generar Reporte");
        okButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                okButtonMouseClicked(evt);
            }
        });
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        cancelButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/eliminar.png"))); // NOI18N
        cancelButton.setToolTipText("Cancelar");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        jPanel3.setBackground(new java.awt.Color(91, 91, 95));

        jLabel1.setBackground(new java.awt.Color(91, 91, 95));
        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Reporte Valuación");
        jLabel1.setOpaque(true);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Datos para Reporte", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        jTextField1.setText("VALUACIÓN");
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jTextField2.setEditable(false);
        jTextField2.setToolTipText("Seleccione Ruta");

        jButton1.setText("...");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jCheckBox1.setText("PDF");

        jCheckBox2.setText("Excel");

        jLabel2.setText("Ruta:");

        jLabel3.setText("Titulo:");

        jLabel4.setText("Fecha:");

        jCheckBox3.setText("Con fecha");
        jCheckBox3.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jCheckBox3StateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(39, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jCheckBox1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBox2))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBox3))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jTextField2, javax.swing.GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1))
                    .addComponent(jTextField1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jCheckBox1)
                            .addComponent(jCheckBox2))
                        .addGap(8, 8, 8)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCheckBox3)
                            .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(294, Short.MAX_VALUE)
                .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(52, 52, 52))
        );

        getRootPane().setDefaultButton(okButton);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        
    }//GEN-LAST:event_okButtonActionPerformed
    
    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        doClose(RET_CANCEL);
    }//GEN-LAST:event_cancelButtonActionPerformed

    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        doClose(RET_CANCEL);
    }//GEN-LAST:event_closeDialog

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        int seleccion = fileChooser.showSaveDialog(jTextField1);
        
        File fichero = fileChooser.getSelectedFile();
        jTextField2.setText(fichero.getPath().toString());
        ruta = jTextField2.getText().toString();
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jCheckBox3StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jCheckBox3StateChanged
        if(jCheckBox3.isSelected()){
            jDateChooser1.setEnabled(true);
            fecha = format.format(jDateChooser1.getDate());
        }else{
            jDateChooser1.setEnabled(false);
              fecha="";
        }
        
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox3StateChanged

    private void okButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_okButtonMouseClicked
        generarreporte();
        this.dispose();
        // TODO add your handling code here:
    }//GEN-LAST:event_okButtonMouseClicked

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed
    
    private void doClose(int retStatus) {
        returnStatus = retStatus;
        setVisible(false);
        dispose();
    }

    /**
     * @param args the command line arguments
     */
   
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton jButton1;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox3;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JButton okButton;
    // End of variables declaration//GEN-END:variables
    private int returnStatus = RET_CANCEL;
}
