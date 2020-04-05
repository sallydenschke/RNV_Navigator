package importData;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PushbackReader;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import classes.Route;
import classes.RouteRegistry;
import classes.StopRegistry;
import exceptions.NoStopsDataException;
import importData.ImportFunctions;

public class StopTimes {

	public static boolean readCsvStopTimes(RouteRegistry routeRegistry, 
			Path stop_times, 
			StopRegistry stopRegistry) 
			throws IOException, 
			FileNotFoundException {
		
		try (BufferedReader reader = new BufferedReader(new FileReader(stop_times.toFile())); 
				PushbackReader pbr = new PushbackReader(reader)) {

			// Date Formatter for start and end time
			SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
			
			// variables to store intermediate results in
			Date lastDepartureTime = null;
			int lastRouteID = 0;
			
			// array to collect Stops in (one per Route)
			List<Integer> stopIDs = new ArrayList<Integer>();			
			
			String line = reader.readLine();

			boolean start = true;

			// read lines
			while ((line = reader.readLine()) != null) {
				// skip empty lines
				if (line.trim().isEmpty())
					continue;
				// get the text
				String[] columns = line.split(",");
				
				/*
				 * StopTimes file has the following columns in the following order:
				 *   [0] trip_id (we will use this as routeID)
				 *   [1] arrival_time (of the first Stop: startTime)
				 *   [2] departure_time (of the last Stop: stopTime)
				 *   [3] stop_id
				 *   [4] stop_sequence
				 *   [5] stop_headsign (empty in most cases; this is of no interest to us)
				 *   [6] pickup_type (empty in most cases; this is of no interest to us)
				 *   [7] drop_off_type (empty in most cases; this is of no interest to us)
				 *   [8] shape_dist_traveled (empty in most cases; this is of no interest to us)
				 *   [9] timepoint (empty in most cases; this is of no interest to us)
				 */
				// get the data
				int routeID = Integer.parseInt(columns[0]);
				Date arrivalTime = sdf.parse(columns[1]);
				int stopID = Integer.parseInt(columns[3]);
				// Stop sequence, starts with 1
				int stopIndex = Integer.parseInt(columns[4]);
				// index for the Stop in the Stops-array of a Route, starts with 0
				int indx = stopIndex-1;
				
				/*
				 * If the Stop in this line is the first of the Route, we know we're
				 *   starting a new Route with this line. That means we have collected
				 *   all of the Stops for the last Route. In that case we can set the
				 *   Stops array of the last Route to those Stops we have collected, 
				 *   and start with a fresh Stops array for the new Route.
				 *   We can also set the start time for this new Route.
				 *   Now that we finished the last Route, we can reset our Stops array
				 *   and store this Route's id for when the Route changes again.
				 */
				if (start) {
					/* If this is the first line, the StopIndex will be 1 but there
					 * won't be any StopIDs nor lastRouteID etc.
					 * This is a special case, that we have to handle differently.
					 */
					Route firstRoute = routeRegistry.getElement(routeID);
					firstRoute.setStartTime(arrivalTime);
					lastRouteID = routeID;
				} else if (stopIndex == 1) {
					// finish last Route
					Route finishedRoute = routeRegistry.getElement(lastRouteID);
					// get stopIDs as int array
					int[] intStops = stopIDs.stream().mapToInt(i -> i).toArray();
					
					boolean alreadyGotThisRoute = ImportFunctions.checkGotThisRoute(routeRegistry, intStops);
					
					// only add this Route if we do not have that Route yet
					if (!alreadyGotThisRoute) {
						setRoute(stopRegistry, lastDepartureTime, intStops, finishedRoute);
					} else {
						// we do not want to add redundancy
						routeRegistry.remove(lastRouteID);
					}
										
					// prepare (for) this Route
					routeRegistry.getElement(routeID).setStartTime(arrivalTime);
					stopIDs = new ArrayList<Integer>();
					lastRouteID = routeID;
				}
				/* We cannot, however, handle the departure time as we did the RouteID.
				 *   Would we save the departure time only when we encounter a new Route,
				 *   the departure time would be that of the first Stop. It's less efficient
				 *   to store the departure time for every line; alas it's the way to go here.
				 */
				lastDepartureTime = sdf.parse(columns[2]);
				
				// add the current Stop to the Stops array at the specified index
				stopIDs.add(indx, Integer.valueOf(stopID));
				
				start = false;
			}
			// last Route needs to be finished
			int[] intStops = stopIDs.stream().mapToInt(i -> i).toArray();
			
			Route candidateRoute = routeRegistry.getElement(lastRouteID);
			
			boolean alreadyGotThisRoute = ImportFunctions.checkGotThisRoute(routeRegistry, intStops);
			
			// only add this Route if we do not have that Route yet
			if (!alreadyGotThisRoute) {
				Route lastRoute = candidateRoute;
				setRoute(stopRegistry, lastDepartureTime, intStops, lastRoute);
			} else {
				// we do not want to add redundancy
				routeRegistry.remove(lastRouteID);
			}
			
			System.out.println("  done reading");
			return false;
		} catch (IOException | ParseException e) {
			System.out.println(ImportFunctions.readingErrors);
			return true;
		} catch (NoStopsDataException f) {
			System.out.println(f.getMessage());
			return true;
		}
	}

	private static void setRoute(StopRegistry stopRegistry, 
			Date lastDepartureTime, 
			int[] intStops, 
			Route lastRoute) {
		// if there's no Stop data yet re-throw the exception
		try {
			lastRoute.setStops(intStops);
		} catch (NoStopsDataException e) {
			throw e;
		}
		lastRoute.setEndTime(lastDepartureTime);
		lastRoute.setLocationLine(stopRegistry);
	}
}
