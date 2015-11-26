package presupuestos;
import com.mysql.jdbc.ResultSetMetaData;
import com.mysql.jdbc.Statement;
import config.Redondeo;
import herramienta.Validacion;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SpinnerListModel;
import javax.swing.table.DefaultTableModel;
import valuaciones.reconsideraciones;
import winspapus.Principal;

/**
 *
 * @author Betmart
 */
public class Partida extends javax.swing.JDialog {

    
    public static final int RET_CANCEL = 0;
    public static final int RET_OK = 1;
    Validacion vali;
    Connection conex;
    BigDecimal totalprecunit= new BigDecimal("0.00"), totalprecasu=new BigDecimal("0.00"), totaltotal=new BigDecimal("0.00"), totalcantidad=new BigDecimal("0.00");
    String presupuesto;
    private List<Integer> id;
    private String numero1;
    private Integer entero;
    int edita=0;
    Redondeo redondear = new Redondeo();
    String mbdat;
    int reconsidera=0;
    reconsideraciones recon;
    Principal p;
    String partida, numpartida;
    String [] partidas;
    String tipo;
    private String adm;
    private String fina;
    private String imp;
    String val;
    Partida partida1=this;
    Presupuesto pres;
    private String impart;
    private String prest;
    String total;
    private String util;
    public Partida(java.awt.Frame parent, boolean modal, Connection conex, String pres, Principal prin, Presupuesto pre, String total) {
        super(parent, false);
        initComponents();
        jLabel13.setVisible(false);
        jTextField2.setVisible(false);
        this.pres= pre;
        vali = new Validacion(conex);
        this.total = total;
        this.conex = conex;
        this.p = prin;
        jLabel9.setVisible(false);
        jLabel20.setVisible(false);
        this.setTitle("Nueva Partida");
        this.presupuesto = pres;
        id = new ArrayList<Integer>();
        try {
            buscagrupo();
            contar();
            jTextField9.setText(total);
            if(jComboBox2.getSelectedItem().equals("No Prevista")){
                jLabel13.setVisible(false);
                jTextField2.setVisible(false);
            }       
           
            jButton2.setEnabled(false);
            jButton3.setEnabled(false);
            jButton4.setEnabled(false);
            jButton5.setEnabled(false);
            jButton6.setEnabled(false);
            cargavalorespres();
          
        } catch (SQLException ex) {
            Logger.getLogger(Partida.class.getName()).log(Level.SEVERE, null, ex);
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
    public Partida(java.awt.Frame parent, boolean modal, Connection conex, String pres, Principal prin, int edita, int contar, String codicove, String partida, Presupuesto pre, String total, reconsideraciones recon) {
       
         super(parent, false);
        initComponents();
         this.edita = edita;
          this.pres= pre;
        this.conex = conex;
        this.recon=recon;
        reconsidera=1;
        jSpinner1.setEnabled(true);
        this.partida = codicove;
        this.numpartida = partida;
        vali = new Validacion(conex);
        this.p = prin;
        jLabel9.setVisible(false);
        jLabel20.setVisible(false);
        this.setTitle("Modifica Partida");
        this.presupuesto = pres;
        if(!jComboBox2.getSelectedItem().equals("No Prevista")){
                jLabel13.setVisible(false);
                jTextField2.setVisible(false);
            }  
        id = new ArrayList<Integer>();
        try {
            buscagrupo();
            contar();
            definemodelo();
            buscapres();
            llenavalores();
           jTextField9.setText(String.valueOf(total));
            
            // System.out.println("numpartida "+numpartida);
            
        } catch (SQLException ex) {
            Logger.getLogger(Partida.class.getName()).log(Level.SEVERE, null, ex);
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
    
   
//*********************************EDITAAAAAAAAAAAAAA*******************************************/
    public Partida(java.awt.Frame parent, boolean modal, Connection conex, String pres, Principal prin, int edita, int contar, String codicove, String partida, Presupuesto pre, String total) {
       
         super(parent, false);
        initComponents();
         this.edita = edita;
          this.pres= pre;
        this.conex = conex;
        jSpinner1.setEnabled(true);
        this.partida = codicove;
        this.numpartida = partida;
        vali = new Validacion(conex);
        this.p = prin;
        jLabel9.setVisible(false);
        jLabel20.setVisible(false);
        this.setTitle("Modifica Partida");
        this.presupuesto = pres;
        if(!jComboBox2.getSelectedItem().equals("No Prevista")){
                jLabel13.setVisible(false);
                jTextField2.setVisible(false);
            }  
        id = new ArrayList<Integer>();
        try {
            buscagrupo();
            contar();
            definemodelo();
            buscapres();
            llenavalores();
           jTextField9.setText(String.valueOf(total));
            
            // System.out.println("numpartida "+numpartida);
            
        } catch (SQLException ex) {
            Logger.getLogger(Partida.class.getName()).log(Level.SEVERE, null, ex);
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
    
    public final void cargavalorespres(){
        String consultapre = "SELECT porgam, poruti, porpre FROM mpres WHERE id='"+presupuesto+"'";
        String porcgam="", porcpre="", porcutil="";
        try {
            Statement consulto = (Statement) conex.createStatement();
            ResultSet rstconsulto = consulto.executeQuery(consultapre);
            while(rstconsulto.next()){
                porcgam = rstconsulto.getString(1);
                porcpre = rstconsulto.getString(3);
                porcutil = rstconsulto.getString(2);
            }
            jTextField5.setText(porcpre);
              jTextField6.setText(porcutil);   
              jTextField4.setText(porcgam);
        } catch (SQLException ex) {
            Logger.getLogger(Partida.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public final void buscapres(){
        String sql = "SELECT porgam, porcfi, porimp, poripa, porpre, poruti FROM mpres WHERE id='"+presupuesto+"'";
        try {
            Statement st = (Statement) conex.createStatement();
            ResultSet rst= st.executeQuery(sql);
            
            while(rst.next()){
                adm = rst.getObject(1).toString();
                fina = rst.getObject(2).toString();
                imp = rst.getObject(3).toString();
                impart = rst.getObject(4).toString();
                prest = rst.getObject(5).toString();
                util = rst.getObject(6).toString();
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(apu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public final void llenavalores(){
        String llena = "SELECT id, tipo, descri, nropresupuesto, porcutil, idband, "
                + "IFNULL(precunit,0.00) as precunit,"
                + " IFNULL(precasu,0.00) as precasu, redondeo, porcpre, porcgad, "
                + "rendimi, IFNULL(cantidad,0.00), unidad,numero, porcutil,tiponp FROM mppres WHERE "
                + "numegrup='"+numpartida+"' "
                + " AND (mpre_id='"+presupuesto+"' OR mpre_id IN "
                + "(SELECT id from mpres where mpres_id='"+presupuesto+"' GROUP BY id)) ";
        int redondeo;
       BigDecimal precio; 
        
      
        try {
            Statement sts = (Statement) conex.createStatement();
            ResultSet rsts = sts.executeQuery(llena);
            
            while(rsts.next()){
                partida = rsts.getObject(1).toString();
                jTextField1.setText(partida);
                tipo = rsts.getObject(2).toString();
                if(tipo.equals("Org")){
                    jComboBox2.setSelectedItem("Original");
                }
                if(tipo.equals("NP")){
                    
                    jComboBox2.setSelectedItem("No Prevista");
                }
                if(tipo.equals("VP")){
                    jComboBox2.setSelectedItem("Variación de Precio");
                }
                jTextArea1.setText(rsts.getObject(3).toString());
                if(rsts.getObject(4)!=null) {
                    jTextField2.setText(rsts.getObject(4).toString());
                }
                BigDecimal presta=rsts.getBigDecimal("porcpre");
                BigDecimal admin = rsts.getBigDecimal("porcgad");
                BigDecimal utili = rsts.getBigDecimal("porcutil");
             
                jTextField5.setText(String.valueOf(presta));
                jTextField6.setText(String.valueOf(utili));
                jTextField4.setText(String.valueOf(admin));
                mbdat = rsts.getObject(6).toString();
                if(!mbdat.equals("")&&mbdat!=null){
                    jComboBox1.setSelectedIndex(Integer.parseInt(mbdat)-1);
                }
                BigDecimal precunit = rsts.getBigDecimal(7);
                BigDecimal precasu = rsts.getBigDecimal(8);
                NumberFormat formatoNumero = NumberFormat.getNumberInstance();
            formatoNumero.setMaximumFractionDigits(2);
            formatoNumero.setMinimumFractionDigits(2);
            jTextField12.setText(String.valueOf(formatoNumero.format(precunit)));
               totalprecunit = precunit;
                jTextField15.setText(String.valueOf(precasu));
                totalprecasu=precasu;
                redondeo = Integer.parseInt(rsts.getObject(9).toString());
                if(redondeo==0){
                    jCheckBox1.setSelected(false);
                    precio = precunit;
                } else{
                    jCheckBox1.setSelected(true);
                     precio =  precasu;
                }
                if(precasu==new BigDecimal("0.00"))
                    precio =  precunit;
                totalprecasu = precasu;
                totalprecunit = precunit;
                jSpinner1.setValue(numpartida);
                jTextField13.setText(rsts.getObject(12).toString());
                jTextField10.setText(rsts.getObject(14).toString());
                BigDecimal cantidad = rsts.getBigDecimal(13);
                totalcantidad=cantidad;
                jTextField7.setText(String.valueOf(formatoNumero.format(cantidad)));
                precio = redondear.redondearDosDecimales(precio.multiply(cantidad));               
                jTextField8.setText(String.valueOf(formatoNumero.format(precio)));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Partida.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    public final void definemodelo(){
        int i=0;
        String sql = "SELECT numegrup from mppres WHERE mpre_id='"+presupuesto+"' OR mpre_id IN (SELECT id from mpres where mpres_id ='"+presupuesto+"' GROUP BY id) GROUP BY numegrup" ;
        try {
            Statement st = (Statement) conex.createStatement();
            ResultSet rst = st.executeQuery(sql);
            while(rst.next()){
                partidas[i] = rst.getObject(1).toString();
                i++;
            }
            
            SpinnerListModel modelo = new SpinnerListModel(partidas);
            jSpinner1.setModel(modelo);
        } catch (SQLException ex) {
            Logger.getLogger(Partida.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void contar() throws SQLException
    {        
       
        Statement cuenta = (Statement) conex.createStatement();
        ResultSet result = cuenta.executeQuery("Select numero FROM mppres WHERE mpre_id='"+presupuesto+"' "
                + "OR mpre_id IN (SELECT id from mpres where mpres_id ='"+presupuesto+"' GROUP BY id)"
                + " ORDER BY numero DESC LIMIT 1");
       numero1="0";
        while(result.next()){
            numero1 = result.getObject(1).toString();            
        }
        
        System.out.println(numero1);
        entero = Integer.valueOf(numero1);
        entero++;
        numero1 = Integer.toString(entero);   
        String[] modelo={numero1};
        SpinnerListModel modelos = new SpinnerListModel(modelo);
        jSpinner1.setModel(modelos);
       if(edita==0)
            jSpinner1.setEnabled(false);           
       else
            partidas = new String[entero];      
    }  
    
public final void buscagrupo() throws SQLException{
        DefaultTableModel partida10 = new DefaultTableModel();
        
        
     
            Statement s = (Statement) conex.createStatement();
            ResultSet rs = s.executeQuery("SELECT id, descri FROM mbdats");
            
         
           
             ResultSetMetaData rsMd = (ResultSetMetaData) rs.getMetaData();
            int cantidadColumnas = rsMd.getColumnCount();
            
             for (int i = 1; i <= cantidadColumnas; i++) {
                
                 partida10.addColumn(rsMd.getColumnLabel(i));
            }
         
             
             while (rs.next()) {
                 
                 Object[] filas = new Object[cantidadColumnas];
                 
                for (int i = 0; i < cantidadColumnas; i++) {
                    filas[i]=rs.getObject(i+1);
                }
                partida10.addRow(filas);
                
            }
                 
                 
                for (int i = 0; i < partida10.getRowCount(); i++) {
                   
                  
                    jComboBox1.addItem(partida10.getValueAt(i, 1).toString());
                }
                
                
           
      
    }
     public void cargartotal(){
        BigDecimal subtotal1 = new BigDecimal("0.00"), subtotal2=new BigDecimal("0.00"), subtotal,impuesto, total1;
        String cargasinredondeo = "SELECT SUM(cantidad*precunit) FROM `winspapu`.`mppres` WHERE redondeo = 0"
                + " AND tipo='Org' AND mpre_id='"+presupuesto+"'";
        String cargaconredondeo = "SELECT SUM(cantidad*precasu) FROM `winspapu`.`mppres` WHERE redondeo = 1"
                + " AND tipo='Org' AND mpre_id='"+presupuesto+"'";
        //System.out.println("idpres="+id);
        try {
            Statement st1 = (Statement) conex.createStatement();
            Statement st2 = (Statement) conex.createStatement();
            ResultSet rst1 = st1.executeQuery(cargasinredondeo);
            ResultSet rst2 = st2.executeQuery(cargaconredondeo);
            
            while (rst1.next()){
                if(rst1.getObject(1)!=null) {
                    subtotal1 = rst1.getBigDecimal(1);
                }
            }
            while (rst2.next()){
                if(rst2.getObject(1)!=null) {
                    subtotal2 = rst2.getBigDecimal(1);
                }
            }
            impuesto = new BigDecimal(imp);
            subtotal = subtotal1.add(subtotal2);
            impuesto = redondear.redondearDosDecimales(subtotal.multiply(impuesto.divide(new BigDecimal("100"))));
            total1 = subtotal.add(impuesto);
            
           
        NumberFormat formatoNumero = NumberFormat.getNumberInstance();
            formatoNumero.setMaximumFractionDigits(2);
            formatoNumero.setMinimumFractionDigits(2);
            jTextField9.setText(String.valueOf(formatoNumero.format(total1))); 
            totaltotal=total1;
         } catch (SQLException ex) {
            Logger.getLogger(Presupuesto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void settotal(String total){
        
        
        BigDecimal precunit = new BigDecimal(total);
        
        
        BigDecimal tota= redondear.redondearDosDecimales(precunit.multiply(totalcantidad));
        totalprecunit = redondear.redondearDosDecimales(precunit);
        NumberFormat formatoNumero = NumberFormat.getNumberInstance();
            formatoNumero.setMaximumFractionDigits(2);
            formatoNumero.setMinimumFractionDigits(2);
            jTextField8.setText(String.valueOf(formatoNumero.format(tota)));
            String variable = String.valueOf(formatoNumero.format(tota));
            jTextField8.setText(variable);
            System.out.println("total partida "+String.valueOf(formatoNumero.format(tota)));
            System.out.println("total partida texto "+jTextField8.getText());
            jTextField12.setText(String.valueOf(formatoNumero.format(precunit)));
            
  
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

        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jSpinner1 = new javax.swing.JSpinner();
        jTextField1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox();
        jLabel13 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jTextField7 = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jTextField10 = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JFormattedTextField();
        jLabel7 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jTextField12 = new javax.swing.JTextField();
        jCheckBox1 = new javax.swing.JCheckBox();
        jTextField15 = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jTextField13 = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jButton5 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        okButton = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();
        jTextField9 = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jCheckBox2 = new javax.swing.JCheckBox();

        jTextArea2.setColumns(20);
        jTextArea2.setRows(5);
        jScrollPane2.setViewportView(jTextArea2);

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(100, 100, 100));

        jLabel1.setBackground(new java.awt.Color(91, 91, 95));
        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Partidas del Presupuesto");
        jLabel1.setOpaque(true);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 759, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jLabel3.setText("Número *:");

        jSpinner1.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(1)));
        jSpinner1.setNextFocusableComponent(jComboBox2);
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

        jTextField1.setToolTipText("Ingrese Código de la Partida");
        jTextField1.setNextFocusableComponent(jSpinner1);
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField1KeyTyped(evt);
            }
        });

        jLabel2.setText("Código Covenin *:");

        jLabel5.setText("Tipo:");

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Original", "No Prevista", "Obra Extra", "Obra Adicional", "Obra Complementaria", "Variación de Precio" }));
        jComboBox2.setNextFocusableComponent(jTextArea1);
        jComboBox2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox2ItemStateChanged(evt);
            }
        });

        jLabel13.setText("Presupuesto NP:");

        jTextField2.setText("0");
        jTextField2.setNextFocusableComponent(jComboBox1);

        jLabel4.setText("Descripción:");

        jTextArea1.setColumns(22);
        jTextArea1.setFont(new java.awt.Font("Monospaced", 0, 11));
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(3);
        jTextArea1.setWrapStyleWord(true);
        jTextArea1.setNextFocusableComponent(jTextField2);
        jTextArea1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextArea1KeyTyped(evt);
            }
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextArea1KeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(jTextArea1);

        jTextField7.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField7.setText("0.00");
        jTextField7.setToolTipText("");
        jTextField7.setNextFocusableComponent(jTextField8);
        jTextField7.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField7FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField7FocusLost(evt);
            }
        });
        jTextField7.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField7KeyTyped(evt);
            }
        });

        jLabel12.setText("Cantidad:");

        jComboBox1.setNextFocusableComponent(jTextField5);

        jLabel6.setText("Grupo de Partidas:");

        jTextField5.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField5.setText("0.00");
        jTextField5.setToolTipText("");
        jTextField5.setNextFocusableComponent(jTextField13);
        jTextField5.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField5FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField5FocusLost(evt);
            }
        });
        jTextField5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField5KeyTyped(evt);
            }
        });

        jLabel11.setText("Prestaciones %:");

        jLabel19.setText("Unidad de Medida:");

        jTextField10.setToolTipText("");
        jTextField10.setNextFocusableComponent(jTextField15);
        jTextField10.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField10FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField10FocusLost(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 14));
        jLabel9.setForeground(new java.awt.Color(255, 0, 0));
        jLabel9.setText("*");

