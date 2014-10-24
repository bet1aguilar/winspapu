/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package reportes;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author spapu1
 */
public class presupuestogeneral {
    
    
    
    public presupuestogeneral(){
        try {
            generarpdf();
        } catch (DocumentException ex) {
            Logger.getLogger(presupuestogeneral.class.getName()).log(Level.SEVERE, null, ex);
        }
     }
    public final void generarpdf() throws DocumentException{
        FileOutputStream archivo = null;
        try {
            archivo = new FileOutputStream("hola.pdf");
            Document documento = new Document();
            PdfWriter.getInstance(documento, archivo);
            documento.open();
            documento.add(new Paragraph("Hola Mundo!"));
            documento.add(new Paragraph("SoloInformaticaYAlgoMas.blogspot.com"));
            documento.close();
            PdfReader reader;
            try {
                reader = new PdfReader("hola.pdf");
            } catch (IOException ex) {
                Logger.getLogger(presupuestogeneral.class.getName()).log(Level.SEVERE, null, ex);
            }
           File file = new File("hola.pdf");
            try {
                Desktop.getDesktop().open(file);
                file.deleteOnExit();
            } catch (IOException ex) {
                Logger.getLogger(presupuestogeneral.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(presupuestogeneral.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                archivo.close();
            } catch (IOException ex) {
                Logger.getLogger(presupuestogeneral.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
