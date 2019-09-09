package com.arjav.gameoflife.client;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.arjav.gameoflife.client.net.Connect;

public class Main extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField nametextField;
	private JPasswordField passwordField;
	private JButton btnOk;
	private Connect serverCon;
	
	private Main(String title, int width, int height) {
		super(title);
		setSize(new Dimension(250, 200));
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		connectToServer();
	}
	
	private void connectToServer() {
		JLabel label = new JLabel("Connecting to server...");
		getContentPane().add(label);
		serverCon = new Connect();
		serverCon.init();
		getContentPane().remove(label);
		createAndShowGUI();
	}
	
	private void okButtonCallback(ActionEvent e) {
		System.out.println(serverCon.requestConnection(nametextField.getText(), new String(passwordField.getPassword())));
	}
	
	public static void main(String[] args) {
		new Main("The Game of Life", 500, 500);
	}
	
	private void createAndShowGUI() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {250};
		gridBagLayout.rowHeights = new int[] {150, 100};
		gridBagLayout.columnWeights = new double[]{0.0};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0};
		getContentPane().setLayout(gridBagLayout);
		
		JPanel labelAndFieldPanel = new JPanel();
		GridBagConstraints gbcLabelAndFieldPanel = new GridBagConstraints();
		gbcLabelAndFieldPanel.fill = GridBagConstraints.HORIZONTAL;
		gbcLabelAndFieldPanel.insets = new Insets(0, 0, 5, 0);
		gbcLabelAndFieldPanel.gridx = 0;
		gbcLabelAndFieldPanel.gridy = 0;
		gbcLabelAndFieldPanel.weighty = 0.7;
		getContentPane().add(labelAndFieldPanel, gbcLabelAndFieldPanel);
		GridBagLayout gblLabelAndFieldPanel = new GridBagLayout();
		gblLabelAndFieldPanel.columnWidths = new int[] {0, 0, 0};
		gblLabelAndFieldPanel.rowHeights = new int[]{0, 0};
		gblLabelAndFieldPanel.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gblLabelAndFieldPanel.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		labelAndFieldPanel.setLayout(gblLabelAndFieldPanel);
		
		JPanel labelPanel = new JPanel();
		GridBagConstraints gbcLabelPanel = new GridBagConstraints();
		gbcLabelPanel.insets = new Insets(0, 0, 0, 5);
		gbcLabelPanel.fill = GridBagConstraints.VERTICAL;
		gbcLabelPanel.gridx = 0;
		gbcLabelPanel.gridy = 0;
		gbcLabelPanel.weightx = 0.3;
		gbcLabelPanel.weighty = 1.0;
		labelAndFieldPanel.add(labelPanel, gbcLabelPanel);
		labelPanel.setLayout(new GridLayout(2, 1, 0, 0));
		
		JLabel nameLabel = new JLabel("Username: ");
		labelPanel.add(nameLabel);
		
		JLabel passwordLabel = new JLabel("Password: ");
		labelPanel.add(passwordLabel);
		
		JPanel fieldPanel = new JPanel();
		GridBagConstraints gbcFieldPanel = new GridBagConstraints();
		gbcFieldPanel.fill = GridBagConstraints.VERTICAL;
		gbcFieldPanel.gridx = 1;
		gbcFieldPanel.gridy = 0;
		gbcFieldPanel.weightx = 0.7;
		gbcFieldPanel.weighty = 1.0;
		labelAndFieldPanel.add(fieldPanel, gbcFieldPanel);
		fieldPanel.setLayout(new GridLayout(0, 1, 0, 0));
		
		nametextField = new JTextField();
		fieldPanel.add(nametextField);
		nametextField.setColumns(10);
		
		passwordField = new JPasswordField();
		fieldPanel.add(passwordField);
		
		btnOk = new JButton("OK");
		GridBagConstraints gbc_btnOk = new GridBagConstraints();
		gbc_btnOk.gridx = 0;
		gbc_btnOk.gridy = 1;
		gbc_btnOk.weighty = 0.3;
		btnOk.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				okButtonCallback(e);
			}
			
		});
		getContentPane().add(btnOk, gbc_btnOk);
		setVisible(true);
	}

}
