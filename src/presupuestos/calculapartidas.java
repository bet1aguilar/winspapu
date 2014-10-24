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

/**
 *
 * @author Betmart
 */
public class calculapartidas {
    Connection conex;
    String mpres, numero;
    float admin=0,conteq=0,contmat=0,contmano=0, contotal=0;
    float util=0,finan=0,impart=0;
    float prest=0;
    public calculapartidas(Connection conex, String mpres, String numpartida){
        this.mpres=mpres;
        this.numero=numpartida;
        this.conex=conex;
        buscaparametros();      
        cambiapreciopartida();
    }
    public final void cambiapreciopartida(){
        //AL CAMBIAR UN VALOR DE LOS PARAMETROS DEL PRESUPUESTO COMO LOS PORCENTAJES DE PRESTACIONES SOCIALES
        // SE RECALCULA EL COSTO DE TODAS LAS PARTIDAS      
        float rendimi=0;
        float cantidad=0,bono=0,subsid=0;
        //CONSULTA RENDIMIENTO DE LA PARTIDA
        String selecrendimi = "SELECT rendimi FROM mppres WHERE numero="+numero+" AND mpre_id='"+mpres+"'";
        try {
            Statement sts = (Statement) conex.createStatement();
            ResultSet rsts = sts.executeQuery(selecrendimi);
            while(rsts.next()){
                rendimi=rsts.getFloat(1);
            }
        } catch (SQLException ex) {
            System.out.println("Error al consultar el rendimiento de la partida, cuando se va a insertar en una nueva reconsideración");
            Logger.getLogger(calculapartidas.class.getName()).log(Level.SEVERE, null, ex);
        }        
        // SUMANDO TOTAL DE MATERIAL
        String totalmaterial="SELECT SUM((mm.precio+(mm.precio*(mm.desperdi/100)))*dm.cantidad) as total "
                + "FROM dmpres as dm, mmpres as mm WHERE dm.numepart="+numero+" AND dm.mmpre_id=mm.id "
                + "AND dm.mpre_id='"+mpres+"' "
                + " AND dm.mpre_id = mm.mpre_id";
        try {
            Statement st = (Statement) conex.createStatement();
            ResultSet rst = st.executeQuery(totalmaterial);
            while(rst.next()){
              contmat = rst.getFloat("total");
            }
        } catch (SQLException ex) {
            System.out.println("Error al Sumar materiales de la partida de la valuación para agregarla en reconsideración");
            Logger.getLogger(calculapartidas.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        String totalequipo = "SELECT SUM(IF(me.deprecia=0, (de.cantidad*me.precio),(de.cantidad*me.deprecia*me.precio))) as total "
                + "FROM mepres as me, deppres as de WHERE de.mepre_id=me.id AND de.numero="+numero+" AND "
                + "de.mpre_id='"+mpres+"' "
                + "AND de.mpre_id=me.mpre_id";
        try {
            Statement st= (Statement) conex.createStatement();
            ResultSet rst = st.executeQuery(totalequipo);
            while(rst.next()){
                conteq=rst.getFloat(1);
            }
        } catch (SQLException ex) {
            System.out.println("Error al consultar el total en equipos para la inserción de partidas en la reconsideración");
            Logger.getLogger(calculapartidas.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        String consultamano = "SELECT SUM(dm.cantidad) as cantidad, mm.bono, mm.subsid, "
                    + "SUM(mm.salario*dm.cantidad) as total FROM mmopres as mm, dmoppres as dm "
                + "WHERE dm.numero ="+numero+" AND dm.mpre_id='"+mpres+"'  AND dm.mmopre_id=mm.id"
                + " AND dm.mpre_id = mm.mpre_id";
        try {
            Statement st = (Statement) conex.createStatement();
            ResultSet rst = st.executeQuery(consultamano);
            while(rst.next()){
                contmano = rst.getFloat("total");
                cantidad = rst.getFloat("cantidad");
                bono = rst.getFloat("mm.bono");
                subsid = rst.getFloat("mm.subsid");
            }
        } catch (SQLException ex) {
            System.out.println("Error al sumar la cantidad de mano de obra en la inserción de la reconsideración");
            Logger.getLogger(calculapartidas.class.getName()).log(Level.SEVERE, null, ex);
        }
        bono = cantidad*bono;
        subsid = cantidad*subsid;
        prest = contmano*prest;
        float auxcontmano = contmano+bono+subsid+prest;
        if(rendimi==0)
            rendimi=1;
        contmano=auxcontmano/rendimi;
        conteq=conteq/rendimi;
        float total = contmat+conteq+contmano;
        float auxtotal=total;
        admin = total*admin;
        util=(auxtotal+admin)*util;
        auxtotal=total+admin+util;
        finan=auxtotal*finan;
        impart=auxtotal*impart;
        total=auxtotal+finan+impart;
        String actualiza="UPDATE mppres SET precunit="+total+" WHERE numero="+numero+" AND mpre_id='"+mpres+"'";
        try {
            Statement sta = (Statement) conex.createStatement();
            sta.execute(actualiza);
        } catch (SQLException ex) {
            Logger.getLogger(calculapartidas.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    public final void buscaparametros(){
        String selecpart = "SELECT porcgad, porcpre, porcutil FROM mppres WHERE mpre_id='"+mpres+"' AND numero="+numero+"";
        String selecpres = "SELECT porgam,porcfi,poripa,porpre,poruti FROM mpres WHERE id='"+mpres+"'";
        String adminpart=null,prestpart=null,utilpart=null,adminpres=null,prestpres=null,utilpres=null;
        
        try {
            Statement st = (Statement) conex.createStatement();
            ResultSet rst = st.executeQuery(selecpart);
            while(rst.next()){
                adminpart=rst.getString("porcgad");
                prestpart=rst.getString("porcpre");
                utilpart=rst.getString("porcutil");
            }
           Statement stpres = (Statement) conex.createStatement();
           ResultSet rstpres = stpres.executeQuery(selecpres);
           while(rstpres.next()){
               adminpres=rstpres.getString("porgam");
               prestpres = rstpres.getString("porpre");
               utilpres = rstpres.getString("poruti");
               finan = rstpres.getFloat("porcfi")/100;
               impart=rstpres.getFloat("poripa");
               
           }
          if(adminpart==null){
              admin=Float.valueOf(adminpres)/100;
          }else{
              admin=Float.valueOf(adminpart)/100;
          }
          if(prestpart==null){
              prest=Float.valueOf(prestpres)/100;
          }else{
              prest=Float.valueOf(prestpart)/100;
          }
          if(utilpart==null){
              util=Float.valueOf(utilpres)/100;             
          }else{
              util=Float.valueOf(utilpart)/100;
          }
        } catch (SQLException ex) {
            Logger.getLogger(calculapartidas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
