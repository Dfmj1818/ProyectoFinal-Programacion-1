package co.edu.uptc.model;

import org.bson.Document;

public class Loan {

	private long loanAmount;
	private double interestRate;
	private long monthlyInstallment;
	private Integer installmentNumber;

	public Loan() {
		loanAmount = 0 ;
		interestRate = 0;
		monthlyInstallment = 0;
		installmentNumber = 0;
	}

	public Integer getInstallmentNumber() {
		return installmentNumber;
	}

	public void setInstallmentNumber(Integer installmentNumber) {
		this.installmentNumber = installmentNumber;
	}



	public long getLoanAmount() {
		return loanAmount;
	}

	public void setLoanAmount(long loanAmount) {
		this.loanAmount = loanAmount;
	}

	public double getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(double interestRate) {
		this.interestRate = interestRate;
	}

	public long getMonthlyInstallment() {
		return monthlyInstallment;
	}

	public void setMonthlyInstallment(long monthlyInstallment) {
		this.monthlyInstallment = monthlyInstallment;
	}
	
	 public Document toDocument() {
	        return new Document("interestRate", this.interestRate)
	                .append("monthlyInstallment", this.monthlyInstallment)
	                .append("loanAmount", this.loanAmount)
	                .append("installmentNumber", this.installmentNumber);
	    }
	
	@Override
	public String toString() {
		StringBuilder loanInformation = new StringBuilder();
		loanInformation.append("Monto Del Prestamo: ").append(loanAmount).append("\n")
		.append("Taza de Interes Mensual del prestamo: ").append(getInterestRate()).append("\n")
		.append("Cuota mensual: ").append(getMonthlyInstallment()).append("\n")
		.append("Numero de cuotas: ").append(getInstallmentNumber());
		return loanInformation.toString();
	}
}
