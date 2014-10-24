/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package reportes;

import java.awt.Image;

/**
 *
 * @author Betmart
 */

public class modificado {
    String encabezado, titulo, obra, lugar, partidapres,nrocon, fecha;
    Object logo1,logo2;
    String ubicacion, nro, descri, unidad;
    Double cantidad, precunit, total;
    String contrepleg, cedrep, ingres, cedres, civres, ingins, cedins, civins;
    
   public modificado(String encabezado, String titulo, String obra, String lugar, 
           String partidapres,String nrocon, String fecha, Image logo1, Image logo2, 
           String ubicacion, String nro, String descri, 
           String unidad, Double cantidad, Double precunit, 
           Double total, String contrepleg,
           String cedrep, String civres, String ingres, String cedres, String ingins,
           String cedins, String civins)
    {
       this.encabezado = encabezado;
       this.titulo = titulo;
       this.obra = obra;
       this.lugar = lugar;
       this.partidapres = partidapres;
       this.nrocon = nrocon;
       this.fecha = fecha;
       this.logo1 = logo1;
       this.logo2 = logo2;
       this.ubicacion = ubicacion;
       this.nro = nro;
       this.descri = descri;
       this.unidad = unidad;
       this.cantidad = cantidad;
       this.precunit = precunit;
       this.total = total;
       this.contrepleg = contrepleg;
       this.ingres = ingres;
       this.cedres = cedres;
       this.civres = civres;
       this.ingins = ingins;
       this.ingins=ingins;
       this.cedins = cedins;
       this.civins = civins;
       
    }

    public Double getCantidad() {
        return cantidad;
    }

    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
    }

    public String getCedins() {
        return cedins;
    }

    public void setCedins(String cedins) {
        this.cedins = cedins;
    }

    public String getCedrep() {
        return cedrep;
    }

    public void setCedrep(String cedrep) {
        this.cedrep = cedrep;
    }

    public String getCedres() {
        return cedres;
    }

    public void setCedres(String cedres) {
        this.cedres = cedres;
    }

    public String getCivins() {
        return civins;
    }

    public void setCivins(String civins) {
        this.civins = civins;
    }

    public String getCivres() {
        return civres;
    }

    public void setCivres(String civres) {
        this.civres = civres;
    }

    public String getContrepleg() {
        return contrepleg;
    }

    public void setContrepleg(String contrepleg) {
        this.contrepleg = contrepleg;
    }

    public String getDescri() {
        return descri;
    }

    public void setDescri(String descri) {
        this.descri = descri;
    }

    public String getEncabezado() {
        return encabezado;
    }

    public void setEncabezado(String encabezado) {
        this.encabezado = encabezado;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getIngins() {
        return ingins;
    }

    public void setIngins(String ingins) {
        this.ingins = ingins;
    }

    public String getIngres() {
        return ingres;
    }

    public void setIngres(String ingres) {
        this.ingres = ingres;
    }

    public Object getLogo1() {
        return logo1;
    }

    public void setLogo1(Object logo1) {
        this.logo1 = logo1;
    }

    public Object getLogo2() {
        return logo2;
    }

    public void setLogo2(Object logo2) {
        this.logo2 = logo2;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getNro() {
        return nro;
    }

    public void setNro(String nro) {
        this.nro = nro;
    }

    public String getNrocon() {
        return nrocon;
    }

    public void setNrocon(String nrocon) {
        this.nrocon = nrocon;
    }

    public String getObra() {
        return obra;
    }

    public void setObra(String obra) {
        this.obra = obra;
    }

    public String getPartidapres() {
        return partidapres;
    }

    public void setPartidapres(String partidapres) {
        this.partidapres = partidapres;
    }

    public Double getPrecunit() {
        return precunit;
    }

    public void setPrecunit(Double precunit) {
        this.precunit = precunit;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }
    
}
