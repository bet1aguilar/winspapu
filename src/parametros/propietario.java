/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * propietario.java
 *
 * Created on 27/10/2012, 08:36:11 PM
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

/**
 *
 * @author Betmart
 */
public class propietario extends javax.swing.JDialog {

    public static final int RET_CANCEL = 0;
    public static final int RET_OK = 1;
    private Connection conex;
    int edita=1;
    Icon icono;
    public String prop;
    String primero;
    File fichero;
    int nuevo;
    public propietario(java.awt.Frame parent, boolean modal, Connection conex) {
        super(parent, modal);
        initComponents();
        jTextField1.setVisible(false);
        this.conex = conex;
        jLabel21.setVisible(false);
        jLabel10.setVisible(false);
        jLabel9.setVisible(false);
        definemodelo();
        cargaventana();
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

    public propietario(java.awt.Frame parent, boolean modal, Connection conex, int i) {
        super(parent, modal);
        initComponents();
        jTextField1.setVisible(false);
        this.conex = conex;
        nuevo= 1;
        jLabel21.setVisible(false);
        jLabel10.setVisible(false);
        jLabel9.setVisible(false);
        nuevo();
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
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel4 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jLabel7 = new javax.swing.JLabel();
        jSpinner1 = new javax.swing.JSpinner();
        jPanel4 = new javax.swing.JPanel();
        cancelButton = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        okButton = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(91, 91, 95));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Propietarios");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 751, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(12, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addContainerGap())
        );

        jLabel2.setText("Código:");

        jLabel3.setText("Nombre:");

