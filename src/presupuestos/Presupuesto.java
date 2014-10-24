
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * Presupuesto.java
 *
 * Created on 23/10/2012, 12:32:27 AM
 */
package presupuestos;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSetMetaData;
import com.mysql.jdbc.Statement;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import presupuesto.materiales.matrizmaterialespres;
import presupuestos.capitulos.capitulos;
import presupuestos.equipo.matrizequipospres;
import presupuestos.manoobra.matrizmanopres;
import reportes.reportepresupuesto;
import valuaciones.aumentosdismi;
import valuaciones.parametrorecon;
import valuaciones.reconsideraciones;
import valuaciones.valuacion;
import winspapus.Principal;

/**
 *
 * @author Betmart
 */
public class Presupuesto extends javax.swing.JInternalFrame {

    /** Creates new form Presupuesto */
    int focuslost=0;
    String numero="";
    int inicio=0;
    String [] partidas;
    String auxid="";
    int insertar=0;
    public double subtotal1 = 0,  subtotal=0,impuesto=0, total=0;
    int contsel=0;
    String  codicove="";
    private Connection conex;
    DefaultTableModel metabs;
    int x, y, auxcont=0;
      int nuevo = 0, cuentan=0, entra=0,nuevonumegrup;
    float contequipo=0, contmat=0, contmano=0, contototal=0;
    String [] auxpart;
    String obradicional = "";
    int filapartida=0;
   DefaultTableModel mptabs;
   float porcgad=0,porcpre=0,porcutil=0;
    int adicional=0;
    public float presta=0, admin=0, finan=0, impart=0, util=0, impgen=0;
     String id, nombre, partidanueva, tabu;
    Principal prin;
    int entrarafocus15=1;
    String rendimi, mbdat, nuevonum, numpartida, precasu, precunit, redondeo;
    int pos;
    int partida;
    String idpartida;
    public Presupuesto(Connection conex, Principal p) {
        this.conex = conex;
        prin = p;
        initComponents();
        
        try {
            
            buscartab();
        } catch (SQLException ex) {
            Logger.getLogger(Presupuesto.class.getName()).log(Level.SEVERE, null, ex);
        }
        jTable1.setOpaque(true);
    jTable1.setShowHorizontalLines(true);
    jButton24.setVisible(false);
    jTable1.setShowVerticalLines(false);
    jTable1.getTableHeader().setSize(new Dimension(25,40));
    jTable1.getTableHeader().setPreferredSize(new Dimension(30,40));
    jTable1.setRowHeight(25);
    jTable2.setOpaque(true);
    jTable2.setShowHorizontalLines(true);
    jTable2.setShowVerticalLines(false);
    jTable2.getTableHeader().setSize(new Dimension(25,40));
    jTable2.getTableHeader().setPreferredSize(new Dimension(30,40));
    jTable2.setRowHeight(25);
    }

    
    public void settabu(String tab){
        this.tabu = tab;
        jLabel2.setText("Agregar Partidas del Tabulador "+tabu);
        String select = "SELECT padyga, pcosfin, pimpue, pprest, putild FROM mtabus WHERE id='"+tab+"'";
        try {
            Statement st = (Statement) conex.createStatement();
            ResultSet rst = st.executeQuery(select);
            inicio=1;
            jComboBox1.setSelectedItem(tabu);
            inicio=0;
            while(rst.next()){
                jLabel29.setText(rst.getObject(5)+"%");
                jLabel27.setText(rst.getObject(4)+"%");
                jLabel28.setText(rst.getObject(1)+"%");
                jLabel30.setText(rst.getObject(2)+"%");
                jLabel31.setText(rst.getObject(3)+"%");
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(Presupuesto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void setid (String presu){
        this.id = presu;
        idpartida = id;
        String select = "UPDATE seleccionado=1 FROM mppres WHERE id='"+id+"'" ;
        prin.setpres(presu);
        
    }
    public double settotal(){
        return total;
    }
    public final void buscartab() throws SQLException{
         DefaultTableModel cont = new DefaultTableModel();
        jComboBox1.removeAllItems();
        
     
            Statement s = (Statement) conex.createStatement();
            ResultSet rs = s.executeQuery("SELECT id FROM mtabus WHERE status=1");
            
         
           
             ResultSetMetaData rsMd = (ResultSetMetaData) rs.getMetaData();
            int cantidadColumnas = rsMd.getColumnCount();
            
             for (int i = 1; i <= cantidadColumnas; i++) {
                
                 cont.addColumn(rsMd.getColumnLabel(i));
            }
         
                 
             
             while (rs.next()) {
                 
                 Object[] filas = new Object[cantidadColumnas];
                 
                for (int i = 0; i < cantidadColumnas; i++) {
                    filas[i]=rs.getObject(i+1);
                }
               cont.addRow(filas);
                
            }
                 
                 
                for (int i = 0; i < cont.getRowCount(); i++) {
                    jComboBox1.addItem(cont.getValueAt(i, 0).toString());
                }
                String consultaactivo = "SELECT id FROM mtabus WHERE seleccionado=1";
                Statement selectactivo = (Statement) conex.createStatement();
                ResultSet rstselect = selectactivo.executeQuery(consultaactivo);
                String select = "";
                while(rstselect.next()){
                    select = rstselect.getString(1);
                }
                jComboBox1.setSelectedItem(select);
    }
    public final void cargapresupuesto() throws SQLException{
        Statement st = (Statement) conex.createStatement();
        ResultSet rs = st.executeQuery("SELECT codicove, descri, numero, numegrup, unidad, IF(precunit=0, precasu, precunit) as precunit, "
                + "mbdat_id FROM Mptabs m WHERE m.mtabus_id = '"+tabu+"' AND status=1 ORDER BY numegrup");
        ResultSetMetaData rsMd = (ResultSetMetaData) rs.getMetaData();
        //System.out.println("siii entra en presupuesto");
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
             
             while (rs.next()) {
                 
                 Object[] filas = new Object[cantidadColumnas];
                  Object obj = Boolean.valueOf(false);
                  filas[0]= obj;
                for (int i = 1; i < cantidadColumnas; i++) {
                  
                       filas[i]=rs.getObject(i);
                }
                metabs.addRow(filas);
                
            }
             jTable1.getColumnModel().getColumn(3).setMaxWidth(0);

             jTable1.getColumnModel().getColumn(3).setMinWidth(0);

             jTable1.getTableHeader().getColumnModel().getColumn(3).setMaxWidth(0);

             jTable1.getTableHeader().getColumnModel().getColumn(3).setMinWidth(0);
             
              jTable1.getColumnModel().getColumn(7).setMaxWidth(0);

             jTable1.getColumnModel().getColumn(7).setMinWidth(0);

             jTable1.getTableHeader().getColumnModel().getColumn(7).setMaxWidth(0);

             jTable1.getTableHeader().getColumnModel().getColumn(7).setMinWidth(0);
             
             int fila = jTable1.getRowCount();
             auxpart = new String[fila];
             
                 
             cambiarcabecera();
    }
    public void cambiarcabecera(){
       JTableHeader th = jTable1.getTableHeader();
       TableColumnModel tcm = th.getColumnModel();
       TableColumn tc = tcm.getColumn(0); 
       tc.setPreferredWidth(5);
       tc = tcm.getColumn(1); 
       tc.setHeaderValue("Código");
       tc.setPreferredWidth(50);
       tc = tcm.getColumn(2); 
        tc.setHeaderValue("Descripción");
        tc.setPreferredWidth(250);
        tc = tcm.getColumn(4); 
        tc.setHeaderValue("Número");
        tc.setPreferredWidth(20);
        tc = tcm.getColumn(5); 
        tc.setHeaderValue("Unidad");
        tc.setPreferredWidth(20);
        tc = tcm.getColumn(6); 
        tc.setHeaderValue("Precio Unitario");
        tc.setPreferredWidth(20);
        
       th.repaint(); 
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar1 = new javax.swing.JToolBar();
        jButton8 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jButton16 = new javax.swing.JButton();
        jButton17 = new javax.swing.JButton();
        jButton18 = new javax.swing.JButton();
        jButton19 = new javax.swing.JButton();
        jButton21 = new javax.swing.JButton();
        jButton33 = new javax.swing.JButton();
        jButton34 = new javax.swing.JButton();
        jButton35 = new javax.swing.JButton();
        jButton23 = new javax.swing.JButton();
        jButton25 = new javax.swing.JButton();
        jButton31 = new javax.swing.JButton();
        jButton37 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jPanel14 = new javax.swing.JPanel();
        jTextField14 = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        jTextField17 = new javax.swing.JTextField();
        jTextField19 = new javax.swing.JTextField();
        jTextField18 = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jTextField20 = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jTextField15 = new javax.swing.JTextField();
        jTextField16 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox();
        jPanel13 = new javax.swing.JPanel();
        jButton11 = new javax.swing.JButton();
        jButton22 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton26 = new javax.swing.JButton();
        jButton27 = new javax.swing.JButton();
        jButton28 = new javax.swing.JButton();
        jButton29 = new javax.swing.JButton();
        jButton30 = new javax.swing.JButton();
        jButton32 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton20 = new javax.swing.JButton();
        jButton24 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jButton36 = new javax.swing.JButton();
        jLabel25 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jPanel4 = new javax.swing.JPanel();
        jLabel26 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jTextField11 = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jTextField12 = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jTextField13 = new javax.swing.JTextField();
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

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Gestionar Presupuesto de Obra");
        setNextFocusableComponent(jTable2);
        setPreferredSize(new java.awt.Dimension(1100, 645));
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameClosed(evt);
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
            }
        });

        jToolBar1.setBorder(null);
        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/equipobarra.png"))); // NOI18N
        jButton8.setToolTipText("Gestionar Tabuladores");
        jButton8.setFocusable(false);
        jButton8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton8.setPreferredSize(new java.awt.Dimension(37, 37));
        jButton8.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton8);

        jButton15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/moneda.fw.png"))); // NOI18N
        jButton15.setToolTipText("Valuaciones");
        jButton15.setFocusable(false);
        jButton15.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton15.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton15);

