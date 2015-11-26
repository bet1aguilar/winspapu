/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package presupuestos;

import com.mysql.jdbc.Statement;
import config.Redondeo;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;



public class modificaparametro extends Thread{
    private Connection conex;
    Presupuesto obj;
    String idpres;
    Boolean mientras=true;
    Redondeo redon= new Redondeo();
    int valormano=0, valormaterial=0, valorequipo=0;
    BigDecimal [] vectormano, vectormat, vectorequip, totalpartida;
    String [] partidamano, partidamat, partidaequip, partida;
    String [][] partidas;
    BigDecimal presta, admin, finan, util, impart, imptotal;
    int posmano=0, posmat=0, posequip=0, pospart=0, postotal=0;
    
    public modificaparametro(Connection conex, Presupuesto obj, String idpres, BigDecimal prestaciones, BigDecimal admin, BigDecimal finan, BigDecimal util, BigDecimal imppart, BigDecimal imptotal){
        this.conex = conex;
        this.obj = obj;
        this.idpres = idpres;
        this.presta= prestaciones;
        this.admin = admin;
        this.finan = finan;
        this.util = util;
        this.impart = imppart;
        this.imptotal = imptotal;
    }
    public void calculamano(){
        BigDecimal valor=new BigDecimal("0.00"), salario, cantidad, bono, subsid, prestaciones;
       BigDecimal rendimi=new BigDecimal("0.00");
        int numeroreg = 0;
        String sql = "SELECT SUM(dm.salario*dm.cantidad), SUM(dm.cantidad), mo.bono, mo.subsid, dm.mppre_id, mp.rendimi"
                + " FROM mmopres as mo, dmoppres as dm, mppres as mp WHERE dm.mpre_id=mo.mpre_id AND "
                + " dm.mmopre_id = mo.id  AND dm.mpre_id='"+idpres+"' "
                + " AND dm.mppre_id = mp.id GROUP BY dm.mppre_id";
        String coun = "SELECT count(DISTINCT mppre_id) FROM dmoppres WHERE mpre_id='"+idpres+"'";
        try {
            Statement st = (Statement) conex.createStatement();
            ResultSet rst = st.executeQuery(sql);
            
            Statement cuentamelo = (Statement) conex.createStatement();
            ResultSet cuentst = cuentamelo.executeQuery(coun);
            
            
            while(cuentst.next()){
                numeroreg = Integer.parseInt(cuentst.getObject(1).toString());
            }
            vectormano = new BigDecimal[numeroreg];
            partidamano = new String[numeroreg];
            while(rst.next()){
                
                partidamano[posmano] = rst.getObject(5).toString();
                rendimi = rst.getBigDecimal(6);
                valor = rst.getBigDecimal(1);
                cantidad = rst.getBigDecimal(2);
                bono = rst.getBigDecimal(3);
                subsid =rst.getBigDecimal(4);
                prestaciones =redon.redondearDosDecimales(valor.multiply(presta.divide(new BigDecimal("100")))); 
                bono = redon.redondearDosDecimales(cantidad.multiply(bono));
                subsid = redon.redondearDosDecimales(cantidad.multiply(subsid));
                valor = valor.add(prestaciones).add( bono).add( subsid);               
                valor = valor.divide(rendimi,2,BigDecimal.ROUND_HALF_UP);
                vectormano[posmano]= valor;
                System.out.println("valor mano "+valor);
                posmano++;
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(modificaparametro.class.getName()).log(Level.SEVERE, null, ex);
        }  
    }
    public void calcularmat(){
        int nofilas = 0;
        String sql = "SELECT SUM(IF(mp.desperdi>0,(mp.desperdi*dm.cantidad*mp.precio),(dm.cantidad*mp.precio))) as total, dm.mppre_id FROM  dmpres as dm,"
                + "mmpres as mp WHERE dm.mpre_id=mp.mpre_id AND dm.mmpre_id=mp.id AND dm.mpre_id='"+idpres+"' GROUP BY dm.mppre_id";
        String filas = "SELECT count(DISTINCT mppre_id) FROM dmpres WHERE mpre_id='"+idpres+"'";
        try {
            Statement st = (Statement) conex.createStatement();
            ResultSet rst = st.executeQuery(sql);
            Statement stfilas = (Statement) conex.createStatement();
            ResultSet rstfilas = stfilas.executeQuery(filas);
            
            while(rstfilas.next()){
                nofilas = Integer.parseInt(rstfilas.getObject(1).toString());
                
            }
            vectormat = new BigDecimal[nofilas];
            partidamat = new String[nofilas];
            
            while(rst.next()){
                partidamat[posmat]=rst.getString(2);
                vectormat[posmat]= rst.getBigDecimal(1);
                posmat++;
            }
        } catch (SQLException ex) {
            Logger.getLogger(modificaparametro.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void calcularequip(){
        int filas=0;
        String sql = "SELECT SUM(de.cantidad*me.deprecia*de.precio)/mp.rendimi as total, de.mppre_id FROM deppres as de,"
                + "mepres as me, mppres as mp WHERE de.mpre_id=me.mpre_id AND de.mepre_id=me.id AND"
                + " de.mpre_id='"+idpres+"' AND de.mppre_id=mp.id GROUP BY de.mppre_id";
        String conta = "SELECT count(DISTINCT mppre_id) FROM deppres WHERE mpre_id ='"+idpres+"'";
        try {
            Statement str1 = (Statement) conex.createStatement();
            ResultSet rstr1 = str1.executeQuery(sql);
            Statement str2 = (Statement) conex.createStatement();
            ResultSet rstr2 = str2.executeQuery(conta);
            while(rstr2.next()){
                filas = Integer.parseInt(rstr2.getObject(1).toString());
            }
            vectorequip = new BigDecimal[filas];
            partidaequip = new String[filas];
            
            while(rstr1.next()){
                partidaequip[posequip]=rstr1.getObject(2).toString();
                vectorequip[posequip]=rstr1.getBigDecimal(1);
                posequip++;
            }
        } catch (SQLException ex) {
            Logger.getLogger(modificaparametro.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    public void llenarpartida(){
        int numfila=0;
        String sql ="SELECT (count(id)) FROM mppres where mpre_id='"+idpres+"' OR mpre_id IN (SELECT "
                + "id FROM mpres WHERE mpre_id='"+idpres+"')";
        String partid = "SELECT id, cantidad FROM mppres WHERE mpre_id='"+idpres+"' OR mpre_id IN (SELECT "
                + "id FROM mpres WHERE mpre_id='"+idpres+"')";
        try {
            Statement rs = (Statement) conex.createStatement();
            ResultSet rst = rs.executeQuery(partid);
            Statement filas = (Statement) conex.createStatement();
            ResultSet rsfilas = filas.executeQuery(sql);
            
            while(rsfilas.next()){
                numfila=Integer.parseInt(rsfilas.getObject(1).toString());
            }
            
            
            partidas = new String[2][numfila];
            
            while(rst.next()){
                partidas[0][pospart] = rst.getObject(1).toString();
                partidas[1][pospart] = rst.getObject(2).toString();
                pospart++;
            }
        } catch (SQLException ex) {
            Logger.getLogger(modificaparametro.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Columna 1 id, columna 2 cantidad      
    }
    public void calcular(){
        int i=0, encmano=0, encmat=0, encequip=0;
        int posman=0, posequipo=0, posmate=0;
        int maxmano, maxequip, maxmat;
        BigDecimal admini, financ, impues, utili;
        BigDecimal valorman= new BigDecimal("0.00"), valorequip= new BigDecimal("0.00"), valormat= new BigDecimal("0.00");
        int largo = partidas[0].length;
        posmano=posmat=posequip=0;
        maxmano=partidamano.length;
        maxequip = partidaequip.length;
        maxmat = partidamat.length;
        partida = new String[largo];
        totalpartida = new BigDecimal[largo];
        for(i=0;i<pospart;i++)
        {
            posmano = posmat = posequip=0;
            valorman=valormat=valorequip= new BigDecimal("0.00");
            encmano=encmat=encequip=0;
            while(encmano==0 && posmano<maxmano)
            {
                System.out.println("i="+i);
                if(partidas[0][i].equals(partidamano[posmano])&& posmano<maxmano){
                    valorman = vectormano[posmano];
                    encmano=1;
                }else{
                    posmano++;
                    
                }
            }
            while(encmat==0&& posmat<maxmat){
                if(partidas[0][i].equals(partidamat[posmat])&& posmat<maxmat){
                    valormat = vectormat[posmat];
                    encmat=1;
                }else{
                    posmat++;
                }
            }
            while(encequip==0&&posequip<maxequip){
                if(partidas[0][i].equals(partidaequip[posequip])&& posequip<maxequip){
                    valorequip=vectorequip[posequip];
                    encequip=1;
                }else{
                 
                    posequip++;
                }
            }
            
            partida[i] = partidas[0][i];
            totalpartida[i] = valorman.add(valormat).add(valorequip);
            admini = redon.redondearDosDecimales(totalpartida[i].multiply(admin.divide(new BigDecimal("100"))));
            utili = redon.redondearDosDecimales(totalpartida[i].multiply(util.divide(new BigDecimal("100"))));
            totalpartida[i] = totalpartida[i].add(admini).add(utili);
             financ = redon.redondearDosDecimales(totalpartida[i].multiply(finan.divide(new BigDecimal("100"))));
            impues = redon.redondearDosDecimales(totalpartida[i].multiply(impart.divide(new BigDecimal("100"))));
            totalpartida[i]= totalpartida[i].add(financ).add(impues);
            
        }
    
    }
    
    public void actualizar(){
        int i=0;
        for(i=0; i<pospart;i++){
        String consulta = "UPDATE mppres SET precunit="+totalpartida[i]+" WHERE id='"+partida[i]+"'";
        try {
            Statement st = (Statement) conex.createStatement();
            st.execute(consulta);
        } catch (SQLException ex) {
            Logger.getLogger(modificaparametro.class.getName()).log(Level.SEVERE, null, ex);
        }
        }
    }
    @Override
    public void run(){
            
            calculamano();
            calcularmat();
            calcularequip();
             llenarpartida();
            calcular();
            actualizar();   
            obj.cargartotal();
    }
    
}
