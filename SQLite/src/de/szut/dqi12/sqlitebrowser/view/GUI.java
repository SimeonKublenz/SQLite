package de.szut.dqi12.sqlitebrowser.view;

import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import de.szut.dqi12.sqlitebrowser.model.Database;
import de.szut.dqi12.sqlitebrowser.model.Table;
import de.szut.dqi12.sqlitebrowser.settings.PropertyReader;
import de.szut.dqi12.sqlitebrowser.settings.Settings;

public class GUI {
	
	private Transmitter transmitter;
	private Settings settings;
	
	private ArrayList<Frame> frameList = new ArrayList<Frame>();
	
	/**
	 * Der Konstruktor der GUI
	 * @param transmitter der Transmitter mit dessen Hilfe übertragen werden soll
	 */
	public GUI(Transmitter transmitter) {
		this.transmitter = transmitter;
		settings = new PropertyReader().loadProperties();
		createFrame();
	}
	
	/**
	 * Erstellt ein neues Frame
	 */
	public void createFrame() {
		frameList.add(new Frame(transmitter, this, settings));
	}
	
	/**
	 * Erstellt ein neues Frame mit Settings
	 * @param oldSettings Settings des alten Fensters
	 */
	public void createFrame(Settings oldSettings) {
		Rectangle oldRectangle = oldSettings.getRectangle();
		Rectangle newRectangle = new Rectangle((int) oldRectangle.getX() + 24, (int) oldRectangle.getY() + 24, (int) oldRectangle.getWidth(), (int) oldRectangle.getHeight());
		frameList.add(new Frame(transmitter, this, new Settings(newRectangle, oldSettings.getTreeWidth())));
	}
	
	/**
	 * Erstellt ein neues Frame
	 * @param tab zu ladender Tab
	 * @param oldSettings  Settings des alten Fensters
	 */
	public void createFrame(DatabaseTab tab, Settings oldSettings) {
		createFrame(oldSettings);
		frameList.get(frameList.size()-1).addTab(tab);
	}
	
	/**
	 * Lädt eine Datenbank
	 * @param database z ladende Datenbank
	 * @param iD ID des Tabs
	 * @param frameID ID des Frames in welches sich der Tab befindet
	 */
	public void loadDatabase(Database database, int iD, int frameID) {
		if (database != null) {
			frameList.get(frameID).loadDatabase(database, iD);
		}
		else {
			showErrorMessage("Datenbank konnte nicht geöffnet werden");
		}
	}
	
	/**
	 * Zeigt die Tabelle an
	 * @param table Anzuzeigende Tabelle
	 * @param iD ID des Tab welcher die Tabelle anzeigen soll
	 * @param frameID ID des Frames welches den Tab enthällt
	 * @param isQuery zeigt im falle eines Fehler woher die Query kommt
	 */
	public void showDataBase(Table table, int iD, int frameID, boolean isQuery) {
		if (table != null) {
			frameList.get(frameID).showTable(table, iD);
		}
		else if (isQuery) {
			showErrorMessage("Query konnte nicht ausgeführt werden");
		}
		else {
			showErrorMessage("Tabelle konnte nicht geladen werden");
		}
	}
	
	/**
	 * Schließt ein Frame
	 * @param frame Zu schließendes Frame
	 */
	public void closeWindow(Frame frame) {
		if (frameList.size() == 1) {
			new PropertyReader().savePreperties(frame.getBounds(), frame.getTreeWidth());
		}
		frameList.remove(frame);
	}
	
	/**
	 * Gibt die ID eines Frames zurück
	 * @param frame Das Frame wessen ID gesucht ist
	 * @return ID des Frames
	 */
	public int getID(Frame frame) {
		return frameList.indexOf(frame);
	}
	
	/**
	 * Zeigt die Fehlermeldung welche angezeigt werden soll wenn eine verbindung nicht geschloßen werden konnte
	 */
	public void showClosingError() {
		showErrorMessage("Datenbank konnte nicht geschlossen werden");
	}
	
	/**
	 * Zeigt die Übergebene Fehlermeldung
	 * @param error Die Fehlermeldung
	 */
	private void showErrorMessage(String error) {
		JOptionPane.showMessageDialog(null, error, "Error", JOptionPane.WARNING_MESSAGE);
	}
}
