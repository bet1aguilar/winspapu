/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * contratista.java
 *
 * Created on 02/11/2012, 11:44:57 AM
 */
package parametros;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.SpinnerListModel;
import javax.swing.filechooser.FileNameExtensionFilter;


public class contratistas extends javax.swing.JDialog {

  public String contra;
    public static final int RET_CANCEL = 0;
    public static final int RET_OK = 1;
    private Connection conex;
    File fichero;
    private String primero;
    private int edita=1;
    private int nuevo=0;
    public contratistas(java.awt.Frame parent, boolean modal, Connection conex) {
        super(parent, modal);
        initComponents();
       
        this.conex = conex;
        jTextField1.setVisible(false);
         definemodelo();
         
         cargaventana();
        
        jLabel9.setVisible(false);
        jLabel10.setVisible(false);
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

    public contratistas(java.awt.Frame parent, boolean modal, Connection conex, int i) {
        super(parent, modal);
        initComponents();
       
        this.conex = conex;
        nuevo=1;
        nuevo();
               
        jLabel9.setVisible(false);
        jLabel10.setVisible(false);
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
    public final void cargaventana(){
        String nombre=null, telefono=null, email=null, pagina=null;
        String ingins=null, cedins=null, civins=null, ingres=null, cedres=null, civres=null;
        String repleg=null, cedrep=null;
        String rif=null;
        String direcc=null, encabe=null;
        ImageIcon image=null;
        Blob bytes = null;
        byte[] leidos;
              
       String sql = "Select nombre, direcc, telefo, pagina, email, logo "
               + ", ingres, cedres, civres, ingins, cedins, civins"
               + ", repleg, cedrep, rif, encabe FROM mconts WHERE id='"+primero+"'";
        try {
            Statement st = (Statement) conex.createStatement();
            ResultSet rst = st.executeQuery(sql);
            while(rst.next()){
                nombre = rst.getString("nombre");
                telefono = rst.getString("telefo");
                email= rst.getString("email");
                pagina = rst.getString("pagina");
                bytes = (Blob) rst.getBlob("logo");
                ingins = rst.getString("ingins");
                cedins = rst.getString("cedins");
                civins = rst.getString("civins");
                ingres = rst.getString("ingres");
                direcc = rst.getString("direcc");
                cedres = rst.getString("cedres");
                civres = rst.getString("civres");
                repleg = rst.getString("repleg");
                cedrep = rst.getString("cedrep");
                rif = rst.getString("rif");
                encabe = rst.getString("encabe");
                
            }
            if(bytes!=null){
            leidos= bytes.getBytes(1,(int) bytes.length());
            image = new ImageIcon(leidos);
            Icon icono = new ImageIcon(image.getImage().getScaledInstance(image.getIconWidth(), image.getIconHeight(), Image.SCALE_DEFAULT));
                jLabel14.setIcon(icono);
            }else{
                jLabel14.setIcon(null);
            }
                jTextField15.setText(nombre);
                jTextField2.setText(telefono);
                jTextField3.setText(pagina);
                jTextField4.setText(email);
                jTextArea2.setText(direcc);
                jTextField5.setText(rif);
                jTextField6.setText(ingres);
                jTextField8.setText(cedres);
                jTextField9.setText(civres);
                jTextField10.setText(ingins);
                jTextField11.setText(cedins);
                jTextField12.setText(civins);
                jTextField13.setText(repleg);
                jTextField14.setText(cedrep);
                jTextArea3.setText(encabe);
        } catch (SQLException ex) {
            Logger.getLogger(propietario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
 public final void definemodelo(){
         String consulta = "SELECT id From mconts";
        String [] prop;
        try {
            Statement st = (Statement) conex.createStatement();
            ResultSet rst = st.executeQuery(consulta);
            rst.last();
            prop = new String[rst.getRow()];
            rst.first();
            prop[0]=rst.getString(1);
            int i=1;
            while(rst.next()){
                prop[i]=rst.getString(1);
                i++;
            }
            primero = prop[0];
            SpinnerListModel modelo = new SpinnerListModel(prop);
            jSpinner1.setModel(modelo);
        } catch (SQLException ex) {
            Logger.getLogger(propietario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public int getReturnStatus() {
        return returnStatus;
    }

   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jLabel8 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jSpinner1 = new javax.swing.JSpinner();
        jTextField8 = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jTextField9 = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jTextField10 = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jTextField11 = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jTextField12 = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        okButton = new javax.swing.JButton();
        okButton1 = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jTextField13 = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jTextField14 = new javax.swing.JTextField();
        jTextField15 = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea3 = new javax.swing.JTextArea();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel3 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        jPanel1.setPreferredSize(new java.awt.Dimension(800, 604));

        jPanel2.setBackground(new java.awt.Color(91, 91, 95));
        jPanel2.setPreferredSize(new java.awt.Dimension(707, 45));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Contratistas");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 707, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
        );

        jLabel2.setText("Código:");

        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField1KeyTyped(evt);
            }
        });

        jLabel3.setText("Nombre:");

        jLabel4.setText("Teléfono:");

        jLabel5.setText("Página:");

        jLabel6.setText("Correo:");

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 14));
        jLabel9.setForeground(new java.awt.Color(255, 0, 0));
        jLabel9.setText("*");

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 14));
        jLabel10.setForeground(new java.awt.Color(255, 0, 0));
        jLabel10.setText("*");

