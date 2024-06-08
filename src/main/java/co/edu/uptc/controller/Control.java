package co.edu.uptc.controller;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.bson.types.ObjectId;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import co.edu.uptc.exceptions.AgeOutsideAllowedRangesException;
import co.edu.uptc.exceptions.ApplicantNotSuitableForAnylLoanException;
import co.edu.uptc.exceptions.InvalidInputException;
import co.edu.uptc.model.Applicant;
import co.edu.uptc.model.ApplicantManager;
import co.edu.uptc.model.Contract;
import co.edu.uptc.model.ContractManager;
import co.edu.uptc.model.DataBaseManager;
import co.edu.uptc.model.DataValidator;
import co.edu.uptc.model.Loan;
import co.edu.uptc.model.LoanManager;
import co.edu.uptc.view.IoManager;

public class Control {
	private IoManager ioManager;
	private ApplicantManager applicantManager;
	private ContractManager contractManager;
	private DataValidator dataValidator;
	private DataBaseManager dataBaseManager;
	private LoanManager loanManager;
	private int digitedOption;


	public Control() {
		ioManager = new IoManager();
		applicantManager = new ApplicantManager();
		dataValidator = new DataValidator();
		digitedOption = 0;	
		contractManager = new ContractManager();
		dataBaseManager = new DataBaseManager();
		loanManager = new LoanManager();	
	}



	public void runMenu(){
		boolean exit = false;

		while(!exit){
			digitedOption = showInputMessage();
			switch(digitedOption){
			case 1:
                
				break;
			case 2:				
				inputBasicDataForm();
				break;
			case 3:
				ioManager.printMessage("Saliendo de la aplicacion");
				exit = true;
				break;

			default:
				ioManager.printMessage("La opcion digitada no esta disponible");
				break;
			}
		}

	}

	/*Metodo que muestra el menu principal para el usuario y retorna la opcion que haya digitado,y con base en esta
	 *determina por camino del switch case deba irse
	 * 
	 */
	public int showInputMessage() {
		ioManager.printMessage("Bienvenido Al Banco ColBank");
		int verifiedInt = verifyDigitedInt("BIENVENIDO AL SISTEMA DE PRESTAMOS\nA Continuacion elige la opcion que desees"
				+ "\n1.Iniciar Sesion(solo para clientes)"
				+ "\n2.Solicitar Prestamo por primera vez"
				+ "\n3.Salir ");
		return verifiedInt;
	}

	/*Metodo que muestra un mensaje intuitivo a los usuarios,el cual consta de la secuencia de pasos
	 * sobre la que trabaja el sistema de prestamos colBank
	 * 
	 */

	public void showLoanPhases() {
		ioManager.printMessage("El Proceso De Aprobacion de Prestamo Consta de 3 Fases"
				+ "\nFase 1:Ingreso de Datos Basicos"
				+ "\nFase 2:Ingreso de De Datos Socioeconomicos"
				+ "\nFase 3.Fase de Evaluacion y Comprobacion de requisitos");
	}

	/*Metodo que muestra al usuario los tipos de contratos bajo los cuales trabaja el sistema de colbank
	 * a la vez,recibe la opcion digitada,bajo la cual se ejecutara el metodo runLogicByContractType
	 */
	public int showContractMenu() {
		int choosedContract = 0;
		choosedContract = ioManager.readInt("Escoge el tipo de Contrato que tengas"
				+ "\n1.Contrato por Obra y labor"
				+ "\n2.Contrato de termino Fijo"
				+ "\n3.Contrato de Aprendizaje"
				+ "\n4.Contrato de trabajo a termino indefinido"
				+ "\n5.Pensionado"
				+ "\n6.Comerciante");
		return choosedContract;
	}


	/*Metodo que chequea en la base de datos de mongoDB si existe un usuario asociado
	 * al numero de identificacion digitado por el usuario,mongo trabaja con documentos en lugar de objetos
	 * por lo tanto se busca a traves del metodo getApplicantByIdentificationNumber,
	 * se evaluan dos condiciones,en caso de que el metodo sea nulo y contenga el atributo montlysalary y este tenga un valor
	 * mayor de 0,significa que ya paso por ambos formularios.
	 * -En Caso de que solo cumpla la condicion de que no sea nulo,se devolvera de igual forma,pero se llega a la conclusion de que 
	 * solo a pasado por el primer formulario.
	 * -En caso De que sea nulo se devolvera de igual forma y se comprobara en el metodo inputBasicDataForm por que parte del flujo se debe ir
	 * 
	 */
	public Applicant checkIfApplicantExistsInDb(String identificationNumber) {
		Applicant foundApplicant = null;
		Document foundApplicantAsDocument = dataBaseManager.getApplicantByIdentificationNumber(identificationNumber);

		if(foundApplicantAsDocument!=null) {
			if(foundApplicantAsDocument.containsKey("montlySalary")&&foundApplicantAsDocument.getLong("montlySalary") > 0){
				foundApplicant = applicantManager.convertFromDocumentToApplicantWithAllData(foundApplicantAsDocument);
				return foundApplicant;
			}
			foundApplicant = applicantManager.convertFromDocumentToApplicantWithBasicInformation(foundApplicantAsDocument);
			return foundApplicant;
		}
		return null;
	}	
	
