package router;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.locationtech.jts.geom.Point;

import classes.Route;
import classes.RouteRegistry;
import classes.Stop;
import classes.StopRegistry;
import router.CodingHelpers;

public class InterFunctions {

	public static HashMap<Stop, Route> reachableInterchanges(List<Route> reachableRoutes, 
			StopRegistry stopRegistry, 
			RouteRegistry routeRegistry) {
		
		// get the interchanges of all reachable Routes
		HashMap<Stop, Route> allInterchangesPerRoute = new HashMap<Stop, Route>();
		reachableRoutes.stream()
			.filter(CodingHelpers.distinctByKey(Route::getName))
			.forEach(route -> {
				Arrays.stream(route.getStops())
					.filter(stopID -> stopRegistry.getElement(stopID).numOfRoutes() >= 2)
					.forEach(stopID -> allInterchangesPerRoute.put(stopRegistry.getElement(stopID), route));
			});

		return allInterchangesPerRoute;
	}
	
	public static String nextReachableInterchange(Point inPoint, 
			Stop nextStop, 
			List<Route> reachableRoutes,
			StopRegistry stopRegistry,
			RouteRegistry routeRegistry){
		
		HashMap<Stop, Route> allInterchanges = reachableInterchanges(reachableRoutes, stopRegistry, routeRegistry);
		
		List<Stop> candidates = new ArrayList<Stop>();
		allInterchanges.entrySet().stream()
				.forEach(Entry -> candidates.add(Entry.getKey()));
		
		Stop closestInterchange = StopFunctions.getClosest(inPoint, candidates);
		Route viaRoute = allInterchanges.get(closestInterchange);
		double distance = nextStop.getLocation().distance(closestInterchange.getLocation());
		
		String nextInterchange = "";
		
		if (closestInterchange.getName() == nextStop.getName()) {
			nextInterchange = "The Stop closest to you actually is an interchange!\n"
					+ "It's " + closestInterchange.getName() + "\n"
					+ "at [" + nextStop.getX()+","+nextStop.getY() + "].\n"
					+ "It is " + Math.round(distance) + " m away from your position.\n";
		} else {
			nextInterchange = "The next interchange that is reacheable from\n"
					+ "the Stop closest to you (" + nextStop.getName() + "\n"
					+ "at [" + nextStop.getX()+","+nextStop.getY() + "] )\n"
					+ "is " + closestInterchange.getName() + " at [" 
					+ closestInterchange.getX()+","+closestInterchange.getY()+"].\n"
					+ "It is " + Math.round(distance) + " m away from your position.\n"
					+ "You can reach it via Route no. " + viaRoute.getNumber() + " :\n"
					+ viaRoute.getName();
		}
		
		return nextInterchange;
	}
	
}
