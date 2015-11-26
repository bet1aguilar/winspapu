
package valuaciones.liquidaciones;
import java.sql.Connection;
import com.mysql.jdbc.Statement;
import config.Redondeo;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
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
import presupuestos.Presupuesto;
import valuaciones.valuacion;
import winspapus.denumeroaletra1;

/**
 *
 * @author Betmart
 */
public class corpointa extends javax.swing.JDialog {

    /** A return status code - returned if Cancel button has been pressed */
    public static final int RET_CANCEL = 0;
    /** A return status code - returned if OK button has been pressed */
    public static final int RET_OK = 1;
 SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
    /** A return status code - returned if Cancel button has been pressed */
    Redondeo redon = new Redondeo();
    String mpres, mvalus;
    Connection conex;
     BigDecimal subtotal= new BigDecimal("0.00"), amort= new BigDecimal("0.00"), subtotalpres=new BigDecimal("0.00");
BigDecimal impupres=new BigDecimal("0.00");
     BigDecimal multa = new BigDecimal("0.00"),anticipo = new BigDecimal("0.00");
    BigDecimal otorgado= new BigDecimal("0.00"), modificado= new BigDecimal("0.00"),  np1= new BigDecimal("0.00");
    BigDecimal  np2= new BigDecimal("0.00"),  vp1= new BigDecimal("0.00"),  vp2= new BigDecimal("0.00");
    BigDecimal impuesto = new BigDecimal("0.00"), siniva=new BigDecimal("0.00"), retlab = new BigDecimal("0.00");
    BigDecimal totalvalu = new BigDecimal("0.00"), totalrecon = new BigDecimal("0.00"), totalextras = new BigDecimal("0.00"), totaldismi = new BigDecimal("0.00");
    BigDecimal totalaum = new BigDecimal("0.00"), totalpres = new BigDecimal("0.00"), acumliqui = new BigDecimal("0.00"), impuacum =new BigDecimal("0.00");
    BigDecimal liquianterior = new BigDecimal("0.00"), presenteliq =new BigDecimal("0.00"), acumuladas = new BigDecimal("0.00");
    private valuacion val;
    BigDecimal antiotorgado = new BigDecimal("0.00"), antiacum = new BigDecimal("0.00");
    private Presupuesto pres;
     NumberFormat formatoNumero = NumberFormat.getNumberInstance();
            
