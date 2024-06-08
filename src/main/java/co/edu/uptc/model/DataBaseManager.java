package co.edu.uptc.model;
import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;

import co.edu.uptc.exceptions.UserNotFoundException;

public class DataBaseManager {
	private MongoClient mongoClient;
	private MongoDatabase colBankDatabase;
	private MongoCollection<Document> applicantsCollection;
	private String message;

	public DataBaseManager() {
		initDataBaseConnection();
	}

	public void initDataBaseConnection() {
		String databaseName = "ColBankDB";
		ConnectionString databaseDirection = new ConnectionString("mongodb://localhost:27017/" + databaseName);
		//Le indico a donde debe apuntar para la construccion de la bd
		MongoClientSettings mongoClientConfig = MongoClientSettings.builder()
				.applyConnectionString(databaseDirection)
				.build();
		mongoClient = MongoClients.create(mongoClientConfig);
		colBankDatabase = mongoClient.getDatabase(databaseDirection.getDatabase());
		applicantsCollection = colBankDatabase.getCollection("Applicants");
	}


	// Método para cerrar la conexión
	public void close() {
		if (mongoClient != null) {
			mongoClient.close();
		}
	}
	
	public void addLoanToApplicant(String applicantIdentificationNumber, Loan loan) {
		Document conditionToSearch = new Document("identificationNumber", applicantIdentificationNumber);
		Document loanDocument = loan.toDocument();
		Document updateOperation = new Document("$set", new Document("loan", loanDocument));

		applicantsCollection.updateOne(conditionToSearch, updateOperation);
	}



	public String saveInDb(Document elementToSave) {
		try {
			applicantsCollection.insertOne(elementToSave);
			message = "Se ha Registrado Correctamente Su Informacion en Nuestro Sistema";
		} catch (Exception e) {
			System.out.println("Error en guardado: " + e.getMessage());
		}
		return message;
	}

	public void addSocioEconomicInfoToDocument(Applicant applicant,Document socioEconomicInfo) {
		Document conditionToSearch = new Document("identificationNumber",applicant.getIdentificationNumber());
		Document updateOperation = new Document("$set", socioEconomicInfo);
		applicantsCollection.updateOne(conditionToSearch, updateOperation);
	}

	
	public Document getApplicantByIdentificationNumber(String identificationNumber) {
		Document conditionToSearch = new Document("identificationNumber", identificationNumber);
		Document foundApplicantAsDocument = applicantsCollection.find(conditionToSearch).first();
		if(foundApplicantAsDocument!=null){
			return foundApplicantAsDocument;
		}
		else {
			return null;
		}
	}

	public String deleteByIdentificationNumber(String identificationNumber) {
		Bson conditionToDelete = Filters.eq("identificationNumber", identificationNumber);
		DeleteResult numberOfDeletedDocuments = applicantsCollection.deleteOne(conditionToDelete);
		if(numberOfDeletedDocuments.getDeletedCount()>0){
			message = "Aplicante eliminado Satisfactoriamente";
			return message;
		}
		else {
			throw new UserNotFoundException();
		}
	}

}
