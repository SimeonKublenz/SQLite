package de.szut.dqi12.sqlitebrowser.model;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import de.szut.dqi12.sqlitebrowser.controller.Controller;
import de.szut.dqi12.sqlitebrowser.model.Message.MessageTyp;

public class Model implements Observer {
	
	private Controller controller;
	private HashMap<Integer, Connection> connections = new HashMap<Integer, Connection>();
	private int nextID;
	
	/**
	 * Der Konstruktor des Models
	 * @param controller Der Controller der die Informationen erhalten soll
	 */
	public Model(Controller controller) {
		this.controller = controller;
		nextID = -1;
	}
	
	/**
	 * Empfängt die Aufträge der GUI und sendet die Antworten an den Controller
	 */
	@Override
	public void update(Observable arg0, Object arg1) {
		Message message = (Message) arg1;
		if (message.getMessageTyp() == MessageTyp.QUERY) {
			controller.showTable(connections.get(message.getID()).executeQuery(message), message.getID(), message.getFrameID(), message.isQuery());
		}
		else if (message.getMessageTyp() == MessageTyp.PATH) {
			Connection connection = new Connection(message.getFile());
			int iD = getID();
			connections.put(iD, connection);
			controller.addDatabase(connection.loadDatabase(), iD, message.getID());
		}
		else {
			boolean error = connections.get(message.getID()).close();
			connections.remove(message.getID());
			if (error) {
				controller.sendClosingError();
			}
		}
	}
	
	/**
	 * Gibt die nächste freie ID zurück
	 * @return nächste freie ID
	 */
	private Integer getID() {
		nextID++;
		return nextID;
	}
}