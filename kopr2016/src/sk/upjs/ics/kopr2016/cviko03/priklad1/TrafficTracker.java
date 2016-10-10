package sk.upjs.ics.kopr2016.cviko03.priklad1;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class TrafficTracker {

	private final Map<String, Point> locations;
	
	public TrafficTracker(Map<String, Point> locations) {
		this.locations = copyMap(locations);
	}

	public Map<String, Point> getLocations() {
		return copyMap(locations);
	}

	public synchronized Point getLocation(String id) {
		Point loc = locations.get(id);
		return loc == null ? null : new Point(loc);
	}

	public synchronized void setLocation(String id, int x, int y) {
		Point loc = (Point) locations.get(id);
		if (loc == null)
			throw new IllegalArgumentException("No such ID: " + id);
		loc.setX(x);
		loc.setY(y);
	}
	
	private synchronized Map<String, Point> copyMap(Map<String, Point> oldMap) {
		Map<String, Point> newMap = new HashMap<>();
		for (Entry<String, Point> entry : oldMap.entrySet()) {
			newMap.put(entry.getKey(), new Point(entry.getValue()));
		}
		return newMap;
	}
}
