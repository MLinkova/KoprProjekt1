package sk.gursky.kopr2016.cviko02.zadanie;

public class Chlap {

	private int id;
	private String vek;
	private String krasa;
	
	public Chlap(int id) {
		this.id = id;
		vek = "mladı";
		krasa = "peknı";
	}

	public int getId() {
		return id;
	}
	
	public String getVek() {
		return vek;
	}

	public void setVek(String vek) {
		this.vek = vek;
	}

	public String getKrasa() {
		return krasa;
	}

	public void setKrasa(String krasa) {
		this.krasa = krasa;
	}
	
	@Override
	public String toString() {
		return vek + " a " + krasa;
	}
}
