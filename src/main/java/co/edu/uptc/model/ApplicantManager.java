package co.edu.uptc.model;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;
import org.bson.types.ObjectId;
import co.edu.uptc.exceptions.AgeOutsideAllowedRangesException;
import co.edu.uptc.exceptions.InvalidInputException;

/**
 * La clase ApplicantManager se encarga de gestionar los solicitantes y sus datos.
 */
public class ApplicantManager {
    private List<Applicant> applicantsList;
    private List<String> genderOptions;

    /**
     * Constructor de la clase ApplicantManager.
     * Inicializa las listas de solicitantes y opciones de género.
     */
    public ApplicantManager() {
        applicantsList = new ArrayList<Applicant>();
        genderOptions = new ArrayList<String>();
        addGenderOptions();
    }

    /**
     * Obtiene el sexo del solicitante según la opción seleccionada.
     * 
     * @param applicantSex el índice de la opción de sexo seleccionada.
     * @return el sexo del solicitante como cadena.
     */
    public String getApplicantSex(int applicantSex) {
        String applicantChoosedSex = genderOptions.get(applicantSex - 1);
        return applicantChoosedSex;
    }

    /**
     * Asigna un préstamo a un solicitante.
     * 
     * @param applicant el solicitante al que se le asignará el préstamo.
     * @param loan el préstamo a asignar.
     */
    public void assignApplicantLoan(Applicant applicant, Loan loan) {
        applicant.setLoan(loan);
    }

    /**
     * Asigna información básica a un solicitante.
     * 
     * @param applicantSex el sexo del solicitante.
     * @param ApplicantName el nombre del solicitante.
     * @param ApplicantLastName el apellido del solicitante.
     * @param identificationNumber el número de identificación del solicitante.
     * @param applicantPhoneNumber el número de teléfono del solicitante.
     * @param applicantBirthDate la fecha de nacimiento del solicitante.
     * @return el solicitante con la información básica asignada.
     */
    public Applicant assignApplicantBasicInformation(String applicantSex, String ApplicantName, String ApplicantLastName, String identificationNumber, String applicantPhoneNumber, LocalDate applicantBirthDate) {
        Applicant applicantWithBasicInformation = new Applicant();
        applicantWithBasicInformation.setApplicantSex(applicantSex);
        applicantWithBasicInformation.setName(ApplicantName);
        applicantWithBasicInformation.setLastName(ApplicantLastName);
        applicantWithBasicInformation.setIdentificationNumber(identificationNumber);
        applicantWithBasicInformation.setPhoneNumber(applicantPhoneNumber);
        applicantWithBasicInformation.setBirthDate(applicantBirthDate);
        return applicantWithBasicInformation;
    }

    /**
     * Asigna toda la información a un solicitante.
     * 
     * @param contract el contrato del solicitante.
     * @param applicantSex el sexo del solicitante.
     * @param ApplicantName el nombre del solicitante.
     * @param ApplicantLastName el apellido del solicitante.
     * @param identificationNumber el número de identificación del solicitante.
     * @param applicantPhoneNumber el número de teléfono del solicitante.
     * @param applicantBirthDate la fecha de nacimiento del solicitante.
     * @param montlyPaymentCapacity la capacidad de pago mensual del solicitante.
     * @param montlySalary el salario mensual del solicitante.
     * @param numberOfDependents el número de dependientes del solicitante.
     * @return el solicitante con toda la información asignada.
     */
    public Applicant assignApplicantWithAllInformation(Contract contract, String applicantSex, String ApplicantName, String ApplicantLastName, String identificationNumber, String applicantPhoneNumber, LocalDate applicantBirthDate, long montlyPaymentCapacity, long montlySalary, int numberOfDependents) {
        Applicant applicantWithAllInformation = new Applicant();
        applicantWithAllInformation.setApplicantSex(applicantSex);
        applicantWithAllInformation.setName(ApplicantName);
        applicantWithAllInformation.setLastName(ApplicantLastName);
        applicantWithAllInformation.setIdentificationNumber(identificationNumber);
        applicantWithAllInformation.setPhoneNumber(applicantPhoneNumber);
        applicantWithAllInformation.setBirthDate(applicantBirthDate);
        applicantWithAllInformation.setContract(contract);
        applicantWithAllInformation.setMontlySalary(montlySalary);
        applicantWithAllInformation.setMontlyPaymentCapacity(montlyPaymentCapacity);
        applicantWithAllInformation.setNumberOfDependents(numberOfDependents);
        return applicantWithAllInformation;
    }

