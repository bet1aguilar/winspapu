/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * importarpartidas.java
 *
 * Created on 30/08/2013, 11:13:44 AM
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
 * @author Betmart
 */
public class importarpartidas extends javax.swing.JDialog {

    /** A return status code - returned if Cancel button has been pressed */
    public static final int RET_CANCEL = 0;
    private Connection conex;
    String pres, seleccionado;
    /** A return status code - returned if OK button has been pressed */
    public static final int RET_OK = 1;
    int auxcont = 0;
    String[] auxpart;
    private int partida;
    private String[] partidas;
    private int contsel;
    private String nuevonum;
    private int nuevo;
    private int nuevonumegrup;
    private int insertar;
    private String rendimi;
    float contmano=0, contequip=0, contmat=0;
    private String numero;
    Presupuesto presu;
    /** Creates new form importarpartidas */
    public importarpartidas(java.awt.Frame parent, boolean modal, Connection conex, String pres, Presupuesto presu) {
        super(parent, modal);
        initComponents();
        this.presu = presu; 
        this.conex = conex;
       
        seleccionado=pres;
        hacemodelo();
        
        this.pres=jComboBox1.getSelectedItem().toString();
        // Close the dialog when Esc is pressed
        String cancelName = "cancel";
        InputMap inputMap = getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), cancelName);
        ActionMap actionMap = getRootPane().getActionMap();
         jTable1.setOpaque(true);
    jTable1.setShowHorizontalLines(true);
    jTable1.setShowVerticalLines(false);
    jTable1.getTableHeader().setSize(new Dimension(25,40));
    jTable1.getTableHeader().setPreferredSize(new Dimension(30,40));
    jTable1.setRowHeight(25);
        actionMap.put(cancelName, new AbstractAction() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                doClose(RET_CANCEL);
            }
        });
    }
    public final void hacemodelo(){
        String consulta = "SELECT id FROM mpres where status=1 AND id!='"+seleccionado+"'";
        try {
            Statement st = (Statement) conex.createStatement();
            ResultSet rst = st.executeQuery(consulta);
            jComboBox1.removeAllItems();
            int i=0;
            while(rst.next()){
                jComboBox1.addItem(rst.getObject("id"));
               i++;
            }
            
            if(i>0)
            jComboBox1.setSelectedIndex(0);
            else{
             JOptionPane.showMessageDialog(null, "No hay otros presupuestos para importar");
             doClose(RET_CANCEL);
            }
        } catch (SQLException ex) {
            Logger.getLogger(importarpartidas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void busca(){
        Boolean enc;
        String busqueda = jTextField1.getText().toString();
        jTextField1.setText("");
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
            ResultSet rs = s.executeQuery("SELECT id, descri, numero, numegrup, precasu, precunit, tipo FROM Mppres m WHERE (m.mpre_id = '"+pres+"' OR m.mpre_id IN (SELECT id FROM mpres "
                                    + "WHERE mpres_id = '"+pres+"'))"
                                    + " AND status=1  AND (id LIKE'%"+busqueda+"%' || descri LIKE '%"+busqueda+"%') ORDER BY numegrup");
            
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
            Logger.getLogger(importarpartidas.class.getName()).log(Level.SEVERE, null, ex);
        }
         jTable1.getColumnModel().getColumn(3).setMaxWidth(0);

             jTable1.getColumnModel().getColumn(3).setMinWidth(0);

             jTable1.getTableHeader().getColumnModel().getColumn(3).setMaxWidth(0);

             jTable1.getTableHeader().getColumnModel().getColumn(3).setMinWidth(0);
             
             int cantidadfilas= jTable1.getRowCount();
             auxpart= new String[cantidadfilas];
               cambiarcabecera();
    }
    public void cambiarcabecera(){
       JTableHeader th = jTable1.getTableHeader();
       TableColumnModel tcm = th.getColumnModel();
       TableColumn tc = tcm.getColumn(0); 
       tc.setPreferredWidth(5);
       tc = tcm.getColumn(1); 
       tc.setHeaderValue("Código");
       tc.setPreferredWidth(50);
       tc = tcm.getColumn(2); 
        tc.setHeaderValue("Descripción");
        tc.setPreferredWidth(250);
        tc = tcm.getColumn(4); 
        tc.setHeaderValue("Número");
        tc.setPreferredWidth(20);
        tc = tcm.getColumn(5); 
        tc.setHeaderValue("Precio Asumido");
        tc.setPreferredWidth(20);
        tc = tcm.getColumn(6); 
        tc.setHeaderValue("Precio Unitario");
        tc.setPreferredWidth(20);
        tc = tcm.getColumn(7); 
        tc.setHeaderValue("Tipo");
        tc.setPreferredWidth(20);
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
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jPanel5 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        cancelButton = new javax.swing.JButton();
        okButton = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        jPanel3.setBackground(new java.awt.Color(100, 100, 100));

        jLabel1.setBackground(new java.awt.Color(91, 91, 95));
        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Importar Partidas de Presupuestos");
        jLabel1.setOpaque(true);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 663, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
        );

        jLabel2.setText("Seleccione Presupuesto:");

        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(352, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(12, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addContainerGap())
        );

        jLabel3.setText("Buscar:");

        jTextField1.setToolTipText("Busque Número, Código COVENIN o Descripción");

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/buscar1.fw.png"))); // NOI18N
        jButton1.setToolTipText("Buscar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTable1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
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
        jScrollPane1.setViewportView(jTable1);

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/selecc1.fw.png"))); // NOI18N
        jButton2.setToolTipText("Seleccionar Todo");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/quita1.fw.png"))); // NOI18N
        jButton3.setToolTipText("Deseleccionar Todos");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        cancelButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/eliminar.png"))); // NOI18N
        cancelButton.setToolTipText("Cancelar");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        okButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/anade.fw.png"))); // NOI18N
        okButton.setToolTipText("Importar Partidas");
        okButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                okButtonMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        getRootPane().setDefaultButton(okButton);

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/limpiar.fw.png"))); // NOI18N
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 643, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jLabel3)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
            for (j = 0; j < 1; j++) {
Object obj1 = jTable1.getValueAt(i, 3);
                obj = jTable1.getValueAt(i, j);
                if (obj instanceof Boolean) {
                    bol = (Boolean) obj;
                    if (bol.booleanValue()) {
                        
                        partidas[contsel] = jTable1.getValueAt(i, 3).toString();
                        strNombre =jTable1.getValueAt(i, 3).toString();
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
            try {
            Statement st= null;
            String sql;
            partidas = new String [auxpart.length];
            System.arraycopy(auxpart, 0, partidas, 0, auxpart.length);
            contsel= auxcont;
            jTextField1.setText("");
            busca();
            try {
                st = (Statement) conex.createStatement();
            } catch (SQLException ex) {
                Logger.getLogger(Presupuesto.class.getName()).log(Level.SEVERE, null, ex);
            }
           
           
           
           // verificarcheck();
            
            
            String codicove="";
            
            for(int i=0; i<contsel;i++){
                String sqlnumero = "SELECT numero FROM mppres where mpre_id='"+seleccionado+"' OR mpre_id IN (SELECT id from mpres where mpres_id ='"+seleccionado+"' GROUP BY id) ORDER BY numero DESC LIMIT 1";
               
                ResultSet rst = st.executeQuery(sqlnumero);
                while(rst.next()){
                    nuevonum = rst.getObject(1).toString();
                    nuevo = Integer.parseInt(nuevonum)+1;
                }
                String sqlnumegrup = "SELECT numegrup FROM mppres where mpre_id='"+seleccionado+"' OR mpre_id IN (SELECT id from mpres where mpres_id ='"+seleccionado+"' GROUP BY id) ORDER BY numegrup DESC LIMIT 1";
                Statement str = (Statement) conex.createStatement();
                ResultSet rstr = str.executeQuery(sqlnumegrup);
                while(rstr.next()){
                    nuevonumegrup = rstr.getInt(1)+1;
                }
                insertar=1;
            
                
              
                int cuenta=0;
              
                   String rendimiento = "";
                
                
                if(cuenta==0){
                    sql="INSERT INTO mppres (mpre_id, id, numero, numegrup, descri, idband,porcgad, "
                        + "rendimi, unidad, redondeo, status, cantidad, tipo, nropresupuesto, nrocuadro, "
                            + "tiporec, cron,rango, lapso) "  
                        + "SELECT '"+seleccionado+"', id, "+nuevo+", "+nuevonumegrup+", descri, idband, "
                            + "porcgad, rendimi, unidad, redondeo, status, cantidad, tipo, nropresupuesto,"
                            + "nrocuadro, tiporec, 0,0,0 FROM mppres WHERE numero='"+partidas[i]+"' AND "
                            + "(mpre_id='"+pres+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"'))";
                try {
                    st.execute(sql);
                    System.out.println(sql);
                    String select = "SELECT rendimi, id FROM mppres WHERE numero='"+nuevo+"' AND mpre_id='"+seleccionado+"'";
                    Statement sts = (Statement) conex.createStatement();
                    ResultSet rsts = sts.executeQuery(select);
                    while(rsts.next()){
                        rendimiento = rsts.getString(1);
                        codicove = rsts.getString(2);
                    }
                    
                    //pres es el presupuesto seleccionado con el jcombobox y el seleccionado es el presupuesto seleccionado al que se agregaran las partidas
                    String selecnumero = "SELECT numero FROM mppres WHERE id='"+codicove+"' AND mpre_id'"+pres+"'";
                } catch (SQLException ex) {
                    Logger.getLogger(Presupuesto.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                
                presu.numero= String.valueOf(nuevonumegrup);
                presu.insertar=1;
               agregarmat(partidas[i]);
               agregaequipo(partidas[i]);
            agregamano(partidas[i]);
            presu.contmano=contmano;
            presu.contmat=contmat;
            presu.contequipo = contequip;
               presu.calculapartida(String.valueOf(nuevo), seleccionado, 0);
                presu.cargartotal();
                presu.buscapartida();
                presu.cargapresupuesto();
            }else{
                    entrar=1;
                    auxpartida=partidas[i];
                }
                
            }
            contsel=0;
            JOptionPane.showMessageDialog(this, "Se  insertaron las partidas para este presupuesto");
        } catch (SQLException ex) {
            Logger.getLogger(importarpartidas.class.getName()).log(Level.SEVERE, null, ex);
        }
            if(entrar==1){
                
                JOptionPane.showMessageDialog(this, "No se inserto la partida "+auxpartida+" porque ya se insertó para este presupuesto");
            try {
                presu.buscapartida();
            } catch (SQLException ex) {
                Logger.getLogger(importarpartidas.class.getName()).log(Level.SEVERE, null, ex);
            }
            }

}
    
    public void agregaequipo(String partidas){
        String cantidad, precio ;
           int cuantos=0;
           float valor=0;
           String codiequip = null, mtabu = "", descri, desperdi, unidad;
        
           
           
        String insert = "INSERT INTO deppres "
                + "SELECT '"+seleccionado+"', mppre_id, mepre_id,'"+nuevo+"', cantidad, "
                + "precio, status FROM deppres WHERE mpre_id = '"+pres+"' AND "
                + "numero='"+partidas+"'";
            try {
                Statement str = (Statement) conex.createStatement();
                str.execute(insert);
            } catch (SQLException ex) {
                Logger.getLogger(importarpartidas.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            String select= "SELECT IF(me.deprecia=0,de.precio*de.cantidad,"
                    + "de.precio*me.deprecia*de.cantidad)"
                    + " AS valor, me.id as codiequip FROM deppres as de, mepres as me "
                    + "WHERE me.mpre_id=de.mpre_id AND de.mpre_id='"+seleccionado+"' AND de.mepre_id=me.id AND "
                    + "de.numero = '"+nuevo+"'";
            try {
                Statement str = (Statement) conex.createStatement();
                ResultSet rstr = str.executeQuery(select);
                while(rstr.next()){
                    contequip+=rstr.getFloat(1);
                    codiequip = rstr.getString(2);
                    
                    int cuenta=0;
                String busca = "SELECT count(*) FROM mepres WHERE mpre_id='"+seleccionado+"' AND"
                        + " id='"+codiequip+"'";
                
                Statement stbusca= (Statement) conex.createStatement();
                ResultSet rstbusca = stbusca.executeQuery(busca);
                while(rstbusca.next()){
                    cuenta = rstbusca.getInt(1);
                }
                
                if(cuenta==0){
                    String insertamat = "INSERT INTO mepres "
                            + "SELECT '"+seleccionado+"', id, descri, deprecia, precio, status "
                            + " FROM mepres WHERE mpre_id='"+pres+"' AND id='"+codiequip+"'";
                    Statement inserta = (Statement) conex.createStatement();
                    inserta.execute(insertamat);
                }
                }
                
                
            } catch (SQLException ex) {
                Logger.getLogger(importarpartidas.class.getName()).log(Level.SEVERE, null, ex);
            }
            
           
           
           
        
          
       
    }
     public void agregamano(String partidas){
        String cantidad, precio ;
           int cuantos=0;
           float valor=0;
           String codimano = null, mtabu = "", descri, desperdi, unidad;
        
           
           
        String insert = "INSERT INTO dmoppres "
                + "SELECT '"+seleccionado+"', mmopre_id, mppre_id,'"+nuevo+"', cantidad, "
                + "bono, salario,subsidi, status FROM dmoppres WHERE mpre_id = '"+pres+"' AND "
                + "numero='"+partidas+"'";
            try {
                Statement str = (Statement) conex.createStatement();
                str.execute(insert);
            } catch (SQLException ex) {
                Logger.getLogger(importarpartidas.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            String select= "SELECT dmo.cantidad*dmo.salario "
                    + " AS valor, mmo.id as codiequip FROM dmoppres as dmo, mmopres as mmo "
                    + "WHERE mmo.mpre_id=dmo.mpre_id AND dmo.mpre_id='"+seleccionado+"' AND dmo.mmopre_id=mmo.id AND "
                    + "dmo.numero = '"+nuevo+"'";
            try {
                Statement str = (Statement) conex.createStatement();
                ResultSet rstr = str.executeQuery(select);
                while(rstr.next()){
                    contmano+=rstr.getFloat(1);
                    codimano = rstr.getString(2);
                     int cuenta=0;
                String busca = "SELECT count(*) FROM mmopres WHERE mpre_id='"+seleccionado+"' AND"
                        + " id='"+codimano+"'";
                
                Statement stbusca= (Statement) conex.createStatement();
                ResultSet rstbusca = stbusca.executeQuery(busca);
                while(rstbusca.next()){
                    cuenta = rstbusca.getInt(1);
                }
                
                if(cuenta==0){
                    String insertamat = "INSERT INTO mmopres "
                            + "SELECT '"+seleccionado+"', id, descri, bono, salario, subsid"
                            + " FROM mmopres WHERE mpre_id='"+pres+"' AND id='"+codimano+"'";
                    Statement inserta = (Statement) conex.createStatement();
                    inserta.execute(insertamat);
                }
                }
                
               
            } catch (SQLException ex) {
                Logger.getLogger(importarpartidas.class.getName()).log(Level.SEVERE, null, ex);
            }
            
           
           
           
        
          
       
    }
    public void agregarmat(String partidas){
        String cantidad, precio ;
           int cuantos=0;
           float valor=0;
           String codimate = null, mtabu = "", descri, desperdi, unidad;
        
           
           
        String insert = "INSERT INTO dmpres "
                + "SELECT '"+seleccionado+"', mppre_id, mmpre_id,'"+nuevo+"', cantidad, "
                + "precio, status FROM dmpres WHERE mpre_id = '"+pres+"' AND "
                + "numepart='"+partidas+"'";
            try {
                Statement str = (Statement) conex.createStatement();
                str.execute(insert);
            } catch (SQLException ex) {
                Logger.getLogger(importarpartidas.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            String select= "SELECT IF(mm.desperdi=0,dm.precio*dm.cantidad,((dm.precio+(dm.precio*(mm.desperdi/100)))*dm.cantidad))"
                    + " AS valor, mm.id as codimate FROM dmpres as dm, mmpres as mm "
                    + "WHERE mm.mpre_id=dm.mpre_id AND dm.mpre_id='"+seleccionado+"' AND dm.mmpre_id=mm.id AND "
                    + "dm.numepart = '"+nuevo+"'";
            try {
                Statement str = (Statement) conex.createStatement();
                ResultSet rstr = str.executeQuery(select);
                while(rstr.next()){
                    contmat+=rstr.getFloat(1);
                    codimate = rstr.getString(2);
                    int cuenta=0;
                String busca = "SELECT count(*) FROM mmpres WHERE mpre_id='"+seleccionado+"' AND"
                        + " id='"+codimate+"'";
                
                Statement stbusca= (Statement) conex.createStatement();
                ResultSet rstbusca = stbusca.executeQuery(busca);
                while(rstbusca.next()){
                    cuenta = rstbusca.getInt(1);
                }
                
                if(cuenta==0){
                    String insertamat = "INSERT INTO mmpres "
                            + "SELECT '"+seleccionado+"', id, descri, desperdi, precio, unidad, status "
                            + "FROM mmpres WHERE mpre_id='"+pres+"' AND id='"+codimate+"'";
                    Statement inserta = (Statement) conex.createStatement();
                    inserta.execute(insertamat);
                }
                }
                
                
            } catch (SQLException ex) {
                Logger.getLogger(importarpartidas.class.getName()).log(Level.SEVERE, null, ex);
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
        
        
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable1MouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
      busca();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        pres= jComboBox1.getSelectedItem().toString();
        busca();
               
        
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void okButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_okButtonMouseClicked
        
        
        agrega();
         
        doClose(RET_OK);        // TODO add your handling code here:
    }//GEN-LAST:event_okButtonMouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        int totalfilas = jTable1.getRowCount();
      // System.out.println("totalfilas: "+totalfilas);
        for(int i=0; i<totalfilas; i++){
            auxpart[i] =String.valueOf( jTable1.getValueAt(i, 3));
        }
        auxcont = totalfilas;
        busca();
        
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        int totalfilas = jTable1.getRowCount();
     //  System.out.println("totalfilas: "+totalfilas);
        for(int i=0; i<totalfilas; i++){
            auxpart[i] ="";
        }
        auxcont = 0;
        busca();
        
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
    jTextField1.setText("");
    busca();
    
        
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton4ActionPerformed
    
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
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JButton okButton;
    // End of variables declaration//GEN-END:variables
    private int returnStatus = RET_CANCEL;
}
