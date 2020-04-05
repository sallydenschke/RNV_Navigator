package importData;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Point;

import classes.Stop;
import classes.StopRegistry;
import exceptions.AlreadyInRegistryException;

public class Stops {

	public static boolean readCsvStops(StopRegistry stopRegistry, 
			Path stop) 
			throws IOException, 
				FileNotFoundException {
		
		try (BufferedReader reader = new BufferedReader(new FileReader(stop.toFile()))) {
			
			String line = reader.readLine();
			while ((line = reader.readLine()) != null) {
				if (line.trim().isEmpty())
					continue;

				String[] columns = line.split(",");
				
				/*
				 * Stop file has the following columns in the following order:
				 *   [0] stop_id
				 *   [1] stop_name
				 *   [2] stop_lat
				 *   [3] stop_lon
				 *   [4] stop_url (empty in most cases; this is of no interest to us)
				 *   [5] location_type (0 in most cases; this is of no interest to us)
				 */
				int stopID = Integer.parseInt(columns[0]);
				String name = columns[1];
				double lat = Double.parseDouble(columns[2]);
				double lon = Double.parseDouble(columns[3]);
				Point inPoint = ImportFunctions.geometryFactory.createPoint(new Coordinate(lon, lat));
				
				// transform the point
				Point location = Transformer.transformPoint(inPoint, "EPSG:4326", "EPSG:32632");
				
				// add the new Stop to the StopRegistry
				Stop currStop = new Stop(location, stopID, name);
				stopRegistry.addToRegistry(Integer.valueOf(stopID), currStop);
			}
			System.out.println("  done!");
			return false;
			
		} catch (IOException | NumberFormatException | NullPointerException | AlreadyInRegistryException e) {
			System.out.println(ImportFunctions.readingErrors);
			System.out.println(e.getMessage());
			return true;
		}
	}
	
}
