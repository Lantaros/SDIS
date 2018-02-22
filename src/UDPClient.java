import java.io.*;
import java.net.*;

public class UDPClient {
	private byte[] receiveData = new byte[1024];
	private byte[] sendData = new byte[1024];
	DatagramSocket unicastSocket;


	public UDPClient(){
		try {
			this.unicastSocket = new DatagramSocket();
		}
		catch (SocketException e){
			System.out.println("Socket creation error");
		}
	}



	public static void main(String[] args) {
		UDPClient client = new UDPClient();


		if(args[2].equals("register"))
			client.transmit("REGISTER " + args[3] + " " + args[4], args[0], Integer.parseInt(args[1]));
		else if(args[2].equals("lookup"))
			client.transmit("LOOKUP " + args[3], args[0], Integer.parseInt(args[1]));
		else{
			System.out.println("Application invoked with wrong arguments");
			System.exit(1);
		}

		String response = null;
		client.receive(response);
			System.out.println(response);


	}

	private int receive(String response) {
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

		try {
			unicastSocket.receive(receivePacket);
		}
		catch (IOException e) {
			System.out.println("IOException while receive the message\n");
			return 0;
		}

		response = new String(receivePacket.getData(), 0, receivePacket.getLength());
		return response.length();
	}

	private int transmit(String message, String ip, int port) {
		byte[] messageBytes = message.getBytes();

		try {
			DatagramPacket packet = new DatagramPacket(messageBytes, messageBytes.length, InetAddress.getByName(ip), port);
			unicastSocket.send(packet);
		}
		catch (IOException e){
			System.out.println("Error while sending the message");
		}
		return messageBytes.length;
	}
}
