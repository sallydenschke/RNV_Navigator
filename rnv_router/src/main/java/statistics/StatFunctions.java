package statistics;

import java.util.LinkedHashMap;

import classes.RouteRegistry;

public class StatFunctions {	
	
	public static LinkedHashMap<Integer,Double> longestMetre(RouteRegistry routeRegistry) {
		return Meter.compareMeter(routeRegistry, false);
	}
	
	public static LinkedHashMap<Integer,Double> shortestMetre(RouteRegistry routeRegistry) {
		return Meter.compareMeter(routeRegistry, true);
	}
	
	public static LinkedHashMap<Integer,Long> longestMinutes(RouteRegistry routeRegistry) {
		return Minute.compareMinutes(routeRegistry, false);
	}
	
	public static LinkedHashMap<Integer,Long> shortestMinutes(RouteRegistry routeRegistry) {
		return Minute.compareMinutes(routeRegistry, true);
	}
	
}
