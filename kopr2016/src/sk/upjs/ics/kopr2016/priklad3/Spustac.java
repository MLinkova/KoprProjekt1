package sk.upjs.ics.kopr2016.priklad3;

public class Spustac {

	public static void main(String[] args) {
		Pocitadlo pocitadlo = new Pocitadlo();
		VypisovanieCisiel uloha = new VypisovanieCisiel(pocitadlo);
		Thread vlakno1 = new Thread(uloha);
		Thread vlakno2 = new Thread(uloha);
		vlakno1.setName("Jozo");
		vlakno2.setName("Fero");
		vlakno1.start();
		vlakno2.start();
	}
}
