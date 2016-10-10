package sk.upjs.ics.kopr2016.cviko03.priklad3;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class TrafficTracker {

	private final ConcurrentMap<String, Point> locations;
	
	public TrafficTracker(Map<String, Point> locations) {
		this.locations = new ConcurrentHashMap<>(locations);
	}

	public Map<String, Point> getLocations() {
		return Collections.unmodifiableMap(locations);
	}

	public Point getLocation(String id) {
		return locations.get(id);
	}

	public  void setLocation(String id, int x, int y) {
		Point loc = locations.get(id);
		if (loc == null)
			throw new IllegalArgumentException("No such ID: " + id);
		loc.set(x,y);
	}
}
