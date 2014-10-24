/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package winspapus;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Betmart
 */
public class recalculasegunapu extends Thread{
    Connection conex;
    String tabu="";
    recalcula obj;
    public recalculasegunapu(Connection conex, String tabu, recalcula obj){
        this.conex=conex;
        this.tabu=tabu;
        this.obj=obj;
    }
    @Override
    public void run(){
        String cambia = "SELECT numero, rendimi, porcgad, porcutil, porcpre"
                + " FROM mptabs WHERE mtabus_id='"+tabu+"'";
        String datostab = "SELECT pimpue, pcosfin FROM mtabus WHERE id='"+tabu+"'";
        float impu=0, cosfin=0, rendimi=0, porcgad=0, porcutil=0, porcpre=0;
        int cuenta=0;
        
        String numero;
        float contmat = 0, contequipo =0, contmano = 0, cantmano=0, bono=0, subsid=0, precunitrecalculado=0;
             float precunit=0;
        try {
            
            // DATOS TABULADOR
            Statement stabu = (Statement) conex.createStatement();
            ResultSet rstabu = stabu.executeQuery(datostab);
            while(rstabu.next()){
                impu = rstabu.getFloat(1);
                cosfin = rstabu.getFloat(2);
            }
            
            // CALCULO DE LA PARTIDA
            Statement conteo = (Statement) conex.createStatement();
            ResultSet rsconteo = conteo.executeQuery(cambia);
             int i=0;
            while(rsconteo.next()){
                numero = rsconteo.getString(1);
                rendimi = rsconteo.getFloat("rendimi");
                porcgad = rsconteo.getFloat("porcgad");
                porcutil = rsconteo.getFloat("porcutil");
                porcpre = rsconteo.getFloat("porcpre");
                contmat = 0; contequipo =0; contmano = 0; cantmano=0; bono=0; subsid=0; precunitrecalculado=0;
                precunit=0;
                
                String consulta = "SELECT SUM(((mm.precio+(mm.precio*(mm.desperdi/100)))*dm.cantidad)) as total FROM "
                        + "dmtabs as dm, mmtabs as mm "
                        + " WHERE dm.mmtab_id=mm.id AND dm.mtabus_id=mm.mtabus_id AND mm.mtabus_id='"+tabu+"' AND "
                        + "dm.numepart="+numero;
                Statement mates = (Statement) conex.createStatement();
                ResultSet rmates = mates.executeQuery(consulta);
                while(rmates.next()){
                    contmat = rmates.getFloat(1);
                }
                System.out.println("contmat "+contmat);
                 contmat = (float) (Math.rint((contmat+0.000001)*100)/100);
                String consultaeq = "SELECT SUM(IF(me.deprecia=0,de.cantidad*me.precio, de.cantidad*me.deprecia*me.precio)) "
                        + "as total FROM "
                        + "deptabs as de, metabs as me WHERE me.id=de.metab_id AND me.mtabus_id=de.mtabus_id AND de.mtabus_id='"+tabu+"' AND de.numero="+numero+"";
                Statement steq = (Statement) conex.createStatement();
                ResultSet rsteq = steq.executeQuery(consultaeq);
                while(rsteq.next()){
                    contequipo = rsteq.getFloat(1);
                }
                 System.out.println("contequipo "+contequipo);
                if(rendimi>0){
                contequipo = contequipo/rendimi;
                }
                contequipo= (float) (Math.rint((contequipo+0.000001)*100)/100);
                String consultaman = "SELECT SUM(do.cantidad) as cantidad, mo.bono, mo.subsid, SUM(mo.salario*do.cantidad) as total"
                        + " FROM mmotabs as mo,dmoptabs as do WHERE do.mmotab_id = mo.id AND do.mtabus_id= mo.mtabus_id"
                        + " AND do.mtabus_id='"+tabu+"' AND do.numero = "+numero+"";
                Statement stman = (Statement) conex.createStatement();
                ResultSet rstman = stman.executeQuery(consultaman);
                while(rstman.next()){
                    contmano = rstman.getFloat("total");
                    cantmano= rstman.getFloat("cantidad");
                    bono = rstman.getFloat("mo.bono");
                    subsid=rstman.getFloat("mo.subsid");                    
                }
                 System.out.println("contmano "+contmano);
                  System.out.println("cantmano "+cantmano);
                   System.out.println("bono "+bono);
                    System.out.println("subsid "+subsid);
                float presta = contmano*porcpre/100;
                bono = cantmano*bono;
                subsid = cantmano*subsid;
                 System.out.println("bono*cantidad "+bono);
                    System.out.println("subsid*cantidad "+subsid);
                float auxmonto = contmano + presta + bono + subsid;
                if(rendimi==0){
                    rendimi=1;
                }
                contmano = auxmonto/rendimi;
                System.out.println("contmano/rendimi "+contmano);
                precunit = contmat + contequipo + contmano;
                System.out.println("preunit (mat+eq+mano) "+precunit);
                porcgad = precunit * (porcgad/100);
                System.out.println("porcgad precunit * (porcgad/100)"+porcgad);
                porcgad = precunit+porcgad;
                 System.out.println("porcgad precunit+porcgad "+porcgad);
                porcutil = porcgad * porcutil/100;
                  System.out.println("porcutil porcgad * porcutil/100 "+porcutil);
                porcutil = porcgad+porcutil;
                 System.out.println("porcutil porcgad + porcutil "+porcutil);
               
                float auximpu = porcutil * impu/100;
                 System.out.println("auximpu y impu "+auximpu+" "+impu);
                float auxcosfin = porcutil * cosfin/100;
                 System.out.println("auxcosfin y cosfin "+auxcosfin+" "+cosfin);
                porcutil = porcutil + auximpu +auxcosfin;
                 System.out.println("porcutil porcutil + auximpu +auxcosfin "+porcutil);
                precunitrecalculado = porcutil;
                precunitrecalculado= (float) (Math.rint((precunitrecalculado+0.000001)*100)/100);
                System.out.println("precunitrecalculado = porcutil "+precunitrecalculado);
                String actualiza = "UPDATE mptabs SET precunit="+precunitrecalculado+" WHERE numero="+numero+" AND mtabus_id='"+tabu+"'";
                Statement stact = (Statement) conex.createStatement();
              stact.execute(actualiza);
              i++;
              obj.setprogres(i);
              
            }
           JOptionPane.showMessageDialog(null, "Se han actualizado los precios unitarios de las partidas según el APU");
        } catch (SQLException ex) {
            Logger.getLogger(recalcula.class.getName()).log(Level.SEVERE, null, ex);
        }
        obj.doClose(1); 
    }
}