    /**
     * Convierte un solicitante con datos básicos a un documento BSON.
     * 
     * @param applicantWithBasicData el solicitante con datos básicos.
     * @return el documento BSON con la información del solicitante.
     */
    public Document convertApplicantWithBasicDataToDocument(Applicant applicantWithBasicData) {
        Document applicantDocumentWithBasicData = new Document("identificationNumber", applicantWithBasicData.getIdentificationNumber())
                .append("sex", applicantWithBasicData.getApplicantSex())
                .append("name", applicantWithBasicData.getName())
                .append("lastName", applicantWithBasicData.getLastName())
                .append("phoneNumber", applicantWithBasicData.getPhoneNumber())
                .append("birthDate", applicantWithBasicData.getBirthDate().toString()); // Almacenar la fecha como cadena
        return applicantDocumentWithBasicData;
    }

    /**
     * Añade información socioeconómica a un solicitante.
     * 
     * @param applicant el solicitante al que se le añadirá la información.
     * @param montlySalary el salario mensual del solicitante.
     * @param montlyPaymentCapacity la capacidad de pago mensual del solicitante.
     * @param numberOfDependents el número de dependientes del solicitante.
     * @param pendingInstallmentsValue el valor de las cuotas pendientes.
     * @return el solicitante con la información socioeconómica añadida.
     */
    public Applicant addSocioEconomicInfoToApplicant(Applicant applicant, long montlySalary, long montlyPaymentCapacity, int numberOfDependents, long pendingInstallmentsValue) {
        applicant.setMontlySalary(montlySalary);
        applicant.setMontlyPaymentCapacity(montlyPaymentCapacity);
        applicant.setNumberOfDependents(numberOfDependents);
        applicant.setPendingInstallmentsValue(pendingInstallmentsValue);
        return applicant;
    }

    /**
     * Añade un contrato a un solicitante.
     * 
     * @param applicant el solicitante al que se le añadirá el contrato.
     * @param contract el contrato a añadir.
     */
    public void addContractToApplicant(Applicant applicant, Contract contract) {
        applicant.setApplicantContract(contract);
    }

    /**
     * Convierte la información socioeconómica de un solicitante a un documento BSON.
     * 
     * @param applicant el solicitante cuya información será convertida.
     * @return el documento BSON con la información socioeconómica.
     */
    public Document addSocioEconomicInfoToDocument(Applicant applicant) {
        Contract applicantContract = applicant.getContract();
        Document socioEconomicInfo = new Document();
        socioEconomicInfo.append("pendingInstallmentsValue", applicant.getPendingInstallmentsValue());
        socioEconomicInfo.append("montlySalary", applicant.getMontlySalary());
        socioEconomicInfo.append("montlyPaymenCapacity", applicant.getMontlyPaymentCapacity());
        socioEconomicInfo.append("numberOfDependents", applicant.getNumbertOfDependents());
        socioEconomicInfo.append("contract", (applicantContract.convertContractToDocument(applicantContract)));
        return socioEconomicInfo;
    }

    /**
     * Convierte un documento BSON a un solicitante con información básica.
     * 
     * @param applicantAsDocument el documento BSON del solicitante.
     * @return el solicitante con información básica.
     */
    public Applicant convertFromDocumentToApplicantWithBasicInformation(Document applicantAsDocument) {
        Applicant applicantObjectWithBasicInformation = null;
        String identificationNumber = applicantAsDocument.getString("identificationNumber");
        String applicantSex = applicantAsDocument.getString("sex");
        String applicantName = applicantAsDocument.getString("name");
        String applicantLastName = applicantAsDocument.getString("lastName");
        LocalDate birthDate = LocalDate.parse(applicantAsDocument.getString("birthDate"));
        String applicantPhoneNumber = applicantAsDocument.getString("phoneNumber");
        applicantObjectWithBasicInformation = assignApplicantBasicInformation(applicantSex, applicantName, applicantLastName, identificationNumber, applicantPhoneNumber, birthDate);
        return applicantObjectWithBasicInformation;
    }

