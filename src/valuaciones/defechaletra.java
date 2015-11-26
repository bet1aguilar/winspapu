/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package valuaciones;

import java.util.regex.Pattern;

/**
 *
 * @author Betmart
 */
public class defechaletra {
    
   private final String[] UNIDADES = {"", "uno ", "dos ", "tres ", "cuatro ", "cinco ", "seis ", "siete ", "ocho ", "nueve "};
    private final String[] DECENAS = {"diez ", "once ", "doce ", "trece ", "catorce ", "quince ", "dieciseis ",
        "diecisiete ", "dieciocho ", "diecinueve", "veinti", "treinta "};
    private final String[] CENTENAS = {"", "ciento ", "doscientos ", "trescientos ", "cuatrocientos ", "quinientos ", "seiscientos ",
        "setecientos ", "ochocientos ", "novecientos "};
    
    public defechaletra(){
        
    }
    
    public String Convertir(String numero, boolean mayusculas) {
        String literal = "";
        String parte_decimal;    
        String auxnumero=numero;
        //si el numero utiliza (.) en lugar de (,) -> se reemplaza
        numero = numero.replace(".", ",");
        //si el numero no tiene parte decimal, se le agrega ,00
        if(numero.indexOf(",")==-1){
            numero = numero + ",00";
        }
        String Num[] = numero.split(",");
        if (Integer.parseInt(Num[1]) == 0) {//si el valor es cero
                literal = "cero ";
            } else if (Integer.parseInt(Num[1]) > 999999) {//si es millon
                literal = getMillones(Num[1]);
            } else if (Integer.parseInt(Num[1]) > 999) {//si es miles
                literal = getMiles(Num[1]);
            } else if (Integer.parseInt(Num[1]) > 99) {//si es centena
                literal = getCentenas(Num[1]);
            } else if (Integer.parseInt(Num[1]) > 9) {//si es decena
                literal = getDecenas(Num[1]);
            } else {//sino unidades -> 9
                if(Integer.parseInt(numero)==1)
                {
                   literal = "primero "; 
                }else{
                literal = getUnidades(Num[1]);
                }
            }
             parte_decimal = literal + " CÉNTIMOS";
        //se valida formato de entrada -> 0,00 y 999 999 999,00
        if (Pattern.matches("\\d{1,9},\\d{1,2}", numero)) {
            //se divide el numero 0000000,00 -> entero y decimal
                        
            //de da formato al numero decimal
           
            //se convierte el numero a literal
            if (Integer.parseInt(Num[0]) == 0) {//si el valor es cero
                literal = "cero ";
            } else if (Integer.parseInt(Num[0]) > 999999) {//si es millon
                literal = getMillones(Num[0]);
            } else if (Integer.parseInt(Num[0]) > 999) {//si es miles
                literal = getMiles(Num[0]);
            } else if (Integer.parseInt(Num[0]) > 99) {//si es centena
                literal = getCentenas(Num[0]);
            } else if (Integer.parseInt(Num[0]) > 9) {//si es decena
                literal = getDecenas(Num[0]);
            } else {//sino unidades -> 9
               if(Integer.parseInt(auxnumero)==1)
                {
                   literal = "primero "; 
                }else{
                literal = getUnidades(Num[0]);
                }
            }
            
            //devuelve el resultado en mayusculas o minusculas
          
                return (literal).toUpperCase();
           
        } else {//error, no se puede convertir
            return literal = null;
        }
    }

private String getUnidades(String numero) {// 1 - 9
        //si tuviera algun 0 antes se lo quita -> 09 = 9 o 009=9
        String num = numero.substring(numero.length() - 1);
        return UNIDADES[Integer.parseInt(num)];
    }

    private String getDecenas(String num) {// 99                        
        int n = Integer.parseInt(num);
        if (n < 10) {//para casos como -> 01 - 09
            return getUnidades(num);
        } else if (n > 19) {//para 20...99
            if(n<30){
                if(n==20)
                return "veinte";
                else{
                    String u = getUnidades(num);
                    return "veinti"+u;
                }
            }else{
            String u = getUnidades(num);
            
            if (u.equals("")) { //para 20,30,40,50,60,70,80,90
                return DECENAS[Integer.parseInt(num.substring(0, 1)) + 8];
            } else {
                return DECENAS[Integer.parseInt(num.substring(0, 1)) + 8] + "y " + u;
            }
            }
        } else {//numeros entre 11 y 19
            return DECENAS[n - 10];
        }
    }

    private String getCentenas(String num) {// 999 o 099
        if( Integer.parseInt(num)>99 ){//es centena
            if (Integer.parseInt(num) == 100) {//caso especial
                return " cien ";
            } else {
                 return CENTENAS[Integer.parseInt(num.substring(0, 1))] + getDecenas(num.substring(1));
            } 
        }else{//por Ej. 099 
            //se quita el 0 antes de convertir a decenas
            return getDecenas(Integer.parseInt(num)+"");            
        }        
    }

    private String getMiles(String numero) {// 999 999
        //obtiene las centenas
        String c = numero.substring(numero.length() - 3);
        //obtiene los miles
        String m = numero.substring(0, numero.length() - 3);
        String n="";
        //se comprueba que miles tenga valor entero
        if (Integer.parseInt(m) > 0) {
            n = getCentenas(m);           
            return n + "mil " + getCentenas(c);
        } else {
            return "" + getCentenas(c);
        }

    }

    private String getMillones(String numero) { //000 000 000        
        //se obtiene los miles
        String miles = numero.substring(numero.length() - 6);
        //se obtiene los millones
        String millon = numero.substring(0, numero.length() - 6);
        String n = "";
        if(Integer.parseInt(millon) >1){
            n = getCentenas(millon) + "millones ";
        }else{
            n = getUnidades(millon) + "millon ";
        }
        return n + getMiles(miles);        
    }
}