        jLabel16.setText("Precio Unitario:");

        jTextField4.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField4.setText("0.00");
        jTextField4.setToolTipText("");
        jTextField4.setNextFocusableComponent(jTextField10);
        jTextField4.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField4FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField4FocusLost(evt);
            }
        });
        jTextField4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField4KeyTyped(evt);
            }
        });

        jLabel7.setText("Administración y Gastos %:");

        jTextField6.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField6.setText("0.00");
        jTextField6.setToolTipText("");
        jTextField6.setNextFocusableComponent(jTextField4);
        jTextField6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField6ActionPerformed(evt);
            }
        });
        jTextField6.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField6FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField6FocusLost(evt);
            }
        });
        jTextField6.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField6KeyTyped(evt);
            }
        });

        jLabel8.setText("Utilidades %:");

        jTextField12.setEditable(false);
        jTextField12.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField12.setText("0.00");
        jTextField12.setToolTipText("");

        jCheckBox1.setText("Redondeo");
        jCheckBox1.setNextFocusableComponent(jTextField7);
        jCheckBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBox1ItemStateChanged(evt);
            }
        });

        jTextField15.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField15.setText("0.00");
        jTextField15.setToolTipText("");
        jTextField15.setNextFocusableComponent(jCheckBox1);
        jTextField15.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField15FocusLost(evt);
            }
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField15FocusGained(evt);
            }
        });
        jTextField15.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField15KeyTyped(evt);
            }
        });

        jLabel14.setText("Precio Asumido:");

        jTextField13.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField13.setText("1.00");
        jTextField13.setToolTipText("");
        jTextField13.setNextFocusableComponent(jTextField6);
        jTextField13.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField13FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField13FocusLost(evt);
            }
        });
        jTextField13.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField13KeyTyped(evt);
            }
        });

        jLabel17.setText("Rendimiento:");

        jLabel15.setText("Total de Partida:");

        jTextField8.setEditable(false);
        jTextField8.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField8.setToolTipText("");
        jTextField8.setNextFocusableComponent(okButton);

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/apu.png"))); // NOI18N
        jButton5.setToolTipText("APU");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/borra.png"))); // NOI18N
        jButton2.setToolTipText("Eliminar Partida");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        okButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/guardar.png"))); // NOI18N
        okButton.setToolTipText("Guardar Partida");
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

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/copia.png"))); // NOI18N
        jButton3.setToolTipText("Copiar Partida");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/copiapu.png"))); // NOI18N
        jButton4.setToolTipText("Copiar APU");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        cancelButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/eliminar.png"))); // NOI18N
        cancelButton.setToolTipText("Salir");
        cancelButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cancelButtonMouseClicked(evt);
            }
        });
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/reporte.png"))); // NOI18N
        jButton6.setToolTipText("Reporte APU");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/agregapartidas.fw.png"))); // NOI18N
        jButton1.setToolTipText("Guardar y Nueva Partida");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(192, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(165, 165, 165))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getRootPane().setDefaultButton(okButton);

        jLabel18.setText("Total de Presupuesto:");

        jTextField9.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField9.setToolTipText("");
        jTextField9.setEnabled(false);

        jLabel20.setForeground(new java.awt.Color(255, 0, 0));
        jLabel20.setText("* Campo no puede ser vacio");

        jCheckBox2.setText("Igualar a Unitario");
        jCheckBox2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBox2ItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13)
                    .addComponent(jLabel8)
                    .addComponent(jLabel17)
                    .addComponent(jLabel11)
                    .addComponent(jLabel6)
                    .addComponent(jLabel4)
                    .addComponent(jLabel2)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField4, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE)
                    .addComponent(jTextField6, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE)
                    .addComponent(jTextField13, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE)
                    .addComponent(jTextField5, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE)
                    .addComponent(jComboBox1, 0, 164, Short.MAX_VALUE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(134, 134, 134)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12)
                    .addComponent(jLabel15)
                    .addComponent(jLabel18)
                    .addComponent(jLabel14)
                    .addComponent(jLabel16)
                    .addComponent(jLabel19)
                    .addComponent(jCheckBox2))
                .addGap(39, 39, 39)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBox1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextField15, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25))
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(309, 309, 309)
                .addComponent(jLabel20)
                .addContainerGap(314, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(146, 146, 146)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox2, 0, 264, Short.MAX_VALUE)
                .addContainerGap(25, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(146, 146, 146)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 588, Short.MAX_VALUE)
                .addContainerGap(25, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel9)
                    .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel16)
                    .addComponent(jTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jTextField15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jCheckBox1)
                    .addComponent(jCheckBox2)
                    .addComponent(jTextField13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel8)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12)
                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel18)
                    .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 465, Short.MAX_VALUE)
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

 /*   public void settotalpartida(String total){
        jTextField8.setText(total);
    }*/
    public void buscaapu(){
    String codicove, numero;
    String mpres="";
        codicove = jTextField1.getText().toString();
        numero = String.valueOf(jSpinner1.getValue());
        String sql = "SELECT numero,mpre_id FROM mppres WHERE numegrup="+numero+" AND (mpre_id='"+presupuesto+"' "
                + "OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+presupuesto+"'))";
        try {
            Statement stme = (Statement) conex.createStatement();
            ResultSet rst = stme.executeQuery(sql);
            while(rst.next()){
                numero = rst.getObject(1).toString();
                mpres = rst.getString(2);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Partida.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        apu ap = new apu(null, true, conex, mpres,codicove, numero,p, jTextField13.getText().toString(),this);
       int x = (p.getWidth()/2)-475;
       int y = (p.getHeight()/2)-330;
       ap.setBounds(x, y, 950, 660);
       ap.setVisible(true);
       
       cargartotal();
       
}
public void validaDouble( java.awt.event.KeyEvent evt,String valor){
        char car = evt.getKeyChar();
        
        int repite = new StringTokenizer(valor,".").countTokens() - 1;
        if ((car<'0' || car>'9') && car!='.') {            
            evt.consume();
        }
        if(car=='.'&& repite==1){
             evt.consume();
        }
    }
    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
       
        buscaapu();

      /*   Double precunit;
         
          precunit = (Double) totalprecunit;
        if(totalprecunit==0)
        {
            precunit = (Double) totalprecasu;
        }
        Double cantidad = (Double) totalcantidad, total1;
        total1 = precunit*cantidad;
         NumberFormat formatoNumero = NumberFormat.getNumberInstance();
            formatoNumero.setMaximumFractionDigits(2);
            formatoNumero.setMinimumFractionDigits(2);
            jTextField8.setText(String.valueOf(formatoNumero.format(total1)));
            jTextField12.setText(String.valueOf(formatoNumero.format(precunit)));*/
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jSpinner1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner1StateChanged
        numpartida = jSpinner1.getValue().toString();
        if(edita!=0){
            llenavalores();
        }
        
    }//GEN-LAST:event_jSpinner1StateChanged

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
      int op = JOptionPane.showConfirmDialog(null, "¿Desea Eliminar esta Partida?");
        if(op == JOptionPane.YES_OPTION){
        String borramat = "DELETE FROM dmpres WHERE mppre_id='"+partida+"' AND mpre_id='"+presupuesto+"'";
        try {
            Statement stm = (Statement) conex.createStatement();
            stm.execute(borramat);
        } catch (SQLException ex) {
            Logger.getLogger(Partida.class.getName()).log(Level.SEVERE, null, ex);
        }
        String borraeq = "DELETE FROM deppres WHERE mppre_id='"+partida+"' AND mpre_id='"+presupuesto+"'";
        try {
            Statement stm = (Statement) conex.createStatement();
            stm.execute(borraeq);
        } catch (SQLException ex) {
            Logger.getLogger(Partida.class.getName()).log(Level.SEVERE, null, ex);
        }
        String borraman = "DELETE FROM dmoppres WHERE mppre_id='"+partida+"' AND mpre_id='"+presupuesto+"'";
        try {
            Statement stm = (Statement) conex.createStatement();
            stm.execute(borraman);
        } catch (SQLException ex) {
            Logger.getLogger(Partida.class.getName()).log(Level.SEVERE, null, ex);
        }
        String borrapart = "DELETE FROM mppres WHERE id='"+partida+"' AND mpre_id='"+presupuesto+"' OR mpre_id IN (SELECT id from mpres where mpres_id ='"+presupuesto+"' GROUP BY id)";
        try {
            Statement stm = (Statement) conex.createStatement();
            stm.execute(borrapart);
            JOptionPane.showMessageDialog(null, "Se ha eliminado la partida "+partida);
            pres.buscapartida();
            pres.cargartotal();
            doClose(RET_OK);
        } catch (SQLException ex) {
            Logger.getLogger(Partida.class.getName()).log(Level.SEVERE, null, ex);
        }
        }
    }//GEN-LAST:event_jButton2ActionPerformed

     public void setnumero (String numero){
         this.entero=Integer.parseInt(numero);
     }
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        try {
            copiarpart cop = new copiarpart(p, true, numpartida,jComboBox1.getSelectedItem().toString(), conex, partida, presupuesto, this);
            int x = (p.getWidth()/2)-325;
            int y = (p.getHeight()/2)-150;
            cop.setBounds(x, y, 650, 350);
            cop.setVisible(true);
            
            definemodelo();
            jSpinner1.setValue(String.valueOf(entero));
            numpartida = jSpinner1.getValue().toString();
            llenavalores();            
            pres.buscapartida();
        } catch (SQLException ex) {
            Logger.getLogger(Partida.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jComboBox2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox2ItemStateChanged
        if(jComboBox2.getSelectedItem().equals("No Prevista") || jComboBox2.getSelectedIndex()==2|| jComboBox2.getSelectedIndex()==3|| jComboBox2.getSelectedIndex()==4){
            jLabel13.setVisible(true);
            jTextField2.setVisible(true);
        }else{
            jLabel13.setVisible(false);
            jTextField2.setVisible(false);
        }
        
    }//GEN-LAST:event_jComboBox2ItemStateChanged

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        copiarapu apu = new copiarapu(p, true, numpartida,partida, presupuesto, conex);
         int x=(p.getWidth()/2)-175;
            int y=(p.getHeight()/2-150);
            apu.setBounds(x, y, 350,300);            
           apu.setVisible(true);
           buscaapu();
           
        cargartotal();
        
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jTextArea1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextArea1KeyPressed

        if (evt.getKeyCode() == KeyEvent.VK_TAB) {
            if (evt.getModifiers() == KeyEvent.SHIFT_MASK) {
                ((javax.swing.JTextArea) evt.getSource()).transferFocusBackward();
            } else {
                ((javax.swing.JTextArea) evt.getSource()).transferFocus();
            }
            evt.consume();
        }   
        
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextArea1KeyPressed

    private void jTextField6FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField6FocusLost
if(jTextField6.getText().equals(""))
    jTextField6.setText(val);      
 
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField6FocusLost

    private void jTextField7FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField7FocusLost
        BigDecimal precunit;
          precunit =  totalprecunit;
          System.out.println("precunit "+totalprecunit);

        if(totalprecunit.doubleValue()==0)
        {
            precunit = totalprecasu;
        }
        int nomodifica=0;
        if(jTextField7.getText().equals("")){
            jTextField7.setText(val);
            nomodifica=1;
        }
         BigDecimal cantidad =new BigDecimal("0.00");
        if(edita==0)
       cantidad= new BigDecimal(jTextField7.getText().toString());
        else{
            if(nomodifica==1)
            cantidad = totalcantidad;
            else
                cantidad= new BigDecimal(jTextField7.getText().toString());
        }
         BigDecimal  total1;
         if(totalprecasu.doubleValue()!=0.00)
        total1 = redondear.redondearDosDecimales(totalprecasu.multiply(cantidad));
         else
             total1= redondear.redondearDosDecimales(precunit.multiply(cantidad));
        
        NumberFormat formatoNumero = NumberFormat.getNumberInstance();
            formatoNumero.setMaximumFractionDigits(2);
            formatoNumero.setMinimumFractionDigits(2);
            totalcantidad=cantidad;
            jTextField7.setText(String.valueOf(formatoNumero.format(totalcantidad)));
            jTextField8.setText(String.valueOf(formatoNumero.format(total1)));
      totaltotal=total1;
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField7FocusLost

    private void jSpinner1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jSpinner1KeyTyped
      
        int code = evt.getKeyCode();
        char car = evt.getKeyChar();
        if ((car<'0' || car>'9')) 
        {
            
            evt.consume();
        }
        
    }//GEN-LAST:event_jSpinner1KeyTyped

    private void jTextField5KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField5KeyTyped