        jLabel7.setText("Dirección:");

        jTextArea2.setColumns(20);
        jTextArea2.setLineWrap(true);
        jTextArea2.setRows(3);
        jTextArea2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextArea2KeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextArea2KeyTyped(evt);
            }
        });
        jScrollPane2.setViewportView(jTextArea2);

        jLabel8.setText("RIF:");

        jLabel11.setText("Ing. Residente:");

        jTextField6.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField6KeyTyped(evt);
            }
        });

        jLabel12.setText("Logo:");

        jTextField7.setEditable(false);
        jTextField7.setToolTipText("Enlace del Logo");
        jTextField7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField7ActionPerformed(evt);
            }
        });

        jButton1.setText("Examinar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jSpinner1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner1StateChanged(evt);
            }
        });

        jTextField8.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField8KeyTyped(evt);
            }
        });

        jLabel13.setText("C.I.");

        jLabel15.setText("C.I.V.");

        jLabel16.setText("Ing. Inspector:");

        jTextField10.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField10KeyTyped(evt);
            }
        });

        jLabel17.setText("C.I.");

        jTextField11.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField11KeyTyped(evt);
            }
        });

        jLabel18.setText("C.I.V.");

        jLabel19.setText("Representante Legal:");

        okButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/guardar.png"))); // NOI18N
        okButton.setToolTipText("Guardar");
        okButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                okButtonMouseClicked(evt);
            }
        });

        okButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/nuevo.png"))); // NOI18N
        okButton1.setToolTipText("Nuevo");
        okButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButton1ActionPerformed(evt);
            }
        });

        cancelButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/eliminar.png"))); // NOI18N
        cancelButton.setToolTipText("Cancelar");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/borra.png"))); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(okButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(okButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        getRootPane().setDefaultButton(okButton);
        getRootPane().setDefaultButton(okButton);

        jTextField13.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField13KeyTyped(evt);
            }
        });

        jLabel20.setText("C.I.");

        jTextField14.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField14KeyTyped(evt);
            }
        });

        jTextField15.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField15KeyTyped(evt);
            }
        });

        jLabel21.setText("Encabezado:");

        jTextArea3.setColumns(20);
        jTextArea3.setRows(3);
        jTextArea3.setWrapStyleWord(true);
        jTextArea3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextArea3KeyTyped(evt);
            }
        });
        jScrollPane3.setViewportView(jTextArea3);

        jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.lightGray, java.awt.Color.gray, java.awt.Color.black, java.awt.Color.darkGray));

        jLabel14.setBackground(new java.awt.Color(153, 153, 153));
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, 131, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(jPanel3);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel7)
                    .addComponent(jLabel3)
                    .addComponent(jLabel2)
                    .addComponent(jLabel21))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 542, Short.MAX_VALUE)
                            .addComponent(jTextField15, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 542, Short.MAX_VALUE)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 542, Short.MAX_VALUE)
                            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel9)
                        .addGap(35, 35, 35))))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel19)
                    .addComponent(jLabel16)
                    .addComponent(jLabel11)
                    .addComponent(jLabel12)
                    .addComponent(jLabel8)
                    .addComponent(jLabel6)
                    .addComponent(jLabel5)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 383, Short.MAX_VALUE)
                    .addComponent(jTextField3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 383, Short.MAX_VALUE)
                    .addComponent(jTextField4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 383, Short.MAX_VALUE)
                    .addComponent(jTextField5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 383, Short.MAX_VALUE)
                    .addComponent(jTextField7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 383, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField6, javax.swing.GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE)
                            .addComponent(jTextField10, javax.swing.GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE)
                            .addComponent(jTextField13, javax.swing.GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel20)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField14, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel15)
                                .addGap(2, 2, 2)
                                .addComponent(jTextField9))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel17)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(16, 16, 16)
                                .addComponent(jLabel18)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(109, 109, 109)
                        .addComponent(jButton1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(7, 7, 7)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jTextField15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel9))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13)
                            .addComponent(jLabel11)
                            .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel16)
                                    .addComponent(jLabel17)
                                    .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel18)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                                .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel19)
                            .addComponent(jTextField13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel20)
                            .addComponent(jTextField14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 69, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 707, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 555, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        doClose(RET_CANCEL);
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        doClose(RET_CANCEL);
    }//GEN-LAST:event_closeDialog

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
       JFileChooser fileChooser = new JFileChooser();
       File rutaactual= new File("./logos");
       fileChooser.setCurrentDirectory(rutaactual);
       FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG, GIF, PNG", "jpg", "gif", "png", "jpeg");
        fileChooser.setFileFilter(filter);
        int seleccion = fileChooser.showOpenDialog(jTextField7);
        if (seleccion == JFileChooser.APPROVE_OPTION)
        {
            fichero = fileChooser.getSelectedFile();
            jTextField7.setText(fichero.getPath().toString());
            ImageIcon fot = new ImageIcon(fichero.getPath().toString());
            Icon icono = new ImageIcon(fot.getImage().getScaledInstance(fot.getIconWidth(),fot.getIconHeight(), Image.SCALE_DEFAULT));
            jLabel14.setIcon(icono);
            this.repaint();
            BufferedImage img = new BufferedImage(icono.getIconWidth(), icono.getIconHeight(), BufferedImage.TYPE_INT_RGB);
                Image imge = ((ImageIcon) icono).getImage();
                Graphics2D g2 = img.createGraphics();
                g2.drawImage(imge, 0, 0, null);
                g2.dispose();
            try {
                ImageIO.write(img, "jpg", new File("img.jpg"));
            } catch (IOException ex) {
                Logger.getLogger(contratistas.class.getName()).log(Level.SEVERE, null, ex);
            }
                
                fileChooser.setSelectedFile(new File("img.jpg"));
                fichero = fileChooser.getSelectedFile();
        }
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void okButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButton1ActionPerformed
        
        
       nuevo();
    }//GEN-LAST:event_okButton1ActionPerformed
