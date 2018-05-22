package client;

import protocol.Message;

import java.io.IOException;
import java.util.Arrays;

class ServerChannel implements Runnable {

    @Override
    public void run() {

        while (true) {
            try {
                Arrays.fill(Client.msgReceivedServer, (byte) 0);
                Client.receiveStream.read(Client.msgReceivedServer, 0, Client.msgReceivedServer.length);
                System.out.println(new String(Client.msgReceivedServer));

                Message message = new Message(new String(Client.msgReceivedServer));
                switch (message.getType()) {
                    case SEND_PORTS://
                        if (message.getNPorts() == 0) {
                            Client.rooms[1].setOwner(true);
                            break;
                        }
                        Client.requestPort(message.getNPorts());
                        break;
                    case OWN_CLIENT_ID:
                        Client.clientID = message.getClientID();
                        break;
                    case PORT_TO_CONNECT:
                        Client.connectPeer(message.getPort(), message.getAddress());
                        break;
                }

            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }
}