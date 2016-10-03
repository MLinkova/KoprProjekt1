package sk.upjs.ics.kopr2016.cviko02.priklad2a;

import java.util.List;

public class Zivot implements Runnable {

	private final List<Chlap> chlapi;
	
	public Zivot(List<Chlap> chlapi) {
		this.chlapi = chlapi;
	}

	@Override
	public void run() {
		for (int i = 0; i < chlapi.size(); i++) {
			Chlap staryChlap = Chlap.getStaryChlap(chlapi.get(i).getId());
			System.out.println("Život: bijem "+chlapi.get(i).getId()
					+". chlapa");
			chlapi.set(i, staryChlap);
		}
	}

}