        jTextArea1.setColumns(20);
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(2);
        jTextArea1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextArea1KeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextArea1KeyTyped(evt);
            }
        });
        jScrollPane1.setViewportView(jTextArea1);

        jLabel4.setText("Teléfono:");

        jLabel5.setText("Página:");

        jLabel6.setText("Correo:");

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 14));
        jLabel9.setForeground(new java.awt.Color(255, 0, 0));
        jLabel9.setText("*");

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 14));
        jLabel10.setForeground(new java.awt.Color(255, 0, 0));
        jLabel10.setText("*");

        jLabel21.setForeground(new java.awt.Color(255, 0, 0));
        jLabel21.setText("* Campo no puede ser vacio");

        jLabel12.setText("Logo:");

        jTextField7.setEditable(false);

        jButton1.setText("Examinar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.gray, new java.awt.Color(153, 153, 153), java.awt.Color.black, java.awt.Color.darkGray));
        jPanel3.setAutoscrolls(true);

        jLabel7.setAutoscrolls(true);
        jScrollPane2.setViewportView(jLabel7);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 226, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)
        );

        jSpinner1.setModel(new javax.swing.SpinnerNumberModel());
        jSpinner1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner1StateChanged(evt);
            }
        });

        cancelButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/eliminar.png"))); // NOI18N
        cancelButton.setToolTipText("Cancelar");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/nuevo.png"))); // NOI18N
        jButton2.setToolTipText("Nuevo");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

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

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/borra.png"))); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(93, Short.MAX_VALUE)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        getRootPane().setDefaultButton(okButton);

        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField1KeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2)
                            .addComponent(jLabel5)
                            .addComponent(jLabel4)
                            .addComponent(jLabel6)
                            .addComponent(jLabel12))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jTextField7)
                                        .addComponent(jTextField4, javax.swing.GroupLayout.DEFAULT_SIZE, 397, Short.MAX_VALUE)
                                        .addComponent(jTextField2)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 397, Short.MAX_VALUE)
                                        .addComponent(jTextField3))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel21)
                                        .addGap(98, 98, 98)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel9)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jButton1)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(jLabel10)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jSpinner1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel9)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel4)
                                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel5)
                                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(6, 6, 6)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel6)))
                                    .addComponent(jPanel3, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel12))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel21)))))
                .addGap(13, 13, 13)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    public final void cargaventana(){
        String nombre=null, telefono=null, email=null, pagina=null;
        ImageIcon image=null;
        Blob bytes = null;
        byte[] leidos;
              
       String sql = "Select nombre, telefo, pagina, email, logo FROM mprops WHERE id='"+primero+"'";
        try {
            Statement st = (Statement) conex.createStatement();
            ResultSet rst = st.executeQuery(sql);
            while(rst.next()){
                nombre = rst.getString(1);
                telefono = rst.getString(2);
                email= rst.getString(4);
                pagina = rst.getString(3);
                bytes = (Blob) rst.getBlob(5);
                
            }
            if(bytes!=null){
            leidos= bytes.getBytes(1,(int) bytes.length());
            image = new ImageIcon(leidos);
            icono = new ImageIcon(image.getImage().getScaledInstance(image.getIconWidth(), image.getIconHeight(), Image.SCALE_DEFAULT));
            
            jLabel7.setIcon(icono);
            }else{
                jLabel7.setIcon(null);
            }
            jTextArea1.setText(nombre);
            jTextField2.setText(telefono);
             jTextField3.setText(pagina);
              jTextField4.setText(email);
        } catch (SQLException ex) {
            Logger.getLogger(propietario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public final void definemodelo(){
        String consulta = "SELECT id From mprops";
        String [] prop;
        try {
            Statement st = (Statement) conex.createStatement();
            ResultSet rst = st.executeQuery(consulta);
            rst.last();
            prop = new String[rst.getRow()];
            rst.first();
            prop[0]=rst.getString(1);
            int i=0;
            do{
                prop[i]=rst.getString(1);
                i++;
            }while(rst.next());
            primero = prop[0];
            SpinnerListModel modelo = new SpinnerListModel(prop);
            jSpinner1.setModel(modelo);
        } catch (SQLException ex) {
            Logger.getLogger(propietario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public String getprop(){
        return prop;
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

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG, GIF, PNG", "jpg", "gif", "png", "jpeg");
        fileChooser.setFileFilter(filter);
        int seleccion = fileChooser.showOpenDialog(jTextField7);
        if (seleccion == JFileChooser.APPROVE_OPTION) {
            try {
                fichero = fileChooser.getSelectedFile();
                 jTextField7.setText(fichero.getPath().toString());
                ImageIcon fot = new ImageIcon(fichero.getPath().toString());
                icono = new ImageIcon(fot.getImage().getScaledInstance(fot.getIconWidth(), fot.getIconHeight(), Image.SCALE_AREA_AVERAGING));
                jLabel7.setIcon(icono); 
                BufferedImage img = new BufferedImage(icono.getIconWidth(), icono.getIconHeight(), BufferedImage.TYPE_INT_RGB);
                Image imge = ((ImageIcon) icono).getImage();
                Graphics2D g2 = img.createGraphics();
                g2.drawImage(imge, 0, 0, null);
                g2.dispose();
                ImageIO.write(img, "jpg", new File("img.jpg"));
                
                fileChooser.setSelectedFile(new File("img.jpg"));
                fichero = fileChooser.getSelectedFile();
                this.repaint();
            } catch (IOException ex) {
                Logger.getLogger(propietario.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
nuevo();
    }//GEN-LAST:event_jButton2ActionPerformed

    public final void nuevo(){
        Rectangle rect = new Rectangle();
        jSpinner1.setVisible(false);
        rect.setBounds(jSpinner1.getBounds());
        
        jTextField1.setVisible(true);
        jTextField1.setBounds(rect);
        jTextArea1.setText("");
        jTextField2.setText("");
        jTextField3.setText("");
        jTextField4.setText("");
        jTextField7.setText("");
        edita=0;
        nuevo=1;
        jLabel7.setIcon(null);
    }
    private void jSpinner1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner1StateChanged
        
        if(nuevo==0){
        primero = jSpinner1.getValue().toString();
        cargaventana();
        }
    }//GEN-LAST:event_jSpinner1StateChanged

    private void jTextArea1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextArea1KeyPressed

                if (evt.getKeyCode() == KeyEvent.VK_TAB) {
            if (evt.getModifiers() == KeyEvent.SHIFT_MASK) {
                ((javax.swing.JTextArea) evt.getSource()).transferFocusBackward();
            } else {
                ((javax.swing.JTextArea) evt.getSource()).transferFocus();
            }
            evt.consume();
        }        // TODO add your handling code here:// TODO add your handling code here:
    }//GEN-LAST:event_jTextArea1KeyPressed

private void okButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_okButtonMouseClicked
       int vacio=0; 
       String sql;
        if(jTextArea1.getText().toString().equals("")|| jSpinner1.getValue().toString().equals("")){
            vacio=1;
        }
        if(vacio==0){
            try {
                jLabel10.setVisible(false);
            jLabel21.setVisible(false);
            jLabel9.setVisible(false);
                Statement st = (Statement) conex.createStatement();
                if(edita==0){
                     prop = jTextArea1.getText().toString().toUpperCase();
                    if(fichero!=null){
                        prop = jTextArea1.getText().toString().toUpperCase();
                        System.out.println("prop de la clase propietario: "+prop);
                    sql = "INSERT INTO mprops (id, nombre, telefo, pagina, email,status, logo) VALUES"
                          + "('"+jTextField1.getText().toString().toUpperCase()+"',"
                            + "'"+jTextArea1.getText().toString().toUpperCase()+"',"
                            + "'"+jTextField2.getText().toString()+"',"
                            + "'"+jTextField3.getText().toString()+"',"
                            + "'"+jTextField4.getText().toString()+"',"                          
                            + "'1', ?)";
                     PreparedStatement ps = (PreparedStatement) conex.prepareStatement(sql);
                     conex.setAutoCommit(false);
                    try {
                        
                        FileInputStream fis = new FileInputStream(fichero);
                        ps.setBinaryStream(1, fis, (int) fichero.length());
                         ps.executeUpdate();
                        conex.commit();
                        System.out.println(fichero);
                        
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(propietario.class.getName()).log(Level.SEVERE, null, ex);
                    }
                     System.out.println(sql);
                    
                    JOptionPane.showMessageDialog(this, "Se ha añadido un nuevo Propietario");
                    }else{
                        sql = "INSERT INTO mprops (id, nombre, telefo, pagina, email,status) VALUES"
                          + "('"+jTextField1.getText().toString().toUpperCase()+"',"
                            + "'"+jTextArea1.getText().toString().toUpperCase()+"',"
                            + "'"+jTextField2.getText().toString()+"',"
                            + "'"+jTextField3.getText().toString()+"',"
                            + "'"+jTextField4.getText().toString()+"',"                          
                            + "'1')";
                     Statement ste = (Statement) conex.createStatement();
                     ste.execute(sql);
                     System.out.println(sql);
                    
                    JOptionPane.showMessageDialog(this, "Se ha añadido un nuevo Propietario");
                    }
                }else{
                     prop = jTextArea1.getText().toString().toUpperCase();
                    if(fichero!=null){
                    sql = "UPDATE mprops SET nombre='"+jTextArea1.getText().toString().toUpperCase()+"'"
                            + ", telefo='"+jTextField2.getText().toString()+"'"
                            + ", pagina='"+jTextField3.getText().toString()+"'"
                            + ", email='"+jTextField4.getText().toString()+"', status=1,"
                            + " logo= ? WHERE id='"+jSpinner1.getValue().toString().toUpperCase()+"'";
                     PreparedStatement ps = (PreparedStatement) conex.prepareStatement(sql);
                     conex.setAutoCommit(false);
                    try {
                        FileInputStream fis = new FileInputStream(fichero);
                        ps.setBinaryStream(1, fis, (int) fichero.length());
                         ps.execute();
                        conex.commit();
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(propietario.class.getName()).log(Level.SEVERE, null, ex);
                    }
                     System.out.println(fichero);
                    }else{
                        sql = "UPDATE mprops SET nombre='"+jTextArea1.getText().toString().toUpperCase()+"'"
                            + ", telefo='"+jTextField2.getText().toString()+"'"
                            + ", pagina='"+jTextField3.getText().toString()+"'"
                            + ", email='"+jTextField4.getText().toString()+"', status=1"
                            + "  WHERE id='"+jSpinner1.getValue().toString().toUpperCase()+"'";
                        Statement ste = (Statement) conex.createStatement();
                        ste.execute(sql);
                    }
                    JOptionPane.showMessageDialog(this, "Se ha añadido un nuevo Propietario");
                }
                jTextField1.setVisible(false);
           jSpinner1.setVisible(true);
        
           jTextField7.setText("");
           edita=1;
           nuevo=0;
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Ocurrió un error al tratar de agregar un nuevo Propietario");
                Logger.getLogger(propietario.class.getName()).log(Level.SEVERE, null, ex);
            }
           
        }else{
            jLabel10.setVisible(true);
            jLabel21.setVisible(true);
            jLabel9.setVisible(true);
            
        }
         doClose(RET_CANCEL);// TODO add your handling code here:
}//GEN-LAST:event_okButtonMouseClicked

private void jTextField1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyTyped
 Character c = evt.getKeyChar();
                if(Character.isLetter(c)) {
                    evt.setKeyChar(Character.toUpperCase(c));
                }// TODO add your handling code here:
}//GEN-LAST:event_jTextField1KeyTyped

private void jTextArea1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextArea1KeyTyped
 Character c = evt.getKeyChar();
                if(Character.isLetter(c)) {
                    evt.setKeyChar(Character.toUpperCase(c));
                }// TODO add your handling code here:
}//GEN-LAST:event_jTextArea1KeyTyped

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        String id=jSpinner1.getValue().toString();
        int cuenta=0;
         String consulta = "SELECT COUNT(*) FROM mpres WHERE codpro='"+id+"'";
        try {
            Statement stcon = (Statement) conex.createStatement();
            ResultSet rstcon = stcon.executeQuery(consulta);
            while(rstcon.next()){
                cuenta=rstcon.getInt(1);
            }
            if(cuenta>0){
                JOptionPane.showMessageDialog(rootPane, "No se puede eliminar propietario porque hay presupuestos con este propietario relacionado, elimine los presupuestos relacionados para poder eliminar este propietario");
            }else{
                int op = JOptionPane.showConfirmDialog(rootPane, "¿Desea Eliminar el propietario?", "Eliminar Propietario", JOptionPane.YES_NO_OPTION);
                if(op==JOptionPane.YES_OPTION){
                    String delete = "DELETE FROM mprops WHERE id='"+id+"'";
                    Statement std = (Statement) conex.createStatement();
                    std.execute(delete);
                    JOptionPane.showMessageDialog(rootPane, "El propietario "+id+" ha sido eliminado");
                      definemodelo();
        cargaventana();
                }
                        
            }
        } catch (SQLException ex) {
            Logger.getLogger(propietario.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed
    
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
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JButton okButton;
    // End of variables declaration//GEN-END:variables
    private int returnStatus = RET_CANCEL;
}