	public Applicant checkIfClientExistsInDb(String identificationNumber) {
		Applicant foundApplicant = null;
		Document foundApplicantAsDocument = dataBaseManager.getApplicantByIdentificationNumber(identificationNumber);
		if(foundApplicantAsDocument.containsKey("loan")){
			foundApplicant = applicantManager.convertFromDocumentToApplicantWithAllData(foundApplicantAsDocument);
	       return foundApplicant;
		}
		return null;
	}

	/*Metodo inputBasicDataForm
	 * Inicialmente el metodo ejecuta el metodo showLoanPhases,el cual muestra 
	 * todo el orden de ejecucion del programa.
	 *- Luego se le solicita la cedula y se verifica que este en formato correcto
	 * -Luego se llama al metodo checkIfApplicantExistsInDb para comprobar si el usuario ya esta
	 * registrado en la base de datos,qui hay dos condiciones las cuales son(en caso de que el usuario encontrado
	 * no sea nulo y tenga el atributo montlySalary este asignado y sea mayor que 0,significa que el usuario ya paso
	 * por ambos formularios,a lo cual se procedera  a llamar al metodo calculateCreditAmountsForInstallments.
	 * -En caso de que solo cumpla la condicion de no ser nulo,significa que ya paso por el primer fomulario a lo cual
	 * se procedera a llamar al segundo formulario evitando asi que vuelva a llenar cosas que ya habia llenado
	 * 
	 * 
	 */

	public Applicant inputBasicDataForm() {
		int verifiedApplicantSex = 0;
		String verifiedApplicantName = " ";
		String verifiedApplicantLastName= " ";
		String verifiedApplicantLicense = " ";
		String verifiedApplicantPhoneNumber = " ";
		LocalDate verifiedApplicantBirthDate = null;
		Applicant newApplicant = null;
		Applicant applicantRegistered = null;

		showLoanPhases();
		ioManager.printMessage("A continuacion se le mostrara un formulario donde se le solicitaran\n algunos datos personales");
		verifiedApplicantLicense = verifyDigitedLicense();
		applicantRegistered = checkIfApplicantExistsInDb(verifiedApplicantLicense);

		if(applicantRegistered!=null&&applicantRegistered.getMontlySalary()>0){
			List<Loan>loanOfferts = calculateCreditAmountsForInstallments(applicantRegistered);
			loanManager.validateLoansList(loanOfferts);
			chooseOfertLoans(applicantRegistered, loanOfferts);
			return applicantRegistered;
		}

		if(applicantRegistered!=null){
			inputSocioEconomicDataForm(applicantRegistered);
			return applicantRegistered;
		} else {
			verifiedApplicantBirthDate = verifyApplicantBirthDate("Ingresa Tu Fecha de Nacimiento(Debes tener entre 18 y 65 años)");
			verifiedApplicantSex = verifyDigitedOption("Ingrese su sexo\n1.Masculino\n2.Femenino");
			verifiedApplicantName = verifyDigitedString("Digite su primer nombre");
			verifiedApplicantLastName = verifyDigitedString("Digite su Apellido");
			verifiedApplicantPhoneNumber = verifyDigitedPhoneNumber();
			ioManager.printMessage("Primera fase del proceso Exitosa :)");
			newApplicant = basicInformationCheckPoint(verifiedApplicantSex, verifiedApplicantName, verifiedApplicantLastName, verifiedApplicantLicense, verifiedApplicantPhoneNumber, verifiedApplicantSex, verifiedApplicantBirthDate);
			return newApplicant;
		}
	}

