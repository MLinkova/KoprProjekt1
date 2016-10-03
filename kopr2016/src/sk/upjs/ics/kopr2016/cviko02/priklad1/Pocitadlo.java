package sk.upjs.ics.kopr2016.cviko02.priklad1;

import java.util.concurrent.atomic.AtomicInteger;

public class Pocitadlo {

	private AtomicInteger hodnota = new AtomicInteger();
	
	public int getNext() {
		return hodnota.incrementAndGet();
	}
}
