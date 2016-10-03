package sk.upjs.ics.kopr2016.cviko01.priklad1;

public class Spustac {

	public static void main(String[] args) {
		VypisovanieCisiel uloha = new VypisovanieCisiel();
		Thread vlakno = new Thread(uloha);
		vlakno.start();
		for(int i = 1; i <= 10; i++) {
			System.out.println("hlavny program: " + i + ". krat");
		}
	}
}
