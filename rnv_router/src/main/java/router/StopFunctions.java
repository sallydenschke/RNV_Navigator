package router;

import java.util.List;

import org.locationtech.jts.geom.Point;

import classes.Stop;
import classes.StopRegistry;


public class StopFunctions {

	/*
	 * get the closest stop to a given Point
	 * creating a list of candidates with a dynamic buffer
	 * and getting the closest one by calling getClosest
	 */
	public static Stop getClosestStop(Point inPoint, 
			StopRegistry stopRegistry) {
		
		List<Stop> candidates = GeoHelpers.dynamicBuffer(inPoint, stopRegistry);
				
		Stop closestStop = getClosest(inPoint, candidates);
		return closestStop;
	}

	/*
	 * get the closest stop to a given point from a list of candidates
	 */
	public static Stop getClosest(Point inPoint, List<Stop> candidates) {
		/* Select closest Stop from candidates
		 * Compare the candidates' distances to the input Point.
		 * Compare the first candidate's distance to the input Point
		 * to an initial, high distance to start off.
		 * Then, iterate through the candidates. If the candidate's
		 * distance is lower than the one currently stored in the 
		 * 'dist' variable, overwrite the latter with this distance
		 * and the closestStop with this Stop. Thus, the candidate
		 * with the smallest distance to the input Point is selected.
		 */
		Stop closestStop = null;
		double dist = 200000.0;
		for (Stop stop : candidates) {
			double thisDist = stop.getLocation().distance(inPoint);
			if (thisDist < dist) {
				closestStop = stop;
				dist = thisDist;
			}
		}
		return closestStop;
	}
	
}
