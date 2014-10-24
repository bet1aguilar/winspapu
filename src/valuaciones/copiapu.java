
package valuaciones;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class copiapu {
    String mpres, mtabu, partida;
    private Connection conex;
    String numero, numenpres;
    float totamat=0, totaequipo=0, totamano=0;
    float cantmano=0;
    float porcgad=0, porcutil=0, porcfi=0, porcpre=0, rendimi=0, totalpartida=0, porcimp=0;
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
                rendimi = rstr.getFloat(2);
            }
        } catch (SQLException ex) {
            Logger.getLogger(copiapu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public final void totalpartida(){
        totalpartida = totaequipo+totamano+totamat;
        float admin, util, finan, imp,aux;
        admin = porcgad/100;
        admin = totalpartida*admin;
        admin = totalpartida+admin;
        util = porcutil/100;
        util = admin*util;
       totalpartida=admin+util;
       finan = porcfi/100;
       finan = totalpartida*finan;
       imp = porcimp/100;
       imp = totalpartida*imp;
       totalpartida = totalpartida+imp+finan;
       
    }
    public float gettotalpartida(){
        return totalpartida;
    }
    public final void cargamat(){
        String mmtab_id, cantidad, precio, status;
        float valor=0;
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
                    Statement insert = (Statement) conex.createStatement();
                    insert.execute(inserta);
                }else{
                    String actualiza = "UPDATE mmpres SET desperdi="+desperdi+", precio="+precios+" WHERE "
                            + "mpre_id='"+mpres+"' AND id='"+mmtab_id+"'";
                    Statement insert = (Statement) conex.createStatement();
                    insert.execute(actualiza);
                }  
                
                float precio1, cant, desp;
                precio1 = Float.valueOf(precios);
                cant = Float.valueOf(cantidad);
                desp = Float.valueOf(desperdi);
                
                totamat +=precio1+(precio1*desp/100)*cant; 
                
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
                float precio1, cant, desp;
                precio1 = Float.valueOf(precios);
                cant = Float.valueOf(cantidad);
                desp = Float.valueOf(depreciar);
                if(desp==0) {
                    desp=1;
                }
                totaequipo +=(precio1*cant*desp); 
            }
            totaequipo = totaequipo/rendimi;
        } catch (SQLException ex) {
            Logger.getLogger(copiapu.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public final void cargamano(){
        float precio1=0, cant=0, bon = 0, sub=0;
        String mmotabs, cantidad, bono = null,salario=null, subsid=null, deprecia=null;
        String consulta = "SELECT mmotab_id, cantidad, salario, bono, subsidi FROM dmoptabs WHERE "
                + "numero="+numero+" AND mtabus_id='"+mtabu+"'";
        try {
            String parametros = "SELECT porcgad,porcpre,porcutil FROM mppres WHERE numero="+numenpres+" AND mpre_id='"+mpres+"'";
            Statement stp = (Statement) conex.createStatement();
            ResultSet rstp = stp.executeQuery(parametros);
            while(rstp.next()){
                porcgad = rstp.getFloat("porcgad");
                    porcutil = rstp.getFloat("porcutil");
                      porcpre = rstp.getFloat("porcpre");
            }
            String busca = "SELECT porcfi, poripa FROM mpres WHERE id='"+mpres+"'";
                Statement buscar = (Statement) conex.createStatement();
                ResultSet rbusca = buscar.executeQuery(busca);
                
                while(rbusca.next()){
                  
                    porcfi = rbusca.getFloat("porcfi");
                    porcimp = rbusca.getFloat("poripa");
                  
                }
                if(selec==1){
                    String consultadetabu="SELECT porcgad,porcpre,porcutil FROM mptabs WHERE mtabus_id='"+mtabu+"' AND"
                            + " numero="+numero+"";
                    Statement sc = (Statement) conex.createStatement();
                    ResultSet rsc = sc.executeQuery(consultadetabu);
                    while(rsc.next()){
                        porcgad=rsc.getFloat("porcgad");
                        porcpre = rsc.getFloat("porcpre");
                        porcutil = rsc.getFloat("porcutil");
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
                    
               String inserta = "INSERT INTO dmoppres (mpre_id, mppre_id, mepre_id,numero, "
                       + "cantidad, salario, bono, subsid,status)"
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
                
                
                
                precio1 = Float.valueOf(salario);
                cant = Float.valueOf(cantidad);
                bon = Float.valueOf(bono);
                sub = Float.valueOf(subsid);
                
                totamano +=(precio1*cant); 
                cantmano += cant;
                
                
            }
            float prestacion, bonos, subsidio;
            prestacion = porcpre/100;
                prestacion = totamano*prestacion;
                bonos = bon*cantmano;
                subsidio = sub * cantmano;
                
                totamano = totamano+prestacion+bonos+subsidio;
            totamano = totamano / rendimi;
        } catch (SQLException ex) {
            Logger.getLogger(copiapu.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
