package sk.upjs.ics.kopr2016.cviko02.priklad2a;

public class Chlap {

	private final int id;
	private final String vek;
	private final String krasa;
	
	private Chlap(int id, String vek, String krasa) {
		this.id = id;
		this.vek = vek;
		this.krasa = krasa;
	}
	
	public static Chlap getMladyChlap(int id) {
		return new Chlap(id, "mlad�", "pekn�");
	}

	public static Chlap getStaryChlap(int id) {
		return new Chlap(id, "star�", "�kared�");
	}
	
	public int getId() {
		return id;
	}
	
	public String getVek() {
		return vek;
	}

	public String getKrasa() {
		return krasa;
	}

	@Override
	public String toString() {
		return vek + " a " + krasa;
	}


}
