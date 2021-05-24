package model;

import java.time.LocalTime;

public class Event implements Comparable<Event>{
	
	//tipi di evento
	enum EventType{
		ARRIVAL, // ARRIVA NUOVO PAZIENTE, ENTRA IN TRIAGE
		TRIAGE, //è FINITO IL TRIAGE, ENTRO IN SALA D'ATTESA
		TIMEOUT,//PASSA UN CERTO TEMPO DI ATTESA
		FREE_STUDIO,//SI è LIBERATO UNO STUDIO: QUALCUNO PUò ESSERE CHIAMATO
		TREATED //PAZIENTE CURATO
		//queste sono le cose che possono succedere 
		//e causano una variazione dello stato del paziente
 	};
        private LocalTime time;
        private EventType type;
        
        //ho bisogno che l'evento si ricordi a quale event è relativo
        private Patient patient; // sevo avere un roferimento al paziente 

		public Event(LocalTime time, EventType type, Patient patient) {
			super();
			this.time = time;
			this.type = type;
			this.patient = patient;
		}

		public LocalTime getTime() {
			return time;
		}

		public void setTime(LocalTime time) {
			this.time = time;
		}

		public EventType getType() {
			return type;
		}

		public void setType(EventType type) {
			this.type = type;
		}

		public Patient getPatient() {
			return patient;
		}

		public void setPatient(Patient patient) {
			this.patient = patient;
		}
		
		 // mi serve anche un comparatore perchè questi eventi devono essere inseriti 
		//in una coda prioritarià
		
		@Override
		public int compareTo(Event other) {
			return this.time.compareTo(other.time);
		}

		@Override
		public String toString() {
			return "Event [time=" + time + ", type=" + type + ", patient=" + patient + "]";
		}
        
       
		
		
}
