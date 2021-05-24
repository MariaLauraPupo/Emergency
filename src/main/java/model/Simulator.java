package model;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import model.Event.EventType;
import model.Patient.ColorCode;

public class Simulator {
	
	//Coda di eventi
	PriorityQueue<Event> queue;
	
	//Modello del mondo
	private List<Patient> patients; // vediamo se ci conviene di più avere i pazienti in una lista
	                        // o in una coda prioritaria o entrambe
	//quanti studi sono liberi
	private int freeStudios;
	
	private Patient.ColorCode ultimoColore;
	
	//Parametri di input
	private int totStudios = 3; //totale di studi NS è una costante 
	private int numPatients = 120; //NP
	//totStudio e numPatients hanno valori di default che poi il simulatore dovrà cambiare
	private Duration T_ARRIVAL = Duration.ofMinutes(5);
	private Duration DURATION_TRIAGE = Duration.ofMinutes(5);
	private Duration DURATION_WHITE = Duration.ofMinutes(10);
	private Duration DURATION_YELLOW = Duration.ofMinutes(15);
	private Duration DURATION_RED = Duration.ofMinutes(30);
	
	private Duration TIMEOUT_WHITE = Duration.ofMinutes(60);
	private Duration TIMEOUT_YELLOW = Duration.ofMinutes(30);
	private Duration TIMEOUT_RED = Duration.ofMinutes(30);
	
	private LocalTime startTime = LocalTime.of(8, 00);
	private LocalTime endTime = LocalTime.of(20, 00);
	

	//Paramentri di output
	private int patientsTreated;
	private int patientsAbandoned;
	private int patientsDead;

	//Metodo che inizializza il simulatore preparando tutte le strutture dati che servono
	public void init() {
		//inizializzo le strutture dati che mi servono
		this.queue = new PriorityQueue<>();
		this.patients = new ArrayList<>();
		
		//inizializzo tutto il modello del mondo
		this.freeStudios = this.totStudios;//inizialmente ho tutti gli studi liberi
		
		 this.ultimoColore = ColorCode.WHITE;
		
		//inizializzo i parametri di output
		this.patientsAbandoned = 0;
		this.patientsDead = 0;
		this.patientsTreated = 0;
		
		//iniettare gli eventi di input (ARRIVAL)
		LocalTime ora = this.startTime;
		int inseriti = 0;
		
		while(ora.isBefore(this.endTime) && inseriti<this.numPatients) {
			//finche no ho ancora raggiunto l'orario di chiusura 
			//e contemporaneamente ci sono ancora pazienti da inserire li inserisco 
			//per ognuno di questi dovrò creare un nuovo paziente e 
			//inserirlo nella coda
			Patient p = new Patient(ora,ColorCode.NEW);//Stato dell'evento di arrivo
			                                           //arrivato un paziente non so ancora il colore
			
			Event e = new Event(ora,EventType.ARRIVAL, p);
			
			this.queue.add(e);
			
			//inoltre aggiungo il paziente nelle lista di tutti i pazienti che abbiamo ricevuto
			this.patients.add(p);
			
			
			//operazione che faccio oer passare da un paziente a quello successivo
			inseriti++;
			ora = ora.plus(T_ARRIVAL);
			
		}
		
	}

	
	private Patient.ColorCode prossimoColore(){
		if(ultimoColore.equals(ColorCode.WHITE))
			ultimoColore = ColorCode.YELLOW;
		else if(ultimoColore.equals(ColorCode.YELLOW))
			ultimoColore = ColorCode.RED;
		else 
			ultimoColore = ColorCode.WHITE;
		return ultimoColore;
		
	}
	
	//Metodo che esegue la simulazione vera e propria e crea gli eventi iniziali
	public void run() {
		while(!this.queue.isEmpty()) {
			Event e = this.queue.poll();
			System.out.println(e);
			processEvent(e);
		}
		
	}
	
	public void processEvent(Event e) {
		Patient p = e.getPatient();
		LocalTime ora = e.getTime();
		Patient.ColorCode colore = p.getColor();
		
		switch(e.getType()) {
		//tutti itipi di eventi che devo gestire
		case ARRIVAL:
			this.queue.add(new Event(ora.plus(DURATION_TRIAGE),EventType.TRIAGE,p));
			break;
			
		case TRIAGE:
			p.setColor(prossimoColore());//imposto il paziente con un nuovo colore
			//a questo punto il paziente è in sala d'attesa con un nuovo colore
			//a seconda del colore che gli ho dato imposterò un colore diverso
			if(p.getColor().equals(Patient.ColorCode.WHITE))
					this.queue.add(new Event(ora.plus(TIMEOUT_WHITE), EventType.TIMEOUT,p));
			
			else if (p.getColor().equals(Patient.ColorCode.YELLOW))
				this.queue.add(new Event(ora.plus(TIMEOUT_YELLOW), EventType.TIMEOUT,p));
			
			else if (p.getColor().equals(Patient.ColorCode.RED))
				this.queue.add(new Event(ora.plus(TIMEOUT_RED), EventType.TIMEOUT,p));

			break;
			
		case FREE_STUDIO:
			break;
			
		case TIMEOUT:
			switch(colore) {
			case WHITE:
				p.setColor(ColorCode.OUT);
				this.patientsAbandoned++;
				break;
			case YELLOW: 
				p.setColor(ColorCode.RED);
				this.queue.add(new Event(ora.plus(TIMEOUT_RED), EventType.TIMEOUT,p));
				break;
			case RED:
				p.setColor(ColorCode.BLACK);
				this.patientsDead++;
				break;
			default:
				System.out.println("ERRORE: TIME OUT CON COLORE "+colore);
			}
			break;
			
		case TREATED:
			break;
			
		}
	}
	
	//Creo i metodi Set per i parametri che devono poter essere cambiati dall'esterno
	//cioè i parametri di input
	public void setTotStudios(int totStudios) {
		this.totStudios = totStudios;
	}

	public void setNumPatients(int numPatients) {
		this.numPatients = numPatients;
	}

	public void setT_ARRIVAL(Duration t_ARRIVAL) {
		T_ARRIVAL = t_ARRIVAL;
	}

	public void setDURATION_TRIAGE(Duration dURATION_TRIAGE) {
		DURATION_TRIAGE = dURATION_TRIAGE;
	}

	public void setDURATION_WHITE(Duration dURATION_WHITE) {
		DURATION_WHITE = dURATION_WHITE;
	}

	public void setDURATION_YELLOW(Duration dURATION_YELLOW) {
		DURATION_YELLOW = dURATION_YELLOW;
	}

	public void setDURATION_RED(Duration dURATION_RED) {
		DURATION_RED = dURATION_RED;
	}

	public void setTIMEOUT_WHITE(Duration tIMEOUT_WHITE) {
		TIMEOUT_WHITE = tIMEOUT_WHITE;
	}

	public void setTIMEOUT_YELLOW(Duration tIMEOUT_YELLOW) {
		TIMEOUT_YELLOW = tIMEOUT_YELLOW;
	}

	public void setTIMEOUT_RED(Duration tIMEOUT_RED) {
		TIMEOUT_RED = tIMEOUT_RED;
	}

	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}

	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}
 
	//Creo i metodi Get per tutti i paramtri di output
	public int getPatientsTreated() {
		return patientsTreated;
	}

	public int getPatientsAbandoned() {
		return patientsAbandoned;
	}

	public int getPatientsDead() {
		return patientsDead;
	}
	
	
	

}
