package importData;

import classes.StopRegistry;
import classes.RouteRegistry;


import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;


import org.geotools.feature.SchemaException;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;

public class ImportData {
	
	/*
	 * Import Data Dialog
	 * @param scan 
	 * @param stopRegistry
	 * @param routeRegistry
	 * @return
	 */
	public static boolean run(Scanner scan, 
			StopRegistry stopRegistry, 
			RouteRegistry routeRegistry) {

		// inform the user on what to do
		System.out.println("We will need to import your data.\n"
				+ "Please enter the path to your comma-separated txt files.\n" + "You will need\n"
				+ "one file containing the Stops,\n" + "one file containing the Routes,\n"
				+ "one file containing the Trips and\n" + "one file contining the Stop times.\n"
				+ "You will find these files in the directory\n"
				+ "provided with this code.\n"
				+ "Alternatively, you can download any\n"
				+ "GTFS-dataset from the RNV website:\n"
				+ "https://opendata.rnv-online.de/dataset/gtfs-general-transit-feed-specification\n\n"
				+ "Please enter the paths below,\n"
				+ "separated with a semicolon \";\" only.\n"
				+ "Other separators will lead to errors.\n"
				+ "When you entered all your paths,\n"
				+ "please press enter.\n");

		boolean fail = true;
		// getUserChoice returns true if there's been trouble in the import
		fail = getUserChoice(scan, stopRegistry, routeRegistry);
		
		int retries = 0;
		// in case of a fail, users have 4 more retries before the program
		// shuts down
		while (fail && retries < 4) {
			
			System.out.println("It looks like there were problems importing\n"
					+ "your data. Maybe you didn't enter the\n"
					+ "correct paths to all four neccessary files?\n"
					+ "The paths should lead to .txt files containing\n"
					+ "stops, routes, trips and stop_times\n"
					+ "in the GTFS-Format.\n"
					+ "You have " + (4-retries) + " more tries left.\n"
					+ "After that, the program shuts down.\n"
					+ "Please enter your paths\n"
					+ "separated by semicolons (\";\").\n");
			try {
				// retry with clean registers
				stopRegistry.resetRegistry();
				routeRegistry.resetRegistry();
				fail = getUserChoice(scan, stopRegistry, routeRegistry);
			} catch (NullPointerException e) {
				System.out.println("Sorry, we don't get it.\n"
						+ "You're being refered back to the\n"
						+ "main menu.");
				return true;
			}
			retries += 1;
		}
		if (! fail) {
			return false;
		}
		System.out.println("Sorry, we don't get it.\n"
			+ "You're being refered back to the\n"
			+ "main menu.");
		return true;
	}

	/*
	 * getUserChoice (ImportData)
	 * the user's input is converted into paths
	 * and the import functions are run
	 */
	private static boolean getUserChoice(Scanner scan, 
			StopRegistry stopRegistry, 
			RouteRegistry routeRegistry) {
		
		String input = "";
		String line;
		if ((line = scan.next()) != "") {
			input = line;
		}
		String[] pathList;

		// catch mistakes the user may make in entering the paths
		try {
			pathList = input.split(";");
		} catch (NoSuchElementException e) {
			System.out.println("Please enter your paths separated by a semicolon \";\" only.");
			return true;
		} 

		// check if user entered all the paths to all four files
		if (pathList.length < 4) {
			System.out.println("\nIt looks like you didn't enter the\n"
					+ "paths to all four neccessary files.\n"
					+ "Please enter the paths to your files containing\n"
					+ "stops;routes;trips;stop_times.");
			return true;
		}
		
		try {	
			// collect paths in list
			ArrayList<Path> paths = new ArrayList<Path>();

			// variables for the file's paths
			Path stopPath = null;
			Path routePath = null;
			Path tripPath = null;
			Path timesPath = null;

			// iterate through string-paths
			for (int a = 0; a < pathList.length; a++) {
				String substring = pathList[a];
				Path currpath = Paths.get(substring);
				paths.add(a, currpath);
				/*
				 * check which file the path belongs to Order is of importance here! In the
				 * programmer's case the files were contained within a "rnv_router" directory.
				 * Ie. ALL the paths contained "route". Additionally, the Stop times file is
				 * called "stop_times" ie. Stop is contained in the substring, but we want to
				 * recognize it as the times file.
				 */
				if (!substring.contains("txt")) {
					System.out.println("Please enter a path to a txt file");
				}
				if (substring.contains("trip")) {
					// trip path
					tripPath = currpath;
				} else if (substring.contains("times")) {
					// Stop times path
					timesPath = currpath;
				} else if (substring.contains("stop")) {
					// Stop path
					stopPath = currpath;
				} else if (substring.contains("route")) {
					// Route path
					routePath = currpath;
				} else {
					System.out.println("There seems to be a problem with the\n"
							+ "path files you\'ve chosen.\n"
							+ "Are you sure they were correct?\n");
					System.out.println("Is this your path?");
					System.out.println(substring);
					return true;
				}
			}
			// If you want to check your paths just run this
			/*
			System.out.println(stopPath);
			System.out.println(routePath);
			System.out.println(tripPath);
			System.out.println(timesPath);
			 */ 

			// next step: go to the import functions to call each import
			// this returns true if there is trouble in the imports
			try {
				return ImportFunctions.run(stopRegistry, routeRegistry, stopPath, routePath, tripPath, timesPath);

			} catch (MismatchedDimensionException | IOException | FactoryException | TransformException
					| SchemaException e) {
				System.out.println(e.getMessage());
				return true;
			} catch (InvalidPathException e) {
				System.out.println("It looks like what you entered aren't\n"
						+ "correct paths.\n"
						+ "Please enter the paths to your files containing\n"
						+ "stops;routes;trips;stop_times\n"
						+ "separated by semicolons (\"\").");
				return true;
			}

		} catch (NullPointerException e) {
			System.out.println("It looks like you didn't enter the\n"
					+ "correct paths to all four neccessary files.\n"
					+ "Please enter the paths to your files containing\n"
					+ "stops;routes;trips;stop_times\n"
					+ "separated by semicolons (\"\").");
			return true;
		} catch (InvalidPathException e) {
			System.out.println("It looks like what you entered aren't\n"
					+ "correct paths.\n"
					+ "Please enter the paths to your files containing\n"
					+ "stops;routes;trips;stop_times\n"
					+ "separated by semicolons (\"\").");
			return true;
		}	
	}
}
