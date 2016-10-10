package sk.upjs.ics.kopr2016.cviko03.priklad3;

public class Car implements Runnable {

	private String name;
	private TrafficTracker trafficTracker;
	private static final int[][] movements = new int[][] {{1,1},{1,-1},{-1,1},{-1,-1}};
	
	public Car(String name, TrafficTracker traficTracker) {
		this.name = name;
		this.trafficTracker = traficTracker;
	}
	
	public void run() {
		while(true) {
			if (trafficTracker!= null) {
				Point position = trafficTracker.getLocation(name);
				try {
					int movement = (int) (4*Math.random());
					int[] posArray = position.getPosition(); 
					int newx = posArray[0]+Car.movements[movement][0];
					int newy = posArray[1]+Car.movements[movement][1];
					trafficTracker.setLocation(name,newx,newy);
				} catch(Exception e) {
					System.err.println(name + ": Niekto mi zmazal auticko!");
					break;
				}
			}
		}
	}
}
