
package sk.upjs.ics.projektkopr1.oprava;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;


public class Klient {
    public static final String SERVER_NAME = "localhost";
    public static final int FILE_SERVER_PORT = 5555;
    public final static String
            FILE_TO_RECEIVED = "C:\\Users\\Michaela\\Desktop\\KoprProjekt1-downloaded.mp4";
    private  int pocetTCPspojeni;
    public static int uzSkopirovane;
    static int DlzkaSuboru;
    private RandomAccessFile raf;
    private boolean prebiehaKopirovanie = false;
    private volatile int prijate = 0;
    private PrijimamSubor[] vlakna;
    private Socket sock ;

    //trieda reprazentucuja klientsku cast
    public Klient(int pocetTCP){
        pocetTCPspojeni=pocetTCP;
    }

     public Klient() {
      }
    
    
    
     public int getPrijate() {
        return prijate;
    }
    
    public void setPrijate(int prijate) {
        this.prijate = prijate;
    }

    
    public boolean isPrebiehaKopirovanie() {
        return prebiehaKopirovanie;
    }
    
    public void setPrebiehaKopirovanie(boolean prebiehaKopirovanie) {
        this.prebiehaKopirovanie = prebiehaKopirovanie;
    }
    
 
    
    public  void spusti(){
        //metoda sa vola ak chcem poslat subor od zaciatku
        int bytesRead;
        int current = 0;
        setPrebiehaKopirovanie(true);
        DatagramSocket soket= null;
        try {
            // na zaciatok poslem spravu serveru ze chcem subor a informaciu o pocte TCP spojeni, 
            //server mi posle dlzku suboru
            sock = new Socket(SERVER_NAME, FILE_SERVER_PORT);
            soket = new DatagramSocket();
            String sprava = "POSLI "+pocetTCPspojeni;
            DatagramPacket paket = new DatagramPacket(sprava.getBytes(), sprava.getBytes().length,
                    InetAddress.getByName(SERVER_NAME), FILE_SERVER_PORT);
            soket.send(paket);
            System.out.println("Posielam");
            
            byte[] buffer = new byte[soket.getReceiveBufferSize()];
            DatagramPacket prijatyPaket = new DatagramPacket(buffer, buffer.length);
            soket.receive(prijatyPaket);
            ByteArrayInputStream bais = new ByteArrayInputStream(prijatyPaket.getData());
            ObjectInputStream ois = new ObjectInputStream(bais);
            long dlzkaVybranehoSuboru = ois.readLong();
            
            sock.close();
             
            System.out.println("Prijimam subor");
            File f = new File(FILE_TO_RECEIVED);
            raf = new RandomAccessFile(f, "rw");
            raf.setLength(dlzkaVybranehoSuboru);
            
            DlzkaSuboru = (int) dlzkaVybranehoSuboru;
            setPrijate(0);
            
          // rozdelim si dlzku na casti a kazdemu vlaknu dam precitat a zapisat jednu cast
            int dlzkaChunks = (int) (DlzkaSuboru / pocetTCPspojeni);
            System.gc();
            byte[] bytearrayII = new byte[dlzkaChunks];
            int zvysok = DlzkaSuboru
                    - (dlzkaChunks * pocetTCPspojeni);
            byte[] bytearray = new byte[dlzkaChunks
                    + zvysok];
           vlakna=new PrijimamSubor[pocetTCPspojeni];
            for (int i = 0; i < pocetTCPspojeni; i++) {
                if (i + 1 == pocetTCPspojeni) {
                  PrijimamSubor prijimam=new PrijimamSubor(dlzkaChunks*i, raf,this,  bytearray, pocetTCPspojeni, i);
                    Thread t = new Thread(prijimam);
                    t.start();
                    vlakna[i]=prijimam;
               
                } else {
                    PrijimamSubor prijimam=new PrijimamSubor(dlzkaChunks*i, raf, this, bytearrayII, pocetTCPspojeni, i);
                    Thread t = new Thread(prijimam);
                    t.start();
                    vlakna[i]=prijimam;

                }
              
            }
           
           
            
        }
        catch (IOException ex) {
            Logger.getLogger(Klient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
    public  void zrusit() {
        // v tejto metode potrebujem zrusit a zmazat subor co sa atial nacital
        try {
            if (isPrebiehaKopirovanie() == true) {
                for (int i = 0; i < vlakna.length; i++) {
                    if(vlakna[i]!=null)
                    vlakna[i].setInterrupted(true);
                    
                    
                }
            } 
            if(raf!=null)
            raf.close();
            if(sock!= null)
            sock.close();
            
            File file=new File(FILE_TO_RECEIVED);
            file.delete();
            System.out.println("Kopirovanie zrusene");
        } catch (IOException ex) {
            Logger.getLogger(Klient.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

   public void prerus() {
       //  v metode kazdemu vlaknu poviem ze ide o zastavenie a potom doposial zapisane
       //casti si zapisem do suboru
        if (isPrebiehaKopirovanie() == true) {
            for (int i = 0; i < vlakna.length; i++) {
                    vlakna[i].setInterrupted(true);
                
                
            }
            
            PrintWriter pw = null;
            try {
                pw = new PrintWriter("UlozKopirovanie.txt");
                pw.println(pocetTCPspojeni);
                for (int i = 0; i < vlakna.length; i++) {
                    pw.println(vlakna[i].getZapisane());
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (pw != null) {
                    pw.close();
                }
            }
            System.out.println("kopirovanie prerusene");
            setPrebiehaKopirovanie(false);
        }
    }
    
    


   public void pokracuj() {
       // metoda podobna ako metoda spusti avsak vlaknam uz posielam len tie casti suboru, 
       //ktore zatial nie su zapisane
       setPrebiehaKopirovanie(true);
       DatagramSocket soket= null;
       try {
           sock = new Socket(SERVER_NAME, FILE_SERVER_PORT);
           soket = new DatagramSocket();
           String sprava = "ZNOVA ";
           File f = new File("UlozKopirovanie.txt");
           Scanner sc = new Scanner(f);
           pocetTCPspojeni=  Integer.parseInt(sc.nextLine());
           sprava=sprava+pocetTCPspojeni+" ";
           int[] zapisaneCasti = new int[pocetTCPspojeni];
           for (int i = 0; i < pocetTCPspojeni; i++) {
               int bytes = Integer.parseInt(sc.nextLine());
               zapisaneCasti[i] = bytes;
               sprava=sprava + bytes +" ";
           }
           sc.close();
           DatagramPacket paket = new DatagramPacket(sprava.getBytes(), sprava.getBytes().length,
                   InetAddress.getByName(SERVER_NAME), FILE_SERVER_PORT);
           soket.send(paket);
           System.out.println("Posielam "+ sprava);
           sock.close();
           byte[] buffer = new byte[soket.getReceiveBufferSize()];
           DatagramPacket prijatyPaket = new DatagramPacket(buffer, buffer.length);
           soket.receive(prijatyPaket);
           ByteArrayInputStream bais = new ByteArrayInputStream(prijatyPaket.getData());
           ObjectInputStream ois = new ObjectInputStream(bais);
           long dlzkaVybranehoSuboru = ois.readLong();
           
           File subor = new File(FILE_TO_RECEIVED);
           raf = new RandomAccessFile(subor, "rw");
           raf.setLength(dlzkaVybranehoSuboru);
           
           DlzkaSuboru = (int) dlzkaVybranehoSuboru;
           setPrijate(0);
           int dlzkaChunks = (int) (DlzkaSuboru / pocetTCPspojeni);
           int zvysok = DlzkaSuboru
                   - (dlzkaChunks * pocetTCPspojeni);
           
           vlakna = new PrijimamSubor[pocetTCPspojeni];
           for (int i = 0; i < pocetTCPspojeni; i++) {
               if (i + 1 == pocetTCPspojeni) {
                   PrijimamSubor prijimam=new PrijimamSubor((dlzkaChunks*i)+zapisaneCasti[i], raf,this,  new byte[dlzkaChunks
                           + zvysok
                           - zapisaneCasti[i]], pocetTCPspojeni, 10+i);
                   
                   Thread t = new Thread(prijimam);
                   t.start();
                   vlakna[i] = prijimam;
               } else {
                   PrijimamSubor prijimam=new PrijimamSubor((dlzkaChunks*i)+zapisaneCasti[i], raf, this, new byte[dlzkaChunks
                           - zapisaneCasti[i]], pocetTCPspojeni,10+i);
                   
                   Thread t = new Thread(prijimam);
                   t.start();
                   vlakna[i] = prijimam;
                   
               }
               
           }
       } catch (IOException ex) {
           Logger.getLogger(Klient.class.getName()).log(Level.SEVERE, null, ex);
       }
   }

    void informAboutFinishedCopy() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
   

}


