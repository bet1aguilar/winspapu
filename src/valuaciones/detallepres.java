/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * detallepres.java
 *
 * Created on 06/11/2013, 03:47:46 PM
 */
package valuaciones;
import java.sql.Connection;
import com.mysql.jdbc.Statement;
import config.Redondeo;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Betmart
 */
public class detallepres extends javax.swing.JDialog {

    /** A return status code - returned if Cancel button has been pressed */
    public static final int RET_CANCEL = 0;
    /** A return status code - returned if OK button has been pressed */
    public static final int RET_OK = 1;
    private Connection conex;
    Redondeo redon = new Redondeo();
    String pres="";
            
    /** Creates new form detallepres */
    public detallepres(java.awt.Frame parent, boolean modal, Connection conex, String pres) {
        super(parent, modal);
        initComponents();
        this.conex=conex;
        this.pres=pres;
        buscapres();
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
    public final void buscapres(){
        String covenin="";
        BigDecimal acumaum=new BigDecimal("0.00"), acumdismi=new BigDecimal("0.00"), acumvari=new BigDecimal("0.00"), acumnp=new BigDecimal("0.00"), acumoe=new BigDecimal("0.00"), acumoa=new BigDecimal("0.00"), acumoc=new BigDecimal("0.00");
        try {
            
           int pos=1;
             DefaultTableModel mepres = new DefaultTableModel(){
                   @Override
               public boolean isCellEditable (int row, int column) {
           
            return false;
        }
                @Override
                    public Class getColumnClass(int columna)
               {
                   if (columna == 0) {
                        return Integer.class;
                    }
                    if(columna>0){
                        return Double.class;
                    }

                    return Object.class;
                }
               };
             
             jTable1.setModel(mepres);
             mepres.addColumn("Nro.");
             mepres.addColumn("Aumentos");
             mepres.addColumn("Disminución");
             mepres.addColumn("VP");
             mepres.addColumn("NP");
             mepres.addColumn("OA");
             mepres.addColumn("OE");
             mepres.addColumn("OC");
             while(pos<31){
                 Object[] filas = new Object[8];
                 BigDecimal cantaumento = new BigDecimal("0.00"), precunitaumento = new BigDecimal("0.00"), cantdismi=new BigDecimal("0.00"), precunitdismi=new BigDecimal("0.00");
                 BigDecimal precunitvaria=new BigDecimal("0.00"), cantvari=new BigDecimal("0.00"), precnp=new BigDecimal("0.00"), cantnp=new BigDecimal("0.00"),cantoa=new BigDecimal("0.00"), cantoe=new BigDecimal("0.00"), cantoc=new BigDecimal("0.00");
                 BigDecimal precoa=new BigDecimal("0.00"), precoe=new BigDecimal("0.00"), precoc=new BigDecimal("0.00");
                 filas[0]=pos;
                 //AUMENTOS
                 String aumentopart = "SELECT IFNULL(SUM(ROUND(IFNULL(ROUND(a.aumento,2),0)*IFNULL(IF(precasu=0,precunit,precasu),0),2)),0) as precunit"
                         + " FROM admppres a, mppres m WHERE  "
                         + " (a.mpre_id='"+pres+"' OR a.mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"'))"
                             + "AND (m.mpre_id='"+pres+"' OR m.mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"'))"
                           + " AND a.payd_id='"+pos+"' AND (m.mpre_id = a.mpre_id OR m.mpre_id IN "
                         + "(SELECT id FROM mpres WHERE mpres_id=a.mpre_id)) AND m.numero = a.numepart AND "
                             + "a.aumento>0 ORDER BY numegrup";
                 Statement saumento = (Statement) conex.createStatement();
                 ResultSet rsaument = saumento.executeQuery(aumentopart);
                 while(rsaument.next()){
                     precunitaumento = rsaument.getBigDecimal(1);
                 }
                 acumaum = acumaum.add(precunitaumento);
                  filas[1]=precunitaumento;
                  //DISMINUCIONES
                  String dismi = "SELECT IFNULL(SUM(ROUND(IFNULL(ROUND(a.disminucion,2),0)*IFNULL(IF(precasu=0,precunit,precasu),0),2)),0) as precunit"
                         + " FROM admppres a, mppres m WHERE  "
                         + " (a.mpre_id='"+pres+"' OR a.mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"'))"
                             + "AND (m.mpre_id='"+pres+"' OR m.mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"'))"
                           + " AND a.payd_id='"+pos+"' AND (m.mpre_id = a.mpre_id OR m.mpre_id IN "
                         + "(SELECT id FROM mpres WHERE mpres_id=a.mpre_id)) AND m.numero = a.numepart AND "
                             + "a.disminucion>0 ORDER BY numegrup";
                  System.out.println("dismi "+dismi);
                  Statement sdisminuacion = (Statement) conex.createStatement();
                 ResultSet rsdismi = sdisminuacion.executeQuery(dismi);
                 while(rsdismi.next())
                   
                     precunitdismi = rsdismi.getBigDecimal(1);
                 
                
                  filas[2]=precunitdismi;
                  acumdismi=acumdismi.add(precunitdismi);
                  //VARIACIONES DE PRECIO
                  
                  /*
                   * 
                   * SELECT ROUND((IF(mp.precasu = 0,mp.precunit,mp.precasu) - 
                   * (SELECT IF(precasu = 0, precunit, precasu) FROM mppres WHERE
                   * (mpre_id = 'COR01' OR mpre_id IN (SELECT id FROM mpres WHERE
                            mpres_id = 'COR01'))
                        AND numero = mp.mppre_id)) * IFNULL(mp.cantidad, 0), 2)
                    FROM
                        mppres as mp
                    WHERE
                        mp.tipo = 'VP' AND mp.nrocuadro = 1
                            AND (mp.mpre_id = 'COR01'
                            OR mp.mpre_id IN (SELECT 
                                id
                            FROM
                                mpres
                            WHERE
                                mpres_id = 'COR01'))
                   */
                  String consultavari = "SELECT IFNULL(ROUND((IF(mp.precasu=0, mp.precunit, mp.precasu)-"
                          + "(SELECT IF(precasu=0, precunit, precasu) FROM mppres WHERE (mpre_id='"+pres+"' "
                          + "OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) AND numero = mp.mppre_id))"
                          + "*IFNULL(mp.cantidad,0),2),0) FROM "
                          + "mppres as mp WHERE mp.tipo='VP' AND mp.nrocuadro="+pos+" AND (mp.mpre_id='"+pres+"' "
                          + "OR mp.mpre_id IN (SELECT id FROM"
                          + " mpres WHERE mpres_id='"+pres+"'))";
                  System.out.println("consultavari "+consultavari);
                  Statement stconsutavari = (Statement) conex.createStatement();
                  ResultSet rstconsutavari = stconsutavari.executeQuery(consultavari);
                  while(rstconsutavari.next()){
                      precunitvaria = precunitvaria.add(rstconsutavari.getBigDecimal(1));
                  }
                 
                  filas[3]=precunitvaria;
                  acumvari= acumvari.add(precunitvaria);
                  //NO PREVISTAS
                  String np = "SELECT  IFNULL(SUM(ROUND(IFNULL((IF(precasu = 0, precunit, precasu)),0) *IFNULL((cantidad), 0),2)),0)  FROM "
                          + "mppres WHERE tipo='NP' AND tiponp='NP' AND nropresupuesto="+pos+" "
                          + "AND (mpre_id='"+pres+"' OR mpre_id IN (SELECT id FROM"
                          + " mpres WHERE mpres_id='"+pres+"')) ";
                 Statement stconsutanp = (Statement) conex.createStatement();
                  ResultSet rstconsutanp = stconsutanp.executeQuery(np);
                  while(rstconsutanp.next()){
                      precnp = rstconsutanp.getBigDecimal(1);
                  }
                  filas[4]=precnp;
                  acumnp= acumnp.add( precnp); 
              
                  //OBRAS ADICIONALES
                   String oa = "SELECT  IFNULL(SUM(ROUND(IFNULL((IF(precasu = 0, precunit, precasu)),0) *IFNULL((cantidad), 0),2)),0)  FROM "
                          + "mppres WHERE tipo='NP' AND tiponp='OA' AND nropresupuesto="+pos+" "
                          + "AND (mpre_id='"+pres+"' OR mpre_id IN (SELECT id FROM"
                          + " mpres WHERE mpres_id='"+pres+"')) ";
                 
                 Statement stconsutaoa = (Statement) conex.createStatement();
                  ResultSet rstconsutaoa = stconsutaoa.executeQuery(oa);
                  while(rstconsutaoa.next()){
                      precoa = rstconsutaoa.getBigDecimal(1);
                  }
                  filas[5]=precoa;
                  acumoa= acumoa.add(precoa); 
                  //OBRAS EXTRAS
                   String oe = "SELECT  IFNULL(SUM(ROUND(IFNULL((IF(precasu = 0, precunit, precasu)),0) *IFNULL((cantidad), 0),2)),0)  FROM "
                          + "mppres WHERE tipo='NP' AND tiponp='OE' AND nropresupuesto="+pos+" "
                          + "AND (mpre_id='"+pres+"' OR mpre_id IN (SELECT id FROM"
                          + " mpres WHERE mpres_id='"+pres+"')) ";

                 Statement stconsutaoe = (Statement) conex.createStatement();
                  ResultSet rstconsutaoe = stconsutaoe.executeQuery(oe);
                  while(rstconsutaoe.next()){
                      precoe = rstconsutaoe.getBigDecimal(1);
                  }
                  filas[6]=precoe;
                  acumoe = acumoe.add(precoe); 
                  //OBRAS COMPLEMENTARIAS
                   String oc = "SELECT  IFNULL(SUM(ROUND(IFNULL((IF(precasu = 0, precunit, precasu)),0) *IFNULL((cantidad), 0),2)),0)  FROM "
                          + "mppres WHERE tipo='NP' AND tiponp='OC' AND nropresupuesto="+pos+" "
                          + "AND (mpre_id='"+pres+"' OR mpre_id IN (SELECT id FROM"
                          + " mpres WHERE mpres_id='"+pres+"')) ";
                 Statement stconsutaoc = (Statement) conex.createStatement();
                  ResultSet rstconsutaoc = stconsutaoc.executeQuery(oc);
                  while(rstconsutaoc.next()){
                      precoc = rstconsutaoc.getBigDecimal(1);
                  }
                  filas[7]=precoc;
                  acumoc= acumoc.add(precoc); 
                  
                  mepres.addRow(filas);
                  
                  pos++;
             }
             
            } catch (SQLException ex) {
            Logger.getLogger(detallepres.class.getName()).log(Level.SEVERE, null, ex);
            }
        jTextField2.setText(String.valueOf(acumaum));
        jTextField1.setText(String.valueOf(acumdismi));
        jTextField3.setText(String.valueOf(acumvari));
        jTextField5.setText(String.valueOf(acumnp));
        jTextField7.setText(String.valueOf(acumoa));
        jTextField8.setText(String.valueOf(acumoe));
        jTextField9.setText(String.valueOf(acumoc));
        BigDecimal acumdif = acumaum.add(acumvari).add(acumnp).add(acumoa).add(acumoe).add(acumoc).subtract(acumdismi);
        jTextField6.setText(String.valueOf(acumdif));
         
           
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField4 = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        cancelButton = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        jTextField6 = new javax.swing.JTextField();
        jTextField7 = new javax.swing.JTextField();
        jTextField8 = new javax.swing.JTextField();
        jTextField9 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();

        jTextField4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField4ActionPerformed(evt);
            }
        });

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        cancelButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/eliminar.png"))); // NOI18N
        cancelButton.setToolTipText("Salir");
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
                .addContainerGap()
                .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel4.setBackground(new java.awt.Color(91, 91, 95));

        jLabel1.setBackground(new java.awt.Color(91, 91, 95));
        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Detalle de Presupuesto");
        jLabel1.setOpaque(true);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 743, Short.MAX_VALUE)
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
        jTable1.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(jTable1);

        jTextField1.setEditable(false);
        jTextField1.setFont(new java.awt.Font("Tahoma", 0, 10));
        jTextField1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jTextField2.setEditable(false);
        jTextField2.setFont(new java.awt.Font("Tahoma", 0, 10));
        jTextField2.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        jTextField3.setEditable(false);
        jTextField3.setFont(new java.awt.Font("Tahoma", 0, 10));
        jTextField3.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });

        jTextField5.setEditable(false);
        jTextField5.setFont(new java.awt.Font("Tahoma", 0, 10));
        jTextField5.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField5ActionPerformed(evt);
            }
        });

        jTextField6.setEditable(false);
        jTextField6.setFont(new java.awt.Font("Tahoma", 0, 10));
        jTextField6.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jTextField7.setEditable(false);
        jTextField7.setFont(new java.awt.Font("Tahoma", 0, 10));
        jTextField7.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField7ActionPerformed(evt);
            }
        });

        jTextField8.setEditable(false);
        jTextField8.setFont(new java.awt.Font("Tahoma", 0, 10));
        jTextField8.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField8ActionPerformed(evt);
            }
        });

        jTextField9.setEditable(false);
        jTextField9.setFont(new java.awt.Font("Tahoma", 0, 10));
        jTextField9.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField9ActionPerformed(evt);
            }
        });

        jLabel2.setText("Diferencia:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 723, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(116, 116, 116)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jTextField2, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField3, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField5, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField7, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField8, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE))
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE)
                            .addComponent(jTextField9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(14, 14, 14))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(678, 678, 678)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
    
    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        doClose(RET_CANCEL);
    }//GEN-LAST:event_cancelButtonActionPerformed

    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        doClose(RET_CANCEL);
    }//GEN-LAST:event_closeDialog

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField3ActionPerformed

    private void jTextField4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField4ActionPerformed

    private void jTextField5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField5ActionPerformed

    private void jTextField7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField7ActionPerformed

    private void jTextField8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField8ActionPerformed

    private void jTextField9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField9ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField9ActionPerformed
    
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
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
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
