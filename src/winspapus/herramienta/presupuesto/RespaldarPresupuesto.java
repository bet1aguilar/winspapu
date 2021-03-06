/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * RespaldarPresupuesto.java
 *
 * Created on 29/07/2015, 11:32:44 AM
 */
package winspapus.herramienta.presupuesto;

import java.sql.Connection;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

/**
 *
 * @author spapu1
 * Respaldar Presupuesto activo en un archivo sql el cual contiene en la primera linea
 * el código del presupuesto, y en las siguientes un conjunto de REPLACE INTO.
 * 
 */
public class RespaldarPresupuesto extends javax.swing.JDialog {
    private String ruta="", nombreSql=null;
    StringBuffer sqlInsert = new StringBuffer();
    public String mpres1="", mppres="",deppres="", mepres="", dmpres="", mmpres = "", dmoppres ="";
    public String mmopres = "", mvalus="", dvalus = "", admppres="", mconts = "", mprops = "", cmpres = "", rcppres = "";
    public int hilos=14; // Nro de hilos máximos activos para el respaldo
    /** A return status code - returned if Cancel button has been pressed */
    public static final int RET_CANCEL = 0;
    /** A return status code - returned if OK button has been pressed */
    public static final int RET_OK = 1;
    private Connection conex;
    String mpres="";
    /** Creates new form RespaldarPresupuesto */
    public RespaldarPresupuesto(java.awt.Frame parent, boolean modal, Connection conex, String mpres) {
        super(parent, modal);
        initComponents();
        this.conex=conex;
        this.mpres=mpres;
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

        jPanel1 = new javax.swing.JPanel();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        okButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/ok.fw.png"))); // NOI18N
        okButton.setToolTipText("Respaldar");
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

        jLabel1.setBackground(new java.awt.Color(91, 91, 95));
        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Exportar Presupuesto Activo");
        jLabel1.setOpaque(true);

        jLabel2.setText("Seleccione Ruta para Respaldo:");

        jTextField1.setEditable(false);

        jButton1.setText("...");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE))
                    .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jButton1, 0, 0, Short.MAX_VALUE)
                    .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, Short.MAX_VALUE))
                .addGap(18, 18, 18))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cancelButton, okButton});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(81, 81, 81)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jButton1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
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
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void verificaHilos()
    {
        hilos--;
        if(hilos==0)
        {
            sqlInsert.append(mpres1).append(mppres).append(deppres).
                    append(mepres).append(dmpres).append(mmpres).append(dmoppres).
                    append(mmopres).append(mvalus).append(dvalus).append(admppres).
                    append(mconts).append(mprops).append(cmpres).append(rcppres);
            File file = new File(ruta);
            try {
                PrintWriter print = new PrintWriter(file);
                print.write(sqlInsert.toString());
                print.close();
                JOptionPane.showMessageDialog(rootPane, "Se ha realizado el respaldo del presupuesto "+mpres+" "
                        + "en la ruta "+ruta+". Debe tomar este archivo para respaldar "
                        + "el presupuesto en su otro dispositivo." );
                
             doClose(RET_OK);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(RespaldarPresupuesto.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
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
        jTextField1.setText(fichero.getPath().toString());
        ruta = jTextField1.getText().toString();    
    // TODO add your handling code here:
}//GEN-LAST:event_jButton1ActionPerformed

    private void iniciaRespaldo()
    {
        int cont=0;
        mpres1 = mpres+";\n";
        String sql = "SELECT id, nomabr, IFNULL(nombre,'') as nombre, IFNULL(ubicac,'') as ubicac, "
                + "IF(fecini='0000-00-00', NULL, fecini) as fecini, "
                + "IF(fecfin='0000-00-00', NULL, fecfin) as fecfin, "
                + "IF(feccon='0000-00-00',NULL, feccon) as feccon, "
                + "IF(fecimp='0000-00-00', NULL, fecimp) as fecimp, IFNULL(porgam,0) as porgam, IFNULL(porcfi,0) as porcfi, "
                + "IFNULL(porimp,0) as porimp, IFNULL(poripa,0) as poripa, IFNULL(porpre,0) as porpre, "
                + "IFNULL(poruti,0) as poruti, codpro, codcon, "
                + "IFNULL(parpre,'') as parpre, IFNULL(nrocon,'') as nrocon, IFNULL(nroctr,'') as nroctr, "
                + "IF(fecapr='0000-00-00',NULL,fecapr) as fecapr, IFNULL(nrolic,'') as nrolic, "
                + "IFNULL(status,'') as status, IFNULL(mpres_id,'') as mpres_id, "
                + "IFNULL(memo,'') as memo, IFNULL(timemo,'') as timemo, "
                + "fecmemo, seleccionado, IFNULL(partidapres,'') as partidapres, "
                + "fecharegistro, IFNULL(valu,0) as valu"
                + " FROM mpres WHERE id='"+mpres+"' OR mpres_id='"+mpres+"'";
     
        try {
            Statement st1 = (Statement) conex.createStatement();
            ResultSet rst1 = st1.executeQuery(sql);
            
            while(rst1.next()){
                if(cont>0)
                    mpres1=mpres1+",\n";
                if(cont==0)
                   mpres1=mpres1+"REPLACE INTO mpres (id, nomabr, nombre, ubicac, fecini, fecfin, feccon, fecimp, porgam,"
                + "porcfi, porimp, poripa, porpre, poruti, codpro, codcon, parpre, nrocon, nroctr, fecapr, nrolic, status,"
                + "mpres_id, memo, timemo, fecmemo, seleccionado, partidapres, fecharegistro, valu) VALUES \n"; 
                String fecini=rst1.getString("fecini");
                String fecfin = rst1.getString("fecfin"), feccon = rst1.getString("feccon");
                String fecimp = rst1.getString("fecimp") ;
                String fecapr = rst1.getString("fecapr");
                String fecmemo = rst1.getString("fecmemo");
                String fechareg = rst1.getString("fecharegistro");
                mpres1 = mpres1+"('"+rst1.getString("id") +"', '"+rst1.getString("nomabr") +"', "
                        + "'"+rst1.getString("nombre") +"', '"+rst1.getString("ubicac") +"', "
                        + "IF("+fecini+" IS NULL, NULL,'"+fecini+"'), IF("+fecfin+" IS NULL,NULL,'"+fecfin+"'), "
                        + "IF("+feccon+" IS NULL,NULL,'"+feccon+"'),IF("+fecimp+" IS NULL,NULL,'"+fecimp+"'),"
                        + ""+rst1.getString("porgam") +", "+rst1.getString("porcfi") +","
                        + ""+rst1.getString("porimp") +", "+rst1.getString("poripa") +","
                        + ""+rst1.getString("porpre") +", "+rst1.getString("poruti") +","
                        + "'"+rst1.getString("codpro") +"', '"+rst1.getString("codcon") +"',"                        
                        + "'"+rst1.getString("parpre") +"', '"+rst1.getString("nrocon") +"', "
                        + "'"+rst1.getString("nroctr") +"',"
                        + "IF("+fecapr+" IS NULL, NULL, '"+fecapr+"'), '"+rst1.getString("nrolic") +"',"
                        + "'"+rst1.getString("status") +"', '"+rst1.getString("mpres_id") +"',"
                        + "'"+rst1.getString("memo") +"', '"+rst1.getString("timemo") +"',"
                        + "IF("+fecmemo+" IS NULL, NULL,'"+fecmemo+"'), "+rst1.getString("seleccionado") +","
                        + "'"+rst1.getString("partidapres") +"', IF("+fechareg+" IS NULL, NULL, '"+fechareg+"'),"
                        + ""+rst1.getString("valu") +")";
                cont++;
            }
           if(cont>0)
            mpres1=mpres1+";\n";
            cont=0;   
            ThreadMppres tMppres = new ThreadMppres(conex, mpres, this);
            tMppres.start();
            ThreadDeppres tDeppres = new ThreadDeppres(conex, mpres, this);
            tDeppres.start();
            ThreadMepres tMepres = new ThreadMepres(conex, mpres, this);
            tMepres.start();
            ThreadDmpres tDmpres = new ThreadDmpres(conex, mpres, this);
            tDmpres.start();
            ThreadMmpres tMmpres = new ThreadMmpres(conex, mpres, this);
            tMmpres.start();
            ThreadDmoppres tDmoppres = new ThreadDmoppres(conex, mpres, this);
            tDmoppres.start();
            ThreadMmopres tMmopres = new ThreadMmopres(conex, mpres, this);
            tMmopres.start();
            ThreadMvalus tMvalus = new ThreadMvalus(conex, mpres, this);
            tMvalus.start();
            ThreadDvalus tDvalus = new ThreadDvalus(conex, mpres, this);
            tDvalus.start();
            ThreadAdmppres tadmppres = new ThreadAdmppres(conex, mpres, this);
            tadmppres.start();
            ThreadMconts tmconts = new ThreadMconts(conex, mpres, this);
            tmconts.start();
            ThreadMprops tmprops = new ThreadMprops(conex, mpres, this);
            tmprops.start();
            ThreadCmpres tcmpres = new ThreadCmpres(conex, mpres, this);
            tcmpres.start();
            ThreadRcppres trcppres = new ThreadRcppres(conex, mpres, this);
            trcppres.start();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(rootPane, "Error "+ex.getMessage());
            Logger.getLogger(RespaldarPresupuesto.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
private void okButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_okButtonMouseClicked
    nombreSql=mpres+new Date()+".sql";
    iniciaRespaldo();
    
  // TODO add your handling code here:
}//GEN-LAST:event_okButtonMouseClicked
    
    private void doClose(int retStatus) {
        returnStatus = retStatus;
        setVisible(false);
        dispose();
    }

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JButton okButton;
    // End of variables declaration//GEN-END:variables
    private int returnStatus = RET_CANCEL;
}
