package de.szut.dqi12.sqlitebrowser.settings;

import java.awt.Rectangle;

public class Settings {
	
	private Rectangle rectangle;
	private int treeWidth;
	
	/**
	 * 
	 * @param rectangle
	 * @param treeWith
	 */
	public Settings(Rectangle rectangle, int treeWith) {
		this.rectangle = rectangle;
		this.treeWidth = treeWith;
	}

	/**
	 * Gibt die Position und Größe und Position als Rectangle zurück
	 * @return Der Rectangle
	 */
	public Rectangle getRectangle() {
		return rectangle;
	}

	/**
	 * Gibt die Breite des Baumes zurück
	 * @return Die Breite des Baumes
	 */
	public int getTreeWidth() {
		return treeWidth;
	}
}
