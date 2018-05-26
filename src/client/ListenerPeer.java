package client;

import protocol.Message;

import java.io.IOException;
import java.util.Arrays;

class ListenerPeer implements Runnable {
    private int id;
    private byte[] msg = new byte[1024];

    public ListenerPeer(int id) {
        this.id = id;
    }

    @Override
    public void run() {

        while (true) {
            try {
                Arrays.fill(msg, (byte) 0);
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
                        if(Client.rooms[1].getOwner()) {
                            ListenerPeer2 listPeer2 = new ListenerPeer2(this.id);
                            new Thread(listPeer2).start();
                            Client.guessLetter();                            
                        }
                        break;
                    case WORD_TO_GUI:
                        Client.setWordInGUI(message.getWord());
                        break;
                    case GAME_FINISH:
                        //TODO::receber a mensagem e fazer alguma coisa!
                        break;
                    case READY_TO_START:
                    	Client.rooms[1].setReady(message.getClientID());
                    	
                        break;
                    case TURN_PEER_ID:
                        if(message.getClientID() == Client.clientID)
                            Client.rooms[1].getGame().setTurn(true);
                        else
                            Client.rooms[1].getGame().setTurn(false);
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