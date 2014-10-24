/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * Principal.java
 *
 * Created on 31/08/2012, 10:26:12 AM
 */
package winspapus;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSetMetaData;
import com.mysql.jdbc.Statement;
import java.awt.Dimension;
import java.beans.PropertyVetoException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import parametros.contratistas;
import parametros.propietario;
import presupuesto.materiales.matrizmaterialespres;
import reportes.presupuestogeneral;
import valuaciones.aumentosdismi;
import valuaciones.reconsideraciones;
import valuaciones.valuacion;
import winspapus.equipos.equipos;
import winspapus.equipos.matrizequipos;
import winspapus.herramienta.RecuperarPre;
import winspapus.herramienta.RecuperarTab;
import winspapus.manos.manoobra;
import winspapus.manos.matrizmano;
import winspapus.materiales.*;
import winspapus.partidas.*;
import presupuestos.Nuevo;
import presupuestos.Presupuesto;
import presupuestos.diagrama;
import presupuestos.equipo.*;
import presupuestos.manoobra.matrizmanopres;
import presupuestos.memoria;
import presupuestos.tabpresupuesto;
import reportes.reportecuadrocierre;
import reportes.reportepresupuesto;
import valuaciones.parametrorecon;
/**
 *
 * @author Betmart
 */
public class Principal extends javax.swing.JFrame {
    String principio="";
    String seleccionado;
    String utilidad;    
    public int sinst;
    int buscala = 0;
    private Presupuesto presupuesto = null;
    int filamate = 0, filaequipo=0, filamano=0;
    int noredondeo=0;
    public int haypresupuesto=0;
    String cadena="", descrip="";
    DecimalFormat formatoNumero = new DecimalFormat("#,##0.00");;
     int row=-1, fila;
     String busqueda="";
     
     private int entro=0;
      String codicove="", descri="",num="" , num2="";
      private consulta aqui;
       DefaultTableModel mptabs;
   String codimate, descrimate, preciomate, cantidadmate, unidadmate, desperdiciomate;
   String codiequipo, codimano, impu, finanzas;
   private Connection conexion = null, conexremota=null, conespapu = null;
   public int llamaabusca=0;
     Principal obj= this;
     JScrollPane scroll;
     float contmat=0, contmano=0, conteq=0, contotal=0;;
     
    private int x=0, y=0;
    private String presup;
    /** Creates new form Principal */
    public Principal() {
        this.setIconImage (new ImageIcon(getClass().getResource("imagenes/LOGO.png")).getImage());
        
        System.out.println("Formato"+formatoNumero.getCurrency());
        this.setDefaultCloseOperation(Principal.EXIT_ON_CLOSE);
        this.setVisible(true);
       
        initComponents();
        formatoNumero.setMaximumFractionDigits(2);
        jTable1.setShowHorizontalLines(true);
    jTable1.setShowVerticalLines(false);
    jTable1.getTableHeader().setSize(new Dimension(25,40));
    jTable1.getTableHeader().setPreferredSize(new Dimension(25,30));
    jTable1.setRowHeight(20);
    jTable2.setOpaque(true);
    jTable2.setShowHorizontalLines(true);
    jTable2.setShowVerticalLines(false);
    jTable2.getTableHeader().setSize(new Dimension(25,40));
    jTable2.getTableHeader().setPreferredSize(new Dimension(25,30));
    jTable2.setRowHeight(20);
    jTable3.setOpaque(true);
    jTable3.setShowHorizontalLines(true);
    jTable3.setShowVerticalLines(false);
    jTable3.getTableHeader().setSize(new Dimension(25,40));
    jTable3.getTableHeader().setPreferredSize(new Dimension(20,30));
    jTable3.setRowHeight(20);
    jTable4.setOpaque(true);
    jTable4.setShowHorizontalLines(true);
    jTable4.setShowVerticalLines(false);
    jTable4.getTableHeader().setSize(new Dimension(25,40));
    jTable4.getTableHeader().setPreferredSize(new Dimension(20,30));
    jTable4.setRowHeight(20);            
    jTable5.setOpaque(true);
    jTable5.setShowHorizontalLines(true);
    jTable5.setShowVerticalLines(false);
    jTable5.getTableHeader().setSize(new Dimension(25,40));
    jTable5.getTableHeader().setPreferredSize(new Dimension(20,30));
    jTable5.setRowHeight(20);
        jDesktopPane1.setBorder(new Imagen(jDesktopPane1));
       
        jScrollPane8.getViewport().add(jPanel1);
      jInternalFrame1.setVisible(false);
        principio = jLabel3.getText();
       conectar();
       
       buscatab();
       consultactivo();
       verificarpres();
       jButton28.setVisible(false);
       jButton14.setEnabled(false);
       jButton15.setEnabled(false);
       jButton16.setEnabled(false);
       jButton17.setEnabled(false);
       jButton18.setEnabled(false);
       jButton19.setEnabled(false);
       jButton20.setEnabled(false);
       jButton21.setEnabled(false);
       jButton22.setEnabled(false);
    }
    
