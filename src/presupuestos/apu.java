/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * apu.java
 *
 * Created on 13/02/2013, 09:39:31 PM
 */
package presupuestos;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSetMetaData;
import com.mysql.jdbc.Statement;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
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
public class apu extends javax.swing.JDialog {

    /** A return status code - returned if Cancel button has been pressed */
    public static final int RET_CANCEL = 0;
    /** A return status code - returned if OK button has been pressed */
    public static final int RET_OK = 1;
    String partida, pres, numero;
    Connection conex;
    private float contmat=0;
    int insertare=0;
    private float contequip=0;
    private float contmano=0;
    float cantidad=0, precio=0, desperdi=0;
    String descri;
    String prest="0", util="0", fina="0", adm="0", imp="0", impart="0";
    float rendimiento=0;
    int filamat;
    float cantmano=0;
    Principal prin;
    private float contototal;
    Partida part;
    private int filaequip;
    
    private int filamano;
    public apu(java.awt.Frame parent, boolean modal, Connection conex, String pres, String partida, String numero, Principal p, String rendimi, Partida part) {
        super(parent, false);
        initComponents();
        this.conex = conex;
        this.pres = pres;
        this.numero = numero; //Es numegrup
        
        this.partida = partida;
        this.part = part;
        this.prin = p;
        this.rendimiento= Float.valueOf(rendimi);
        cargartabus();
        buscapres();
        buscamat();
        buscaequip();
        buscamano();
        calculapartida();
        jTable1.setShowHorizontalLines(true);
        jTable1.setShowVerticalLines(false);
        jTable1.getTableHeader().setSize(new Dimension(25,40));
        jTable1.getTableHeader().setPreferredSize(new Dimension(30,40));
        jTable1.setRowHeight(20);
        jTable2.setShowHorizontalLines(true);
        jTable2.setShowVerticalLines(false);
        jTable2.getTableHeader().setSize(new Dimension(25,40));
        jTable2.getTableHeader().setPreferredSize(new Dimension(30,40));
        jTable2.setRowHeight(20);
        jTable3.setShowHorizontalLines(true);
        jTable3.setShowVerticalLines(false);
        jTable3.getTableHeader().setSize(new Dimension(25,40));
        jTable3.getTableHeader().setPreferredSize(new Dimension(30,40));
        jTable3.setRowHeight(20);
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
    public void vernumero(){
        String select = "SELECT numero FROM mppres WHERE (mpre_id='"+pres+"' AND "
                + "mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) AND "
                + "numegrup="+numero+"";
        try {
            Statement stselect = (Statement) conex.createStatement();
            ResultSet rstselect = stselect.executeQuery(select);
            while(rstselect.next()){
                numero=rstselect.getString(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(apu.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    public final void cargartabus(){
        try {
            DefaultTableModel cont = new DefaultTableModel();
            jComboBox1.removeAllItems();
            String selec="", item="";
            int seleccionado=0;
                Statement s = (Statement) conex.createStatement();
                ResultSet rs = s.executeQuery("SELECT id, seleccionado FROM mtabus WHERE status=1");
                
                while(rs.next()){
                    item =rs.getString("id"); 
                    jComboBox1.addItem(item);
                    seleccionado = rs.getInt("seleccionado");
                    if(seleccionado==1){
                        selec=item;
                    }
                }
             
               jComboBox1.setSelectedItem(selec);
               
        } catch (SQLException ex) {
            Logger.getLogger(materiales.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public final void buscapres(){
        String admi=null,presta=null,utili=null;
        String sql = "SELECT porgam, porcfi, porimp, poripa, porpre, poruti FROM mpres WHERE id='"+pres+"'";
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
            String datospart = "SELECT porcgad, porcpre,porcutil FROM mppres WHERE numero="+numero+" AND "
                    + "(mpre_id = '"+pres+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"'))";
            Statement sts = (Statement) conex.createStatement();
            ResultSet rsts = sts.executeQuery(datospart);
            while(rsts.next()){
                admi = rsts.getString("porcgad");
                presta = rsts.getString("porcpre");
                utili = rsts.getString("porcutil");
            }
            if(admi!=null){
                adm=admi;
            }
            if(presta!=null)
            {
                prest=presta;
            }
            if(utili!=null)
            {
                util=utili;
            }
            System.out.println("Adm "+adm+", "+fina+" "+imp+" "+impart+" "+prest+" "+util);
        } catch (SQLException ex) {
            Logger.getLogger(apu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public final void buscamat(){
        float valor=0;
        contmat =0;
        float aux=0;
        System.out.println("cadena"+pres+" num"+numero);
        Statement part1=null;
        try {
            part1 = (Statement) conex.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(apu.class.getName()).log(Level.SEVERE, null, ex);
        }
        DefaultTableModel mmtabs = new DefaultTableModel(){@Override
     public boolean isCellEditable (int row, int column) {
       
         return false;
     }
        
        
       
       
 };
        String sql = "SELECT mm.id, mm.descri, mm.precio, dm.cantidad, mm.unidad, "
                + "mm.desperdi, ((mm.precio+(mm.precio*(mm.desperdi/100)))*dm.cantidad) as total FROM "
                + "mmpres as mm, dmpres as dm WHERE "
                + "(dm.mpre_id='"+pres+"' OR dm.mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) "
                + "AND dm.numepart='"+numero+"'"
                + " AND dm.mmpre_id=mm.id AND dm.mpre_id=mm.mpre_id GROUP BY mm.id";
        jTable1.setModel(mmtabs);
        try {
            Statement s1 = (Statement) conex.createStatement();
           
            ResultSet rs1 = s1.executeQuery(sql);
            
            
            ResultSetMetaData rsMd = (ResultSetMetaData) rs1.getMetaData();
            int cantidadColumnas = rsMd.getColumnCount();
             for (int i = 1; i <= cantidadColumnas; i++) {
                 mmtabs.addColumn(rsMd.getColumnLabel(i));
            }
             
             while (rs1.next()) {
                 Object[] fila = new Object[cantidadColumnas];
                for (int i = 0; i < cantidadColumnas; i++) {
                    if(i==6){
                       
                            
                            valor = (float) (Math.rint(Float.valueOf(rs1.getObject(i+1).toString())*100)/100);
                            fila[i]=Float.toString(valor);
                    
                       contmat += valor; 
                    }else
                       fila[i]=rs1.getObject(i+1);
                    
                    
                }
                mmtabs.addRow(fila);
                
            }
             aux = (float) (Math.rint((contmat+0.000001)*100)/100);
        contmat=aux;
               String esnan = String.valueOf(aux);
               System.out.println("contmat "+esnan);
                jTextField2.setText(String.valueOf(aux));
        } catch (SQLException ex) {
            System.out.println("Noooo");
            Logger.getLogger(apu.class.getName()).log(Level.SEVERE, null, ex);
        }
    
        cambiacabeceramat();
    }
     
     
       private void cambiacabeceramat() {
         JTableHeader th = jTable1.getTableHeader();
       TableColumnModel tcm = th.getColumnModel();
       TableColumn tc = tcm.getColumn(0); 
       
       tc.setHeaderValue("Código");
       tc.setPreferredWidth(20);
       tc = tcm.getColumn(1); 
        tc.setHeaderValue("Descripción");
        tc.setPreferredWidth(100);
        tc = tcm.getColumn(2); 
        tc.setHeaderValue("Precio");
        tc.setPreferredWidth(20);
        tc = tcm.getColumn(3); 
        tc.setHeaderValue("Cantidad");
        tc.setPreferredWidth(20);
        tc = tcm.getColumn(4); 
        tc.setHeaderValue("Unidad");
        tc.setPreferredWidth(20);
        tc = tcm.getColumn(5); 
        tc.setHeaderValue("Desperdicio");
        tc.setPreferredWidth(20);
       tc = tcm.getColumn(6); 
        tc.setHeaderValue("Total");
        tc.setPreferredWidth(20);
       th.repaint(); 
        }
       
       
        public final void buscaequip(){
        float valor=0;
         float aux =0;
        contequip=0;
        System.out.println("cadena"+pres+" num"+numero);
        Statement parti=null;
        try {
            parti = (Statement) conex.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(apu.class.getName()).log(Level.SEVERE, null, ex);
        }
        DefaultTableModel mmtabs = new DefaultTableModel(){@Override
     public boolean isCellEditable (int row, int column) {
       
         return false;
     }
        
        
       
       
 };
        jTable2.setModel(mmtabs);
        try {
            Statement s1 = (Statement) conex.createStatement();
           String consulta="SELECT mm.id, mm.descri, mm.precio, dm.cantidad, "
                    + "mm.deprecia, (mm.deprecia*dm.cantidad*mm.precio) as total "
                    + "FROM mepres as mm, deppres as dm WHERE "
                   + "(dm.mpre_id='"+pres+"' OR dm.mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"'))"
                   + " AND"
                    + " dm.numero='"+numero+"' AND dm.mepre_id=mm.id AND "
                   + "dm.mpre_id=mm.mpre_id GROUP BY mm.id";
            ResultSet rs1 = s1.executeQuery(consulta);
            
            ResultSetMetaData rsMd = (ResultSetMetaData) rs1.getMetaData();
            int cantidadColumnas = rsMd.getColumnCount();
             for (int i = 1; i <= cantidadColumnas; i++) {
                 mmtabs.addColumn(rsMd.getColumnLabel(i));
            }
             
             while (rs1.next()) {
                 Object[] fila = new Object[cantidadColumnas];
                for (int i = 0; i < cantidadColumnas; i++) {
                    if(i==5){
                        if(Float.valueOf(rs1.getObject(5).toString())==0.00){
                            valor = (float) (Math.rint(Float.valueOf(rs1.getObject(3).toString())*Float.valueOf(rs1.getObject(4).toString())*100)/100);
                            fila[i]= Float.toString(valor);
                        }else{
                            
                            valor = (float) (Math.rint(Float.valueOf(rs1.getObject(i+1).toString())*100)/100);
                            fila[i]=Float.toString(valor);
                        }
                       contequip += valor; 
                    }else
                       fila[i]=rs1.getObject(i+1);
                    
                    
                }
                contequip = (float) (Math.rint((contequip+0.000001)*100)/100);
                
                
                mmtabs.addRow(fila);
                        
                
            }
             if(rendimiento==0)
                 rendimiento=1;
             contequip = contequip/rendimiento;
            aux = (float) (Math.rint((contequip+0.000001)*100)/100);
            contequip=aux;
            System.out.println("conteq "+aux);
        } catch (SQLException ex) {
            System.out.println("Noooo");
            Logger.getLogger(apu.class.getName()).log(Level.SEVERE, null, ex);
        }
                jTextField3.setText(String.valueOf(rendimiento));
               jTextField4.setText(String.valueOf(aux)); 
        cambiacabeceraequip();
    }
     
     
       private void cambiacabeceraequip() {
         JTableHeader th = jTable2.getTableHeader();
       TableColumnModel tcm = th.getColumnModel();
       TableColumn tc = tcm.getColumn(0); 
       
       tc.setHeaderValue("Código");
       tc.setPreferredWidth(20);
       tc = tcm.getColumn(1); 
        tc.setHeaderValue("Descripción");
        tc.setPreferredWidth(100);
        tc = tcm.getColumn(2); 
        tc.setHeaderValue("Precio");
        tc.setPreferredWidth(20);
        tc = tcm.getColumn(3); 
        tc.setHeaderValue("Cantidad");
        tc.setPreferredWidth(20);
        tc = tcm.getColumn(4); 
        
        tc.setHeaderValue("Depreciación");
        tc.setPreferredWidth(20);
       tc = tcm.getColumn(5); 
        tc.setHeaderValue("Total");
        tc.setPreferredWidth(20);
       th.repaint(); 
    }
       
        
        public final void buscamano(){
         float bono = 0, subsid = 0, presta;
        float valor=0;
        contmano =0;
        System.out.println("cadena"+pres+" num"+numero);
        Statement parti=null;
        try {
            parti = (Statement) conex.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(apu.class.getName()).log(Level.SEVERE, null, ex);
        }
        DefaultTableModel mmtabs = new DefaultTableModel(){@Override
     public boolean isCellEditable (int row, int column) {
       
         return false;
     }
        
        
       
       
 };
        jTable3.setModel(mmtabs);
        try {
            Statement s1 = (Statement) conex.createStatement();
           String sql= "SELECT mm.id, mm.descri, dm.cantidad, mm.bono,"
                   + " mm.subsid, mm.salario,"
                    + " (dm.cantidad*mm.salario) as total "
                   + "FROM mmopres as mm, dmoppres as dm "
                    + " WHERE (dm.mpre_id='"+pres+"' OR dm.mpre_id IN "
                   + "(SELECT id FROM mpres WHERE mpres_id='"+pres+"')) "
                   + "AND dm.numero='"+numero+"' "
                    + " AND dm.mmopre_id=mm.id AND dm.mpre_id=mm.mpre_id "
                    + "GROUP BY mm.id";
            ResultSet rs1 = s1.executeQuery(sql);
            
            ResultSetMetaData rsMd = (ResultSetMetaData) rs1.getMetaData();
            int cantidadColumnas = rsMd.getColumnCount();
             for (int i = 1; i <= cantidadColumnas; i++) {
                 mmtabs.addColumn(rsMd.getColumnLabel(i));
            }
             
             while (rs1.next()) {
                 Object[] fila = new Object[cantidadColumnas];
                for (int i = 0; i < cantidadColumnas; i++) {
                    if(i==3&& rs1.getObject(4)!=null)
                        bono = Float.valueOf(rs1.getObject(4).toString());
                    if(i==4&& rs1.getObject(5)!=null)
                        subsid = Float.valueOf(rs1.getObject(5).toString());
                    
                    
                    if(i==6){
                        
                            valor+= rs1.getFloat(7);
                            fila[i] =Math.rint(rs1.getFloat(7)*100)/100;
                           
                    }
                        if(i==2){
                            cantmano += rs1.getFloat(3);
                           
                        }
                       
                    if(i!=6){
                       fila[i]=rs1.getObject(i+1);
                    }
                    
                    
                }
               
                mmtabs.addRow(fila);
              
                
            }
             
        } catch (SQLException ex) {
            System.out.println("Noooo");
            Logger.getLogger(apu.class.getName()).log(Level.SEVERE, null, ex);
        }
         
           
          contmano = (float) Math.rint(valor*100)/100; 
          System.out.println("contmano en apu "+contmano);
        presta = contmano* (Float.valueOf(prest)/100);
        presta = (float) (Math.rint(Float.valueOf(presta)*100)/100);
         System.out.println("Presta "+presta);
          System.out.println("Bono antes de multiplicar "+bono);
        bono = cantmano * bono;
        bono = (float) (Math.rint(Float.valueOf(bono)*100)/100);
         System.out.println("cantmano "+cantmano);
         System.out.println("Bono "+bono);
         System.out.println("Subsid antes de multiplicar "+subsid);
        subsid = cantmano * subsid;
       
         subsid= (float) (Math.rint(Float.valueOf(subsid)*100)/100);
         System.out.println("subsidio "+subsid);
        contmano = contmano + presta +bono +subsid;
         System.out.println("contmano "+contmano);
         if(rendimiento==0)
                 rendimiento=1;
        contmano = contmano / rendimiento;
        
        float aux= (float) (Math.rint((contmano+0.000001)*100)/100);
        contmano=aux;
        System.out.println("total mano/rendimi "+aux);
        jTextField5.setText(String.valueOf(rendimiento));
        jTextField6.setText(String.valueOf(aux));
        cambiacabeceramano();
    }
     
     
       private void cambiacabeceramano() {
         JTableHeader th = jTable3.getTableHeader();
       TableColumnModel tcm = th.getColumnModel();
       TableColumn tc = tcm.getColumn(0); 
       
       tc.setHeaderValue("Código");
       tc.setPreferredWidth(20);
       tc = tcm.getColumn(1); 
        tc.setHeaderValue("Descripción");
        tc.setPreferredWidth(100);
        tc = tcm.getColumn(2); 
        tc.setHeaderValue("Cantidad");
        tc.setPreferredWidth(20);
        tc = tcm.getColumn(3); 
        tc.setHeaderValue("Bono");
        tc.setPreferredWidth(20);
        tc = tcm.getColumn(4); 
        
        tc.setHeaderValue("Subsidio");
        tc.setPreferredWidth(20);
       tc = tcm.getColumn(5); 
        tc.setHeaderValue("Salario");
        tc.setPreferredWidth(20);
        tc = tcm.getColumn(6); 
        tc.setHeaderValue("Total");
        tc.setPreferredWidth(20);
       th.repaint(); 
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

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jTextField9 = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jTextField10 = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jTextField11 = new javax.swing.JTextField();
        jTextField24 = new javax.swing.JTextField();
        jPanel9 = new javax.swing.JPanel();
        jButton7 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel30 = new javax.swing.JLabel();
        jTextField20 = new javax.swing.JTextField();
        jPanel13 = new javax.swing.JPanel();
        jTextField2 = new javax.swing.JTextField();
        jTextField1 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jTextField12 = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jTextField13 = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jTextField14 = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jTextField15 = new javax.swing.JTextField();
        jTextField25 = new javax.swing.JTextField();
        jPanel10 = new javax.swing.JPanel();
        jButton5 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jPanel15 = new javax.swing.JPanel();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jPanel12 = new javax.swing.JPanel();
        jTextField5 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jTextField23 = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jTextField16 = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        jTextField17 = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        jTextField18 = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        jTextField19 = new javax.swing.JTextField();
        jTextField26 = new javax.swing.JTextField();
        jPanel11 = new javax.swing.JPanel();
        jButton9 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jButton16 = new javax.swing.JButton();
        jButton18 = new javax.swing.JButton();
        jButton19 = new javax.swing.JButton();
        jLabel31 = new javax.swing.JLabel();
        jTextField21 = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel28 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        cancelButton = new javax.swing.JButton();
        okButton = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();

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
        jLabel1.setText("Análisis de Partidas del Presupuesto");
        jLabel1.setOpaque(true);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 796, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
        );

        jTabbedPane1.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);

        jTable1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jTable1.setSelectionBackground(new java.awt.Color(216, 141, 0));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jTable1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTable1KeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(102, 102, 102), 1, true), "Ingrese el Código para Agregar un Material"));

        jLabel9.setText("Código:");

        jLabel10.setText("Descripción:");

        jTextField8.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField8KeyTyped(evt);
            }
        });

        jLabel11.setText("Cantidad:");

        jTextField9.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField9.setText("0.00");
        jTextField9.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField9FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField9FocusLost(evt);
            }
        });
        jTextField9.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField9KeyTyped(evt);
            }
        });

        jLabel12.setText("Precio:");

        jTextField10.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField10.setText("0.00");
        jTextField10.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField10FocusLost(evt);
            }
        });
        jTextField10.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField10KeyTyped(evt);
            }
        });

        jLabel13.setText("Desperdicio");

        jTextField11.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField11.setText("0.00");
        jTextField11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField11ActionPerformed(evt);
            }
        });
        jTextField11.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField11FocusLost(evt);
            }
        });
        jTextField11.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField11KeyTyped(evt);
            }
        });

        jTextField24.setToolTipText("Ingrese el código del material");
        jTextField24.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField24MouseClicked(evt);
            }
        });
        jTextField24.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField24FocusLost(evt);
            }
        });
        jTextField24.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField24KeyTyped(evt);
            }
        });

        jButton7.setText("Guardar");
        jButton7.setEnabled(false);
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton14.setText("Borrar");
        jButton14.setEnabled(false);
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        jButton2.setText("Agregar desde Presupuesto");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Agregar desde Listado");
        jButton3.setToolTipText("Agregar desde Listado de Precios Seleccionado");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap(253, Short.MAX_VALUE)
                .addComponent(jButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton14)
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jButton14)
                .addComponent(jButton7)
                .addComponent(jButton2)
                .addComponent(jButton3))
        );

        jLabel30.setText("Unidad:");

        jTextField20.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField20.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField20FocusLost(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel10)
                            .addComponent(jLabel9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jTextField24, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(39, 39, 39)
                                .addComponent(jLabel30)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField20, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jTextField8, javax.swing.GroupLayout.DEFAULT_SIZE, 434, Short.MAX_VALUE))
                        .addGap(42, 42, 42)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel13)
                            .addComponent(jLabel12))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jTextField11, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
                            .addComponent(jTextField10, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE))
                        .addContainerGap())
                    .addComponent(jPanel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel11)
                    .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12)
                    .addComponent(jLabel30)
                    .addComponent(jTextField20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTextField2.setEditable(false);
        jTextField2.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jTextField1.setEditable(false);
        jTextField1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jLabel3.setText("Costo por Unidad:");

        jLabel2.setText("Precio Unitario:");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(27, 27, 27)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jTextField1)
                    .addComponent(jTextField2, javax.swing.GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 743, Short.MAX_VALUE)))
                    .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, 93, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Materiales", new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/materialbarra.png")), jPanel3); // NOI18N

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 1, true), "Ingrese el Código para Agregar un Equipo"));
        jPanel7.setPreferredSize(new java.awt.Dimension(740, 115));

        jLabel15.setText("Código:");

        jLabel16.setText("Descripción:");

        jTextField12.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField12KeyTyped(evt);
            }
        });

        jLabel17.setText("Cantidad:");

        jTextField13.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField13.setText("0.00");
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

        jLabel18.setText("Precio:");

        jTextField14.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField14.setText("0.00");
        jTextField14.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField14FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField14FocusLost(evt);
            }
        });
        jTextField14.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField14KeyTyped(evt);
            }
        });

        jLabel19.setText("Depreciación:");

        jTextField15.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField15.setText("0.00");
        jTextField15.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField15FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField15FocusLost(evt);
            }
        });
        jTextField15.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField15KeyTyped(evt);
            }
        });

        jTextField25.setToolTipText("Ingrese el código del material");
        jTextField25.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField25MouseClicked(evt);
            }
        });
        jTextField25.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField25FocusLost(evt);
            }
        });
        jTextField25.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField25KeyTyped(evt);
            }
        });

        jButton5.setText("Agregar desde Listado");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton11.setText("Guardar");
        jButton11.setEnabled(false);
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jButton15.setText("Borrar");
        jButton15.setEnabled(false);
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });

        jButton10.setText("Agregar desde Presupuesto");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap(211, Short.MAX_VALUE)
                .addComponent(jButton5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton15)
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jButton15)
                .addComponent(jButton11)
                .addComponent(jButton10)
                .addComponent(jButton5))
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel16)
                            .addComponent(jLabel15))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(jTextField25, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel17)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField13, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jTextField12, javax.swing.GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE))
                        .addGap(44, 44, 44)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel19)
                            .addComponent(jLabel18))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField14, javax.swing.GroupLayout.DEFAULT_SIZE, 128, Short.MAX_VALUE)
                            .addComponent(jTextField15, javax.swing.GroupLayout.DEFAULT_SIZE, 128, Short.MAX_VALUE))
                        .addContainerGap())
                    .addComponent(jPanel10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15)
                    .addComponent(jTextField13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17)
                    .addComponent(jLabel18))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16)
                    .addComponent(jLabel19)
                    .addComponent(jTextField15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );

        jTable2.setFont(new java.awt.Font("Tahoma", 0, 10));
        jTable2.setSelectionBackground(new java.awt.Color(216, 141, 0));
        jTable2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable2MouseClicked(evt);
            }
        });
        jTable2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTable2KeyReleased(evt);
            }
        });
        jScrollPane2.setViewportView(jTable2);

        jTextField3.setEditable(false);
        jTextField3.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jTextField4.setEditable(false);
        jTextField4.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField4ActionPerformed(evt);
            }
        });

        jLabel4.setText("Rendimiento:");

        jTextField7.setEditable(false);
        jTextField7.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField7ActionPerformed(evt);
            }
        });

        jLabel26.setText("Precio Unitario:");

        jLabel29.setText("Costo por Unidad:");

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap(50, Short.MAX_VALUE)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel29))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                        .addComponent(jLabel26)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel26))
                .addContainerGap(11, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 743, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 743, Short.MAX_VALUE)
                    .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 192, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.addTab("Equipos", new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/equipobarra.png")), jPanel4); // NOI18N

        jTable3.setFont(new java.awt.Font("Tahoma", 0, 10));
        jTable3.setSelectionBackground(new java.awt.Color(216, 141, 0));
        jTable3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable3MouseClicked(evt);
            }
        });
        jTable3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTable3KeyReleased(evt);
            }
        });
        jScrollPane3.setViewportView(jTable3);

        jTextField5.setEditable(false);
        jTextField5.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField5ActionPerformed(evt);
            }
        });

        jLabel7.setText("Rendimiento:");

        jTextField6.setEditable(false);
        jTextField6.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField6ActionPerformed(evt);
            }
        });

        jTextField23.setEditable(false);
        jTextField23.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField23ActionPerformed(evt);
            }
        });

        jLabel27.setText("Precio Unitario:");

        jLabel6.setText("Costo por Unidad:");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel7)
                    .addComponent(jLabel27)
                    .addComponent(jLabel6))
                .addGap(18, 18, 18)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextField5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField23, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addGap(7, 7, 7)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addGap(5, 5, 5)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel27))
                .addContainerGap())
        );

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 1, true), "Ingrese el Código para Agregar Mano de Obra"));

        jLabel21.setText("Código:");

        jLabel22.setText("Descripción:");

        jTextField16.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField16KeyTyped(evt);
            }
        });

        jLabel23.setText("Cantidad:");

        jTextField17.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField17.setText("0.00");
        jTextField17.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField17FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField17FocusLost(evt);
            }
        });
        jTextField17.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField17KeyTyped(evt);
            }
        });

        jLabel24.setText("Bono:");

        jTextField18.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField18.setText("0.00");
        jTextField18.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField18FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField18FocusLost(evt);
            }
        });
        jTextField18.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField18KeyTyped(evt);
            }
        });

        jLabel25.setText("Salario:");

        jTextField19.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField19.setText("0.00");
        jTextField19.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField19FocusLost(evt);
            }
        });
        jTextField19.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField19KeyTyped(evt);
            }
        });

        jTextField26.setToolTipText("Ingrese el código del material");
        jTextField26.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField26MouseClicked(evt);
            }
        });
        jTextField26.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField26FocusLost(evt);
            }
        });
        jTextField26.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField26KeyTyped(evt);
            }
        });

        jButton9.setText("Agregar desde listado");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButton13.setText("Guardar");
        jButton13.setEnabled(false);
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        jButton16.setText("Borrar");
        jButton16.setToolTipText("Borrar Detalle de Equipo");
        jButton16.setEnabled(false);
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });

        jButton18.setText("Agregar desde Presupuesto");
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton18ActionPerformed(evt);
            }
        });

        jButton19.setText("Nuevo");
        jButton19.setToolTipText("Nueva mano de Obra al Presupuesto}");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(107, 107, 107)
                .addComponent(jButton9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton16)
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jButton9)
                .addComponent(jButton16)
                .addComponent(jButton13)
                .addComponent(jButton18)
                .addComponent(jButton19))
        );

        jLabel31.setText("Subsidio:");

        jTextField21.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField21.setText("0.00");
        jTextField21.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField21FocusLost(evt);
            }
        });
        jTextField21.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField21KeyTyped(evt);
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
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel22)
                                    .addComponent(jLabel21))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel8Layout.createSequentialGroup()
                                        .addComponent(jTextField26, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel23)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextField17, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jTextField16, javax.swing.GroupLayout.DEFAULT_SIZE, 409, Short.MAX_VALUE))
                                .addGap(70, 70, 70)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel24)
                                    .addComponent(jLabel31)))
                            .addComponent(jLabel25, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jTextField18, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)
                            .addComponent(jTextField21, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)
                            .addComponent(jTextField19, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE))
                        .addContainerGap())
                    .addComponent(jPanel11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21)
                    .addComponent(jLabel23)
                    .addComponent(jTextField17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel24))
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel22)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel31))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel25)
                            .addComponent(jTextField19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(15, 15, 15)))
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 743, Short.MAX_VALUE)
                    .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.addTab("Mano de Obra", new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/casco.png")), jPanel5); // NOI18N

        jLabel28.setText("Seleccione Tabulador:");

        cancelButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/eliminar.png"))); // NOI18N
        cancelButton.setToolTipText("Cancelar");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
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

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/imprimir.png"))); // NOI18N
        jButton4.setToolTipText("Imprimir APU");

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                .addContainerGap(84, Short.MAX_VALUE)
                .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11))
        );

        getRootPane().setDefaultButton(okButton);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel28)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(649, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 768, Short.MAX_VALUE))
                    .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18))
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel28)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 472, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.getAccessibleContext().setAccessibleName("Materiales");

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

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed

    }//GEN-LAST:event_okButtonActionPerformed
    
    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        part.settotal(String.valueOf(contototal));
        
        doClose(RET_CANCEL);
    }//GEN-LAST:event_cancelButtonActionPerformed

    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        part.settotal(String.valueOf(contototal));
        doClose(RET_CANCEL);
    }//GEN-LAST:event_closeDialog

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jTextField5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField5ActionPerformed

    private void jTextField6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField6ActionPerformed

    private void jTextField23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField23ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField23ActionPerformed

    
    public void agregamat(){
     
       int cuanto=0;
       
        
        String contar = "SELECT count(mppre_id) FROM dmpres WHERE mmpre_id='"+jTextField24.getText().toString()+"' AND"
                + " mppre_id='"+partida+"' AND (mpre_id='"+pres+"' OR mpre_id IN (SELECT "
                + "id FROM mpres WHERE mpres_id='"+pres+"'))";
       
        
        
        String sql = "INSERT INTO dmpres (mpre_id, mppre_id, mmpre_id, numepart, cantidad, precio, status)"
                + " VALUES ( '"+pres+"', '"+partida+"', '"+jTextField24.getText().toString()+"',"
                + ""+numero+", "+jTextField9.getText().toString()+", "+jTextField10.getText().toString()+","
                + "1)";
        int cuentan=0, cuentapartida=0;
        try {
            
            Statement cuantos = (Statement) conex.createStatement();
            ResultSet rcuantos = cuantos.executeQuery(contar);
            while(rcuantos.next()){
                cuanto = Integer.parseInt(rcuantos.getObject(1).toString());
            }
            
            if(cuanto==0){
            Statement st = (Statement) conex.createStatement();
            st.execute(sql);
            JOptionPane.showMessageDialog(null, "Se ha insertado el material");
            
            String cuenta = "SELECT count(id) FROM mmpres WHERE "
                    + "id='"+jTextField24.getText().toString()+"' AND (mpre_id='"+pres+"' OR mpre_id IN (SELECT id FROM mpres "
                    + "WHERE mpres_id='"+pres+"'))";
            Statement ste = (Statement) conex.createStatement();
            ResultSet rste = ste.executeQuery(cuenta);
            while(rste.next()){
                cuentan = Integer.parseInt(rste.getObject(1).toString());
            }
            if(cuentan==0){
                String inserta = "INSERT into mmpres VALUES('"+pres+"', '"+jTextField24.getText().toString()+"',"
                        + "'"+jTextField8.getText()+"', "+jTextField11.getText()+", "+jTextField10.getText()+","
                        + "'"+jTextField20.getText()+"',1)";
                Statement sts = (Statement) conex.createStatement();
                sts.execute(inserta);
                
                int cuentabu=0;
                String enestudio = "SELECT count(*) FROM mtabus WHERE id='ESTUDIO'";
                Statement tabus = (Statement) conex.createStatement();
                ResultSet rstabus = tabus.executeQuery(enestudio);
                while(rstabus.next()){
                    cuentabu=rstabus.getInt(1);
                }
                if(cuentabu>0){
                    String select= "SELECT count(*) FROM mmtabs WHERE id='"+jTextField24.getText()+"' AND mtabus_id='ESTUDIO'";
                    Statement stselect = (Statement) conex.createStatement();
                    ResultSet rstselect = stselect.executeQuery(select);
                    while(rstselect.next()){
                        cuentapartida = rstselect.getInt(1);
                    }
                    
                }
                if(cuentabu==0){
                    int op = JOptionPane.showConfirmDialog(this, "¿Desea agregar nuevo material en el tabulador de estudio?","",JOptionPane.YES_NO_OPTION);
                    if(op==JOptionPane.YES_OPTION){
                        String insertabu = "INSERT INTO mtabus (id, descri, vigencia,status,seleccionado) "
                                + "VALUES ('ESTUDIO',PARTIDAS A SER INSERTADAS EN SIGUIENTES TABULADORES', CURDATE(), 1,0)";
                        Statement insertar = (Statement) conex.createStatement();
                        insertar.execute(insertabu);
                        String insertmat = "INSERT INTO mmtabs (mtabus_id,id, descri, desperdi, precio, unidad, status)"
                                + " VALUES ('ESTUDIO', '"+jTextField24.getText()+"', '"+jTextField8.getText()+"',"+jTextField11.getText()+""
                                + ", "+jTextField10.getText()+","
                                + "'"+jTextField20.getText()+"',1)";
                        Statement insertamat = (Statement) conex.createStatement();
                        insertamat.execute(insertmat);
                        JOptionPane.showMessageDialog(this, "Se ha insertado el material para el tabulador");
                    }
                }else{
                    if(cuentapartida==0){
                         int op = JOptionPane.showConfirmDialog(this, "¿Desea agregar nuevo material en el tabulador de estudio?","",JOptionPane.YES_NO_OPTION);
                    if(op==JOptionPane.YES_OPTION){
                        
                        String insertmat = "INSERT INTO mmtabs (mtabus_id,id, descri, desperdi, precio, unidad, status)"
                                + " VALUES ('ESTUDIO', '"+jTextField24.getText()+"', '"+jTextField8.getText()+"',"+jTextField11.getText()+""
                                + ", "+jTextField10.getText()+","
                                + "'"+jTextField20.getText()+"',1)";
                        Statement insertamat = (Statement) conex.createStatement();
                        insertamat.execute(insertmat);
                        JOptionPane.showMessageDialog(this, "Se ha insertado el material para el tabulador");
                    }
                    }
                }
                
            }
            contmano=contequip=contmano=cantmano=0;
            buscamat();
            buscaequip();
            buscamano();
            calculapartida();
            }else{
                 JOptionPane.showMessageDialog(null, "No se ha insertado el material, el material "+jTextField24.getText().toString()+" "
                         + "ya se encuentra en el detalle de la partida");
                 
            }
        } catch (SQLException ex) {
             JOptionPane.showMessageDialog(null, "No se ha insertado el material, el material "+jTextField24.getText().toString());
            Logger.getLogger(apu.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
    }
    public void agregaequipo(){
        
       int cuanto=0;
        String contar = "SELECT count(mppre_id) FROM deppres WHERE mepre_id='"+jTextField25.getText().toString()+"' AND"
                + " mppre_id='"+partida+"' AND (mpre_id='"+pres+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"'))";
       
        
        
        String sql = "INSERT INTO deppres (mpre_id, mppre_id, mepre_id, numero, cantidad, precio, status)"
                + " VALUES ( '"+pres+"', '"+partida+"', '"+jTextField25.getText().toString()+"',"
                + ""+numero+", "+jTextField13.getText().toString()+", "+jTextField14.getText().toString()+","
                + "1)";
        int cuentan=0;
        int cuentapartida=0;
        try {
            
            Statement cuantos = (Statement) conex.createStatement();
            ResultSet rcuantos = cuantos.executeQuery(contar);
            while(rcuantos.next()){
                cuanto = Integer.parseInt(rcuantos.getObject(1).toString());
            }
            
            if(cuanto==0){
            Statement st = (Statement) conex.createStatement();
            st.execute(sql);
            JOptionPane.showMessageDialog(null, "Se ha insertado el material");
            
            String cuenta = "SELECT count(id) FROM mepres WHERE id='"+jTextField25.getText().toString()+"' "
                    + "AND (mpre_id='"+pres+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"'))";
            Statement ste = (Statement) conex.createStatement();
            ResultSet rste = ste.executeQuery(cuenta);
            while(rste.next()){
                cuentan = Integer.parseInt(rste.getObject(1).toString());
            }
            if(cuentan==0){
                String inserta = "INSERT into mepres VALUES('"+pres+"', '"+jTextField25.getText().toString()+"',"
                        + "'"+jTextField12.getText()+"', "+jTextField15.getText()+", "+jTextField14.getText()+""
                        + ",1 )";
                
                Statement sts = (Statement) conex.createStatement();
                sts.execute(inserta);
                
                 int cuentabu=0;
                String enestudio = "SELECT count(*) FROM mtabus WHERE id='ESTUDIO'";
                Statement tabus = (Statement) conex.createStatement();
                ResultSet rstabus = tabus.executeQuery(enestudio);
                while(rstabus.next()){
                    cuentabu=rstabus.getInt(1);
                }
                if(cuentabu>0){
                    String select= "SELECT count(*) FROM metabs WHERE id='"+jTextField24.getText()+"'"
                            + " AND mtabus_id='ESTUDIO'";
                    Statement stselect = (Statement) conex.createStatement();
                    ResultSet rstselect = stselect.executeQuery(select);
                    while(rstselect.next()){
                        cuentapartida = rstselect.getInt(1);
                    }
                    
                }
                if(cuentabu==0){
                    int op = JOptionPane.showConfirmDialog(this, "¿Desea agregar nuevo equipo en el tabulador de estudio?","",JOptionPane.YES_NO_OPTION);
                    if(op==JOptionPane.YES_OPTION){
                        String insertabu = "INSERT INTO mtabus (id, descri, vigencia,status,seleccionado) "
                                + "VALUES ('ESTUDIO','PARTIDAS DEL PROPIETARIO DEL SISTEMA', CURDATE(), 1,0)";
                        Statement insertar = (Statement) conex.createStatement();
                        insertar.execute(insertabu);
                        String insertmat = "INSERT INTO metabs (mtabus_id,id, descri, deprecia, precio, status)"
                                + " VALUES ('ESTUDIO', '"+jTextField25.getText()+"', '"+jTextField12.getText()+"', "+jTextField15.getText()+","
                                + "'"+jTextField14.getText()+"',1)";
                        Statement insertamat = (Statement) conex.createStatement();
                        insertamat.execute(insertmat);
                        JOptionPane.showMessageDialog(this, "Se ha insertado el equipo para el tabulador");
                    }
                }else{
                    if(cuentapartida==0){
                         int op = JOptionPane.showConfirmDialog(this, "¿Desea agregar nuevo equipo en el tabulador de estudio?","",JOptionPane.YES_NO_OPTION);
                    if(op==JOptionPane.YES_OPTION){
                        
                        String insertmat = "INSERT INTO metabs (mtabus_id,id, descri, deprecia, precio, status)"
                                + " VALUES ('ESTUDIO', '"+jTextField25.getText()+"',"
                                + " '"+jTextField12.getText()+"', "+jTextField15.getText()+","
                                + "'"+jTextField14.getText()+"',1)";
                        Statement insertamat = (Statement) conex.createStatement();
                        insertamat.execute(insertmat);
                        JOptionPane.showMessageDialog(this, "Se ha insertado el equipo para el tabulador");
                    }
                    }
                }
                
            }
            contmano=contequip=contmano=cantmano=0;
            buscamat();
            buscaequip();
            buscamano();
            calculapartida();
            }else{
                 JOptionPane.showMessageDialog(null, "No se ha insertado el equipo "+jTextField25.getText().toString()+" "
                         + "ya se encuentra en el detalle de la partida");
                 
            }
        } catch (SQLException ex) {
            Logger.getLogger(apu.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
    }
    
     public void agregamano(){
         String mtabu = jComboBox1.getSelectedItem().toString();
       int cuanto=0;
        String contar = "SELECT count(mppre_id) FROM dmoppres WHERE mmopre_id='"+jTextField26.getText().toString()+"' AND"
                + " mppre_id='"+partida+"' AND mpre_id='"+pres+"'";
       
        
        
        String sql = "INSERT INTO dmoppres (mpre_id, mppre_id, mmopre_id, numero, cantidad, bono, salario, status)"
                + " VALUES ( '"+pres+"', '"+partida+"', '"+jTextField26.getText().toString()+"',"
                + ""+numero+", "+jTextField17.getText().toString()+", "+jTextField18.getText().toString()+","+jTextField19.getText().toString()+","
                + "1)";
        int cuentan=0;
        try {
            
            Statement cuantos = (Statement) conex.createStatement();
            ResultSet rcuantos = cuantos.executeQuery(contar);
            while(rcuantos.next()){
                cuanto = Integer.parseInt(rcuantos.getObject(1).toString());
            }
            
            if(cuanto==0){
            Statement st = (Statement) conex.createStatement();
            st.execute(sql);
            
            
            String cuenta = "SELECT count(id) FROM mmopres WHERE id='"+jTextField26.getText().toString()+"' AND mpre_id='"+pres+"'";
            Statement ste = (Statement) conex.createStatement();
            ResultSet rste = ste.executeQuery(cuenta);
            while(rste.next()){
                cuentan = Integer.parseInt(rste.getObject(1).toString());
            }
            if(cuentan==0){
                String inserta = "INSERT into mmopres (mpre_id,id, descri,bono,salario,status, subsid) "
                        + "VALUES('"+pres+"', '"+jTextField26.getText()+"',"
                        + "'"+jTextField16.getText()+"', "+jTextField18.getText()+", "+jTextField19.getText()+""
                        + ",1,'"+jTextField21.getText()+"')";
                
                Statement sts = (Statement) conex.createStatement();
                sts.execute(inserta);
                
                
            }
            JOptionPane.showMessageDialog(null, "Se ha insertado la mano de obra");
            contmano=contequip=contmano=cantmano=0;
            buscamat();
            buscaequip();
            buscamano();
            calculapartida();
            }else{
                 JOptionPane.showMessageDialog(null, "No se ha insertado la mano de obra "+jTextField24.getText().toString()+" "
                         + "ya se encuentra en el detalle de la partida");
                 
            }
        } catch (SQLException ex) {
            Logger.getLogger(apu.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
    }
    private void jTextField11FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField11FocusLost
 if(jTextField11.getText().equals("")){
        jTextField11.setText("0.00");
    }       
        if(insertare==1){
        agregamat();
        insertare=0;
        }
    }//GEN-LAST:event_jTextField11FocusLost

    private void jTextField15FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField15FocusLost
 if(jTextField15.getText().equals("")){
        jTextField15.setText("0.00");
    }
        
        agregaequipo();
    }//GEN-LAST:event_jTextField15FocusLost

    private void jTextField4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField4ActionPerformed

    private void jTextField7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField7ActionPerformed

    private void jTextField19FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField19FocusLost
 if(jTextField19.getText().equals("")){
        jTextField19.setText("0.00");
    }
        agregamano();
    }//GEN-LAST:event_jTextField19FocusLost

    private void jTextField24FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField24FocusLost
insertare=1;        
        
        String sql = "SELECT precio, descri, desperdi, unidad FROM mmtabs WHERE mtabus_id='"+jComboBox1.getSelectedItem().toString()+"'"
                + " AND id='"+jTextField24.getText().toString()+"'";
        try {
            Statement st = (Statement) conex.createStatement();
            ResultSet rst = st.executeQuery(sql);
            
            while(rst.next()){
                jTextField10.setText(rst.getObject(1).toString());
                jTextField8.setText(rst.getObject(2).toString());
                jTextField11.setText(rst.getObject(3).toString());
                jTextField20.setText(rst.getObject(4).toString());
            }
                    
        } catch (SQLException ex) {
            Logger.getLogger(apu.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }//GEN-LAST:event_jTextField24FocusLost
    private void calculapartida() {
          float auxcontotal = 0;
        float admini=Float.valueOf(adm);
        float utili=Float.valueOf(util);
        float financiero= Float.valueOf(fina);
        float impuesto = Float.valueOf(impart);
        float redondeado = 0;
        contototal=0;
        contototal = contmat+contequip+contmano;
        
        auxcontotal = contototal;
        auxcontotal = (float) Math.rint(auxcontotal*100)/100;
        System.out.println("auxcontotal "+auxcontotal);
        admini = auxcontotal * admini/100;
        admini = (float) Math.rint(admini*100)/100;
        System.out.println("admin "+admini);
        utili = (auxcontotal+admini) * utili/100;
        utili = (float) Math.rint(utili*100)/100;
        System.out.println("util "+utili);
        auxcontotal = auxcontotal + admini + utili;
        System.out.println("sumado "+auxcontotal);
        financiero = auxcontotal * financiero/100;
        financiero = (float) Math.rint(financiero*100)/100;
        impuesto = auxcontotal * impuesto/100;
        impuesto = (float) Math.rint(impuesto*100)/100;
        System.out.println("financiero "+financiero);
        System.out.println("impuesto "+impuesto);
        contototal = auxcontotal + impuesto + financiero;
        System.out.println("contotal impu fin "+contototal);
        auxcontotal = (float) (Math.rint((contototal+0.00001)*100)/100);
        System.out.println("auxcontotalredondeado: "+auxcontotal);
        redondeado = (float) Math.rint(contototal+0.000001);
        
        NumberFormat formatoNumero = NumberFormat.getNumberInstance();
            formatoNumero.setMaximumFractionDigits(2);
            formatoNumero.setMinimumFractionDigits(2);
            jTextField1.setText(String.valueOf(formatoNumero.format(auxcontotal)));
            jTextField7.setText(String.valueOf(formatoNumero.format(auxcontotal)));
            jTextField23.setText(String.valueOf(formatoNumero.format(auxcontotal)));
        
        contototal = auxcontotal;
     
        String consulta = "UPDATE mppres SET precunit="+contototal+", precasu="+redondeado+" WHERE numero='"+numero+"' AND (mpre_id='"+pres+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"'))";
        System.out.println(consulta);
        try {
            Statement stmt = (Statement) conex.createStatement();
            stmt.execute(consulta);
        } catch (SQLException ex) {
            Logger.getLogger(Presupuesto.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        int x = (prin.getWidth()/2)-350, y= (prin.getHeight()/2)-175;
        contequip=contmano=contmat=cantmano=0;
        materiales mat = new materiales(null, true, pres, conex, jComboBox1.getSelectedItem().toString(), numero, partida);
        mat.setBounds(x, y, 800, 450);
        mat.setVisible(true);
        buscamat();
        buscaequip();
        buscamano();
        calculapartida();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        int x = (prin.getWidth()/2)-350, y= (prin.getHeight()/2)-175;
        contequip=contmano=contmat=cantmano=0;
        mano mano = new mano(null, true, pres, conex, jComboBox1.getSelectedItem().toString(), numero, partida);
        mano.setBounds(x, y, 800, 450);
        mano.setVisible(true);
        
        buscamat();
        buscaequip();
        buscamano();
        calculapartida();
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        
        jButton7.setEnabled(true);
        
        filamat = jTable1.rowAtPoint(evt.getPoint());
        
        String mat = jTable1.getValueAt(filamat, 0).toString();
        jTextField24.setText(mat);
       String sql = "SELECT mm.id, mm.descri, mm.precio, dm.cantidad, mm.unidad, "
                + "mm.desperdi FROM "
                + "mmpres as mm, dmpres as dm WHERE dm.mpre_id='"+pres+"' AND dm.mppre_id='"+partida+"'"
                + " AND dm.mmpre_id=mm.id AND mm.id='"+mat+"' AND dm.mpre_id=mm.mpre_id GROUP BY mm.id";
        try {
            Statement stmt = (Statement) conex.createStatement();
            ResultSet rst = stmt.executeQuery(sql);
            while(rst.next()){
                jTextField9.setText(rst.getObject("dm.cantidad").toString());
                jTextField10.setText(rst.getObject("mm.precio").toString());
                jTextField8.setText(rst.getObject("mm.descri").toString());
                jTextField11.setText(rst.getObject("mm.desperdi").toString());
                jTextField20.setText(rst.getObject("mm.unidad").toString());
            }
        } catch (SQLException ex) {
            Logger.getLogger(apu.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        jButton14.setEnabled(true);
    }//GEN-LAST:event_jTable1MouseClicked

    private void jTextField11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField11ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField11ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        contmat=contequip=contmano=contototal=0;
        String mat = jTextField24.getText().toString();
        String cant = jTextField9.getText().toString();
        String precios = jTextField10.getText().toString();
        String sql = "UPDATE dmpres SET cantidad="+cant+", precio="+precios+""
                + " WHERE mmpre_id='"+mat+"' AND mppre_id='"+partida+"' "
                + "AND mpre_id='"+pres+"'";
        
        System.out.println("sql "+sql);
        
        try {
            Statement stmt = (Statement) conex.createStatement();
            stmt.execute(sql);
        } catch (SQLException ex) {
            Logger.getLogger(apu.class.getName()).log(Level.SEVERE, null, ex);
        }
        String modifica = "UPDATE mmpres SET descri='"+jTextField8.getText()+"' "
                + ", precio="+precios+", unidad='"+jTextField20.getText()+"', "
                + "desperdi="+jTextField11.getText()+" WHERE id='"+mat+"' AND mpre_id='"+pres+"'";
        try{
        Statement actualizamat=(Statement) conex.createStatement();
        actualizamat.execute(modifica); 
    } catch (SQLException ex) {
            Logger.getLogger(apu.class.getName()).log(Level.SEVERE, null, ex);
        }
        buscamat();
        buscaequip();
        buscamano();
        calculapartida();
        jTable1.getSelectionModel().setSelectionInterval(filamat, filamat);
       jTable1.requestFocus();
    }//GEN-LAST:event_jButton7ActionPerformed
 public void validafloat( java.awt.event.KeyEvent evt,String valor){
        char car = evt.getKeyChar();
        
        int repite = new StringTokenizer(valor,".").countTokens() - 1;
        if ((car<'0' || car>'9') && car!='.') {            
            evt.consume();
        }
        if(car=='.'&& repite==1){
             evt.consume();
        }
    }
    private void jTextField24MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField24MouseClicked
        jTextField24.setText("");
        jTextField9.setText("0.00");
        jTextField20.setText("");
        jTextField10.setText("");
        jTextField8.setText("");
        jTextField11.setText("");
        jButton7.setEnabled(false);
    }//GEN-LAST:event_jTextField24MouseClicked

    private void jTextField25FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField25FocusLost
          String sql = "SELECT precio, descri, deprecia FROM metabs WHERE mtabus_id='"+jComboBox1.getSelectedItem().toString()+"'"
                + " AND id='"+jTextField25.getText().toString()+"'";
        try {
            Statement st = (Statement) conex.createStatement();
            ResultSet rst = st.executeQuery(sql);
            
            while(rst.next()){
                jTextField14.setText(rst.getObject(1).toString());
                jTextField12.setText(rst.getObject(2).toString());
                jTextField15.setText(rst.getObject(3).toString());
            }
                    
        } catch (SQLException ex) {
            Logger.getLogger(apu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jTextField25FocusLost

    private void jTextField20FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField20FocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField20FocusLost

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        int x = (prin.getWidth()/2)-350, y= (prin.getHeight()/2)-175;
        contequip=contmano=contmat=cantmano=0;
        equipos eq = new equipos(null, true, pres, conex, jComboBox1.getSelectedItem().toString(), numero, partida);
        eq.setBounds(x, y, 800, 450);
        eq.setVisible(true);
        
        buscamat();
        buscaequip();
        buscamano();
        calculapartida();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseClicked

        
        filaequip = jTable2.rowAtPoint(evt.getPoint());
        String eq = jTable2.getValueAt(filaequip, 0).toString();
        jTextField25.setText(eq);
        String sql = "SELECT mm.id, mm.descri, mm.precio, dm.cantidad, "
                    + "mm.deprecia "
                    + "FROM mepres as mm, deppres as dm WHERE dm.mpre_id='"+pres+"' AND"
                    + " dm.mppre_id='"+partida+"' AND dm.mepre_id=mm.id AND mm.id='"+eq+"' AND dm.mpre_id=mm.mpre_id GROUP BY mm.id";
         System.out.println("sql equip"+sql);
        try {
            Statement stmt = (Statement) conex.createStatement();
            ResultSet rst = stmt.executeQuery(sql);
            while(rst.next()){
                jTextField13.setText(rst.getObject("dm.cantidad").toString());
                jTextField14.setText(rst.getObject("mm.precio").toString());
                jTextField12.setText(rst.getObject("mm.descri").toString());
                jTextField15.setText(rst.getObject("mm.deprecia").toString());
            }
        } catch (SQLException ex) {
            Logger.getLogger(apu.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        jButton15.setEnabled(true);
        jButton11.setEnabled(true);
    }//GEN-LAST:event_jTable2MouseClicked

    private void jTextField26FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField26FocusLost
        String sql = "SELECT salario, descri, bono FROM mmotabs WHERE mtabus_id='"+jComboBox1.getSelectedItem().toString()+"'"
                + " AND id='"+jTextField26.getText().toString()+"'";
        try {
            Statement st = (Statement) conex.createStatement();
            ResultSet rst = st.executeQuery(sql);
            
            while(rst.next()){
                jTextField19.setText(rst.getObject(1).toString());
                jTextField16.setText(rst.getObject(2).toString());
                jTextField18.setText(rst.getObject(3).toString());
            }
                    
        } catch (SQLException ex) {
            Logger.getLogger(apu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jTextField26FocusLost

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        contmat=contequip=contmano=contototal=0;
        String man = jTextField26.getText().toString();
        String cant = jTextField17.getText().toString();
        String bono = jTextField18.getText().toString();
        String salario = jTextField19.getText().toString();
        String descrip = jTextField16.getText().toUpperCase();
        String sql = "UPDATE dmoppres SET cantidad="+cant+", salario="+salario+", bono="+bono+", subsidi="+jTextField21.getText()+" WHERE mmopre_id='"+man+"' AND mppre_id='"+partida+"' AND mpre_id='"+pres+"'";
       String actualizamano = "UPDATE mmopres SET descri='"+descrip+"', salario="+salario+", bono="+bono+", subsid="+jTextField21.getText()+" "
               + "WHERE mpre_id='"+pres+"' AND id='"+man+"'";
        
        try {
            Statement stmt = (Statement) conex.createStatement();
            stmt.execute(sql);
            
            Statement actualiza = (Statement) conex.createStatement();
            actualiza.execute(actualizamano);
        } catch (SQLException ex) {
            Logger.getLogger(apu.class.getName()).log(Level.SEVERE, null, ex);
        }
        buscamat();
        buscaequip();
        buscamano();
        calculapartida();
        jTable3.getSelectionModel().setSelectionInterval(filamano, filamano);
        jTable3.requestFocus();
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jTable3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable3MouseClicked
       filamano = jTable3.rowAtPoint(evt.getPoint());
        String mano = jTable3.getValueAt(filamano, 0).toString();
        jTextField26.setText(mano);
        String sql = "SELECT mm.id, mm.descri, dm.cantidad, mm.bono, mm.subsid, mm.salario,"
                    + " (dm.cantidad*dm.salario) as total FROM mmopres as mm, dmoppres as dm"
                    + " WHERE dm.mpre_id='"+pres+"' AND dm.mppre_id='"+partida+"' "
                    + " AND dm.mmopre_id=mm.id AND mm.id='"+mano+"' AND dm.mpre_id=mm.mpre_id "
                    + "GROUP BY mm.id";
        System.out.println(" sql mano obra"+sql);
        try {
            Statement stmt = (Statement) conex.createStatement();
            ResultSet rst = stmt.executeQuery(sql);
            while(rst.next()){
                jTextField17.setText(rst.getObject("dm.cantidad").toString());
                jTextField19.setText(rst.getObject("mm.salario").toString());
                jTextField18.setText(rst.getObject("mm.bono").toString());
                jTextField16.setText(rst.getObject("mm.descri").toString());
                jTextField21.setText(rst.getObject("mm.subsid").toString());
            }
        } catch (SQLException ex) {
            Logger.getLogger(apu.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        jButton16.setEnabled(true);
        jButton13.setEnabled(true);
    }//GEN-LAST:event_jTable3MouseClicked

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        contmat=contequip=contmano=contototal=0;
        
        String eq = jTextField25.getText().toString();
        String cant = jTextField13.getText().toString();
        String precios = jTextField14.getText().toString();
        String sql = "UPDATE deppres SET cantidad="+cant+", precio="+precios+" WHERE mepre_id='"+eq+"' AND mppre_id='"+partida+"' AND mpre_id='"+pres+"'";
     
        String modificar = "UPDATE mepres SET descri='"+jTextField12.getText()+"',"
                + " deprecia="+jTextField15.getText()+", precio="+precios+" WHERE "
                + "id='"+eq+"' AND mpre_id='"+pres+"'";
        
        try {
            Statement stmt = (Statement) conex.createStatement();
            stmt.execute(sql);
            Statement actualizaeq = (Statement) conex.createStatement();
            actualizaeq.execute(modificar);
        } catch (SQLException ex) {
            Logger.getLogger(apu.class.getName()).log(Level.SEVERE, null, ex);
        }
        buscamat();
        buscaequip();
        buscamano();
        calculapartida();
        jTable2.getSelectionModel().setSelectionInterval(filaequip, filaequip);
        jTable2.requestFocus();
               
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        String mat =  jTable1.getValueAt(filamat, 0).toString();
        String delete = "DELETE FROM dmpres WHERE mmpre_id='"+mat+"' AND mppre_id='"+partida+"' AND mpre_id='"+pres+"'";
        try {
            Statement st = (Statement) conex.createStatement();
            st.execute(delete);
            JOptionPane.showMessageDialog(this, "Se ha eliminado el material "+mat);
            buscamat();
            buscaequip();
            buscamano();
            calculapartida();
        } catch (SQLException ex) {
            Logger.getLogger(apu.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        String equip =  jTable2.getValueAt(filaequip, 0).toString();
        String delete = "DELETE FROM deppres WHERE mepre_id='"+equip+"' AND mppre_id='"+partida+"' AND mpre_id='"+pres+"'";
        try {
            Statement st = (Statement) conex.createStatement();
            st.execute(delete);
            JOptionPane.showMessageDialog(this, "Se ha eliminado el equipo "+equip);
            buscamat();
            buscaequip();
            buscamano();
            calculapartida();
        } catch (SQLException ex) {
            Logger.getLogger(apu.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }//GEN-LAST:event_jButton15ActionPerformed

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
         String mano =  jTable3.getValueAt(filamano, 0).toString();
        String delete = "DELETE FROM dmoppres WHERE mmopre_id='"+mano+"' AND mppre_id='"+partida+"' AND mpre_id='"+pres+"'";
        try {
            Statement st = (Statement) conex.createStatement();
            st.execute(delete);
            JOptionPane.showMessageDialog(this, "Se ha eliminado la mano de obra "+mano);
            buscamat();
            buscaequip();
            buscamano();
            calculapartida();
        } catch (SQLException ex) {
            Logger.getLogger(apu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton16ActionPerformed

private void jTextField21FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField21FocusLost
 if(jTextField21.getText().equals("")){
        jTextField21.setText("0.00");
    }
    // TODO add your handling code here:
}//GEN-LAST:event_jTextField21FocusLost
    
    
    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        int x = (prin.getWidth()/2)-350, y= (prin.getHeight()/2)-175;
        contequip=contmano=contmat=cantmano=0;
        equipospres eq = new equipospres(null, true, pres, conex, jComboBox1.getSelectedItem().toString(), numero, partida);
        eq.setBounds(x, y, 800, 450);
        eq.setVisible(true);
        
        buscamat();
        buscaequip();
        buscamano();
        calculapartida();        // TODO add your handling code here:
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed
        int x = (prin.getWidth()/2)-350, y= (prin.getHeight()/2)-175;
        contequip=contmano=contmat=cantmano=0;
        manopres mano = new manopres(null, true, pres, conex, jComboBox1.getSelectedItem().toString(), numero, partida);
        mano.setBounds(x, y, 800, 450);
        mano.setVisible(true);
        
        buscamat();
        buscaequip();
        buscamano();
        calculapartida();        // TODO add your handling code here:
    }//GEN-LAST:event_jButton18ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        int x = (prin.getWidth()/2)-350, y= (prin.getHeight()/2)-175;
        contequip=contmano=contmat=cantmano=0;
        materialespres mat = new materialespres(null, true, pres, conex, jComboBox1.getSelectedItem().toString(), numero, partida);
        mat.setBounds(x, y, 800, 450);
        mat.setVisible(true);
        buscamat();
        buscaequip();
        buscamano();
        calculapartida();        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTextField9KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField9KeyTyped
validafloat(evt, jTextField9.getText().toString());        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField9KeyTyped

    private void jTextField10KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField10KeyTyped
validafloat(evt, jTextField10.getText().toString());        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField10KeyTyped

    private void jTextField11KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField11KeyTyped
validafloat(evt, jTextField11.getText().toString());        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField11KeyTyped

    private void jTextField13KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField13KeyTyped
validafloat(evt, jTextField13.getText().toString());        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField13KeyTyped

    private void jTextField14KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField14KeyTyped
validafloat(evt, jTextField14.getText().toString());        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField14KeyTyped

    private void jTextField15KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField15KeyTyped
validafloat(evt, jTextField15.getText().toString());        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField15KeyTyped

    private void jTextField17KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField17KeyTyped
validafloat(evt, jTextField17.getText().toString());        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField17KeyTyped

    private void jTextField18KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField18KeyTyped
validafloat(evt, jTextField18.getText().toString());        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField18KeyTyped

    private void jTextField19KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField19KeyTyped
validafloat(evt, jTextField19.getText().toString());        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField19KeyTyped

    private void jTextField21KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField21KeyTyped
validafloat(evt, jTextField21.getText().toString());        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField21KeyTyped

private void okButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_okButtonMouseClicked
        part.settotal(String.valueOf(contototal));
        
        
        doClose(RET_OK);// TODO add your handling code here:
}//GEN-LAST:event_okButtonMouseClicked

private void jTextField9FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField9FocusLost
    if(jTextField9.getText().equals("")){
        jTextField9.setText("0.00");
    }
    /* float cantidades = Float.valueOf(jTextField9.getText().toString());         
     float precios = Float.valueOf(jTextField10.getText().toString());         
     String mat = jTextField8.getText().toString();          
     float totalmat = cantidades * precios;*/
     
    // TODO add your handling code here:
}//GEN-LAST:event_jTextField9FocusLost

private void jTextField9FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField9FocusGained
    jTextField9.setText("");
}//GEN-LAST:event_jTextField9FocusGained

private void jTextField13FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField13FocusLost
// TODO add your handling code here:
    if(jTextField13.getText().equals("")){
        jTextField13.setText("0.00");
    }
}//GEN-LAST:event_jTextField13FocusLost

private void jTextField13FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField13FocusGained
    jTextField13.setText("");
    
    // TODO add your handling code here:
}//GEN-LAST:event_jTextField13FocusGained

private void jTextField17FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField17FocusGained
    jTextField17.setText("");
    // TODO add your handling code here:
}//GEN-LAST:event_jTextField17FocusGained

private void jTextField17FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField17FocusLost
    if(jTextField17.getText().equals("")){
        jTextField17.setText("0.00");
    }
        // TODO add your handling code here:
}//GEN-LAST:event_jTextField17FocusLost

private void jTextField25MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField25MouseClicked
    jTextField25.setText("");
    jTextField12.setText("");
    jTextField13.setText("0.00");
    jTextField14.setText("0.00");
    jTextField15.setText("0.00");
    // TODO add your handling code here:
}//GEN-LAST:event_jTextField25MouseClicked

private void jTextField14FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField14FocusLost
    if(jTextField14.getText().equals("")){
        jTextField14.setText("0.00");
    }
    // TODO add your handling code here:
}//GEN-LAST:event_jTextField14FocusLost

private void jTextField10FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField10FocusLost
  if(jTextField10.getText().equals("")){
        jTextField10.setText("0.00");
    }
    // TODO add your handling code here:
}//GEN-LAST:event_jTextField10FocusLost

private void jTextField18FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField18FocusLost
 if(jTextField18.getText().equals("")){
        jTextField18.setText("0.00");
    }// TODO add your handling code here:
}//GEN-LAST:event_jTextField18FocusLost

private void jTextField26MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField26MouseClicked
    jButton13.setEnabled(false);
    jTextField16.setText("");
    jTextField17.setText("0.00");
    jTextField18.setText("0.00");
    jTextField21.setText("0.00");
    jTextField19.setText("0.00");
   
}//GEN-LAST:event_jTextField26MouseClicked

private void jTable1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable1KeyReleased
 
        int code = evt.getKeyCode();
        if(code==38 || code==40){
            filamat = jTable1.getSelectedRow();
           String mat = jTable1.getValueAt(filamat, 0).toString();
        jTextField24.setText(mat);
       String sql = "SELECT mm.id, mm.descri, mm.precio, dm.cantidad, mm.unidad, "
                + "mm.desperdi FROM "
                + "mmpres as mm, dmpres as dm WHERE dm.mpre_id='"+pres+"' AND dm.mppre_id='"+partida+"'"
                + " AND dm.mmpre_id=mm.id AND mm.id='"+mat+"' AND dm.mpre_id=mm.mpre_id GROUP BY mm.id";
        try {
            Statement stmt = (Statement) conex.createStatement();
            ResultSet rst = stmt.executeQuery(sql);
            while(rst.next()){
                jTextField9.setText(rst.getObject("dm.cantidad").toString());
                jTextField10.setText(rst.getObject("mm.precio").toString());
                jTextField8.setText(rst.getObject("mm.descri").toString());
                jTextField11.setText(rst.getObject("mm.desperdi").toString());
                jTextField20.setText(rst.getObject("mm.unidad").toString());
            }
        } catch (SQLException ex) {
            Logger.getLogger(apu.class.getName()).log(Level.SEVERE, null, ex);
        }
        
                    
        } 
    
    // TODO add your handling code here:
}//GEN-LAST:event_jTable1KeyReleased

private void jTable2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable2KeyReleased
 int code = evt.getKeyCode();
        if(code==38 || code==40){
            filaequip=jTable2.getSelectedRow();
            String eq=jTable2.getValueAt(filaequip, 0).toString();
            jTextField25.setText(eq);
        String sql = "SELECT mm.id, mm.descri, mm.precio, dm.cantidad, "
                    + "mm.deprecia "
                    + "FROM mepres as mm, deppres as dm WHERE dm.mpre_id='"+pres+"' AND"
                    + " dm.mppre_id='"+partida+"' AND dm.mepre_id=mm.id AND mm.id='"+eq+"' AND dm.mpre_id=mm.mpre_id GROUP BY mm.id";
         System.out.println("sql equip"+sql);
        try {
            Statement stmt = (Statement) conex.createStatement();
            ResultSet rst = stmt.executeQuery(sql);
            while(rst.next()){
                jTextField13.setText(rst.getObject("dm.cantidad").toString());
                jTextField14.setText(rst.getObject("mm.precio").toString());
                jTextField12.setText(rst.getObject("mm.descri").toString());
                jTextField15.setText(rst.getObject("mm.deprecia").toString());
            }
        } catch (SQLException ex) {
            Logger.getLogger(apu.class.getName()).log(Level.SEVERE, null, ex);
        }
        }    
    
    // TODO add your handling code here:
}//GEN-LAST:event_jTable2KeyReleased

private void jTable3KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable3KeyReleased

     int code = evt.getKeyCode();
        if(code==38 || code==40){
            filaequip=jTable3.getSelectedRow();
            String mano = jTable3.getValueAt(filamano, 0).toString();
        jTextField26.setText(mano);
        String sql = "SELECT mm.id, mm.descri, dm.cantidad, mm.bono, mm.subsid, mm.salario,"
                    + " (dm.cantidad*dm.salario) as total FROM mmopres as mm, dmoppres as dm"
                    + " WHERE dm.mpre_id='"+pres+"' AND dm.mppre_id='"+partida+"' "
                    + " AND dm.mmopre_id=mm.id AND mm.id='"+mano+"' AND dm.mpre_id=mm.mpre_id "
                    + "GROUP BY mm.id";
        System.out.println(" sql mano obra"+sql);
        try {
            Statement stmt = (Statement) conex.createStatement();
            ResultSet rst = stmt.executeQuery(sql);
            while(rst.next()){
                jTextField17.setText(rst.getObject("dm.cantidad").toString());
                jTextField19.setText(rst.getObject("mm.salario").toString());
                jTextField18.setText(rst.getObject("mm.bono").toString());
                jTextField16.setText(rst.getObject("mm.descri").toString());
                jTextField21.setText(rst.getObject("mm.subsid").toString());
            }
        } catch (SQLException ex) {
            Logger.getLogger(apu.class.getName()).log(Level.SEVERE, null, ex);
        }
        }
    
    // TODO add your handling code here:
}//GEN-LAST:event_jTable3KeyReleased

