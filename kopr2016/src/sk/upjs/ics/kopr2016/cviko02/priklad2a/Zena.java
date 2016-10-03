package sk.upjs.ics.kopr2016.cviko02.priklad2a;

import java.util.List;

public class Zena implements Runnable {

	private final List<Chlap> chlapi;
	
	public Zena(List<Chlap> chlapi) {
		this.chlapi = chlapi;
	}

	@Override
	public void run() {
		for (Chlap chlap: chlapi) {
			System.out.println("Žena: hodnotím "+chlap.getId()+". chlapa: " + chlap.toString());
		}
	}

}