    public final void verificarpres()
    {
        
        int contador=0;
        String select = "SELECT COUNT(*) FROM mpres WHERE mpres_id IS NULL";
        try {
            Statement stselect = (Statement) conexion.createStatement();
            ResultSet rstselect = stselect.executeQuery(select);
                    while(rstselect.next()){
                        contador=rstselect.getInt(1);
                    }
                    
                    if(contador>0){
                        jMenuItem6.setEnabled(true);
                        jButton26.setEnabled(true);
                    }
        } catch (SQLException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void conectar(){
          String sSistemaOperativo = System.getProperty("os.name");
                System.out.println(sSistemaOperativo);
                 String sSistemaOperativoversion = System.getProperty("os.version");
                System.out.println(sSistemaOperativoversion);
         try {
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
        } catch (SQLException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {          
            //
            conexion = (Connection) DriverManager.getConnection("jdbc:mysql://localhost/winspapu", "spapu01", "04160481070MSag");
            //establer seguridad local
            Statement seg = (Statement) conexion.createStatement();
            Statement esc = (Statement) conexion.createStatement();

            String sql;            

 
            sql="select * from mpresadm";
            Serialdd  obj2=new Serialdd();
              String dd=obj2.getSerialNumber("C");
              int status=0;
            ResultSet rst = seg.executeQuery(sql);             
            rst.last();
            int regis = rst.getRow();    
            if (regis!=0){
            Date fec1=rst.getDate("fecha");
            
            Date hoy = new Date();
            double difer=hoy.getTime()-fec1.getTime(),dias=difer/(1000*60*24*60);
            if (dias>=30)
            {
              sql="update mpresadm set status='0'";  
              Statement demo1 = (Statement) conexion.createStatement();
              demo1.execute(sql); 
              JOptionPane.showMessageDialog(presupuesto, "Tiempo Agotado de la Versión DEMO, comunicarse con SISTEMAS RH");              
              System.exit(0);
            } 
            status = rst.getInt("status");
            }
            if (regis==0){
                int opc;
                opc=JOptionPane.showConfirmDialog(null, "Atención el Sistema se va a Ejecutar por primera vez, Desea Continuar?","INICIAR DE SISTEMA",JOptionPane.YES_NO_OPTION);
                if ((opc==1)||(opc==2)){
                   System.exit(0);
                } 
               
                  //establecer seguridad remota
                sSistemaOperativo = System.getProperty("os.name");
                System.out.println(sSistemaOperativo);
                opc=JOptionPane.showConfirmDialog(null, "Atención Asegúrese de estar conectado a Internet","INICIAR DE SISTEMA",JOptionPane.YES_NO_OPTION);
                if ((opc==1)||(opc==2)){
                   esc.execute("delete from mpresadm");                      
                   System.exit(0);
                }     
                conespapu = (Connection) DriverManager.getConnection("jdbc:mysql://spapu2.db.11811826.hostedresource.com/spapu2", "spapu2", "Rahp81261!");

                instalador instalar=new instalador(this, true,conespapu, this, conexion,dd);
                int xi=(this.getWidth()/2)-350/2;
                int yi=(this.getHeight()/2)-100/2;
                instalar.setBounds(xi, yi, 350, 200);
                instalar.setVisible(true);
                if (sinst==0) {
                   esc.execute("delete from mpresadm");                      
                   System.exit(0); 
                }                    
               
                 

//                conexremota = (Connection) DriverManager.getConnection("jdbc:mysql://dominiospapu/spapu", "root", "04160481070MSag");
         //       Statement st = (Statement) conexremota.createStatement();
                
            }
            else{
                if(!rst.getString("codigo").equals(dd)&&status==1)  {
                   JOptionPane.showMessageDialog(null, "LICENCIA NO AUTORIZADA, LLAMAR A SISTEMAS RH");
                   System.exit(0);
                }                                    
            }
            //
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al Conectar con la Base de Datos, verifique que este ejecución el servicio de la Base de Datos");
          
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error al conectar la bd");
              System.exit(0);
            
        }
    }
    public final void consultactivo(){
          String sql = "SELECT id FROM mtabus WHERE seleccionado=1 AND status=1";
          String id="0";
          int filas=0;
          
        try {
            Statement st = (Statement) conexion.createStatement();
            ResultSet rst = st.executeQuery(sql); 
            while(rst.next()){
                id=rst.getString("id");
            }
            if(id.equals("0") ||id.equals("")){
                filas = 0;
                activatab(filas);
            }else{
                int enc=0;
                int i=0, max;
                String selec = jTable1.getValueAt(i, 0).toString();
                max = jTable1.getRowCount()+1;
                while(enc==0 && i<max){
                    if(selec.equals(id)){
                        enc=1;
                        jTable1.setRowSelectionInterval(i, i);
                        cadena=id;
                        fila=jTable1.getSelectedRow();
                        activatab(i);
                        buscapartidas(i);
                    }else{
                        i++;
                        selec = jTable1.getValueAt(i, 0).toString();
                        fila=0;
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuBar2 = new javax.swing.JMenuBar();
        jMenu4 = new javax.swing.JMenu();
        jMenu5 = new javax.swing.JMenu();
        jPanel23 = new javax.swing.JPanel();
        jPanel26 = new javax.swing.JPanel();
        jPanel27 = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jMenuBar3 = new javax.swing.JMenuBar();
        jMenu8 = new javax.swing.JMenu();
        jMenu9 = new javax.swing.JMenu();
        jMenu10 = new javax.swing.JMenu();
        jDesktopPane1 = new javax.swing.JDesktopPane();
        jLabel13 = new javax.swing.JLabel();
        jInternalFrame1 = new javax.swing.JInternalFrame();
        jScrollPane8 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton23 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton31 = new javax.swing.JButton();
        jButton32 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jTextField2 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jButton8 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton24 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jPanel10 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jPanel29 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel5 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        jButton14 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jButton16 = new javax.swing.JButton();
        jPanel24 = new javax.swing.JPanel();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTable5 = new javax.swing.JTable();
        jPanel15 = new javax.swing.JPanel();
        jButton22 = new javax.swing.JButton();
        jButton21 = new javax.swing.JButton();
        jButton20 = new javax.swing.JButton();
        jPanel28 = new javax.swing.JPanel();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jButton17 = new javax.swing.JButton();
        jButton18 = new javax.swing.JButton();
        jButton19 = new javax.swing.JButton();
        jPanel25 = new javax.swing.JPanel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jToolBar1 = new javax.swing.JToolBar();
        jButton7 = new javax.swing.JButton();
        jButton25 = new javax.swing.JButton();
        jButton29 = new javax.swing.JButton();
        jButton26 = new javax.swing.JButton();
        jButton27 = new javax.swing.JButton();
        jButton28 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton30 = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JPopupMenu.Separator();
        jMenuItem45 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        jSeparator6 = new javax.swing.JPopupMenu.Separator();
        jMenuItem38 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem46 = new javax.swing.JMenuItem();
        jMenuItem10 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenu15 = new javax.swing.JMenu();
        jMenuItem33 = new javax.swing.JMenuItem();
        jMenuItem34 = new javax.swing.JMenuItem();
        jMenuItem35 = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        jMenuItem36 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenuItem14 = new javax.swing.JMenuItem();
        jMenuItem47 = new javax.swing.JMenuItem();
        jMenuItem15 = new javax.swing.JMenuItem();
        jMenuItem37 = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        jMenu7 = new javax.swing.JMenu();
        jMenuItem16 = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMenuItem19 = new javax.swing.JMenuItem();
        jMenuItem20 = new javax.swing.JMenuItem();
        jMenu12 = new javax.swing.JMenu();
        jMenuItem21 = new javax.swing.JMenuItem();
        jMenuItem22 = new javax.swing.JMenuItem();
        jMenuItem23 = new javax.swing.JMenuItem();
        jMenu13 = new javax.swing.JMenu();
        jMenuItem24 = new javax.swing.JMenuItem();
        jMenuItem25 = new javax.swing.JMenuItem();
        jMenuItem26 = new javax.swing.JMenuItem();
        jMenuItem27 = new javax.swing.JMenuItem();
        jMenuItem28 = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        jMenuItem29 = new javax.swing.JMenuItem();
        jMenu14 = new javax.swing.JMenu();
        jMenuItem30 = new javax.swing.JMenuItem();
        jMenuItem31 = new javax.swing.JMenuItem();
        jMenu11 = new javax.swing.JMenu();
        jMenuItem17 = new javax.swing.JMenuItem();
        jMenuItem18 = new javax.swing.JMenuItem();
        jMenu16 = new javax.swing.JMenu();
        jMenu17 = new javax.swing.JMenu();
        jMenuItem39 = new javax.swing.JMenuItem();
        jMenu18 = new javax.swing.JMenu();
        jMenuItem42 = new javax.swing.JMenuItem();
        jMenuItem44 = new javax.swing.JMenuItem();

        jMenu4.setText("File");
        jMenuBar2.add(jMenu4);

        jMenu5.setText("Edit");
        jMenuBar2.add(jMenu5);

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        jMenu8.setText("File");
        jMenuBar3.add(jMenu8);

        jMenu9.setText("Edit");
        jMenuBar3.add(jMenu9);

        jMenu10.setText("jMenu10");

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("WinSpapu Sistema de Presupuesto y Análisis de Precio Unitario");
        setBackground(new java.awt.Color(97, 126, 171));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jDesktopPane1.setBackground(new java.awt.Color(204, 204, 204));
        jDesktopPane1.setAlignmentX(0.0F);
        jDesktopPane1.setAlignmentY(0.0F);
        jDesktopPane1.setAutoscrolls(true);
        jDesktopPane1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jDesktopPane1.setDesktopManager(null);
        jDesktopPane1.setDoubleBuffered(true);
        jDesktopPane1.setSelectedFrame(jInternalFrame1);
        jLabel13.setBounds(0, 0, 0, 0);
        jDesktopPane1.add(jLabel13, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jInternalFrame1.setClosable(true);
        jInternalFrame1.setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
        jInternalFrame1.setIconifiable(true);
        jInternalFrame1.setMaximizable(true);
        jInternalFrame1.setTitle("Gestionar Listado de Precios Referenciales");
        jInternalFrame1.setDoubleBuffered(true);
        jInternalFrame1.setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/logonuevopeq.fw.png"))); // NOI18N
        jInternalFrame1.setPreferredSize(new java.awt.Dimension(1040, 600));
        try {
            jInternalFrame1.setSelected(true);
        } catch (java.beans.PropertyVetoException e1) {
            e1.printStackTrace();
        }
        jInternalFrame1.setVisible(true);
        jInternalFrame1.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentHidden(java.awt.event.ComponentEvent evt) {
                jInternalFrame1ComponentHidden(evt);
            }
        });

        jScrollPane8.setBorder(null);
        jScrollPane8.setAutoscrolls(true);

        jPanel1.setAutoscrolls(true);
        jPanel1.setPreferredSize(new java.awt.Dimension(1000, 550));

        jPanel2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(102, 102, 102), 1, true));
        jPanel2.setAutoscrolls(true);

        jPanel5.setBackground(new java.awt.Color(100, 100, 100));
        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(100, 100, 100)));

        jLabel1.setBackground(new java.awt.Color(91, 91, 95));
        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setLabelFor(jPanel5);
        jLabel1.setText("Listado de Precios Referenciales");
        jLabel1.setOpaque(true);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 572, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jTextField1.setPreferredSize(new java.awt.Dimension(20, 30));

        jLabel2.setText("Buscar:");

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/buscar1.fw.png"))); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/añade1.fw.png"))); // NOI18N
        jButton23.setToolTipText("Nuevo Tabulador");
        jButton23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton23ActionPerformed(evt);
            }
        });

        jButton13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/guardar1.fw.png"))); // NOI18N
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/edita1.fw.png"))); // NOI18N
        jButton4.setToolTipText("Editar");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/copia1.fw.png"))); // NOI18N
        jButton5.setToolTipText("Copiar Tabulador");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/respaldo.fw.png"))); // NOI18N
        jButton6.setToolTipText("Respaldar");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton31.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/borra1.fw.png"))); // NOI18N
        jButton31.setToolTipText("Eliminar");
        jButton31.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton31ActionPerformed(evt);
            }
        });

        jButton32.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/actualizar.fw.png"))); // NOI18N
        jButton32.setToolTipText("Actualizar");
        jButton32.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton32ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jButton23, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jButton31, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jButton32, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton23, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton31, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton32, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jTable1.setAutoCreateRowSorter(true);
        jTable1.setFont(new java.awt.Font("Tahoma", 0, 9));
        jTable1.setEditingColumn(0);
        jTable1.setEditingRow(0);
        jTable1.setName("mtabustable"); // NOI18N
        jTable1.setSelectionBackground(new java.awt.Color(216, 141, 0));
        jTable1.getTableHeader().setReorderingAllowed(false);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);
        jTable1.getAccessibleContext().setAccessibleName("tabla");
        jTable1.getAccessibleContext().setAccessibleParent(jPanel2);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 554, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(108, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        jPanel7.setBackground(new java.awt.Color(100, 100, 100));
        jPanel7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(100, 100, 100)));

        jLabel3.setBackground(new java.awt.Color(91, 91, 95));
        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setLabelFor(jPanel5);
        jLabel3.setText("Partidas del Listado de Precios");
        jLabel3.setOpaque(true);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 572, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
        );

        jTextField2.setPreferredSize(new java.awt.Dimension(20, 20));
        jTextField2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField2FocusGained(evt);
            }
        });

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/buscar1.fw.png"))); // NOI18N
        jButton2.setPreferredSize(new java.awt.Dimension(59, 29));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel4.setText("Buscar:");

        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/añade1.fw.png"))); // NOI18N
        jButton8.setToolTipText("Nueva Partida");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/ordenar1.fw.png"))); // NOI18N
        jButton12.setToolTipText("Reordenar Partidas");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/edita1.fw.png"))); // NOI18N
        jButton9.setToolTipText("Detalles de La partida");
        jButton9.setEnabled(false);
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButton11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/borra1.fw.png"))); // NOI18N
        jButton11.setToolTipText(" Eliminar Partida");
        jButton11.setEnabled(false);
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/copia1.fw.png"))); // NOI18N
        jButton10.setToolTipText("Copiar Partida");
        jButton10.setEnabled(false);
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jButton24.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/copiapu1.fw.png"))); // NOI18N
        jButton24.setToolTipText("Copiar APU");
        jButton24.setEnabled(false);
        jButton24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton24ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jButton24, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(113, 113, 113))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField2, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                            .addComponent(jLabel4))
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton24, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jTable2.setAutoCreateRowSorter(true);
        jTable2.setFont(new java.awt.Font("Tahoma", 0, 10));
        jTable2.setEditingColumn(0);
        jTable2.setEditingRow(0);
        jTable2.setName("mtabustable"); // NOI18N
        jTable2.setSelectionBackground(new java.awt.Color(216, 141, 0));
        jTable2.getTableHeader().setReorderingAllowed(false);
        jTable2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable2MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTable2);
        jTable2.getAccessibleContext().setAccessibleParent(jPanel3);

        jPanel16.setBackground(new java.awt.Color(217, 224, 231));
        jPanel16.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        jLabel22.setFont(new java.awt.Font("Tahoma", 1, 10));
        jLabel22.setText("Rendimiento:");
        jLabel22.setAlignmentY(0.0F);

        jLabel26.setFont(new java.awt.Font("Tahoma", 1, 10));
        jLabel26.setText("Utilidad %:");
        jLabel26.setAlignmentY(0.0F);

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 10));
        jLabel18.setText("Grupo Partidas:");
        jLabel18.setAlignmentY(0.0F);

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 10));
        jLabel14.setText("Código COVENIN:");
        jLabel14.setAlignmentY(0.0F);

        jLabel15.setFont(new java.awt.Font("Tahoma", 0, 10));

        jLabel19.setFont(new java.awt.Font("Tahoma", 0, 10));

        jLabel23.setFont(new java.awt.Font("Tahoma", 0, 10));

        jLabel27.setFont(new java.awt.Font("Tahoma", 0, 10));

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel18, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel22, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel26, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(44, Short.MAX_VALUE))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel22)
                    .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel26))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        jPanel17.setBackground(new java.awt.Color(217, 224, 231));
        jPanel17.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        jLabel25.setFont(new java.awt.Font("Tahoma", 0, 10));

        jLabel24.setFont(new java.awt.Font("Tahoma", 1, 10));
        jLabel24.setText("Admin. y Gastos %:");

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 10));
        jLabel16.setText("Número:");

        jLabel29.setFont(new java.awt.Font("Tahoma", 0, 10));

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 10));
        jLabel20.setText("Unidad Medida:");

        jLabel28.setFont(new java.awt.Font("Tahoma", 1, 10));
        jLabel28.setText("Prestaciones %:");

        jLabel21.setFont(new java.awt.Font("Tahoma", 0, 10));

        jLabel17.setFont(new java.awt.Font("Tahoma", 0, 10));

        jLabel31.setFont(new java.awt.Font("Tahoma", 1, 10));
        jLabel31.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel31.setText("Precio Unitario:");

        jLabel33.setFont(new java.awt.Font("Tahoma", 0, 10));

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel24, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel20, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel28, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel31, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(17, Short.MAX_VALUE))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel20)
                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel24)
                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel28))
                .addGap(0, 0, 0)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel31))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        jPanel29.setBackground(new java.awt.Color(217, 224, 231));
        jPanel29.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        jScrollPane3.setBorder(null);

        jTextArea1.setColumns(20);
        jTextArea1.setEditable(false);
        jTextArea1.setFont(new java.awt.Font("Times New Roman", 0, 11));
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(2);
        jTextArea1.setWrapStyleWord(true);
        jScrollPane3.setViewportView(jTextArea1);

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 10));
        jLabel5.setText("Descripción:");

        javax.swing.GroupLayout jPanel29Layout = new javax.swing.GroupLayout(jPanel29);
        jPanel29.setLayout(jPanel29Layout);
        jPanel29Layout.setHorizontalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 448, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel29Layout.setVerticalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel29Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(jLabel5))
                    .addGroup(jPanel29Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel29, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel16, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 554, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel4.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(102, 102, 102), 1, true));

        jPanel11.setBackground(new java.awt.Color(100, 100, 100));
        jPanel11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(100, 100, 100)));

        jLabel6.setBackground(new java.awt.Color(91, 91, 95));
        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Análisis de Precio Unitario");
        jLabel6.setOpaque(true);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 410, Short.MAX_VALUE)
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)), "Materiales", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        jTable4.setAutoCreateRowSorter(true);
        jTable4.setFont(new java.awt.Font("Tahoma", 0, 9));
        jTable4.setToolTipText("Para editar Cantidades haga doble click en la columna deseada");
        jTable4.setSelectionBackground(new java.awt.Color(216, 141, 0));
        jTable4.getTableHeader().setReorderingAllowed(false);
        jTable4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable4MouseClicked(evt);
            }
        });
        jTable4.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jTable4PropertyChange(evt);
            }
        });
        jTable4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTable4KeyPressed(evt);
            }
        });
        jScrollPane5.setViewportView(jTable4);

        jButton14.setText("Nuevo");
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        jButton15.setText("Guardar");
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });

        jButton16.setText("Quitar");
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });

        jLabel34.setFont(new java.awt.Font("Tahoma", 1, 10));
        jLabel34.setText("Total:");

        jLabel35.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel24Layout.createSequentialGroup()
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel34))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 364, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel9)
                        .addContainerGap())
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(jButton14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                        .addComponent(jPanel24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(14, 14, 14))))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(jLabel9))
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton14)
                        .addComponent(jButton15)
                        .addComponent(jButton16))
                    .addComponent(jPanel24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel14.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 1, true), "Mano de Obra", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        jTable5.setAutoCreateRowSorter(true);
        jTable5.setFont(new java.awt.Font("Tahoma", 0, 9));
        jTable5.setToolTipText("Para editar Cantidades haga doble click en la columna deseada");
        jTable5.setSelectionBackground(new java.awt.Color(216, 141, 0));
        jTable5.getTableHeader().setReorderingAllowed(false);
        jTable5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable5MouseClicked(evt);
            }
        });
        jTable5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTable5KeyPressed(evt);
            }
        });
        jScrollPane6.setViewportView(jTable5);

        jButton22.setText("Quitar");

        jButton21.setText("Guardar");
        jButton21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton21ActionPerformed(evt);
            }
        });

        jButton20.setText("Nuevo");
        jButton20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton20ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addComponent(jButton20)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton21)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton22)
                .addContainerGap())
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jButton20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel38.setFont(new java.awt.Font("Tahoma", 1, 10));
        jLabel38.setText("Total:");

        jLabel39.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(jPanel28);
        jPanel28.setLayout(jPanel28Layout);
        jPanel28Layout.setHorizontalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel28Layout.setVerticalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel28Layout.createSequentialGroup()
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel38))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel14Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 364, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel14Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jPanel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel11))
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 1, true), "Equipos", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        jTable3.setAutoCreateRowSorter(true);
        jTable3.setFont(new java.awt.Font("Tahoma", 0, 10));
        jTable3.setToolTipText("Para editar Cantidades haga doble click en la columna deseada");
        jTable3.setSelectionBackground(new java.awt.Color(216, 141, 0));
        jTable3.getTableHeader().setReorderingAllowed(false);
        jTable3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable3MouseClicked(evt);
            }
        });
        jTable3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTable3KeyPressed(evt);
            }
        });
        jScrollPane4.setViewportView(jTable3);

        jButton17.setText("Nuevo");
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton17ActionPerformed(evt);
            }
        });

        jButton18.setText("Guardar");
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton18ActionPerformed(evt);
            }
        });

        jButton19.setText("Quitar");
        jButton19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton19ActionPerformed(evt);
            }
        });

        jLabel36.setFont(new java.awt.Font("Tahoma", 1, 10));
        jLabel36.setText("Total:");

        jLabel37.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addContainerGap(12, Short.MAX_VALUE)
                .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel25Layout.createSequentialGroup()
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel36))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addContainerGap(388, Short.MAX_VALUE)
                .addComponent(jLabel7))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jButton17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton18)
                .addGap(2, 2, 2)
                .addComponent(jButton19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 368, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton17)
                        .addComponent(jButton18)
                        .addComponent(jButton19))
                    .addComponent(jPanel25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(8, 8, 8))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jScrollPane8.setViewportView(jPanel1);

        jToolBar1.setBackground(new java.awt.Color(245, 244, 244));
        jToolBar1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.lightGray, java.awt.Color.darkGray, java.awt.Color.white, java.awt.Color.gray));
        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);
        jToolBar1.setBorderPainted(false);

        jButton7.setBackground(new java.awt.Color(245, 244, 244));
        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/materialbarra.png"))); // NOI18N
        jButton7.setToolTipText("Materiales");
        jButton7.setFocusable(false);
        jButton7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton7.setMaximumSize(new java.awt.Dimension(42, 42));
        jButton7.setMinimumSize(new java.awt.Dimension(42, 42));
        jButton7.setPreferredSize(new java.awt.Dimension(42, 42));
        jButton7.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton7);

        jButton25.setBackground(new java.awt.Color(245, 244, 244));
        jButton25.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/equiposv1.fw.png"))); // NOI18N
        jButton25.setToolTipText("Equipos");
        jButton25.setFocusable(false);
        jButton25.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton25.setMaximumSize(new java.awt.Dimension(42, 42));
        jButton25.setMinimumSize(new java.awt.Dimension(42, 42));
        jButton25.setPreferredSize(new java.awt.Dimension(42, 42));
        jButton25.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton25ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton25);

        jButton29.setBackground(new java.awt.Color(245, 244, 244));
        jButton29.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/barracasco.png"))); // NOI18N
        jButton29.setToolTipText("Mano de Obra");
        jButton29.setFocusable(false);
        jButton29.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton29.setMaximumSize(new java.awt.Dimension(42, 42));
        jButton29.setMinimumSize(new java.awt.Dimension(42, 42));
        jButton29.setPreferredSize(new java.awt.Dimension(42, 42));
        jButton29.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton29ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton29);

        jButton26.setBackground(new java.awt.Color(245, 244, 244));
        jButton26.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/obra.png"))); // NOI18N
        jButton26.setToolTipText("Abrir Presupuesto de Obra");
        jButton26.setFocusable(false);
        jButton26.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton26.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton26ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton26);

        jButton27.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/capitulobarra.png"))); // NOI18N
        jButton27.setToolTipText("Gestionar Capítulos");
        jButton27.setFocusable(false);
        jButton27.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton27.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton27ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton27);

        jButton28.setBackground(new java.awt.Color(245, 244, 244));
        jButton28.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/apu.png"))); // NOI18N
        jButton28.setToolTipText("Ánalisis Precio Unitario");
        jButton28.setEnabled(false);
        jButton28.setFocusable(false);
        jButton28.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton28.setMaximumSize(new java.awt.Dimension(42, 42));
        jButton28.setMinimumSize(new java.awt.Dimension(42, 42));
        jButton28.setPreferredSize(new java.awt.Dimension(40, 40));
        jButton28.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton28ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton28);

        jButton3.setBackground(new java.awt.Color(245, 244, 244));
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/imprimir.png"))); // NOI18N
        jButton3.setToolTipText("Imprimir Análisis de Precio Unitario");
        jButton3.setEnabled(false);
        jButton3.setFocusable(false);
        jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton3);

        jButton30.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/recalcula1.fw.png"))); // NOI18N
        jButton30.setToolTipText("Recalcular Tabulador");
        jButton30.setFocusable(false);
        jButton30.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton30.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton30.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton30ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton30);

        javax.swing.GroupLayout jInternalFrame1Layout = new javax.swing.GroupLayout(jInternalFrame1.getContentPane());
        jInternalFrame1.getContentPane().setLayout(jInternalFrame1Layout);
        jInternalFrame1Layout.setHorizontalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 1004, Short.MAX_VALUE)
            .addComponent(jScrollPane8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1004, Short.MAX_VALUE)
        );
        jInternalFrame1Layout.setVerticalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jInternalFrame1Layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 589, Short.MAX_VALUE))
        );

        jInternalFrame1.setBounds(0, 0, 1020, 660);
        jDesktopPane1.add(jInternalFrame1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        try {
            jInternalFrame1.setMaximum(true);
        } catch (java.beans.PropertyVetoException e1) {
            e1.printStackTrace();
        }
        jInternalFrame1.getAccessibleContext().setAccessibleParent(jDesktopPane1);

        jMenu1.setText("Precios Referenciales");
        jMenu1.setName("tabuladores"); // NOI18N

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/listar.png"))); // NOI18N
        jMenuItem1.setText("Gestionar");
        jMenuItem1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenuItem1MouseClicked(evt);
            }
        });
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/partidas.fw.png"))); // NOI18N
        jMenuItem2.setText("Grupo de Partidas");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenu3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/maquinas.fw.png"))); // NOI18N
        jMenu3.setText("Matriz de Costos");

        jMenuItem3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/material.png"))); // NOI18N
        jMenuItem3.setText("Materiales");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem3);

        jMenuItem4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/maquinaria.png"))); // NOI18N
        jMenuItem4.setText("Equipos");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem4);

        jMenuItem5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/casco.png"))); // NOI18N
        jMenuItem5.setText("Mano de Obra");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem5);
        jMenu3.add(jSeparator5);

        jMenuItem45.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/apu.png"))); // NOI18N
        jMenuItem45.setText("Matriz de Costos");
        jMenuItem45.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem45ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem45);

        jMenu1.add(jMenu3);

        jMenuItem9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/capitulos.png"))); // NOI18N
        jMenuItem9.setText("Capítulos");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem9);
        jMenu1.add(jSeparator6);

        jMenuItem38.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/recalcula.fw.png"))); // NOI18N
        jMenuItem38.setText("Recalcular Tabuladores");
        jMenuItem38.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem38ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem38);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Presupuestos");

        jMenuItem6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/obra.png"))); // NOI18N
        jMenuItem6.setText("Trabajar Presupuesto");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem6);

        jMenuItem46.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem46.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/agregarverde.fw.png"))); // NOI18N
        jMenuItem46.setText("Nuevo Presupuesto");
        jMenuItem46.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem46ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem46);

        jMenuItem10.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/cambiar.fw.png"))); // NOI18N
        jMenuItem10.setText("Cambiar Presupuesto");
        jMenuItem10.setToolTipText("");
        jMenuItem10.setEnabled(false);
        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem10ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem10);

        jMenuItem7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/listar2.png"))); // NOI18N
        jMenuItem7.setText("Valuaciones");
        jMenuItem7.setEnabled(false);
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem7);

        jMenu15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/moneda.png"))); // NOI18N
        jMenu15.setText("Matriz de Costos");
        jMenu15.setEnabled(false);

        jMenuItem33.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/materialbarra.png"))); // NOI18N
        jMenuItem33.setText("Materiales");
        jMenuItem33.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem33ActionPerformed(evt);
            }
        });
        jMenu15.add(jMenuItem33);

        jMenuItem34.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/equipobarra.png"))); // NOI18N
        jMenuItem34.setText("Equipos");
        jMenuItem34.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem34ActionPerformed(evt);
            }
        });
        jMenu15.add(jMenuItem34);

        jMenuItem35.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/barracasco.png"))); // NOI18N
        jMenuItem35.setText("Mano de Obra");
        jMenuItem35.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem35ActionPerformed(evt);
            }
        });
        jMenu15.add(jMenuItem35);
        jMenu15.add(jSeparator3);

        jMenuItem36.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/imprimir.png"))); // NOI18N
        jMenuItem36.setText("Imprimir Matriz");
        jMenu15.add(jMenuItem36);

        jMenu2.add(jMenu15);

        jMenuItem8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/aumentos.fw.png"))); // NOI18N
        jMenuItem8.setText("Aumentos y Disminuciones");
        jMenuItem8.setEnabled(false);
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem8);

        jMenuItem14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/reloj.fw.png"))); // NOI18N
        jMenuItem14.setText("Cronograma de Actividades");
        jMenuItem14.setEnabled(false);
        jMenuItem14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem14ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem14);

        jMenuItem47.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/moneda.fw.png"))); // NOI18N
        jMenuItem47.setText("Reconsideración de Precios");
        jMenuItem47.setEnabled(false);
        jMenuItem47.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem47ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem47);

        jMenuItem15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/memo.fw.png"))); // NOI18N
        jMenuItem15.setText("Memoria Descriptiva");
        jMenuItem15.setEnabled(false);
        jMenuItem15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem15ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem15);

        jMenuItem37.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/recalcula.fw.png"))); // NOI18N
        jMenuItem37.setText("Recalcular Presupuesto");
        jMenuItem37.setEnabled(false);
        jMenu2.add(jMenuItem37);
        jMenu2.add(jSeparator4);

        jMenu7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/reporte.png"))); // NOI18N
        jMenu7.setText("Reportes de Presupuesto");
        jMenu7.setEnabled(false);

        jMenuItem16.setText("Presupuesto");
        jMenuItem16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem16ActionPerformed(evt);
            }
        });
        jMenu7.add(jMenuItem16);
        jMenu7.add(jSeparator1);

        jMenuItem19.setText("Montos por Capitulos");
        jMenu7.add(jMenuItem19);

        jMenuItem20.setText("Computos Metrícos");
        jMenuItem20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem20ActionPerformed(evt);
            }
        });
        jMenu7.add(jMenuItem20);

        jMenu12.setText("Resúmenes");

        jMenuItem21.setText("Materiales");
        jMenu12.add(jMenuItem21);

        jMenuItem22.setText("Equipo");
        jMenu12.add(jMenuItem22);

        jMenuItem23.setText("Mano de Obra");
        jMenu12.add(jMenuItem23);

        jMenu7.add(jMenu12);

        jMenu13.setText("Resúmenes Detallados");

        jMenuItem24.setText("Materiales");
        jMenu13.add(jMenuItem24);

        jMenuItem25.setText("Equipo");
        jMenu13.add(jMenuItem25);

        jMenuItem26.setText("Mano de Obra");
        jMenu13.add(jMenuItem26);

        jMenu7.add(jMenu13);

        jMenuItem27.setText("Resúmen General");
        jMenu7.add(jMenuItem27);

        jMenuItem28.setText("Resúmen de Pago");
        jMenu7.add(jMenuItem28);
        jMenu7.add(jSeparator2);

        jMenuItem29.setText("Cuadro de Cierre");
        jMenuItem29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem29ActionPerformed(evt);
            }
        });
        jMenu7.add(jMenuItem29);

        jMenu2.add(jMenu7);

        jMenu14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/nuevo.png"))); // NOI18N
        jMenu14.setText("Actas");
        jMenu14.setEnabled(false);

        jMenuItem30.setText("Acta de Inicio");
        jMenu14.add(jMenuItem30);

        jMenuItem31.setText("Acta de Terminación");
        jMenu14.add(jMenuItem31);

        jMenu2.add(jMenu14);

        jMenuBar1.add(jMenu2);

        jMenu11.setText("Parametros");

        jMenuItem17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/llave.fw.png"))); // NOI18N
        jMenuItem17.setText("Propietario");
        jMenuItem17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem17ActionPerformed(evt);
            }
        });
        jMenu11.add(jMenuItem17);

        jMenuItem18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/prop.fw.png"))); // NOI18N
        jMenuItem18.setText("Contratista");
        jMenuItem18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem18ActionPerformed(evt);
            }
        });
        jMenu11.add(jMenuItem18);

        jMenuBar1.add(jMenu11);

        jMenu16.setText("Herramientas");

        jMenu17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/moneda.png"))); // NOI18N
        jMenu17.setText("Tabuladores");

        jMenuItem39.setText("Recuperar");
        jMenuItem39.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem39ActionPerformed(evt);
            }
        });
        jMenu17.add(jMenuItem39);

        jMenu16.add(jMenu17);

        jMenu18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/obra.png"))); // NOI18N
        jMenu18.setText("Presupuestos");

        jMenuItem42.setText("Respaldar");
        jMenu18.add(jMenuItem42);

        jMenu16.add(jMenu18);

        jMenuItem44.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/usuarios.fw.png"))); // NOI18N
        jMenuItem44.setText("Usuarios");
        jMenu16.add(jMenuItem44);

        jMenuBar1.add(jMenu16);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jDesktopPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1017, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jDesktopPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 660, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    public void activatab(int filas){
        
       int numfilas=jTable1.getRowCount();
       if(numfilas>0){
       seleccionado = jTable1.getValueAt(filas, 0).toString();
       String actualiza = "UPDATE mtabus SET seleccionado='1' WHERE id='"+seleccionado+"'";
        String actualiza0 = "UPDATE mtabus SET seleccionado='0' WHERE id!='"+seleccionado+"'";        
        try {
            Statement st1 = (Statement) conexion.createStatement();
            Statement st2 = (Statement) conexion.createStatement();
            st1.execute(actualiza);
            st2.execute(actualiza0);
            super.setTitle("WinSpapu Sistema de Presupuesto y Análisis de Precio Unitario, Tabulador Activo: "+seleccionado);
        } catch (SQLException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
       }
    }
    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        
        row=-1;
        jButton3.setEnabled(false);
        fila = jTable1.rowAtPoint(evt.getPoint());
        buscapartidas(fila);
        DefaultTableModel mtabs = new DefaultTableModel();
        mtabs.setRowCount(0);
        jTable4.setModel(mtabs);
        jTable3.setModel(mtabs);
        jTable5.setModel(mtabs);
        jTextArea1.setText("");
        jButton15.setEnabled(false);
        jButton16.setEnabled(false);
         jButton12.setEnabled(true);
        jTextField2.setText("");
        jLabel27.setText("");
        jButton7.setEnabled(true);
       jButton14.setEnabled(false);
       jButton17.setEnabled(false);
       jLabel35.setText("0.00");
       jButton20.setEnabled(false);
       jLabel15.setText("");
       jLabel17.setText("");
       jLabel21.setText("");
       jLabel19.setText("");
       jLabel25.setText("");
       jLabel23.setText("");
       jLabel29.setText("");
               jButton9.setEnabled(false);
               jButton10.setEnabled(false);
               jButton11.setEnabled(false);
               jButton12.setEnabled(true);
               jButton28.setEnabled(false);
        jButton2.setEnabled(true);
       jButton8.setEnabled(true);
       jButton13.setEnabled(true);
       jButton4.setEnabled(true);
       jButton5.setEnabled(true);
       jButton6.setEnabled(true);
       jLabel33.setText("");
       int filas = jTable1.rowAtPoint(evt.getPoint());
       activatab(filas);
       
    }//GEN-LAST:event_jTable1MouseClicked
    
    
    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
           Nueva partida = new Nueva (this, true, cadena, conexion, this);
           x=(this.getWidth()/2)-295;
            y=(this.getHeight()/2-550/2);
            partida.setBounds(x, y, 700, 550);
            partida.setVisible(true);
            partida();
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        
        String descripcion="", adm="", gastos="", util="", pres="", impuesto="", id="";
        Statement st = null;
        try {
            st = (Statement) conexion.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
        
            descripcion= jTable1.getValueAt(fila, 1).toString();
            adm = jTable1.getValueAt(fila, 4).toString();
            pres = jTable1.getValueAt(fila, 3).toString();
            util = jTable1.getValueAt(fila, 5).toString();
            gastos = jTable1.getValueAt(fila, 6).toString();
            impuesto = jTable1.getValueAt(fila, 7).toString();
            
            String update = "UPDATE mtabus set descri='"+descripcion+"', padyga="+adm+", pcosfin="+gastos+", pimpue="+impuesto+", pprest="+pres+", putild="+util+" WHERE id='"+cadena+"'";
        try {
            st.execute(update);
        } catch (SQLException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
        JOptionPane.showMessageDialog(this, "Se ha actualizado el precio Referencial");
    }//GEN-LAST:event_jButton13ActionPerformed

    
    public void buscamat(){
        float valor;
        contmat =0;
        Statement part=null;
        try {
            part = (Statement) conexion.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
        DefaultTableModel mmtabs = new DefaultTableModel(){@Override
     public boolean isCellEditable (int row, int column) {
        if(column==3) {
             return true;
         }
         return false;
     }
        
        
       
       
 };
        jTable4.setModel(mmtabs);
        try {
            Statement s1 = (Statement) conexion.createStatement();
            String sql="SELECT mm.id, mm.descri, mm.precio, dm.cantidad, mm.unidad, mm.desperdi,"
                    + " ((mm.precio+(mm.precio*(mm.desperdi/100)))*dm.cantidad) as total FROM mmtabs as mm, "
                    + "dmtabs as dm, mptabs as mp WHERE dm.mtabus_id='"+cadena+"' AND mm.mtabus_id=dm.mtabus_id "
                    + "AND dm.numepart=mp.numero "
                   + "AND mp.numero='"+num+"' AND dm.mmtab_id=mm.id GROUP BY mm.id";
            ResultSet rs1 = s1.executeQuery(sql);
            
            ResultSetMetaData rsMd = (ResultSetMetaData) rs1.getMetaData();
            int cantidadColumnas = rsMd.getColumnCount();
             for (int i = 1; i <= cantidadColumnas; i++) {
                 mmtabs.addColumn(rsMd.getColumnLabel(i));
            }
             
             while (rs1.next()) {
                 Object[] filas = new Object[cantidadColumnas];
                for (int i = 0; i < cantidadColumnas; i++) {
                    if(i==6){
                       
                            
                            valor = (float) (Math.rint(Float.valueOf(rs1.getObject(i+1).toString())*100)/100);
                            filas[i]=Float.toString(valor);
                        
                       contmat += valor; 
                    }else {
                        filas[i]=rs1.getObject(i+1);
                    }
                    
                    
                }
               
                mmtabs.addRow(filas);
               
                
            }
             
        } catch (SQLException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
         float auxcontmat;
                auxcontmat = (float) (Math.rint((contmat+0.000001)*100)/100);
        jLabel35.setText(Float.toString(auxcontmat));
        System.out.println("contmat: "+contmat);
        if(buscala==1){
            try {
                actualizaprecio();
            } catch (SQLException ex) {
                Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
            }
        buscala=0;
        }
        cambiacabeceramat();
    }
    
    public void buscaequip(){
         float valor, cont=0, modulo=0;
         conteq=0;
        DefaultTableModel metabs = new DefaultTableModel(){@Override
     public boolean isCellEditable (int row, int column) {
        if(column==4) {
             return true;
         }
         return false;
     }
        
        
       
       
 };
         jTable3.setModel(metabs);
          try {
            Statement s1 = (Statement) conexion.createStatement();
            ResultSet rs2 = s1.executeQuery("SELECT mm.id, mm.descri, mm.precio, mm.deprecia, "
                    + "dm.cantidad, (dm.cantidad*mm.deprecia*mm.precio) as total FROM metabs as mm, "
                    + "deptabs as dm WHERE dm.mtabus_id='"+cadena+"' AND dm.numero='"+num+"'"
                    + " AND dm.metab_id=mm.id AND dm.mtabus_id=mm.mtabus_id GROUP BY mm.id");
            
            ResultSetMetaData rsMd = (ResultSetMetaData) rs2.getMetaData();
            int cantidadColumnas = rsMd.getColumnCount();
             for (int i = 1; i <= cantidadColumnas; i++) {
                 metabs.addColumn(rsMd.getColumnLabel(i));
            }
             
             while (rs2.next()) {
                 System.out.println(" deprecia: "+rs2.getObject(4));
                 Object[] filas = new Object[cantidadColumnas];
                for (int i = 0; i < cantidadColumnas; i++) {
                   if(i==5){
                        if(rs2.getObject(4).toString()==null || Float.valueOf(rs2.getObject(4).toString())==0.00){
                            valor = (float) (Math.rint(Float.valueOf(rs2.getObject(3).toString())*Float.valueOf(rs2.getObject(5).toString())*100)/100);
                           
                            filas[i]= Float.toString(valor);
                        }else{
                            
                            valor = (float) (Math.rint(Float.valueOf(rs2.getObject(i+1).toString())*100)/100);
                            filas[i]=Float.toString(valor);
                        }
                       conteq += valor; 
                    }else {
                        filas[i]=rs2.getObject(i+1);
                    }
                }
                metabs.addRow(filas);
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
          float auxconteq;
          float rendimi = Float.valueOf(jLabel23.getText().toString());
          if(rendimi==0)
              rendimi=1;
          conteq = conteq / (rendimi);
          System.out.println("contequip: "+conteq);
          auxconteq= (float) (Math.rint((conteq+0.000001)*100)/100);
          jLabel37.setText(Float.toString(auxconteq));
           if(buscala==1){
            try {
                actualizaprecio();
            } catch (SQLException ex) {
                Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
            }
        buscala=0;
        }
      cambiacabeceraequip();
    }
    
    public void buscamano(){
        contmano=0;
        float cant=0;
        float bono=0, subsid=0, auxcontmano;
        float valor;
        DefaultTableModel motabs = new DefaultTableModel(){@Override
     public boolean isCellEditable (int row, int column) {
        if(column==2) {
             return true;
         }
         return false;
     }
        
        
       
       
 };
        jTable5.setModel(motabs);
        try {
            Statement s1 = (Statement) conexion.createStatement();
            ResultSet rs3 = s1.executeQuery("SELECT mm.id, mm.descri, "
                    + "dm.cantidad, mm.bono, mm.subsid, mm.salario, "
                    + "(mm.salario*dm.cantidad) as total FROM mmotabs as mm,"
                    + " dmoptabs as dm WHERE dm.mtabus_id='"+cadena+"' AND "
                    + "dm.numero='"+num+"' AND dm.mmotab_id=mm.id AND "
                    + "dm.mtabus_id=mm.mtabus_id GROUP BY mm.id");
           
            ResultSetMetaData rsMd = (ResultSetMetaData) rs3.getMetaData();
            int cantidadColumnas = rsMd.getColumnCount();
             for (int i = 1; i <= cantidadColumnas; i++) {
                 motabs.addColumn(rsMd.getColumnLabel(i));
            }
            
             while (rs3.next()) {
                 Object[] filas = new Object[cantidadColumnas];
                 bono = Float.valueOf(rs3.getObject(4).toString());
                subsid= Float.valueOf(rs3.getObject(5).toString());
                for (int i = 0; i < cantidadColumnas; i++) {
                    if(i==2){
                        cant = cant + Float.valueOf(rs3.getObject(3).toString());
                    }
                    if(i==6){
                        if(Float.valueOf(rs3.getObject(7).toString())==0.00){
                            valor = (float) (Math.rint(Float.valueOf(rs3.getObject(6).toString())*Float.valueOf(rs3.getObject(3).toString())*100)/100);
                           
                            filas[i]= Float.toString(valor);
                        }else{
                            
                            valor = (float) (Math.rint(Float.valueOf(rs3.getObject(i+1).toString())*100)/100);
                            filas[i]=Float.toString(valor);
                        }
                       contmano += valor; 
                       
                    }else {
                        filas[i]=rs3.getObject(i+1);
                    }
                    
                }
                motabs.addRow(filas);
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
            float prestaciones = Float.valueOf(jLabel29.getText().toString())/100;
            auxcontmano = contmano;
            System.out.println("auxcontmano "+auxcontmano);
            prestaciones = contmano * prestaciones;
             System.out.println("prestaciones "+prestaciones);
            bono = cant * bono;
             subsid = cant * subsid;
             auxcontmano = contmano + prestaciones + bono + subsid;
              System.out.println("auxcontmano+prestaciones+bono+subsid "+auxcontmano);
             float auxcont;
             float rendimi = Float.valueOf(jLabel23.getText().toString());
          if(rendimi==0)
              rendimi=1;
             contmano = auxcontmano /  (rendimi); //RENDIMIENTO
            auxcont= (float) (Math.rint((contmano+0.000001)*100)/100);
            System.out.println("contmano: "+contmano);
            jLabel39.setText(Float.toString(auxcont));
             if(buscala==1){
            try {
                actualizaprecio();
            } catch (SQLException ex) {
                Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
            }
        buscala=0;
        }
             cambiacabeceramano();
    }
    
    
    public void buscapartida() throws SQLException{
        contmat=conteq=contmano=0;
        Statement st = (Statement) conexion.createStatement();
        ResultSet rst = st.executeQuery("SELECT mb.descri, mp.unidad, mp.rendimi, mp.porcgad, mp.porcpre,"
                + " mp.porcutil,mp.redondeo FROM mbdats mb, mptabs mp WHERE mp.mtabus_id='"+cadena+"'"
                + " AND mp.numero="+num+" AND mp.mbdat_id=mb.id");
          
        
        while (rst.next()) {
                 
                
                    jLabel19.setText(rst.getObject(1).toString());
                    jLabel21.setText(rst.getObject(2).toString());    
                    jLabel23.setText(rst.getObject(3).toString()); 
                    jLabel25.setText(rst.getObject(4).toString());       
                    jLabel29.setText(rst.getObject(5).toString());   
                    jLabel27.setText(rst.getObject(6).toString());
                    noredondeo = rst.getInt("redondeo");
                
            }
    }
    public void selecpartida(){
            contmat=conteq=contmano=0;
            jButton15.setEnabled(false);
            jButton16.setEnabled(false);
            jButton19.setEnabled(false);
            jButton18.setEnabled(false);
            jButton21.setEnabled(false);
            jButton22.setEnabled(false);
           codicove=jTable2.getValueAt(row,0).toString();
           descri = jTable2.getValueAt(row,1).toString();
           num = jTable2.getValueAt(row,2).toString();
           num2 = jTable2.getValueAt(row,3).toString();
           jLabel33.setText(jTable2.getValueAt(row, 5).toString());
          
           jTextArea1.setText(descri); 
           jButton8.setEnabled(true);
           jButton9.setEnabled(true);
           jButton10.setEnabled(true);
           jButton11.setEnabled(true);
          jButton14.setEnabled(true);
          jButton17.setEnabled(true);
          jButton20.setEnabled(true);
         
          jLabel15.setText(codicove);
          jLabel17.setText(num2);
     try {
          buscapartida();
        
    } catch (SQLException ex) {
        Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
    }
     
          buscamat();
           buscaequip();
           buscamano();
        try {
            actualizaprecio();
            
        } catch (SQLException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseClicked
            //jButton28.setEnabled(true);
            jButton3.setEnabled(true);
            jButton24.setEnabled(true);
            row = jTable2.rowAtPoint(evt.getPoint());
            noredondeo=1;
          
            selecpartida();
               
    }//GEN-LAST:event_jTable2MouseClicked
    public void actualizaprecio() throws SQLException{
        contotal = contmat+conteq+contmano;
           float auxcontotal, admin, financiero, impuesto, util;
           auxcontotal = contotal;
           auxcontotal = (float) auxcontotal;
           System.out.println("conttotal: "+contotal);
           admin = Float.valueOf(jLabel25.getText().toString())/100;
        
           admin = contotal * admin;
           admin = (float) (admin);
          System.out.println("admin: "+admin);
           util = Float.valueOf(jLabel27.getText().toString())/100;
         
           util = (contotal+admin) * util;
         System.out.println("utilidad: "+util);
           auxcontotal = contotal+admin+util;
           financiero=0;
           if(finanzas!=null){
           financiero = Float.valueOf(finanzas.toString())/100;
           
           financiero = auxcontotal * financiero;
           System.out.println("financiero: "+financiero);
           }
          if(impu!=null) 
           impuesto = Float.valueOf(this.impu)/100;
           else
              impuesto=0;
           impuesto = auxcontotal * impuesto;
           System.out.println("impuesto: "+impuesto);
           auxcontotal = auxcontotal + impuesto + financiero;
           System.out.println("auxcontotal antes de redondear: "+auxcontotal);
           auxcontotal = (float) (Math.rint((auxcontotal+0.000001)*100)/100);
           contotal = auxcontotal;
           System.out.println("contotalsinredondeo: "+contotal);
          contotal = (float) (Math.rint((contotal+0.000001)));
          System.out.println("contotalfinal: "+contotal);
          
          
          denumeroaletra nume = new denumeroaletra();
          String letras="";
          jLabel33.setText(formatoNumero.format(auxcontotal));
          if(noredondeo==0){
              contotal=0;
         
          letras=nume.Convertir(String.valueOf(auxcontotal), true);
          }else{
              
              letras=nume.Convertir(String.valueOf(contotal), true);
          }
          
          System.out.println("numenletras "+letras);
           Statement actualiza = (Statement) conexion.createStatement();
            String sql= "UPDATE mptabs set precasu="+contotal+", precunit="+auxcontotal+", redondeo="+noredondeo+", numenletra='"+letras+"' WHERE mtabus_id='"+cadena+"' AND numero="+num;
           actualiza.execute(sql);
          
         
          noredondeo=0;
          
              busca();
              llamaabusca=0;
          
    }
    private void jButton23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton23ActionPerformed
        
               
        tab tab = new tab(this,true, conexion);
        x=(this.getWidth()/2)-295;
        y=(this.getHeight()/2-480/2);
        tab.setBounds(x, y, 680, 480);
        tab.setVisible(true);
       
    }//GEN-LAST:event_jButton23ActionPerformed

    private void jMenuItem1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem1MouseClicked
        
     
    }//GEN-LAST:event_jMenuItem1MouseClicked

    
    public void visible(){
         jInternalFrame1.setVisible(true);
        try {
            jInternalFrame1.setMaximum(true);
            jInternalFrame1.setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
         visible();
       
       
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        tab objtab= new tab(this, true, cadena, conexion);
        x=(this.getWidth()/2)-295;
        y=(this.getHeight()/2-480/2);
        objtab.setBounds(x, y, 680, 480);
        objtab.setVisible(true);
       
    }//GEN-LAST:event_jButton4ActionPerformed
// COPIAR*******************
    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        copia copiatab= new copia(this, true, cadena, descrip, conexion);
        x=(this.getWidth()/2)-295;
        y=(this.getHeight()/2-203);
        copiatab.setBounds(x, y, 680, 380);
        copiatab.setVisible(true);
        
        
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        busqueda = jTextField1.getText().toString();
        DefaultTableModel mptab = new DefaultTableModel() {@Override
        public boolean isCellEditable (int fila, int columna) {
           if(columna==1 || columna>2){
               return true;
           }
            return false;
            }
        };
     
               
               jTable1.setModel(mptab);
       
        try {
            Statement s = (Statement) conexion.createStatement();
            ResultSet rs = s.executeQuery("SELECT id,descri, DATE_FORMAT(vigencia, '%d/%m/%Y'), padyga, pprest, putild, pcosfin, pimpue FROM Mtabus WHERE status=1 AND (id LIKE '%"+busqueda+"%' || descri LIKE '%"+busqueda+"%')");
            ResultSetMetaData rsMd = (ResultSetMetaData) rs.getMetaData();
            int cantidadColumnas = rsMd.getColumnCount();
             for (int i = 1; i <= cantidadColumnas; i++) {
                 mptab.addColumn(rsMd.getColumnLabel(i));
            }
             
             while (rs.next()) {
                 Object[] filas = new Object[cantidadColumnas];
                for (int i = 0; i < cantidadColumnas; i++) {
                    filas[i]=rs.getObject(i+1);
                }
                mptab.addRow(filas);
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
               cambiarcabecera();
        
    }//GEN-LAST:event_jButton1ActionPerformed

    public void busca(){
               busqueda = jTextField2.getText().toString();       
       DefaultTableModel mtabs = new DefaultTableModel();
       mtabs.setRowCount(0);
       if(llamaabusca==1){
        jTable4.setModel(mtabs);
        jTable3.setModel(mtabs);
        jTable5.setModel(mtabs);
        jTextArea1.setText("");
       }
        DefaultTableModel mtabu = new DefaultTableModel() {@Override
        public boolean isCellEditable (int fila, int columna) {
           if(columna==1 || columna>2){
               return true;
           }
            return false;
            }
            @Override
        public Class getColumnClass(int columna)
        {
            if (columna == 2 || columna==3) {
                return Integer.class;
            }
            
            return Object.class;
        }
        };
               
               jTable2.setModel(mtabu);
       
        try {
            Statement s = (Statement) conexion.createStatement();
            //ResultSet rs = s.executeQuery("SELECT id,descri, DATE_FORMAT(vigencia, '%d/%m/%Y'), padyga, pprest, putild, pcosfin, pimpue FROM Mtabus WHERE id LIKE '%"+busqueda+"%' || descri LIKE '%"+busqueda+"%'");
            ResultSet rs = s.executeQuery("SELECT codicove, descri, numero, numegrup, unidad, IF(precunit=0,precasu, precunit) as precunit, mbdat_id"
                    + " FROM Mptabs m WHERE status=1 AND m.mtabus_id = '"+cadena+"' AND (codicove LIKE '%"+busqueda+"%' || descri LIKE '%"+busqueda+"%' || numegrup LIKE '%"+busqueda+"%') ORDER BY numegrup");
            ResultSetMetaData rsMd = (ResultSetMetaData) rs.getMetaData();
            int cantidadColumnas = rsMd.getColumnCount();
             for (int i = 1; i <= cantidadColumnas; i++) {
                 mtabu.addColumn(rsMd.getColumnLabel(i));
            }
             
             while (rs.next()) {
                 Object[] filas = new Object[cantidadColumnas];
                for (int i = 0; i < cantidadColumnas; i++) {
                    filas[i]=rs.getObject(i+1);
                }
                mtabu.addRow(filas);
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
             jTable2.getColumnModel().getColumn(2).setMaxWidth(0);

             jTable2.getColumnModel().getColumn(2).setMinWidth(0);

             jTable2.getTableHeader().getColumnModel().getColumn(2).setMaxWidth(0);

             jTable2.getTableHeader().getColumnModel().getColumn(2).setMinWidth(0);
             jTable2.getColumnModel().getColumn(6).setMaxWidth(0);

             jTable2.getColumnModel().getColumn(6).setMinWidth(0);

             jTable2.getTableHeader().getColumnModel().getColumn(6).setMaxWidth(0);

             jTable2.getTableHeader().getColumnModel().getColumn(6).setMinWidth(0);
               cambiacabecera2();
           if(row!=-1&&llamaabusca==0){
                jTable2.setRowSelectionInterval(row,row);
                
               
            try {
                buscapartida();
            } catch (SQLException ex) {
                Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
            }
             }
               
    }
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        
        llamaabusca=1;
        busca();
        llamaabusca=0;      
        
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
       DefaultTableModel mtabs = new DefaultTableModel();
          Statement s= null;
        try {
            s = (Statement) conexion.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            s.execute("UPDATE Mtabus set status=0 WHERE id='"+cadena+"'");
            mtabs.setRowCount(0);
            jTable2.setModel(mtabs);
            jTable3.setModel(mtabs);
            jTable4.setModel(mtabs);
            jTable5.setModel(mtabs);
            JOptionPane.showMessageDialog(this, "El precio Referencial Código: "+cadena+" ha sido Respaldado");
            buscatab();
        } catch (SQLException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
          Nueva edita = new Nueva (this, true, cadena, num, conexion,this);
           x=(this.getWidth()/2)-295;
            y=(this.getHeight()/2)-550/2;
            edita.setBounds(x, y, 790, 550);            
            edita.setVisible(true);
             noredondeo=1;
            selecpartida();
            
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
           
        materiales mat = new materiales(this, true, cadena, num, codicove,this);
           x=(this.getWidth()/2)-315;
            y=(this.getHeight()/2-275);
            mat.setBounds(x, y, 750,550);            
            mat.setVisible(true);
            buscamat();
            noredondeo=0;
            buscaequip();
            buscamano();     
        try {
            actualizaprecio();
        } catch (SQLException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
       Statement s= null;
        try {
            s = (Statement) conexion.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            s.execute("UPDATE Mptabs set status=0 WHERE mtabus_id='"+cadena+"' AND numero="+num);
            JOptionPane.showMessageDialog(this, "La partida del Precio Referencial Código: "+cadena+" número: "+num+" ha sido Respaldado");
            partida();
        } catch (SQLException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        
        copiar copiatab = null;
        try {
            copiatab = new copiar(this, true, num, cadena, conexion, codicove, num2);
        } catch (SQLException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
        x=(this.getWidth()/2)-680/2;
        y=(this.getHeight()/2-400/2);
        copiatab.setBounds(x, y, 680, 400);
        copiatab.setVisible(true);
        partida();
        
    }//GEN-LAST:event_jButton10ActionPerformed

    
    
    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
//***********************************************REORDENAR***************************************************************        
        int op = JOptionPane.showConfirmDialog(presupuesto, "Proceso Delicado, desea continuar con el reordenado?", "Reordenar Partidas", JOptionPane.YES_NO_OPTION);
        if(op==JOptionPane.YES_OPTION){
       int n = mptabs.getRowCount();
       
      Vector data = mptabs.getDataVector();
       String[] coldata = new String[mptabs.getRowCount()];
       
       int [] numeros = new int[mptabs.getRowCount()], grupo = new int[mptabs.getRowCount()];
       
       
        for (int i=0; i<coldata.length; i++) {
        coldata[i] = ((Vector)data.get(i)).get(0).toString();
        numeros[i] = Integer.valueOf(((Vector)data.get(i)).get(2).toString());
        grupo[i] = Integer.valueOf(((Vector)data.get(i)).get(6).toString());
        
    }
       
        for(int k=0;k<coldata.length;k++) {
            for(int f=0;f<coldata.length-1-k;f++) {
                if (coldata[f].compareTo(coldata[f+1])>0) {
                    String auxnota;
                    auxnota=coldata[f].toString();
                    coldata[f]=coldata[f+1];
                    coldata[f+1]=auxnota;
                    int auxnumero, auxgrupo;
                    auxnumero=numeros[f];
                    auxgrupo = grupo[f];
                    numeros[f]=numeros[f+1];
                    grupo[f]=grupo[f+1];
                    numeros[f+1]=auxnumero;
                    grupo[f+1]=auxgrupo;
                }
            }
        }
        
        
        for(int k=0;k<coldata.length;k++) {
            for(int f=0;f<coldata.length-1-k;f++) {
                if (grupo[f]>grupo[f+1]) {
                    int auxnota;
                    auxnota= grupo[f];
                    grupo[f]=grupo[f+1];
                    grupo[f+1]=auxnota;
                    int auxnumero;
                    String auxcodi;
                    auxnumero =numeros[f];
                    auxcodi = coldata[f];
                    numeros[f]=numeros[f+1];
                    coldata[f]=coldata[f+1];
                    numeros[f+1]=auxnumero;
                    coldata[f+1]=auxcodi;
                }
            }
        }
                  
      reordena reor = new reordena(conexion, coldata, numeros, cadena, this);
      reor.start();
        }
       
       
    }//GEN-LAST:event_jButton12ActionPerformed
    
    private void jTable4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable4MouseClicked
       
        filamate = jTable4.rowAtPoint(evt.getPoint());       
        jButton15.setEnabled(true);
        jButton16.setEnabled(true);
        codimate = jTable4.getValueAt(filamate, 0).toString();
   
    }//GEN-LAST:event_jTable4MouseClicked

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        String sql = "";
        int registros = jTable4.getRowCount();
        int columnas = jTable4.getColumnCount();
        String materiales [] = new String[registros], cantidades[] = new String[registros];
        
        int i, j;
        Object objeto;
        for (i = 0; i < registros; i++) {
           
            for (j = 0; j < columnas; j++) {

                objeto = jTable4.getValueAt(i, j);
               
                        materiales[i] = (String) jTable4.getValueAt(i, 0);
                       cantidades[i] =  jTable4.getValueAt(i, 3).toString();
                       try {
            //GUARDAR MATERIALES*************
                        Statement stmate = (Statement) conexion.createStatement();
                        sql = "UPDATE dmtabs set cantidad="+cantidades[i]+" WHERE mtabus_id='"+cadena+"' AND numepart ="+num+" AND mmtab_id='"+materiales[i]+"'";
                        stmate.execute(sql);
        
                    } catch (SQLException ex) {
                        Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
                    }
               

            }

        }
        noredondeo=0;
        buscaequip();
        buscamano();     
        buscala = 1;
        buscamat();
        
       
    }//GEN-LAST:event_jButton15ActionPerformed

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
        equipos equi = new equipos(this,true, cadena, num, codicove, conexion);
        x=(this.getWidth()/2)-315;
            y=(this.getHeight()/2-275);
            equi.setBounds(x, y, 750,550);            
            equi.setVisible(true);
            buscaequip();
            
          
            noredondeo=0;
              buscamat();
            buscamano();     
        try {
            actualizaprecio();
        } catch (SQLException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton17ActionPerformed

    private void jTable3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable3MouseClicked
       jButton18.setEnabled(true);
       jButton19.setEnabled(true);
       filaequipo= jTable3.rowAtPoint(evt.getPoint());
       codiequipo = (String) jTable3.getValueAt(filaequipo, 0);
        
    }//GEN-LAST:event_jTable3MouseClicked

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed
        String sql = "";
        conteq=0;
        int registros = jTable3.getRowCount();
        int columnas = jTable3.getColumnCount();
        String equipos [] = new String[registros], cantidades[] = new String[registros];
        
        int i, j;
        Object objeto;
        for (i = 0; i < registros; i++) {
           
            for (j = 0; j < columnas; j++) {

                objeto = jTable3.getValueAt(i, j);
               
                        equipos[i] = (String) jTable3.getValueAt(i, 0);
                       cantidades[i] =  jTable3.getValueAt(i, 4).toString();
                       try {
            //GUARDAR MATERIALES*************
                        Statement stmate = (Statement) conexion.createStatement();
                        sql = "UPDATE deptabs set cantidad="+cantidades[i]+" WHERE mtabus_id='"+cadena+"' AND numero ="+num+" AND metab_id='"+equipos[i]+"'";
                        stmate.execute(sql);
        
                    } catch (SQLException ex) {
                        Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
                    }
               

            }

        }
         noredondeo=0;
        buscamat();
        buscamano();
        buscala = 1;
        buscaequip();
    }//GEN-LAST:event_jButton18ActionPerformed

    private void jButton20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton20ActionPerformed
        manoobra mano = new manoobra(this,true, cadena, num, codicove, conexion);
         x=(this.getWidth()/2)-315;
            y=(this.getHeight()/2-275);
            mano.setBounds(x, y, 750,550);            
            mano.setVisible(true);
            buscamano();
        
            noredondeo=0;
              buscamat();
            buscaequip();     
        try {
            actualizaprecio();
        } catch (SQLException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton20ActionPerformed

    private void jTable5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable5MouseClicked
        jButton21.setEnabled(true);
        jButton22.setEnabled(true);
        filamano = jTable5.rowAtPoint(evt.getPoint());
    }//GEN-LAST:event_jTable5MouseClicked

    private void jButton21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton21ActionPerformed
        String sql = "";
        contmano=0;
        int registros = jTable5.getRowCount();
        int columnas = jTable5.getColumnCount();
        String mano [] = new String[registros], cantidades[] = new String[registros];
        
        int i, j;
        Object objeto;
        for (i = 0; i < registros; i++) {
           
            for (j = 0; j < columnas; j++) {

                objeto = jTable5.getValueAt(i, j);
               
                        mano[i] = (String) jTable5.getValueAt(i, 0);
                       cantidades[i] =  jTable5.getValueAt(i, 2).toString();
                       try {
            //GUARDAR MATERIALES*************
                        Statement stmate = (Statement) conexion.createStatement();
                        sql = "UPDATE dmoptabs set cantidad="+cantidades[i]+" WHERE mtabus_id='"+cadena+"' AND numero ="+num+" AND mmotab_id='"+mano[i]+"'";
                        stmate.execute(sql);
        
                    } catch (SQLException ex) {
                        Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
                    }
               

            }

        }
         noredondeo=0;
        buscamat();
        buscaequip();
        buscala = 1;
        buscamano();
    }//GEN-LAST:event_jButton21ActionPerformed

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        
         String sql="DELETE FROM dmtabs WHERE mtabus_id='"+cadena+"' AND numepart="+num+" AND mmtab_id='"+codimate+"'";
        try {
            Statement st = (Statement) conexion.createStatement();
           int op = JOptionPane.showConfirmDialog(null, "Desea Elimar el material del Tabulador "+cadena+" de la partida número "+num2+" código"+codimate+"? ");
          
          if(op==0){
           st.execute(sql);
          
           JOptionPane.showMessageDialog(null, "Se ha eliminado el material");
           buscala = 1;
           buscamat();
        }
        } catch (SQLException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }//GEN-LAST:event_jButton16ActionPerformed

    private void jButton19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton19ActionPerformed
         String sql="DELETE FROM deptabs WHERE mtabus_id='"+cadena+"' AND numero="+num+" AND metab_id='"+codiequipo+"'";
        try {
            Statement st = (Statement) conexion.createStatement();
           int op = JOptionPane.showConfirmDialog(null, "Desea Elimar el equipo del Tabulador "+cadena+" de la partida número "+num2+" código"+codimate+"? ");
          
          if(op==0){
           st.execute(sql);
          
           JOptionPane.showMessageDialog(null, "Se ha eliminado el equipo");
           buscala = 1;
           buscaequip();
        }
        } catch (SQLException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton19ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        grupo nuevo = new grupo(this, false, conexion, obj);
         x=(this.getWidth()/2)-295;
            y=(this.getHeight()/2-203);
            nuevo.setBounds(x, y, 700,300);            
           nuevo.setVisible(true);
          
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        matrizmateriales mat= new matrizmateriales(this, false, conexion, obj);
       x=(this.getWidth()/2)-500;
            y=(this.getHeight()/2-360);
            mat.setBounds(x, y,1000,720);            
           mat.setVisible(true);
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        matrizequipos equip = new matrizequipos(this, false,conexion, obj);
       x=(this.getWidth()/2)-500;
            y=(this.getHeight()/2-360);
           equip.setBounds(x, y,1000,720);             
           equip.setVisible(true);
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jInternalFrame1ComponentHidden(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jInternalFrame1ComponentHidden
 
        
    }//GEN-LAST:event_jInternalFrame1ComponentHidden

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        matrizmano mano = new matrizmano(this, false,conexion, obj);
        x=(this.getWidth()/2)-500;
            y=(this.getHeight()/2-360);
            mano.setBounds(x, y,1000,720);             
           mano.setVisible(true);
    }//GEN-LAST:event_jMenuItem5ActionPerformed

private void jTable4KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable4KeyPressed
    String codigomate, cantidad, sql;
    System.out.println("TECLAS: "+evt.getKeyCode());
    if(evt.getKeyCode()==9 || evt.getKeyCode()==10){
            codigomate=jTable4.getValueAt( filamate, 0).toString();
            cantidad = jTable4.getValueAt(filamate, 3).toString();
            jTable4.removeEditor();
            System.out.println("TECLAS: "+evt.getKeyCode());
            Statement stmate;
            try {
                stmate = (Statement) conexion.createStatement();
                sql = "UPDATE dmtabs set cantidad="+cantidad+" WHERE mtabus_id='"+cadena+"' AND numepart ="+num+" AND mmtab_id='"+codigomate+"'";
            stmate.execute(sql);
                 noredondeo=0;
                buscaequip();
                buscamano();
                buscala = 1;
                buscamat();
            } catch (SQLException ex) {
                Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
            }
            
    }
        
}//GEN-LAST:event_jTable4KeyPressed

private void jTable4PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jTable4PropertyChange
    
     
}//GEN-LAST:event_jTable4PropertyChange

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        matrizmateriales mat= new matrizmateriales(this, false, conexion, obj);
        x=(this.getWidth()/2)-500;
            y=(this.getHeight()/2-360);
            mat.setBounds(x, y,1000,720);            
           mat.setVisible(true);
          
        
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton24ActionPerformed
        copiarapu apu = new copiarapu(this, true, obj, num, cadena, conexion);
         x=(this.getWidth()/2)-300;
            y=(this.getHeight()/2-300/2);
            apu.setBounds(x, y, 350,300);            
           apu.setVisible(true);
          
           selecpartida();
           
        
        
        
        // TODO add your handling code here:}//GEN-LAST:event_jButton24ActionPerformed
    }
        private void jTable3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable3KeyPressed
           String codigomate, cantidad, sql;
    System.out.println("TECLAS: "+evt.getKeyCode());
    if(evt.getKeyCode()==9 || evt.getKeyCode()==10){
            codigomate=jTable3.getValueAt(filaequipo , 0).toString();
            cantidad = jTable3.getValueAt(filaequipo, 4).toString();
            jTable3.removeEditor();
            System.out.println("TECLAS: "+evt.getKeyCode());
            Statement stmate;
            try {
                stmate = (Statement) conexion.createStatement();
                sql = "UPDATE deptabs set cantidad="+cantidad+" WHERE mtabus_id='"+cadena+"' AND numero ="+num+" AND metab_id='"+codigomate+"'";
            stmate.execute(sql);
                 noredondeo=0;
                buscaequip();
                buscamano();
                buscala = 1;
                buscamat();
            } catch (SQLException ex) {
                Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
            }
            
    }
    
    }//GEN-LAST:event_jTable3KeyPressed

    private void jTable5KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable5KeyPressed
           String codigomate, cantidad, sql;
    System.out.println("TECLAS: "+evt.getKeyCode());
    if(evt.getKeyCode()==9 || evt.getKeyCode()==10){
            codigomate=jTable5.getValueAt( filamano, 0).toString();
            cantidad = jTable5.getValueAt(filamano,2).toString();
            jTable5.removeEditor();
            System.out.println("TECLAS: "+evt.getKeyCode());
            Statement stmate;
            try {
                stmate = (Statement) conexion.createStatement();
                sql = "UPDATE dmoptabs set cantidad="+cantidad+" WHERE mtabus_id='"+cadena+"' AND numero ="+num+" AND mmotab_id='"+codigomate+"'";
            stmate.execute(sql);
                 noredondeo=0;
                buscaequip();
                buscamano();
                buscala = 1;
                buscamat();
            } catch (SQLException ex) {
                Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
            }
            
    }
    }//GEN-LAST:event_jTable5KeyPressed

    private void jButton25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton25ActionPerformed
        matrizequipos equip = new matrizequipos(this, false,conexion, obj);
       x=(this.getWidth()/2)-500;
            y=(this.getHeight()/2-360);
           equip.setBounds(x, y,1000,720);          
           equip.setVisible(true);
    }//GEN-LAST:event_jButton25ActionPerformed

    private void jButton29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton29ActionPerformed
         matrizmano mano = new matrizmano(this, false,conexion, obj);
      x=(this.getWidth()/2)-500;
            y=(this.getHeight()/2-360);
            mano.setBounds(x, y,1000,720);        
           mano.setVisible(true);
    }//GEN-LAST:event_jButton29ActionPerformed

    
    public void entrapres(){
                
        if(entro==0){
            
             presupuesto = new Presupuesto(conexion, this);
             presupuesto.setBounds(0, 0, jDesktopPane1.getWidth(), jDesktopPane1.getHeight());
             jDesktopPane1.add(presupuesto);
             entro=1;
             presupuesto.setVisible(true);
              x=(this.getWidth()/2)-350;
             y=(this.getHeight()/2-250);
             tabpresupuesto  pres = new tabpresupuesto(this, true, presupuesto, conexion, cadena,x,y);
            
             pres.setBounds(x, y, 700,500);            
             pres.setVisible(true);
             
        try {
             presupuesto.setSelected(true);
             presupuesto.setMaximum(true);
         
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
        }else{
            try {
                presupuesto.setSelected(true);
                presupuesto.setMaximum(true);
                presupuesto.setVisible(true);
            } catch (PropertyVetoException ex) {
                Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    public void setpres(String pres){
        this.presup = pres;
    }
    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        entrapres();
        jMenuItem10.setEnabled(true);
        jMenuItem7.setEnabled(true);
        jMenuItem15.setEnabled(true);
        jMenu15.setEnabled(true);
        jMenu7.setEnabled(true);
        jMenu14.setEnabled(true);
        jMenuItem8.setEnabled(true);
        jMenuItem14.setEnabled(true);
        jMenuItem18.setEnabled(true);
        jMenuItem47.setEnabled(true);
        jMenuItem37.setEnabled(true);
    }//GEN-LAST:event_jMenuItem6ActionPerformed
    public void setentro(int ent){
        entro=ent;
       

    }
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        reporteapu apu = new reporteapu(this, false, conexion, cadena, num);
        int xy = (this.getWidth()/2)-450/2;
        int yy = (this.getHeight()/2)-300/2;
        apu.setBounds(xy,yy, 450,300);
        apu.setVisible(true);
        
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton28ActionPerformed
        apu analisis = new apu(this, false, conexion, cadena, num);
         x=(this.getWidth()/2)-400;
            y=(this.getHeight()/2-300);
           analisis.setBounds(x, y, 800,600);            
           analisis.setVisible(true);
    }//GEN-LAST:event_jButton28ActionPerformed

    public void entro1(){
        if(entro==0){
            presupuesto = new Presupuesto(conexion, this);
             
             Nuevo  pres = new Nuevo(this, true, presupuesto, conexion);
             x=(this.getWidth()/2)-350;
             y=(this.getHeight()/2-250);
             pres.setBounds(x, y, 900,550);            
             pres.setVisible(true);
                
        if(haypresupuesto==1){
            jMenuItem10.setEnabled(true);
            jMenuItem6.setEnabled(true);
            jButton26.setEnabled(true);
        jMenuItem7.setEnabled(true);
        jMenuItem15.setEnabled(true);
        jMenu15.setEnabled(true);
        jMenu7.setEnabled(true);
        jMenu14.setEnabled(true);
        jMenuItem8.setEnabled(true);
        jMenuItem14.setEnabled(true);
        jMenuItem18.setEnabled(true);
        jMenuItem37.setEnabled(true);
        try {
         
             presupuesto.setBounds(0, 0, jDesktopPane1.getWidth(), jDesktopPane1.getHeight());
             jDesktopPane1.add(presupuesto);
             entro=1;
             presupuesto.setVisible(true);
             presupuesto.setSelected(true);
             presupuesto.setMaximum(true);
         
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
        haypresupuesto=0;
        }
        }else{
            try {
                Nuevo  pres = new Nuevo(this, true, presupuesto, conexion);
             x=(this.getWidth()/2)-350;
             y=(this.getHeight()/2-250);
             pres.setBounds(x, y, 900,550);            
             pres.setVisible(true);
                presupuesto.setSelected(true);
            } catch (PropertyVetoException ex) {
                Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
            }
        }   
    }
    public void entro(){
        if(entro==0){
            
             presupuesto = new Presupuesto(conexion, this);
             presupuesto.setBounds(0, 0, jDesktopPane1.getWidth(), jDesktopPane1.getHeight());
             jDesktopPane1.add(presupuesto);
             entro=1;
             presupuesto.setVisible(true);
             x=(this.getWidth()/2)-350;
             y=(this.getHeight()/2-250);
             tabpresupuesto  pres = new tabpresupuesto(this, true, presupuesto, conexion, cadena,x,y);
             x=(this.getWidth()/2)-350;
             y=(this.getHeight()/2-250);
             pres.setBounds(x, y, 700,500);            
             pres.setVisible(true);
             jMenuItem10.setEnabled(true);
            
        jMenuItem7.setEnabled(true);
        jMenuItem15.setEnabled(true);
        jMenu15.setEnabled(true);
        jMenu7.setEnabled(true);
        jMenu14.setEnabled(true);
        jMenuItem8.setEnabled(true);
        jMenuItem14.setEnabled(true);
        jMenuItem18.setEnabled(true);
        jMenuItem37.setEnabled(true);
        try {
             presupuesto.setSelected(true);
             presupuesto.setMaximum(true);
         
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
        }else{
            try {
                presupuesto.setSelected(true);
            } catch (PropertyVetoException ex) {
                Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
            }
        }   
    }
    private void jButton26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton26ActionPerformed
         entro();
    }//GEN-LAST:event_jButton26ActionPerformed

    private void jButton27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton27ActionPerformed
        capitulos cap = new capitulos(this, true, conexion, cadena);
        x=(this.getWidth()/2)-(700/2);
        y=(this.getHeight()/2)-(600/2);
        cap.setBounds(x, y, 700, 600);
        cap.setVisible(true);
        
    }//GEN-LAST:event_jButton27ActionPerformed

    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed
       x=(this.getWidth()/2)-350;
             y=(this.getHeight()/2-250);
        tabpresupuesto  pres = new tabpresupuesto(this, true, presupuesto, conexion, cadena,x,y);
             
             pres.setBounds(x, y, 700,500);            
             pres.setVisible(true);
    }//GEN-LAST:event_jMenuItem10ActionPerformed

    private void jMenuItem15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem15ActionPerformed
         memoria mem = new memoria(this, true, conexion, presup);
         int xi = (this.getWidth()/2)-700/2;
          int yi = (this.getHeight()/2)-500/2;
          mem.setBounds(xi, yi, 700, 500);
          mem.setVisible(true);
    }//GEN-LAST:event_jMenuItem15ActionPerformed

    private void jMenuItem17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem17ActionPerformed
        propietario prop = new propietario(this, true, conexion);
        int xi = (this.getWidth()/2)-800/2;
          int yi = (this.getHeight()/2)-400/2;
          prop.setBounds(xi, yi, 800, 400);
          prop.setVisible(true);
        
    }//GEN-LAST:event_jMenuItem17ActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        
        
    }//GEN-LAST:event_formWindowClosed

    private void jMenuItem18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem18ActionPerformed
        contratistas prop = new contratistas(this, true, conexion);
        int xi = (this.getWidth()/2)-800/2;
          int yi = (this.getHeight()/2)-650/2;
          prop.setBounds(xi, yi, 800, 650);
          prop.setVisible(true);
    }//GEN-LAST:event_jMenuItem18ActionPerformed

    private void jMenuItem45ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem45ActionPerformed
        confirmarmatriz conf = new confirmarmatriz(this, false,conexion, cadena,this);
         int xi = (this.getWidth()/2)-100;
          int yi = (this.getHeight()/2)-100;
          conf.setBounds(xi, yi, 250, 300);
          conf.setVisible(true);
        
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem45ActionPerformed

    private void jMenuItem46ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem46ActionPerformed
         entro1();
    }//GEN-LAST:event_jMenuItem46ActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
        capitulos cap = new capitulos(this, true, conexion, cadena);
        x=(this.getWidth()/2)-(700/2);
        y=(this.getHeight()/2)-(600/2);
        cap.setBounds(x, y, 700, 600);
        cap.setVisible(true);
    }//GEN-LAST:event_jMenuItem9ActionPerformed

