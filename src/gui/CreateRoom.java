package gui;

import javax.swing.JPanel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;

import client.Client;

import javax.swing.JButton;
import javax.swing.JFrame;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class CreateRoom extends JPanel {
	private JTextField roomNameInput;

	/**
	 * Create the panel.
	 */
	public CreateRoom(Frame frame) {
		
		JLabel lblRoomName = new JLabel("Room Name: ");
		
		roomNameInput = new JTextField();
		roomNameInput.setColumns(10);
		
		JButton btnCreateRoom = new JButton("Create Room");
		btnCreateRoom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Client.createRoom(roomNameInput.getText());
				frame.setpanel(frame.setWordRoom);
				
			}
		});
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(36)
					.addComponent(lblRoomName)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(btnCreateRoom)
						.addComponent(roomNameInput, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(182, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(132)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(roomNameInput, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblRoomName))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnCreateRoom)
					.addContainerGap(112, Short.MAX_VALUE))
		);
		setLayout(groupLayout);

	}
}