public final void nuevo(){
     Rectangle rect = new Rectangle();
        jSpinner1.setVisible(false);
        rect.setBounds(jSpinner1.getBounds());
        
        jTextField1.setVisible(true);
        jTextField1.setBounds(rect);
        jTextField1.setText("");
        jTextField15.setText("");
        
        jTextField2.setText("");
        jTextField3.setText("");
        jTextField4.setText("");
        jTextField5.setText("");
        jTextField6.setText("");
        jTextField8.setText("");
        jTextField9.setText("");
        jTextField10.setText("");
       jTextField11.setText("");
       jTextField12.setText("");
       jTextField13.setText("");
       jTextField14.setText("");
       jTextArea3.setText("");
        jTextField7.setText("");
        edita=0;
        nuevo=1;
        jLabel14.setIcon(null);
}
    private void jSpinner1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner1StateChanged
      if(nuevo==0){
        primero = jSpinner1.getValue().toString();
        cargaventana();
        }
    }//GEN-LAST:event_jSpinner1StateChanged

    private void jTextField7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField7ActionPerformed
       
    }//GEN-LAST:event_jTextField7ActionPerformed

    private void jTextArea2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextArea2KeyPressed

        
        
                if (evt.getKeyCode() == KeyEvent.VK_TAB) {
            if (evt.getModifiers() == KeyEvent.SHIFT_MASK) {
                ((javax.swing.JTextArea) evt.getSource()).transferFocusBackward();
            } else {
                ((javax.swing.JTextArea) evt.getSource()).transferFocus();
            }
            evt.consume();
                }// TODO add your handling code here:
    }//GEN-LAST:event_jTextArea2KeyPressed

