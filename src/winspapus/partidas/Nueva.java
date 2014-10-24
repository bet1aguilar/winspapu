/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * Nueva.java
 *
 * Created on 11/09/2012, 10:08:38 AM
 */
package winspapus.partidas;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSetMetaData;
import com.mysql.jdbc.Statement;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
import winspapus.Principal;
import winspapus.tab;

/**
 *
 * @author Betmart
 */
public final class Nueva extends javax.swing.JDialog {

    /** A return status code - returned if Cancel button has been pressed */
    public static final int RET_CANCEL = 0;
    /** A return status code - returned if OK button has been pressed */
    public static final int RET_OK = 1;
    private String mtabu="", numpartida="";
    
    private int edita=0;
    private Connection conexion;
    private Principal obj;
    private Statement stmt = null;
    private List<Integer> id;
    int entero=0;
    private String codicove, numero1, descri, numegrup, refere, mbdat_id, porcgad, porcpre, porcutil;
    private String precasu, precunit, rendimi, unidad, redondeo, status, mtabu_id;
    private float precunitar , precasum;
    /** Creates new form Nueva */
    public Nueva(java.awt.Frame parent, boolean modal, String mtabu, Connection conex, Principal obj) {
        super(parent, modal);
        initComponents();
          MaskFormatter mascara ;
        try {
         mascara = new MaskFormatter("##.##");
        } catch (ParseException ex) {
            Logger.getLogger(Nueva.class.getName()).log(Level.SEVERE, null, ex);
        }
        id = new ArrayList<Integer>();
        this.mtabu = mtabu;
        
        this.setTitle("Agregar Nueva Partida");
        jLabel9.setVisible(false);
        jLabel10.setVisible(false);
        jLabel20.setVisible(false);
        
        this.conexion= conex;
        buscavalores();
        try {
            buscagrupo();
        } catch (SQLException ex) {
            Logger.getLogger(Nueva.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            contar();
            this.obj=obj;
        } catch (SQLException ex) {
            Logger.getLogger(Nueva.class.getName()).log(Level.SEVERE, null, ex);
        }
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
    public final void buscavalores(){
        String consulta = "SELECT padyga, pcosfin, pimpue, pprest, putild FROM mtabus"
                + " WHERE id='"+mtabu+"'";
        String padyga="", pcosfin="", pimpue="", pprest="", putild="";
        try {
            Statement st = (Statement) conexion.createStatement();
            ResultSet rst = st.executeQuery(consulta);
            while(rst.next()){
               padyga = rst.getString(1);
               pcosfin = rst.getString(2);
               pimpue = rst.getString(3);
               pprest = rst.getString(4);
               putild = rst.getString(5);
            }
            jTextField4.setText(padyga);
            jTextField5.setText(pprest);
            jTextField6.setText(putild);
        } catch (SQLException ex) {
            Logger.getLogger(Nueva.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     public Nueva(java.awt.Frame parent, boolean modal, String mtabu, String partida, Connection conex, Principal obj) {
        super(parent, modal);
        initComponents();
        id = new ArrayList<Integer>();
        this.mtabu = mtabu;
        jLabel9.setVisible(false);
        jLabel10.setVisible(false);
        this.setTitle("Editar Partida");
        jLabel20.setVisible(false);
         this.conexion= conex;
        numpartida = partida;
        edita = 1;
        jLabel1.setText("Editar Partida del Tabulador");
        try {
            buscagrupo();
            contar();
             cargardatos();
             this.obj=obj;
        } catch (SQLException ex) {
            Logger.getLogger(Nueva.class.getName()).log(Level.SEVERE, null, ex);
        }
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
    
     private void cargardatos() throws SQLException{
        
        Statement s = (Statement) conexion.createStatement();
       
            ResultSet rs = s.executeQuery("SELECT codicove,numero, descri, mbdat_id, porcgad,cantidad, porcpre, "
                    + "porcutil, precasu, precunit, rendimi, unidad, redondeo, numegrup FROM Mptabs WHERE mtabus_id='"+mtabu+"' AND numero="+numpartida);
        
        while (rs.next()) {
                 
                
                    jTextField1.setText(rs.getObject(1).toString());
                    jTextField2.setText(rs.getObject(14).toString());    
                    jTextField8.setText(rs.getObject(3).toString()); 
                    jComboBox1.setSelectedIndex(Integer.valueOf(rs.getObject(4).toString())-1);
                    System.out.println(rs.getObject(5).toString()+" "+Integer.valueOf(rs.getObject(6).toString()));
                    jTextField4.setText(rs.getObject(5).toString());
                    jTextField7.setText(rs.getObject(6).toString());
                    jTextField5.setText(rs.getObject(7).toString());
                    jTextField6.setText(rs.getObject(8).toString());
                    jTextField15.setText(rs.getObject(9).toString());
                    jTextField12.setText(rs.getObject(10).toString());
                    jTextField13.setText(rs.getObject(11).toString());
                    jTextField10.setText(rs.getObject(12).toString());
                    
                    if(Integer.valueOf(rs.getObject(13).toString())==0){
                        jCheckBox1.setSelected(false);
                    }else
                         jCheckBox1.setSelected(true);
                
            }
        
    }
    private void contar() throws SQLException{
        Statement cuenta = (Statement) conexion.createStatement();
        ResultSet result = cuenta.executeQuery("Select count(*) FROM mptabs where mtabus_id='"+mtabu+"' AND status=1");
       
        while(result.next()){
            numero1 = result.getObject(1).toString();
            
        }
        entero = Integer.valueOf(numero1);
        entero++;
        numero1 = Integer.toString(entero);
        jTextField2.setText(numero1);
    }
    
    public void buscagrupo() throws SQLException{
        DefaultTableModel partida = new DefaultTableModel();
        
        
     
            Statement s = (Statement) conexion.createStatement();
            ResultSet rs = s.executeQuery("SELECT id, descri FROM mbdats");
            
         
           
             ResultSetMetaData rsMd = (ResultSetMetaData) rs.getMetaData();
            int cantidadColumnas = rsMd.getColumnCount();
            
             for (int i = 1; i <= cantidadColumnas; i++) {
                
                 partida.addColumn(rsMd.getColumnLabel(i));
            }
         
                 
             
             while (rs.next()) {
                 
                 Object[] filas = new Object[cantidadColumnas];
                 
                for (int i = 0; i < cantidadColumnas; i++) {
                    filas[i]=rs.getObject(i+1);
                }
                partida.addRow(filas);
                
            }
                 
                 
                for (int i = 0; i < partida.getRowCount(); i++) {
                   
                    id.add(Integer.valueOf(partida.getValueAt(i, 0).toString()));
                    jComboBox1.addItem(partida.getValueAt(i, 1).toString());
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

        jLabel13 = new javax.swing.JLabel();
        jTextField9 = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jTextField11 = new javax.swing.JTextField();
        jTextField14 = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jTextField10 = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jTextField12 = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jTextField13 = new javax.swing.JTextField();
        jTextField15 = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jLabel12 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JFormattedTextField();
        jButton1 = new javax.swing.JButton();

        jLabel13.setText("Prestaciones %:");

        jTextField9.setToolTipText("Ingrese Código del Tabulador");

        jLabel15.setText("Precio Asumido %:");

        jTextField11.setToolTipText("Ingrese Código del Tabulador");

        jTextField14.setToolTipText("Ingrese Código del Tabulador");

        jLabel18.setText("Rendimiento:");

        setTitle("Agregar Partidas");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(100, 100, 100));

        jLabel1.setBackground(new java.awt.Color(91, 91, 95));
        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Agregar Partida del Tabulador");
        jLabel1.setOpaque(true);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 690, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jLabel2.setText("Código Covenin *:");

        jTextField1.setToolTipText("Ingrese Código del Tabulador");

        jLabel3.setText("Número *:");

        jTextField2.setEditable(false);

        jLabel4.setText("Descripción:");

        jLabel6.setText("Grupo de Partidas:");

        jTextField5.setText("0.00");
        jTextField5.setToolTipText("Ingrese El porcentaje de Prestaciones");

        jLabel7.setText("Administración y Gastos %:");

        jLabel8.setText("Utilidades %:");

        jTextField6.setText("0.00");
        jTextField6.setToolTipText("Ingrese Código del Tabulador");

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 14));
        jLabel9.setForeground(new java.awt.Color(255, 0, 0));
        jLabel9.setText("*");

        jTextField8.setToolTipText("Ingrese Código del Tabulador");

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 14));
        jLabel10.setForeground(new java.awt.Color(255, 0, 0));
        jLabel10.setText("*");

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

        cancelButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/eliminar.png"))); // NOI18N
        cancelButton.setToolTipText("Cancelar");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        jLabel11.setText("Prestaciones %:");

        jLabel14.setText("Precio Asumido:");

        jTextField10.setToolTipText("Ingrese La unidad De Medida");

        jLabel16.setText("Precio Unitario:");

        jTextField12.setEditable(false);
        jTextField12.setText("0.00");
        jTextField12.setToolTipText("Ingrese Código del Tabulador");
        jTextField12.setEnabled(false);

        jLabel17.setText("Rendimiento:");

        jTextField13.setText("1.00");
        jTextField13.setToolTipText("Ingrese Código del Tabulador");

        jTextField15.setText("0.00");
        jTextField15.setToolTipText("Ingrese Código del Tabulador");

        jLabel19.setText("Unidad de Medida:");

        jLabel20.setForeground(new java.awt.Color(255, 0, 0));
        jLabel20.setText("* Campo no puede ser vacio");

        jCheckBox1.setText("Redondeo");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jLabel12.setText("Cantidad:");

        jTextField7.setText("0.00");
        jTextField7.setToolTipText("Ingrese Código del Tabulador");
        jTextField7.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField7FocusLost(evt);
            }
        });

