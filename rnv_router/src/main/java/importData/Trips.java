package importData;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;

import classes.Route;
import classes.RouteRegistry;
import exceptions.AlreadyInRegistryException;

public class Trips {

	public static boolean readCsvTrips(RouteRegistry routeRegistry, 
			Path trips) 
			throws IOException, 
			FileNotFoundException {
		
		try (BufferedReader reader = new BufferedReader(new FileReader(trips.toFile()))) {

			String line = reader.readLine();

			while ((line = reader.readLine()) != null) {
				if (line.trim().isEmpty())
					continue;

				String[] columns = line.split(",");
				
				/*
				 * StopTimes file has the following columns in the following order:
				 *   [0] route_id (we will use this as route number)
				 *   [1] trip_id (we will use this as route id)
				 *   [2] service_id (this is of no interest to us)
				 *   [3] trip_headsign (this is of no interest to us)
				 */
				int routeNo = Integer.parseInt(columns[0]);
				int routeID = Integer.parseInt(columns[1]);
				String routeName = ImportFunctions.intermediateStorage.get(routeNo);
				
				Route currRoute = new Route(routeID, routeNo, routeName);
				routeRegistry.addToRegistry(Integer.valueOf(routeID), currRoute);
			}
			System.out.println("  done!");
			return false;
		} catch (IOException | NumberFormatException e) {
			System.out.println(ImportFunctions.readingErrors);
			return true;
		} catch (AlreadyInRegistryException f) {
			System.out.println(f.getMessage());
			return true;
		} finally {
			// we will not need this intermediate storage any longer
			// so, to save resources, we set it to null
			ImportFunctions.intermediateStorage = null;
		}
	}
	
}
