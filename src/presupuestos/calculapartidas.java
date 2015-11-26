/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package presupuestos;

import java.sql.Connection;
import com.mysql.jdbc.Statement;
import config.Redondeo;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Betmart
 */
public class calculapartidas {
    private Connection conex;
    String mpres, numero;
    Redondeo redondear = new Redondeo();
    BigDecimal admin= new BigDecimal("0.00"),conteq=new BigDecimal("0.00"),contmat=new BigDecimal("0.00"),contmano=new BigDecimal("0.00"), contotal=new BigDecimal("0.00");
    BigDecimal util=new BigDecimal("0.00"),finan=new BigDecimal("0.00"),impart=new BigDecimal("0.00");
    BigDecimal prest=new BigDecimal("0.00");
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
        BigDecimal rendimi=new BigDecimal("0.00");
        BigDecimal cantidad=new BigDecimal("0.00"),bono=new BigDecimal("0.00"),subsid=new BigDecimal("0.00");
        //CONSULTA RENDIMIENTO DE LA PARTIDA
        String selecrendimi = "SELECT rendimi FROM mppres WHERE numero="+numero+" AND mpre_id='"+mpres+"'";
        try {
            Statement sts = (Statement) conex.createStatement();
            ResultSet rsts = sts.executeQuery(selecrendimi);
            while(rsts.next()){
                rendimi=rsts.getBigDecimal(1);
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
              contmat = rst.getBigDecimal("total");
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
                conteq=rst.getBigDecimal(1);
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
                contmano = rst.getBigDecimal("total");
                cantidad = rst.getBigDecimal("cantidad");
                bono = rst.getBigDecimal("mm.bono");
                subsid = rst.getBigDecimal("mm.subsid");
            }
        } catch (SQLException ex) {
            System.out.println("Error al sumar la cantidad de mano de obra en la inserción de la reconsideración");
            Logger.getLogger(calculapartidas.class.getName()).log(Level.SEVERE, null, ex);
        }
        bono = redondear.redondearDosDecimales(cantidad.multiply(bono));
        subsid = redondear.redondearDosDecimales(cantidad.multiply(subsid));
        prest = redondear.redondearDosDecimales(contmano.multiply(prest));
        BigDecimal auxcontmano = contmano.add(bono).add(subsid).add(prest);
        if(rendimi==new BigDecimal("0.00"))
            rendimi=new BigDecimal("1.00");
        contmano=auxcontmano.divide(rendimi,2,BigDecimal.ROUND_HALF_UP);
        conteq=conteq.divide(rendimi,2,BigDecimal.ROUND_HALF_UP);
        BigDecimal total = contmat.add(conteq).add(contmano);
        BigDecimal auxtotal=total;
        admin = redondear.redondearDosDecimales(total.multiply(admin));
        util=redondear.redondearDosDecimales((auxtotal.add(admin)).multiply(util));
        auxtotal=total.add(admin).add(util);
        finan=redondear.redondearDosDecimales(auxtotal.multiply(finan));
        impart=redondear.redondearDosDecimales(auxtotal.multiply(impart));
        total=auxtotal.add(finan).add(impart);
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
        BigDecimal adminpart=new BigDecimal("0.00"),prestpart=new BigDecimal("0.00"),utilpart=new BigDecimal("0.00"),adminpres=new BigDecimal("0.00"),prestpres=new BigDecimal("0.00"),utilpres=new BigDecimal("0.00");
        
        try {
            Statement st = (Statement) conex.createStatement();
            ResultSet rst = st.executeQuery(selecpart);
            while(rst.next()){
                adminpart=rst.getBigDecimal("porcgad");
                prestpart=rst.getBigDecimal("porcpre");
                utilpart=rst.getBigDecimal("porcutil");
            }
           Statement stpres = (Statement) conex.createStatement();
           ResultSet rstpres = stpres.executeQuery(selecpres);
           while(rstpres.next()){
               adminpres=rstpres.getBigDecimal("porgam");
               prestpres = rstpres.getBigDecimal("porpre");
               utilpres = rstpres.getBigDecimal("poruti");
               finan = rstpres.getBigDecimal("porcfi").divide(new BigDecimal("100"));
               impart=rstpres.getBigDecimal("poripa");
               
           }
          if(adminpart==null){
              admin=(adminpres).divide(new BigDecimal("100"));
          }else{
              admin=(adminpart).divide(new BigDecimal("100"));
          }
          if(prestpart==null){
              prest=(prestpres).divide(new BigDecimal("100"));
          }else{
              prest=(prestpart).divide(new BigDecimal("100"));
          }
          if(utilpart==null){
              util=(utilpres).divide(new BigDecimal("100"));             
          }else{
              util= utilpart.divide(new BigDecimal("100"));
          }
        } catch (SQLException ex) {
            Logger.getLogger(calculapartidas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
