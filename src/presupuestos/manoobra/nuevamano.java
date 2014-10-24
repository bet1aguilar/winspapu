/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * nuevosmateriales.java
 *
 * Created on 18/09/2012, 03:22:55 AM
 */
package presupuestos.manoobra;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.StringTokenizer;
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
public class nuevamano extends javax.swing.JDialog {
    private int edita=0;
    private String codmano="";
    /** A return status code - returned if Cancel button has been pressed */
    public static final int RET_CANCEL = 0;
    /** A return status code - returned if OK button has been pressed */
    public static final int RET_OK = 1;
    private Connection conex;
    private String pres;
    int nuevo=0;
    private String primero;
    /** Creates new form nuevosmateriales */
    public nuevamano(java.awt.Frame parent, boolean modal, Connection conex, String pres) {
        super(parent, modal);
        initComponents();
        this.pres = pres;
        this.conex = conex;
        
        jLabel9.setVisible(false);
        jLabel10.setVisible(false);
        jLabel20.setVisible(false);
        nuevo();
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
    
    
     public nuevamano(java.awt.Frame parent, boolean modal, Connection conex, String pres, int edita, String codimate) {
        super(parent, modal);
        initComponents();
        this.pres = pres;
        this.conex = conex;
        this.codmano = codimate;
        this.edita=1;
        jTextField1.setVisible(false);
        super.setTitle("Modificar Equipo");
        jLabel1.setText("Modificar Equipo");
        jLabel9.setVisible(false);
        jLabel10.setVisible(false);
        jLabel20.setVisible(false);
        definemodelo();
        cargardatos();
        jSpinner1.setValue(codmano);
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
      public final void definemodelo(){
        String consulta = "SELECT id FROM mmopres WHERE mpre_id='"+pres+"' AND status=1";
        String [] mate;
        try {
            Statement st = (Statement) conex.createStatement();
            ResultSet rst = st.executeQuery(consulta);
            rst.last();
            mate = new String[rst.getRow()];
            rst.first();
            mate[0]=rst.getString(1);
            int i=1;
            while(rst.next()){
                mate[i]=rst.getString(1);
                i++;
            }
            primero = mate[0];
            if(codmano==null|| codmano.equals("")){
                codmano=primero;
            }
            SpinnerListModel modelo = new SpinnerListModel(mate);
            jSpinner1.setModel(modelo);
        } catch (SQLException ex) {
            Logger.getLogger(nuevamano.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
      public void validafloat( java.awt.event.KeyEvent evt,String valor){
        char car = evt.getKeyChar();
        
        int repite = new StringTokenizer(valor,".").countTokens() - 1;
        if ((car<'0' || car>'9') && car!='.') {            
            evt.consume();
        }
        if(car=='.'&& repite==1){
             evt.consume();
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
        jTextField7.setText("");
        jTextField8.setText("");
        edita=0;
        nuevo=1;
}
     public final void cargardatos(){
        try {
            Statement carga = (Statement) conex.createStatement();
            
            String consulta="SELECT id, descri, bono , salario, subsid "
                    + "FROM mepres WHERE mpre_id='"+pres+"' AND id='"+codmano+"' AND status=1";
            ResultSet rscarga = carga.executeQuery(consulta);
            
            while(rscarga.next()){
                jTextField1.setText(rscarga.getObject(1).toString());
                jTextField8.setText(rscarga.getObject(2).toString());
                jTextField3.setText(rscarga.getObject(3).toString());
                jTextField7.setText(rscarga.getObject(4).toString());
                jTextField7.setText(rscarga.getObject(5).toString());
            }
        } catch (SQLException ex) {
            Logger.getLogger(nuevamano.class.getName()).log(Level.SEVERE, null, ex);
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
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        jSpinner1 = new javax.swing.JSpinner();
        jTextField9 = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.darkGray, java.awt.Color.lightGray, java.awt.Color.gray, java.awt.Color.lightGray));

        jPanel2.setBackground(new java.awt.Color(91, 91, 95));
        jPanel2.setPreferredSize(new java.awt.Dimension(500, 36));

        jLabel1.setBackground(new java.awt.Color(91, 91, 95));
        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Agregar Mano de Obra");
        jLabel1.setOpaque(true);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 689, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel2.setText("Código *:");

        jTextField1.setToolTipText("Ingrese Código del Tabulador");
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField1KeyTyped(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel4.setText("Descripción:");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel5.setText("Bono:");

        jTextField3.setToolTipText("Ingrese Código del Tabulador");
        jTextField3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField3KeyTyped(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 14));
        jLabel9.setForeground(new java.awt.Color(255, 0, 0));
        jLabel9.setText("*");

        jTextField8.setToolTipText("Ingrese Código del Tabulador");
        jTextField8.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField8KeyTyped(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 14));
        jLabel10.setForeground(new java.awt.Color(255, 0, 0));
        jLabel10.setText("*");

        jLabel20.setForeground(new java.awt.Color(255, 0, 0));
        jLabel20.setText("* Campo no puede ser vacio");

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel12.setText("Salario:");

        jTextField7.setToolTipText("Ingrese Código del Tabulador");
        jTextField7.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField7KeyTyped(evt);
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

        cancelButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/eliminar.png"))); // NOI18N
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        jSpinner1.setModel(new javax.swing.SpinnerListModel(new String[] {""}));
        jSpinner1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner1StateChanged(evt);
            }
        });

        jTextField9.setToolTipText("Ingrese Código del Tabulador");
        jTextField9.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField9KeyTyped(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel13.setText("Subsidio:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(88, 88, 88)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel4)
                            .addComponent(jLabel2)
                            .addComponent(jLabel12)
                            .addComponent(jLabel13))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 474, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel10))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(297, 297, 297)
                        .addComponent(jLabel20))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 699, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel2)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel20)
                .addGap(37, 37, 37)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18))
        );

        getRootPane().setDefaultButton(okButton);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 703, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 332, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed

    }//GEN-LAST:event_okButtonActionPerformed
    
    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        doClose(RET_CANCEL);
    }//GEN-LAST:event_cancelButtonActionPerformed

    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        doClose(RET_CANCEL);
    }//GEN-LAST:event_closeDialog

    private void jSpinner1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner1StateChanged
        codmano = jSpinner1.getValue().toString();
        cargardatos();
        // TODO add your handling code here:
    }//GEN-LAST:event_jSpinner1StateChanged

    private void okButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_okButtonMouseClicked
        int valida=0;
        if(jTextField8.getText().toString().equals(""))
            valida=1;
        if(jTextField1.getText().toString().equals(""))
            valida=1;
            
            if(valida==0){
                jLabel10.setVisible(false);
                jLabel9.setVisible(false);
                jLabel20.setVisible(false);
                if(edita==0){
            try {
                Statement st = (Statement) conex.createStatement();
                String sql = "INSERT INTO mmopres"
                        + "(mpre_id, id, descri, bono, salario, subsid, status)"
                        + " VALUES ('"+pres+"', '"+jTextField1.getText().toString()+"'"
                                                                            + ",'"+jTextField8.getText().toString()+"' "
                                                                            + ", "+jTextField3.getText().toString()+" "
                                                                            + ","+jTextField7.getText().toString()+""
                                                                            + ","+jTextField9.getText().toString()+""
                                                                            + ",1); ";
                st.execute(sql);
                JOptionPane.showMessageDialog(null, "La mano de obra ha sido insertada");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "La mano de obra no ha sido insertada");
                Logger.getLogger(nuevamano.class.getName()).log(Level.SEVERE, null, ex);
            }
                }else
                    if(edita==1){
                         try {
                Statement str = (Statement) conex.createStatement();
                String sql = "UPDATE mmopres SET descri='"+jTextField8.getText().toString()+"' "
                                                                            + ", bono="+jTextField3.getText().toString()+" "
                                                                            + ", salario="+jTextField7.getText().toString()+""
                                                                            + ", subsid="+jTextField9.getText().toString()+""
                                                                            + " WHERE mpre_id='"+pres+"' AND id='"+codmano+"'; ";
                str.execute(sql);
                JOptionPane.showMessageDialog(null, "La mano de obra ha sido modificada");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "La mano de obra no ha sido modificada");
                Logger.getLogger(nuevamano.class.getName()).log(Level.SEVERE, null, ex);
            }
                    }
                doClose(RET_OK);
            }else{
                jLabel10.setVisible(true);
                jLabel9.setVisible(true);
                jLabel20.setVisible(true);
            }        // TODO add your handling code here:
    }//GEN-LAST:event_okButtonMouseClicked

