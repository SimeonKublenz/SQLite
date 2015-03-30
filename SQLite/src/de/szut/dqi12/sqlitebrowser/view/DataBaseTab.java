package de.szut.dqi12.sqlitebrowser.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import de.szut.dqi12.sqlitebrowser.settings.Settings;

public class DataBaseTab extends JPanel {
	
	private static final long serialVersionUID = 8258966354246805261L;
	private String name;
	private JLabel connection;
	private JTable dataTable;
	private JTree dataBaseTree;
	
	public DataBaseTab() {
		createTab();
	}
	
	public DataBaseTab(String dataBaseName, String[] tables) {
		createTab();
		this.name = dataBaseName;
		createTree(tables);
	}
	
	public void createTab() {
		setLayout(new BorderLayout());
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		JPanel tablePanel = new JPanel();
		tablePanel.setLayout(new BorderLayout(0, 0));
		JPanel dataPanel = new JPanel();
		dataPanel.setLayout(new BorderLayout(0, 2));
		JPanel outerLimitPanel = new JPanel();
		outerLimitPanel.setLayout(new BorderLayout(3,0));
		JPanel innerLimitPanel = new JPanel();
		innerLimitPanel.setLayout(new GridLayout(1, 2, 3, 0));
		
		add(splitPane, BorderLayout.CENTER);
		splitPane.add(tablePanel, JSplitPane.LEFT);
		splitPane.add(dataPanel, JSplitPane.RIGHT);
		dataPanel.add(outerLimitPanel, BorderLayout.SOUTH);
		outerLimitPanel.add(innerLimitPanel, BorderLayout.CENTER);
		
		connection = new JLabel("not connected");
		connection.setHorizontalAlignment(SwingConstants.RIGHT);
		connection.setPreferredSize(new Dimension(0,20));
		
		dataBaseTree = new JTree();
		((DefaultTreeModel) dataBaseTree.getModel()).setRoot(null);
		JScrollPane scrollPaneTree = new JScrollPane(dataBaseTree);
		scrollPaneTree.setPreferredSize(new Dimension(Settings.TREEWIDTH, 0));
		dataBaseTree.addTreeSelectionListener(e -> {
			DefaultMutableTreeNode n = (DefaultMutableTreeNode) dataBaseTree.getLastSelectedPathComponent();
			if (n != null) {
				if(n.getChildCount() == 0){
					System.out.println(n.getUserObject());
				}
			}
		});
		
		dataTable = new JTable();
		JScrollPane scrollPaneTable = new JScrollPane(dataTable);
		
		
		JTextField querryField = new JTextField();
		
		JTextField lowerLimit = new JTextField();

		JTextField upperLimit = new JTextField();
		
		JCheckBox limitCheckBox = new JCheckBox();
		limitCheckBox.setText("LIMIT");

		tablePanel.add(scrollPaneTree, BorderLayout.CENTER);
		dataPanel.add(scrollPaneTable, BorderLayout.CENTER);
		dataPanel.add(querryField, BorderLayout.NORTH);
		outerLimitPanel.add(limitCheckBox, BorderLayout.WEST);
		innerLimitPanel.add(lowerLimit, 0);
		innerLimitPanel.add(upperLimit, 1);
		add(connection, BorderLayout.SOUTH);
	}
	
	private void createTree(String[] tableNames) {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(getName());
		for(String name : tableNames) {
			root.add(new DefaultMutableTreeNode(name));
		}
		((DefaultTreeModel) dataBaseTree.getModel()).setRoot(root);
	}
	
	public String getTabName() {
		return name;
	}
	
	public void setTableContent(String[][] content) {
		
	}
}
