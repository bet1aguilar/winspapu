/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * tabpresupuesto.java
 *
 * Created on 03/12/2012, 11:55:27 AM
 */
package presupuestos;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSetMetaData;
import com.mysql.jdbc.Statement;
import java.awt.Dimension;
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
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author Spapu
 */
public class tabpresupuesto extends javax.swing.JDialog {

    /** A return status code - returned if Cancel button has been pressed */
    public static final int RET_CANCEL = 0;
    /** A return status code - returned if OK button has been pressed */
    public static final int RET_OK = 1;
    String presu="", tab="", descri="";
    private Connection conex;
    int fila=0;
    String mtabu;
    int x,y;
    int muestramensaje=0;
    private Presupuesto obj;
    /** Creates new form tabpresupuesto */
    public tabpresupuesto(java.awt.Frame parent, boolean modal, Presupuesto obj, Connection conex, String mtabu,int x, int y) {
        super(parent, modal);
        this.obj = obj;
        this.conex = conex;
        initComponents();
        this.mtabu = mtabu;
        this.x = x;
        this.y = y;
        try {
            this.buscarpres();
        } catch (SQLException ex) {
            Logger.getLogger(tabpresupuesto.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            this.buscartab();
            selectabu();
        } catch (SQLException ex) {
            Logger.getLogger(tabpresupuesto.class.getName()).log(Level.SEVERE, null, ex);
        }
        jTable1.setShowHorizontalLines(true);
    jTable1.setShowVerticalLines(false);
    jTable1.getTableHeader().setSize(new Dimension(25,40));
    jTable1.getTableHeader().setPreferredSize(new Dimension(25,30));
    jTable1.setRowHeight(20);
    jTable2.setShowHorizontalLines(true);
    jTable2.setShowVerticalLines(false);
    jTable2.getTableHeader().setSize(new Dimension(25,40));
    jTable2.getTableHeader().setPreferredSize(new Dimension(25,30));
    jTable2.setRowHeight(20);
        this.cambiarcabecera();
        this.cambiarcabecera2();
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
    public final void selectabu(){
        int filas = 0;
        int selec = 0;
        String seleccionado="";
        int enc=0,i=0;
        filas = jTable1.getRowCount();
        if(jTable1.getRowCount()>0){
        seleccionado = jTable1.getValueAt(i, 0).toString();
            while(i<filas&&enc==0){
               if(seleccionado.equals(mtabu)){
                   enc=1;
                   tab=mtabu;
                   jTable1.setRowSelectionInterval(i, i);
               }else{
                   i++;
                   seleccionado = jTable1.getValueAt(i, 0).toString();
               }
            }
        }
        
    }
    
    public final void buscarpres () throws SQLException{
        Statement st = (Statement) conex.createStatement();
        ResultSet rs = st.executeQuery("SELECT id, nomabr, DATE_FORMAT(fecini,'%d/%m/%y'), DATE_FORMAT(fecfin,'%d/%m/%y'), porimp, porpre, poruti, poripa FROM mpres WHERE status=1 AND (mpres_id IS NULL OR mpres_id='')");
        ResultSetMetaData rsMd = (ResultSetMetaData) rs.getMetaData();
        
        DefaultTableModel metabs = new DefaultTableModel(){@Override
        public boolean isCellEditable (int row, int column) {
           
            return false;
        }
        };
        jTable2.setModel(metabs);
            int cantidadColumnas = rsMd.getColumnCount();
             for (int i = 1; i <= cantidadColumnas; i++) {
                
                 metabs.addColumn(rsMd.getColumnLabel(i));
            }
             
             while (rs.next()) {
                 
                Object[] filas = new Object[cantidadColumnas];
                for (int i = 0; i < cantidadColumnas; i++) {
                  
                       filas[i]=rs.getObject(i+1);
                }
                metabs.addRow(filas);
                
            }
    }
    
    public final void buscartab () throws SQLException{
        Statement st = (Statement) conex.createStatement();
        ResultSet rs = st.executeQuery("SELECT id, descri, DATE_FORMAT(vigencia,'%d/%m/%y'), putild, pprest, pimpue, pcosfin, padyga FROM mtabus WHERE status=1");
        ResultSetMetaData rsMd = (ResultSetMetaData) rs.getMetaData();
        
        DefaultTableModel metabs = new DefaultTableModel(){@Override
        public boolean isCellEditable (int row, int column) {
           
            return false;
        }
        };
        jTable1.setModel(metabs);
            int cantidadColumnas = rsMd.getColumnCount();
             for (int i = 1; i <= cantidadColumnas; i++) {
                
                 metabs.addColumn(rsMd.getColumnLabel(i));
            }
             
             while (rs.next()) {
                 
                Object[] filas = new Object[cantidadColumnas];
                for (int i = 0; i < cantidadColumnas; i++) {
                  
                       filas[i]=rs.getObject(i+1);
                }
                metabs.addRow(filas);
                
            }
    }
    
    public final void cambiarcabecera(){
       JTableHeader th = jTable2.getTableHeader();
       TableColumnModel tcm = th.getColumnModel();
       TableColumn tc = tcm.getColumn(0); 
       
       tc.setHeaderValue("Código");
       tc.setPreferredWidth(20);
       tc = tcm.getColumn(1); 
       tc.setHeaderValue("Nombre");
       tc.setPreferredWidth(100);
       tc = tcm.getColumn(2); 
       tc.setHeaderValue("Fecha Inicio");
       tc.setPreferredWidth(30);
       tc = tcm.getColumn(3); 
       tc.setHeaderValue("Fecha Fin");
       tc.setPreferredWidth(30);
       tc = tcm.getColumn(4); 
       tc.setHeaderValue("Impuestos");
       tc.setPreferredWidth(30);
       tc = tcm.getColumn(5); 
       tc.setHeaderValue("Prestaciones");
       tc.setPreferredWidth(30);
       tc = tcm.getColumn(6); 
       tc.setHeaderValue("Utilidades");
       tc.setPreferredWidth(30);
       tc = tcm.getColumn(7); 
       tc.setHeaderValue("Imp. Part.");
       tc.setPreferredWidth(50);
       th.repaint(); 
    }
    
    public final void cambiarcabecera2(){
       JTableHeader th = jTable1.getTableHeader();
       TableColumnModel tcm = th.getColumnModel();
       TableColumn tc = tcm.getColumn(0); 
       
       tc.setHeaderValue("Código");
       tc.setPreferredWidth(20);
       tc = tcm.getColumn(1); 
       tc.setHeaderValue("Descripción");
       tc.setPreferredWidth(100);
       tc = tcm.getColumn(2); 
       tc.setHeaderValue("Vigencia");
       tc.setPreferredWidth(30);
       tc = tcm.getColumn(3); 
       tc.setHeaderValue("Utilidad");
       tc.setPreferredWidth(30);
       tc = tcm.getColumn(4); 
       tc.setHeaderValue("Prestaciones");
       tc.setPreferredWidth(30);
       tc = tcm.getColumn(5); 
       tc.setHeaderValue("Impuestos");
       tc.setPreferredWidth(30);
       tc = tcm.getColumn(6); 
       tc.setHeaderValue("Cost. Fin.");
       tc.setPreferredWidth(30);
       tc = tcm.getColumn(7); 
       tc.setHeaderValue("Gastos Adm.");
       tc.setPreferredWidth(50);
       th.repaint(); 
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        okButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setTitle("Lista de Obras y Precios Referenciales");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        okButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/selecc.fw.png"))); // NOI18N
        okButton.setToolTipText("Aceptar");
        okButton.setEnabled(false);
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

        jPanel2.setBackground(new java.awt.Color(97, 126, 171));

        jLabel1.setBackground(new java.awt.Color(91, 91, 95));
        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Seleccione el Listado de Precios Referenciales y la Obra a Trabajar");
        jLabel1.setOpaque(true);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 692, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jTable1.setAutoCreateRowSorter(true);
        jTable1.setFont(new java.awt.Font("Tahoma", 0, 10));
        jTable1.setToolTipText("Seleccione Un Tabulador");
        jTable1.setSelectionBackground(new java.awt.Color(216, 141, 0));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jTable2.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jTable2.setToolTipText("Seleccione Un proyecto de Obra");
        jTable2.setSelectionBackground(new java.awt.Color(216, 141, 0));
        jTable2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable2MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTable2);

        jLabel2.setText("Proyectos de Obra:");

        jLabel3.setText("Precios Referenciales:");

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/borra.png"))); // NOI18N
        jButton1.setToolTipText("Borrar Presupuesto Seleccionado");
        jButton1.setEnabled(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/anade.fw.png"))); // NOI18N
        jButton2.setToolTipText("Nuevo Presupuesto");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel2)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 660, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 660, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(okButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(36, 36, 36))
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
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 482, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        doClose(RET_CANCEL);
    }//GEN-LAST:event_closeDialog

    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseClicked
        fila = jTable2.rowAtPoint(evt.getPoint());
        presu = jTable2.getValueAt(fila, 0).toString();
        descri = jTable2.getValueAt(fila,1).toString();
        jButton1.setEnabled(true);
        okButton.setEnabled(true);
    }//GEN-LAST:event_jTable2MouseClicked

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        int row = jTable1.rowAtPoint(evt.getPoint());
        tab = jTable1.getValueAt(row, 0).toString();
    }//GEN-LAST:event_jTable1MouseClicked

