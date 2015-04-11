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
	
	public Connection(File file) {
		this.file = file;
	}
	
	/**
	 * Lädt eine Datenbank und gibt die Namen aller Tabellen zurück
	 * @param path = Pfad der Datenbank
	 * @return Array aller Tabellennamen
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new Database(file.getName().substring(0, file.getName().lastIndexOf(".")) , tables.toArray(new String[tables.size()]));
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
		try {
			while(resultSet.next()) {
				data.add(new String[resultSet.getMetaData().getColumnCount()]);
				for (int i = 0; i < resultSet.getMetaData().getColumnCount(); i++) {
					data.get(data.size() - 1)[i] = resultSet.getString(i+1);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new Table(columnHeaders, (String[][]) data.toArray(new String[data.size()][]));
	}
	
	private String[] getColumnHeaders() {
		String[] columnHeaders = null;
		try {
			columnHeaders = new String[resultSet.getMetaData().getColumnCount()];
			for (int i = 0; i < resultSet.getMetaData().getColumnCount(); i++) {
				columnHeaders[i] = resultSet.getMetaData().getColumnName(i + 1);
			}
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return columnHeaders;
	}
	
	/**
	 * Gibt den Namen der Datenbank zurück
	 * @return Name der Datenbank
	 */
	public String getDatabaseName() {
		try {
		    ResultSet catalogs = connection.getMetaData().getCatalogs();
		    while (catalogs.next()) {
		        String strg = catalogs.getString("TABLE_CAT");
		    }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public void close() {
		try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
