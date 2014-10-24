/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * vercapitulo.java
 *
 * Created on 13/07/2013, 12:54:16 PM
 */
package winspapus;

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
 * @author Betmart
 */
public class vercapitulo extends javax.swing.JDialog {

    /** A return status code - returned if Cancel button has been pressed */
    public static final int RET_CANCEL = 0;
    /** A return status code - returned if OK button has been pressed */
    public static final int RET_OK = 1;
    String mtabus;
    String capitulo;
    Connection conex;
    String ids;
    DefaultTableModel metabs;
    int fila1 = 0;
    /** Creates new form vercapitulo */
    public vercapitulo(java.awt.Frame parent, boolean modal, String mtabus, Connection conex, String capitulo) {
        super(parent, modal);
        initComponents();
        this.conex = conex;
        this.capitulo = capitulo;
        this.mtabus = mtabus;
         jTable1.setOpaque(true);
    jTable1.setShowHorizontalLines(true);
    jTable1.setShowVerticalLines(false);
    jTable1.getTableHeader().setSize(new Dimension(25,40));
    jTable1.getTableHeader().setPreferredSize(new Dimension(30,40));
    jTable1.setRowHeight(25);
     jTable2.setOpaque(true);
    jTable2.setShowHorizontalLines(true);
    jTable2.setShowVerticalLines(false);
    jTable2.getTableHeader().setSize(new Dimension(25,40));
    jTable2.getTableHeader().setPreferredSize(new Dimension(30,40));
    jTable2.setRowHeight(25);
        buscasub();
        buscapartida();
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

    public final void buscasub(){
        try {
            Statement st = (Statement) conex.createStatement();
             ResultSet rs = st.executeQuery("SELECT codigo, descri, id "
                     + " FROM ctabs WHERE mtabus_id='"+mtabus+"' AND ctabs_id="+capitulo+"");
             ResultSetMetaData rsMd = (ResultSetMetaData) rs.getMetaData();
             
             DefaultTableModel metabs1 = new DefaultTableModel(){@Override
             public boolean isCellEditable (int row, int column) {
                
                 return false;
             }
             };
             jTable1.setModel(metabs1);
                 int cantidadColumnas = 3;
                  
                     
                      metabs1.addColumn("Código");
                      metabs1.addColumn("Descripción");
                      metabs1.addColumn("Partidas");
              
                  
                  while (rs.next()) {
                     String id = rs.getString("id"); 
                     Object[] filas = new Object[cantidadColumnas];
                     for (int i = 0; i < cantidadColumnas; i++) {
                         if(i<=1){
                            filas[i]=rs.getObject(i+1);
                         }
                         else{
                             int cantsub=0, cantpart=0;
                             String sub = "SELECT count(*) as cant FROM ctabs "
                                     + "WHERE ctabs_id="+id+" AND mtabus_id='"+mtabus+"'";
                             Statement stsub = (Statement) conex.createStatement();
                             ResultSet rstsub = stsub.executeQuery(sub);
                             while(rstsub.next()){
                                 cantsub = rstsub.getInt(1);
                             }
                             String partida = "SELECT count(*) as cant FROM mptabs "
                                     + "WHERE mtabus_id='"+mtabus+"' AND "
                                     + "(capitulo="+id+" OR capitulo IN "
                                     + "(SELECT id FROM ctabs "
                                     + "WHERE ctabs_id="+id+" AND mtabus_id='"+mtabus+"')) ";
                             Statement part = (Statement) conex.createStatement();
                             ResultSet rspart = part.executeQuery(partida);
                             while(rspart.next()){
                                 cantpart = rspart.getInt(1);
                             }
                             
                             filas[2]=cantpart;
                         }
                     }
                     metabs1.addRow(filas);
                     
                 }
        } catch (SQLException ex) {
            Logger.getLogger(capitulos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public final void buscapartida(){
       try {
            Statement st = (Statement) conex.createStatement();
            ResultSet rs = st.executeQuery("SELECT codicove, descri, numero, numegrup  "
                    + " FROM Mptabs m WHERE m.mtabus_id = '"+mtabus+"' AND status=1 AND m.capitulo="+capitulo+" ORDER BY numegrup");
            ResultSetMetaData rsMd = (ResultSetMetaData) rs.getMetaData();
            //System.out.println("siii entra en presupuesto");
            metabs = new DefaultTableModel(){@Override
            public boolean isCellEditable (int row, int column) {
               
                return false;
            }
                @Override
            public Class getColumnClass(int columna)
            {
                
                
                return Object.class;
            }
            };
            jTable2.setModel(metabs);
                int cantidadColumnas = rsMd.getColumnCount();
               
                 for (int i = 1; i <= cantidadColumnas; i++) {
                     System.out.println("Entra a hacer columnas "+cantidadColumnas);
                     metabs.addColumn(rsMd.getColumnLabel(i));
                }
                 
                 while (rs.next()) {
                     
                     Object[] filas = new Object[cantidadColumnas];
                      
                    for (int i = 0; i < cantidadColumnas; i++) {
                      
                           filas[i]=rs.getObject(i+1);
                    }
                    metabs.addRow(filas);
                    
                }
                 jTable2.getColumnModel().getColumn(2).setMaxWidth(0);

                 jTable2.getColumnModel().getColumn(2).setMinWidth(0);

                 jTable2.getTableHeader().getColumnModel().getColumn(2).setMaxWidth(0);

                 jTable2.getTableHeader().getColumnModel().getColumn(2).setMinWidth(0);
                 
                 
                 
                
                 
                     
                 cambiarcabecera();
        } catch (SQLException ex) {
            Logger.getLogger(capitulospartidas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void cambiarcabecera(){
       JTableHeader th = jTable2.getTableHeader();
       TableColumnModel tcm = th.getColumnModel();
       TableColumn tc = tcm.getColumn(0); 
       tc.setHeaderValue("Código");
       tc.setPreferredWidth(80);
       tc = tcm.getColumn(1); 
        tc.setHeaderValue("Descripción");
        tc.setPreferredWidth(500);
        tc = tcm.getColumn(3); 
        tc.setHeaderValue("No");
        tc.setPreferredWidth(30);
       
       th.repaint(); 
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

        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        cancelButton = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        jPanel4.setBackground(new java.awt.Color(100, 100, 100));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Detalles del Capitulo");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 699, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        cancelButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/eliminar.png"))); // NOI18N
        cancelButton.setToolTipText("Cerrar");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/borra.png"))); // NOI18N
        jButton4.setToolTipText("Borrar Partida de Capitulo");
        jButton4.setEnabled(false);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/partida.png"))); // NOI18N
        jButton7.setToolTipText("Agregar Partidas al Capitulo");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/anadir.png"))); // NOI18N
        jButton6.setToolTipText("Nuevo Subcapitulo");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(67, Short.MAX_VALUE)
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel3.setText("Partidas");

        jTable2.setFont(new java.awt.Font("Tahoma", 0, 10));
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTable2.setSelectionBackground(new java.awt.Color(255, 153, 51));
        jTable2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable2MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTable2);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 622, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 669, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/verpartidas.fw.png"))); // NOI18N
        jButton1.setToolTipText("Ver Detalle del Subcapitulo");
        jButton1.setEnabled(false);
        jButton1.setPreferredSize(new java.awt.Dimension(53, 29));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/partida.png"))); // NOI18N
        jButton2.setToolTipText("Agregar Partidas al Subcapitulo");
        jButton2.setEnabled(false);
        jButton2.setPreferredSize(new java.awt.Dimension(53, 29));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/borra.png"))); // NOI18N
        jButton3.setToolTipText("Borrar Subcapitulo");
        jButton3.setEnabled(false);
        jButton3.setPreferredSize(new java.awt.Dimension(53, 29));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/edita.fw.png"))); // NOI18N
        jButton5.setToolTipText("Editar Subcapitulo");
        jButton5.setEnabled(false);
        jButton5.setPreferredSize(new java.awt.Dimension(53, 29));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jTable1.setFont(new java.awt.Font("Tahoma", 0, 10));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTable1.setSelectionBackground(new java.awt.Color(255, 153, 51));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel2.setText("Subcapitulos");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 669, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 597, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 609, Short.MAX_VALUE)
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

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
 capitulospartidas nuevo = new capitulospartidas(null, true, conex, mtabus,ids);
       System.out.println("mtabus "+mtabus+ " capitulo: "+capitulo); 
       int xi = (this.getX());
        int yi = (this.getY());
        nuevo.setBounds(xi, yi, 700, 550);
        nuevo.setVisible(true);
        buscasub();        
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed

        
                nuevocapitulo nuevo = new nuevocapitulo(null, true, mtabus, conex,2, ids);
        int xi = (this.getX()+100);
        int yi = (this.getY()+100);
        nuevo.setBounds(xi, yi, 550, 400);
        nuevo.setVisible(true);
        buscasub();// TODO add your handling code here:
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        int fila = jTable1.rowAtPoint(evt.getPoint());
        
        String cap= jTable1.getValueAt(fila, 0).toString();
        String sql = "SELECT id FROM ctabs WHERE codigo='"+cap+"'";
        try {
            Statement st = (Statement) conex.createStatement();
            ResultSet rst = st.executeQuery(sql);
            while(rst.next()){
                ids= rst.getString("id");
            }
        } catch (SQLException ex) {
            Logger.getLogger(vercapitulo.class.getName()).log(Level.SEVERE, null, ex);
        }
        jButton1.setEnabled(true);
        jButton2.setEnabled(true);
        jButton3.setEnabled(true);
        jButton5.setEnabled(true);
        
    }//GEN-LAST:event_jTable1MouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
       
        
        partidasub sub = new partidasub(null, true, conex, mtabus, ids);
        System.out.println("mtabus "+mtabus+" ids "+ids);
        int xi = (this.getX());
        int yi = (this.getY());
        sub.setBounds(xi, yi, 700, 550);
        sub.setVisible(true);
        buscasub();        
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
       int op = JOptionPane.showConfirmDialog(null, "¿Desea borrar esta partida del Capitulo?");
       if(op==JOptionPane.YES_OPTION){
           
           
           String actualiza = "UPDATE mptabs SET capitulo=NULL WHERE mtabus_id='"+mtabus+"' "
                   + "AND codicove='"+jTable2.getValueAt(fila1, 0) +"' AND (capitulo="+capitulo+" OR capitulo IN (SELECT id FROM ctabs WHERE "
                   + "mtabus_id='"+mtabus+"' AND ctabs_id="+capitulo+"))";
           try {
                Statement borra = (Statement) conex.createStatement();
                borra.execute(actualiza);
                JOptionPane.showMessageDialog(null, "Se ha eliminado la partida");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "No se ha eliminado la partida");
                Logger.getLogger(capitulos.class.getName()).log(Level.SEVERE, null, ex);
            }
           
           
            
           buscapartida();
       }        
        
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed

        
        nuevocapitulo nuevo = new nuevocapitulo(null, true, mtabus, conex,3, capitulo);
        int xi = (this.getX()+100);
        int yi = (this.getY()+100);
        nuevo.setBounds(xi, yi, 550, 400);
        nuevo.setVisible(true);
       buscasub();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
               capitulospartidas nuevo = new capitulospartidas(null, true, conex, mtabus,capitulo);
       System.out.println("mtabus "+mtabus); 
       int xi = (this.getX());
        int yi = (this.getY());
        nuevo.setBounds(xi, yi, 700, 550);
        nuevo.setVisible(true);
        buscapartida();
        
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
               int op = JOptionPane.showConfirmDialog(null, "¿Desea borrar este subcapitulo?");
       if(op==JOptionPane.YES_OPTION){
           
           
           String actualiza = "UPDATE mptabs SET capitulo=NULL WHERE mtabus_id='"+mtabus+"' "
                   + "AND (capitulo="+ids+" OR capitulo IN (SELECT id FROM ctabs WHERE "
                   + "mtabus_id='"+mtabus+"' AND ctabs_id="+ids+"))";
           try {
                Statement borra = (Statement) conex.createStatement();
                borra.execute(actualiza);
            } catch (SQLException ex) {
                Logger.getLogger(capitulos.class.getName()).log(Level.SEVERE, null, ex);
            }
           
           String delete = "DELETE FROM ctabs WHERE (id="+ids+" OR ctabs_id="+ids+")"
                   + " AND mtabus_id='"+mtabus+"'";
            try {
                Statement borra = (Statement) conex.createStatement();
                borra.execute(delete);
            } catch (SQLException ex) {
                Logger.getLogger(capitulos.class.getName()).log(Level.SEVERE, null, ex);
            }
            JOptionPane.showMessageDialog(null, "Se ha eliminado el subcapitulo");
            buscasub();
       }
        
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseClicked

        jButton4.setEnabled(true);
        fila1=jTable2.rowAtPoint(evt.getPoint());
        
        
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable2MouseClicked
    
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
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    // End of variables declaration//GEN-END:variables
    private int returnStatus = RET_CANCEL;
}