private void okButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_okButtonMouseClicked
        int vacio=0;
        String nombre = null, direcc = null, telefo= null, pagina = null;
        String email = null, rif = null, ingres= null, cedres= null;
        String civres = null, ingins = null, cedins = null, civins = null;
        String repleg = null, cedrep = null, encabe = null, logo = null;
        Statement st = null ;
        try {
            st= (Statement) conex.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(contratistas.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(nuevo==0){
            jTextField1.setText(jSpinner1.getValue().toString());
        }
        if(jTextField1.getText().toString().equals("")|| jTextField15.getText().toString().equals("")){
            vacio = 1;            
        }
        if(vacio==0){
            contra=jTextField15.getText().toString();
            jLabel9.setVisible(false);
            nombre = jTextField15.getText().toString();
            direcc = jTextArea2.getText().toString();
            telefo = jTextField2.getText().toString();
            pagina = jTextField3.getText().toString();
            email =  jTextField4.getText().toString();
            rif = jTextField5.getText().toString();
            ingres = jTextField6.getText().toString();
            cedres = jTextField8.getText().toString();
            civres = jTextField9.getText().toString();
            ingins = jTextField10.getText().toString();
            cedins = jTextField11.getText().toString();
            civins = jTextField12.getText().toString();
            repleg = jTextField13.getText().toString();
            cedrep = jTextField14.getText().toString();
            encabe = jTextArea3.getText().toString();
        jLabel10.setVisible(false);
        if(edita==0){
            if(fichero!=null){
            String sql = "INSERT mconts (id, nombre, direcc, telefo, pagina, "
                    + "email, rif, ingres, "
                    + "cedres, civres, ingins, cedins, civins, repleg, cedrep, "
                    + "encabe, status,logo) VALUES ("
                    + "'"+jTextField1.getText().toString()+"',"
                    + "'"+jTextField15.getText().toString()+"',"
                    + "'"+jTextArea2.getText().toString()+"',"
                     + "'"+jTextField2.getText().toString()+"',"
                    + "'"+jTextField3.getText().toString()+"',"
                    + "'"+jTextField4.getText().toString()+"',"
                    + "'"+jTextField5.getText().toString()+"',"
                    + "'"+jTextField6.getText().toString()+"',"
                    + "'"+jTextField8.getText()+"',"
                    + "'"+civres+"', '"+ingins+"', '"+cedins+"', '"+civins+"',"
                    + "'"+repleg+"', '"+cedrep+"', '"+encabe+"',"
                    + "'1',?)";
            System.out.println(sql);
            PreparedStatement ps = null;
            try {
                ps = (PreparedStatement) conex.prepareStatement(sql);
            } catch (SQLException ex) {
                Logger.getLogger(contratistas.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                conex.setAutoCommit(false);
                
            } catch (SQLException ex) {
                Logger.getLogger(contratistas.class.getName()).log(Level.SEVERE, null, ex);
            }
                    try {
                        FileInputStream fis = new FileInputStream(fichero);
                try {
                    ps.setBinaryStream(1, fis, (int) fichero.length());
                } catch (SQLException ex) {
                    Logger.getLogger(contratistas.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Se ha añadido un nuevo contratista");
                } catch (SQLException ex) {
                    Logger.getLogger(contratistas.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    conex.commit();
                } catch (SQLException ex) {
                    Logger.getLogger(contratistas.class.getName()).log(Level.SEVERE, null, ex);
                }
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(propietario.class.getName()).log(Level.SEVERE, null, ex);
                    }
        }else{
                String sql = "INSERT mconts (id, nombre, direcc, telefo, pagina, "
                    + "email, rif, ingres, "
                    + "cedres, civres, ingins, cedins, civins, repleg, cedrep, "
                    + "encabe, status) VALUES ("
                    + "'"+jTextField1.getText().toString()+"',"
                    + "'"+jTextField15.getText().toString()+"',"
                    + "'"+jTextArea2.getText().toString()+"',"
                    + "'"+jTextField2.getText().toString()+"',"
                    + "'"+jTextField3.getText().toString()+"',"
                    + "'"+jTextField4.getText().toString()+"',"
                    + "'"+jTextField5.getText().toString()+"',"
                    + "'"+jTextField6.getText().toString()+"',"
                    + "'"+jTextField8.getText()+"',"
                    + "'"+civres+"', '"+ingins+"', '"+cedins+"', '"+civins+"',"
                    + "'"+repleg+"', '"+cedrep+"', '"+encabe+"',"
                    + "'1')";
            System.out.println(sql);
                    try {
                        Statement str = (Statement) conex.createStatement();
                        str.execute(sql);
                        JOptionPane.showMessageDialog(this, "Se ha añadido un nuevo contratista");
                    } catch (SQLException ex) {
                        Logger.getLogger(contratistas.class.getName()).log(Level.SEVERE, null, ex);
                    }
            
            }
        }else{
            if(fichero!=null){
                    try {
                        String sql = "UPDATE mconts SET nombre='"+nombre+"', direcc = '"+direcc+"',"
                                + "telefo='"+telefo+"', pagina='"+pagina+"', email='"+email+"',"
                                + "rif='"+rif+"', ingres='"+ingres+"', cedres='"+cedres+"',"
                                + "civres='"+civres+"', ingins='"+ingins+"', cedins='"+cedins+"',"
                                + "civins='"+civins+"', repleg='"+repleg+"', cedrep='"+cedrep+"',"
                                + "encabe='"+encabe+"', status=1, logo=? "
                                + "WHERE id='"+jSpinner1.getValue().toString().toUpperCase()+"'";
                        PreparedStatement ps = (PreparedStatement) conex.prepareStatement(sql);
                             conex.setAutoCommit(false);
                            try {
                                FileInputStream fis = new FileInputStream(fichero);
                                ps.setBinaryStream(1, fis, (int) fichero.length());
                                 ps.executeUpdate();
                                conex.commit();
                            } catch (FileNotFoundException ex) {
                                Logger.getLogger(propietario.class.getName()).log(Level.SEVERE, null, ex);
                            }
                             System.out.println(sql);
                    } catch (SQLException ex) {
                        Logger.getLogger(contratistas.class.getName()).log(Level.SEVERE, null, ex);
                    }
            }else{
                    try {
                        String sql = "UPDATE mconts SET nombre='"+nombre+"', direcc = '"+direcc+"',"
                                   + "telefo='"+telefo+"', pagina='"+pagina+"', email='"+email+"',"
                                   + "rif='"+rif+"', ingres='"+ingres+"', cedres='"+cedres+"',"
                                   + "civres='"+civres+"', ingins='"+ingins+"', cedins='"+cedins+"',"
                                   + "civins='"+civins+"', repleg='"+repleg+"', cedrep='"+cedrep+"',"
                                   + "encabe='"+encabe+"', status=1 "
                                   + "WHERE id='"+jSpinner1.getValue().toString().toUpperCase()+"'";
                        Statement ste = (Statement) conex.createStatement();
                        ste.execute(sql);
                    } catch (SQLException ex) {
                        Logger.getLogger(contratistas.class.getName()).log(Level.SEVERE, null, ex);
                    }
            }
            jTextField1.setVisible(false);
           jSpinner1.setVisible(true);           
           primero= jSpinner1.getValue().toString();
           cargaventana();
           jTextField7.setText("");
           nuevo=0;
                    
        }
        }else{
            jLabel9.setVisible(true);
            jLabel10.setVisible(true);
            
        }
        
        JOptionPane.showMessageDialog(rootPane, "Se ha guardado el registro");// TODO add your handling code here:
}//GEN-LAST:event_okButtonMouseClicked

private void jTextField1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyTyped
   Character c = evt.getKeyChar();
                if(Character.isLetter(c)) {
                    evt.setKeyChar(Character.toUpperCase(c));
                }// TODO add your handling code here:
}//GEN-LAST:event_jTextField1KeyTyped

private void jTextField15KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField15KeyTyped
   Character c = evt.getKeyChar();
                if(Character.isLetter(c)) {
                    evt.setKeyChar(Character.toUpperCase(c));
                }// TODO add your handling code here:
}//GEN-LAST:event_jTextField15KeyTyped

private void jTextArea2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextArea2KeyTyped
   Character c = evt.getKeyChar();
                if(Character.isLetter(c)) {
                    evt.setKeyChar(Character.toUpperCase(c));
                }// TODO add your handling code here:
}//GEN-LAST:event_jTextArea2KeyTyped

private void jTextField6KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField6KeyTyped
   Character c = evt.getKeyChar();
                if(Character.isLetter(c)) {
                    evt.setKeyChar(Character.toUpperCase(c));
                }// TODO add your handling code here:
}//GEN-LAST:event_jTextField6KeyTyped

