package client;

import game.Hangman;
import game.Room;
import gui.Launcher;
import protocol.ClientData;
import protocol.Message;
import protocol.MessageType;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.util.Enumeration;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.ArrayList;


public class Client {

    public static int clientID;

    //conection with the server
    //protected int port;
    protected static SSLSocket sslSocket;
    protected static InputStream receiveStream;
    protected static OutputStream sendStream;

    protected static Launcher launcher;

    protected static byte[] msgReceivedServer = new byte[1024];


    public static Room currentRoom;
    protected static ClientData[] peer = new ClientData[100];
    protected static int countPeer = 0;

    protected static String newLetter = "";
    protected static String newWord = "";
    protected static int requestNumber = 0;
    protected static ArrayList<Integer> confirmMsg = new ArrayList<Integer>();
    protected static ArrayList<Integer> confirmTurnMsg = new ArrayList<Integer>();
    protected static ArrayList<Integer> confirmWordMsg = new ArrayList<Integer>();
    protected static int numTurn = 1;
    protected static int confirmTurn = 0;
    protected static int confirmTimerUP = 0;
    protected static boolean resetTimer = false;
    protected static int confirmWord = 0;
    protected static boolean cancel = false;
    protected static boolean cancelTurn = false;
    protected static boolean cancelWord = false;
    protected static boolean cancelLetter = false;

    private static Object srvChannelLock;
    private static Object rcvRoomsLock;

    protected static GameThread gameThread = new GameThread("");