        jTextField4.setText("0.00");

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/añade1.fw.png"))); // NOI18N
        jButton1.setToolTipText("Agrega Nuevo Grupo de Partidas");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel20)
                .addGap(241, 241, 241))
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGap(53, 53, 53)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel17)
                            .addComponent(jLabel19)
                            .addComponent(jLabel11)
                            .addComponent(jLabel6)
                            .addComponent(jLabel12)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel4))
                            .addComponent(jLabel3)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addGap(6, 6, 6)
                                .addComponent(jLabel2)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextField13, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jTextField1, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jTextField2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 143, Short.MAX_VALUE)))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel16)
                                    .addComponent(jLabel14))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jCheckBox1)
                                    .addComponent(jTextField15, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jTextField8, javax.swing.GroupLayout.DEFAULT_SIZE, 521, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGap(522, 522, 522)
                        .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel2)
                    .addComponent(jLabel9)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel10))
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel8)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16)
                    .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel14)
                    .addComponent(jTextField15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jCheckBox1)
                        .addGap(27, 27, 27)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(jLabel20)))
                .addGap(17, 17, 17))
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
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 456, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void guardapartida(){
       String sql, cantidad;
       
         codicove = jTextField1.getText().toString();
         numero1 = jTextField2.getText().toString();
         descri = jTextField8.getText().toString();
         mbdat_id = Integer.toString(id.get(jComboBox1.getSelectedIndex()));
         System.out.println(mbdat_id);
         porcgad = jTextField4.getText().toString();
         porcpre = jTextField5.getText().toString();
         porcutil = jTextField6.getText().toString();
         unidad = jTextField10.getText().toString();
         precunit = jTextField12.getText().toString();
         precasu = jTextField15.getText().toString();
         rendimi = jTextField13.getText().toString();
         cantidad = jTextField7.getText().toString();
         if(jCheckBox1.isSelected()){
             
             redondeo = "1";
         }else{
             redondeo = "0";
         }
        
         
         sql = "INSERT INTO Mptabs "
                 + ""
                 + "VALUES ('"+jTextField1.getText().toString()+"',"
                 + " "+jTextField2.getText().toString()+",  "
                 + ""+jTextField2.getText().toString()+", "+ 

                                                        "'"+descri+"'," + 

                                                        "'', " + 

                                                        "'"+mbdat_id+"', " +

                                                        ""+porcgad+", " + 
                 
                                                        ""+porcpre+", " + 
                 
                                                        ""+porcutil+", " + 
                                                        
                                                        ""+precasu+", " + 
                                                        
                                                        ""+precunit+", " + 
                                                        
                                                        ""+rendimi+", " +
                 
                                                        "'"+unidad+"', " +
                                                        
                                                        "'"+redondeo+"', " +
                 
                                                        "'1', " +
                 
                                                        "'"+mtabu+"', " +
                
                                                        ""+cantidad+", NULL, '');";
         System.out.println(sql);
         
         try {
                stmt.execute(sql);
            } catch (SQLException ex) {
                Logger.getLogger(tab.class.getName()).log(Level.SEVERE, null, ex);
            }
        
    }
    
    
    public void editapartida(){
        String sql, cantidad;
       
         codicove = jTextField1.getText().toString();
         numero1 = jTextField2.getText().toString();
         descri = jTextField8.getText().toString();
         mbdat_id = Integer.toString(id.get(jComboBox1.getSelectedIndex()));
         System.out.println(mbdat_id);
         porcgad = jTextField4.getText().toString();
         porcpre = jTextField5.getText().toString();
         porcutil = jTextField6.getText().toString();
         unidad = jTextField10.getText().toString();
         precunit = jTextField12.getText().toString();
         precasu = jTextField15.getText().toString();
         rendimi = jTextField13.getText().toString();
         cantidad = jTextField7.getText().toString();
         if(jCheckBox1.isSelected()){
             
             redondeo = "1";
         }else{
             redondeo = "0";
         }
        
         
         sql = "UPDATE Mptabs set codicove='"+jTextField1.getText().toString()+"', "+ 

                                                        "descri='"+descri+"'," + 

                                                        "refere='"+refere+"', " + 

                                                        "mbdat_id='"+mbdat_id+"', " +

                                                        "porcgad="+porcgad+", " + 
                 
                                                        "porcpre="+porcpre+", " + 
                 
                                                        "porcutil="+porcutil+", " + 
                                                        
                                                        "precasu="+precasu+", " + 
                                                        
                                                        "precunit="+precunit+", " + 
                                                        
                                                        "rendimi="+rendimi+", " +
                 
                                                        "unidad='"+unidad+"', " +
                                                        
                                                        "redondeo='"+redondeo+"', " +
                 
                                                        "status='1', " +     
                                                        
                
                                                        "cantidad="+cantidad+" WHERE mtabus_id='"+mtabu+"' AND numero="+numpartida+";";
         System.out.println(sql);
         
         try {
                stmt.execute(sql);
            } catch (SQLException ex) {
                Logger.getLogger(tab.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        
    }//GEN-LAST:event_okButtonActionPerformed
    
    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        doClose(RET_CANCEL);
    }//GEN-LAST:event_cancelButtonActionPerformed

    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        doClose(RET_CANCEL);
    }//GEN-LAST:event_closeDialog

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        precunitar = Float.parseFloat(jTextField12.getText().toString());
        if(jCheckBox1.isSelected()){
           
           precasum = (float) (Math.rint(precunitar));
           jTextField15.setText(Float.toString(precasum)+"0");
        }else{
            jTextField15.setText("0.00");
        }
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jTextField7FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField7FocusLost
if(jTextField7.getText().equals("")){
    
jTextField7.setText("0.00");
}// TODO add your handling code here:
    }//GEN-LAST:event_jTextField7FocusLost

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    String grupo="";
                       nuevogrupo nuevo = new nuevogrupo(null, true, "",conexion);
        int x=(obj.getWidth()/2)-295;
           int y=(obj.getHeight()/2-203);
            nuevo.setBounds(x, y, 450, 300);            
           nuevo.setVisible(true);// TODO add your handling code here:
        try {
            buscagrupo();
            grupo = nuevo.grupo;
            jComboBox1.setSelectedItem(grupo);
        } catch (SQLException ex) {
            Logger.getLogger(Nueva.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void okButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_okButtonMouseClicked
        int evalua= 0;
        String sql="";
        if(jTextField1.getText().equals("")){
            evalua=1;
        }
        if(jTextField8.getText().equals("")){
            evalua=1;
        }
        
        if(evalua==0){
            jLabel9.setVisible(false);
            jLabel10.setVisible(false);
            jLabel20.setVisible(false);
            
            if(edita==0){
            guardapartida();
            JOptionPane.showMessageDialog(null, "La partida ha sido guardada");
            }
            else{
                editapartida();
                
                 JOptionPane.showMessageDialog(null, "La partida ha sido guardada");
            }
            doClose(RET_OK);
        }
        else{
            jLabel9.setVisible(true);
            jLabel10.setVisible(true);
            jLabel20.setVisible(true);
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
    private javax.swing.JButton jButton1;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField12;
    private javax.swing.JTextField jTextField13;
    private javax.swing.JTextField jTextField14;
    private javax.swing.JTextField jTextField15;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JFormattedTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    private javax.swing.JButton okButton;
    // End of variables declaration//GEN-END:variables
    private int returnStatus = RET_CANCEL;
}
