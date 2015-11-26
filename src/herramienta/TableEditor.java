/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package herramienta;

import java.awt.Component;
import java.sql.Connection;
import javax.swing.AbstractCellEditor;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author Betmart
 */
public class TableEditor extends AbstractCellEditor implements TableCellEditor{
private String oldValue=""; //Valor antiguo de la celda
    private String newValue=""; //valor nuevo de la celda
    private String nameColum="";//nombre de la columna
    private String id="";// Llave del registro
    private JComponent component = new JTextField();
    private String tableName="", columnId;
    private Connection conex;
    private int numOrChar=0; //0 char 1 numero
    
    
    @Override
    public Object getCellEditorValue() {
        return ((JTextField)component).getText();
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        oldValue = value.toString();//Toma valor de celda antes de cualquier modificación
        id = table.getValueAt(row,0).toString();//obtiene el ID unico del registro
        ((JTextField)component).setText(value.toString());//coloca valor de la celda al JTextField
        ((JTextField)component).selectAll();
        return component;
    }
    @Override
    public boolean stopCellEditing() {
        newValue = (String)getCellEditorValue();//Captura nuevo valor de la celda
        ((JTextField)component).select(0, 0);
        //Compara valores, si no son iguales, debe actualizar registro
     /*   if( !newValue.equals(oldValue))
        {
            
            String update = "";
            if(numOrChar==0)//Char
            {
                update = "UPDATE "+tableName+" SET "+nameColum+"="+newValue+" WHERE ";
            }else{ //numero
                
            }
        }*/
        return super.stopCellEditing();
    }
}
