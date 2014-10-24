/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * copiapres.java
 *
 * Created on 21/03/2014, 07:47:46 PM
 */
package presupuestos;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
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
 * @author Betmart
 */
public class copiapres extends javax.swing.JDialog {

    /** A return status code - returned if Cancel button has been pressed */
    public static final int RET_CANCEL = 0;
    /** A return status code - returned if OK button has been pressed */
    public static final int RET_OK = 1;
    private Connection conex;
    String pres="";
    /** Creates new form copiapres */
    public copiapres(java.awt.Frame parent, boolean modal, Connection conex, String mpres) {
        super(parent, modal);
        initComponents();
        this.conex = conex;
        this.pres = mpres;
        
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
    public void copiarresto(){
         String nombre = jTextField1.getText();
        try {
            //------------COPIA PARTIDAS
            Statement stpart = (Statement) conex.createStatement();
            String partidas = "INSERT INTO mppres "
                    + "SELECT '"+nombre+"',id,numero,numegrup,descri,idband,porcgad,porcpre,porcutil,precasu,precunit,rendimi,"
                    + "unidad, redondeo, status, cantidad, tipo, nropresupuesto, nrocuadro, mppre_id, tiporec, refere, "
                    + "fechaini, fechafin, cron, rango, lapso, capitulo,tiponp FROM mppres WHERE mpre_id= '"+pres+"' OR "
                    + "mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"')";
            stpart.execute(partidas);
            String mepres = "INSERT INTO mepres "
                    + "SELECT '"+nombre+"', id, descri, deprecia, precio, status FROM mepres WHERE mpre_id='"+pres+"'";
            stpart.execute(mepres);
            String deppres = "INSERT INTO deppres"
                    + " SELECT '"+nombre+"', mppre_id, mepre_id, numero, cantidad, precio, status FROM deppres WHERE mpre_id='"+pres+"'";
            Statement otro = (Statement) conex.createStatement();
            otro.execute(deppres);
            String dmpres = "INSERT INTO dmpres SELECT '"+nombre+"', mppre_id, mmpre_id, numepart, cantidad, precio, status FROM "
                    + "dmpres WHERE mpre_id = '"+pres+"'";
            otro.execute(dmpres);
            String mmpres = "INSERT INTO mmpres SELECT '"+nombre+"', id, descri, desperdi, precio, unidad, status FROM mmpres"
                    + " WHERE mpre_id='"+pres+"'";
            Statement otro1 = (Statement) conex.createStatement();
            otro1.execute(mmpres);
            String dmoppres = "INSERT INTO dmoppres SELECT '"+nombre+"', mmopre_id, mppre_id, numero, cantidad, bono, salario, subsidi, status"
                    + " FROM dmoppres WHERE mpre_id = '"+pres+"'";
            Statement sdmppres = (Statement) conex.createStatement();
            sdmppres.execute(dmoppres);
            String mmopres = "INSERT INTO mmopres SELECT '"+nombre+"', id, descri, bono,salario, subsid, status FROM mmopres "
                    + "WHERE mpre_id='"+pres+"'";
            Statement smmopres = (Statement) conex.createStatement();
            smmopres.execute(mmopres);
            String pays = "INSERT INTO pays"
                    + "(aumento, disminucion, mpre_id)"
                    + " SELECT aumento, disminucion, '"+nombre+"' FROM pays WHERE mpre_id='"+pres+"'";
            Statement spays = (Statement) conex.createStatement();
            spays.execute(pays);
            String rcppres = "INSERT INTO rcppres "
                    + "(mppres_id, fechaini, fechafin,mpre_id)"
                    + "SELECT mppres_id, fechaini, fechafin, '"+nombre+"' FROM "
                    + "rcppres WHERE mpre_id='"+pres+"'";
            Statement srcppres = (Statement) conex.createStatement();
            srcppres.execute(rcppres);
            String admppres = "INSERT INTO admppres SELECT payd_id, '"+nombre+"', numepart, mvalu_id, aumento, disminucion"
                    + " FROM admppres WHERE mpre_id = '"+pres+"'";
            Statement sadmppres = (Statement) conex.createStatement();
            sadmppres.execute(admppres);
            String cmpres = "INSERT INTO cmpres "
                    + "(id,descri, cmpres_id, codigo, mpre_id) "
                    + "SELECT id, descri, cmpres_id, codigo,  '"+nombre+"' "
                    + "FROM cmpres WHERE mpre_id='"+pres+"'";
            Statement scmpres = (Statement) conex.createStatement();
            scmpres.execute(cmpres);
            Statement valus = (Statement) conex.createStatement();
            String sql = "INSERT INTO mvalus (desde, hasta, status, mpre_id, tipo, lapso)"
                    + "SELECT desde, hasta, status, '"+nombre+"', tipo, lapso FROM mvalus WHERE mpre_id='"+pres+"'";
            valus.execute(sql);
            Statement dvalus = (Statement) conex.createStatement();
            String sdvalus = "INSERT INTO dvalus SELECT '"+nombre+"', mvalu_id, mppre_id,cantidad,"
                    + " precio, numepart, status, aumento, payd_id"
                    + " FROM dvalus WHERE mpre_id='"+pres+"'";
            dvalus.execute(sdvalus);
            

        } catch (SQLException ex) {
            Logger.getLogger(copiapres.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public final void copiar(){
        String nombre = jTextField1.getText();
        try {
            Statement ste = (Statement) conex.createStatement();
            String sql = "INSERT INTO mpres (id,nomabr,nombre,ubicac,fecini,fecfin,feccon,fecimp,porgam,porcfi,porimp"
                    + ", poripa,porpre,poruti,codpro,codcon,parpre,nrocon,nroctr,fecapr,nrolic,status,mpres_id,memo,"
                    + "timemo,fecmemo,seleccionado,"
                    + "fecharegistro)"
                    + "SELECT '"+nombre+"', nomabr, nombre, ubicac, fecini, fecfin,"
                    + "feccon, fecimp, "
                    + " porgam,porcfi, porimp,poripa,porpre,"
                    + "poruti, codpro,codcon, parpre, nrocon, nroctr, fecapr,"
                    + "nrolic, status, mpres_id, memo, timemo, fecmemo, "
                    + "seleccionado, NOW()"
                    + " FROM mpres WHERE id='"+pres+"'";
            ste.execute(sql);
            copiarresto();
            JOptionPane.showMessageDialog(null, "Se ha copiado el presupuesto");
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "No se ha copiado el presupuesto");
            Logger.getLogger(copiapres.class.getName()).log(Level.SEVERE, null, ex);
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
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        cancelButton = new javax.swing.JButton();
        okButton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        jPanel3.setBackground(new java.awt.Color(100, 100, 100));

        jLabel1.setBackground(new java.awt.Color(91, 91, 95));
        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Copiar Presupuesto");
        jLabel1.setOpaque(true);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 426, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
        );

        cancelButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/eliminar.png"))); // NOI18N
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        okButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/guardar.png"))); // NOI18N
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

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(63, 63, 63)
                .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(okButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cancelButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(13, 13, 13))
        );

        getRootPane().setDefaultButton(okButton);

        jLabel2.setText("Nombre de la Copia de la Obra:");

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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(257, 257, 257)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(46, 46, 46))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
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

    private void okButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_okButtonMouseClicked
  copiar();
  
        
        doClose(RET_OK);        // TODO add your handling code here:
    }//GEN-LAST:event_okButtonMouseClicked

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_okButtonActionPerformed

private void jTextField1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyTyped
   Character c = evt.getKeyChar();
                if(Character.isLetter(c)) {
                    evt.setKeyChar(Character.toUpperCase(c));
                }
                if(jTextField1.getText().length()==20){
                    evt.consume();
                }
}//GEN-LAST:event_jTextField1KeyTyped
    
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
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JButton okButton;
    // End of variables declaration//GEN-END:variables
    private int returnStatus = RET_CANCEL;
}
