package sk.upjs.ics.projektkopr1.oprava;

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
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

public class PosliSubor implements Runnable {

	private byte[] data;
	private int poradie;
	private Socket soketKlienta;

	public PosliSubor(Socket soketKlienta, byte[] pole, int poradie) {
		this.data = pole;
		this.poradie = poradie;
		this.soketKlienta = soketKlienta;
	}
// trieda ma na starosti posielanie dat klientovi
	@Override
	public void run() {

		try {
			OutputStream os = soketKlienta.getOutputStream();

			try {
				os.write(data, 0, data.length);
				os.flush();
				soketKlienta.close();

			} catch (SocketException e) {
				soketKlienta.close();
			}	
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
