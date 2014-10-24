/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * parametrorecon.java
 *
 * Created on 17/06/2014, 02:25:33 PM
 */
package valuaciones;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSetMetaData;
import com.mysql.jdbc.Statement;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.StringTokenizer;
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
public class parametrorecon extends javax.swing.JDialog {

    /** A return status code - returned if Cancel button has been pressed */
    public static final int RET_CANCEL = 0;
    /** A return status code - returned if OK button has been pressed */
    public static final int RET_OK = 1;
    Connection conex;
    String pres;
    int nume, numegrupo;
    private DefaultTableModel metabs;
    private String[] auxpart;
    private int partida;
    private int auxcont;
    private int contsel;
    private String[] partidas;
    private float[] cantidades;
    /** Creates new form parametrorecon */
    String nrocuadro=null;
    private boolean enc;
    String presrecon;
    public parametrorecon(Principal parent, boolean modal, Connection conex, String pres, String nrocuadro, String presrecon) {
        super(parent, modal);
        initComponents();
        this.conex = conex;
        this.pres = pres;
        this.presrecon = presrecon;
        this.nrocuadro = nrocuadro;
        cargapres();
        modelovalu();
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
        jTable1.setOpaque(true);
    jTable1.setShowHorizontalLines(true);
    jTable1.setShowVerticalLines(false);
    jTable1.getTableHeader().setSize(new Dimension(25,40));
    jTable1.getTableHeader().setPreferredSize(new Dimension(30,40));
    jTable1.setRowHeight(25);
    }
    public final void cargapres()
    {   try {
            Statement stmt = (Statement) conex.createStatement();
              String carga="SELECT codpro, codcon, porpre, poruti, porgam, porcfi, porimp, poripa FROM mpres WHERE id='"+pres+"'";
               ResultSet rs = stmt.executeQuery(carga);
              while(rs.next()){
                  
                  jTextField5.setText(rs.getObject(3).toString());
                  jTextField7.setText(rs.getObject(4).toString());
                  jTextField6.setText(rs.getObject(5).toString());
                  jTextField8.setText(rs.getObject(6).toString());
                  jTextField10.setText(rs.getObject(7).toString());
                  jTextField9.setText(rs.getObject(8).toString());
                  
                  
              }
        } catch (SQLException ex) {
            Logger.getLogger(parametrorecon.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public final void modelovalu(){
        String selectvalu="SELECT id FROM mvalus WHERE mpre_id='"+pres+"' AND id NOT IN "
                + "(SELECT valu FROM mpres WHERE mpres_id='"+pres+"' AND valu IS NOT NULL)";
        System.out.println("selectvalu "+selectvalu);
        int cuenta=0;
        Statement st;
        try {
            st = (Statement) conex.createStatement();
            ResultSet rst = st.executeQuery(selectvalu);
            while(rst.next()){
                jComboBox1.addItem(rst.getString("id"));
                cuenta++;
            }
        } catch (SQLException ex) {
            Logger.getLogger(parametrorecon.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(cuenta==0){
            JOptionPane.showMessageDialog(null, "No hay más valuaciones para reconsiderar");
            
            okButton.setEnabled(false);
            setVisible(false);
            this.dispose();
        }
                
        
    }
    /** @return the return status of this dialog - one of RET_OK or RET_CANCEL */
    public int getReturnStatus()
    {
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

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jTextField9 = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jTextField10 = new javax.swing.JTextField();
        jCheckBox1 = new javax.swing.JCheckBox();
        jPanel6 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setTitle("Parametros Reconsideración de Precios");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        okButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/guardar.png"))); // NOI18N
        okButton.setToolTipText("Guardar Reconsideración");
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
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(52, Short.MAX_VALUE)
                .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getRootPane().setDefaultButton(okButton);

        jPanel4.setBackground(new java.awt.Color(100, 100, 100));

        jLabel1.setBackground(new java.awt.Color(91, 91, 95));
        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Valuaciones del Presupuesto");
        jLabel1.setOpaque(true);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 510, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Parametros de la Reconsideración"));

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 10));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("% Prest. Soc.:");

        jTextField5.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField5KeyTyped(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 10));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("% Admón.:");

        jTextField6.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField6.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField6KeyTyped(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 10));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("% Utilidad.:");

        jTextField7.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField7.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField7KeyTyped(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 10));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("% Financ.:");

        jTextField8.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField8.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField8KeyTyped(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 10));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText("% Imp. Part.:");