private void jTextField26KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField26KeyTyped
   Character c = evt.getKeyChar();
                if(Character.isLetter(c)) {
                    evt.setKeyChar(Character.toUpperCase(c));
                }
    
    char car = evt.getKeyChar();
        
        
        if (jTextField26.getText().length()>7) {            
            evt.consume();
        }// TODO add your handling code here:
}//GEN-LAST:event_jTextField26KeyTyped

private void jTextField25KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField25KeyTyped

       Character c = evt.getKeyChar();
                if(Character.isLetter(c)) {
                    evt.setKeyChar(Character.toUpperCase(c));
                }
                if (jTextField25.getText().length()>7) {            
            evt.consume();
        }// TODO add your handling code here:        
    
    // TODO add your handling code here:
}//GEN-LAST:event_jTextField25KeyTyped

private void jTextField24KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField24KeyTyped
   Character c = evt.getKeyChar();
                if(Character.isLetter(c)) {
                    evt.setKeyChar(Character.toUpperCase(c));
                }
    if (jTextField24.getText().length()>7) {            
            evt.consume();
        }// TODO add your handling code here:
    
    // TODO add your handling code here:
}//GEN-LAST:event_jTextField24KeyTyped

private void jTextField18FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField18FocusGained
    jTextField18.setText("");
    // TODO add your handling code here:
}//GEN-LAST:event_jTextField18FocusGained

