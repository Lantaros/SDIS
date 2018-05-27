package server;

import protocol.Message;
import protocol.MessageType;

import java.io.IOException;
import java.util.Arrays;

class ListenerClient implements Runnable {
    private int id;
    private byte[] msg = new byte[1024];

    public ListenerClient(int id) {
        this.id = id;
    }

    @Override
    public void run() {
        Message messageSend;
        int readBytes;
        while (true) {
            try {
                Arrays.fill(msg, (byte) 0);

                    readBytes = Server.clients[this.id].getInputStream().read(msg, 0, msg.length);

                    if(readBytes < 0) {
                        System.out.println("Client " + id + " has disconnected");
                        Server.removeClient(id);
                        break;
                    }

                System.out.println("RECEIVED: " + new String(msg));
                Message message = new Message(new String(msg));


                switch (message.getType()) {
                    case ROOM_CONNECT:
                        int roomId = message.getRoomId();
                        Server.clients[this.id].setRoomId(roomId);
                        messageSend = new Message(MessageType.SEND_PORTS, Server.rooms[roomId].getNClients());
                        Server.server.sendMessage(messageSend, this.id);
                        if (Server.rooms[roomId].getNClients() == 0) {
                            Server.rooms[roomId].addClientId(this.id);
                        }
                        break;

                    case PORT_TO_SEND:
                        Server.sendPortToClients(message.getPort(), message.getAddress(), this.id);
                    break;
                    case ROOM_CREATE:
                        int roomID = Server.createRoom(message.getRoomName(), message.getClientID());
                        System.out.println("RoomID " + roomID);
                        if(roomID >= 0) {
                            messageSend = new Message(MessageType.ROOM_CREATED, roomID, message.getRoomName());
                            System.out.println("Created Room " + "'" + message.getRoomName() + "'" + " ID " + roomID);
                        }

                        else if(roomID == -1) {
                            messageSend = new Message(MessageType.MAX_ROOMS_REACHED);
                            System.out.println("Max number rooms reached");
                        }

                        else {
                            messageSend = new Message(MessageType.DUP_ROOM_NAME);
                            System.out.println("Duplicated room name '" + message.getRoomName() + "'");
                        }

                        System.out.println("SENT: " + messageSend);
                        Server.clients[this.id].getOutputStream().write(messageSend.getBytes());
                    break;

                    case GET_ROOMS_AVAILABLE:
                        String roomsInfo = "";

                        for (int i = 0; i < Server.nRooms; i ++){
                            if(i > 0)
                                roomsInfo += " ";

                            roomsInfo += Server.rooms[i].getRoomId() + " " +
                                    Server.rooms[i].getName() + " " +
                                    Server.rooms[i].getNClients();
                        }

                        Message roomsAvailable = new Message(MessageType.ROOMS_AVAILABLE, roomsInfo);

                        Server.clients[this.id].getOutputStream().write(roomsAvailable.getBytes());
                    break;



                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}