    /** Creates new form estandar */
    public corpointa(java.awt.Frame parent, boolean modal, Connection conex, String pres, String valus, Presupuesto mpres, valuacion val) {
        super(parent, modal);
        initComponents();
        this.conex=conex;
        formatoNumero.setMaximumFractionDigits(2);
         formatoNumero.setMinimumFractionDigits(2);
        this.val = val;
        this.mpres = pres;
        this.mvalus = valus;
        this.pres = mpres;
         cargardatos();
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

    public final void cargardatos(){
        datosObra();
        obraEjecutada();
        estadoCuenta();
        estadoAnticipo();
        tramiteObras();
        tiempoEjecucion();
    }
    private int lapsoCrono()
    {
        int dias=0;
        String sql = "SELECT DATEDIFF(MAX(fechafin), MIN(fechaini)) FROM mppres WHERE (mpre_id='"+mpres+"' OR "
                + "mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+mpres+"'))";
        try {
            Statement st = (Statement) conex.createStatement();
            ResultSet rst = st.executeQuery(sql);
            while(rst.next())
            {
                dias = rst.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(corpointa.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dias;
    }
    private void tiempoEjecucion()
    {
        jTextField10.setText(String.valueOf(lapsoCrono()));
        jTextField37.setText(String.valueOf(lapsoCrono()));
        try {
            jDateChooser11.setDate(format.parse(pres.getFechaIni()));
        } catch (ParseException ex) {
            Logger.getLogger(corpointa.class.getName()).log(Level.SEVERE, null, ex);
        }
        jDateChooser15.setDate(sumaFecha(lapsoCrono()));
        BigDecimal porcEjecutado= val.acum.multiply(new BigDecimal("100")).divide(pres.total, 2, BigDecimal.ROUND_HALF_UP);
        jTextField44.setText(porcEjecutado.toString());
        jTextField45.setText(new BigDecimal("100").subtract(porcEjecutado).toString());
        int transcurrido=tiempoTranscurrido();
        jTextField46.setText(String.valueOf(transcurrido));
        jTextField47.setText(String.valueOf(lapsoCrono()-transcurrido));
    }
    /**
     * Diferencia entre la fecha menor de las valuaciones y la mayor
     * @return 
     */
    private int tiempoTranscurrido()
    {
        int dias = 0;
       String sql = "SELECT DATEDIFF(MAX(hasta), MIN(desde)) FROM mvalus WHERE mpre_id='"+mpres+"' "
               + "AND status=1 AND id<="+mvalus+"";
        try {
            Statement st = (Statement) conex.createStatement();
            ResultSet rst = st.executeQuery(sql);
            while(rst.next())
            {
                dias = rst.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(corpointa.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dias;
    }
    private void llenaNP()
    {
        String select = "SELECT nropresupuesto FROM mppres WHERE tipo='NP' AND (mpre_id IN "
                + "(SELECT id FROM mpres WHERE mpres_id='"+mpres+"')) GROUP BY nropresupuesto";
        try {
            Statement st = (Statement) conex.createStatement();
            ResultSet rst = st.executeQuery(select);
            while(rst.next())
            {
                jComboBox3.addItem(rst.getString(1));
                jComboBox4.addItem(rst.getString(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(corpointa.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void llenaVP()
    {
        String select = "SELECT nrocuadro FROM mppres WHERE tipo='VP' AND (mpre_id IN "
                + "(SELECT id FROM mpres WHERE mpres_id='"+mpres+"')) GROUP BY nrocuadro";
        try {
            Statement st = (Statement) conex.createStatement();
            ResultSet rst = st.executeQuery(select);
            while(rst.next())
            {
                jComboBox5.addItem(rst.getString(1));
                jComboBox6.addItem(rst.getString(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(corpointa.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void tramiteObras()
    {
        modificado = pres.total.add(totalextrasAcum()).add(aumentoAcum()).subtract(disminucionAcum());
        jTextField1.setText(formatoNumero.format(modificado));
        llenaNP();
        llenaVP();
    }
    private void estadoAnticipo()
    {
        
        jTextField38.setText(formatoNumero.format(val.getporcAmort()));
        if(val.getporcAmort().doubleValue()!=0.00)
        {
            otorgado = pres.subtotal.multiply(val.getporcAmort().divide(new BigDecimal("100")));
            jTextField50.setText(formatoNumero.format(otorgado));   
        }else
        {
            jTextField38.setText(jTextField26.getText());
             otorgado = pres.subtotal.multiply(new BigDecimal(jTextField38.getText()).divide(new BigDecimal("100")));
            jTextField50.setText(formatoNumero.format(otorgado));   
        }
            
        jTextField39.setText(formatoNumero.format(val.acumAmortizacion()));
        antiotorgado=val.acumAmortizacion();
        jTextField40.setText(formatoNumero.format(otorgado.subtract(antiotorgado)));
        
        
    }
    private void estadoCuenta()
    {
        subtotalpres=pres.subtotal.add(aumentoAcum().
                add(totalextrasAcum().add(totalreconAcum()).subtract(disminucionAcum())));
       impupres = subtotal.multiply(impuesto().divide(new BigDecimal("100")));
        totalpres = subtotal.add(impupres);
        jTextField27.setText(formatoNumero.format(pres.subtotal));
        jTextField23.setText(formatoNumero.format(aumentoAcum()));
        jTextField30.setText(formatoNumero.format(disminucionAcum()));
        jTextField28.setText(formatoNumero.format(totalextrasAcum().add(totalreconAcum())));        
        jTextField31.setText(formatoNumero.format(subtotal));       
        jTextField21.setText(formatoNumero.format(impupres));        
        jTextField24.setText(formatoNumero.format(totalpres));
        jTextField22.setText(formatoNumero.format(val.acum));
        jTextField29.setText(formatoNumero.format(totalpres.subtract(val.acum))); //SALDO A LIQUIDAR
        
    }
    private void datosObra()
    {
        String nombre="", ubicac="", nrocon="";
        String sql = "SELECT nombre, ubicac, nrocon FROM mpres WHERE id='"+mpres+"'";
        try {
            Statement st = (Statement) conex.createStatement();
            ResultSet rst = st.executeQuery(sql);
            while(rst.next())
            {
                nombre = rst.getString(1);
                ubicac = rst.getString(2);
                nrocon = rst.getString(3);
            }
            jTextField32.setText(nombre);
            jTextField33.setText(ubicac);
            jTextField34.setText(nrocon);
        } catch (SQLException ex) {
            Logger.getLogger(corpointa.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void obraEjecutada()
    {
        /*
         * Valuación
         */
        totalvalu = val.total;
        jTextField19.setText(formatoNumero.format(totalvalu));
        impuesto = val.impu;
        jTextField18.setText(formatoNumero.format(impuesto));
        siniva = val.estavalu;      
        jTextField17.setText(formatoNumero.format(siniva));
        jTextField16.setText(formatoNumero.format(totalrecon()));
        jTextField15.setText(formatoNumero.format(totalextras()));
        jTextField14.setText(formatoNumero.format(disminucion()));
        jTextField13.setText(formatoNumero.format(aumento()));
        subtotal = redon.redondearDosDecimales(siniva.subtract(aumento().subtract(totalextras()).subtract(totalrecon())));
        jTextField20.setText(formatoNumero.format(subtotal));  
        
    }
    public final BigDecimal impuesto() 
    {
        BigDecimal impuestos = new BigDecimal("0.00");
        String imp = "SELECT porimp FROM mpres WHERE id='" + mpres + "'";
        System.out.println("imp en liqui ivt "+imp);
        try {
            Statement simp = null;
            try {
                simp = (Statement) conex.createStatement();
            } catch (SQLException ex) {
                Logger.getLogger(ivt.class.getName()).log(Level.SEVERE, null, ex);
            }
            ResultSet rsimp = simp.executeQuery(imp);
            while (rsimp.next()) {
                impuestos = rsimp.getBigDecimal(1);
            }


        } catch (SQLException ex) {
            Logger.getLogger(ivt.class.getName()).log(Level.SEVERE, null, ex);
        }
        return redon.redondearDosDecimales(impuestos);
    }

   

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel19 = new javax.swing.JLabel();
        jTextField11 = new javax.swing.JTextField();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        jLabel21 = new javax.swing.JLabel();
        jToggleButton1 = new javax.swing.JToggleButton();
        jPanel1 = new javax.swing.JPanel();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jTextField17 = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jTextField18 = new javax.swing.JTextField();
        jTextField13 = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        jTextField20 = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        jTextField15 = new javax.swing.JTextField();
        jTextField19 = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        jTextField14 = new javax.swing.JTextField();
        jTextField16 = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jTextField26 = new javax.swing.JTextField();
        jTextField42 = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jTextField48 = new javax.swing.JTextField();
        jLabel34 = new javax.swing.JLabel();
        jTextField49 = new javax.swing.JTextField();
        jPanel13 = new javax.swing.JPanel();
        jLabel50 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        jTextField32 = new javax.swing.JTextField();
        jTextField33 = new javax.swing.JTextField();
        jLabel52 = new javax.swing.JLabel();
        jTextField34 = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jTextField12 = new javax.swing.JTextField();
        jDateChooser3 = new com.toedter.calendar.JDateChooser();
        jLabel22 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jComboBox1 = new javax.swing.JComboBox();
        jComboBox2 = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jDateChooser7 = new com.toedter.calendar.JDateChooser();
        jLabel9 = new javax.swing.JLabel();
        jDateChooser8 = new com.toedter.calendar.JDateChooser();
        jComboBox3 = new javax.swing.JComboBox();
        jComboBox4 = new javax.swing.JComboBox();
        jPanel5 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        jTextField9 = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jDateChooser9 = new com.toedter.calendar.JDateChooser();
        jLabel15 = new javax.swing.JLabel();
        jDateChooser10 = new com.toedter.calendar.JDateChooser();
        jComboBox5 = new javax.swing.JComboBox();
        jComboBox6 = new javax.swing.JComboBox();
        jPanel7 = new javax.swing.JPanel();
        jLabel43 = new javax.swing.JLabel();
        jTextField38 = new javax.swing.JTextField();
        jTextField39 = new javax.swing.JTextField();
        jLabel44 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        jTextField40 = new javax.swing.JTextField();
        jTextField50 = new javax.swing.JTextField();
        jLabel61 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jLabel31 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jTextField21 = new javax.swing.JTextField();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        jTextField22 = new javax.swing.JTextField();
        jTextField23 = new javax.swing.JTextField();
        jLabel41 = new javax.swing.JLabel();
        jTextField27 = new javax.swing.JTextField();
        jLabel42 = new javax.swing.JLabel();
        jTextField28 = new javax.swing.JTextField();
        jTextField29 = new javax.swing.JTextField();
        jLabel48 = new javax.swing.JLabel();
        jTextField30 = new javax.swing.JTextField();
        jTextField31 = new javax.swing.JTextField();
        jLabel49 = new javax.swing.JLabel();
        jTextField24 = new javax.swing.JTextField();
        jLabel60 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jTextField10 = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jDateChooser11 = new com.toedter.calendar.JDateChooser();
        jLabel17 = new javax.swing.JLabel();
        jDateChooser12 = new com.toedter.calendar.JDateChooser();
        jLabel18 = new javax.swing.JLabel();
        jDateChooser13 = new com.toedter.calendar.JDateChooser();
        jLabel53 = new javax.swing.JLabel();
        jTextField35 = new javax.swing.JTextField();
        jDateChooser14 = new com.toedter.calendar.JDateChooser();
        jLabel54 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        jTextField36 = new javax.swing.JTextField();
        jLabel56 = new javax.swing.JLabel();
        jTextField37 = new javax.swing.JTextField();
        jLabel57 = new javax.swing.JLabel();
        jLabel58 = new javax.swing.JLabel();
        jDateChooser15 = new com.toedter.calendar.JDateChooser();
        jDateChooser16 = new com.toedter.calendar.JDateChooser();
        jPanel8 = new javax.swing.JPanel();
        jTextField44 = new javax.swing.JTextField();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jTextField45 = new javax.swing.JTextField();
        jLabel38 = new javax.swing.JLabel();
        jTextField46 = new javax.swing.JTextField();
        jLabel59 = new javax.swing.JLabel();
        jTextField47 = new javax.swing.JTextField();

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel19.setText("Otros");

        jTextField11.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField11.setText("0.00");
        jTextField11.setToolTipText("Monto");

        jLabel21.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel21.setText("Fecha:");

        jToggleButton1.setText("jToggleButton1");

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        okButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/selecc.fw.png"))); // NOI18N
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

        jLabel1.setBackground(new java.awt.Color(91, 91, 95));
        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Párametros Liquidación Estandar");
        jLabel1.setOpaque(true);

        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Obra Ejecutada Según:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 10))); // NOI18N

        jLabel27.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel27.setText("Subtotal");

        jLabel28.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel28.setText("Reconsideración");

        jTextField17.setEditable(false);
        jTextField17.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField17.setText("0.00");

        jLabel29.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel29.setText("Impuesto IVA");

        jLabel24.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel24.setText("Aumentos");

        jTextField18.setEditable(false);
        jTextField18.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField18.setText("0.00");

        jTextField13.setEditable(false);
        jTextField13.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField13.setText("0.00");

        jLabel26.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel26.setText("Extras");

        jTextField20.setEditable(false);
        jTextField20.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField20.setText("0.00");

        jLabel30.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel30.setText("Total");

        jTextField15.setEditable(false);
        jTextField15.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField15.setText("0.00");

        jTextField19.setEditable(false);
        jTextField19.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField19.setText("0.00");

        jLabel23.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel23.setText("Según Contrato:");

        jTextField14.setEditable(false);
        jTextField14.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        jTextField14.setText("0.00");

        jTextField16.setEditable(false);
        jTextField16.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField16.setText("0.00");

        jLabel25.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel25.setText("Disminución");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel30)
                    .addComponent(jLabel29)
                    .addComponent(jLabel27)
                    .addComponent(jLabel28)
                    .addComponent(jLabel26)
                    .addComponent(jLabel25)
                    .addComponent(jLabel24)
                    .addComponent(jLabel23))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField19, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE)
                    .addComponent(jTextField18, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE)
                    .addComponent(jTextField17, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE)
                    .addComponent(jTextField16, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE)
                    .addComponent(jTextField15, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE)
                    .addComponent(jTextField14, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE)
                    .addComponent(jTextField13, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE)
                    .addComponent(jTextField20, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(jTextField20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(jTextField13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(jTextField14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(jTextField15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28)
                    .addComponent(jTextField16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27)
                    .addComponent(jTextField17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jTextField18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel29))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel30))
                .addContainerGap())
        );

        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Anticipo", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 10))); // NOI18N

        jTextField26.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField26.setText("0.00");
        jTextField26.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField26FocusLost(evt);
            }
        });

        jTextField42.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField42.setText("0.00");
        jTextField42.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField42FocusLost(evt);
            }
        });

        jLabel32.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel32.setText("Amort. Antic. %");

        jLabel46.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel46.setText("Multa %:");

        jLabel33.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel33.setText("Total Amortización:");

        jTextField48.setEditable(false);
        jTextField48.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField48.setText("0.00");

        jLabel34.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel34.setText("Total Multa:");

        jTextField49.setEditable(false);
        jTextField49.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField49.setText("0.00");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel32)
                            .addComponent(jLabel46))
                        .addGap(25, 25, 25)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jTextField42, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)
                            .addComponent(jTextField26, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel33)
                            .addComponent(jLabel34))
                        .addGap(5, 5, 5)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField49, javax.swing.GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)
                            .addComponent(jTextField48, javax.swing.GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel32)
                    .addComponent(jTextField26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jTextField42, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel46))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel33)
                    .addComponent(jTextField48, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel34)
                    .addComponent(jTextField49, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(77, Short.MAX_VALUE))
        );

        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Datos Obra", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 10))); // NOI18N

        jLabel50.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel50.setText("Nombre Obra:");

        jLabel51.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel51.setText("Ubicación:");

        jTextField32.setEditable(false);

        jTextField33.setEditable(false);

        jLabel52.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel52.setText("Contrato:");

        jTextField34.setEditable(false);

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(jLabel50)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField32, javax.swing.GroupLayout.DEFAULT_SIZE, 678, Short.MAX_VALUE))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel51)
                            .addComponent(jLabel52))
                        .addGap(24, 24, 24)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField34, javax.swing.GroupLayout.DEFAULT_SIZE, 678, Short.MAX_VALUE)
                            .addComponent(jTextField33, javax.swing.GroupLayout.DEFAULT_SIZE, 678, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel50)
                    .addComponent(jTextField32, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel51)
                    .addComponent(jTextField33, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel52)
                    .addComponent(jTextField34, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel13, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, 237, Short.MAX_VALUE))
                .addGap(30, 30, 30))
        );

        jTabbedPane1.addTab("Valuación Actual", jPanel2);

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Tramites de Obra Aprobados", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 10))); // NOI18N

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel20.setText("Oficio Presup. Mod.:");

        jTextField12.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField12.setToolTipText("Monto");

        jLabel22.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel22.setText("Fecha de Oficio:");

        jLabel45.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel45.setText("Monto Modificado:");

        jTextField1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField1.setText("0.00");
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Obras Extras", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "NP", "OE", "OA", "OC" }));

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "NP", "OE", "OA", "OC" }));

        jLabel3.setText("No.");

        jLabel4.setText("No.");

        jLabel6.setText("Monto");

        jLabel7.setText("Monto");

        jLabel8.setText("Fecha");

        jLabel9.setText("Fecha");

        jComboBox3.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox3ItemStateChanged(evt);
            }
        });

        jComboBox4.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox4ItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addGap(3, 3, 3)
                        .addComponent(jComboBox3, 0, 44, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jDateChooser7, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jComboBox2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox4, 0, 43, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jDateChooser8, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(jDateChooser7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jDateChooser8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(28, Short.MAX_VALUE))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Reconsideraciones de Precios", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        jLabel10.setText("No.");

        jLabel11.setText("No.");

        jLabel12.setText("Monto");

        jLabel13.setText("Monto");

        jLabel14.setText("Fecha");

        jLabel15.setText("Fecha");

        jComboBox5.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox5ItemStateChanged(evt);
            }
        });

        jComboBox6.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox6ItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11))
                .addGap(9, 9, 9)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jComboBox6, javax.swing.GroupLayout.Alignment.TRAILING, 0, 40, Short.MAX_VALUE)
                    .addComponent(jComboBox5, javax.swing.GroupLayout.Alignment.TRAILING, 0, 40, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel12)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel15))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel14)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jDateChooser10, javax.swing.GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE)
                    .addComponent(jDateChooser9, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jDateChooser9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14)
                    .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12)
                    .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addGap(10, 10, 10)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(jLabel13)
                    .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15)
                    .addComponent(jDateChooser10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel9Layout.createSequentialGroup()
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel22, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel45, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel20, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(44, 44, 44)
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jDateChooser3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jTextField1)
                                    .addComponent(jTextField12, javax.swing.GroupLayout.DEFAULT_SIZE, 251, Short.MAX_VALUE))))
                        .addGap(10, 10, 10))))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel45))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jDateChooser3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(86, Short.MAX_VALUE))
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Anticipos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 10))); // NOI18N

        jLabel43.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel43.setText("% Otorgado:");

        jTextField38.setEditable(false);
        jTextField38.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField38.setText("0.00");
        jTextField38.setToolTipText("Anticipo sobre Subtotal Contratado");
        jTextField38.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField38FocusLost(evt);
            }
        });

        jTextField39.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField39.setText("0.00");
        jTextField39.setToolTipText("Amortización Acumulada de todas las Valuaciones");
        jTextField39.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField39FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField39FocusLost(evt);
            }
        });
        jTextField39.addHierarchyListener(new java.awt.event.HierarchyListener() {
            public void hierarchyChanged(java.awt.event.HierarchyEvent evt) {
                jTextField39HierarchyChanged(evt);
            }
        });
        jTextField39.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                jTextField39InputMethodTextChanged(evt);
            }
        });

        jLabel44.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel44.setText("Amort. Acum.");

        jLabel47.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel47.setText("Saldo por Amortizar:");

        jTextField40.setEditable(false);
        jTextField40.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField40.setText("0.00");
        jTextField40.setToolTipText("");

        jTextField50.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField50.setText("0.00");
        jTextField50.setToolTipText("Anticipo sobre Subtotal Contratado");
        jTextField50.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField50FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField50FocusLost(evt);
            }
        });
        jTextField50.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                jTextField50InputMethodTextChanged(evt);
            }
        });

        jLabel61.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel61.setText("Monto Otorgado:");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel43)
                    .addComponent(jLabel44)
                    .addComponent(jLabel61)
                    .addComponent(jLabel47))
                .addGap(35, 35, 35)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField38, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
                    .addComponent(jTextField40, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
                    .addComponent(jTextField39, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
                    .addComponent(jTextField50, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel43)
                    .addComponent(jTextField38, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jTextField50, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel61))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jTextField39, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel44))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jTextField40, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel47))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Estado de Cuenta", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 10))); // NOI18N

        jLabel31.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel31.setText("IVA");

        jLabel35.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel35.setText("Subtotal");

        jTextField21.setEditable(false);
        jTextField21.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField21.setText("0.00");

        jLabel39.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel39.setText("Liquidaciones Acumuladas");

        jLabel40.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel40.setText("Aumentos");

        jTextField22.setEditable(false);
        jTextField22.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField22.setText("0.00");

        jTextField23.setEditable(false);
        jTextField23.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField23.setText("0.00");

        jLabel41.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel41.setText("Extras");

        jTextField27.setEditable(false);
        jTextField27.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField27.setText("0.00");

        jLabel42.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel42.setText("Saldo Por Liquidar");

        jTextField28.setEditable(false);
        jTextField28.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField28.setText("0.00");

        jTextField29.setEditable(false);
        jTextField29.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField29.setText("0.00");

        jLabel48.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel48.setText("Contrato:");

        jTextField30.setEditable(false);
        jTextField30.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField30.setText("0.00");

        jTextField31.setEditable(false);
        jTextField31.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField31.setText("0.00");

        jLabel49.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel49.setText("Disminuciones");

        jTextField24.setEditable(false);
        jTextField24.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField24.setText("0.00");

        jLabel60.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel60.setText("Total");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel48)
                    .addComponent(jLabel40)
                    .addComponent(jLabel49)
                    .addComponent(jLabel41)
                    .addComponent(jLabel35)
                    .addComponent(jLabel31)
                    .addComponent(jLabel60)
                    .addComponent(jLabel39)
                    .addComponent(jLabel42))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField27, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
                    .addComponent(jTextField23, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
                    .addComponent(jTextField30, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
                    .addComponent(jTextField28, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
                    .addComponent(jTextField31, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
                    .addComponent(jTextField21, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
                    .addComponent(jTextField24, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField22, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
                    .addComponent(jTextField29, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jTextField27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel48))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jTextField23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel40))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jTextField30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel49))
                .addGap(5, 5, 5)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jTextField28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel41))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel35)
                    .addComponent(jTextField31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jTextField21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel31))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jTextField24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel60))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jTextField22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel39))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jTextField29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel42))
                .addGap(16, 16, 16))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(38, 38, 38))
        );

        jTabbedPane1.addTab("Estado General", jPanel3);

        jPanel14.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Tiempo de Ejecución", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 10))); // NOI18N

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel5.setText("Plazo Ejecución (días)");

        jTextField10.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField10.setText("0");
        jTextField10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField10ActionPerformed(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel16.setText("Fecha Inicio:");

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel17.setText("Paralización:");

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel18.setText("Reinicio:");

        jLabel53.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel53.setText("No. días Prorroga");

        jTextField35.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField35.setText("0");

        jLabel54.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel54.setText("Prorroga");

        jLabel55.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel55.setText("Total días Prorroga");

        jTextField36.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField36.setText("0");
        jTextField36.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField36FocusLost(evt);
            }
        });

        jLabel56.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel56.setText("Lapso Ejecución Total");

        jTextField37.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField37.setText("0");
        jTextField37.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField37ActionPerformed(evt);
            }
        });

        jLabel57.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel57.setText("Fecha Terminación Orig.");

        jLabel58.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel58.setText("Fecha Terminación Prorroga");

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(jLabel16)
                    .addComponent(jLabel17)
                    .addComponent(jLabel18)
                    .addComponent(jLabel54)
                    .addComponent(jLabel53)
                    .addComponent(jLabel55)
                    .addComponent(jLabel56)
                    .addComponent(jLabel57)
                    .addComponent(jLabel58))
                .addGap(4, 4, 4)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField36, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE)
                    .addComponent(jTextField35, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE)
                    .addComponent(jDateChooser14, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE)
                    .addComponent(jDateChooser13, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE)
                    .addComponent(jDateChooser12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE)
                    .addComponent(jDateChooser11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE)
                    .addComponent(jTextField10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE)
                    .addComponent(jTextField37, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE)
                    .addComponent(jDateChooser15, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE)
                    .addComponent(jDateChooser16, javax.swing.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                .addContainerGap(85, Short.MAX_VALUE)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel5)
                    .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jDateChooser11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16))
                .addGap(8, 8, 8)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jDateChooser12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel18)
                    .addComponent(jDateChooser13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jDateChooser14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel54))
                .addGap(8, 8, 8)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel53)
                    .addComponent(jTextField35, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel55)
                    .addComponent(jTextField36, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel56)
                    .addComponent(jTextField37, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jDateChooser15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel57))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jDateChooser16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel58))
                .addGap(64, 64, 64))
        );

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Porcentaje de Ejecución Final", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 10))); // NOI18N

        jTextField44.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField44.setText("0");
        jTextField44.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField44ActionPerformed(evt);
            }
        });

        jLabel36.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel36.setText("Porcentaje Ejecutado");

        jLabel37.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel37.setText("Porc. por Realizar");

        jTextField45.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField45.setText("0");
        jTextField45.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField45ActionPerformed(evt);
            }
        });

        jLabel38.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel38.setText("Tiempo Transcurrido");

        jTextField46.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField46.setText("0");
        jTextField46.setToolTipText("Lapso de las Valuaciones");

        jLabel59.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel59.setText("Tiempo por Transcurrir");

        jTextField47.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField47.setText("0");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel36)
                    .addComponent(jLabel37)
                    .addComponent(jLabel38)
                    .addComponent(jLabel59))
                .addGap(11, 11, 11)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField47, javax.swing.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE)
                    .addComponent(jTextField46, javax.swing.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE)
                    .addComponent(jTextField45, javax.swing.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE)
                    .addComponent(jTextField44, javax.swing.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(138, 138, 138)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel36)
                    .addComponent(jTextField44, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel37)
                    .addComponent(jTextField45, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel38)
                    .addComponent(jTextField46, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel59)
                    .addComponent(jTextField47, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(171, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Tiempo de Ejecución", jPanel6);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 814, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(10, 10, 10))
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 834, Short.MAX_VALUE)
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cancelButton, okButton});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        getRootPane().setDefaultButton(okButton);
        jTabbedPane1.getAccessibleContext().setAccessibleName("Parámetros");

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
    public void generarreporte() {
        JasperPrint print = null;
     
        String liquidacion = " VALUACIÓN No " + mvalus;
        FileInputStream input = null;
        Map parameters = new HashMap();
        try {
            input = new FileInputStream(new File("liquidacioncorpo.jrxml"));


            String hoy = format.format(new Date());
            BigDecimal amortAnticipo= new BigDecimal(jTextField26.getText());
            String update = "UPDATE mvalus SET porcentajeAmort="+amortAnticipo+" WHERE id="+mvalus+" AND mpre_id='"+mpres+"'";
            try {
                Statement stupdate=(Statement) conex.createStatement();
                stupdate.execute(update);
            } catch (SQLException ex) {
                Logger.getLogger(corpointa.class.getName()).log(Level.SEVERE, null, ex);
            }
          denumeroaletra1 nume = new denumeroaletra1();
              String letras="";
               letras="("+nume.Convertir(String.valueOf(mvalus), true)+")";
            parameters.put("mpres", mpres);
            parameters.put("mvalu", mvalus);
            parameters.put("fecha", format.format(new Date()));
            parameters.put("impuesto", impuesto());           
           parameters.put("fechafirmacontrato", pres.getFechaCon());
           parameters.put("contrato", subtotal);           
           parameters.put("reconsideracion", totalrecon());
           parameters.put("extras",totalextras());
           parameters.put("disminucion", disminucion());
           parameters.put("aumento", aumento());
           parameters.put("siniva", val.subtotal);
           parameters.put("impvalu", val.impu);
           parameters.put("montovalu", val.total);           
           parameters.put("retlab", amort);
           parameters.put("retant", Double.valueOf(jTextField26.getText()));
           parameters.put("otros", multa);
           parameters.put("aumentoacum",aumentoAcum());
           parameters.put("disminucionacum", disminucionAcum());
           parameters.put("extrasacum", totalextrasAcum());
           parameters.put("subtotal", subtotalpres);
           parameters.put("imptotalpres", impupres);
           parameters.put("totaltotal", totalpres);
           parameters.put("liquidacion", letras);
           parameters.put("totalpres", pres.subtotal);           
           parameters.put("acumliquidaciones", val.acum);
           parameters.put("oficiono", jTextField12.getText());
           parameters.put("fechaoficio", jDateChooser3.getDate()!=null?format.format(jDateChooser3.getDate()):"");
           parameters.put("titoe1", buscarTit(jComboBox1.getSelectedItem().toString(), jComboBox3.getSelectedItem().toString()));           
           parameters.put("titoe2", buscarTit(jComboBox2.getSelectedItem().toString(), jComboBox4.getSelectedItem().toString()));           
           parameters.put("titvp1", buscarTit("VP", jComboBox5.getSelectedItem().toString()));           
           parameters.put("titvp2", buscarTit("VP", jComboBox6.getSelectedItem().toString())); 
           parameters.put("oe1", montoNP(jComboBox3.getSelectedItem().toString()));
           parameters.put("oe2", montoNP(jComboBox4.getSelectedItem().toString()));
           parameters.put("rp1", montoVP(jComboBox5.getSelectedItem().toString()));
           parameters.put("rp2", montoVP(jComboBox6.getSelectedItem().toString()));
           parameters.put("fechaoe1", jDateChooser7.getDate()!=null?format.format(jDateChooser7.getDate()):"");
           parameters.put("fechaoe2", jDateChooser8.getDate()!=null?format.format(jDateChooser8.getDate()):"");
           parameters.put("fecharp1", jDateChooser9.getDate()!=null?format.format(jDateChooser9.getDate()):"");
           parameters.put("fecharp2", jDateChooser10.getDate()!=null?format.format(jDateChooser10.getDate()):"");
           parameters.put("anticipootorgado", otorgado);
           parameters.put("anticipoacumulado",antiotorgado);
           parameters.put("plazoejecucion", Integer.parseInt(jTextField10.getText()));
           parameters.put("fechainicio", jDateChooser11.getDate()!=null?format.format(jDateChooser11.getDate()):"");
           parameters.put("paralizacion", jDateChooser12.getDate()!=null?format.format(jDateChooser12.getDate()):"");
           parameters.put("reinicio", jDateChooser13.getDate()!=null?format.format(jDateChooser13.getDate()):"");
           parameters.put("fechaprorroga", jDateChooser14.getDate()!=null?format.format(jDateChooser14.getDate()):"");    
           parameters.put("diasprorro",Integer.parseInt(jTextField35.getText()));           
           parameters.put("totalprorro",Integer.parseInt(jTextField36.getText()));  
           parameters.put("ejecuciontotal",Integer.parseInt(jTextField37.getText()));  
           parameters.put("fechafinorig", jDateChooser15.getDate()!=null?format.format(jDateChooser15.getDate()):"");
           parameters.put("fechafinprorroga", jDateChooser16.getDate()!=null?format.format(jDateChooser16.getDate()):"");
           parameters.put("porcejecucion", jTextField44.getText());
           parameters.put("porcporejecutar",jTextField45.getText());
           parameters.put("tiempotranscurrido", jTextField46.getText());
           parameters.put("tiempoxtranscurrir",jTextField47.getText());
           JasperDesign design; 
            try {
                design = JRXmlLoader.load(input);
                JasperReport report = JasperCompileManager.compileReport(design);
                print = JasperFillManager.fillReport(report, parameters, conex);
                 JasperViewer.viewReport(print, false);
            } catch (JRException ex) {
                Logger.getLogger(ivt.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ivt.class.getName()).log(Level.SEVERE, null, ex);
        }


    }
    private String buscarTit(String tipo, String nro)
    {
        String tit=tipo.equals("VP")?"Reconsideración de Precio No."+nro:tipo.equals("NP")?""
                + "No previstas No "+nro:tipo.equals("OE")?"Obra Extra No. "+nro:tipo.equals("OA")?
                "Obra Adic. No. "+nro:"Obra Comp. No. "+nro;
        return tit;
        
    }
    //--------------------------------- ACUMULADO EN LAS VALUACIONES
    /**
     * 
     * @return 
     */
    
    public BigDecimal aumentoAcum() {
        String consultaaum= "SELECT IFNULL(SUM(ROUND(ROUND(a.aumento,2)*IF(mp.precasu = 0,mp.precunit,mp.precasu),2)),0) FROM mppres as mp,"
                     + " admppres as a "
                     + "WHERE a.mpre_id='"+mpres+"' AND "
                     + "(mp.mpre_id ='"+mpres+"' OR mp.mpre_id IN (SELECT id FROM mpres WHERE mpres_id=a.mpre_id)) AND a.numepart = mp.numero "
                     + "AND a.aumento>0 AND a.mvalu_id<='"+mvalus+"'";
         System.out.println("aum "+consultaaum);
        BigDecimal aumento = new BigDecimal("0.00");
        try {
            Statement st = (Statement) conex.createStatement();
            ResultSet rst = st.executeQuery(consultaaum);
            while (rst.next()) {
                aumento = rst.getBigDecimal(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ivt.class.getName()).log(Level.SEVERE, null, ex);
        }
        return redon.redondearDosDecimales(aumento);
    }

    public BigDecimal disminucionAcum() {
        String consultadismi = "SELECT IFNULL(SUM(ROUND(ROUND(a.disminucion,2)*IF(mp.precasu = 0,mp.precunit,mp.precasu),2)),0) FROM mppres as mp,"
                         + " admppres as a "
                         + "WHERE a.mpre_id='"+mpres+"' AND "
                         + "(mp.mpre_id ='"+mpres+"' OR mp.mpre_id IN (SELECT id FROM mpres WHERE mpres_id=a.mpre_id)) AND a.numepart = mp.numero AND "
                    + "a.disminucion>0 AND a.mvalu_id<='"+mvalus+"'";
        System.out.println("dismi "+consultadismi);
        BigDecimal dismi = new BigDecimal("0.00");
        try {
            Statement st = (Statement) conex.createStatement();
            ResultSet rst = st.executeQuery(consultadismi);
            while (rst.next()) {
                dismi = rst.getBigDecimal(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ivt.class.getName()).log(Level.SEVERE, null, ex);
        }
        return redon.redondearDosDecimales(dismi);
    }

    public BigDecimal totalextrasAcum() {
        String select = "SELECT IFNULL(SUM(cantidad*IF(precasu=0,precunit,precasu)),0) FROM "
                + "mppres WHERE (mpre_id = '" + mpres + "' OR mpre_id IN (SELECT id FROM mpres"
                + " WHERE mpres_id='" + mpres + "')) AND numero IN (SELECT numepart FROM dvalus WHERE mpre_id='"+mpres+"' AND mvalu_id<='"+mvalus+"')"
                + " AND tipo='NP'";
        System.out.println("select extras "+select);
        BigDecimal extras = new BigDecimal("0.00");
        try {
            Statement st = (Statement) conex.createStatement();
            ResultSet rst = st.executeQuery(select);
            while (rst.next()) {
                extras = rst.getBigDecimal(1);
            }

        } catch (SQLException ex) {
            Logger.getLogger(ivt.class.getName()).log(Level.SEVERE, null, ex);
        }
        return redon.redondearDosDecimales(extras);
    }

    public BigDecimal totalreconAcum() {
        //------------ Determinar cual presupuesto de reconsideración pertenece a la valuación mvalus
       
        BigDecimal recon = new BigDecimal("0.00");
        try {
           
            //--------UNA VEZ CONSULTADO EL PRESUPUESTO, SE CONSULTAN LAS PARTIDAS QUE PERTENECEN A PRESU Y SE LE CALCULA
            // EL TOTAL RECONSIDERADO DE DICHAS PARTIDAS, COMO HAY QUE CALCULAR EL DELTA ENTRE EL COSTO DE LA PARTIDA ORIGINAL
            // Y LA NUEVA SE DEBEN HACER DOS CONSULTAS.
            
            String sql = "SELECT IFNULL(SUM(ROUND((IF(mp.precasu=0,mp.precunit, mp.precasu)-(SELECT IF(m.precasu=0,m.precunit,m.precasu)"
                    + " FROM mppres as m WHERE m.numero=mp.mppre_id AND (m.mpre_id='"+mpres+"' OR m.mpre_id IN "
                    + "(SELECT id FROM mpres WHERE mpres_id='"+mpres+"'))))*mp.cantidad,2)),0) FROM mppres as mp WHERE "
                    + "(mp.mpre_id='"+mpres+"' OR mp.mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+mpres+"')) AND  "
                    + "mp.numero IN (SELECT numepart FROM dvalus WHERE mpre_id='"+mpres+"' AND mvalu_id<='"+mvalus+"')"
                    + "AND mp.tipo='VP'";
            
            Statement srecon = (Statement) conex.createStatement();
            ResultSet rsrecon = srecon.executeQuery(sql);
            while (rsrecon.next()) {
                recon = rsrecon.getBigDecimal(1);
            }
            //--------------------------TOTAL RECONSIDERADO DE LA VALUACIÓN MVALUS
        } catch (SQLException ex) {
            Logger.getLogger(ivt.class.getName()).log(Level.SEVERE, null, ex);
        }
        //-------------------------------------------------------------------------------------------
        return redon.redondearDosDecimales(recon);
    }
     //-----------------------------------VALUACION ACTUAL
      public BigDecimal aumento() {
        String consultaaum= "SELECT IFNULL(SUM(ROUND(ROUND(a.aumento,2)*IF(mp.precasu = 0,mp.precunit,mp.precasu),2)),0) FROM mppres as mp,"
                     + " admppres as a "
                     + "WHERE a.mpre_id='"+mpres+"' AND "
                     + "(mp.mpre_id ='"+mpres+"' OR mp.mpre_id IN (SELECT id FROM mpres WHERE mpres_id=a.mpre_id)) "
                + "AND a.numepart IN (SELECT numepart FROM dvalus "
                + "WHERE mpre_id='"+mpres+"' AND mvalu_id=a.mvalu_id)"
                + "AND a.numepart = mp.numero "
                     + "AND a.aumento>0 AND a.mvalu_id='"+mvalus+"'";
         System.out.println("aum "+consultaaum);
        BigDecimal aumento = new BigDecimal("0.00");
        try {
            Statement st = (Statement) conex.createStatement();
            ResultSet rst = st.executeQuery(consultaaum);
            while (rst.next()) {
                aumento = rst.getBigDecimal(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ivt.class.getName()).log(Level.SEVERE, null, ex);
        }
        return redon.redondearDosDecimales(aumento);
    }

    public BigDecimal disminucion() {
        String consultadismi = "SELECT IFNULL(SUM(ROUND(ROUND(a.disminucion,2)*IF(mp.precasu = 0,mp.precunit,mp.precasu),2)),0) FROM mppres as mp,"
                         + " admppres as a "
                         + "WHERE a.mpre_id='"+mpres+"' AND "
                         + "(mp.mpre_id ='"+mpres+"' OR mp.mpre_id IN (SELECT id FROM mpres WHERE mpres_id=a.mpre_id)) AND a.numepart = mp.numero AND "
                    + "a.disminucion>0 AND a.mvalu_id='"+mvalus+"' AND a.numepart IN (SELECT numepart FROM dvalus "
                + "WHERE mpre_id='"+mpres+"' AND mvalu_id=a.mvalu_id)";
        System.out.println("dismi "+consultadismi);
        BigDecimal dismi = new BigDecimal("0.00");
        try {
            Statement st = (Statement) conex.createStatement();
            ResultSet rst = st.executeQuery(consultadismi);
            while (rst.next()) {
                dismi = rst.getBigDecimal(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ivt.class.getName()).log(Level.SEVERE, null, ex);
        }
        return redon.redondearDosDecimales(dismi);
    }

    public BigDecimal totalextras() {
        String select = "SELECT IFNULL(SUM(cantidad*IF(precasu=0,precunit,precasu)),0) FROM "
                + "mppres WHERE (mpre_id = '" + mpres + "' OR mpre_id IN (SELECT id FROM mpres"
                + " WHERE mpres_id='" + mpres + "')) AND numero IN (SELECT numepart FROM dvalus WHERE mpre_id='"+mpres+"' AND mvalu_id="+mvalus+")"
        + " AND tipo='NP'";
        System.out.println("select extras "+select);
        BigDecimal extras = new BigDecimal("0.00");
        try {
            Statement st = (Statement) conex.createStatement();
            ResultSet rst = st.executeQuery(select);
            while (rst.next()) {
                extras = rst.getBigDecimal(1);
            }

        } catch (SQLException ex) {
            Logger.getLogger(ivt.class.getName()).log(Level.SEVERE, null, ex);
        }
        return redon.redondearDosDecimales(extras);
    }

    public BigDecimal totalrecon() {
        //------------ Determinar cual presupuesto de reconsideración pertenece a la valuación mvalus
       
        BigDecimal recon = new BigDecimal("0.00");
        try {
           
            //--------UNA VEZ CONSULTADO EL PRESUPUESTO, SE CONSULTAN LAS PARTIDAS QUE PERTENECEN A PRESU Y SE LE CALCULA
            // EL TOTAL RECONSIDERADO DE DICHAS PARTIDAS, COMO HAY QUE CALCULAR EL DELTA ENTRE EL COSTO DE LA PARTIDA ORIGINAL
            // Y LA NUEVA SE DEBEN HACER DOS CONSULTAS.
            
            String sql = "SELECT IFNULL(SUM(ROUND((IF(mp.precasu=0,mp.precunit, mp.precasu)-(SELECT IF(m.precasu=0,m.precunit,m.precasu)"
                    + " FROM mppres as m WHERE m.numero=mp.mppre_id AND (m.mpre_id='"+mpres+"' OR m.mpre_id IN "
                    + "(SELECT id FROM mpres WHERE mpres_id='"+mpres+"'))))*mp.cantidad,2)),0) FROM mppres as mp WHERE "
                    + "(mp.mpre_id='"+mpres+"' OR mp.mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+mpres+"')) AND  "
                    + "mp.numero IN (SELECT numepart FROM dvalus WHERE mpre_id='"+mpres+"' AND mvalu_id='"+mvalus+"')"
                    + "AND mp.tipo='VP'";
            
            Statement srecon = (Statement) conex.createStatement();
            ResultSet rsrecon = srecon.executeQuery(sql);
            while (rsrecon.next()) {
                recon = rsrecon.getBigDecimal(1);
            }
            //--------------------------TOTAL RECONSIDERADO DE LA VALUACIÓN MVALUS
        } catch (SQLException ex) {
            Logger.getLogger(ivt.class.getName()).log(Level.SEVERE, null, ex);
        }
        //-------------------------------------------------------------------------------------------
        return redon.redondearDosDecimales(recon);
    }
    //---------------------------------------------------------FIN VALUACION ACTUAL
    //---------------------PRESUPUESTO ADICIONALES O VARIACIONES DE PRECIO
    
    private BigDecimal montoNP(String nropresupuesto)
    {
        BigDecimal monto = new BigDecimal("0.00");
        String sql = "SELECT IFNULL(SUM(ROUND(IF(precasu=0, precunit,precasu)*cantidad,2)),0) FROM mppres WHERE "
                + "nropresupuesto='"+nropresupuesto+"' AND mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+mpres+"') AND tipo = 'NP'";
        try {
            Statement st = (Statement) conex.createStatement();
            ResultSet rst = st.executeQuery(sql);
            while(rst.next())
            {
                monto = rst.getBigDecimal(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(corpointa.class.getName()).log(Level.SEVERE, null, ex);
        }
        return monto;
    }

    private BigDecimal montoVP(String nrocuadro)
    {
        BigDecimal monto = new BigDecimal("0.00");
        String sql = "SELECT IFNULL(SUM(ROUND((IF(mp.precasu=0, mp.precunit,mp.precasu)-"
                + "IFNULL((SELECT IF(m.precasu=0, m.precunit, m.precasu) FROM mppres as m WHERE "
                + "m.numero = mp.mppre_id AND (m.mpre_id='"+mpres+"' OR "
                + "m.mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+mpres+"'))),0))*mp.cantidad,2)),0) "
                + "FROM mppres as mp WHERE "
                + "mp.nrocuadro='"+nrocuadro+"' AND (mp.mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+mpres+"')) AND tipo = 'VP'";
        System.out.println(" totalVP "+sql);
        try {
            Statement st = (Statement) conex.createStatement();
            ResultSet rst = st.executeQuery(sql);
            while(rst.next())
            {
                monto = rst.getBigDecimal(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(corpointa.class.getName()).log(Level.SEVERE, null, ex);
        }
        return monto;
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

private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
    
}//GEN-LAST:event_jTextField1ActionPerformed

private void jTextField26FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField26FocusLost
    amort = siniva.multiply(new BigDecimal(jTextField26.getText()).divide(new BigDecimal("100")));
    jTextField38.setText(jTextField26.getText());
    otorgado=pres.subtotal.multiply(new BigDecimal(jTextField26.getText()).divide(new BigDecimal("100")));
    jTextField50.setText(formatoNumero.format(otorgado));
    jTextField48.setText(formatoNumero.format(amort));
}//GEN-LAST:event_jTextField26FocusLost

private void jTextField42FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField42FocusLost
    multa= siniva.multiply(new BigDecimal(jTextField42.getText()).divide(new BigDecimal("100")));
    jTextField49.setText(formatoNumero.format(multa));    
}//GEN-LAST:event_jTextField42FocusLost

private void jTextField38FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField38FocusLost
   /* anticipo = new BigDecimal(jTextField38.getText());
    otorgado = pres.subtotal.multiply(anticipo.divide(new BigDecimal("100")));
    jTextField50.setText(formatoNumero.format(otorgado.toString()));
    */
}//GEN-LAST:event_jTextField38FocusLost

private void jTextField50FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField50FocusLost
    otorgado = new BigDecimal(jTextField50.getText());
    jTextField50.setText(formatoNumero.format(otorgado));
    jTextField40.setText(formatoNumero.format(otorgado.subtract(antiotorgado)));
}//GEN-LAST:event_jTextField50FocusLost

private void jTextField39FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField39FocusGained
    jTextField39.setText(antiotorgado.toString());
    
}//GEN-LAST:event_jTextField39FocusGained

private void jTextField39HierarchyChanged(java.awt.event.HierarchyEvent evt) {//GEN-FIRST:event_jTextField39HierarchyChanged
    
}//GEN-LAST:event_jTextField39HierarchyChanged

private void jTextField50FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField50FocusGained
    jTextField50.setText(otorgado.toString());
}//GEN-LAST:event_jTextField50FocusGained

private void jTextField39FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField39FocusLost
antiotorgado=new BigDecimal(jTextField39.getText());
    jTextField39.setText(formatoNumero.format(antiotorgado));
    jTextField40.setText(formatoNumero.format(otorgado.subtract(antiotorgado)));
}//GEN-LAST:event_jTextField39FocusLost

private void jComboBox3ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox3ItemStateChanged
 // Monto de presupuesto de NP numero seleccionado
    np1= montoNP(jComboBox3.getSelectedItem().toString());
    jTextField4.setText(formatoNumero.format(np1));
    
}//GEN-LAST:event_jComboBox3ItemStateChanged

private void jComboBox4ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox4ItemStateChanged
// Monto de presupuesto de NP numero seleccionado
    np2= montoNP(jComboBox4.getSelectedItem().toString());
    jTextField5.setText(formatoNumero.format(np2));
}//GEN-LAST:event_jComboBox4ItemStateChanged

