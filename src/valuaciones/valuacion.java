/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package valuaciones;

import java.sql.Connection;
import com.mysql.jdbc.ResultSetMetaData;
import config.Redondeo;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import presupuestos.Presupuesto;

public final class valuacion extends javax.swing.JDialog {
    BigDecimal cantidad;
    Redondeo redon = new Redondeo();
    int pierdefoco=0;
    TableCellEditor editor;
    public static final int RET_CANCEL = 0;
    public BigDecimal impu, acum, impuesto;
    int busca=0;
    public BigDecimal estavalu=new BigDecimal("0.00");
    int filapart = 0;
    int lapso=0;
    public static final int RET_OK = 1;
    private Connection conex;
    String pres;
    String mvalu;
    Presupuesto mpres;
    public BigDecimal subtotal, total;
    int numval = 0;
    String imp = "";
    String tipo;

    public valuacion(java.awt.Frame parent, boolean modal, Connection conex, String pres, Presupuesto mpres) {
        super(parent,false);
        initComponents();
        buttonGroup1.add(jRadioButton1);
        buttonGroup1.add(jRadioButton2);
        jTextField3.setEditable(false);
        mvalu = jSpinner1.getValue().toString();
        jDateChooser1.setDate(new Date());
        jDateChooser2.setDate(new Date());
        this.pres = pres;
        this.mpres = mpres;
        this.conex = conex;
        try {
            cargapresupuesto();
        } catch (SQLException ex) {
            Logger.getLogger(valuacion.class.getName()).log(Level.SEVERE, null, ex);
        }
         jTable1.setOpaque(true);
    jTable1.setShowHorizontalLines(true);
    jTable1.setShowVerticalLines(false);
    jTable1.getTableHeader().setSize(new Dimension(25,40));
    jTable1.getTableHeader().setPreferredSize(new Dimension(30,40));
    jTable1.setRowHeight(25);
    //jTable1.getColumnModel().getColumn( 5 ).setCellEditor(new TableEditor()); 
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
    public int getlapso(){
        if(jRadioButton1.isSelected()){
            return 0;
        }else{
            return 1;
        }
        
    }
    public void calculacantpart(String partida){
        BigDecimal cantval=new BigDecimal("0.00");
       BigDecimal cantcont=new BigDecimal("0.00");  
       BigDecimal valdismi=new BigDecimal("0.00");
        String consultar = "SELECT IFNULL(cantidad,0) FROM mppres WHERE numero="+partida+" "
                + "AND (mpre_id='"+pres+"' OR mpre_id IN "
                + "(SELECT id FROM mpres WHERE mpres_id='"+pres+"' GROUP BY id))";
        System.out.println(consultar);
        try {
            Statement stconsulta = conex.createStatement();
            ResultSet rstconsulta = stconsulta.executeQuery(consultar);
           
            while(rstconsulta.next()){
                cantcont = rstconsulta.getBigDecimal(1);
            }
           
        } catch (SQLException ex) {
            Logger.getLogger(valuacion.class.getName()).log(Level.SEVERE, null, ex);
        }
        String consulte = "SELECT IFNULL(SUM(cantidad),0) FROM dvalus WHERE numepart = "+partida+" AND "
                + "mpre_id='"+pres+"'";
         try {
            Statement stconsulta = conex.createStatement();
            ResultSet rstconsulta = stconsulta.executeQuery(consulte);
            
            while(rstconsulta.next()){
                cantval = rstconsulta.getBigDecimal(1);
            }
           
        } catch (SQLException ex) {
            Logger.getLogger(valuacion.class.getName()).log(Level.SEVERE, null, ex);
        }
         String disminuciones = "SELECT IFNULL(SUM(disminucion),0) FROM admppres WHERE mpre_id='"+pres+"' AND numepart="+partida+"";
        try {
            Statement stdismi = conex.createStatement();
            ResultSet rstdismi = stdismi.executeQuery(disminuciones);
            while(rstdismi.next()){
                valdismi = rstdismi.getBigDecimal(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(valuacion.class.getName()).log(Level.SEVERE, null, ex);
        }
         BigDecimal cantidisp = cantcont.subtract(cantval).subtract(valdismi);
         
         if(cantidisp.doubleValue()>0){
             jLabel9.setText("Cantidad Disponible a Valuar:");
             jLabel10.setText(String.valueOf(cantidisp));
         }
         if(cantidisp.doubleValue()==0){
             jLabel9.setText("No tiene cantidades disponibles a valuar");
             jLabel10.setText(String.valueOf(""));
         }
         if(cantidisp.doubleValue()<0)
         {
             jLabel9.setText("Aumento de:");
             jLabel10.setText(String.valueOf(cantidisp.doubleValue()*-1));
         }
    }
            
    public void cargapresupuesto() throws SQLException 
    {
        jTextField1.setText(pres);
       
        String sql = "SELECT nombre From mpres WHERE id='" + pres + "'";
        Statement st = (Statement) conex.createStatement();
        ResultSet rst = st.executeQuery(sql);
        while (rst.next()) 
            jTextArea1.setText(rst.getObject(1).toString());
        
        String cuenta = "SELECT count(id) FROM mvalus WHERE mpre_id='" + pres + "' AND id!=0 AND status=1";
        Statement stm = (Statement) conex.createStatement();
        ResultSet rstm = stm.executeQuery(cuenta);
        while (rstm.next()) {
            numval = Integer.parseInt(rstm.getObject(1).toString());
        }
        if (numval == 0) {
            numval = 1;
        }

        jTextField2.setText(String.valueOf(numval));
        jRadioButton1.setSelected(true);
        SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy-MM-dd");
        Date ini = null, fin = null;
        String tipos = null;
        String con = "SELECT desde, hasta, tipo,lapso FROM mvalus where id=" + mvalu + " AND mpre_id='" + pres+"'";
        Statement ste = conex.createStatement();
        ResultSet rste = ste.executeQuery(con);
        while (rste.next()) {
            try {
                if (rste.getObject(1) != null) {
                    ini = formatoDelTexto.parse(rste.getObject(1).toString());
                }
                if (rste.getObject(2) != null) {
                    fin = formatoDelTexto.parse(rste.getObject(2).toString());
                }
                if (rste.getObject(3) != null) {
                    tipos = rste.getObject(3).toString();
                }
                lapso=rste.getInt("lapso");

            } catch (ParseException ex) {
                Logger.getLogger(valuacion.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        jDateChooser1.setDate(ini);
        jDateChooser2.setDate(fin);
        jComboBox1.setSelectedItem(tipos);
        if(lapso==1){
            jRadioButton1.setSelected(true);
        }else
        {
            jRadioButton2.setSelected(false);
        }
        buscapartida();
    }

    public int getReturnStatus() {
        return returnStatus;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jLabel3 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jSpinner1 = new javax.swing.JSpinner();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel6 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jLabel7 = new javax.swing.JLabel();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        jLabel8 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jTextField3 = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        jTextField6 = new javax.swing.JTextField();
        jTextField7 = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        jTextField9 = new javax.swing.JTextField();
        jTextField10 = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        jPanel4.setBackground(new java.awt.Color(100, 100, 100));

        jLabel1.setBackground(new java.awt.Color(91, 91, 95));
        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Valuaciones del Presupuesto");
        jLabel1.setOpaque(true);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 768, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

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
        cancelButton.setToolTipText("Salir");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/nuevo.png"))); // NOI18N
        jButton1.setToolTipText("Nueva Valuación");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/borra.png"))); // NOI18N
        jButton2.setToolTipText("Eliminar Valuación");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/borrar.fw.png"))); // NOI18N
        jButton3.setToolTipText("Borrar Partida");
        jButton3.setEnabled(false);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/partida.png"))); // NOI18N
        jButton4.setToolTipText("Buscar Partida");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(18, Short.MAX_VALUE)
                .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        getRootPane().setDefaultButton(okButton);

        jLabel2.setText("Obra Nro.:");

        jTextField1.setEditable(false);
        jTextField1.setToolTipText("Ingrese Código de la Partida");

        jRadioButton1.setText("Con lapso");
        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton1ActionPerformed(evt);
            }
        });

        jRadioButton2.setText("Sin lapso");
        jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton2ActionPerformed(evt);
            }
        });

        jLabel3.setText("Nro. de Valuaciones:");

        jTextField2.setEditable(false);
        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        jLabel4.setText("No. Valuación:");

        jSpinner1.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(1)));
        jSpinner1.setToolTipText("");
        jSpinner1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner1StateChanged(evt);
            }
        });
        jSpinner1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jSpinner1KeyTyped(evt);
            }
        });

        jLabel5.setText("Nombre:");

        jTextArea1.setColumns(20);
        jTextArea1.setEditable(false);
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(3);
        jTextArea1.setWrapStyleWord(true);
        jScrollPane1.setViewportView(jTextArea1);

        jLabel6.setText("Desde:");

        jDateChooser1.setDateFormatString("dd-MM-yyyy");

        jLabel7.setText("Hasta:");

        jDateChooser2.setDateFormatString("dd-MM-yyyy");

        jLabel8.setText("Tipo Valuación:");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Parcial", "Unica", "Final", "Reconsideración", "Otro" }));
        jComboBox1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jComboBox1MouseClicked(evt);
            }
        });
        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });

        jTextField3.setEditable(false);

        jTable1.setFont(new java.awt.Font("Tahoma", 0, 10));
        jTable1.setSelectionBackground(new java.awt.Color(216, 141, 0));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jTable1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTable1FocusGained(evt);
            }
        });
        jTable1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTable1KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTable1KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTable1KeyTyped(evt);
            }
        });
        jScrollPane2.setViewportView(jTable1);

        jButton7.setText("Liquidación");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setText("Valuación");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton9.setText("Inspección");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButton5.setText("Avance");
        jButton5.setToolTipText("Hoja de Avance");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jButton7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton5)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton7)
                    .addComponent(jButton8)
                    .addComponent(jButton9)
                    .addComponent(jButton5))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37))
        );

        jLabel13.setText("Sub-total Valuación:");

        jLabel14.setText("Imp. Valuación:");

        jLabel15.setText("Total Valuación:");

        jLabel16.setText("Total Acumulado:");

        jTextField4.setEditable(false);
        jTextField4.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField4ActionPerformed(evt);
            }
        });

        jTextField5.setEditable(false);
        jTextField5.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jTextField6.setEditable(false);
        jTextField6.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jTextField7.setEditable(false);
        jTextField7.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(14, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel16)
                    .addComponent(jLabel15)
                    .addComponent(jLabel14)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextField4)
                    .addComponent(jTextField5)
                    .addComponent(jTextField6)
                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel13)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel14)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel15)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel16)
                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel18.setText("Ingrese Código:");

        jLabel19.setText("Cantidad en Valuación:");

        jLabel17.setText("Ingrese Número:");

        jTextField8.setToolTipText("Número de Partida");
        jTextField8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField8MouseClicked(evt);
            }
        });
        jTextField8.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField8FocusLost(evt);
            }
        });
        jTextField8.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField8KeyTyped(evt);
            }
        });

        jTextField9.setToolTipText("Código COVENIN");
        jTextField9.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField9FocusLost(evt);
            }
        });

        jTextField10.setToolTipText("Cantidad Valuada");
        jTextField10.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField10FocusLost(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField9, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jLabel19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19)
                    .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18)
                    .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17))
                .addContainerGap())
        );

        jLabel9.setText("Cantidad Disponible a Valuar:");

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(jLabel9)
                .addGap(18, 18, 18)
                .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, 81, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel5)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 688, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField3, javax.swing.GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jRadioButton1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jRadioButton2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 124, Short.MAX_VALUE)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(26, 26, 26)
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(106, 106, 106)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 744, Short.MAX_VALUE))
                .addGap(14, 14, 14))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(430, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jRadioButton2)
                    .addComponent(jRadioButton1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel6)
                    .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 606, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        doClose(RET_CANCEL);
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        doClose(RET_CANCEL);
    }//GEN-LAST:event_closeDialog

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed
    }//GEN-LAST:event_jRadioButton1ActionPerformed

    private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton2ActionPerformed
    }//GEN-LAST:event_jRadioButton2ActionPerformed

    private void jTextField4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField4ActionPerformed
    }//GEN-LAST:event_jTextField4ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        nuevo(0);
    }//GEN-LAST:event_jButton1ActionPerformed

    public void nuevo(int nuevo) {
//
        if (nuevo == 0) {
            int valor = Integer.parseInt(jSpinner1.getValue().toString()) + 1;
            jSpinner1.setValue(valor);

        }
        jDateChooser1.setDate(null);
        jDateChooser2.setDate(null);
        DefaultTableModel metabs = new DefaultTableModel() {

            @Override
            public boolean isCellEditable(int row, int column) {
                if (column == 4) {
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            public Class getColumnClass(int columna) {
                if (columna == 0) {
                    return Integer.class;
                }
                
                return Object.class;
            }
        };
        Date fecha=null;
        String consultafechafin = "SELECT hasta FROM mvalus WHERE mpre_id='"+pres+"' ORDER BY hasta DESC LIMIT 1";
        try {
            Statement sfecha = conex.createStatement();
            ResultSet rsfecha = sfecha.executeQuery(consultafechafin);
            while(rsfecha.next()){
                fecha=rsfecha.getDate(1);
            }
            if(fecha!=null){
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(fecha);
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                fecha=calendar.getTime();
            }else
            {
                fecha=new Date();
            }
            jDateChooser1.setDate(fecha);
        } catch (SQLException ex) {
            Logger.getLogger(valuacion.class.getName()).log(Level.SEVERE, null, ex);
        }
        jTable1.setModel(metabs);
        jTextField4.setText("0.00");
        estavalu=new BigDecimal("0.00");
        jTextField5.setText("0.00");
        jTextField6.setText("0.00");
        jTextField8.setText("");
        jTextField9.setText("");
    }

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed

        int x = this.getX() + (this.getWidth() - 750) / 2;
        int y = this.getY() + (this.getHeight() - 450) / 2;
        partidas part = new partidas(null, true, conex, pres, mvalu, this);
        part.setBounds(x, y, 750, 450);
        part.setVisible(true);

    }//GEN-LAST:event_jButton4ActionPerformed

    private void jSpinner1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner1StateChanged
        acum=new BigDecimal("0.00");
        mvalu = jSpinner1.getValue().toString();
        int cont = 0;
        System.out.println("mvalu " + mvalu);
        String valuacion = "SELECT count(*) FROM mvalus WHERE id=" + mvalu+ " AND mpre_id='"+pres+"'";
        try {
            Statement sts = (Statement) conex.createStatement();
            ResultSet rst = sts.executeQuery(valuacion);
            while (rst.next()) {
                cont = rst.getInt(1);
            }
            if (cont == 0) {
                nuevo(1);
            } else {
                cargapresupuesto();
            }
        } catch (SQLException ex) {
            Logger.getLogger(valuacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jSpinner1StateChanged

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
jButton3.setEnabled(true);

        if (!jTable1.isEditing() && jTable1.editCellAt(jTable1.getSelectedRow(),
                jTable1.getSelectedColumn())) {
            
            
            JTextField field = ((JTextField)jTable1.getEditorComponent());
            field.requestFocusInWindow();
            if(jTable1.getCellEditor()!=null)
                editor = jTable1.getCellEditor();
            else
            {
                jTable1.setCellEditor(editor);
            }
            field.selectAll();
            field.addKeyListener(
                    new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent evt)
                    {
                        filapart = jTable1.getSelectedRow();
                        String codigopart, sql;
                        BigDecimal original, cantidad1; 
                        char car = evt.getKeyChar();
                        if((car<'0'||car>'9')&& evt.getKeyCode()!=10 &&evt.getKeyCode() !=9 ){
                            evt.consume(); 
                        }else{
                        if (evt.getKeyCode() == 9 || evt.getKeyCode() == 10) {
                            try {
                                
                       if(jTable1.getCellEditor()!=null)
                        jTable1.getCellEditor().stopCellEditing();
                       
                        codigopart = jTable1.getValueAt(filapart, 1).toString();
                        String num="";
                        String consultanumero = "SELECT numero FROM mppres where numegrup="+codigopart+" AND "
                                + "(mpre_id='"+pres+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"'))";
                        Statement stnum = conex.createStatement();
                        ResultSet rstnum = stnum.executeQuery(consultanumero);
                        while(rstnum.next())
                            num = rstnum.getString(1);
                        
                        cantidad1 = new BigDecimal(jTable1.getValueAt(filapart, 5).toString());
                        original = new BigDecimal(jTable1.getValueAt(filapart, 3).toString());
                        jTable1.removeEditor();
                        System.out.println("TECLAS: " + evt.getKeyCode());
                        Statement stmate;
                        if (original.doubleValue() < cantidad1.doubleValue()) {
                            int op = JOptionPane.showConfirmDialog(null, "Cantidad Valuada excede a la cantidad registrada en " + (cantidad1.doubleValue() - original.doubleValue()) + " unidades");
                            if (op == JOptionPane.YES_OPTION) 
                            {
                                try 
                                {
                                    stmate = (Statement) conex.createStatement();
                                    sql = "UPDATE dvalus SET cantidad = " + cantidad1 + ", aumento=1 WHERE numepart='" + num + "' AND mpre_id='" + pres+"' AND mvalu_id="+mvalu+"";
                                    stmate.execute(sql);
                                } catch (SQLException ex) 
                                {
                                    Logger.getLogger(valuacion.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        } else {
                            try {
                                stmate = (Statement) conex.createStatement();
                                sql = "UPDATE dvalus SET cantidad = " + cantidad1 + " WHERE numepart='" + num + "' AND mpre_id='" + pres+"' AND mvalu_id="+mvalu+"";
                                stmate.execute(sql);

                            } catch (SQLException ex) {
                                Logger.getLogger(valuacion.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        buscapartida();
                    } catch (SQLException ex) {
                        Logger.getLogger(valuacion.class.getName()).log(Level.SEVERE, null, ex);
                    }
                        }
                        
                        
                        }
                    }
                    });
            
        }   
    }//GEN-LAST:event_jTable1MouseClicked

    private void jTable1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable1KeyPressed
        



    }//GEN-LAST:event_jTable1KeyPressed

    private void jTextField8FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField8FocusLost
        String numero = jTextField8.getText().toString();
        
        if(pierdefoco==0){
        if (!numero.equals("")) {
           pierdefoco=1;
            String nume="";
            String sql = "SELECT id,numero FROM mppres WHERE numegrup='"+numero+"' AND (mpre_id='" + pres+"' "
                    + " OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"' GROUP BY id))";
            try {
                Statement st = conex.createStatement();
                ResultSet rst = st.executeQuery(sql);
                while (rst.next()) {
                    jTextField9.setText(rst.getObject(1).toString());
                    nume = rst.getString(2);
                }
                calculacantpart(nume);
            } catch (SQLException ex) {
                Logger.getLogger(valuacion.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        }
    }//GEN-LAST:event_jTextField8FocusLost

    private void jTextField9FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField9FocusLost
        String codigo = jTextField9.getText().toString();

        if(pierdefoco==0){
        if (!codigo.equals("")) {
      
            String sql = "SELECT numegrup FROM mppres WHERE id='" + codigo + "' AND mpre_id='" + pres+"' GROUP BY id";
            try {
                Statement st = conex.createStatement();
                ResultSet rst = st.executeQuery(sql);
                while (rst.next()) {
                    jTextField8.setText(rst.getObject(1).toString());
                }
            } catch (SQLException ex) {
                Logger.getLogger(valuacion.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        }


    }//GEN-LAST:event_jTextField9FocusLost

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jTextField8KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField8KeyTyped

        int code = evt.getKeyCode();
        char car = evt.getKeyChar();
        if ((car < '0' || car > '9')) {
            evt.consume();
        }
        
    }//GEN-LAST:event_jTextField8KeyTyped

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
     
        filapart = jTable1.getSelectedRow();
        String numegrup = jTable1.getValueAt(filapart, 1).toString();
        String select ="SELECT numero FROM mppres WHERE numegrup="+numegrup+""
                + " AND (mpre_id='"+pres+"' OR "
                + "mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"'))";
        String numero="";
        try {
            Statement stselect = (Statement) conex.createStatement();
            ResultSet rstselect = stselect.executeQuery(select);
            while(rstselect.next())
                numero=rstselect.getString(1);            
        } catch (SQLException ex) {
            Logger.getLogger(valuacion.class.getName()).log(Level.SEVERE, null, ex);
        }
        int op = JOptionPane.showConfirmDialog(rootPane, "¿Desea Eliminar la partida Nro. "+numegrup+"?", "Eliminar Partida", JOptionPane.YES_NO_OPTION);
        if(op==JOptionPane.YES_OPTION){
        String borra = "DELETE FROM dvalus WHERE numepart='" + numero + "' AND mvalu_id=" + mvalu+" AND "
                + "mpre_id='"+pres+"'";
        try {
            Statement st = (Statement) conex.createStatement();
            st.execute(borra);
            JOptionPane.showMessageDialog(this, "Se ha borrado la partida de la valuación");
            buscapartida();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "No se ha borrado la partida de la valuación");
            Logger.getLogger(valuacion.class.getName()).log(Level.SEVERE, null, ex);
        }
        }

    }//GEN-LAST:event_jButton3ActionPerformed

    private void jTable1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable1KeyReleased
        //jScrollPane2.setViewportView(jTable1);
    }//GEN-LAST:event_jTable1KeyReleased

    private void jTable1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable1KeyTyped

    }//GEN-LAST:event_jTable1KeyTyped

    private void jSpinner1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jSpinner1KeyTyped
       
        int code = evt.getKeyCode();
        char car = evt.getKeyChar();
        if ((car<'0' || car>'9')) {
            evt.consume();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_jSpinner1KeyTyped

    private void okButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_okButtonMouseClicked
        String fechaini;
        String fechafin;
        SimpleDateFormat formatoDelTexto = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formatofecha = new SimpleDateFormat("yyyy-MM-dd");
        
        if(jDateChooser1.getDate()==null || jDateChooser2.getDate()==null || jComboBox1.getSelectedItem()==null){
            JOptionPane.showMessageDialog(null, "Los campos de fecha o el tipo de valuación no puede estar vacío");
        }else{
        fechaini = formatofecha.format(jDateChooser1.getDate());
        fechafin = formatofecha.format(jDateChooser2.getDate());

//************************************DEBO VER SI ESTA GUARDADA LA VALUACION Y SINO GUARDARLA
        String selectCount = "SELECT count(*) FROM mvalus WHERE (mpre_id='"+pres+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"')"
                + " ) AND id='"+mvalu+"'";
        int cuenta=0;
            try {
                Statement stCuenta = conex.createStatement();
                ResultSet rstCuenta = stCuenta.executeQuery(selectCount);
                while(rstCuenta.next())
                {
                    cuenta = rstCuenta.getInt(1);
                }
            } catch (SQLException ex) {
                Logger.getLogger(valuacion.class.getName()).log(Level.SEVERE, null, ex);
            }
        String tipos = jComboBox1.getSelectedItem().toString();
        String consulta ="";
        
        if(cuenta==0){
            consulta = "INSERT INTO mvalus (id, desde, hasta, status, mpre_id, tipo, lapso) "
                    + "VALUES "
                    + "('"+mvalu+"', '"+fechaini+"', '"+fechafin+"',1,'"+pres+"', '"+tipos+"',0)";
        }else{
        consulta="UPDATE mvalus SET desde = '" + fechaini + "', hasta ='" + fechafin + "', tipo = '" + tipos + "', status=1 WHERE id='" + mvalu + "' AND mpre_id='" + pres+"'";
        }
        try {
            Statement st = conex.createStatement();
            st.execute(consulta);
            
            JOptionPane.showMessageDialog(null, "Se ha guardado la valuación");
        } catch (SQLException ex) {
            Logger.getLogger(valuacion.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        }
       // doClose(RET_OK);        // TODO add your handling code here:
    }//GEN-LAST:event_okButtonMouseClicked

private void jTextField10FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField10FocusLost
    jLabel10.setText("");
    pierdefoco=0;
    cantidad = new BigDecimal(jTextField10.getText());
    BigDecimal cantacum=new BigDecimal("0.00"), cantcontratado=new BigDecimal("0.00"), compara;
    String numero="";
    int siexiste=0;
    String consultanumero = "SELECT numero, cantidad FROM mppres WHERE (mpre_id='"+pres+"' OR "
            + "mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) "
            + "AND numegrup='"+jTextField8.getText()+"'";
        try {
            Statement stnumero = conex.createStatement();
            ResultSet rstnumero = stnumero.executeQuery(consultanumero);
            while(rstnumero.next()){
                numero = rstnumero.getString("numero");
                cantcontratado = rstnumero.getBigDecimal("cantidad");
            }
        } catch (SQLException ex) {
            Logger.getLogger(valuacion.class.getName()).log(Level.SEVERE, null, ex);
        }
            
    String acumulada = "SELECT SUM(cantidad) FROM dvalus WHERE (mpre_id='"+pres+"' OR mpre_id "
            + "IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"'))"
            + " AND numepart='"+numero+"' GROUP BY numepart";
        try {
            Statement stacum = conex.createStatement();
            ResultSet rstacum = stacum.executeQuery(acumulada);
            while(rstacum.next())
            {
                cantacum=rstacum.getBigDecimal(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(valuacion.class.getName()).log(Level.SEVERE, null, ex);
        }
        String existe = "SELECT COUNT(*) FROM dvalus WHERE numepart='"+numero+"' AND mvalu_id='"+mvalu+"' "
                + "AND (mpre_id='"+pres+"' OR mpre_id "
            + "IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"'))";
        try {
            Statement stexiste = conex.createStatement();
            ResultSet rstexiste = stexiste.executeQuery(existe);
            while(rstexiste.next()){
                siexiste=rstexiste.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(valuacion.class.getName()).log(Level.SEVERE, null, ex);
        }
     compara=cantacum.add(cantidad);
     if(siexiste==0){
     if(compara.doubleValue()>cantcontratado.doubleValue()){
         int op = JOptionPane.showConfirmDialog(this, "La cantidad valuada acumulada: "+compara+" excede a la cantidad contratada: "+cantcontratado+" en "+(compara.doubleValue()-cantcontratado.doubleValue())+" Unidades, Desea Continuar? Sí/No", "Exceso", JOptionPane.YES_NO_OPTION);
         if(op==JOptionPane.YES_OPTION){
         inserta();
         }
     }else{  
        inserta();
     }
     }else{
         siexiste=0;
         int op1=JOptionPane.showConfirmDialog(null, "Partida ya fue insertada en esta valuación, desea modificarla? Sí/No", "Ya Existe", JOptionPane.YES_NO_OPTION);
         if(op1==JOptionPane.YES_OPTION){
             if(compara.doubleValue()>cantcontratado.doubleValue()){
                int op = JOptionPane.showConfirmDialog(this, "La cantidad valuada acumulada: "+compara+" excede a la cantidad contratada: "+cantcontratado+" en "+(compara.doubleValue()-cantcontratado.doubleValue())+" Unidades, Desea Continuar? Sí/No", "Exceso", JOptionPane.YES_NO_OPTION);
                if(op==JOptionPane.YES_OPTION){
                    modifica(numero);
                    } 
             }
             
         }
     }
    pierdefoco=0;
    jTextField10.setText("");
    jTextField8.setText("");
    jTextField9.setText("");
    // TODO add your handling code here:
}//GEN-LAST:event_jTextField10FocusLost

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_okButtonActionPerformed

private void jTextField8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField8MouseClicked
    jTextField9.setText("");
    jTextField10.setText("");
    jTextField8.setText("");
    
    // TODO add your handling code here:
}//GEN-LAST:event_jTextField8MouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
       int op=JOptionPane.showConfirmDialog(rootPane, "Desea Eliminar Valuación?", "Eliminar Valuación", JOptionPane.YES_NO_OPTION);
       String numvalu = jSpinner1.getValue().toString();
       if(op==JOptionPane.YES_OPTION){
        String eliminar = "DELETE FROM mvalus WHERE id="+numvalu+" AND mpre_id='"+pres+"'";
        String elidvalu = "DELETE FROM dvalus WHERE mvalu_id="+numvalu+" AND mpre_id='"+pres+"'";
            try {
                Statement ste = conex.createStatement();
                ste.execute(eliminar);
                ste.execute(elidvalu);
            } catch (SQLException ex) {
                Logger.getLogger(valuacion.class.getName()).log(Level.SEVERE, null, ex);
            }
            JOptionPane.showMessageDialog(rootPane, "Valuación Eliminada");
                    doClose(RET_CANCEL);
       }  
        
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    
       
    
        public String vervalu (){
         String valormvalu=mvalu;
        if(jComboBox1.getSelectedItem().equals("Parcial")){
            valormvalu="VALUACIÓN No.: "+mvalu;
        }
        if(jComboBox1.getSelectedItem().equals("Unica")){
            valormvalu="VALUACIÓN UNICA";
        }
        if(jComboBox1.getSelectedItem().equals("Final")){
            valormvalu = "VALUACIÓN FINAL No.: "+mvalu;
        }
        if(jComboBox1.getSelectedItem().equals("Reconsideración")){
            valormvalu = "RECONSIDERACIÓN DE PRECIOS No.: "+mvalu;
        }
        if(jComboBox1.getSelectedItem().equals("Otro")){
            valormvalu = jTextField3.getText().toUpperCase()+" "+mvalu;
        }
        return valormvalu;
    }
    private void llenaValu()
    {
                 String borra = "TRUNCATE TABLE reportevaluacion";
                  Statement truncate;
                try {
                    truncate = (Statement) conex.createStatement();
                      truncate.execute(borra);
                      String consultaoriginal= "INSERT INTO reportevaluacion "
                              + "SELECT mp.numegrup as nro, mp.id as codigo, mp.descri as descri, mp.unidad as unidad,"
                              + "IFNULL((SELECT SUM(cantidad) as cantidades "
                              + "FROM dvalus WHERE numepart=mp.numero AND (mpre_id='"+pres+"' OR mpre_id IN "
                              + "(SELECT id FROM mpres WHERE mpres_id='"+pres+"')) AND "
                              + "mvalu_id<"+mvalu+"),0) as cantidadanterior,IFNULL((SELECT SUM(cantidad) as cantidades"
                              + " FROM dvalus WHERE numepart=mp.numero AND (mpre_id='"+pres+"' OR mpre_id IN "
                              + "(SELECT id FROM mpres WHERE mpres_id='"+pres+"')) AND "
                              + "mvalu_id<="+mvalu+"),0) as cantidadactual, IF(mp.precasu=0,mp.precunit,mp.precasu)"
                              + " as preciounitario,"
                              + "ROUND(IFNULL((SELECT SUM(cantidad) as cantidad "
                              + "FROM dvalus WHERE numepart=mp.numero AND mpre_id='"+pres+"' AND "
                              + "mvalu_id<="+mvalu+")*IF(mp.precasu=0,mp.precunit,mp.precasu),0),2) as total,"+mvalu+",'"+pres+"'"
                              + " FROM mppres as mp, dvalus as dv, mvalus as mv WHERE mp.tipo='Org' AND"
                              + " mp.numero = dv.numepart AND dv.mpre_id=mp.mpre_id AND "
                              + "dv.mvalu_id<=mv.id AND mv.id="+mvalu+" AND"
                              + " (mv.mpre_id = '"+pres+"' OR mv.mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) "
                              + "AND (dv.mpre_id='"+pres+"' "
                              + "OR dv.mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"'))"
                              + " AND (mp.mpre_id='"+pres+"' "
                              + "OR mp.mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) "
                              + "GROUP BY dv.numepart ORDER BY mp.numegrup";
                      Statement original = (Statement) conex.createStatement();
                      original.execute(consultaoriginal);
                      
                      //------------NO Prevista------------------------------------------
                      String cuenta = "SELECT COUNT(*) FROM mppres as mp, dvalus as dv, mvalus as mv "
                              + "WHERE dv.mvalu_id<=mv.id AND mv.id = "+mvalu+" AND (mp.mpre_id='"+pres+"' OR mp.mpre_id IN "
                              + "(SELECT id FROM mpres WHERE mpres_id='"+pres+"')) AND (dv.mpre_id ='"+pres+"' OR "
                              + "dv.mpre_id IN (SELECT id FROM mpres WHERE mpres_id = '"+pres+"'))AND mp.numero = dv.numepart"
                              + " AND (mv.mpre_id='"+pres+"' "
                              + "OR mv.mpre_id IN (SELECT id FROM mpres WHERE mpres_id ='"+pres+"')) AND mp.tipo='NP' "
                              + "AND mp.tiponp='NP'";
                      Statement cuantosnp= (Statement) conex.createStatement();
                      ResultSet rscuantosnp = cuantosnp.executeQuery(cuenta);
                      int cuentas=0;
                      while(rscuantosnp.next()){
                          cuentas = rscuantosnp.getInt(1);
                      }
                      if(cuentas>0)
                      {
                          String insertatit = "INSERT INTO reportevaluacion (codigo,descri,mvalu,mpre)"
                                        + "VALUES ('','PARTIDAS NO PREVISTAS',"+mvalu+",'"+pres+"')";
                                Statement ins = (Statement) conex.createStatement();
                                ins.execute(insertatit);
                                String consultanp= "INSERT INTO reportevaluacion "
                              + " SELECT mp.numegrup as nro, mp.id as codigo, mp.descri as descri, mp.unidad as unidad,"
                              + " IFNULL((SELECT SUM(cantidad) as cantidades "
                              + " FROM dvalus WHERE numepart=mp.numero AND (mpre_id='"+pres+"' OR mpre_id IN "
                              + "(SELECT id FROM mpres WHERE mpres_id='"+pres+"')) AND "
                              + " mvalu_id<"+mvalu+"),0) as cantidadanterior,IFNULL((SELECT SUM(cantidad) as cantidades"
                              + " FROM dvalus WHERE numepart=mp.numero AND (mpre_id='"+pres+"' OR mpre_id IN "
                              + "(SELECT id FROM mpres WHERE mpres_id='"+pres+"')) AND "
                              + " mvalu_id<="+mvalu+"),0) as cantidadactual, IF(mp.precasu=0,mp.precunit,mp.precasu) as preciounitario,"
                              + " ROUND(IFNULL((SELECT SUM(cantidad) as cantidad "
                              + " FROM dvalus WHERE numepart=mp.numero AND mpre_id='"+pres+"' AND "
                              + " mvalu_id<="+mvalu+")*IF(mp.precasu=0,mp.precunit,mp.precasu),0),2) as total,"+mvalu+",'"+pres+"'"
                              + " FROM mppres as mp, dvalus as dv, mvalus as mv WHERE mp.tipo='NP' AND mp.tiponp='NP' AND"
                              + " mp.numero = dv.numepart AND"
                              + " dv.mvalu_id<=mv.id AND mv.id="+mvalu+" AND"
                              + " (mv.mpre_id = '"+pres+"' OR mv.mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) "
                              + " AND (dv.mpre_id='"+pres+"'"
                              + " OR dv.mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"'))"
                              + " AND (mp.mpre_id='"+pres+"'"
                              + " OR mp.mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"'))"
                              + " GROUP BY dv.numepart ORDER BY mp.numegrup";
                                System.out.println("consultanp "+consultanp);
                                Statement noprevista = (Statement) conex.createStatement();
                                noprevista.execute(consultanp);
                                
                      }
                      //------------------Obras extras------------------
                      String cuentaoe = "SELECT COUNT(*) FROM mppres as mp, dvalus as dv, mvalus as mv "
                              + "WHERE dv.mvalu_id<=mv.id AND mv.id = "+mvalu+" AND (mp.mpre_id='"+pres+"' OR mp.mpre_id IN "
                              + "(SELECT id FROM mpres WHERE mpres_id='"+pres+"')) AND (dv.mpre_id ='"+pres+"' OR "
                              + "dv.mpre_id IN (SELECT id FROM mpres WHERE mpres_id = '"+pres+"'))AND mp.numero = dv.numepart"
                              + " AND (mv.mpre_id='"+pres+"' "
                              + "OR mv.mpre_id IN (SELECT id FROM mpres WHERE mpres_id ='"+pres+"')) AND mp.tipo='NP' "
                              + "AND mp.tiponp='OE'";
                       Statement cuantosoe= (Statement) conex.createStatement();
                      ResultSet rscuantosoe = cuantosoe.executeQuery(cuentaoe);
                      int cuentasoe=0;
                      while(rscuantosoe.next()){
                          cuentasoe = rscuantosoe.getInt(1);
                      }
                      if(cuentasoe>0){
                          String insertatit = "INSERT INTO reportevaluacion (codigo,descri,mvalu,mpre)"
                                        + "VALUES ('','OBRAS EXTRAS',"+mvalu+",'"+pres+"')";
                                Statement ins = (Statement) conex.createStatement();
                                ins.execute(insertatit);
                                String consultaoe= "INSERT INTO reportevaluacion "
                              + " SELECT mp.numegrup as nro, mp.id as codigo, mp.descri as descri, mp.unidad as unidad,"
                              + " IFNULL((SELECT SUM(cantidad) as cantidades "
                              + " FROM dvalus WHERE numepart=mp.numero AND (mpre_id='"+pres+"' OR mpre_id IN "
                              + " (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) AND "
                              + " mvalu_id<"+mvalu+"),0) as cantidadanterior,IFNULL((SELECT SUM(cantidad) as cantidades"
                              + " FROM dvalus WHERE numepart=mp.numero AND (mpre_id='"+pres+"' OR mpre_id IN "
                              + " (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) AND "
                              + " mvalu_id<="+mvalu+"),0) as cantidadactual, IF(mp.precasu=0,mp.precunit,mp.precasu) as preciounitario,"
                              + " ROUND(IFNULL((SELECT SUM(cantidad) as cantidad "
                              + " FROM dvalus WHERE numepart=mp.numero AND mpre_id='"+pres+"' AND "
                              + " mvalu_id<="+mvalu+")*IF(mp.precasu=0,mp.precunit,mp.precasu),0),2) as total,"+mvalu+",'"+pres+"'"
                              + " FROM mppres as mp, dvalus as dv, mvalus as mv WHERE mp.tipo='NP' AND mp.tiponp='OE' AND"
                              + " mp.numero = dv.numepart AND "
                              + " dv.mvalu_id<=mv.id AND mv.id="+mvalu+" AND"
                              + " (mv.mpre_id = '"+pres+"' OR mv.mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) "
                              + " AND (dv.mpre_id='"+pres+"'"
                              + " OR dv.mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"'))"
                              + " AND (mp.mpre_id='"+pres+"'"
                              + " OR mp.mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"'))"
                              + " GROUP BY dv.numepart ORDER BY mp.numegrup";
                                Statement obraextra= (Statement) conex.createStatement();
                                obraextra.execute(consultaoe);
                      }
                      //------------Obras Adicionales--------------------------------------------------------
                      String cuentaoa = "SELECT COUNT(*) FROM mppres as mp, dvalus as dv, mvalus as mv "
                              + "WHERE dv.mvalu_id<=mv.id AND mv.id = "+mvalu+" AND (mp.mpre_id='"+pres+"' OR mp.mpre_id IN "
                              + "(SELECT id FROM mpres WHERE mpres_id='"+pres+"')) AND (dv.mpre_id ='"+pres+"' OR "
                              + "dv.mpre_id IN (SELECT id FROM mpres WHERE mpres_id = '"+pres+"'))AND mp.numero = dv.numepart"
                              + " AND (mv.mpre_id='"+pres+"' "
                              + "OR mv.mpre_id IN (SELECT id FROM mpres WHERE mpres_id ='"+pres+"')) AND mp.tipo='NP' "
                              + "AND mp.tiponp='OA'";
                       Statement cuantosoa= (Statement) conex.createStatement();
                      ResultSet rscuantosoa = cuantosoa.executeQuery(cuentaoa);
                      int cuentasoa=0;
                      while(rscuantosoa.next()){
                          cuentasoa = rscuantosoa.getInt(1);
                      }
                      if(cuentasoa>0){
                          String insertatit = "INSERT INTO reportevaluacion (codigo,descri,mvalu,mpre)"
                                        + "VALUES ('','OBRAS ADICIONALES',"+mvalu+",'"+pres+"')";
                                Statement ins = (Statement) conex.createStatement();
                                ins.execute(insertatit);
                                String consultaAD= "INSERT INTO reportevaluacion "
                              + " SELECT mp.numegrup as nro, mp.id as codigo, mp.descri as descri, mp.unidad as unidad,"
                              + " IFNULL((SELECT SUM(cantidad) as cantidades "
                              + " FROM dvalus WHERE numepart=mp.numero AND (mpre_id='"+pres+"' OR mpre_id IN "
                              + " (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) AND "
                              + " mvalu_id<"+mvalu+"),0) as cantidadanterior,IFNULL((SELECT SUM(cantidad) as cantidades"
                              + " FROM dvalus WHERE numepart=mp.numero AND (mpre_id='"+pres+"' OR mpre_id IN "
                              + " (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) AND "
                              + " mvalu_id<="+mvalu+"),0) as cantidadactual, IF(mp.precasu=0,mp.precunit,mp.precasu) as preciounitario,"
                              + " ROUND(IFNULL((SELECT SUM(cantidad) as cantidad "
                              + " FROM dvalus WHERE numepart=mp.numero AND mpre_id='"+pres+"' AND "
                              + " mvalu_id<="+mvalu+")*IF(mp.precasu=0,mp.precunit,mp.precasu),0),2) as total,"+mvalu+",'"+pres+"'"
                              + " FROM mppres as mp, dvalus as dv, mvalus as mv WHERE mp.tipo='NP' AND mp.tiponp='OA' AND"
                              + " mp.numero = dv.numepart AND "
                              + " dv.mvalu_id<=mv.id AND mv.id="+mvalu+" AND"
                              + " (mv.mpre_id = '"+pres+"' OR mv.mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) "
                              + " AND (dv.mpre_id='"+pres+"'"
                              + " OR dv.mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"'))"
                              + " AND (mp.mpre_id='"+pres+"'"
                              + " OR mp.mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"'))"
                              + " GROUP BY dv.numepart ORDER BY mp.numegrup";
                                Statement obraAD= (Statement) conex.createStatement();
                                obraAD.execute(consultaAD);
                      }
                      //------------Obras COMPLEMENTARIAS--------------------------------------------------------
                      String cuentaoc = "SELECT COUNT(*) FROM mppres as mp, dvalus as dv, mvalus as mv "
                              + "WHERE dv.mvalu_id<=mv.id AND mv.id = "+mvalu+" AND (mp.mpre_id='"+pres+"' OR mp.mpre_id IN "
                              + "(SELECT id FROM mpres WHERE mpres_id='"+pres+"')) AND (dv.mpre_id ='"+pres+"' OR "
                              + "dv.mpre_id IN (SELECT id FROM mpres WHERE mpres_id = '"+pres+"'))AND mp.numero = dv.numepart"
                              + " AND (mv.mpre_id='"+pres+"' "
                              + "OR mv.mpre_id IN (SELECT id FROM mpres WHERE mpres_id ='"+pres+"')) AND mp.tipo='NP' "
                              + "AND mp.tiponp='OC'";
                       Statement cuantosoc= (Statement) conex.createStatement();
                      ResultSet rscuantosoc = cuantosoc.executeQuery(cuentaoc);
                      int cuentasoc=0;
                      while(rscuantosoc.next()){
                          cuentasoc = rscuantosoc.getInt(1);
                      }
                      if(cuentasoc>0){
                          String insertatit = "INSERT INTO reportevaluacion (codigo,descri,mvalu,mpre)"
                                        + "VALUES ('','OBRAS COMPLEMENTARIAS',"+mvalu+",'"+pres+"'))";
                                Statement ins = (Statement) conex.createStatement();
                                ins.execute(insertatit);
                                String consultaoc= "INSERT INTO reportevaluacion "
                              + " SELECT mp.numegrup as nro, mp.id as codigo, mp.descri as descri, mp.unidad as unidad,"
                              + " IFNULL((SELECT SUM(cantidad) as cantidades "
                              + " FROM dvalus WHERE numepart=mp.numero AND (mpre_id='"+pres+"' OR mpre_id IN "
                              + "(SELECT id FROM mpres WHERE mpres_id='"+pres+"')) AND "
                              + " mvalu_id<"+mvalu+"),0) as cantidadanterior,IFNULL((SELECT SUM(cantidad) as cantidades"
                              + " FROM dvalus WHERE numepart=mp.numero AND (mpre_id='"+pres+"' OR mpre_id IN "
                              + " (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) AND "
                              + " mvalu_id<="+mvalu+"),0) as cantidadactual, IF(mp.precasu=0,mp.precunit,mp.precasu) as preciounitario,"
                              + " ROUND(IFNULL((SELECT SUM(cantidad) as cantidad "
                              + " FROM dvalus WHERE numepart=mp.numero AND mpre_id='"+pres+"' AND "
                              + " mvalu_id<="+mvalu+")*IF(mp.precasu=0,mp.precunit,mp.precasu),0),2) as total,"+mvalu+",'"+pres+"'"
                              + " FROM mppres as mp, dvalus as dv, mvalus as mv WHERE mp.tipo='NP' AND mp.tiponp='OC' AND"
                              + " mp.numero = dv.numepart AND "
                              + " dv.mvalu_id<=mv.id AND mv.id="+mvalu+" AND"
                              + " (mv.mpre_id = '"+pres+"' OR mv.mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) "
                              + " AND (dv.mpre_id='"+pres+"'"
                              + " OR dv.mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"'))"
                              + " AND (mp.mpre_id='"+pres+"'"
                              + " OR mp.mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"'))"
                              + " GROUP BY dv.numepart ORDER BY mp.numegrup";
                                Statement obraoc= (Statement) conex.createStatement();
                                obraoc.execute(consultaoc);
                      }
                      
                      //-----------VARIACIÓN DE PRECIOS----------------------------------------------
                      String cuentavp = "SELECT COUNT(*) FROM mppres as mp, dvalus as dv, mvalus as mv "
                              + "WHERE dv.mvalu_id<=mv.id AND mv.id = "+mvalu+" AND"
                              + " (mp.mpre_id='"+pres+"' OR mp.mpre_id IN "
                              + "(SELECT id FROM mpres WHERE mpres_id='"+pres+"')) AND (dv.mpre_id ='"+pres+"' OR "
                              + "dv.mpre_id IN (SELECT id FROM mpres WHERE mpres_id = '"+pres+"'))AND mp.numero = dv.numepart"
                              + " AND (mv.mpre_id='"+pres+"' "
                              + "OR mv.mpre_id IN (SELECT id FROM mpres WHERE mpres_id ='"+pres+"')) AND mp.tipo='VP'";
                       Statement cuantosvp= (Statement) conex.createStatement();
                      ResultSet rscuantosvp = cuantosvp.executeQuery(cuentavp);
                      int cuentasvp=0;
                      while(rscuantosvp.next()){
                          cuentasvp= rscuantosvp.getInt(1);
                      }
                      if(cuentasvp>0){
                           String deltas="IF((mp.precunit-(SELECT m.precunit FROM "
                    + "mppres as m WHERE (m.mpre_id='"+pres+"' OR m.mpre_id IN "
                    + " (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) AND m.numero=mp.mppre_id))<0,0,"
                    + "mp.precunit-(SELECT m.precunit FROM "
                    + "mppres as m WHERE (m.mpre_id='"+pres+"' OR m.mpre_id IN "
                    + " (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) AND m.numero=mp.mppre_id))";
                          String insertatit = "INSERT INTO reportevaluacion (codigo,descri,mvalu,mpre)"
                                        + "VALUES ('','VARIACIONES DE PRECIO',"+mvalu+",'"+pres+"')";
                                Statement ins = (Statement) conex.createStatement();
                                ins.execute(insertatit);
                                String consultavp= "INSERT INTO reportevaluacion "
                              + " SELECT mp.tiporec as nro, mp.id as codigo, mp.descri as descri, mp.unidad as unidad,"
                              + " IFNULL((SELECT SUM(cantidad) as cantidades "
                              + " FROM dvalus WHERE numepart=mp.numero AND (mpre_id='"+pres+"' OR mpre_id IN "
                              + " (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) AND "
                              + " mvalu_id<"+mvalu+"),0) as cantidadanterior,IFNULL((SELECT SUM(cantidad) as cantidades"
                              + " FROM dvalus WHERE numepart=mp.numero AND (mpre_id='"+pres+"' OR mpre_id IN "
                              + " (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) AND "
                              + " mvalu_id<="+mvalu+"),0) as cantidadactual, "
                                        + ""+deltas+" as preciounitario,"
                              + " ROUND(IFNULL((SELECT SUM(cantidad) as cantidad "
                              + " FROM dvalus WHERE numepart=mp.numero AND mpre_id='"+pres+"' AND "
                              + " mvalu_id<="+mvalu+")*"+deltas+",0),2) as total,"+mvalu+",'"+pres+"'"
                              + " FROM mppres as mp, dvalus as dv, mvalus as mv WHERE mp.tipo='VP' AND"
                              + " mp.numero = dv.numepart AND"
                              + " dv.mvalu_id<=mv.id AND mv.id="+mvalu+" AND"
                              + " (mv.mpre_id = '"+pres+"' OR mv.mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) "
                              + " AND (dv.mpre_id='"+pres+"' "
                              + " OR dv.mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"'))"
                              + " AND (mp.mpre_id='"+pres+"' "
                              + " OR mp.mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) "
                              + " GROUP BY dv.numepart ORDER BY mp.numegrup";
                                System.out.println("consulta vp "+consultavp);
                                Statement obraoc= (Statement) conex.createStatement();
                                obraoc.execute(consultavp);
                      }
                } catch (SQLException ex) {
                    Logger.getLogger(reporte.class.getName()).log(Level.SEVERE, null, ex);
                }
                
           
    }
    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        mvalu = jSpinner1.getValue().toString();     
        reporte rep = new reporte(null, true, conex, pres, mvalu,vervalu(), jRadioButton1.isSelected()?1:0);  //1 con lapso 0 sin lapso
        int x =  this.getX() + (this.getWidth() - 600) / 2;
        int y = this.getY() + (this.getHeight() - 350) / 2;
        rep.setBounds(x, y, 600, 350);
        this.setModal(false);
        rep.setVisible(true);
        
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        mvalu = jSpinner1.getValue().toString();       
        reporteinspeccion rep = new reporteinspeccion(null, true, conex, pres, mvalu,vervalu(), impu, String.valueOf(impuesto));  
        int x=  this.getX() + (this.getWidth() - 400) / 2;
        int y = this.getY() + (this.getHeight() - 350) / 2;
        rep.setBounds(x, y, 400, 350);
        this.setModal(false);
        rep.setVisible(true);        
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        if(jComboBox1.getSelectedItem()!=null)
        if(jComboBox1.getSelectedItem().equals("Reconsideración"))        
            jRadioButton2.setSelected(true);
        else
            jRadioButton1.setSelected(true);
     
    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void jComboBox1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jComboBox1MouseClicked
        if(jComboBox1.getSelectedItem()!=null)
        if (jComboBox1.getSelectedItem().equals("Otro")) {
                jTextField3.setVisible(true);
                jTextField3.setEditable(true);
            } else {
                jTextField3.setVisible(false);
                jTextField3.setEditable(false);
            }        // TODO add your handling code here:
                // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1MouseClicked
   
    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        
        liquidacion liqui = new liquidacion(null, false, conex, pres, mvalu, mpres, this);
        int x=  this.getX() + (this.getWidth() - 400) / 2;
        int y = this.getY() + (this.getHeight() - 300) / 2;
        liqui.setBounds(x, y, 400, 300);
        liqui.setVisible(true);
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        reportecuadravance re = new reportecuadravance(null, false, conex, pres, mvalu);
        int x=  this.getX() + (this.getWidth() - 450) / 2;
        int y = this.getY() + (this.getHeight() - 400) / 2;
        re.setBounds(x, y, 450, 400);
        re.setVisible(true);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jTable1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTable1FocusGained
 
           
        
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable1FocusGained

    public BigDecimal getporcAmort()
    {
        BigDecimal porcAmort=new BigDecimal("0.00");
        String sql = "SELECT IFNULL(porcentajeAmort,0) FROM mvalus WHERE id = "+mvalu+" AND mpre_id='"+pres+"'";
        try {
            Statement st = conex.createStatement();
            ResultSet rst = st.executeQuery(sql);
            while(rst.next())
                 porcAmort = rst.getBigDecimal(1);  
        } catch (SQLException ex) {
            Logger.getLogger(valuacion.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return porcAmort;
    }
    /**
     * Método encargado de calcular la amortización de anticipo acumulada en todas las valuaciones hasta la actual.
     * @return BigDecimal acumAmort 
     */
   public BigDecimal acumAmortizacion()
   {
       BigDecimal acumAmort = new BigDecimal("0.00");
       String sql = "SELECT IFNULL(SUM(ROUND("
               + "(SELECT IFNULL(SUM(ROUND(dv.cantidad*IF(mp.precasu=0, mp.precunit, mp.precasu),2)),0) FROM dvalus as dv "
               + ", mppres as mp WHERE dv.numepart=mp.numero AND (mp.mpre_id=dv.mvalu_id OR mp.mpre_id IN "
               + "(SELECT id FROM mpres WHERE mpres_id = dv.mvalu_id)) AND dv.mpre_id='"+pres+"' AND dv.mvalu_id=d.mvalu_id)*"
               + "(IFNULL(v.porcentajeAmort,0)/100),2)),0) "
               + "FROM dvalus as d, mvalus as v WHERE d.mvalu_id=v.id AND d.mpre_id=v.mpre_id AND d.mpre_id='"+pres+"' AND d.mvalu_id<="+mvalu+"";
        try {
            System.out.println(sql);
            Statement st = conex.createStatement();
            ResultSet rst = st.executeQuery(sql);
            while(rst.next())
            {
                acumAmort=rst.getBigDecimal(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(valuacion.class.getName()).log(Level.SEVERE, null, ex);
        }
       return acumAmort;
   }
    public void modifica(String num){
        try {
            String actualiza = "UPDATE dvalus SET cantidad="+jTextField10.getText()+" WHERE numepart="+num+" AND ("
                    + "mpre_id='"+pres+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"'))";
            Statement st = conex.createStatement();
            st.execute(actualiza);
            JOptionPane.showMessageDialog(null, "Se ha modificado la partida");
        } catch (SQLException ex) {
            Logger.getLogger(valuacion.class.getName()).log(Level.SEVERE, null, ex);
        }
        buscapartida();
}


public void inserta(){
            try {
            String codigo = jTextField9.getText().toString();
            String numegrup = jTextField8.getText().toString();
            String precio = "0";
            String numero="";
            
            String consultanumero="SELECT numero FROM mppres WHERE numegrup='"+numegrup+"' AND (mpre_id='"+pres+"'"
                    + " OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"'))";
            Statement stconsulta = conex.createStatement();
            ResultSet rstconsulta = stconsulta.executeQuery(consultanumero);
            while(rstconsulta.next()){
                numero=rstconsulta.getString(1);
            }
            mvalu = jSpinner1.getValue().toString();
            String select = "SELECT IF(precunit=0,precasu, precunit) FROM mppres where id='" + codigo + "'"
                    + " AND mpre_id='" + pres+"'";
            Statement st = conex.createStatement();
            ResultSet rst = st.executeQuery(select);
            while (rst.next()) {
                precio = rst.getObject(1).toString();
            }
            String inserta = "INSERT into dvalus (mpre_id, mvalu_id, mppre_id, cantidad, precio, numepart, status) VALUES "
                    + "('" + pres + "', '" + mvalu + "',"
                    + "'" + codigo + "', "+cantidad+", " + precio + ", '" + numero + "', 1)";
            Statement stm = (Statement) conex.createStatement();
            stm.execute(inserta);
        } catch (SQLException ex) {
            Logger.getLogger(valuacion.class.getName()).log(Level.SEVERE, null, ex);
        }
        int cuentas = 0;
        
        String cuenta = "SELECT count(*) FROM mvalus WHERE id=" + jSpinner1.getValue().toString() + " "
                + "AND mpre_id='" + pres + "'";
        try {
            Statement conta = conex.createStatement();
            ResultSet rstconta = conta.executeQuery(cuenta);
            while (rstconta.next()) {
                cuentas = rstconta.getInt(1);
            }
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date desde = jDateChooser1.getDate(), hasta = jDateChooser2.getDate();
            String fecdesde = "", fechasta = "";
           
            Date fecha = new Date();
            String fecha1;
            fecha1 = format.format(fecha);
            if (desde != null) {
                fecdesde = format.format(desde);
            } else {
                fecdesde=fecha1 ;
               
            }
            if (hasta != null) {
                fechasta = format.format(hasta);
            } else {
                fechasta=fecha1;
            }
            if (cuentas == 0) {
                if (jComboBox1.getSelectedItem() != null) {
                    tipo = jComboBox1.getSelectedItem().toString();
                    if (tipo.equals("Otro") && !jTextField3.getText().equals("")) {
                        tipo = jTextField3.getText();

                    }
                }
                
                if(jRadioButton1.isSelected()){
                    lapso=1;
                }
                else
                {
                    lapso=2;
                }
                String insertmvalu = "INSERT INTO mvalus VALUES(" + jSpinner1.getValue() + ","
                        + "'" + fecdesde + "', '" + fechasta + "', 1, '" + pres + "','" + tipo + "',"+lapso+" )";
                Statement inserta = conex.createStatement();
                inserta.execute(insertmvalu);
            }
        } catch (SQLException ex) {
            Logger.getLogger(valuacion.class.getName()).log(Level.SEVERE, null, ex);
        }
        buscapartida();
}   

    public void buscapartida() {
        try {
            estavalu = new BigDecimal("0.00");
            String deltas="IF((IF(mp.precasu=0,mp.precunit, mp.precasu)-(SELECT IF(m.precasu=0,m.precunit, m.precasu) FROM "
                    + "mppres as m WHERE (m.mpre_id='"+pres+"' OR m.mpre_id IN "
                    + " (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) AND m.numero=mp.mppre_id))<0,0,"
                    + "IF(mp.precasu=0,mp.precunit, mp.precasu)-(SELECT IF(m.precasu=0,m.precunit, m.precasu) FROM "
                    + "mppres as m WHERE (m.mpre_id='"+pres+"' OR m.mpre_id IN "
                    + " (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) AND m.numero=mp.mppre_id))";
            
            String sql = "SELECT dv.mppre_id, mp.numegrup, mp.descri, mp.cantidad, dv.numepart, "
                    + "dv.cantidad,IF(mp.tipo!='VP',if(mp.precasu=0,mp.precunit,mp.precasu),"+deltas+") as precio, "
                    + "ROUND(dv.cantidad*IF(mp.tipo!='VP',if(mp.precasu=0,mp.precunit,mp.precasu),"+deltas+"),2), mp.tiporec"
                    + " FROM dvalus as dv, mppres as mp"
                    + " WHERE mp.numero = dv.numepart AND "
                    + "dv.mvalu_id='" + mvalu + "' AND (dv.mpre_id='" + pres + "' "
                    + "OR dv.mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"'))"
                    + " AND (mp.mpre_id='" + pres + "' "
                    + "OR mp.mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"'))"
                    + "GROUP BY dv.numepart ORDER BY mp.numegrup";
            Statement st = (Statement) conex.createStatement();
            ResultSet rs = st.executeQuery(sql);
            ResultSetMetaData rsMd = (ResultSetMetaData) rs.getMetaData();

            DefaultTableModel metabs = new DefaultTableModel() {

                @Override
                public boolean isCellEditable(int row, int column)
                {
                    if (column == 5) {
                        return true;
                    } else
                    {
                        return false;
                    }
                }

                @Override
                public Class getColumnClass(int columna) {
                    if (columna == 1) {
                        return Integer.class;
                    }
                    if(columna>2&& columna<8){
                        return Double.class;
                    }

                    return Object.class;
                }
            };

            jTable1.setModel(metabs);
            int cantidadColumnas = rsMd.getColumnCount();
            for (int i = 1; i <= cantidadColumnas; i++) {
                
                if(i!=5){
                metabs.addColumn(rsMd.getColumnLabel(i));
                }else{
                    metabs.addColumn("Cant. Acum.");
                }
            }
            String numero;
            BigDecimal cantidadacum;
            while (rs.next()) {
                
                Object[] filas = new Object[cantidadColumnas];
                for (int i = 0; i < cantidadColumnas; i++) {
                    if (i == 7) {
                        estavalu = estavalu.add(rs.getBigDecimal(i+1));
                        System.out.println("estavalusin sumar "+(rs.getObject(i+1).toString()));
                        System.out.println("estavalu "+estavalu);
                        
                    }
                    if(i!=4){
                    filas[i] = rs.getObject(i + 1);
                    }else{
                        numero = rs.getObject(5).toString();
                        String consulto = "SELECT SUM(cantidad) as cantidad "
                                + "FROM dvalus WHERE numepart='"+numero+"' AND mpre_id='"+pres+"' AND "
                                + " mvalu_id<="+jSpinner1.getValue()+"";
                        Statement stconsulto = (Statement) conex.createStatement();
                        ResultSet rstconsulto = stconsulto.executeQuery(consulto);
                        while(rstconsulto.next()){
                            cantidadacum = rstconsulto.getBigDecimal(1);
                            filas[i]=cantidadacum;
                        }
                    }
                }
                metabs.addRow(filas);

            }
        } catch (SQLException ex) {
            Logger.getLogger(valuacion.class.getName()).log(Level.SEVERE, null, ex);
        }
        cambiarcabecera();

        estavalu = redon.redondearDosDecimales(estavalu);
        System.out.println("despues de redondear "+estavalu);
         NumberFormat formatoNumero = NumberFormat.getNumberInstance();
            formatoNumero.setMaximumFractionDigits(2);
            formatoNumero.setMinimumFractionDigits(2);
            jTextField4.setText(String.valueOf(formatoNumero.format(estavalu)));
      

        buscapres();
        llenaValu();
        
    }
   
    public void cambiarcabecera() {
        JTableHeader th = jTable1.getTableHeader();
        TableColumnModel tcm = th.getColumnModel();
        TableColumn tc = tcm.getColumn(0);

        tc.setHeaderValue("Código.");
        tc.setPreferredWidth(20);
        tc = tcm.getColumn(1);
        tc.setHeaderValue("Núm");
        tc.setPreferredWidth(2);
        tc = tcm.getColumn(2);
        tc.setHeaderValue("Descripción");
        tc.setPreferredWidth(150);
        tc = tcm.getColumn(3);
        tc.setHeaderValue("Cant. Contratada");
        tc.setPreferredWidth(15);
        tc = tcm.getColumn(4);
        tc.setHeaderValue("Cant. Acum");
        tc.setPreferredWidth(15);
        tc = tcm.getColumn(5);
        tc.setHeaderValue("Cant. a Valuar");
        tc.setPreferredWidth(15);
        tc = tcm.getColumn(6);
        tc.setHeaderValue("Precio");
        tc.setPreferredWidth(10);
        tc = tcm.getColumn(7);
        tc.setHeaderValue("Total");
        tc.setPreferredWidth(10);
        tc = tcm.getColumn(8);
        tc.setHeaderValue("Reconsideración");
        tc.setPreferredWidth(10);
        th.repaint();
    }

    public void buscapres() {
        
        String consulta = "SELECT porimp FROM mpres WHERE id='" + pres+"'";
        try {
            Statement stmt = (Statement) conex.createStatement();
            ResultSet rst = stmt.executeQuery(consulta);
            while (rst.next()) {
                impu = rst.getBigDecimal(1);

            }
        } catch (SQLException ex) {
            Logger.getLogger(valuacion.class.getName()).log(Level.SEVERE, null, ex);
        }
        impuesto = impu;
        subtotal = estavalu;
        /**
         * Calculo de Impuestos con acumulados (acumactual-relacionanterior)
         */
        String deltas="IF((IF(mp.precasu=0,mp.precunit,mp.precasu)-(SELECT IF(m.precasu=0,m.precunit,m.precasu) FROM "
                    + "mppres as m WHERE (m.mpre_id='"+pres+"' OR m.mpre_id IN "
                    + " (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) AND m.numero=mp.mppre_id))<0,0,"
                    + "IF(mp.precasu=0,mp.precunit,mp.precasu)-(SELECT IF(m.precasu=0,m.precunit,m.precasu) FROM "
                    + "mppres as m WHERE (m.mpre_id='"+pres+"' OR m.mpre_id IN "
                    + " (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) AND m.numero=mp.mppre_id))";
        
        BigDecimal anter= new BigDecimal("0.00"), acumula= new BigDecimal("0.00");
        BigDecimal impanter =new BigDecimal("0.00"), impacum = new BigDecimal("0.00");
        String anterior = "SELECT ROUND((SELECT SUM(cantidad) as cantidad "
                              + " FROM dvalus WHERE numepart=mp.numero AND mpre_id='"+pres+"' AND "
                              + " mvalu_id<"+mvalu+") * IF(tipo!='VP',IF(mp.precasu=0,mp.precunit,mp.precasu),"+deltas+"),2) "
                + "FROM dvalus as dv, mppres as mp WHERE"
                + " mp.numero=dv.numepart AND dv.mvalu_id <'"+jSpinner1.getValue()+"' AND (dv.mpre_id='" + pres + "' "
                    + "OR dv.mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"'))"
                    + " AND (mp.mpre_id='" + pres + "' "
                    + "OR mp.mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) GROUP BY dv.numepart";
        try {
            Statement stant = conex.createStatement();
            ResultSet rstant = stant.executeQuery(anterior);
            while(rstant.next())            
                anter = anter.add(rstant.getBigDecimal(1));
            
        } catch (SQLException ex) {
            Logger.getLogger(valuacion.class.getName()).log(Level.SEVERE, null, ex);
        }
        String acumulado = "SELECT ROUND((SELECT SUM(cantidad) as cantidad "
                              + " FROM dvalus WHERE numepart=mp.numero AND mpre_id='"+pres+"' AND "
                              + " mvalu_id<="+mvalu+") * IF(tipo!='VP',IF(mp.precasu=0,mp.precunit,mp.precasu),"+deltas+"),2) "
                + "FROM dvalus as dv, mppres as mp WHERE"
                + " mp.numero=dv.numepart AND dv.mvalu_id <='"+jSpinner1.getValue()+"' AND (dv.mpre_id='" + pres + "' "
                    + "OR dv.mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"'))"
                    + " AND (mp.mpre_id='" + pres + "' "
                    + "OR mp.mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) GROUP BY dv.numepart";
        try {
            Statement stacum = conex.createStatement();
            ResultSet rstacum = stacum.executeQuery(acumulado);
            while(rstacum.next())            
                acumula = acumula.add(rstacum.getBigDecimal(1));
            
        } catch (SQLException ex) {
            Logger.getLogger(valuacion.class.getName()).log(Level.SEVERE, null, ex);
        }
        impanter = redon.redondearDosDecimales(anter.multiply(impu.divide(new BigDecimal(100))));
        impacum =redon.redondearDosDecimales(acumula.multiply(impu.divide(new BigDecimal(100))));
        impu = impacum.subtract(impanter);
         NumberFormat formatoNumero = NumberFormat.getNumberInstance();
            formatoNumero.setMaximumFractionDigits(2);
            formatoNumero.setMinimumFractionDigits(2);
            jTextField5.setText(String.valueOf(formatoNumero.format(impu)));
        total = subtotal.add(impu);
        jTextField6.setText(String.valueOf(formatoNumero.format(total)));
        buscaacum();
    }

    public void buscaacum() {
        acum=new BigDecimal("0.00");
        String deltas="IF((IF(mp.precasu=0,mp.precunit,mp.precasu)-(SELECT IF(m.precasu=0,m.precunit,m.precasu) FROM "
                    + "mppres as m WHERE (m.mpre_id='"+pres+"' OR m.mpre_id IN "
                    + " (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) AND m.numero=mp.mppre_id))<0,0,"
                    + "IF(mp.precasu=0,mp.precunit,mp.precasu)-(SELECT IF(m.precasu=0,m.precunit,m.precasu) FROM "
                    + "mppres as m WHERE (m.mpre_id='"+pres+"' OR m.mpre_id IN "
                    + " (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) AND m.numero=mp.mppre_id))";
        
        String sql = "SELECT ROUND((SELECT SUM(cantidad) as cantidad "
                              + " FROM dvalus WHERE numepart=mp.numero AND mpre_id='"+pres+"' AND "
                              + " mvalu_id<="+mvalu+") * IF(tipo!='VP',IF(mp.precasu=0,mp.precunit,mp.precasu),"+deltas+"),2) "
                + "FROM dvalus as dv, mppres as mp WHERE"
                + " mp.numero=dv.numepart AND dv.mvalu_id <='"+jSpinner1.getValue()+"' AND (dv.mpre_id='" + pres + "' "
                    + "OR dv.mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"'))"
                    + " AND (mp.mpre_id='" + pres + "' "
                    + "OR mp.mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) GROUP BY dv.numepart";
        try {
            Statement stmt = (Statement) conex.createStatement();
            ResultSet rste = stmt.executeQuery(sql);

            while (rste.next()) {
                if (rste.getObject(1) != null) 
                    acum = acum.add(rste.getBigDecimal(1));
                
            }
            acum =  redon.redondearDosDecimales(acum.multiply(new BigDecimal(1).add(impuesto.divide(new BigDecimal("100")))));
            NumberFormat formatoNumero = NumberFormat.getNumberInstance();
            formatoNumero.setMaximumFractionDigits(2);
            formatoNumero.setMinimumFractionDigits(2);
            jTextField7.setText(String.valueOf(formatoNumero.format(acum)));

        } catch (SQLException ex) {
            Logger.getLogger(valuacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void doClose(int retStatus) {
        returnStatus = retStatus;
        setVisible(false);
        dispose();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox jComboBox1;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    private javax.swing.JButton okButton;
    // End of variables declaration//GEN-END:variables
    private int returnStatus = RET_CANCEL;
}
