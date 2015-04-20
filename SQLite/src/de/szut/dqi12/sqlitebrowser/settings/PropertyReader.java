package de.szut.dqi12.sqlitebrowser.settings;

import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyReader {
	
	private Properties properties;
	private File file;
	private int windowWidth;
	private int windowHeight;
	private int windowXPosition;
	private int windowYPosition;
	private int treeWidth;
	
	/**
	 * Konstruktor des PropertyReaders
	 */
	public PropertyReader() {
		properties = new Properties();
		try {
			file = new File (new File(".").getCanonicalPath() + "/Properties.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Lädt die Properties
	 * @return geladene Properties
	 */
	public Settings loadProperties() {
		treeWidth = 0;
			try {
				properties.load(new FileInputStream(file));
				windowWidth = (int) Double.parseDouble(properties.getProperty("FensterBreite"));
				windowHeight = (int) Double.parseDouble(properties.getProperty("FensterHoehe"));
				windowXPosition = (int) Double.parseDouble(properties.getProperty("XPosition"));
				windowYPosition = (int) Double.parseDouble(properties.getProperty("YPosition"));
				treeWidth = (int) Double.parseDouble(properties.getProperty("BaumBreite"));
			}
			catch (FileNotFoundException e) {
				loadDefaultProperties();
			}
			catch (IOException e) {
				loadDefaultProperties();
			}
			catch (NumberFormatException e) {
				loadDefaultProperties();
				e.printStackTrace();
			}
		return new Settings(new Rectangle(windowXPosition, windowYPosition, windowWidth, windowHeight), treeWidth);
	}
	
	/**
	 * Setzt die Properties auf Default Werte
	 */
	private void loadDefaultProperties() {
		windowWidth = 800;
		windowHeight = 700;
		windowXPosition = (Toolkit.getDefaultToolkit().getScreenSize().width - windowWidth)/2;
		windowYPosition = (Toolkit.getDefaultToolkit().getScreenSize().height - windowHeight)/2;
		treeWidth = 200;
	}
	
	
	/**
	 * Speichert die übergebenen Werte in der Properties Datei
	 * @param rectangle position un größe des Frames
	 * @param treeWidth breite des treePanels
	 */
	public void savePreperties(Rectangle rectangle, int treeWidth) {
		properties.setProperty("FensterBreite", String.valueOf(rectangle.getWidth()));
		properties.setProperty("FensterHoehe", String.valueOf(rectangle.getHeight()));
		properties.setProperty("XPosition", String.valueOf(rectangle.getX()));
		properties.setProperty("YPosition", String.valueOf(rectangle.getY()));
		properties.setProperty("BaumBreite", String.valueOf((double) treeWidth));
		try {
			FileOutputStream fileOut = new FileOutputStream(file);
			properties.store(fileOut, "SQLite Browser Properties");
			fileOut.close();
		}
		catch (FileNotFoundException e) {}
		catch (IOException e) {}
	}
	
}