    /**
     * Convierte un documento BSON a un solicitante con toda la información.
     * 
     * @param applicantAsDocument el documento BSON del solicitante.
     * @return el solicitante con toda la información.
     */
    public Applicant convertFromDocumentToApplicantWithAllData(Document applicantAsDocument) {
        Applicant applicantWithAllData = null;
        String identificationNumber = applicantAsDocument.getString("identificationNumber");
        String applicantSex = applicantAsDocument.getString("sex");
        String applicantName = applicantAsDocument.getString("name");
        String applicantLastName = applicantAsDocument.getString("lastName");
        LocalDate birthDate = LocalDate.parse(applicantAsDocument.getString("birthDate"));
        String applicantPhoneNumber = applicantAsDocument.getString("phoneNumber");
        Object contractAsObject = applicantAsDocument.get("contract");
        Contract contract = convertToContract(contractAsObject);
        int numberOfDependents = applicantAsDocument.getInteger("numberOfDependents");
        long applicantMontlyPaymentCapacity = applicantAsDocument.getLong("montlyPaymenCapacity");
        long applicantMontlySalary = applicantAsDocument.getLong("montlySalary");
        applicantWithAllData = assignApplicantWithAllInformation(contract, applicantSex, applicantName, applicantLastName, identificationNumber, applicantPhoneNumber, birthDate, applicantMontlyPaymentCapacity, applicantMontlySalary, numberOfDependents);
        return applicantWithAllData;
    }

    /**
     * Convierte un objeto a un contrato.
     * 
     * @param contractObject el objeto a convertir.
     * @return el contrato convertido.
     * @throws IllegalArgumentException si el objeto no es un documento BSON.
     */
    public Contract convertToContract(Object contractObject) {
        if (contractObject instanceof Document) {
            Document contractDocument = (Document) contractObject;
            // Extraer los campos del documento
            String contractType = contractDocument.getString("contracType");
            LocalDate startDate = LocalDate.parse(contractDocument.getString("startDate"));
            LocalDate endDate = LocalDate.parse(contractDocument.getString("endDate"));
            // Crear un nuevo objeto Contract y asignar los valores extraídos
            Contract contract = new Contract();
            contract.setContractType(contractType);
            contract.setStartDate(startDate);
            contract.setEndDate(endDate);
            return contract;
        } else {
            throw new IllegalArgumentException("El objeto no es un documento BSON");
        }
    }

    /**
     * Verifica si la opción digitada es válida.
     * 
     * @param digitedOption la opción digitada.
     * @return la opción si es válida.
     * @throws InvalidInputException si la opción no es válida.
     */
    public int verifyDigitedOption(int digitedOption) {
        if (digitedOption == 1 || digitedOption == 2) {
            return digitedOption;
        } else {
            throw new InvalidInputException("Opcion no valida,vuelva a intentarlo ");
        }
    }

    /**
     * Verifica si la fecha de nacimiento del solicitante es válida.
     * 
     * @param applicantBirthDate la fecha de nacimiento del solicitante.
     * @return la fecha de nacimiento si es válida.
     * @throws AgeOutsideAllowedRangesException si la edad no está en el rango permitido.
     */
    public LocalDate verifyApplicantBirthDate(LocalDate applicantBirthDate) {
        Period period = Period.between(applicantBirthDate, LocalDate.now());
        int applicantAge = period.getYears();
        if (applicantAge >= 18 && applicantAge <= 65) {
            return applicantBirthDate;
        } else {
            throw new AgeOutsideAllowedRangesException();
        }
    }

    /**
     * Añade las opciones de género a la lista.
     */
    public void addGenderOptions() {
        genderOptions.add("Masculino");
        genderOptions.add("Femenino");
        genderOptions.add("Prefiero no decirlo");
    }

    /**
     * Obtiene la lista de solicitantes como una cadena.
     * 
     * @return la lista de solicitantes en formato de cadena.
     */
    public String getListAsString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Applicant applicant : applicantsList) {
            stringBuilder.append(applicant.toString()).append("\n");
        }
        return stringBuilder.toString();
    }
}
