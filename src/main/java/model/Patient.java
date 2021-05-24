package model;

import java.time.LocalTime;

public class Patient {
	
	public enum ColorCode{ //STATI
		NEW, //in triage
		WHITE, YELLOW, RED, BLACK,
		TREATING, //dentro lo studio medico
		OUT // a casa (abbandonato o curato)
		//gli unici modi per passare da uno stato all'altro è 
	};
	
	
	private LocalTime arrivalTime;
	private ColorCode color;
	
	public Patient(LocalTime arrivalTime, ColorCode color) {
		super();
		this.arrivalTime = arrivalTime;
		this.color = color;
	}

	public LocalTime getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(LocalTime arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public ColorCode getColor() {
		return color;
	}

	public void setColor(ColorCode color) {
		this.color = color;
	}

	@Override
	public String toString() {
		return "Patient [arrivalTime=" + arrivalTime + ", color=" + color + "]";
	}
	
	
	

}
