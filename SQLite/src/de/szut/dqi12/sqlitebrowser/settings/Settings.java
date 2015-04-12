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
	 * Gibt die Position und Gr��e und Position als Rectangle zur�ck
	 * @return Der Rectangle
	 */
	public Rectangle getRectangle() {
		return rectangle;
	}

	/**
	 * Gibt die Breite des Baumes zur�ck
	 * @return Die Breite des Baumes
	 */
	public int getTreeWidth() {
		return treeWidth;
	}
}
