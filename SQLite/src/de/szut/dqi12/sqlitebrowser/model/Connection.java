package de.szut.dqi12.sqlitebrowser.model;

import java.sql.*;

public class Connection {
	
	private Statement statement;
	private ResultSet resultSet;
	
	/**
	 * Lädt eine Datenbank
	 * @param path = Pfad der Datenbank
	 */
	public void loadDatabase(String path) {
		try {
			statement = DriverManager.getConnection("jdbc:sqlite:" + path).createStatement();
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Führt eine SQL-Query aus
	 * @param query = Auszuführende Query
	 */
	public void executeQuery(String query) {
		try {
			resultSet = statement.executeQuery(query);
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Führt eine SQL-Query aus
	 * @param query = Auszuführende Query
	 * @param start = Start der Anzeige
	 * @param end = Ende der Anzeige
	 */
	public void executeQuery(String query, int start, int end) {
		if (start < 0 || start + end == 0) {
			executeQuery(query);
		}
		else if (start > end) {
			executeQuery(query);
		}
		else {
			executeQuery(query + "LIMIT" + start + "," + end);
		}
	}
	
	/**
	 * Gibt den Inhalt der Tabelle zurück
	 * @return String Doppelarray
	 */
	public String[][] getTableData() {
		try {
			String[][] data = new String[resultSet.getMetaData().getColumnCount()][];
			for (int i = 0; i < resultSet.getMetaData().getColumnCount(); i++) {
				data[i] = (String[]) resultSet.getArray(i+1).getArray();
			}
			return data;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
