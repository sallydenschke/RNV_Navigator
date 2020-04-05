package exceptions;

public class NoStopsDataException extends NullPointerException {

	private static final long serialVersionUID = 4296472573179934175L;

	// we don't add the original error message here, as it's of no
	// interest to the user
	public NoStopsDataException () {
		super("\nSorry, you don't seem to have set the stops yet.\n Please try again with the stops.\n");
	}
	
}
