/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.upjs.ics.projektkopr1.oprava;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JProgressBar;
import sk.upjs.ics.kopr1.Konstanty;

/**
 *
 * Trieda pracujuca s ProgressBarom
 */
public class SwingWorker extends javax.swing.SwingWorker<String, Integer> {
  private JProgressBar progressBar;
  private Klient klient;
  private  Set<Long> readedOffSets;
  private long dlzkaSuboru;
  private int pocetTCPSpojeni;
  private File subor;
   private AtomicInteger finished = new AtomicInteger(0);
   private List<Thread> threads;
    private List<PrijimamSubor> rThreads;
    private RandomAccessFile raf;
    private int interupted;

  
    public SwingWorker(JProgressBar StahujemProgresssBar, Klient klient, Set<Long> readedOffSets, int pocetTcp, long dlzkaSuboru) {
      progressBar=StahujemProgresssBar;
      this.klient=klient;
      this.readedOffSets=readedOffSets;
      this.pocetTCPSpojeni=pocetTcp;
      this.dlzkaSuboru=dlzkaSuboru;
    }
  
    
    
    @Override
    protected String doInBackground() throws Exception {
        subor=new File(Konstanty.FILE_TO_RECEIVED);
        finished.set(readedOffSets.size());
        
        raf = new RandomAccessFile(subor, "rw");
        raf.setLength(dlzkaSuboru);

        interupted = -1;
        threads = new ArrayList<>();
        rThreads = new ArrayList<>();
        for (int i = 0; i < pocetTCPSpojeni; i++) {
            PrijimamSubor prijimam = new PrijimamSubor();
            Thread thread = new Thread(prijimam);
            thread.start();
            threads.add(thread);
            rThreads.add(prijimam);
        }

        while (finished.get() < pocetTCPSpojeni) {
            int percent = (int) (finished.get() * 100.0 / pocetTCPSpojeni);
            publish(percent);
            Thread.sleep(10);
        }

 
        return "USPECH";
    }

    @Override
    protected void done() {
        try {
            String result=get();
            System.out.println("Subor je skopirovany");
            progressBar.setValue(100);
            klient.informAboutFinishedCopy();
            try {
                raf.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            File file = new File(Konstanty.ULOZ_SUBOR);
            if (file.exists()) {
                file.delete();
            }
            
        } catch (InterruptedException ex) {
            Logger.getLogger(SwingWorker.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
          Logger.getLogger(SwingWorker.class.getName()).log(Level.SEVERE, null, ex);
      }catch(CancellationException e ){
           rThreads.stream().forEach((t) -> {
                t.terminate();
            });
            threads.stream().forEach((t) -> {
                t.interrupt();
            });

            try {
                raf.close();
                if (interupted == Konstanty.ZASTAVIT) {
                    subor.delete();
                }
            } catch (IOException ex) {
                throw new RuntimeException("Súbor sa nepodarilo zatvoriť/vymazať.", ex.getCause());
            }
            if (interupted == Konstanty.ZASTAVIT) {
                progressBar.setValue(0);
                
            } else {
                int percent =(int) (finished.get() * 100.0 / pocetTCPSpojeni);
                progressBar.setValue(percent);
               
            }
      
      }
         
    }
    

    @Override
    protected void process(List<Integer> chunks) {
       if (interupted == -1) {
            int value = chunks.get(chunks.size() - 1);
            progressBar.setValue(value);
 
        }
    }
    public void interrupted(int index){
        interupted=index;
    }
    
}
