package co.edu.uptc.model;

import java.util.ArrayList;
import java.util.List;

public class User extends Applicant{
	
	private List<Loan>loanHistory;
	private Loan loan;
	
	public User() {
		loanHistory = new ArrayList<Loan>();
		loan = new Loan();
	}

	public List<Loan> getLoanHistory() {
		return loanHistory;
	}

	public void setLoanHistory(List<Loan>loanHistory) {
		this.loanHistory = loanHistory;
	}

	public Loan getLoan() {
		return loan;
	}

	public void setLoan(Loan loan) {
		this.loan = loan;
	}






}