	/*Metodo basicInformationCheckPoint
	 * Este metodo es utilizado para realizar un guardadado de la informacion digitada hasta el momento(en este caso solo guarda
	 * la informacion basica del usuario en la base de datos),esto con el fin de que por algun motivo o que el usuario se haya salido de la app
	 * este no tenga que volver a llenar el primer formulario
	 */
	public Applicant basicInformationCheckPoint(int choosedSex,String applicantName,String applicantLastName,String identificationNumber,String applicantPhoneNumber,int contractType,LocalDate applicantBirthDate) {
		Applicant applicantWithBasicInformation = null;
		Document applicantAsDocument = null;
		String applicantSex = " ";

		applicantSex = applicantManager.getApplicantSex(choosedSex);
		applicantWithBasicInformation = applicantManager.assignApplicantBasicInformation(applicantSex,applicantName, applicantLastName, identificationNumber, applicantPhoneNumber,applicantBirthDate);
		applicantAsDocument = convertApplicantWithBasicInfoToDocument(applicantWithBasicInformation);
		saveApplicantInDataBase(applicantAsDocument);
		runLogicByContractType(applicantWithBasicInformation);
		return applicantWithBasicInformation;
	}

	/*Metodo  convertApplicantWithBasicInfoToDocument
	 * Este metodo convierte un aplicante con la informacion basica,en un documento de mongoDB,esto con fines de transformar
	 * la informacion digitada en el formulario basico en algo que la base de datos pueda leer y almacenar
	 */
	public Document convertApplicantWithBasicInfoToDocument(Applicant applicantWithBasicInformation) {
		Document applicantAsDocumennt = null;
		applicantAsDocumennt = applicantManager.convertApplicantWithBasicDataToDocument(applicantWithBasicInformation);
		return applicantAsDocumennt;
	}

	/*Metodo saveApplicantInDataBase
	 * Este metodo Almacena un documento de mongoDB en la base de datos y retorna un mensaje 
	 * para el usuario en caso de que se haya añadido satisfoctariamente
	 */
	public void saveApplicantInDataBase(Document document) {
		String messageToTheUser = " ";
		messageToTheUser = dataBaseManager.saveInDb(document);
		ioManager.printMessage(messageToTheUser);
	}

	/*Metodo runLogicByContractType
	 * Este metodo ejecuta el metodo verifyDigitedContractType,el cual verifica que se digite un contrato dentro de los rangos permitidos
	 * y devuelve el contrato elegido,luego con base en esa opcion se procederan a ejecutar los metodos de asignacion de contrato
	 * el metodo que llama al metodo que ejecuta el segundo formulario,luego ejecuta el metodo assignSocioEconomicInfoToDocument
	 * el cual asigna toda la informacion recibida y seteada el objeto aplicante y se la asigna al documento de mongo.
	 * -Luego llama al metodo calculateCreditAmountsForInstallments,el cual evalua los requisitos socioeconomicos del aplicante
	 * este metodo devuelve una lista la cual contiene los prestamos que el aplicante puede adquirir.
	 * 	-En caso de que el tipo de contrato sea igual o distinto de 4 no se llamara al metodo createContract,ya que estos tipos de 
	 * aplicantes no tienen contrato
	 * 
	 */
	//pendiente logica de contrato a termino indefinido
	public void runLogicByContractType(Applicant applicantWithBasicInfo) {
		int choosedContractType = 0;
		choosedContractType = verifyDigitedContractType();
		Document applicantAsDocument = null;
		Applicant applicantWithSocioEconomicInfo = null;
		List<Loan>loanOfferts = null;;

		if(choosedContractType<4){
			applicantWithSocioEconomicInfo = inputSocioEconomicDataForm(applicantWithBasicInfo);
			Contract applicantContract = createApplicantContract(applicantWithSocioEconomicInfo, choosedContractType);
			assignContractToApplicant(applicantWithSocioEconomicInfo, applicantContract);
			assignSocioEconomicInfoToDocument(applicantWithSocioEconomicInfo);
			loanOfferts = calculateCreditAmountsForInstallments(applicantWithSocioEconomicInfo);
			loanManager.validateLoansList(loanOfferts);
			try {
				loanManager.verifyLoansOffertsNotNull(loanOfferts);
			}catch(ApplicantNotSuitableForAnylLoanException e){
				ioManager.printMessage(e.getMessage());
			}
			chooseOfertLoans(applicantWithSocioEconomicInfo, loanOfferts);
		}
		else {
			applicantWithSocioEconomicInfo = inputSocioEconomicDataForm(applicantWithBasicInfo);
			loanOfferts = calculateCreditAmountsForInstallments(applicantWithSocioEconomicInfo);
			assignSocioEconomicInfoToDocument(applicantWithSocioEconomicInfo);
			loanManager.validateLoansList(loanOfferts);
			chooseOfertLoans(applicantWithSocioEconomicInfo, loanOfferts);
		}
	}

