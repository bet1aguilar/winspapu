/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * nuevo.java
 *
 * Created on 21/09/2012, 12:06:21 AM
 */
package winspapus.manos;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import herramienta.Validacion;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.SpinnerListModel;

/**
 *
 * @author Betmart
 */
public class nuevo extends javax.swing.JDialog {

    /** A return status code - returned if Cancel button has been pressed */
    public static final int RET_CANCEL = 0;
    /** A return status code - returned if OK button has been pressed */
    public static final int RET_OK = 1;
    private Connection conex;
    String mtabu, codimano;
    int edita=0;
    Validacion val;
    public final String tableName = "mmotabs";
    private int nuevo;
    private String primero;
            /** Creates new form nuevo */
    public nuevo(java.awt.Frame parent, boolean modal, Connection conex, String mtabu) {
        super(parent, modal);
        initComponents();
        jLabel10.setVisible(false);
        jLabel9.setVisible(false);
        jLabel21.setVisible(false);
        this.conex = conex;
        val = new Validacion(conex);
        this.mtabu = mtabu;
        nuevo();
        // Close the dialog when Esc is pressed
        String cancelName = "cancel";
        InputMap inputMap = getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), cancelName);
        ActionMap actionMap = getRootPane().getActionMap();
        actionMap.put(cancelName, new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                doClose(RET_CANCEL);
            }
        });
    }
    
    
 public nuevo(java.awt.Frame parent, boolean modal, Connection conex, String mtabu, String codimano) {
        super(parent, modal);
        initComponents();
        jLabel10.setVisible(false);
        jLabel9.setVisible(false);
        jLabel21.setVisible(false);
        this.conex = conex;
        val = new Validacion(conex);
        edita = 1;
        this.mtabu = mtabu;
        this.codimano = codimano;
        definemodelo();
        cargardatos();
        jSpinner1.setValue(this.codimano);
        jTextField1.setVisible(false);
        // Close the dialog when Esc is pressed
        String cancelName = "cancel";
        InputMap inputMap = getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), cancelName);
        ActionMap actionMap = getRootPane().getActionMap();
        actionMap.put(cancelName, new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                doClose(RET_CANCEL);
            }
        });
    }
    /** @return the return status of this dialog - one of RET_OK or RET_CANCEL */
    public int getReturnStatus() {
        return returnStatus;
    }

    public final void definemodelo(){
        String consulta = "SELECT id FROM mmotabs WHERE mtabus_id='"+mtabu+"' AND status=1";
        String [] mano;
        try {
            Statement st = (Statement) conex.createStatement();
            ResultSet rst = st.executeQuery(consulta);
            rst.last();
            mano = new String[rst.getRow()];
            rst.first();
            mano[0]=rst.getString(1);
            int i=1;
            while(rst.next()){
                mano[i]=rst.getString(1);
                i++;
            }
            primero = mano[0];
            if(codimano==null|| codimano.equals("")){
                codimano=primero;
            }
            SpinnerListModel modelo = new SpinnerListModel(mano);
            jSpinner1.setModel(modelo);
        } catch (SQLException ex) {
            Logger.getLogger(nuevo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public final void nuevo(){
     Rectangle rect = new Rectangle();
        jSpinner1.setVisible(false);
        rect.setBounds(jSpinner1.getBounds());
        
        jTextField1.setVisible(true);
        jTextField1.setBounds(rect);
        jTextField1.setText("");
        jTextField3.setText("");
        jTextField4.setText("");
        jTextField8.setText("");
        jTextField2.setText("");
        edita=0;
        nuevo=1;
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
        cancelButton = new javax.swing.JButton();
        okButton = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jSpinner1 = new javax.swing.JSpinner();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        cancelButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/eliminar.png"))); // NOI18N
        cancelButton.setToolTipText("Cancelar");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        okButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/guardar.png"))); // NOI18N
        okButton.setToolTipText("Guardar");
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

        jPanel7.setBackground(new java.awt.Color(100, 100, 100));

        jLabel6.setBackground(new java.awt.Color(91, 91, 95));
        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Agregar Mano de Obra");
        jLabel6.setOpaque(true);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 670, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jTextField1.setToolTipText("Ingrese Código de Equipo");
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField1KeyTyped(evt);
            }
        });

        jLabel7.setText("Código *:");

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 14));
        jLabel9.setForeground(new java.awt.Color(255, 0, 0));
        jLabel9.setText("*");

        jTextField8.setToolTipText("Ingrese Descripción del material");
        jTextField8.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField8KeyTyped(evt);
            }
        });

        jLabel8.setText("Descripción:");

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 14));
        jLabel10.setForeground(new java.awt.Color(255, 0, 0));
        jLabel10.setText("*");

        jLabel21.setForeground(new java.awt.Color(255, 0, 0));
        jLabel21.setText("* Campo no puede ser vacio");

        jLabel11.setText("Bono:");

        jTextField2.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField2.setText("0.00");
        jTextField2.setToolTipText("Bono Compensatorio");
        jTextField2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField2FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField2FocusLost(evt);
            }
        });
        jTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField2KeyTyped(evt);
            }
        });

        jTextField3.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField3.setText("0.00");
        jTextField3.setToolTipText("Salario");
        jTextField3.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField3FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField3FocusLost(evt);
            }
        });
        jTextField3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField3KeyTyped(evt);
            }
        });

        jLabel12.setText("Salario:");

        jTextField4.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField4.setText("0.00");
        jTextField4.setToolTipText("Subsidio");
        jTextField4.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField4FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField4FocusLost(evt);
            }
        });
        jTextField4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField4KeyTyped(evt);
            }
        });

        jLabel13.setText("Subsidio:");

        jSpinner1.setModel(new javax.swing.SpinnerListModel(new String[] {""}));
        jSpinner1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner1StateChanged(evt);
            }
        });
        jSpinner1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jSpinner1KeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(103, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8)
                    .addComponent(jLabel11)
                    .addComponent(jLabel12)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 418, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel10))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(75, 75, 75))
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(233, 233, 233)
                .addComponent(jLabel21)
                .addContainerGap(301, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(479, Short.MAX_VALUE)
                .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(95, 95, 95))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42, 42, 42)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel21)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(113, 113, 113)
                .addComponent(jLabel10)
                .addGap(185, 185, 185))
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
    
    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        doClose(RET_CANCEL);
    }//GEN-LAST:event_closeDialog

    public final void cargardatos(){
        try {
            Statement carga = (Statement) conex.createStatement();
           
            String sql = "SELECT id, descri, bono, salario, subsid FROM mmotabs WHERE mtabus_id='"+mtabu+"' AND id='"+codimano+"'";
             ResultSet rscarga = carga.executeQuery(sql);
             
             while(rscarga.next()){
                 jTextField1.setText(rscarga.getObject(1).toString());
                 jTextField8.setText(rscarga.getObject(2).toString());
                 jTextField2.setText(rscarga.getObject(3).toString());
                 jTextField3.setText(rscarga.getObject(4).toString());
                 jTextField4.setText(rscarga.getObject(5).toString());
             }
            
        } catch (SQLException ex) {
            Logger.getLogger(nuevo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed

        doClose(RET_CANCEL);     }//GEN-LAST:event_cancelButtonActionPerformed

    private void jSpinner1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner1StateChanged
        codimano=jSpinner1.getValue().toString();
        cargardatos();
        
        // TODO add your handling code here:
    }//GEN-LAST:event_jSpinner1StateChanged

    private void okButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_okButtonMouseClicked
       int valida=0;
       String sql="";
       
       if(jTextField1.getText().isEmpty() || jTextField8.getText().isEmpty()){
           valida=1;
       }
       
        if(valida==0){
            jLabel10.setVisible(false);
            jLabel9.setVisible(false);
            jLabel21.setVisible(false);
            try {
                Statement st = (Statement) conex.createStatement();
                if(edita==0){
                sql= "INSERT INTO mmotabs (mtabus_id, id, descri, bono, salario, subsid, status) VALUES ('"+mtabu+"',"
                                                         + "'"+jTextField1.getText().toString()+"',"
                                                         + "'"+jTextField8.getText().toString()+"',"
                                                         + "'"+jTextField2.getText().toString()+"',"
                                                         + ""+jTextField3.getText().toString()+","
                                                         + ""+jTextField4.getText().toString()+","
                                                            + "1)";
                }else{
                    if(edita==1){
                        sql = "UPDATE mmotabs set descri='"+jTextField8.getText().toString()+"', "
                                                               + "bono="+jTextField2.getText().toString()+", "
                                                                + "salario="+jTextField3.getText().toString()+", "
                                                                + "subsid="+jTextField4.getText().toString()+""
                                                                + " WHERE id='"+codimano+"'";
                    }
                }
                    st.execute(sql);
                    JOptionPane.showMessageDialog(null, "Se ha guardado la mano de obra");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "No se ha guardado la mano de obra");
                Logger.getLogger(nuevo.class.getName()).log(Level.SEVERE, null, ex);
            }
            doClose(RET_OK);
        }else{
            jLabel10.setVisible(true);
            jLabel9.setVisible(true);
            jLabel21.setVisible(true);
            
        }        // TODO add your handling code here:
    }//GEN-LAST:event_okButtonMouseClicked

