/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package reportes;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEvent;
import com.itextpdf.text.pdf.PdfWriter;
import com.mysql.jdbc.Blob;
import java.sql.Connection;
import com.mysql.jdbc.Statement;
import java.io.IOException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author Betmart
 */
public class cabecera implements PdfPageEvent{
    protected Phrase header;
    protected PdfPTable footer;
    String mpres;
    private Connection conex;
     SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
     int dias, semanas, meses, ayos;
   String consulta; 
    PdfPTable tabla = new PdfPTable(3);
   String encabezado;
   String nombre, nrocontrato, ubicacion, nombrecontratista;
   Image contratista,propietario;
   Document document;
    PdfPCell celda1;
           ImageIcon imagen;
    Date fecha;       
           
    public cabecera(Connection conex, String mpres, Document document, Date fecha){
    this.mpres=mpres;
    this.conex=conex;
    this.document=document;
    this.fecha=fecha;
    consulta();
    consultaprop();
    consultadatos();
    lapsopiedepagina();
    header = new Paragraph(encabezado);
    
    
    }
    
    public final void lapsopiedepagina()
    {
        String fecini = "SELECT fechaini FROM mppres WHERE (mpre_id='"+mpres+"' OR mpre_id IN "
                + "(SELECT id FROM mpres WHERE mpres_id = '"+mpres+"')) AND cron=1 ORDER BY fechaini LIMIT 1";
        String fecfin = "SELECT fechafin FROM mppres WHERE (mpre_id='"+mpres+"' OR mpre_id IN "
                + "(SELECT id FROM mpres WHERE mpres_id = '"+mpres+"')) AND cron=1 ORDER BY fechafin DESC LIMIT 1";
        Date fechaini = null,fechafin = null;
        long diferencia;
        try {
            Statement sini = (Statement) conex.createStatement();
            ResultSet rsini = sini.executeQuery(fecini);
            rsini.first();
            fechaini=rsini.getDate(1);
            Statement sfin = (Statement) conex.createStatement();
            ResultSet rsfin = sfin.executeQuery(fecfin);
            rsfin.first();
            fechafin=rsfin.getDate(1);
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al consultar las fechas de la partida");
            Logger.getLogger(cabecera.class.getName()).log(Level.SEVERE, null, ex);
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(fechaini);
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(fechafin);
        semanas = cal1.get(Calendar.WEEK_OF_YEAR)-cal.get(Calendar.WEEK_OF_YEAR)+1;
        dias=cal1.get(Calendar.DAY_OF_YEAR)-cal.get(Calendar.DAY_OF_YEAR);
        meses=semanas/4;
        ayos =meses/12;
    }
    public final void consultadatos(){
        String consultar = "SELECT nombre, ubicac, nrocon FROM mpres WHERE id='"+mpres+"'";
        try {
            Statement stdatos = (Statement) conex.createStatement();
            ResultSet rstdatos = stdatos.executeQuery(consultar);
            while(rstdatos.next()){
                nombre = rstdatos.getString(1);
                ubicacion = rstdatos.getString(2);
                nrocontrato = rstdatos.getString(3);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al consultar datos de presupuesto");
            Logger.getLogger(cabecera.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public final void consulta(){
        Blob logo = null;
       
         ImageIcon icon;
        byte [] leidos;
         consulta = "SELECT IFNULL(encabe,''), logo, mc.nombre FROM mconts as mc, mpres as mp WHERE "
           + "mp.codcon = mc.id AND (mp.id = '"+mpres+"' OR mp.id IN (SELECT id FROM mpres WHERE mpres_id='"+mpres+"'))";
         
        try {
            Statement st = (Statement) conex.createStatement();
            ResultSet rst = st.executeQuery(consulta);
            while(rst.next()){
                encabezado = rst.getString(1);
                celda1=new PdfPCell(new Paragraph(encabezado,FontFactory.getFont(BaseFont.TIMES_ROMAN,10,Font.BOLD)));
                tabla.addCell(celda1);
                nombrecontratista = rst.getString(3);
                logo = (Blob) rst.getBlob(2);
            }
            if(logo!=null){
               leidos=logo.getBytes(1, (int) logo.length());
               imagen = new ImageIcon(leidos);
               int altura = (149*imagen.getIconHeight())/imagen.getIconWidth();
               int ancho = (66*imagen.getIconWidth())/imagen.getIconHeight();
                icon = new ImageIcon(imagen.getImage().getScaledInstance(ancho, 66, java.awt.Image.SCALE_SMOOTH));
                if(icon!=null){
                try {
                    contratista =  Image.getInstance(icon.getImage(),null);
                } catch (BadElementException ex) {
                    JOptionPane.showMessageDialog(null, "Error al consultar la contratista "+ex.getMessage());
                    Logger.getLogger(cabecera.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error al consultar la contratista "+ex.getMessage());
                    Logger.getLogger(cabecera.class.getName()).log(Level.SEVERE, null, ex);
                }
                contratista.setAbsolutePosition(10, 10);
                //contratista.scalePercent(50);
                }
             //  tabla.addCell(contratista);
               tabla.setTotalWidth(350f);     
               
            }
                    
        } catch (SQLException ex) {
            Logger.getLogger(cabecera.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public final void consultaprop(){
        Blob logo = null;
       ImageIcon icon;
        Rectangle rect = new Rectangle(149, 66);
        byte [] leidos;
         consulta = "SELECT logo FROM mprops as mc, mpres as mp WHERE "
           + "mp.codpro = mc.id AND (mp.id = '"+mpres+"' OR mp.id IN (SELECT id FROM mpres WHERE mpres_id='"+mpres+"'))";
         
        try {
            Statement st = (Statement) conex.createStatement();
            ResultSet rst = st.executeQuery(consulta);
            while(rst.next()){
               
                
                logo = (Blob) rst.getBlob(1);
            }
            if(logo!=null){
               leidos=logo.getBytes(1, (int) logo.length());
               imagen = new ImageIcon(leidos);
               int altura = (149*imagen.getIconHeight())/imagen.getIconWidth();
               int ancho = (66*imagen.getIconWidth())/imagen.getIconHeight();
               
               icon = new ImageIcon(imagen.getImage().getScaledInstance(ancho, 66, java.awt.Image.SCALE_DEFAULT));
               if(icon!=null){
                try {
                   propietario =  Image.getInstance(icon.getImage(),null);
                } catch (BadElementException ex) {
                    JOptionPane.showMessageDialog(null, "Error al consultar propietario "+ex.getMessage());
                    Logger.getLogger(cabecera.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error al consultar propietario "+ex.getMessage());
                    Logger.getLogger(cabecera.class.getName()).log(Level.SEVERE, null, ex);
                }
               propietario.setAbsolutePosition(10, 10);
               }
               //propietario.scalePercent(50);
                
             //  tabla.addCell(contratista);
           
               
            }
                    
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al consultar propietario "+ex.getMessage());
            Logger.getLogger(cabecera.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @Override
        public void onEndPage(PdfWriter writer, Document document) {
       
         footer = new PdfPTable(3);
    footer.setTotalWidth(document.getPageSize().getWidth()-(document.leftMargin()+document.rightMargin()));
    footer.getDefaultCell()
    .setHorizontalAlignment(Element.ALIGN_LEFT);
    PdfPCell celda = new PdfPCell(new Phrase("Lapso", FontFactory.getFont(BaseFont.TIMES_ROMAN, 10, Font.BOLD)));
    
    celda.setColspan(3);
    celda.setPadding(5);
    celda.setHorizontalAlignment(Element.ALIGN_CENTER);
    footer.addCell(celda);
    
    celda = new PdfPCell(new Phrase("Días: "+String.valueOf(dias), FontFactory.getFont(BaseFont.TIMES_ROMAN, 10, Font.BOLD)));
   celda.setPadding(5);
    footer.addCell(celda);
    
    celda = new PdfPCell(new Phrase("Semanas: "+String.valueOf(semanas), FontFactory.getFont(BaseFont.TIMES_ROMAN, 10, Font.BOLD)));
  celda.setPadding(5);
    footer.addCell(celda);
    
    celda = new PdfPCell(new Phrase("Meses: "+String.valueOf(meses), FontFactory.getFont(BaseFont.TIMES_ROMAN, 10, Font.BOLD)));
celda.setPadding(5);
    footer.addCell(celda);
    
    celda = new PdfPCell(new Phrase("Contratista: ", FontFactory.getFont(BaseFont.TIMES_ROMAN, 10, Font.BOLD)));

    celda.setPaddingBottom(20);
    footer.addCell(celda);
    
  
     celda = new PdfPCell(new Phrase("Ing. Residente: ", FontFactory.getFont(BaseFont.TIMES_ROMAN, 10, Font.BOLD)));
   
    celda.setPaddingBottom(20);
    footer.addCell(celda);
    
     celda = new PdfPCell(new Phrase("Ing. Inspector: ", FontFactory.getFont(BaseFont.TIMES_ROMAN, 10, Font.BOLD)));

    celda.setPaddingBottom(20);
    footer.addCell(celda);
    
           
              footer.writeSelectedRows(0, -1, document.left(), document.bottom()-10, writer.getDirectContent());
  

            
    }

    @Override
    public void onOpenDocument(PdfWriter writer, Document dcmnt) {
     
    }

    @Override
    public void onStartPage(PdfWriter writer, Document dcmnt) {
          PdfPTable table = new PdfPTable(3);
            try {
                table.setWidths(new int[]{10,20,10});
                table.setTotalWidth(document.getPageSize().getWidth()-(document.leftMargin()+document.rightMargin()+60));
                //table.setHorizontalAlignment(Element.ALIGN_CENTER);
                
                
           
                table.getDefaultCell().setBorder(0);
                PdfPCell cell=null;
                if(contratista!=null)
                cell = new PdfPCell(Image.getInstance(contratista));
                else
                    cell = new PdfPCell(new Phrase(Chunk.NEWLINE));
                cell.setBorder(0);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(cell);
                
                
                celda1.setBorder(0);
                celda1.setHorizontalAlignment(Element.ALIGN_CENTER);                
                table.addCell(celda1);
                table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
               
                PdfPCell celda = null ;
                if(propietario!=null)
                    celda = new PdfPCell(Image.getInstance(propietario));
                else
                     celda = new PdfPCell(new Phrase(Chunk.NEWLINE));
                
                celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
                celda.setBorder(0);
                table.addCell(celda);
                Phrase obra = new Phrase("Obra: "+nombre,FontFactory.getFont(BaseFont.TIMES_ROMAN,10, Font.BOLD));
                
                PdfPCell datos = new PdfPCell(obra);
                datos.setColspan(2);
                datos.setBorder(0);
                datos.setPadding(5);
                datos.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(datos);
                
                Phrase fechatransformada = new Phrase(format.format(fecha),FontFactory.getFont(BaseFont.TIMES_ROMAN,10, Font.BOLD));
                PdfPCell fechas = new PdfPCell(fechatransformada);
                fechas.setPadding(5);
                fechas.setHorizontalAlignment(Element.ALIGN_RIGHT);
                fechas.setBorder(0);
                table.addCell(fechas);
                
                Phrase ubicaciones = new Phrase("Ubicación: "+ubicacion, FontFactory.getFont(BaseFont.TIMES_ROMAN,10, Font.BOLD));
                PdfPCell ubica = new PdfPCell(ubicaciones);
                ubica.setColspan(3);
                ubica.setPadding(5);
                ubica.setHorizontalAlignment(Element.ALIGN_LEFT);
                ubica.setBorder(0);
                
                table.addCell(ubica);
                
                Phrase nombrecontra = new Phrase("Contratista "+nombrecontratista,FontFactory.getFont(BaseFont.TIMES_ROMAN,10, Font.BOLD));
                PdfPCell contra = new PdfPCell(nombrecontra);
                contra.setColspan(2);
                contra.setPadding(5);
                contra.setBorder(0);
                contra.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(contra);
                
                
                Phrase contrato = new Phrase("Nro. Contrato: "+nrocontrato,FontFactory.getFont(BaseFont.TIMES_ROMAN,10, Font.BOLD) );
                PdfPCell contras = new PdfPCell(contrato);
                contras.setBorder(0);
                contras.setPadding(5);
                contras.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(contras);
                
                table.writeSelectedRows(0, -1, document.left()+20
      , document.top() + 130, writer.getDirectContent());
            }
            catch(DocumentException de) {
                JOptionPane.showMessageDialog(null, "Error al construir encabezado "+de.getMessage());
                throw new ExceptionConverter(de);
            }
    }

    @Override
    public void onCloseDocument(PdfWriter writer, Document dcmnt) {
      
    }

    @Override
    public void onParagraph(PdfWriter writer, Document dcmnt, float f) {
       
    }

    @Override
    public void onParagraphEnd(PdfWriter writer, Document dcmnt, float f) {
       
    }

    @Override
    public void onChapter(PdfWriter writer, Document dcmnt, float f, Paragraph prgrph) {
      
    }

    @Override
    public void onChapterEnd(PdfWriter writer, Document dcmnt, float f) {
    
    }

    @Override
    public void onSection(PdfWriter writer, Document dcmnt, float f, int i, Paragraph prgrph) {

    }

    @Override
    public void onSectionEnd(PdfWriter writer, Document dcmnt, float f) {
   
    }

    @Override
    public void onGenericTag(PdfWriter writer, Document dcmnt, Rectangle rctngl, String string) {

    }
      
}