validaDouble(evt, jTextField5.getText().toString());         // TODO add your handling code here:
    }//GEN-LAST:event_jTextField5KeyTyped

    private void jTextField13KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField13KeyTyped
validaDouble(evt, jTextField13.getText().toString());         // TODO add your handling code here:
    }//GEN-LAST:event_jTextField13KeyTyped

    private void jTextField6KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField6KeyTyped
validaDouble(evt, jTextField6.getText().toString());         // TODO add your handling code here:
    }//GEN-LAST:event_jTextField6KeyTyped

    private void jTextField4KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField4KeyTyped
validaDouble(evt, jTextField4.getText().toString());         // TODO add your handling code here:
    }//GEN-LAST:event_jTextField4KeyTyped

    private void jTextField15KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField15KeyTyped
validaDouble(evt, jTextField15.getText().toString());         // TODO add your handling code here:
    }//GEN-LAST:event_jTextField15KeyTyped

    private void jTextField7KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField7KeyTyped
validaDouble(evt, jTextField7.getText().toString());         // TODO add your handling code here:
    }//GEN-LAST:event_jTextField7KeyTyped

    public boolean compruebaCampos(){
        boolean comprueba=false;
        if(jTextField1.getText().equals(""))
            comprueba=false;
        else
            comprueba=true;
        return comprueba;
    }
    public void guardarPartida()        
    {
        if(!compruebaCampos())
        {
            jLabel9.setVisible(true);   
            jLabel20.setVisible(true);
            JOptionPane.showMessageDialog(rootPane, "Campos Claves vacios, llene los campos vacios.");
            return;
        }else{
            jLabel9.setVisible(false);   
            jLabel20.setVisible(false);
        }
        String mpre_id=presupuesto, ids=jTextField1.getText().toString();
        String numero = "0", descri = jTextArea1.getText().toString();
        int idband = jComboBox1.getSelectedIndex()+1;
        String porcgad = jTextField4.getText().toString(), porcpre = jTextField5.getText().toString();
        String porcutil = jTextField6.getText().toString(), precasu = String.valueOf(jTextField15.getText());
        String precunit = String.valueOf(totalprecunit), rendimi = jTextField13.getText().toString();
        String unidad = jTextField10.getText().toString(), redondeo, cantidad = String.valueOf(totalcantidad);
        String tipos = null;
        String adicional = jTextField2.getText();
        if(edita==1){
            BigDecimal precuni = new BigDecimal(precunit);            
                precasu="0";
                if(jCheckBox1.isSelected()){
                    precasu = precuni.toString();
                    System.out.println("precasu"+precasu);
                }
                precasu = String.valueOf(jTextField15.getText());
            String sqls = "SELECT numero FROM mppres where numegrup ="+jSpinner1.getValue()+" And (mpre_id='"+presupuesto+"'"
                    + " OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+presupuesto+"'))";
                try {
                Statement st = (Statement) conex.createStatement();
                ResultSet rst = st.executeQuery(sqls);
                numero="0";
                while(rst.next()){
                    numero = rst.getObject(1).toString();
                }
                if(numero.equals("0"))
                    
                {
                    numero="1";
                }
            } catch (SQLException ex) {
            Logger.getLogger(Partida.class.getName()).log(Level.SEVERE, null, ex);
            }
            }
        String tiponp=null;
        if(jComboBox2.getSelectedItem().equals("Original")) {
            tipos = "Org";
        }
        if(jComboBox2.getSelectedItem().equals("No Prevista")) {
            tipos = "NP";
            tiponp="NP";
        }
        if(jComboBox2.getSelectedItem().equals("Obra Extra")) {
            tipos = "NP";
            tiponp = "OE";
        }
        if(jComboBox2.getSelectedItem().equals("Obra Adicional")) {
            tipos = "NP";
            tiponp = "OA";
        }
        if(jComboBox2.getSelectedItem().equals("Obra Complementaria")) {
            tipos = "NP";
            tiponp = "OC";
        }
        if(jComboBox2.getSelectedItem().equals("Variación de Precio")) {
            
            tipos = "VP";
        }
        
        if(jCheckBox1.isSelected()) {
            redondeo="1";
        }
        else {
            redondeo="0";
        }
        if(edita==0){
            int numegrup=0;
            String sqls = "SELECT numero FROM mppres WHERE (mpre_id='"+mpre_id+"' OR mpre_id IN "
                    + "(SELECT id FROM mpres WHERE mpres_id='"+mpre_id+"')) "
                    + "ORDER BY numero DESC LIMIT 1";
                try {
                Statement sts = (Statement) conex.createStatement();
                ResultSet rsts = sts.executeQuery(sqls);
                numero="0";
                int number=0;
                while(rsts.next()){
                    numero = rsts.getObject(1).toString();
                    number = Integer.parseInt(numero)+1;
                    numero = String.valueOf(number);
                }
                
                String snume = "SELECT numegrup FROM mppres WHERE (mpre_id='"+mpre_id+"' OR mpre_id IN "
                        + "(SELECT id FROM mpres WHERE mpres_id='"+mpre_id+"')) "
                        + "ORDER BY numegrup DESC LIMIT 1";
                Statement sts1 = (Statement) conex.createStatement();
                ResultSet rsts1 = sts1.executeQuery(snume);
             
               
                while(rsts1.next()){
                    numegrup = rsts1.getInt(1)+1;                    
                }
            if(numero.equals("0")){
                numero="1";
            }
            if(numegrup==0){
                numegrup=1;
            }
            if(tipos.equals("NP")){
                String codpres = mpre_id+tiponp+adicional;
                int cuenta=0;
                String consultadi = "SELECT COUNT(*) FROM mpres WHERE id='"+codpres+"'";
                Statement stadi = (Statement) conex.createStatement();
                ResultSet rstadi = stadi.executeQuery(consultadi);
                while(rstadi.next()){
                    cuenta = rstadi.getInt(1);
                }
                if(cuenta==0){
                    String insertare = "INSERT INTO mpres "
                    + "SELECT '"+codpres+"', nomabr, nombre, ubicac, fecini,"
                    + "fecfin, feccon, fecimp, porgam, porcfi, porimp, poripa,"
                    + "porpre, poruti, codpro, codcon, parpre, nrocon, nroctr, fecapr,"
                    + "nrolic, 1, '"+mpre_id+"',memo,timemo, fecmemo, 0, partidapres, "
                    + "fecharegistro, valu  FROM mpres WHERE id='"+mpre_id+"'";

                    // System.out.println("Inserta Presupuesto Adicional: "+insertar);
                    Statement insert = (Statement) conex.createStatement();
                    insert.execute(insertare);
                }
                 String sql = "INSERT INTO mppres (mpre_id, id, numero, numegrup, descri, idband, porcgad, porcpre"
                    + ", porcutil, precasu, precunit, rendimi, unidad, redondeo, cantidad, tipo, status,nropresupuesto,tiponp)"
                    + " VALUE ('"+codpres+"','"+ids+"', "+numero+", "+numegrup+", '"+descri+"', "+idband+","
                    + " "+porcgad+", "+porcpre+","+porcutil+", "+precasu+", "+precunit+", "+rendimi+", '"+unidad+"',"
                    + ""+redondeo+", "+cantidad+", '"+tipos+"', 1,'"+adicional+"','"+tiponp+"')";
            
                Statement st = (Statement) conex.createStatement();
                st.execute(sql);
            }else{
            String sql = "INSERT INTO mppres (mpre_id, id, numero, numegrup, descri, idband, porcgad, porcpre"
                    + ", porcutil, precasu, precunit, rendimi, unidad, redondeo, cantidad, tipo, status,nropresupuesto)"
                    + " VALUE ('"+mpre_id+"','"+ids+"', "+numero+", "+numegrup+", '"+descri+"', "+idband+","
                    + " "+porcgad+", "+porcpre+","+porcutil+", "+precasu+", "+precunit+", "+rendimi+", '"+unidad+"',"
                    + ""+redondeo+", "+cantidad+", '"+tipos+"', 1,'"+adicional+"')";
            
                Statement st = (Statement) conex.createStatement();
                st.execute(sql);
            }
                  String cuentas = "SELECT count(*) FROM mptabs WHERE mtabus_id='PRUEBA' AND codicove='"+ids+"'";
                  Statement stcuentas = (Statement) conex.createStatement();
                  ResultSet rstcuenta = stcuentas.executeQuery(cuentas);
                  int si=0;
                  while(rstcuenta.next()){
                      si = rstcuenta.getInt(1);
                  }
                  if(si==0){
                int op =JOptionPane.showConfirmDialog(null, "¿Desea Agregar esta partida en Tabulador de Prueba?", "Tabulador", JOptionPane.YES_NO_OPTION);
                if(op==JOptionPane.YES_OPTION){
                    String selectcount = "SELECT count(*) FROM mtabus WHERE id='PRUEBA'";
                    Statement stcount = (Statement) conex.createStatement();
                    ResultSet rst = stcount.executeQuery(selectcount);
                    int cuenta =0;
                    while(rst.next()){
                        cuenta = rst.getInt(1);
                    }
                    if(cuenta==0){
                        String inserta = "INSERT INTO mtabus (id, descri, vigencia, status) VALUES "
                                + "('PRUEBA', 'PARTIDAS A SER INSERTADAS EN SIGUIENTES TABULADORES', NOW(), 1)";
                        Statement stinsert = (Statement) conex.createStatement();
                        stinsert.execute(inserta);
                        JOptionPane.showMessageDialog(rootPane, "Tabulador de prueba agregado, modifique los valores necesarios");
                    }
                  String insertapart = "INSERT INTO mptabs (codicove,numero,numegrup,descri, mbdat_id,porcgad, porcpre, porcutil, "
                          + "precasu, precunit, rendimi, unidad, redondeo, status, mtabus_id, cantidad,capitulo)"
                          + " SELECT id, numero, numegrup, descri,idband, porcgad, porcpre, porcutil, precasu, precunit, rendimi, "
                          + "unidad, redondeo, 1, 'PRUEBA', cantidad, capitulo FROM mppres WHERE numero="+numero+" AND"
                          + " (mpre_id='"+mpre_id+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+mpre_id+"'))";
                  Statement stinserta = (Statement) conex.createStatement();
                  stinserta.execute(insertapart);
                }
                  }
               pres.buscapartida();   
                JOptionPane.showMessageDialog(rootPane, "Se ha insertado la Partida");                
            } catch (SQLException ex) {
                Logger.getLogger(Partida.class.getName()).log(Level.SEVERE, null, ex);
            }
           
        }else{
            String codpres=mpre_id;
             if(tipos.equals("NP"))
                codpres = mpre_id+tiponp+adicional;
             String sql="";
             if(!tipos.equals("VP"))                 
             sql= "UPDATE mppres SET mpre_id='"+codpres+"', id='"+jTextField1.getText()+"',descri= '"+descri+"', idband="+idband+", porcgad="+porcgad+""
                    + ", porcpre="+porcpre+", porcutil="+porcutil+", precasu="+precasu+", precunit="+precunit+""
                    + ", rendimi="+rendimi+", unidad='"+unidad+"', redondeo="+redondeo+", nropresupuesto='"+adicional+"',"
                    + "cantidad="+cantidad+", tipo='"+tipos+"', tiponp='"+tiponp+"', status=1"
                    + " WHERE (mpre_id='"+mpre_id+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+mpre_id+"')) "
                    + " AND numero="+numero;
             else
                 sql= "UPDATE mppres SET id='"+jTextField1.getText()+"',descri= '"+descri+"', idband="+idband+", porcgad="+porcgad+""
                    + ", porcpre="+porcpre+", porcutil="+porcutil+", precasu="+precasu+", precunit="+precunit+""
                    + ", rendimi="+rendimi+", unidad='"+unidad+"', redondeo="+redondeo+", nropresupuesto='"+adicional+"',"
                    + "cantidad="+cantidad+", tipo='"+tipos+"', tiponp='"+tiponp+"', status=1"
                    + " WHERE (mpre_id='"+mpre_id+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+mpre_id+"')) "
                    + " AND numero="+numero;
            System.out.println("actualiza "+sql);
            try {
                Statement st = (Statement) conex.createStatement();
                st.execute(sql);
                JOptionPane.showMessageDialog(rootPane, "Se ha modificado la Partida");
            } catch (SQLException ex) {
                Logger.getLogger(Partida.class.getName()).log(Level.SEVERE, null, ex);
            } 
        }
        
}
    
    private void vaciaCampo(JTextField field){
        val = field.getText().toString();
        field.setText("");
    }
