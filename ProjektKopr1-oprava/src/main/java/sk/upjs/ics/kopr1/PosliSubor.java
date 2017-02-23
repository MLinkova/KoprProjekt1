package sk.upjs.ics.kopr1;

import sk.upjs.ics.projektkopr1.oprava.*;
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
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;

public class PosliSubor implements Runnable {

    private RandomAccessFile raf;
    private BlockingQueue<List<Integer>> offsets;
    private ServerSocket serverSocket;
    private boolean interrupted;
    private int dlzkaJednejCasti;
    private int zvysok;

    public boolean isInterrupted() {
        return interrupted;
    }

    public void setInterrupted(boolean interrupted) {
        this.interrupted = interrupted;
    }

	public PosliSubor(ServerSocket soketKlienta, RandomAccessFile raf, BlockingQueue<List<Integer>> offsets,int dlzka,int zvysok) {
		this.offsets = offsets;
		this.raf = raf;
		this.serverSocket = soketKlienta;
                this.zvysok=zvysok;
                this.dlzkaJednejCasti=dlzka;
	}
// trieda ma na starosti posielanie dat klientovi
	@Override
	public void run() {
            
            try {
                Socket socket = serverSocket.accept();
                DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                byte[] data = new byte[dlzkaJednejCasti];
                try {
                    List<Integer> offsetDlzka= offsets.take();
                    interrupted=false;
                    while (offsetDlzka != null && !interrupted) {
                  
                        long offset = offsetDlzka.get(0);
                        int lenght = offsetDlzka.get(1);
                        if (offset == -1 && lenght == -1) {
                            dos.writeLong(-1);
                            dos.writeInt(-1);
                            dos.flush();
                            break;
                        } else {
                            synchronized (raf) {
                                if (lenght != data.length) {
                                    data = new byte[lenght];
                                }/* else {
                                    Arrays.fill(bytes, (byte) 0);
                                }*/
                                raf.seek(offset);
                                raf.read(data, 0, lenght);
                                
                                dos.writeLong(offset);
                                dos.writeInt(lenght);
                                dos.write(data);
                                dos.flush();
                            }
                        }
                        offsetDlzka = offsets.take();
                    }
                    data = null;
                    if (!socket.isClosed()) {
                        socket.close();
                    }
                    
                } catch (SocketException e) {
                    if (!socket.isClosed()) {
                        socket.close();
                    }
                } catch (InterruptedException e) {
                    if (!socket.isClosed()) {
                        socket.close();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

	}
}
