/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import mediaEncoder.TreatMediaFiles;
import view.DesignerView;
import view.ParameterView;
import java.io.FileNotFoundException;
import javax.swing.SwingWorker;
import mediaEncoder.TreatMediaFiles;
import parameters.NoiseParameter;


/**
 *@author Jean-Étienne
 */
public class Main {
    
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
            java.util.logging.Logger.getLogger(DesignerView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DesignerView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DesignerView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DesignerView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
              //Thread encoding media not already taken care of
     
        TreatMediaFiles.treatFiles();

        final DesignerView view = new DesignerView();
        final ParameterView parameterView = new ParameterView(); 
        
        //display the form
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {               

                view.setVisible(true);
                
                parameterView.setVisible(true);
            }
        });
    }
    
}
