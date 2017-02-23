/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package sk.upjs.ics.kopr1;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;


/**
 *
 * @author Michaela
 */
public class Klient {
    
    
    private static int pocetTCPspojeni;
    private static boolean kopirujem=false;
    private SwingWorker worker;
    private Set<Long> precitaneofsety;
    private Socket soket;
    private long dlzkaSuboru;
    private   JProgressBar progressBar;
    private MainForm mainForm;
    
    public Klient(int pocetTCP,  JProgressBar progressBar){
        pocetTCPspojeni=pocetTCP;
        this.progressBar=progressBar;
    }
    
    public Klient( MainForm mainForm) {
        this.mainForm=mainForm;
        
    }
    
    public void spusti(){
        DatagramSocket dataSoket= null;
        ByteArrayInputStream bais = null;
        ObjectInputStream ois=null;
        try {
            dataSoket= new DatagramSocket();
            soket=new Socket(Konstanty.SERVER_NAME, Konstanty.SERVER_PORT);
            String sprava = "POSLI "+pocetTCPspojeni;
            DatagramPacket paket = new DatagramPacket(sprava.getBytes(), sprava.getBytes().length,
                    InetAddress.getByName(Konstanty.SERVER_NAME), Konstanty.SERVER_PORT);
            dataSoket.send(paket);
           
            
            byte[] buffer = new byte[soket.getReceiveBufferSize()];
            DatagramPacket prijatyPaket = new DatagramPacket(buffer, buffer.length);
            dataSoket.receive(prijatyPaket);
            bais = new ByteArrayInputStream(prijatyPaket.getData());
            ois = new ObjectInputStream(bais);
            dlzkaSuboru = ois.readLong();
            
            precitaneofsety = Collections.synchronizedSet(new HashSet<Long>());
            kopirujem = true;
            
            worker = new SwingWorker(progressBar,this,precitaneofsety,pocetTCPspojeni,dlzkaSuboru);
            worker.execute();
            
            
            
        } catch (SocketException ex) {
            Logger.getLogger(Klient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Klient.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(dataSoket!=null){
                    dataSoket.close();
                }
                if(ois!=null){
                    
                    ois.close();
                    
                }
                if(bais!=null){
                    bais.close();
                }
                if(soket!=null){
                    soket.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(Klient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
    void informAboutFinishedCopy() {
        kopirujem=false;
    }
    
    void prerusit() {
        kopirujem = false;
        DatagramSocket dataSoket= null;
        try {
            dataSoket= new DatagramSocket();
            soket=new Socket(Konstanty.SERVER_NAME, Konstanty.SERVER_PORT);
            String sprava = Konstanty.PRERUS;
            DatagramPacket paket = new DatagramPacket(sprava.getBytes(), sprava.getBytes().length,
                    InetAddress.getByName(Konstanty.SERVER_NAME), Konstanty.SERVER_PORT);
            dataSoket.send(paket);
            
            worker.interrupted(pocetTCPspojeni);
            worker.cancel(true);
            
            
            File subor= new File(Konstanty.ULOZ_SUBOR);
            PrintWriter pw= new PrintWriter(subor);
            pw.write(pocetTCPspojeni+"\n");
            for (Long l : precitaneofsety) {
                pw.write(l + "\n");
            }
            pw.close();
            
            
        } catch (SocketException ex) {
            Logger.getLogger(Klient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Klient.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(dataSoket!=null){
                    dataSoket.close();
                }
                if(soket!=null){                    
                    soket.close();                    
                }
            } catch (IOException ex) {
                Logger.getLogger(Klient.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }
    
    void pokracuj() {
        File f= new File(Konstanty.ULOZ_SUBOR);
        if(f.exists() && f.length()>0){
            DatagramSocket dataSoket= null;
            ByteArrayInputStream bais = null;
            ObjectInputStream ois=null;
            try {
                dataSoket= new DatagramSocket();
                soket=new Socket(Konstanty.SERVER_NAME, Konstanty.SERVER_PORT);
                String sprava = Konstanty.POSLI_ZNOVA+" ";
                precitaneofsety = Collections.synchronizedSet(new HashSet<Long>());
                Scanner sc= new Scanner(f);
                pocetTCPspojeni=Integer.parseInt(sc.next());
                sprava=sprava+pocetTCPspojeni+" ";
                while(sc.hasNext()){
                    long l =Long.parseLong(sc.next());
                    precitaneofsety.add(l);
                    sprava=sprava+l+" ";
                }
                sc.close();
                DatagramPacket paket = new DatagramPacket(sprava.getBytes(), sprava.getBytes().length,
                        InetAddress.getByName(Konstanty.SERVER_NAME), Konstanty.SERVER_PORT);
                dataSoket.send(paket);
                
                byte[] buffer = new byte[soket.getReceiveBufferSize()];
                DatagramPacket prijatyPaket = new DatagramPacket(buffer, buffer.length);
                dataSoket.receive(prijatyPaket);
                bais = new ByteArrayInputStream(prijatyPaket.getData());
                ois = new ObjectInputStream(bais);
                dlzkaSuboru = ois.readLong();
                kopirujem = true;
                
                worker = new SwingWorker(progressBar,this,precitaneofsety,pocetTCPspojeni,dlzkaSuboru);
                worker.execute();
                
                
                
            } catch (SocketException ex) {
                Logger.getLogger(Klient.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Klient.class.getName()).log(Level.SEVERE, null, ex);
            }finally{
                try {
                    if(dataSoket!=null){
                        dataSoket.close();
                    }
                    if(ois!=null){
                        
                        ois.close();
                        
                    }
                    if(bais!=null){
                        bais.close();
                    }
                } catch (IOException ex) {
                    Logger.getLogger(Klient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        }else{
            JOptionPane.showMessageDialog(mainForm, "Nebolo prerusene kopirovanie!");}
    }
    
    void zrusit() {
        kopirujem=false;
        DatagramSocket dataSoket= null;
        try {
            dataSoket= new DatagramSocket();
            soket=new Socket(Konstanty.SERVER_NAME, Konstanty.SERVER_PORT);
            String sprava = Konstanty.ZRUS;
            DatagramPacket paket = new DatagramPacket(sprava.getBytes(), sprava.getBytes().length,
                    InetAddress.getByName(Konstanty.SERVER_NAME), Konstanty.SERVER_PORT);
            dataSoket.send(paket);
            
            worker.interrupted(Konstanty.ZASTAVIT);
            worker.cancel(true);
            
            File subor = new File(Konstanty.ULOZ_SUBOR);
            if (subor.exists()) {
                subor.delete();
            }
            
            File f = new File(Konstanty.FILE_TO_RECEIVED);
            if (f.exists()) {
                f.delete();
            }
            
            
    
        } catch (SocketException ex) {
            Logger.getLogger(Klient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Klient.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(dataSoket!=null){
                    dataSoket.close();
                }
                if(soket!=null){                    
                    soket.close();                    
                }
                
            } catch (IOException ex) {
                Logger.getLogger(Klient.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    
    }
    
    
}
