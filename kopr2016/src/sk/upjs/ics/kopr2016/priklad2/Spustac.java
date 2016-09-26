package sk.upjs.ics.kopr2016.priklad2;

public class Spustac {

	public static void main(String[] args) {
		VypisovanieCisiel uloha = new VypisovanieCisiel();
		Thread vlakno1 = new Thread(uloha);
		Thread vlakno2 = new Thread(uloha);
		vlakno1.setName("Jozo");
		vlakno2.setName("Fero");
		vlakno1.start();
		vlakno2.start();
	}
}