private void okButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_okButtonMouseClicked
    guardarPartida();
    pres.numPartidas();
     //doClose(RET_OK);
    // TODO add your handling code here:
}//GEN-LAST:event_okButtonMouseClicked

    private void jTextField7FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField7FocusGained
        vaciaCampo(jTextField7);
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField7FocusGained

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_okButtonActionPerformed

    private void jTextField15FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField15FocusLost
        if(jTextField15.getText().equals(""))
            jTextField15.setText(val);
        
        totalprecasu = new BigDecimal(jTextField15.getText().toString());
        
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField15FocusLost

   
private void jTextField1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyTyped
vali.validaText(evt);
vali.sizeField("id", "mppres", evt, jTextField1.getText());
}//GEN-LAST:event_jTextField1KeyTyped

private void jTextArea1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextArea1KeyTyped
  vali.validaText(evt);
vali.sizeField("descri", "mppres", evt, jTextField1.getText());
}//GEN-LAST:event_jTextArea1KeyTyped

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        String nume=jSpinner1.getValue().toString();
        String presu="SELECT mpre_id FROM mppres WHERE (mpre_id='"+presupuesto+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+presupuesto+"'))"
                + " AND numegrup="+nume+"";
        String mpres="";
        try {
            Statement st=(Statement) conex.createStatement();
            ResultSet rst = st.executeQuery(presu);
            while(rst.next()){
                mpres=rst.getString(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Partida.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        this.setModal(false);
        reporteapu apu = new reporteapu(p, false, conex, mpres, nume,this);
        int xy = (p.getWidth()/2)-450/2;
        int yy = (p.getHeight()/2)-300/2;
        apu.setBounds(xy,yy, 450,300);
        apu.setVisible(true);
        doClose(RET_OK);
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jCheckBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBox1ItemStateChanged
        BigDecimal unitario = totalprecunit;
        BigDecimal redondeado = new BigDecimal(Math.rint(unitario.floatValue()));
        if(unitario.doubleValue()==0)
            redondeado = new BigDecimal(Math.rint(totalprecasu.floatValue()));
        BigDecimal cantidad = totalcantidad;
        if(jCheckBox1.isSelected()){
            jTextField15.setText(String.valueOf(redondeado));
            jTextField8.setText((cantidad.multiply(redondeado)).toString());
        }
        else {
            jTextField8.setText(redondear.redondearDosDecimales(cantidad.multiply(unitario)).toString());
            jTextField15.setText(totalprecunit.toString());
        }             // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox1ItemStateChanged

private void cancelButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cancelButtonMouseClicked
pres.cargartotal();

    
    // TODO add your handling code here:
}//GEN-LAST:event_cancelButtonMouseClicked

private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    int auxedita=edita;
    edita=0;
    totalprecasu=totalprecunit=new BigDecimal("0.00");
    jLabel13.setVisible(false);
        jTextField2.setVisible(false);
        jLabel9.setVisible(false);
        jLabel20.setVisible(false);
        this.setTitle("Nueva Partida");
        id = new ArrayList<Integer>();
        if(auxedita==0)
        guardarPartida();
        try {
            
            contar();
        } catch (SQLException ex) {
            Logger.getLogger(Partida.class.getName()).log(Level.SEVERE, null, ex);
        }
            
            jTextField9.setText(total);
            if(jComboBox2.getSelectedItem().equals("No Prevista")){
                jLabel13.setVisible(false);
                jTextField2.setVisible(false);
            }       
           
            jButton2.setEnabled(false);
            jButton3.setEnabled(false);
            jButton4.setEnabled(false);
            jButton5.setEnabled(false);
            jButton6.setEnabled(false);
            cargavalorespres();
            vaciaCampos();
    // TODO add your handling code here:
}//GEN-LAST:event_jButton1ActionPerformed

private void jTextField5FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField5FocusGained
    vaciaCampo(jTextField5);
    
    // TODO add your handling code here:
}//GEN-LAST:event_jTextField5FocusGained

