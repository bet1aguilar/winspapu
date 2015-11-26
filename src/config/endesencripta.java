/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package config;

import java.sql.Connection;
import com.mysql.jdbc.Statement;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JOptionPane;

/**
 *
 * @author Betmart
 */
public class endesencripta {
    private Connection conex=null;
    public endesencripta(Connection conex)
    {
        this.conex=conex;
    }
    public endesencripta()
    {
    }
    public String getClaveBD(){
        String clave=decodifica(leearchivo());        
        return clave;
    }
    private String consulta(String select){
        String result="";
        try {
            Statement st = (Statement) conex.createStatement();
            ResultSet rst = st.executeQuery(select);
            while(rst.next()){
                result=rst.getString(1);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al consultar claves en la BD");
            Logger.getLogger(endesencripta.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    public String getClaveServer(){
        String clave="";
        String select = "SELECT claveServer FROM configinterna";
        clave = decodifica(consulta(select));
        return clave;
    }
    public String getClaveMatriz(){
        String clave="";
        String select = "SELECT claveMatriz FROM configinterna";
        clave = decodifica(consulta(select));
        return clave;
    }
    public String getClaveOrganismo(){
        String clave="";
        String select = "SELECT claveOrganismos FROM configinterna";
        clave = decodifica(consulta(select));
        return clave;
    }
    public String getClaveAccess(){
        String clave="";
        String select = "SELECT claveAccess FROM configinterna";
        clave = decodifica(consulta(select));
        return clave;
    }
    
    
    private String decodifica(String adeco){
        String deco="";
        

        String secretKey = "betmartJavierRonald"; //llave para desenciptar datos
        try {
            byte[] message = Base64.decode(adeco);
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digestOfPassword = md.digest(secretKey.getBytes("utf-8"));
            byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
            SecretKey key = new SecretKeySpec(keyBytes, "DESede");
            Cipher decipher = Cipher.getInstance("DESede");
            decipher.init(Cipher.DECRYPT_MODE, key);

            byte[] plainText = decipher.doFinal(message);

            deco = new String(plainText, "UTF-8");

        } catch (Exception ex) {
        }
          
        return deco;
    }
    private String leearchivo(){
         FileInputStream fis = null;
        DataInputStream entrada = null;
        String n="";
        try {
            fis = new FileInputStream("ini.spapu");
            entrada = new DataInputStream(fis);             
            n = entrada.readLine();  //se lee  conjunto de Bytes del fichero        
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (EOFException e) {
            System.out.println("Fin de fichero "+e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                if (entrada != null) {
                    entrada.close();
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        return n;
    }
}
