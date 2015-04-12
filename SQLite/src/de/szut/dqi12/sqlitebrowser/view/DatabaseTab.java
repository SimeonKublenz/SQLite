package de.szut.dqi12.sqlitebrowser.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.NumberFormatter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import de.szut.dqi12.sqlitebrowser.model.Database;
import de.szut.dqi12.sqlitebrowser.model.Message;
import de.szut.dqi12.sqlitebrowser.model.Table;

public class DatabaseTab extends JPanel {
	
	private static final long serialVersionUID = 8258966354246805261L;
	private JSplitPane splitPane;
	private String name;
	private String suffix = "";
	private JLabel connectionLabel;
	private JTable dataTable;
	private JTree databaseTree;
	private JTextField lowerLimit;
	private JFormattedTextField upperLimit;
	private Transmitter transmitter;
	private int iD;
	private int frameID;
	
	/**
	 * Der Konstruktor des DefaultDatabaseTabs
	 * @param iD die ID des Tabs
	 * @param databaseName der Name der Datenbank
	 * @param treeWidth Die Breite des Baumes
	 */
	public DatabaseTab(int iD, String databaseName, int treeWidth) {
		this.name = databaseName;
		this.iD = iD;
		createTab(treeWidth);
	}
	
	/**
	 * Der Konstruktor des DatabaseTabs
	 * @param database die Datenbank welche angezeigt werden soll
	 * @param transmitter der Name der Datenbank
	 * @param iD die ID des Tabs
	 * @param treeWidth Die Breite des Baumes
	 */
	public DatabaseTab(Database database, Transmitter transmitter, int iD, int treeWidth) {
		this.transmitter = transmitter;
		this.iD = iD;
		createTab(treeWidth);
		this.name = database.getDatabaseName();
		createTree(database.getDataTables());
		connectionLabel.setText(name + " geladen");
	}
	
	/**
	 * Erstellt den Tab
	 * @param treeWidth Die Breite des Baumes
	 */
	public void createTab(int treeWidth) {
		setLayout(new BorderLayout());
		
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		JPanel tablePanel = new JPanel();
		tablePanel.setLayout(new BorderLayout(0, 0));
		JPanel dataPanel = new JPanel();
		dataPanel.setLayout(new BorderLayout(0, 2));
		JPanel queryPanel = new JPanel();
		queryPanel.setLayout(new BorderLayout(0, 0));
		JPanel outerLimitPanel = new JPanel();
		outerLimitPanel.setLayout(new BorderLayout(3, 0));
		JPanel innerLimitPanel = new JPanel();
		innerLimitPanel.setLayout(new GridLayout(1, 2, 3, 0));
		
		add(splitPane, BorderLayout.CENTER);
		splitPane.add(tablePanel, JSplitPane.LEFT);
		splitPane.add(dataPanel, JSplitPane.RIGHT);
		dataPanel.add(queryPanel, BorderLayout.NORTH);
		dataPanel.add(outerLimitPanel, BorderLayout.SOUTH);
		outerLimitPanel.add(innerLimitPanel, BorderLayout.CENTER);
		
		connectionLabel = new JLabel("nicht verbunden");
		connectionLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		connectionLabel.setPreferredSize(new Dimension(0,20));
		
		databaseTree = new JTree();
		((DefaultTreeModel) databaseTree.getModel()).setRoot(null);
		JScrollPane scrollPaneTree = new JScrollPane(databaseTree);
		scrollPaneTree.setPreferredSize(new Dimension(treeWidth, 0));
		databaseTree.addTreeSelectionListener(e -> {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) databaseTree.getLastSelectedPathComponent();
			if (node != null) {
				if(node.getChildCount() == 0) {
					try {
						transmitter.transmit(new Message("select * from " + '"' + node.getUserObject() + '"', Integer.parseInt(lowerLimit.getText()), Integer.parseInt(upperLimit.getText()), iD, frameID, false));
					}
					catch (NumberFormatException exception) {
						transmitter.transmit(new Message("select * from " + '"' + node.getUserObject() + '"', 0, 0, iD, frameID, false));
					}
				}
			}
		});
		
		
		dataTable = new JTable();
		JScrollPane scrollPaneTable = new JScrollPane(dataTable);
		
		JCheckBox limitCheckBox = new JCheckBox();
		limitCheckBox.setText("LIMIT");
		
