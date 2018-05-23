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
<<<<<<< HEAD
                    case LETTER_TO_GUESS:
                        Client.handleLetter(this.id, message.getLetter());
                        break;
                    case LETTER_CHECK:
                        System.out.println(message.getBool());
                        Client.requestNumber++;
                        if(message.getBool() == "yes"){
                            System.out.println("ENTROU CARALHO");
                            
                            System.out.println(Client.requestNumber);
                        }
                        break;
                    case LETTER_GO:
                        if(Client.rooms[1].getOwner())
                            Client.guessLetter();
=======
                    case READY_TO_START:
                        
>>>>>>> 2e0b613f7c7c01fe02872698769bb51911b8554c
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}