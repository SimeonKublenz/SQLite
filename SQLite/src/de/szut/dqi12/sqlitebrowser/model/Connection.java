package de.szut.dqi12.sqlitebrowser.model;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;

public class Connection {
	
	private File file;
	private java.sql.Connection connection;
	private Statement statement;
	private ResultSet resultSet;
	private int start;
	private int end;
	
	/**
	 * Der Konstruktor der Connection
	 * @param file die zu ladende Datenbankdatei
	 */
	public Connection(File file) {
		this.file = file;
	}
	
	/**
	 * Lädt eine Datenbank und gibt ein Datenbankobjekt zurück
	 * @return das Datenbankobjekt der geladenen Datenbank
	 */
	public Database loadDatabase() {
		boolean exception = false;
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:" + file.getPath());
			statement = connection.createStatement();
		}
		catch (SQLException e) {
			exception = true;
		}
		
		ArrayList<String> tables = new ArrayList<String>();
		try {
			DatabaseMetaData meta = connection.getMetaData();
			ResultSet res = meta.getTables(null, null, "%", null);
			while (res.next()) {
				tables.add(res.getString("TABLE_NAME"));
			}
			res.close();
		}
		catch (SQLException e) {
			exception = true;
		}
		if (!exception) {
			return new Database(file.getName().substring(0, file.getName().lastIndexOf(".")) , tables.toArray(new String[tables.size()]));
		}
		else {
			return null;
		}
	}
	
	/**
	 * Führt eine SQL-Query aus
	 * @param query Auszuführende Query
	 */
	private void executeQuery(String query) {
		try {
			resultSet = statement.executeQuery(query);
		}
		catch (SQLException e) {}
	}
	
	/**
	 * Führt eine SQL-Query aus
	 * @param message Nachricht welches die Query und einige Randinformationen enthällt
	 * @return tabellenobjekt welches die Tabelle enthällt
	 */
	public Table executeQuery(Message message) {
		String queryText = message.getQuery();
		start = message.getStart();
		end = message.getEnd();
		
		if (start <= 0 || start + end == 0 || start > end) {
			executeQuery(queryText);
		}
		else {
			executeQuery(queryText + " LIMIT " + (start - 1) + ", " + (end - start + 1));
		}
		return getTableData();
	}
	
	/**
	 * Gibt den Inhalt der Tabelle zurück
	 * @return Inhalt der Tabelle
	 */
	private Table getTableData() {
		ArrayList<String[]> data =  new ArrayList<String[]>();
		String[] columnHeaders = getColumnHeaders();
		boolean exception = false;
		try {
			while(resultSet.next()) {
				data.add(new String[resultSet.getMetaData().getColumnCount()]);
				for (int i = 0; i < resultSet.getMetaData().getColumnCount(); i++) {
					data.get(data.size() - 1)[i] = resultSet.getString(i+1);
				}
			}
		} catch (SQLException e) {
			exception = true;
		}
		if (!exception && columnHeaders != null) {
			return new Table(columnHeaders, (String[][]) data.toArray(new String[data.size()][]));
		}
		else {
			return null;
		}
	}
	
	/**
	 * Gibt die Zeilenheader der Tabelle zurück
	 * @return die Zeilenheader
	 */
	private String[] getColumnHeaders() {
		String[] columnHeaders = null;
		try {
			columnHeaders = new String[resultSet.getMetaData().getColumnCount()];
			for (int i = 0; i < resultSet.getMetaData().getColumnCount(); i++) {
				columnHeaders[i] = resultSet.getMetaData().getColumnName(i + 1);
			}
		}
		catch (Exception e) {
			return null;
		}
		return columnHeaders;
	}
	
	/**
	 * Beendet eine Verbindug
	 * @return gibt true zurück wenn die Verbindung nicht geschlossen werden konnte
	 */
	public boolean close() {
		try {
			connection.close();
		}
		catch (SQLException e) {
			return true;
		}
		return false;
	}
}
