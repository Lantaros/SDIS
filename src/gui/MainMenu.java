package gui;

import javax.swing.JPanel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;

public class MainMenu extends JPanel {

	private Frame frame;

	/**
	 * Create the panel.
	 * 
	 * @param frame
	 */
	public MainMenu(Frame frame) {
		this.frame = frame;

		JLabel lblMainmenu = new JLabel("Hangman");
		lblMainmenu.setFont(new Font("Arial", Font.PLAIN, 18));

		JButton btnCreateLobby = new JButton("Create Room");
		btnCreateLobby.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setpanel(frame.setWordRoom);

			}
		});
		
		JButton btnFindRoom = new JButton("Find Room");
		btnFindRoom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setpanel(frame.createLobby);
			}
		});
		
		JButton btnExitGame = new JButton("Exit Game");
		btnExitGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.dispose();
			}
		});
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(166)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
								.addComponent(btnCreateLobby, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(btnFindRoom, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(btnExitGame, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(179)
							.addComponent(lblMainmenu)))
					.addContainerGap(181, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(35)
					.addComponent(lblMainmenu)
					.addGap(64)
					.addComponent(btnCreateLobby)
					.addGap(18)
					.addComponent(btnFindRoom)
					.addGap(18)
					.addComponent(btnExitGame)
					.addContainerGap(74, Short.MAX_VALUE))
		);
		setLayout(groupLayout);

	}

}
