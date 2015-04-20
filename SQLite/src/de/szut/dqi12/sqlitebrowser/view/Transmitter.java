package de.szut.dqi12.sqlitebrowser.view;

import java.util.Observable;

public class Transmitter extends Observable {
	
	/**
	 * Der Konstruktor der Transmitters
	 * @param object zu sendendes Objekt
	 */
	public void transmit(Object object) {
	    setChanged();
		notifyObservers(object);
	}
}