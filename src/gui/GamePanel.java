package gui;

import client.Client;

import javax.swing.JPanel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.Font;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")

public class GamePanel extends JPanel {

	@SuppressWarnings("unused")
	private Frame frame;
	private JTextField textField;
	private JLabel lblHagmanWord;
	private JLabel lblTimeRemaining;

	/**
	 * Create the panel.
	 * 
	 * @param frame
	 */
	public GamePanel(Frame frame) {

		this.frame = frame;

		JLabel lblGamepanel = new JLabel("Hangman");
		lblGamepanel.setFont(new Font("Tahoma", Font.PLAIN, 16));

		JLabel lblWordToGuess = new JLabel("Word To Guess:");

		lblHagmanWord = new JLabel("");
		lblHagmanWord.setFont(new Font("Tahoma", Font.PLAIN, 14));

		textField = new JTextField();
		textField.setColumns(10);

		JButton btnGuessLetter = new JButton("Guess Letter");
		btnGuessLetter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String letterGuessed = textField.getText();

				String aswner = Client.sendLetter(letterGuessed);
				if(aswner == "ok") {
					textField.setText("");
				} else {
					textField.setText(aswner);
				}
			}
		});

		JButton btnLeaveRoom = new JButton("Leave Room");
		btnLeaveRoom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setpanel(frame.mainMenu);
			}
		});
		
		JLabel TimeRemaininglbl = new JLabel("Time Remaining");
		
		lblTimeRemaining = new JLabel("10");
		
		JLabel lblNumberOfErrors = new JLabel("Number of Errors: ");
		
		JLabel label_1 = new JLabel("0");
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
							.addGap(183)
							.addComponent(lblGamepanel, GroupLayout.PREFERRED_SIZE, 93, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 71, Short.MAX_VALUE)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(TimeRemaininglbl, Alignment.TRAILING)
								.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
									.addComponent(lblTimeRemaining)
									.addGap(27))))
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap(330, Short.MAX_VALUE)
							.addComponent(btnLeaveRoom)))
					.addGap(29))
				.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
					.addGap(19)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblNumberOfErrors)
							.addGap(18)
							.addComponent(label_1))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(btnGuessLetter))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblWordToGuess)
							.addGap(18)
							.addComponent(lblHagmanWord)))
					.addContainerGap(228, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblGamepanel, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
								.addComponent(TimeRemaininglbl))
							.addGap(21)
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblNumberOfErrors)
								.addComponent(label_1))
							.addGap(46)
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblWordToGuess)
								.addComponent(lblHagmanWord))
							.addGap(55)
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnGuessLetter))
							.addGap(31)
							.addComponent(btnLeaveRoom))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(34)
							.addComponent(lblTimeRemaining)))
					.addContainerGap(33, Short.MAX_VALUE))
		);
		setLayout(groupLayout);

	}

	public void setWordToGuess(String wordToGuess) {
		lblHagmanWord.setText(wordToGuess);
	}
	
	public void setTimeRemaining(int timeRemain) {
		lblHagmanWord.setText(Integer.toString(timeRemain));
	}
}
