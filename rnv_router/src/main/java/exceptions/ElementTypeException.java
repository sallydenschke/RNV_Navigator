package exceptions;

public class ElementTypeException extends Throwable {

	private static final long serialVersionUID = 5129834540038318514L;

	public ElementTypeException (String provided, String needed) {
		super("Your Object of class "+provided+" is not a "+needed+" and thus does not belong in the "+provided+"Registry.\n"
					+ "Please make sure you're adding your object to the correct registry.");
	}
	
}
