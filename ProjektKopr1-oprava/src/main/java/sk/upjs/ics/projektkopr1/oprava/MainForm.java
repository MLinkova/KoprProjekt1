/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.upjs.ics.projektkopr1.oprava;

/**
 *
 * @author Michaela
 */
public class MainForm extends javax.swing.JFrame {

    /**
     * Creates new form MainForm
     */
    private Klient klient;
    public MainForm() {
        initComponents();
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pocetTCPSpojeniLabel = new javax.swing.JLabel();
        pocetTCPSpojeniTextField = new javax.swing.JTextField();
        spustiButton = new javax.swing.JButton();
        prerusButton = new javax.swing.JButton();
        pokracujButton = new javax.swing.JButton();
        zrusButton = new javax.swing.JButton();
        stahujemProgressBar = new javax.swing.JProgressBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        pocetTCPSpojeniLabel.setText("Zadajte počet TCP spojení : ");

        pocetTCPSpojeniTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pocetTCPSpojeniTextFieldActionPerformed(evt);
            }
        });

        spustiButton.setText("Spustiť");
        spustiButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                spustiButtonActionPerformed(evt);
            }
        });

        prerusButton.setText("Prerušiť");
        prerusButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prerusButtonActionPerformed(evt);
            }
        });

        pokracujButton.setText("Pokračovať");
        pokracujButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pokracujButtonActionPerformed(evt);
            }
        });

        zrusButton.setText("Zrušiť");
        zrusButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zrusButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(68, 68, 68)
                                .addComponent(pocetTCPSpojeniLabel)
                                .addGap(28, 28, 28)
                                .addComponent(pocetTCPSpojeniTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addComponent(spustiButton)
                                .addGap(18, 18, 18)
                                .addComponent(prerusButton)
                                .addGap(18, 18, 18)
                                .addComponent(pokracujButton)
                                .addGap(18, 18, 18)
                                .addComponent(zrusButton)))
                        .addGap(0, 29, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(stahujemProgressBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pocetTCPSpojeniLabel)
                    .addComponent(pocetTCPSpojeniTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addComponent(stahujemProgressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(spustiButton)
                    .addComponent(prerusButton)
                    .addComponent(pokracujButton)
                    .addComponent(zrusButton))
                .addContainerGap(30, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void pocetTCPSpojeniTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pocetTCPSpojeniTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pocetTCPSpojeniTextFieldActionPerformed

    private void spustiButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_spustiButtonActionPerformed
      klient=new Klient(Integer.parseInt(pocetTCPSpojeniTextField.getText()));
      klient.spusti();
      setProgressBar();
    }//GEN-LAST:event_spustiButtonActionPerformed

    private void prerusButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prerusButtonActionPerformed
       klient.prerus();
    }//GEN-LAST:event_prerusButtonActionPerformed

    private void pokracujButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pokracujButtonActionPerformed
        klient=new Klient();
        klient.pokracuj();
        setProgressBar();
    }//GEN-LAST:event_pokracujButtonActionPerformed

    private void zrusButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_zrusButtonActionPerformed
      klient.zrusit();
    }//GEN-LAST:event_zrusButtonActionPerformed

    public void setProgressBar(){
      //   SwingWorker swing= new SwingWorker(stahujemProgressBar,klient,);
        // swing.execute();
        
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel pocetTCPSpojeniLabel;
    private javax.swing.JTextField pocetTCPSpojeniTextField;
    private javax.swing.JButton pokracujButton;
    private javax.swing.JButton prerusButton;
    private javax.swing.JButton spustiButton;
    private javax.swing.JProgressBar stahujemProgressBar;
    private javax.swing.JButton zrusButton;
    // End of variables declaration//GEN-END:variables
}
