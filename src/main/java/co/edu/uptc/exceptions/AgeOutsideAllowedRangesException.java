package co.edu.uptc.exceptions;

public class AgeOutsideAllowedRangesException extends RuntimeException{

	public AgeOutsideAllowedRangesException() {
		super("Debe Tener entre 18 y 65 años,para poder solictar un prestamo con nosotros");
	}
}
