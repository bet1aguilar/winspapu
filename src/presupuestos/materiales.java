/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * materiales.java
 *
 * Created on 18/02/2013, 09:20:10 AM
 */
package presupuestos;

import com.mysql.jdbc.ResultSetMetaData;
import com.mysql.jdbc.Statement;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.sql.Connection;
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
 * @author Betmart
 */

public class materiales extends javax.swing.JDialog {

    /** A return status code - returned if Cancel button has been pressed */
    public static final int RET_CANCEL = 0;
    /** A return status code - returned if OK button has been pressed */
    public static final int RET_OK = 1;
private Connection conexion;
    private Statement stmt;
    private String tabu;
String [] materiales, auxmat;
    int material = 0;
 int contsel =0, auxcont=0;
 String numepart, codicove;
 String mpres;
 String descri, desperdi, precio, unidad; 
    private int cuantos;
    /** Creates new form materiales */
    public materiales(java.awt.Frame parent, boolean modal, String mpres, Connection conex, String mtabu, String num, String codicove) {
        super(parent, modal);
        initComponents();
        tabu = mtabu;
        this.mpres = mpres;
        numepart = num;
        this.codicove = codicove;
        this.conexion = conex;
        cargartabus();
        cargarmateriales();
        jTable1.setShowHorizontalLines(true);
    jTable1.setShowVerticalLines(false);
    jTable1.getTableHeader().setSize(new Dimension(25,40));
    jTable1.getTableHeader().setPreferredSize(new Dimension(30,40));
    jTable1.setRowHeight(20);
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

    public final void cargartabus(){
        try {
            DefaultTableModel cont = new DefaultTableModel();
            jComboBox1.removeAllItems();
            String selec="", item="";
            int seleccionado=0;
                Statement s = (Statement) conexion.createStatement();
                ResultSet rs = s.executeQuery("SELECT id, seleccionado FROM mtabus WHERE status=1");
                
                while(rs.next()){
                    item =rs.getString("id"); 
                    jComboBox1.addItem(item);
                    seleccionado = rs.getInt("seleccionado");
                    if(seleccionado==1){
                        selec=item;
                    }
                }
             
               jComboBox1.setSelectedItem(selec);
               tabu = selec;
        } catch (SQLException ex) {
            Logger.getLogger(materiales.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
      public final void cargarmateriales(){
        DefaultTableModel mat = new DefaultTableModel() {@Override
        public boolean isCellEditable (int fila, int columna) {
          if(columna==0) {
                return true;
            }
            return false;
            }
            @Override
       public Class getColumnClass(int columna)
        {
            if (columna == 0) {
                return Boolean.class;
            }
            
            return Object.class;
        }
        };
     
               
               jTable1.setModel(mat);
       
        try {
            Statement s = (Statement) conexion.createStatement();
            ResultSet rs = s.executeQuery("SELECT id,descri, desperdi, precio, unidad FROM Mmtabs WHERE mtabus_id='"+tabu+"'");
            
            ResultSetMetaData rsMd = (ResultSetMetaData) rs.getMetaData();
            int cantidadColumnas = rsMd.getColumnCount()+1;
             mat.addColumn("");
             for (int i = 2; i <= cantidadColumnas; i++) {
                 mat.addColumn(rsMd.getColumnLabel(i-1));
            }
             
             while (rs.next()) {
                Object[] fila = new Object[cantidadColumnas];
                Object obj = Boolean.valueOf(false);
                fila[0]= obj;
                for (int i = 1; i < cantidadColumnas; i++) {
                    fila[i]=rs.getObject(i);
                }
                mat.addRow(fila);
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(materiales.class.getName()).log(Level.SEVERE, null, ex);
        }
            int filas = jTable1.getRowCount();
            auxmat = new String[filas];
               cambiarcabecera();
    }
    
      
      public void verificarcheck() {

        int registros = jTable1.getRowCount();
        int columnas = jTable1.getColumnCount();
       
        Object obj;
        materiales = new String [registros];
        Boolean bol;
        String strNombre;
        StringBuilder builder = null;
        int i, j;
        for (i = 0; i < registros; i++) {
            if (i == 0) {
                builder = new StringBuilder();
                builder.append("Materiales Seleccionados :").append("\n");
            }
            for (j = 0; j < columnas; j++) {

                obj = jTable1.getValueAt(i, j);
                if (obj instanceof Boolean) {
                    bol = (Boolean) obj;
                    if (bol.booleanValue()) {
                        
                        materiales[contsel] = (String) jTable1.getValueAt(i, 1);
                        strNombre = (String) jTable1.getValueAt(i, 1);
                        builder.append("Nombre  :").append(strNombre).append("\n");
                        contsel++;
                    }
                }

            }

        }
        
    }
private void cambiarcabecera() {
        JTableHeader th = jTable1.getTableHeader();
 
       TableColumnModel tcm = th.getColumnModel();
       TableColumn tc = tcm.getColumn(0);        
       tc.setHeaderValue("");
       tc.setPreferredWidth(20);
       tc = tcm.getColumn(1);        
       tc.setHeaderValue("Código");
       tc.setPreferredWidth(50);
       tc = tcm.getColumn(2);        
        tc.setHeaderValue("Descripción");
        tc.setPreferredWidth(250);
        tc = tcm.getColumn(3);        
        tc.setHeaderValue("Desperdicio");
        tc.setPreferredWidth(50);
        tc = tcm.getColumn(4);
        tc.setHeaderValue("Precio");
        tc.setPreferredWidth(50);
        tc = tcm.getColumn(5);         
        tc.setHeaderValue("Unidad");
        tc.setPreferredWidth(50);
       th.repaint(); 
    }
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jTextField4 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        jPanel4.setBackground(new java.awt.Color(100, 100, 100));

        jLabel1.setBackground(new java.awt.Color(91, 91, 95));
        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Agregar Materiales");
        jLabel1.setOpaque(true);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 748, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
        );

        jTextField4.setPreferredSize(new java.awt.Dimension(20, 20));

        jLabel6.setText("Buscar:");

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/buscar1.fw.png"))); // NOI18N
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jLabel2.setText("Seleccionar Listado:");

        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel6)
                    .addComponent(jLabel2))
                .addGap(53, 53, 53)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextField4, javax.swing.GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(17, 17, 17)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );

        jTable1.setAutoCreateRowSorter(true);
        jTable1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTable1.setEditingColumn(0);
        jTable1.setEditingRow(0);
        jTable1.setSelectionBackground(new java.awt.Color(216, 141, 0));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        okButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/selecc.fw.png"))); // NOI18N
        okButton.setToolTipText("Añade Materiales Seleccionados");
        okButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                okButtonMouseClicked(evt);
            }
        });

        cancelButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/eliminar.png"))); // NOI18N
        cancelButton.setToolTipText("Cancelar");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        getRootPane().setDefaultButton(okButton);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(396, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 726, Short.MAX_VALUE)
                .addGap(12, 12, 12))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 239, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
        );

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

     public void busca(){
        Boolean enc;
        String busqueda = jTextField4.getText().toString();
        DefaultTableModel mat = new DefaultTableModel() {@Override
        public boolean isCellEditable (int fila, int columna) {
          if(columna==0) {
                return true;
            }
            return false;
            }
            @Override
       public Class getColumnClass(int columna)
        {
            if (columna == 0) {
                return Boolean.class;
            }
            
            return Object.class;
        }
        };
     
               
               jTable1.setModel(mat);
       
        try {
            Statement s = (Statement) conexion.createStatement();
            ResultSet rs = s.executeQuery("SELECT id,descri, desperdi, precio, unidad FROM Mmtabs WHERE mtabus_id='"+tabu+"' AND (id LIKE'%"+busqueda+"%' || descri LIKE '%"+busqueda+"%')");
            
            ResultSetMetaData rsMd = (ResultSetMetaData) rs.getMetaData();
            int cantidadColumnas = rsMd.getColumnCount()+1;
             mat.addColumn("");
             for (int i = 2; i <= cantidadColumnas; i++) {
                 mat.addColumn(rsMd.getColumnLabel(i-1));
            }
             
             while (rs.next()) {
                Object[] fila = new Object[cantidadColumnas];
                enc=false;
                    for(int j=0;j<auxcont;j++){
                         if(rs.getObject(1).toString().equals(auxmat[j])){
                                  enc=true;
                         }
                    }
                Object obj = Boolean.valueOf(enc);
                fila[0]= obj;
                for (int i = 1; i < cantidadColumnas; i++) {
                    fila[i]=rs.getObject(i);
                }
                mat.addRow(fila);
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(materiales.class.getName()).log(Level.SEVERE, null, ex);
        }
               cambiarcabecera();
       }
    
    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed

        doClose(RET_CANCEL);     }//GEN-LAST:event_cancelButtonActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        material = jTable1.rowAtPoint(evt.getPoint());
       String mat = (String) jTable1.getValueAt(material, 1);
       Object obj;
       Boolean bol;
        obj = jTable1.getValueAt(material, 0);
                if (obj instanceof Boolean) {
                    bol = (Boolean) obj;
                    if (bol.booleanValue()) {
                        System.out.println("Selecciono este material");
                        auxmat[auxcont] = mat;
                        auxcont++;
                    }else{
                        for(int i=0; i<auxcont;i++){
                            if(auxmat[i].equals(mat)){
                                for(int j=i+1;j<auxcont;j++){
                                    auxmat[i]=auxmat[i+1];
                                }
                                auxcont--;
                            }
                        }
                    }
                }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        busca();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        tabu=jComboBox1.getSelectedItem().toString();
        cargarmateriales();
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void okButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_okButtonMouseClicked

        Statement st= null;
        String sql;
        jTextField4.setText("");
        busca();
        try {
            st = (Statement) conexion.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(materiales.class.getName()).log(Level.SEVERE, null, ex);
        }
        verificarcheck();
         for(int i=0; i<contsel;i++){
             System.out.println(materiales[i]);
         }
            
        
        for(int i=0; i<contsel;i++){
            
            try {
                String consulta = "SELECT descri, desperdi, precio, unidad FROM mmtabs WHERE id='"+materiales[i]+"' AND mtabus_id='"+tabu+"'";
                Statement stmts = (Statement) conexion.createStatement();
                ResultSet rst = stmts.executeQuery(consulta);
                while(rst.next()){
                    descri = rst.getObject(1).toString();
                    desperdi = rst.getObject(2).toString();
                    precio = rst.getObject(3).toString();
                    unidad = rst.getObject(4).toString();
                }
                sql="INSERT INTO dmpres (mpre_id, mppre_id, mmpre_id, numepart, cantidad, precio, status)"
                        + " VALUES ('"+mpres+"', '"+codicove+"', " + 

                                                            "'"+materiales[i]+"'," + 

                                                            ""+numepart+", 1,"
                                                            + ""+precio+"," + 

                                                            "1);";
                try {
                    st.execute(sql);
                    System.out.println(sql);
                    
                } catch (SQLException ex) {
                    Logger.getLogger(materiales.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                
                        
                
                 Statement cuenta = (Statement) conexion.createStatement();
                    
                    String sqlcuenta = "SELECT count(id) FROM mmpres WHERE mpre_id='"+mpres+"' AND id='"+materiales[i] +"'";
                    ResultSet rscuenta = cuenta.executeQuery(sqlcuenta);
                    while(rscuenta.next()){
                        cuantos = Integer.parseInt(rscuenta.getObject(1).toString());
                        
                    }
                    if(cuantos==0){
                        Statement mat = (Statement) conexion.createStatement();
                        String insertmat = "INSERT INTO mmpres (mpre_id, id, descri,desperdi, precio, "
                                + "unidad, status)"
                                + "VALUES('"+mpres+"', '"+materiales[i] +"', '"+descri+"', "+desperdi+""
                                + ", "+precio+", '"+unidad+"', '1')";
                        mat.execute(insertmat);
                    }
            } catch (SQLException ex) {
                Logger.getLogger(materiales.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        JOptionPane.showMessageDialog(null, "Se han Agregado los materiales, modifique los valores necesarios"); 
        
        
        doClose(RET_OK);          // TODO add your handling code here:
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
    private javax.swing.JButton jButton5;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JButton okButton;
    // End of variables declaration//GEN-END:variables
    private int returnStatus = RET_CANCEL;
}
