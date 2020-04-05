package main;

import java.util.Scanner;

import statistics.Statistics;

import classes.RouteRegistry;
import classes.StopRegistry;
import importData.ImportData;
import router.Router;

/*
 * RNV_Router
 * @author Selina Schilling
 * Matnr. 3158782
 * final project "RNV_Router"
 * for class "Einführung in die Programmierung für Geographen mit Java"
 * winter-semester 2019/2020
 * Dossenheim, 05.04.2020
 */
public class Main {

	// store some messages for use in several other classes
	public static String madeChoice =  "When you made your choice, please press\n"
			+ "enter after the corresponding keyword.\n";
	public static String menuReturn = "Or, if you want to return to the main menu,\n"
			+ "Enter \"menu\"\n";
	public static String didntGetThat = "Sorry, we didn't get that."
			+ "\n Please enter your choice once again.\n";
	
	// store the main message of this class
	private static String mainChoice = "If you want to calculate some statistics, enter\n"
			+ "    \"statistics\"\n"
			+ "Or, if you want to use the RNV-Router, enter\n"
			+ "    \"router\"\n"
			+ madeChoice
			+ "To end the program, enter \"end\"";

	/*
	 * Main Menu
	 * entry point for program & main menu
	 */
	public static void main(String[] args) {
		// inform the user on what this is
		System.out.println("Hello and welcome to this little program!\n"
				+ "Here you can explore the RNV data. The functionality is\n"
				+ "split in two parts:\n"
				+ "Statistics and\n"
				+ "RNV-Router.\n");
		
		// create a scanner to get the user input
		// throughout this program
		Scanner scan = new Scanner(System.in);
		// create registers for Stops and Routes
		StopRegistry stopRegistry = new StopRegistry();
		RouteRegistry routeRegistry = new RouteRegistry();

		boolean end = false;
		// Import Data returns true if there was trouble in the import
		end = ImportData.run(scan, stopRegistry, routeRegistry);

		if (end) {
			System.out.println("\nSorry, we couldn't import your data.\n"
					+ "Please check if the files you want to import\n"
					+ "before restarting this program.\n"
					+ "Goodbye!");
		} else {
			System.out.println("\nYour data has been imported!\n"
					+ "You can now choose what you want to do with it.\n"
					+ mainChoice);
			end = getUserChoice(scan, stopRegistry, routeRegistry);
		}

		// users may come back to main menu anytime, landing them here
		while (!end) {
			System.out.println("\n" + mainChoice );
			end = getUserChoice(scan, stopRegistry, routeRegistry);
		}
	}
	
	/*
	 * getUserChoice (Main)
	 * method to get the user's choices in the main menu
	 */
	private static boolean getUserChoice(Scanner scan, 
			StopRegistry stopRegistry, 
			RouteRegistry routeRegistry) {
		String input = "";
		String line;
		if ((line = scan.next()) != "") {
			input = line;
		}

		// user chose statistics
		if (input.toLowerCase().contains("stat")) {
			Statistics.run(scan, stopRegistry, routeRegistry);
			return false;
		// user chose the router
		} else if (input.toLowerCase().contains("rout")) {
			Router.run(scan, stopRegistry, routeRegistry);
			return false;
		// user chose to end this program
		} else if (input.toLowerCase().contains("end")) {
			System.out.println("You have chosen to end this program.\n"
					+ "Goodbye!");
			return true;

		// something went wrong
		} else {
			System.out.println(didntGetThat);
			try {
				getUserChoice(scan, stopRegistry, routeRegistry);
			} catch (NullPointerException e) {
				System.out.println("There seems to be a problem with your choice:\n");
				System.out.println(e);
			}
			return false;
		}
	}

}

