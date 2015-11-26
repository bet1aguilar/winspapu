/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package valuaciones;

import com.mysql.jdbc.ResultSetMetaData;
import com.mysql.jdbc.Statement;
import config.Redondeo;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.sql.Connection;
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
import javax.swing.SpinnerListModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import winspapus.Principal;
import presupuestos.Partida;

/**
 *
 * @author spapu1
 */
public class aumentosdismi extends javax.swing.JDialog {

    /**
     * A return status code - returned if Cancel button has been pressed
     */
    public static final int RET_CANCEL = 0;
    /**
     * A return status code - returned if OK button has been pressed
     */
    Redondeo redondear = new Redondeo();
    int cambie = 0;
    public static final int RET_OK = 1;
    String pres;
    String [] valus;
    String valua;
    private Connection conex;
    private String[] partidas, codigo;
    private String numero1;
    private Integer entero;
   int filapart=0;
   BigDecimal totalpres;
   Principal p; 
    public aumentosdismi(java.awt.Frame parent, boolean modal, String pres, Connection conex, BigDecimal total) 
    {
        super(parent, false);
        initComponents();
        this.pres = pres;
        this.totalpres = total;
        jTextField19.setText(String.valueOf(total));
        
        p = (Principal) parent;
        this.conex = conex;
        try {
            contar();
            cargapres();
            modelonumepart();
            cargavalu();
            modelovalu();
            buscapartida();
            cargavalores();
            jTable1.setOpaque(true);
            jTable1.setShowHorizontalLines(true);
            jTable1.setShowVerticalLines(false);
            jTable1.getTableHeader().setSize(new Dimension(25,40));
            jTable1.getTableHeader().setPreferredSize(new Dimension(30,40));
            jTable1.setRowHeight(25);
            Font fuente = new Font("Tahoma",Font.PLAIN, 10);
            jTable1.setFont(fuente);
        } catch (SQLException ex) {
            Logger.getLogger(aumentosdismi.class.getName()).log(Level.SEVERE, null, ex);
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
    public final void modelovalu(){
        String consulta = "SELECT id FROM mvalus WHERE mpre_id='"+pres+"' AND status=1";
        String cuenta = "SELECT COUNT(*) FROM mvalus WHERE mpre_id='"+pres+"' AND status=1";
        int pos=0;
        try {
            Statement stcuenta = (Statement) conex.createStatement();
            ResultSet rstcuenta = stcuenta.executeQuery(cuenta);
            while(rstcuenta.next()){
                pos = rstcuenta.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(aumentosdismi.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        String[] valuaciones=new String[pos];
        
        int i=0;
        try {
            Statement st = (Statement) conex.createStatement();
            ResultSet rst = st.executeQuery(consulta);
            
            while(rst.next()){
                valuaciones[i]=rst.getString(1);
                i++;
            }
        } catch (SQLException ex) {
            Logger.getLogger(aumentosdismi.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        SpinnerListModel modelo;
        if(i>0){
            modelo = new SpinnerListModel(valuaciones);
              jSpinner4.setModel(modelo);
              jButton2.setEnabled(true);
             cambiapartida();
        }else{
            jSpinner4.setEnabled(false);
            jButton2.setEnabled(false);
            jLabel32.setText("");
            jLabel6.setText("");
        }
         
            
          
    }
    public final void cargavalores()
    {
        BigDecimal aumentos=new BigDecimal("0.00"), disminucion=new BigDecimal("0.00"), np=new BigDecimal("0.00");
        try {
            BigDecimal precunit=new BigDecimal("0.00"), precasu=new BigDecimal("0.00");
            //Falta agregarle los precios de las valuaciones restar el delta de 
            //la reconsideracion, multiplicar por el nuevo precio la cantidad del delta y por el precio viejo el restante
             String aumento = "SELECT IFNULL(SUM(ROUND(ROUND(a.aumento,2)*IF(mp.precasu = 0,mp.precunit,mp.precasu),2)),0) FROM mppres as mp,"
                     + " admppres as a "
                     + "WHERE a.mpre_id='"+pres+"' AND "
                     + "(mp.mpre_id ='"+pres+"' OR mp.mpre_id IN (SELECT id FROM mpres WHERE mpres_id=a.mpre_id)) AND a.numepart = mp.numero "
                     + "AND a.aumento>0";
            System.out.println("aumento "+aumento);
            Statement sts = (Statement) conex.createStatement();
            ResultSet rsts = sts.executeQuery(aumento);
            while(rsts.next())
            {
                aumentos=rsts.getBigDecimal(1);
            }
            NumberFormat formatoNumero = NumberFormat.getNumberInstance();
            formatoNumero.setMaximumFractionDigits(2);
            formatoNumero.setMinimumFractionDigits(2);
            jTextField19.setText(String.valueOf(formatoNumero.format(totalpres)));
            jTextField18.setText(String.valueOf(formatoNumero.format(aumentos)));
     
            
            //******************VP*******************************
            
            String vp = "SELECT IFNULL(SUM(ROUND((IF(mp.precasu=0,mp.precunit, mp.precasu)-(SELECT IF(m.precasu=0,m.precunit,m.precasu)"
                    + " FROM mppres as m WHERE m.numero=mp.mppre_id AND (m.mpre_id='"+pres+"' OR m.mpre_id IN "
                    + "(SELECT id FROM mpres WHERE mpres_id='"+pres+"'))))*mp.cantidad,2)),0) FROM mppres as mp WHERE "
                        + "(mp.mpre_id='"+pres+"' OR mp.mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) "
                    + "AND mp.tipo='VP'";
            System.out.println(vp);
            Statement st = (Statement) conex.createStatement();
            ResultSet rst = st.executeQuery(vp);
            BigDecimal sumtotal=new BigDecimal("0.00");
            while(rst.next()){
                sumtotal = rst.getBigDecimal(1);
                
            }
            if(sumtotal.doubleValue()<new BigDecimal("0.00").doubleValue())
                sumtotal=new BigDecimal("0.00");
            
            jTextField22.setText(String.valueOf(formatoNumero.format(sumtotal)));
           
            //---------------PNP
            String pnp="SELECT  IFNULL(SUM(ROUND(IFNULL((IF(precasu = 0, precunit, precasu)),0) *IFNULL((cantidad), 0),2)),0)  FROM "
                          + "mppres WHERE tipo='NP' AND tiponp='NP' "
                          + "AND (mpre_id='"+pres+"' OR mpre_id IN (SELECT id FROM"
                          + " mpres WHERE mpres_id='"+pres+"')) ";
            Statement stpnp = (Statement) conex.createStatement();
            ResultSet rstpnp = stpnp.executeQuery(pnp);
            while(rstpnp.next()){
                np=rstpnp.getBigDecimal(1);
            }
            jTextField17.setText(String.valueOf(formatoNumero.format(np.doubleValue())));    
            BigDecimal totaleste = totalpres.add(np).add(aumentos).add(sumtotal); //sumtotal es vp
            //----------------------DISMI
            
            String dismi = "SELECT IFNULL(SUM(ROUND(ROUND(a.disminucion,2)*IF(mp.precasu = 0,mp.precunit,mp.precasu),2)),0) FROM mppres as mp,"
                         + " admppres as a "
                         + "WHERE a.mpre_id='"+pres+"' AND "
                         + "(mp.mpre_id ='"+pres+"' OR mp.mpre_id IN (SELECT id FROM mpres WHERE mpres_id=a.mpre_id)) AND a.numepart = mp.numero AND "
                    + "a.disminucion>0";
            Statement sdismi = (Statement) conex.createStatement();
            
            ResultSet rsdismi = sdismi.executeQuery(dismi);
            while(rsdismi.next()){
                
                disminucion = rsdismi.getBigDecimal(1);
            }
            jTextField16.setText(String.valueOf(formatoNumero.format(disminucion)));
            BigDecimal totalesto = totaleste.subtract(disminucion);
            BigDecimal impuesto=new BigDecimal("0.00");
            String consulpres = "SELECT porimp FROM mpres WHERE id='"+pres+"'";
            Statement sconsulpres = (Statement) conex.createStatement();
            ResultSet rsconsulpres = sconsulpres.executeQuery(consulpres);
            while(rsconsulpres.next())
                impuesto = rsconsulpres.getBigDecimal(1);
            
            BigDecimal impeste = redondear.redondearDosDecimales(totalesto.multiply(impuesto.divide(new BigDecimal("100"))));
            BigDecimal imporig = redondear.redondearDosDecimales(totalpres.multiply(impuesto.divide(new BigDecimal("100"))));
            BigDecimal difimp = impeste.subtract(imporig);
            jTextField15.setText(String.valueOf(formatoNumero.format(difimp)));
            BigDecimal modificado = totalesto;
            jTextField14.setText(String.valueOf(formatoNumero.format(modificado)));
            BigDecimal diferencia = modificado.subtract(totalpres);
            jTextField13.setText(String.valueOf(formatoNumero.format(diferencia)));
        } catch (SQLException ex) {
            Logger.getLogger(aumentosdismi.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    public final void cargapres()
    {
        String id= jSpinner3.getValue().toString();
        int cont=0;
        String sql = "SELECT DATE_FORMAT(mp.fecini,'%d/%m/%y'),mp.nombre, cn.nombre FROM "
                + "mpres as mp, mconts as cn WHERE mp.id='"+pres+"' AND mp.codcon=cn.id";
        try {
            Statement st = (Statement) conex.createStatement();
            ResultSet rst = st.executeQuery(sql);
            while(rst.next()){
                jTextField1.setText(pres);
                jTextField2.setText(rst.getObject(1).toString());
                jTextField3.setText(rst.getObject(3).toString());
                jTextArea1.setText(rst.getObject(2).toString());
            }
            
            String sqls = "SELECT count(*) FROM pays WHERE id="+id+" AND (mpre_id='"+pres+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"'))";
            Statement str = (Statement) conex.createStatement();
            ResultSet rstr = str.executeQuery(sqls);
            while(rstr.next()){
                cont = rstr.getInt(1);
            }
            if(cont==0){
                String inserta = "INSERT INTO pays (id) VALUES ("+id+")";
            }
        } catch (SQLException ex) {
            Logger.getLogger(aumentosdismi.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
    }
    private void contar() throws SQLException
    {
        Statement cuenta = (Statement) conex.createStatement();
        ResultSet result = cuenta.executeQuery("Select count(*) FROM mppres WHERE mpre_id='"+pres+"' OR mpre_id IN (SELECT id from mpres where mpres_id ='"+pres+"' GROUP BY id)");
       
        while(result.next()){
            numero1 = result.getObject(1).toString();
            
        }
        System.out.println(numero1);
        entero = Integer.valueOf(numero1);
        entero++;
        numero1 = Integer.toString(entero);
       
       
      
           partidas = new String[entero];
           codigo = new String[entero];
    }
    public final void modelonumepart()
    {
        int i=0;
        String sql = "SELECT numero, id from mppres WHERE mpre_id='"+pres+"' OR mpre_id"
                + " IN (SELECT id from mpres where mpres_id ='"+pres+"' GROUP BY id) ORDER BY numero " ;
        try {
            Statement st = (Statement) conex.createStatement();
            ResultSet rst = st.executeQuery(sql);
            while(rst.next()){
                partidas[i] = rst.getObject(1).toString();
                codigo[i]= rst.getObject(2).toString();
                i++;
            }
            SpinnerListModel modelo = new SpinnerListModel(partidas);
            
            jSpinner1.setModel(modelo);
            
            
        } catch (SQLException ex) {
            Logger.getLogger(Partida.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public final void cargavalu(){
         int i=0;
        String sql = "SELECT numero, id from mppres WHERE mpre_id='"+pres+"' "
                + "OR mpre_id IN (SELECT id from mpres where mpres_id ='"+pres+"' GROUP BY id) ORDER BY numegrup " ;
        try {
            Statement st = (Statement) conex.createStatement();
            ResultSet rst = st.executeQuery(sql);
            while(rst.next()){
                partidas[i] = rst.getObject(1).toString();
                codigo[i]= rst.getObject(2).toString();
                i++;
            }
            SpinnerListModel modelo = new SpinnerListModel(partidas);
            
            jSpinner1.setModel(modelo);
            
            
        } catch (SQLException ex) {
            Logger.getLogger(Partida.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public int getReturnStatus() {
        return returnStatus;
    }

 
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jScrollPane4 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jTextField11 = new javax.swing.JTextField();
        jTextField12 = new javax.swing.JTextField();
        jTextField20 = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jTextField21 = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jTextField3 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jSpinner4 = new javax.swing.JSpinner();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel11 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jLabel32 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jSpinner1 = new javax.swing.JSpinner();
        jTextField1 = new javax.swing.JTextField();
        jSpinner3 = new javax.swing.JSpinner();
        jLabel6 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jTextField13 = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jTextField14 = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        jTextField15 = new javax.swing.JTextField();
        jTextField16 = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jTextField17 = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        jTextField18 = new javax.swing.JTextField();
        jTextField19 = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jTextField22 = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        jTextField8 = new javax.swing.JTextField();
        jTextField9 = new javax.swing.JTextField();
        jTextField10 = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jTextField6 = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        jScrollPane4.setPreferredSize(new java.awt.Dimension(828, 650));

        jPanel1.setName(""); // NOI18N
        jPanel1.setPreferredSize(new java.awt.Dimension(850, 670));
        jPanel1.setRequestFocusEnabled(false);

        jPanel4.setBackground(new java.awt.Color(100, 100, 100));

        jLabel1.setBackground(new java.awt.Color(91, 91, 95));
        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Aumentos y Disminuciones");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 860, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        okButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/cambiar.fw.png"))); // NOI18N
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

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/listar2.fw.png"))); // NOI18N
        jButton4.setToolTipText("Valuaciones");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/quita.fw.png"))); // NOI18N
        jButton3.setToolTipText("Partidas a Disminución Total que no estan valuadas");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/disminuye.fw.png"))); // NOI18N
        jButton5.setToolTipText("Partidas a Disminución Parcial que no estan valuadas");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/obra.png"))); // NOI18N
        jButton6.setToolTipText("Ver Presupuesto");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/informacion.fw.png"))); // NOI18N
        jButton7.setToolTipText("Información de la partida");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/memo.fw.png"))); // NOI18N
        jButton8.setToolTipText("Vista Previa del Presupuesto");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/borra.png"))); // NOI18N
        jButton9.setToolTipText("Borrar Partida");
        jButton9.setEnabled(false);
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/anade.fw.png"))); // NOI18N
        jButton1.setToolTipText("Añadir Partidas en Aumento");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(cancelButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(okButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        getRootPane().setDefaultButton(okButton);

        jPanel7.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.darkGray, java.awt.Color.gray, java.awt.Color.gray, java.awt.Color.darkGray));
        jPanel7.setToolTipText("Aumentos y Disminuciones para la partida en todos los presupuestos");

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel20.setText("Total Partida:");

        jLabel21.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel21.setText("Aumento:");

        jTextField11.setEditable(false);
        jTextField11.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField11.setText("0.00");

        jTextField12.setEditable(false);
        jTextField12.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField12.setText("0.00");

        jTextField20.setEditable(false);
        jTextField20.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField20.setText("0.00");

        jLabel30.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel30.setText("Total:");

        jLabel31.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel31.setText("Disminución:");

        jTextField21.setEditable(false);
        jTextField21.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField21.setText("0.00");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel21)
                    .addComponent(jLabel20))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField12, javax.swing.GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE)
                    .addComponent(jTextField11, javax.swing.GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE))
                .addGap(53, 53, 53)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel31)
                    .addComponent(jLabel30))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField20, javax.swing.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE)
                    .addComponent(jTextField21, javax.swing.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel31)
                            .addComponent(jTextField21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel30)))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel20)
                            .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel21)
                            .addComponent(jTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(18, Short.MAX_VALUE))
        );

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
        jScrollPane3.setViewportView(jTable1);

        jPanel5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.gray, java.awt.Color.lightGray, java.awt.Color.black, java.awt.Color.darkGray));

        jTextField3.setEditable(false);
        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });

        jTextField2.setEditable(false);

        jTextField4.setEditable(false);

        jLabel5.setText("Nombre:");

        jLabel4.setText("Contratista:");

        jSpinner4.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));
        jSpinner4.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner4StateChanged(evt);
            }
        });
        jSpinner4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jSpinner4KeyTyped(evt);
            }
        });

        jScrollPane1.setEnabled(false);

        jTextArea1.setColumns(20);
        jTextArea1.setEditable(false);
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(1);
        jTextArea1.setWrapStyleWord(true);
        jScrollPane1.setViewportView(jTextArea1);

        jLabel11.setText("Valuación:");

        jLabel3.setText("Fecha Inicio:");

        jLabel2.setText("Obra Nro.:");

        jLabel8.setText("Nro. de Partida:");

        jTextArea2.setColumns(20);
        jTextArea2.setEditable(false);
        jTextArea2.setLineWrap(true);
        jTextArea2.setRows(2);
        jTextArea2.setWrapStyleWord(true);
        jTextArea2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextArea2KeyPressed(evt);
            }
        });
        jScrollPane2.setViewportView(jTextArea2);

        jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jLabel7.setText("Presp. Nro.:");

        jLabel9.setText("Código de Partida:");

        jLabel10.setText("Descripción:");

        jSpinner1.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(1), null, null, Integer.valueOf(1)));
        jSpinner1.setEnabled(false);
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

        jTextField1.setEditable(false);
        jTextField1.setToolTipText("Ingrese Código de la Partida");

        jSpinner3.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(1)));
        jSpinner3.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner3StateChanged(evt);
            }
        });
        jSpinner3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jSpinner3KeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(178, 178, 178)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSpinner4, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(120, 120, 120))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel8Layout.createSequentialGroup()
                                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel3)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel4)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 486, Short.MAX_VALUE)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 486, Short.MAX_VALUE)))
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel8Layout.createSequentialGroup()
                                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 333, Short.MAX_VALUE))
                                    .addGroup(jPanel8Layout.createSequentialGroup()
                                        .addComponent(jSpinner3, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 429, Short.MAX_VALUE)))))))
                .addContainerGap())
            .addComponent(jLabel32, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 595, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel2)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel7)
                    .addComponent(jLabel11)
                    .addComponent(jSpinner4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSpinner3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 609, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE))
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.darkGray, java.awt.Color.gray, java.awt.Color.lightGray, java.awt.Color.darkGray));

        jTextField13.setEditable(false);
        jTextField13.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField13.setText("0.00");

        jLabel22.setText("Diferencia:");

        jLabel23.setText("Modificado:");

        jTextField14.setEditable(false);
        jTextField14.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel24.setText("Dif. Imp.:");

        jTextField15.setEditable(false);
        jTextField15.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jTextField16.setEditable(false);
        jTextField16.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel25.setText("Disminuciones:");

        jLabel26.setText("Extras:");

        jTextField17.setEditable(false);
        jTextField17.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel27.setText("Aumentos");

        jTextField18.setEditable(false);
        jTextField18.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jTextField19.setEditable(false);
        jTextField19.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel28.setText("Original:");

        jLabel29.setText("VP:");

        jTextField22.setEditable(false);
        jTextField22.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel25)
                    .addComponent(jLabel22)
                    .addComponent(jLabel23)
                    .addComponent(jLabel24)
                    .addComponent(jLabel26)
                    .addComponent(jLabel29)
                    .addComponent(jLabel27)
                    .addComponent(jLabel28))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField18, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE)
                    .addComponent(jTextField19, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE)
                    .addComponent(jTextField22, javax.swing.GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE)
                    .addComponent(jTextField17, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE)
                    .addComponent(jTextField16, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE)
                    .addComponent(jTextField15, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE)
                    .addComponent(jTextField14, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE)
                    .addComponent(jTextField13, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel28))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel27))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel29))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel26))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel25))
                .addGap(12, 12, 12)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel24))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22)))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.gray, java.awt.Color.lightGray, java.awt.Color.darkGray, java.awt.Color.darkGray));
        jPanel3.setPreferredSize(new java.awt.Dimension(228, 239));

        jLabel15.setText("Cantidad:");

        jLabel16.setText("Precio:");

        jLabel17.setText("Unidad:");

        jLabel18.setText("Tipo:");

        jTextField7.setEditable(false);
        jTextField7.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField7.setEnabled(false);
        jTextField7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField7ActionPerformed(evt);
            }
        });

        jTextField8.setEditable(false);
        jTextField8.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField8.setEnabled(false);
        jTextField8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField8ActionPerformed(evt);
            }
        });

        jTextField9.setEditable(false);
        jTextField9.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField9.setEnabled(false);
        jTextField9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField9ActionPerformed(evt);
            }
        });

        jTextField10.setEditable(false);
        jTextField10.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField10.setEnabled(false);
        jTextField10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField10ActionPerformed(evt);
            }
        });

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setText("Información de Partida");

        jButton2.setText("Insertar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel13.setText("Aumento:");

        jTextField5.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField5.setText("0.00");
        jTextField5.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField5FocusLost(evt);
            }
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField5FocusGained(evt);
            }
        });

        jTextField6.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField6.setText("0.00");
        jTextField6.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField6FocusLost(evt);
            }
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField6FocusGained(evt);
            }
        });

        jLabel14.setText("Disminución:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButton2))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14)
                            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE)
                            .addComponent(jLabel18)
                            .addComponent(jLabel17)
                            .addComponent(jLabel16)
                            .addComponent(jLabel15))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jTextField10, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)
                            .addComponent(jTextField6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)
                            .addComponent(jTextField5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)
                            .addComponent(jTextField8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)
                            .addComponent(jTextField7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)
                            .addComponent(jTextField9, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 225, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(354, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 516, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(3, 3, 3))
                    .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(116, 116, 116))
        );

        jScrollPane4.setViewportView(jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 872, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 581, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        doClose(RET_CANCEL);
    }//GEN-LAST:event_cancelButtonActionPerformed

    /**
     * Closes the dialog
     */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        doClose(RET_CANCEL);
    }//GEN-LAST:event_closeDialog

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField3ActionPerformed
public void buscarpartida1(String part){
       int i=0;
       BigDecimal cantidad, precio;
      
           
        try {
            String partida = part;
            String cuenta = "SELECT count(*) FROM dvalus as dv, mppres as mp "
                    + "WHERE mp.numegrup="+partida+" AND mp.id = dv.mppre_id";
                int cuentan=0;
             Statement stt = (Statement) conex.createStatement();
             ResultSet rstt = stt.executeQuery(cuenta);
             while(rstt.next()){
                 cuentan = Integer.parseInt(rstt.getObject(1).toString());
             }
             valus = new String[cuentan];
            
           
            
           String sql = "SELECT mp.id, mp.descri , mp.cantidad, mp.precunit,"
                   + "mp.unidad, mp.tipo FROM mppres as mp WHERE "
                   + "mp.numegrup="+partida+" AND"
                   + " (mp.mpre_id='"+pres+"' OR mp.mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"'))"
                   + "";
           String nume = partida;
            try {
                Statement st = (Statement) conex.createStatement();
                ResultSet rst = st.executeQuery(sql);
                while(rst.next())
                {
                    partida = rst.getObject(1).toString();
                    jTextField4.setText(rst.getObject(1).toString());
                    jTextArea2.setText(rst.getObject(2).toString());   
                    jTextField7.setText(rst.getBigDecimal("mp.cantidad").toString());
                   jTextField8.setText(rst.getBigDecimal("mp.precunit").toString());
                   jTextField9.setText(rst.getObject("mp.unidad").toString());
                   jTextField10.setText(rst.getObject("mp.tipo").toString());
                   
                }
                BigDecimal aumento= new BigDecimal("0.00"), dismi= new BigDecimal("0.00"), totalaum, totaldismi, total, totalpart;
                String num = "SELECT numero FROM mppres WHERE numegrup='"+nume+"' AND (mpre_id='"+pres+"' "
                        + "OR mpre_id IN (SELECT id"
                        + " FROM mpres WHERE mpres_id='"+pres+"'))";
                Statement str = (Statement) conex.createStatement();
                String numero=null;
                ResultSet rstr = str.executeQuery(num);
                while(rstr.next()){
                    numero = rstr.getString(1);
                }                
                String consultaumento = "SELECT ROUND(SUM(aumento),2), ROUND(SUM(disminucion),2) FROM admppres "
                        + "WHERE payd_id="+jSpinner3.getValue()+" "
                        + "AND numepart="+numero+" AND mpre_id='"+pres+"' OR mpre_id IN (SELECT id FROM "
                        + "mpres WHERE mpres_id='"+pres+"')";
                Statement con = (Statement) conex.createStatement();
                ResultSet rcon = con.executeQuery(consultaumento);
                while(rcon.next())
                {
                    aumento = rcon.getBigDecimal(1);
                    dismi = rcon.getBigDecimal(2);
                }
                jLabel6.setText("");
                BigDecimal acumaumento = new BigDecimal("0.00"), acumdismi= new BigDecimal("0.00");
                String consultacum = "SELECT IFNULL(ROUND(SUM(aumento),2),0), IFNULL(ROUND(SUM(disminucion),2),0) FROM admppres WHERE "
                        + "numepart="+numero+" AND (mpre_id='"+pres+"' OR mpre_id IN "
                        + "(SELECT id FROM mpres WHERE mpres_id='"+pres+"'))";
                Statement stacum = (Statement) conex.createStatement();
                ResultSet rstacum = stacum.executeQuery(consultacum);
                while(rstacum.next()){
                    acumaumento = rstacum.getBigDecimal(1);
                    acumdismi = rstacum.getBigDecimal(2);
                }
                cantidad = new BigDecimal(jTextField7.getText().toString());
                BigDecimal cantvaluado = new BigDecimal("0.00");
                String valuado = "SELECT IFNULL(SUM(cantidad),0) FROM dvalus WHERE  (mpre_id='"+pres+"' "
                        + "OR mpre_id IN (SELECT id FROM mpres "
                        + "WHERE "
                        + "mpres_id='"+pres+"')) AND numepart="+numero+"";
                Statement stvaluado = (Statement) conex.createStatement();
                ResultSet rstvaluado = stvaluado.executeQuery(valuado);
                while(rstvaluado.next()){
                    cantvaluado=rstvaluado.getBigDecimal(1);
                }
                 BigDecimal diferencia = cantvaluado.subtract(cantidad);
                if(diferencia.doubleValue()>0){
                   
                    diferencia = redondear.redondearDosDecimales(diferencia.subtract(acumaumento));
                    if(diferencia.doubleValue()>0){
                        jLabel32.setText("La partida numero "+part+" Tiene "+diferencia+" unidades para aumentar");
                    }else{
                        jLabel32.setText("La partida numero "+part+" No tiene unidades para aumentar");
                    }
                }else{
                    diferencia = redondear.redondearDosDecimales(diferencia.multiply(new BigDecimal(-1)));
                    diferencia = diferencia.subtract(acumdismi);
                    if(diferencia.doubleValue()>0){
                        jLabel32.setText("La partida numero "+part+" Tiene "+diferencia+" unidades para disminuir");
                    }else{
                        jLabel32.setText("La partida numero "+part+" No tiene unidades para disminuir");
                    }
                }
                precio = new BigDecimal(jTextField8.getText().toString());
                totalpart = redondear.redondearDosDecimales(cantidad.multiply(precio));
                jTextField11.setText(String.valueOf(totalpart));  
                totalaum = redondear.redondearDosDecimales(aumento.multiply(precio));
                jTextField12.setText(String.valueOf(totalaum));  
                totaldismi = redondear.redondearDosDecimales(dismi.multiply(precio));
                jTextField21.setText(String.valueOf(totaldismi));  
                total = totalpart.add(totalaum).subtract(totaldismi);
                jTextField20.setText(String.valueOf(total)); 
                cambie=0;
            } catch (SQLException ex) {
                Logger.getLogger(aumentosdismi.class.getName()).log(Level.SEVERE, null, ex);
            }
          
           
           
            
        } catch (SQLException ex) {
            Logger.getLogger(aumentosdismi.class.getName()).log(Level.SEVERE, null, ex);
        }
      
       
       
        
    }
    public void buscarpartida(String part)
    {
        jLabel6.setText("");
        jTextField5.setText("0.00");
        
       BigDecimal cantidad, precio;
       if(cambie==0){
           cambie=1;
        try {
            String partida = part;
            String cuenta = "SELECT IFNULL(count(*),0) FROM dvalus as dv, mppres as mp WHERE mp.numegrup="+partida+""
                    + " AND mp.id = dv.mppre_id";
                int cuentan=0;
             Statement stt = (Statement) conex.createStatement();
             ResultSet rstt = stt.executeQuery(cuenta);
             while(rstt.next())
                 cuentan = Integer.parseInt(rstt.getObject(1).toString());
             
             valus = new String[cuentan];
           String sql = "SELECT mp.id, mp.descri, mp.cantidad, IF(mp.precasu=0,mp.precunit,mp.precasu) as precunit, "
                   + "mp.unidad, mp.tipo FROM mppres as mp WHERE "
                   + "mp.numegrup="+partida+" AND"
                   + " (mp.mpre_id='"+pres+"' OR mp.mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"'))"
                   + "";
           String nume = partida;
            try {
                Statement st = (Statement) conex.createStatement();
                ResultSet rst = st.executeQuery(sql);
                while(rst.next())
                {
                    partida = rst.getObject(1).toString();
                    jTextField4.setText(rst.getObject(1).toString());
                    jTextArea2.setText(rst.getObject(2).toString());   
                    jTextField7.setText(rst.getBigDecimal("mp.cantidad").toString());
                   jTextField8.setText(String.valueOf(rst.getBigDecimal("precunit")));
                   jTextField9.setText(rst.getObject("mp.unidad").toString());
                   jTextField10.setText(rst.getObject("mp.tipo").toString());
                   
                }
                BigDecimal aumento= new BigDecimal("0.00"), dismi= new BigDecimal("0.00"), totalaum, totaldismi, total, totalpart;
                String num = "SELECT numero FROM mppres WHERE numegrup='"+nume+"' AND "
                        + "(mpre_id='"+pres+"' OR mpre_id IN (SELECT id"
                        + " FROM mpres WHERE mpres_id='"+pres+"'))";
                Statement str = (Statement) conex.createStatement();
                String numero=null;
                ResultSet rstr = str.executeQuery(num);
                while(rstr.next())
                    numero = rstr.getString(1);
                                
                String consultaumento = "SELECT IFNULL(SUM(aumento),0), IFNULL(SUM(disminucion),0) FROM admppres "
                        + "WHERE numepart="+numero+" AND mpre_id='"+pres+"' OR mpre_id IN "
                        + "(SELECT id FROM mpres WHERE mpres_id='"+pres+"')";
                Statement con = (Statement) conex.createStatement();
                ResultSet rcon = con.executeQuery(consultaumento);
                while(rcon.next()){
                    aumento = rcon.getBigDecimal(1);
                    dismi = rcon.getBigDecimal(2);
                }
                BigDecimal acumaumento = new BigDecimal("0.00"), acumdismi= new BigDecimal("0.00");
                String consultacum = "SELECT IFNULL(SUM(aumento),0), IFNULL(SUM(disminucion),0) FROM admppres WHERE "
                        + "numepart="+numero+" AND mpre_id='"+pres+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"')";
                Statement stacum = (Statement) conex.createStatement();
                ResultSet rstacum = stacum.executeQuery(consultacum);
                while(rstacum.next()){
                    acumaumento = rstacum.getBigDecimal(1);
                    acumdismi = rstacum.getBigDecimal(2);
                }               
                cantidad = new BigDecimal(jTextField7.getText().toString());
                BigDecimal cantvaluado = new BigDecimal("0.00");
                String valuado = "SELECT IFNULL(SUM(cantidad),0) FROM dvalus WHERE  "
                        + "(mpre_id='"+pres+"' OR mpre_id IN (SELECT id FROM mpres "
                        + "WHERE "
                        + "mpres_id='"+pres+"')) AND numepart="+numero+"";
                Statement stvaluado = (Statement) conex.createStatement();
                ResultSet rstvaluado = stvaluado.executeQuery(valuado);
                while(rstvaluado.next()){
                    cantvaluado=rstvaluado.getBigDecimal(1);
                }
                 BigDecimal diferencia = redondear.redondearDosDecimales(cantvaluado.subtract(cantidad));
                if(diferencia.doubleValue()>0){
                   
                    diferencia = redondear.redondearDosDecimales(diferencia.subtract(acumaumento));
                    if(diferencia.doubleValue()>0){
                        jLabel32.setText("La partida numero "+part+" Tiene "+diferencia+" unidades para aumentar");
                        diferencia=diferenciaMayorValuado(diferencia, numero);
                        jTextField5.setText(diferencia.toString());
                        jLabel6.setText("En esta valuación hay aumento de  "+diferencia);                        
                    }else{
                        jLabel32.setText("La partida numero "+part+" No tiene unidades para aumentar");
                    }
                }else{
                    diferencia = diferencia.multiply(new BigDecimal(-1));
                    diferencia = redondear.redondearDosDecimales(diferencia.subtract(acumdismi));
                    if(diferencia.doubleValue()>0){
                        jLabel32.setText("La partida numero "+part+" Tiene "+diferencia+" unidades para disminuir");
                        jTextField6.setText(diferencia.toString());
                    }else{
                        jLabel32.setText("La partida numero "+part+" No tiene unidades para disminuir");
                    }
                }
                
               
                
                precio = new BigDecimal(jTextField8.getText().toString());
                totalpart = redondear.redondearDosDecimales(cantidad.multiply(precio));
                jTextField11.setText(String.valueOf(totalpart));  
                
                totalaum = redondear.redondearDosDecimales(aumento.multiply(precio));
                jTextField12.setText(String.valueOf(totalaum));  
                totaldismi = redondear.redondearDosDecimales(dismi.multiply(precio));
                jTextField21.setText(String.valueOf(totaldismi));  
                total = totalpart.add(totalaum).subtract(totaldismi);
                jTextField20.setText(String.valueOf(total)); 
                cambie=0;
             
            } catch (SQLException ex) {
                Logger.getLogger(aumentosdismi.class.getName()).log(Level.SEVERE, null, ex);
            }
          
           
           
            
        } catch (SQLException ex) {
            Logger.getLogger(aumentosdismi.class.getName()).log(Level.SEVERE, null, ex);
        }
      
       
       }
        
    }
    
    private BigDecimal diferenciaMayorValuado(BigDecimal diferencia, String numero)
    {
        BigDecimal cant=new BigDecimal("0.00");
        String select = "SELECT cantidad FROM dvalus WHERE numepart="+numero+" AND mpre_id='"+pres+"' AND "
                + "mvalu_id = "+jSpinner4.getValue()+"";
        try {
            Statement st = (Statement) conex.createStatement();
            ResultSet rst = st.executeQuery(select);
            while(rst.next())
            
                cant=rst.getBigDecimal(1);
            
        } catch (SQLException ex) {
            Logger.getLogger(aumentosdismi.class.getName()).log(Level.SEVERE, null, ex);
        }
        return diferencia.floatValue()>cant.floatValue()?cant:diferencia;
    }
    private void jSpinner1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner1StateChanged
        buscarpartida(jSpinner1.getValue().toString());
        jButton2.setEnabled(true);
    }//GEN-LAST:event_jSpinner1StateChanged
     
    public final void buscapartida(){
        try {
            String sql = "SELECT mp.numegrup, mp.id, mp.descri,mp.cantidad, IFNULL((SELECT SUM(cantidad) FROM dvalus "
                    + "WHERE mpre_id='"+pres+"' AND numepart=mp.numero AND mvalu_id=ad.mvalu_id),0) "
                    + "as cantidad, ad.aumento,"
                    + "ad.disminucion, ad.mvalu_id"
                    + " FROM mppres as mp, admppres as ad, dvalus as dv WHERE ad.payd_id="+jSpinner3.getValue()+" "
                    + "AND ad.mpre_id='"+pres+"' AND ad.numepart=mp.numero AND (mp.mpre_id=ad.mpre_id OR mp.mpre_id IN "
                    + "(SELECT id FROM mpres WHERE mpres_id=ad.mpre_id)) "
                    + "AND dv.mpre_id=ad.mpre_id AND (dv.numepart = mp.numero OR mp.numero NOT IN"
                    + " (SELECT numepart FROM dvalus WHERE mpre_id='"+pres+"')) "
                    + "GROUP BY mp.numegrup";
            
           System.out.println("partida"+sql);
            Statement st = (Statement) conex.createStatement();
            ResultSet rs = st.executeQuery(sql);
            ResultSetMetaData rsMd = (ResultSetMetaData) rs.getMetaData();
          
           DefaultTableModel metabs = new DefaultTableModel(){
               @Override
           public boolean isCellEditable (int row, int column) {
              if(column==5||column==6|| column==7)
                  return true;
              else
               return false;
              
           }
         
                @Override
                public Class getColumnClass(int columna)
           {
               if (columna == 0) {
                   return Integer.class;
               }
               
               return Object.class;
           }
           };
           
           jTable1.setModel(metabs);
               int cantidadColumnas = rsMd.getColumnCount();
                for (int i = 1; i <= cantidadColumnas; i++) {
                   // System.out.println("Entra a hacer columnas "+cantidadColumnas);
                    metabs.addColumn(rsMd.getColumnLabel(i));
               }
                
                while (rs.next()) {
                    
                    Object[] filas = new Object[cantidadColumnas];
                   for (int i = 0; i < cantidadColumnas; i++) {
                     
                          filas[i]=rs.getObject(i+1);
                   }
                   metabs.addRow(filas);
                   
               }
        } catch (SQLException ex) {
            Logger.getLogger(valuacion.class.getName()).log(Level.SEVERE, null, ex);
        }
        cambiarcabecera();
    }
      public void cambiarcabecera(){
        JTableHeader th = jTable1.getTableHeader();
        TableColumnModel tcm = th.getColumnModel();
        TableColumn tc = tcm.getColumn(0); 
       
       tc.setHeaderValue("Número.");
       tc.setPreferredWidth(5);
       tc = tcm.getColumn(1); 
       tc.setHeaderValue("Código");
       tc.setPreferredWidth(5);
       tc = tcm.getColumn(2); 
       tc.setHeaderValue("Descripción");
       tc.setPreferredWidth(150);
       tc = tcm.getColumn(3); 
       tc.setHeaderValue("Cantidad");
       tc.setPreferredWidth(20);
       tc = tcm.getColumn(4); 
       tc.setHeaderValue("Cant. Valu.");
       tc.setPreferredWidth(30);
       tc = tcm.getColumn(5); 
       tc.setHeaderValue("Aumento");
       tc.setPreferredWidth(20);
      tc = tcm.getColumn(6); 
       tc.setHeaderValue("Disminución");
       tc.setPreferredWidth(20);
       tc = tcm.getColumn(7); 
       tc.setHeaderValue("Valuación");
       tc.setPreferredWidth(20);
     
       th.repaint(); 
    }
    private void jSpinner3StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner3StateChanged

       modelovalu();
        buscapartida();
      //cargavalores();
        
        
    }//GEN-LAST:event_jSpinner3StateChanged

    public void cambiapartida(){
         String [] partida, numpartida;
        int cuenta=0, i = 0;
        jSpinner1.setEnabled(true);
        String valu = jSpinner4.getValue().toString();
        jButton2.setEnabled(true);
        String consulta="SELECT COUNT(mppre_id) FROM dvalus WHERE mvalu_id="+valu+" AND mpre_id='"+pres+"'";
         
        String sql = "SELECT dv.mppre_id, mp.numegrup FROM dvalus as dv, mppres as mp WHERE"
                + " dv.numepart NOT IN (SELECT numepart FROM admppres WHERE numepart=dv.numepart AND "
                + "mpre_id='"+pres+"' AND payd_id='"+jSpinner3.getValue().toString()+"') AND "
                + "dv.mvalu_id = "+valu+" AND mp.numero = dv.numepart AND (dv.mpre_id='"+pres+"' OR "
                + "dv.mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) "
                + " AND (((SELECT MAX(mvalu_id) FROM dvalus WHERE numepart=mp.numero AND mpre_id='"+pres+"')="+valu+""
                + " AND ((mp.cantidad-IFNULL((SELECT SUM(cantidad) FROM dvalus WHERE numepart=mp.numero AND mpre_id='"+pres+"'),0))"
                + "-IFNULL((SELECT SUM(disminucion) FROM admppres WHERE  "
                + "numepart = mp.numero AND mpre_id='"+pres+"'),0))>0) " // Partidas con monto disponible en su ultima valuacion
                + " OR "
                + "(SELECT SUM(cantidad) FROM dvalus WHERE numepart = mp.numero AND mpre_id='"+pres+"' AND mvalu_id<="+valu+")>"
                + "(mp.cantidad+IFNULL((SELECT SUM(aumento) FROM admppres WHERE mpre_id='"+pres+"' AND numepart=mp.numero),0)))"
                + "AND (mp.mpre_id='"+pres+"' OR "
                + "mp.mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) " 
                + " GROUP BY mp.numegrup ORDER BY  mp.numegrup ";
        System.out.println("sql aumdismi "+sql);
        try {
            Statement str =  (Statement) conex.createStatement();
            ResultSet rste = str.executeQuery(consulta);
            while(rste.next())
            {
                cuenta = Integer.parseInt(rste.getObject(1).toString());
            }
            cuenta=0;
            Statement st = (Statement) conex.createStatement();
            ResultSet rst = st.executeQuery(sql);
            while(rst.next())
            {
                cuenta++;
            }
            rst.first();
             partida = new String[cuenta];
             numpartida = new String[cuenta];
             i=0;
            if(i<cuenta){
                do
                {
                partida[i]=rst.getObject(1).toString();
                numpartida[i]=rst.getObject(2).toString();
                i++;
                }while(rst.next());
            }
            if(cuenta>0){
            
            SpinnerListModel modelo = new SpinnerListModel(numpartida);
                jSpinner1.setModel(modelo);
              
                jSpinner1.setEnabled(true);
                buscarpartida(numpartida[0]);
            }else{
                jSpinner1.setEnabled(false);
            }
        } catch (SQLException ex) {
            Logger.getLogger(aumentosdismi.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void jSpinner4StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner4StateChanged
       cambiapartida();
        
        
    }//GEN-LAST:event_jSpinner4StateChanged

    private void jTextField7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField7ActionPerformed

    private void jTextField8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField8ActionPerformed

    private void jTextField9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField9ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField9ActionPerformed

    private void jTextField10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField10ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField10ActionPerformed

    private void jTextArea2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextArea2KeyPressed
        
        if (evt.getKeyCode() == KeyEvent.VK_TAB) {
            if (evt.getModifiers() == KeyEvent.SHIFT_MASK) {
                ((javax.swing.JTextArea) evt.getSource()).transferFocusBackward();
            } else {
                ((javax.swing.JTextArea) evt.getSource()).transferFocus();
            }
            evt.consume();
        }        
    }//GEN-LAST:event_jTextArea2KeyPressed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
               
        String auxpres="";
        String id = jSpinner3.getValue().toString();
        String numepart = jSpinner1.getValue().toString();
        BigDecimal aumenta = new BigDecimal(jTextField5.getText());
        BigDecimal disminuye = new BigDecimal(jTextField6.getText());
        String dvalu = jSpinner4.getValue().toString();
        jTextField6.setText("0.00");
        jTextField5.setText("0.00");
        int contar=0;
        String buscapayd = "SELECT count(*) FROM pays WHERE mpre_id='"+pres+"' "
                + "AND id='"+id+"'";
        Statement cuenta;
        try {
            cuenta = (Statement) conex.createStatement();
            ResultSet rscuenta = cuenta.executeQuery(buscapayd);
            while(rscuenta.next()){
                contar = rscuenta.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(aumentosdismi.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println(contar);
        
        String consulta = "SELECT numero, mpre_id FROM mppres "
                + "WHERE numegrup="+numepart+" AND (mpre_id='"+pres+"' "
                + "OR mpre_id IN (SELECT id from mpres where mpres_id ='"+pres+"' GROUP BY id))ORDER BY numegrup" ;
        try {
            Statement stmt = (Statement) conex.createStatement();
            ResultSet rstmt = stmt.executeQuery(consulta);
            auxpres = pres;
            while(rstmt.next()){
                numepart = rstmt.getObject(1).toString();
              
            }
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(aumentosdismi.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(contar==0){
                String inserta = "INSERT INTO pays VALUES('"+id+"',0,0,'"+pres+"')";
            try {
                Statement insert = (Statement) conex.createStatement();
                insert.execute(inserta);
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(aumentosdismi.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        int cuantos=0;
        String consultar = "Select count(*) FROM admppres WHERE numepart="+numepart+" AND mpre_id='"+pres+"' AND "
                + "payd_id="+id+"";
        try {
            Statement contare = (Statement) conex.createStatement();
            ResultSet rscontare = contare.executeQuery(consultar);
            while(rscontare.next()){
                cuantos = rscontare.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(aumentosdismi.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(cuantos==0){
        String sql = "INSERT INTO admppres (payd_id, mpre_id, numepart, mvalu_id, aumento, disminucion) VALUES "
                + " ("+id+", '"+pres+"',"+numepart+","+dvalu+",'"+aumenta+"','"+disminuye+"')";
        
        pres = auxpres;
        try {
            Statement stm = (Statement) conex.createStatement();
            stm.execute(sql);
            
            buscapartida();        
            cargavalores();
            cambiapartida();
        } catch (SQLException ex) 
        {
            Logger.getLogger(aumentosdismi.class.getName()).log(Level.SEVERE, null, ex);
        }
        }else{
            JOptionPane.showMessageDialog(null, "Partida ya Fue insertada para presupuesto de aumento y disminución");
            
        }
        
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTable1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable1KeyPressed
        
            String codigopart, sql = null, numero;
            BigDecimal original, cantidad, aumento, dismi;
            System.out.println("TECLAS: "+evt.getKeyCode());
            
            if (!jTable1.isEditing() && jTable1.editCellAt(jTable1.getSelectedRow(),
                jTable1.getSelectedColumn())) {
            jTable1.getEditorComponent().requestFocusInWindow();
            if(evt.getKeyCode()==9 || evt.getKeyCode()==10){
                codigopart=jTable1.getValueAt( filapart, 1).toString();
                numero = jTable1.getValueAt( filapart, 0).toString();
                cantidad = new BigDecimal(jTable1.getValueAt(filapart, 4).toString());
                original = new BigDecimal(jTable1.getValueAt(filapart, 3).toString());
                aumento = new BigDecimal(jTable1.getValueAt(filapart, 5).toString());
                dismi = new BigDecimal(jTable1.getValueAt(filapart, 6).toString());
                
                String select = "SELECT numero FROM mppres WHERE numegrup = "+numero+
                        " AND (mpre_id='"+pres+"' OR mpre_id IN (SELECT id "
                        + "FROM mpres WHERE mpres_id='"+pres+"' GROUP BY id))";
            try {
                Statement st = (Statement) conex.createStatement();
                ResultSet rst = st.executeQuery(select);
                while(rst.next()){
                    numero = rst.getObject(1).toString();
                }
            } catch (SQLException ex) {
                Logger.getLogger(aumentosdismi.class.getName()).log(Level.SEVERE, null, ex);
            }
                jTable1.removeEditor();
                System.out.println("TECLAS: "+evt.getKeyCode());
                Statement stmate;
                if(cantidad.doubleValue()>original.doubleValue()) { 
                    if((cantidad.doubleValue()-original.doubleValue())<aumento.doubleValue())
                    {
                        int op = JOptionPane.showConfirmDialog(null, "Cantidad en aumento excede a la cantidad valuada - cantidad original en "+(aumento.doubleValue() - cantidad.doubleValue())+" unidades ¿ Desea Continuar ?");
                      if(op==0){
                        
                    try {
                        stmate = (Statement) conex.createStatement();
                        sql = "UPDATE admppres SET aumento= "+aumento+", disminucion = "+dismi+" WHERE numepart='"+numero+"' AND mpre_id='"+pres+"' AND payd_id="+jSpinner3.getValue()+"";
                        
                    stmate.execute(sql);
                         cargavalores();
                    } catch (SQLException ex) {
                        Logger.getLogger(valuacion.class.getName()).log(Level.SEVERE, null, ex);
                    }
                   
                      }
                        
                        
                    
                    }else
                    {
                        try {
                        stmate = (Statement) conex.createStatement();
                        sql = "UPDATE admppres SET aumento= "+aumento+", disminucion = "+dismi+" WHERE numepart='"+numero+"' AND mpre_id='"+pres+"' AND payd_id="+jSpinner3.getValue()+"";
                        
                    stmate.execute(sql);
                    buscarpartida(numero);
                         
                    } catch (SQLException ex) {
                        Logger.getLogger(valuacion.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    }
                }else{
                    try {
                        stmate = (Statement) conex.createStatement();
                         sql = "UPDATE admppres SET aumento= "+aumento+", disminucion = "+dismi+" WHERE numepart='"+numero+"' AND mpre_id='"+pres+"' AND payd_id="+jSpinner3.getValue()+"";
                    stmate.execute(sql);
                         
                    } catch (SQLException ex) {
                        Logger.getLogger(valuacion.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                if((cantidad.doubleValue()-original.doubleValue())>aumento.doubleValue()){
                        try {
                        stmate = (Statement) conex.createStatement();
                       sql = "UPDATE admppres SET aumento= "+aumento+", disminucion = "+dismi+" WHERE numepart='"+numero+"' AND mpre_id='"+pres+"' AND payd_id="+jSpinner3.getValue()+"";
                    stmate.execute(sql);
                         
                    } catch (SQLException ex) {
                        Logger.getLogger(valuacion.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    }
                System.out.println("UPDATE "+sql);
                 buscapartida(); 
                jTable1.changeSelection(filapart, 1, false, false);
                System.out.println(filapart);
    }
        
            }
    }//GEN-LAST:event_jTable1KeyPressed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
       
        filapart = jTable1.rowAtPoint(evt.getPoint());
        String partida = jTable1.getValueAt(filapart, 0).toString();
        System.out.println("partida "+partida);
        jButton9.setEnabled(true);
        jButton2.setEnabled(false);
        
        buscarpartida1(partida);
    }//GEN-LAST:event_jTable1MouseClicked

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
         valuacion val = new valuacion(p, true, conex, pres, p.presupuesto);
        int xv = (p.getWidth()/2)-375;
        int yv = (p.getHeight()/2)-350;
        val.setBounds(xv, yv, 830, 700);
        val.setVisible(true);
        modelovalu();
        modelonumepart();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        disminuciontotal dismi = new disminuciontotal(p, true, conex, pres,jSpinner3.getValue().toString());
       int xv = (p.getWidth()/2)-800/2;
        int yv = (p.getHeight()/2)-270;
       dismi.setBounds(xv, yv, 800, 450);
        dismi.setVisible(true);
        buscapartida();
        cargavalores();
        // 700 420TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jSpinner3KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jSpinner3KeyTyped
       
        int code = evt.getKeyCode();
        char car = evt.getKeyChar();
        if ((car<'0' || car>'9')) {
            evt.consume();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_jSpinner3KeyTyped

    private void jSpinner4KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jSpinner4KeyTyped
       
        
        char car = evt.getKeyChar();
        if ((car<'0' || car>'9')) {
            evt.consume();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_jSpinner4KeyTyped

    private void jSpinner1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jSpinner1KeyTyped
       
        int code = evt.getKeyCode();
        char car = evt.getKeyChar();
        if ((car<'0' || car>'9')) {
            evt.consume();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_jSpinner1KeyTyped

    private void okButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_okButtonMouseClicked
        cargavalores();     
        buscapartida(); 
    }//GEN-LAST:event_okButtonMouseClicked

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
       int op = JOptionPane.showConfirmDialog(null, "Recuerde Hacer las valuaciones correspondientes primero, ¿Desea Continuar? Sí/No","Valuaciones",JOptionPane.YES_NO_OPTION);
        
       if(op==JOptionPane.YES_OPTION){
        
        disminucionparcial dismi = new disminucionparcial(null, true, conex, pres,jSpinner3.getValue().toString());
        int xv = (p.getWidth()/2)-800/2;
        int yv = (p.getHeight()/2)-270;
        dismi.setBounds(xv, yv, 800, 450);
        dismi.setVisible(true);
        buscapartida();
       }

    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
           int x = (p.getWidth()/2)-200;
           int y = (p.getHeight()/2)-150;
           String partida = jTable1.getValueAt(filapart, 0).toString();
           infopartida partida1 = new infopartida(p, true,conex,partida,pres);
           partida1.setBounds(x, y, 400, 300);
           partida1.setVisible(true);          
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
           int x = (p.getWidth()/2)-760/2;
           int y = (p.getHeight()/2)-250;
           detallepres partida1 = new detallepres(p, true,conex,pres);
           partida1.setBounds(x, y, 760,500);
           partida1.setVisible(true);            
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton6ActionPerformed

private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
     int op = JOptionPane.showConfirmDialog(null, "¿Desea Eliminar Partida?","Eliminar Partida de Presupuesto de Aumento y Disminución",JOptionPane.YES_NO_OPTION);
     if(op==JOptionPane.YES_OPTION){
         String numero="";
         String idaum=jSpinner3.getValue().toString();
         String idval=jSpinner4.getValue().toString();
         String partida = jTable1.getValueAt(filapart, 0).toString();
         String buscanumero = "SELECT numero FROM mppres WHERE numegrup="+partida+" AND (mpre_id='"+pres+"' OR "
                 + "mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"' GROUP BY id))";
            try {
                Statement stbusca = (Statement) conex.createStatement();
                ResultSet rstbusca = stbusca.executeQuery(buscanumero);
                while(rstbusca.next()){
                    numero = rstbusca.getString(1);
                }
            } catch (SQLException ex) {
                Logger.getLogger(aumentosdismi.class.getName()).log(Level.SEVERE, null, ex);
            }
         
         String borrar = "DELETE FROM admppres WHERE payd_id="+idaum+" AND mpre_id='"+pres+"' AND numepart="+numero;
         
            try {
                Statement stborrar = (Statement) conex.createStatement();
                stborrar.execute(borrar);
                JOptionPane.showMessageDialog(null, "Se ha eliminado la partida");
                buscapartida();
            } catch (SQLException ex) {
                Logger.getLogger(aumentosdismi.class.getName()).log(Level.SEVERE, null, ex);
            }
            int cont=0;
        String valu0 = "SELECT COUNT(*) FROM dvalus WHERE numepart="+numero+" AND mvalu_id=0 AND (mpre_id='"+pres+"' OR"
                + " mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"'))";
            try {
                Statement sts = (Statement) conex.createStatement();
                ResultSet rst = sts.executeQuery(valu0);
                while(rst.next())
                {
                    cont = rst.getInt(1);
                }
                if(cont>0){
                    String borraValu = "DELETE FROM dvalus WHERE mvalu_id=0 AND numepart="+numero+" AND (mpre_id='"+pres+"'"
                            + " OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"'))";
                    Statement borra = (Statement) conex.createStatement();
                    borra.execute(borraValu);
                }
            } catch (SQLException ex) {
                Logger.getLogger(aumentosdismi.class.getName()).log(Level.SEVERE, null, ex);
            }
     }
    
    // TODO add your handling code here:
}//GEN-LAST:event_jButton9ActionPerformed

private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
           int x = (p.getWidth()/2)-800/2;
           int y = (p.getHeight()/2)-400/2;
           aumentos partida1 = new aumentos(p, true,pres,conex,jSpinner4.getValue().toString(), jSpinner3.getValue().toString());
           partida1.setBounds(x, y, 800, 400);
           partida1.setVisible(true);     
           buscapartida();
}//GEN-LAST:event_jButton1ActionPerformed

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
   
    }//GEN-LAST:event_okButtonActionPerformed

    private void jTextField5FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField5FocusGained
        jTextField5.setText("");
    }//GEN-LAST:event_jTextField5FocusGained

    private void jTextField5FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField5FocusLost
        if(jTextField5.getText().toString().equals("")){
            jTextField5.setText("0.00");
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField5FocusLost

    private void jTextField6FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField6FocusGained

         jTextField6.setText("");
    }//GEN-LAST:event_jTextField6FocusGained

    private void jTextField6FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField6FocusLost
      if(jTextField6.getText().toString().equals("")){
            jTextField6.setText("0.00");
        }
    }//GEN-LAST:event_jTextField6FocusLost

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        reporteaumdismi report = new reporteaumdismi(p, false, conex, pres, jSpinner3.getValue().toString());
        int x = (p.getWidth()/2)-450/2;
        int y = (p.getHeight()/2)-400/2;
        report.setBounds(x, y, 450, 400);
        report.setVisible(true);
    }//GEN-LAST:event_jButton8ActionPerformed
    
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
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
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
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JSpinner jSpinner3;
    private javax.swing.JSpinner jSpinner4;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField12;
    private javax.swing.JTextField jTextField13;
    private javax.swing.JTextField jTextField14;
    private javax.swing.JTextField jTextField15;
    private javax.swing.JTextField jTextField16;
    private javax.swing.JTextField jTextField17;
    private javax.swing.JTextField jTextField18;
    private javax.swing.JTextField jTextField19;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField20;
    private javax.swing.JTextField jTextField21;
    private javax.swing.JTextField jTextField22;
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
