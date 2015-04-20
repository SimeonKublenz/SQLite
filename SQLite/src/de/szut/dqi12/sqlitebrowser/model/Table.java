package de.szut.dqi12.sqlitebrowser.model;

public class Table {
	
	private String[] tableHeaders;
	private String[][] tableContent;
	
	/**
	 * Der Konstruktor der Tabel
	 * @param tableHeaders Die Tabellenheader
	 * @param tableContent Der Tabelleninhalt
	 */
	public Table(String[] tableHeaders, String[][] tableContent) {
		this.tableHeaders = tableHeaders;
		this.tableContent = tableContent;
	}
	
	/**
	 * Gibt die Tabellenheader zurück
	 * @return Die Tabellenheader
	 */
	public String[] getTableHeaders() {
		return tableHeaders;
	}
	
	/**
	 * Gibt den Tabelleninhalt zurück
	 * @return der Tabelleninhalt
	 */
	public String[][] getTableContent() {
		return tableContent;
	}
}
