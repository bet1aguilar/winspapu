/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * disminuciontotal.java
 *
 * Created on 11/09/2013, 10:12:15 PM
 */
package valuaciones;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSetMetaData;
import com.mysql.jdbc.Statement;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import winspapus.Principal;

/**
 *
 * @author Betmart
 */
public class disminucionparcial extends javax.swing.JDialog {

    /** A return status code - returned if Cancel button has been pressed */
    public static final int RET_CANCEL = 0;
    /** A return status code - returned if OK button has been pressed */
    public static final int RET_OK = 1;
    Connection conex;
    String pres, numaumydis;
    String [] auxpart;
    private int partida;
    private int auxcont=0;
    private int contsel;
    private String[] partidas;
    private float [] cantidades;
    private String nuevonum;
    private int nuevo;
    private int nuevonumegrup;
    private int insertar;
    Object ob;
    private String rendimi;
    /** Creates new form disminuciontotal */
    public disminucionparcial(Principal parent, boolean modal, Connection conex, String pres, String numpread) {
        super(parent, modal);
        initComponents();
        this.pres=pres;
        this.conex=conex;
        this.numaumydis=numpread;
        cargapartidas();
        this.setTitle("Partidas que no están en ninguna Valuación para disminución Parcial");
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
    public final void cargapartidas(){
        DefaultTableModel mat = new DefaultTableModel() {@Override
        public boolean isCellEditable (int fila, int columna) {
          if(columna==0 || columna==7) {
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
            if(columna==7||columna==6){
                return Double.class;
            }
            
            return Object.class;
        }
        };
     
               
               jTable1.setModel(mat);
       
        try {
            Statement s = (Statement) conex.createStatement();
            ResultSet rs = s.executeQuery("SELECT id, descri, numero, numegrup, precunit, cantidad, tipo FROM mppres "
                    + "WHERE numero NOT "
                    + "IN(SELECT numepart FROM dvalus WHERE (mpre_id='"+pres+"' OR mpre_id "
                    + "IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"'))) AND (mpre_id='"+pres+"' OR mpre_id IN "
                    + "(SELECT id FROM mpres WHERE mpres_id='"+pres+"')) ORDER BY numegrup");
            
            ResultSetMetaData rsMd = (ResultSetMetaData) rs.getMetaData();
            int cantidadColumnas = rsMd.getColumnCount()+1;
             mat.addColumn("");
             for (int i = 2; i <= cantidadColumnas; i++) {
                 if(i!=7){
                 mat.addColumn(rsMd.getColumnLabel(i-1));
                 }else{
                  mat.addColumn("Disminución");   
                 }
            }
             
             while (rs.next()) {
                Object[] fila = new Object[cantidadColumnas];
                Boolean enc=false;
                    for(int j=0;j<auxcont;j++){
                         if(rs.getObject(3).toString().equals(auxpart[j])){
                                  enc=true;
                         }
                    }
                Object obj = Boolean.valueOf(enc);
           
                
                fila[0]= obj;
                for (int i = 1; i < cantidadColumnas; i++) {
                   if(i<7){
                    fila[i]=rs.getObject(i);
                   }else
                   {
                       fila[i]=0;
                   }
                    
                }
                mat.addRow(fila);
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(disminucionparcial.class.getName()).log(Level.SEVERE, null, ex);
        }
            int filas = jTable1.getRowCount();
            auxpart = new String[filas];
            jTable1.getColumnModel().getColumn(3).setMaxWidth(0);

             jTable1.getColumnModel().getColumn(3).setMinWidth(0);

             jTable1.getTableHeader().getColumnModel().getColumn(3).setMaxWidth(0);

             jTable1.getTableHeader().getColumnModel().getColumn(3).setMinWidth(0);
               cambiarcabecera();
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
        tc = tcm.getColumn(4);        
        tc.setHeaderValue("Número");
        tc.setPreferredWidth(50);
        tc = tcm.getColumn(5);
        tc.setHeaderValue("Precio Unitario");
        tc.setPreferredWidth(50);
        tc = tcm.getColumn(6);         
        tc.setHeaderValue("Cant. Contratada");
        tc.setPreferredWidth(50);
         tc = tcm.getColumn(7);         
        tc.setHeaderValue("Disminución");
        tc.setPreferredWidth(30);
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

        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        cancelButton = new javax.swing.JButton();
        okButton = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel10 = new javax.swing.JPanel();
        jTextField4 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();

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

        okButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/agregarverde.fw.png"))); // NOI18N
        okButton.setToolTipText("");
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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(51, Short.MAX_VALUE)
                .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(cancelButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(okButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getRootPane().setDefaultButton(okButton);

        jPanel4.setBackground(new java.awt.Color(100, 100, 100));

        jLabel1.setBackground(new java.awt.Color(91, 91, 95));
        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Agregar Partidas a Dismininución Parcial");
        jLabel1.setOpaque(true);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 694, Short.MAX_VALUE)
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

        jTextField4.setPreferredSize(new java.awt.Dimension(20, 20));

        jLabel6.setText("Buscar:");

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/buscar1.fw.png"))); // NOI18N
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/selecc1.fw.png"))); // NOI18N
        jButton1.setToolTipText("Seleccionar Todo");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/quita1.fw.png"))); // NOI18N
        jButton2.setToolTipText("Deseleccionar Todo");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel2.setText("Ingrese la cantidad que desea disminuir");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 674, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel2))
                    .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(340, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(13, 13, 13)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addGap(15, 15, 15)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
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
    ob = jTable1.getValueAt(0, 0);
        int registros = jTable1.getRowCount();
        int columnas = jTable1.getColumnCount();
       ob = jTable1.getValueAt(0, 0);
        Object obj;
        partidas = new String [registros];
        cantidades =  new float [registros];
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
                String oj = jTable1.getValueAt(i, 2).toString();
              //  System.out.println(oj);
                if (obj instanceof Boolean) {
                    bol = (Boolean) obj;
                    if (bol.booleanValue()) {
                        cantidades[contsel]= Float.valueOf(jTable1.getValueAt(i, 7).toString());
                        partidas[contsel] = jTable1.getValueAt(i, 3).toString();
                        strNombre = jTable1.getValueAt(i, 3).toString();
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
        String codicoves="";
            
            Statement st= null;
            String sql;
            
            jTextField4.setText("");
          //  busca();
            try {
                st = (Statement) conex.createStatement();
            } catch (SQLException ex) {
                Logger.getLogger(disminucionparcial.class.getName()).log(Level.SEVERE, null, ex);
            }
           ob = jTable1.getValueAt(partida, 0);
            verificarcheck();
             for(int i=0; i<contsel;i++){
                 System.out.println(partidas[i]);
             }
              
            int cuenta=0;
            String cuenta0 = "SELECT count(*) as cuenta FROM mvalus WHERE id=0 AND mpre_id='"+pres+"'";
        try {
            Statement sts = (Statement) conex.createStatement();
            ResultSet rsts = sts.executeQuery(cuenta0);
            while(rsts.next())
            {
                cuenta = rsts.getInt("cuenta");
            }
            if(cuenta==0){
                
                Date fecha = new Date();
                SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
                
                String date = formato.format(fecha);
                String insert = "INSERT INTO mvalus VALUES(0,'"+date+"', "
                        + "'"+date+"', 0, '"+pres+"', 'Parcial',1)";
                Statement inserta = (Statement) conex.createStatement();
                inserta.execute(insert);
            }
        } catch (SQLException ex) {
            Logger.getLogger(disminucionparcial.class.getName()).log(Level.SEVERE, null, ex);
        }
            for(int i=0; i<contsel;i++){
                String codicove = null, precio = null, cantidad=null;
                String datospartida = "SELECT id, IF(precunit=0, precasu, precunit) AS precio "
                        + ", cantidad, numero FROM mppres "
                        + "WHERE numegrup ="+partidas[i]+" AND (mpre_id='"+pres+"' "
                        + "OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"'))";
            try {
                Statement datos = (Statement) conex.createStatement();
                ResultSet rstdatos = datos.executeQuery(datospartida);
                while(rstdatos.next()){
                    codicove = rstdatos.getString("id");
                    precio = rstdatos.getString("precio");
                    cantidad = rstdatos.getString("cantidad");
                }
            } catch (SQLException ex) {
                Logger.getLogger(disminucionparcial.class.getName()).log(Level.SEVERE, null, ex);
            }
            
                String contar = "SELECT count(*) FROM dvalus WHERE mvalu_id=0 AND mpre_id='"+pres+"' "
                        + "AND numepart ="+partidas[i]+"";
                int contare=0;
            try {
                Statement stcontar = (Statement) conex.createStatement();
                ResultSet rstcontar = stcontar.executeQuery(contar);
                while(rstcontar.next()){
                    contare = rstcontar.getInt(1);
                }
            } catch (SQLException ex) {
                Logger.getLogger(disminucionparcial.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(contare==0){
                String insert = "INSERT INTO dvalus VALUES ('"+pres+"', 0, '"+codicove+"', 0, "+precio+""
                        + ", "+partidas[i]+", 0,0,"+numaumydis+")";
                try {
                    Statement inserta = (Statement) conex.createStatement();
                    inserta.execute(insert);
                } catch (SQLException ex) {
                    Logger.getLogger(disminucionparcial.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            String cuentapres = "SELECT count(*) FROM admppres WHERE numepart= "+partidas[i]+""
                    + " AND (mpre_id='"+pres+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"'))"
                    + " AND payd_id="+numaumydis+" AND mvalu_id=0";
            int cuentas=0;
            try {
                Statement cont = (Statement) conex.createStatement();
                ResultSet rcont = cont.executeQuery(cuentapres);
                while(rcont.next())
                {
                    cuentas = rcont.getInt(1);
                }
            } catch (SQLException ex) {
                Logger.getLogger(disminucionparcial.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(cuentas==0){
                
                if(cantidades[i]<Float.valueOf(cantidad)){
                    cantidad = String.valueOf(cantidades[i]);
                }
                String inserte = "INSERT INTO admppres VALUES "
                        + "("+numaumydis+", '"+pres+"',"+partidas[i]+",0,0, "+cantidad+")";
                try {
                    Statement stinserte = (Statement) conex.createStatement();
                    stinserte.execute(inserte);
                } catch (SQLException ex) {
                    Logger.getLogger(disminucionparcial.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            }
            contsel=0;
        
            if(entrar==1){
                
                JOptionPane.showMessageDialog(this, "No se inserto la partida "+auxpartida+" porque ya se insertó para este presupuesto");
           
            }

}    
    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        doClose(RET_CANCEL);
    }//GEN-LAST:event_cancelButtonActionPerformed

    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        doClose(RET_CANCEL);
    }//GEN-LAST:event_closeDialog

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
             partida = jTable1.rowAtPoint(evt.getPoint());
        String part =jTable1.getValueAt(partida, 3).toString();
        Object obj;
       Boolean bol;
        obj = jTable1.getValueAt(partida, 0);
                if (obj instanceof Boolean) {
                    bol = (Boolean) obj;
                    if (bol.booleanValue()) {
                       // System.out.println("Selecciono este material");
                        if(auxcont<auxpart.length){
                        auxpart[auxcont] = part;
                        auxcont++;
                        }
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
         ob = jTable1.getValueAt(partida, 0);
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable1MouseClicked
public void busca(){
        Boolean enc;
        String busqueda = jTextField4.getText().toString();
        jTextField4.setText("");
        DefaultTableModel mat = new DefaultTableModel() {@Override
       public boolean isCellEditable (int fila, int columna) {
          if(columna==0 || columna==7) {
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
            if(columna==7){
                return Integer.class;
            }
            
            return Object.class;
        }
        };
     
               
               jTable1.setModel(mat);
       
        try {
             
            Statement s = (Statement) conex.createStatement();
            String sql = "SELECT id, descri, numero, numegrup, precunit, cantidad,tipo FROM mppres WHERE numero NOT "
                    + "IN(SELECT numepart FROM dvalus WHERE (mpre_id='"+pres+"' OR mpre_id "
                    + "IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"'))) AND (mpre_id='"+pres+"' OR mpre_id IN "
                    + "(SELECT id FROM mpres WHERE mpres_id='"+pres+"'))"
                    + " AND (id LIKE'%"+busqueda+"%' || descri LIKE '%"+busqueda+"%') ORDER BY numegrup";
            
            ResultSet rs = s.executeQuery(sql);
            
            ResultSetMetaData rsMd = (ResultSetMetaData) rs.getMetaData();
            int cantidadColumnas = rsMd.getColumnCount()+1;
             mat.addColumn("");
            for (int i = 2; i <= cantidadColumnas; i++) {
                 if(i!=7){
                 mat.addColumn(rsMd.getColumnLabel(i-1));
                 }else{
                  mat.addColumn("Disminución");   
                 }
            }
             
             while (rs.next()) {
                 Object[] fila = new Object[cantidadColumnas];
                enc=false;
                    for(int j=0;j<auxcont;j++){
                         if(rs.getObject(3).toString().equals(auxpart[j])){
                                  enc=true;
                         }
                    }
                Object obj = Boolean.valueOf(enc);
           
                
                fila[0]= obj;
                for (int i = 1; i < cantidadColumnas; i++) {
                   if(i<6){
                    fila[i]=rs.getObject(i);
                   }else
                   {
                       fila[i]="";
                   }
                    
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
             
              jTable1.getColumnModel().getColumn(5).setMaxWidth(0);

             jTable1.getColumnModel().getColumn(5).setMinWidth(0);

             jTable1.getTableHeader().getColumnModel().getColumn(5).setMaxWidth(0);

             jTable1.getTableHeader().getColumnModel().getColumn(5).setMinWidth(0);
               cambiarcabecera();
    }
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        int totalfilas = jTable1.getRowCount();
      // System.out.println("totalfilas: "+totalfilas);
        for(int i=0; i<totalfilas; i++){
            auxpart[i] =String.valueOf( jTable1.getValueAt(i, 3));
        }
        auxcont = totalfilas;
        cargapartidas();        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void okButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_okButtonMouseClicked
        agrega();
        
        doClose(RET_OK);        // TODO add your handling code here:
    }//GEN-LAST:event_okButtonMouseClicked

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed

        // TODO add your handling code here:
    }//GEN-LAST:event_okButtonActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        busca();
        jTextField4.setText("");
        
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jTable1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable1KeyPressed
if (!jTable1.isEditing() && jTable1.editCellAt(jTable1.getSelectedRow(),jTable1.getSelectedColumn())) {
            char car = evt.getKeyChar();
            jTable1.getEditorComponent().requestFocusInWindow(); 
            if((car<'0'||car>'9')&& evt.getKeyCode()!=10 &&evt.getKeyCode() !=9 ){
                evt.consume();
                
            }else{
                 if (evt.getKeyCode() == 9 || evt.getKeyCode() == 10) {
                     ob = jTable1.getValueAt(partida, 0);
                     float cantidad = Float.valueOf(jTable1.getValueAt(partida, 7).toString());
                     float contratada = Float.valueOf(jTable1.getValueAt(partida, 6).toString());
                     ob = jTable1.getValueAt(partida, 0);
                     if(cantidad>contratada){
                          JOptionPane.showMessageDialog(this, "La cantidad a disminuir no puede ser mayor a la contratada, se almacenará para esta disminución, el número de la cantidad contratada");
                 }
            }
            
}
}   
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable1KeyPressed

private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
            int totalfilas = jTable1.getRowCount();
     //  System.out.println("totalfilas: "+totalfilas);
        for(int i=0; i<totalfilas; i++){
            auxpart[i] ="";
        }
        auxcont = 0;
        cargapartidas();
    
    // TODO add your handling code here:
}//GEN-LAST:event_jButton2ActionPerformed
    
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
    private javax.swing.JButton jButton5;
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
