package client;

import protocol.Message;

import java.io.IOException;
import java.util.Arrays;

class ListenerPeer2 implements Runnable {
    private int id;
    private byte[] msg = new byte[1024];

    public ListenerPeer2(int id) {
        this.id = id;
    }

    @Override
    public void run() {
        boolean aux = true;
        while (aux) {
            try {
                msg = new byte[1024];
                Client.peer[this.id].getInputStream().read(msg, 0, msg.length);
                System.out.println(new String(msg));
                Client.peer[this.id].setMessage(msg);
                Message message = new Message(new String(msg));
                switch (message.getType()) {
                    case PEER_INFO:
                        Client.addPeer(this.id, message.getClientID());
                        break;
                    case WORD_TO_GUESS:
                        Client.setWord(message.getWord());
                        break;
                    case LETTER_TO_GUESS:
                        Client.handleLetter(this.id, message.getLetter());
                        break;
                    case LETTER_CHECK:
                        Client.confirmMsg.add(message.getClientID());
                        
                        break;
                    case LETTER_GO:
                        if(Client.getRooms()[1].getOwner())
                            Client.guessLetter();
                        break;
                    case WORD_TO_GUI:
                        Client.setWordInGUI(message.getWord());
                        break;
                    case GAME_FINISH:
                        //TODO::receber a mensagem!
                        break;
                    case READY_TO_START:
                    	Client.getRooms()[1].setReady(message.getClientID());
                    	//if()
                        break;
                    case TURN_PEER_ID:
                        if(message.getClientID() == Client.clientID)
                            Client.getRooms()[1].getGame().setTurn(true);
                        else
                            Client.getRooms()[1].getGame().setTurn(false);
                        Client.sendNextTurn(this.id);
                        break;
                    case TURN_CHECK:
                        Client.confirmTurn++;
                        break;
                    case TURN_GO:
                        Client.handleMyTurn();
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}