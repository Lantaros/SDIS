package gui;

import javax.swing.JPanel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import client.Client;
import protocol.Message;
import protocol.MessageType;

public class WaitingRoom extends JPanel {

	/**
	 * Create the panel.
	 */
	public WaitingRoom(Frame frame) {
		
		JLabel lblWaitingForUser = new JLabel("Waiting for User to choose a Word...");
		lblWaitingForUser.setFont(new Font("Tahoma", Font.PLAIN, 20));
		
		JButton btnLeaveRoom = new JButton("Leave Room");
		btnLeaveRoom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setpanel(frame.mainMenu);
				
			}
		});
		
		JButton btnImReady = new JButton("I'm Ready");
		btnImReady.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Client.sendReadyToAll();
				Client.getRooms()[1].setReady(Client.clientID);
				if(Client.getRooms()[1].isEveryoneReady() && Client.getRooms()[1].getOwner()){
            		System.out.println("num cli: " + Client.getRooms()[1].getNClients());
            		Client.sendAll(new Message(MessageType.START_GAME));
            	}
			}
		});
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(56)
					.addComponent(lblWaitingForUser, GroupLayout.PREFERRED_SIZE, 339, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(55, Short.MAX_VALUE))
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap(322, Short.MAX_VALUE)
					.addComponent(btnLeaveRoom)
					.addGap(37))
				.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
					.addGap(170)
					.addComponent(btnImReady)
					.addContainerGap(191, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(130)
					.addComponent(lblWaitingForUser, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnImReady)
					.addPreferredGap(ComponentPlacement.RELATED, 49, Short.MAX_VALUE)
					.addComponent(btnLeaveRoom)
					.addGap(24))
		);
		setLayout(groupLayout);

	}

}
