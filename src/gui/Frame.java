package gui;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Frame extends JFrame {


	public MainMenu mainMenu;
	public GamePanel gamePanel;
	public JoinLobby createLobby;
	public SetWordRoom setWordRoom;
	public WaitingRoom waitingRoom;

	/**
	 * Create the frame.
	 */
	public Frame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 350);

		mainMenu = new MainMenu(this);
		gamePanel = new GamePanel(this);
		createLobby = new JoinLobby(this);
		setWordRoom = new SetWordRoom(this);
		waitingRoom = new WaitingRoom(this);

		setpanel(mainMenu);
		setVisible(true);
	}

	public void setpanel(JPanel panel) {
		JPanel contentPane = (JPanel) getContentPane();

		contentPane.removeAll();
		contentPane.add(panel);
		contentPane.revalidate();
		contentPane.repaint();
	}

}
