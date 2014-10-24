/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package presupuestos;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;



public class modificaparametro extends Thread{
    Connection conex;
    Presupuesto obj;
    String idpres;
    Boolean mientras=true;
    int valormano=0, valormaterial=0, valorequipo=0;
    float [] vectormano, vectormat, vectorequip, totalpartida;
    String [] partidamano, partidamat, partidaequip, partida;
    String [][] partidas;
    float presta, admin, finan, util, impart, imptotal;
    int posmano=0, posmat=0, posequip=0, pospart=0, postotal=0;
    
    public modificaparametro(Connection conex, Presupuesto obj, String idpres, float prestaciones, float admin, float finan, float util, float imppart, float imptotal){
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
        float valor=0, salario, cantidad, bono, subsid, prestaciones;
       float rendimi=0;
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
            vectormano = new float[numeroreg];
            partidamano = new String[numeroreg];
            while(rst.next()){
                
                partidamano[posmano] = rst.getObject(5).toString();
                rendimi = Float.valueOf(rst.getObject(6).toString());
                valor = Float.valueOf(rst.getObject(1).toString());
                cantidad = Float.valueOf(rst.getObject(2).toString());
                bono = Float.valueOf(rst.getObject(3).toString());
                subsid = Float.valueOf(rst.getObject(4).toString());
                prestaciones = valor * presta/100;
                bono = cantidad * bono;
                subsid = cantidad * subsid;
                valor = valor + prestaciones + bono + subsid;               
                valor = valor/rendimi;
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
            vectormat = new float[nofilas];
            partidamat = new String[nofilas];
            
            while(rst.next()){
                partidamat[posmat]=rst.getObject(2).toString();
                vectormat[posmat]= Float.valueOf(rst.getObject(1).toString());
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
            vectorequip = new float[filas];
            partidaequip = new String[filas];
            
            while(rstr1.next()){
                partidaequip[posequip]=rstr1.getObject(2).toString();
                vectorequip[posequip]=Float.valueOf(rstr1.getObject(1).toString());
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
        float admini, financ, impues, utili;
        float valorman=0, valorequip=0, valormat=0;
        int largo = partidas[0].length;
        posmano=posmat=posequip=0;
        maxmano=partidamano.length;
        maxequip = partidaequip.length;
        maxmat = partidamat.length;
        partida = new String[largo];
        totalpartida = new float[largo];
        for(i=0;i<pospart;i++)
        {
            posmano = posmat = posequip=0;
            valorman=valormat=valorequip=0;
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
            totalpartida[i] = valorman+valormat+valorequip;
            admini = totalpartida[i]*admin/100;
            utili = totalpartida[i]*util/100;
            totalpartida[i] = totalpartida[i]+admini+utili;
             financ = totalpartida[i]*finan/100;
            impues = totalpartida[i]*impart/100;
            totalpartida[i]= totalpartida[i]+financ + impues;
            
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
            
   this.stop();
    }
    
}
