package statistics;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import classes.RouteRegistry;
import classes.Stop;
import classes.StopRegistry;

public class Frequency {

	public static LinkedHashMap<Integer,Integer> mostFrequented(StopRegistry stopRegistry, 
			RouteRegistry routeRegistry) {

		LinkedHashMap<Integer,Integer> top5Freq = new LinkedHashMap<Integer,Integer>();
		
		// 2-dimensional int array: top 5 elements, each consisting of stopID and number of Routes
		int[][] results = new int[5][2];
		String[] stopNames = new String[5];
		
		Iterator<Map.Entry<Integer, Stop>>it = stopRegistry.iterate();
		// iterate through routeRegistry
		while (it.hasNext()) {
			// get the routeRegistry entry
			Map.Entry<Integer, Stop> pair = (Map.Entry<Integer, Stop>)it.next();
			Stop currstop = pair.getValue();
			currstop.setRoutes(routeRegistry);
			int numOfRoutes = currstop.numOfRoutes();
			int[] currstopElement = new int[] {currstop.getStopID(), numOfRoutes};
			String currstopName = currstop.getName();
			CompareAndConquer.compareAndConquer(results, stopNames, currstopElement, numOfRoutes, currstopName, 0);
		}
		/* 
		 * Iterate through the results array and add the stopID-numOfRoutes-couples
		 * to the Top5Minutes Map in the same order as they are in the results
		 *     [0] most frequented
		 *     [1] second-most-frequented
		 *     [3] third-most-frequented
		 *     ...
		 * Returning the results as a LinkedHashMap ensures that the insertion order
		 * is kept, thus representing the Top 5 most frequented Stops. 
		 */
		Arrays.stream(results).forEach(element -> top5Freq.put(element[0], element[1])); 
		return top5Freq;
	}
	
}
