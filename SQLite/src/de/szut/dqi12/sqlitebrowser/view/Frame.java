package de.szut.dqi12.sqlitebrowser.view;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.MenuItem;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.HashMap;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import de.szut.dqi12.sqlitebrowser.model.Database;
import de.szut.dqi12.sqlitebrowser.model.Message;
import de.szut.dqi12.sqlitebrowser.model.Table;
import de.szut.dqi12.sqlitebrowser.settings.Settings;

public class Frame extends JFrame {
	
	private static final long serialVersionUID = -1206806827748738729L;
	private JLayeredPane mainPanel;
	private JTabbedPane tabPane;
	private Transmitter transmitter;
	private DatabaseTab defaultTab;
	private JFileChooser file;
	private GUI gui;
	private HashMap<Integer, DatabaseTab> tabs = new HashMap<Integer, DatabaseTab>();
	
	/**
	 * Der Konstruktor des Frames
	 * @param transmitter der Transmitter mit wessen hilfe übertragen werden soll
	 * @param gui Die GUI auf dem sich das Frame befindet
	 * @param settings die Settings welche angewandt werden sollen
	 */
	public Frame(Transmitter transmitter, GUI gui, Settings settings) {
		this.transmitter = transmitter;
		this.gui = gui;
		setTitle("SQLite");
		setBounds(settings.getRectangle());
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		mainPanel = createMainPanel(settings);
		setContentPane(mainPanel);
		setVisible(true);
		addWindowListener(new WindowListener() {
			@Override
			public void windowClosing(WindowEvent event) {
				closeWindow();
			}
			
			@Override
			public void windowActivated(WindowEvent event) {}
			@Override
			public void windowClosed(WindowEvent event) {}
			@Override
			public void windowDeactivated(WindowEvent event) {}
			@Override
			public void windowDeiconified(WindowEvent event) {}
			@Override
			public void windowIconified(WindowEvent event) {}
			@Override
			public void windowOpened(WindowEvent event) {}
		});
		file = new JFileChooser(new File("."));
		file.removeChoosableFileFilter(file.getChoosableFileFilters()[0]);
		file.setFileFilter(new FileNameExtensionFilter("Datenbank Datei","db3"));
	}
	
	/**
	 * Erstellt das Mainpanel
	 * @param settings Die Settings welche angewandt werden sollen
	 * @return Das Mainpanel
	 */
	public JLayeredPane createMainPanel(Settings settings) {
		JLayeredPane mainPanel = new JLayeredPane();
		mainPanel.setLayout(new BorderLayout(0, 0));
		
		NumeratedPopupMenu rightclickMenu = new NumeratedPopupMenu();
		
		MenuItem newWindow = new MenuItem("In neues Fenster verschieben");
		newWindow.addActionListener(e -> {
			int index = ((NumeratedPopupMenu)((MenuItem) e.getSource()).getParent()).getNumber();
			DatabaseTab tab = (DatabaseTab) tabPane.getComponentAt(index);
			if (tab.getID() != -1) {
				removeTab(tab.getID());
				gui.createFrame(tab, new Settings(this.getBounds(), tab.getTreeWidth()));
			}
			else {
				gui.createFrame(new Settings(this.getBounds(), tab.getTreeWidth()));
			}
		});
		
		MenuItem close = new MenuItem("Schließen");
		close.addActionListener(e -> {
			int index = ((NumeratedPopupMenu)((MenuItem) e.getSource()).getParent()).getNumber();
			DatabaseTab tab = (DatabaseTab) tabPane.getComponentAt(index);
			if (tab.getID() != -1) {
				removeTab(tab.getID());
				tab.closeConnection();
			}
		});
		MenuItem closeOther = new MenuItem("Andere Schließen");
		closeOther.addActionListener(e -> {
			int index = ((NumeratedPopupMenu)((MenuItem) e.getSource()).getParent()).getNumber();
			for (int i = 0; i < tabPane.getTabCount(); i++) {
				if (i != index) {
					DatabaseTab tab = (DatabaseTab) tabPane.getComponentAt(i);
					removeTab(tab.getID());
					tab.closeConnection();
				}
			}
		});
		rightclickMenu.add(newWindow);
		rightclickMenu.addSeparator();
		rightclickMenu.add(close);
		rightclickMenu.add(closeOther);
		
		add(rightclickMenu);
		enableEvents(AWTEvent.MOUSE_EVENT_MASK);
		
		tabPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
		tabPane.removeMouseListener(tabPane.getMouseListeners()[0]);
		tabPane.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseClicked(MouseEvent event) {
				JTabbedPane tabbedPane = (JTabbedPane) event.getComponent();
				if (event.getButton() == MouseEvent.BUTTON3) {
					// Menüliste zum "In neues Fenster verschieben", "Schließen", "Andere Schließen"
					if (tabbedPane.getTabCount() == 1) {
						newWindow.setEnabled(false);
						closeOther.setEnabled(false);
					}
					else {
						newWindow.setEnabled(true);
						closeOther.setEnabled(true);
					}
					for (int i = 0; i < tabbedPane.getTabCount(); i++) {
						if (tabbedPane.getBoundsAt(i).contains(event.getPoint())) {
							rightclickMenu.setNumber(i);
							rightclickMenu.show(tabPane, event.getX(), event.getY());
						}
					}
				}
				else {
					for (int i = 0; i < tabbedPane.getTabCount(); i++) {
						if (tabbedPane.getBoundsAt(i).contains(event.getPoint())) {
							tabbedPane.setSelectedIndex(i);
						}
					}
				}
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {}
			@Override
			public void mouseExited(MouseEvent arg0) {}
			@Override
			public void mousePressed(MouseEvent arg0) {}
			@Override
			public void mouseReleased(MouseEvent arg0) {}
		});
		
