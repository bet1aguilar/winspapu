
package presupuestos;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSetMetaData;
import com.mysql.jdbc.Statement;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerListModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import winspapus.tablas.ColumnGroup;
import winspapus.tablas.GroupableTableHeader;
import winspapus.tablas.RenderCelda;
/**
 *
 * @author Betmart
 */
public final class diagrama extends javax.swing.JDialog {

    /** A return status code - returned if Cancel button has been pressed */
    public static final int RET_CANCEL = 0;
    /** A return status code - returned if OK button has been pressed */
    public static final int RET_OK = 1;
    private Connection conex;
     final long mil = 86400000 ; 
     JScrollBar scroller;
    String partida, pres;
    String partidaselect;
     int cont=0;
    int monto = 0;
    float totalpres=0;
      int numdias=0, numsemanas=0, numeses=0;
    diagrama dia = this;
    String fecha;
    Date date= null;
     JTable tabla=null;
    int filafase;
    String [] semanas, vecmes, vecanio, vecmeses,vecmesanio;
    Date fechaini=null, fechafin=null;
    float costo=0, costolapso=0;
    float totaldias=0;
    int rangos = 0;
    int lapso=0;
    JScrollPane scroll;
    String[] diasemana, semanames;
    String[] fechasdias, semanaanio;
    String numpartida=null;
    private ResultSet rst;
    Frame prin;
    float dias;
    Object [][] matriz;
    private String[] partidas;
     float semana = 0, meses=0, anios=0;
      DefaultTableModel dm ;
    private Object metabs;
    /** Creates new form diagrama */
    public diagrama(java.awt.Frame parent, boolean modal, Connection conex, String pres, float total) {
        super(parent, modal);
        initComponents();
        this.totalpres = total;
        this.date = new Date();
        prin=parent;
       jComboBox2.setVisible(false);
      jLabel20.setVisible(false);
        this.pres = pres;
        this.conex = conex;
        jTextField11.setText(String.valueOf(total));
         jTable1.setOpaque(true);
    jTable1.setShowHorizontalLines(true);
    jTable1.setShowVerticalLines(false);
    jTable1.getTableHeader().setSize(new Dimension(25,60));
    jTable1.getTableHeader().setPreferredSize(new Dimension(30,65));
    jTable1.setRowHeight(25);
        buscapartida();
        creamodelo();
        iniciatabla();
        hacetabla1();
        hacerscroll();
        calculatotaldias();
        
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

    
    public final void buscapartida()
    {
        
        String nombre="", conta="", pro="";
        String con = "SELECT mp.nombre, mc.nombre, mpr.nombre FROM mpres as mp, mconts as mc, mprops as mpr"
                + " WHERE mp.id = '"+pres+"' AND mp.codpro = mpr.id AND mp.codcon = mc.id";
        try {
            Statement stm = (Statement) conex.createStatement();
            ResultSet rstm = stm.executeQuery(con);
            while(rstm.next()){
                nombre = rstm.getString(1);
                conta = rstm.getString(2);
                pro = rstm.getString(3);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(diagrama.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        jTextField11.setText(String.valueOf(totalpres));
        jTextField1.setText(pres);
        jTextField2.setText(conta);
        int reg=0, i=0;
        float prec, cant;
        String partida1, descri="", rendimi="", cantidad="",precunit="", id="";
        String consulta = "SELECT numero, numegrup, id, descri, tipo, rendimi, cantidad, precunit FROM mppres WHERE mpre_id='"+pres+"' OR mpre_id IN "
                + "         (SELECT id from mpres where mpres_id ='"+pres+"' GROUP BY id) ORDER BY numegrup";
        try {
            Statement st = (Statement) conex.createStatement();
            rst = st.executeQuery(consulta);
            rst.last();
            reg = rst.getRow();
            rst.first();
            partidas = new String[reg];
                do
                {
                    if(i==0){
                        descri = rst.getString("descri");
                        rendimi = rst.getString("rendimi");
                        cantidad = rst.getString("cantidad");
                        precunit = rst.getString("precunit");
                        id = rst.getString("id");
                    }
                    partidas[i] = rst.getString("numegrup");
                    i++;
                }while(rst.next());
                prec = Float.valueOf(precunit);
                cant = Float.valueOf(cantidad);
                prec = prec*cant;
                prec = (float) Math.rint((prec+0.000001)*100)/100;
                jTextArea1.setText(descri);
                jTextField2.setText(id);
                jTextField7.setText(rendimi);
                jTextField8.setText(cantidad);
                jTextField9.setText(precunit);
                jTextField10.setText(String.valueOf(prec));
                jTextField4.setText(id);
                jTextField5.setText(jTextField10.getText());
                SpinnerListModel modelo = new SpinnerListModel(partidas);
                jSpinner1.setModel(modelo);
                                
                jSpinner1.setEnabled(true);
                
        } catch (SQLException ex) {
            Logger.getLogger(diagrama.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void creamodelo(){
        try {
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
            DefaultTableModel partida1 = null;
            String sql = "SELECT numegrup,id, fechaini, fechafin, lapso FROM mppres WHERE cron=1 AND "
                        + "(mpre_id='"+pres+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"' group by id)) ORDER BY fechaini";
            
            Statement st = (Statement) conex.createStatement();
            ResultSet rs = st.executeQuery(sql);
            ResultSetMetaData rsMd = (ResultSetMetaData) rs.getMetaData();
           
            partida1 = new DefaultTableModel(){@Override
            public boolean isCellEditable (int row, int column) {
               
                return false;
            }
                @Override
            public Class getColumnClass(int columna)
            {
               return Object.class;
            }
            };
            jTable1.setModel(partida1);
            
                int cantidadColumnas = rsMd.getColumnCount();
                partida1.addColumn("");
                 for (int i = 1; i <= cantidadColumnas; i++) {
                     System.out.println("Entra a hacer columnas "+cantidadColumnas);
                     if(i!=2&&i!=5){
                     partida1.addColumn(rsMd.getColumnLabel(i));
                     }
                }
                 
                 while (rs.next()) {
                     cont++;
                     Object[] filas = new Object[cantidadColumnas];
                      
                    for (int i = 0; i < cantidadColumnas; i++) {
                      if(i!=1&&i!=4){
                          if(i==2||i==3){
                              filas[i]= formato.format(rs.getDate(i+1));
                          }
                          
                          if(i!=2&&i!=3){
                           filas[i]=rs.getObject(i+1);
                          }
                           
                      }else{
                         
                          int lapsos=0;
                          lapsos = rs.getInt("lapso");
                          fechaini = rs.getDate("fechaini");
                              fechafin = rs.getDate("fechafin");
                          if(lapsos==0){
                              
                                  calculadias();
                                 filas[i]=dias+" días";
                                  System.out.println("dias "+dias);
                                 
                              totaldias +=dias;
                          }if(lapsos==1){
                              calculasemanas();
                              filas[i]=dias+" semanas";
                          }if(lapsos==2){
                              filas[i]=dias+" meses";
                          }
                          
                      }
                    }
                    partida1.addRow(filas);
                    
                }
                 jTextField12.setText(String.valueOf(totaldias));
            cambiarcabecera();
        } catch (SQLException ex) {
            Logger.getLogger(diagrama.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void hacerscroll(){
             
    tabla.setVisible(true);
    
    
    tabla.addMouseListener(new java.awt.event.MouseAdapter()
            {
            @Override
public void mouseClicked(java.awt.event.MouseEvent e)
{
    int fila=0;
    fila = tabla.rowAtPoint(e.getPoint());
    jTable1.setRowSelectionInterval(fila, fila);
    filafase=fila;
    partidaselect = jTable1.getValueAt(filafase, 0).toString();
    Rectangle r = jTable1.getCellRect(jTable1.getSelectedRow(),jTable1.getSelectedColumn(), false);
    jTable1.scrollRectToVisible(r);
    // :D :D foco a la fila seleccionada
    partidaselect = jTable1.getValueAt(filafase, 0).toString();
    llenacampos();  
}
}
            
            
            
);
        scroll = new JScrollPane( tabla );
    scroll.setVisible(true);
       scroll.setAutoscrolls(true);
       scroll.setBorder(BorderFactory.createEtchedBorder(Color.lightGray, Color.gray));
       scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
       scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setSize(jPanel7.getWidth()-10, jPanel7.getHeight()-20);
       // scroll.add(jTable1);
        scroll.setViewportView(tabla);
       jLabel10.add(scroll); 
     
    }
    public void numdia(){
        int numdia=0;
         SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        int dif=0;
        String formateada=null;
        Calendar calen = Calendar.getInstance();
        
        Date fecsem1=null, fechsum=null;
        Calendar calendario = Calendar.getInstance();
        calendario.setTime(fechaini);
        numdia = calendario.get(Calendar.DAY_OF_WEEK);
        dif = numdia-2;
        if(dif==0){
            formateada = formato.format(fechaini);
            semanas[0]=formateada;
            fecsem1 = fechaini;
        }else{
            long tiempoactual = fechaini.getTime();
            long difdias = dif*24*60*60*1000;
            fecsem1 = new Date(tiempoactual+(-1)*difdias);
            formateada = formato.format(fecsem1);
            semanas[0]=formateada;
        }
        for(int i=1;i<semana;i++){
            calen.setTime(fecsem1);
            calen.add(Calendar.DATE, 7);
            fecsem1 = calen.getTime();
            formateada = formato.format(fecsem1);
            semanas[i]=formateada;
        }
       
    }
    public void nummes(){
        //Para obtener el vector de mes/año para el cronograma por semanas la fila de arriba
        
        Calendar fechini = Calendar.getInstance();
        String mes1, anio1;
        int i=0;
      
        fechini.setTime(fechaini);
        mes1 = String.valueOf(fechini.get(Calendar.MONTH)+1);
        anio1 = String.valueOf(fechini.get(Calendar.YEAR));
        fechini.set(Integer.parseInt(anio1), Integer.parseInt(mes1), 1);
        while(i<meses){
            int month = Integer.parseInt(mes1);
            if(month<10){
                vecmes[i]="0"+mes1+"/"+anio1;
            }
            else{
                 vecmes[i]=mes1+"/"+anio1;
            }
            fechini.add(Calendar.MONTH , 1);
            mes1=String.valueOf(fechini.get(Calendar.MONTH));
            anio1 = String.valueOf(fechini.get(Calendar.YEAR));
            i++;
        }
        
    } 
    public void numanio(){
        
        Calendar fechini = Calendar.getInstance();
        String mes1, anio1;
        int i=0;
      
        fechini.setTime(fechaini);
        anio1 = String.valueOf(fechini.get(Calendar.YEAR));
        while(i<anios){         
            
                vecanio[i]=anio1;
           
            fechini.add(Calendar.YEAR , 1);
            
            anio1 = String.valueOf(fechini.get(Calendar.YEAR));
            i++;
        }
        
    }
    public void vectordias(){
        SimpleDateFormat formatoDelTexto = new SimpleDateFormat("dd/MM/yyyy");
        String[] letradias={"L","M","M","J","V","S","D"};
        Calendar cal = Calendar.getInstance();
        String formateada=null;
        Date fechas =null;
        try {
            fechas = formatoDelTexto.parse(semanas[0]);
        } catch (ParseException ex) {
            Logger.getLogger(diagrama.class.getName()).log(Level.SEVERE, null, ex);
        }
        int i=0, j=0;
        int numdia = (int) (semana*7);
        while(i<numdia){
            j=0;
            while(j<7){
                diasemana[i]=letradias[j];
                cal.setTime(fechas);
                
                fechas = cal.getTime();
                formateada = formatoDelTexto.format(fechas);
                fechasdias[i]=formateada;
                cal.add(Calendar.DATE, 1);
                fechas=cal.getTime();
                j++;
                i++;
            } 
        }
    }
    public void vectorsemanas(){
       String sem1;
       Calendar fechini = Calendar.getInstance();
       fechini.setTime(fechaini);
       sem1 = String.valueOf(fechini.get(Calendar.WEEK_OF_YEAR));
       int i=0;
       String mes, anio;
       mes = String.valueOf(fechini.get(Calendar.MONTH)+1);
       anio = String.valueOf(fechini.get(Calendar.YEAR));
       while(i<numsemanas){
           semanaanio[i]=sem1;
           int month = Integer.parseInt(mes);
           if(month<10){
                semanames[i]="0"+mes+"/"+anio;
           }else{
               semanames[i]=mes+"/"+anio;
           }
           fechini.add(Calendar.WEEK_OF_YEAR, 1);
           sem1=String.valueOf(fechini.get(Calendar.WEEK_OF_YEAR));
           mes =String.valueOf(fechini.get(Calendar.MONTH)+1);
           anio = String.valueOf(fechini.get(Calendar.YEAR));
           i++;
       }
        
    }
    public void vectormeses(){
       int mes1;
       Calendar fechini = Calendar.getInstance();
       fechini.setTime(fechaini);    
       int mesresta=0;
       int i=0;
       int mes;
       mes1 = fechini.get(Calendar.MONTH)+1;
       String anio;
       mes = 1;
       mesresta= mes1-1;
       fechini.add(Calendar.MONTH, -mesresta);
       anio = String.valueOf(fechini.get(Calendar.YEAR));
       while(i<numeses){
          
         
           if(mes<10){
                vecmeses[i]="0"+mes;
           }else{
               vecmeses[i]=String.valueOf(mes);
           }
           vecmesanio[i]=anio;
          
           
           fechini.add(Calendar.MONTH, 1);
           mes = fechini.get(Calendar.MONTH)+1;
           anio = String.valueOf(fechini.get(Calendar.YEAR));
           i++;
       }
        
    }
    public void iniciamatriz(int partidas, int dias){
        
        //Inicia la matriz en 0 para dias habiles y 2 para fines de semana
        for(int i=0; i<partidas;i++){
            for(int j=0;j<dias;j++){
                if(j>0){
                    j--;
                } matriz[i][j]=new Integer(0);
                for(int k=0;k<7;k++){
                    if(k<=4){
                    matriz[i][j]=new Integer(0);
                    }else{
                        matriz[i][j]=new Integer(2);
                    }
                    j++;
                }
            }
        }
    }
    public void iniciamatriz2(int partidas, int dias){
        
        //Inicia la matriz en 0 para dias habiles y 2 para fines de semana
        for(int i=0; i<partidas;i++){
            for(int j=0;j<dias;j++){
                matriz[i][j]=new Integer(0);                
            }
        }
    }
    public DefaultTableModel modelotabla(){
         SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        Date fecini=null, fecfin=null, fecini1=null, fecfin1 = null, fecfin2=null, fecini2=null;
        int cron=0, lapsos=0, rango=0;
        String numegrup="";
        int cant = 0, ix =0;
        int numpartidas=0;
      
       dm= new DefaultTableModel(){@Override
            public boolean isCellEditable (int fila, int columna) {            
                return false;
            }
        };
        String select = "SELECT fechaini, fechafin, cron, lapso, rango, numegrup"
                + " FROM mppres"
                + " WHERE cron=1 AND (mpre_id='"+pres+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) "
                + "ORDER BY fechaini";
        String selfin = "SELECT fechaini, fechafin FROM mppres WHERE cron=1 "
                + "AND (mpre_id='"+pres+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) "
                + "ORDER BY fechafin";
        try {
            Statement st = (Statement) conex.createStatement();
            Statement stfin = (Statement) conex.createStatement();
            ResultSet rste = st.executeQuery(select);
            ResultSet rstfin = stfin.executeQuery(selfin);
            Object [][] objeto;
            Object [] cabecera;
            rste.last();
            rstfin.last();
            numpartidas= rste.getRow();
            if(numpartidas>0){
            fecini2 = rste.getDate(1);
            fecfin2 = rste.getDate(2);
            lapsos = rste.getInt("lapso");
            cant = rste.getRow();
            fecfin2 = rstfin.getDate(2);
            rste.first();
            fecini1 = rste.getDate(1);
            fecfin1 = rste.getDate(2);
            fechaini = fecini1;
            fechafin = fecfin2;
            calculasemanas();
            semana = (float) Math.ceil(dias)+1; // numero de semanas
            if(semana<8){
                semana=8;
            }
            numdias = (int) (semana*7);
            semanas = new String[(int) semana];
            fechasdias = new String[numdias];
            diasemana = new String[numdias];
            matriz= new Object[numpartidas][numdias];
            String formini, formfin;
            formini = formato.format(fecini1);
            formfin = formato.format(fecfin1);
            int i = 0;
            int enc=0;
            numdia();
            vectordias();
            iniciamatriz(numpartidas,numdias);
            System.out.println("numdias="+numdias);
            jTextField12.setText(String.valueOf(semana*5));
            int num = 0;
            do{
                if(i>0){
                    fecini1 = rste.getDate(1);
                    fecfin1 = rste.getDate(2);
                    formini = formato.format(fecini1);
                    formfin = formato.format(fecfin1);
                }                
                numegrup = rste.getString("numegrup"); 
                rango = rste.getInt("rango");
                if(rango==0){
                    //pinta seguido 1 de fecha ini a fechafin
                    int n=0;
                    int posdia=0;
                    enc=0;
                    while(n<numdias && enc==0){
                        if(formini.equals(fechasdias[n])){
                            matriz[num][n]=new Integer(1);
                            int enc2=0;
                            int n2=n+1;
                            enc=1;
                            while(n2<numdias && enc2==0){
                                matriz[num][n2]=new Integer(1);
                                if(formfin.equals(fechasdias[n2])){
                                    enc2=1;
                                }
                                n2++;
                                
                            }
                        }
                        n++;
                    }
                }else{
                    // rango==1
                    int n=0;
                    int posdia=0;
                    enc=0;
                    int in = 0;
                    int conta = 0;
                   String [] fechini, fechfin;
                    String selec = "SELECT count(*) FROM rcppres as rc, mppres as mp"
                            + " WHERE mp.numegrup="+numegrup+" AND rc.mppres_id=mp.numero "
                            + "AND rc.mpre_id='"+pres+"'";
                    Statement ste = (Statement) conex.createStatement();
                    ResultSet rsts = ste.executeQuery(selec);
                    while(rsts.next()){
                        conta = rsts.getInt(1);
                    }
                    fechini = new String[conta];
                    fechfin = new String[conta];
                    String sele = "SELECT rc.fechaini, rc.fechafin FROM rcppres as rc, mppres as mp"
                            + " WHERE mp.numegrup="+numegrup+" AND rc.mppres_id=mp.numero "
                            + "AND rc.mpre_id='"+pres+"'";
                    Statement str = (Statement) conex.createStatement();
                    ResultSet rstr = str.executeQuery(sele);
                    int g=0;
                    while(rstr.next()){
                        fecini = rstr.getDate(1);
                        fecfin = rstr.getDate(2);
                        fechini[g]=formato.format(fecini);
                         fechfin[g]=formato.format(fecfin);
                         g++;
                    }
                    while(in<conta){
                       formini=fechini[in];
                       enc=0;
                        while(n<numdias && enc==0){
                            if(formini.equals(fechasdias[n])){
                                matriz[num][n]=new Integer(1);
                                int enc2=0;
                                int n2=n+1;
                                enc=1;
                            formfin = fechfin[in];
                            while(n2<numdias && enc2==0){
                                matriz[num][n2]=new Integer(1);
                                if(formfin.equals(fechasdias[n2]))
                                {
                                    enc2=1;
                                }
                                n2++;
                                
                            }
                        }
                        n++;
                    }
                    in++;
                }
                }
                num++;
                
               i++; 
                
            }while(rste.next());
            dm.setDataVector(matriz, diasemana);
            }
            
           
        } catch (SQLException ex) {
            Logger.getLogger(diagrama.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        
        
     
        
    return dm;
    }
    public void calculatotaldias(){
        //jtextfield13 para total de dias suma(cant/rendimi)
        float cantdias=0;
        String selec = "SELECT SUM(cantidad/rendimi) FROM mppres WHERE "
                + "(mpre_id='"+pres+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"'))";
        try {
            Statement ste = (Statement) conex.createStatement();
            ResultSet rste = ste.executeQuery(selec);
            while(rste.next()){
                cantdias = rste.getFloat(1);
            }
            jTextField13.setText(String.valueOf(Math.rint(cantdias*100)/100));
        } catch (SQLException ex) {
            Logger.getLogger(diagrama.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    
    public void agrupacolumna(){
    String[] letradias={"L","M","M","J","V","S","D"};
     TableColumnModel cm = tabla.getColumnModel();
     int recor=0;
     int semana1 = (int) semana;
     ColumnGroup [] cg = new ColumnGroup[semana1];
     for(int i=0;i<semana1;i++){    
         cg[i]=new ColumnGroup(semanas[i]);
         for (int j=0; j<7;j++){
             cg[i].add(cm.getColumn(recor));
             recor++;
         }
     }
    GroupableTableHeader header = (GroupableTableHeader)tabla.getTableHeader();
        for(int i=0;i<semana1;i++){
            header.addColumnGroup(cg[i]);
        }
       
        
    }
    public void iniciatabla(){
        tabla = new JTable(modelotabla()){
        @Override
      protected JTableHeader createDefaultTableHeader() {
          return new GroupableTableHeader(columnModel);
      }
         
    };
    }
    public void llenatabla(){
        
        
         
       modelotabla();
       tabla.setModel(dm);
       TableCellRenderer renderer = new RenderCelda();
        
            tabla.setDefaultRenderer( Object.class, renderer );
       tabla.setSize(jPanel7.getWidth(), jPanel7.getHeight());     
    tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    tabla.setOpaque(false);
    tabla.setShowHorizontalLines(true);
    tabla.setShowVerticalLines(false);
    Font fuente = new Font("Tahoma", Font.PLAIN, 11);
    tabla.setFont(fuente);
    tabla.setRowHeight(25);
    int num=tabla.getColumnCount();
    tabla.setRowSelectionAllowed(false);
    JTableHeader th = tabla.getTableHeader();
     TableColumnModel tcm = th.getColumnModel();
     th.setEnabled(true);
    for (int i=0;i<num;i++){
        TableColumn tc = tcm.getColumn(i); 
       tc.setPreferredWidth(10);
    }
      agrupacolumna();
      tabla.repaint();
    }
     public void llenatabla2(){
        
        
         
     modelotabla2();
       tabla.setModel(dm);
       TableCellRenderer renderer = new RenderCelda();
        
            tabla.setDefaultRenderer( Object.class, renderer );
       tabla.setSize(jPanel7.getWidth(), jPanel7.getHeight());     
    tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    tabla.setOpaque(false);
    tabla.setShowHorizontalLines(true);
    tabla.setShowVerticalLines(false);
    Font fuente = new Font("Tahoma", Font.PLAIN, 11);
    tabla.setFont(fuente);
    tabla.setRowHeight(25);
    int num=tabla.getColumnCount();
    tabla.setRowSelectionAllowed(false);
    JTableHeader th = tabla.getTableHeader();
     TableColumnModel tcm = th.getColumnModel();
     th.setEnabled(true);
    for (int i=0;i<num;i++){
        TableColumn tc = tcm.getColumn(i); 
       tc.setPreferredWidth(50);
    }
      
      tabla.repaint();
       
      agrupacolumna2();
    }
     public void agrupacolumna2(){
     
    
     TableColumnModel cm = tabla.getColumnModel();//22,23,24,25,26
     int recor=0;
     int enc=0, j=0;
             
     int semana1 = (int) meses;
     ColumnGroup [] cg = new ColumnGroup[semana1];
     for(int i=0;i<semana1;i++){    
         cg[i]=new ColumnGroup(vecmes[i]);
         enc=0;
         if(j>0) {
             j--;
         }
         while(enc==0&&j<semanames.length){
             if(vecmes[i].equals(semanames[j])){
                cg[i].add(cm.getColumn(recor));
                recor++;
                
                
             }else{
                 enc=1;
             }
             j++;
         }
     }
    GroupableTableHeader header = (GroupableTableHeader)tabla.getTableHeader();
        for(int i=0;i<semana1;i++){
            header.addColumnGroup(cg[i]);
        }
       
        
    }
     
     public void agrupacolumna3(){
     
    
     TableColumnModel cm = tabla.getColumnModel();//22,23,24,25,26
     int recor=0;
     int enc=0, j=0;
             
     int semana1 = (int) anios;
     ColumnGroup [] cg = new ColumnGroup[semana1];
     for(int i=0;i<semana1;i++){    
         cg[i]=new ColumnGroup(vecanio[i]);
         enc=0;
         if(j>0) {
             j--;
         }
         while(enc==0&&j<vecmeses.length){
             if(vecanio[i].equals(vecmesanio[j])){
                cg[i].add(cm.getColumn(recor));
                recor++;
                
                
             }else{
                 enc=1;
             }
             j++;
         }
     }
    GroupableTableHeader header = (GroupableTableHeader)tabla.getTableHeader();
        for(int i=0;i<semana1;i++){
            header.addColumnGroup(cg[i]);
        }
       
        
    }
     public DefaultTableModel modelotabla2(){
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        Date fecini=null, fecfin=null, fecini1=null, fecfin1 = null, fecfin2=null, fecini2=null;
        int cron=0, lapsos=0, rango=0;
        String numegrup="";
        int cant = 0, ix =0;
        int numpartidas=0;
      
        dm = new DefaultTableModel(){@Override
            public boolean isCellEditable (int fila, int columna) {            
                return false;
            }
        };
        String select = "SELECT fechaini, fechafin, cron, lapso, rango, numegrup"
                + " FROM mppres"
                + " WHERE cron=1 AND (mpre_id='"+pres+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) "
                + "ORDER BY fechaini";
        String selfin = "SELECT fechaini, fechafin FROM mppres WHERE cron=1 "
                + "AND (mpre_id='"+pres+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) "
                + "ORDER BY fechafin";
        try {
            Statement st = (Statement) conex.createStatement();
            Statement stfin = (Statement) conex.createStatement();
            ResultSet rste = st.executeQuery(select);
            ResultSet rstfin = stfin.executeQuery(selfin);
            Object [][] objeto;
            Object [] cabecera;
            rste.last();
            rstfin.last();
            numpartidas= rste.getRow();
            fecini2 = rste.getDate(1);
            fecfin2 = rste.getDate(2);
            lapsos = rste.getInt("lapso");
            cant = rste.getRow();
            fecfin2 = rstfin.getDate(2);
            rste.first();
            fecini1 = rste.getDate(1);
            fecfin1 = rste.getDate(2);
            fechaini = fecini1;
            fechafin = fecfin2;
            calculames();
            meses = (float) Math.ceil(dias)+1; // numero de meses
            if(meses<6){
                meses=6;
            }
            Calendar cal = Calendar.getInstance();
            Calendar calfin = Calendar.getInstance();
            int mes, anio;
            cal.setTime(fechaini);
            mes=cal.get(Calendar.MONTH);
            anio = cal.get(Calendar.YEAR);
            cal.set(anio, mes, 1);
            String fechas;
            fechas= formato.format(cal.getTime());
            try {
                fechaini = formato.parse(fechas);
            } catch (ParseException ex) {
                Logger.getLogger(diagrama.class.getName()).log(Level.SEVERE, null, ex);
            }
            calfin.setTime(fechafin);
            cal.add(Calendar.MONTH, (int) meses-1);
            mes = cal.get(Calendar.MONTH);
            anio = cal.get(Calendar.YEAR);
            int dis;
            dis = cal.get(Calendar.DATE);
            calfin.set(anio, mes, dis);
            dis = calfin.getMaximum(Calendar.DAY_OF_MONTH);
            calfin.set(anio, mes, dis);
            fechas = formato.format(calfin.getTime());
            try {
                fechafin = formato.parse(fechas);
            } catch (ParseException ex) {
                Logger.getLogger(diagrama.class.getName()).log(Level.SEVERE, null, ex);
            }
            calculasemanas();
            numsemanas = (int) (Math.ceil(dias)+1);
           if(numsemanas<0)
               numsemanas=numsemanas*(-1);
            
            
            vecmes = new String[(int) meses];
            semanaanio = new String[numsemanas]; // Tendra el numero de la semana del año
            semanames = new String[numsemanas];
            matriz= new Object[numpartidas][numsemanas];
            String formini, formfin;
            Calendar fechini= Calendar.getInstance();
            Calendar fechfin = Calendar.getInstance();
           
            fechini.setTime(fecini1);
            fechfin.setTime(fecfin1);
            formini = String.valueOf(fechini.get(Calendar.WEEK_OF_YEAR));
            formfin = String.valueOf(fechfin.get(Calendar.WEEK_OF_YEAR));
            int i = 0;
            int enc=0;
            nummes();
            vectorsemanas();
            
            iniciamatriz2(numpartidas,numsemanas);
            System.out.println("numsemanas="+numsemanas);
            jTextField12.setText(String.valueOf(semana*5));
            int num = 0;
            do{
                if(i>0){
                    fecini1 = rste.getDate(1);
                    fecfin1 = rste.getDate(2);
                    fechini.setTime(fecini1);
                    fechfin.setTime(fecfin1);
                    formini = String.valueOf(fechini.get(Calendar.WEEK_OF_YEAR));
                    formfin = String.valueOf(fechfin.get(Calendar.WEEK_OF_YEAR));
                }                
                numegrup = rste.getString("numegrup"); 
                rango = rste.getInt("rango");
                if(rango==0){
                    //pinta seguido 1 de fecha ini a fechafin
                    int n=0;
                    int posdia=0;
                    enc=0;
                    while(n<numsemanas && enc==0){
                        if(formini.equals(semanaanio[n])){
                            matriz[num][n]=new Integer(1);
                            int enc2=0;
                            int n2=n;
                            enc=1;
                            while(n2<numsemanas && enc2==0){
                                matriz[num][n2]=new Integer(1);
                                if(formfin.equals(semanaanio[n2])){
                                    enc2=1;
                                }
                                n2++;
                                
                            }
                        }
                        n++;
                    }
                }else{
                    // rango==1
                    int n=0;
                    int posdia=0;
                    enc=0;
                    int in = 0;
                    int conta = 0;
                   String [] fechini1, fechfin2;
                    String selec = "SELECT count(*) FROM rcppres as rc, mppres as mp"
                            + " WHERE mp.numegrup="+numegrup+" AND rc.mppres_id=mp.numero "
                            + "AND rc.mpre_id='"+pres+"'";
                    Statement ste = (Statement) conex.createStatement();
                    ResultSet rsts = ste.executeQuery(selec);
                    while(rsts.next()){
                        conta = rsts.getInt(1);
                    }
                    fechini1 = new String[conta];
                    fechfin2 = new String[conta];
                    String sele = "SELECT rc.fechaini, rc.fechafin FROM rcppres as rc, mppres as mp"
                            + " WHERE mp.numegrup="+numegrup+" AND rc.mppres_id=mp.numero "
                            + "AND rc.mpre_id='"+pres+"'";
                    Statement str = (Statement) conex.createStatement();
                    ResultSet rstr = str.executeQuery(sele);
                    int g=0;
                    while(rstr.next()){
                        fecini = rstr.getDate(1);
                        fecfin = rstr.getDate(2);
                        fechini1[g]=formato.format(fecini);
                         fechfin2[g]=formato.format(fecfin);
                         g++;
                    }
                    Calendar cale = Calendar.getInstance();
                    Date fechainicio=null,fechafinal=null ;
                    while(in<conta){
                       formini=fechini1[in];
                       String semana1, semana2;
                        try {
                            fechainicio = formato.parse(formini);
                        } catch (ParseException ex) {
                            Logger.getLogger(diagrama.class.getName()).log(Level.SEVERE, null, ex);
                        }
                       cal.setTime(fechainicio);
                       semana1 = String.valueOf(cal.get(Calendar.WEEK_OF_YEAR));
                       enc=0;
                        while(n<numsemanas && enc==0){
                            if(semana1.equals(semanaanio[n])){
                                matriz[num][n]=new Integer(1);
                                int enc2=0;
                                int n2=n;
                                enc=1;
                            formfin = fechfin2[in];
                                try {
                                    fechafinal = formato.parse(formfin);
                                } catch (ParseException ex) {
                                    Logger.getLogger(diagrama.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            cal.setTime(fechafinal);
                            semana2 = String.valueOf(cal.get(Calendar.WEEK_OF_YEAR));
                            while(n2<numsemanas && enc2==0){
                                matriz[num][n2]=new Integer(1);
                                if(semana2.equals(semanaanio[n2]))
                                {
                                    enc2=1;
                                }
                                n2++;
                                
                            }
                        }
                        n++;
                    }
                    in++;
                }
                }
                num++;
                
               i++; 
                
            }while(rste.next());
            dm.setDataVector(matriz, semanaanio);
           
        } catch (SQLException ex) {
            Logger.getLogger(diagrama.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        
        
     
        
    return dm;
    }
     public void llenatabla4(){
        
        
         
     modelotabla4();
       tabla.setModel(dm);
       TableCellRenderer renderer = new RenderCelda();
        
            tabla.setDefaultRenderer( Object.class, renderer );
       tabla.setSize(jPanel7.getWidth(), jPanel7.getHeight());     
    tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    tabla.setOpaque(false);
    tabla.setShowHorizontalLines(true);
    tabla.setShowVerticalLines(false);
    Font fuente = new Font("Tahoma", Font.PLAIN, 11);
    tabla.setFont(fuente);
    tabla.setRowHeight(25);
    int num=tabla.getColumnCount();
    tabla.setRowSelectionAllowed(false);
    JTableHeader th = tabla.getTableHeader();
     TableColumnModel tcm = th.getColumnModel();
     th.setEnabled(true);
    for (int i=0;i<num;i++){
        TableColumn tc = tcm.getColumn(i); 
       tc.setPreferredWidth(350);
    }
      
      tabla.repaint();
       
      
    }
      public DefaultTableModel modelotabla4(){
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        Date fecini=null, fecfin=null, fecini1=null, fecfin1 = null, fecfin2=null, fecini2=null;
        int cron=0, lapsos=0, rango=0;
        String numegrup="";
        int cant = 0, ix =0;
        int numpartidas=0;
      
       dm= new DefaultTableModel(){@Override
            public boolean isCellEditable (int fila, int columna) {            
                return false;
            }
        };
        String select = "SELECT fechaini, fechafin, cron, lapso, rango, numegrup"
                + " FROM mppres"
                + " WHERE cron=1 AND (mpre_id='"+pres+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) "
                + "ORDER BY fechaini";
        String selfin = "SELECT fechaini, fechafin FROM mppres WHERE cron=1 "
                + "AND (mpre_id='"+pres+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) "
                + "ORDER BY fechafin";
        try {
            Statement st = (Statement) conex.createStatement();
            Statement stfin = (Statement) conex.createStatement();
            ResultSet rste = st.executeQuery(select);
            ResultSet rstfin = stfin.executeQuery(selfin);
            Object [][] objeto;
            Object [] cabecera;
            rste.last();
            rstfin.last();
            numpartidas= rste.getRow();
            if(numpartidas>0){
            fecini2 = rste.getDate(1);
            fecfin2 = rste.getDate(2);
            lapsos = rste.getInt("lapso");
            cant = rste.getRow();
            fecfin2 = rstfin.getDate(2);
            rste.first();
            fecini1 = rste.getDate(1);
            fecfin1 = rste.getDate(2);
            fechaini = fecini1;
            fechafin = fecfin2;
            calculaanio();
            anios = (float) Math.ceil(dias)+1; // numero de semanas
            if(anios<2){
                anios=2;
                        
            }
           
            vecanio = new String[(int) anios];
           
            matriz= new Object[numpartidas][(int)anios];
            String formini,yearini,yearfin, formfin;
            int i = 0;
            int enc=0;
            numanio(); 
           
            Calendar fechini1= Calendar.getInstance();
            Calendar fechfin1 = Calendar.getInstance();
           
            fechini1.setTime(fecini1);
            fechfin1.setTime(fecfin1);
            
                     
           
            yearini = String.valueOf(fechini1.get(Calendar.YEAR));
            yearfin = String.valueOf(fechini1.get(Calendar.YEAR));
            iniciamatriz2(numpartidas,(int) anios);
            System.out.println("numeses="+numeses);
            jTextField12.setText(String.valueOf(semana*5));
            int num = 0;
            do{
                if(i>0){
                    fechini1.setTime(fecini1);
            fechfin1.setTime(fecfin1);
            
                     
           
            yearini = String.valueOf(fechini1.get(Calendar.YEAR));
            yearfin = String.valueOf(fechini1.get(Calendar.YEAR));
                }                
                numegrup = rste.getString("numegrup"); 
                rango = rste.getInt("rango");
                if(rango==0){
                    //pinta seguido 1 de fecha ini a fechafin
                    int n=0;
                    int posdia=0;
                    enc=0;
                    while(n<anios && enc==0){
                        if(yearini.equals(vecanio[n])){
                            matriz[num][n]=new Integer(1);
                            int enc2=0;
                            int n2=n;
                            enc=1;
                            while(n2<anios && enc2==0){
                                matriz[num][n2]=new Integer(1);
                                if(yearini.equals(vecanio[n2])){
                                    enc2=1;
                                }
                                n2++;
                                
                            }
                        }
                        n++;
                    }
                }else{
                    // rango==1
                    int n=0;
                    int posdia=0;
                    enc=0;
                    int in = 0;
                    int conta = 0;
                   String [] fechini, fechfin;
                    String selec = "SELECT count(*) FROM rcppres as rc, mppres as mp"
                            + " WHERE mp.numegrup="+numegrup+" AND rc.mppres_id=mp.numero "
                            + "AND rc.mpre_id='"+pres+"'";
                    Statement ste = (Statement) conex.createStatement();
                    ResultSet rsts = ste.executeQuery(selec);
                    while(rsts.next()){
                        conta = rsts.getInt(1);
                    }
                    fechini = new String[conta];
                    fechfin = new String[conta];
                    String sele = "SELECT rc.fechaini, rc.fechafin FROM rcppres as rc, mppres as mp"
                            + " WHERE mp.numegrup="+numegrup+" AND rc.mppres_id=mp.numero "
                            + "AND rc.mpre_id='"+pres+"'";
                    Statement str = (Statement) conex.createStatement();
                    ResultSet rstr = str.executeQuery(sele);
                    int g=0;
                    while(rstr.next()){
                        fecini = rstr.getDate(1);
                        fecfin = rstr.getDate(2);
                        fechini[g]=formato.format(fecini);
                         fechfin[g]=formato.format(fecfin);
                         g++;
                    }
                    while(in<conta){
                        Calendar fechini2= Calendar.getInstance();
                        Calendar fechfin2 = Calendar.getInstance();
           
                        String[] fechar=fechini[in].split("/") ;
                        String[] fecharfin=fechfin[in].split("/") ;
                       
                        yearini = fechar[2];
                        yearfin = fecharfin[2];
                       
                       
                       enc=0;
                       while(n<anios && enc==0){
                        if(yearini.equals(vecanio[n])){
                            matriz[num][n]=new Integer(1);
                            int enc2=0;
                            int n2=n;
                            enc=1;
                            while(n2<anios && enc2==0){
                                matriz[num][n2]=new Integer(1);
                                if(yearfin.equals(vecanio[n2])){
                                    enc2=1;
                                }
                                n2++;
                                
                            }
                        }
                        n++;
                    }
                    in++;
                }
                }
                num++;
                
               i++; 
                
            }while(rste.next());
            dm.setDataVector(matriz, vecanio);
            }
            
           
        } catch (SQLException ex) {
            Logger.getLogger(diagrama.class.getName()).log(Level.SEVERE, null, ex);
        }   
        
    return dm;
    }
     public void llenatabla3(){
        
        
         
     modelotabla3();
       tabla.setModel(dm);
       TableCellRenderer renderer = new RenderCelda();
        
            tabla.setDefaultRenderer( Object.class, renderer );
       tabla.setSize(jPanel7.getWidth(), jPanel7.getHeight());     
    tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    tabla.setOpaque(false);
    tabla.setShowHorizontalLines(true);
    tabla.setShowVerticalLines(false);
    Font fuente = new Font("Tahoma", Font.PLAIN, 11);
    tabla.setFont(fuente);
    tabla.setRowHeight(25);
    int num=tabla.getColumnCount();
    tabla.setRowSelectionAllowed(false);
    JTableHeader th = tabla.getTableHeader();
     TableColumnModel tcm = th.getColumnModel();
     th.setEnabled(true);
    for (int i=0;i<num;i++){
        TableColumn tc = tcm.getColumn(i); 
       tc.setPreferredWidth(60);
    }
      
      tabla.repaint();
       
      agrupacolumna3();
    }
    public DefaultTableModel modelotabla3(){
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        Date fecini=null, fecfin=null, fecini1=null, fecfin1 = null, fecfin2=null, fecini2=null;
        int cron=0, lapsos=0, rango=0;
        String numegrup="";
        int cant = 0, ix =0;
        int numpartidas=0;
      
       dm= new DefaultTableModel(){@Override
            public boolean isCellEditable (int fila, int columna) {            
                return false;
            }
        };
        String select = "SELECT fechaini, fechafin, cron, lapso, rango, numegrup"
                + " FROM mppres"
                + " WHERE cron=1 AND (mpre_id='"+pres+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) "
                + "ORDER BY fechaini";
        String selfin = "SELECT fechaini, fechafin FROM mppres WHERE cron=1 "
                + "AND (mpre_id='"+pres+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) "
                + "ORDER BY fechafin";
        try {
            Statement st = (Statement) conex.createStatement();
            Statement stfin = (Statement) conex.createStatement();
            ResultSet rste = st.executeQuery(select);
            ResultSet rstfin = stfin.executeQuery(selfin);
            Object [][] objeto;
            Object [] cabecera;
            rste.last();
            rstfin.last();
            numpartidas= rste.getRow();
            if(numpartidas>0){
            fecini2 = rste.getDate(1);
            fecfin2 = rste.getDate(2);
            lapsos = rste.getInt("lapso");
            cant = rste.getRow();
            fecfin2 = rstfin.getDate(2);
            rste.first();
            fecini1 = rste.getDate(1);
            fecfin1 = rste.getDate(2);
            fechaini = fecini1;
            fechafin = fecfin2;
            calculaanio();
            anios = (float) Math.ceil(dias)+1; // numero de semanas
            
            numeses = (int) (anios*12);
            vecanio = new String[(int) anios];
            vecmeses = new String[(int) numeses];
            vecmesanio = new String[(int) numeses];
            matriz= new Object[numpartidas][numeses];
            String formini,yearini,yearfin, formfin;
            int i = 0;
            int enc=0;
            numanio(); 
            vectormeses();
           
            Calendar fechini1= Calendar.getInstance();
            Calendar fechfin1 = Calendar.getInstance();
           
            fechini1.setTime(fecini1);
            fechfin1.setTime(fecfin1);
            int mese = fechini1.get(Calendar.MONTH)+1;
            if(mese<10){
            formini = "0"+String.valueOf(fechini1.get(Calendar.MONTH)+1);
            }else{
                formini = String.valueOf(fechini1.get(Calendar.MONTH)+1);
            }
            mese = fechfin1.get(Calendar.MONTH)+1;
            if(mese<10){
            formfin = "0"+String.valueOf(fechfin1.get(Calendar.MONTH)+1);
            }else{
                formfin = String.valueOf(fechfin1.get(Calendar.MONTH)+1);
            }
           
            yearini = String.valueOf(fechini1.get(Calendar.YEAR));
            yearfin = String.valueOf(fechini1.get(Calendar.YEAR));
            iniciamatriz2(numpartidas,numeses);
            System.out.println("numeses="+numeses);
            jTextField12.setText(String.valueOf(semana*5));
            int num = 0;
            do{
                if(i>0){
                    fecini1 = rste.getDate(1);
                    fecfin1 = rste.getDate(2);
                    fechini1.setTime(fecini1);
                    fechfin1.setTime(fecfin1);
                    int mes1=fechini1.get(Calendar.MONTH)+1;
                    if(mes1<10){
                    formini = "0"+String.valueOf(fechini1.get(Calendar.MONTH)+1);
                    }else{
                        formini = String.valueOf(fechini1.get(Calendar.MONTH)+1);
                    }
                    mes1=fechfin1.get(Calendar.MONTH)+1;
                    if(mes1<10){
                    formfin = "0"+String.valueOf(fechfin1.get(Calendar.MONTH)+1);
                    }else{
                        formfin = String.valueOf(fechfin1.get(Calendar.MONTH)+1);
                    }
                    yearini = String.valueOf(fechini1.get(Calendar.YEAR));
                    yearfin = String.valueOf(fechfin1.get(Calendar.YEAR));
                }                
                numegrup = rste.getString("numegrup"); 
                rango = rste.getInt("rango");
                if(rango==0){
                    //pinta seguido 1 de fecha ini a fechafin
                    int n=0;
                    int posdia=0;
                    enc=0;
                    while(n<numeses && enc==0){
                        if(formini.equals(vecmeses[n]) && yearini.equals(vecmesanio[n])){
                            matriz[num][n]=new Integer(1);
                            int enc2=0;
                            int n2=n;
                            enc=1;
                            while(n2<numeses && enc2==0){
                                matriz[num][n2]=new Integer(1);
                                if(formfin.equals(vecmeses[n2]) && yearini.equals(vecmesanio[n2])){
                                    enc2=1;
                                }
                                n2++;
                                
                            }
                        }
                        n++;
                    }
                }else{
                    // rango==1
                    int n=0;
                    int posdia=0;
                    enc=0;
                    int in = 0;
                    int conta = 0;
                   String [] fechini, fechfin;
                    String selec = "SELECT count(*) FROM rcppres as rc, mppres as mp"
                            + " WHERE mp.numegrup="+numegrup+" AND rc.mppres_id=mp.numero "
                            + "AND rc.mpre_id='"+pres+"'";
                    Statement ste = (Statement) conex.createStatement();
                    ResultSet rsts = ste.executeQuery(selec);
                    while(rsts.next()){
                        conta = rsts.getInt(1);
                    }
                    fechini = new String[conta];
                    fechfin = new String[conta];
                    String sele = "SELECT rc.fechaini, rc.fechafin FROM rcppres as rc, mppres as mp"
                            + " WHERE mp.numegrup="+numegrup+" AND rc.mppres_id=mp.numero "
                            + "AND rc.mpre_id='"+pres+"'";
                    Statement str = (Statement) conex.createStatement();
                    ResultSet rstr = str.executeQuery(sele);
                    int g=0;
                    while(rstr.next()){
                        fecini = rstr.getDate(1);
                        fecfin = rstr.getDate(2);
                        fechini[g]=formato.format(fecini);
                         fechfin[g]=formato.format(fecfin);
                         g++;
                    }
                    while(in<conta){
                        Calendar fechini2= Calendar.getInstance();
                        Calendar fechfin2 = Calendar.getInstance();
           
                        String[] fechar=fechini[in].split("/") ;
                        String[] fecharfin=fechfin[in].split("/") ;
                        formini = fechar[1];
                        formfin = fecharfin[1];
                        yearini = fechar[2];
                        yearfin = fecharfin[2];
                       
                       
                       enc=0;
                       while(n<numeses && enc==0){
                        if(formini.equals(vecmeses[n]) && yearini.equals(vecmesanio[n])){
                            matriz[num][n]=new Integer(1);
                            int enc2=0;
                            int n2=n;
                            enc=1;
                            while(n2<numeses && enc2==0){
                                matriz[num][n2]=new Integer(1);
                                if(formfin.equals(vecmeses[n2]) && yearfin.equals(vecmesanio[n2])){
                                    enc2=1;
                                }
                                n2++;
                                
                            }
                        }
                        n++;
                    }
                    in++;
                }
                }
                num++;
                
               i++; 
                
            }while(rste.next());
            dm.setDataVector(matriz, vecmeses);
            }
            
           
        } catch (SQLException ex) {
            Logger.getLogger(diagrama.class.getName()).log(Level.SEVERE, null, ex);
        }   
        
    return dm;
    }
    public void hacetabla1()    
    {
        creamodelo();
       llenatabla();
    
 
    
   
    }
     public void hacetabla2()    
    {
        creamodelo();
        llenatabla2();
       
    int num=tabla.getColumnCount();
   
    JTableHeader th = tabla.getTableHeader();
     TableColumnModel tcm = th.getColumnModel();
     th.setEnabled(true);
    for (int i=0;i<num;i++){
        TableColumn tc = tcm.getColumn(i); 
       tc.setPreferredWidth(50);
    }
  
    }
     public void hacetabla3()    
    {
        creamodelo();
       llenatabla3(); 
    
    }
     public void hacetabla4()    
    {
        creamodelo();
       llenatabla4(); 
    
    }
     
    public void hacetabla()
    {
        
        creamodelo();
        final IntervalCategoryDataset dataset = createTasks();
        final JFreeChart chart = createChart(dataset);

      
        if(cont>0){
        final ChartPanel chartPanel = new ChartPanel(chart);
        int y = jTable1.getHeight();
        if(y<320){
            y=cont*10;
            if(y<320){
                y=320;
            }
        }
       
        System.out.println("y "+y);
        chartPanel.setPreferredSize(new java.awt.Dimension(700,y));
        chartPanel.setVisible(true);
        BufferedImage image = chart.createBufferedImage(700,y);   
        scroller = new JScrollBar(1, 0, 15, 0, 50);
        dia.add(chartPanel);
        jPanel6.add(chartPanel);
       jLabel10.setIcon(new ImageIcon(image));
        cont=0;
        }
        
    }
      private IntervalCategoryDataset createTasks() {
            ResultSet rsts=null;
           
             TaskSeriesCollection taskseriescollection = null;
            String sqls = "SELECT rango, fechaini, fechafin, numegrup FROM mppres WHERE cron=1 AND "
                    + "(mpre_id='"+pres+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) ORDER BY fechaini";
        try {
            Statement str = (Statement) conex.createStatement();
            rsts = str.executeQuery(sqls);
            taskseriescollection = new TaskSeriesCollection();
               int i=0;
               String numegrup=null;
               Date fecini=null, fecfin=null;
               int rango;
                TaskSeries taskseries=new TaskSeries("");
               while(rsts.next()){
                   numegrup = rsts.getString("numegrup");
                   fecini= rsts.getDate("fechaini");
                   fecfin = rsts.getDate("fechafin");
                   rango = rsts.getInt("rango");
                   if(rango==0){       
                     
                    taskseries.add(new Task(numegrup,fecini,fecfin ));
                   
                   }
                   cont++;
                   
               }
                taskseriescollection.add(taskseries);
        } catch (SQLException ex) {
            Logger.getLogger(diagrama.class.getName()).log(Level.SEVERE, null, ex);
        }
            
        
                return taskseriescollection;
       
        }
      private void cambiarcabecera() {
       JTableHeader th = jTable1.getTableHeader();
       TableColumnModel tcm = th.getColumnModel();
       TableColumn tc = tcm.getColumn(0); 
       tc.setPreferredWidth(10);
        tc.setHeaderValue("Num. Partida");
       tc = tcm.getColumn(1); 
       tc.setHeaderValue("Duración");
       tc.setPreferredWidth(10);
       tc = tcm.getColumn(2); 
        tc.setHeaderValue("Inicio");
        tc.setPreferredWidth(10);
        tc = tcm.getColumn(3); 
       tc.setHeaderValue("Fin");
    tc.setPreferredWidth(10);
        
        
       th.repaint(); 
      
        }


     private JFreeChart createChart(IntervalCategoryDataset intervalxydataset)
        {
                JFreeChart jfreechart = ChartFactory.createGanttChart("", 
                        "Partidas", 
                        "Fecha", 
                        intervalxydataset, false, false, false);
                jfreechart.setBackgroundPaint(Color.white);
        /*      XYPlot xyplot = (XYPlot)jfreechart.getPlot();
                xyplot.setRangePannable(true);
                SymbolAxis symbolaxis = new SymbolAxis("", new String[] {
                        "X", "Y", "Z", "D"
                });
                symbolaxis.setGridBandsVisible(false);
                xyplot.setDomainAxis(symbolaxis);
                XYBarRenderer xybarrenderer = (XYBarRenderer)xyplot.getRenderer();
                xybarrenderer.setUseYInterval(true);
                xyplot.setRangeAxis(new DateAxis(""));
                ChartUtilities.applyCurrentTheme(jfreechart);*/
        try {
            File foto = new File("chart1.jpg");
            ChartUtilities.saveChartAsJPEG(foto, jfreechart, 700 ,(cont*15));
            
            
        } catch (IOException ex) {
            Logger.getLogger(diagrama.class.getName()).log(Level.SEVERE, null, ex);
        }
                return jfreechart;
        }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        cancelButton = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        okButton = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jSpinner1 = new javax.swing.JSpinner();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel13 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jTextField9 = new javax.swing.JTextField();
        jTextField10 = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jTextField11 = new javax.swing.JTextField();
        jComboBox2 = new javax.swing.JComboBox();
        jLabel20 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jLabel7 = new javax.swing.JLabel();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        jButton1 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jTextField5 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel21 = new javax.swing.JLabel();
        jTextField12 = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        jTextField13 = new javax.swing.JTextField();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        jPanel4.setBackground(new java.awt.Color(100, 100, 100));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Cronograma de Actividades");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1175, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
        );

        cancelButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/eliminar.png"))); // NOI18N
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/nuevo.png"))); // NOI18N
        jButton3.setToolTipText("Generar Reporte");

        okButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/guardar.png"))); // NOI18N
        okButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                okButtonMouseClicked(evt);
            }
        });

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/borra.png"))); // NOI18N
        jButton5.setToolTipText("Borrar Partida Seleccionada");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jButton5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cancelButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(okButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        getRootPane().setDefaultButton(okButton);

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel2.setText("Nro. Presupuesto:");

        jTextField1.setEditable(false);

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel3.setText("Nro. Partida:");

        jTextField2.setEditable(false);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel4.setText("Código de Partida:");

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel9.setText("Calcular Monto por día:");

        jSpinner1.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(1)));
        jSpinner1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner1StateChanged(evt);
            }
        });

        jTextArea1.setColumns(20);
        jTextArea1.setEditable(false);
        jTextArea1.setFont(new java.awt.Font("Monospaced", 0, 11));
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(2);
        jScrollPane4.setViewportView(jTextArea1);

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel13.setText("Descripción:");

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel11.setText("Rendimiento:");

        jTextField7.setEditable(false);
        jTextField7.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel12.setText("Cantidad:");

        jTextField8.setEditable(false);
        jTextField8.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel14.setText("Precio:");

        jTextField9.setEditable(false);
        jTextField9.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jTextField10.setEditable(false);
        jTextField10.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel15.setText("Total:");

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel16.setText("Total Presupuesto:");

        jTextField11.setEditable(false);
        jTextField11.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tabla", "Gráfico" }));
        jComboBox2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox2ItemStateChanged(evt);
            }
        });

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel20.setText("Mostrar:");

        jTextField3.setEditable(false);
        jTextField3.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel19.setText("No. de días por Partida:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextField3)
                    .addComponent(jSpinner1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBox1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 47, Short.MAX_VALUE)
                        .addComponent(jLabel11))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 367, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel12)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField7, javax.swing.GroupLayout.DEFAULT_SIZE, 99, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel20)
                        .addGap(61, 61, 61)
                        .addComponent(jComboBox2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel16)
                            .addComponent(jLabel15))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextField11, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jTextField10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(11, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel2)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9)
                            .addComponent(jCheckBox1)
                            .addComponent(jLabel11)
                            .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14)
                            .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                    .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel15)
                                    .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel12)
                                    .addComponent(jLabel13)
                                    .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel3))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel19)
                                    .addComponent(jLabel16)
                                    .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel20))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(72, 72, 72)
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 1, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel5.setText("Partida:");

        jTextField4.setEditable(false);
        jTextField4.setToolTipText("Descripción");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel6.setText("Inicio:");

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel7.setText("Terminado:");

        jDateChooser2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jDateChooser2MouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jDateChooser2MousePressed(evt);
            }
        });
        jDateChooser2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jDateChooser2FocusLost(evt);
            }
        });
        jDateChooser2.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                jDateChooser2InputMethodTextChanged(evt);
            }
        });
        jDateChooser2.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser2PropertyChange(evt);
            }
        });

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/añade1.fw.png"))); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jScrollPane3.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        jScrollPane3.setPreferredSize(new java.awt.Dimension(900, 333));

        jPanel6.setAutoscrolls(true);
        jPanel6.setPreferredSize(new java.awt.Dimension(900, 350));

        jPanel7.setAutoscrolls(true);
        jPanel7.setPreferredSize(new java.awt.Dimension(600, 380));

        jScrollPane2.setViewportView(jLabel10);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 724, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 352, Short.MAX_VALUE)
                .addContainerGap())
        );

        jScrollPane1.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                jScrollPane1MouseWheelMoved(evt);
            }
        });
        jScrollPane1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jScrollPane1MouseClicked(evt);
            }
        });

        jTable1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Nombre", "Duración", "Inicio", "Terminado", "Costo"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setAutoscrolls(false);
        jTable1.setSelectionBackground(new java.awt.Color(216, 141, 0));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTable1MousePressed(evt);
            }
        });
        jTable1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTable1KeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 431, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 724, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 363, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 363, Short.MAX_VALUE))
                .addContainerGap())
        );

        jScrollPane3.setViewportView(jPanel6);

        jTextField5.setEditable(false);
        jTextField5.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel8.setText("Costo:");

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/guardar1.fw.png"))); // NOI18N
        jButton2.setToolTipText("Modificar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/valua.fw.png"))); // NOI18N
        jButton4.setToolTipText("Rangos de Tiempo");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel17.setText("Costo por Lapso:");

        jTextField6.setEditable(false);
        jTextField6.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel18.setText("Lapsos:");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Días", "Semanas", "Meses", "Años" }));
        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jLabel21.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel21.setText("Días proyecto:");

        jTextField12.setEditable(false);
        jTextField12.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel22.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel22.setText("Días del Cronograma:");

        jTextField13.setEditable(false);
        jTextField13.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1163, Short.MAX_VALUE)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel21))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField4, javax.swing.GroupLayout.DEFAULT_SIZE, 326, Short.MAX_VALUE)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jTextField13, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel22)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField5, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jDateChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE)))
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(68, 68, 68)
                        .addComponent(jLabel17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField6, javax.swing.GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(98, 98, 98)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox1, 0, 111, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5)
                        .addComponent(jLabel6))
                    .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel7)
                        .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(jTextField13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22)
                    .addComponent(jTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 354, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(947, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
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
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

    
    public void llenacampos(){
        String busca = "SELECT id, fechaini, fechafin, rango, lapso, precunit, precasu, redondeo, rendimi, cantidad"
                + ",descri FROM mppres Where numegrup = '"+partidaselect+"' AND (mpre_id='"+pres+"' "
                + "OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) AND cron=1";
        String id=null, descri="";        
        int redondeo=0, rango=0, lapsos=0;
        float precunit=0, precasu=0, rendimi=0, cantidad = 0;        
        Date fecini=null, fecfin=null;
        
        try {
            Statement st = (Statement) conex.createStatement();
            ResultSet rsts = st.executeQuery(busca);
            while(rsts.next()){
                id = rsts.getString("id");
                fecini = rsts.getDate("fechaini");
                fecfin = rsts.getDate("fechafin");
                rango = rsts.getInt("rango");
                lapsos = rsts.getInt("lapso");
                precunit = rsts.getFloat("precunit");
                precasu = rsts.getFloat("precasu");
                redondeo = rsts.getInt("redondeo");
                rendimi = rsts.getFloat("rendimi");  
                cantidad = rsts.getInt("cantidad");
                descri = rsts.getString("descri");
            }
            float numedias =cantidad/rendimi ;
            jTextField3.setText(String.valueOf(Math.rint(numedias*100)/100));
            jTextArea1.setText(descri);
            jTextField4.setText(id);
            jDateChooser1.setDate(fecini);
            jDateChooser2.setDate(fecfin);
            jSpinner1.setValue(partidaselect);
            if(redondeo==0){
                jTextField5.setText(String.valueOf(precunit));
                costo = precunit;
            }else
            {
                jTextField5.setText(String.valueOf(precasu));
                costo=precasu;
            }
//            jComboBox1.setSelectedIndex(lapsos);
            lapso=lapsos;
            lapso = jComboBox1.getSelectedIndex();
            fechaini=fecini;
            fechafin = fecfin;
            if(lapso==0){
                calculadias();
            //    costolapso = (float) Math.rint(costo/(dias*5)*100)/100;
               
            }
             if(lapso==1){
                calculasemanas();
                
            }
             if(lapso==2){
                 calculames();
             }
             if(lapso==3){
                 calculaanio();
             }
        String select = "SELECT rango FROM mppres WHERE numegrup="+partidaselect+""
                + " AND (mpre_id='"+pres+ "' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"'))";
        try {
            int rangos1=0;
            Statement sts = (Statement) conex.createStatement();
            ResultSet rstss = sts.executeQuery(select);
            while(rstss.next()){
                rangos1 = rstss.getInt(1);
            }
            if(rangos1==0){
                jButton2.setEnabled(true);
            }else
            {
                jButton2.setEnabled(false);
            }
        } catch (SQLException ex) {
            Logger.getLogger(diagrama.class.getName()).log(Level.SEVERE, null, ex);
        }
            jButton4.setEnabled(true);
           
            
            jTextField6.setText(String.valueOf(costolapso));
        } catch (SQLException ex) {
            Logger.getLogger(diagrama.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    public void muevetabla(){
        
        jButton2.setEnabled(true);
        jButton1.setEnabled(false);
        partidaselect = jTable1.getValueAt(filafase, 0).toString();
        jButton4.setEnabled(true);
        String select = "SELECT rango FROM mppres WHERE numegrup="+partidaselect+""
                + " AND (mpre_id='"+pres+ "' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"'))";
        try {
            int rango=0;
            Statement st = (Statement) conex.createStatement();
            ResultSet rsts = st.executeQuery(select);
            while(rsts.next()){
                rango = rsts.getInt(1);
            }
            if(rango==0){
                jButton2.setEnabled(true);
            }else
            {
                jButton2.setEnabled(false);
            }
        } catch (SQLException ex) {
            Logger.getLogger(diagrama.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        llenacampos();         
    }
    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        filafase = jTable1.rowAtPoint(evt.getPoint());
       
   
    tabla.setRowSelectionInterval(filafase, filafase);
   
    partidaselect =tabla.getValueAt(filafase, 0).toString();
    Rectangle r = tabla.getCellRect(tabla.getSelectedRow(),tabla.getSelectedColumn(), false);
    tabla.scrollRectToVisible(r);
    
        muevetabla();     
        jButton1.setEnabled(false);
    }//GEN-LAST:event_jTable1MouseClicked

    private void jSpinner1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner1StateChanged
        String part = jSpinner1.getValue().toString();
        jButton2.setEnabled(false);
        jButton1.setEnabled(true);
        int redondeo = 0;
        float cant = 0, prec = 0;
        System.out.println("cambia");
        lapso = jComboBox1.getSelectedIndex();
        
        String codicove = "", descri = "", rendimi="", cantidad="", precio="", precasu="";
        String sql = "SELECT id, descri, rendimi, cantidad, precunit,precasu, redondeo FROM mppres WHERE numegrup = "+part+" AND (mpre_id='"+pres+"' OR mpre_id IN "
        + "(SELECT id FROM mpres WHERE mpres_id='"+pres+"'))";
        try {
            Statement st = (Statement) conex.createStatement();
            ResultSet rste = st.executeQuery(sql);
            while(rste.next()){
                codicove = rste.getString("id");
                descri = rste.getString("descri");
                rendimi = rste.getString("rendimi");
                precasu = rste.getString("precasu");
                cantidad = rste.getString("cantidad");
                precio = rste.getString("precunit");
                redondeo = rste.getInt("redondeo");
            }
            cant = Float.valueOf(cantidad);
            if(redondeo==0){
            prec = Float.valueOf(precio);
            }else{
                prec = Float.valueOf(precasu);
            }
            prec = cant*prec;
            prec = (float) Math.rint(((prec+0.000001)*100)/100);
            jTextField2.setText(codicove);
            jTextArea1.setText(descri);
            jTextField7.setText(rendimi);
            jTextField4.setText(codicove);
            jTextField5.setText(String.valueOf(prec));
            jTextField8.setText(cantidad);
            jTextField9.setText(precio);
            jTextField10.setText(String.valueOf(prec));
            
            costo  = prec;
            if(lapso==0){
                //Debe verificar Dias habiles, aqui solo se quitan fines de semana
                calculasemanas();
                dias= dias*5; // Dias sin fines de semana 
                costolapso = (float) Math.rint(((costo/dias)*100)/100);
            }
             if(lapso==1){
                calculasemanas();
            }
            jTextField6.setText(String.valueOf(costolapso));
            
        } catch (SQLException ex) {
            Logger.getLogger(diagrama.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jSpinner1StateChanged

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        int edita = 0;
        int rango = 0;
        String numegrup = jSpinner1.getValue().toString();
        String consulta = "Select rango FROM mppres WHERE numegrup="+numegrup+" AND "
                + "(mpre_id='"+pres+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"'))";
        try {
            Statement str = (Statement) conex.createStatement();
            ResultSet rstr = str.executeQuery(consulta);
            while(rstr.next()){
                rango = rstr.getInt(1);                
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(diagrama.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        rangos=1;
        
        String selected = "Select numero FROM mppres WHERE numegrup="+numegrup+" AND "
                + " (mpre_id='"+pres+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"'))";
        String numero=null;
        try {
            Statement st = (Statement) conex.createStatement();
            ResultSet rste = st.executeQuery(selected);
            while(rste.next()){
                numero = rste.getString(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(diagrama.class.getName()).log(Level.SEVERE, null, ex);
        }
         rangos r=null;
         if(rango==0){
             r= new rangos(prin, true, conex, pres, numero);
         }else{
             r= new rangos(prin, true, conex, pres, numero, 1);
         }
        int x = (prin.getWidth()/2)-650/2;
        int y = (prin.getHeight()/2)-350/2;
        r.setBounds(x, y, 650, 420);
        r.setVisible(true);
        String fecini, fecfin;
        lapso = jComboBox1.getSelectedIndex();
        if(r.ok==1){
        fecini = r.fecini1;
        fecfin = r.fecfin5;
        
         if(lapso==0){
                calculadias();               
            }
             if(lapso==1){
                calculasemanas(); 
            }
            jTextField6.setText(String.valueOf(costolapso)); 
            partida = jTextField4.getText().toString();
            String sql = "UPDATE mppres SET fechaini='"+fecini+"', fechafin='"+fecfin+"', cron=1, lapso = "+lapso+", rango=1"
                    + " WHERE id='"+partida+"' AND "
                    + "mpre_id='"+pres+"' AND numegrup='"+numegrup+"'";
             try {
                Statement st = (Statement) conex.createStatement();
                st.execute(sql);
                hacetabla1();
            } catch (SQLException ex) {
                Logger.getLogger(diagrama.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    public void calculadias(){
       
       dias = (fechafin.getTime()-fechaini.getTime())/mil+1;
       System.out.println("costo "+costo+" dias "+dias);
        costolapso = (float) Math.rint(((costo/dias)*100)/100);
        if(costolapso>costo){
            costolapso=costo;
        }
                
    }
    
    public void calculasemanas(){
        float semana1=0, semana2=0;
        Calendar c = Calendar.getInstance();
        Calendar cfin = Calendar.getInstance();
        Calendar paraanio = Calendar.getInstance();
        int numsemana=0;
        DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        c.setTime(fechaini);
        cfin.setTime(fechafin);
        float year1, year2, anios1;
        year1 = c.get(Calendar.YEAR);
        year2 = cfin.get(Calendar.YEAR);
        paraanio.set((int)year1, 0, 1);
        anios1 = year2-year1;
        numsemana= paraanio.getActualMaximum(Calendar.WEEK_OF_YEAR);
        if(anios1==0){
            semana1 = c.get(Calendar.WEEK_OF_YEAR);
            semana2 = cfin.get(Calendar.WEEK_OF_YEAR);
        }else{
            if(anios1>0){
                semana1 = c.get(Calendar.WEEK_OF_YEAR);
                semana2 = cfin.get(Calendar.WEEK_OF_YEAR);
                semana2 = semana2 + numsemana;
            }
        }
        dias = semana2-semana1;
        System.out.println("costo "+costo+" semanas "+dias);
        costolapso = (float) Math.rint(((costo/dias)*100)/100);
        if(costolapso>costo){
            costolapso=costo;
        }
        System.out.println("semanas: "+dias);
    }
    public void calculames(){
        float mes1, mes2;
        Calendar c = Calendar.getInstance();
        Calendar cfin = Calendar.getInstance();
        DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        c.setTime(fechaini);
        cfin.setTime(fechafin);
        mes1 = c.get(Calendar.MONTH);
        mes2 = cfin.get(Calendar.MONTH);
        dias = (mes2-mes1)+1; // Nro. de meses
        
        costolapso = (float) Math.rint(((costo/(dias))*100)/100);
        if(costolapso>costo){
            costolapso=costo;
        }
        System.out.println("semanas: "+dias);
    }
    public void calculaanio(){
        float mes1, mes2;
        Calendar c = Calendar.getInstance();
        Calendar cfin = Calendar.getInstance();
        DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        c.setTime(fechaini);
        cfin.setTime(fechafin);
        mes1 = c.get(Calendar.YEAR);
        mes2 = cfin.get(Calendar.YEAR);
        dias = (mes2-mes1)+1; // Nro. de años
        costolapso = (float) Math.rint(((costo/dias)*100)/100);
        if(costolapso>costo){
            costolapso=costo;
        }
        System.out.println("semanas: "+dias);
    }
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
      
       SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
       String fecini=null, fecfin=null;
       float semanas1=0, meses1=0;
       int contador=0;
        numpartida = jSpinner1.getValue().toString();//numpartida numegrup
       String enc = "SELECT count(*) FROM mppres WHERE numegrup="+numpartida+" "
               + "AND mpre_id='"+pres+"' AND cron=1";
        try {
            Statement sts = (Statement) conex.createStatement();
            ResultSet rsts = sts.executeQuery(enc);
            while(rsts.next()){
                contador = rsts.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(diagrama.class.getName()).log(Level.SEVERE, null, ex);
        }
       if(contador==0){
        if(rangos==0){
            partida= jTextField4.getText().toString(); //CODICOVE
           
            fechaini = jDateChooser1.getDate();
            fechafin = jDateChooser2.getDate();
            lapso = jComboBox1.getSelectedIndex();
            costo = Float.valueOf(jTextField5.getText().toString());
            if(lapso==0){
                calculadias();               
            }
             if(lapso==1){
                calculasemanas();
            }
            jTextField6.setText(String.valueOf(costolapso)); 
            fecini=format.format(fechaini);
            fecfin = format.format(fechafin);
            String sql = "UPDATE mppres SET fechaini='"+fecini+"', fechafin='"+fecfin+"', cron=1, lapso = "+lapso+", rango=0"
                    + " WHERE id='"+partida+"' AND "
                    + " (mpre_id='"+pres+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) AND numegrup='"+numpartida+"'";
            try {
                Statement st = (Statement) conex.createStatement();
                st.execute(sql);
                
                hacetabla1();
                DefaultTableModel dt= (DefaultTableModel) tabla.getModel();
                dt.fireTableDataChanged();
                this.repaint();
                this.validate();
               
            } catch (SQLException ex) {
                Logger.getLogger(diagrama.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
       }else{
           JOptionPane.showMessageDialog(null, "Partida ya fue insertada en el cronograma");
       }
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        lapso = jComboBox1.getSelectedIndex();
        costo = Float.valueOf(jTextField5.getText().toString());
        if(lapso==0){
            calculadias();
            hacetabla1();
        }
         if(lapso==1){
                calculasemanas();
                hacetabla2();
            }
         if(lapso==2){
             calculames();
             hacetabla3();
         }
         if(lapso==3){
             calculaanio();
             hacetabla4();
         }
         System.out.println("costolapso= "+costolapso);
        jTextField6.setText(String.valueOf(costolapso));
        
    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void jDateChooser2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jDateChooser2FocusLost
       
    }//GEN-LAST:event_jDateChooser2FocusLost

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        
        Date fecini, fecfin;
        String fechai, fechaf;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        fecini = jDateChooser1.getDate();
        fecfin = jDateChooser2.getDate();
        fechai = format.format(fecini);
        fechaf = format.format(fecfin);
        String update = "UPDATE mppres SET fechaini='"+fechai+"', fechafin ='"+fechaf+"' "
                + "WHERE numegrup="+partidaselect+ " AND (mpre_id='"+pres+"' OR (SELECT id FROM mpres WHERE mpres_id='"+pres+"'))";     
        try {
            Statement stupdate = (Statement) conex.createStatement();
            stupdate.execute(update);
            lapso = jComboBox1.getSelectedIndex();
            creamodelo();
            if(lapso==0){
            hacetabla1();
            }
            if(lapso==1){
                hacetabla2();
            }
            if(lapso==2){
                hacetabla3();
            }
            JOptionPane.showMessageDialog(this, "Se modificó la partida");
        } catch (SQLException ex) {
            Logger.getLogger(diagrama.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTable1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MousePressed
       
    }//GEN-LAST:event_jTable1MousePressed

    private void jTable1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable1KeyReleased
        System.out.println(evt.getKeyCode());
            int code = evt.getKeyCode();
            if(code==38 || code==40){
                filafase = jTable1.getSelectedRow();
                muevetabla();                     
            }  
    }//GEN-LAST:event_jTable1KeyReleased
 
    
    
    
    private void jDateChooser2MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jDateChooser2MousePressed
         
        lapso = jComboBox1.getSelectedIndex();
            costo = Float.valueOf(jTextField5.getText().toString());
            if(lapso==0){//DIAS
                calculadias();
                hacetabla1();
            }
            if(lapso==1){
                calculasemanas();
                
            }
            if(lapso==2){
                calculames();
            }
            jTextField6.setText(String.valueOf(costolapso));
        
    }//GEN-LAST:event_jDateChooser2MousePressed

    private void jDateChooser2PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser2PropertyChange
        
        
        
    }//GEN-LAST:event_jDateChooser2PropertyChange

    private void jDateChooser2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jDateChooser2MouseClicked
      
    }//GEN-LAST:event_jDateChooser2MouseClicked

    private void jDateChooser2InputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_jDateChooser2InputMethodTextChanged
         lapso = jComboBox1.getSelectedIndex();
            costo = Float.valueOf(jTextField5.getText().toString());
            if(lapso==0){//DIAS
                calculadias();               
            }
            if(lapso==1){
                calculasemanas();
            }
            jTextField6.setText(String.valueOf(costolapso));
    }//GEN-LAST:event_jDateChooser2InputMethodTextChanged

    private void jComboBox2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox2ItemStateChanged

        if(jComboBox2.getSelectedIndex()==1){
        tabla.setVisible(false);
        scroll.setVisible(false);
        hacetabla();
        }else{
            tabla.setVisible(true);
        scroll.setVisible(true);
        jLabel10.setIcon(null);
       
        }        
    }//GEN-LAST:event_jComboBox2ItemStateChanged

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jScrollPane1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jScrollPane1MouseClicked
System.out.println("Si soy el de la table 1");        // TODO add your handling code here:
    }//GEN-LAST:event_jScrollPane1MouseClicked

    private void jScrollPane1MouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_jScrollPane1MouseWheelMoved
     System.out.println("Si soy el de la table 1");      // TODO add your handling code here:
    }//GEN-LAST:event_jScrollPane1MouseWheelMoved

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        int filaselect = jTable1.getSelectedRow();
        String id = jTable1.getValueAt(filaselect, 0).toString();
        String sql = "UPDATE mppres SET cron=0, lapso=0, rango=0 WHERE "
                + "numegrup="+id+" AND mpre_id='"+pres+"' OR mpre_id IN "
                + "(SELECT id FROM mpres WHERE mpres_id='"+pres+"')";
        int op=JOptionPane.showConfirmDialog(null, "¿Desea quitar la partida del cronograma?");
        if(op == JOptionPane.YES_OPTION){
            try {
                Statement st = (Statement) conex.createStatement();
                st.execute(sql);
                JOptionPane.showMessageDialog(null, "Se ha quitado la partida del cronograma");
              
        
        hacetabla1();
            } catch (SQLException ex) {
                Logger.getLogger(diagrama.class.getName()).log(Level.SEVERE, null, ex);
            }
                    
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void okButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_okButtonMouseClicked
        doClose(RET_OK);        // TODO add your handling code here:
    }//GEN-LAST:event_okButtonMouseClicked

    private void doClose(int retStatus) {
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
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private com.toedter.calendar.JDateChooser jDateChooser2;
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
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField12;
    private javax.swing.JTextField jTextField13;
    private javax.swing.JTextField jTextField2;
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
