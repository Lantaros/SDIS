package gui;

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

public class GamePanel extends JPanel {

	private Frame frame;
	private JTextField textField;
	private JLabel lblHagmanWord;

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

		lblHagmanWord = new JLabel("_ _ _ _ _ _   _ __");
		lblHagmanWord.setFont(new Font("Tahoma", Font.PLAIN, 14));

		textField = new JTextField();
		textField.setColumns(10);

		JButton btnGuessLetter = new JButton("Guess Letter");
		btnGuessLetter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String letterGuessed = textField.getText();

				// gotta do smt with it
				
				textField.setText("");

			}
		});

		JButton btnLeaveRoom = new JButton("Leave Room");
		btnLeaveRoom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setpanel(frame.mainMenu);
			}
		});
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout
				.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup().addGap(183).addComponent(lblGamepanel,
								GroupLayout.PREFERRED_SIZE, 93, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup().addGap(19).addGroup(groupLayout
								.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
										.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.UNRELATED)
										.addComponent(btnGuessLetter))
								.addGroup(groupLayout.createSequentialGroup().addComponent(lblWordToGuess).addGap(18)
										.addComponent(lblHagmanWord)))))
				.addContainerGap(174, Short.MAX_VALUE))
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup().addContainerGap(332, Short.MAX_VALUE)
						.addComponent(btnLeaveRoom).addGap(29)));
		groupLayout
				.setVerticalGroup(
						groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup().addContainerGap()
										.addComponent(lblGamepanel, GroupLayout.PREFERRED_SIZE, 26,
												GroupLayout.PREFERRED_SIZE)
										.addGap(81)
										.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
												.addComponent(lblWordToGuess).addComponent(lblHagmanWord))
										.addGap(55)
										.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
												.addComponent(textField, GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
												.addComponent(btnGuessLetter))
										.addGap(31).addComponent(btnLeaveRoom).addContainerGap(33, Short.MAX_VALUE)));
		setLayout(groupLayout);

	}

	public void setWordToGuess(String wordToGuess) {
		lblHagmanWord.setText(wordToGuess);
	}
}