	/*Metodo inputSocioEconomicDataForm
	 * Este metodo ejecuta la logica del segundo formulario(formulario socioeconomico),tambien pregunta
	 * si el usuario tiene cuotas pendientes de otros creditos(y le dice que ej caso de que si tenga que digite el valor mensual que paga de 
	 * otros creditos.
	 * -Luego llama al metodo assignSocioEconomicInfoToApplicant,el cual setea toda la informacion adquirida del segundo formulario y se la asigna
	 * al aplicante,tambien llama al metodo assignPaymentCapacityApplicantData,el cual calcula la capacidad mensual de pago que tiene el aplicante
	 * 
	 */
	public Applicant inputSocioEconomicDataForm(Applicant applicantReceived) {
		int numberOfDependents = 0;
		int hasPendingInstallments = 0;
		long pendingInstallmentValue = 0;
		long applicantMontlySalary = 0;
		Applicant applicantWithBasicInfo = applicantReceived;
		Applicant applicantWithSocioEconomicInfo = null;
		long montlyPaymentCapacity = 0;

		ioManager.printMessage("Segunda Fase del proceso de Autorizacion de Prestamos...");
		applicantMontlySalary = verifyDigitedLong("Digite sus ingresos mensuales");
		hasPendingInstallments = pendingInstallmentsInput();
		if(hasPendingInstallments==1){
			pendingInstallmentValue = ioManager.readLong("Ingrese el Valor de la(s) cuota(s) mensual(es) que pagas");
		}
		numberOfDependents = verifyDigitedInt("Ingrese el numero de personas a cargo que tenga");
		applicantWithSocioEconomicInfo = assignSocioEconomicInfoToApplicant(applicantReceived,applicantMontlySalary,montlyPaymentCapacity,numberOfDependents,pendingInstallmentValue);
		assignPaymentCapacityApplicantData(applicantWithSocioEconomicInfo);
		return applicantWithSocioEconomicInfo;
	}

	/*Metodo calculateCreditAmountsForInstallments
	 * Metodo el cual calcula los tipos de prestamos a los que el usuario puede aplicar,devuelve una lista de prestamos
	 * la cual ya tiene los objetos prestamos con sus respectivos atributos
	 * 
	 */
	public List<Loan>calculateCreditAmountsForInstallments(Applicant applicant) {
		List<Loan> offertLoans = loanManager.calculateCreditAmountsForInstallments(applicant);
		return offertLoans;
	}


	/*Metodo chooseOfertLoans
	 * Metodo el cual imprime la lista de prestamos que el aplicante ,muestra una ventana de input,la cual
	 * para que el aplicante escoga el prestamo que desee ,luego este prestamo se debe asociar al aplicante llamando a un metodo
	 * assginLoanToApplicant.
	 */
	public void chooseOfertLoans(Applicant applicant,List<Loan>offertLoans) {
		if(offertLoans.size() > 0) {
			String loanOffertsAsString = loanManager.showLoanOffertsList(offertLoans);
			int digitedLoanId = ioManager.readInt(loanOffertsAsString);
			Loan choosedLoan = null;
			choosedLoan =loanManager.chooseLoan(offertLoans, digitedLoanId);
			assignLoanToApplicant(applicant,choosedLoan);
			dataBaseManager.addLoanToApplicant(applicant.getIdentificationNumber(), choosedLoan);
			ioManager.printMessage("Haz escogido el prestamo numero: "+digitedLoanId+"\n"+choosedLoan.toString());
		} else {
			ioManager.printMessage("Lo sentimos, ColBank no puede profecerte opciones de crédito");
		}

	}

	public void assignLoanToApplicant(Applicant applicant,Loan loan){
		applicantManager.assignApplicantLoan(applicant, loan);
	}

	/*Metodo assignSocioEconomicInfoToApplicant
	 * metodo el cual asigna la informacion del segundo forumulario al aplicante y retorna el aplicante modificado
	 * 
	 */
	public Applicant assignSocioEconomicInfoToApplicant(Applicant applicant,long montlySalary,long montlyPaymentCapacity,int numberOfDependents, long pendingInstallmentValue) {
		Applicant applicantWithSocioEconomicInfo = applicantManager.addSocioEconomicInfoToApplicant(applicant,montlySalary, montlyPaymentCapacity, numberOfDependents, pendingInstallmentValue);
		return applicantWithSocioEconomicInfo;
	}

