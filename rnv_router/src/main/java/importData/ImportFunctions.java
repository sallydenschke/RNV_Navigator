package importData;


import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.geotools.feature.SchemaException;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.locationtech.jts.geom.GeometryFactory;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.operation.TransformException;

import classes.Route;
import classes.RouteRegistry;
import classes.Stop;
import classes.StopRegistry;


public class ImportFunctions {
	
	public static GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
	
	public static HashMap<Integer,String> intermediateStorage;
	
	public static String readingErrors = "  The program has problems reading your file.\n"
			+ "  Are you sure you've handed in the right path?";
	
	public static boolean run(StopRegistry stopRegistry, 
			RouteRegistry routeRegistry,
			Path stops, 
			Path routes, 
			Path trips, 
			Path stop_times) 
			throws IOException, 
				NoSuchAuthorityCodeException, 
				FactoryException, 
				MismatchedDimensionException, 
				TransformException, 
				SchemaException {
		
		boolean fail = true;
		
		System.out.println("Reading Stop data");
		// returns true if the import of stops data failed
		fail = Stops.readCsvStops(stopRegistry, stops);
		// only do the other imports if this one worked
		// because there are too many interdependencies within the imports
		if (!fail) {
			System.out.println("Reading Route data");
			fail = Routes.readCsvRoutes(routes);
		}
		if (!fail) {
			System.out.println("Reading Trip data");
			fail = Trips.readCsvTrips(routeRegistry, trips);
		}
		if (!fail) {
			System.out.println("Reading Stop_times data");
			System.out.println("  This may take a while. Thank you for your patience.");
			fail = StopTimes.readCsvStopTimes(routeRegistry, stop_times, stopRegistry);
		}
		if (!fail) {
			System.out.println("Finishing the import");
			// iterate through StopRegistry and collect the Stop arrays
			Iterator<Map.Entry<Integer, Stop>>it = stopRegistry.iterate();
			while (it.hasNext()) {
				Map.Entry<Integer, Stop> pair = (Map.Entry<Integer, Stop>)it.next();
				Stop currstop = pair.getValue();
				currstop.setRoutes(routeRegistry);
			}
		}
		return fail;
	}

	/*
	 * method to check if a route with these stops already exists
	 */
	public static boolean checkGotThisRoute(RouteRegistry routeRegistry, 
			int[] intStops) {
		
		boolean alreadyGotThisRoute = false;
		
		// iterate through Route Registry and collect the Stop arrays
		Iterator<Map.Entry<Integer, Route>>it = routeRegistry.iterate();
		List<int[]> stopslist = new ArrayList<int[]>();
		int i=0;
		while (it.hasNext()) {
			Map.Entry<Integer, Route> pair = (Map.Entry<Integer, Route>)it.next();
			Route curroute = pair.getValue();
			stopslist.add(i, curroute.getStops());
			i+=1;
		}
		
		/* compare the current Stops array to the Stops arrays 
		 * previously extracted from the routeRegistry to check 
		 * if we already have a Route with these exact Stops
		 */
		for (i = 0; i<stopslist.size(); i++) {
			int[] element = intStops;
			int[] comparison = stopslist.get(i);
			if (Arrays.equals(element,comparison)) {
				/* if we found one match with a Stop array 
				 * from routeRegistry, we can Stop comparing
				 */
				break;
			}
		}
		return alreadyGotThisRoute;
	}
	
}
