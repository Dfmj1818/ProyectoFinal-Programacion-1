package co.edu.uptc.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.edu.uptc.exceptions.ApplicantNotSuitableForAnylLoanException;

public class LoanManager {
    private final static Map<Integer, Double> interestRatesAndInstallments = new HashMap<>();

    private final long colBankMinimumLoan = 5000000;
    
    public LoanManager() {
		loadLoansRules();
	} 
    
    public void loadLoansRules() {
    	interestRatesAndInstallments.put(12, 2.50);
    	interestRatesAndInstallments.put(24, 2.45);
    	interestRatesAndInstallments.put(48, 2.40);
    	interestRatesAndInstallments.put(60, 2.35);
    }
    
    /*Si es menos de 5.000.000 se remueve de la lista de opciones 
     * 
     */
    
    public void validateLoansList(List<Loan> loansList){
    	loansList.removeIf(loan -> loan.getLoanAmount() < colBankMinimumLoan);
    }

	public long calculateMontlyEconomicCapacity(Applicant applicant) {
		long montlyEconomicCapacity = 0;
		long dependentsExpenses = 800000*applicant.getNumbertOfDependents();
		montlyEconomicCapacity = applicant.getMontlySalary() - applicant.getPendingInstallmentsValue() -dependentsExpenses;

		return montlyEconomicCapacity;
	}
	
	public long calculateMonthlyInstallment(long loanAmount, long totalInstallments) {
		long monthlyInstallment = loanAmount / totalInstallments;
		System.out.println("*** monthlyInstallment: " + monthlyInstallment);
		return monthlyInstallment;
	}
	
	/*Formula amaortizacion francesa
	 * 
	 */
		
    public long calculateLoanAmount(double monthlyPaymentCapacity, double monthlyInterestRate, int maxInstallments) {
        monthlyInterestRate = monthlyInterestRate / 100; // Convertir la tasa de interés a decimal
        double numerator = monthlyPaymentCapacity * (Math.pow(1 + monthlyInterestRate, maxInstallments) - 1);
        double denominator = monthlyInterestRate * Math.pow(1 + monthlyInterestRate, maxInstallments);
        double loanAmount = numerator / denominator;

        return Math.round(loanAmount); // Convertir a long redondeando
    }

	public List<Loan> calculateCreditAmountsForInstallments(Applicant applicant) {
		List<Loan> offertLoans = new ArrayList<Loan>();

        for (Map.Entry<Integer, Double> entry : interestRatesAndInstallments.entrySet()) {
            int totalInstallments = entry.getKey();
            double interestRate = entry.getValue();
            long loanAmount = calculateLoanAmount(applicant.getMontlyPaymentCapacity(), interestRate, totalInstallments);
            
            Loan loan = new Loan();
            loan.setInterestRate(interestRate);
            loan.setLoanAmount(loanAmount);
            loan.setInstallmentNumber(totalInstallments);
            long monthlyInstallment = calculateMonthlyInstallment(loanAmount, totalInstallments);
            loan.setMonthlyInstallment(monthlyInstallment);
            
            offertLoans.add(loan);
        }
        
        sortList(offertLoans);
        return offertLoans;
    }
	
	public void sortList(List<Loan> offertLoans) {
		Collections.sort(offertLoans, new Comparator<Loan>() {
	            @Override
	            public int compare(Loan l1, Loan l2) {
	                return Integer.compare(l1.getInstallmentNumber(), l2.getInstallmentNumber());
	            }
	        });
	}
	
	public List<Loan> verifyLoansOffertsNotNull(List<Loan>contractOffers) {
		if(!contractOffers.isEmpty()){
			return contractOffers;
		}
		else {
			throw new ApplicantNotSuitableForAnylLoanException();
		}
	}
	
	
	
	public String showLoanOffertsList(List<Loan>loanOfferts) {
		StringBuilder loanOffertsAsString = new StringBuilder();
		int counter = 0;
		
		loanOffertsAsString.append("Te ofrecemos las siguientes opciones de credito:").append("Elige la Opcion que desees").append("\n");
		for(Loan loan:loanOfferts){
			counter++;
			loanOffertsAsString.append("Opcion ").append(counter).append(" ").append("\n").append(loan.toString()).append("\n").append("-----------------------").append("\n");
		}
		return loanOffertsAsString.toString();
	}
	
	
	

	public Loan chooseLoan(List<Loan>loanOfferts ,int digitedLoanId) {
       Loan choosedLoan = loanOfferts.get(digitedLoanId-1);
       return choosedLoan;
	}

}