private void jTextField10KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField10KeyTyped
   Character c = evt.getKeyChar();
                if(Character.isLetter(c)) {
                    evt.setKeyChar(Character.toUpperCase(c));
                }// TODO add your handling code here:
}//GEN-LAST:event_jTextField10KeyTyped

private void jTextField13KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField13KeyTyped
   Character c = evt.getKeyChar();
                if(Character.isLetter(c)) {
                    evt.setKeyChar(Character.toUpperCase(c));
                }// TODO add your handling code here:
}//GEN-LAST:event_jTextField13KeyTyped

private void jTextField8KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField8KeyTyped
   Character c = evt.getKeyChar();
                if(Character.isLetter(c)) {
                    evt.setKeyChar(Character.toUpperCase(c));
                }// TODO add your handling code here:
}//GEN-LAST:event_jTextField8KeyTyped

private void jTextField11KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField11KeyTyped
   Character c = evt.getKeyChar();
                if(Character.isLetter(c)) {
                    evt.setKeyChar(Character.toUpperCase(c));
                }// TODO add your handling code here:
}//GEN-LAST:event_jTextField11KeyTyped

private void jTextField14KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField14KeyTyped
 Character c = evt.getKeyChar();
                if(Character.isLetter(c)) {
                    evt.setKeyChar(Character.toUpperCase(c));
                }// TODO add your handling code here:
}//GEN-LAST:event_jTextField14KeyTyped