private void jMenuItem39ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem39ActionPerformed
    RecuperarTab recu=new RecuperarTab(this, true,conexion, this);
    int xi=(this.getWidth()/2)-600/2;
    int yi=(this.getHeight()/2)-200/2;
    recu.setBounds(xi, yi, 600, 250);
    recu.setVisible(true);        
}//GEN-LAST:event_jMenuItem39ActionPerformed

    private void jTextField2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField2FocusGained
jTextField2.setText("");    
busqueda="";// TODO add your handling code here:
    }//GEN-LAST:event_jTextField2FocusGained

private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        valuacion val = new valuacion(this, true, conexion, presup);
        int xv = (this.getWidth()/2)-375;
        int yv = (this.getHeight()/2)-700/2;
        val.setBounds(xv, yv, 800, 700);
        val.setVisible(true);
}//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jMenuItem38ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem38ActionPerformed
    recalcula();        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem38ActionPerformed

    private void jButton30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton30ActionPerformed
recalcula();        // TODO add your handling code here:
    }//GEN-LAST:event_jButton30ActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        float total = (float) presupuesto.gettotal();
        aumentosdismi aumento = new aumentosdismi(this, true, presup, conexion,total);
        int xi = (this.getWidth()/2)-1200/2;
        int yi = (this.getHeight()/2)-650/2;
        aumento.setBounds(xi, yi, 1250, 700);
        aumento.setVisible(true);
        
        
        
                // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jMenuItem14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem14ActionPerformed
               float total = (float) presupuesto.gettotal();
        diagrama cron = new diagrama(this, true,conexion,presup,total);
        int xi = (this.getWidth()/2)-1150/2;
        int yi = (this.getHeight()/2)-720/2;
        cron.setBounds(xi, yi,1200, 720);
        cron.setVisible(true);
        
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem14ActionPerformed

    private void jMenuItem47ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem47ActionPerformed