private void jTextField13FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField13FocusGained
    vaciaCampo(jTextField13);
    // TODO add your handling code here:
}//GEN-LAST:event_jTextField13FocusGained

private void jTextField6FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField6FocusGained
    vaciaCampo(jTextField6);
    // TODO add your handling code here:
}//GEN-LAST:event_jTextField6FocusGained

private void jTextField4FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField4FocusGained
vaciaCampo(jTextField4);
    // TODO add your handling code here:
}//GEN-LAST:event_jTextField4FocusGained

private void jTextField10FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField10FocusGained
vaciaCampo(jTextField10);
    // TODO add your handling code here:
}//GEN-LAST:event_jTextField10FocusGained

private void jTextField15FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField15FocusGained
vaciaCampo(jTextField15);
    // TODO add your handling code here:
}//GEN-LAST:event_jTextField15FocusGained

private void jTextField5FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField5FocusLost
    if(jTextField5.getText().equals(""))
    jTextField5.setText(val);
    // TODO add your handling code here:
}//GEN-LAST:event_jTextField5FocusLost

private void jTextField6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField6ActionPerformed
    jTextField6.setText(val);
    // TODO add your handling code here:
}//GEN-LAST:event_jTextField6ActionPerformed

private void jTextField4FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField4FocusLost
    if(jTextField4.getText().equals(""))
    jTextField4.setText(val);
    // TODO add your handling code here:
}//GEN-LAST:event_jTextField4FocusLost

