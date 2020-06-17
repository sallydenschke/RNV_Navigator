package classes;

import java.util.Iterator;

import exceptions.ElementTypeException;

public interface IRegistry {

	public void addToRegistry(Integer ID, Object object) throws ElementTypeException;
	
	public void addToRegistry(Object object) throws ElementTypeException;
	
	public Object getElement(Integer ID);
	
	public Object getElement(int ID);
	
	// note: use case for Generics!
	// may also use an Interface for Registry Elements (tagging)
	
	// this Iterator should be a raw type as to allow Iterators
	// for both stops and routes
	@SuppressWarnings("rawtypes")
	public Iterator iterate();
	
	public int size();
	
	public void remove(int ID);
}
