package classes;

import java.util.Date;

import classes.Stop;
import exceptions.NoStopsDataException;

import org.geotools.geometry.jts.JTSFactoryFinder;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;



public class Route {
	// Attributes
	/* 
	 * It's a design decision to implement this with the LineString
	 * as private class attribute instead of extending LineString.
	 * It avoids potential trouble that may come from extending a LineString.
	 * But most importantly, this way we can have several forms of Routes,
	 * without running into the problem that a class can extend only one
	 * other class. This way we can have routes that use LineStrings, MultiLineStrings
	 * or other structures for location without having to define new classes
	 * for them.
	 * Additionally, this helps to allow the user access only trough methods as interfaces.
	 */
	private LineString locationLine;
	/* 
	 * All attributes are  private to allow user access via methods only.
	 */
	// tripID from trips.csv
	private final int routeID;
	//private Stop[] stops;
	// route long name from routes.csv
	private String name;
	// route short name resp. routeID from routes.csv
	private int number;
	// stops from trip in the right sequence from trips.csv
	private int[] stops;
	// departure time of first stop in stops
	private Date startTime;
	// arrival time of last stop in stops
	private Date endTime;
	
	
	// Constructors
	
	// starting constructor for data import
	public Route(final int routeID, final int number, final String name) {
		this.routeID = routeID;
		this.number = number;
		this.name = name;
	}
	
	// only location and ID, no further information
	public Route(final LineString locationLine, 
			final int routeID) {
		this.locationLine = locationLine;
		this.routeID = routeID;
	}

	// additional information
	public Route(final LineString locationLine, 
			final int routeID, 
			final String name, 
			final int number) {
		this.locationLine = locationLine;
		this.routeID = routeID;
		this.name = name;
		this.number = number;
	}
	
	// "complete" Route
	public Route(final LineString locationLine, 
			final int routeID, 
			final String name, 
			final int number, 
			final int[] stops) {
		this.locationLine = locationLine;
		this.routeID = routeID;
		this.name = name;
		this.number = number;
		this.stops = stops;
	}
	
	
	// Methods
	// There's several methods brought with the LineString class.
	// One of them is .getLength(), so we don't have to implement this ourselves	
	public double getLength() {
		try {
			return this.locationLine.getLength();
		} catch (NullPointerException e) {
			System.out.println("There is no LocationLine yet.");
			return 0;
		}
		
	}
	
	/*
	 * This method calculates the duration of a route from start to end stop.
	 * It calculates the difference in milliseconds (diff), then converts the 
	 * result to seconds (seconds), then to minutes (duration).
	 * Notes on this:
	 * 1. This calculation would not work out if the two times to calculate
	 *   the difference between would be on seperate days. Eg. if we had routes
	 *   that started before midnight on day one and end after 00:00:00 on day 2.
	 *   We do have cases of this. However, they are represented differently in
	 *   the data: If a route's time passes midnight, the further time is
	 *   represented as 24:00:01. Thus making this calculation possible.
	 * 2. The difference in seconds is defined as float to avoid rounding errors.
	 *   However, the user is not interested in details such as "duration is 42.4 minutes".
	 *   Thus we return a long.
	 */
	public long getDuration() {
		// get the start time and the end time in the predefined format
		long duration;
		long diff = Math.abs(this.endTime.getTime() - this.startTime.getTime());
		float seconds = diff/60;
		duration = (long) (seconds/60);
		return duration;
	}
	
	public void setLocationLine(StopRegistry stopRegistry) 
			throws NoStopsDataException {
		
		GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
		try {
			int[] intStops = this.stops;
			Coordinate[] nodes = new Coordinate[intStops.length];
			for (int i = 0; i < intStops.length; i++) {
				int stopID = intStops[i];
				Stop stop = stopRegistry.getElement(stopID);
				Coordinate coord = new Coordinate(stop.getY(), stop.getX());
				nodes[i] = coord;
				}
			// lineString from coordinates
			LineString locationLine = geometryFactory.createLineString(nodes);
			this.locationLine = locationLine;
		} catch (NullPointerException e) {
			throw new NoStopsDataException();
		}
	}
	
	public int numOfStops() {
		return this.stops.length;
	}
	
	
	// toString
	public String toString(StopRegistry stopRegistry) {
		return "Route no. "+number + " "+ name  
				+ "\n with RouteID "+ routeID
				+ "\nstarts from " + stopRegistry.getElement(stops[0]).getName()
				+ "and ends from " + stopRegistry.getElement(stops[stops.length-1]).getName();
	}
	
	@Override
	public String toString() {
		return "Route no. "+number + " "+ name  
				+ "\n with RouteID "+ routeID;
	}
	

	// Getters and Setters
	public int getRouteID() {
		return routeID;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public int getNumber() {
		return number;
	}
	
	public void setNumber(int number) {
		this.number = number;
	}
	
	public int[] getStops() {
		return this.stops;
	}
	
	public void setStopAtIndex(int stopID, int Index) {
		this.stops[Index] = stopID;
	}

	public void setStops(int[] stops) {
		this.stops = stops;
	}

	public Stop getStart(StopRegistry stopRegistry) {
		int stopID = this.stops[0];
		return stopRegistry.getElement(stopID);
	}
	
	public Stop getEnd(StopRegistry stopRegistry) {
		int stopID = this.stops[0];
		return stopRegistry.getElement(stopID);
	}
	
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public LineString getLocationLine() {
		return this.locationLine;
	}
	
	public void setLocationLine(LineString locationLine) {
		this.locationLine = locationLine;
	}

}
