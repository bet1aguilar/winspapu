/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package config;

import java.math.BigDecimal;

/**
 *
 * @author spapu1
 */
public class Redondeo {
    public BigDecimal redondearDosDecimales(BigDecimal num)
    {
        num = num.setScale(2, BigDecimal.ROUND_HALF_UP);
        return num;
    }
    
}
