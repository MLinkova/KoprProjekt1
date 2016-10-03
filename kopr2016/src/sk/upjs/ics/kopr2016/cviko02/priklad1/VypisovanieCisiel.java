package sk.upjs.ics.kopr2016.cviko02.priklad1;

public class VypisovanieCisiel implements Runnable {

	private Pocitadlo pocitadlo; 
	
	public VypisovanieCisiel(Pocitadlo pocitadlo) {
		this.pocitadlo = pocitadlo;
	}
	
	@Override
	public void run() {
		String menoVlakna = Thread.currentThread().getName();
		for(int i = 1; i <= 100; i++) {
			System.out.println(menoVlakna + ": " 
						+ pocitadlo.getNext() + ". krat");
		}
	}
}
