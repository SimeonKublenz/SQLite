package de.szut.dqi12.sqlitebrowser.view;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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

public class Frame extends JFrame {
	
	private static final long serialVersionUID = -1206806827748738729L;
	private JLayeredPane mainPanel;
	private JTabbedPane tabPane;
	private Transmitter transmitter;
	private DatabaseTab defaultTab;
	private JFileChooser file;
	private HashMap<Integer, DatabaseTab> tabs = new HashMap<Integer, DatabaseTab>();
	
	public Frame(Transmitter transmitter, String name, Rectangle rectangle) {
		this.transmitter = transmitter;
		setTitle(name);
		setBounds(rectangle);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainPanel = createMainPanel();
		setContentPane(mainPanel);
		setVisible(true);
		String[] e = { "qf", "wwtg", "wer", "wt" }; //TODO Remove
		String[] f = {"wwtg", "wer", "wt" }; //TODO Remove
		loaddatabase("Baum", e, 0); //TODO Remove
		loaddatabase("Zwiebel", f, 1); //TODO Remove
		file = new JFileChooser(new File("."));
		file.setFileFilter(new FileNameExtensionFilter("database file","db3"));
	}
	
	public JLayeredPane createMainPanel() {
		JLayeredPane mainPanel = new JLayeredPane();
		mainPanel.setLayout(new BorderLayout(0, 0));
		
		PopupMenu rightclickMenu = new PopupMenu();
		MenuItem newWindow = new MenuItem("In neues Fenster verschieben");
		MenuItem close = new MenuItem("Schlieﬂen");
		MenuItem closeOther = new MenuItem("Andere Schlieﬂen");
		MenuItem closeAll = new MenuItem("Alle Schlieﬂen");
		rightclickMenu.add(newWindow);
		rightclickMenu.addSeparator();
		rightclickMenu.add(close);
		rightclickMenu.add(closeOther);
		rightclickMenu.add(closeAll);
		
		add(rightclickMenu);
		enableEvents(AWTEvent.MOUSE_EVENT_MASK);
		
		tabPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
		tabPane.removeMouseListener(tabPane.getMouseListeners()[0]);
		tabPane.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseClicked(MouseEvent event) {
				JTabbedPane component = (JTabbedPane) event.getComponent();
				if (event.getButton() == MouseEvent.BUTTON3) {
					// Men¸liste zum "In neues Fenster verschieben", "Schlieﬂen", "Alle Schlieﬂen"
					rightclickMenu.show(component, event.getX(),
							event.getY());
				} else {
					for (int i = 0; i < component.getTabCount(); i++) {
						if (component.getBoundsAt(i).contains(event.getPoint())) {
							component.setSelectedIndex(i);
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
		JMenu edit = new JMenu("Edit");
		JMenu help = new JMenu("Help");
		JMenuItem opendatabase = new JMenuItem("Open");
		opendatabase.addActionListener(e -> {
			selectDatabase();
		});
		menuBar.add(file);
		menuBar.add(edit);
		menuBar.add(help);
		file.add(opendatabase);
		
		mainPanel.add(menuBar, BorderLayout.NORTH);
		mainPanel.add(tabPane, BorderLayout.CENTER);
		
		defaultTab = new DatabaseTab(" database ");
		tabPane.addTab(defaultTab.getName(), defaultTab);
		
		return mainPanel;
	}
	
	public void addTab(DatabaseTab tab) {
		tabPane.add(tab.getTabName(), tab);
		if (tabs.isEmpty()) {
			tabPane.removeTabAt(tabPane.indexOfTab(defaultTab.getName()));
		}
		tabs.put(tab.getID(), tab);
	}
	
	public void removeTab(int index) {
		tabPane.removeTabAt(index);
		tabs.remove(tabPane.getTabComponentAt(index));
		if (tabs.isEmpty()) {
			tabPane.addTab(defaultTab.getName(), defaultTab);
		}
	}
	
	public void loaddatabase(String databaseName, String[] tables, int iD) {
		DatabaseTab tab = new DatabaseTab(databaseName, tables, transmitter, iD);
		addTab(tab);
	}
	
	public void showTable(String[][] table, String tableName) {
		tabPane.getComponents();
	}
	
	private void selectDatabase() {
		file.showOpenDialog(null);
		transmitter.transmit(file.getSelectedFile());
	}
}