package de.szut.dqi12.sqlitebrowser.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
import de.szut.dqi12.sqlitebrowser.settings.Settings;

public class DatabaseTab extends JPanel {
	
	private static final long serialVersionUID = 8258966354246805261L;
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
	
	public DatabaseTab(int iD, String databaseName) {
		this.name = databaseName;
		this.iD = iD;
		createTab();
	}
	
	public DatabaseTab(Database database, Transmitter transmitter, int iD) {
		this.transmitter = transmitter;
		this.iD = iD;
		createTab();
		this.name = database.getDatabaseName();
		createTree(database.getDataTables());
		connectionLabel.setText(name + " loaded");
	}
	
	public void createTab() {
		setLayout(new BorderLayout());
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
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
		
		connectionLabel = new JLabel("not connected");
		connectionLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		connectionLabel.setPreferredSize(new Dimension(0,20));
		
		databaseTree = new JTree();
		((DefaultTreeModel) databaseTree.getModel()).setRoot(null);
		JScrollPane scrollPaneTree = new JScrollPane(databaseTree);
		scrollPaneTree.setPreferredSize(new Dimension(Settings.TREEWIDTH, 0));
		databaseTree.addTreeSelectionListener(e -> {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) databaseTree.getLastSelectedPathComponent();
			if (node != null) {
				if(node.getChildCount() == 0) {
					try {
						transmitter.transmit(new Message("select * from " + '"' + node.getUserObject() + '"', Integer.parseInt(lowerLimit.getText()), Integer.parseInt(upperLimit.getText()), iD, frameID));
					}
					catch (NumberFormatException exception) {
						transmitter.transmit(new Message("select * from " + '"' + node.getUserObject() + '"', 0, 0, iD, frameID));
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
							transmitter.transmit(new Message(queryField.getText(), Integer.parseInt(lowerLimit.getText()), Integer.parseInt(upperLimit.getText()), iD, frameID));
						}
						catch (NumberFormatException exception) {
							transmitter.transmit(new Message(queryField.getText(), 0, 0, iD, frameID));
						}
					}
					else {
						transmitter.transmit(new Message(queryField.getText(), 0, 0, iD, frameID));
					}
				}
			}
		});
		
		JButton queryButton = new JButton("Send");
		queryButton.addActionListener(e -> {
			if (!queryField.getText().equals("") && limitCheckBox.isSelected()) {
				try {
					transmitter.transmit(new Message(queryField.getText(), Integer.parseInt(lowerLimit.getText()), Integer.parseInt(upperLimit.getText()), iD, frameID));
				}
				catch (NumberFormatException exception) {
					transmitter.transmit(new Message(queryField.getText(), 0, 0, iD, frameID));
				}
			}
			else {
				transmitter.transmit(new Message(queryField.getText(), 0, 0, iD, frameID));
			}
		});
		
		lowerLimit = new JTextField();
		
        NumberFormat format = NumberFormat.getInstance();
        format.setGroupingUsed(false);
        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setAllowsInvalid(false);
        
        lowerLimit = new JFormattedTextField(formatter) {
        	protected void keyEvent(final KeyEvent e) {
        		
        	}
        };
//        	private static final long serialVersionUID = 1664905922027529040L;
//        	@Override  
//        	protected void processFocusEvent(final FocusEvent e) {  
//        		if (e.isTemporary())  
//        			return;  
//         
//        		if (e.getID() == FocusEvent.FOCUS_LOST && (getText() == null || getText().isEmpty())) 
//        			setValue(null);  
//         
//        		super.processFocusEvent(e);  
//        	}
//        };
		
//        KeyListener limitListener = new KeyListener() {
//			String old = "";
//			@Override
//			public void keyPressed(KeyEvent event) {}
//			@Override
//			public void keyReleased(KeyEvent event) {}
//			
//			@Override 
//			public void keyTyped(KeyEvent event) {
//				String s = ((JFormattedTextField) event.getComponent()).getText();
//				System.out.println(s);
//				if (old.length() == 1 && (event.getKeyChar() == '' || event.getKeyChar() == '')) {
//					((JFormattedTextField) event.getComponent()).setText("0");
//				}
//				if (event.getKeyChar() == '' || event.getKeyChar() == '') {
//					if (oneChar) {
//						((JFormattedTextField) event.getComponent()).setText("0");
//					}
//					if (((JFormattedTextField) event.getComponent()).getText().length() == 1) {
//						oneChar = true;
//					}
//				}
//				else {
//					System.out.println(((JFormattedTextField) event.getComponent()).getText().length());
//					if (((JFormattedTextField) event.getComponent()).getText().length() == 1) {
//						oneChar = true;
//					}
//					else if (((JFormattedTextField) event.getComponent()).getText().length() > 1) {
//						oneChar = false;
//					}
//				}
//				System.out.println(oneChar);
//				old = ((JFormattedTextField) event.getComponent()).getText();
//			}
//		};
        
//		lowerLimit = new JFormattedTextField(formatter);
//		lowerLimit.addKeyListener(limitListener);

		upperLimit = new JFormattedTextField(formatter);
//		upperLimit.addKeyListener(limitListener);

		tablePanel.add(scrollPaneTree, BorderLayout.CENTER);
		dataPanel.add(scrollPaneTable, BorderLayout.CENTER);
		queryPanel.add(queryField, BorderLayout.CENTER);
		queryPanel.add(queryButton, BorderLayout.EAST);
		outerLimitPanel.add(limitCheckBox, BorderLayout.WEST);
		innerLimitPanel.add(lowerLimit, 0);
		innerLimitPanel.add(upperLimit, 1);
		add(connectionLabel, BorderLayout.SOUTH);
	}
	
	private void createTree(String[] tableNames) {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(getName());
		for(String name : tableNames) {
			root.add(new DefaultMutableTreeNode(name));
		}
		((DefaultTreeModel) databaseTree.getModel()).setRoot(root);
	}
	
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
	
	public String getTabName() {
		return name;
	}
	
	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	
	public String getTabTitle() {
		return name + suffix;
	}
	
	public int getID() {
		return iD;
	}
	
	public void setFrameID(int iD) {
		frameID = iD;
	}

	public void closeConnection() {
		// TODO Auto-generated method stub
		
	}
}
