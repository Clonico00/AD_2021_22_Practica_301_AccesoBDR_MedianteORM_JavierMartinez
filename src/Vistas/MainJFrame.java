package Vistas;

import Controladores.NewHibernateUtil;
import Modelos.FormaPago;
import Modelos.Prestamo;
import Modelos.Recibo;
import Modelos.ReciboId;
import java.math.BigDecimal;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class MainJFrame extends javax.swing.JFrame {
    private Session session;
    private Transaction tx;
    
    public MainJFrame() {
        initComponents();
        prestamosTabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); 
                
        rellenarFPComboBox();       
        rellenarTablaPrestamos();  
        //el siguiente metodo rellena al iniciar la aplicacion con el primer valor que haya en la tabla prestamos
        rellenarTablaRecibosDefecto();
    }
    
    private void rellenarFPComboBox(){
        //creamos la sesion con el metodo del NewHibernateUtil
        session =  NewHibernateUtil.getSessionFactory().openSession();
        try{
            //Iniciamos la transaccion, y le pasamos la consulta al objeto SQLQuery
            //y le pasamos la clase FormaPago
            tx = session.beginTransaction();
            String sql = "SELECT * FROM FORMA_PAGO";
            SQLQuery query = session.createSQLQuery(sql);
            query.addEntity(FormaPago.class);
            //Creamos la lista con los datos de la clase FormaPago y la recorremos con un Iterator
            //en cada vuelta del bucle creamos un objeto de la clase,
            //y lo añado al ComboBox,llamando al metodo de clase
            List list = query.list();
            for (Iterator iterator = list.iterator(); iterator.hasNext();){
                FormaPago fp  = (FormaPago) iterator.next();
                fPagoComboBox.addItem(fp);
            }
            tx.commit();
        }catch (HibernateException e) {
            if (tx!=null) 
                tx.rollback();
        }finally {
            session.close();
        }

    }
   
    private void rellenarTablaPrestamos(){
        //inicializamos la sesion y el model de la tabla
        session = NewHibernateUtil.getSessionFactory().openSession();
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnCount(0);
        model.setRowCount(0);
        try {
            //me creo un Criteria al cual le paso la clase Prestamo de la cual saque los datos
            //que estan en mi base de datos. Luego me creo mi lista para los objetos que me devuelva el objeto criteria
            Criteria p = session.createCriteria(Prestamo.class);
            List<Prestamo> list = p.list();
           
            model.addColumn("NºPrestamo");
            model.addColumn("Fecha");
            model.addColumn("Importe");
            model.addColumn("Importe pagado");
            model.addColumn("Forma de pago");
            
            //recorro la lista, y cada elemento lo guardo en un objeto de tipo de Prestamo
            //a la hora de acceder a Forma de pago, llamo tambien al toString, para que me devuelva solo un String y no
            //un objeto completo
            for (Prestamo pt : list) {
                model.addRow(new Object[]{pt.getNPrestamo(),pt.getFecha(),pt.getImporte(),pt.getImportePagado(),pt.getFormaPago().toString()});
            }
        } catch (HibernateException he) {
            JOptionPane.showMessageDialog(null,"Erro"+he.getMessage(),"",JOptionPane.ERROR_MESSAGE);
        } finally {
            session.close();
            prestamosTabla.setModel(model);

        }

    }
    
    private void rellenarTablaRecibosDefecto(){
    //iniciamos la sesion y creamos el model de la tabla
        DefaultTableModel model = new DefaultTableModel();
        session = NewHibernateUtil.getSessionFactory().openSession();
        BigDecimal nPrestamo = (BigDecimal) prestamosTabla.getValueAt(0, 0);
        model.setColumnCount(0);
        model.setRowCount(0);
        try {        
            //le creo sus columnas
            model.addColumn("NºPrestamo");
            model.addColumn("NªRecibo");
            model.addColumn("Fecha");
            model.addColumn("Importe");
            model.addColumn("Fecha pagado");
            
            //iniciamos la transaccion, creamos la consulta a realizar, en este caso con la condicion de que solo muestre
            //los recibos asociados al prestamo seleccionado en la tabla
            tx = session.beginTransaction();
            String sql = "SELECT * FROM RECIBO WHERE N_PRESTAMO = "+nPrestamo;
            SQLQuery query = session.createSQLQuery(sql);
            query.addEntity(Recibo.class);
            List<Recibo> list = query.list();
            //recorro mi lista, y voy pasandolo los datos recogidos por los metodos a la tabla
            for (Recibo rb : list) {
                //el metodo getNRecibo es uno creado propio que a traves del objeto Reciboid id, llamo al metodo getNRecibo
                // el cual esta en la clase Reciboid
                model.addRow(new Object[]{nPrestamo,rb.getNRecibo(),rb.getFecha(),rb.getImporte(),rb.getFechaPagado()});
            }
            tx.commit();
        } catch (HibernateException he) {
            JOptionPane.showMessageDialog(null,"Erro"+he.getMessage(),"",JOptionPane.ERROR_MESSAGE);
        } finally {
            recibosTabla.setModel(model);

        }
            session.close();

    
    }
    
    
    private void rellenarTablaRecibos(BigDecimal nPrestamo){
        //iniciamos la sesion y creamos el model de la tabla
        DefaultTableModel model = new DefaultTableModel();
        session = NewHibernateUtil.getSessionFactory().openSession();
        model.setColumnCount(0);
        model.setRowCount(0);
        try {        
            //le creo sus columnas
            model.addColumn("NºPrestamo");
            model.addColumn("NªRecibo");
            model.addColumn("Fecha");
            model.addColumn("Importe");
            model.addColumn("Fecha pagado");
            
            //iniciamos la transaccion, creamos la consulta a realizar, en este caso con la condicion de que solo muestre
            //los recibos asociados al prestamo seleccionado en la tabla
            tx = session.beginTransaction();
            String sql = "SELECT * FROM RECIBO WHERE N_PRESTAMO = "+nPrestamo;
            SQLQuery query = session.createSQLQuery(sql);
            query.addEntity(Recibo.class);
            List<Recibo> list = query.list();
            //recorro mi lista, y voy pasandolo los datos recogidos por los metodos a la tabla
            for (Recibo rb : list) {
                //el metodo getNRecibo es uno creado propio que a traves del objeto Reciboid id, llamo al metodo getNRecibo
                // el cual esta en la clase Reciboid
                model.addRow(new Object[]{nPrestamo,rb.getNRecibo(),rb.getFecha(),rb.getImporte(),rb.getFechaPagado()});
            }
            tx.commit();
        } catch (HibernateException he) {
            JOptionPane.showMessageDialog(null,"Erro"+he.getMessage(),"",JOptionPane.ERROR_MESSAGE);
        } finally {
            recibosTabla.setModel(model);

        }
            session.close();

    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        prestamosTabla = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        recibosTabla = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        fPagoComboBox = new javax.swing.JComboBox<>();
        pagarReciboButton = new javax.swing.JButton();
        fechaChooser = new com.toedter.calendar.JDateChooser();
        nPrestamoText = new javax.swing.JFormattedTextField();
        importRecibText = new javax.swing.JFormattedTextField();
        addPrestamoButton = new javax.swing.JButton();
        deletePrestamoButton = new javax.swing.JButton();
        modifyPrestamoButton = new javax.swing.JButton();
        salirButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("AD_2021_22_Practica_301_AccesoBDR_MedianteORM_JavierMartinez");

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        prestamosTabla.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        prestamosTabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
            },
            new String [] {
            }
        ));
        prestamosTabla.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                prestamosTablaMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(prestamosTabla);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 715, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        recibosTabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
            }
        ));
        recibosTabla.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                recibosTablaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(recibosTabla);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 436, Short.MAX_VALUE)
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        jLabel1.setText("NºPrestamo");

        jLabel2.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        jLabel2.setText("Fecha");

        jLabel3.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        jLabel3.setText("Importe");

        jLabel4.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        jLabel4.setText("Forma de Pago");

        fPagoComboBox.setFont(new java.awt.Font("Dialog", 0, 13)); // NOI18N
        fPagoComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] {}));

        pagarReciboButton.setText("Pagar");
        pagarReciboButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pagarReciboButtonActionPerformed(evt);
            }
        });

        nPrestamoText.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));

        importRecibText.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 72, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(fechaChooser, javax.swing.GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE)
                            .addComponent(nPrestamoText)))
                    .addComponent(pagarReciboButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel3))
                        .addGap(48, 48, 48)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(fPagoComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(importRecibText))))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(nPrestamoText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(fechaChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(21, 21, 21)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3)
                    .addComponent(importRecibText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(jLabel4))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(fPagoComboBox)))
                .addGap(57, 57, 57)
                .addComponent(pagarReciboButton)
                .addContainerGap(592, Short.MAX_VALUE))
        );

        addPrestamoButton.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        addPrestamoButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/agregar-archivo.png"))); // NOI18N
        addPrestamoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addPrestamoButtonActionPerformed(evt);
            }
        });

        deletePrestamoButton.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        deletePrestamoButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/eliminar.png"))); // NOI18N
        deletePrestamoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deletePrestamoButtonActionPerformed(evt);
            }
        });

        modifyPrestamoButton.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        modifyPrestamoButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/editar.png"))); // NOI18N
        modifyPrestamoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modifyPrestamoButtonActionPerformed(evt);
            }
        });

        salirButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/salir.png"))); // NOI18N
        salirButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salirButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(addPrestamoButton)
                        .addGap(18, 18, 18)
                        .addComponent(deletePrestamoButton)
                        .addGap(18, 18, 18)
                        .addComponent(modifyPrestamoButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(salirButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(salirButton, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(addPrestamoButton)
                        .addComponent(deletePrestamoButton)
                        .addComponent(modifyPrestamoButton)))
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void addPrestamoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addPrestamoButtonActionPerformed
        //creamos el modelo y generamos la columnas de la tabla
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("NºPrestamo");
        model.addColumn("NªRecibo");
        model.addColumn("Fecha");
        model.addColumn("Importe");
        model.addColumn("Fecha pagado");
        
        //comprobamos que estan rellenos los campos
        if(nPrestamoText.getText().isEmpty() || importRecibText.getText().isEmpty() || fPagoComboBox.toString().isEmpty() || fechaChooser.toString().isEmpty()){            
            JOptionPane.showMessageDialog(null,"Rellene todos los campos por favor","ERROR", 0);
           
        }else{
            //recogemos los datos introducidos y los cambiamos al tipo de dato correcto
            //luego iniciamos sesion y comenzamos la transaccion           
            BigDecimal nPrestamo = BigDecimal.valueOf(Double.valueOf(nPrestamoText.getText()));
            FormaPago fp =(FormaPago) fPagoComboBox.getSelectedItem();                                     
            BigDecimal importe = BigDecimal.valueOf(Double.valueOf(importRecibText.getText().replace(",", ".")));
            Calendar fecha=fechaChooser.getCalendar();
            Date fechaP = fecha.getTime();

            session = NewHibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();

            //comprobamos que ese NºPrestamo no exista en nuestra clase, si es null lo creamos e introducimos y si no, le avisamos al usuario
            //y ponemos el texto del NºRecibo vacio y cerramos la transaccion y la sesion.
            if(session.get(Prestamo.class, nPrestamo)==null){
                //guardamos los datos introducidos en un objeto de la clase Prestamo para asi poder guardarlo.
                //para el importe pagado le ponemos por defecto siempre 0
                BigDecimal importePagado = new BigDecimal(0);
                Prestamo prestamo = new Prestamo(nPrestamo, fp, fechaP, importe, importePagado);
                session.save(prestamo); 
                session.getTransaction().commit();
                session.close();
                //llamamos a mi metodo de rellenar tablas para que se actualize con el objeto creado automaticamente
                rellenarTablaPrestamos();
            }else{
                JOptionPane.showMessageDialog(null,"Clave primaria de la tabla Prestamos violada, introduzca otro NºPrestamo por favor","ERROR", 0);
                nPrestamoText.setText("");
                session.getTransaction().commit();
                session.close();

            }

                    
        }
        
            
    }//GEN-LAST:event_addPrestamoButtonActionPerformed

    private void recibosTablaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_recibosTablaMouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_recibosTablaMouseClicked

    private void prestamosTablaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_prestamosTablaMouseClicked
        // Recogemos la fila que ha sido clicada, y vamos recogiendo los datos de la tabla y convirtiendolos
        // para mostrarlos en los FormattedTextField
        int row = prestamosTabla.getSelectedRow();
        BigDecimal nPrestamo = (BigDecimal) prestamosTabla.getValueAt(row, 0);
        String nPrestamos = String.valueOf(nPrestamo.toPlainString());

        Date fecha = (Date) prestamosTabla.getValueAt(row, 1);
        Calendar fechaP = Calendar.getInstance();
        fechaP.setTime(fecha);
        
        BigDecimal importe = (BigDecimal) prestamosTabla.getValueAt(row, 2);
        String importes = String.valueOf(importe.toPlainString());

        nPrestamoText.setText(nPrestamos);
        fechaChooser.setCalendar(fechaP);
        importRecibText.setText(importes);
        //aqui llamamos al metodo para rellenar la tabla de los recibos, y le pasamos el numero del prestamo
        rellenarTablaRecibos(nPrestamo);
        
    }//GEN-LAST:event_prestamosTablaMouseClicked

    private void deletePrestamoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deletePrestamoButtonActionPerformed
        //creamos el modelo y generamos la columnas de la tabla
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("NºPrestamo");
        model.addColumn("NªRecibo");
        model.addColumn("Fecha");
        model.addColumn("Importe");
        model.addColumn("Fecha pagado");
        
        //comprobamos que estan rellenos el campo del NºPrestamo que es el que usaremos para borrar el objeto
        if(nPrestamoText.getText().isEmpty()){            
            JOptionPane.showMessageDialog(null,"Rellene el NºPrestamo que quiere borrar","ERROR", 0);
           
        }else{
            //recogemos el NºPrestamo y lo cambiamos al tipo de dato correcto
            //luego iniciamos sesion y comenzamos la transaccion
            BigDecimal nPrestamo = BigDecimal.valueOf(Double.valueOf(nPrestamoText.getText()));  
            session = NewHibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();

            //comprobamos que ese NºPrestamo  exista en nuestra clase, si es null le avisamos que no existe y si no, 
            // buscamos por clave primaria y lo borramos y cerramos la transaccion y la sesion.
            if(session.get(Prestamo.class, nPrestamo)==null){
                JOptionPane.showMessageDialog(null,"No existe ese prestamo","ERROR", 0);
                nPrestamoText.setText("");
                session.getTransaction().commit();
                session.close();                
            }else{
                //guardamos los datos introducidos en un objeto de la clase Prestamo para asi poder guardarlo.
                //para el importe pagado le ponemos por defecto siempre 0
                Prestamo prestamo = (Prestamo) session.get(Prestamo.class, nPrestamo);
                //comprobamos que para ese prestamo no tiene ningun recibo pagado
                if(prestamo.getImportePagado().doubleValue()!=0){
                    JOptionPane.showMessageDialog(null,"No puedes borrar un prestamo que ya se haya pagado o que este en proceso","ERROR", 0);
                    nPrestamoText.setText("");
                    session.getTransaction().commit();
                    session.close(); 
                }else {
                    session.delete(prestamo); 
                    session.getTransaction().commit();
                    session.close();
                    //llamamos a mi metodo de rellenar tablas para que se actualize con el objeto borrado automaticamente
                    rellenarTablaPrestamos();
 
                }
           }
        }
        
    }//GEN-LAST:event_deletePrestamoButtonActionPerformed

    private void salirButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_salirButtonActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_salirButtonActionPerformed

    private void modifyPrestamoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modifyPrestamoButtonActionPerformed
        //creamos el modelo y generamos la columnas de la tabla
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("NºPrestamo");
        model.addColumn("NªRecibo");
        model.addColumn("Fecha");
        model.addColumn("Importe");
        model.addColumn("Fecha pagado");
        
        //comprobamos que estan rellenos el campo de la fecha, importe y forma de pago que son los que usaremos 
        //para actualizar el objeto
        if(fechaChooser.getCalendar().toString().isEmpty() || importRecibText.getText().isEmpty()) {            
            JOptionPane.showMessageDialog(null,"Rellene los campos de fecha,importe, NºPrestamo y forma de pago para modificiar ","ERROR", 0);
           
        }else{
            //recogemos los datos y lo cambiamos al tipo de dato correcto
            //luego iniciamos sesion y comenzamos la transaccion
            BigDecimal nPrestamo = BigDecimal.valueOf(Double.valueOf(nPrestamoText.getText()));  
            FormaPago fp =(FormaPago) fPagoComboBox.getSelectedItem();                                     
            BigDecimal importe = BigDecimal.valueOf(Double.valueOf(importRecibText.getText().replace(",", ".")));
            Calendar fecha=fechaChooser.getCalendar();      
            Date fechaP = fecha.getTime();
            session = NewHibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();

            //comprobamos que ese NºPrestamo  exista en nuestra clase, si es null le avisamos que no existe 
            if(session.get(Prestamo.class, nPrestamo)==null){
                JOptionPane.showMessageDialog(null,"No existe ese prestamo","ERROR", 0);
                nPrestamoText.setText("");
                session.getTransaction().commit();
                session.close();                
            }else{
                //recogemos el objeto con esa clave primaria
                Prestamo prestamo = (Prestamo) session.get(Prestamo.class, nPrestamo);
                //comprobamos que para ese prestamo no tiene ningun recibo pagado
                if(prestamo.getImportePagado().doubleValue()!=0){
                    JOptionPane.showMessageDialog(null,"No puedes modificar un prestamo que ya se haya pagado o que este en proceso","ERROR", 0);
                    nPrestamoText.setText("");
                    session.getTransaction().commit();
                    session.close(); 
                }else {
                    //le modificamos los valores al objeto
                    prestamo.setFecha(fechaP);
                    prestamo.setImporte(importe);
                    prestamo.setFormaPago(fp);
                    session.update(prestamo); 
                    session.getTransaction().commit();
                    session.close();
                    //llamamos a mi metodo de rellenar tablas para que se actualize con el objeto borrado automaticamente
                    rellenarTablaPrestamos();
 
                }
           }
        }

    }//GEN-LAST:event_modifyPrestamoButtonActionPerformed

    private void pagarReciboButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pagarReciboButtonActionPerformed
       //primero comprobamos que haya algun recibo seleciionado para poder pagarlo
        if(recibosTabla.getSelectedRow()==-1){
            JOptionPane.showMessageDialog(null,"Seleccione una file de la tabla Recibos para poder pagarla","ERROR", 0);
            //comprobamos que ese recibo no se ha pagado ya
        }else if (recibosTabla.getValueAt(recibosTabla.getSelectedRow(),4)==null){
            //creamos el modelo y generamos la columnas de la tabla
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("NºRecibo");
            model.addColumn("NªFecha");
            model.addColumn("Importe");
            model.addColumn("Fecha de pago");
            
            //conseguimos la fecha actual, y el numero del prestamo y del recibo y los convertimos a tipos mas adecuados
            Date date = new Date();            
            BigDecimal nPrestamo = (BigDecimal) recibosTabla.getValueAt(recibosTabla.getSelectedRow(),0);
            Long nRecibo = (Long) recibosTabla.getValueAt(recibosTabla.getSelectedRow(), 1);
            
            //empezamos la sesion para actualizar en primer lugar el objeto Recibo que queremos pagar con la fecha actual
            session = NewHibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            //nos creamos un onjketo ReciboId, porque este es el que contiene el nPrestamo y el Nrecibo asociados
            ReciboId reciboid = new ReciboId(nPrestamo, nRecibo);
            //le pasamos este objeto como clave primaria
            Recibo recibo = (Recibo) session.get(Recibo.class,reciboid);
            recibo.setFechaPagado(date);            
            session.getTransaction().commit();
            session.close();      
            //actualizamos la tbla de los recibos
            rellenarTablaRecibos(nPrestamo);
            
            //iniciamos nuevamente sesion para actualizar el importe pagado en la tabla Prestamos
            session = NewHibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            
            //del recibo pagado anteriormente cogemos el importe pagado
            reciboid = new ReciboId(nPrestamo, nRecibo);
            recibo = (Recibo) session.get(Recibo.class,reciboid);            
            BigDecimal importeRecib = recibo.getImporte();
            
            //y del prestamo asociado a ese recibo tambien. Para asi ir sumando los valores del importe pagado
            Prestamo prestamo = (Prestamo) session.get(Prestamo.class, nPrestamo);            
            BigDecimal importePrest = prestamo.getImportePagado();
            
            Double importe  = importeRecib.doubleValue()+importePrest.doubleValue();  
            
            //actualizamos el prestamo con el importe pagado total
            prestamo.setImportePagado(BigDecimal.valueOf(importe));
            session.getTransaction().commit();
            
            session.close(); 
            //y actualizamos la tabla de los prestamos
            rellenarTablaPrestamos();
        }else{
            JOptionPane.showMessageDialog(null,"Recibo ya pagado","ERROR", 0);

        }
    }//GEN-LAST:event_pagarReciboButtonActionPerformed

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainJFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addPrestamoButton;
    private javax.swing.JButton deletePrestamoButton;
    private javax.swing.JComboBox<Object> fPagoComboBox;
    private com.toedter.calendar.JDateChooser fechaChooser;
    private javax.swing.JFormattedTextField importRecibText;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton modifyPrestamoButton;
    private javax.swing.JFormattedTextField nPrestamoText;
    private javax.swing.JButton pagarReciboButton;
    private javax.swing.JTable prestamosTabla;
    private javax.swing.JTable recibosTabla;
    private javax.swing.JButton salirButton;
    // End of variables declaration//GEN-END:variables
}
