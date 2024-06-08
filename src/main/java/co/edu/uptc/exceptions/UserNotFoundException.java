package co.edu.uptc.exceptions;

public class UserNotFoundException extends RuntimeException{

	public UserNotFoundException() {
		super("No hay Un Usuario asociado al documento ingresado");
	}
}
