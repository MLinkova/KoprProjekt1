package sk.upjs.ics.projektkopr1.oprava;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    
    public static final int FILE_RECIEVE_PORT = 7777;
    public static final int FIRST_THREAD_SOKET_PORT=4001;
    public static int pocetTCPSpojeni=0 ;
    public final static String FILE_TO_SEND="C:\\Users\\Michaela\\Desktop\\KoprProjekt1.mp4";
    private static ServerSocket[] sokety = new ServerSocket[10];
    
 // Trieda reprezentujuca serverovu cast komunikacie
    
    public static void main(String[] args) {
     
        ServerSocket servsock = null;
        Socket sock = null;
       
        try {
            DatagramSocket soket = new DatagramSocket(Klient.FILE_SERVER_PORT);
            servsock = new ServerSocket(Klient.FILE_SERVER_PORT);
            
            // Od klienta dostanem spravu a na zaklade nej sa rozhodujem ci mam subor poslat od zaciatku
            // alebo ci ho mam doposielat pretoze bolo prerusenie
            File subor = new File(FILE_TO_SEND);
            while (true) {
                System.out.println("Waiting...");
                sock=servsock.accept();
                byte[] buffer = new byte[soket.getReceiveBufferSize()];
                DatagramPacket paket = new DatagramPacket(buffer, buffer.length);
                soket.receive(paket);
                
                byte[] data = paket.getData();
                String sprava = new String(data).trim();
                System.out.println("Prijimam spravu " + sprava);
                
                
                
                if(sprava.contains("ZNOVA")){
                    String [] rozdlenaSprava=sprava.split(" ");
                    pocetTCPSpojeni= Integer.parseInt(rozdlenaSprava[1]);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(baos);
                    
                    oos.writeLong(subor.length());
                    oos.flush();
                    byte[] dataToSend = baos.toByteArray();
                    DatagramPacket paketToSend = new DatagramPacket(dataToSend, dataToSend.length, paket.getAddress(),
                            paket.getPort());
                    System.out.println("Posielam dlzku " + subor.length());
                    soket.send(paketToSend);
                    sock.close();
                    for (int i = 0; i < pocetTCPSpojeni; i++) {
                   sokety[i] = new ServerSocket(FIRST_THREAD_SOKET_PORT + i);
                 }
                    
                    RandomAccessFile raf = new RandomAccessFile(subor, "r");
                    File myFile = new File(FILE_TO_SEND);
                    
                    int dlzkaSuboru=(int) subor.length() ;
                    int dlzkaChunks = (int) (subor.length() / pocetTCPSpojeni);
                    
                    Socket[] soketyKlientov = new Socket[pocetTCPSpojeni];
                    for (int i = 0; i < soketyKlientov.length; i++) {
                        soketyKlientov[i] = sokety[i].accept();
                    }
                    
                    for (int i = 0; i < pocetTCPSpojeni; i++) {
                        int zapisanaDlzka=Integer.parseInt(rozdlenaSprava[i+2]);
                        raf.seek(i * dlzkaChunks+zapisanaDlzka);
                        byte[] bytearray;
                        if (i == pocetTCPSpojeni - 1) {
                            int zvysok = dlzkaSuboru - (dlzkaChunks * pocetTCPSpojeni);
                            bytearray = new byte[dlzkaChunks + zvysok-zapisanaDlzka];
                            raf.read(bytearray);
                        } else {
                            bytearray = new byte[dlzkaChunks-zapisanaDlzka];
                            raf.read(bytearray);
                        }
              
                        PosliSubor posli = new PosliSubor(soketyKlientov[i],bytearray,i);
                        Thread thread = new Thread(posli);
                        thread.start();
                    }
                    
                        
                    } 

                if (sprava.contains("POSLI")) {
                    
                    pocetTCPSpojeni= Integer.parseInt(sprava.split(" ")[1]);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(baos);
                    
                    oos.writeLong(subor.length());
                    oos.flush();
                    byte[] dataToSend = baos.toByteArray();
                    DatagramPacket paketToSend = new DatagramPacket(dataToSend, dataToSend.length, paket.getAddress(),
                            paket.getPort());
                    System.out.println("Posielam dlzku " + subor.length());
                    soket.send(paketToSend);
                    sock.close();
                    
                    for (int i = 0; i < pocetTCPSpojeni; i++) {
                  sokety[i] = new ServerSocket(FIRST_THREAD_SOKET_PORT + i);
                 }
                    
                    
                    RandomAccessFile raf = new RandomAccessFile(subor, "r");
                    File myFile = new File(FILE_TO_SEND);
                    
                    int dlzkaSuboru=(int) subor.length() ;
                    int dlzkaChunks = (int) (subor.length() / pocetTCPSpojeni);
                    
                     Socket[] soketyKlientov = new Socket[pocetTCPSpojeni];
                    for (int i = 0; i < soketyKlientov.length; i++) {
                        soketyKlientov[i] = sokety[i].accept();
                    }
                    
                    for (int i = 0; i < pocetTCPSpojeni; i++) {
                        raf.seek(i * dlzkaChunks);
                        byte[] bytearray;
                        if (i == pocetTCPSpojeni - 1) {
                            int zvysok = dlzkaSuboru - (dlzkaChunks * pocetTCPSpojeni);
                            bytearray = new byte[dlzkaChunks + zvysok];
                            raf.read(bytearray);
                        } else {
                            bytearray = new byte[dlzkaChunks];
                            raf.read(bytearray);
                        }
                        
                        PosliSubor posli = new PosliSubor(soketyKlientov[i],bytearray,i);
                        Thread thread = new Thread(posli);
                        thread.start();
                    }
                    
                        
                    
                    }
                
            }
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } 
            
        }
    }
    

