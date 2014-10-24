/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * copiarapu.java
 *
 * Created on 26/11/2012, 12:25:44 PM
 */
package winspapus.partidas;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
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
import winspapus.Principal;

/**
 *
 * @author Betmart
 */
public class copiarapu extends javax.swing.JDialog {

    /** A return status code - returned if Cancel button has been pressed */
    public static final int RET_CANCEL = 0;
    /** A return status code - returned if OK button has been pressed */
    public static final int RET_OK = 1;
    private Principal obj;
    private Connection conex;
    String realnumero, realnumero2;
    String tabu, partida, acopiar;
    /** Creates new form copiarapu */
    public copiarapu(java.awt.Frame parent, boolean modal, Principal obj, String numpartida, String tabu, Connection conex) {
        super(parent, modal);
        this.obj = obj;
        this.tabu = tabu;
        this.conex = conex;
        this.partida = numpartida;
        
        initComponents();
        jLabel3.setVisible(false);
        jLabel4.setVisible(false);
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

    public int getReturnStatus() {
        return returnStatus;
    }

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        cancelButton = new javax.swing.JButton();
        okButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jtextfield1 = new javax.swing.JFormattedTextField();

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

        okButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/copia.png"))); // NOI18N
        okButton.setToolTipText("Aceptar");
        okButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                okButtonMouseClicked(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(100, 100, 100));

        jLabel1.setBackground(new java.awt.Color(91, 91, 95));
        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Copiar APU");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 286, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jLabel2.setText("Seleccione el número de la Partida a Copiar el APU:");

        jLabel3.setForeground(new java.awt.Color(255, 0, 0));
        jLabel3.setText("*");

        jLabel4.setForeground(new java.awt.Color(255, 0, 0));
        jLabel4.setText("Campo no puede estar vacio");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addContainerGap(32, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jtextfield1, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addContainerGap(38, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(88, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4)
                    .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jtextfield1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addGap(24, 24, 24)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
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
    
    
     private void insertamat() throws SQLException{
             Statement s = (Statement) conex.createStatement();
             Statement st = (Statement) conex.createStatement();
             String sql, cantidad, codicove;
      
        ResultSet rsmat = st.executeQuery("SELECT  mptab_id, mmtab_id, cantidad, precio FROM dmtabs WHERE mtabus_id = '"+tabu+"' AND numepart="+realnumero2);
        String mmtab_id, precio;
             
             while (rsmat.next()) {
                  mmtab_id =rsmat.getObject(2).toString();
                  cantidad = rsmat.getObject(3).toString();
                  precio = rsmat.getObject(4).toString();
                  codicove = rsmat.getObject(1).toString();
                  sql = "INSERT INTO dmtabs VALUES ('"+tabu+"', '"+codicove+"', " + 

                                                        "'"+mmtab_id+"'," + 

                                                        ""+realnumero+", " + 

                                                        ""+cantidad+", " +

                                                        ""+precio+", " + 
                                                                                                                           
                                                        "1);";
        try {
                s.execute(sql);
            } catch (SQLException ex) {
                Logger.getLogger(copiarapu.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
        }
     
     
      private void insertaequip() throws SQLException{
             Statement stmt = (Statement) conex.createStatement();
          Statement s = (Statement) conex.createStatement();
          String sql;
        ResultSet rse = s.executeQuery("SELECT metab_id, cantidad, precio, mptab_id FROM deptabs WHERE mtabus_id = '"+tabu+"' AND numero="+realnumero2);
        String metab_id, precio, codicove;
        String cantidad;
             while (rse.next()) {
                  metab_id =rse.getObject(1).toString();
                  cantidad = rse.getObject(2).toString();
                  precio = rse.getObject(3).toString();
                  codicove = rse.getObject(4).toString();
                  sql = "INSERT INTO deptabs VALUES ('"+tabu+"', '"+codicove+"', " + 

                                                        "'"+metab_id+"'," + 

                                                        ""+realnumero+", " + 

                                                        ""+cantidad+", " +

                                                        ""+precio+", " + 
                                                                                                                           
                                                        "1);";
        try {
                stmt.execute(sql);
            } catch (SQLException ex) {
                Logger.getLogger(copiarapu.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    }
     
        private void insertamano() throws SQLException{
           Statement s = (Statement) conex.createStatement();
           Statement st = (Statement) conex.createStatement();
        ResultSet rsma = st.executeQuery("SELECT mmotab_id, cantidad, bono, salario, subsidi, mptab_id FROM dmoptabs WHERE mtabus_id = '"+tabu+"' AND numero="+realnumero2);
        String mmoptab_id, bono, salario, subsidi, codicove;
             String sql, cantidad;
             while (rsma.next()) {
                  mmoptab_id =rsma.getObject(1).toString();
                  cantidad = rsma.getObject(2).toString();
                  bono = rsma.getObject(3).toString();
                  salario = rsma.getObject(4).toString();
                  subsidi = rsma.getObject(5).toString();
                  codicove = rsma.getObject(6).toString();

                  sql = "INSERT INTO dmoptabs VALUES ('"+tabu+"', " + 

                                                        "'"+mmoptab_id+"'," + 

                                                        ""+realnumero+", " + 

                                                        ""+cantidad+", " +

                                                        ""+bono+", " +
                                                        
                                                        ""+salario+", " +
                                                        
                                                        ""+subsidi+", " +
                          
                                                        "1,'"+codicove+"');";
        try {
                s.execute(sql);
            } catch (SQLException ex) {
                Logger.getLogger(copiarapu.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
             rsma.close();
    }    
    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        doClose(RET_CANCEL);
    }//GEN-LAST:event_cancelButtonActionPerformed

    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        doClose(RET_CANCEL);
    }//GEN-LAST:event_closeDialog

    private void okButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_okButtonMouseClicked
        acopiar = jtextfield1.getText().toString();
        int cont = 0, enc =0;
        if(acopiar.equals("")){
            enc=0;
            jLabel3.setVisible(true);
            jLabel4.setVisible(true);
        }else{
            jLabel3.setVisible(false);
            jLabel4.setVisible(false);
        String sql = "SELECT count(numero) FROM mptabs WHERE numegrup="+acopiar;
        try {
            Statement st = (Statement) conex.createStatement();
            ResultSet rst = st.executeQuery(sql);
            
            while(rst.next()){
                cont = Integer.parseInt(rst.getObject(1).toString());
            }
            
            if(cont>0){
                String sqlnum = "Select numero FROM mptabs WHERE numegrup ='"+partida+"'";
                Statement sta = (Statement) conex.createStatement();
                ResultSet rsta = sta.executeQuery(sqlnum);
                
                while(rsta.next()){
                    realnumero = rsta.getObject(1).toString();
                }
                
                sqlnum = "Select numero From mptabs WHERE numegrup ='"+jtextfield1.getText().toString()+"'";
                Statement sta2 = (Statement) conex.createStatement();
                ResultSet rsta2 = sta2.executeQuery(sqlnum);
                while(rsta2.next()){
                    realnumero2 = rsta2.getObject(1).toString();
                }

                sqlnum = "DELETE FROM dmtabs WHERE mtabus_id ='"+tabu+"' AND numepart ="+realnumero;
                Statement str = (Statement) conex.createStatement();
                str.execute(sqlnum);
                sqlnum = "DELETE FROM deptabs WHERE mtabus_id ='"+tabu+"' AND numero ="+realnumero;
                Statement ste = (Statement) conex.createStatement();
                ste.execute(sqlnum);
                sqlnum = "DELETE FROM dmoptabs WHERE mtabus_id ='"+tabu+"' AND numero ="+realnumero;
                Statement stm = (Statement) conex.createStatement();
                stm.execute(sqlnum);
                insertaequip();
                insertamat();
                insertamano();
                
            }else{
                jLabel4.setVisible(true);
                jLabel4.setText("La partida no existe");
                jtextfield1.setText("");
            }
            
            
            JOptionPane.showMessageDialog(rootPane, "Se ha copiado el APU");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(rootPane, "No se ha copiado el APU");
            Logger.getLogger(copiarapu.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        doClose(RET_OK);
        }        // TODO add your handling code here:
    }//GEN-LAST:event_okButtonMouseClicked
    
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
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JFormattedTextField jtextfield1;
    private javax.swing.JButton okButton;
    // End of variables declaration//GEN-END:variables
    private int returnStatus = RET_CANCEL;
}
