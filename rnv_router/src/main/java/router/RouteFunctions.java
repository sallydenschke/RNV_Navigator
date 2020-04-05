package router;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import classes.Route;
import classes.RouteRegistry;
import classes.Stop;

public class RouteFunctions {
	
	// get all Routes that frequent a Stop
	public static List<Route> allRoutesFromStop(Stop stop,
			RouteRegistry routeRegistry) {
		
		// get all Routes at the Stop as List
		List<Route> routesAtStop = new ArrayList<Route>();	
		Arrays.stream(stop.getRoutes())
			.forEach(routeid -> routesAtStop.add(routeRegistry.getElement(routeid)));
		return routesAtStop;
	}
	
	// get the distinct (by RouteNumber) Routes that frequent a Stop
	public static List<Route> distinctRoutesFromStop(Stop stop, 
			RouteRegistry routeRegistry) {
		
		// get all Routes at Stop as list
		List<Route> allRoutes = allRoutesFromStop(stop, routeRegistry);
		/* get distinct Routes by number
		 * This is necessary because we have different versions 
		 * of each Route
		 */
		List<Route> distinctRoutes = allRoutes.stream()
				.filter(CodingHelpers.distinctByKey(Route::getNumber))
				.collect(Collectors.toList());
		return distinctRoutes;
	}

}
