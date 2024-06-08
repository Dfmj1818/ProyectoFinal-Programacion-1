package co.edu.uptc.model;

import java.time.LocalDate;

import org.bson.Document;

public class Contract {
	private String contractType;
	private LocalDate startDate;
	private LocalDate endDate;
	
    public Contract() {
    	
    }

    public Document convertContractToDocument(Contract contract) {
		Document contractAsDocument  = new Document();
		contractAsDocument.append("contracType", contract.getContractType());
		contractAsDocument.append("startDate", contract.getStartDate().toString());
		contractAsDocument.append("endDate", contract.getEndDate().toString());
		return contractAsDocument;
	}

    public void setStartDate(LocalDate startDate) {
    	this.startDate = startDate;
    }

    public LocalDate getStartDate() {
    	return startDate;
    }
    
    public void setEndDate(LocalDate endDate) {
    	this.endDate = endDate;
    }
    
    public LocalDate getEndDate() {
    	return endDate;
    }

    public void setContractType(String contractType) {
		this.contractType = contractType;
	}

	public String getContractType() {
		return contractType;
	}


	
}
