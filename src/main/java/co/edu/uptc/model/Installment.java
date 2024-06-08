package co.edu.uptc.model;

import java.time.LocalDate;

public class Installment {

	private int installmentId;
	private long installmentValue;
	private LocalDate startDate;
	private LocalDate dueDate;
	private LocalDate maxDueDate;
	
	public Installment() {
		
	}

	public int getInstallmentId() {
		return installmentId;
	}

	public void setInstallmentId(int installmentId) {
		this.installmentId = installmentId;
	}

	public long getInstallmentValue() {
		return installmentValue;
	}

	public void setInstallmentValue(long installmentValue) {
		this.installmentValue = installmentValue;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getDueDate() {
		return dueDate;
	}

	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}

	public LocalDate getMaxDueDate() {
		return maxDueDate;
	}

	public void setMaxDueDate(LocalDate maxDueDate) {
		this.maxDueDate = maxDueDate;
	}
	

}
