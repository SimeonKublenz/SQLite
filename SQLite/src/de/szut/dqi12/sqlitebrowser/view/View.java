package de.szut.dqi12.sqlitebrowser.view;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import settings.*;

public class View extends JFrame {
	
	public static void main(String[] args) {
		new View();
	}
	
	public View() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(Settings.SCREENWIDTH, Settings.SCREENHEIGHT, 800, 620);
		setTitle("SQLite");
		setLayout(new BorderLayout());
		setVisible(true);
		
	}
}