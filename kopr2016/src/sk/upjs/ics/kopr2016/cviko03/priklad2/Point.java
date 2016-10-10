package sk.upjs.ics.kopr2016.cviko03.priklad2;

public class Point {

	private final int x;
	private final int y;
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Point(Point ip) {
		this.x = ip.getX();
		this.y = ip.getY();
	}
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
}
