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
	
	public JLabel lblWinOrLose;

	/**
	 * Create the panel.
	 */
	public WaitingRoom(Frame frame) {
		
		JLabel lblWaitingForUser = new JLabel("Waiting for Other Users...");
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
				Client.currentRoom.setReady(Client.clientID);
				if(Client.currentRoom.isEveryoneReady() && Client.currentRoom.getOwner() && Client.currentRoom.getNClients()>1){
            		System.out.println("num cli: " + Client.currentRoom.getNClients());
            		Client.sendAll(new Message(MessageType.START_GAME));
            		Launcher.getFrame().setpanel(Launcher.getFrame().gamePanel);
            	}
			}
		});
		
		lblWinOrLose = new JLabel("");
		lblWinOrLose.setFont(new Font("Tahoma", Font.PLAIN, 18));
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap(322, Short.MAX_VALUE)
					.addComponent(btnLeaveRoom)
					.addGap(37))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(170)
					.addComponent(btnImReady)
					.addContainerGap(199, Short.MAX_VALUE))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(56)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(lblWaitingForUser, GroupLayout.PREFERRED_SIZE, 339, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblWinOrLose))
					.addContainerGap(55, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(34)
					.addComponent(lblWinOrLose)
					.addGap(61)
					.addComponent(lblWaitingForUser, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
					.addGap(32)
					.addComponent(btnImReady)
					.addPreferredGap(ComponentPlacement.RELATED, 49, Short.MAX_VALUE)
					.addComponent(btnLeaveRoom)
					.addGap(24))
		);
		setLayout(groupLayout);

	}
	
	public void setVictory(boolean hasWon) {
		if (hasWon)
			lblWinOrLose.setText("Your team won the last game");
		else
			lblWinOrLose.setText("Your team lost the last game");

	}
	
	public void setWordGuessed(boolean wordGuessed) {
		if (wordGuessed)
			lblWinOrLose.setText("Someone has guessed your word");
		else
			lblWinOrLose.setText("No one has guessed your word");

	}
}
