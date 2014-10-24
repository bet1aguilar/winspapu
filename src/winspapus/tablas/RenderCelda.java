/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package winspapus.tablas;
import java.awt.Component;
import java.awt.Color;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
/**
 *
 * @author Betmart
 */
public class RenderCelda extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,boolean hasFocus, int row, int column) 
    {
        Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if( value instanceof Integer )
        {
            Integer amount = (Integer) value;
            if( amount.intValue() == 0 )
            {
                cell.setBackground(Color.WHITE);
                cell.setForeground(Color.WHITE);
            }
            else
            {
             if(amount.intValue()==1){
                 
              cell.setBackground(new Color(184,184,190));
                    cell.setForeground(new Color(184,184,190));
             }
             else{
              cell.setBackground(new Color(238, 233, 233));
              cell.setForeground(new Color(238, 233, 233));
             }
            }
        }
        return cell;
    }
}
