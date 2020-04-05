package classes;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import exceptions.AlreadyInRegistryException;
import exceptions.ElementTypeException;
import exceptions.NoRegistryException;

public class RouteRegistry implements IRegistry {

	/* This class is essentially a HashMap with added functionality.
	 * It's a design decision to implement this with the HashMap
	 * as private class attribute instead of extending HashMap.
	 * This avoids potential trouble that may come from extending a HashMap.
	 * But most importantly, this way we can have several forms of registers,
	 * without running into the problem that a class can extend only one
	 * other class. This way we can have registers that use HashMap, TreeMap
	 * or other structures for storage without having to define new classes
	 * for them.
	 * Additionally, this helps to allow the user access only trough methods as interfaces.
	 */
	private HashMap<Integer, Route> registry;
	
	// Constructor
	public RouteRegistry() {
		this.registry = new HashMap<Integer,Route>();
	}
	
	
	// Iterator
	@Override
	public Iterator<Map.Entry<Integer, Route>> iterate() {
		Iterator<Map.Entry<Integer, Route>> iterator = this.registry.entrySet().iterator();
		return iterator;
	}
	
	public int size() {
		return this.registry.size();
	}
	
	
	public Route getElement(int routeID) {
		Route thisRoute = registry.get(routeID);
		return thisRoute;
	}

	public Route getElement(Integer routeID) {
		return registry.get(routeID);
	}
	
	public HashMap<Integer,Route> getRegistry(){
		return this.registry;
	}
	
	public void resetRegistry() {
		this.registry = new HashMap<Integer,Route>();
	}
	
	// add a Route to the RouteRegistry with explicit ID as key
	public void addToRegistry(Integer ID, Route route) 
			throws AlreadyInRegistryException,
				NoRegistryException {
		try {
			if(registry.get(ID)!=null) {
				throw new AlreadyInRegistryException("Route");
			} else {
				registry.put(ID, route);
			}
		} catch (NullPointerException e) {
			throw new NoRegistryException("Route");
		}
	}

	// add a Route to the RouteRegistry
	public void addToRegistry(Route route) 
			throws AlreadyInRegistryException,
				NoRegistryException {
		Integer ID = Integer.valueOf(route.getRouteID());
		try {
			addToRegistry(ID, route);
		} catch (AlreadyInRegistryException | NoRegistryException e) {
			throw e;
		}
	}


	@Override
	public void addToRegistry(Integer ID, Object object) 
			throws ElementTypeException {
		if (object instanceof Route) {
			addToRegistry(ID, object);
		} else {
			throw new ElementTypeException(object.getClass().toString(), "Route");
		}
	}

	@Override
	public void addToRegistry(Object object) 
			throws ElementTypeException {
		if (object instanceof Route) {
			addToRegistry(object);
		} else {
			throw new ElementTypeException(object.getClass().toString(), "Route");
		}
	}

	
	public void remove(int ID) {
		this.registry.remove(ID);
	}

}
