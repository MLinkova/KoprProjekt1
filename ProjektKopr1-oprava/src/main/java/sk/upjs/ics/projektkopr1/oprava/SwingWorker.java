/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.upjs.ics.projektkopr1.oprava;

import java.util.List;
import javax.swing.JProgressBar;

/**
 *
 * Trieda pracujuca s ProgressBarom
 */
public class SwingWorker extends javax.swing.SwingWorker<Void, Integer> {
  private JProgressBar progressBar;
  private Klient klient;

    SwingWorker(JProgressBar StahujemProgresssBar, Klient klient) {
      progressBar=StahujemProgresssBar;
      this.klient=klient;
    }
  
    
    
    @Override
    protected Void doInBackground() throws Exception {
        while(Klient.uzSkopirovane<Klient.DlzkaSuboru){
            if (Thread.currentThread().isInterrupted())
                return null;
            publish(Klient.uzSkopirovane);
        }
        return null;
    }

    @Override
    protected void done() {
        System.out.println("Subor je skopirovany");
        progressBar.setValue(100);
        klient.setPrebiehaKopirovanie(false);
        klient.setPrijate(0);
    }
    

    @Override
    protected void process(List<Integer> chunks) {
        progressBar.setValue(chunks.get(chunks.size()-1)); 
    }
    
}
