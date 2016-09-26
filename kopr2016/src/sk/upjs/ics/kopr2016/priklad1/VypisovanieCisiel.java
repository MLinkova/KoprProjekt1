package sk.upjs.ics.kopr2016.priklad1;

public class VypisovanieCisiel implements Runnable {

	@Override
	public void run() {
		for(int i = 1; i <= 10; i++) {
			System.out.println("vlakno: " + i + ". krat");
		}
	}

}
