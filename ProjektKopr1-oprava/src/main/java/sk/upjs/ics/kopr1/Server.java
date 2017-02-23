
package sk.upjs.ics.kopr1;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import sk.upjs.ics.projektkopr1.oprava.Klient;

/**
 *
 * @author Michaela
 */
public class Server {
    
    private static Socket socket;
    private static List<ServerSocket> sokety;
    private static PosliSubor[] posliSubor;
    private static Thread[] vlakna;
    
    private static int pocetTCPSpojeni;
    
    public static void main(String[] args){
        ServerSocket serverSoket= null;
        sokety= new ArrayList<>();
        try {
            DatagramSocket soket = new DatagramSocket(Klient.FILE_SERVER_PORT);
            serverSoket= new ServerSocket(Konstanty.SERVER_PORT);
            
            while(true){
                socket=serverSoket.accept();
                byte[] buffer = new byte[soket.getReceiveBufferSize()];
                DatagramPacket paket = new DatagramPacket(buffer, buffer.length);
                soket.receive(paket);
                
                byte[] data = paket.getData();
                String sprava = new String(data).trim();
                
                String[] rozdelenaSprava=sprava.split(" ");
                File subor= new File(Konstanty.FILE_TO_SEND);
                
                if(Konstanty.POSLI.equals(rozdelenaSprava[0].trim())){
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(baos);
                    
                    oos.writeLong(subor.length());
                    oos.flush();
                    byte[] dataToSend = baos.toByteArray();
                    DatagramPacket paketToSend = new DatagramPacket(dataToSend, dataToSend.length, paket.getAddress(),
                            paket.getPort());
                    soket.send(paketToSend);
                    
                    poslat(rozdelenaSprava);
                }
                if(Konstanty.POSLI_ZNOVA.equals(rozdelenaSprava[0].trim())){
                    
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(baos);
                    oos.writeLong(subor.length());
                    oos.flush();
                    byte[] dataToSend = baos.toByteArray();
                    DatagramPacket paketToSend = new DatagramPacket(dataToSend, dataToSend.length, paket.getAddress(),
                            paket.getPort());
                    soket.send(paketToSend);
                    
                    pokracovat(rozdelenaSprava);
                }
                if(Konstanty.PRERUS.equals(rozdelenaSprava[0].trim())){
                    zrusit(rozdelenaSprava);
                }
                if(Konstanty.ZRUS.equals(rozdelenaSprava[0].trim())){
                    zrusit(rozdelenaSprava);
                }
                
                
            }
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            ukoncit();
        }
    }
    