private void jTextField1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyTyped
val.validaText(evt);
val.sizeField("id", tableName, evt, jTextField1.getText());// TODO add your handling code here:
}//GEN-LAST:event_jTextField1KeyTyped

private void jTextField8KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField8KeyTyped
 val.validaText(evt);
val.sizeField("descri", tableName, evt, jTextField8.getText());// TODO add your handling code here:
}//GEN-LAST:event_jTextField8KeyTyped

private void jTextField2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField2FocusGained
    val.focusGained(jTextField2);    // TODO add your handling code here:
}//GEN-LAST:event_jTextField2FocusGained

private void jTextField2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField2FocusLost
val.focusLost(jTextField2);    // TODO add your handling code here:
}//GEN-LAST:event_jTextField2FocusLost

private void jTextField2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyTyped
val.validaFloat(jTextField2.getText(), evt);    // TODO add your handling code here:
}//GEN-LAST:event_jTextField2KeyTyped

private void jTextField3FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField3FocusGained
val.focusGained(jTextField3);    // TODO add your handling code here:
}//GEN-LAST:event_jTextField3FocusGained

private void jTextField3FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField3FocusLost
val.focusLost(jTextField3);    // TODO add your handling code here:
}//GEN-LAST:event_jTextField3FocusLost

private void jTextField3KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField3KeyTyped
val.validaFloat(jTextField3.getText(), evt);    // TODO add your handling code here:
}//GEN-LAST:event_jTextField3KeyTyped

private void jTextField4KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField4KeyTyped
val.validaFloat(jTextField4.getText(), evt);    // TODO add your handling code here:
}//GEN-LAST:event_jTextField4KeyTyped

private void jTextField4FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField4FocusGained
val.focusGained(jTextField4);    // TODO add your handling code here:
}//GEN-LAST:event_jTextField4FocusGained

private void jTextField4FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField4FocusLost
val.focusLost(jTextField4);    // TODO add your handling code here:
}//GEN-LAST:event_jTextField4FocusLost

private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
    // TODO add your handling code here:
}//GEN-LAST:event_okButtonActionPerformed

private void jSpinner1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jSpinner1KeyTyped
val.sizeField("id", tableName, evt, jSpinner1.getValue().toString());
}//GEN-LAST:event_jSpinner1KeyTyped
    
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
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JButton okButton;
    // End of variables declaration//GEN-END:variables
    private int returnStatus = RET_CANCEL;
}
