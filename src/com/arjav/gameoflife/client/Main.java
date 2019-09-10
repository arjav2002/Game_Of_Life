package com.arjav.gameoflife.client;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.lwjgl.glfw.GLFW;

import com.arjav.gameoflife.client.game.Game;
import com.arjav.gameoflife.client.gl.WindowNotCreatedException;
import com.arjav.gameoflife.client.net.Connect;
import com.arjav.gameoflife.client.game.Type;

public class Main {
	
	private JTextField nametextField;
	private JPasswordField passwordField;
	private JComboBox<String> comboBox;
	private DefaultComboBoxModel<String> comboBoxModel;
	private ArrayList<String> serverList;
	private JButton btnOk;
	private Connect serverCon;
	private JFrame frame;;
	private Thread serverSearchThread;
	
	private Main(String title, int width, int height) {
		frame = new JFrame(title);
		frame.setSize(new Dimension(250, 200));
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		connectToServer();
	}
	
	private void connectToServer() {
		JLabel label = new JLabel("Connecting to server...");
		frame.getContentPane().add(label);
		serverCon = new Connect();
		serverCon.init();
		frame.getContentPane().remove(label);
		createAndShowGUI();
		serverSearchThread = new Thread(new Runnable() {
			@Override
			public void run() {
				serverCon.findServers(serverList, comboBoxModel);
			}
		});
		serverCon.searchForServers(true);
		serverSearchThread.start();
	}
	
	private void okButtonCallback(ActionEvent e) {
		String password = new String(passwordField.getPassword());
		if(!nametextField.getText().equals("") && !password.equals("")) {
			if(serverCon.requestConnection(nametextField.getText(), password, (String)comboBox.getSelectedItem())) {
				// we can go onto the openGL rendering now
				if(!GLFW.glfwInit()) {
					nametextField.setText("Failed to initialise openGL");
					serverCon.sendMessage("initFailure");
				}
				else {
					try {
						serverCon.searchForServers(false);
						serverSearchThread.join();
					} catch (InterruptedException e1) {
						System.err.println("not able to stop server searching thread");
						e1.printStackTrace();
					}
					try {
						Game game = new Game("The Game of Life!", 640, 480, serverCon);
						frame.setVisible(false);
						// frame.dispose(); //TODO why is this causing an error
						// if this is called after glfwInit, it causes a BadWindow error
						game.init();
						game.start();
					}
					catch(WindowNotCreatedException e2) {
						serverCon.sendMessage("initFailure");
						e2.printStackTrace();
					}
				}
				
			}
			else {
				nametextField.setText("Wrong password entered");
			}
		}
		else {
			nametextField.setText("Enter something in both fields");
		}
	}
	
	public static void main(String[] args) {
		new Main("The Game of Life", 500, 500);
	}
	
	private void createAndShowGUI() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {250};
		gridBagLayout.rowHeights = new int[] {150, 50, 50};
		gridBagLayout.columnWeights = new double[]{0.0};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0};
		frame.getContentPane().setLayout(gridBagLayout);
		
		JPanel labelAndFieldPanel = new JPanel();
		GridBagConstraints gbcLabelAndFieldPanel = new GridBagConstraints();
		gbcLabelAndFieldPanel.fill = GridBagConstraints.HORIZONTAL;
		gbcLabelAndFieldPanel.insets = new Insets(0, 0, 5, 0);
		gbcLabelAndFieldPanel.gridx = 0;
		gbcLabelAndFieldPanel.gridy = 0;
		gbcLabelAndFieldPanel.weighty = 0.7;
		frame.getContentPane().add(labelAndFieldPanel, gbcLabelAndFieldPanel);
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
		
		comboBoxModel = new DefaultComboBoxModel<String>();
		comboBox = new JComboBox<String>(comboBoxModel);
		serverList = new ArrayList<String>();
		GridBagConstraints gbcComboBox = new GridBagConstraints();
		gbcComboBox.gridx = 0;
		gbcComboBox.gridy = 1;
		gbcComboBox.weighty = 0.15;
		frame.getContentPane().add(comboBox, gbcComboBox);
		
		btnOk = new JButton("OK");
		GridBagConstraints gbcBtnOk = new GridBagConstraints();
		gbcBtnOk.gridx = 0;
		gbcBtnOk.gridy = 2;
		gbcBtnOk.weighty = 0.15;
		btnOk.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				okButtonCallback(e);
			}
			
		});
		frame.getContentPane().add(btnOk, gbcBtnOk);
		
		frame.setVisible(true);
	}


}
