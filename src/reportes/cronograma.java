/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package reportes;

import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.builders.FastReportBuilder;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import java.sql.Connection;
import com.mysql.jdbc.Statement;
import java.awt.Color;
import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Betmart
 */
public class cronograma {
    Style detail=new Style();
    String pres;
    int numsemana=0;
     String [] semanas, semanasSN, vecmes, vecanio, vecmeses,vecmesanio;
    private Connection conex;
          int numdias=0, numsemanas=0, numeses=0;
        float totaldias=0;
     float dias;
      DynamicReport dynamicReport;
    FastReportBuilder frb = new FastReportBuilder();
    int lapso=0;
    Document document= new Document( PageSize.LEGAL.rotate());
    String[][] matrizpartidas;
     SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
     Date fechaini=null, fechafin=null;
       String[] fechasdias, semanaanio;
         String[] diasemana, semanames;
          Object [][] matriz;
          PdfWriter writer;
    private String[] partidas;
    Date fecha;
    String nombrearchivo="";
    BigDecimal totalpres;
    FileOutputStream file;
       float semana = 0, meses=0, anios=0;
        float costo=0, costolapso=0;
          final long mil = 86400000 ; 
          String ruta;
          int confecha=0;
          int conprecio=0;
    public cronograma(Connection conex, String mpres, int lapso, int confecha, Date fecha, BigDecimal totalpres, int conprecio, String ruta){
        detail.setBackgroundColor(Color.DARK_GRAY);
        this.pres=mpres;
        this.conprecio=conprecio;
        this.ruta=ruta;
        this.fecha=fecha;
        this.totalpres=totalpres;
      this.conex=conex;
      this.confecha = confecha; //--- 0 imprime S1, S2,..., SN / 1 imprime fecha de la semana
        this.lapso=lapso; //0 dias, 1 semanas, 2 meses, 3 años
        try {
            try {
                Date date = new Date();
                String fechanombre;
                DateFormat hourdateFormat = new SimpleDateFormat("HHmmssddMMyyyy");
                fechanombre = hourdateFormat.format(date);
                nombrearchivo=ruta+"/cronograma"+fechanombre+".pdf";
             file=   new FileOutputStream(nombrearchivo);
               writer= PdfWriter.getInstance(document, file);
             writer.setPageEvent(new cabecera(conex,mpres,document, fecha));
             document.setMargins(36, 36, 150, 100);
                document.open();
           
            } catch (DocumentException ex) {
                Logger.getLogger(cronograma.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(cronograma.class.getName()).log(Level.SEVERE, null, ex);
        }
        hacercabecera();
      
      
    }
    public final void hacercabecera(){
        if(lapso==0){
       dias();
        }
    }
    
  public void calculasemanas(){
        float semana1=0, semana2=0;
        Calendar c = Calendar.getInstance();
        Calendar cfin = Calendar.getInstance();
        Calendar paraanio = Calendar.getInstance();
        
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
   
   
  //Llena el vector de fechas por semanas, en el vector guarda la fecha del lunes de cada semana
    public void numdia(){
        int numdia=0;
        
        int dif=0;
        String formateada=null;
        Calendar calen = Calendar.getInstance();
        
        Date fecsem1=null, fechsum=null;
        Calendar calendario = Calendar.getInstance();
        calendario.setTime(fechaini);
        numdia = calendario.get(Calendar.DAY_OF_WEEK);
        dif = numdia-2;
        numsemana++;
        if(dif==0){
            formateada = formato.format(fechaini);
            semanas[0]=formateada;
           semanasSN[0]="S"+numsemana;
            fecsem1 = fechaini;
        }else{
            long tiempoactual = fechaini.getTime();
            long difdias = dif*24*60*60*1000;
            fecsem1 = new Date(tiempoactual+(-1)*difdias);
            formateada = formato.format(fecsem1);
            semanas[0]=formateada;
            semanasSN[0]="S"+numsemana;
        }
        for(int i=1;i<semana;i++){
            calen.setTime(fecsem1);
            calen.add(Calendar.DATE, 7); //Suma 7 dias, los dias de la semana, a partir de la fecha menor
            fecsem1 = calen.getTime();
            formateada = formato.format(fecsem1);
             numsemana++;
            semanasSN[i]="S"+numsemana;
            semanas[i]=formateada;
           
        }
       
    }
    
     //Guarda dos vectores paralelos donde se almacena en uno los dias de L a V y en el otro las respectivas fechas
     public void vectordias(){
        SimpleDateFormat formatoDelTexto = new SimpleDateFormat("dd/MM/yyyy");
        String[] letradias={"L","M","M","J","V","S","D"};
        Calendar cal = Calendar.getInstance();
        String formateada=null;
        Date fechas =null;
        try {
            fechas = formatoDelTexto.parse(semanas[0]);
        } catch (ParseException ex) {
            Logger.getLogger(cronograma.class.getName()).log(Level.SEVERE, null, ex);
        }
        int i=0, j=0;
        int numdia = (int) (semana*7);
        while(i<numdia){
            j=0;
            while(j<7){
                diasemana[i]=letradias[j]; //Guarda el los dias de la semana
                cal.setTime(fechas);
                
                fechas = cal.getTime();
                formateada = formatoDelTexto.format(fechas);
                fechasdias[i]=formateada; //Guarda las fechas correspondientes a cada dia.
                cal.add(Calendar.DATE, 1);
                fechas=cal.getTime();
                j++;
                i++;
            } 
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
    
    public String[][] iniciamatrizpartidas(int partidas, String[][] matrizpartidas){
        
        int columnas=0;
        //Inicia la matriz en 0 para dias habiles y 2 para fines de semana
      if(conprecio==0){
          columnas=3;
      }if(conprecio==1){
          columnas=4;
      }
        for(int i=0; i<partidas;i++){
            for(int j=0;j<columnas;j++){
               matrizpartidas[i][j]="";
            }
        }
        return matrizpartidas;
    }
    
    //--------------------------------  NUEVO METODO PARA IMPRIMIR POR DIAS SALTANDO PAGINA CADA 4 SEMANAS
    public final void dias()
    {
         SimpleDateFormat formatoDelTexto = new SimpleDateFormat("dd/MM/yyyy");
           SimpleDateFormat formateo = new SimpleDateFormat("yyyy-MM-dd");
        Date inicio;
        int nomas=0;
        try {
            String selectfecha = "SELECT fechaini FROM mppres WHERE (mpre_id='"+pres+"' "
                    + "OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) AND cron=1 ORDER BY fechaini LIMIT 1";
            Statement st = (Statement) conex.createStatement();
            ResultSet rst = st.executeQuery(selectfecha);
            while(rst.next()){
               fechaini= rst.getDate(1);
            }
            int numpartidas=0;
            String cuantatotalcrono = "SELECT COUNT(*) FROM mppres WHERE (mpre_id='"+pres+"'"
                    + "OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) AND cron=1";
            Statement stcrono = (Statement) conex.createStatement();
            ResultSet rstcrono = stcrono.executeQuery(cuantatotalcrono);
            rstcrono.first();
            int totalcrono = rstcrono.getInt(1);
            
            String selecfechafin = "SELECT fechafin FROM mppres WHERE (mpre_id='"+pres+"'"
                    + "OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) AND cron=1 ORDER BY fechafin DESC LIMIT 1";
            Statement stfin = (Statement) conex.createStatement();
            ResultSet rstfin = stfin.executeQuery(selecfechafin);
            rstfin.first();
            Date ultimafecha=rstfin.getDate(1);
            while(fechaini.before(ultimafecha)){
           if(nomas>0){
               document.newPage();
           }
                nomas++;
            semana=6;
             numdias = (int) (semana*7);
            semanas = new String[(int) semana];
            semanasSN = new String[(int) semana];
            fechasdias = new String[numdias];
            diasemana = new String[numdias];
         String nuevoprimerlunes="";
             numdia();
             Calendar cal = Calendar.getInstance();
             Date primerlunes = null;
            try {
                primerlunes = formatoDelTexto.parse(semanas[0]);
                nuevoprimerlunes=formateo.format(primerlunes);
                primerlunes = formateo.parse(nuevoprimerlunes);
                
            } catch (ParseException ex) {
                Logger.getLogger(cronograma.class.getName()).log(Level.SEVERE, null, ex);
            }
           numpartidas=0;
            cal.setTime(primerlunes);
            cal.add(Calendar.DATE,42);
            String fecfinstring="";
                try {
                    fecfinstring = formateo.format(cal.getTime());
                    fechafin = formateo.parse(fecfinstring);
                } catch (ParseException ex) {
                    Logger.getLogger(cronograma.class.getName()).log(Level.SEVERE, null, ex);
                }
           String consultapartidas ="SELECT COUNT(*)"
                + " FROM mppres"
                + " WHERE cron=1 AND (mpre_id='"+pres+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) AND"
                   + " ((('"+nuevoprimerlunes+"' BETWEEN  fechaini AND fechafin) OR ('"+fecfinstring+"' BETWEEN fechaini "
                   + "AND fechafin)) OR ((fechaini BETWEEN '"+nuevoprimerlunes+"' AND '"+fecfinstring+"') OR "
                   + "(fechafin BETWEEN '"+nuevoprimerlunes+"' AND  '"+fecfinstring+"')))"
                + "ORDER BY fechaini";
           System.out.println(consultapartidas);
           Statement stnumpartida = (Statement) conex.createStatement();
           ResultSet rstnumpartida = stnumpartida.executeQuery(consultapartidas);
           
           while(rstnumpartida.next()){
               numpartidas = rstnumpartida.getInt(1);
           }
           if(numpartidas>0){
              matriz= new Object[numpartidas][numdias];
              vectordias();           
            iniciamatriz(numpartidas,numdias); // Matriz de fechas
            if(conprecio==1){
                matrizpartidas = new String[numpartidas][4];
            }
            else{
                matrizpartidas = new String[numpartidas][3];
            }
            matrizpartidas=iniciamatrizpartidas(numpartidas, matrizpartidas);
            //CONSULTAR PARTIDAS fechaini, fechafin, cron, lapso, rango, numegrup, CONCAT(id, '\n', descri) as descri, unidad
            String select ="SELECT fechaini, fechafin, cron, lapso, rango, numegrup, CONCAT(id, '\n', descri) as descri,"
                    + " unidad, IF(precasu=0, precunit,precasu) as precunit"
                   + " FROM mppres"
                + " WHERE cron=1 AND (mpre_id='"+pres+"' OR mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"')) AND"
                   + " ((('"+nuevoprimerlunes+"' BETWEEN  fechaini AND fechafin) OR ('"+fecfinstring+"' BETWEEN fechaini "
                   + "AND fechafin)) OR ((fechaini BETWEEN '"+nuevoprimerlunes+"' AND '"+fecfinstring+"') OR "
                   + "(fechafin BETWEEN '"+nuevoprimerlunes+"' AND  '"+fecfinstring+"')))"
                + "ORDER BY fechaini";
            //---------------LLENAR MATRIZ PARA IMPRIMIR REPORTE--------------------------------------------------
            matrizpartidas=llenapartidas(select,matrizpartidas);
             llenacrono(select,fechaini,fechafin);
            for(int i=0;i<matrizpartidas.length;i++){
                System.out.print(matrizpartidas[i][0]+" ");
                System.out.print(matrizpartidas[i][1]+" ");
                System.out.print(matrizpartidas[i][2]+" ");
                for(int j=0;j<matriz[i].length;j++){
                    System.out.print(matriz[i][j]+" ");
                }
                System.out.println();
            }
           //--------------------------IMPRIMIR REPORTE-----------------------------------------------------------
            agrupacolumna(numpartidas);
            cal.setTime(fechafin);
            cal.add(Calendar.DATE, 1);
            fechaini=fechafin;
           }
            
            }
          
            
            document.close();
            File fpdf = new File(nombrearchivo);
            try {
                Desktop.getDesktop().open(fpdf);
            fpdf.deleteOnExit();
               
            } catch (IOException ex) {
                Logger.getLogger(cronograma.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(cronograma.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void agrupacolumna(int numpartidas){//Para Dias
    String[] letradias={"L","M","M","J","V","S","D"};     
     int recor=0, primerrecor=0;
     int semana1 = (int) semana;
     int numceldas=0;
     int empieza;
     if(conprecio==0){
      numceldas = 45;
      empieza=3;
     }else{
         empieza=4;
         numceldas=46;
     }
     int dividido=0;
     float [] anchos=new float[numceldas];
     anchos[0]=4f;
     anchos[1]=100f;
     anchos[2]=7f;
     if(numceldas==32){
         anchos[3]=30f;
         anchos[1]=70f;
     }
     float anchoreport = (float) (document.getPageSize().getWidth()*0.9);
     float anchoceldas = (anchoreport/(numceldas-empieza))/10;
     //JOptionPane.showMessageDialog(null, anchoceldas);
     for(int i=empieza;i<numceldas;i++){
         anchos[i]=(float) (anchoceldas-new Double(0.80));
     }
     
     
     PdfPTable table = new PdfPTable(anchos); 
     table.setWidthPercentage(100);
       
     PdfPCell celda = new PdfPCell();
     Paragraph p1 = new Paragraph("Nro", FontFactory.getFont(BaseFont.TIMES_ROMAN,10, Font.BOLD));
     p1.setAlignment(Element.ALIGN_CENTER);
     celda.addElement(p1);
     celda.setRowspan(2);
     table.addCell(celda);
     PdfPCell celda1= new PdfPCell();
       Paragraph p2 = new Paragraph("Código y Nombre", FontFactory.getFont(BaseFont.TIMES_ROMAN,10, Font.BOLD));
     p2.setAlignment(Element.ALIGN_CENTER);
     celda1.addElement(p2);
     celda1.setRowspan(2);
     table.addCell(celda1);
     PdfPCell celda2= new PdfPCell();
      Paragraph p3 = new Paragraph("Unidad", FontFactory.getFont(BaseFont.TIMES_ROMAN, 10, Font.BOLD));
     p3.setAlignment(Element.ALIGN_CENTER);     
     celda2.addElement(p3);     
     celda2.setRowspan(2);
     table.addCell(celda2);
     if(conprecio==1){
         PdfPCell celda3= new PdfPCell();
          Paragraph p4 = new Paragraph("Monto por Lapso", FontFactory.getFont(BaseFont.TIMES_ROMAN, 10, Font.BOLD));
         p4.setAlignment(Element.ALIGN_CENTER);     
         celda3.addElement(p4);     
         celda3.setRowspan(2);
         table.addCell(celda3);
     }
     
     
     
     for(int i=0;i<6;i++){    
         //Haciendo cabecera
         PdfPCell celdasemana = new PdfPCell();
         if(confecha==0){
             Paragraph p5=new Paragraph(semanasSN[i], FontFactory.getFont(BaseFont.TIMES_ROMAN, 7, Font.BOLD));
             p5.setAlignment(Element.ALIGN_CENTER);
            celdasemana.addElement(p5);         
         }else{
              Paragraph p5=new Paragraph(semanas[i], FontFactory.getFont(BaseFont.TIMES_ROMAN,7, Font.BOLD));
             p5.setAlignment(Element.ALIGN_CENTER);
              celdasemana.addElement(p5);  
         }
         celdasemana.setColspan(7);
         table.addCell(celdasemana);             
     }  int cuentaceldas=0;
    for(int i=0; i<6;i++){
        for(int j=0;j<7;j++){
            PdfPCell dia = new PdfPCell();
            Paragraph pi = new Paragraph(letradias[j], FontFactory.getFont(BaseFont.TIMES_ROMAN, 7, Font.BOLD));
            pi.setAlignment(Element.ALIGN_CENTER);
             dia.addElement(pi);
             table.addCell(dia);
        }
    }
     for(int i=0;i<numpartidas;i++){ 
          cuentaceldas=0;
         PdfPCell Partida = new PdfPCell();
         Paragraph nro = new Paragraph(matrizpartidas[i][0], FontFactory.getFont(BaseFont.TIMES_ROMAN, 7, Font.BOLD));
         nro.setAlignment(Element.ALIGN_CENTER);
             Partida.addElement(nro); // Nro Partida
             table.addCell(Partida);
             cuentaceldas++;
             Partida = new PdfPCell();
             Paragraph descri = new Paragraph(matrizpartidas[i][1], FontFactory.getFont(BaseFont.TIMES_ROMAN,7, Font.BOLD));
             descri.setAlignment(Element.ALIGN_JUSTIFIED);
             Partida.addElement(descri); // Descri Partida + Codicove
             table.addCell(Partida);
             cuentaceldas++;
             Partida = new PdfPCell();
             Paragraph unidad = new Paragraph(matrizpartidas[i][2], FontFactory.getFont(BaseFont.TIMES_ROMAN, 7, Font.BOLD));
             unidad.setAlignment(Element.ALIGN_CENTER);
             Partida.addElement(unidad); // Unidad de Medida
             table.addCell(Partida);
             cuentaceldas++;
             if(conprecio==1){
                 Partida = new PdfPCell();
                 Double var = Math.rint(Double.valueOf(matrizpartidas[i][3])*100)/100;
                 matrizpartidas[i][3]=String.valueOf(var);
             Paragraph precio = new Paragraph(matrizpartidas[i][3], FontFactory.getFont(BaseFont.TIMES_ROMAN, 7, Font.BOLD));
             precio.setAlignment(Element.ALIGN_RIGHT);
             Partida.addElement(precio); // Precio Por Lapso
             Partida.setPaddingRight(5);
             table.addCell(Partida);
             cuentaceldas++;
             }
     for(int j=0;j<matriz[i].length;j++)
     { //Deberia ser 42
         
            if(Integer.parseInt(matriz[i][j].toString())==1){
             PdfPCell dia = new PdfPCell();
             dia.setBackgroundColor(BaseColor.GRAY);
             table.addCell(dia);
             cuentaceldas++;
            }
            if(Integer.parseInt(matriz[i][j].toString())==2){
             PdfPCell dia = new PdfPCell();
             dia.setBackgroundColor(BaseColor.LIGHT_GRAY);
             table.addCell(dia);
             cuentaceldas++;
            }
            if(Integer.parseInt(matriz[i][j].toString())==0){
             PdfPCell dia = new PdfPCell();
             dia.setBackgroundColor(BaseColor.WHITE);
             table.addCell(dia);
             cuentaceldas++;
            }
         }
     }
     System.out.println("Cuenta celdas "+cuentaceldas);
     table.setHeaderRows(2);
    
        try {
            document.add(table);
           
          
        } catch (DocumentException ex) {
            Logger.getLogger(cronograma.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }  
//LLENA MATRIZ DE FECHAS DEL CRONOGRAMA------------USO LA VARIABLE Object[][] MATRIZ----------------------------------
    public void llenacrono(String consulta, Date fechaini, Date fechafin)
    {
       SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Statement st = (Statement) conex.createStatement(); 
            ResultSet rste = st.executeQuery(consulta);
            int num = 0;
              Date fecini=null, fecfin=null, fecini1=fechaini, fecfin1 ;
        int cron=0, lapsos=0, rango=0, i=0;
               String formini, formfin;
            
              String numegrup; 
              
                int enc=0;
                rste.first();
                
                do{
                  
                        fecini1 = rste.getDate("fechaini");
                        fecfin1 = rste.getDate("fechafin");
                        formini = formato.format(fecini1);
                        formfin = formato.format(fecfin1);
                                
                    numegrup = rste.getString("numegrup"); 
                    rango = rste.getInt("rango");
                    if(rango==0){
                        //pinta seguido 1 de fecha ini a fechafin
                        int n=0;
                        int posdia=0;
                        enc=0;
                        
                         int enc2=0;
                        while(fecini1.before(fecfin1)&& enc==0 && enc2==0){
                            formini = format.format(fecini1);
                        enc=0; enc2=0;
                        n=0; 
                        while(n<numdias && enc==0){
                            if(formini.equals(fechasdias[n])){
                                matriz[num][n]=new Integer(1);
                               
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
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(fecini1);
                        cal.add(Calendar.DATE, 1);
                        fecini1=cal.getTime();
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
                                + "AND (rc.mpre_id='"+pres+"' OR rc.mpre_id IN (SELECT id FROM mpres WHERE mpres_id)) "
                                + "AND (mp.mpre_id='"+pres+"' OR "
                                + "mp.mpre_id IN (SELECT id FROM mpres WHERE mpres_id='"+pres+"'))";
                        Statement ste = (Statement) conex.createStatement();
                        ResultSet rsts = ste.executeQuery(selec);
                        while(rsts.next()){
                            conta = rsts.getInt(1);
                        }
                        fechini = new String[conta];
                        fechfin = new String[conta];
                        String sele = "SELECT rc.fechaini, rc.fechafin FROM rcppres as rc, mppres as mp"
                                + " WHERE mp.numegrup="+numegrup+" AND rc.mppres_id=mp.numero "
                                + "AND rc.mpre_id='"+pres+"' AND mp.mpre_id='"+pres+"'";
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
                           formfin = fechfin[in];
                           enc=0;
                           //RECORRER DE FECHAINI DEL RANGO A FECHAFIN
                           //TRANSFORMAR EN DATE PARA COMPROBAR QUE ESTE ANTES QUE FECHAFIN
                           Date inicio = null, fin = null;
                           String auxini, auxfin;
                        try {
                            inicio = formato.parse(formini);
                            fin = formato.parse(formfin);
                            auxini = format.format(inicio);
                            
                        } catch (ParseException ex) {
                            Logger.getLogger(cronograma.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        int enc2=0;
                          while(inicio.before(fin)&& enc2==0 &&enc==0){
                              formini = formato.format(inicio);
                              enc=0; enc2=0; n=0;
                            while(n<numdias && enc==0){
                                if(formini.equals(fechasdias[n]))
                                {
                                    matriz[num][n]=new Integer(1);
                                    enc2=0;
                                    int n2=n+1;
                                    enc=1;
                                
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
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(inicio);
                
                            inicio = cal.getTime();
                          
                            cal.add(Calendar.DATE, 1);
                            inicio=cal.getTime();
                            
                          }
                        in++;
                    }
                    }
                    num++;
                    
                   i++; 
                    
                }while(rste.next());
        } catch (SQLException ex) {
            Logger.getLogger(cronograma.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    //---------------------------------------------------------------------------------------------
    
    
    //-------LLENA EL VECTOR DE PARTIDAS PARA IMPRIMIR EN EL REPORTE POR CADA PARTIDA EN CIERTO RANGO DE FECHAS
    public String[][] llenapartidas(String consulta, String[][] matrizpartidas){
        try {
            Statement st = (Statement) conex.createStatement();
            ResultSet rst = st.executeQuery(consulta);
            Double montopartida;
            Date fechini, fechfin;
            int i=0;
            while(rst.next()){
                matrizpartidas[i][0]=rst.getString("numegrup"); //Posición 0 es el numero de partida
                matrizpartidas[i][1]=rst.getString("descri"); //Posición 1 es descripción concatenado con codicove
                matrizpartidas[i][2]=rst.getString("unidad"); //Posición 2 es la unidad de medida
                fechini = rst.getDate("fechaini");
                fechfin = rst.getDate("fechafin");
                montopartida=rst.getDouble("precunit");
                Calendar cal1 = Calendar.getInstance();
                cal1.setTime(fechini);
                Calendar cal2 = Calendar.getInstance();
                cal2.setTime(fechfin);
                if(conprecio==1){
                    if(lapso==0){
                        int dia=cal2.get(Calendar.DAY_OF_YEAR)-cal1.get(Calendar.DAY_OF_YEAR);
                          matrizpartidas[i][3]=(new BigDecimal(montopartida).divide(new BigDecimal(dia),2, BigDecimal.ROUND_HALF_UP)).toString();//Monto por día
                    }
                }
            
                i++;
            }
        } catch (SQLException ex) {
            Logger.getLogger(cronograma.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return matrizpartidas;
    }
    
    
    
//-----------------------------------------------------------------------------------------------------------
    
    public final void modelotabla(){
         
        Date fecini=null, fecfin=null, fecini1=null, fecfin1 = null, fecfin2=null, fecini2=null;
        int cron=0, lapsos=0, rango=0;
        String numegrup="";
        int cant = 0, ix =0;
        int numpartidas=0;
      
     
        String select = "SELECT fechaini, fechafin, cron, lapso, rango, numegrup, CONCAT(id, '\n', descri) as descri, unidad"
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
            if(semana<4){
                semana=4;
            }
            numdias = (int) (semana*7);
            semanas = new String[(int) semana];
            semanasSN = new String[(int) semana];
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
            agrupacolumna(numpartidas);
            iniciamatriz(numpartidas,numdias);
            System.out.println("numdias="+numdias);
            
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
        
            }
            
           
        } catch (SQLException ex) {
            Logger.getLogger(cronograma.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        
        
     

    }


    
}
