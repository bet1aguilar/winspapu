/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package reportes;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.mysql.jdbc.Connection;
import com.toedter.calendar.JDateChooser;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.Color;
 

 
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;


public class memoriad extends PdfPageEventHelper{
    String mpres;
    Connection conex;
    private String strNombreDelPDF;
    String strRotuloPDF;
   String sql,memo,timemo,fecmemo,nombrep,nombrec,ubicac;
   byte[] leidos;
   Image im=null;
   Blob bytes=null;
   ImageIcon image=null;
    Color grisClaro = new Color( 230,230,230); 
    Color azulClaro = new Color( 124,195,255 );
    private Font fuenteAzul25= new Font(Font.FontFamily.TIMES_ROMAN,12,Font.BOLD);
    String fecha ;
     SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
    
    public memoriad(String mpres, Connection conex,String fecha){
        try {
            this.mpres=mpres;
            this.conex=conex;
            this.fecha = fecha;
            fuenteAzul25.setColor(BaseColor.BLUE);
            generarpdf();
            
        } catch (DocumentException ex) {
            Logger.getLogger(presupuestogeneral.class.getName()).log(Level.SEVERE, null, ex);
        }
     }
    
    public final void generarpdf() throws DocumentException{
        try {
              FileInputStream input=null;
            try {
                input = new FileInputStream(new File("memoria.jrxml"));
            } catch (FileNotFoundException ex) {
                Logger.getLogger(memoriad.class.getName()).log(Level.SEVERE, null, ex);
            }
           
            JasperDesign design = null; 
            try {
                design = JRXmlLoader.load(input);
            } catch (JRException ex) {
                Logger.getLogger(memoriad.class.getName()).log(Level.SEVERE, null, ex);
            }
            JasperReport report = JasperCompileManager.compileReport(design);
            Map parameters = new HashMap();
            
       
            parameters.put("fecha", fecha);
             parameters.put("mpres", mpres);
            JasperPrint print = JasperFillManager.fillReport(report, parameters, conex);
            FileOutputStream output=null;
            
            JasperViewer.viewReport(print, false);
            
        } catch (JRException ex) {
            JOptionPane.showMessageDialog(null, "No se pudo abrir el archivo "+ex.getMessage());
            Logger.getLogger(memoriad.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
   
    
}
