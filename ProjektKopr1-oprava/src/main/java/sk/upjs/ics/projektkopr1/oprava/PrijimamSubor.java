package sk.upjs.ics.projektkopr1.oprava;

import static sk.upjs.ics.projektkopr1.oprava.Klient.uzSkopirovane;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;


public class PrijimamSubor implements Runnable {
    
    private int poradie;
    private int vlakna;
    private byte[] data;
    private Klient klient;
    private RandomAccessFile raf;
    private int zaciatok;
    private boolean interrupted = false;
    private int zapisane = 0;
    
    public PrijimamSubor(int offset, RandomAccessFile raf, Klient k,
            byte[] data, int vlakna, int poradie) {
        this.poradie = poradie;
        this.vlakna = vlakna;
        this.data = data;
        this.klient = k;
        this.zaciatok = offset;
        this.raf = raf;
    }
    
    public int getZapisane() {
        return zapisane;
    }
    
    public void setZapisane(int zapisane) {
        this.zapisane = zapisane;
    }
    
    public boolean isInterrupted() {
        return interrupted;
    }
    
    public void setInterrupted(boolean interrupted) {
        this.interrupted = interrupted;
    }
   /// trieda na zapis suboru, tu by som potrebovala vyriesit ako lepsie  
    //zaistiti prerusenie a nasledne poklacovanie kopirovania suboru 
    @Override
    public void run() {
        
        int bytesRead;
        int dlzka = 0;
        int cast = 1024;
        
        
        try {
            Socket prijimatel = new Socket(Klient.SERVER_NAME, Server.FIRST_THREAD_SOKET_PORT + poradie);
            InputStream is = prijimatel.getInputStream();
            dlzka = 0;
            
            while ((dlzka < data.length)) {
                if (interrupted != true) {
                    if (dlzka + cast > data.length) {
                        bytesRead = is.read(data, dlzka,
                                data.length - dlzka);
                    } else {
                        bytesRead = is.read(data, dlzka, cast);
                    }
                    dlzka += bytesRead;
                }  else {
                    
                    synchronized (raf) {
                        raf.seek(zaciatok);
                        raf.write(data);
                        
                    }
                    zapisane = dlzka;
                    setZapisane(zapisane);
                    prijimatel.close();
                    dlzka = data.length;
                    
                }
            }
            
            if (interrupted != true) {
                synchronized (raf) {
                    raf.seek(zaciatok);
                    raf.write(data);
                    klient.setPrijate(klient.getPrijate() + 1);
                    setZapisane(data.length);
                    uzSkopirovane=uzSkopirovane+data.length;
                }
                
                data = null;
                prijimatel.close();
            }
            
            data = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
