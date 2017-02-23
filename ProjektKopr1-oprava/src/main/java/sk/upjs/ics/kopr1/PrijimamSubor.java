package sk.upjs.ics.kopr1;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.util.Arrays;
import java.util.Set;


public class PrijimamSubor implements Runnable {

    private int port;
    private RandomAccessFile raf;

    private Set<Long> precitaneOffsety;
    private int pocetTCPSpojeni;
    private Socket socket;
    private boolean bezi;
    private int dlzkaCasti;

    public PrijimamSubor(int port, RandomAccessFile raf, Set<Long> readedOffSets, int pocetTCP,int dlzkaCasti) {
        this.port = port;
        this.raf = raf;
        this.precitaneOffsety = readedOffSets;
        this.pocetTCPSpojeni=pocetTCP;
        this.dlzkaCasti=dlzkaCasti;
    }

    public void terminate() {
        bezi = false;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(Konstanty.SERVER_NAME, port);
            DataInputStream dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            byte[] bytes = new byte[dlzkaCasti];
            final int len = 1024;
            int off = 0;
            int readBytes = 0;
            bezi = true;
            while (bezi) {
                
                long offset = dis.readLong();
                int lenght = dis.readInt();

                if (offset == -1 || lenght == -1) {
         
                    break;
                }
                if (lenght != bytes.length) {
                    bytes = new byte[lenght];
                } /*else {
                    Arrays.fill(bytes, (byte) 0);
                }*/
                readBytes = 0;
                off = 0;
                while (off < lenght) {
                    if (off + len > lenght) {
                        readBytes = dis.read(bytes, off,
                                lenght - off);
                    } else {
                        readBytes = dis.read(bytes, off, len);
                    }
                    off += readBytes;
                }
                synchronized (raf) {
                    if (!bezi) {
                        break;
                    }
                    raf.seek(offset);
                    raf.write(bytes);
                    precitaneOffsety.add(offset);
                    SwingWorker.uzSkopirovane.incrementAndGet();
                  
                }
            }
            bytes = null;

        } catch (IOException e) {
            e.printStackTrace();
            try {
                socket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

}
