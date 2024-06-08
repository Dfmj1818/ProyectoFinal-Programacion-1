package co.edu.uptc.model;

import java.time.LocalDate;
import java.util.List;

import org.bson.types.ObjectId;

public class Applicant {

	private String identificationNumber;
	private String name;
	private String lastName;
	private LocalDate birthDate;
	private String phoneNumber;
	private String applicantSex;
	private long montlyPaymentCapacity;
	private long montlySalary;
	private long pendingInstallmentsValue;
	private int numbertOfDependents;
	private Contract contract;
	private Loan loan;


	public Applicant() {
		
	}

	public long getPendingInstallmentsValue() {
		return pendingInstallmentsValue;
	}


	public void setPendingInstallmentsValue(long pendingInstallmentsValue) {
		this.pendingInstallmentsValue = pendingInstallmentsValue;
	}


	public void setNumbertOfDependents(int numbertOfDependents) {
		this.numbertOfDependents = numbertOfDependents;
	}


	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getLastName() {
		return  lastName;
	}


	public void setIdentificationNumber(String identificationNumber){
		this.identificationNumber = identificationNumber;
	}

	public String getIdentificationNumber() {
		return identificationNumber;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}


	public void setApplicantContract(Contract contract) {
		this.contract = contract;				
	}

	public Contract getApplicantContract() {
		return contract;
	}

	public String getApplicantSex() {
		return applicantSex;
	}

	public void setApplicantSex(String applicantSex) {
		this.applicantSex = applicantSex;
	}



	public long getMontlyPaymentCapacity() {
		return montlyPaymentCapacity;
	}


	public void setMontlyPaymentCapacity(long montlyPaymentCapacity) {
		this.montlyPaymentCapacity = montlyPaymentCapacity;
	}


	public long getMontlySalary() {
		return montlySalary;
	}


	public void setMontlySalary(long montlySalary) {
		this.montlySalary = montlySalary;
	}


	public int getNumbertOfDependents() {
		return numbertOfDependents;
	}


	public void setNumberOfDependents(int numbertOfDependents) {
		this.numbertOfDependents = numbertOfDependents;
	}


	public Contract getContract() {
		return contract;
	}


	public void setContract(Contract contract) {
		this.contract = contract;
	}

	public Loan getLoan() {
		return loan;
	}

	public void setLoan(Loan loan) {
		this.loan = loan;
	}

}
