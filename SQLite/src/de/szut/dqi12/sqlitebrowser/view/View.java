package de.szut.dqi12.sqlitebrowser.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.SwingConstants;

import settings.*;

public class View extends JFrame {
	
	private static final long serialVersionUID = -1206806827748738729L;
	private JPanel mainPanel;
	
	
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
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout(0, 0));
		JPanel menuPanel = new JPanel();
		menuPanel.setLayout(new GridLayout(2, 1));
		JPanel dataPanel = new JPanel();
		dataPanel.setLayout(new BorderLayout(0, 0));
		JPanel limitPanel = new JPanel();
		limitPanel.setLayout(new GridLayout(1, 3, 0, 10));
		panel.add(menuPanel, SwingConstants.NORTH);
		panel.add(dataPanel, SwingConstants.CENTER);
		dataPanel.add(limitPanel, SwingConstants.SOUTH);
		
		
		JLabel connection = new JLabel("not connected");
		connection.setHorizontalAlignment(SwingConstants.RIGHT);
		
		JTree dataBaseTree = new JTree();
		
		JMenuBar menuBar = new JMenuBar();
		JMenu file = new JMenu();
		JMenu edit = new JMenu();
		JMenu help = new JMenu();
		JMenuItem openDataBase = new JMenuItem();
		menuBar.add(file);
		menuBar.add(edit);
		menuBar.add(help);
		file.add(openDataBase);
		
		panel.add(connection, BorderLayout.SOUTH);
		panel.add(dataBaseTree, BorderLayout.WEST);
		dataPanel.add()
		
		return panel;
	}
}