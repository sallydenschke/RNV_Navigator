package exceptions;

public class NoRegistryException extends NullPointerException {

	private static final long serialVersionUID = -4282896509557739058L;

	public NoRegistryException(String type) {
		super("Sorry, we have trouble finding the "+type+"Registry.");
	}
	
}
