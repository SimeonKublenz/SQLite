package de.szut.dqi12.sqlitebrowser.model;

import java.sql.*;
import java.util.ArrayList;

public class Connection {
	
	private java.sql.Connection connection;
	private Statement statement;
	private ResultSet resultSet;
	
	public Connection(String path) {
		loadDatabase(path);
	}
	
	/**
	 * Lädt eine Datenbank und gibt die Namen aller Tabellen zurück
	 * @param path = Pfad der Datenbank
	 * @return Array aller Tabellennamen
	 */
	private String[] loadDatabase(String path) {
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:" + path);
			statement = connection.createStatement();
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ArrayList<String> tables = new ArrayList<String>();
		try {
			DatabaseMetaData meta = connection.getMetaData();
			ResultSet res = meta.getTables(null, null, null, new String[] {"TABLE"});
			while (res.next()) {
				tables.add(res.getString("TABLE_NAME"));
			}
			res.close();
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (String[]) tables.toArray();
	}
	
	/**
	 * Führt eine SQL-Query aus
	 * @param query = Auszuführende Query
	 */
	private void executeQuery(String query) {
		try {
			resultSet = statement.executeQuery(query);
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String[][] executeQuery(Query query) {
		String queryText = query.getQuery();
		int start = query.getStart();
		int end = query.getEnd();
		
		if (start < 0 || start + end == 0) {
			executeQuery(queryText);
		}
		else if (start > end) {
			executeQuery(queryText);
		}
		else {
			executeQuery(queryText + "LIMIT" + start + "," + end);
		}
		return getTableData();
	}
	
	/**
	 * Gibt den Inhalt der Tabelle zurück
	 * @return Inhalt der Tabelle
	 */
	private String[][] getTableData() {
		String[][] data = null;
		try {
			data = new String[resultSet.getMetaData().getColumnCount()][];
			for (int i = 0; i < resultSet.getMetaData().getColumnCount(); i++) {
				data[i] = (String[]) resultSet.getArray(i+1).getArray();
			}
			return data;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}
	
	/**
	 * Gibt den Namen der Datenbank zurück
	 * @return = Name der Datenbank
	 */
	public String getDatabaseName() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Gibt die namen aller Tabellen zurück
	 * @return = Namen aller Tabellen
	 */
	public String[] getTableNames() {
		// TODO Auto-generated method stub
		return null;
	}
}
