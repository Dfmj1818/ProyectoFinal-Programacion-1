package co.edu.uptc.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import co.edu.uptc.exceptions.InvalidInputException;

public class DataValidator {

	private String message;
	private String digitedNumberAsString;
	private String verifiedString;

	public DataValidator() {
		message = " ";
		digitedNumberAsString = " ";
		verifiedString = " ";
	}

	public String verifyDigitedString(String digitedString) {
		verifiedString = digitedString.trim();
		String regex = "^[a-zA-Z0-9]+$";
		if(!digitedString.isEmpty()&&digitedString.matches(regex)){
			return verifiedString;	
		}
		else {
			throw new InvalidInputException("La cadena ingresada esta vacia o esta en un formato incorrecto");
		}

	}


	public String verifyLicense(String license) {
		String licenseRegex = "^(10|40)[0-9]{8}$";
		if(license.matches(licenseRegex)){
			return license;
		}
		else {
			throw new InvalidInputException("Cedula de ciudadania con formato incorrecto");
		}
	}

	public String verifyPhoneNumber(String digitedPhoneNumber){
		String phoneNumberRegex = "^3{1}[0-9]{2}\\s?[0-9]{3}\\s?[0-9]{4}$";
		if(digitedPhoneNumber.matches(phoneNumberRegex)){
			return verifiedString;
		}
		else {
			throw new InvalidInputException("Numero de telefono con formato incorrecto");
		}
	}

	public String verifyMail(String digitedMail){
		String mailRegex ="^[a-zA-Z0-9.-]+(@hotmail\\.com|@gmail\\.com|@uptc\\.edu\\.co){1}$";
		verifiedString = verifyDigitedString(digitedMail);
		if(verifiedString.matches(mailRegex)){
			return verifiedString;
		}
		else {
			throw new InvalidInputException("Correo electronico con el formato incorrecto");
		}

	}

	public LocalDate formatBirthDate(String inputDate)throws DateTimeParseException {
		LocalDate digitedBirthDate = null;
		DateTimeFormatter birthDateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		digitedBirthDate = LocalDate.parse(inputDate,birthDateFormat);
		return digitedBirthDate;
	}





}
