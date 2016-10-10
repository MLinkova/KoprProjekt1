package sk.upjs.ics.kopr2016.cviko03.priklad3;

public class Point {

	private int x;
	private int y;
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public synchronized int[] getPosition() {
		return new int[]{x,y};
	}

	public synchronized void set(int x, int y) {
		this.x = x;
		this.y = y;
	}
}
