package sk.upjs.ics.kopr2016.cviko01.priklad2;

public class VypisovanieCisiel implements Runnable {

	@Override
	public void run() {
		String menoVlakna = Thread.currentThread().getName();
		for(int i = 1; i <= 10; i++) {
			System.out.println(menoVlakna+": " + i + ". krat");
		}
	}

}
