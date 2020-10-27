/************************************************************
** EditPlayersWindow  -  Form for edit players list
**
** author	            : 21 Oct 2020 (C) 2020 by CRiMER (crimer.dev@gmail.com)
*************************************************************/
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

public class EditPlayersWindow extends JDialog {
	
	private static final long serialVersionUID = 1L;
	
	JTable 					tPlayers;
	TableModelListener		tmlPlayers;
	TeamRandomizerWindow	trwMainWindow;
	
	public EditPlayersWindow(TeamRandomizerWindow mainWindow) {
		setModal(true);
		setUndecorated(true);
		getContentPane().setBackground(Color.GRAY);
		
		trwMainWindow = mainWindow;
		
		tPlayers = new JTable();
		tPlayers.setModel(new DefaultTableModel(new String[][] {}, new String[] {"Players list:"}));
		JScrollPane spTable = new JScrollPane(tPlayers);
		
		JButton btnOk = new JButton("Ok");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (tPlayers.getSelectedRow() < tPlayers.getRowCount() && tPlayers.getSelectedRow() >= 0
						|| tPlayers.getSelectedColumn() == 0)
					tPlayers.getCellEditor(tPlayers.getSelectedRow(), tPlayers.getSelectedColumn()).stopCellEditing();
				
				String[] playersName;
				if (tPlayers.getRowCount() > 1) {
					playersName = new String[tPlayers.getRowCount()-1];
					for (int i = 0; i < tPlayers.getRowCount()-1; i++) {
						playersName[i] = tPlayers.getValueAt(i, 0).toString();
					}
				} else {
					playersName = new String[0];
				}
				setVisible(false);
				mainWindow.savePlayersList(playersName);
			}
		});
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
			}
		});
		
		// Layouts
		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setAutoCreateGaps(true); // Turn on automatically adding gaps between components
		layout.setAutoCreateContainerGaps(true); // Turn on automatically creating gaps between components that touch
												 // the edge of the container and the container
		// Create horizontal group
		GroupLayout.ParallelGroup hGroup = layout.createParallelGroup(GroupLayout.Alignment.CENTER); // Create a sequential group for the horizontal axis
			hGroup.addComponent(spTable);
			GroupLayout.SequentialGroup phGroup = layout.createSequentialGroup();	
				phGroup.addComponent(btnOk, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(btnCancel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);
			hGroup.addGroup(phGroup);
		layout.setHorizontalGroup(hGroup);
		
		// Create vertical group
		GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup(); // Create a sequential group for the vertical axis
			vGroup.addComponent(spTable);
			GroupLayout.ParallelGroup pvGroup = layout.createParallelGroup(GroupLayout.Alignment.BASELINE);	
				pvGroup.addComponent(btnOk).addComponent(btnCancel);
			vGroup.addGroup(pvGroup);
		layout.setVerticalGroup(vGroup);
	}
	
	public void showEditPlayersWindow(String[] playersList) {
		setLocationRelativeTo(trwMainWindow); // locate dialog to center main window
		repaintPlayersList(playersList);
		setVisible(true);
		
	}
	
	private void repaintPlayersList(String[] playersList) {
		String[] nameColumn = {"Players list:"};
		String[][] nameList = new String[playersList.length+1][1];
		for (int i = 0; i < playersList.length; i++) {
			nameList[i][0] = playersList[i];
		}
		nameList[playersList.length][0] = "";
		
		tPlayers.setModel(new DefaultTableModel(nameList, nameColumn));
		tPlayers.getModel().addTableModelListener(new TableModelListener() {
			public void tableChanged(TableModelEvent event) {
				List<String> newPlayersArrayList = new ArrayList<String>();
				for (int i = 0; i < tPlayers.getRowCount(); i++) {
					if (!tPlayers.getValueAt(i, 0).toString().equals("")) {
						newPlayersArrayList.add(tPlayers.getModel().getValueAt(i, 0).toString());
					}
				}
				String[] newPlayersList = new String[newPlayersArrayList.size()];
				for (int i = 0; i < newPlayersArrayList.size(); i++) {
					newPlayersList[i] = newPlayersArrayList.get(i);
				}
				repaintPlayersList(newPlayersList);
			}
		});
	}
}
