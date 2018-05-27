package client;

import game.Hangman;
import game.Room;
import protocol.Message;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

class ServerChannel implements Runnable {

    private static ArrayList<Room> availableRooms;
    private Object lock;

    public ServerChannel(){
        this.lock = new Object();
    }

    public static ArrayList<Room> getAvailableRooms() {
        return availableRooms;
    }


    public Object getLock() {
        return lock;
    }


    @Override
    public void run() {

        int readBytes;

        while (true) {
            try {
                Client.msgReceivedServer = new byte[1024];

                readBytes = Client.receiveStream.read(Client.msgReceivedServer, 0, Client.msgReceivedServer.length);

                if(readBytes < 0) {
                    System.out.println("Server connection has dropped");
                    break;
                }

                String msgStr = new String(Client.msgReceivedServer);
                System.out.println();

                System.out.println("RECEIVED SERVER: " + msgStr);

                Message message = new Message(msgStr);

                switch (message.getType()) {
                    case SEND_PORTS:
                        if (message.getNPorts() == 0) {
                            Client.getRooms()[1].setOwner(true);
                            break;
                        }
                        Client.requestPort(message.getNPorts());
                        break;
                    case OWN_CLIENT_ID:
                        Client.clientID = message.getClientID();
                        System.out.println("Received ID - notified Client");

                        synchronized (this.lock) {
                            this.lock.notify();
                        }

                        break;
                    case PORT_TO_CONNECT:
                        Client.connectPeer(message.getPort(), message.getAddress());
                        break;

                    case MAX_ROOMS_REACHED:
                        System.out.println("Max Server Rooms reached");
                        break;
                    case DUP_ROOM_NAME:
                        System.out.println("Duplicated Room Name");
                        break;

                    case ROOM_CREATED:
                        System.out.println("Created Room " + "'" + message.getRoomName() + "'" + " ID " + message.getRoomId());
                        Client.rooms[Client.nRooms] = new Room(message.getRoomId());

                        Room newRoom = Client.rooms[Client.nRooms];

                        Client.nRooms++;

                        newRoom.setOwner(true);
                        newRoom.addClientId(Client.clientID);

                        newRoom.addGame(new Hangman(newRoom.getRoomId()));

                        break;
                    case ROOMS_AVAILABLE:
                        availableRooms = message.getAvailableRooms();

                }

            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }
}