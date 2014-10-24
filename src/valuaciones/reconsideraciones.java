/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
import java.text.NumberFormat;
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
import presupuesto.materiales.matrizmaterialespres;
import presupuestos.Partida;
import presupuestos.Presupuesto;
import presupuestos.equipo.matrizequipospres;
import presupuestos.manoobra.matrizmanopres;
import winspapus.Principal;


public class reconsideraciones extends javax.swing.JDialog {
    
    
    int nrocuadro=0;
   String mpres, numero, numegrup, codicove;
    public static final int RET_CANCEL = 0;
    
    public static final int RET_OK = 1;
    private Connection conex;
    int contar=0;
    private Presupuesto pres;
   int filapart=0;
   private Principal prin;
   float cantidadrecon=0;
    String codnuevopres ;
   float nuevopreciasum=0;
   double totalrecons=0;
   String nuevopres=null;
   String valu=null;
    public reconsideraciones(java.awt.Frame parent, boolean modal, String mpres, Connection conex, Presupuesto pres, Principal prin) {
        super(parent,false);
        initComponents();
        this.conex = conex;
        this.prin = prin;
        this.mpres = mpres;
        this.pres = pres;
        buscapres();
       buscacuadro(); 
       
       jTable1.setOpaque(true);
    jTable1.setShowHorizontalLines(true);
    jTable1.setShowVerticalLines(false);
    jTable1.getTableHeader().setSize(new Dimension(25,40));
    jTable1.getTableHeader().setPreferredSize(new Dimension(30,40));
    jTable1.setRowHeight(25);
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
    public final void buscapres(){
        String numcuadro = jSpinner1.getValue().toString();
      codnuevopres= mpres+" VP-"+numcuadro;
      //PARAMETROS DE LA RECONSIDERACIÓN
        String select="SELECT mp.mpre_id, p.valu,"
                + "p.porpre, p.porgam, p.poruti, p.porcfi, p.poripa, p.porimp"                
                + " FROM mppres as mp, mpres as p "
                + "WHERE mp.nrocuadro="+numcuadro+" AND mp.mpre_id = p.id AND mp.tipo='VP'"
                + " AND p.mpres_id='"+mpres+"' LIMIT 1";
      
        try {
            Statement st = (Statement) conex.createStatement();
            ResultSet rst = st.executeQuery(select);
            while(rst.next()){
                nuevopres = rst.getString(1);
                valu = rst.getString(2);
                jTextField7.setText(rst.getString("p.porpre"));
                jTextField9.setText(rst.getString("p.porgam"));
                jTextField10.setText(rst.getString("p.poruti"));
                jTextField11.setText(rst.getString("p.porcfi"));
                jTextField7.setText(rst.getString("p.porpre"));
                jTextField12.setText(rst.getString("p.poripa"));
                jTextField13.setText(rst.getString("p.porimp"));
            }
            
            //PARAMETROS DEL PRESUPUESTO ORIGINAL PARA LA VALUACION
            
            
            
            //-----------------------------------------------------
            float totalvalu=0, totalrecon=0;
            jTextField8.setText(nuevopres);
            jTextField1.setText(valu);
            //TOTAL DE LA VALUACION
              String preciovalu = "SELECT SUM(cantidad*precio) FROM dvalus WHERE mvalu_id="+valu+" AND "
                      + "(mpre_id='"+mpres+"' or mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+mpres+"'))";
              Statement stvalu = (Statement) conex.createStatement();
              ResultSet rstvalu = stvalu.executeQuery(preciovalu);
              while(rstvalu.next()){
                  totalvalu=rstvalu.getFloat(1);
              }
              jTextField15.setText(String.valueOf(totalvalu));
              //TOTAL DE LA RECONSIDERACION
              String cargasinredondeo = "SELECT SUM(ROUND(IF(mppres.`precasu`=0,mppres.`precunit`,precasu)*cantidad,2)) "
                + "FROM `winspapu`.`mppres` WHERE "
                      + "mpre_id='"+codnuevopres+"' AND nrocuadro="+numcuadro+"";
        System.out.println("totales cargartotal: "+cargasinredondeo);
        double subtotal=0;
            Statement st1 = (Statement) conex.createStatement();
          
            ResultSet rst1 = st1.executeQuery(cargasinredondeo);
            
            while (rst1.next()){
                if(rst1.getObject(1)!=null) {
                    subtotal = rst1.getDouble(1);
                     System.out.println("subtotal bd: "+rst1.getDouble(1));
                }
            }
              NumberFormat formatoNumero = NumberFormat.getNumberInstance();
            formatoNumero.setMaximumFractionDigits(2);
            formatoNumero.setMinimumFractionDigits(2);
            jTextField14.setText(String.valueOf(formatoNumero.format(subtotal)));
            jTextField15.setText(String.valueOf(formatoNumero.format(totalvalu)));
              
          
        } catch (SQLException ex) {
            Logger.getLogger(reconsideraciones.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
    public int getReturnStatus() {
        return returnStatus;
    }

   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        WinspapusPUEntityManager = java.beans.Beans.isDesignTime() ? null : javax.persistence.Persistence.createEntityManagerFactory("WinspapusPU").createEntityManager();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        cancelButton = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jTextField3 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel6 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jSpinner1 = new javax.swing.JSpinner();
        jPanel5 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jButton9 = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jTextField9 = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jTextField10 = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jTextField11 = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jTextField12 = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jTextField13 = new javax.swing.JTextField();
        jButton7 = new javax.swing.JButton();
        jCheckBox1 = new javax.swing.JCheckBox();
        jLabel8 = new javax.swing.JLabel();
        jTextField14 = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jTextField15 = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

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
        jLabel1.setText("Reconsideración de Precios");
        jLabel1.setOpaque(true);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 757, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        cancelButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/eliminar.png"))); // NOI18N
        cancelButton.setToolTipText("Cancelar");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/agregapartidas.fw.png"))); // NOI18N
        jButton1.setToolTipText("Agregar Partidas de la Valuación a esta Reconsideración");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/borra.png"))); // NOI18N
        jButton5.setToolTipText("Borrar Partida");
        jButton5.setEnabled(false);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/listar1.png"))); // NOI18N
        jButton3.setToolTipText("Variar Reconsideracion por tabulador");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/apu.png"))); // NOI18N
        jButton4.setToolTipText("Ver Apu de la Partida");
        jButton4.setEnabled(false);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/reportes1.fw.png"))); // NOI18N
        jButton8.setToolTipText("Reporte de Reconsideración de Precios");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/anade.fw.png"))); // NOI18N
        jButton6.setToolTipText("Nueva Reconsideración");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/borrar1.fw.png"))); // NOI18N
        jButton10.setToolTipText("Borrar Reconsideración");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jButton11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/materialbarra.png"))); // NOI18N
        jButton11.setToolTipText("Materiales");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jButton12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/camion.fw.png"))); // NOI18N
        jButton12.setToolTipText("Equipos Reconsideración");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jButton13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/barracasco.png"))); // NOI18N
        jButton13.setToolTipText("Mano de Obra Reconsideración");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(190, Short.MAX_VALUE)
                .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.darkGray, java.awt.Color.gray, java.awt.Color.darkGray, java.awt.Color.gray));

        jTextField3.setEditable(false);
        jTextField3.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });

