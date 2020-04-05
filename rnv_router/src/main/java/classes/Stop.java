package classes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Point;


public class Stop {
	// Attributes
	/*
	 * It's a design decision to implement this with the Point
	 * as private class attribute instead of extending Point.
	 * This avoids potential trouble that may come from extending a Point.
	 * But most importantly, this way we can have several forms of Stops,
	 * without running into the problem that a class can extend only one
	 * other class. This way we can have Stops that use Point, MultiPoints
	 * or other structures for location without having to define new classes
	 * for them.
	 * Additionally, this helps to allow user access via methods only.
	 */
	private Point location;
	/* Most of these attributes will not be changed once the object
	 * has been initiated. Thus most attributes are defined as final.
	 * All attributes are private to allow user access via methods only.
	 */
	// unique ID for the Stop
	private final int stopID;
	private final String name;
	private int[] routes;
	
	// Constructors
	// "basic" Stop
	public Stop(Point location, 
			int stopID, 
			String name) {
		this.location = location;
		this.stopID = stopID;
		this.name = name;
	}
	
	// "complete" Stop including routes
	public Stop(Point location, 
			int stopID, 
			String name, 
			int[] routes) {
		this.location = location;
		this.stopID = stopID;
		this.name = name;
		this.routes = routes;
	}

	
	// Methods
	/* Some methods come with the Point, eg. getCoordinates(), 
	 * which returns an array containing the values of all the
	 * vertices for this geometry.
	 * getX and getY are also inherited.
	 */
	
	public int numOfRoutes() {
		return this.routes.length;
	}
	
	
	// toString
	@Override
	public String toString() {
		return "Stop " + name + " with StopID "+stopID+" at coordinates " + getX() + ", " + getY() ;
	}
	
	
	// Getters and Setters
	public int getStopID() {
		return stopID;
	}

	public String getName() {
		return name;
	}
	
	public int[] getRoutes() {
		return routes;
	}
	
	public void setRoutes(RouteRegistry routeRegistry) {

		Set<Integer> routesSet = new HashSet<Integer>();
			
		Iterator<Map.Entry<Integer, Route>>it = routeRegistry.iterate();
		// iterate through routeRegistry
		while (it.hasNext()) {
			// get the routeRegistry entry
			Map.Entry<Integer, Route> pair = (Map.Entry<Integer, Route>)it.next();
			Route curroute = pair.getValue();
			// get the names of all the Routes we got already
			List<String> thisRoutesNames = new ArrayList<String>();
			try {
				routesSet.stream()
					.forEach(routeID -> {
						String routename = routeRegistry.getElement(routeID).getName();
						thisRoutesNames.add(routename);}
					);
			} catch (NullPointerException e) {
				// don't do anything if this Routes is empty as of yet
			}
			// add the RouteIDs to this Routes attribute only if
			// this Stop is listed in its Stop attribute and if
			// that Route's name is new to this Stop
			int[] routesStops = curroute.getStops();
			for (int i = 0; i<routesStops.length; i++) {
				if (this.stopID == routesStops[i] &&
						!(thisRoutesNames.contains(curroute.getName()))) {
					routesSet.add(curroute.getRouteID());
				}
			}
		}
		// store the RouteIDs as array of primitive integers
		int[] routes = routesSet.stream().mapToInt(Integer::intValue).toArray();
		// set the attribute
		this.routes = routes;;
	}
	
	public Point getLocation() {
		return this.location;
	}
	
	public void setLocation(Point point) {
		this.location = point;
	}
	
	public Coordinate[] getCoordinates() {
		return this.location.getCoordinates();
	}
	
	public double getX() {
		return this.location.getX();
	}
	
	public double getY() {
		return this.location.getY();
	}
	
}
