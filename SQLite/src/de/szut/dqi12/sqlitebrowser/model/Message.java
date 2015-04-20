package de.szut.dqi12.sqlitebrowser.model;

import java.io.File;

public class Message {
	
	public enum MessageTyp {
		QUERY, PATH, CLOSE
	}
	
	private MessageTyp e;
	private String query;
	private File file;
	private int start;
	private int end;
	private int iD;
	private int frameID;
	private boolean isQuery;
	
	/**
	 * Der Query-Konstruktor der Message
	 * @param query die auszuf�hrende Query 
	 * @param start der Start des Limits
	 * @param end das ende des Limits
	 * @param iD die ID der Connection und des Tabs
	 * @param frameID die Nummer des Frames in dem sich der Tab befindet
	 * @param isQuery gibt im fallse eines Fehlers an woher dieser Befehl kam
	 */
	public Message(String query, int start, int end, int iD, int frameID, boolean isQuery) {
		e = MessageTyp.QUERY;
		this.query = query;
		this.start = start;
		this.end = end;
		this.iD = iD;
		this.isQuery = isQuery;
	}
	
	/**
	 * Der Pfad-Konstruktor der Message
	 * @param file Die Datei die geladen werden soll
	 * @param iD Die ID der Connection und des Tabs
	 */
	public Message(File file, int iD) {
		e = MessageTyp.PATH;
		this.file = file;
		this.iD = iD;
	}
	
	/**
	 * Der Close-Konstruktor der Message
	 * @param iD Die ID der zu schlie�enden Verbindung und des Tabs
	 */
	public Message(int iD) {
		e = MessageTyp.CLOSE;
		this.iD = iD;
	}
	
	/**
	 * gibt den Typ der Message zur�ck
	 * @return der Typ der Message:
	 * 	QUERY: Diese Message enth�lt eine Query
	 * 	PATH: Diese Message enth�lt eine Datenbankdatei
	 * 	CLOSE: Diese Message enth�lt eine Schlie�befehl
	 */
	public MessageTyp getMessageTyp() {
		return e;
	}
	
	/**
	 * Gibt die Query zur�ck
	 * @return die Query
	 */
	public String getQuery() {
		return query;
	}
	
	/**
	 * Gibt die Datei zur�ck
	 * @return die Datei
	 */
	public File getFile() {
		return file;
	}
	
	/**
	 * Gibt den LIMIT-Start zur�ck
	 * @return LIMIT-Start
	 */
	public int getStart() {
		return start;
	}
	
	/**
	 * Gibt das LIMIT-Ende zur�ck
	 * @return LIMIT-Ende
	 */
	public int getEnd() {
		return end;
	}
	
	/**
	 * GIBT die ID der Connection zur�ck
	 * @return die ID der Connection
	 */
	public int getID() {
		return iD;
	}
	
	/**
	 * Gibt die Nummer des Frames zur�ck
	 * @return Die Nummer des Frames
	 */
	public int getFrameID() {
		return frameID;
	}
	
	/**
	 * Gibt zur�ck woher diese Query kommt
	 * @return
	 */
	public boolean isQuery() {
		return isQuery;
	}
}