String select = "SELECT count(*) FROM mppres WHERE tipo='VP' AND ("
                + "mpre_id='"+presup+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+presup+"'))";
        int count=0;
        try {
            Statement sts = (Statement) conexion.createStatement();
            ResultSet rsts = sts.executeQuery(select);
            while(rsts.next()){
                count=rsts.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Presupuesto.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(count==0){
            String cuentavalu="SELECT COUNT(*) FROM dvalus WHERE mpre_id='"+presup+"'";
            int cuen=0;
            try {
                Statement sts = (Statement) conexion.createStatement();
                ResultSet rsts = sts.executeQuery(cuentavalu);
                while(rsts.next()){
                    cuen = rsts.getInt(1);
                }
            } catch (SQLException ex) {
                Logger.getLogger(Presupuesto.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(cuen>0){
            parametrorecon para = new parametrorecon(this, true, conexion, presup, "1", presup+" VP-1");
            int xi = (this.getWidth()/2)-550/2;
        int yi = (this.getHeight()/2)-600/2;
        para.setBounds(xi, yi, 550, 600);
        para.setVisible(true);
            }else{
                JOptionPane.showMessageDialog(rootPane, "Debe hacer por lo menos una valuación para poder reconsiderarla");
            }
        }
         String select1 = "SELECT count(*) FROM mppres WHERE tipo='VP' AND ("
                + "mpre_id='"+presup+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+presup+"'))";
        int count1=0;
        try {
            Statement sts = (Statement) conexion.createStatement();
            ResultSet rsts = sts.executeQuery(select1);
            while(rsts.next()){
                count1=rsts.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Presupuesto.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(count1>0){
        reconsideraciones recon = new reconsideraciones(this, true, presup, conexion, presupuesto,this);
       int xi = (this.getWidth()/2)-800/2;
        int yi = (this.getHeight()/2)-600/2;
        recon.setBounds(xi, yi, 800, 600);
        recon.setVisible(true);
        try {
            presupuesto.buscapartida();
        } catch (SQLException ex) {
            Logger.getLogger(Presupuesto.class.getName()).log(Level.SEVERE, null, ex);
        }
        }else{
            JOptionPane.showMessageDialog(null, "Debe agregar las partidas para abrir la reconsideración de precios");
        }
        
        
    }//GEN-LAST:event_jMenuItem47ActionPerformed

    private void jMenuItem33ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem33ActionPerformed
        matrizmaterialespres materiales = new matrizmaterialespres(this, true, conexion, presup,this);
        int xi = (this.getWidth()/2)-700/2;
        int yi = (this.getHeight()/2)-500/2;
        materiales.setBounds(xi, yi, 700, 500);
        materiales.setVisible(true);
        
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem33ActionPerformed

    private void jMenuItem16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem16ActionPerformed
        double totalpres = presupuesto.gettotal();
        reportepresupuesto report = new reportepresupuesto(this, false, conexion, presup,totalpres);
         int xi = (this.getWidth()/2)-700/2;
        int yi = (this.getHeight()/2)-450/2;
         report.setBounds(xi, yi, 700, 450);
        report.setVisible(true);
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem16ActionPerformed

private void jMenuItem34ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem34ActionPerformed
// TODO add your handling code here:
    
        matrizequipospres equipos = new matrizequipospres(this, true, conexion, presup,this);
        int xi = (this.getWidth()/2)-700/2;
        int yi = (this.getHeight()/2)-500/2;
        equipos.setBounds(xi, yi, 700, 500);
        equipos.setVisible(true);
}//GEN-LAST:event_jMenuItem34ActionPerformed

private void jMenuItem35ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem35ActionPerformed
    matrizmanopres manoobra = new matrizmanopres(this, true, conexion, presup,this);
        int xi = (this.getWidth()/2)-700/2;
        int yi = (this.getHeight()/2)-500/2;
        manoobra.setBounds(xi, yi, 700, 500);
        manoobra.setVisible(true);
    
    // TODO add your handling code here:
}//GEN-LAST:event_jMenuItem35ActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing

        int op = JOptionPane.showConfirmDialog(presupuesto, "¿Desea Cerrar?", "Cerrar", JOptionPane.YES_NO_OPTION);
        if(op==JOptionPane.YES_OPTION){
            System.exit(0);
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_formWindowClosing

    private void jMenuItem20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem20ActionPerformed

        reportecomputos compu = new reportecomputos(this, false, conexion, presup);
        int xi = (this.getWidth()/2)-450/2;
        int yi = (this.getHeight()/2)-200/2;
        compu.setBounds(xi, yi, 450, 200);
        compu.setVisible(true);
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem20ActionPerformed

    private void jButton31ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton31ActionPerformed
        int op = JOptionPane.showConfirmDialog(this, "Desea borrar el listado de precios con todas sus partidas y análisis?",
                "Borrar Listado de Precios",JOptionPane.YES_NO_OPTION);
        int fil,col;
        fil=jTable1.getSelectedRow();
        String idtabu=jTable1.getValueAt(fil, 0).toString();
        if(op==JOptionPane.YES_OPTION)
        {
            String borracap="DELETE FROM ctabs WHERE mtabus_id='"+idtabu+"' AND mtabus_id NOT IN "
                    + "(SELECT id FROM mtabus)";
            try {
                Statement st = (Statement) conexion.createStatement();
                st.execute(borracap);
                String borradetmat = "DELETE FROM deptabs WHERE mtabus_id='"+idtabu+"' "
                        + "AND mtabus_id NOT IN (SELECT id FROM mtabus)";
                st.execute(borradetmat);
                String borrademano = "DELETE FROM dmoptabs WHERE mtabus_id='"+idtabu+"' "
                        + " AND mtabus_id NOT IN (SELECT id FROM mtabus)";
                st.execute(borrademano);
                String dmtabs = "DELETE FROM dmtabs WHERE mtabus_id='"+idtabu+"' AND "
                        + "mtabus_id NOT IN (SELECT id FROM mtabus)";
                st.execute(dmtabs);
                String metabs = "DELETE FROM metabs WHERE mtabus_id='"+idtabu+"' "
                        + "AND mtabus_id NOT IN (SELECT id FROM mtabus)";
                st.execute(metabs);
                String mmotabs = "DELETE FROM mmotabs WHERE mtabus_id='"+idtabu+"' "
                        + "AND mtabus_id NOT IN (SELECT id FROM mtabus)";
                st.execute(mmotabs);
                String mmtabs = "DELETE FROM mmtabs WHERE mtabus_id='"+idtabu+"'"
                        + " AND mtabus_id NOT IN (SELECT id FROM mtabus)";
                st.execute(mmtabs);
                String mptabus = "DELETE FROM mptabs WHERE mtabus_id='"+idtabu+"'"
                        + " AND mtabus_id NOT IN (SELECT id FROM mtabus)";
                st.execute(mptabus);
                String mtabus = "DELETE FROM mtabus WHERE id='"+idtabu+"'";
                st.execute(mtabus);
                JOptionPane.showMessageDialog(presupuesto, "Se ha borrado el listado de precios referencial "+idtabu);
                buscatab();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(presupuesto, "No se ha borrado el listado de precios referencial "+idtabu);
                Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton31ActionPerformed

    private void jButton32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton32ActionPerformed
        buscatab();
        
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton32ActionPerformed

    private void jMenuItem29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem29ActionPerformed
        reportecuadrocierre cierre = new reportecuadrocierre(this, false, conexion, presup);
        int xi=(this.getWidth()/2)-500/2;
        int yi = (this.getHeight()/2)-300/2;
        cierre.setBounds(xi, yi, 500, 300);
        cierre.setVisible(true);
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem29ActionPerformed
    public void recalcula()
    {
       recalcula recal = new recalcula(this, true, conexion, cadena);
       int xi = (this.getWidth()/2)-350/2;
       int yi = (this.getHeight()/2)-450/2;
       recal.setBounds(xi, yi, 370, 470);
       recal.setVisible(true);
       partida();
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
   try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
       
       
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
              new Principal().setExtendedState(JFrame.MAXIMIZED_BOTH);
              
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton19;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton20;
    private javax.swing.JButton jButton21;
    private javax.swing.JButton jButton22;
    private javax.swing.JButton jButton23;
    private javax.swing.JButton jButton24;
    private javax.swing.JButton jButton25;
    private javax.swing.JButton jButton26;
    private javax.swing.JButton jButton27;
    private javax.swing.JButton jButton28;
    private javax.swing.JButton jButton29;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton30;
    private javax.swing.JButton jButton31;
    private javax.swing.JButton jButton32;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JDesktopPane jDesktopPane1;
    private javax.swing.JInternalFrame jInternalFrame1;
    private javax.swing.JLabel jLabel1;
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
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu10;
    private javax.swing.JMenu jMenu11;
    private javax.swing.JMenu jMenu12;
    private javax.swing.JMenu jMenu13;
    private javax.swing.JMenu jMenu14;
    private javax.swing.JMenu jMenu15;
    private javax.swing.JMenu jMenu16;
    private javax.swing.JMenu jMenu17;
    private javax.swing.JMenu jMenu18;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu7;
    private javax.swing.JMenu jMenu8;
    private javax.swing.JMenu jMenu9;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuBar jMenuBar2;
    private javax.swing.JMenuBar jMenuBar3;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem14;
    private javax.swing.JMenuItem jMenuItem15;
    private javax.swing.JMenuItem jMenuItem16;
    private javax.swing.JMenuItem jMenuItem17;
    private javax.swing.JMenuItem jMenuItem18;
    private javax.swing.JMenuItem jMenuItem19;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem20;
    private javax.swing.JMenuItem jMenuItem21;
    private javax.swing.JMenuItem jMenuItem22;
    private javax.swing.JMenuItem jMenuItem23;
    private javax.swing.JMenuItem jMenuItem24;
    private javax.swing.JMenuItem jMenuItem25;
    private javax.swing.JMenuItem jMenuItem26;
    private javax.swing.JMenuItem jMenuItem27;
    private javax.swing.JMenuItem jMenuItem28;
    private javax.swing.JMenuItem jMenuItem29;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem30;
    private javax.swing.JMenuItem jMenuItem31;
    private javax.swing.JMenuItem jMenuItem33;
    private javax.swing.JMenuItem jMenuItem34;
    private javax.swing.JMenuItem jMenuItem35;
    private javax.swing.JMenuItem jMenuItem36;
    private javax.swing.JMenuItem jMenuItem37;
    private javax.swing.JMenuItem jMenuItem38;
    private javax.swing.JMenuItem jMenuItem39;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem42;
    private javax.swing.JMenuItem jMenuItem44;
    private javax.swing.JMenuItem jMenuItem45;
    private javax.swing.JMenuItem jMenuItem46;
    private javax.swing.JMenuItem jMenuItem47;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JPopupMenu.Separator jSeparator5;
    private javax.swing.JPopupMenu.Separator jSeparator6;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable jTable4;
    private javax.swing.JTable jTable5;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables

    private void cambiarcabecera() {
        JTableHeader th = jTable1.getTableHeader();
 
       TableColumnModel tcm = th.getColumnModel();
       TableColumn tc = tcm.getColumn(0); 
       tc.setPreferredWidth(20);
       tc.setHeaderValue("Código");
       tc = tcm.getColumn(1); 
       tc.setPreferredWidth(50);
        tc.setHeaderValue("Descripción");
        tc = tcm.getColumn(2);
        tc.setPreferredWidth(15);
        tc.setHeaderValue("Vigencia");
        tc = tcm.getColumn(3);
        tc.setPreferredWidth(10);
        tc.setHeaderValue("Prestaciones %");
        tc = tcm.getColumn(4); 
        tc.setPreferredWidth(10);
        tc.setHeaderValue("Admin. y Gastos %");
        tc = tcm.getColumn(5); 
        tc.setPreferredWidth(10);
        tc.setHeaderValue("Utilidad %");
        tc = tcm.getColumn(6); 
        tc.setPreferredWidth(10);
        tc.setHeaderValue("Costos Financieros %");
        tc = tcm.getColumn(7); 
        tc.setPreferredWidth(10);
        tc.setHeaderValue("Impuestos");
        
       th.repaint(); 
    }

    private void cambiacabecera2() {
        JTableHeader th = jTable2.getTableHeader();
       TableColumnModel tcm = th.getColumnModel();
       TableColumn tc = tcm.getColumn(0); 
       tc.setHeaderValue("Cod. Covenin");
       tc.setPreferredWidth(50);
       tc = tcm.getColumn(1); 
        tc.setHeaderValue("Descripción");
        tc.setPreferredWidth(300);
        tc = tcm.getColumn(2); 
        tc.setHeaderValue("Númeroq");
        tc.setPreferredWidth(2);
         tc = tcm.getColumn(3); 
        tc.setHeaderValue("Número");
        tc.setPreferredWidth(2);
        tc = tcm.getColumn(4); 
        tc.setHeaderValue("Unidad");
        tc.setPreferredWidth(20);
        tc = tcm.getColumn(5); 
        tc.setHeaderValue("Precio Unitario");
        tc.setPreferredWidth(20);
     
        th.repaint(); 
    }
    private void cambiacabeceramat() {
         JTableHeader th = jTable4.getTableHeader();
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
    private void cambiacabeceraequip() {
         JTableHeader th = jTable3.getTableHeader();
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
        tc.setHeaderValue("Depreciación");
        
        tc.setPreferredWidth(20);
        tc = tcm.getColumn(4); 
        tc.setHeaderValue("Cantidad");
        tc.setPreferredWidth(30);
        tc = tcm.getColumn(5);
         tc.setHeaderValue("Total");
        tc.setPreferredWidth(20);  
                   
       th.repaint(); 
    }
    private void cambiacabeceramano() {
         JTableHeader th = jTable5.getTableHeader();
       TableColumnModel tcm = th.getColumnModel();
       TableColumn tc = tcm.getColumn(0); 
       tc.setHeaderValue("Código");
       tc.setPreferredWidth(20);
       tc = tcm.getColumn(1); 
        tc.setHeaderValue("Descripción");
        tc.setPreferredWidth(100);
        tc = tcm.getColumn(2); 
        tc.setHeaderValue("Cantidad");
        tc.setPreferredWidth(30);
        tc = tcm.getColumn(3); 
        tc.setHeaderValue("Bono");
        tc.setPreferredWidth(30);
        tc = tcm.getColumn(4); 
        tc.setHeaderValue("Subsidio");
        tc.setPreferredWidth(30);
         tc = tcm.getColumn(5); 
        tc.setHeaderValue("Salario");
        tc.setPreferredWidth(30);  
         tc = tcm.getColumn(6); 
         tc.setHeaderValue("Total");
        tc.setPreferredWidth(30);  
       th.repaint(); 
    }
    
    public void partida(){
       mptabs = new DefaultTableModel(){@Override
     public boolean isCellEditable (int row, int column) {
        
         return false;
     }
        
        
            @Override
        public Class getColumnClass(int columna)
        {
            if (columna == 2 || columna==3) return Integer.class;
            
            return Object.class;
        }
       
 };
      busqueda = jTextField2.getText().toString();  
            jTable2.setModel(mptabs);
        try {
            Statement s = (Statement) conexion.createStatement();
            ResultSet rs = s.executeQuery("SELECT codicove, descri, numero, numegrup, unidad,IF(precunit=0,precasu, precunit) as precunit, "
                    + "mbdat_id FROM Mptabs m WHERE m.mtabus_id = '"+cadena+"' AND status=1 ORDER BY numegrup");
            
            ResultSetMetaData rsMd = (ResultSetMetaData) rs.getMetaData();
            int cantidadColumnas = rsMd.getColumnCount();
             for (int i = 1; i <= cantidadColumnas; i++) {
                 
                 mptabs.addColumn(rsMd.getColumnLabel(i));
            }
             
             while (rs.next()) {
                 
                 Object[] filas = new Object[cantidadColumnas];
                 
                for (int i = 0; i < cantidadColumnas; i++) {
                   
                    filas[i]=rs.getObject(i+1);
                }
                mptabs.addRow(filas);
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
        
      

        

             jTable2.getColumnModel().getColumn(2).setMaxWidth(0);

             jTable2.getColumnModel().getColumn(2).setMinWidth(0);

             jTable2.getTableHeader().getColumnModel().getColumn(2).setMaxWidth(0);

             jTable2.getTableHeader().getColumnModel().getColumn(2).setMinWidth(0);
             
              jTable2.getColumnModel().getColumn(6).setMaxWidth(0);

             jTable2.getColumnModel().getColumn(6).setMinWidth(0);

             jTable2.getTableHeader().getColumnModel().getColumn(6).setMaxWidth(0);

             jTable2.getTableHeader().getColumnModel().getColumn(6).setMinWidth(0);

       cambiacabecera2();
             if(row!=-1){
                jTable2.setRowSelectionInterval(row,row);
            try {
                buscapartida();
            } catch (SQLException ex) {
                Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
            }
             }
               
    }
    public void buscapartidas(int fila) {
         
        
        
        if(jTable1.getRowCount()>0){
            if(jTable1.getValueAt(fila,7)!=null){
            impu = jTable1.getValueAt(fila,7).toString();
            }
            if(jTable1.getValueAt(fila,6)!=null){
                finanzas = jTable1.getValueAt(fila,6).toString();
            }
            if(jTable1.getValueAt(fila,0)!=null){
                cadena=jTable1.getValueAt(fila,0).toString();
            }
            if(jTable1.getValueAt(fila,1)!=null){
               descrip=jTable1.getValueAt(fila, 1).toString();
            }
               
               
               jLabel3.setText(principio);
               jLabel3.setText(jLabel3.getText()+" "+cadena);  
               
               partida();
        }
       
        
    }
 
    
    public final void buscatab() {
         int cantidadColumnas=0;
         int contreg=0;
        DefaultTableModel mtabus = new DefaultTableModel() {@Override
        public boolean isCellEditable (int fila, int columna) {
           if(columna==1 || columna>2){
               return true;
           }
            return false;
            }
        };
     
               
               jTable1.setModel(mtabus);
       
        try {
            Statement s = (Statement) conexion.createStatement();
            ResultSet rs = s.executeQuery("SELECT id,descri, DATE_FORMAT(vigencia, '%d/%m/%Y'), "
                    + "pprest,padyga, putild, pcosfin, pimpue FROM Mtabus"
                    + " WHERE status=1 ORDER BY vigencia DESC");
            
            ResultSetMetaData rsMd = (ResultSetMetaData) rs.getMetaData();
            cantidadColumnas = rsMd.getColumnCount();
             for (int i = 1; i <= cantidadColumnas; i++) {
                 mtabus.addColumn(rsMd.getColumnLabel(i));
            }
             
             while (rs.next()) {
                 Object[] filas = new Object[cantidadColumnas];
                for (int i = 0; i < cantidadColumnas; i++) {
                    filas[i]=rs.getObject(i+1);
                }
                mtabus.addRow(filas);
                contreg++;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(contreg>0){
        jTable1.setRowSelectionInterval(0, 0);
        }
               cambiarcabecera();
    }
    
    
}
