/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package config;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import respaldoData.configuracionBD;

/**
 *
 * @author spapu1
 */
public class BaseDatos {
    private String ip, puerto, dump, usuario, bd;
    private String ruta;
    public boolean isOpenConfig = false;
    private static final String SEPARADOR= ";";
    Toolkit t = Toolkit.getDefaultToolkit();
    
    public BaseDatos(String ip, String puerto, String dump) {
        this.ip = ip;
        this.puerto = puerto;
        this.dump = dump;
    }
    
    public BaseDatos(String ruta, boolean isOpenConfig) {
        this.ruta=ruta;
        this.isOpenConfig=isOpenConfig;
    }
    
    public boolean guardarConfig(String ruta)
    {
        StringBuilder datos = new StringBuilder();
        datos.append(ip).append(SEPARADOR).append(puerto).append(SEPARADOR).append(dump)
                .append(SEPARADOR).append(usuario).append(SEPARADOR).append(bd);
        File file = new File(ruta);
        try {
            PrintWriter print = new PrintWriter(file);
            print.write(datos.toString());
            return true;
        } catch (FileNotFoundException ex) {
            
            Logger.getLogger(BaseDatos.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "No se pudo crear la configuración "+ ex.getMessage());
            return false;
        }
    }
    public boolean obtenerDatos()
    {
        try {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            FileReader file=null;
            String cad=null;
            StringBuilder contenido = new StringBuilder();
            String [] split;
            try {
                file = new FileReader(ruta);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(BaseDatos.class.getName()).log(Level.SEVERE, null, ex);
                
                if(isOpenConfig){
                int op = JOptionPane.showConfirmDialog(null, "Archivo de Configuración de Base de Datos no existe, ¿Desea crearlo?", "Configurar Base de Datos", JOptionPane.YES_NO_OPTION);
                if(op== JOptionPane.YES_OPTION)
                {
                    configuracionBD bd = new configuracionBD(null, true);
                    int x = (screenSize.width/2)-(450/2);
                    int y = (screenSize.height/2)-(350/2);
                    bd.setBounds(x, y, 450, 350);
                    bd.setVisible(true);
                }
                }else
                {
                    JOptionPane.showMessageDialog(null, "Primera Configuración de la Base de Datos");
                }
                return false;
            }
           BufferedReader io = new BufferedReader(file);
           while((cad = io.readLine())!=null){
               contenido.append(cad);
           }
           split = contenido.toString().split(";");
           this.setIp(split[0]);
           this.setPuerto(split[1]);
           this.setDump(split[2]);
           this.setUsuario(split[3]);
           this.setBd(split[4]);
           return true;
        } catch (IOException ex) {
            
            Logger.getLogger(BaseDatos.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
       
    }

    public String getBd() {
        return bd;
    }

    public void setBd(String bd) {
        this.bd = bd;
    }
    
    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
 
    public String getDump() {
        return dump;
    }

    public void setDump(String dump) {
        this.dump = dump;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPuerto() {
        return puerto;
    }

    public void setPuerto(String puerto) {
        this.puerto = puerto;
    }
    
}
