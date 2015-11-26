
package valuaciones;

import java.sql.Connection;
import com.mysql.jdbc.Statement;
import config.Redondeo;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class copiapu {
    String mpres, mtabu, partida;
    private Connection conex;
    String numero, numenpres;
    Redondeo redon = new Redondeo();
    BigDecimal totamat=new BigDecimal("0.00"), totaequipo=new BigDecimal("0.00"), totamano=new BigDecimal("0.00");
    BigDecimal cantmano=new BigDecimal("0.00");
    BigDecimal porcgad=new BigDecimal("0.00"), porcutil=new BigDecimal("0.00"), porcfi=new BigDecimal("0.00"), porcpre=new BigDecimal("0.00"), rendimi=new BigDecimal("0.00"), totalpartida=new BigDecimal("0.00"), porcimp=new BigDecimal("0.00");
    String num="";
    int selec;
    public copiapu(Connection conex, String mpres, String mtabu, String partida, int selec, String numero){
        
        this.conex = conex;
        this.mpres = mpres;
        this.mtabu = mtabu;
        this.num=numero;
        this.partida = partida;
        this.selec=selec;
        buscanum();
        cargamat();
        cargaequip();
        cargamano();
        totalpartida();
    }
    
    public final void buscanum(){
        String busca = "SELECT numero FROM mptabs WHERE codicove='"+partida+"' AND mtabus_id='"+mtabu+"'";
        try {
            Statement st = (Statement) conex.createStatement();
            ResultSet rst = st.executeQuery(busca);
            while(rst.next()){
                numero = rst.getString("numero");
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(copiapu.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        String num = "SELECT numero, rendimi FROM mppres WHERE id='"+partida+"' AND mpre_id='"+mpres+"'";
        try {
            Statement str = (Statement) conex.createStatement();
            ResultSet rstr = str.executeQuery(num);
            while(rstr.next()){
                numenpres = rstr.getString(1);
                rendimi = rstr.getBigDecimal(2);
            }
        } catch (SQLException ex) {
            Logger.getLogger(copiapu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public final void totalpartida(){
        totalpartida = totaequipo.add(totamano).add(totamat);
        BigDecimal admin, util, finan, imp,aux;
        admin = porcgad.divide(new BigDecimal("100"));
        admin = redon.redondearDosDecimales(totalpartida.multiply(admin));
        admin = totalpartida.add(admin);
        util = porcutil.divide(new BigDecimal("100"));
        util = redon.redondearDosDecimales(admin.multiply(util));
       totalpartida=admin.add(util);
       finan = porcfi.divide(new BigDecimal("100"));
       finan = totalpartida.multiply(finan);
       imp = porcimp.divide(new BigDecimal("100"));
       imp = redon.redondearDosDecimales(totalpartida.multiply(imp));
       totalpartida = totalpartida.add(imp).add(finan);
       
    }
    public BigDecimal gettotalpartida(){
        return totalpartida;
    }
    public final void cargamat(){
        String mmtab_id, cantidad, precio, status;
        BigDecimal valor=new BigDecimal("0.00");
        String select = "SELECT mmtab_id, cantidad, precio, status FROM dmtabs WHERE"
                + " numepart="+numero+" AND mtabus_id='"+mtabu+"'";
        try {
            Statement st = (Statement) conex.createStatement();
            ResultSet rst = st.executeQuery(select);
            
            while(rst.next()){
                int conta=0;
                mmtab_id= rst.getString(1);
                cantidad = rst.getString(2);
                precio = rst.getString(3);
                status = rst.getString(4);
                String cuenta = "SELECT count(*) FROM dmpres WHERE mmpre_id='"+mmtab_id+"' "
                        + "AND mppre_id='"+partida+"' AND mpre_id='"+mpres+"'";
                Statement contar = (Statement) conex.createStatement();
                ResultSet rscontar = contar.executeQuery(cuenta);
                while(rscontar.next()){
                    conta= rscontar.getInt(1);
                }
                
                if(conta<=0){
                    String inserta = "INSERT INTO dmpres (mpre_id, mppre_id, mmpre_id,numepart, cantidad, precio,status)"
                            + " VALUES ('"+mpres+"', '"+partida+"', '"+mmtab_id+"', "+numenpres+", "+cantidad+", "+precio+", 1)";
                    Statement insert = (Statement) conex.createStatement();
                    insert.execute(inserta);
                }else{
                    String actualiza = "UPDATE dmpres SET cantidad = "+cantidad+", precio="+precio+" WHERE numepart ="+numenpres+" AND "
                            + "mppre_id='"+partida+"' AND mmpre_id='"+mmtab_id+"' AND mpre_id = '"+mpres+"'";
                    Statement actual = (Statement) conex.createStatement();
                    actual.execute(actualiza);
                }
                
                
                String contador = "SELECT count(*) FROM mmpres WHERE id='"+mmtab_id+"' AND mpre_id='"+mpres+"'";
                Statement consult = (Statement) conex.createStatement();
                ResultSet rscon = consult.executeQuery(contador);
                int conte =0;
                while(rscon.next()){
                    conte = rscon.getInt(1);                  
                }
                 String descri=null, desperdi=null, precios=null, unidad=null;
                 String mptab = "SELECT descri, desperdi, precio, unidad FROM mmtabs WHERE "
                            + "id='"+mmtab_id+"' AND mtabus_id='"+mtabu+"'";
                    Statement stm = (Statement) conex.createStatement();
                    ResultSet rstm = stm.executeQuery(mptab);
                    while(rstm.next()){
                        descri = rstm.getString(1);
                        desperdi = rstm.getString(2);
                        precios = rstm.getString(3);
                        unidad = rstm.getString(4);
                    }
                if(conte==0)
                {
                    String inserta = "INSERT INTO mmpres (mpre_id, id, descri, desperdi, precio, unidad, status)"
                            + " VALUES ('"+mpres+"', '"+mmtab_id+"', '"+descri+"', "+desperdi+", "+precios+",'"+unidad+"', 1)'";
                    System.out.println("inserta "+inserta);
                    Statement insert = (Statement) conex.createStatement();
                    insert.execute(inserta);
                }else{
                    String actualiza = "UPDATE mmpres SET desperdi="+desperdi+", precio="+precios+" WHERE "
                            + "mpre_id='"+mpres+"' AND id='"+mmtab_id+"'";
                    Statement insert = (Statement) conex.createStatement();
                    insert.execute(actualiza);
                }  
                
                BigDecimal precio1, cant, desp;
                precio1 = new BigDecimal (precios);
                cant = new BigDecimal(cantidad);
                desp = new BigDecimal(desperdi);
                
                totamat =totamat.add(redon.redondearDosDecimales(precio1.add((precio1.multiply(desp.divide(new BigDecimal("100")))).multiply(cant)))); 
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(copiapu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public final void cargaequip(){
        String metabs, cantidad, precios, deprecia;
        String consulta = "SELECT metab_id, cantidad, precio FROM deptabs WHERE  "
                + "numero="+numero+" AND mtabus_id='"+mtabu+"'";
        try {
            Statement st = (Statement) conex.createStatement();
            ResultSet rst = st.executeQuery(consulta);
            int cuenta=0;
            while(rst.next()){
                metabs = rst.getString(1);
                cantidad = rst.getString(2);
                precios = rst.getString(3);  
                String contar = "SELECT count(*) FROM deppres WHERE mepre_id='"+metabs+"' AND numero="+numenpres+""
                                + " AND mpre_id='"+mpres+"'";
                Statement sts = (Statement) conex.createStatement();
                ResultSet rsts = sts.executeQuery(contar);
                
                while(rsts.next()){
                    cuenta = rsts.getInt(1);                    
                }
                if(cuenta <=0){
                    
               String inserta = "INSERT INTO deppres (mpre_id, mppre_id, mepre_id,numero, cantidad, precio,status)"
                            + " VALUES ('"+mpres+"', '"+partida+"', '"+metabs+"', "+numenpres+", "+cantidad+", "+precios+", 1)";
                    Statement insert = (Statement) conex.createStatement();
                    insert.execute(inserta);
                }else{
                    String actualiza = "UPDATE deppres SET cantidad = "+cantidad+", precio="+precios+" WHERE numero ="+numenpres+" AND "
                            + "mppre_id='"+partida+"' AND mepre_id='"+metabs+"' AND mpre_id = '"+mpres+"'";
                    Statement actual = (Statement) conex.createStatement();
                    actual.execute(actualiza);
                }
                
                String contador = "SELECT count(*) FROM mepres WHERE id='"+metabs+"' AND mpre_id='"+mpres+"'";
                Statement consult = (Statement) conex.createStatement();
                ResultSet rscon = consult.executeQuery(contador);
                int conte =0;
                while(rscon.next()){
                    conte = rscon.getInt(1);                  
                }
                 String descri=null, depreciar=null, unidad=null;
                 String mptab = "SELECT descri, deprecia, precio FROM metabs WHERE "
                            + "id='"+metabs+"' AND mtabus_id='"+mtabu+"'";
                    Statement stm = (Statement) conex.createStatement();
                    ResultSet rstm = stm.executeQuery(mptab);
                    while(rstm.next()){
                        descri = rstm.getString(1);
                        depreciar = rstm.getString(2);
                        precios = rstm.getString(3);
                    }
                if(conte==0){
                   
                    
                    String inserta = "INSERT INTO mepres (mpre_id, id, descri, deprecia, precio, status)"
                            + " VALUES ('"+mpres+"', '"+metabs+"', '"+descri+"', "+depreciar+", "+precios+", 1)";
                    Statement insert = (Statement) conex.createStatement();
                    insert.execute(inserta);
                }  
                else{
                    String inserta = "UPDATE mepres SET precio="+precios+" WHERE mpre_id='"+mpres+"'"
                            + " AND id='"+metabs+"'";
                    Statement insert = (Statement) conex.createStatement();
                    insert.execute(inserta); 
                }
                BigDecimal precio1, cant, desp;
                precio1 = new BigDecimal(precios);
                cant = new BigDecimal(cantidad);
                desp = new BigDecimal(depreciar);
                if(desp==new BigDecimal("0.00")) {
                    desp=new BigDecimal("1.00");
                }
                totaequipo =totaequipo.add( redon.redondearDosDecimales(precio1.multiply(cant).multiply(desp))); 
            }
            totaequipo = totaequipo.divide(rendimi,2,BigDecimal.ROUND_HALF_UP);
        } catch (SQLException ex) {
            Logger.getLogger(copiapu.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public final void cargamano(){
        BigDecimal precio1= new BigDecimal("0.00"), cant=new BigDecimal("0.00"), bon = new BigDecimal("0.00"), sub=new BigDecimal("0.00");
        String mmotabs, cantidad, bono = null,salario=null, subsid=null, deprecia=null;
        String consulta = "SELECT mmotab_id, cantidad, salario, bono, subsidi FROM dmoptabs WHERE "
                + "numero="+numero+" AND mtabus_id='"+mtabu+"'";
        try {
            String parametros = "SELECT porcgad,porcpre,porcutil FROM mppres WHERE numero="+numenpres+" AND mpre_id='"+mpres+"'";
            Statement stp = (Statement) conex.createStatement();
            ResultSet rstp = stp.executeQuery(parametros);
            while(rstp.next()){
                porcgad = rstp.getBigDecimal("porcgad");
                    porcutil = rstp.getBigDecimal("porcutil");
                      porcpre = rstp.getBigDecimal("porcpre");
            }
            String busca = "SELECT porcfi, poripa FROM mpres WHERE id='"+mpres+"'";
                Statement buscar = (Statement) conex.createStatement();
                ResultSet rbusca = buscar.executeQuery(busca);
                
                while(rbusca.next()){
                  
                    porcfi = rbusca.getBigDecimal("porcfi");
                    porcimp = rbusca.getBigDecimal("poripa");
                  
                }
                if(selec==1){
                    String consultadetabu="SELECT porcgad,porcpre,porcutil FROM mptabs WHERE mtabus_id='"+mtabu+"' AND"
                            + " numero="+numero+"";
                    Statement sc = (Statement) conex.createStatement();
                    ResultSet rsc = sc.executeQuery(consultadetabu);
                    while(rsc.next()){
                        porcgad=rsc.getBigDecimal("porcgad");
                        porcpre = rsc.getBigDecimal("porcpre");
                        porcutil = rsc.getBigDecimal("porcutil");
                    }
                        
                    //ACTUALIZAR PARAMETROS DE PARTIDA
                    String actualiza = "UPDATE mppres SET porcgad="+porcgad+", porcpre="+porcpre+",porcutil="+porcutil+" "
                            + "WHERE numero="+numenpres+" AND mpre_id='"+mpres+"'";
                    Statement actpart = (Statement) conex.createStatement();
                    actpart.execute(actualiza);
                }
            Statement st = (Statement) conex.createStatement();
            ResultSet rst = st.executeQuery(consulta);
            int cuenta=0;
            while(rst.next()){
                mmotabs = rst.getString(1);
                cantidad = rst.getString(2);
                salario = rst.getString(3);  
                bono = rst.getString(4);
                subsid = rst.getString(5);
                String contar = "SELECT count(*) FROM dmoppres WHERE mmopre_id='"+mmotabs+"' AND numero="+numenpres+""
                                + " AND mpre_id='"+mpres+"'";
                Statement sts = (Statement) conex.createStatement();
                ResultSet rsts = sts.executeQuery(contar);
                
                while(rsts.next()){
                    cuenta = rsts.getInt(1);                    
                }
                if(cuenta <=0){
                    
               String inserta = "INSERT INTO dmoppres (mpre_id, mppre_id, mmopre_id,numero, "
                       + "cantidad, salario, bono, subsidi,status)"
                            + " VALUES ('"+mpres+"', '"+partida+"', '"+mmotabs+"', "+numenpres+", "+cantidad+", "+salario+", "
                       + ""+bono+","+subsid+",1)";
                    Statement insert = (Statement) conex.createStatement();
                    insert.execute(inserta);
                }else{
                    String actualiza = "UPDATE dmoppres SET cantidad = "+cantidad+", salario="+salario+", bono="+bono+","
                            + "subsidi = "+subsid+" WHERE numero ="+numenpres+" AND "
                            + "mppre_id='"+partida+"' AND mmopre_id='"+mmotabs+"' AND mpre_id = '"+mpres+"'";
                    Statement actual = (Statement) conex.createStatement();
                    actual.execute(actualiza);
                }
                
                String contador = "SELECT count(*) FROM mmopres WHERE id='"+mmotabs+"' AND mpre_id='"+mpres+"'";
                Statement consult = (Statement) conex.createStatement();
                ResultSet rscon = consult.executeQuery(contador);
                int conte =0;
                while(rscon.next()){
                    conte = rscon.getInt(1);                  
                }
                 String descri=null, depreciar=null, unidad=null;
                 String mptab = "SELECT descri, bono, salario, subsid FROM mmotabs WHERE "
                            + "id='"+mmotabs+"' AND mtabus_id='"+mtabu+"'";
                    Statement stm = (Statement) conex.createStatement();
                    ResultSet rstm = stm.executeQuery(mptab);
                    while(rstm.next()){
                        descri = rstm.getString(1);
                        bono = rstm.getString(2);
                        salario =rstm.getString(3);
                        subsid = rstm.getString(4);
                    }
                if(conte==0){
                    String inserta = "INSERT INTO mmopres (mpre_id, id, descri, salario, bono, subsid, status)"
                            + " VALUES ('"+mpres+"', "+mmotabs+", "+descri+", "+salario+","+bono+","+subsid+","
                            + " 1)";
                    Statement insert = (Statement) conex.createStatement();
                    insert.execute(inserta);
                }  else{
                    String inserta = "UPDATE mmopres SET salario="+salario+", bono="+bono+", subsid="+subsid+" "
                            + "WHERE mpre_id='"+mpres+"' AND id='"+mmotabs+"'";
                    Statement insert = (Statement) conex.createStatement();
                    insert.execute(inserta);
                }
                precio1 = new BigDecimal(salario);
                cant = new BigDecimal(cantidad);
                bon = new BigDecimal(bono);
                sub = new BigDecimal(subsid);
                totamano =totamano.add(redon.redondearDosDecimales(precio1.multiply(cant))); 
                cantmano = cantmano.add(cant);
                
                
            }
            BigDecimal prestacion, bonos, subsidio;
            prestacion = porcpre.divide(new BigDecimal("100"));
                prestacion = redon.redondearDosDecimales(totamano.multiply(prestacion));
                bonos = redon.redondearDosDecimales(bon.multiply(cantmano));
                subsidio = redon.redondearDosDecimales(sub .multiply( cantmano));
                
                totamano = totamano.add(prestacion).add(bonos).add(subsidio);
            totamano = totamano.divide(rendimi,2,BigDecimal.ROUND_HALF_UP);
        } catch (SQLException ex) {
            Logger.getLogger(copiapu.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