        jTextField9.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField9.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField9KeyTyped(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 10));
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel14.setText("%Imp. Gen.:");

        jTextField10.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField10.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField10KeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField7, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE))
                            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField5, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                            .addComponent(jTextField6, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE))))
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField10, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, 128, Short.MAX_VALUE)
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField8, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE)
                            .addComponent(jTextField9, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13)
                    .addComponent(jLabel10)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14)
                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jCheckBox1.setFont(new java.awt.Font("Tahoma", 0, 10));
        jCheckBox1.setText("Asignar Valores a todas las Partidas");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(293, Short.MAX_VALUE)
                .addComponent(jCheckBox1)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jCheckBox1))
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Valuación a reconsiderar"));

        jLabel2.setText("Seleccione Valuación a Reconsiderar:");

        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/selecc1.fw.png"))); // NOI18N
        jButton1.setToolTipText("Seleccionar Todas las Partidas");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/quita1.fw.png"))); // NOI18N
        jButton2.setToolTipText("Deseleccionar todas");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(139, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)))
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
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 460, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 216, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(352, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
   
    
    public void cuenta(){
        try {
            String cuenta = "SELECT numero FROM mppres WHERE mpre_id='"+pres+"'"
                    + " OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"') "
                    + "ORDER BY numero DESC LIMIT 1";

             Statement st = (Statement) conex.createStatement();
                 ResultSet rst = st.executeQuery(cuenta);
                 while(rst.next()){
                     nume = Integer.parseInt(rst.getObject("numero").toString());
                    nume++;
                 }
               
           String cuentanumegrup = "SELECT numegrup FROM mppres WHERE mpre_id='"+pres+"'"
                    + " OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"') "
                    + "ORDER BY numegrup DESC LIMIT 1";
           
              Statement st1 = (Statement) conex.createStatement();
                 ResultSet rst1 = st.executeQuery(cuentanumegrup);
                 while(rst1.next()){
                     numegrupo = Integer.parseInt(rst1.getObject("numegrup").toString());
                    numegrupo++;
                 }
               
        } catch (SQLException ex) {
            Logger.getLogger(reconsideraciones.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     public float cambiapreciopartida(String numeropartida){
        //AL CAMBIAR UN VALOR DE LOS PARAMETROS DEL PRESUPUESTO COMO LOS PORCENTAJES DE PRESTACIONES SOCIALES
        // SE RECALCULA EL COSTO DE TODAS LAS PARTIDAS
        float presta = Float.valueOf(jTextField5.getText())/100;
        float admin = Float.valueOf(jTextField6.getText())/100;
        float util = Float.valueOf(jTextField7.getText())/100;
        float finan = Float.valueOf(jTextField8.getText())/100;
        float impart = Float.valueOf(jTextField9.getText())/100;
        float impgen = Float.valueOf(jTextField10.getText())/100;
        float precio=0, totalmat=0, totaleq=0, totalmano=0, totalcantidad=0;
        float bono=0, subsid=0;
        String codpres = pres+" VP-"+nrocuadro;
        float rendimi=0;
        String mvalu=jComboBox1.getSelectedItem().toString();
        if(!jCheckBox1.isSelected()){
            String select="SELECT porcgad, porcpre, porcutil FROM mppres WHERE numero="+numeropartida+" "
                    + "AND (mpre_id='"+pres+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"'))";
            String porcgad=null, porcpre=null, porcutil=null;
            try {
                Statement str = (Statement) conex.createStatement();
                ResultSet rstr = str.executeQuery(select);
                while(rstr.next()){
                    porcgad = rstr.getString(1);
                    porcpre = rstr.getString(2);
                    porcutil=rstr.getString(3);
                }
                if(porcgad!=null){
                    admin=Float.valueOf(porcgad)/100;
                }
                if(porcpre!=null){
                    presta=Float.valueOf(porcpre)/100;
                }
                if(porcutil!=null){
                    util=Float.valueOf(porcutil)/100;
                }
            } catch (SQLException ex) {
                System.out.println("No se pudo consultar el porcentaje de las partidas en los parametros para la reconsideración");
                Logger.getLogger(parametrorecon.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //CONSULTA RENDIMIENTO DE LA PARTIDA
        String selecrendimi = "SELECT rendimi FROM mppres WHERE numero="+numeropartida+" AND (mpre_id='"+pres+"' OR "
                + "mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"'))";
        try {
            Statement sts = (Statement) conex.createStatement();
            ResultSet rsts = sts.executeQuery(selecrendimi);
            while(rsts.next()){
                rendimi=rsts.getFloat(1);
            }
        } catch (SQLException ex) {
            System.out.println("Error al consultar el rendimiento de la partida, cuando se va a insertar en una nueva reconsideración");
            Logger.getLogger(parametrorecon.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // SUMANDO TOTAL DE MATERIAL
        String totalmaterial="SELECT SUM((mm.precio+(mm.precio*(mm.desperdi/100)))*dm.cantidad) as total "
                + "FROM dmpres as dm, mmpres as mm WHERE dm.numepart="+numeropartida+" AND dm.mmpre_id=mm.id "
                + "AND (dm.mpre_id='"+pres+"' OR dm.mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"'))"
                + " AND dm.mpre_id = mm.mpre_id";
        try {
            Statement st = (Statement) conex.createStatement();
            ResultSet rst = st.executeQuery(totalmaterial);
            while(rst.next()){
              totalmat = rst.getFloat("total");
            }
        } catch (SQLException ex) {
            System.out.println("Error al Sumar materiales de la partida de la valuación para agregarla en reconsideración");
            Logger.getLogger(parametrorecon.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        String totalequipo = "SELECT SUM(IF(me.deprecia=0, (de.cantidad*me.precio),(de.cantidad*me.deprecia*me.precio))) as total "
                + "FROM mepres as me, deppres as de WHERE de.mepre_id=me.id AND de.numero="+numeropartida+" AND "
                + "(de.mpre_id='"+pres+"' OR de.mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) "
                + "AND de.mpre_id=me.mpre_id";
        try {
            Statement st= (Statement) conex.createStatement();
            ResultSet rst = st.executeQuery(totalequipo);
            while(rst.next()){
                totaleq=rst.getFloat(1);
            }
        } catch (SQLException ex) {
            System.out.println("Error al consultar el total en equipos para la inserción de partidas en la reconsideración");
            Logger.getLogger(parametrorecon.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        String consultamano = "SELECT SUM(dm.cantidad) as cantidad, mm.bono, mm.subsid, "
                    + "SUM(mm.salario*dm.cantidad) as total FROM mmopres as mm, dmoppres as dm "
                + "WHERE dm.numero ="+numeropartida+" AND (dm.mpre_id='"+pres+"' OR dm.mpre_id IN "
                + "(SELECT id FROM mpres WHERE mpres_id='"+pres+"')) AND dm.mmopre_id=mm.id AND dm.mpre_id = mm.mpre_id";
        try {
            Statement st = (Statement) conex.createStatement();
            ResultSet rst = st.executeQuery(consultamano);
            while(rst.next()){
                totalmano = rst.getFloat("total");
                totalcantidad = rst.getFloat("cantidad");
                bono = rst.getFloat("mm.bono");
                subsid = rst.getFloat("mm.subsid");
            }
        } catch (SQLException ex) {
            System.out.println("Error al sumar la cantidad de mano de obra en la inserción de la reconsideración");
            Logger.getLogger(parametrorecon.class.getName()).log(Level.SEVERE, null, ex);
        }
        bono = totalcantidad*bono;
        subsid = totalcantidad*subsid;
        presta = totalmano*presta;
        float auxcontmano = totalmano+bono+subsid+presta;
        if(rendimi==0)
            rendimi=1;
        totalmano=auxcontmano/rendimi;
        totaleq=totaleq/rendimi;
        float total = totalmat+totaleq+totalmano;
        float auxtotal=total;
        admin = total*admin;
        util=(auxtotal+admin)*util;
        auxtotal=total+admin+util;
        finan=auxtotal*finan;
        impart=auxtotal*impart;
        total=auxtotal+finan+impart;
        return total;
        
    }
    public void inserta(){
        String  idband = null, porcgad=null, porcpre=jTextField5.getText(), porcutil=null, precasu=null;
        String precunit = null, rendimi = null, unidad = null, redondeo = null, status = null;
        String mtabus_id="", cantidad="";
        float porfin = Float.valueOf(jTextField8.getText()), admin=Float.valueOf(jTextField6.getText());
        float porutil = Float.valueOf(jTextField7.getText()), impart = Float.valueOf(jTextField9.getText());
        float impgen = Float.valueOf(jTextField10.getText());
        int valu = Integer.parseInt(jComboBox1.getSelectedItem().toString());
        verificarcheck();
        for(int i=0; i<contsel;i++){
           
            String numero = partidas[i];
            float cantvalu = cantidades[i];
            
            // CONTAR NUMERO DE REGISTROS DE MPRES DONDE ESTA REGISTRADO EL CODIGO 
            // DE PRESUPUESTO DE MPRES VP-NROCUADRO
            String codpres = pres+" VP-"+nrocuadro;
            int cuenta=0;
            try {
                Statement stpres = (Statement) conex.createStatement();
                ResultSet rstpres = stpres.executeQuery("SELECT count(*) FROM mpres"
                        + " WHERE id='"+codpres+"'");
                while(rstpres.next()){
                    cuenta=rstpres.getInt(1);
                }
                
                if(cuenta==0){
                    Statement insert = (Statement) conex.createStatement();
                    insert.execute("INSERT INTO mpres (id, nomabr,nombre,ubicac,fecini,fecfin,feccon,fecimp,"
                        + "porgam,porcfi, porimp,"
                        + "poripa, porpre, poruti, codpro, codcon, parpre, nrocon, nroctr, fecapr, nrolic, status, "
                        + "mpres_id, memo, "
                        + "timemo, fecmemo, seleccionado, partidapres, fecharegistro,valu) "
                        + "SELECT '"+codpres+"', nomabr, nombre, ubicac, fecini,"
                    + "fecfin, feccon, fecimp,"+admin+", "+porfin+", "+impgen+", "+impart+", "
                    + ""+porcpre+", "+porutil+", codpro, codcon, parpre, nrocon, nroctr, fecapr,"
                    + "nrolic, 1, '"+pres+"',memo,timemo, fecmemo, 0,partidapres,NOW(), "+valu+" FROM mpres WHERE id='"+pres+"'");
                }
                cuenta();  
                float precio=cambiapreciopartida(numero);
                String insertpart=null;
                if(jCheckBox1.isSelected()){
                    String gad = jTextField6.getText();
                    String pre = jTextField5.getText();
                    String uti = jTextField7.getText();
                    insertpart = "INSERT INTO mppres (mpre_id, id, numero, numegrup, descri, "
                    + " idband, porcgad, porcpre, porcutil, precasu, precunit, rendimi, "
                    + "unidad, redondeo, status,"
                    + "cantidad, tipo, nrocuadro, tiporec,mppre_id) "
                        + "SELECT '"+codpres+"',id,"+nume+","+numegrupo+",descri,idband,"
                        + ""+gad+", "+pre+", "+uti+", "+precio+", "+precio+", rendimi, unidad, "
                        + "redondeo, 1, "+cantvalu+", 'VP', "+nrocuadro+",'VP-"+numero+"',"+numero+" "
                        + "FROM mppres WHERE numero="+numero+" AND (mpre_id='"+pres+"' OR "
                        + "mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"'))";
                }else{
                insertpart = "INSERT INTO mppres (mpre_id, id, numero, numegrup, descri, "
                    + " idband, porcgad, porcpre, porcutil, precasu, precunit, rendimi, "
                    + "unidad, redondeo, status,"
                    + "cantidad, tipo, nrocuadro, tiporec,mppre_id) "
                        + "SELECT '"+codpres+"',id,"+nume+","+numegrupo+",descri,idband,"
                        + "porcgad, porcpre, porcutil, "+precio+", "+precio+", rendimi, unidad, "
                        + "redondeo, 1, "+cantvalu+", 'VP', "+nrocuadro+",'VP-"+numero+"',"+numero+" "
                        + "FROM mppres WHERE numero="+numero+" AND (mpre_id='"+pres+"' OR "
                        + "mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"'))";
                }
                Statement insertapart = (Statement) conex.createStatement();
                insertapart.execute(insertpart);
                 insertamateriales(codpres,numero);
                insertaequipos(codpres, numero);
                insertamano(codpres, numero);
                
            } catch (SQLException ex) {
                Logger.getLogger(parametrorecon.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        contsel=0;
    }
    public void insertamateriales(String codpres, String numero){
        try {
            String selecmat = "INSERT INTO dmpres "
                        + "SELECT '"+codpres+"', mppre_id, mmpre_id, "+nume+", cantidad, precio, status "
                        + "FROM dmpres WHERE numepart="+numero+" AND (mpre_id='"+pres+"' OR mpre_id IN "
                        + "(SELECT id FROM mpres WHERE mpres_id='"+pres+"')) AND numepart NOT IN "
                    + "(SELECT numepart FROM dmpres WHERE mpre_id='"+codpres+"')";
                Statement detmat = (Statement) conex.createStatement();
                detmat.execute(selecmat);
                String insertemat = "INSERT INTO mmpres "
                            + "SELECT '"+codpres+"', id, descri,desperdi, precio, unidad, status "
                            + "FROM mmpres WHERE (id IN (SELECT mmpre_id FROM "
                        + "dmpres WHERE mpre_id='"+codpres+"')"
                        + ") AND mpre_id='"+pres+"' AND id NOT IN (SELECT id FROM mmpres WHERE mpre_id='"+codpres+"') LIMIT 1";
                    Statement insertar = (Statement) conex.createStatement();
                    insertar.execute(insertemat);
        } catch (SQLException ex) {
            Logger.getLogger(parametrorecon.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    public void insertaequipos(String codpres, String numero)
    {
         try {
            String seleceq = "INSERT INTO deppres "
                        + "SELECT '"+codpres+"', mppre_id, mepre_id, "+nume+", cantidad, precio, status "
                        + "FROM deppres WHERE numero="+numero+" AND (mpre_id='"+pres+"' OR mpre_id IN "
                        + "(SELECT id FROM mpres WHERE mpres_id='"+pres+"')) AND numero NOT IN "
                    + "(SELECT numero FROM deppres WHERE mpre_id='"+codpres+"')";
                Statement deteq = (Statement) conex.createStatement();
                deteq.execute(seleceq);
                String inserteeq = "INSERT INTO mepres "
                            + "SELECT '"+codpres+"', id, descri,deprecia, precio, status "
                            + "FROM mepres WHERE (id IN (SELECT mepre_id FROM "
                        + "deppres WHERE mpre_id='"+codpres+"')"
                        + ") AND mpre_id='"+pres+"' AND id NOT IN (SELECT id FROM mepres WHERE mpre_id='"+codpres+"') LIMIT 1";
                    Statement insertar = (Statement) conex.createStatement();
                    insertar.execute(inserteeq);
        } catch (SQLException ex) {
            Logger.getLogger(parametrorecon.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void insertamano(String codpres, String numero)
    {
         try {
            String selecmano = "INSERT INTO dmoppres "
                        + "SELECT '"+codpres+"',mmopre_id , mppre_id, "+nume+", cantidad, bono, salario, subsidi, status "
                        + "FROM dmoppres WHERE numero="+numero+" AND (mpre_id='"+pres+"' OR mpre_id IN "
                        + "(SELECT id FROM mpres WHERE mpres_id='"+pres+"')) AND numero NOT IN "
                    + "(SELECT numero FROM dmoppres WHERE mpre_id='"+codpres+"')";
                Statement detmano = (Statement) conex.createStatement();
                detmano.execute(selecmano);
                String insertemano = "INSERT INTO mmopres "
                            + "SELECT '"+codpres+"', id, descri,bono, salario, subsid, status "
                            + "FROM mmopres WHERE (id IN (SELECT mmopre_id FROM "
                        + "dmoppres WHERE mpre_id='"+codpres+"')"
                        + ") AND mpre_id='"+pres+"' AND id NOT IN (SELECT id FROM mmopres WHERE mpre_id='"+codpres+"') LIMIT 1";
                    Statement insertar = (Statement) conex.createStatement();
                    insertar.execute(insertemano);
        } catch (SQLException ex) {
            Logger.getLogger(parametrorecon.class.getName()).log(Level.SEVERE, null, ex);
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

    private void jTextField5KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField5KeyTyped

        char car = evt.getKeyChar();         int repite = new StringTokenizer(jTextField5.getText().toString(), ".").countTokens() - 1;         if ((car < '0' || car > '9') && car != '.') {             evt.consume();         }         if (car == '.' && repite == 1) {             evt.consume();         }       }//GEN-LAST:event_jTextField5KeyTyped

    private void jTextField6KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField6KeyTyped

        char car = evt.getKeyChar();         int repite = new StringTokenizer(jTextField6.getText().toString(), ".").countTokens() - 1;         if ((car < '0' || car > '9') && car != '.') {             evt.consume();         }         if (car == '.' && repite == 1) {             evt.consume();         }     }//GEN-LAST:event_jTextField6KeyTyped

    private void jTextField7KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField7KeyTyped

        char car = evt.getKeyChar();         int repite = new StringTokenizer(jTextField7.getText().toString(), ".").countTokens() - 1;         if ((car < '0' || car > '9') && car != '.') {             evt.consume();         }         if (car == '.' && repite == 1) {             evt.consume();         }     }//GEN-LAST:event_jTextField7KeyTyped

    private void jTextField8KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField8KeyTyped

        char car = evt.getKeyChar();         int repite = new StringTokenizer(jTextField8.getText().toString(), ".").countTokens() - 1;         if ((car < '0' || car > '9') && car != '.') {             evt.consume();         }         if (car == '.' && repite == 1) {             evt.consume();         }     }//GEN-LAST:event_jTextField8KeyTyped

    private void jTextField9KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField9KeyTyped

        char car = evt.getKeyChar();         int repite = new StringTokenizer(jTextField9.getText().toString(), ".").countTokens() - 1;         if ((car < '0' || car > '9') && car != '.') {             evt.consume();         }         if (car == '.' && repite == 1) {             evt.consume();         }     }//GEN-LAST:event_jTextField9KeyTyped

    private void jTextField10KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField10KeyTyped

        char car = evt.getKeyChar();         int repite = new StringTokenizer(jTextField10.getText().toString(), ".").countTokens() - 1;         if ((car < '0' || car > '9') && car != '.') {             evt.consume();         }         if (car == '.' && repite == 1) {             evt.consume();         }     }//GEN-LAST:event_jTextField10KeyTyped

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        auxcont=0;
        buscapartida();
        
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
            partida = jTable1.rowAtPoint(evt.getPoint());
    String part =jTable1.getValueAt(partida, 2).toString();
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
                int totalfilas = jTable1.getRowCount();
      // System.out.println("totalfilas: "+totalfilas);
        for(int i=0; i<totalfilas; i++){
            auxpart[i] =String.valueOf( jTable1.getValueAt(i, 1));
        }
        auxcont = totalfilas;
        buscapartida();
        
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed
public void verificarcheck() {

        int registros = jTable1.getRowCount();
        int columnas = jTable1.getColumnCount();
       
        Object obj;
        partidas = new String [registros];
        cantidades = new float [registros];
        Boolean bol;
        String strNombre;
        StringBuilder builder = null;
        int i, j;
        for (i = 0; i < registros; i++) {
            if (i == 0) {
                builder = new StringBuilder();
                builder.append("Partidas Seleccionadas:").append("\n");
            }
            for (j = 0; j < columnas; j++) {

                obj = jTable1.getValueAt(i, j);
                if (obj instanceof Boolean) {
                    bol = (Boolean) obj;
                    if (bol.booleanValue()) {
                        //Falta buscar numero real en mptabs
                        String numegrup=jTable1.getValueAt(i, 1).toString();
                        cantidades[contsel]= Float.valueOf(jTable1.getValueAt(i, 5).toString());
                        String select= "SELECT numero FROM mppres WHERE (mpre_id='"+pres+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"'))"
                                + " AND numegrup="+numegrup+"";
                        try {
                            Statement st = (Statement) conex.createStatement();
                            ResultSet rst = st.executeQuery(select);
                            while(rst.next()){
                                partidas[contsel] = rst.getString(1);
                            }
                        } catch (SQLException ex) {
                            Logger.getLogger(parametrorecon.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                        
                        strNombre = jTable1.getValueAt(i, 3).toString();
                        builder.append("Nombre  :").append(strNombre).append("\n");
                        contsel++;
                    }
                }

            }

        }
        
    }
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        int totalfilas = jTable1.getRowCount();
     //  System.out.println("totalfilas: "+totalfilas);
        for(int i=0; i<totalfilas; i++){
            auxpart[i] ="";
        }
        auxcont = 0;
        buscapartida();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void okButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_okButtonMouseClicked
        inserta();
        JOptionPane.showMessageDialog(null, "Se han insertado las partidas");
        
         doClose(RET_OK);
        // TODO add your handling code here:
    }//GEN-LAST:event_okButtonMouseClicked
    public void buscapartida(){
         String codpres = pres+" VP-"+nrocuadro;
        String mvalu=jComboBox1.getSelectedItem().toString();
        String partvalu = "SELECT mp.numegrup, mp.id, mp.descri, mp.precunit, "
                + "dv.cantidad FROM mppres as mp, "
                + "dvalus as dv WHERE dv.mvalu_id="+mvalu+" AND dv.numepart=mp.numero "
                + " AND (mp.mpre_id ='"+pres+"' "
                + "OR mp.mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) And dv.mpre_id='"+pres+"' "
                + "AND mp.numero NOT IN"
                + " (SELECT mppre_id FROM mppres WHERE mpre_id='"+codpres+"')";
        try {
            Statement st = (Statement) conex.createStatement();
            ResultSet rst = st.executeQuery(partvalu);
             ResultSetMetaData rsMd = (ResultSetMetaData) rst.getMetaData();
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
               
                 metabs.addColumn(rsMd.getColumnLabel(i-1));
            }
              while (rst.next()) {
                 
                 Object[] filas = new Object[cantidadColumnas];
                 enc=false;
                    for(int j=0;j<auxcont;j++){
                         if(rst.getObject(1).toString().equals(auxpart[j])){
                                  enc=true;
                         }
                    }
                  Object obj = Boolean.valueOf(enc);
                  filas[0]= obj;
                for (int i = 1; i < cantidadColumnas; i++) {
                  
                       filas[i] = rst.getObject(i);
                }
                metabs.addRow(filas);
              
                
            }
             
        } catch (SQLException ex) {
            Logger.getLogger(parametrorecon.class.getName()).log(Level.SEVERE, null, ex);
        }
          int fila = jTable1.getRowCount();
             auxpart = new String[fila];
        cambiarcabecera();
                
    }
    private void doClose(int retStatus) {
        returnStatus = retStatus;
        setVisible(false);
        dispose();
    }
 public void cambiarcabecera(){
        JTableHeader th = jTable1.getTableHeader();
        TableColumnModel tcm = th.getColumnModel();
        TableColumn tc = tcm.getColumn(0); 
    tc.setHeaderValue("");
       tc.setPreferredWidth(10);
   tc = tcm.getColumn(1); 
       tc.setHeaderValue("Número");
       tc.setPreferredWidth(10);
       tc = tcm.getColumn(2); 
       tc.setHeaderValue("Código");
       tc.setPreferredWidth(10);
       tc = tcm.getColumn(3); 
       tc.setHeaderValue("Descripción");
       tc.setPreferredWidth(150);
       tc = tcm.getColumn(4); 
       tc.setHeaderValue("Precio Unitario");
       tc.setPreferredWidth(20);
       tc = tcm.getColumn(5); 
       tc.setHeaderValue("Cant. Valuada");
       tc.setPreferredWidth(30);
       
     
       th.repaint(); 
    }
    /**
     * @param args the command line arguments
     */
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    private javax.swing.JButton okButton;
    // End of variables declaration//GEN-END:variables
    private int returnStatus = RET_CANCEL;
}