    private static void poslat(String[] rozdelenaSprava) {
        try {
            pocetTCPSpojeni=Integer.parseInt(rozdelenaSprava[1]);
            posliSubor=new PosliSubor[pocetTCPSpojeni];
            vlakna=new Thread[pocetTCPSpojeni];
            File subor= new File(Konstanty.FILE_TO_SEND);
            long velkostSuboru= subor.length();
            int dlzkaJednejCasti= (int) (velkostSuboru/ pocetTCPSpojeni);
            int zvysok= (int) (velkostSuboru-(dlzkaJednejCasti*pocetTCPSpojeni));
            
            for (int i = 0; i < pocetTCPSpojeni; i++) {
                ServerSocket serv= new ServerSocket(Konstanty.PRVY_POSLI_POST+i);
                sokety.add(serv);
            }
            
            BlockingQueue<List<Integer>> offsetDlzka = new LinkedBlockingQueue<>();
            for (int i = 0; i < pocetTCPSpojeni; i++) {
                List<Integer> list= new ArrayList<>();
                if(i==pocetTCPSpojeni-1){
                    list.add(i*dlzkaJednejCasti);
                    list.add(dlzkaJednejCasti+zvysok);
                }else{
                    list.add(i*dlzkaJednejCasti);
                    list.add(dlzkaJednejCasti+zvysok);
                }
                offsetDlzka.offer(list);
            }
            
            for (int i = 0; i < pocetTCPSpojeni; i++) {
                List<Integer> list= new ArrayList<>();
                list.add(-1);
                list.add(-1);
                offsetDlzka.add(list);
                
                
            }
            RandomAccessFile raf = new RandomAccessFile(subor, "r");
            for (int i = 0; i < pocetTCPSpojeni; i++) {
                PosliSubor posli= new PosliSubor(sokety.get(i), raf, offsetDlzka,dlzkaJednejCasti,zvysok);
                 Thread thread= new Thread(posli);
                 thread.start();
                 posliSubor[i]=posli;
                 vlakna[i]=thread;
            }
        } catch (IOException ex) {
            ukoncit();
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
    private static void pokracovat(String[] rozdelenaSprava) {
        try {
            pocetTCPSpojeni=Integer.parseInt(rozdelenaSprava[1]);
            posliSubor=new PosliSubor[pocetTCPSpojeni];
            vlakna=new Thread[pocetTCPSpojeni];
            File subor= new File(Konstanty.FILE_TO_SEND);
            long velkostSuboru= subor.length();
            int dlzkaJednejCasti= (int) (velkostSuboru/ pocetTCPSpojeni);
            int zvysok= (int) (velkostSuboru-(dlzkaJednejCasti*pocetTCPSpojeni));
            
            if(sokety.size()<pocetTCPSpojeni)
            for (int i = sokety.size(); i < pocetTCPSpojeni; i++) {
                ServerSocket serv= new ServerSocket(Konstanty.PRVY_POSLI_POST+i);
                sokety.add(serv);
            }
            
            Set<Long> readedOffsets = new HashSet<>();
            for (int i = 2; i < rozdelenaSprava.length; i++) {
                long offset=Long.parseLong(rozdelenaSprava[i]);
                readedOffsets.add(offset);
            }
            
            BlockingQueue<List<Integer>> offsetDlzka = new LinkedBlockingQueue<>();
       
             
            for (int i = 0; i < pocetTCPSpojeni; i++) {
                long off=i*dlzkaJednejCasti;
                if(!readedOffsets.contains(off)){
                    List<Integer> list= new ArrayList<>();
                    if(i==pocetTCPSpojeni-1){
                        list.add((int) off);
                        list.add(dlzkaJednejCasti+zvysok);
                    }else{
                        list.add((int) off);
                        list.add(dlzkaJednejCasti+zvysok);
                    }
                    offsetDlzka.offer(list);
                }
            }
            
            for (int i = 0; i < pocetTCPSpojeni; i++) {
                List<Integer> list= new ArrayList<>();
                list.add(-1);
                list.add(-1);
                offsetDlzka.add(list);
                
                
            }
            RandomAccessFile raf = new RandomAccessFile(subor, "r");
            for (int i = 0; i < pocetTCPSpojeni; i++) {
                PosliSubor posli= new PosliSubor(sokety.get(i), raf, offsetDlzka,dlzkaJednejCasti,zvysok);
                 Thread thread= new Thread(posli);
                 thread.start();
                 posliSubor[i]=posli;
                 vlakna[i]=thread;
            }
        } catch (IOException ex) {
            ukoncit();
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }

    
    private static void zrusit(String[] rozdelenaSprava) {
        if(posliSubor!=null){
            for (int i = 0; i < posliSubor.length; i++) {
                posliSubor[i].setInterrupted(true);
            }
        }
        if(vlakna!=null){
            for (int i = 0; i < vlakna.length; i++) {
                vlakna[i].interrupt();
            }
        }
    }
    
    private static void ukoncit(){
        try {
            if(socket!=null&& !socket.isClosed()){    
                socket.close();               
            }
            if(sokety!=null){
                for (int i = 0; i < sokety.size(); i++) {
                    if(sokety.get(i)!=null)
                        sokety.get(i).close();
                    
                }
            }
            if(posliSubor!=null){
                for (int i = 0; i < posliSubor.length; i++) {
                    if(posliSubor[i]!=null)
                    posliSubor[i].setInterrupted(true);
                }
            }
            if(vlakna!=null){
                for (int i = 0; i < vlakna.length; i++) {
                    if(vlakna[i]!=null)
                    vlakna[i].interrupt();
                }
            }
            
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
