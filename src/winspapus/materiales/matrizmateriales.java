/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * matrizmateriales.java
 *
 * Created on 18/09/2012, 02:44:12 AM
 */
package winspapus.materiales;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSetMetaData;
import com.mysql.jdbc.Statement;
import java.awt.Dimension;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import winspapus.Principal;

/**
 *
 * @author Betmart
 */
public class matrizmateriales extends javax.swing.JDialog {
int row, x, y;
String codigo, codimate;
Connection conex;
String palabra="";
Principal obj;
    /** Creates new form matrizmateriales */
    public matrizmateriales(java.awt.Frame parent, boolean modal, Connection conex, Principal obj) {
        super(parent, modal);
        initComponents();
        this.conex=conex;
        this.obj = obj;
        cargartabus();
        cambiarcabecera();
        
         jTable1.setShowHorizontalLines(true);
    jTable1.setShowVerticalLines(false);
    jTable1.getTableHeader().setSize(new Dimension(25,40));
    jTable1.getTableHeader().setPreferredSize(new Dimension(25,30));
    jTable1.setRowHeight(20);
    jTable2.setOpaque(true);
    jTable2.setShowHorizontalLines(true);
    jTable2.setShowVerticalLines(false);
    jTable2.getTableHeader().setSize(new Dimension(25,40));
    jTable2.getTableHeader().setPreferredSize(new Dimension(25,30));
    jTable2.setRowHeight(20);
    }
private void cambiarcabecera() {
    
       JTableHeader th = jTable1.getTableHeader(); 
       TableColumnModel tcm = th.getColumnModel();
       TableColumn tc = tcm.getColumn(0); 
       tc.setPreferredWidth(10);
       tc.setHeaderValue("Código");
       tc = tcm.getColumn(1); 
       tc.setPreferredWidth(50);
       tc.setHeaderValue("Descripción");
                
       th.repaint(); 
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
        jPanel8 = new javax.swing.JPanel();
        jTextField2 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jTextField2.setPreferredSize(new java.awt.Dimension(20, 20));

        jLabel4.setText("Buscar:");

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/buscar1.fw.png"))); // NOI18N
        jButton3.setToolTipText("Busca");
        jButton3.setEnabled(false);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE))
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jTable1.setFont(new java.awt.Font("Tahoma", 0, 10));
        jTable1.setSelectionBackground(new java.awt.Color(255, 153, 51));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jPanel2.setBackground(new java.awt.Color(100, 100, 100));

        jLabel1.setBackground(new java.awt.Color(91, 91, 95));
        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Materiales Matriz de Costos");
        jLabel1.setOpaque(true);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 846, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jLabel2.setText("Seleccione el Precio Referencial para el cual desea modificar los materiales");

        jTable2.setFont(new java.awt.Font("Tahoma", 0, 9));
        jTable2.setSelectionBackground(new java.awt.Color(255, 153, 51));
        jTable2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable2MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTable2);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/agregarverde.fw.png"))); // NOI18N
        jButton1.setToolTipText("Nuevo");
        jButton1.setEnabled(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/edita.fw.png"))); // NOI18N
        jButton2.setToolTipText("Editar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/recalcula.fw.png"))); // NOI18N
        jButton4.setToolTipText("Recalcular");
        jButton4.setEnabled(false);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/eliminar.png"))); // NOI18N
        jButton5.setToolTipText("Cancelar");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 387, Short.MAX_VALUE)
                .addGap(449, 449, 449))
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(590, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 826, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 826, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(13, 13, 13)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 416, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    public final void cargartabus(){
          DefaultTableModel mtabus = new DefaultTableModel() {@Override
        public boolean isCellEditable (int fila, int columna) {
           
            return false;
            }
        };
     
               
               jTable1.setModel(mtabus);
       
        try {
            Statement s = (Statement) conex.createStatement();
            ResultSet rs = s.executeQuery("SELECT id,descri, DATE_FORMAT(vigencia, '%d/%m/%Y') as Vigencia FROM Mtabus WHERE status=1");
            
            ResultSetMetaData rsMd = (ResultSetMetaData) rs.getMetaData();
            int cantidadColumnas = rsMd.getColumnCount();
             for (int i = 1; i <= cantidadColumnas; i++) {
                 mtabus.addColumn(rsMd.getColumnLabel(i));
            }
             
             while (rs.next()) {
                 Object[] fila = new Object[cantidadColumnas];
                for (int i = 0; i < cantidadColumnas; i++) {
                    fila[i]=rs.getObject(i+1);
                }
                mtabus.addRow(fila);
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
               cambiarcabecera();
    }
    
    
    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        row = jTable1.rowAtPoint(evt.getPoint());
        jButton1.setEnabled(true);
        jButton3.setEnabled(true);
           jButton4.setEnabled(true);
        
        codigo = jTable1.getValueAt(row, 0).toString();
        try {
            buscamat();
        } catch (SQLException ex) {
            Logger.getLogger(matrizmateriales.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        nuevosmateriales nuevos = new nuevosmateriales(obj, true, conex, codigo);
         x=(obj.getWidth()/2)-350;
            y=(obj.getHeight()/2-200);
            nuevos.setBounds(x, y, 750, 380);            
           nuevos.setVisible(true);
        try {
            buscamat();
        } catch (SQLException ex) {
            Logger.getLogger(matrizmateriales.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
       nuevosmateriales nuevos = new nuevosmateriales(obj, true, conex, codigo, 1, codimate);
         x=(obj.getWidth()/2)-350;
            y=(obj.getHeight()/2-200);
            nuevos.setBounds(x, y, 750, 380);            
           nuevos.setVisible(true);
        try {
            buscamat();
        } catch (SQLException ex) {
            Logger.getLogger(matrizmateriales.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
       recalcula recalculo= new recalcula(obj, true,codigo, conex);
       x=(obj.getWidth()/2)-150;
            y=(obj.getHeight()/2-125);
            recalculo.setBounds(x, y, 300, 250);            
           recalculo.setVisible(true);
             try {
            buscamat();
        } catch (SQLException ex) {
            Logger.getLogger(matrizmateriales.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseClicked
        jButton2.setEnabled(true);
        int fila = jTable2.rowAtPoint(evt.getPoint());
        codimate = jTable2.getValueAt(fila, 0).toString();
    }//GEN-LAST:event_jTable2MouseClicked

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        setVisible(false);
        dispose();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        palabra = jTextField2.getText().toString();
        try {
            buscamat();
        } catch (SQLException ex) {
            Logger.getLogger(matrizmateriales.class.getName()).log(Level.SEVERE, null, ex);
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void cambiacabecera() {
         JTableHeader th = jTable2.getTableHeader();
       TableColumnModel tcm = th.getColumnModel();
       TableColumn tc = tcm.getColumn(0); 
       tc.setHeaderValue("Código");
       tc.setPreferredWidth(20);
       tc = tcm.getColumn(1); 
        tc.setHeaderValue("Descripción");
        tc.setPreferredWidth(100);
        tc = tcm.getColumn(2); 
        tc.setHeaderValue("Precio");
        tc.setPreferredWidth(30);
        tc = tcm.getColumn(3); 
        tc.setHeaderValue("Desperdicio");
        tc.setPreferredWidth(30);
        tc = tcm.getColumn(4); 
        tc.setHeaderValue("Unidad");
        tc.setPreferredWidth(30);
         
       th.repaint(); 
    }
    private void buscamat() throws SQLException{
        DefaultTableModel mmtabs = new DefaultTableModel(){@Override
        public boolean isCellEditable (int fila, int columna) {
          
            return false;
            }
        };
        Statement st = (Statement) conex.createStatement();
        String sql="SELECT id, descri, precio, desperdi, unidad FROM mmtabs WHERE mtabus_id='"+codigo+"' AND "
                + "(descri LIKE '%"+palabra+"%' || id LIKE '%"+palabra+"%') AND status=1";
        ResultSet rst = st.executeQuery(sql);
        jTable2.setModel(mmtabs);
        
         ResultSetMetaData rsMd = (ResultSetMetaData) rst.getMetaData();
            int cantidadColumnas = rsMd.getColumnCount();
             for (int i = 1; i <= cantidadColumnas; i++) {
                 mmtabs.addColumn(rsMd.getColumnLabel(i));
            }
             
             while (rst.next()) {
                 Object[] fila = new Object[cantidadColumnas];
                for (int i = 0; i < cantidadColumnas; i++) {
                    fila[i]=rst.getObject(i+1);
                }
                mmtabs.addRow(fila);
                
            }
             cambiacabecera();
    }
    /**
     * @param args the command line arguments
     */
   
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextField jTextField2;
    // End of variables declaration//GEN-END:variables
}