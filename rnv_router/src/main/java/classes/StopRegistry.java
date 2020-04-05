package classes;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import exceptions.AlreadyInRegistryException;
import exceptions.ElementTypeException;
import exceptions.NoRegistryException;

public class StopRegistry implements IRegistry {

	/* 
	 * This class is essentially a HashMap with added functionality.
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
	private HashMap<Integer,Stop> registry;
	
	
	// Constructor
	public StopRegistry() {
		this.registry = new HashMap<Integer,Stop>();
	}
	
	
	// Iterator
	@Override
	public Iterator<Map.Entry<Integer, Stop>> iterate() {
		Iterator<Map.Entry<Integer, Stop>> iterator = this.registry.entrySet().iterator();
		return iterator;
	}
	
	public int size() {
		return this.registry.size();
	}
	
	
	public Stop getElement(int stopID) {
		Stop thisStop = registry.get(stopID);
		return thisStop;
	}
	
	public Stop getElement(Integer stopID) {
		return registry.get(stopID);
	}

	public HashMap<Integer,Stop> getRegistry(){
		return this.registry;
	}
	
	public void resetRegistry() {
		this.registry = new HashMap<Integer,Stop>();
	}
	
	// add a Stop to the StopRegistry with explicit ID as key
	public void addToRegistry(Integer ID, Stop stop) 
			throws AlreadyInRegistryException,
			NoRegistryException {
		try {
			if (registry.get(ID) != null) {
				throw new AlreadyInRegistryException("Stop");
			} else {
				registry.put(ID, stop);
			}
		} catch (NullPointerException e) {
			throw new NoRegistryException("Stop");
		}
	}
	
	// add a Stop to the StopRegistry
	public void addToRegistry(Stop stop) 
			throws AlreadyInRegistryException,
				NoRegistryException {
		Integer ID = Integer.valueOf(stop.getStopID());
		try {
			addToRegistry(ID, stop);
		} catch (AlreadyInRegistryException | NoRegistryException e) {
			throw e;
		}
	}
	
	
	@Override
	public void addToRegistry(Integer ID, Object object) 
			throws ElementTypeException {
		if (object instanceof Stop) {
			addToRegistry(ID, object);
		} else {
			throw new ElementTypeException(object.getClass().toString(), "Stop");
		}
	}
	
	@Override
	public void addToRegistry(Object object)
		throws ElementTypeException {
		if (object instanceof Stop) {
			addToRegistry(object);
		} else {
			throw new ElementTypeException(object.getClass().toString(), "Stop");
		}
	}
	
	
	public void remove(int ID) {
		this.registry.remove(ID);
	}
	
}