private void jTextField14FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField14FocusGained
    jTextField14.setText("");
    
}//GEN-LAST:event_jTextField14FocusGained

private void jTextField15FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField15FocusGained
    jTextField15.setText("");
    // TODO add your handling code here:
}//GEN-LAST:event_jTextField15FocusGained

private void jTextField8KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField8KeyTyped
   Character c = evt.getKeyChar();
                if(Character.isLetter(c)) {
                    evt.setKeyChar(Character.toUpperCase(c));
                }// TODO add your handling code here:
}//GEN-LAST:event_jTextField8KeyTyped

private void jTextField12KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField12KeyTyped
   Character c = evt.getKeyChar();
                if(Character.isLetter(c)) {
                    evt.setKeyChar(Character.toUpperCase(c));
                }// TODO add your handling code here:
}//GEN-LAST:event_jTextField12KeyTyped

private void jTextField16KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField16KeyTyped
   Character c = evt.getKeyChar();
                if(Character.isLetter(c)) {
                    evt.setKeyChar(Character.toUpperCase(c));
                }// TODO add your handling code here:
}//GEN-LAST:event_jTextField16KeyTyped

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
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton19;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
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
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
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
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
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
    private javax.swing.JTextField jTextField23;
    private javax.swing.JTextField jTextField24;
    private javax.swing.JTextField jTextField25;
    private javax.swing.JTextField jTextField26;
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
