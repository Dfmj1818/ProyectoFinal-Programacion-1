package co.edu.uptc.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;
import co.edu.uptc.exceptions.InvalidInputException;
public class ContractManager {
	private List<String>contractsTypesList;

	public ContractManager() {
        contractsTypesList = new ArrayList<String>();
        addContractTypesToList();
	}


	public Contract createContract(String contractType,LocalDate contractStartDate,LocalDate contractEndDate) {
		Contract contract = new Contract();
		contract.setContractType(contractType);
		contract.setStartDate(contractStartDate);
		contract.setEndDate(contractEndDate);
		return contract;
	}
    
	public void assignContractToApplicant(Applicant applicant,Contract contract){
		applicant.setApplicantContract(contract);
	}
 	

	public int verifyDigitedContract(int digitedContract) {
		if(digitedContract>0&&digitedContract<=7){
			return digitedContract;
		}
		else {
			throw new InvalidInputException("El Contrato digitado no esta entre los rangos permitidos");
		}
	}




	public void addContractTypesToList() {
		contractsTypesList.add("Obra y labor");
		contractsTypesList.add("Termino Fijo");                 
		contractsTypesList.add("Aprendizaje");    
		contractsTypesList.add("Termino Indefinido");
		contractsTypesList.add("Pensionado");
		contractsTypesList.add("Comerciante");
	}
	
	public String getContractByType(int contractType) {
		String applicantSelectedContract = contractsTypesList.get(contractType+1);
		return applicantSelectedContract;
	}
	
	
	
	
}
