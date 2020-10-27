/************************************************************
** Team Randomizer App - app for randomize players in groups
**
** begin                : 16 Oct 2020
** copyright            : (C) 2020 by CRiMER
** email                : crimer.dev@gmail.com
** notes:				: TeamRandomizerWindow - form for choice and randomize players to groups
*************************************************************/
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.*; // File
import java.util.Scanner; // Scanner class to read text files
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TeamRandomizerWindow extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	int 				count_team 			= 1;	// number of groups
	int 				count_players		= 0;	// number of players
	int 				count_active		= 0;	// number of active players
	JTable 				tGroups;					// table team
	List<JCheckBox>		alCheckBoxPlayers;			// arraylist checkbox
	Box 				boxCheckBoxPlayers;			// box checkbox players
	EditPlayersWindow 	cEditPlayers;				// edit players list window
	
	public TeamRandomizerWindow() {
		super("Team randomizer by CRiMER v0.1"); // title window
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // close app when window close
		getContentPane().setBackground(Color.LIGHT_GRAY);
		
		// Components
		JLabel lblPlayers = new JLabel("Players:");
		JLabel lblCountTeam = new JLabel("Count team:");
		
		SpinnerNumberModel snmCountTeam = new SpinnerNumberModel(count_team, 1, 100, 1);
		JSpinner spnCountTeam = new JSpinner(snmCountTeam);		
		spnCountTeam.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				count_team = snmCountTeam.getNumber().intValue();
				updateTableGroups(); // repaint table when change number of groups
			}
		});
		
		JButton btnEditPlayers = new JButton("Edit");
		btnEditPlayers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String[] playersName = new String[count_players];
				for (int i = 0; i < alCheckBoxPlayers.size(); i++) {
					playersName[i] = alCheckBoxPlayers.get(i).getText();
				}
				cEditPlayers.showEditPlayersWindow(playersName);
			}
		});
		JButton btnRnd = new JButton("RND!");
		btnRnd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (count_active == 0) return;
				updateTableGroups();
				Integer[] arr = new Integer[count_active]; // from 0 to count_active
				int num = 0;
				for (int i = 0; i < alCheckBoxPlayers.size(); i++) {
					if (alCheckBoxPlayers.get(i).isSelected()) {
						arr[num] = i;
						num++;
					}
				}
				Collections.shuffle(Arrays.asList(arr));
				num = 0;
				for (int i = 0; i < tGroups.getRowCount(); i++) {
					if (num >= count_active) break;
					for (int j = 0; j < tGroups.getColumnCount(); j++) {
						if (num >= count_active) break;
						tGroups.getModel().setValueAt(alCheckBoxPlayers.get(arr[num]).getText(), i, j);
						num++;
					}
				}
			}
		});
		
		alCheckBoxPlayers = new ArrayList<>();
		try {
			File fPlayersList = new File("PlayersList.txt");
			if (fPlayersList.exists()) {
				Scanner myReader = new Scanner(fPlayersList);
				while (myReader.hasNextLine()) {
					String data = myReader.nextLine();
					alCheckBoxPlayers.add(new JCheckBox(data));
				}
				myReader.close();
			}
		} catch (FileNotFoundException e) {
			System.out.println("Error read file.");
			e.printStackTrace();
		}
		boxCheckBoxPlayers = Box.createVerticalBox();
		for (JCheckBox element : alCheckBoxPlayers) {
			boxCheckBoxPlayers.add(element);
			count_players++;
			element.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent arg0) {
					count_active += (element.isSelected()?1:-1);
					updateTableGroups();
				}
			});
		}
		JScrollPane spCheckBoxes = new JScrollPane(boxCheckBoxPlayers);
		
		
		tGroups = new JTable();
		updateTableGroups();
		JScrollPane spTable = new JScrollPane(tGroups);
		
		// Layout
		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setAutoCreateGaps(true); // Turn on automatically adding gaps between components
		layout.setAutoCreateContainerGaps(true); // Turn on automatically creating gaps between components that touch
												 // the edge of the container and the container
		// Create horizontal group
		GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup(); // Create a sequential group for the horizontal axis
			GroupLayout.ParallelGroup phGroupLeft = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
				phGroupLeft.addComponent(lblPlayers);
				phGroupLeft.addComponent(spCheckBoxes);
				phGroupLeft.addComponent(btnEditPlayers, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);
			hGroup.addGroup(phGroupLeft);
			GroupLayout.ParallelGroup phGroupRight = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
				GroupLayout.SequentialGroup pvGroupRightUp = layout.createSequentialGroup();
					pvGroupRightUp.addComponent(lblCountTeam);
					pvGroupRightUp.addComponent(spnCountTeam);
				phGroupRight.addGroup(pvGroupRightUp);
				phGroupRight.addComponent(spTable);
				phGroupRight.addComponent(btnRnd, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);
			hGroup.addGroup(phGroupRight);
		layout.setHorizontalGroup(hGroup);
		
		// Create vertical group
		GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
			GroupLayout.ParallelGroup pvGroupUp = layout.createParallelGroup(GroupLayout.Alignment.BASELINE);	
				pvGroupUp.addComponent(lblPlayers).addComponent(lblCountTeam).addComponent(spnCountTeam);
			vGroup.addGroup(pvGroupUp);
			GroupLayout.ParallelGroup pvGroupMiddle = layout.createParallelGroup(GroupLayout.Alignment.CENTER);
				pvGroupMiddle.addComponent(spCheckBoxes);
				pvGroupMiddle.addComponent(spTable);
			vGroup.addGroup(pvGroupMiddle);	
			GroupLayout.ParallelGroup pvGroupDown = layout.createParallelGroup(GroupLayout.Alignment.BASELINE);	
				pvGroupDown.addComponent(btnEditPlayers).addComponent(btnRnd);
			vGroup.addGroup(pvGroupDown);
		layout.setVerticalGroup(vGroup);
		
		// create players edit window
		cEditPlayers = new EditPlayersWindow(this);
		cEditPlayers.setVisible(false);
		cEditPlayers.pack();
	}
	
	private void updateTableGroups() {
		tGroups.setModel(new DefaultTableModel((int)Math.ceil((double)count_active/(double)count_team), count_team));
	}
	
	public void savePlayersList(String[] newPlayersList) {
		if (newPlayersList == null) return;
		
		alCheckBoxPlayers.clear();
		boxCheckBoxPlayers.removeAll();
		
		count_players = newPlayersList.length;
		for (int i = 0; i < newPlayersList.length; i++) {
			alCheckBoxPlayers.add(new JCheckBox(newPlayersList[i]));
			boxCheckBoxPlayers.add(alCheckBoxPlayers.get(i));
		}
		for (JCheckBox element : alCheckBoxPlayers) {
			element.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent arg0) {
					count_active += (element.isSelected()?1:-1);
					updateTableGroups();
				}
			});
		}
		
		try {
			FileWriter myWriter = new FileWriter("PlayersList.txt");
			for (int i = 0; i < newPlayersList.length; i++) {
				myWriter.write(newPlayersList[i]+"\n");
			}
			myWriter.close();
	    } catch (IOException e) {
	    	System.out.println("Error save file.");
	    	e.printStackTrace();
	    }
		
		// update frame
		validate();
		repaint();
		
		count_active = 0;
		updateTableGroups();
	}
	
	public static void main(String[] args) {
		TeamRandomizerWindow TeamRandomizerApp = new TeamRandomizerWindow();
		TeamRandomizerApp.setVisible(true); // set visible - true
		TeamRandomizerApp.pack(); // optimal size window		
	}
}