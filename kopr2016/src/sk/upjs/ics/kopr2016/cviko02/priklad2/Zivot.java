package sk.upjs.ics.kopr2016.cviko02.priklad2;

import java.util.List;

public class Zivot implements Runnable {

	private final List<Chlap> chlapi;
	
	public Zivot(List<Chlap> chlapi) {
		this.chlapi = chlapi;
	}

	@Override
	public void run() {
		for (Chlap chlap : chlapi) {
			synchronized(chlap) {
				chlap.setVek("star�");
				System.out.println("�ivot: bijem "+chlap.getId()+". chlapa");
				chlap.setKrasa("�kared�");
			}
		}
	}

}
