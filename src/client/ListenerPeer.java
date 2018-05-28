package client;

import protocol.Message;
import protocol.MessageType;

import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;

import game.Room;
import gui.Launcher;

class ListenerPeer implements Runnable {
	private int roomID;
	private int peerID;
	private int serverPeerID = -1;
	private byte[] msg = new byte[1024];
	private ReentrantLock lock = new ReentrantLock();
	private ReentrantLock lockWord = new ReentrantLock();
	private ReentrantLock lockLetter = new ReentrantLock();

	public ListenerPeer(int peerID) {
		this.peerID = peerID;
	}

	@Override
	public void run() {


		while (true) {
			try {
				msg = new byte[1024];
				int readBytes = Client.peer[this.peerID].getInputStream().read(msg, 0, msg.length);
				if(readBytes < 0) {
					//Client.removePeer(this.serverPeerID);
                    System.out.println("Peer " + serverPeerID + " has disconnected");
                    break;
                }
				System.out.println(new String(msg));
				// Client.peer[this.peerID].setMessage(msg);
				Message message = new Message(new String(msg));
				switch (message.getType()) {
				case PEER_INFO:
					serverPeerID = message.getClientID();
					Client.addPeer(this.peerID, message.getClientID());
					break;
				case WORD_TO_GUESS:
					Client.setWord(message.getWord());
					break;

				case WORD_TO_GUESS_PEER:
                    Client.handleWord(this.peerID, message.getWord());
                    break;

                case WORD_CHECK:
                	lockWord.lock();
                    Client.confirmWordMsg.add(this.serverPeerID);
                    lockWord.unlock();
                    
                    break;

               case WORD_GO:
                    if(Client.currentRoom.getOwner()) {
                        Client.guessWord();                            
                    }
                  break;

				case LETTER_TO_GUESS:
					Client.handleLetter(this.peerID, message.getLetter());

					break;
				case LETTER_CHECK:
					lockLetter.lock();
					Client.confirmMsg.add(message.getClientID());
					lockLetter.unlock();

					break;
				case LETTER_GO:
					if (Client.currentRoom.getOwner()) {
						// ListenerPeer2 listPeer2 = new
						// ListenerPeer2(this.peerID);
						// new Thread(listPeer2).start();
						Client.guessLetter();
					}
					break;
				case WORD_TO_GUI:
					System.out.println("First WOrd: |"+ message.getWord()+  "|");
					System.out.println("Sec WOrd: |"+Launcher.getFrame().gamePanel.getWordToGuess()+ "|");
					if(message.getWord().trim().equals(Launcher.getFrame().gamePanel.getWordToGuess()))
						Launcher.getFrame().gamePanel.incrementNumberOfErrors();
					Client.setWordInGUI(message.getWord());
					break;
				case GAME_FINISH:
					Launcher.getFrame().setpanel(Launcher.getFrame().waitingRoom);
					Launcher.getFrame().waitingRoom.setVictory(message.getGameOver());
					break;
				case READY_TO_START:
					Client.currentRoom.setReady(message.getClientID());
					Room a = Client.currentRoom;
					if (Client.currentRoom.isEveryoneReady() && Client.currentRoom.getOwner()) {
						System.out.println("num cli: " + Client.currentRoom.getNClients());
						Client.sendAll(new Message(MessageType.START_GAME));
						Launcher.getFrame().setpanel(Launcher.getFrame().gamePanel);
					}

					break;
				case START_GAME:
					Launcher.getFrame().gamePanel.resetNumberOfErrors();
					Launcher.getFrame().setpanel(Launcher.getFrame().gamePanel);

					break;
				case TURN_PEER_ID:
					if (message.getClientID() == Client.clientID)
						Client.currentRoom.getGame().setTurn(true);
					else
						Client.currentRoom.getGame().setTurn(false);
					Client.sendNextTurn(this.peerID);
					break;
				case TURN_CHECK:
					Client.confirmTurnMsg.add(this.serverPeerID);
					break;
				case TURN_GO:
					Client.handleMyTurn();
					break;
				case TIMER_UP:
					if (Client.currentRoom.getOwner()){
						lock.lock();
						Client.handleTimerUP();
						lock.unlock();
					}
						
					break;
				case PASS_OWNERSHIP:
					if (message.getClientID() == Client.clientID) {
						Launcher.getFrame().gamePanel.resetValues();
						System.out.println("Im the one who owns");
						Client.currentRoom.setOwner(true);
						Client.currentRoom.setAllReadyToFalse();
						Launcher.getFrame().setpanel(Launcher.getFrame().setWordRoom);
						Launcher.getFrame().gamePanel.setButtons(false);
						
					}
					break;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}