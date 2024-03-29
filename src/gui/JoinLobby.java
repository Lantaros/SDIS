package gui;

import client.Client;
import game.Room;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JList;
import javax.swing.DefaultListModel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class JoinLobby extends JPanel {

	@SuppressWarnings("unused")
	private Frame frame;

	JList<Room> listofIDs;
	DefaultListModel<Room> listModel;

	/**
	 * Create the panel.
	 */
	public JoinLobby(Frame frame) {
		this.frame = frame;

		JLabel lblLobby = new JLabel("Lobby");
		lblLobby.setFont(new Font("Tahoma", Font.PLAIN, 18));

		JButton btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setpanel(frame.mainMenu);
			}
		});

		JScrollPane scrollPane = new JScrollPane();

		listofIDs = new JList<Room>();

		
		listModel = new DefaultListModel<Room>();
		listofIDs.setModel(listModel);
		
		ArrayList<Room> rooms = Client.requestAvailableRooms();

		//String[] items = { "A", "B", "C", "D" };
		for (int i = 0; i < rooms.size(); i++) {
			listModel.add(0, rooms.get(i));

		}

		scrollPane.setViewportView(listofIDs);

		JButton btnJoinSelected = new JButton("Join Selected");
		btnJoinSelected.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

                int roomID = listofIDs.getSelectedValue().getRoomId();
                System.out.println("Selected RoomID " + roomID);
                Client.connectRoom(roomID);
//
				frame.setpanel(frame.waitingRoom);
//

			}
		});
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup().addGap(195).addComponent(lblLobby))
								.addGroup(Alignment.TRAILING,
										groupLayout.createSequentialGroup().addGap(39).addComponent(btnJoinSelected)
												.addPreferredGap(ComponentPlacement.RELATED, 251, Short.MAX_VALUE)
												.addComponent(btnBack))
								.addGroup(groupLayout.createSequentialGroup().addGap(49).addComponent(scrollPane,
										GroupLayout.PREFERRED_SIZE, 337, GroupLayout.PREFERRED_SIZE)))
						.addContainerGap()));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout
				.createSequentialGroup().addContainerGap().addComponent(lblLobby).addGap(18)
				.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 170, GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
				.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING,
								groupLayout.createSequentialGroup().addComponent(btnBack).addContainerGap())
						.addGroup(Alignment.TRAILING,
								groupLayout.createSequentialGroup().addComponent(btnJoinSelected).addGap(26)))));
		setLayout(groupLayout);

	}

	void setRooms(String[] roomsIDs) {
		//clearRooms();
		
//		for (int i = 0; i < roomsIDs.length; i++) {
//			listModel.add(i, "Room " + roomsIDs[i]);
//
//		}

	}

	void clearRooms() {
		listModel.clear();

	}

}