private void jTextField10FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField10FocusLost
    if(jTextField10.getText().equals(""))
    jTextField10.setText(val);
    // TODO add your handling code here:
}//GEN-LAST:event_jTextField10FocusLost

private void jTextField13FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField13FocusLost
if(jTextField13.getText().equals(""))
    jTextField13.setText(val);
    // TODO add your handling code here:
}//GEN-LAST:event_jTextField13FocusLost

private void jCheckBox2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBox2ItemStateChanged
   if(jCheckBox2.isSelected())
    jTextField15.setText(totalprecunit.toString());
   else
       jTextField15.setText("0.00");
    
}//GEN-LAST:event_jCheckBox2ItemStateChanged
    public void vaciaCampos()
    {
        jTextField1.setText("");
        jTextArea1.setText("");
        jTextField10.setText("");
        jTextField12.setText("0.00");
        jTextField15.setText("0.00");
        jTextField7.setText("0.00");
        jTextField8.setText("0.00");
    }
    public void doClose(int retStatus) {
        pres.numPartidas();
        if(reconsidera==1)
            recon.buscacuadro();
        try {
           // pres.setid(presupuesto);
            pres.buscapartida();
        } catch (SQLException ex) {
            Logger.getLogger(Partida.class.getName()).log(Level.SEVERE, null, ex);
        }
        returnStatus = retStatus;
        setVisible(false);
        dispose();
    }

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JLabel jLabel1;
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
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField12;
    private javax.swing.JTextField jTextField13;
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
