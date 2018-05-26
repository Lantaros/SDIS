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
	private JTextField textFieldGuessLetter;
	private JLabel lblHagmanWord;
	private JLabel lblTimeRemaining;
	private JTextField textFieldGuessWord;
	private JButton btnGuessLetter;
	JLabel lblWarning;

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

		textFieldGuessLetter = new JTextField();
		textFieldGuessLetter.setColumns(10);

		btnGuessLetter = new JButton("Guess Letter");
		btnGuessLetter.setEnabled(false);
		btnGuessLetter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String letterGuessed = textFieldGuessLetter.getText();

				String aswner = Client.sendLetter(letterGuessed);
				if(aswner == "ok") {
					textFieldGuessLetter.setText("");
				} else {
					textFieldGuessLetter.setText(aswner);
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
		
		lblWarning = new JLabel("");
		
		textFieldGuessWord = new JTextField();
		textFieldGuessWord.setColumns(10);
		
		JButton btnGuessWord = new JButton("Guess Word");
		btnGuessWord.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String wordGuessed = textFieldGuessWord.getText();
				
				//TODO Delete tests
//				setWarning("work pls");
//				int i = Integer.parseInt(lblTimeRemaining.getText())+1;
//				setTimeRemaining(i);
				
			}
		});
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(27)
					.addComponent(lblWarning)
					.addContainerGap(423, Short.MAX_VALUE))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(19)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(textFieldGuessWord, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED, 197, Short.MAX_VALUE))
								.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
									.addComponent(lblGamepanel, GroupLayout.PREFERRED_SIZE, 83, GroupLayout.PREFERRED_SIZE)
									.addGap(35)))
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
									.addComponent(TimeRemaininglbl)
									.addGap(45))
								.addGroup(Alignment.TRAILING, groupLayout.createParallelGroup(Alignment.LEADING)
									.addComponent(lblTimeRemaining)
									.addComponent(btnLeaveRoom)))
							.addGap(29))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblNumberOfErrors)
							.addGap(18)
							.addComponent(label_1))
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(lblWordToGuess)
									.addGap(18)
									.addComponent(lblHagmanWord))
								.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
									.addComponent(btnGuessWord)
									.addGroup(groupLayout.createSequentialGroup()
										.addComponent(textFieldGuessLetter, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.UNRELATED)
										.addComponent(btnGuessLetter))))
							.addContainerGap(242, Short.MAX_VALUE))))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblGamepanel, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
							.addGap(9)
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblNumberOfErrors)
								.addComponent(label_1))
							.addGap(46)
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblWordToGuess)
								.addComponent(lblHagmanWord))
							.addGap(55)
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(textFieldGuessLetter, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnGuessLetter)))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(34)
							.addComponent(TimeRemaininglbl)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblTimeRemaining)))
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(31)
							.addComponent(btnLeaveRoom))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(18)
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(textFieldGuessWord, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnGuessWord))))
					.addPreferredGap(ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
					.addComponent(lblWarning)
					.addContainerGap())
		);
		setLayout(groupLayout);

	}

	public void setWordToGuess(String wordToGuess) {
		lblHagmanWord.setText(wordToGuess);
	}
	
	public void setTimeRemaining(int timeRemain) {
		String a = Integer.toString(timeRemain);	
		lblTimeRemaining.setText(a);
		this.repaint();		
	}
	
	public void setWarning(String warning) {
		lblWarning.setText(warning);
	}

	public void setTurn(boolean check) {
		if(check) 
			btnGuessLetter.setEnabled(true);
		else
			btnGuessLetter.setEnabled(false);
	}
}
