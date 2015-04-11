package de.szut.dqi12.sqlitebrowser.view;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.MenuItem;
import java.awt.Rectangle;
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

public class Frame extends JFrame {
	
	private static final long serialVersionUID = -1206806827748738729L;
	private JLayeredPane mainPanel;
	private JTabbedPane tabPane;
	private Transmitter transmitter;
	private DatabaseTab defaultTab;
	private JFileChooser file;
	private GUI gui;
	private HashMap<Integer, DatabaseTab> tabs = new HashMap<Integer, DatabaseTab>();
	
	public Frame(Transmitter transmitter, GUI gui, String name, Rectangle rectangle) {
		this.transmitter = transmitter;
		this.gui = gui;
		setTitle(name);
		setBounds(rectangle);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		mainPanel = createMainPanel();
		setContentPane(mainPanel);
		setVisible(true);
		addWindowListener(new WindowListener() {
			@Override
			public void windowClosing(WindowEvent event) {
				for (int index = 0; index < tabPane.getTabCount(); index++) {
					((DatabaseTab) tabPane.getComponentAt(index)).closeConnection();
				}
				gui.closeWindow((Frame) event.getComponent());
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
//		String[] e = { "qf", "wwtg", "wer", "wt" }; //TODO Remove
//		String[] f = {"wwtg", "wer", "wt" }; //TODO Remove
//		loaddatabase("Baum", e, 0); //TODO Remove
//		loaddatabase("Zwiebel", f, 1); //TODO Remove
		file = new JFileChooser(new File("."));
		file.removeChoosableFileFilter(file.getChoosableFileFilters()[0]);
		file.setFileFilter(new FileNameExtensionFilter("Database File","db3"));
	}
	
	public JLayeredPane createMainPanel() {
		JLayeredPane mainPanel = new JLayeredPane();
		mainPanel.setLayout(new BorderLayout(0, 0));
		
		numeratedPopupMenu rightclickMenu = new numeratedPopupMenu();
		
		MenuItem newWindow = new MenuItem("In neues Fenster verschieben");
		newWindow.addActionListener(e -> {
			int index = ((numeratedPopupMenu)((MenuItem) e.getSource()).getParent()).getNumber();
			DatabaseTab tab = (DatabaseTab) tabPane.getComponentAt(index);
			if (tab.getID() != -1) {
				removeTab(tab.getID());
				gui.createFrame(getTitle(), tab, this.getBounds());
			}
			else {
				gui.createFrame(getTitle(), this.getBounds());
			}
		});
		
		MenuItem close = new MenuItem("Schließen");
		close.addActionListener(e -> {
			int index = ((numeratedPopupMenu)((MenuItem) e.getSource()).getParent()).getNumber();
			DatabaseTab tab = (DatabaseTab) tabPane.getComponentAt(index);
			if (tab.getID() != -1) {
				removeTab(tab.getID());
				tab.closeConnection();
			}
		});
		MenuItem closeOther = new MenuItem("Andere Schließen");
		closeOther.addActionListener(e -> {
			int index = ((numeratedPopupMenu)((MenuItem) e.getSource()).getParent()).getNumber();
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
		JMenu file = new JMenu("File");
		JMenuItem opendatabase = new JMenuItem("Open");
		opendatabase.addActionListener(e -> {
			selectDatabase();
		});
		JMenuItem closedatabase = new JMenuItem("Close");
		closedatabase.addActionListener(e -> {
			DatabaseTab tab = ((DatabaseTab) tabPane.getComponentAt(tabPane.getSelectedIndex()));
			if (tab.getID() != -1) {
				tab.closeConnection();
				tabs.remove(tab);
				tabPane.removeTabAt(tabPane.getSelectedIndex());
			}
		});
		menuBar.add(file);
		file.add(opendatabase);
		file.add(closedatabase);
		
		mainPanel.add(menuBar, BorderLayout.NORTH);
		mainPanel.add(tabPane, BorderLayout.CENTER);
		
		defaultTab = new DatabaseTab(-1, " Database ");
		tabPane.addTab(defaultTab.getTabName(), defaultTab);
		tabs.put(defaultTab.getID(), defaultTab);
		
		return mainPanel;
	}
	
	public void addTab(DatabaseTab tab) {
		String suffix = getSuffix(tab.getTabName());
		tab.setSuffix(suffix);
		tabPane.add(tab.getTabName(), tab);
		tabs.put(tab.getID(), tab);
		if (tabs.containsValue(defaultTab)) {
			tabPane.removeTabAt(tabPane.indexOfTab(defaultTab.getTabName()));
			tabs.remove(defaultTab.getID());
		}
		tabs.put(tab.getID(), tab);
	}
	
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
	
	public void loadDatabase(Database database, int iD) {
		DatabaseTab tab = new DatabaseTab(database, transmitter, iD);
		addTab(tab);
	}
	
//	public void showTable(String[][] table, String tableName) {
//		tabPane.getComponents();
//	}
	
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
		return suffix;
	}
	
	private void selectDatabase() {
		file.showOpenDialog(null);
		if (file.getSelectedFile() != null && file.getSelectedFile().getName().endsWith(".db3")) {
			transmitter.transmit(new Message(file.getSelectedFile(), gui.getID(this)));
		}
	}
	

	public void showTable(Table table, int iD, int frameID) {
		tabs.get(iD).setTableContent(table);
	}
}