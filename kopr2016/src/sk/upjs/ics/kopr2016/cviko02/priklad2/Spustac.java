package sk.upjs.ics.kopr2016.cviko02.priklad2;

import java.util.ArrayList;
import java.util.List;

public class Spustac {

	public static void main(String[] args) {
		List<Chlap> chlapi = new ArrayList<Chlap>();
		for (int i = 1; i <= 10; i++) {
			chlapi.add(new Chlap(i));
		}
			
		Zena zena = new Zena(chlapi);
		Zivot zivot = new Zivot(chlapi);
		
		Thread vlakno1 = new Thread(zena);
		Thread vlakno2 = new Thread(zivot);

		vlakno2.start();
		vlakno1.start();
	}

}
