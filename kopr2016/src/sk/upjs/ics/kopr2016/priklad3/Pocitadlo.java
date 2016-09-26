package sk.upjs.ics.kopr2016.priklad3;

public class Pocitadlo {

	private int hodnota;
	
	public int getNext() {
		synchronized (this) {
			return ++hodnota;
		}
	}
}
