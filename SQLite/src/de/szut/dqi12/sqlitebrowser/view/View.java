package de.szut.dqi12.sqlitebrowser.view;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import de.szut.dqi12.sqlitebrowser.settings.Settings;

public class View extends JFrame {
	
	private static final long serialVersionUID = -1206806827748738729L;
	private JPanel mainPanel;
	private JTabbedPane tabPane;
	private DataBaseTab defaultDataBaseTab;
	
	
	public static void main(String[] args) {
		new View();
	}
	
	public View() {
		setTitle(Settings.NAME);
		setBounds((Settings.SCREENWIDTH - Settings.WINDOWWIDTH)/2,
				(Settings.SCREENHEIGHT - Settings.WINDOWHEIGHT)/2,
				Settings.WINDOWWIDTH, Settings.WINDOWHEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainPanel = createMainPanel();
		setContentPane(mainPanel);
		setVisible(true);
		
	}
	
	public JPanel createMainPanel() {
		JPanel mainPanel = new JPanel(new BorderLayout(10, 0));
		tabPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
		
		JMenuBar menuBar = new JMenuBar();
		JMenu file = new JMenu();
		file.setText("File");
		JMenu edit = new JMenu();
		edit.setText("Edit");
		JMenu help = new JMenu();
		help.setText("Help");
		JMenuItem openDataBase = new JMenuItem();
		openDataBase.setText("Open");
		menuBar.add(file);
		menuBar.add(edit);
		menuBar.add(help);
		file.add(openDataBase);
		
		DataBaseTab defaultDataBaseTab = new DataBaseTab();
		
		mainPanel.add(menuBar, BorderLayout.NORTH);
		mainPanel.add(tabPane, BorderLayout.CENTER);
		tabPane.addTab("Database", defaultDataBaseTab);
		
		return mainPanel;
	}
	
	public void loadDataBase(String dataBaseName, String[] tables) {
		tabPane.add(dataBaseName, new DataBaseTab(dataBaseName, tables));
		tabPane.remove(defaultDataBaseTab);
	}
}