private void jTextArea3KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextArea3KeyTyped
 Character c = evt.getKeyChar();
                if(Character.isLetter(c)) {
                    evt.setKeyChar(Character.toUpperCase(c));
                }// TODO add your handling code here:
}//GEN-LAST:event_jTextArea3KeyTyped

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        String id=jSpinner1.getValue().toString();
         String consulta="SELECT COUNT(*) FROM mpres WHERE codcon='"+id+"'";
         int cuenta=0;
     
        try {
            Statement count = (Statement) conex.createStatement();
            ResultSet rstcount = count.executeQuery(consulta);
            while(rstcount.next()){
                cuenta=rstcount.getInt(1);
            }
            if(cuenta>0){
                JOptionPane.showMessageDialog(rootPane, "La contratista está relacionada con uno o más presupuestos, no puede ser eliminada hasta que los presupuestos relacionados sean eliminados");
                
            }else{
                int op=JOptionPane.showConfirmDialog(rootPane, "¿Desea Eliminar la contratista "+id+"?", "Eliminar contratista", JOptionPane.YES_NO_OPTION);
          if(op==JOptionPane.YES_OPTION){
              String borra = "DELETE FROM mconts WHERE id='"+id+"'";
              Statement stborra = (Statement) conex.createStatement();
              stborra.execute(borra);
              JOptionPane.showMessageDialog(rootPane, "La contratista "+id+" ha sido eliminada");
               definemodelo();
         
         cargaventana();
          }   
                
            }
                    
        } catch (SQLException ex) {
            Logger.getLogger(contratistas.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed
    
    private void doClose(int retStatus) {
        returnStatus = retStatus;
        setVisible(false);
        dispose();
    }

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
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
    private javax.swing.JLabel jLabel21;
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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JTextArea jTextArea3;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField12;
    private javax.swing.JTextField jTextField13;
    private javax.swing.JTextField jTextField14;
    private javax.swing.JTextField jTextField15;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    private javax.swing.JButton okButton;
    private javax.swing.JButton okButton1;
    // End of variables declaration//GEN-END:variables
    private int returnStatus = RET_CANCEL;
}
