package sk.gursky.kopr2016.cviko02.zadanie;

import java.util.List;

public class Zivot implements Runnable {

	private List<Chlap> chlapi;
	
	public Zivot(List<Chlap> chlapi) {
		this.chlapi = chlapi;
	}

	@Override
	public void run() {
		for (Chlap chlap : chlapi) {
			chlap.setVek("star�");
			System.out.println("�ivot: bijem "+chlap.getId()+". chlapa");
			chlap.setKrasa("�kared�");
		}
	}

}
