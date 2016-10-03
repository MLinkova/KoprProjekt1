package sk.upjs.ics.kopr2016.cviko02.priklad2;

import java.util.List;

public class Zena implements Runnable {

	private List<Chlap> chlapi;
	
	public Zena(List<Chlap> chlapi) {
		this.chlapi = chlapi;
	}

	@Override
	public void run() {
		for (Chlap chlap: chlapi) {
			synchronized(chlap) {
				System.out.println("Žena: hodnotím "+chlap.getId()+". chlapa: " + chlap.toString());
			}
		}
	}

}
