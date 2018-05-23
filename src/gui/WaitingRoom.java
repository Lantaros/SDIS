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
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(56)
					.addComponent(lblWaitingForUser, GroupLayout.PREFERRED_SIZE, 339, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(55, Short.MAX_VALUE))
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addContainerGap(324, Short.MAX_VALUE)
					.addComponent(btnLeaveRoom)
					.addGap(37))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(130)
					.addComponent(lblWaitingForUser, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 83, Short.MAX_VALUE)
					.addComponent(btnLeaveRoom)
					.addGap(24))
		);
		setLayout(groupLayout);

	}

}