private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    obj.id=presu;
    obj.borrarpres();
    
     try {
            buscarpres();
            cambiarcabecera();
            int filas = jTable1.getRowCount();
            if(filas>0){
                presu=jTable1.getValueAt(0, 0).toString();
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(tabpresupuesto.class.getName()).log(Level.SEVERE, null, ex);
        }   
    jButton1.setEnabled(false);
    okButton.setEnabled(false);
    // TODO add your handling code here:
}//GEN-LAST:event_jButton1ActionPerformed

private void okButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_okButtonMouseClicked
        
        doClose(RET_OK);// TODO add your handling code here:
}//GEN-LAST:event_okButtonMouseClicked

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_okButtonActionPerformed

    
    
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
           
        Nuevo s = new Nuevo(obj.prin, true, obj, conex);
        s.setBounds(x,y, 900, 550);
        s.setVisible(true);
        obj.vaciacampos();
        doClose(RET_OK);
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed
    
    
    public void selecciona(){
       
        String sql = "SELECT COUNT(*) FROM mpres";
        int cont=0;
        try {
            Statement st = (Statement) conex.createStatement();
            ResultSet rst = st.executeQuery(sql);
            while(rst.next()){
                cont = rst.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(tabpresupuesto.class.getName()).log(Level.SEVERE, null, ex);
        }
        int cuenta = jTable2.getRowCount();
        int contar = jTable1.getRowCount();
        if(cont==0&&muestramensaje==0){
            muestramensaje=1;
            JOptionPane.showMessageDialog(null, "Para continuar Debe Agregar un presupuesto.");
           /* Nuevo s = new Nuevo(obj.prin, true, obj, conex);
                s.setBounds(x, y, 900, 550);
                s.setVisible(true);
                obj.vaciacampos();*/
                doClose(RET_OK);
        }
        if(cuenta>0&&contar>0){
        if(tab.equals("")){
            tab=jTable1.getValueAt(0, 0).toString();
        }
        if(presu.equals("")){
            if(cont>0){
            presu = jTable2.getValueAt(0, 0).toString();
            }else{
                Nuevo s = new Nuevo(obj.prin, true, obj, conex);
                s.setBounds(x, y, 900, 550);
                s.setVisible(true);
                obj.vaciacampos();
                doClose(RET_OK);
            }
        }
        if(descri.equals("")){
            descri= jTable2.getValueAt(0, 1).toString();
        }
        try {
            
            obj.settabu(tab);
            if(cont>0){
            obj.filapartida=0;
            obj.setid(presu);
            obj.settitulo(presu+" "+descri);
            obj.cargapresupuesto();
            obj.buscaobra();
            obj.cargartotal();
            }
            if(cont==0){
                obj.buscapartida();
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(tabpresupuesto.class.getName()).log(Level.SEVERE, null, ex);
        }
        }
    }
    private void doClose(int retStatus) {
        returnStatus = retStatus;
        selecciona();
        setVisible(false);
        dispose();
    }

  
 
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JButton okButton;
    // End of variables declaration//GEN-END:variables
    private int returnStatus = RET_CANCEL;
}
