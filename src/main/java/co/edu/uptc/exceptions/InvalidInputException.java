package co.edu.uptc.exceptions;

public class InvalidInputException extends RuntimeException {
	private String message;
	
	public InvalidInputException (String message) {
		super(message);
	}
}