private void jComboBox5ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox5ItemStateChanged
    vp1 = montoVP(jComboBox5.getSelectedItem().toString());
    jTextField8.setText(formatoNumero.format(vp1));
}//GEN-LAST:event_jComboBox5ItemStateChanged

private void jComboBox6ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox6ItemStateChanged
    vp2 = montoVP(jComboBox6.getSelectedItem().toString());
    jTextField9.setText(formatoNumero.format(vp2));
}//GEN-LAST:event_jComboBox6ItemStateChanged

private void jTextField36FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField36FocusLost
    int suma = this.lapsoCrono()+Integer.parseInt((jTextField36.getText().isEmpty()?"0":jTextField36.getText()));
    jTextField37.setText(String.valueOf(suma));
    jDateChooser16.setDate(sumaFecha(suma));
}//GEN-LAST:event_jTextField36FocusLost

private void jTextField50InputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_jTextField50InputMethodTextChanged

    jTextField40.setText(new BigDecimal(jTextField50.getText()).subtract(new BigDecimal(jTextField39.getText())).toString());
}//GEN-LAST:event_jTextField50InputMethodTextChanged

private void jTextField39InputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_jTextField39InputMethodTextChanged
    jTextField40.setText(new BigDecimal(jTextField50.getText()).subtract(new BigDecimal(jTextField39.getText())).toString());
}//GEN-LAST:event_jTextField39InputMethodTextChanged