        jLabel3.setText("Número Partida:");

        jLabel5.setText("Cantidad:");

        jTextField5.setEditable(false);
        jTextField5.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel2.setText("No. de Cuadro:");

        jLabel7.setText("Precio Unitario:");

        jTextField2.setEditable(false);
        jTextField2.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField2.setNextFocusableComponent(jTextField7);
        jTextField2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField2MouseClicked(evt);
            }
        });
        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });
        jTextField2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField2FocusLost(evt);
            }
        });
        jTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField2KeyTyped(evt);
            }
        });

        jTextArea1.setColumns(20);
        jTextArea1.setEditable(false);
        jTextArea1.setFont(new java.awt.Font("Monospaced", 0, 11));
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(3);
        jTextArea1.setWrapStyleWord(true);
        jTextArea1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextArea1KeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(jTextArea1);

        jLabel6.setText("Precio Asumido:");

        jTextField6.setEditable(false);
        jTextField6.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel4.setText("Partida:");

        jTextField4.setEditable(false);
        jTextField4.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jSpinner1.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(1)));
        jSpinner1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner1StateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(54, 54, 54)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 98, Short.MAX_VALUE)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 733, Short.MAX_VALUE)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(84, 84, 84)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 129, Short.MAX_VALUE)
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel7)
                    .addComponent(jLabel6))
                .addGap(11, 11, 11))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.gray, java.awt.Color.darkGray, java.awt.Color.gray, java.awt.Color.darkGray));

        jTextField1.setEditable(false);

        jLabel12.setText("Valuación:");

        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/edita1.fw.png"))); // NOI18N
        jButton9.setToolTipText("Cambiar Valuación (Borra Partidas de Esta valuación y agrega las de la nueva)");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jLabel9.setText("Cod. Pres:");

        jTextField8.setEditable(false);
        jTextField8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField8ActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 10));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("% Prest. Soc.:");

        jTextField7.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField7.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField7KeyTyped(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 10));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("% Admón.:");

        jTextField9.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField9.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField9KeyTyped(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 10));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText("% Utilidad.:");

        jTextField10.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField10.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField10KeyTyped(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 10));
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel14.setText("% Financ.:");

        jTextField11.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField11.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField11KeyTyped(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Tahoma", 0, 10));
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel15.setText("% Imp. Part.:");

        jTextField12.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField12.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField12KeyTyped(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Tahoma", 0, 10));
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel16.setText("%Imp. Gen.:");

        jTextField13.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField13.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField13KeyTyped(evt);
            }
        });

        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/guardar1.fw.png"))); // NOI18N
        jButton7.setToolTipText("Guardar Parametros de la Reconsideración");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jCheckBox1.setText("Asignar Parametros a todas las Partidas");

        jLabel8.setText("Total Recon:");

        jTextField14.setEditable(false);

        jLabel17.setText("Total Valu.:");

        jTextField15.setEditable(false);

        jLabel18.setForeground(new java.awt.Color(102, 102, 102));
        jLabel18.setText("(Montos Totales Sin Impuestos)");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel11)
                            .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, 99, Short.MAX_VALUE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jTextField7, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE)
                            .addComponent(jTextField9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE)
                            .addComponent(jTextField10, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE)
                            .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField13, javax.swing.GroupLayout.DEFAULT_SIZE, 86, Short.MAX_VALUE)
                            .addComponent(jTextField12, javax.swing.GroupLayout.DEFAULT_SIZE, 86, Short.MAX_VALUE)
                            .addComponent(jTextField11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 86, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(jCheckBox1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(70, 70, 70)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel17)
                    .addComponent(jLabel12)
                    .addComponent(jLabel9)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel18)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)
                            .addComponent(jTextField15, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)
                            .addComponent(jTextField8, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)
                            .addComponent(jTextField14, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14)
                            .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10))
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabel11))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel15)
                                    .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                        .addComponent(jLabel12)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel13)
                        .addComponent(jLabel16))
                    .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jCheckBox1)
                            .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel17))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel18)))
                .addContainerGap(6, Short.MAX_VALUE))
        );

        jTable1.setFont(new java.awt.Font("Tahoma", 0, 10));
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
        jScrollPane2.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 757, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 193, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
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
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        doClose(RET_CANCEL);
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        doClose(RET_CANCEL);
    }//GEN-LAST:event_closeDialog

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
       
        
    }//GEN-LAST:event_jTextField3ActionPerformed
   public void setnrocuadro(int numero){
       jSpinner1.setValue(numero);
   }
    
    public final void buscacuadro(){
        
         if(!"".equals(jSpinner1.getValue())) 
         {
            nrocuadro = Integer.parseInt(jSpinner1.getValue().toString());
        }
        
         totalrecons=0;

       String deltas="IF((IF(m.precasu=0,m.precunit,m.precasu)-(SELECT mp.precunit FROM "
                + "mppres as mp WHERE (mp.mpre_id='"+mpres+"' OR mp.mpre_id IN "
                + " (SELECT id FROM mpres WHERE mpres_id='"+mpres+"')) AND mp.numero=m.mppre_id))<0,0,"
                + "IF(m.precasu=0,m.precunit,m.precasu)-(SELECT mp.precunit FROM "
                + "mppres as mp WHERE (mp.mpre_id='"+mpres+"' OR mp.mpre_id IN "
                + " (SELECT id FROM mpres WHERE mpres_id='"+mpres+"')) AND mp.numero=m.mppre_id))";
       
        String sql = "SELECT m.id, m.numegrup, m.cantidad, IF(m.precasu=0,m.precunit,m.precasu), (SELECT mp.precunit FROM "
                + "mppres as mp WHERE (mp.mpre_id='"+mpres+"' OR mp.mpre_id IN "
                + "(SELECT id FROM mpres WHERE mpres_id='"+mpres+"')) AND mp.numero=m.mppre_id) as precunitario,"
                + "IF((IF(m.precasu=0,m.precunit,m.precasu)-(SELECT mp.precunit FROM "
                + "mppres as mp WHERE (mp.mpre_id='"+mpres+"' OR mp.mpre_id IN "
                + " (SELECT id FROM mpres WHERE mpres_id='"+mpres+"')) AND mp.numero=m.mppre_id))<0,0,"
                + "IF(m.precasu=0,m.precunit,m.precasu)-(SELECT mp.precunit FROM "
                + "mppres as mp WHERE (mp.mpre_id='"+mpres+"' OR mp.mpre_id IN "
                + " (SELECT id FROM mpres WHERE mpres_id='"+mpres+"')) AND mp.numero=m.mppre_id)) as delta"
                + ", m.tiporec,"
                + ""+deltas+"*m.cantidad as total FROM "
                + "mppres as m WHERE "
                + "m.nrocuadro="+nrocuadro+" "
                + "AND m.nrocuadro IS NOT NULL AND (m.mpre_id='"+mpres+"' OR m.mpre_id IN "
                + "(SELECT id FROM mpres WHERE mpres_id='"+mpres+"')) ORDER BY m.numegrup";
        
        System.out.println(sql);
        
          try {
           
            Statement st = (Statement) conex.createStatement();
            ResultSet rs = st.executeQuery(sql);
            ResultSetMetaData rsMd = (ResultSetMetaData) rs.getMetaData();
          
           DefaultTableModel metabs = new DefaultTableModel(){
               @Override
           public boolean isCellEditable (int row, int column) {
              if(column==3|| column==4)
              {
                  return true;
              }
              else{
               return false;
              }
           }
               @Override
                public Class getColumnClass(int columna)
           {
                              if(columna==3 || columna==4 || columna==5 || columna==7){
                        return Double.class;
                    }
               
               return Object.class;
           }
           };
           
           jTable1.setModel(metabs);
               int cantidadColumnas = rsMd.getColumnCount();
                for (int i = 1; i <= cantidadColumnas; i++) {
                  
                    
                    
                    metabs.addColumn(rsMd.getColumnLabel(i));
               }
                
                while (rs.next()) {
                    
                    Object[] filas = new Object[cantidadColumnas];
                   for (int i = 0; i < cantidadColumnas; i++) {
                       if(i==7){
                           totalrecons+=rs.getDouble(i+1);
                       }
                       if(i==5){
                         double delta=rs.getDouble(i+1);
                         if(delta<0){
                             delta=delta*(-1);
                         }
                          filas[i]=delta;
                     }else{
                         filas[i]=rs.getObject(i+1);
                     }
                     
                   }
                   metabs.addRow(filas);
                   
               }
        } catch (SQLException ex) {
            Logger.getLogger(valuacion.class.getName()).log(Level.SEVERE, null, ex);
        }
        cambiarcabecera();
         NumberFormat formatoNumero = NumberFormat.getNumberInstance();
            formatoNumero.setMaximumFractionDigits(2);
            formatoNumero.setMinimumFractionDigits(2);
            jTextField14.setText(String.valueOf(formatoNumero.format(totalrecons)));
    }
    
    public void cambiarcabecera(){
        JTableHeader th = jTable1.getTableHeader();
        TableColumnModel tcm = th.getColumnModel();
        TableColumn tc = tcm.getColumn(0); 
       
       tc.setHeaderValue("Código");
       tc.setPreferredWidth(10);
       tc = tcm.getColumn(1); 
       tc.setHeaderValue("Nro.");
       tc.setPreferredWidth(10);
     
       tc = tcm.getColumn(2); 
       tc.setHeaderValue("Cantidad");
       tc.setPreferredWidth(20);
       tc = tcm.getColumn(3); 
       tc.setHeaderValue("Prec. Recon.");
       tc.setPreferredWidth(30);
       tc = tcm.getColumn(4); 
       tc.setHeaderValue("Prec. Orig");
       tc.setPreferredWidth(20);
        tc = tcm.getColumn(5); 
       tc.setHeaderValue("Diferencia");
       tc.setPreferredWidth(20);
        tc = tcm.getColumn(6); 
       tc.setHeaderValue("Tipo");
       tc.setPreferredWidth(20);
      tc = tcm.getColumn(7); 
       tc.setHeaderValue("Total");
       tc.setPreferredWidth(20);
       th.repaint(); 
    }
    
    
    
    public void detectafloat(java.awt.event.KeyEvent evt){
        char car = evt.getKeyChar();
        
        if ((car<'0' || car>'9') && car!='.') {            
            evt.consume();
        }
    }
    public void detectaentero(java.awt.event.KeyEvent evt){
        char car = evt.getKeyChar();
        
        if ((car<'0' || car>'9')) {            
            evt.consume();
        }
    }
    public void buscapartida(){
               
        numegrup = jTextField2.getText().toString();
        String sql = "SELECT id, descri, cantidad, precasu, precunit, numero FROM "
                + "mppres WHERE numegrup = "+numegrup+""
                + " AND (mpre_id='"+mpres+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+mpres+"'))";
        
        System.out.println("consulta "+sql);
        if(!"".equals(numegrup)){
        try {
            Statement st = (Statement) conex.createStatement();
            ResultSet rst = st.executeQuery(sql);
            while(rst.next()){
                numero = rst.getObject("numero").toString();
                jTextField3.setText(rst.getObject("id").toString());
                jTextArea1.setText(rst.getObject("descri").toString());
                jTextField4.setText(rst.getObject("cantidad").toString());
                jTextField5.setText(rst.getObject("precasu").toString());
                jTextField6.setText(rst.getObject("precunit").toString());
            }
            int nume=0,numerogrup=0;
            String nuevonum= "SELECT numero FROM mppres WHERE (mpre_id='"+mpres+"' "
                    + "OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+mpres+"')) ORDER BY numero DESC LIMIT 1";
            Statement sts = (Statement) conex.createStatement();
            ResultSet rsts = sts.executeQuery(nuevonum);
            while(rsts.next()){
                nume = rsts.getInt(1)+1;
            }
            String nuevonumegrup = "SELECT numegrup FROM mppres WHERE (mpre_id='"+mpres+"' "
                    + "OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+mpres+"')) ORDER BY numegrup DESC LIMIT 1";
            Statement stn = (Statement) conex.createStatement();
            ResultSet rstn = stn.executeQuery(nuevonumegrup);
            while(rstn.next()){
                numerogrup=rstn.getInt(1)+1;
            }
 
        } catch (SQLException ex) {
            Logger.getLogger(reconsideraciones.class.getName()).log(Level.SEVERE, null, ex);
        }
        }
    }
    private void jTextField2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField2FocusLost
        String cuenta = "SELECT count(*) FROM mppres WHERE "
                + "mppre_id='"+jTextField2.getText().toString()+"' "
                + "AND ("
                + "mpre_id='"+mpres+"' OR mpre_id IN "
                + "(SELECT id FROM mpres WHERE mpres_id='"+mpres+"'))";
        int cuantos=0;
        try {
            
            Statement stcuenta = (Statement) conex.createStatement();
            ResultSet rstcuenta = stcuenta.executeQuery(cuenta);
            while(rstcuenta.next()){
                cuantos = rstcuenta.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(reconsideraciones.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(cuantos==0){
            buscapartida();
        }
        else{
            JOptionPane.showMessageDialog(rootPane, "Partida ya existe en esta reconsideración ingrese otra");
            jTextField2.setText("");
            jTextField2.requestFocus();
        }
      
        
    }//GEN-LAST:event_jTextField2FocusLost
    public void cuenta(){
        try {
            String cuenta = "SELECT numero FROM mppres WHERE mpre_id='"+mpres+"'"
                    + " OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+mpres+"') "
                    + "ORDER BY numero DESC LIMIT 1";

             Statement st = (Statement) conex.createStatement();
                 ResultSet rst = st.executeQuery(cuenta);
                 while(rst.next()){
                     contar = Integer.parseInt(rst.getObject("numero").toString());
                     contar++;
                 }

                 cantidadrecon = Float.valueOf(jTextField7.getText().toString());
                 nuevopreciasum = Float.valueOf(jTextField9.getText().toString());
                 nuevopreciasum = cantidadrecon*nuevopreciasum;
               
        } catch (SQLException ex) {
            Logger.getLogger(reconsideraciones.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
       public void cuenta1(){
        try {
            String cuenta = "SELECT numero FROM mppres"
                    + " WHERE mpre_id='"+mpres+"' OR mpre_id IN"
                    + "  (SELECT id FROM mpres WHERE mpres_id='"+mpres+"') ORDER BY numero DESC LIMIT 1";

             Statement st = (Statement) conex.createStatement();
                 ResultSet rst = st.executeQuery(cuenta);
                 while(rst.next()){
                     contar = Integer.parseInt(rst.getObject("numero").toString());
                     contar++;
                 }
             
                 cantidadrecon = Float.valueOf(jTextField7.getText().toString());
                 nuevopreciasum = Float.valueOf(jTextField9.getText().toString());
                 nuevopreciasum = cantidadrecon*nuevopreciasum;
                
        } catch (SQLException ex) {
            Logger.getLogger(reconsideraciones.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void agregar(){
        
        String  idband = null, porcgad=null, porcpre=null, porcutil=null, precasu=null;
        String precunit = null, rendimi = null, unidad = null, redondeo = null, status = null;
        String mtabus_id="", cantidad="";
        try {
            int numegrup1=0;
            cuenta();
            String selecnumgrup = "SELECT numegrup FROM mppres WHERE (mpre_id='"+mpres+"' "
                    + "OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+mpres+"')) ORDER BY "
                    + "numegrup DESC LIMIT 1";
            Statement consultanumegrup = (Statement) conex.createStatement();
            ResultSet rsnumegrup = consultanumegrup.executeQuery(selecnumgrup);
            while(rsnumegrup.next()){
                numegrup1=rsnumegrup.getInt(1)+1;
            }
            //Copia de la partida
            String selecciona = "SELECT idband, porcgad, porcpre, porcutil, precasu, precunit, rendimi, unidad"
                    + ", redondeo, status, numero FROM mppres WHERE "
                    + "numegrup = "+numegrup+" AND (mpre_id='"+mpres+"' OR mpre_id IN (SELECT id FROM mpres "
                    + "WHERE mpres_id='"+mpres+"'))";
            
            Statement str = (Statement) conex.createStatement();
            ResultSet rstr = str.executeQuery(selecciona);
            while(rstr.next()){
                idband = rstr.getObject("idband").toString();
                if(rstr.getObject("porcgad")!=null) {
                    porcgad = rstr.getObject("porcgad").toString();
                }
                if(rstr.getObject("porcpre")!=null) {
                    porcpre = rstr.getObject("porcpre").toString();
                }
                if(rstr.getObject("porcutil")!=null) {
                    porcutil = rstr.getObject("porcutil").toString();
                }
                if(rstr.getObject("precasu")!=null) {
                    precasu = rstr.getObject("precasu").toString();
                }
                precunit = rstr.getObject("precunit").toString();
                rendimi = rstr.getObject("rendimi").toString();
                unidad = rstr.getObject("unidad").toString();
                redondeo = rstr.getObject("redondeo").toString();
                status = rstr.getObject("status").toString();
                numero = rstr.getObject("numero").toString();
            }                   
            cantidad = jTextField7.getText().toString();
            precunit = jTextField9.getText().toString();
            precasu = precunit;
           codnuevopres= mpres+" VP-"+nrocuadro;
            String inserta = "INSERT INTO mppres (mpre_id, id, numero, numegrup, descri, "
                    + " idband, porcgad, porcpre, porcutil, precasu, precunit, rendimi, "
                    + "unidad, redondeo, status,"
                    + "cantidad, tipo, nrocuadro, tiporec,mppre_id) "
                    + " VALUES ('"+codnuevopres+"','"+jTextField3.getText()+"', "+contar+", "+numegrup1+", "
                    + "'"+jTextArea1.getText()+"',"
                    + ""+idband+", "+porcgad+", "+porcpre+","+porcutil+","
                    + " "+precasu+", "+precunit+", "+rendimi+", "
                    + "'"+unidad+"', "+redondeo+", "+status+","+cantidad+",'VP', "
                    + " "+nrocuadro+", 'VP-"+jTextField2.getText()+"',"+numero+")";
            
         //   System.out.println("Inserta "+inserta);
            Statement sts = (Statement) conex.createStatement();
            sts.execute(inserta);
            
            //COPIA PRESUPUESTO PARA MATRIZ DE COSTOS
            
            String consulta = "SELECT count(*) FROM mpres WHERE id='"+codnuevopres+"'";
            Statement cuenta = (Statement) conex.createStatement();
            ResultSet rscuenta = cuenta.executeQuery(consulta);
            int contare = 0;
            while(rscuenta.next()){
                contare = rscuenta.getInt(1);
            }
            String num="";
            String select = "SELECT numero FROM mppres WHERE numegrup="+jTextField2.getText()+" "
                    + "AND (mpre_id='"+mpres+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+mpres+"'))";
            Statement sts1 = (Statement) conex.createStatement();
            ResultSet rsts = sts1.executeQuery(select);
            while(rsts.next()){
                num=rsts.getString(1);
            }
            if(contare==0){
                //INSERT
                
                String consultainsert = "INSERT INTO mpres (id, nomabr,nombre,ubicac,fecini,fecfin,feccon,fecimp,"
                        + "porgam,porcfi, porimp,"
                        + "poripa, porpre, poruti, codpro, codcon, parpre, nrocon, nroctr, fecapr, nrolic, status, "
                        + "mpres_id, memo, "
                        + "timemo, fecmemo, seleccionado, partidapres)"
                        + "SELECT '"+codnuevopres+"', nomabr, nombre, ubicac, fecini,"
                    + "fecfin, feccon, fecimp, porgam, porcfi, porimp, poripa, "
                    + "porpre, poruti, codpro, codcon, parpre, nrocon, nroctr, fecapr,"
                    + "nrolic, 1, '"+mpres+"',memo,timemo, fecmemo, 0,partidapres FROM mpres WHERE id='"+mpres+"'";
                Statement insertapres = (Statement) conex.createStatement();
                insertapres.execute(consultainsert);                
            }
            
            // DETALLE DE PARTIDA
            
            // MATERIALES
            String selecmat = "INSERT INTO dmpres "
                    + "SELECT '"+codnuevopres+"', mppre_id, mmpre_id, "+contar+", cantidad, precio, status "
                    + "FROM dmpres WHERE numepart="+num+" AND (mpre_id='"+mpres+"' OR mpre_id IN "
                    + "(SELECT id FROM mpres WHERE mpres_id='"+mpres+"'))";
            Statement detmat = (Statement) conex.createStatement();
            detmat.execute(selecmat);
            // EQUIPOS
            String selecequip = "INSERT INTO deppres "
                    + "SELECT '"+codnuevopres+"', mppre_id, mepre_id,"+contar+", cantidad, precio, status "
                    + "FROM deppres WHERE numero = "+num+" AND (mpre_id='"+mpres+"' OR mpre_id IN (SELECT id "
                    + "FROM mpres WHERE mpres_id='"+mpres+"'))";
            Statement deteq = (Statement) conex.createStatement();
            deteq.execute(selecequip);
            //MANO DE OBRA
            String selecmano = "INSERT INTO dmoppres "
                    + "SELECT '"+codnuevopres+"', mmopre_id,mppre_id, "+contar+", cantidad, bono, salario,subsidi, status "
                    + "FROM dmoppres WHERE numero="+num+" AND (mpre_id='"+mpres+"' OR mpre_id IN "
                    + "(SELECT id FROM mpres WHERE mpres_id='"+mpres+"'))";
            Statement detmano = (Statement) conex.createStatement();
            
            // CONSULTA MATRIZ DE COSTOS DEL Pres ANTERIOR Y LO COMPARA CON EL NUEVO PRESUPUESTO PARA INSERTAR LOS NUEVOS
            // MATERIALES, EQUIPOS Y MANO DE OBRA
            String materiales = "SELECT * FROM dmpres WHERE numepart = "+num+" AND (mpre_id = '"+pres+"' OR"
                    + " mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"'))";
            Statement consultmat = (Statement) conex.createStatement();
            ResultSet rsmat = consultmat.executeQuery(materiales);
            while(rsmat.next()){
                String codmat = rsmat.getString("mmpre_id");
                String consultar = "SELECT count(*) FROM mmpres WHERE id='"+codmat+"' AND "
                        + "mpre_id='"+codnuevopres+"'";
                Statement inter = (Statement) conex.createStatement();
                ResultSet rsinter = inter.executeQuery(consultar);
                int cuantos = 0;
                while(rsinter.next()){
                    cuantos = rsinter.getInt(1);
                }
                //Inserte materiales para la nueva matriz de costos del presupuesto
                if(cuantos==0){
                    String insertemat = "INSERT INTO mmpres "
                            + "SELECT '"+codnuevopres+"', id, descri,desperdi, precio, unidad, status "
                            + "FROM mmpres WHERE id='"+codmat+"' AND numepart = "+num+" AND (mpre_id='"+mpres+"' OR "
                            + "mpre_id IN (SELECT id FROM mpres WHERE mpres_id = '"+mpres+"' )) LIMIT 1";
                    Statement insertar = (Statement) conex.createStatement();
                    insertar.execute(insertemat);
                }
            }
            //INSERTA EQUIPOS EN MATRIZ DE COSTOS PARA EL NUEVO PRESUPUESTO
            String equipos = "SELECT * FROM deppres WHERE numero="+num+" "
                    + "AND (mpre_id='"+mpres+"' OR mpre_id IN "
                    + "(SELECT id FROM mpres WHERE mpres_id = '"+mpres+"'))";
            Statement sequipos = (Statement) conex.createStatement();
            ResultSet rsequipos = sequipos.executeQuery(equipos);
            while(rsequipos.next()){
                String codeq = rsequipos.getString("mepre_id");
                String cuentas = "SELECT count(*) FROM mepres WHERE id = '"+codeq+"' AND "
                        + " mpre_id='"+codnuevopres+"'";
                
                Statement stcuentas = (Statement) conex.createStatement();
                ResultSet rstcuentas = stcuentas.executeQuery(cuentas);
                int cuantos=0;
                while(rstcuentas.next()){
                    cuantos = rstcuentas.getInt(1);
                }
                if(cuantos==0){
                    String inserteq = "INSERT INTO mepres "
                            + "SELECT '"+codnuevopres+"', id, descri,deprecia, precio, status "
                            + "FROM mepres WHERE id = '"+codeq+"' AND (mpre_id='"+mpres+"' OR "
                            + "mpre_id IN (SELECT id FROM mpres WHERE mpres_id = '"+mpres+"' )) LIMIT 1";
                    Statement insertar = (Statement) conex.createStatement();
                    insertar.execute(inserteq);
                }
            }
            //INSERTA MANO DE OBRA EN MATRIZ DE COSTOS PARA EL NUEVO PRESUPUESTO
            
            String mano = "SELECT * FROM dmoppres WHERE numero="+num+" AND (mpre_id='"+mpres+"' OR mpre_id IN "
                    + "(SELECT id FROM mpres WHERE mpres_id='"+mpres+"'))";
            Statement smano = (Statement) conex.createStatement();
            ResultSet rsmano = smano.executeQuery(mano);
            while(rsmano.next()){
                String codmano = rsmano.getString("mmopre_id");
                String cuentas = "SELECT count(*) FROM "
                        + "mmopres WHERE id = '"+codmano+"' AND "
                        + "mpre_id='"+codnuevopres+"'";
                System.out.println("cuenta mano de obra vp "+cuentas);
                Statement stcuentas = (Statement) conex.createStatement();
                ResultSet rstcuentas = stcuentas.executeQuery(cuentas);
                int cuantos=0;
                while(rstcuentas.next()){
                    cuantos = rstcuentas.getInt(1);
                }
                if(cuantos==0){
                    String insermano = "INSERT INTO mmopres "
                            + "SELECT '"+codnuevopres+"', id, descri, bono, salario, subsid, status "
                            + "FROM mmopres WHERE id = '"+codmano+"' AND (mpre_id='"+mpres+"' OR "
                            + "mpre_id IN (SELECT id FROM mpres WHERE mpres_id = '"+mpres+"' )) LIMIT 1";
                    System.out.println(" inserta mano "+insermano);
                    Statement insertar = (Statement) conex.createStatement();
                    insertar.execute(insermano);
                }
            }
            
            JOptionPane.showMessageDialog(null, "Se ha insertado una reconsideración de precios para la partida");
            buscacuadro();
            
        } catch (SQLException ex) {
            Logger.getLogger(reconsideraciones.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String mvalu = jTextField1.getText();
        buscapartida busca = new buscapartida(null, true, mpres, conex, nrocuadro,pres,mvalu);
        int xi = (pres.getWidth()/2)-650/2;
        int yi = (pres.getHeight()/2)-450/2;
        busca.setBounds(xi, yi, 650, 450);
        busca.setVisible(true);
        buscacuadro();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTable1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable1KeyPressed
        
        float cantidad;
        float precio;
        String nume=null;
        String numpartida, sql;
            
            System.out.println("TECLAS: "+evt.getKeyCode());
            if(evt.getKeyCode()==9 || evt.getKeyCode()==10){
                numpartida=jTable1.getValueAt( filapart, 1).toString();
                cantidad = Float.valueOf(jTable1.getValueAt(filapart, 4).toString());
                precio = Float.valueOf(jTable1.getValueAt(filapart, 3).toString());
                jTable1.removeEditor();
                System.out.println("TECLAS: "+evt.getKeyCode());
            try {
               sql = "SELECT numero FROM mppres WHERE numegrup = "+numpartida+" AND mpre_id='"+mpres+"'"
                       + " OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+mpres+"')";
                Statement stpart = (Statement) conex.createStatement();
                ResultSet rstpart = stpart.executeQuery(sql);
                while(rstpart.next()){
                    nume = rstpart.getObject("numero").toString();                   
                }
                
                String update= "UPDATE mppres SET cantidad = "+cantidad+", precio="+precio+" WHERE numero="+nume+" "
                        + "AND mpre_id='"+mpres+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+mpres+"')";
                Statement st = (Statement) conex.createStatement();
                st.execute(update);
                buscacuadro();
            } catch (SQLException ex) {
                Logger.getLogger(reconsideraciones.class.getName()).log(Level.SEVERE, null, ex);
            }
              
                
            }
        
    }//GEN-LAST:event_jTable1KeyPressed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        filapart = jTable1.rowAtPoint(evt.getPoint());
        numegrup=jTable1.getValueAt(filapart, 1).toString();
        jTextField2.setText(numegrup);
             

        jButton4.setEnabled(true);
        buscapartida();
       
        jTextField7.setEnabled(true);
        jTextField9.setEnabled(true);
        jButton3.setEnabled(true);
        jButton5.setEnabled(true);
        if(evt.getClickCount()==2){
            ver();
            }   
    }//GEN-LAST:event_jTable1MouseClicked

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
       numegrup = jTable1.getValueAt(filapart, 1).toString();
        String selecnuevo = "SELECT mpre_id FROM mppres WHERE numegrup="+numegrup+" AND (mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+mpres+"'))";
           
        try {
                Statement stse = (Statement) conex.createStatement();
                ResultSet rstse = stse.executeQuery(selecnuevo);
                while(rstse.next()){
                    codnuevopres=rstse.getString(1);
                }
            } catch (SQLException ex) {
                Logger.getLogger(reconsideraciones.class.getName()).log(Level.SEVERE, null, ex);
            }
        int op=JOptionPane.showConfirmDialog(rootPane, "Desea Eliminar Esta Partida de la reconsideración "
                + "de precios? "+codnuevopres,"Borrar de Reconsideración",JOptionPane.YES_NO_OPTION);
       if(op==JOptionPane.YES_OPTION){
       
        String delete ="DELETE FROM mppres WHERE numegrup="+numegrup+" AND "
                + "mpre_id='"+codnuevopres+"'";
        String deletemat = "DELETE FROM dmpres WHERE mpre_id='"+codnuevopres+"' AND numepart="+numero+"";
        String deleteequipo = "DELETE FROM deppres WHERE mpre_id='"+codnuevopres+"' AND numero="+numero+"";
        String deletemano = "DELETE FROM dmoppres WHERE mpre_id='"+codnuevopres+"' AND numero="+numero+"";
        try {
            Statement st = (Statement) conex.createStatement();
            st.execute(delete);
             st.execute(deleteequipo);
              st.execute(deletemano);
               st.execute(deletemat);
            JOptionPane.showMessageDialog(null, "Se ha eliminado la partida");
            buscacuadro();
        } catch (SQLException ex) {
            Logger.getLogger(reconsideraciones.class.getName()).log(Level.SEVERE, null, ex);
        }
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
         String numepart=jTable1.getValueAt(filapart, 1).toString();     
         codnuevopres = mpres+" VP-"+nrocuadro;    
         
        variatab var = new variatab(null, true, conex, codnuevopres, numepart);
        int xi = (pres.getWidth()/2)-500/2;
        int yi = (pres.getHeight()/2)-200/2;
        var.setBounds(xi, yi, 500, 250);
        var.setVisible(true);
        buscacuadro();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jTextArea1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextArea1KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_TAB) {
            if (evt.getModifiers() == KeyEvent.SHIFT_MASK) {
                ((javax.swing.JTextArea) evt.getSource()).transferFocusBackward();
            } else {
                ((javax.swing.JTextArea) evt.getSource()).transferFocus();
            }
            evt.consume();
        }       
    }//GEN-LAST:event_jTextArea1KeyPressed

    private void jSpinner1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner1StateChanged
 buscacuadro();    
 
 String buscapres = "SELECT mpre_id FROM mppres WHERE nrocuadro="+jSpinner1.getValue()+" "
                    + " AND (mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+mpres+"'))";
 System.out.println("buscapres "+buscapres);
 String presu="";
        try {
            Statement st = (Statement) conex.createStatement();
            ResultSet rst = st.executeQuery(buscapres);
            while(rst.next()){
                presu=rst.getString(1);
            }
            jTextField8.setText(presu);
        } catch (SQLException ex) {
            Logger.getLogger(reconsideraciones.class.getName()).log(Level.SEVERE, null, ex);
        }
 // TODO add your handling code here:
    }//GEN-LAST:event_jSpinner1StateChanged

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
    ver();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jTextField2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyTyped
 int code = evt.getKeyCode();
        char car = evt.getKeyChar();
 if ((car<'0' || car>'9') ) {  
           
            evt.consume();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2KeyTyped

    private void jTextField2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField2MouseClicked
        jTextField2.setText("");
        
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2MouseClicked

    
    public void borrarpres (String id){
          
            try {
                
                String borrarpartidas = "DELETE FROM mppres WHERE mpre_id='"+id+"'";
                String borrarmats = "DELETE FROM deppres WHERE mpre_id='"+id+"'";
                String borrarequips = "DELETE FROM dmpres WHERE mpre_id='"+id+"'";
                String borrarmates = "DELETE FROM mmpres WHERE mpre_id='"+id+"'";
                String borrarequipos = "DELETE FROM mepres WHERE mpre_id='"+id+"'";
                String borrarmanos = "DELETE FROM mmopres WHERE mpre_id='"+id+"'";
                String borrardmanos = "DELETE FROM dmoppres WHERE mpre_id='"+id+"'";
                String detvalus = "DELETE FROM dvalus WHERE mpre_id='"+id+"'";
                String mvalus = "DELETE FROM mvalus WHERE mpre_id='"+id+"'";
                String admppres = "DELETE FROM admppres WHERE mpre_id='"+id+"'";
                String pays = "DELETE FROM pays WHERE mpre_id='"+id+"'";
                String cmpres = "DELETE FROM cmpres WHERE mpre_id='"+id+"'";
                String rcppres = "DELETE FROM rcppres WHERE mpre_id='"+id+"'";
                String borrar = "DELETE FROM mpres where id='"+id+"'";
                String borrarNP =  "DELETE FROM mpres WHERE mpres_id='"+id+"'";
                Statement stpres = (Statement) conex.createStatement();
                Statement stppres = (Statement) conex.createStatement();
                Statement stmats= (Statement) conex.createStatement();
                Statement stequipo = (Statement) conex.createStatement();
                Statement stmano = (Statement) conex.createStatement();
                Statement stpresNP = (Statement) conex.createStatement();
                
                
                stppres.execute(borrarpartidas);
                stppres.execute(borrarmats);
                stppres.execute(borrarequips);
                stppres.execute(rcppres);
                stppres.execute(borrardmanos);
                stppres.execute(borrarmates);
                stppres.execute(borrarequipos);
                stppres.execute(borrarmanos);
                stppres.execute(detvalus);
                stppres.execute(mvalus);
                stppres.execute(admppres);
                stppres.execute(pays);
                stpres.execute(cmpres);
                stpres.execute(borrar);
                stpresNP.execute(borrarNP);
                JOptionPane.showMessageDialog(this, "La reconsideración ha sido eliminada!!");
                buscacuadro();
               }
            // TODO add your handling code here:
            catch (SQLException ex) {
                Logger.getLogger(Presupuesto.class.getName()).log(Level.SEVERE, null, ex);
            }
    
    
    }
    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        int op=JOptionPane.showConfirmDialog(rootPane, "Desea eliminar esta reconsideración?","Eliminar Reconsideración", JOptionPane.YES_NO_OPTION);
        if(op==JOptionPane.YES_OPTION){
            String presrecon = jTextField8.getText();
            borrarpres(presrecon);
        }
        
        
        
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jTextField8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField8ActionPerformed

    private void jTextField7KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField7KeyTyped

    }//GEN-LAST:event_jTextField7KeyTyped

    private void jTextField9KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField9KeyTyped

    }//GEN-LAST:event_jTextField9KeyTyped

    private void jTextField10KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField10KeyTyped

    }//GEN-LAST:event_jTextField10KeyTyped

    private void jTextField11KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField11KeyTyped

    }//GEN-LAST:event_jTextField11KeyTyped

    private void jTextField12KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField12KeyTyped
        
        
    }//GEN-LAST:event_jTextField12KeyTyped

    private void jTextField13KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField13KeyTyped

    }//GEN-LAST:event_jTextField13KeyTyped

    
    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
       
        String consultacuadro = "SELECT nrocuadro FROM mppres WHERE (mpre_id='"+mpres+"' OR mpre_id "
                + "IN (SELECT id FROM mpres WHERE mpres_id='"+mpres+"')) ORDER BY nrocuadro DESC LIMIT 1";
        int cuadro=0;
        try {
            Statement stcuadro = (Statement) conex.createStatement();
            ResultSet rstcuadro = stcuadro.executeQuery(consultacuadro);
            while(rstcuadro.next()){
                cuadro =rstcuadro.getInt(1);
            }
            cuadro++;
        } catch (SQLException ex) {
            Logger.getLogger(reconsideraciones.class.getName()).log(Level.SEVERE, null, ex);
        }
        String codpres=mpres+" VP-"+cuadro;
        parametrorecon para = new parametrorecon(prin, true, conex, mpres, String.valueOf(cuadro), codpres);
        int xi = (prin.getWidth()/2)-550/2;
        int yi =(prin.getHeight()/2)-600/2;
         para.setBounds(xi, yi, 550, 600);
        para.setVisible(true);
        jSpinner1.setValue(cuadro);
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton6ActionPerformed
 public float cambiapreciopartida(String numeropartida){
        //AL CAMBIAR UN VALOR DE LOS PARAMETROS DEL PRESUPUESTO COMO LOS PORCENTAJES DE PRESTACIONES SOCIALES
        // SE RECALCULA EL COSTO DE TODAS LAS PARTIDAS
        float presta = Float.valueOf(jTextField7.getText())/100;
        float admin = Float.valueOf(jTextField9.getText())/100;
        float util = Float.valueOf(jTextField10.getText())/100;
        float finan = Float.valueOf(jTextField11.getText())/100;
        float impart = Float.valueOf(jTextField12.getText())/100;
        float impgen = Float.valueOf(jTextField13.getText())/100;
        float precio=0, totalmat=0, totaleq=0, totalmano=0, totalcantidad=0;
        float bono=0, subsid=0;
        String codpres = pres+" VP-"+nrocuadro;
        float rendimi=0;
        String mvalu=jTextField1.getText();
        if(!jCheckBox1.isSelected()){
            String select="SELECT porcgad, porcpre, porcutil FROM mppres WHERE numero="+numeropartida+" "
                    + "AND (mpre_id='"+codpres+"')";
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
        String selecrendimi = "SELECT rendimi FROM mppres WHERE numero="+numeropartida+" AND mpre_id='"+codpres+"'";
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
                + "AND dm.mpre_id='"+codpres+"' "
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
                + "de.mpre_id='"+codpres+"' "
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
                + "WHERE dm.numero ="+numeropartida+" AND dm.mpre_id='"+codpres+"'  AND dm.mmopre_id=mm.id AND dm.mpre_id = mm.mpre_id";
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
    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        
        
        cambiapreciopartida(numero);        
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton7ActionPerformed

    
    public void actualizapartidas(){
     String consultapartidas="SELECT numero FROM mppres WHERE mpre_id='"+codnuevopres+"'";
        try {
            Statement st = (Statement) conex.createStatement();
            ResultSet rst = st.executeQuery(consultapartidas);
            
            while(rst.next()){
               numero = rst.getString(1); 
               float precio=cambiapreciopartida(numero);
               String partidas = "UPDATE mppres SET precunit="+precio+" WHERE mpre_id='"+codnuevopres+"' AND "
                       + "numero="+numero+"";
               Statement actualiza = (Statement) conex.createStatement();
               actualiza.execute(partidas);
            }
        } catch (SQLException ex) {
            Logger.getLogger(reconsideraciones.class.getName()).log(Level.SEVERE, null, ex);
        }
}
private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        codnuevopres= mpres+" VP-"+nrocuadro;       
        matrizmaterialespres materiales = new matrizmaterialespres(prin, true, conex, codnuevopres,prin);
        int xi = (prin.getWidth()/2)-700/2;
        int yi = (prin.getHeight()/2)-500/2;
        materiales.setBounds(xi, yi, 700, 500);
        materiales.setVisible(true);    
        buscacuadro();
       
}//GEN-LAST:event_jButton11ActionPerformed

private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed

     codnuevopres= mpres+" VP-"+nrocuadro;      
    matrizequipospres equipos = new matrizequipospres(prin, true, conex, codnuevopres,prin);
        int xi = (prin.getWidth()/2)-700/2;
        int yi = (prin.getHeight()/2)-500/2;
        equipos.setBounds(xi, yi, 700, 500);
        equipos.setVisible(true);
  
        buscacuadro();
    
    // TODO add your handling code here:
}//GEN-LAST:event_jButton12ActionPerformed

private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
    codnuevopres= mpres+" VP-"+nrocuadro;              
    matrizmanopres mano = new matrizmanopres(prin, true, conex, codnuevopres,prin);
        int xi = (prin.getWidth()/2)-700/2;
        int yi = (prin.getHeight()/2)-500/2;
        mano.setBounds(xi, yi, 700, 500);
        mano.setVisible(true);

        buscacuadro();
    
    // TODO add your handling code here:
}//GEN-LAST:event_jButton13ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed

    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        reporterecon recon = new reporterecon(prin, false, conex, mpres, nrocuadro);
         int xi = (prin.getWidth()/2)-500/2;
        int yi = (prin.getHeight()/2)-400/2;
        recon.setBounds(xi, yi, 500, 400);
        recon.setVisible(true);
        
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton8ActionPerformed
     private void ver() 
     {
         String parti=jTable1.getValueAt(filapart, 1).toString();
         codnuevopres= mpres+" VP-"+nrocuadro;
            Partida part = new Partida(prin, true, conex,codnuevopres, prin, 1, 0, codicove, parti, pres,"0");
        int x = (prin.getWidth()/2)-390;
        int y = (prin.getHeight()/2)-300;
        
        part.setBounds(x, y, 770, 520);
        part.setVisible(true);
        buscacuadro();
    }
     
    private void doClose(int retStatus) {
        returnStatus = retStatus;
        setVisible(false);
        dispose();
    }

    /**
     * @param args the command line arguments
     */
 
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.persistence.EntityManager WinspapusPUEntityManager;
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JCheckBox jCheckBox1;
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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextArea jTextArea1;
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
    // End of variables declaration//GEN-END:variables
    private int returnStatus = RET_CANCEL;

   
}
