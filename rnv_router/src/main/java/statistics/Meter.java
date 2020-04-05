package statistics;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import classes.Route;
import classes.RouteRegistry;

public class Meter {

	public static LinkedHashMap<Integer,Double> compareMeter(RouteRegistry routeRegistry,
			boolean order){

		// if order is false, it's the 5 largest (descending order),
		// if order is true, it's the 5 smallest (ascending order)
		LinkedHashMap<Integer,Double> top5Meter = new LinkedHashMap<Integer,Double>();
		
		// 2-dimensional double array: top 5 elements, each consisting of RouteID, RouteNumber and length
		double[][] results = new double[5][3];
		double[] routeNumbers = new double[5];

		Iterator<Map.Entry<Integer, Route>>it = routeRegistry.iterate();
		// iterate through routeRegistry
		while (it.hasNext()) {
			// get the routeRegistry entry
			Map.Entry<Integer, Route> pair = (Map.Entry<Integer, Route>)it.next();
			Route curroute = pair.getValue();
			double length = curroute.getLength();
			double number = (double) curroute.getNumber();
			// current element to compare and to results entries and possibly enter into the results
			double[] currouteElement = new double[] {(double)curroute.getRouteID(), length, number};
			
			CompareAndConquer.compareAndConquer(results, routeNumbers, currouteElement, length, 0, order);
		}
		/* 
		 * Iterate through the results array and add the routeID-length-couples
		 * to the Top5Metre Map in the same order as they are in the results
		 *     [0] longest resp. shortest
		 *     [1] second-longest resp. second-shortest
		 *     [3] third-longest resp. third-shortest
		 *     ...
		 * Returning the results as a LinkedHashMap ensures that the insertion order
		 * is kept, thus representing the Top 5 longest Routes. 
		 */		
		Arrays.stream(results).forEach(element -> top5Meter.put((int)element[0], element[1])); 
		return top5Meter;
	}
	
}
