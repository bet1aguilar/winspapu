
package winspapus;

import winspapus.manos.guardaman;
import winspapus.equipos.guardaeq;
import winspapus.materiales.guardamat;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.KeyStroke;

/**
 *
 * @author Betmart
 */
public class copia extends javax.swing.JDialog {

    /** A return status code - returned if Cancel button has been pressed */
   public static final int RET_CANCEL = 0;
    /** A return status code - returned if OK button has been pressed */
   public static final int RET_OK = 1;
   private String mtabuid="", descri="", tabnuevo="", descrinuevo="";
   protected Statement stmt = null;
   private Connection conexion=null;
   private String codicove="", numero, numegrup, refere,mbdat_id, porcgad, porcpre, porcutil;
   private String precasu, precunit, redondeo, status, mtabus_id=tabnuevo,cantidad;
   private String sql="";
   Principal obj;
   JProgressBar barra;
   private guardaeq equip;
   private guardamat req;
   private guardaman mano;
   private consulta partida;
    JLabel etiqueta;
    /** Creates new form copia */
    public copia(Principal parent, boolean modal, String mtabu, String descri, Connection conex) {
        super(parent, modal);
        obj=parent;
       
        initComponents();
        mtabuid=mtabu;
        this.descri = descri;
        System.out.println(mtabuid);
        jTextField1.setText(mtabu);
        jTextField2.setText(descri);
       conexion=conex;
       jLabel6.setVisible(false);
       jLabel7.setVisible(false);
       jLabel8.setVisible(false);
        
        // Close the dialog when Esc is pressed
        String cancelName = "cancel";
        InputMap inputMap = getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), cancelName);
        ActionMap actionMap = getRootPane().getActionMap();
        actionMap.put(cancelName, new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                doClose(RET_CANCEL);
            }
        });
    }
    public consulta getprogres(){
        
        return partida;
    }
    
    /** @return the return status of this dialog - one of RET_OK or RET_CANCEL */
    public int getReturnStatus() {
        return returnStatus;
    }
    private void insertatab() throws SQLException{
        int cont=0;
         Statement s = (Statement) conexion.createStatement();
       Statement st = (Statement) conexion.createStatement();
        ResultSet rst = st.executeQuery("SELECT vigencia, pprest, padyga, putild, pcosfin, pimpue FROM Mtabus WHERE id='"+mtabuid+"'");
        String vigencia="", adm="", prest="", util="", costo="", impu="";
        
           
        String sqls="";
        while (rst.next()) {
                 vigencia= rst.getObject(1).toString();
                 adm = rst.getObject(2).toString();
                 prest = rst.getObject(3).toString();
                 util = rst.getObject(4).toString();
                 costo = rst.getObject(5).toString();
                 impu = rst.getObject(6).toString();                   
        }
        rst.close();
        sqls = "INSERT INTO Mtabus VALUES "
                + "('"+tabnuevo+"', '"+descrinuevo+"', " + 

                                                        "'"+vigencia+"'," + 
                                                        
                                                        ""+prest+", " +                                                        

                                                        ""+costo+", " +

                                                        ""+impu+", " + 
                                                        
                                                        ""+adm+", " + 
                                                        
                                                        ""+util+", " +
                
                                                        "1,0);";
        
        
        try {
                s.execute(sqls);
            } catch (SQLException ex) {
                Logger.getLogger(tab.class.getName()).log(Level.SEVERE, null, ex);
            }
        s.close();
        
        
        Statement mat = (Statement) conexion.createStatement();
         sqls = "INSERT INTO mmtabs (mtabus_id, id, descri, desperdi, precio, unidad, status)"+
                 "SELECT DISTINCT tb.id as mtabus_id, mm.id as id, mm.descri as descri, mm.desperdi as desperdi,  "
                + "mm.precio as precio, mm.unidad as unidad, mm.status as status FROM mtabus as tb, mmtabs as mm"
                + " WHERE tb.id='"+tabnuevo+"'  AND mm.mtabus_id='"+mtabuid+"'";
         mat.execute(sqls);
        Statement eq = (Statement) conexion.createStatement();
       sqls = "INSERT INTO metabs (mtabus_id, id, descri, deprecia, precio, status)"
               + "SELECT DISTINCT tb.id as mtabus_id, me.id as id, me.descri as descri, me.deprecia as deprecia,  "
               + "me.precio as precio, me.status as status FROM mtabus as tb, metabs as me"
               + "  WHERE tb.id='"+tabnuevo+"'  AND me.mtabus_id='"+mtabuid+"'";
       eq.execute(sqls);
        Statement man = (Statement) conexion.createStatement();
       sqls="INSERT INTO mmotabs (mtabus_id, id, descri, bono, salario, subsid, status)"
               + "SELECT DISTINCT tb.id as mtabus_id, mmo.id as id, mmo.descri as descri, mmo.bono as bono, "
               + " mmo.salario as salario, mmo.subsid as subsid, mmo.status as status FROM mtabus as tb, mmotabs as mmo"
               + "  WHERE tb.id='"+tabnuevo+"'  AND mmo.mtabus_id='"+mtabuid+"'";
       man.execute(sqls);
       
        
    }
    
    
    
    private void insertapartidas() throws SQLException{
        
         Statement s = (Statement) conexion.createStatement();
       int numfila=0, tiempo = 0, valor = 0, cont = 0;
        ResultSet rs = s.executeQuery("SELECT * FROM Mptabs m WHERE m.mtabus_id = '"+mtabuid+"'");
         String rendimi, unidad;
         Statement st = (Statement) conexion.createStatement();
             
             partida = new consulta(rs, tiempo, conexion, tabnuevo, mtabuid, obj);
             partida.start();
        
       
        
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();

        setTitle("Copiar Precio Referencial");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(100, 100, 100));

        jLabel1.setBackground(new java.awt.Color(91, 91, 95));
        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Copiar Precio Referencial");
        jLabel1.setOpaque(true);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 660, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel2.setText("Código del Listado a Copiar:");

        jTextField1.setEditable(false);

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel3.setText("Nombre del Listado a Copiar:");

        jTextField2.setEditable(false);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel4.setText("Código del Listado Nuevo:");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel5.setText("Nombre del Listado Nuevo:");

        jTextField3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField3KeyTyped(evt);
            }
        });

        jTextField4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField4KeyTyped(evt);
            }
        });

        okButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/copia.png"))); // NOI18N
        okButton.setToolTipText("Copiar");
        okButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                okButtonMouseClicked(evt);
            }
        });

        cancelButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/winspapus/imagenes/eliminar.png"))); // NOI18N
        cancelButton.setToolTipText("Cancelar");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        jLabel6.setForeground(new java.awt.Color(255, 0, 0));
        jLabel6.setText("*");

        jLabel7.setForeground(new java.awt.Color(255, 0, 0));
        jLabel7.setText("*");

        jLabel8.setForeground(new java.awt.Color(255, 0, 0));
        jLabel8.setText("* Campo no puede ser vacio");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField4, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6))
                    .addComponent(jTextField2, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel7)
                .addGap(30, 30, 30))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(534, Short.MAX_VALUE)
                .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30))
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(266, 266, 266)
                .addComponent(jLabel8)
                .addContainerGap(258, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42, 42, 42)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel6))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getRootPane().setDefaultButton(okButton);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        doClose(RET_CANCEL);
    }//GEN-LAST:event_cancelButtonActionPerformed

    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        doClose(RET_CANCEL);
    }//GEN-LAST:event_closeDialog

    private void okButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_okButtonMouseClicked
        int valida=0, yaesta=0;
       tabnuevo = jTextField3.getText().toString();
            descrinuevo = jTextField4.getText().toString();
       
        if(jTextField3.getText().toString().equals("")){
            valida=1;
        }
        if(jTextField4.getText().toString().equals("")){
            valida=1;
        }
        Statement xs=null;
        try {
            xs = (Statement) conexion.createStatement();
            ResultSet rxs = xs.executeQuery("SELECT COUNT(*) as cont FROM Mtabus WHERE id='"+tabnuevo+"'");

         while (rxs.next()) {
                         System.out.println(tabnuevo+"count: "+rxs.getObject("cont").toString());
             if(Integer.valueOf(rxs.getObject(1).toString())>0){
             yaesta=1;
             valida++;
             }
         }
        } catch (SQLException ex) {
            Logger.getLogger(copia.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        if(valida==0){     
            
            try {
                insertatab();
            } catch (SQLException ex) {
                Logger.getLogger(copia.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                insertapartidas();
            } catch (SQLException ex) {
                Logger.getLogger(copia.class.getName()).log(Level.SEVERE, null, ex);
            }
             
             
            doClose(RET_OK);
        }
        else{
            if(yaesta==0){
            jLabel6.setVisible(true);
            jLabel7.setVisible(true);
            jLabel8.setVisible(true);
            }else
            {
                jLabel6.setVisible(true);
                jTextField3.setText("");
                JOptionPane.showMessageDialog(null, "Código de Precio Referencial ya existe ingrese otro.");
            }
        }        // TODO add your handling code here:
    }//GEN-LAST:event_okButtonMouseClicked

private void jTextField3KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField3KeyTyped
 Character c = evt.getKeyChar();
                if(Character.isLetter(c)) {
                    evt.setKeyChar(Character.toUpperCase(c));
                }// TODO add your handling code here:
}//GEN-LAST:event_jTextField3KeyTyped

private void jTextField4KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField4KeyTyped
 Character c = evt.getKeyChar();
                if(Character.isLetter(c)) {
                    evt.setKeyChar(Character.toUpperCase(c));
                }// TODO add your handling code here:
}//GEN-LAST:event_jTextField4KeyTyped
    
    private void doClose(int retStatus) {
        returnStatus = retStatus;
        setVisible(false);
        dispose();
    }

    /**
     * @param args the command line arguments
     */
   
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JButton okButton;
    // End of variables declaration//GEN-END:variables
    private int returnStatus = RET_CANCEL;
}
