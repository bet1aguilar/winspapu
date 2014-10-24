/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * capitulospartidas.java
 *
 * Created on 13/07/2013, 10:15:53 AM
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
public class capitulospartidas extends javax.swing.JDialog {

    /** A return status code - returned if Cancel button has been pressed */
    public static final int RET_CANCEL = 0;
    /** A return status code - returned if OK button has been pressed */
    public static final int RET_OK = 1;
    Connection conex;
    String tabu;
    private DefaultTableModel metabs;
    private String[] auxpart;
    private int auxcont;
    private String[] partidas;
    private int contsel;
    /** Creates new form capitulospartidas */
    String capitulo;
    int shift=0;
    private int partida;
    public capitulospartidas(java.awt.Frame parent, boolean modal, Connection conex, String tabu, String capitulo) {
        super(parent, modal);
        initComponents();
        this.conex = conex;
        this.tabu = tabu;
        this.capitulo = capitulo;
        buscapartida();
        jTable1.setOpaque(true);
    jTable1.setShowHorizontalLines(true);
    jTable1.setShowVerticalLines(false);
    jTable1.getTableHeader().setSize(new Dimension(25,40));
    jTable1.getTableHeader().setPreferredSize(new Dimension(30,40));
    jTable1.setRowHeight(25);
    
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
    public final void buscapartida(){
        try {
            Statement st = (Statement) conex.createStatement();
            ResultSet rs = st.executeQuery("SELECT codicove, descri, numero, numegrup  "
                    + " FROM Mptabs m WHERE m.mtabus_id = '"+tabu+"' AND status=1 AND m.capitulo IS NULL OR capitulo='0' ORDER BY numegrup");
            ResultSetMetaData rsMd = (ResultSetMetaData) rs.getMetaData();
            //System.out.println("siii entra en presupuesto");
            metabs = new DefaultTableModel(){@Override
            public boolean isCellEditable (int row, int column) {
               if(column==0) {
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
            jTable1.setModel(metabs);
                int cantidadColumnas = rsMd.getColumnCount()+1;
                metabs.addColumn("");
                 for (int i = 2; i <= cantidadColumnas; i++) {
                     System.out.println("Entra a hacer columnas "+cantidadColumnas);
                     metabs.addColumn(rsMd.getColumnLabel(i-1));
                }
                 
                 while (rs.next()) {
                     
                     Object[] filas = new Object[cantidadColumnas];
                      Object obj = Boolean.valueOf(false);
                      filas[0]= obj;
                    for (int i = 1; i < cantidadColumnas; i++) {
                      
                           filas[i]=rs.getObject(i);
                    }
                    metabs.addRow(filas);
                    
                }
                 jTable1.getColumnModel().getColumn(3).setMaxWidth(0);

                 jTable1.getColumnModel().getColumn(3).setMinWidth(0);

                 jTable1.getTableHeader().getColumnModel().getColumn(3).setMaxWidth(0);

                 jTable1.getTableHeader().getColumnModel().getColumn(3).setMinWidth(0);
                 
                 
                 
                 int fila = jTable1.getRowCount();
                 auxpart = new String[fila];
                 
                     
                 cambiarcabecera();
        } catch (SQLException ex) {
            Logger.getLogger(capitulospartidas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void busca(){
        Boolean enc;
        String busqueda = jTextField1.getText().toString();
        
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
            Statement s = (Statement) conex.createStatement();
            ResultSet rs = s.executeQuery("SELECT codicove, descri, numero, numegrup FROM Mptabs m WHERE m.mtabus_id = '"+tabu+"' AND status=1 AND m.capitulo IS NULL OR m.capitulo='0'  AND (codicove LIKE'%"+busqueda+"%' || descri LIKE '%"+busqueda+"%') ORDER BY numegrup");
            
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
                         if(rs.getObject(1).toString().equals(auxpart[j])){
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
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
         jTable1.getColumnModel().getColumn(3).setMaxWidth(0);

             jTable1.getColumnModel().getColumn(3).setMinWidth(0);

             jTable1.getTableHeader().getColumnModel().getColumn(3).setMaxWidth(0);

             jTable1.getTableHeader().getColumnModel().getColumn(3).setMinWidth(0);
             
             
               cambiarcabecera();
    }
    public void cambiarcabecera(){
       JTableHeader th = jTable1.getTableHeader();
       TableColumnModel tcm = th.getColumnModel();
       TableColumn tc = tcm.getColumn(0); 
       tc.setPreferredWidth(2);
       tc = tcm.getColumn(1); 
       tc.setHeaderValue("Código");
       tc.setPreferredWidth(80);
       tc = tcm.getColumn(2); 
        tc.setHeaderValue("Descripción");
        tc.setPreferredWidth(500);
        tc = tcm.getColumn(4); 
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
        jPanel1 = new javax.swing.JPanel();
        cancelButton = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        cancelButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/eliminar.png"))); // NOI18N
        cancelButton.setToolTipText("Cerrar");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel4.setBackground(new java.awt.Color(100, 100, 100));

        jLabel1.setBackground(new java.awt.Color(91, 91, 95));
        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Agregar Partidas");
        jLabel1.setOpaque(true);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 615, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jTable1.setFont(new java.awt.Font("Tahoma", 0, 10));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTable1.setToolTipText("Mantenga Presionado Shift para Seleccionar Varias al mismo tiempo");
        jTable1.setSelectionBackground(new java.awt.Color(216, 141, 0));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jTable1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTable1KeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jTextField1.setToolTipText("");
        jTextField1.setPreferredSize(new java.awt.Dimension(20, 20));

        jLabel4.setText("Buscar:");

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/buscar1.fw.png"))); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/selecc1.fw.png"))); // NOI18N
        jButton5.setToolTipText("Seleccionar Todo");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/quita1.fw.png"))); // NOI18N
        jButton12.setToolTipText("Deseleccionar Todo");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/añade1.fw.png"))); // NOI18N
        jButton4.setToolTipText("Añadir Partidas Seleccionadas Al capitulo");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(261, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton12, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                        .addComponent(jLabel4)
                        .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 596, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(496, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(1, 1, 1))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 304, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    public void verificarcheck() {

        int registros = jTable1.getRowCount();
        int columnas = jTable1.getColumnCount();
       
        Object obj;
        partidas = new String [registros];
        Boolean bol;
        String strNombre;
        StringBuilder builder = null;
        int i, j;
        for (i = 0; i < registros; i++) {
            if (i == 0) {
                builder = new StringBuilder();
                builder.append("Partidas Seleccionadas :").append("\n");
            }
            for (j = 0; j < columnas; j++) {

                obj = jTable1.getValueAt(i, j);
                if (obj instanceof Boolean) {
                    bol = (Boolean) obj;
                    if (bol.booleanValue()) {
                        
                        partidas[contsel] = (String) jTable1.getValueAt(i, 1);
                        strNombre = (String) jTable1.getValueAt(i, 1);
                        builder.append("Nombre  :").append(strNombre).append("\n");
                        contsel++;
                    }
                }

            }

        }
        
    }
    
    public void agrega(){
        String num="", descri = null, mbda = null, rendim = null, unidad = null, redond = null, status;
        int entrar=0;
        String auxpartida = null;
            
            Statement st= null;
            String sql;
            
            //jTextField1.setText("");
           // busca();
            try {
                st = (Statement) conex.createStatement();
            } catch (SQLException ex) {
                Logger.getLogger(capitulospartidas.class.getName()).log(Level.SEVERE, null, ex);
            }
            verificarcheck();
             /*for(int i=0; i<contsel;i++){
                 System.out.println(partidas[i]);
             }*/
              
            
            
            for(int i=0; i<contsel;i++){
                String actualiza = "UPDATE mptabs SET capitulo="+capitulo+" WHERE codicove='"+partidas[i]+"' "
                        + "AND mtabus_id='"+tabu+"'";
            try {
                Statement sts = (Statement) conex.createStatement();
                sts.execute(actualiza);
            } catch (SQLException ex) {
                Logger.getLogger(capitulospartidas.class.getName()).log(Level.SEVERE, null, ex);
            }
            }
            contsel=0;
        
        
            

}    
    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        doClose(RET_CANCEL);
    }//GEN-LAST:event_cancelButtonActionPerformed

    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        doClose(RET_CANCEL);
    }//GEN-LAST:event_closeDialog

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
         int totalfilas = jTable1.getRowCount();
      // System.out.println("totalfilas: "+totalfilas);
        for(int i=0; i<totalfilas; i++){
            auxpart[i] =(String) jTable1.getValueAt(i, 1);
        }
        auxcont = totalfilas;
        busca();
        
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
      int totalfilas = jTable1.getRowCount();
     //  System.out.println("totalfilas: "+totalfilas);
        for(int i=0; i<totalfilas; i++){
            auxpart[i] ="";
        }
        auxcont = 0;
        busca();
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
       busca();
        
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton15ActionPerformed

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton16ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
    agrega();   
    int op=JOptionPane.showConfirmDialog(null, "¿Desea agregar más partidas al capitulo?");
        if(op ==JOptionPane.YES_OPTION){
            jTextField1.setText("");
            buscapartida();
        }else{ 
            doClose(RET_CANCEL);
            
        }
    // TODO add your handling code here:
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jTable1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable1KeyPressed
       int tecla = evt.getKeyCode();
       System.out.println(tecla);
       if(tecla == 16){
           shift=1;
           
       }
    }//GEN-LAST:event_jTable1KeyPressed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
         
        
        if(shift==1){
            
        int filas[]=new int[auxcont];
        int auxfilas[]=jTable1.getSelectedRows();
       
            filas=auxfilas;
        
                 int totalfilas = filas.length;
                 
      // System.out.println("totalfilas: "+totalfilas);
        for(int i=0; i<auxcont; i++){
            auxpart[i] =(String) jTable1.getValueAt(filas[i], 1);
        }
        auxcont = totalfilas;
        busca();
        shift=0;
        }
        else{
          partida = jTable1.rowAtPoint(evt.getPoint());
    String part =(String) jTable1.getValueAt(partida, 1);
    Object obj;
       Boolean bol;
        obj = jTable1.getValueAt(partida, 0);
                if (obj instanceof Boolean) {
                    bol = (Boolean) obj;
                    if (bol.booleanValue()) {
                       // System.out.println("Selecciono este material");
                        auxpart[auxcont] = part;
                        auxcont++;
                    }else
                    {
                        for(int i=0; i<auxcont;i++){
                            if(auxpart[i].equals(part)){
                                for(int j=i+1;j<auxcont;j++){
                                    auxpart[j]=auxpart[i+1];
                                }
                                auxcont--;
                            }
                        }
                    }
                
                }
        
        }
    }//GEN-LAST:event_jTable1MouseClicked
    
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
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
    private int returnStatus = RET_CANCEL;
}