private void jTextField10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField10ActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_jTextField10ActionPerformed

private void jTextField37ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField37ActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_jTextField37ActionPerformed

private void jTextField44ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField44ActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_jTextField44ActionPerformed

private void jTextField45ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField45ActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_jTextField45ActionPerformed
    private Date sumaFecha(int dias)
    {
        Date fecha=null;
        String sql = "SELECT DATE_ADD(fecini,INTERVAL "+dias+" DAY) FROM mpres WHERE id='"+mpres+"'";
        try {
            Statement st = (Statement) conex.createStatement();
            ResultSet rst = st.executeQuery(sql);
            while(rst.next())
            {
                fecha = rst.getDate(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(corpointa.class.getName()).log(Level.SEVERE, null, ex);
        }
        return fecha;
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
    private javax.swing.JButton cancelButton;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JComboBox jComboBox3;
    private javax.swing.JComboBox jComboBox4;
    private javax.swing.JComboBox jComboBox5;
    private javax.swing.JComboBox jComboBox6;
    private com.toedter.calendar.JDateChooser jDateChooser10;
    private com.toedter.calendar.JDateChooser jDateChooser11;
    private com.toedter.calendar.JDateChooser jDateChooser12;
    private com.toedter.calendar.JDateChooser jDateChooser13;
    private com.toedter.calendar.JDateChooser jDateChooser14;
    private com.toedter.calendar.JDateChooser jDateChooser15;
    private com.toedter.calendar.JDateChooser jDateChooser16;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private com.toedter.calendar.JDateChooser jDateChooser3;
    private com.toedter.calendar.JDateChooser jDateChooser7;
    private com.toedter.calendar.JDateChooser jDateChooser8;
    private com.toedter.calendar.JDateChooser jDateChooser9;
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
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JTabbedPane jTabbedPane1;
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
    private javax.swing.JTextField jTextField20;
    private javax.swing.JTextField jTextField21;
    private javax.swing.JTextField jTextField22;
    private javax.swing.JTextField jTextField23;
    private javax.swing.JTextField jTextField24;
    private javax.swing.JTextField jTextField26;
    private javax.swing.JTextField jTextField27;
    private javax.swing.JTextField jTextField28;
    private javax.swing.JTextField jTextField29;
    private javax.swing.JTextField jTextField30;
    private javax.swing.JTextField jTextField31;
    private javax.swing.JTextField jTextField32;
    private javax.swing.JTextField jTextField33;
    private javax.swing.JTextField jTextField34;
    private javax.swing.JTextField jTextField35;
    private javax.swing.JTextField jTextField36;
    private javax.swing.JTextField jTextField37;
    private javax.swing.JTextField jTextField38;
    private javax.swing.JTextField jTextField39;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField40;
    private javax.swing.JTextField jTextField42;
    private javax.swing.JTextField jTextField44;
    private javax.swing.JTextField jTextField45;
    private javax.swing.JTextField jTextField46;
    private javax.swing.JTextField jTextField47;
    private javax.swing.JTextField jTextField48;
    private javax.swing.JTextField jTextField49;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField50;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JButton okButton;
    // End of variables declaration//GEN-END:variables
    private int returnStatus = RET_CANCEL;
}