	/*Metodo assignSocioEconomicInfoToApplicant
	 * Metodo el cual asigna la ifnromacion del aplicante al documento de mongoDB
	 */

	public void assignSocioEconomicInfoToDocument(Applicant applicant) {
		Document documentWithSocioEconomicInfo = applicantManager.addSocioEconomicInfoToDocument(applicant);
		dataBaseManager.addSocioEconomicInfoToDocument(applicant, documentWithSocioEconomicInfo);
	}

	/*Metodo el cual asigna un contrato al aplicante
	 * 
	 */

	public void assignContractToApplicant(Applicant applicant,Contract contract) {
		applicant.setApplicantContract(contract);
	}

	/*Metodo assignPaymentCapacityApplicantData
	 * Metodo para que calcula y asigna la capacidad mensual de pago del usuario
	 */
	public void assignPaymentCapacityApplicantData(Applicant applicantWithSocioEconomicInfo) {
		long montlyEconomicCapacity = loanManager.calculateMontlyEconomicCapacity(applicantWithSocioEconomicInfo);
		applicantWithSocioEconomicInfo.setMontlyPaymentCapacity(montlyEconomicCapacity);
	}
	/*Metodo createApplicantContract
	 * metodo el cual le solicita al usuario la fecha inicial y final de su contrato
	 */

	public Contract createApplicantContract(Applicant applicant,int choosedContractType) {
		LocalDate contractStartDate = null;
		LocalDate contractEndDate = null;
		String contractType = " ";

		contractStartDate = formatDigitedDate("Ingresa la fecha de inicio de tu contrato");
		contractEndDate = formatDigitedDate("Ingresa la fecha de terminacion de tu contrato");
		contractType = contractManager.getContractByType(choosedContractType);
		Contract createdContract = contractManager.createContract(contractType,contractStartDate, contractEndDate);
		return createdContract;
	}

	/*Metodo pendingInstallmentsInput
	 * Metodo el cual le pregunta al usuario si tiene optras cuotas mensuales pendientes y captura su respuesta
	 */
	public int pendingInstallmentsInput() {
		int hasPendingQuotas = verifyDigitedOption("¿Tienes Otros Creditos Pendientes?\n1.Si\n2.No");
		return hasPendingQuotas;
	}

	/*Metodo verifyDigitedInt
	 * metodo el cual verifica que un dato de tipo int no tenga caracteres extraños,y que el campo que digite no este vacio
	 * en acaso de que este vacio retorna una excepcion y le vuelve a pedir que ingrese el dato,hasta que lo ingrese de forma correcta
	 */
	public int verifyDigitedInt(String messageToShow) {
		boolean exit = false;
		int digitedInt= 0;
		int verifiedInt = 0;

		while(!exit){
			try {
				digitedInt= ioManager.readInt(messageToShow);
				exit = true;
			}catch(InvalidInputException e) {
				ioManager.printMessage(e.getMessage());
			}catch(NumberFormatException e) {
				ioManager.printMessage("La cadena no puede estar vacia y debe ser un numero entero");
			}
		}
		return digitedInt;
	}

	/*Metodo  verifyDigitedLong
	 * 
	 * metodo el cual verifica que un dato de tipo Long no tenga caracteres extraños,y que el campo que digite no este vacio
	 * en acaso de que este vacio retorna una excepcion y le vuelve a pedir que ingrese el dato,hasta que lo ingrese de forma correcta
	 */
	public long verifyDigitedLong(String messageToShow) {
		boolean exit = false;
		long verifiedLong = 0;
		long digitedLong = 0;

		while(!exit) {
			try {
				verifiedLong = ioManager.readLong(messageToShow);
				exit = true;
			}catch(NumberFormatException e) {
				ioManager.printMessage(e.getMessage());
			}
		}
		return verifiedLong;
	}

	/*Metodo verifyDigitedString
	 * metodo el cual verifica que un dato de tipo String no tenga caracteres extraños,y que el campo que digite no este vacio
	 * en acaso de que este vacio retorna una excepcion y le vuelve a pedir que ingrese el dato,hasta que lo ingrese de forma correcta
	 */

	public String verifyDigitedString(String messageToShow) {
		boolean exit = false;
		String verifiedString = " ";
		String digitedString = " ";

		while(!exit) {
			try {
				digitedString = ioManager.readString(messageToShow);
				verifiedString = dataValidator.verifyDigitedString(digitedString);
				exit = true;
			}catch(InvalidInputException e) {
				ioManager.printMessage(e.getMessage());
			}
		}
		return verifiedString;
	}

