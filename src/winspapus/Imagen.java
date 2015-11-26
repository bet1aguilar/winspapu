/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package winspapus;


import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JDesktopPane;
import javax.swing.border.Border;
/**
 * @web http://jc-mouse.blogspot.com/
 * @author Mouse
 */
public class Imagen implements Border {    
    BufferedImage fondo;
    JDesktopPane panel;
    public Imagen(JDesktopPane jpanel){    
        try {       
            //se obtiene la imagen   
            this.panel = jpanel;
            URL url = new URL(getClass().getResource("/winspapus/imagenes/spapufondo.jpg").toString());
            fondo = ImageIO.read(url);  
            
            
        } catch (IOException ex) {
            Logger.getLogger(Imagen.class.getName()).log(Level.SEVERE, null, ex);
        }      
    }
    // se sobreescriben metodos propios de Border
    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height){     
        //se dibuja la imagen de fondo en el centro del contenedor
        //cada que se redimensione el contenedor, la imagen automaticamente se posiciona en el centro
        g.drawImage(fondo, 0,0, panel.getWidth(), panel.getHeight(), null);
    }

    @Override
    public Insets getBorderInsets(Component c){
    return new Insets(0,0,0,0);
    }

    @Override
    public boolean isBorderOpaque(){
    return true;
    }
}