		JTextField queryField = new JTextField();
		queryField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent event) {
				if (event.getKeyCode() == KeyEvent.VK_ENTER) {
					if (!queryField.getText().equals("") && limitCheckBox.isSelected()) {
						try {
							transmitter.transmit(new Message(queryField.getText(), Integer.parseInt(lowerLimit.getText()), Integer.parseInt(upperLimit.getText()), iD, frameID, true));
						}
						catch (NumberFormatException exception) {
							transmitter.transmit(new Message(queryField.getText(), 0, 0, iD, frameID, true));
						}
					}
					else {
						transmitter.transmit(new Message(queryField.getText(), 0, 0, iD, frameID, true));
					}
				}
			}
		});
		
		JButton queryButton = new JButton("Senden");
		queryButton.addActionListener(e -> {
			if (!queryField.getText().equals("") && limitCheckBox.isSelected()) {
				try {
					transmitter.transmit(new Message(queryField.getText(), Integer.parseInt(lowerLimit.getText()), Integer.parseInt(upperLimit.getText()), iD, frameID, true));
				}
				catch (NumberFormatException exception) {
					transmitter.transmit(new Message(queryField.getText(), 0, 0, iD, frameID, true));
				}
			}
			else {
				transmitter.transmit(new Message(queryField.getText(), 0, 0, iD, frameID, true));
			}
		});
		
		lowerLimit = new JTextField();
		
        NumberFormat format = NumberFormat.getInstance();
        format.setGroupingUsed(false);
        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setAllowsInvalid(false);
        
        lowerLimit = new JFormattedTextField(formatter);
		upperLimit = new JFormattedTextField(formatter);

		tablePanel.add(scrollPaneTree, BorderLayout.CENTER);
		dataPanel.add(scrollPaneTable, BorderLayout.CENTER);
		queryPanel.add(queryField, BorderLayout.CENTER);
		queryPanel.add(queryButton, BorderLayout.EAST);
		outerLimitPanel.add(limitCheckBox, BorderLayout.WEST);
		innerLimitPanel.add(lowerLimit, 0);
		innerLimitPanel.add(upperLimit, 1);
		add(connectionLabel, BorderLayout.SOUTH);
	}
	
	/**
	 * Erstellt den Baum
	 * @param tableNames Die Namen der Tabellen
	 */
	private void createTree(String[] tableNames) {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(getName());
		for(String name : tableNames) {
			root.add(new DefaultMutableTreeNode(name));
		}
		((DefaultTreeModel) databaseTree.getModel()).setRoot(root);
	}
	
	/**
	 * Setzt den Tabelleninhalt
	 * @param table Die anzuzeigende Tabelle
	 */
	public void setTableContent(Table table) {
		String[][] content = table.getTableContent();
		dataTable.setModel(new DefaultTableModel(content.length, content[0].length) {
			private static final long serialVersionUID = 5005712245985643723L;

			@Override
			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return false;
			}
		});
		((DefaultTableModel) dataTable.getModel()).setColumnIdentifiers(table.getTableHeaders());
		for (int x = 0; x < content.length; x++) {
			for (int y = 0; y < content[x].length; y++) {
				dataTable.getModel().setValueAt(content[x][y], x, y);
			}
		}
	}
	
	/**
	 * Gibt den namen des Tabs zurück
	 * @return Der Name des Tabs
	 */
	public String getTabName() {
		return name;
	}
	
	/**
	 * Gibt das Suffix des Tabs zurück
	 * @return
	 */
	public String getSuffix() {
		return suffix;
	}
	
	/**
	 * Setzt das Suffix des Tabs
	 * @param suffix Das Suffix des Tabs
	 */
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	
	/**
	 * Gibt den Titel des Tabs zurück
	 * @return Titel des Tabs (Name + Suffix)
	 */
	public String getTabTitle() {
		return name + suffix;
	}
	
	/**
	 * Gibt die ID des Tabs zurück
	 * @return Die ID des Tabs
	 */
	public int getID() {
		return iD;
	}
	
	/**
	 * Setzt die FrameID auf dessen Frame sich der Tab befindet
	 * @param iD Die FrameID
	 */
	public void setFrameID(int iD) {
		frameID = iD;
	}
	
	/**
	 * Schließt die Connection
	 */
	public void closeConnection() {
		transmitter.transmit(new Message(iD));
	}
	
	/**
	 * Gibt die breite des Baumes zurück
	 * @return Die Breite des Baumes
	 */
	public int getTreeWidth() {
		return splitPane.getDividerLocation();
	}
}
