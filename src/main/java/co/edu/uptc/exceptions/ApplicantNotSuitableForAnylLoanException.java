package co.edu.uptc.exceptions;

public class ApplicantNotSuitableForAnylLoanException extends RuntimeException{

	public ApplicantNotSuitableForAnylLoanException() {
		super("Los sentimos,pero no aplicas para ninguno de los tipo de prestamos que ofrecemos");
	}
}
