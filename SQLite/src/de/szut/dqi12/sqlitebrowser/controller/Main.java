package de.szut.dqi12.sqlitebrowser.controller;

import de.szut.dqi12.sqlitebrowser.model.Model;
import de.szut.dqi12.sqlitebrowser.view.GUI;
import de.szut.dqi12.sqlitebrowser.view.Transmitter;

public class Main {
	
	/**
	 * die Main
	 * @param args
	 */
	public static void main(String[] args) {
		Transmitter transmitter = new Transmitter();
		GUI gui = new GUI(transmitter);
		Controller controller = new Controller(gui);
		Model model = new Model(controller);
		transmitter.addObserver(model);
	}
}