		JMenuBar menuBar = new JMenuBar();
		JMenu file = new JMenu("Datei");
		JMenuItem openDatabase = new JMenuItem("Öffnen");
		openDatabase.addActionListener(e -> {
			selectDatabase();
		});
		JMenuItem newFrame = new JMenuItem("neues Fenster");
		newFrame.addActionListener(e -> {
			DatabaseTab tab = (DatabaseTab) tabPane.getComponentAt(((NumeratedPopupMenu)((MenuItem) e.getSource()).getParent()).getNumber());
			gui.createFrame(new Settings(this.getBounds(), tab.getTreeWidth()));
		});
		JMenuItem closeDatabase = new JMenuItem("Schließen");
		closeDatabase.addActionListener(e -> {
			DatabaseTab tab = ((DatabaseTab) tabPane.getComponentAt(tabPane.getSelectedIndex()));
			if (tab.getID() != -1) {
				tab.closeConnection();
				tabs.remove(tab);
				tabPane.removeTabAt(tabPane.getSelectedIndex());
			}
		});
		JMenuItem closeFrame = new JMenuItem("Beenden");
		closeFrame.addActionListener(e -> {
			closeWindow();
			dispose();
		});
		menuBar.add(file);
		file.add(openDatabase);
		file.add(newFrame);
		file.addSeparator();
		file.add(closeDatabase);
		file.add(closeFrame);
		
		mainPanel.add(menuBar, BorderLayout.NORTH);
		mainPanel.add(tabPane, BorderLayout.CENTER);
		
		defaultTab = new DatabaseTab(-1, " Datenbank ", settings.getTreeWidth());
		tabPane.addTab(defaultTab.getTabName(), defaultTab);
		tabs.put(defaultTab.getID(), defaultTab);
		
		return mainPanel;
	}
	
	/**
	 * Fügt dem Frame einen tab hinzu
	 * @param tab der hinzuzufügende tab
	 */
	public void addTab(DatabaseTab tab) {
		String suffix = getSuffix(tab.getTabName());
		tab.setSuffix(suffix);
		tabPane.add(tab.getTabTitle(), tab);
		tabs.put(tab.getID(), tab);
		if (tabs.containsValue(defaultTab)) {
			tabPane.removeTabAt(tabPane.indexOfTab(defaultTab.getTabTitle()));
			tabs.remove(defaultTab.getID());
		}
		tabs.put(tab.getID(), tab);
	}
	
	/**
	 * Entfernt einen Tab
	 * @param iD Die ID des zu entfernenden Tabs
	 */
	public void removeTab(int iD) {
		if (tabs.containsKey(iD)) {
			if (tabs.size() == 1) {
				tabPane.addTab(defaultTab.getTabName(), defaultTab);
				tabs.put(defaultTab.getID(), defaultTab);
			}
			tabPane.remove(tabs.get(iD));
			tabs.remove(iD);
		}
	}
	
	/**
	 * Lädt eine Datenbank in einen neuen Tab
	 * @param database Die zu ladende Datenbank
	 * @param iD Die ID des Tabs
	 */
	public void loadDatabase(Database database, int iD) {
		DatabaseTab focusedTab = ((DatabaseTab) tabPane.getComponentAt(tabPane.getSelectedIndex()));
		DatabaseTab tab = new DatabaseTab(database, transmitter, iD, focusedTab.getTreeWidth());
		addTab(tab);
	}
	
	/**
	 * Gibt das Suffix  zurück das ein neuer Tab mit dem übergebenen Namen haben muss
	 * @param tableName Der Name des neuen tabs
	 * @return Das Suffix das der Tab braucht
	 */
	private String getSuffix(String tableName) {
		String suffix = "";
		boolean end = false;
		int count = 1;
		while (!end) {
			end = true;
			for (int i = 0; i < tabPane.getTabCount(); i++) {
				if (((DatabaseTab) tabPane.getComponentAt(i)).getSuffix().equals(suffix) &&
						((DatabaseTab) tabPane.getComponentAt(i)).getTabName().equals(tableName)) {
					end = false;
				}
			}
			if (!end) {
				count++;
				suffix = "(" + count +")";
			}
		}
		System.out.println(suffix);
		return suffix;
	}
	
	/**
	 * Öffent das Dateiauswahlfenster und sendet gewählte die Datei
	 */
	private void selectDatabase() {
		file.showOpenDialog(null);
		if (file.getSelectedFile() != null && file.getSelectedFile().getName().endsWith(".db3")) {
			transmitter.transmit(new Message(file.getSelectedFile(), gui.getID(this)));
		}
	}
	
	/**
	 * Setzt den Tabelleninhalt des Tabs mit gewählter ID
	 * @param table Anzuzeigende Tabelle
	 * @param iD ID des Tabs
	 */
	public void showTable(Table table, int iD) {
		tabs.get(iD).setTableContent(table);
	}
	
	/**
	 * Schließt eine Tab und vordert ihn auf seine Connection zu schließen
	 */
	private void closeWindow() {
		gui.closeWindow(this);
		for (int index = 0; index < tabPane.getTabCount(); index++) {
			DatabaseTab tab = ((DatabaseTab) tabPane.getComponentAt(index));
			if (tab.getID() != -1) {
				tab.closeConnection();
			}
		}
	}
	
	/**
	 * Gibt die Breite des Baumes des aktuell gewählten Tabs
	 * @return Die Breite des Baumes des aktuell gewählten Tabs
	 */
	public int getTreeWidth() {
		return ((DatabaseTab) tabPane.getComponentAt(tabPane.getSelectedIndex())).getTreeWidth();
	}
}