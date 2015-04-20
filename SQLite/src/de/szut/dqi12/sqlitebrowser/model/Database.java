package de.szut.dqi12.sqlitebrowser.model;

public class Database {
	
	public String databaseName;
	public String[] dataTables;
	
	/**
	 * Det Konstruktor der Datenbank
	 * @param databaseName Name der Datenbank
	 * @param dataTables Tabellennamen der Datenbank
	 */
	public Database(String databaseName, String[] dataTables) {
		this.databaseName = databaseName;
		this.dataTables = dataTables;
	}
	
	/**
	 * Gibt den Datenbanknamen zur�ck
	 * @return der Datenbankname
	 */
	public String getDatabaseName() {
		return databaseName;
	}
	
	/**
	 * Gibt die Tabellennamen zur�ck
	 * @return die Tabellennamen
	 */
	public String[] getDataTables() {
		return dataTables;
	}
}
