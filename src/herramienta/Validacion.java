/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package herramienta;

import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author Betmart
 */
public class Validacion {
    private Connection conex;
    public String value="";
    public Validacion(Connection conex){
     this.conex=conex;   
    }
    public void validaInt(KeyEvent evt)
    {
    char car = evt.getKeyChar();
    
        if ((car<'0' || car>'9')) {            
            evt.consume();
        }
    }
  public void validaFloat(String value, KeyEvent evt)
    {
    char car = evt.getKeyChar();
    int repite = new StringTokenizer(value,".").countTokens() - 1;
        if ((car<'0' || car>'9') && car!='.') {            
            evt.consume();
        }
        if(car=='.'&& repite==1){
             evt.consume();
        }
    }
  public void validaFloatNeg(String value, KeyEvent evt)
    {
    char car = evt.getKeyChar();
    int repite = new StringTokenizer(value,".").countTokens() - 1;
    int negativo = new StringTokenizer(value,"-").countTokens()-1;
        if ((car<'0' || car>'9') && car!='.' && car!='-') {            
            evt.consume();
        }
        if((car=='.' && repite==1 ))
             evt.consume();
        if((car =='-' && negativo==1))
            evt.consume();
    }
  public void validaText(KeyEvent evt){
    Character c = evt.getKeyChar();
        if(Character.isLetter(c)) {
            evt.setKeyChar(Character.toUpperCase(c));
        }
        if(c=='\'' || c==';') //ELIMINAR TODA COMILLA SIMPLE Y ; DE LOS CAMPOS DE TEXTO
            evt.consume();
  }
  public void sizeField(String field, String table, KeyEvent evt, String text){
      int size=0;
      String sql = "SELECT "+field+" FROM "+table+" LIMIT 1";
        try {
            Statement st = conex.createStatement();
            ResultSet rst = st.executeQuery(sql);
            while(rst.next())
            {
                size = rst.getMetaData().getColumnDisplaySize(1);
            }
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al obtener tamaño del campo a editar");
            Logger.getLogger(Validacion.class.getName()).log(Level.SEVERE, null, ex);
        }
      if(size==text.length() && size!=0)
          evt.consume();
  }
  public void focusGained(JTextField field){
      value = field.getText();
      field.setText("");
  }
  public void focusLost(JTextField field)
  {
    if(field.getText().isEmpty())  
    field.setText(value);         
  }
}