private void jTextField3KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField3KeyTyped
     char car = evt.getKeyChar();
        int repite = new StringTokenizer(jTextField3.getText().toString(),".").countTokens() - 1;
        if ((car<'0' || car>'9') && car!='.') {            
            evt.consume();
        }
        if(car=='.'&& repite==1){
             evt.consume();
        }
    
    // TODO add your handling code here:
}//GEN-LAST:event_jTextField3KeyTyped

private void jTextField7KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField7KeyTyped
     char car = evt.getKeyChar();
        int repite = new StringTokenizer(jTextField7.getText().toString(),".").countTokens() - 1;
        if ((car<'0' || car>'9') && car!='.') {            
            evt.consume();
        }
        if(car=='.'&& repite==1){
             evt.consume();
        }
    
    // TODO add your handling code here:
}//GEN-LAST:event_jTextField7KeyTyped

private void jTextField1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyTyped
       Character c = evt.getKeyChar();
                if(Character.isLetter(c)) {
                    evt.setKeyChar(Character.toUpperCase(c));
                }
    
    // TODO add your handling code here:
}//GEN-LAST:event_jTextField1KeyTyped

    private void jTextField9KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField9KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField9KeyTyped

private void jTextField8KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField8KeyTyped
   Character c = evt.getKeyChar();
                if(Character.isLetter(c)) {
                    evt.setKeyChar(Character.toUpperCase(c));
                }// TODO add your handling code here:
}//GEN-LAST:event_jTextField8KeyTyped
    
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
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    private javax.swing.JButton okButton;
    // End of variables declaration//GEN-END:variables
    private int returnStatus = RET_CANCEL;
}