	/*Metodo formatDigitedDate
	 * Metodo el cual transforma una cadena en un objeto de tipo localDate,en caso de no cumplir
	 * con el dormato dia/mes/año generara una excepcion y volvera a reptir la solicitud del dato hasta 
	 * que lo ingrese de forma correcta,tambien compara la cadena digitada con una expresion regular
	 */
	public LocalDate formatDigitedDate(String messageToShow) {
		boolean exit = false;
		LocalDate formattedDate =  null;
		String digitedDateAsString = " ";

		while(!exit){
			try {
				digitedDateAsString = ioManager.readString(messageToShow+"\nIngresa la fecha en formato:Dia\\Mes\\Año"
						+ "\nEjemplo:12/05/2001");
				formattedDate = dataValidator.formatBirthDate(digitedDateAsString);
				exit = true;
			}catch(DateTimeParseException e){
				ioManager.printMessage("Formato incorrecto,vuelve a intentarlo");
			}
		}
		return formattedDate;
	}

	/*Metodo verifyApplicantBirthDate
	 * Metodo el cual verifica la fecha de nacimiento del usuario
	 * (para verificar que tenga entre 18 y 65 años)
	 */
	public LocalDate verifyApplicantBirthDate(String messageToShow) {
		boolean exit = false;
		LocalDate formattedDate = null;
		LocalDate verifiedDate = null;
		while(!exit){
			try {
				formattedDate = formatDigitedDate(messageToShow);
				verifiedDate = applicantManager.verifyApplicantBirthDate(formattedDate);
				exit = true;
			}catch(AgeOutsideAllowedRangesException e){
				ioManager.printMessage(e.getMessage());
			}
		}
		return verifiedDate;
	}
	/*Metodo verifyDigitedContractType
	 * Metodo el cual verifica la opcion dgitada desde el menu de contratos,cverifica si esta entre el rango
	 * de 1 a 6,si no 
	 */

	public int verifyDigitedContractType() {
		boolean exit = false;
		int verifiedChoosedContract = 0;
		int digitedContract = 0;

		while(!exit){
			try {
				digitedContract = showContractMenu();
				verifiedChoosedContract = contractManager.verifyDigitedContract(digitedContract);
				exit = true;
			}catch(InvalidInputException e){
				ioManager.printMessage(e.getMessage());
			}catch(NumberFormatException e){
				ioManager.printMessage("La cadena no puede estar vacia,ni tener caracteres extraños");
			}
		}
		return verifiedChoosedContract;
	}

	public int verifyDigitedOption(String message) {
		boolean exit = false;
		int digitedSex = 0;
		int digitedOption = 0;
		int verifiedDigitedOption = 0;

		while(!exit){	
			try {
				digitedOption = verifyDigitedInt(message);
				verifiedDigitedOption = applicantManager.verifyDigitedOption(digitedOption);
				exit =true;
			}catch(InvalidInputException e){
				ioManager.printMessage(e.getMessage());
			}catch(NumberFormatException e){
				ioManager.printMessage("Formato Digitado incorrecto,vuelve a intentarlo");
			}

		}
		return verifiedDigitedOption;			
	}

	public String verifyDigitedPhoneNumber() {
		boolean exit = false;
		String verifiedPhoneNumber = " ";
		String digitedPhoneNumber = " ";

		while(!exit) {
			digitedPhoneNumber = verifyDigitedString("Ingrese su numero de telefono"
					+ "\nEjemplo de Formato:3105646566");
			try {
				verifiedPhoneNumber = dataValidator.verifyPhoneNumber(digitedPhoneNumber);
				exit = true;
			}catch(InvalidInputException e) {
				ioManager.printMessage(e.getMessage());
			}
		}
		return verifiedPhoneNumber;
	}

	public String verifyDigitedLicense() {
		boolean exit = false;
		String digitedLicense = " ";
		String verifiedLicense = " ";

		while(!exit) {
			digitedLicense = verifyDigitedString("Ingresa tu cedula de ciudadania"
					+ "\nEjemplo de formato:1051064181");
			try {
				verifiedLicense =  dataValidator.verifyLicense(digitedLicense);
				exit = true;
			}catch(InvalidInputException e) {
				ioManager.printMessage(e.getMessage());
			}
		}
		return verifiedLicense;
	}

	public void init() {
		runMenu();
	}




}
