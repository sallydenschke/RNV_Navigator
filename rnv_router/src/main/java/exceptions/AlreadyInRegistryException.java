package exceptions;

public class AlreadyInRegistryException extends Throwable {

	private static final long serialVersionUID = -1225388616714439770L;

	public AlreadyInRegistryException(String element) {
		super("This "+element+" is in the registry already.");
	}
	
}
