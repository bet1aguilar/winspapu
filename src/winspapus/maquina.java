/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * maquina.java
 *
 * Created on 06-oct-2014, 22:23:55
 */
package winspapus;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

/**
 *
 * @author home
 */
public class maquina extends javax.swing.JDialog {

    /** A return status code - returned if Cancel button has been pressed */
    public static final int RET_CANCEL = 0;
    /** A return status code - returned if OK button has been pressed */
    public static final int RET_OK = 1;
Connection conex, conexion;
String idcompra, dd, tm, licencia;
Principal prin;
SimpleDateFormat formatofecha=new SimpleDateFormat("yyyy-MM-dd");
String usuarioid;
String fecha1,fecha2;   
   Date hoy = new Date();
    /** Creates new form maquina */
    public maquina(java.awt.Frame parent, boolean modal, Connection conex, String idcompra, String dd, String tm, String licencia, Principal prin, Connection conexion, String usuarioid) {
        super(parent, modal);
        initComponents();
        this.usuarioid=usuarioid;
        this.prin=prin;
        this.conex=conex;
        this.conexion=conexion;
        this.idcompra=idcompra;
        this.tm = tm;
        this.licencia=licencia;
        this.dd = dd;
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

        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        okButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/selecc.fw.png"))); // NOI18N
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        cancelButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/eliminar.png"))); // NOI18N
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        jLabel1.setText("Nombre:");

        jLabel2.setText("Descripción:");

        jLabel3.setBackground(new java.awt.Color(91, 91, 95));
        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Registro de la Máquina");
        jLabel3.setOpaque(true);

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField1KeyTyped(evt);
            }
        });

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextArea1KeyTyped(evt);
            }
        });
        jScrollPane1.setViewportView(jTextArea1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(277, Short.MAX_VALUE)
                .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 383, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1))
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextField1)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        getRootPane().setDefaultButton(okButton);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        String nombremaquina= jTextField1.getText().toUpperCase();
        String descrimaquina = jTextArea1.getText().toUpperCase();
        String sSistemaOperativo = System.getProperty("os.name");        
        String sql;
        sql="insert into maquinas (nombre,serial,so,descripcion)"
                + " values ('" + nombremaquina + "', '"+dd+"','"+sSistemaOperativo+"','"+descrimaquina+"')";
        String idmaquina = "";
        Statement ma;
        try {
            ma = (Statement) conex.createStatement();
            ma.execute(sql);
            String consultaid = "Select max(id) from maquinas limit 1";
            Statement stid = (Statement) conex.createStatement();
            ResultSet rstid = stid.executeQuery(consultaid);
            while(rstid.next()){
                idmaquina = rstid.getString(1);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(rootPane, "Error al insertar su máquina en el servidor "+ex.getMessage());
            Logger.getLogger(maquina.class.getName()).log(Level.SEVERE, null, ex);
        }
        //sql="insert into instalacion (usuario_id,maquina_id,version_compra";
             String usuario_id="";  
        String consultausuario = "SELECT c.usuario_id FROM compra as c, version_compra as vc "
                + "WHERE vc.compra_id=c.id AND vc.id="+idcompra+"";
        try {
            Statement st = (Statement) conex.createStatement();
            ResultSet rst = st.executeQuery(consultausuario);
            while(rst.next()){
                usuario_id=rst.getString(1);
            }} catch (SQLException ex) {
                JOptionPane.showMessageDialog(rootPane, "Error al consultar el ID del Usuario "+ex.getMessage());
                Logger.getLogger(maquina.class.getName()).log(Level.SEVERE, null, ex);
            }
                String insertinst="INSERT INTO instalacion (usuario_id, maquinas_id, version_compra_id) "
                        + "VALUES ('"+usuario_id+"','"+idmaquina+"','"+idcompra+"')";
        try {
            Statement stinsert = (Statement) conex.createStatement();
            stinsert.execute(insertinst);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(rootPane, "Error al Insertar la instalación "+ex.getMessage());
            Logger.getLogger(maquina.class.getName()).log(Level.SEVERE, null, ex);
        }
               fecha1=formatofecha.format(hoy);    
            fecha2=formatofecha.format(hoy);    
                
                String sqlusuario = "SELECT *, IFNULL(tipo_organismo,0) FROM usuario WHERE id="+usuarioid;
                Statement stusuario = null;
        try {
            stusuario = (Statement) conex.createStatement();
             ResultSet rstusuario = null;
       
            rstusuario = stusuario.executeQuery(sqlusuario);
       
                String nombre = "", representante ="", rif = "", direccion = "", telefono = "";
                int publico=0;
                while(rstusuario.next())
                {
                    nombre=rstusuario.getString("nombre");
                    representante=rstusuario.getString("contacto");
                    rif=rstusuario.getString("rif");
                    direccion = rstusuario.getString("direccion");
                    telefono = rstusuario.getString("telefono");
                    publico = rstusuario.getInt("tipo_organismo");
                    
                }
                    String cuenta = "SELECT COUNT(*) FROM instalacion";
                    
                        Statement stcuenta = (Statement) conexion.createStatement();
                        ResultSet rstcuenta = stcuenta.executeQuery(cuenta);
                        int cuentas=0;
                        while(rstcuenta.next())
                        {
                            cuentas = rstcuenta.getInt(1);
                        }
                        
                        if(cuentas==0){
                            String inserta = "INSERT INTO instalacion (id, propietario, demo, representante, rif, direccion, telefono, publico, bloqueado, licencia)"
                                    + " VALUES ('1','"+nombre+"', '1', '"+representante+"','"+rif+"', '"+direccion+"', '"+telefono+"',"+publico+","+publico+",'"+licencia+"')";
                           Statement st = (Statement) conexion.createStatement();
                           st.execute(inserta);
                        }
                        else{
                             String update = "UPDATE instalacion SET demo='1', propietario='"+nombre+"', rif='"+rif+"'"
                                     + ", direccion='"+direccion+"', telefono='"+telefono+"', publico="+publico+", bloqueado='"+publico+"' WHERE id='1'";
                           Statement st = (Statement) conexion.createStatement();
                           st.execute(update); 
                        }
                        sql="insert into mpresadm (fecha,codigo, tm, licencia,status,activo, comprobacion)"
                     + " values ('"+fecha1+"','"+dd+"','"+tm+"','"+licencia+"','1',0,'"+fecha2+"')";
                System.out.println(sql);
                 Statement esc = (Statement) conexion.createStatement();
                esc.execute(sql); 
                 JOptionPane.showMessageDialog(rootPane, "Instalación registrada Satisfactoriamente");
                 prin.sinst=1; 
                 
              
               } catch (SQLException ex) {
                   doClose(RET_OK);
                    JOptionPane.showMessageDialog(rootPane, "Error al Modificar Instalación local "+ex.getMessage());
            Logger.getLogger(maquina.class.getName()).log(Level.SEVERE, null, ex);
        }
          
        sql="update version_compra set instalacion=1 where id='" + idcompra +"'";
        try {
            Statement vc = (Statement) conex.createStatement();
            vc.execute(sql);
           
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(rootPane, "Error al Modificar Instalación en el servidor "+ex.getMessage());
            Logger.getLogger(Versiones.class.getName()).log(Level.SEVERE, null, ex);
        }
        doClose(RET_OK);
    }//GEN-LAST:event_okButtonActionPerformed
    
    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        doClose(RET_CANCEL);
    }//GEN-LAST:event_cancelButtonActionPerformed

    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        doClose(RET_CANCEL);
    }//GEN-LAST:event_closeDialog

private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_jTextField1ActionPerformed

private void jTextField1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyTyped
Character c = evt.getKeyChar();
                if(Character.isLetter(c)) {
                    evt.setKeyChar(Character.toUpperCase(c));
                }// TODO add your handling code here:
}//GEN-LAST:event_jTextField1KeyTyped

private void jTextArea1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextArea1KeyTyped
Character c = evt.getKeyChar();
                if(Character.isLetter(c)) {
                    evt.setKeyChar(Character.toUpperCase(c));
                }// TODO add your handling code here:
}//GEN-LAST:event_jTextArea1KeyTyped
    
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
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JButton okButton;
    // End of variables declaration//GEN-END:variables
    private int returnStatus = RET_CANCEL;
}
