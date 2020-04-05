package router;

import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

import org.locationtech.jts.geom.Point;

import classes.Route;
import classes.RouteRegistry;
import classes.Stop;
import classes.StopRegistry;
import main.Main;

public class Router {

	private static Point userLoc;	
	
	private static String routingChoices = "To search for the Stop closest to your coordinates, type\n"
			+ "    \"nextstop\"\n"
			+ "To find out which Routes are accessible at the Stop\n"
			+ " closest to your coordinates, type\n"
			+ "    \"nextroutes\"\n"
			+ "To search for all interchanges are accessible\n"
			+ " via the Routes that are accessible (without having\n"
			+ " to change trains) at the Stop closest to your\n"
			+ " coordinates, type\n"
			+ "    \"interaccess\"\n"
			+ "To find the interchange closest to your coordinates, type\n"
			+ "    \"nextinter\"\n";
	
	private static String enterCoordinates = "Please enter your coordinates below.\n"
			+ "Make sure they're UTM zone32N coordinates (EPSG:32632)\n"
			+ "in the form lon;lat\n" 
			+ "and separated by a semicolon (\";\")\n";
	
	public static void run(Scanner scan, 
			StopRegistry stopRegistry, 
			RouteRegistry routeRegistry) {
		
		System.out.println("\nThank you for choosing the routing functionality.\n"
				+ "Here you can use some semi-routing functions\n"
				+ "on your RNV-data.\n"
				+ routingChoices
				+ Main.madeChoice);
		
		boolean mainmenu = false;
		
		// get the user's choice
		mainmenu = getUserChoice(scan, stopRegistry, routeRegistry, 0);
		
		while(!mainmenu) {
			System.out.println("\nIf you want to do some more routing,\n"
				+ "enter your choice below:\n"
				+ routingChoices
				+ Main.menuReturn
				+ Main.madeChoice);
			mainmenu = getUserChoice(scan, stopRegistry, routeRegistry, 0);
		}
		
		if (mainmenu) {
			System.out.println("Goodbye!");
			return;
		}
	}
	
	
	private static boolean getUserChoice(Scanner scan, 
			StopRegistry stopRegistry, 
			RouteRegistry routeRegistry, 
			int function) {
		
		String input = "";
		String line;
		if ((line = scan.next()) != "") {
			input = line;
		}
		
		boolean fail = false;
		
		// the program allows for some deviation from the keywords

		// closest stop
		if (input.toLowerCase().contains("extst")) {
			System.out.println("You have chosen to find the Stop\n "
					+ "closest to your coordinates.\n"
					+ enterCoordinates);
			fail = getUserChoice(scan, stopRegistry, routeRegistry, 1);			
			return fail;
		
			// accessible routes at closest stop
		} else if (input.toLowerCase().contains("extrou")) {
			System.out.println("You have chosen to find out\n"
					+ "which Routes are accessible at the Stop\n" 
					+ "closest to your coordinates.\n"
					+ enterCoordinates);
			fail = getUserChoice(scan, stopRegistry, routeRegistry, 2);			
			return fail;
			
		// accessible interchanges via routes at closest stop
		} else if (input.toLowerCase().contains("terac")) {
			System.out.println("You have chosen to search for the\n"
					+ "reachable interchanges from the Stop\n"
					+ "clostest to your Coordinates.\n"
					+ enterCoordinates);
			fail = getUserChoice(scan, stopRegistry, routeRegistry, 3);			
			return fail;
			
		// closest accessible interface
		} else if (input.toLowerCase().contains("extint")) {
			System.out.println("You have chosen to search for the\n"
					+ "interchange closest to your Coordinates.\n"
					+ enterCoordinates);
			fail = getUserChoice(scan, stopRegistry, routeRegistry, 4);			
			return fail;
			
		// return to main menu
		} else if (input.toLowerCase().contains("menu")) {
			System.out.println("You have chosen to return to the main menu.");
			fail = true;
			return fail;
		
		/* input for coordinates is valid if it contains numbers, points, commas or semicolons
		 * input is not valid if it contains any other character
		 * to test that, the input String is converted to an IntStream consisting of the integer (dec)
		 * representation of each character
		 */
		} else if (input.chars().anyMatch(x -> (x < 58) && (x >= 48))
				&& input.chars().noneMatch(x-> (x < 44) || (x == 45) || (x == 47) || (x == 58) || (x > 59))
				&& function > 0) {
			
			try {
				Point inPoint = GeoHelpers.getPointFromText(input);
				userLoc = inPoint;
				System.out.println("Thank you for entering your coordinates.\n"
						+ "They are at " + input + "\n");
				Stop closestStop = StopFunctions.getClosestStop(userLoc, stopRegistry);
				
				
				// closest stop
				if (function == 1) {
					System.out.println("The closest Stop is " + closestStop.getName() + "\n" 
					+"at coordinates " + closestStop.getX() + ", " + closestStop.getY() + "\n");
					return false;
				
				} else {
					List<Route> closestRoutes = RouteFunctions.distinctRoutesFromStop(closestStop, routeRegistry);
					
					// routes accessible at closest stop
					if (function == 2) {
						System.out.println("We are calculating which Routes are accessible\n" 
							+ "at the closest Stop.\n");
						System.out.println("The following Routes frequent the Stop closest\n"
							+ "to you (" + closestStop.getName() 
							+ " at [" + closestStop.getX() + ", " + closestStop.getY() + "]):");
						// print the Route with number and name
						closestRoutes.stream()
							.forEach(route -> System.out.println("no. " 
									+ route.getNumber() + ": "
									+ route.getName() + ","));
						return false;
					
					// all interchanges accessible via the routes at the closest stop
					} else if (function ==3) {
						System.out.println("We are calculating which interchanges are accessible\n" 
							+ "at the Stop next to you.\n");
						HashMap<Stop, Route> reachableInterfaces = InterFunctions.reachableInterchanges(closestRoutes, stopRegistry, routeRegistry);
						System.out.println("From the Stop next to you"
								+ "(" + closestStop.getName() 
								+ " at [" + closestStop.getX()+", "+closestStop.getY()+ "]),\n"
								+ "the following interchanges are reachable\n"
								+ "without having to change trains/busses:\n");
						reachableInterfaces.entrySet().stream()
							.forEach(e -> System.out.println("via\n"
									+ "  Route no. " + e.getValue().getNumber()
									+ " " + e.getValue().getName() + ":\n"
									+ "  " + e.getKey().getName() + " "
									+ "at [" + e.getKey().getX()+", "+e.getKey().getY()+"])"));
						return false;
					
					// closest accessible interface 
					} else if (function == 4){
						System.out.println("We are calculating which interchange is closest to you.\n");
						System.out.println(InterFunctions.nextReachableInterchange(inPoint, closestStop, closestRoutes, stopRegistry, routeRegistry));
						return false;
					
					// input doesn't match keywords and isn't a coordinate 
					} else {
						return noMatchTryAgain(scan, stopRegistry, routeRegistry, function);
					}	
				}
			} catch (NullPointerException | NumberFormatException e) {
				return noMatchTryAgain (scan, stopRegistry, routeRegistry, function);
			} 
		
		} else {
			//int retry = 0;
			//while (retry <= 3) {
			//	retry += 1;
			try {
				return noMatchTryAgain(scan, stopRegistry, routeRegistry, function);
			} catch (ArrayIndexOutOfBoundsException | NullPointerException | NumberFormatException e) {
				System.out.println("There seems to be a problem with your choice:\n");
				System.out.println(e);
				System.out.println("You're being referred back to main menu.");
				return true;
			}
			//if (retry > 3) {
			//	System.out.println("Too may incorrect entries.\n");
			//	System.out.println("You're being referred back to main menu.");
			//	return true;
			//}
			//return true;
		}
	}


	private static boolean noMatchTryAgain(Scanner scan,
			StopRegistry stopRegistry, 
			RouteRegistry routeRegistry,
			int function) {
		
		boolean fail = true;

		if (function == 0) {
			System.out.println(Main.didntGetThat);
			System.out.println(routingChoices);
		} else {
			System.out.println("Are you sure your coordinates are in EPSG:32632?");
			System.out.println(enterCoordinates);
		}
		try {
			return getUserChoice(scan, stopRegistry, routeRegistry, function);
		} catch (NullPointerException | NumberFormatException e) {
			System.out.println("There seems to be a problem with your choice:\n");
			System.out.println(e);
			fail = true;
		}
		return fail;
	}
	
	
	// Utility function to get distinct stream elements by key
	public static <T> Predicate<T> distinctByKey(Function<? super T,
			?> keyExtractor) {
		
	    Set<Object> seen = ConcurrentHashMap.newKeySet();
	    return t -> seen.add(keyExtractor.apply(t));
	}
}