        jButton16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/aumentos.fw.png"))); // NOI18N
        jButton16.setToolTipText("Aumentos y Disminuciones");
        jButton16.setFocusable(false);
        jButton16.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton16.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton16);

        jButton17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/listar2.png"))); // NOI18N
        jButton17.setToolTipText("Reconsideración de Precios");
        jButton17.setFocusable(false);
        jButton17.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton17.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton17ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton17);

        jButton18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/reloj.fw.png"))); // NOI18N
        jButton18.setToolTipText("Cronograma de Actividades");
        jButton18.setFocusable(false);
        jButton18.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton18.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton18ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton18);

        jButton19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/cambiar.fw.png"))); // NOI18N
        jButton19.setToolTipText("Cambiar Presupuesto");
        jButton19.setFocusable(false);
        jButton19.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton19.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton19ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton19);

        jButton21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/capitulobarra.png"))); // NOI18N
        jButton21.setToolTipText("Capitulos del Presupuesto");
        jButton21.setFocusable(false);
        jButton21.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton21.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton21ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton21);

        jButton33.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/materialbarra.png"))); // NOI18N
        jButton33.setToolTipText("Materiales");
        jButton33.setFocusable(false);
        jButton33.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton33.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton33.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton33ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton33);

        jButton34.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/camion.fw.png"))); // NOI18N
        jButton34.setToolTipText("Equipos del Presupuesto");
        jButton34.setFocusable(false);
        jButton34.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton34.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton34.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton34ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton34);

        jButton35.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/barracasco.png"))); // NOI18N
        jButton35.setToolTipText("Mano de Obra del presupuesto");
        jButton35.setFocusable(false);
        jButton35.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton35.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton35.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton35ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton35);

        jButton23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/borrargrande.fw.png"))); // NOI18N
        jButton23.setToolTipText("Borrar Todas Las Partidas");
        jButton23.setFocusable(false);
        jButton23.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton23.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton23ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton23);

        jButton25.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/recalcula1.fw.png"))); // NOI18N
        jButton25.setToolTipText("Recalcula");
        jButton25.setFocusable(false);
        jButton25.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton25.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton25ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton25);

        jButton31.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/eliminar.png"))); // NOI18N
        jButton31.setToolTipText("Cerrar Presupuesto");
        jButton31.setFocusable(false);
        jButton31.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton31.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton31.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton31ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton31);

        jButton37.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/imprimir.png"))); // NOI18N
        jButton37.setToolTipText("Reporte de Presupuesto");
        jButton37.setFocusable(false);
        jButton37.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton37.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton37.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton37ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton37);

        jScrollPane3.setBorder(null);
        jScrollPane3.setAutoscrolls(true);
        jScrollPane3.setPreferredSize(new java.awt.Dimension(950, 600));

        jPanel1.setPreferredSize(new java.awt.Dimension(1000, 450));

        jPanel2.setPreferredSize(new java.awt.Dimension(950, 550));

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));

        jPanel8.setBackground(new java.awt.Color(100, 100, 100));
        jPanel8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(100, 100, 100)));

        jLabel3.setBackground(new java.awt.Color(100, 100, 100));
        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Obra Activa");
        jLabel3.setOpaque(true);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 595, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jTable2.setFont(new java.awt.Font("Tahoma", 0, 10));
        jTable2.setToolTipText("Haga Doble Click sobre Cualquier Partida para ver Detalle");
        jTable2.setDoubleBuffered(true);
        jTable2.setSelectionBackground(new java.awt.Color(216, 141, 0));
        jTable2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable2MouseClicked(evt);
            }
        });
        jTable2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTable2KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTable2KeyReleased(evt);
            }
        });
        jScrollPane4.setViewportView(jTable2);

        jPanel14.setToolTipText("Para insertar partida presione la tecla Tab hasta finalizar en el total");

        jTextField14.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField14.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField14MouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTextField14MousePressed(evt);
            }
        });
        jTextField14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField14ActionPerformed(evt);
            }
        });
        jTextField14.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField14FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField14FocusLost(evt);
            }
        });
        jTextField14.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField14KeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField14KeyTyped(evt);
            }
        });

        jLabel24.setText("Total:");

        jTextField17.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jTextField19.setEditable(false);
        jTextField19.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField19.setText("0.00");
        jTextField19.setEnabled(false);
        jTextField19.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField19FocusLost(evt);
            }
        });

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

        jLabel19.setText("Código de la Partida:");

        jLabel21.setText("Unidad:");

        jLabel20.setText("Nro. de Presupuesto:");

        jLabel18.setText("Nro. Partida Tabulador:");

        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jTextField20.setEditable(false);
        jTextField20.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField20.setText("0.00");
        jTextField20.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField20FocusLost(evt);
            }
        });

        jLabel23.setText("Precio:");

        jLabel22.setText("Cantidad:");

        jTextArea2.setColumns(20);
        jTextArea2.setFont(new java.awt.Font("Monospaced", 0, 11));
        jTextArea2.setLineWrap(true);
        jTextArea2.setRows(3);
        jTextArea2.setWrapStyleWord(true);
        jTextArea2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextArea2KeyPressed(evt);
            }
        });
        jScrollPane5.setViewportView(jTextArea2);

        jTextField15.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField15.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField15MouseClicked(evt);
            }
        });
        jTextField15.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField15FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField15FocusLost(evt);
            }
        });
        jTextField15.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField15KeyPressed(evt);
            }
        });

        jTextField16.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField16.setEnabled(false);
        jTextField16.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField16KeyTyped(evt);
            }
        });

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "No Prevista", "Obra Extra", "Obra Adicional", "Obra Complementaria" }));

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addGap(255, 255, 255)
                        .addComponent(jLabel6))
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel14Layout.createSequentialGroup()
                                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel14Layout.createSequentialGroup()
                                        .addComponent(jCheckBox1)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel20)
                                    .addGroup(jPanel14Layout.createSequentialGroup()
                                        .addComponent(jTextField14, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 126, Short.MAX_VALUE)
                                        .addComponent(jLabel19)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextField15, javax.swing.GroupLayout.DEFAULT_SIZE, 99, Short.MAX_VALUE)
                                    .addComponent(jTextField16, javax.swing.GroupLayout.DEFAULT_SIZE, 99, Short.MAX_VALUE)))
                            .addGroup(jPanel14Layout.createSequentialGroup()
                                .addComponent(jLabel21)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextField17, javax.swing.GroupLayout.DEFAULT_SIZE, 93, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel22)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField18, javax.swing.GroupLayout.DEFAULT_SIZE, 94, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel23)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField19, javax.swing.GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel24)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField20, javax.swing.GroupLayout.DEFAULT_SIZE, 93, Short.MAX_VALUE)))))
                .addGap(10, 10, 10))
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 567, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel18)
                    .addComponent(jTextField15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19)
                    .addComponent(jTextField14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel20)
                    .addComponent(jTextField16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBox1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jTextField19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel24)
                    .addComponent(jTextField20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23)
                    .addComponent(jTextField18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22)
                    .addComponent(jTextField17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jButton11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/ordena.fw.png"))); // NOI18N
        jButton11.setToolTipText("Reordenar Partidas por tipo");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jButton22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/importar.fw.png"))); // NOI18N
        jButton22.setToolTipText("Importar Partidas al Presupuesto");
        jButton22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton22ActionPerformed(evt);
            }
        });

        jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/borra1.fw.png"))); // NOI18N
        jButton10.setToolTipText("Borrar Partida Seleccionada");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jButton14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/buscar1.fw.png"))); // NOI18N
        jButton14.setToolTipText("Ver Detalle de la Partida");
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/añade1.fw.png"))); // NOI18N
        jButton9.setToolTipText("Agregar Nueva Partida");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButton26.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/guardar1.fw.png"))); // NOI18N
        jButton26.setToolTipText("Guardar Valores de Partida");
        jButton26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton26ActionPerformed(evt);
            }
        });

        jButton27.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/subir.fw.png"))); // NOI18N
        jButton27.setToolTipText("Subir Partida");
        jButton27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton27ActionPerformed(evt);
            }
        });

        jButton28.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/ordenar1.fw.png"))); // NOI18N
        jButton28.setToolTipText("Reordenar Partidas por Número");
        jButton28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton28ActionPerformed(evt);
            }
        });

        jButton29.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/bajar.fw.png"))); // NOI18N
        jButton29.setToolTipText("Bajar Partida");
        jButton29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton29ActionPerformed(evt);
            }
        });

        jButton30.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/cambiarpos.fw.png"))); // NOI18N
        jButton30.setToolTipText("Cambiar Partida de Posición");
        jButton30.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton30ActionPerformed(evt);
            }
        });

        jButton32.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/imprimirpeque.fw.png"))); // NOI18N
        jButton32.setToolTipText("Imprimir APU.");
        jButton32.setEnabled(false);
        jButton32.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton32ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton32, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton26, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton22, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton28, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton30, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton29, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton27, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jButton26, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton22, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton28, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton30, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton29, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton27, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton32, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 577, Short.MAX_VALUE))
                .addContainerGap())
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 281, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));

        jPanel7.setBackground(new java.awt.Color(100, 100, 100));
        jPanel7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(100, 100, 100)));

        jLabel2.setBackground(new java.awt.Color(100, 100, 100));
        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Seleccionar Partidas para Presupuesto del Tabulador");
        jLabel2.setOpaque(true);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 480, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE)
        );

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/añade1.fw.png"))); // NOI18N
        jButton6.setToolTipText("Nuevo Presupuesto");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/guardar1.fw.png"))); // NOI18N
        jButton7.setToolTipText("Guardar Valores del Presupuesto");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/edita1.fw.png"))); // NOI18N
        jButton2.setToolTipText("Editar Presupuesto");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/copiapu1.fw.png"))); // NOI18N
        jButton3.setToolTipText("Copiar Presupuesto");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/borra1.fw.png"))); // NOI18N
        jButton20.setToolTipText("Borrar Presupuesto");
        jButton20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton20ActionPerformed(evt);
            }
        });

        jButton24.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/copia1.fw.png"))); // NOI18N
        jButton24.setToolTipText("Copiar APU en otro Presupuesto");
        jButton24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton24ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addContainerGap(242, Short.MAX_VALUE)
                .addComponent(jButton20, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton24, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jButton24, 0, 30, Short.MAX_VALUE)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton20, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jTable1.setFont(new java.awt.Font("Tahoma", 0, 10));
        jTable1.setDoubleBuffered(true);
        jTable1.setSelectionBackground(new java.awt.Color(216, 141, 0));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jTextField1.setPreferredSize(new java.awt.Dimension(20, 20));

        jLabel4.setText("Buscar:");

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/buscar1.fw.png"))); // NOI18N
        jButton1.setToolTipText("Buscar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/asignar.fw.png"))); // NOI18N
        jButton4.setToolTipText("Asignar Partidas Seleccionadas");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/selecc1.fw.png"))); // NOI18N
        jButton5.setToolTipText("Seleccionar Todo");
        jButton5.setPreferredSize(new java.awt.Dimension(53, 30));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/quita1.fw.png"))); // NOI18N
        jButton12.setToolTipText("Deseleccionar Todo");
        jButton12.setPreferredSize(new java.awt.Dimension(51, 30));
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jButton36.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/limpiar.fw.png"))); // NOI18N
        jButton36.setToolTipText("Limpiar Búsqueda");
        jButton36.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton36ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton36, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(87, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                        .addComponent(jButton36, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jLabel4)))
                .addContainerGap())
        );

        jLabel25.setText("Seleccione Tabulador:");

        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });

        jPanel4.setPreferredSize(new java.awt.Dimension(382, 25));

        jLabel26.setText("Imp.:");

        jLabel8.setText("Adm.:");

        jLabel7.setText("Fin.:");

        jLabel5.setText("Prest.:");

        jLabel1.setText("Util.:");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jLabel29, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel27, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, 53, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel30, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel26)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel31, javax.swing.GroupLayout.DEFAULT_SIZE, 54, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                .addComponent(jLabel26, javax.swing.GroupLayout.DEFAULT_SIZE, 22, Short.MAX_VALUE)
                .addComponent(jLabel31, javax.swing.GroupLayout.DEFAULT_SIZE, 22, Short.MAX_VALUE)
                .addComponent(jLabel30, javax.swing.GroupLayout.DEFAULT_SIZE, 22, Short.MAX_VALUE)
                .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 22, Short.MAX_VALUE)
                .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, 22, Short.MAX_VALUE)
                .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 22, Short.MAX_VALUE)
                .addComponent(jLabel27, javax.swing.GroupLayout.DEFAULT_SIZE, 22, Short.MAX_VALUE)
                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 22, Short.MAX_VALUE)
                .addComponent(jLabel29, javax.swing.GroupLayout.DEFAULT_SIZE, 22, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 22, Short.MAX_VALUE))
        );

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Valores del Presupuesto", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        jPanel11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));

        jLabel15.setFont(new java.awt.Font("Tahoma", 0, 10));
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel15.setText("Sub-total:");

        jTextField11.setEditable(false);
        jTextField11.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField11.setText("0.00");

        jLabel16.setFont(new java.awt.Font("Tahoma", 0, 10));
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel16.setText("Impuesto.:");

        jTextField12.setEditable(false);
        jTextField12.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField12.setText("0.00");

        jLabel17.setFont(new java.awt.Font("Tahoma", 0, 10));
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel17.setText("Total:");

        jTextField13.setEditable(false);
        jTextField13.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField13.setText("0.00");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel11Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, 69, Short.MAX_VALUE))
                    .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextField13, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)
                    .addComponent(jTextField12, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)
                    .addComponent(jTextField11, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel15)
                    .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel17)
                    .addComponent(jTextField13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(8, Short.MAX_VALUE))
        );

        jPanel10.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(102, 102, 102), 1, true));

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
                        .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, 63, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField7, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, 63, Short.MAX_VALUE))
                            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField5, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                            .addComponent(jTextField6, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE))))
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, 68, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField10, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE)
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, 59, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField8, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                            .addComponent(jTextField9, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE))))
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

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel11, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel25)
                                .addGap(22, 22, 22)
                                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 461, Short.MAX_VALUE))
                                .addGap(6, 6, 6)))
                        .addGap(5, 5, 5))
                    .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 462, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel25)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 205, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(1, 1, 1)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 1084, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 557, Short.MAX_VALUE)
                .addContainerGap())
        );

        jScrollPane3.setViewportView(jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 1084, Short.MAX_VALUE)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 1084, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 568, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
     public String buscacont(String cont) throws SQLException{
        Statement st = (Statement) conex.createStatement();
        String sql="SELECT nombre from mconts where id='"+cont+"'", nombres="";
        ResultSet rst = st.executeQuery(sql);
        while(rst.next()){
            nombres = rst.getObject(1).toString();
        }
        
        return nombres;
    }
    public String buscapro(String prop) throws SQLException{
        Statement st = (Statement) conex.createStatement();
        String sql="SELECT nombre from mprops where id='"+prop+"'", nombres="";
        ResultSet rst = st.executeQuery(sql);
        while(rst.next()){
            nombres = rst.getObject(1).toString();
        }
        
        return nombres;
    }
    public void buscaobra() throws SQLException{
        Statement stmt = (Statement) conex.createStatement();
        String sql="SELECT codpro, codcon, porpre, poruti, porgam, porcfi, porimp, poripa FROM mpres WHERE id='"+id+"'";
        ResultSet rs = stmt.executeQuery(sql);
        while(rs.next()){
            
            jTextField5.setText(rs.getObject(3).toString());
            jTextField7.setText(rs.getObject(4).toString());
            jTextField6.setText(rs.getObject(5).toString());
            jTextField8.setText(rs.getObject(6).toString());
            jTextField10.setText(rs.getObject(7).toString());
            jTextField9.setText(rs.getObject(8).toString());
            
        }
        jComboBox1.setSelectedItem(tabu);
        buscapartida();
        
    }
    
    public void settitulo(String titulo){
        this.setTitle("Gestionar Presupuesto de Obra: Seleccionada "+titulo);
    }
    
    public void cargartotal(){
        subtotal=0;
       jTextField11.setText("0.00");
       jTextField12.setText("0.00"); 
       jTextField13.setText("0.00");
        String cargasinredondeo = "SELECT SUM(ROUND(IF(mppres.`precasu`=0,mppres.`precunit`,precasu)*cantidad,2)) "
                + "FROM `winspapu`.`mppres` WHERE  tipo='Org' AND mpre_id='"+id+"'";
        System.out.println("totales cargartotal: "+cargasinredondeo);
        try {
            Statement st1 = (Statement) conex.createStatement();
          
            ResultSet rst1 = st1.executeQuery(cargasinredondeo);
            
            while (rst1.next()){
                if(rst1.getObject(1)!=null) {
                    subtotal = rst1.getDouble(1);
                     System.out.println("subtotal bd: "+rst1.getDouble(1));
                }
            }
           System.out.println("subtotal: "+subtotal);
            impuesto = Float.valueOf(jTextField10.getText().toString());
            
          
            impuesto = subtotal*(impuesto/100);
           
            total = subtotal+impuesto;
            System.out.println("total pres "+total);
            
          
         
            NumberFormat formatoNumero = NumberFormat.getNumberInstance();
            formatoNumero.setMaximumFractionDigits(2);
            formatoNumero.setMinimumFractionDigits(2);
            jTextField11.setText(String.valueOf(formatoNumero.format(subtotal)));
            jTextField12.setText(String.valueOf(formatoNumero.format(impuesto)));
            jTextField13.setText(String.valueOf(formatoNumero.format(total))); 
            total = Math.rint(total*100)/100;
         } catch (SQLException ex) {
            Logger.getLogger(Presupuesto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     public void buscapartida1() throws SQLException{
        int cont=0;
        Statement st = (Statement) conex.createStatement();
        ResultSet rs = st.executeQuery("SELECT numero, numegrup, id, descri, IF(tipo='NP',tiponp,tipo) as tipo, idband "
                + "FROM mppres WHERE mpre_id='"+id+"' OR mpre_id IN (SELECT id from mpres where mpres_id ='"+id+"' GROUP BY id) ORDER BY numegrup");
        ResultSetMetaData rsMd = (ResultSetMetaData) rs.getMetaData();
       
        mptabs = new DefaultTableModel(){
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
            
            return Object.class;
        }
        };
        
        jTable2.setModel(mptabs);
            int cantidadColumnas = rsMd.getColumnCount();
             for (int i = 1; i <= cantidadColumnas; i++) {
                // System.out.println("Entra a hacer columnas "+cantidadColumnas);
                 mptabs.addColumn(rsMd.getColumnLabel(i));
            }
             
             while (rs.next()) {
                 
                 Object[] filas = new Object[cantidadColumnas];
                for (int i = 0; i < cantidadColumnas; i++) {
                  
                       filas[i]=rs.getObject(i+1);
                }
                mptabs.addRow(filas);
                
            }
             jTable2.getColumnModel().getColumn(0).setMaxWidth(0);

             jTable2.getColumnModel().getColumn(0).setMinWidth(0);

             jTable2.getTableHeader().getColumnModel().getColumn(0).setMaxWidth(0);

             jTable2.getTableHeader().getColumnModel().getColumn(0).setMinWidth(0);
           jTable2.getColumnModel().getColumn(5).setMaxWidth(0);

             jTable2.getColumnModel().getColumn(5).setMinWidth(0);

             jTable2.getTableHeader().getColumnModel().getColumn(5).setMaxWidth(0);

             jTable2.getTableHeader().getColumnModel().getColumn(5).setMinWidth(0);
           jTable2.getSelectionModel().setSelectionInterval(filapartida, filapartida);
             cambiarcabecera2();
             
             
             if(mptabs.getRowCount()>0) {
                 jTable2.requestFocusInWindow();
                 cargapartida();
                 
            
        }
            
             int filas = filapartida;
             jTable2.getSelectionModel().setSelectionInterval(filas, filas);
             
              Rectangle r = jTable2.getCellRect(jTable2.getSelectedRow(),jTable2.getSelectedColumn(), false);
             jTable2.scrollRectToVisible(r);
             filapartida=filas;
             if(filapartida>=0){
             cargapartida();
             cargartotal();
             }else{
                 jTextField11.setText("0.00");
                 jTextField12.setText("0.00");
                 jTextField13.setText("0.00");
             }  
             
    }
    public void buscapartida() throws SQLException{
        int cont=0;
        Statement st = (Statement) conex.createStatement();
        
        ResultSet rs = st.executeQuery("SELECT numero, numegrup, id, descri, IF(tipo='NP',tiponp,tipo) as tipo, idband "
                + "FROM "
                + "mppres WHERE "
                + "(mpre_id='"+id+"' OR (mpre_id IN "
                + "(SELECT id from mpres where mpres_id ='"+id+"' GROUP BY id))) "
                + "ORDER BY numegrup");
        ResultSetMetaData rsMd = (ResultSetMetaData) rs.getMetaData();
       
        mptabs = new DefaultTableModel(){
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
            
            return Object.class;
        }
        };
        
        jTable2.setModel(mptabs);
            int cantidadColumnas = rsMd.getColumnCount();
             for (int i = 1; i <= cantidadColumnas; i++) {
                // System.out.println("Entra a hacer columnas "+cantidadColumnas);
                 mptabs.addColumn(rsMd.getColumnLabel(i));
            }
             
             while (rs.next()) {
                 
                 Object[] filas = new Object[cantidadColumnas];
                for (int i = 0; i < cantidadColumnas; i++) {
                  
                       filas[i]=rs.getObject(i+1);
                }
                mptabs.addRow(filas);
                
            }
             jTable2.getColumnModel().getColumn(0).setMaxWidth(0);

             jTable2.getColumnModel().getColumn(0).setMinWidth(0);

             jTable2.getTableHeader().getColumnModel().getColumn(0).setMaxWidth(0);

             jTable2.getTableHeader().getColumnModel().getColumn(0).setMinWidth(0);
           jTable2.getColumnModel().getColumn(5).setMaxWidth(0);

             jTable2.getColumnModel().getColumn(5).setMinWidth(0);

             jTable2.getTableHeader().getColumnModel().getColumn(5).setMaxWidth(0);

             jTable2.getTableHeader().getColumnModel().getColumn(5).setMinWidth(0);
             
           jTable2.getSelectionModel().setSelectionInterval(filapartida, filapartida);
             cambiarcabecera2();
             
             
             if(mptabs.getRowCount()>0) {
                 jTable2.requestFocusInWindow();
                 cargapartida();
                 
            
        }
            
             int filas = jTable2.getRowCount()-1;
             jTable2.getSelectionModel().setSelectionInterval(filas, filas);
             Rectangle r = jTable2.getCellRect(jTable2.getSelectedRow(),jTable2.getSelectedColumn(), false);
             jTable2.scrollRectToVisible(r);
             filapartida=filas;
             if(filapartida>=0){
             cargapartida();
             cargartotal();
             }else{
                 jTextField11.setText("0.00");
                 jTextField12.setText("0.00");
                 jTextField13.setText("0.00");
             }
             
    }
    public void cambiarcabecera2(){
         JTableHeader th = jTable2.getTableHeader();
       TableColumnModel tcm = th.getColumnModel();
       TableColumn tc = tcm.getColumn(1); 
       
       tc.setHeaderValue("Nro.");
       tc.setPreferredWidth(90);
       tc = tcm.getColumn(2); 
       tc.setHeaderValue("Código");
       tc.setPreferredWidth(170);
       tc = tcm.getColumn(3); 
       tc.setHeaderValue("Descripción");
       tc.setPreferredWidth(1000);
       tc = tcm.getColumn(4); 
       tc.setHeaderValue("Tipo");
       tc.setPreferredWidth(100);
     
       th.repaint(); 
    }
private void formInternalFrameClosed(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosed
   
    prin.setentro(0);
}//GEN-LAST:event_formInternalFrameClosed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
       
        Nuevo s = new Nuevo(prin, true, this, conex);
        s.setBounds((this.getWidth()/2)-450, (this.getHeight()/2)-275, 900, 600);
        s.setVisible(true);
        vaciacampos();
       
    }//GEN-LAST:event_jButton6ActionPerformed

    
    public void vaciacampos(){
        jTextField14.setText("");
        jTextField15.setText("");
        jCheckBox1.setSelected(false);
        jTextField16.setText("");
        jTextField16.setEnabled(false);
        jTextArea2.setText("");
        jTextField17.setText("");
        jTextField18.setText("0.00");
        jTextField19.setText("");
        jTextField20.setText("");
    }
    public void borrar(){
            int op = JOptionPane.showConfirmDialog(null, "¿Desea Eliminar esta Partida?");
   String numeros = jTable2.getValueAt(filapartida, 0).toString();
   String selectnumero = "SELECT numero, mpre_id FROM mppres WHERE "
           + "numegrup='"+numeros+"' AND (mpre_id ='"+id+"' OR mpre_id IN "
           + "(SELECT id FROM mpres WHERE mpres_id='"+id+"'))";
   String auxpres=id;
        try {
            Statement stnumero = (Statement) conex.createStatement();
            ResultSet rstnumero = stnumero.executeQuery(selectnumero);
            while(rstnumero.next()){
                numeros= rstnumero.getString("numero");
                id=rstnumero.getString("mpre_id");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Presupuesto.class.getName()).log(Level.SEVERE, null, ex);
        }
   
        if(op == JOptionPane.YES_OPTION){
        String borramat = "DELETE FROM dmpres WHERE numepart='"+numeros+"' AND mpre_id='"+id+"'";
        try {
            Statement stm = (Statement) conex.createStatement();
            stm.execute(borramat);
        } catch (SQLException ex) {
            Logger.getLogger(Partida.class.getName()).log(Level.SEVERE, null, ex);
        }
        String borraeq = "DELETE FROM deppres WHERE numero='"+numeros+"' AND mpre_id='"+id+"'";
        try {
            Statement stm = (Statement) conex.createStatement();
            stm.execute(borraeq);
        } catch (SQLException ex) {
            Logger.getLogger(Partida.class.getName()).log(Level.SEVERE, null, ex);
        }
        String borraman = "DELETE FROM dmoppres WHERE numero='"+numeros+"' AND mpre_id='"+id+"'";
        try {
            Statement stm = (Statement) conex.createStatement();
            stm.execute(borraman);
        } catch (SQLException ex) {
            Logger.getLogger(Partida.class.getName()).log(Level.SEVERE, null, ex);
        }
        String borrapart = "DELETE FROM mppres WHERE numero='"+numeros+"' AND (mpre_id='"+id+"' OR mpre_id IN (SELECT id from mpres where mpres_id ='"+id+"' GROUP BY id))";
            try {
                Statement stm = (Statement) conex.createStatement();
                stm.execute(borrapart);
                JOptionPane.showMessageDialog(null, "Se ha eliminado la partida "+idpartida);
                id=auxpres;
                filapartida--;
                buscapartida();
                cargartotal();
              vaciacampos();
            } catch (SQLException ex) {
                Logger.getLogger(Partida.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
borrar();
       
    }//GEN-LAST:event_jButton10ActionPerformed

    
    public void cargapartida(){
        jButton11.setEnabled(true);
        int redondear=0;
        double total2=0;
       int idband = 0;
       String tiponp=null;
       String tipo="";
        adicional = 0;
        int cantfilas = jTable2.getRowCount();
        if(filapartida>cantfilas-1){
            filapartida=cantfilas-1;
        }
        numpartida = jTable2.getValueAt(filapartida, 1).toString();
        idpartida = jTable2.getValueAt(filapartida, 2).toString();
        String mtabs = null, mpre = "", descri = null, unidad = null, cantidad = null, precuni = null, padyga = null, pcosfin = null, pimpue = null, pprest = null, putild = null, numegrup = null;       
        String numeropres = null,nrocuadro=null;
        String sql = "SELECT mpre_id, descri, unidad,cantidad, redondeo,"
                + " ROUND(IFNULL(IF(precasu=0,precunit, precasu),0),2) as precunit, "
                + "ROUND(cantidad*IFNULL(IF(precasu=0,precunit, precasu),0),2) "
                + "as total,"
                + "id,tiponp, tipo, idband, nropresupuesto,nrocuadro FROM mppres "
                + "WHERE id='"+idpartida+"' AND numegrup="+numpartida+" AND "
                + "(mpre_id='"+id+"' OR "
                + "mpre_id IN (SELECT id from mpres where mpres_id ='"+id+"' GROUP BY id))";
       
      
        try {
            Statement partidas1 = (Statement) conex.createStatement();
            ResultSet resultado = partidas1.executeQuery(sql);
            Statement tabus = (Statement) conex.createStatement();  
            Statement ptabs = (Statement) conex.createStatement();           
            
            
            
            while (resultado.next()){
                tiponp = resultado.getString("tiponp");
                idband = resultado.getInt("idband");
                numeropres = resultado.getString("nropresupuesto");
                nrocuadro = resultado.getString("nrocuadro");
                mpre = resultado.getObject("mpre_id").toString();               
                descri = resultado.getObject("descri").toString();
                unidad = resultado.getObject("unidad").toString();
                cantidad = resultado.getObject("cantidad").toString();
                redondear = resultado.getInt("redondeo");
                tipo = resultado.getString("tipo");
                total2=resultado.getDouble("total");
               
                    precuni = resultado.getObject("precunit").toString();
               
                
                
                    
                codicove = resultado.getObject("id").toString();
            
            }
          
            
            auxid=id;
            if(!mpre.equals(id) && tipo.equals("NP")){
                adicional = 1;     
                id=mpre;
                
            }
           if(!mpre.equals(id) && tipo.equals("VP")){
                adicional = 1;     
                id=mpre;
                
            }
            
            String mtabus = "SELECT padyga, pcosfin, pimpue, pprest, putild FROM mtabus WHERE id='"+tabu+"'";
        String mparts = "SELECT numegrup FROM mptabs WHERE codicove='"+codicove+"' AND mtabus_id='"+tabu+"'";
        ResultSet resultabus = tabus.executeQuery(mtabus);
            ResultSet resultptabs = ptabs.executeQuery(mparts);
            while (resultabus.next()){
                if(resultabus.getObject(1)!=null)
                padyga = resultabus.getObject(1).toString()+"%";
                 if(resultabus.getObject(2)!=null)
                pcosfin = resultabus.getObject(2).toString()+"%";
                  if(resultabus.getObject(3)!=null)
                pimpue = resultabus.getObject(3).toString()+"%";
                   if(resultabus.getObject(4)!=null)
                pprest = resultabus.getObject(4).toString()+"%";
                    if(resultabus.getObject(5)!=null)
                putild = resultabus.getObject(5).toString()+"%";
            }
            
            while(resultptabs.next()){
                numegrup = resultptabs.getObject(1).toString();
                
            }    
            
            
       
            jLabel29.setText(putild);
            jLabel27.setText(pprest);
            jLabel28.setText(padyga);
            jLabel30.setText(pcosfin);
            jLabel31.setText(pimpue);
            if(adicional==1){
                
                obradicional = mpre;
                if(tipo.equals("NP")){
                jCheckBox1.setSelected(true);
                jTextField16.setEnabled(true);
                
                jTextField16.setText(numeropres);
                }else{
                     jCheckBox1.setSelected(false);
                jTextField16.setEnabled(false);
                jTextField16.setText("");
                }
                
                if(tiponp!=null){
                                jCheckBox1.setSelected(true);
                                if(tiponp.equals("OA")){
                                jComboBox2.setSelectedItem("Obra Adicional");
                                }
                                if(tiponp.equals("OE")){
                                jComboBox2.setSelectedItem("Obra Extra");
                                }
                                if(tiponp.equals("OC")){
                                jComboBox2.setSelectedItem("Obra Complementaria");
                                }
                                if(tiponp.equals("NP")){
                                    jComboBox2.setSelectedItem("No Prevista");
                                }
                       
                              
                            }
                System.out.println("Estoy entrando a adicional");
                if(tipo.equals("NP")){
                this.setTitle("Gestionar Presupuesto de Obra: Seleccionada "+auxid+" Obra Adicional: "+numeropres);
                }else{
                    this.setTitle("Gestionar Presupuesto de Obra: Seleccionada "+auxid+" Variación Nro.: "+nrocuadro);
                }
                String sqladicional = "SELECT porpre, porgam, poruti, porcfi, poripa, porimp FROM mpres"
                        + " where id='"+obradicional+"'"; 
                Statement stadicional = (Statement) conex.createStatement();
                ResultSet rstadicional = stadicional.executeQuery(sqladicional);
                while(rstadicional.next()){
                    jTextField5.setText(rstadicional.getObject(1).toString());
                    jTextField6.setText(rstadicional.getObject(2).toString());
                    jTextField7.setText(rstadicional.getObject(3).toString());
                    jTextField8.setText(rstadicional.getObject(4).toString());
                    jTextField9.setText(rstadicional.getObject(5).toString());                   
                }
            }else{
               
                jCheckBox1.setSelected(false);
                jTextField16.setEnabled(false);
                jTextField16.setText("");
                this.setTitle("Gestionar Presupuesto de Obra: Seleccionada "+id+"");
                 String sqlobra = "SELECT porpre, porgam, poruti, porcfi, poripa, porimp FROM mpres"
                        + " where id='"+id+"'"; 
                Statement stobra = (Statement) conex.createStatement();
                ResultSet rstobra = stobra.executeQuery(sqlobra);
                while(rstobra.next()){
                    jTextField5.setText(rstobra.getObject(1).toString());
                    jTextField6.setText(rstobra.getObject(2).toString());
                    jTextField7.setText(rstobra.getObject(3).toString());
                    jTextField8.setText(rstobra.getObject(4).toString());
                    jTextField9.setText(rstobra.getObject(5).toString());
                    jTextField10.setText(rstobra.getObject(6).toString());
                }
            }
            jTextField14.setText(numegrup);
            jTextField15.setText(codicove);
            jTextArea2.setText(descri);
            jTextField17.setText(unidad);
            
           
           
         
             NumberFormat formatoNumero = NumberFormat.getNumberInstance();
            formatoNumero.setMaximumFractionDigits(2);
            formatoNumero.setMinimumFractionDigits(2);
            double cant = Math.rint(Float.valueOf(cantidad)*100)/100;
             jTextField18.setText(String.valueOf(cant));
            jTextField19.setText(String.valueOf(precuni));
            jTextField20.setText(formatoNumero.format((total2)));
           
            jButton14.setEnabled(true);
            id=auxid;
        } catch (SQLException ex) {
            Logger.getLogger(Presupuesto.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseClicked
         filapartida = jTable2.rowAtPoint(evt.getPoint());
         jButton27.setEnabled(true);
         jButton29.setEnabled(true);
         jButton26.setEnabled(true);
         jButton10.setEnabled(true);
         jButton30.setEnabled(true);     
         jButton32.setEnabled(true);      
         cargapartida();
         cargartotal();
         if(evt.getClickCount()==2){
            ver();
            }      
        
    }//GEN-LAST:event_jTable2MouseClicked

    
    public void verificarcheck() {

        int registros = jTable1.getRowCount();
        int columnas = jTable1.getColumnCount();
       
        Object obj;
        partidas = new String [registros];
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
                        String numegrup=jTable1.getValueAt(i, 3).toString();
                        String select= "SELECT numero FROM mptabs WHERE mtabus_id='"+tabu+"' AND numegrup="+numegrup+"";
                        try {
                            Statement st = (Statement) conex.createStatement();
                            ResultSet rst = st.executeQuery(select);
                            while(rst.next()){
                                partidas[contsel] = rst.getString(1);
                            }
                        } catch (SQLException ex) {
                            Logger.getLogger(Presupuesto.class.getName()).log(Level.SEVERE, null, ex);
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
         Nuevo s = new Nuevo(prin, true, this, conex, 1, id);
        s.setBounds((this.getWidth()/2)-450, (this.getHeight()/2)-275, 900, 550);
        s.setVisible(true);
        
        
    }//GEN-LAST:event_jButton2ActionPerformed

private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
    partida = jTable1.rowAtPoint(evt.getPoint());
    String part =jTable1.getValueAt(partida, 3).toString();
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
    
}//GEN-LAST:event_jTable1MouseClicked

private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    busca();
}//GEN-LAST:event_jButton1ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        int totalfilas = jTable1.getRowCount();
      // System.out.println("totalfilas: "+totalfilas);
        for(int i=0; i<totalfilas; i++){
            auxpart[i] =String.valueOf( jTable1.getValueAt(i, 3));
        }
        auxcont = totalfilas;
        busca();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        agrega();
        
    }//GEN-LAST:event_jButton4ActionPerformed
public void agrega(){
        String num="", descri = null, mbda = null, rendim = null, 
                unidad = null, redond = null, status;
        int entrar=0;
        String auxpartida = null;
        String codicoves="";
            try {
            Statement st= null;
            String sql;
            
            jTextField1.setText("");
            busca();
            try {
                st = (Statement) conex.createStatement();
            } catch (SQLException ex) {
                Logger.getLogger(Presupuesto.class.getName()).log(Level.SEVERE, null, ex);
            }
            verificarcheck();
             for(int i=0; i<contsel;i++){
                 System.out.println(partidas[i]);
             }
             String tiponp = null, nropresupuesto=null; 
              String sql1 = "SELECT tiponp, nropresupuesto FROM mppres WHERE (mpre_id='"+id+"' OR"
                      + "        mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+id+"')) "
                      + "AND tipo='NP' ORDER BY numegrup DESC LIMIT 1";
              Statement sts = (Statement) conex.createStatement();
              ResultSet rsts = sts.executeQuery(sql1);
              while(rsts.next()){
                  tiponp = rsts.getString(1);
                  nropresupuesto = rsts.getString(2);
              }
            
            
            for(int i=0; i<contsel;i++){
                String sqlnumero = "SELECT numero FROM mppres where mpre_id='"+id+"' "
                        + "OR mpre_id IN (SELECT id from mpres where mpres_id ='"+id+"' GROUP BY id) "
                        + "ORDER BY numero DESC LIMIT 1";
               
                ResultSet rst = st.executeQuery(sqlnumero);
                while(rst.next()){
                    nuevonum=rst.getObject(1).toString();
                    nuevo = Integer.parseInt(nuevonum)+1;
                }
                if(nuevo==0){
                    nuevo=1;
                }
                String sqlnumegrup = "SELECT numegrup FROM mppres where mpre_id='"+id+"' OR mpre_id IN (SELECT id from mpres where mpres_id ='"+id+"' GROUP BY id) ORDER BY numegrup DESC LIMIT 1";
                Statement str = (Statement) conex.createStatement();
                ResultSet rstr = str.executeQuery(sqlnumegrup);
                while(rstr.next()){
                    nuevonumegrup = rstr.getInt(1)+1;
                }
                if(nuevonumegrup==0){
                    nuevonumegrup=1;
                }
                insertar=1;
                String selecciona = "SELECT descri, mbdat_id, rendimi, unidad, redondeo, status, mtabus_id "
                        + ", numero, codicove,porcpre,porcgad,porcutil FROM mptabs where numero='"+partidas[i]+"' AND mtabus_id='"+tabu+"'";
                Statement stmt = (Statement) conex.createStatement();
                ResultSet rstmt = stmt.executeQuery(selecciona);
                
                while(rstmt.next()){
                    descri = rstmt.getObject(1).toString();
                    mbda = rstmt.getObject(2).toString();
                    rendimi = rstmt.getObject(3).toString();
                    unidad = rstmt.getObject(4).toString();
                    redond = rstmt.getObject(5).toString();
                    status = rstmt.getObject(6).toString();
                    numero = rstmt.getObject(8).toString();
                    codicoves = rstmt.getString(9);
                    porcpre=rstmt.getFloat("porcpre");
                    porcgad = rstmt.getFloat("porcgad");
                    porcutil=rstmt.getFloat("porcutil");
                }
                
                String existe = 
                        "SELECT count(id) FROM mppres WHERE id='"+codicoves+"' "
                        + "AND (mpre_id='"+id+"' "
                        + "OR mpre_id IN (SELECT id FROM mpres WHERE "
                        + "mpres_id='"+id+"'))";
                Statement stexiste = (Statement) conex.createStatement();
                ResultSet rstexiste = stexiste.executeQuery(existe);
                int cuenta=0;
                while(rstexiste.next())
                {
                 cuenta = Integer.parseInt(rstexiste.getObject(1).toString());   
                }
                auxid= id;
                if(cuenta==0){
                    if(tiponp==null){
                        
                    
                        sql="INSERT INTO mppres (mpre_id, id, numero, numegrup, descri, idband, "
                        + "rendimi, unidad, redondeo, status, cantidad, tipo, nropresupuesto,"
                                + "porcpre,porcgad,porcutil)"
                        + "VALUES ('"+id+"', '"+codicoves+"', " + 

                                                            ""+nuevo+"," + 

                                                            ""+nuevonumegrup+", "
                                                            + "'"+descri+"', "  
                                                             + ""+mbda+","
                                                            + ""+rendimi+","
                                                            + "'"+unidad+"',"
                                                            + ""+redond+","+
                                                            "1, " +
                                                            "1, " +
                                                            "'Org',"+
                                                            "0, "+porcpre+","
                                + ""+porcgad+","+porcutil+")";
                    }else{
                        String codpres = id+tiponp+nropresupuesto;
                        id=codpres;
                       sql="INSERT INTO mppres (mpre_id, id, numero, numegrup, descri, idband, "
                        + "rendimi, unidad, redondeo, status, cantidad, tipo, nropresupuesto, "
                               + "tiponp, porcpre,porcgad,porcutil)"
                        + "VALUES ('"+codpres+"', '"+codicoves+"', " + 

                                                            ""+nuevo+"," + 

                                                            ""+nuevonumegrup+", "
                                                            + "'"+descri+"', "  
                                                             + ""+mbda+","
                                                            + ""+rendimi+","
                                                            + "'"+unidad+"',"
                                                            + ""+redond+","+
                                                            "1, " +
                                                            "1, " +
                                                            "'NP',"+
                                                            "'"+nropresupuesto+"','"+tiponp+"', "+porcpre+","
                                + ""+porcgad+","+porcutil+")";
                    }
                try {
                    st.execute(sql);
                    System.out.println(sql);
                    
                }
                catch (SQLException ex) {
                    Logger.getLogger(Presupuesto.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                jTextField14.setText(partidas[i]);          
                agregarmat(0);
                agregaequipo(0);
                agregamano(0);
                id=auxid;
                calculapartida(String.valueOf(nuevo), id, 0);
                
                
            }else{
                    entrar=1;
                    auxpartida=partidas[i];
                    JOptionPane.showMessageDialog(this, "No se inserto la partida "+codicoves+" porque ya fue insertada para este presupuesto");
                }
            }
            contsel=0;
        } catch (SQLException ex) {
            Logger.getLogger(Presupuesto.class.getName()).log(Level.SEVERE, null, ex);
        }
            cargartotal();
        try {
            buscapartida();
        } catch (SQLException ex) {
            Logger.getLogger(Presupuesto.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            cargapresupuesto();
        } catch (SQLException ex) {
            Logger.getLogger(Presupuesto.class.getName()).log(Level.SEVERE, null, ex);
        }
            if(entrar==1){
                int totalfilas = jTable1.getRowCount();
     //  System.out.println("totalfilas: "+totalfilas);
        for(int i=0; i<totalfilas; i++){
            auxpart[i] ="";
        }
        auxcont = 0;
        busca();
               // JOptionPane.showMessageDialog(this, "No se inserto la partida "+auxpartida+" porque ya se insertó para este presupuesto");
            try {
                
                buscapartida();
            } catch (SQLException ex) {
                Logger.getLogger(Presupuesto.class.getName()).log(Level.SEVERE, null, ex);
            }
            }

}


    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        int totalfilas = jTable1.getRowCount();
     //  System.out.println("totalfilas: "+totalfilas);
        for(int i=0; i<totalfilas; i++){
            auxpart[i] ="";
        }
        auxcont = 0;
        busca();
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
         String sql;
         modificaparametro mp;
         String auxid1 = id;
            presta = Float.valueOf(jTextField5.getText().toString());
            admin = Float.valueOf(jTextField6.getText().toString());
            util = Float.valueOf(jTextField7.getText().toString());
            finan = Float.valueOf(jTextField8.getText().toString());
            impart = Float.valueOf(jTextField9.getText().toString());
            impgen = Float.valueOf(jTextField10.getText().toString());
          
        
        jTable2.setRowSelectionInterval(0, 0);
            if(adicional==1){
                id=obradicional;
                sql = "UPDATE mpres set porgam="+admin+", porcfi = "+finan+", porimp="+impgen+","
                + "poripa="+impart+", porpre="+presta+", poruti="+util+" WHERE id='"+obradicional+"'";
            }else
            {
                sql = "UPDATE mpres set porgam="+admin+", porcfi = "+finan+", porimp="+impgen+","
                + "poripa="+impart+", porpre="+presta+", poruti="+util+" WHERE id='"+id+"'";
            }
        
            
        try {
            Statement stmt1 = (Statement) conex.createStatement();
            stmt1.execute(sql);
           /* mp = new modificaparametro(conex, this, id, presta, admin, finan, util, impart, impgen);
            mp.start();*/
            id=auxid1;
            if(adicional==1) {
                JOptionPane.showMessageDialog(this, "Se ha modificado el presupuesto adicional "+obradicional);
            }
            else {
                JOptionPane.showMessageDialog(this, "Se ha modificado el presupuesto "+id);
            }
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "No se ha modificado el presupuesto");
            Logger.getLogger(Presupuesto.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        //COPIAR PRESUPUESTO COPIA TODO VALUACIONES, PARTIDAS, VARIACIONES, AUMENTOS, CRONOGRAMA, CAPITULOS
        copiapres copia = new copiapres(null,true,conex,id);
        x = (prin.getWidth()/2)-250;
        y = (prin.getHeight()/2)-100;
        copia.setBounds(x, y, 500, 200);
        copia.setVisible(true);
        
          // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        Partida part = new Partida(prin, true, conex, id, prin,this,jTextField13.getText().toString());
        x = (prin.getWidth()/2)-390;
        y = (prin.getHeight()/2)-350;
        part.setBounds(x, y, 750, 520);
        part.setVisible(true);
        try {
            buscapartida();
        } catch (SQLException ex) {
            Logger.getLogger(Presupuesto.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        ver(); 
        
        
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        prin.visible();
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged

        
       if(jComboBox1.getSelectedItem()!=null&&inicio==0)   {  
        tabu = jComboBox1.getSelectedItem().toString();
        settabu(tabu);
       }
        try {
            cargapresupuesto();
        } catch (SQLException ex) {
            Logger.getLogger(Presupuesto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void jTextField15FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField15FocusLost
        int vacio=0;
        String sql;
        int cont1=0, cont2=0;
        int op =0;
        // System.out.println("TEXTO "+jTextField15.getText());
        if(jTextField15.getText().toString().equals("") || entrarafocus15==0){
            vacio=1;
        }else{
            entrarafocus15=0;
             focuslost = 1;
            Statement stmt = null, st1=null, st2=null;
            try {
                stmt = (Statement) conex.createStatement();
                st1 = (Statement) conex.createStatement();
                st2 = (Statement) conex.createStatement();
            } catch (SQLException ex) {
                Logger.getLogger(Presupuesto.class.getName()).log(Level.SEVERE, null, ex);
            }

            partidanueva = jTextField15.getText().toString();
            String verifica1 = "SELECT count(mp.id) FROM mppres as mp, mptabs as mt WHERE mp.id = '"+partidanueva+"'"
                    + " AND mp.id=mt.codicove AND mp.mpre_id='"+id+"'";
            
            try {
                ResultSet vr1 = st1.executeQuery(verifica1);

                while(vr1.next()) {
                    cont1 = Integer.parseInt(vr1.getObject(1).toString());
                }

               

            } catch (SQLException ex) {
                Logger.getLogger(Presupuesto.class.getName()).log(Level.SEVERE, null, ex);
            }

           if(cont1>0){
               int opi = JOptionPane.showConfirmDialog(null, "Partida Ya fue incluida en presupuesto, Desea Modificarla? Sí/No", "Partida ya Existe", JOptionPane.YES_NO_OPTION);
               if(opi==JOptionPane.YES_OPTION){
                sql = "SELECT codicove, descri, unidad, precunit, precasu, redondeo, rendimi,status,mbdat_id, numero"
                        + ",porcgad,porcpre,porcutil   FROM"
                + " mptabs WHERE codicove='"+partidanueva+"' AND mtabus_id='"+jComboBox1.getSelectedItem().toString()+"'";
                ResultSet rs ;
                try {
                    rs = stmt.executeQuery(sql);

                    while(rs.next()){
                        porcgad = rs.getFloat("porcgad");
                         porcpre= rs.getFloat("porcpre");
                         porcutil = rs.getFloat("porcutil");
                        jTextField15.setText(rs.getObject(1).toString());
                        jTextArea2.setText(rs.getObject(2).toString());
                        jTextField17.setText(rs.getObject(3).toString());
                        if((rs.getObject(6))==null){//redondeo=0
                            redondeo="0";
                            jTextField19.setText(rs.getObject(4).toString());
                        }else{
                            if(Integer.parseInt(rs.getObject(6).toString())==0){
                                redondeo="0";
                                jTextField19.setText(rs.getObject(4).toString());
                            }else{
                                jTextField19.setText(rs.getObject(5).toString());
                                redondeo="1";
                            }
                        }
                        precasu = rs.getObject(5).toString();
                        precunit = rs.getObject(4).toString();
                        rendimi = rs.getObject(7).toString();
                        mbdat = rs.getObject(8).toString();
                        numpartida = rs.getObject(9).toString();
                        jTextField14.setText(rs.getObject("numero").toString());
                        numero = jTextField14.getText();
                        insertar=0;
                        
                        agregarmat(0);
                        agregaequipo(0);
                        agregamano(0);
                        calculapartida(numero, id, 0);
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(Presupuesto.class.getName()).log(Level.SEVERE, null, ex);
                }
               }else{
                   entrarafocus15=1;
                   jTextField15.setText("");
                   jTextField14.setText("");
                   jTextArea2.setText("");
                   jTextField17.setText("");
                   jTextField18.setText("");
                   jTextField19.setText("");
                   jTextField20.setText("");
                   
               }
                }else{
                int contador=0;
                sql = "SELECT COUNT(*) FROM mptabs WHERE codicove='"+partidanueva+"' "
                        + "AND mtabus_id='"+jComboBox1.getSelectedItem().toString()+"'";
                try {
                    Statement stsql = (Statement) conex.createStatement();
                    ResultSet rstsql = stsql.executeQuery(sql);
                        while(rstsql.next()){
                            contador = rstsql.getInt(1);
                        }
                } catch (SQLException ex) {
                    Logger.getLogger(Presupuesto.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                
                if(contador>0){
                    String tiponp = null,nropresupuesto=null;
                String consult = "SELECT tiponp, nropresupuesto FROM mppres WHERE "
                        + "(mpre_id='"+id+"' OR mpre_id IN (SELECT id FROM mpres"
                        + " Where mpres_id='"+id+"')) AND "
                        + "tipo='NP' ORDER BY numero DESC LIMIT 1";
                Statement sts;
                try {
                    sts = (Statement) conex.createStatement();
                     ResultSet rsts = sts.executeQuery(consult);
                while(rsts.next()){
                    tiponp = rsts.getString(1);
                    nropresupuesto = rsts.getString(2);
                }
                } catch (SQLException ex) {
                    Logger.getLogger(Presupuesto.class.getName()).log(Level.SEVERE, null, ex);
                }
                   sql = "SELECT codicove, descri, unidad, precunit, precasu, redondeo, rendimi,status,mbdat_id, numero "
                           + ",porcgad,porcpre,porcutil "
                           + " FROM"
                + " mptabs WHERE codicove='"+partidanueva+"' AND mtabus_id='"+jComboBox1.getSelectedItem().toString()+"'";
                ResultSet rs ;
                try {
                    rs = stmt.executeQuery(sql);
                    
                    while(rs.next()){
                         porcgad = rs.getFloat("porcgad");
                         porcpre= rs.getFloat("porcpre");
                         porcutil = rs.getFloat("porcutil");
                        jTextField15.setText(rs.getObject(1).toString());
                        jTextArea2.setText(rs.getObject(2).toString());
                        jTextField17.setText(rs.getObject(3).toString());
                        if((rs.getObject(6))==null){//redondeo=0
                            redondeo="0";
                            jTextField19.setText(rs.getObject(4).toString());
                        }else{
                            if(Integer.parseInt(rs.getObject(6).toString())==0){
                                redondeo="0";
                                jTextField19.setText(rs.getObject(4).toString());
                            }else{
                                jTextField19.setText(rs.getObject(5).toString());
                                redondeo="1";
                            }
                        }
                        precasu = rs.getObject(5).toString();
                        precunit = rs.getObject(4).toString();
                        rendimi = rs.getObject(7).toString();
                        mbdat = rs.getObject(9).toString();
                        numpartida = rs.getObject("numero").toString();
                        jTextField14.setText(rs.getObject("numero").toString());
                        numero = jTextField14.getText();
                        insertar=0;
                        if(tiponp!=null){
                                jCheckBox1.setSelected(true);
                                if(tiponp.equals("OA")){
                                jComboBox2.setSelectedItem("Obra Adicional");
                                }
                                if(tiponp.equals("OE")){
                                jComboBox2.setSelectedItem("Obra Extra");
                                }
                                if(tiponp.equals("OC")){
                                jComboBox2.setSelectedItem("Obra C");
                                }
                                if(tiponp.equals("NP")){
                                    jComboBox2.setSelectedItem("No Prevista");
                                }
                                jTextField16.setEnabled(true);
                                jTextField16.setText(nropresupuesto);
                            }
                        agregarmat(0);
                        agregaequipo(0);
                        agregamano(0);
                        calculapartida(numero, id, 0);
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(Presupuesto.class.getName()).log(Level.SEVERE, null, ex);
                }
                }else{
                    JOptionPane.showMessageDialog(null, "Partida no existe en el tabulador");
                    jTextField15.setText("");
                   jTextField14.setText("");
                   jTextArea2.setText("");
                   jTextField17.setText("");
                   jTextField18.setText("");
                   jTextField19.setText("");
                   jTextField20.setText("");
                   entrarafocus15=1;
                    
                }
           }
        }

    }//GEN-LAST:event_jTextField15FocusLost

    private void jTextArea2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextArea2KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_TAB) {
            if (evt.getModifiers() == KeyEvent.SHIFT_MASK) {
                ((javax.swing.JTextArea) evt.getSource()).transferFocusBackward();
            } else {
                ((javax.swing.JTextArea) evt.getSource()).transferFocus();
            }
            evt.consume();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_jTextArea2KeyPressed

    private void jTextField20FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField20FocusLost
        int edita=0;
        String auxid1, conte = null;
        String tab, codicoves, num, numpre, descri, unidad, cantidad, precio, total1, tipo,tiponp=null;
        String codpres=null;
        nuevo=0;
        nuevonumegrup=0;
        String select, insertare;
        if(focuslost==1){
        tab = jComboBox1.getSelectedItem().toString();
        codicoves = jTextField15.getText().toString();
        num = jTextField14.getText().toString();
        try {
            Statement str = (Statement) conex.createStatement();
            String consulta="SELECT count(id) FROM mppres WHERE id='"+codicoves+"' "
                    + "AND (mpre_id = '"+id+"'"
                    + " OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+id+"'))";
            ResultSet existe = str.executeQuery(consulta);
            while(existe.next()){
                edita = Integer.parseInt(existe.getObject(1).toString());
            }
        } catch (SQLException ex) {
            Logger.getLogger(Presupuesto.class.getName()).log(Level.SEVERE, null, ex);
        }

        auxid1=id;
        if(jCheckBox1.isSelected()){
            entra=1;
             tipo="NP";
            
            if(jComboBox2.getSelectedIndex()==0){
                tiponp = "NP";
            }
            if(jComboBox2.getSelectedIndex()==1){
                 tiponp = "OE";
            }
             if(jComboBox2.getSelectedIndex()==2){
                 tiponp = "OA";
            }   
             if(jComboBox2.getSelectedIndex()==3){
                 tiponp = "OC";
            }
            String nomabr="";
            numpre = jTextField16.getText().toString();
            codpres = id+tiponp+numpre;
           
            select = "SELECT count(id) FROM mpres WHERE id='"+codpres+"'";

            try {
                Statement cuenta = (Statement) conex.createStatement();
                ResultSet rscuenta = cuenta.executeQuery(select);

                while(rscuenta.next()){
                    cuentan = Integer.parseInt(rscuenta.getObject(1).toString());
                }
                // System.out.println("Cuentan: "+cuentan);
                if(cuentan == 0)
                {
                    Date fechas = new Date();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    String fecha = format.format(fechas);
                    insertare = "INSERT INTO mpres "
                            + "(id, nomabr, nombre, ubicac, fecini, fecfin, feccon, fecimp, porgam, "
                            + "porcfi, porimp, poripa, porpre, poruti, codpro, codcon, parpre, nrocon, nroctr, fecapr, "
                            + "nrolic, status, mpres_id, memo, timemo, fecmemo, seleccionado, partidapres, fecharegistro)"
                    + "SELECT '"+codpres+"', nomabr, nombre, ubicac, fecini,"
                    + "fecfin, feccon, fecimp, porgam, porcfi, porimp, poripa,"
                    + "porpre, poruti, codpro, codcon, parpre, nrocon, nroctr, fecapr,"
                    + "nrolic, 1, '"+id+"',memo,timemo, fecmemo, 0, partidapres, '"+fecha+"'  FROM mpres WHERE id='"+id+"'";

                    // System.out.println("Inserta Presupuesto Adicional: "+insertar);
                    Statement insert = (Statement) conex.createStatement();
                    insert.execute(insertare);
                    System.out.println("Inserta Presupuesto Adicional: "+insertare);
                }
            } catch (SQLException ex) {
                Logger.getLogger(Presupuesto.class.getName()).log(Level.SEVERE, null, ex);
            }
           

        }else{
            numpre = "0";
            tipo="Org";
        }
        descri = jTextArea2.getText().toString();
        unidad = jTextField17.getText().toString();
        cantidad = jTextField18.getText().toString();
        precio = jTextField19.getText().toString();
        total1 = jTextField20.getText().toString();

        if(entra==1){
            entra=0;
            id=codpres;
        }
        try {

            Statement st = (Statement) conex.createStatement();

            String sqlnumero = "SELECT numero FROM mppres where "
                    + "mpre_id='"+auxid1+"' OR mpre_id IN "
                    + "(SELECT id from mpres where mpres_id ='"+auxid1+"' "
                    + "GROUP BY id) ORDER BY numero DESC LIMIT 1";

            ResultSet rst = st.executeQuery(sqlnumero);
            while(rst.next()){
                nuevonum=rst.getObject(1).toString();
                nuevo = Integer.parseInt(nuevonum)+1;
            }
            
            String sqlnumegrup = "SELECT numegrup FROM mppres where "
                    + "mpre_id='"+auxid1+"' "
                    + "OR mpre_id IN (SELECT id from mpres where "
                    + "mpres_id ='"+auxid1+"' GROUP BY id) "
                    + "ORDER BY numegrup DESC LIMIT 1";
                Statement str = (Statement) conex.createStatement();
                ResultSet rstr = str.executeQuery(sqlnumegrup);
                while(rstr.next()){
                    nuevonumegrup = rstr.getInt(1)+1;
                }
            if(nuevo==0) {
                nuevo++;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Presupuesto.class.getName()).log(Level.SEVERE, null, ex);
        }

        String sqlpartida;

        if(edita==0){

            if(nuevo==0){
                nuevo=1;
            }
            if(nuevonumegrup==0){
                nuevonumegrup=1;
            }
            if(tiponp!=null){
            sqlpartida = "INSERT INTO mppres "
                    + "(mpre_id, id, numero, numegrup, descri, idband,"
            + "rendimi, unidad, precasu, precunit, redondeo, status, cantidad, tipo, "
            + "nropresupuesto,tiponp, porcgad,porcpre,porcutil) VALUES ('"+codpres+"', "
                    + "'"+jTextField15.getText().toString() +"',"
            + ""+nuevo+","+nuevonumegrup+",'"+jTextArea2.getText().toString()+"',"
            + ""+mbdat+","+rendimi+", '"+unidad+"', "+precasu+", "+precunit+","
            + ""+redondeo+", '1', "+jTextField18.getText().toString()+", '"+tipo+"',"
            + "'"+numpre+"','"+tiponp+"',"+porcgad+","+porcpre+","+porcutil+")";
            }else{
                sqlpartida = "INSERT INTO mppres "
                    + "(mpre_id, id, numero, numegrup, descri, idband,"
            + "rendimi, unidad, precasu, precunit, redondeo, status, cantidad, tipo, "
            + "nropresupuesto, porcgad,porcpre,porcutil) VALUES ('"+id+"', '"+jTextField15.getText().toString() +"',"
            + ""+nuevo+","+nuevonumegrup+",'"+jTextArea2.getText().toString()+"',"
            + ""+mbdat+","+rendimi+", '"+unidad+"', "+precasu+", "+precunit+","
            + ""+redondeo+", '1', "+jTextField18.getText().toString()+", '"+tipo+"',"
            + "'"+numpre+"',"+porcgad+","+porcpre+","+porcutil+")";
            }
        }else{
            if(tiponp==null){
            sqlpartida = "UPDATE mppres set"
                    + " mpre_id='"+id+"', descri='"+jTextArea2.getText()+"'"
            + ", idband="+mbdat+", rendimi="+rendimi+", unidad='"+unidad+"'"
            + ", precasu="+precasu+", precunit="+precunit+", redondeo="+redondeo+","
            + "cantidad="+jTextField18.getText().toString()+", tipo='"+tipo+"',"
            + "nropresupuesto='"+numpre+"' WHERE id='"+codicoves+"'";

        }else{
              sqlpartida = "UPDATE mppres set"
                    + " mpre_id='"+codpres+"', descri='"+jTextArea2.getText()+"'"
            + ", idband="+mbdat+", rendimi="+rendimi+", unidad='"+unidad+"'"
            + ", precasu="+precasu+", precunit="+precunit+", redondeo="+redondeo+","
            + "cantidad="+jTextField18.getText().toString()+", tipo='"+tipo+"',"
            + "nropresupuesto='"+numpre+"', tiponp='"+tiponp+"' "
            + "WHERE id='"+codicoves+"'";   
            }
        }
        try {
            System.out.println(sqlpartida);
            Statement inserta = (Statement) conex.createStatement();
            inserta.execute(sqlpartida);
            JOptionPane.showMessageDialog(this, "Se ha insertado la partida");
            insertar=1;
            contmat=contequipo=contmano=0;
            numero = jTextField14.getText();
            String sql = "SELECT numero FROM mptabs WHERE numegrup="+numero+" AND mtabus_id='"+tabu+"'";
            Statement st = (Statement) conex.createStatement();
            ResultSet rst = st.executeQuery(sql);
            while(rst.next()){
                numero = rst.getString("numero");
            }
            entrarafocus15=1; 
            agregarmat(edita);
            agregaequipo(edita);
            agregamano(edita);
            calculapartida(numero, id, edita);
            id=auxid1;
          
            buscapartida();
            jTextField14.setText("");
            jTextField15.setText("");
            jTextField16.setText("");
            jTextField16.setEnabled(false);
            jTextField17.setText("");
            jTextField18.setText("");
            jTextField19.setText("0.00");
            jTextField20.setText("0.00");
            jCheckBox1.setSelected(false);
            jTextArea2.setText("");
            
        } catch (SQLException ex) {
          
            JOptionPane.showMessageDialog(this, "No Se ha insertado la partida");

            Logger.getLogger(Presupuesto.class.getName()).log(Level.SEVERE, null, ex);
        }
        focuslost=0;
        }

    }//GEN-LAST:event_jTextField20FocusLost

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        if(jCheckBox1.isSelected()){
            jTextField16.setEnabled(true);
        }else{
            jTextField16.setEnabled(false);
        }
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jTextField18FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField18FocusLost
        if(jTextField18.getText().equals("")){
            jTextField18.setText("0.00");
        }
        
        float cantidad;
        float precio, total1;

        cantidad = Float.valueOf(jTextField18.getText().toString());
        precio = Float.valueOf(jTextField19.getText().toString());
        total1 = cantidad * precio;
         NumberFormat formatoNumero = NumberFormat.getNumberInstance();
            formatoNumero.setMaximumFractionDigits(2);
            formatoNumero.setMinimumFractionDigits(2);
            jTextField20.setText(String.valueOf(formatoNumero.format(total1)));
       

    }//GEN-LAST:event_jTextField18FocusLost

    private void jTextField19FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField19FocusLost

    }//GEN-LAST:event_jTextField19FocusLost

    private void jTextField14KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField14KeyPressed
          System.out.println(evt.getKeyCode()+" "+evt.getKeyChar() );
        int car = evt.getKeyCode();
        
    }//GEN-LAST:event_jTextField14KeyPressed
  public void noprevista(){
        
    }
    private void jTextField14FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField14FocusLost
        int vacio=0;
        String sql;
        int cont1=0, cont2=0, contex=0, noexiste=0;
        int op =0;
        //System.out.println("TEXTO "+jTextField14.getText());
        if(jTextField14.getText().toString().equals("")|| entrarafocus15 == 0){
            vacio=1;
        }else{
            
            entrarafocus15=0;
            focuslost = 1;
            Statement stmt = null, st1=null, st2=null, ext = null;
            try {
                stmt = (Statement) conex.createStatement();
                st1 = (Statement) conex.createStatement();
                ext = (Statement) conex.createStatement();
                st2 = (Statement) conex.createStatement();
            } catch (SQLException ex) {
                Logger.getLogger(Presupuesto.class.getName()).log(Level.SEVERE, null, ex);
            }

            partidanueva = jTextField14.getText().toString();
            
            String siexiste = "SELECT count(codicove) FROM mptabs WHERE"
                    + " numegrup="+partidanueva+  " AND mtabus_id='"+jComboBox1.getSelectedItem().toString()+"'";
            String verifica1 = "SELECT count(mp.id) FROM mppres mp, mptabs mt WHERE  mt.numegrup= "+partidanueva+" AND mp.id = mt.codicove AND mt.mtabus_id ='"+jComboBox1.getSelectedItem().toString()+"'";
            String verifica2 = "SELECT count(mp.id) FROM mppres mp, mptabs mt WHERE  mt.numegrup= "+partidanueva+" AND mp.id = mt.codicove";
            try {
                ResultSet vr1 = st1.executeQuery(verifica1);
                ResultSet vr2 = st2.executeQuery(verifica2);
                ResultSet ex = ext.executeQuery(siexiste);

                while(vr1.next()){
                    cont1 = Integer.parseInt(vr1.getObject(1).toString());
                    //System.out.println("cont1="+cont1);
                }
                while(vr2.next()){
                    cont2 = Integer.parseInt(vr2.getObject(1).toString());
                    // System.out.println("cont2="+cont2);
                }
                while(ex.next()){
                    contex = Integer.parseInt(ex.getObject(1).toString());

                }
            } catch (SQLException ex) {
                Logger.getLogger(Presupuesto.class.getName()).log(Level.SEVERE, null, ex);
            }
            cont1=0;
            cont2=0;
            
            if(cont1!=0&&cont2==0&&contex!=0){
                op= JOptionPane.showConfirmDialog(null, "Número de Partida ya existe, desea sustituirla por esta del tabulador "+jComboBox1.getSelectedItem().toString()+"?");
            }
            if(op==0&&cont2==0&&contex!=0){
                String tiponp = null,nropresupuesto=null;
                String consult = "SELECT tiponp, nropresupuesto FROM mppres WHERE "
                        + "(mpre_id='"+id+"' OR mpre_id IN (SELECT id FROM mpres"
                        + " Where mpres_id='"+id+"')) AND "
                        + "tipo='NP' ORDER BY numero DESC LIMIT 1";
                Statement sts;
                try {
                    sts = (Statement) conex.createStatement();
                     ResultSet rsts = sts.executeQuery(consult);
                while(rsts.next()){
                    tiponp = rsts.getString(1);
                    nropresupuesto = rsts.getString(2);
                }
                } catch (SQLException ex) {
                    Logger.getLogger(Presupuesto.class.getName()).log(Level.SEVERE, null, ex);
                }
               
                sql = "SELECT codicove, descri, unidad, IFNULL(precunit,0.00), IFNULL(precasu,0.00), redondeo, rendimi,"
                        + "status,mbdat_id, numero,porcgad,porcpre,porcutil  FROM"
                + " mptabs WHERE numegrup="+partidanueva+" AND mtabus_id='"+jComboBox1.getSelectedItem().toString()+"'";
                  Statement otro=null;
                try {
                  otro = (Statement) conex.createStatement();
                } catch (SQLException ex) {
                    Logger.getLogger(Presupuesto.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                try {
                   ResultSet rs = otro.executeQuery(sql);
                    String covenin="";
                    while(rs.next()){
                         porcgad = rs.getFloat("porcgad");
                         porcpre= rs.getFloat("porcpre");
                         porcutil = rs.getFloat("porcutil");
                        jTextField15.setText(rs.getObject(1).toString());
                        covenin=jTextField15.getText();
                        jTextArea2.setText(rs.getObject(2).toString());
                        jTextField17.setText(rs.getObject(3).toString());
                        if((rs.getObject(6))==null){//redondeo=0
                            redondeo="0";
                            jTextField19.setText(rs.getObject(4).toString());
                        }else{
                            if(Integer.parseInt(rs.getObject(6).toString())==0){
                                redondeo="0";
                                jTextField19.setText(rs.getObject(4).toString());
                            }else{
                                jTextField19.setText(rs.getObject(5).toString());
                                redondeo="1";
                            }
                        }
                       
                        precasu = rs.getObject(5).toString();
                        precunit = rs.getObject(4).toString();
                        rendimi = rs.getObject(7).toString();
                        mbdat = rs.getObject(8).toString();
                        numpartida = rs.getObject(9).toString();
                        numero = jTextField14.getText();
                        String sql1 = "SELECT numero FROM mptabs WHERE numegrup="+numero+" AND mtabus_id='"+tabu+"'";
                        Statement ste = (Statement) conex.createStatement();
                        ResultSet rste = ste.executeQuery(sql1);
                        jTextField18.setText("0.00");
                        while(rste.next()){
                            numero = rste.getString(1);
                        }
                        int esta=0;
                        String select = "SELECT count(*) FROM mppres WHERE id='"+covenin+"' AND ("
                                + "mpre_id='"+id+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+id+"'))";
                        Statement sselect = (Statement) conex.createStatement();
                        ResultSet rselect = sselect.executeQuery(select);
                        while(rselect.next())
                        {
                            esta = rselect.getInt(1);
                        }
                        
                        if(esta>0){
                            JOptionPane.showMessageDialog(this, "El número de la partida insertada ya existe ingrese otra");
                             entrarafocus15=1;
                              jTextField14.setText("");
                            jTextField15.setText("");
                            jCheckBox1.setSelected(false);
                            
                            jTextField16.setEnabled(false);
                            jTextField16.setText("");
                            jTextArea2.setText("");
                            jTextField17.setText("");
                            jTextField18.setText("");
                            jTextField19.setText("");
                            jTextField20.setText("");
                            jTextField14.requestFocus();
                             
                        }else{
//                            JOptionPane.showMessageDialog(rootPane, tiponp);
                            if(tiponp!=null){
                                jCheckBox1.setSelected(true);
                                if(tiponp.equals("OA")){
                                jComboBox2.setSelectedItem("Obra Adicional");
                                }
                                if(tiponp.equals("OE")){
                                jComboBox2.setSelectedItem("Obra Extra");
                                }
                                if(tiponp.equals("OC")){
                                jComboBox2.setSelectedItem("Obra C");
                                }
                                if(tiponp.equals("NP")){
                                    jComboBox2.setSelectedItem("No Prevista");
                                }
                                jTextField16.setEnabled(true);
                                jTextField16.setText(nropresupuesto);
                            }
                        insertar=0;
                        agregarmat(0);
                        agregaequipo(0);
                        agregamano(0);
                        calculapartida(jTextField14.getText(), id, 0);
                        }
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(Presupuesto.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if(op==1){
                entrarafocus15=1;
                jTextField14.setText("");
                jTextField14.requestFocus();
                
            }
            if(cont2!=0&&contex!=0){
                entrarafocus15=1;
                jTextField14.setText("");
                jTextField14.requestFocus();
                JOptionPane.showMessageDialog(this, "El número de la partida insertada ya existe ingrese otra");
            }
            if(contex==0){
                entrarafocus15=1;
                jTextField14.setText("");
                jTextField14.requestFocus();
                JOptionPane.showMessageDialog(this, "La partida no se encuentra registrada");
            }
        }
    }//GEN-LAST:event_jTextField14FocusLost

    private void jTextField14MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField14MousePressed

    }//GEN-LAST:event_jTextField14MousePressed

    private void jTextField14MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField14MouseClicked
        jTextField14.setText("");
        jTextField15.setText("");
        jCheckBox1.setSelected(false);
        jTextField16.setEnabled(false);
        jTextField16.setText("");
        jTextArea2.setText("");
        jTextField17.setText("");
        jTextField18.setText("");
        jTextField19.setText("");
        jTextField20.setText("");
        entrarafocus15=1;
    }//GEN-LAST:event_jTextField14MouseClicked

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed

        valuacion val = new valuacion(prin, true, conex, id);
        int xv = (prin.getWidth()/2)-375;
        int yv = (prin.getHeight()/2)-700/2;
        val.setBounds(xv, yv, 850, 700);
        val.setVisible(true);
        
    }//GEN-LAST:event_jButton15ActionPerformed

    
    public String getpres(){
        return id;
    }
    public double gettotal(){
        return total;
    }
    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed

        aumentosdismi aumento = new aumentosdismi(prin, closable, id, conex, total);
        int xi = (this.getWidth()/2)-1200/2;
        int yi = (this.getHeight()/2)-750/2;
        aumento.setBounds(xi, yi, 1250, 750);
        aumento.setVisible(true);
    }//GEN-LAST:event_jButton16ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        // TODO add your handling code here:
        int op = JOptionPane.showConfirmDialog(this,"Es un Proceso Delicado ¿Desea Reordenar las Partidas? ", "Reordenar Partidas", JOptionPane.YES_NO_OPTION);
         int n = mptabs.getRowCount();
         int ultima = 0;
      Vector data = mptabs.getDataVector();
       String[] coldata = new String[mptabs.getRowCount()], grupo = new String[mptabs.getRowCount()];
       int[] tipo = new int[mptabs.getRowCount()];
       
       int [] numeros = new int[mptabs.getRowCount()];
       
       if(op==JOptionPane.YES_OPTION){
           int opcion=JOptionPane.showConfirmDialog(this, "Proceso delicado ¿Esta seguro?","Reodenar Partidas", JOptionPane.YES_NO_OPTION);
           if(opcion==JOptionPane.YES_OPTION){
        for (int i=0; i<coldata.length; i++) {
        coldata[i] = ((Vector)data.get(i)).get(2).toString();
        numeros[i] = Integer.valueOf(((Vector)data.get(i)).get(0).toString());
        grupo[i] = ((Vector)data.get(i)).get(4).toString();
        tipo[i] = Integer.parseInt(((Vector)data.get(i)).get(5).toString());
        
    }
        for(int k=0;k<coldata.length;k++) {
            for(int f=0;f<coldata.length-1-k;f++) {
                if (coldata[f].compareTo(coldata[f+1])>0) {
                    String auxnota;
                    auxnota=coldata[f].toString();
                    coldata[f]=coldata[f+1];
                    coldata[f+1]=auxnota;
                    int auxnumero;
                    int auxtipos;
                    String auxgrupo;
                    auxnumero=numeros[f];
                    auxtipos = tipo[f];
                    auxgrupo = grupo[f];
                    numeros[f]=numeros[f+1];
                    grupo[f]=grupo[f+1];
                    tipo[f]=tipo[f+1];
                    tipo[f+1]=auxtipos;
                    numeros[f+1]=auxnumero;
                    grupo[f+1]=auxgrupo;
                }
            }
        }
        for(int k=0;k<coldata.length;k++) {
            for(int f=0;f<coldata.length-1-k;f++) {
                if (tipo[f]>tipo[f+1]) {
                    int auxnota;
                    auxnota= tipo[f];
                    tipo[f]=tipo[f+1];
                    tipo[f+1]=auxnota;
                    int auxnumero;
                     String auxgrupo;
                    String auxcodi;
                    auxnumero =numeros[f];
                      auxgrupo = grupo[f];
                    auxcodi = coldata[f];
                    numeros[f]=numeros[f+1];
                    coldata[f]=coldata[f+1];
                    numeros[f+1]=auxnumero;
                    coldata[f+1]=auxcodi;
                    grupo[f]=grupo[f+1];
                     grupo[f+1]=auxgrupo;
                }
            }
        }
        
        
       
        for(int k=0;k<coldata.length;k++) {
            for(int f=0;f<coldata.length-1-k;f++) {
                if (!(grupo[f].equals("Org")) && (grupo[f+1].equals("Org")) || (!(grupo[f].equals("NP")) && (grupo[f+1].equals("NP") ) && !(grupo[f].equals("Org")))){
                    String auxnota;
                    auxnota= grupo[f];
                    grupo[f]=grupo[f+1];
                    grupo[f+1]=auxnota;
                    int auxnumero;
                    String auxcodi;
                    int auxtipos;
                     auxtipos = tipo[f];
                    auxnumero =numeros[f];
                    auxcodi = coldata[f];
                    numeros[f]=numeros[f+1];
                    coldata[f]=coldata[f+1];
                     tipo[f]=tipo[f+1];
                    tipo[f+1]=auxtipos;
                    numeros[f+1]=auxnumero;
                    coldata[f+1]=auxcodi;
                    ultima++;
                }
            }
           
        }
                  
      reordena reor = new reordena(conex, coldata, numeros, id, this);
      reor.start();
       }
       }
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed

        String select = "SELECT count(*) FROM mppres WHERE tipo='VP' AND ("
                + "mpre_id='"+id+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+id+"'))";
        int count=0;
        try {
            Statement sts = (Statement) conex.createStatement();
            ResultSet rsts = sts.executeQuery(select);
            while(rsts.next()){
                count=rsts.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Presupuesto.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(count==0){
            String cuentavalu="SELECT COUNT(*) FROM dvalus WHERE mpre_id='"+id+"'";
            int cuen=0;
            try {
                Statement sts = (Statement) conex.createStatement();
                ResultSet rsts = sts.executeQuery(cuentavalu);
                while(rsts.next()){
                    cuen = rsts.getInt(1);
                }
            } catch (SQLException ex) {
                Logger.getLogger(Presupuesto.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(cuen>0){
            parametrorecon para = new parametrorecon(prin, true, conex, id, "1",id+" VP-1");
            int xi = (this.getWidth()/2)-550/2;
        int yi = (this.getHeight()/2)-600/2;
        para.setBounds(xi, yi, 550, 600);
        para.setVisible(true);
            }else{
                JOptionPane.showMessageDialog(rootPane, "Debe hacer por lo menos una valuación para poder reconsiderarla");
            }
        }
         String select1 = "SELECT count(*) FROM mppres WHERE tipo='VP' AND ("
                + "mpre_id='"+id+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+id+"'))";
        int count1=0;
        try {
            Statement sts = (Statement) conex.createStatement();
            ResultSet rsts = sts.executeQuery(select1);
            while(rsts.next()){
                count1=rsts.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Presupuesto.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(count1>0){
        reconsideraciones recon = new reconsideraciones(prin, true, id, conex, this,prin);
       int xi = (this.getWidth()/2)-800/2;
        int yi = (this.getHeight()/2)-700/2;
        recon.setBounds(xi, yi, 800, 700);
        recon.setVisible(true);
        try {
            buscapartida();
        } catch (SQLException ex) {
            Logger.getLogger(Presupuesto.class.getName()).log(Level.SEVERE, null, ex);
        }
        }else{
            JOptionPane.showMessageDialog(null, "Debe agregar las partidas para abrir la reconsideración de precios");
        }
    }//GEN-LAST:event_jButton17ActionPerformed

    private void jTable2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable2KeyPressed

    }//GEN-LAST:event_jTable2KeyPressed

    
  
    private void jTextField14FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField14FocusGained
jTextArea2.setEditable(true);
String consulta = "SELECT tipo, nropresupuesto FROM mppres WHERE (mpre_id='"+id+"' OR mpre_id IN (SELECT id "
                + "FROM mpres WHERE mpres_id = '"+id+"')) ORDER BY numero DESC LIMIT 1";
String tipo ="",nro ="";
        try {
           Statement st = (Statement) conex.createStatement();
           ResultSet rst = st.executeQuery(consulta);
           while(rst.next())
           {
               nro = rst.getString(1);
               tipo = rst.getString(2);
               
           }   
           
           if(!tipo.equals("VP") || !tipo.equals("Org"))
           {
               jTextField16.setText(nro);
               
               
           }
                    // TODO add your handling code here:
        } catch (SQLException ex) {
            Logger.getLogger(Presupuesto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jTextField14FocusGained

    private void jTextField14KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField14KeyTyped
       
        int code = evt.getKeyCode();
        char car = evt.getKeyChar();
        if ((car<'0' || car>'9')) {
            System.out.println("Entra el estupidonsqui");
            evt.consume();
        }
        if(code==38 || code==40){
            filapartida = jTable2.getSelectedRow();           
            cargapartida();                  
        }
    }//GEN-LAST:event_jTextField14KeyTyped

    private void jTextField18KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField18KeyTyped
        char car = evt.getKeyChar();
        int repite = new StringTokenizer(jTextField18.getText().toString(),".").countTokens() - 1;
        if ((car<'0' || car>'9') && car!='.') {            
            evt.consume();
        }
        if(car=='.'&& repite==1){
             evt.consume();
        }
        
    }//GEN-LAST:event_jTextField18KeyTyped

    private void jTextField16KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField16KeyTyped
        char car = evt.getKeyChar();
        
        if ((car<'0' || car>'9')) {            
            evt.consume();
        }
        
    }//GEN-LAST:event_jTextField16KeyTyped

    private void jTextField5KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField5KeyTyped
 char car = evt.getKeyChar();
        int repite = new StringTokenizer(jTextField5.getText().toString(),".").countTokens() - 1;
        if ((car<'0' || car>'9') && car!='.') {            
            evt.consume();
        }
        if(car=='.'&& repite==1){
             evt.consume();
        }
        
        
        
    }//GEN-LAST:event_jTextField5KeyTyped

    private void jTextField8KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField8KeyTyped
 char car = evt.getKeyChar();
        int repite = new StringTokenizer(jTextField8.getText().toString(),".").countTokens() - 1;
        if ((car<'0' || car>'9') && car!='.') {            
            evt.consume();
        }
        if(car=='.'&& repite==1){
             evt.consume();
        }
        
    }//GEN-LAST:event_jTextField8KeyTyped

    private void jTextField9KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField9KeyTyped
 char car = evt.getKeyChar();
        int repite = new StringTokenizer(jTextField9.getText().toString(),".").countTokens() - 1;
        if ((car<'0' || car>'9') && car!='.') {            
            evt.consume();
        }
        if(car=='.'&& repite==1){
             evt.consume();
        }
        
    }//GEN-LAST:event_jTextField9KeyTyped

    private void jTextField6KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField6KeyTyped
 char car = evt.getKeyChar();
        int repite = new StringTokenizer(jTextField6.getText().toString(),".").countTokens() - 1;
        if ((car<'0' || car>'9') && car!='.') {            
            evt.consume();
        }
        if(car=='.'&& repite==1){
             evt.consume();
        }
        
    }//GEN-LAST:event_jTextField6KeyTyped

    private void jTextField7KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField7KeyTyped
 char car = evt.getKeyChar();
        int repite = new StringTokenizer(jTextField7.getText().toString(),".").countTokens() - 1;
        if ((car<'0' || car>'9') && car!='.') {            
            evt.consume();
        }
        if(car=='.'&& repite==1){
             evt.consume();
        }
        
    }//GEN-LAST:event_jTextField7KeyTyped

    private void jTextField10KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField10KeyTyped
 char car = evt.getKeyChar();
        int repite = new StringTokenizer(jTextField10.getText().toString(),".").countTokens() - 1;
        if ((car<'0' || car>'9') && car!='.') {            
            evt.consume();
        }
        if(car=='.'&& repite==1){
             evt.consume();
        }
        
    }//GEN-LAST:event_jTextField10KeyTyped

    private void jTable2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable2KeyReleased
        System.out.println(evt.getKeyCode());
        int code = evt.getKeyCode();
        if(code==38 || code==40){
            filapartida = jTable2.getSelectedRow();
           
                cargapartida();
         
                    
        }  
      if(code==127){
          borrar();
      }
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable2KeyReleased

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed
       float total1 = Float.valueOf(contototal);
        diagrama cron = new diagrama(prin, true,conex,id,total1);
        int xi = (this.getWidth()/2)-1150/2;
        int yi = (this.getHeight()/2)-720/2;
        cron.setBounds(xi, yi,1200, 720);
        cron.setVisible(true);
    }//GEN-LAST:event_jButton18ActionPerformed

    private void jButton19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton19ActionPerformed
        x=(this.getWidth()/2)-350;
             y=(this.getHeight()/2-250);
        tabpresupuesto  pres = new tabpresupuesto(prin, true,this, conex, tabu,x,y);
             
             pres.setBounds(x, y, 700,500);            
             pres.setVisible(true);        // TODO add your handling code here:
    }//GEN-LAST:event_jButton19ActionPerformed

    private void jButton21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton21ActionPerformed
        capitulos cap = new capitulos(prin, true, conex, id);
        int xi= (prin.getWidth()/2)-700/2;
        int yi = (prin.getHeight()/2)-600/2;
        cap.setBounds(xi, yi, 700, 600);
        cap.setVisible(true);
        busca();
        
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton21ActionPerformed

    private void jButton22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton22ActionPerformed
       importarpartidas  importa = new importarpartidas(prin, true, conex, id, this);
             x=(this.getWidth()/2)-350;
             y=(this.getHeight()/2-250);
             importa.setBounds(x, y, 700,500);            
             importa.setVisible(true);   
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton22ActionPerformed

    private void jTextField14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField14ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField14ActionPerformed
    public void borrarpres (){
            int op = JOptionPane.showConfirmDialog(this, "¿Desea Eliminar este Presupuesto?", "Borrar Presupuesto", JOptionPane.YES_NO_OPTION);
    if(op==JOptionPane.YES_OPTION){
            try {
                
                String borrarpartidas = "DELETE FROM mppres WHERE mpre_id='"+id+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+id+"')";
                String borrarmats = "DELETE FROM deppres WHERE mpre_id='"+id+"' OR mpre_id IN (SELECT id FROM mpres WHERE  mpres_id='"+id+"')";
                String borrarequips = "DELETE FROM dmpres WHERE mpre_id='"+id+"' OR mpre_id IN (SELECT id FROM mpres WHERE  mpres_id='"+id+"')";
                String borrarmates = "DELETE FROM mmpres WHERE mpre_id='"+id+"' OR mpre_id IN (SELECT id FROM mpres WHERE  mpres_id='"+id+"')";
                String borrarequipos = "DELETE FROM mepres WHERE mpre_id='"+id+"' OR mpre_id IN (SELECT id FROM mpres WHERE  mpres_id='"+id+"')";
                String borrarmanos = "DELETE FROM mmopres WHERE mpre_id='"+id+"' OR mpre_id IN (SELECT id FROM mpres WHERE  mpres_id='"+id+"')";
                String borrardmanos = "DELETE FROM dmoppres WHERE mpre_id='"+id+"' OR mpre_id IN (SELECT id FROM mpres WHERE  mpres_id='"+id+"')";
                String detvalus = "DELETE FROM dvalus WHERE mpre_id='"+id+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+id+"')";
                String mvalus = "DELETE FROM mvalus WHERE mpre_id='"+id+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+id+"')";
                String admppres = "DELETE FROM admppres WHERE mpre_id='"+id+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+id+"')";
                String pays = "DELETE FROM pays WHERE mpre_id='"+id+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+id+"')";
                String cmpres = "DELETE FROM cmpres WHERE mpre_id='"+id+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+id+"')";
                String rcppres = "DELETE FROM rcppres WHERE mpre_id='"+id+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+id+"')";
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
                JOptionPane.showMessageDialog(this, "El presupuesto ha sido eliminado!!");
                
               }
            // TODO add your handling code here:
            catch (SQLException ex) {
                Logger.getLogger(Presupuesto.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    
    }
private void jButton20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton20ActionPerformed
    
    borrarpres();
    int filas = jTable2.getRowCount();
    for(int i=0; i<filas;i++){
        mptabs.removeRow(i);
    }
    
    jTable2.setModel(mptabs);
    vaciacampos();
    vaciacampospres();
    x = (prin.getWidth()/2)-350;
    y = (prin.getHeight()/2)-250;
    tabpresupuesto tab = new tabpresupuesto(prin, true, this, conex, tabu,x,y);
                 
                tab.setBounds(x, y, 700, 500);
                tab.setVisible(true);
    // TODO add your handling code here:
}//GEN-LAST:event_jButton20ActionPerformed

private void jTextField18FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField18FocusGained
    jTextField18.setText("");// TODO add your handling code here:
}//GEN-LAST:event_jTextField18FocusGained

    private void jTextField15FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField15FocusGained
        
        
    }//GEN-LAST:event_jTextField15FocusGained

    private void jTextField15MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField15MouseClicked
        jTextField14.setText("");
        jTextField15.setText("");
        jCheckBox1.setSelected(false);
        jTextField16.setEnabled(false);
        jTextField16.setText("");
        jTextArea2.setText("");
        jTextField17.setText("");
        jTextField18.setText("");
        jTextField19.setText("");
        jTextField20.setText("");        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField15MouseClicked

    private void jButton26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton26ActionPerformed
        String numegrup = jTable2.getValueAt(filapartida, 1).toString();
        String numero1="";
        String consultanum = "SELECT numero FROM mppres WHERE numegrup="+numegrup+" AND "
                + "(mpre_id='"+id+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+id+"'))";
        String tipos = "Org";
       String actualiza="";
        try {
            Statement st = (Statement) conex.createStatement();
            ResultSet rst = st.executeQuery(consultanum);
            while(rst.next()){
                numero1=rst.getString(1);
            }
            int op = JOptionPane.showConfirmDialog(null, "¿Desea Modificar Valores de la Partida? Si/No" , "Modificar Partida", JOptionPane.YES_NO_OPTION);
            if(op==JOptionPane.YES_OPTION){
                String np="0", pres="0", descri="", unidad="", cantidad="";
                if(jCheckBox1.isSelected()){
                    np="1";
                    pres = jTextField16.getText().toString();                    
                }
                descri = jTextArea2.getText();
                unidad = jTextField17.getText();
                 cantidad = jTextField18.getText();
                 if(np.equals("1")){
                     tipos = "NP";
                      int cuenta=0;
                     String select="Select count(*) FROM mpres WHERE id ='"+pres+"'";
                     Statement stselect = (Statement) conex.createStatement();
                     ResultSet rstselect = stselect.executeQuery(select);
                     while(rstselect.next()){
                         cuenta = rstselect.getInt(1);
                     }
                     if(cuenta==0){
                         
                          String insertare = "INSERT INTO mpres "
                                  + "(id,nomabr,nombre,ubicac,fecini,fecfin,feccon,fecimp,porgam,porcfi,porimp,"
                                  + "poripa,porpre,poruti,codpro,codcon,parpre,nrocon,nroctr,fecapr,nrolic,status,"
                                  + "mpres_id,memo,timemo,fecmemo,seleccionado,fecharegistro)"
                                            + "SELECT '"+pres+"', nomabr, nombre, ubicac, fecini,"
                                            + "fecfin, feccon, fecimp, porgam, porcfi, porimp, poripa,"
                                            + "porpre, poruti, codpro, codcon, parpre, nrocon, nroctr, fecapr,"
                                            + "nrolic, 1, '"+id+"',memo,timemo, fecmemo, 0, NOW()  FROM "
                                            + "mpres WHERE id='"+id+"'";

                    System.out.println("Inserta Presupuesto Adicional: "+insertar);
                    Statement insert = (Statement) conex.createStatement();
                    insert.execute(insertare);
                     }
                    actualiza = "UPDATE mppres SET descri='"+descri+"', unidad='"+unidad+"', "
                                 + "nropresupuesto='"+pres+"', tipo ='"+tipos+"', "
                         + "cantidad="+cantidad+", mpre_id='"+pres+"' WHERE numegrup="+numegrup+" AND "
                                 + "(mpre_id='"+id+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+id+"'))";
                 }else{
                     actualiza = "UPDATE mppres SET descri='"+descri+"', unidad='"+unidad+"', "
                                 + "nropresupuesto='"+pres+"', tipo ='"+tipos+"', "
                         + "cantidad="+cantidad+", mpre_id='"+id+"' WHERE numegrup="+numegrup+" AND "
                                 + "(mpre_id='"+id+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+id+"'))";
                 }
                 
                 Statement sts = (Statement) conex.createStatement();
                 sts.execute(actualiza);
                 JOptionPane.showMessageDialog(null, "Se han guardado los valores para la partida");
                 buscapartida();
                 cargartotal();
            }
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "No se han guardado los valores para la partida");
            Logger.getLogger(Presupuesto.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton26ActionPerformed

    private void jButton27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton27ActionPerformed
       if(filapartida>0){
       
         int n = mptabs.getRowCount();
         int ultima = 0;
      Vector data = mptabs.getDataVector();
       String[] coldata = new String[mptabs.getRowCount()], grupo = new String[mptabs.getRowCount()];
       
       int [] numeros = new int[mptabs.getRowCount()];
       
     
        
        for (int i=0; i<coldata.length; i++) {
        coldata[i] = ((Vector)data.get(i)).get(2).toString();
        numeros[i] = Integer.valueOf(((Vector)data.get(i)).get(0).toString());
        grupo[i] = ((Vector)data.get(i)).get(4).toString();
        
    }    
        
        
            String auxcoldata = coldata[filapartida-1];
            int auxnumeros = numeros[filapartida-1];
            String auxgrupos = grupo[filapartida-1];
            coldata[filapartida-1]=coldata[filapartida];
            numeros[filapartida-1]=numeros[filapartida];
            grupo[filapartida-1]=grupo[filapartida];
             coldata[filapartida]=auxcoldata;
            numeros[filapartida]=auxnumeros;
            grupo[filapartida]=auxgrupos;
        
       filapartida=filapartida-1;
          
        
                  
      reordena1 reor = new reordena1(conex, coldata, numeros, id, this);
      reor.start();
    
            
           
       
        }else{
           JOptionPane.showMessageDialog(this, "No se puede subir mas la partida");
           jTable2.getSelectionModel().setSelectionInterval(filapartida, filapartida);
              Rectangle r = jTable2.getCellRect(jTable2.getSelectedRow(),jTable2.getSelectedColumn(), false);
             jTable2.scrollRectToVisible(r);
       }
        
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton27ActionPerformed

    private void jButton28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton28ActionPerformed
                // TODO add your handling code here:
        int op = JOptionPane.showConfirmDialog(this,"Es un Proceso Delicado ¿Desea Reordenar las Partidas? ", "Reordenar Partidas", JOptionPane.YES_NO_OPTION);
         int n = mptabs.getRowCount();
         int ultima = 0;
      Vector data = mptabs.getDataVector();
       String[] coldata = new String[mptabs.getRowCount()], grupo = new String[mptabs.getRowCount()];
       
       int [] numeros = new int[mptabs.getRowCount()];
       
       if(op==JOptionPane.YES_OPTION){
           int opcion=JOptionPane.showConfirmDialog(this, "Proceso delicado ¿Esta seguro?","Reodenar Partidas", JOptionPane.YES_NO_OPTION);
           if(opcion==JOptionPane.YES_OPTION){
        for (int i=0; i<coldata.length; i++) {
        coldata[i] = ((Vector)data.get(i)).get(2).toString();
        numeros[i] = Integer.valueOf(((Vector)data.get(i)).get(0).toString());
        
        String grup = ((Vector)data.get(i)).get(4).toString();
        if(grup.equals("OE") || grup.equals("OA") || grup.equals("OC")){
            grup="NP";
        }
        grupo[i] = grup;
        
    }
       
       
        
        
        for(int k=0;k<coldata.length;k++) {
            for(int f=0;f<coldata.length-1-k;f++) {
                if (!(grupo[f].equals("Org")) && (grupo[f+1].equals("Org")) || (!(grupo[f].equals("NP")) && (grupo[f+1].equals("NP") ) && !(grupo[f].equals("Org")))){
                    String auxnota;
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
                    ultima++;
                }
            }
           /* for(int h=ultima; h<coldata.length;h++){
                
            }*/
        }
                  
      reordena reor = new reordena(conex, coldata, numeros, id, this);
      reor.start();
       }
       }
        
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton28ActionPerformed

    private void jButton29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton29ActionPerformed
      int n = mptabs.getRowCount();
        if(filapartida<n-1){
       
        
         int ultima = 0;
      Vector data = mptabs.getDataVector();
       String[] coldata = new String[mptabs.getRowCount()], grupo = new String[mptabs.getRowCount()];
       
       int [] numeros = new int[mptabs.getRowCount()];
       
     
        
        for (int i=0; i<coldata.length; i++) {
        coldata[i] = ((Vector)data.get(i)).get(2).toString();
        numeros[i] = Integer.valueOf(((Vector)data.get(i)).get(0).toString());
        grupo[i] = ((Vector)data.get(i)).get(4).toString();
        
    }    
        
        
            String auxcoldata = coldata[filapartida+1];
            int auxnumeros = numeros[filapartida+1];
            String auxgrupos = grupo[filapartida+1];
            coldata[filapartida+1]=coldata[filapartida];
            numeros[filapartida+1]=numeros[filapartida];
            grupo[filapartida+1]=grupo[filapartida];
             coldata[filapartida]=auxcoldata;
            numeros[filapartida]=auxnumeros;
            grupo[filapartida]=auxgrupos;
            filapartida=filapartida+1;
          
      reordena1 reor = new reordena1(conex, coldata, numeros, id, this);
      reor.start();
        }else{
           JOptionPane.showMessageDialog(this, "No se puede bajar mas la partida");
           jTable2.getSelectionModel().setSelectionInterval(filapartida, filapartida);
              Rectangle r = jTable2.getCellRect(jTable2.getSelectedRow(),jTable2.getSelectedColumn(), false);
             jTable2.scrollRectToVisible(r);
       }
        
        
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton29ActionPerformed

    private void jButton30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton30ActionPerformed
        JTextField campo = new JTextField();
         campo.addKeyListener(new KeyAdapter()
{
            @Override
   public void keyTyped(KeyEvent e)
   {
      char caracter = e.getKeyChar();

      // Verificar si la tecla pulsada no es un digito
      if(((caracter < '0') ||
         (caracter > '9')) &&
         (caracter != '\b' /*corresponde a BACK_SPACE*/))
      {
         e.consume();  // ignorar el evento de teclado
      }
   }
});
        JOptionPane.showMessageDialog(null, campo, "Número de Partida",JOptionPane.QUESTION_MESSAGE);
       int enc=0;
            int numpartida1 = Integer.parseInt(campo.getText().toString());
            for(int i=0;i<mptabs.getRowCount();i++){
                if(numpartida1==Integer.parseInt(jTable2.getValueAt(i, 1).toString())){
                    numpartida1=i;
                    i=mptabs.getRowCount();
                    enc=1;
                }
            }
        int numpartida2 = filapartida;
        
        Vector data = mptabs.getDataVector();
        String[] coldata = new String[mptabs.getRowCount()], auxcoldata = new String[mptabs.getRowCount()];
        int [] numeros = new int[mptabs.getRowCount()], auxnumeros = new int[mptabs.getRowCount()];
        for (int i=0; i<coldata.length; i++) {
            coldata[i] = ((Vector)data.get(i)).get(2).toString();
            auxcoldata[i] = ((Vector)data.get(i)).get(2).toString();
            numeros[i] = Integer.valueOf(((Vector)data.get(i)).get(0).toString());
            auxnumeros[i] = Integer.valueOf(((Vector)data.get(i)).get(0).toString());
        }    
        
        if(numpartida1<numpartida2){ //LO VOY MOVER PARA ARRIBA
            coldata[numpartida1]=coldata[numpartida2];
            numeros[numpartida1]=numeros[numpartida2];
            for(int i=numpartida1;i<numpartida2;i++){
                coldata[i+1]=auxcoldata[i];
                numeros[i+1]=auxnumeros[i];
            }
        }else
        {
            if(numpartida1<mptabs.getRowCount()){
            coldata[numpartida1]=coldata[numpartida2];
            numeros[numpartida1]=numeros[numpartida2];
            for(int i=numpartida1;i>numpartida2;i--){
                coldata[i-1]=auxcoldata[i];
                numeros[i-1]=auxnumeros[i];
            }
            }
        }
        if(numpartida1<mptabs.getRowCount()&&enc==1){
         reordena reor = new reordena(conex, coldata, numeros, id, this);
      reor.start();
        }else{
                JOptionPane.showMessageDialog(this, "El número de partida ingresado no existe");
            }
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton30ActionPerformed

    private void jButton23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton23ActionPerformed

        int op = JOptionPane.showConfirmDialog(this, "¿Desea Eliminar las partidas este Presupuesto?", "Borrar Partidas", JOptionPane.YES_NO_OPTION);
    if(op==JOptionPane.YES_OPTION){
            try {
                
                String borrarpartidas = "DELETE FROM mppres WHERE mpre_id='"+id+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+id+"')";
                String borrarmats = "DELETE FROM deppres WHERE mpre_id='"+id+"' OR mpre_id IN (SELECT id FROM mpres WHERE  mpres_id='"+id+"')";
                String borrarequips = "DELETE FROM dmpres WHERE mpre_id='"+id+"' OR mpre_id IN (SELECT id FROM mpres WHERE  mpres_id='"+id+"')";
                String borrarmano = "DELETE FROM deppres WHERE mpre_id='"+id+"' OR mpre_id IN (SELECT id FROM mpres WHERE  mpres_id='"+id+"')";
                String borrarmates = "DELETE FROM mmpres WHERE mpre_id='"+id+"' OR mpre_id IN (SELECT id FROM mpres WHERE  mpres_id='"+id+"')";
                String borrarequipos = "DELETE FROM mepres WHERE mpre_id='"+id+"' OR mpre_id IN (SELECT id FROM mpres WHERE  mpres_id='"+id+"')";
                String borrarmanos = "DELETE FROM mmopres WHERE mpre_id='"+id+"' OR mpre_id IN (SELECT id FROM mpres WHERE  mpres_id='"+id+"')";
                String detvalus = "DELETE FROM dvalus WHERE mpre_id='"+id+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+id+"')";
                String mvalus = "DELETE FROM mvalus WHERE mpre_id='"+id+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+id+"')";
                String admppres = "DELETE FROM admppres WHERE mpre_id='"+id+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+id+"')";
                String pays = "DELETE FROM pays WHERE mpre_id='"+id+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+id+"')";
                String cmpres = "DELETE FROM cmpres WHERE mpre_id='"+id+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+id+"')";
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
                stppres.execute(borrarmano);
                stppres.execute(borrarmates);
                stppres.execute(borrarequipos);
                stppres.execute(borrarmanos);
                stppres.execute(detvalus);
                stppres.execute(mvalus);
                stppres.execute(admppres);
                stppres.execute(pays);
                stpres.execute(cmpres);
                stpresNP.execute(borrarNP);
                JOptionPane.showMessageDialog(this, "Las partidas del presupuesto han sido borradas");
                buscapartida();
                cargartotal();
               }
            catch (SQLException ex) {
                Logger.getLogger(Presupuesto.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
        
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton23ActionPerformed

    private void jButton31ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton31ActionPerformed
        this.setVisible(false);
        
        
    }//GEN-LAST:event_jButton31ActionPerformed

    private void jButton24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton24ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton24ActionPerformed

private void jButton25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton25ActionPerformed
     String numegrup = jTable2.getValueAt(filapartida, 0).toString();
    String mpres = consultapresupuesto(numegrup);
    recalcula recal = new recalcula(prin, true, conex, id,this);
       int xi = (this.getWidth()/2)-350/2;
       int yi = (this.getHeight()/2)-450/2;
       recal.setBounds(xi, yi, 370, 470);
       recal.setVisible(true);
    
    // TODO add your handling code here:
}//GEN-LAST:event_jButton25ActionPerformed

public String consultapresupuesto(String numero){
    String partidapres="Select mpre_id FROM mppres WHERE numegrup="+numero+" AND (mpre_id='"+id+"' OR mpre_id IN "
            + "(SELECT id FROM mpres WHERE mpres_id='"+id+"'))";
       String mpres=""; 
        try {
            Statement stpres = (Statement) conex.createStatement();
            ResultSet rstpres = stpres.executeQuery(partidapres);
            while(rstpres.next()){
                mpres=rstpres.getString("mpre_id");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Presupuesto.class.getName()).log(Level.SEVERE, null, ex);
        }
        return mpres;
}
private void jButton33ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton33ActionPerformed
    
    String numegrup = jTable2.getValueAt(filapartida, 0).toString();
    String mpres = consultapresupuesto(numegrup);
    matrizmaterialespres materiales = new matrizmaterialespres(prin, true, conex, mpres,prin);
        int xi = (this.getWidth()/2)-700/2;
        int yi = (this.getHeight()/2)-500/2;
        materiales.setBounds(xi, yi, 700, 500);       
        materiales.setVisible(true);
      try {
            buscapartida();
        } catch (SQLException ex) {
            Logger.getLogger(Presupuesto.class.getName()).log(Level.SEVERE, null, ex);
        }
      cargartotal();
    // TODO add your handling code here:
}//GEN-LAST:event_jButton33ActionPerformed

private void jButton34ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton34ActionPerformed

    String numegrup = jTable2.getValueAt(filapartida, 0).toString();
    String mpres = consultapresupuesto(numegrup);
    matrizequipospres equipos = new matrizequipospres(prin, true, conex, mpres,prin);
        int xi = (this.getWidth()/2)-700/2;
        int yi = (this.getHeight()/2)-500/2;
        equipos.setBounds(xi, yi, 700, 500);
        equipos.setVisible(true);
    try {
            buscapartida();
        } catch (SQLException ex) {
            Logger.getLogger(Presupuesto.class.getName()).log(Level.SEVERE, null, ex);
        }
      cargartotal();
    // TODO add your handling code here:
}//GEN-LAST:event_jButton34ActionPerformed

private void jButton35ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton35ActionPerformed
String numegrup = jTable2.getValueAt(filapartida, 0).toString();
    String mpres = consultapresupuesto(numegrup);
    matrizmanopres mano = new matrizmanopres(prin, true, conex, mpres,prin);
        int xi = (this.getWidth()/2)-700/2;
        int yi = (this.getHeight()/2)-500/2;
        mano.setBounds(xi, yi, 700, 500);
        mano.setVisible(true);
        try {
            buscapartida();
        } catch (SQLException ex) {
            Logger.getLogger(Presupuesto.class.getName()).log(Level.SEVERE, null, ex);
        }
      cargartotal();
}//GEN-LAST:event_jButton35ActionPerformed

private void jButton36ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton36ActionPerformed
        jTextField1.setText("");
        busca();
}//GEN-LAST:event_jButton36ActionPerformed

    private void jTextField15KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField15KeyPressed
 if (evt.getKeyCode() == KeyEvent.VK_TAB) {
            if (evt.getModifiers() == KeyEvent.SHIFT_MASK) {
                ((javax.swing.JTextArea) evt.getSource()).transferFocusBackward();
            } else {
                ((javax.swing.JTextArea) evt.getSource()).transferFocus();
            }
            evt.consume();
        }        // TODO add your handling code here:        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField15KeyPressed

    private void jButton32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton32ActionPerformed
String nume=jTable2.getValueAt(filapartida, 1).toString();      
        String presu="SELECT mpre_id FROM mppres WHERE (mpre_id='"+id+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+id+"'))"
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
        
       
        reporteapu1 apu = new reporteapu1(null, false, conex, mpres, nume);
        int xy = (this.getWidth()/2)-450/2;
        int yy = (this.getHeight()/2)-300/2;
        apu.setBounds(xy,yy, 450,300);
        apu.setVisible(true);
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton32ActionPerformed

    private void jButton37ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton37ActionPerformed
        double totalpres = this.gettotal();
        reportepresupuesto report = new reportepresupuesto(prin, false, conex, id,totalpres);
         int xi = (this.getWidth()/2)-700/2;
        int yi = (this.getHeight()/2)-450/2;
         report.setBounds(xi, yi, 700, 450);
        report.setVisible(true);
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton37ActionPerformed
    public void vaciacampospres()
    {
        jTextField5.setText("");
        jTextField6.setText("");
        jTextField7.setText("");
        jTextField8.setText("");
        jTextField9.setText("");
        jTextField10.setText("");
    }
    
    public void ver(){
        String cuenta = null ;
        int contar =0;
        String mpre=null;
        String verifica = "SELECT mpres_id FROM mpres WHERE id='"+id+"'";
        try {
            Statement sts = (Statement) conex.createStatement();
            ResultSet rsts = sts.executeQuery(verifica);
            while(rsts.next()){
                if(rsts.getObject(1)!=null){
                mpre = rsts.getObject(1).toString();
                }
            }
            if(mpre!=null){
                id = mpre;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Presupuesto.class.getName()).log(Level.SEVERE, null, ex);
        }
        String sql = "SELECT count(id) FROM mppres WHERE mpre_id='"+id+"' OR mpre_id IN "
                + "(SELECT id from mpres where mpres_id ='"+id+"' GROUP BY id) " ;
        try {
            Statement st = (Statement) conex.createStatement();
            ResultSet rst = st.executeQuery(sql);
            
            while(rst.next()){
                cuenta = rst.getObject(1).toString();                
            }
            contar = Integer.parseInt(cuenta);
            
        } catch (SQLException ex) {
            Logger.getLogger(Presupuesto.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Partida part = new Partida(prin, true, conex, id, prin,1, contar, idpartida, numpartida, this, jTextField13.getText().toString());
        x = (prin.getWidth()/2)-400;
        y = (prin.getHeight()/2)-300;
        part.setBounds(x, y, 800, 520);
        part.setVisible(true);
        cargartotal();
        try {
            buscapartida();
        } catch (SQLException ex) {
            Logger.getLogger(Presupuesto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void busca()
    {
        Boolean enc;
        String busqueda = jTextField1.getText().toString();
     
        DefaultTableModel mat = new DefaultTableModel() {
        @Override
        public boolean isCellEditable (int fila, int columna) {
          if(columna==0) {
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
     
               
               jTable1.setModel(mat);
       
        try {
            Statement s = (Statement) conex.createStatement();
            ResultSet rs = s.executeQuery("SELECT codicove, descri, numero, numegrup, precasu, precunit, mbdat_id FROM Mptabs m WHERE m.mtabus_id = '"+tabu+"' AND status=1  AND (codicove LIKE'%"+busqueda+"%' || descri LIKE '%"+busqueda+"%') ORDER BY numegrup");
            
            ResultSetMetaData rsMd = (ResultSetMetaData) rs.getMetaData();
            int cantidadColumnas = rsMd.getColumnCount()+1;
             mat.addColumn("");
             for (int i = 2; i <= cantidadColumnas; i++) {
                 mat.addColumn(rsMd.getColumnLabel(i-1));
            }
             
             while (rs.next()) {
                Object[] fila = new Object[cantidadColumnas];
                enc=false;
                    for(int j=0;j<auxcont;j++){
                         if(rs.getObject(3).toString().equals(auxpart[j])){
                                  enc=true;
                         }
                    }
                Object obj = Boolean.valueOf(enc);
                fila[0]= obj;
                for (int i = 1; i < cantidadColumnas; i++) {
                    fila[i]=rs.getObject(i);
                }
                mat.addRow(fila);
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
         jTable1.getColumnModel().getColumn(3).setMaxWidth(0);

             jTable1.getColumnModel().getColumn(3).setMinWidth(0);

             jTable1.getTableHeader().getColumnModel().getColumn(3).setMaxWidth(0);

             jTable1.getTableHeader().getColumnModel().getColumn(3).setMinWidth(0);
             
              jTable1.getColumnModel().getColumn(7).setMaxWidth(0);

             jTable1.getColumnModel().getColumn(7).setMinWidth(0);

             jTable1.getTableHeader().getColumnModel().getColumn(7).setMaxWidth(0);

             jTable1.getTableHeader().getColumnModel().getColumn(7).setMinWidth(0);
               cambiarcabecera();
    }
        
    public void calculapartida(String nume, String presupuesto, int edita){
        float auxcontotal;
        float admini=porcgad;
        float utili=porcutil;
        float financiero= Float.valueOf(jTextField8.getText().toString());
        float impuestos = Float.valueOf(jTextField9.getText().toString());
        float redondeado;
        
        contototal = contmat+contequipo+contmano;
        System.out.println("contotal "+contototal);
        auxcontotal = contototal;
        
        admini = auxcontotal * admini/100;
        System.out.println("admin "+admini);
        utili = (auxcontotal+admini) * utili/100;
        System.out.println("util "+utili);
        auxcontotal = contototal + admini + utili;
        System.out.println("sumado "+auxcontotal);
        financiero = auxcontotal * financiero/100;
        impuestos = auxcontotal * impuestos/100;
        System.out.println("financiero "+financiero);
        System.out.println("impuesto "+impuestos);
        contototal = auxcontotal + impuestos + financiero;
        System.out.println("contotal impu fin "+contototal);
        auxcontotal = (float) (Math.rint((contototal+0.000001)*100)/100);
        redondeado = (float) Math.rint(auxcontotal);
        
        jTextField19.setText(String.valueOf(redondeado));
        if(insertar==1){ 
        String consulta = "UPDATE mppres SET precunit="+auxcontotal+", precasu="+redondeado+" WHERE numero='"+nume+"' "
                + "AND (mpre_id='"+presupuesto+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+presupuesto+"'))";
        try {
            Statement stmt = (Statement) conex.createStatement();
            stmt.execute(consulta);
        } catch (SQLException ex) {
            Logger.getLogger(Presupuesto.class.getName()).log(Level.SEVERE, null, ex);
        }
        }
    }
    public void agregaequipo (int edita){
            String cantidad, precio;
            int cuantos=0;
            float valor=0;
            String codiequipo, mtabu = "", descri, deprecia;
            String sql = "Select me.descri, me.deprecia, me.precio, me.status,"
                + " me.id, me.precio, dm.cantidad, mp.codicove as codicove"
                + " FROM metabs as me, deptabs as dm, mptabs as mp "
                + "WHERE dm.mtabus_id='"+tabu+"' AND dm.numero="+numero+" "
                + "AND dm.metab_id=me.id AND dm.mtabus_id=me.mtabus_id AND "
                + "mp.numero="+numero+" GROUP BY me.id";
        
        try {
            Statement st = (Statement) conex.createStatement();
            ResultSet rst = st.executeQuery(sql);
            
            while(rst.next()){
                cantidad = rst.getObject(7).toString();
                precio = rst.getObject(3).toString();
                 codiequipo = rst.getObject(5).toString();
               descri = rst.getObject(1).toString();
               deprecia = rst.getObject(2).toString();
               jTextField15.setText(rst.getString("codicove"));
               if(Float.valueOf(deprecia)!=0){
                valor += Float.valueOf(precio)*Float.valueOf(cantidad)* Float.valueOf(deprecia);
               }else{
                   valor += Float.valueOf(precio)*Float.valueOf(cantidad);
               }
                Statement inserta = (Statement) conex.createStatement();
                String sqlinsert;
                if(insertar==1){
               if(edita==0) {
                    sqlinsert = "INSERT INTO deppres (mpre_id, mppre_id, mepre_id, numero"
                            + ", cantidad, precio, status) VALUES "
                            + "('"+id+"', '"+jTextField15.getText().toString()+"',"
                            + "'"+codiequipo+"',"+nuevo+", "+cantidad+", "+precio+", '1')";
                }
               else {
                    sqlinsert =  "UPDATE deppres set cantidad="+cantidad+", precio="+precio+" "
                            + " WHERE mpre_id='"+id+"' AND "
                            + "mppre_id='"+jTextField15.getText().toString()+"' AND mepre_id = '"+codiequipo+"'";
                }
               System.out.println(sqlinsert);
               inserta.execute(sqlinsert);
                Statement cuenta = (Statement) conex.createStatement();
                
                String sqlcuenta = "SELECT count(id) FROM mepres WHERE mpre_id='"+id+"' AND id='"+codiequipo+"'";
                ResultSet rscuenta = cuenta.executeQuery(sqlcuenta);
                while(rscuenta.next()){
                    cuantos = Integer.parseInt(rscuenta.getObject(1).toString());
                    
                }
                if(cuantos==0){
                    Statement equip = (Statement) conex.createStatement();
                    String insertequipo = "INSERT INTO mepres (mpre_id, id, descri,deprecia, precio, "
                            + " status)"
                            + "VALUES('"+id+"', '"+codiequipo+"', '"+descri+"', "+deprecia+""
                            + ", "+precio+", '1')";
                    equip.execute(insertequipo);
                }
            }
            }
            contequipo = valor;
            System.out.println("contaquipo "+contequipo);
            contequipo = valor / Float.valueOf(rendimi);
            contequipo = (float) (Math.rint((contequipo+0.000001)*100)/100);
            
            System.out.println("contequipo con rendimi "+contequipo);
        } catch (SQLException ex) {
            Logger.getLogger(Presupuesto.class.getName()).log(Level.SEVERE, null, ex);
        }
        }
    
//*********************************************************************************    
        public void agregamano (int edita){
           String cantidad, salario , bono ="", subsidi = "";
           int cuantos=0, cantidades=0;
           float prestaciones = porcpre;
           float valor = 0, bonos , subsid;
           String codimano, mtabu = "", descri, deprecia = "";
           
      
          
            String sql = "Select mo.descri, mo.salario, mo.bono, mo.subsid,"
                    + "mo.status, mo.id, mo.salario, do.cantidad, do.mptab_id "
                    + "FROM mmotabs "
                    + "as mo, dmoptabs as do WHERE do.mtabus_id='"+tabu+"' "
                    + "AND do.numero="+numero+" AND do.mmotab_id=mo.id "
                    + "AND do.mtabus_id=mo.mtabus_id GROUP BY mo.id";
        try {
            Statement st = (Statement) conex.createStatement();
            ResultSet rst = st.executeQuery(sql);
            
            while(rst.next()){
                cantidad = rst.getObject(8).toString();
                salario = rst.getObject(2).toString();
                 codimano = rst.getObject(6).toString();
               descri = rst.getObject(1).toString();
               bono = rst.getObject(3).toString();
               subsidi = rst.getObject(4).toString(); 
               jTextField15.setText(rst.getString(9));
               cantidades += Float.valueOf(cantidad);
               valor += Float.valueOf(cantidad)*Float.valueOf(salario);
               
                Statement inserta = (Statement) conex.createStatement();
                String sqlinsert;
                if(insertar==1){
               if(edita==0) {
                    sqlinsert = "INSERT INTO dmoppres (mpre_id, mppre_id, mmopre_id, numero"
                            + ", cantidad, bono, salario, subsidi, status) VALUES "
                            + "('"+id+"', '"+jTextField15.getText().toString()+"',"
                            + "'"+codimano+"',"+nuevo+", "+cantidad+", "+bono+","+salario+", "+subsidi+", '1')";
                }
               else {
                    sqlinsert =  "UPDATE dmoppres set cantidad="+cantidad+", salario="+salario+", bono ="+bono+", subsidi="+subsidi+" WHERE mpre_id='"+id+"' AND "
                            + "mppre_id='"+jTextField15.getText().toString()+"' AND mmopre_id = '"+codimano+"'";
                }
               inserta.execute(sqlinsert);
                Statement cuenta = (Statement) conex.createStatement();
                
                String sqlcuenta = "SELECT count(id) FROM mmopres WHERE mpre_id='"+id+"' AND id='"+codimano+"'";
                ResultSet rscuenta = cuenta.executeQuery(sqlcuenta);
                while(rscuenta.next()){
                    cuantos = Integer.parseInt(rscuenta.getObject(1).toString());
                    
                }
                if(cuantos==0){
                    Statement mano = (Statement) conex.createStatement();
                    String insertmano= "INSERT INTO mmopres (mpre_id, id, descri, salario, bono, subsid,"
                            + " status)"
                            + "VALUES('"+id+"', '"+codimano+"', '"+descri+"', "+salario+""
                            + ", "+bono+","+subsidi+", '1')";
                    mano.execute(insertmano);
                }
            }
            }
            contmano = valor;
           
            prestaciones = valor * prestaciones/100;
            System.out.println("valormano "+contmano);
            if(bono!=null && !"".equals(bono)) {
                bonos = Float.valueOf(cantidades) * Float.valueOf(bono);
            }
            else {
                bonos=0;
            }
            if(subsidi!=null && !"".equals(subsidi)) {
                subsid = Float.valueOf(cantidades) * Float.valueOf(subsidi);
            }
            else {
                subsid=0;
            }
            valor = contmano + prestaciones + bonos +subsid;
            System.out.println("prestaciones "+prestaciones+" bonos "+bonos+" subsid "+subsid);
            contmano = valor/Float.valueOf(rendimi);
            System.out.println("contmanofinal "+contmano);
            
        } catch (SQLException ex) {
            Logger.getLogger(Presupuesto.class.getName()).log(Level.SEVERE, null, ex);
        }
        }
//******************************************************************************  
        public void agregarmat(int edita){
           String cantidad, precio ;
           int cuantos=0;
           float valor=0;
           String codimate, mtabu = "", descri, desperdi, unidad;
        
        
          
        String sql = "Select mm.descri, mm.desperdi, mm.precio, mm.unidad, mm.status,"
                + " mm.id, mm.precio, dm.cantidad,mp.codicove FROM mmtabs as mm,"
                + " dmtabs as dm, mptabs as mp "
                + "WHERE dm.mtabus_id='"+tabu+"' AND mm.mtabus_id=dm.mtabus_id AND mp.mtabus_id=mm.mtabus_id "
                + "AND dm.numepart=mp.numero"
                + " AND mp.numero="+numero+" AND dm.mmtab_id=mm.id GROUP BY mm.id";
        System.out.println(sql);
        try {
            Statement st = (Statement) conex.createStatement();
            ResultSet rst = st.executeQuery(sql);
            
            while(rst.next()){
                cantidad = rst.getObject(8).toString();
                precio = rst.getObject(3).toString();
                 codimate = rst.getObject(6).toString();
               descri = rst.getObject(1).toString();
               desperdi = rst.getObject(2).toString();
               unidad = rst.getObject(4).toString();
               jTextField15.setText(rst.getString(9)); //codicove
                Statement inserta = (Statement) conex.createStatement();
               String sqlinsert;

               valor += (Float.valueOf(precio)+(Float.valueOf(precio)* (Float.valueOf(desperdi)/100)))* Float.valueOf(cantidad);
               
               if(insertar==1){
               if(edita==0) {
                    sqlinsert = "INSERT INTO dmpres (mpre_id, mppre_id, mmpre_id, numepart"
                            + ", cantidad, precio, status) VALUES "
                            + "('"+id+"', '"+jTextField15.getText().toString()+"',"
                            + "'"+codimate+"',"+nuevo+", "+cantidad+", "+precio+", '1')";
                }
               else {
                    sqlinsert =  "UPDATE dmpres set cantidad="+cantidad+", precio="+precio+""
                            + " WHERE mpre_id='"+id+"' AND "
                            + "mppre_id='"+jTextField15.getText().toString()+"' AND mmpre_id = '"+codimate+"'";
                }
               inserta.execute(sqlinsert);
               
                
                Statement cuenta = (Statement) conex.createStatement();
                
                String sqlcuenta = "SELECT count(id) FROM mmpres WHERE mpre_id='"+id+"' AND id='"+codimate+"'";
                ResultSet rscuenta = cuenta.executeQuery(sqlcuenta);
                while(rscuenta.next()){
                    cuantos = Integer.parseInt(rscuenta.getObject(1).toString());
                    
                }
                if(cuantos==0){
                    Statement mat = (Statement) conex.createStatement();
                    String insertmat = "INSERT INTO mmpres (mpre_id, id, descri,desperdi, precio, "
                            + "unidad, status)"
                            + "VALUES('"+id+"', '"+codimate+"', '"+descri+"', "+desperdi+""
                            + ", "+precio+", '"+unidad+"', '1')";
                    mat.execute(insertmat);
                }
                }
            }
            contmat = valor;
            System.out.println("contmat "+contmat);
        } catch (SQLException ex) {
            Logger.getLogger(Presupuesto.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
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
    private javax.swing.JButton jButton33;
    private javax.swing.JButton jButton34;
    private javax.swing.JButton jButton35;
    private javax.swing.JButton jButton36;
    private javax.swing.JButton jButton37;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
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
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
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
    private javax.swing.JTextField jTextField20;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables
}