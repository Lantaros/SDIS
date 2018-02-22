import java.io.*; 
import java.net.*;
import java.util.HashMap;
import java.util.regex.*;

public class UDPServer{
	
	private byte[] receiveData = new byte[1024];
    private byte[] sendData = new byte[1024];
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
		HashMap<String, String> hashtable = new HashMap<>();
		UDPServer server =  new UDPServer(Integer.parseInt(args[0]));
		DatagramPacket receivedPacket = null;
		String receivedMessage;

		while(true) {

			receivedPacket = server.receive();
			receivedMessage = new String(receivedPacket.getData(), 0, receivedPacket.getLength());

			if(receivedMessage.contains("REGISTER")) {
				Pattern regex = Pattern.compile("^[.]+\\s((?:[0-9A-Z]{2}-){2}[0-9A-Z]{2})\\s([A-Za-z]{1,256})$");
				Matcher matcher = regex.matcher(receivedMessage);

				if(hashtable.put(matcher.group(1), matcher.group(2)) == null) {
					System.out.println("License plate already in the system");
					server.transmit("-1", receivedPacket.getAddress(), receivedPacket.getPort());
				}
				else 
					server.transmit(Integer.toString(hashtable.size()), receivedPacket.getAddress(), receivedPacket.getPort());
			}
			else if(receivedMessage.contains("LOOKUP")) {
				Pattern regex = Pattern.compile("^.+\b((?:[0-9A-Z]{2}-){2}[0-9A-Z]{2})$");
				Matcher matcher = regex.matcher(receivedMessage);
				String numberPlate = matcher.group(1), name;
				
				if(numberPlate == null)
					server.transmit("-1", receivedPacket.getAddress(), receivedPacket.getPort());
				
				if((name = hashtable.get(matcher.toString())) != null)
				server.transmit(Integer.toString(hashtable.size()) + "\n" + numberPlate + " " + matcher.toString(),
						receivedPacket.getAddress(),
						receivedPacket.getPort());


				else
					server.transmit("-1", receivedPacket.getAddress(), receivedPacket.getPort());
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
