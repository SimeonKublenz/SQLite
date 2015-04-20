package de.szut.dqi12.sqlitebrowser.controller;

import de.szut.dqi12.sqlitebrowser.model.Database;
import de.szut.dqi12.sqlitebrowser.model.Table;
import de.szut.dqi12.sqlitebrowser.view.GUI;

public class Controller {
	
	private GUI gui;
	
	/**
	 * Konstruktor des Controllers
	 * @param gui Die GUI welche die befehle erhalten soll
	 */
	public Controller(GUI gui) {
		this.gui = gui;
	}
	
	/**
	 * Vordert die GUI auf die erhaltene Tabelle anzuzeigen
	 * @param table Zu zeigende Tabelle
	 * @param iD ID des Tabs welcher die Tabelle anzeigen soll
	 * @param frameID nummer des frames welches den gesuchten Tab enthällt
	 * @param isQuery Zeigt im Falle eines Fehlers woher diese Tabelle kommt
	 */
	public void showTable(Table table, int iD, int frameID, boolean isQuery) {
		gui.showDataBase(table, iD, frameID, isQuery);
	}
	
	/**
	 * Fügt eine Datenbank zur GUI hinzu
	 * @param database hinzuzufügende Datenbank
	 * @param iD ID der Datenbank
	 * @param frameID ID des Frames welches die Datenbank aufnehmen soll
	 */
	public void addDatabase(Database database, int iD, int frameID) {
		gui.loadDatabase(database, iD, frameID);
	}
	
	/**
	 * Sendet eine Fehlermeldung an die GUI wenn eine verbindung nicht geschlossen werden konnte
	 */
	public void sendClosingError() {
		gui.showClosingError();
	}
}
