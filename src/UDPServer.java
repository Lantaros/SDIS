import java.io.*; 
import java.net.*;
import java.util.HashMap;

public class UDPServer{
	
	private byte[] receiveData = new byte[1024];
	private DatagramSocket unicastSocket;

	
    public UDPServer(int port) {
		try {
			unicastSocket = new DatagramSocket(port);
		}
		catch(SocketException e){
			System.out.println("Error creating unicast socket");
		}
    }
    
	public static void main(String[] args) {
		if(args.length != 1) {
			System.out.println("Wrong number of arguments!!\nExpected <port_number>");
			System.exit(1);
		}

    	HashMap<String, String> hashtable = new HashMap<>();
		UDPServer server =  new UDPServer(Integer.parseInt(args[0]));
		DatagramPacket receivedPacket = null;
		String receivedMessage;
		
		while(true) {
			receivedPacket = server.receive();
			receivedMessage = new String(receivedPacket.getData(), 0, receivedPacket.getLength()); //or String(receivedPacket.getData())

			if(receivedMessage.contains("REGISTER")) {
				String[] info = receivedMessage.split(" ");

				if(hashtable.put(info[1], info[2]) == null) {
					System.out.println("License plate already in the system");
					server.transmit("-1", receivedPacket.getAddress(), receivedPacket.getPort());
				}
				else 
					server.transmit(Integer.toString(hashtable.size()), receivedPacket.getAddress(), receivedPacket.getPort());
			}
			else if(receivedMessage.contains("LOOKUP")) {
				String[] info = receivedMessage.split(" ");
				String numberPlate = info[1], name;
				
				
				if((name = hashtable.get(numberPlate)) != null) {
					server.transmit(Integer.toString(hashtable.size()) + "\n" + numberPlate + " " + name,
						receivedPacket.getAddress(),
						receivedPacket.getPort());
					System.out.println("Received\n" + "LOOKUP " + numberPlate);	
				}
				else
					server.transmit("NOT_FOUND", receivedPacket.getAddress(), receivedPacket.getPort());
			}
		}
	}
	
	private DatagramPacket receive() {

		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

		try {
			unicastSocket.receive(receivePacket);
		}
		catch (IOException e) {
			System.out.println("IOException while receive the message");
			return null;
		}		

        return receivePacket;
	}
	
	private int transmit(String message, InetAddress ip, int clientPort)  {

		DatagramPacket packet = new DatagramPacket(message.getBytes(), message.length(), ip, clientPort);
		
		try {
			unicastSocket.send(packet);
		} catch (IOException e) {
			System.out.println("IO Exception receiving a message");
		}	

		return packet.getLength();
	}
}
