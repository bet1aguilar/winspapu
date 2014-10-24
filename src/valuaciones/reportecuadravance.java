 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * reportecuadrocierre.java
 *
 * Created on 25/08/2014, 10:33:17 AM
 */
package valuaciones;
import reportes.*;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Betmart
 */
public class reportecuadravance extends javax.swing.JDialog {

    /** A return status code - returned if Cancel button has been pressed */
    public static final int RET_CANCEL = 0;
    /** A return status code - returned if OK button has been pressed */
    public static final int RET_OK = 1;
    Connection conex;
    String pres;
    private double totalpres;
    private double impuesto;
  SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
  String fecha;
  String mvalu;
  Date fechita = new Date();
    /** Creates new form reportecuadrocierre */
    public reportecuadravance(java.awt.Frame parent, boolean modal, Connection conex, String mpres, String mvalu) {
        super(parent, modal);
        initComponents();
        pres=mpres;
        this.mvalu=mvalu;
        jDateChooser1.setDate(fechita);
        this.conex=conex;
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

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jCheckBox1 = new javax.swing.JCheckBox();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        okButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/guardar.png"))); // NOI18N
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

        jLabel1.setText("Titulo:");

        jTextField1.setText("CUADRO DEMOSTRATIVO DE AVANCE DE OBRA  AL DIA:");

        jLabel2.setText("Nro. Oficio:");

        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        jLabel3.setText("Fecha:");

        jDateChooser1.setDateFormatString("dd-MM-yyyy");

        jCheckBox1.setSelected(true);
        jCheckBox1.setText("Con fecha");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(295, Short.MAX_VALUE)
                .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 327, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jDateChooser1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextField2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jCheckBox1)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jCheckBox1)
                    .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(21, 21, 21)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cancelButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getRootPane().setDefaultButton(okButton);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void generarreporte(){
        JasperPrint print=null;
     totalpres=0;
      Map parameters = new HashMap();
        impuesto = 0;
            String imp = "SELECT porimp FROM mpres WHERE id='"+pres+"'";
        try {
            Statement simp = (Statement) conex.createStatement();
            ResultSet rsimp = simp.executeQuery(imp);
            while(rsimp.next()){
                impuesto=rsimp.getFloat(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(reportepresupuesto.class.getName()).log(Level.SEVERE, null, ex);
        }
         FileInputStream input=null;
              String titulo = jTextField1.getText().toString();
        try {
            input = new FileInputStream(new File("avance.jrxml"));
            
            String borra = "TRUNCATE TABLE reportecuadrocierre";
            Statement truncate;
            try {
                truncate = (Statement) conex.createStatement();
                 truncate.execute(borra);
                 String completa="(SELECT mp.numegrup, mp.id, mp.descri, mp.unidad, IF(mp.precasu=0,mp.precunit, mp.precasu) as precio, "
                         + "mp.cantidad,IF(mp.precasu=0,mp.precunit, mp.precasu)*mp.cantidad, "
                         + "IFNULL((SELECT aumento FROM admppres WHERE numepart=mp.numero AND mpre_id='"+pres+"' AND mvalu_id<='"+mvalu+"'),0) as aumcantidad, "
                         + "IFNULL((SELECT aumento FROM admppres WHERE numepart=mp.numero AND mpre_id='"+pres+"' AND mvalu_id<='"+mvalu+"'),0)*"
                         + "IF(mp.precasu=0,mp.precunit, mp.precasu) as aummonto,"
                          + "IFNULL((SELECT disminucion FROM admppres WHERE numepart=mp.numero AND mpre_id='"+pres+"' AND mvalu_id<='"+mvalu+"'),0)"
                         + " as dismicantidad, "
                          + "IFNULL((SELECT disminucion FROM admppres WHERE numepart=mp.numero AND mpre_id='"+pres+"' AND mvalu_id<='"+mvalu+"'),0)*"
                         + "IF(mp.precasu=0,mp.precunit, mp.precasu) as dismonto, "
                         + "mp.cantidad+IFNULL((SELECT aumento FROM admppres WHERE numepart=mp.numero AND mpre_id='"+pres+"' AND mvalu_id<='"+mvalu+"'),0)-"
                         + "IFNULL((SELECT disminucion FROM admppres WHERE numepart=mp.numero AND mpre_id='"+pres+"' AND mvalu_id<='"+mvalu+"'),0) as cantmodificado"
                         
                         + ", (mp.cantidad+IFNULL((SELECT aumento FROM admppres WHERE numepart=mp.numero AND mpre_id='"+pres+"' AND mvalu_id<='"+mvalu+"'),0)-"
                         + "IFNULL((SELECT disminucion FROM admppres WHERE numepart=mp.numero AND mpre_id='"+pres+"' AND mvalu_id<='"+mvalu+"'),0))"
                         + "*IF(mp.precasu=0,mp.precunit, mp.precasu)"
                         + " as montomodificado, '"+pres+"',(((mp.cantidad+IFNULL((SELECT aumento FROM admppres WHERE numepart=mp.numero AND mpre_id='"+pres+"' AND mvalu_id<='"+mvalu+"'),0)-"
                         + "IFNULL((SELECT disminucion FROM admppres WHERE numepart=mp.numero AND mpre_id='"+pres+"' AND mvalu_id<='"+mvalu+"'),0))"
                         + "*IF(mp.precasu=0,mp.precunit, mp.precasu))"
                         + " /(IF(mp.precasu=0,mp.precunit, mp.precasu)*mp.cantidad))*100 as porcmodificado "
                         + "FROM mppres AS mp WHERE mpre_id='"+pres+"' AND tipo ='Org')";
                 String original= "INSERT INTO reportecuadrocierre (nro, codigo, descri, unidad, precio, origcant, origmonto,"
                         + "aumcantidad, aummonto,discantidad,dismonto,cantmodificado,montomodificado,mpres, porcmodificado)"
                         + completa;
                 Statement insertori = (Statement) conex.createStatement();
                 insertori.execute(original);
                 //-----------------------------------NP
                 
                String cuentanp = "SELECT COUNT(*) FROM mppres WHERE tipo='NP' AND tiponp='NP' AND "
                         + "(mpre_id='"+pres+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"'))";
                 Statement cnp = (Statement) conex.createStatement();
                 ResultSet rscnp = cnp.executeQuery(cuentanp);
                 int cuenta=0;
                 while(rscnp.next()){
                     cuenta=rscnp.getInt(1);
                 }
                 if(cuenta>0)
                 {
                     String titulos = "INSERT INTO reportecuadrocierre (codigo,descri, mpres) "
                             + "VALUES ('','PARTIDAS NO PREVISTAS','"+pres+"')";
                     Statement sts = (Statement) conex.createStatement();
                     sts.execute(titulos);
                     String agrega = "INSERT INTO reportecuadrocierre (nro, codigo, descri,unidad,precio,npcantidad,npmonto,"
                             + "aumcantidad,aummonto,discantidad,dismonto,cantmodificado, montomodificado,mpres)"
                             + " (SELECT mp.numegrup, mp.id, mp.descri, mp.unidad, IF(mp.precasu=0,mp.precunit,mp.precasu) as precio, "
                             + "mp.cantidad,"
                             + "mp.cantidad*IF(mp.precasu=0,mp.precunit,mp.precasu) as npmonto, (SELECT aumento FROM admppres WHERE "
                             + "(mpre_id='"+pres+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) AND "
                             + "numepart=mp.numero "
                             + ") as aumcantidad, (SELECT aumento FROM admppres WHERE "
                             + "(mpre_id='"+pres+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) AND "
                             + "numepart=mp.numero AND mvalu_id<='"+mvalu+"'"
                             + ")*IF(mp.precasu=0,mp.precunit,mp.precasu) as aummonto, (SELECT disminucion FROM admppres WHERE "
                             + "(mpre_id='"+pres+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) AND "
                             + "numepart=mp.numero AND mvalu_id<='"+mvalu+"'"
                             + ") as discantidad, (SELECT aumento FROM admppres WHERE "
                             + "(mpre_id='"+pres+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) AND "
                             + "numepart=mp.numero AND mvalu_id<='"+mvalu+"'"
                             + ")* IF(mp.precasu=0,mp.precunit,mp.precasu) as dismonto, mp.cantidad+ (SELECT aumento FROM admppres WHERE "
                             + "(mpre_id='"+pres+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) AND "
                             + "numepart=mp.numero AND mvalu_id<='"+mvalu+"'"
                             + ")-(SELECT disminucion FROM admppres WHERE "
                             + "(mpre_id='"+pres+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) AND "
                             + "numepart=mp.numero AND mvalu_id<='"+mvalu+"'"
                             + ") as cantmodificado, mp.cantidad+ (SELECT aumento FROM admppres WHERE "
                             + "(mpre_id='"+pres+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) AND "
                             + "numepart=mp.numero AND mvalu_id<='"+mvalu+"'"
                             + ")-(SELECT disminucion FROM admppres WHERE "
                             + "(mpre_id='"+pres+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) AND "
                             + "numepart=mp.numero AND mvalu_id<='"+mvalu+"'"
                             + ")*IF(mp.precasu=0,mp.precunit,mp.precasu) as montomodificado, '"+pres+"' FROM mppres as mp "
                             + "WHERE mp.tipo='NP' AND mp.tiponp='NP' AND (mp.mpre_id='"+pres+"' OR mp.mpre_id IN (SELECT id FROM "
                             + "mpres WHERE mpres_id='"+pres+"')))";
                     Statement np=(Statement) conex.createStatement();
                     np.execute(agrega);
                            
                 }
                 //-----------------------------------OE
                 
                String cuentaoe = "SELECT COUNT(*) FROM mppres WHERE tipo='NP' AND tiponp='OE' AND "
                         + "(mpre_id='"+pres+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"'))";
                 Statement coe = (Statement) conex.createStatement();
                 ResultSet rscoe = coe.executeQuery(cuentaoe);
                 int cuentaoex=0;
                 while(rscoe.next()){
                     cuentaoex=rscoe.getInt(1);
                 }
                 if(cuentaoex>0)
                 {
                     String titulos = "INSERT INTO reportecuadrocierre (codigo,descri, mpres) "
                             + "VALUES ('','PARTIDAS OBRAS EXTRAS','"+pres+"')";
                     Statement sts = (Statement) conex.createStatement();
                     sts.execute(titulos);
                     String agrega = "INSERT INTO reportecuadrocierre (nro, codigo, descri,unidad,precio,npcantidad,npmonto,"
                             + "aumcantidad,aummonto,discantidad,dismonto,cantmodificado, montomodificado,mpres)"
                             + " (SELECT mp.numegrup, mp.id, mp.descri, mp.unidad, IF(mp.precasu=0,mp.precunit,mp.precasu) as precio, "
                             + "mp.cantidad,"
                             + "mp.cantidad*IF(mp.precasu=0,mp.precunit,mp.precasu) as npmonto, (SELECT aumento FROM admppres WHERE "
                             + "(mpre_id='"+pres+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) AND "
                             + "numepart=mp.numero AND mvalu_id<='"+mvalu+"'"
                             + ") as aumcantidad, (SELECT aumento FROM admppres WHERE "
                             + "(mpre_id='"+pres+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) AND "
                             + "numepart=mp.numero AND mvalu_id<='"+mvalu+"'"
                             + ")*IF(mp.precasu=0,mp.precunit,mp.precasu) as aummonto, (SELECT disminucion FROM admppres WHERE "
                             + "(mpre_id='"+pres+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) AND "
                             + "numepart=mp.numero AND mvalu_id<='"+mvalu+"'"
                             + ") as discantidad, (SELECT aumento FROM admppres WHERE "
                             + "(mpre_id='"+pres+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) AND "
                             + "numepart=mp.numero AND mvalu_id<='"+mvalu+"'"
                             + ")* IF(mp.precasu=0,mp.precunit,mp.precasu) as dismonto, mp.cantidad+ (SELECT aumento FROM admppres WHERE "
                             + "(mpre_id='"+pres+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) AND "
                             + "numepart=mp.numero AND mvalu_id<='"+mvalu+"'"
                             + ")-(SELECT disminucion FROM admppres WHERE "
                             + "(mpre_id='"+pres+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) AND "
                             + "numepart=mp.numero AND mvalu_id<='"+mvalu+"'"
                             + ") as cantmodificado, mp.cantidad+ (SELECT aumento FROM admppres WHERE "
                             + "(mpre_id='"+pres+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) AND "
                             + "numepart=mp.numero AND mvalu_id<='"+mvalu+"'"
                             + ")-(SELECT disminucion FROM admppres WHERE "
                             + "(mpre_id='"+pres+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) AND "
                             + "numepart=mp.numero AND mvalu_id<='"+mvalu+"'"
                             + ")*IF(mp.precasu=0,mp.precunit,mp.precasu) as montomodificado, '"+pres+"' FROM mppres as mp "
                             + "WHERE mp.tipo='NP' AND mp.tiponp='OE' AND (mp.mpre_id='"+pres+"' OR mp.mpre_id IN (SELECT id FROM "
                             + "mpres WHERE mpres_id='"+pres+"')))";
                     Statement np=(Statement) conex.createStatement();
                     np.execute(agrega);
                            
                 }
                 //-----------------------------------OA
                 
                String cuentaoa = "SELECT COUNT(*) FROM mppres WHERE tipo='NP' and tiponp='OA' AND "
                         + "(mpre_id='"+pres+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"'))";
                 Statement coa = (Statement) conex.createStatement();
                 ResultSet rscoa = coa.executeQuery(cuentaoa);
                 int cuentaoad=0;
                 while(rscoa.next()){
                     cuentaoad=rscoa.getInt(1);
                 }
                 if(cuentaoad>0)
                 {
                     String titulos = "INSERT INTO reportecuadrocierre (codigo,descri, mpres) "
                             + "VALUES ('','PARTIDAS OBRAS ADICIONALES','"+pres+"')";
                     Statement sts = (Statement) conex.createStatement();
                     sts.execute(titulos);
                     String agrega = "INSERT INTO reportecuadrocierre (nro, codigo, descri,unidad,precio,npcantidad,npmonto,"
                             + "aumcantidad,aummonto,discantidad,dismonto,cantmodificado, montomodificado,mpres)"
                             + " (SELECT mp.numegrup, mp.id, mp.descri, mp.unidad, IF(mp.precasu=0,mp.precunit,mp.precasu) as precio, "
                             + "mp.cantidad,"
                             + "mp.cantidad*IF(mp.precasu=0,mp.precunit,mp.precasu) as npmonto, (SELECT aumento FROM admppres WHERE "
                             + "(mpre_id='"+pres+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) AND "
                             + "numepart=mp.numero AND mvalu_id<='"+mvalu+"'"
                             + ") as aumcantidad, (SELECT aumento FROM admppres WHERE "
                             + "(mpre_id='"+pres+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) AND "
                             + "numepart=mp.numero AND mvalu_id<='"+mvalu+"'"
                             + ")*IF(mp.precasu=0,mp.precunit,mp.precasu) as aummonto, (SELECT disminucion FROM admppres WHERE "
                             + "(mpre_id='"+pres+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) AND "
                             + "numepart=mp.numero AND mvalu_id<='"+mvalu+"'"
                             + ") as discantidad, (SELECT aumento FROM admppres WHERE "
                             + "(mpre_id='"+pres+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) AND "
                             + "numepart=mp.numero AND mvalu_id<='"+mvalu+"'"
                             + ")* IF(mp.precasu=0,mp.precunit,mp.precasu) as dismonto, mp.cantidad+ (SELECT aumento FROM admppres WHERE "
                             + "(mpre_id='"+pres+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) AND "
                             + "numepart=mp.numero AND mvalu_id<='"+mvalu+"'"
                             + ")-(SELECT disminucion FROM admppres WHERE "
                             + "(mpre_id='"+pres+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) AND "
                             + "numepart=mp.numero AND mvalu_id<='"+mvalu+"'"
                             + ") as cantmodificado, mp.cantidad+ (SELECT aumento FROM admppres WHERE "
                             + "(mpre_id='"+pres+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) AND "
                             + "numepart=mp.numero AND mvalu_id<='"+mvalu+"'"
                             + ")-(SELECT disminucion FROM admppres WHERE "
                             + "(mpre_id='"+pres+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) AND "
                             + "numepart=mp.numero AND mvalu_id<='"+mvalu+"'"
                             + ")*IF(mp.precasu=0,mp.precunit,mp.precasu) as montomodificado, '"+pres+"' FROM mppres as mp "
                             + "WHERE mp.tipo='NP' AND mp.tiponp='OA' AND (mp.mpre_id='"+pres+"' OR mp.mpre_id IN (SELECT id FROM "
                             + "mpres WHERE mpres_id='"+pres+"')))";
                     Statement np=(Statement) conex.createStatement();
                     np.execute(agrega);
                            
                 }
                 //-----------------------------------OC
                 
                String cuentaoc = "SELECT COUNT(*) FROM mppres WHERE tipo='NP' AND tiponp='OC' AND "
                         + "(mpre_id='"+pres+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"'))";
                 Statement coc = (Statement) conex.createStatement();
                 ResultSet rscoc = coc.executeQuery(cuentaoc);
                 int cuentaoco=0;
                 while(rscoc.next()){
                     cuentaoco=rscoc.getInt(1);
                 }
                 if(cuentaoco>0)
                 {
                     String titulos = "INSERT INTO reportecuadrocierre (codigo,descri, mpres) "
                             + "VALUES ('','PARTIDAS OBRAS COMPLEMENTARIAS','"+pres+"')";
                     Statement sts = (Statement) conex.createStatement();
                     sts.execute(titulos);
                     String agrega = "INSERT INTO reportecuadrocierre (nro, codigo, descri,unidad,precio,npcantidad,npmonto,"
                             + "aumcantidad,aummonto,discantidad,dismonto,cantmodificado, montomodificado,mpres)"
                             + " (SELECT mp.numegrup, mp.id, mp.descri, mp.unidad, IF(mp.precasu=0,mp.precunit,mp.precasu) as precio, "
                             + "mp.cantidad,"
                             + "mp.cantidad*IF(mp.precasu=0,mp.precunit,mp.precasu) as npmonto, (SELECT aumento FROM admppres WHERE "
                             + "(mpre_id='"+pres+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) AND "
                             + "numepart=mp.numero AND mvalu_id<='"+mvalu+"'"
                             + ") as aumcantidad, (SELECT aumento FROM admppres WHERE "
                             + "(mpre_id='"+pres+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) AND "
                             + "numepart=mp.numero AND mvalu_id<='"+mvalu+"'"
                             + ")*IF(mp.precasu=0,mp.precunit,mp.precasu) as aummonto, (SELECT disminucion FROM admppres WHERE "
                             + "(mpre_id='"+pres+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) AND "
                             + "numepart=mp.numero AND mvalu_id<='"+mvalu+"'"
                             + ") as discantidad, (SELECT aumento FROM admppres WHERE "
                             + "(mpre_id='"+pres+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) AND "
                             + "numepart=mp.numero AND mvalu_id<='"+mvalu+"'"
                             + ")* IF(mp.precasu=0,mp.precunit,mp.precasu) as dismonto, mp.cantidad+ (SELECT aumento FROM admppres WHERE "
                             + "(mpre_id='"+pres+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) AND "
                             + "numepart=mp.numero AND mvalu_id<='"+mvalu+"'"
                             + ")-(SELECT disminucion FROM admppres WHERE "
                             + "(mpre_id='"+pres+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) AND "
                             + "numepart=mp.numero AND mvalu_id<='"+mvalu+"'"
                             + ") as cantmodificado, mp.cantidad+ (SELECT aumento FROM admppres WHERE "
                             + "(mpre_id='"+pres+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) AND "
                             + "numepart=mp.numero AND mvalu_id<='"+mvalu+"'"
                             + ")-(SELECT disminucion FROM admppres WHERE "
                             + "(mpre_id='"+pres+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) AND "
                             + "numepart=mp.numero AND mvalu_id<='"+mvalu+"'"
                             + ")*IF(mp.precasu=0,mp.precunit,mp.precasu) as montomodificado, '"+pres+"' FROM mppres as mp "
                             + "WHERE mp.tipo='NP' AND mp.tiponp='OC' AND (mp.mpre_id='"+pres+"' OR mp.mpre_id IN (SELECT id FROM "
                             + "mpres WHERE mpres_id='"+pres+"')))";
                     Statement np=(Statement) conex.createStatement();
                     np.execute(agrega);
                            
                 }
                 //------------------------------------VP
                 String cuentavp = "SELECT COUNT(*) FROM mppres WHERE tipo='VP' AND "
                         + "(mpre_id='"+pres+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"'))";
                 Statement cvp = (Statement) conex.createStatement();
                 ResultSet rscvp = cvp.executeQuery(cuentavp);
                 int cuentavps=0;
                 while(rscvp.next()){
                     cuentavps=rscvp.getInt(1);
                 }
                 if(cuentavps>0)
                 {
                     String titulos = "INSERT INTO reportecuadrocierre (codigo,descri, mpres) "
                             + "VALUES ('\n','RECONSIDERACIONES DE PRECIOS','"+pres+"')";
                     Statement sts = (Statement) conex.createStatement();
                     sts.execute(titulos);
                     String agrega = "INSERT INTO reportecuadrocierre (nro, codigo, descri,unidad,precio, vpcantidad, vpmonto,"
                             + "aumcantidad,aummonto,discantidad,dismonto,cantmodificado, montomodificado,mpres)"
                             + " (SELECT mp.numegrup, mp.id, mp.descri, mp.unidad, "
                             + "IF(IF(mp.precasu=0,mp.precunit,mp.precasu)-(SELECT IF(precasu=0,precunit, precasu) FROM "
                             + "mppres WHERE (mpre_id='"+pres+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"'))"
                             + " AND numero=mp.mppre_id)<0,0,IF(mp.precasu=0,mp.precunit,mp.precasu)-(SELECT IF(precasu=0,precunit, precasu) "
                             + "FROM "
                             + "mppres WHERE (mpre_id='"+pres+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"'))"
                             + " AND numero=mp.mppre_id)) "
                             + "as precio, "
                             + "mp.cantidad,"
                             + "mp.cantidad*IF((IF(mp.precasu=0,mp.precunit,mp.precasu)-(SELECT IF(precasu=0,precunit, precasu) FROM "
                             + "mppres WHERE (mpre_id='"+pres+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"'))"
                             + " AND numero=mp.mppre_id))<0,0,mp.cantidad*(IF(mp.precasu=0,mp.precunit,mp.precasu)-(SELECT "
                             + "IF(precasu=0,precunit, precasu)"
                             + " FROM "
                             + "mppres WHERE (mpre_id='"+pres+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"'))"
                             + " AND numero=mp.mppre_id))) as vpmonto, "
                             + "(SELECT aumento FROM admppres WHERE "
                             + "(mpre_id='"+pres+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) AND "
                             + "numepart=mp.numero AND mvalu_id<='"+mvalu+"'"
                             + ") as aumcantidad, (SELECT aumento FROM admppres WHERE "
                             + "(mpre_id='"+pres+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) AND "
                             + "numepart=mp.numero AND mvalu_id<='"+mvalu+"'"
                             + ")*IF(mp.precasu=0,mp.precunit,mp.precasu)-(SELECT IF(precasu=0,precunit, precasu) FROM "
                             + "mppres WHERE (mpre_id='"+pres+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"'))"
                             + " AND numero=mp.mppre_id)  as aummonto, (SELECT disminucion FROM admppres WHERE "
                             + "(mpre_id='"+pres+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) AND "
                             + "numepart=mp.numero AND mvalu_id<='"+mvalu+"'"
                             + ") as discantidad, (SELECT aumento FROM admppres WHERE "
                             + "(mpre_id='"+pres+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) AND "
                             + "numepart=mp.numero AND mvalu_id<='"+mvalu+"'"
                             + ")* IF(mp.precasu=0,mp.precunit,mp.precasu)-(SELECT IF(precasu=0,precunit, precasu) FROM "
                             + "mppres WHERE (mpre_id='"+pres+"' OR mpre_id IN (SELECT id FROM mpres where mpres_id='"+pres+"'))"
                             + " AND numero=mp.mppre_id)  as dismonto, mp.cantidad+ (SELECT aumento FROM admppres WHERE "
                             + "(mpre_id='"+pres+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) AND "
                             + "numepart=mp.numero AND mvalu_id<='"+mvalu+"'"
                             + ")-(SELECT disminucion FROM admppres WHERE "
                             + "(mpre_id='"+pres+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) AND "
                             + "numepart=mp.numero AND mvalu_id<='"+mvalu+"'"
                             + ") as cantmodificado, mp.cantidad+ (SELECT aumento FROM admppres WHERE "
                             + "(mpre_id='"+pres+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) AND "
                             + "numepart=mp.numero AND mvalu_id<='"+mvalu+"'"
                             + ")-(SELECT disminucion FROM admppres WHERE "
                             + "(mpre_id='"+pres+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) AND "
                             + "numepart=mp.numero "
                             + ")*IF(mp.precasu=0,mp.precunit,mp.precasu)-(SELECT IF(precasu=0,precunit, precasu) FROM "
                             + "mppres WHERE (mpre_id='"+pres+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"'))"
                             + " AND numero=mp.mppre_id)  as montomodificado, '"+pres+"' FROM mppres as mp "
                             + "WHERE mp.tipo='VP' AND (mp.mpre_id='"+pres+"' OR mp.mpre_id IN (SELECT id FROM "
                             + "mpres WHERE mpres_id='"+pres+"')))";
                     System.out.println("VP "+agrega);
                     Statement np=(Statement) conex.createStatement();
                     np.execute(agrega);
                            
                 }
                 
            } catch (SQLException ex) {
                Logger.getLogger(reportecuadravance.class.getName()).log(Level.SEVERE, null, ex);
            }
                 
             JasperDesign design; 
            try {
                design = JRXmlLoader.load(input);
                 JasperReport report = JasperCompileManager.compileReport(design);
                 if(jCheckBox1.isSelected()){
                   fecha = format.format(jDateChooser1.getDate());
                   titulo = titulo+" "+fecha; 
                 }else{
                     fecha="";
                 }
                 
                  parameters.put("fecha", fecha);
                  parameters.put("titulo",titulo);
                  parameters.put("mpres",pres);
                  print = JasperFillManager.fillReport(report, parameters, conex);
                    JasperViewer.viewReport(print, false);
            } catch (JRException ex) {
                Logger.getLogger(reportecuadravance.class.getName()).log(Level.SEVERE, null, ex);
            }
           
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(reportecuadravance.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        generarreporte();
        
        doClose(RET_OK);
    }//GEN-LAST:event_okButtonActionPerformed
    
    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        doClose(RET_CANCEL);
    }//GEN-LAST:event_cancelButtonActionPerformed

    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        doClose(RET_CANCEL);
    }//GEN-LAST:event_closeDialog

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed
    
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
    private javax.swing.JCheckBox jCheckBox1;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JButton okButton;
    // End of variables declaration//GEN-END:variables
    private int returnStatus = RET_CANCEL;
}