    public Client(String host, int port) {
        SSLSocketFactory serverSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();

        try {
            sslSocket = (SSLSocket) serverSocketFactory.createSocket(host, port);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //-Djavax.net.debug=all -> Debug flag, a TON of information
    //java  -Djavax.net.ssl.keyStore=../client.keys -Djavax.net.ssl.keyStorePassword=123456 -Djavax.net.ssl.trustStore=../truststore -Djavax.net.ssl.trustStorePassword=123456 client.Client 127.0.0.1 3030 "qualquer coisa"
    public static void main(String[] args) {

        if (args.length < 2) {
            System.out.println("Wrong number of arguments\nExpected at least client.Client <SERVER_HOST> <PORT>");
            System.exit(1);
        }

        Client client = new Client(args[0], Integer.parseInt(args[1]));


        client.sslSocket.setEnabledCipherSuites(
            new String[]{"TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384",
                    "TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384",
                    "TLS_RSA_WITH_AES_256_GCM_SHA384",
                    "TLS_ECDH_ECDSA_WITH_AES_256_GCM_SHA384",
                    "TLS_ECDH_RSA_WITH_AES_256_GCM_SHA384",
                    "TLS_DHE_RSA_WITH_AES_256_GCM_SHA384"}
        );



        try {
            client.sslSocket.setReceiveBufferSize(1024);
            client.sslSocket.setSendBufferSize(1024);
        } catch (SocketException e) {
            e.printStackTrace();
        }

        try {
            client.receiveStream = client.sslSocket.getInputStream();
            client.sendStream = client.sslSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            client.sslSocket.startHandshake();
        } catch (IOException e) {
            e.printStackTrace();
        }


        ServerChannel listServer = new ServerChannel();
        new Thread(listServer).start();

        srvChannelLock = listServer.getLock();
        rcvRoomsLock = listServer.getRcvRoomslock();

        try {
            synchronized(srvChannelLock) {
                srvChannelLock.wait();
            }

        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }

        //Open GUI
        launcher = new Launcher();
        launcher.main(null);



       try {
           synchronized (srvChannelLock){
               srvChannelLock.wait();

            }
       } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }


//        if(getRooms()[1].getOwner()) {
//            String word = "qweasd zxc";
//             Hangman game = getRooms()[1].getGame();
//            game.startGame(word);
//            Message sendWord = new Message(MessageType.WORD_TO_GUESS, word);
//            Client.sendAll(sendWord);
//            Client.handleNextTurn();




    }

    public static ArrayList<Room> requestAvailableRooms(){
        Message msg = new Message(MessageType.GET_ROOMS_AVAILABLE);

        try {
            sendStream.write(msg.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Antes do lock");

        try {
            synchronized (rcvRoomsLock){
                rcvRoomsLock.wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Passou");


        ArrayList<Room> avRooms = ServerChannel.getAvailableRooms();
        System.out.println(avRooms);
        return ServerChannel.getAvailableRooms();
    }

    public static void advanceTurn() {
        Message endTurn = new Message(MessageType.TIMER_UP);
        sendAll(endTurn);
    }

    public static void handleTimerUP() {
        int n = Client.currentRoom.getNClients(); 
        
        if(Client.confirmTimerUP == 0) {
        	Client.cancel = false;
	    	GameThread gameThrea = new GameThread("timer_up");
	        new Thread(gameThrea).start();
        }
        Client.confirmTimerUP++;
        if(Client.confirmTimerUP >= n-1){
            confirmTimerUP = 0;
            Client.cancel = true;
            GameThread gameThre = new GameThread("next_turn");
	        new Thread(gameThre).start();
        }
        
    }

    public static void handleNextTurn() {
        int n = currentRoom.getNClients();
        int[] id = currentRoom.getClients();

        if(id[numTurn] == Client.clientID)
            numTurn++;
        Message sendTurn = new Message(MessageType.TURN_PEER_ID, id[numTurn]);
        if(numTurn >= n )
            numTurn = 1;
        else
            numTurn++;
        Client.sendAll(sendTurn);
        Client.cancelTurn = false;
        //GameThread gameThrea = new GameThread("unblock_turn");
       // new Thread(gameThrea).start();
        while(Client.confirmTurnMsg.size() < n-1) {
            System.out.flush();
        }
        if(cancelTurn)
        	cancelTurn = false;
        else
        	Client.cancelTurn = true;
        Client.confirmTurnMsg.clear();
        Message sendGo = new Message(MessageType.TURN_GO);
        sendAll(sendGo);

    }

    public static void sendNextTurn(int peerID) {
        Message msg = new Message(MessageType.TURN_CHECK, "yes");
        try {
            peer[peerID].getOutputStream().write(msg.getBytes());
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static void handleMyTurn() {

        if(Client.currentRoom.getGame().getTurn()) {
            launcher.getFrame().gamePanel.setTurn(true);
        } else {
            launcher.getFrame().gamePanel.setTurn(false);
        }

        if(gameThread.getCountdown() < 11) {
            gameThread.resetTimer();
        }

        gameThread = new GameThread("timer");
        new Thread(gameThread).start();
    }

    public static void createRoom(String roomName) {
        Message message = new Message(MessageType.ROOM_CREATE, Client.clientID, roomName);
        byte[] msgBytes = message.getBytes();

        try {
            Client.sendStream.write(msgBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void guessLetter() {
        Hangman game = Client.currentRoom.getGame();
        game.guessLetter(newLetter.charAt(0));
        String word = game.getWord();
        Message sendWord = new Message(MessageType.WORD_TO_GUI, word);
        Client.sendAll(sendWord);
        
        if(game.gameOver()) {
            if(game.hasLost()) {
                Message message = new Message(MessageType.GAME_FINISH, false);
                Client.sendAll(message);
                Launcher.getFrame().waitingRoom.setWordGuessed(false);
            }   else if (game.hasWon()) {
                Message message = new Message(MessageType.GAME_FINISH, true);
                Client.sendAll(message);
                Launcher.getFrame().waitingRoom.setWordGuessed(true);
            }
            
			int[] ids = currentRoom.getClients();
			int n = currentRoom.getNClients();
			
			if (ids[numTurn] == Client.clientID)
				numTurn++;
	
			Message message = new Message(MessageType.PASS_OWNERSHIP, ids[numTurn]);
			sendAll(message);
			Client.currentRoom.setOwner(false);
			Launcher.getFrame().setpanel(Launcher.getFrame().waitingRoom);
			return;
        }
        gameThread = new GameThread("next_turn");
        new Thread(gameThread).start();
        //Client.handleNextTurn();
    }
    public static void incrementErrors() {
    	Launcher.getFrame().gamePanel.incrementNumberOfErrors();
    }
    public static void guessWord() {
        Hangman game = Client.currentRoom.getGame();
        game.guessWord(newWord);
        String word = game.getWord();
        Message sendWord = new Message(MessageType.WORD_TO_GUI, word);
        Client.sendAll(sendWord);
        
        if(game.gameOver()) {
            if(game.hasLost()) {
                Message message = new Message(MessageType.GAME_FINISH, false);
                Client.sendAll(message);
                Launcher.getFrame().waitingRoom.setWordGuessed(false);
            }   else if (game.hasWon()) {
                Message message = new Message(MessageType.GAME_FINISH, true);
                Client.sendAll(message);
                Launcher.getFrame().waitingRoom.setWordGuessed(true);
            }
            
            int[] ids = currentRoom.getClients();
			int n = currentRoom.getNClients();
			
			if (ids[numTurn] == Client.clientID)
				numTurn++;
	
			Message message = new Message(MessageType.PASS_OWNERSHIP, ids[numTurn]);
			sendAll(message);
			Client.currentRoom.setOwner(false);
			Launcher.getFrame().setpanel(Launcher.getFrame().waitingRoom);
			return;
        }
       
    }

    public static void sendAll(Message message) {
        for(int i = 0; i<countPeer; i++) {
            
            try {
                //Thread.sleep(100);
                peer[i].getOutputStream().write(message.getBytes());
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void chooseWord(String word){

        Hangman game = currentRoom.getGame();
        game.startGame(word);
        Message sendWord = new Message(MessageType.WORD_TO_GUESS, word);
        Client.sendAll(sendWord);
        Client.handleNextTurn();
        Launcher.getFrame().gamePanel.setWordToGuess(game.getWord());
        if(Client.gameThread.getCountdown() < 11) {
            Client.gameThread.resetTimer();
        }
    }

    public static void setWord(String word) {
        System.out.println(word);
        Hangman game = Client.currentRoom.getGame();
        game.startGame(word);
        setWordInGUI(game.getWord());
        launcher.getFrame().gamePanel.setButtonWord(true);
        if(gameThread.getCountdown() < 11) {
            gameThread.resetTimer();
        }
    }

    public static void setWordInGUI(String word) {
        launcher.getFrame().gamePanel.setWordToGuess(word.trim());
    }

    public static String sendLetter(String letter) {
        Hangman game = currentRoom.getGame();
        if(letter.length() == 1 && game.checkLetter(letter.charAt(0))) {
            Message letterToSend = new Message(MessageType.LETTER_TO_GUESS, letter);
            sendAll(letterToSend);
            int i = currentRoom.getNClients();
            System.out.println(i);
            Client.cancelLetter = false;
            GameThread gameThrea = new GameThread("unblock_letter");
            new Thread(gameThrea).start();
            while(Client.confirmMsg.size() < i-1) {
                System.out.flush();
            }
            if(cancelLetter)
            	cancelLetter = false;
            else
            	cancelLetter = true;
            confirmMsg.clear();
            Message message = new Message(MessageType.LETTER_GO);
            sendAll(message);
            requestNumber = 0;
            return "ok";
        } else if(!game.checkLetter(letter.charAt(0))) {
            return "Ja foi tentado";
            
        }
        else {
            return "Must Be 1 Word";        
        }
    }

    public static String sendWord(String word) {
        Hangman game = currentRoom.getGame();

        Message wordToSend = new Message(MessageType.WORD_TO_GUESS_PEER, word);
        sendAll(wordToSend);
        int i = currentRoom.getNClients();
        System.out.println(i);
        Client.cancelWord = false;
        GameThread gameThre = new GameThread("unblock_word");
        new Thread(gameThre).start();
        while(Client.confirmWordMsg.size() < i-1) {
            System.out.flush();
        }
        if(cancelWord)
        	cancelWord = false;
        else
        	Client.cancelWord = true;
        confirmWordMsg.clear();
        Message message = new Message(MessageType.WORD_GO);
        sendAll(message);
        Client.launcher.getFrame().gamePanel.setButtonWord(false);
        GameThread gameThrea = new GameThread("block_3seconds");
        new Thread(gameThrea).start();
        return "ok";
    }

    public static void handleLetter(int id, String letter) {
        newLetter = letter;
        Message message = new Message(MessageType.LETTER_CHECK, Client.clientID, "yes");
        try {
            System.out.println(message.toString());
            peer[id].getOutputStream().write(message.getBytes());
        } catch(IOException e) {
            e.printStackTrace();
        }
    }


    public static void handleWord(int id, String word) {
        newWord = word;
        Message message = new Message(MessageType.WORD_CHECK, Client.clientID, "yes");
        try {
            System.out.println(message.toString());
            peer[id].getOutputStream().write(message.getBytes());
        } catch(IOException e) {
            e.printStackTrace();
        }
    }


    public static void connectRoom(int roomID) {
        //roomsID -> roomsID, neste caso 1

        //send to server to connect to rooms
        Message connectRequest = new Message(MessageType.ROOM_CONNECT, clientID, roomID);

        System.out.println(connectRequest);

        try {
            sendStream.write(connectRequest.getBytes());
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }


        Client.currentRoom = new Room(roomID);
        Hangman game = new Hangman(roomID);
        Client.currentRoom.addGame(game);


        Client.currentRoom.addClientId(clientID);
        //countPeer++;
    }

    public static void requestPort(int nPorts) {
        SSLSocketFactory serverSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        String msg = Integer.toString(clientID);
        String address = "";
        int port = Client.nextFreePort(49152, 65535);
        msg += " ";
        msg += port;
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface
                    .getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) networkInterfaces
                        .nextElement();
                Enumeration<InetAddress> nias = ni.getInetAddresses();
                while (nias.hasMoreElements()) {
                    InetAddress ia = (InetAddress) nias.nextElement();
                    if (!ia.isLinkLocalAddress()
                            && !ia.isLoopbackAddress()
                            && ia instanceof Inet4Address) {
                        address = ia.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            System.out.println(e.getMessage());
        }
        //System.out.println("Local IP " + client.Client.sslSocket.getLocalAddress().getHostAddress());
        try {
            Message message = new Message(MessageType.PORT_TO_SEND, port, address);
            sendStream.write(message.getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        }


        PeerConnection listConnection = new PeerConnection(nPorts, port);
        new Thread(listConnection).start();
    }

    public static int nextFreePort(int from, int to) {
        Random rand = new Random();
        int port = rand.nextInt(to - from) + from;
        while (true) {
            if (isLocalPortFree(port)) {
                return port;
            } else {
                port = ThreadLocalRandom.current().nextInt(from, to);
            }
        }
    }

    private static boolean isLocalPortFree(int port) {
        try {
            new ServerSocket(port).close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Saves new peer info in currentRoom
     * @param socket
     * @param receiveStream
     * @param sendStream
     */
    public static void saveClient(Socket socket, InputStream receiveStream,
                                  OutputStream sendStream) {
        int id = countPeer;
        countPeer++;

        peer[id] = new ClientData(id);
        peer[id].setSocket(socket);
        peer[id].setOutputStream(sendStream);
        peer[id].setInputStream(receiveStream);

        System.out.println("*********DATA********");
        System.out.println(socket.getLocalPort());
        System.out.println(socket.getLocalAddress().getHostAddress());
        System.out.println(socket.getPort());
        System.out.println(socket.getRemoteSocketAddress());


        currentRoom.addClientId(id);


        ListenerPeer listPeer = new ListenerPeer(id);
        new Thread(listPeer).start();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Message message = new Message(MessageType.PEER_INFO, Client.clientID);
        try {
            peer[countPeer - 1].getOutputStream().write(message.getBytes());
            System.out.println(message.toString());
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public static void connectPeer(int port, String address) {

        SSLSocketFactory serverSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        SSLSocket sslSocketP;

        try {
            System.out.println(address);
            System.out.println(port);
            sslSocketP = (SSLSocket) serverSocketFactory.createSocket(address, port);

            peer[countPeer] = new ClientData(countPeer);

            try {
                sslSocketP.setReceiveBufferSize(1024);
                sslSocketP.setSendBufferSize(1024);
            } catch (SocketException e) {
                e.printStackTrace();
            }

            try {
                peer[countPeer].setInputStream(sslSocketP.getInputStream());
                peer[countPeer].setOutputStream(sslSocketP.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                sslSocketP.startHandshake();
            } catch (IOException e) {
                e.printStackTrace();
            }

            currentRoom.addClientId(countPeer);

            ListenerPeer listPeer = new ListenerPeer(countPeer);
            new Thread(listPeer).start();

            countPeer++;

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Message message = new Message(MessageType.PEER_INFO, Client.clientID);
        try {
            peer[countPeer - 1].getOutputStream().write(message.getBytes());
            System.out.println(message.toString());
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public static void addPeer(int clientID, int generalID) {
        peer[clientID].setClientID(generalID);
        currentRoom.setClientId(clientID+2, generalID);
    }
    
    
    public static void sendReadyToAll(){
    	Message sendReady = new Message(MessageType.READY_TO_START, Client.clientID);
    	sendAll(sendReady);    	
    }
    
    public static ClientData getDataClient(int idClient){
    	for(int i = 0; i < peer.length;i++)
    		if(peer[i].getClientID()==idClient)
    			return peer[i];
    	
		return null;
    	
    }


    public static void removeClient() {
        
    	boolean check = false;
        int[] clients = currentRoom.getClients();
        for(int j=2; j <= currentRoom.getNClients();j++) {

            for(int i = 0; i<confirmMsg.size(); i++) {
                if(clients[j] == confirmMsg.get(i))
                    check = true;
            }
            if(check)
                check = false;
            else {
                currentRoom.removeClient(j);
                removeClientInformation(j);
                Message message = new Message(MessageType.PEER_DISCONNECTED, clients[j]);
                sendAll(message);           
            }
        }
        
        Client.confirmMsg.add(0);
    }
    
    public static void removeClientWord() {
        boolean check = false;
        int[] clients = currentRoom.getClients();
        for(int j=2; j <= currentRoom.getNClients();j++) {

            for(int i = 0; i<confirmWordMsg.size(); i++) {
                if(clients[j] == confirmWordMsg.get(i))
                    check = true;
            }
            if(check)
                check = false;
            else {
                currentRoom.removeClient(j);
                removeClientInformation(j);
                
            }
        }
        Client.confirmWordMsg.add(0);
    }
    
    public static void removeClientTurn() {
        boolean check = false;
        int[] clients = currentRoom.getClients();
        for(int j=2; j <= currentRoom.getNClients();j++) {

            for(int i = 0; i<confirmTurnMsg.size(); i++) {
                if(clients[j] == confirmTurnMsg.get(i))
                    check = true;
            }
            if(check)
                check = false;
            else {
                currentRoom.removeClient(j);
                removeClientInformation(j);
                
            }
        }
        Client.confirmTurnMsg.add(0);
    }

    public static void removeClientInformation(int idPos) {
        idPos--; //retirar um porque ele começa a 0 aqui
        idPos--;
        System.out.println("***DATA***");
        print();
        if(idPos + 1 == countPeer) {
            countPeer--;
            print();
            System.out.println("***DATA***");
            return;
        }
        for(int i = idPos + 1; i < countPeer; i++) {
            peer[i-1] = peer[i];
        }
        countPeer--;
        System.out.println("***DATA***");
        print();
    }
    
    public static void removeClientInformationn(int idPos) {
        idPos--; //retirar um porque ele começa a 0 aqui
        idPos--;
        System.out.println("***DATA***");
        print();
        if(idPos + 1 == countPeer) {
            countPeer--;
            print();
            System.out.println("***DATA***");
            return;
        }
        for(int i = idPos + 1; i < countPeer; i++) {
            peer[i-1] = peer[i];
        }
        countPeer--;
        System.out.println("***DATA***");
        print();
    }
    
    public static void removePeer(int id) {
                
        currentRoom.print();
        int n = currentRoom.getNClients();
        int[] clients = currentRoom.getClients();
        for(int j = 2; j<=n;j++) {
            if(id == clients[j]) {
                currentRoom.removeClient(j);
                removeClientInformation(j);
            }
        }
        System.out.println("---");
        currentRoom.print();
        
        
    }

    public static void print() {
        for(int i = 0; i<countPeer; i++) {
            System.out.println(peer[i].getClientID());
        }
    }

    
	public static void sendMsgToPeer(Message message, int peerToSend) {
		try {
			System.out.println(message.toString());
			for (int i = 0; i < countPeer; i++)
				if (peer[i].getClientID() == peerToSend){
					peer[i].getOutputStream().write(message.getBytes());
					return;
					}
			System.out.println("PEer does not exist");
			
			//peer[peerToSend].getOutputStream().write(message.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
