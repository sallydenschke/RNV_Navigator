package importData;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;

public class Routes {

	public static boolean readCsvRoutes(Path routes) 
			throws IOException, 
			FileNotFoundException {
		
		ImportFunctions.intermediateStorage = new HashMap<Integer,String>();
		
		try (BufferedReader reader = new BufferedReader(new FileReader(routes.toFile()))) {

			String line = reader.readLine();

			while ((line = reader.readLine()) != null) {
				if (line.trim().isEmpty())
					continue;

				String[] columns = line.split(",");
				
				/*
				 * StopTimes file has the following columns in the following order:
				 *   [0] agency_id (all the same, this is of no interest to us)
				 *   [1] route_id (we will use this as route number)
				 *   [2] route_short_name (this is of no interest to us)
				 *   [3] route_long_name (we will use this as route name)
				 *   [4] route_type (this is if no interest to us)
				 *   [5] route_desc (this is if no interest to us)
				 *   [6] route_url (this is if no interest to us)
				 *   [7] route_color (this is if no interest to us)
				 *   [8] route_text_color (this is if no interest to us)
				 */
				int routeNo = Integer.parseInt(columns[1]);
				String routeName = columns[3];
				ImportFunctions.intermediateStorage.put(routeNo, routeName);
			}
			System.out.println("  done!");
			return false;
		} catch (IOException | NumberFormatException e) {
			System.out.println(ImportFunctions.readingErrors);
			return true;
		}
	}
	

	
}
