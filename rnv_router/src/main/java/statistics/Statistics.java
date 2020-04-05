package statistics;


import java.util.LinkedHashMap;
import java.util.Scanner;

import classes.RouteRegistry;
import classes.StopRegistry;
import main.Main;

public class Statistics {

	private static String statChoice = "Please enter your Choice below:\n"
			+ "    \"longmeter\" for the 5 longest Routes in meters\n"
			+ "    \"shortmeter\" for the 5 shortest Routes in meters\n"
			+ "    \"longminutes\" for the 5 longest Routes in minutes\n"
			+ "    \"shortminutes\" for the 5 shortest Routes in minutes\n"
			+ "    \"frequented\" or \"busiest\" for the 5 most\n"
			+ "      frequented Stops\n";
	
	public static void run(Scanner scan, 
			StopRegistry stopRegistry, 
			RouteRegistry routeRegistry) {
		
		System.out.println("\nThank you for choosing the statistics functionality.\n"
				+ "Here you can explore the Top5 Routes and Stops:\n"
				+ "The 5 longest and shortest Routes in meters\n"
				+ "and the 5 longest and shortest Routes in minutes.\n"
				+ "You can also explore which Stops are the 5 most\n"
				+ "frequented Stops in this network.\n"
				+ "What would you like to know?\n"
				+ statChoice
				+ Main.madeChoice);
		
		boolean mainmenu = false;
		
		// get the user's choice
		mainmenu = getUserChoice(scan, stopRegistry, routeRegistry);
		
		while(!mainmenu) {
			System.out.println("\nDo you want to choose another statistic?\n"
				+ statChoice
				+ "Or, if you want to return to the main menu,\n"
				+ "Enter \"menu\"\n"
				+ "When you made your choice, please press enter after the corresponding keyword.\n");
			mainmenu = getUserChoice(scan, stopRegistry, routeRegistry);
		}
		
		if (mainmenu) {
			System.out.println("Goodbye!");
			return;
		}
		return;
	}
	
	
	private static boolean getUserChoice(Scanner scan, 
			StopRegistry stopRegistry, 
			RouteRegistry routeRegistry) {
		
		String input = "";
		String line;
		if ((line = scan.next()) != "") {
			input = line;
		}
		if (input.toLowerCase().contains("ongme")) {
			System.out.println("You have chosen to find the 5 longest\n"
					+ "Routes in meter.\n"
					+ "The calculation is being started. Please be patient.\n");
			LinkedHashMap<Integer,Double> top5Meter = StatFunctions.longestMetre(routeRegistry);
			System.out.println("The 5 longest Routes in meter are:\n");
			// get keys and values converting storing them in arrays
			Integer[] keys = top5Meter.keySet().toArray(Integer[]::new);
			Double[] values = top5Meter.values().toArray(Double[]::new);
			for (int i = 0, j=top5Meter.size(); i < top5Meter.size(); i++, j--) {
				System.out.println(j + ". Route number " + routeRegistry.getElement(keys[i]).getNumber() +
						" with length of ca. " + Math.round(values[i]*100.0)/100 + " m.");
			}
			return false;
			
		} else if (input.toLowerCase().contains("ortme")) {
			System.out.println("You have chosen to find the 5 shortest\n"
					+ "Routes in meter.\n"
					+ "The calculation is being started. Please be patient.\n");
			LinkedHashMap<Integer,Double> bottom5Meter = StatFunctions.shortestMetre(routeRegistry);
			System.out.println("The 5 shortest Routes in meter are:\n");
			// get keys and values converting storing them in arrays
			Integer[] keys = bottom5Meter.keySet().toArray(Integer[]::new);
			Double[] values = bottom5Meter.values().toArray(Double[]::new);
			for (int i = 0, j=bottom5Meter.size(); i < bottom5Meter.size(); i++, j--) {
				System.out.println(j + ". Route number " + routeRegistry.getElement(keys[i]).getNumber() +
						" with length of ca. " + Math.round(values[i]*100.0)/100 + " m.");
			}
			return false;
			
		} else if (input.toLowerCase().contains("ongmi")) {
			System.out.println("You have chosen to find the 5 longest\n"
					+ " Routes in minutes.\n"
					+ "The calculation is being started. Please be patient.");
			LinkedHashMap<Integer,Long> top5Minutes = StatFunctions.longestMinutes(routeRegistry);
			System.out.println("The 5 longest Routes in minutes are:\n");
			// get keys and values converting storing them in arrays
			Integer[] keys = top5Minutes.keySet().toArray(Integer[]::new);
			Long[] values = top5Minutes.values().toArray(Long[]::new);
			for (int i = 0, j=top5Minutes.size(); i < top5Minutes.size(); i++, j--) {
				System.out.println(j + ". Route number " + routeRegistry.getElement(keys[i]).getNumber() +
						" with duration of " + values[i] + " minutes.");
			}
			return false;
			
		} else if (input.toLowerCase().contains("ortmi")) {
			System.out.println("You have chosen to find the 5 shortest Routes in minutes.\n"
					+ "The calculation is being started. Please be patient.");
			LinkedHashMap<Integer,Long> bottom5Minutes = StatFunctions.shortestMinutes(routeRegistry);
			System.out.println("The 5 shortest Routes in minutes are:\n");
			// get keys and values converting storing them in arrays
			Integer[] keys = bottom5Minutes.keySet().toArray(Integer[]::new);
			Long[] values = bottom5Minutes.values().toArray(Long[]::new);
			for (int i = 0, j=bottom5Minutes.size(); i < bottom5Minutes.size(); i++, j--) {
				System.out.println(j + ". Route number " + routeRegistry.getElement(keys[i]).getNumber() +
						" with duration of " + values[i] + " minutes");
			}
			return false;
			
		} else if (input.toLowerCase().contains("freq") || input.toLowerCase().contains("bus") ) {
			System.out.println("You have chosen to find the 5 most frequented Stops.\n"
					+ "The calculation is being started. Please be patient.");
			LinkedHashMap<Integer,Integer> top5Freq = Frequency.mostFrequented(stopRegistry, routeRegistry);
			System.out.println("The 5 busiest Stops are:\n");
			// get keys and values converting storing them in arrays
			Integer[] keys = top5Freq.keySet().toArray(Integer[]::new);
			Integer[] values = top5Freq.values().toArray(Integer[]::new);
			for (int i = 0, j=top5Freq.size(); i < top5Freq.size(); i++, j--) {
				System.out.println(j + ". Stop " + stopRegistry.getElement(keys[i]).getName() +
						" is frequented by " + values[i] + " Routes.");
			}
			return false;
			
		} else if (input.toLowerCase().contains("menu")) {
			System.out.println("You have chosen to return to the main menu.");
			return true;
			
		} else {
			System.out.println(Main.didntGetThat);
			try {
				return getUserChoice(scan, stopRegistry, routeRegistry);
			} catch (NullPointerException e) {
				System.out.println("There seems to be a problem with your choice:\n");
				System.out.println(e);
				return true;
			}
		}
	}

}
