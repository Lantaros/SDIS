import java.io.*;
import java.net.*;

public class UDPClient {
	private byte[] receiveData = new byte[1024];
	private DatagramSocket unicastSocket;
	private MulticastSocket mcastSocket;
	private InetAddress mcastAddress;

	public UDPClient(String mcastAddr, String mcastPort){
		try {
			this.unicastSocket = new DatagramSocket();
		}
		catch (SocketException e){
			System.out.println("Unicast Socket creation error");
		}


		try {
			this.mcastSocket = new MulticastSocket(Integer.parseInt(mcastPort));
		} catch (IOException e) {
			System.out.println("Multicast Socket creation error, port " + mcastPort);
		}

		try {
			this.mcastAddress = InetAddress.getByName(mcastAddr);
		} catch (UnknownHostException e) {
			System.out.println("Unknown Multicast host: " + mcastAddr);
		}
		//Join multicast group
		try {
			mcastSocket.joinGroup(this.mcastAddress);
		} catch (IOException e) {
			System.out.println("Failed to join multicast group");
			System.exit(1);
		}
	}

	@Override
	protected void finalize() throws Throwable {
		mcastSocket.leaveGroup(mcastAddress);
	}

	public static void main(String[] args) {
		if(args.length < 3) {
			System.out.println("Expected <mcast_addr> <mcast_portr> <operation> <op_arguments>");
			System.exit(1);
		}

		UDPClient client = new UDPClient(args[0], args[1]);
		DatagramPacket serviceInfo = new DatagramPacket(client.receiveData, client.receiveData.length);


		//Receive Server ip and port
		try {
			client.mcastSocket.receive(serviceInfo);
		} catch (IOException e) {
			System.out.println("Failed to receive service address and port through multicast");
		}

		String mcastResp = new String(serviceInfo.getData());
		mcastResp =  mcastResp.trim();
		String[] mcastRespArr = mcastResp.split(" ");
		System.out.println("Received Server Ip and port: " + mcastRespArr[0] + " " + mcastRespArr[1]);

		if(args[2].equals("register")) {
			try {
				client.transmit("REGISTER " + args[3] + " " + args[4], InetAddress.getByName(mcastRespArr[0]), Integer.parseInt(mcastRespArr[1]));
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		}
		else if(args[2].equals("lookup")) {
			try {
				client.transmit("LOOKUP " + args[3], InetAddress.getByName(mcastRespArr[0]), Integer.parseInt(mcastRespArr[1]));
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			System.out.println("Transmited: \"LOOKUP " + args[3] + "\"");
		}
		else{
			System.out.println("Application invoked with wrong arguments");
			System.out.println("Expected <host_name> <port_number> <oper> <opnd>*");
			System.exit(1);
		}
	
		String response = client.receive();
		System.out.println(response);
	}

	private String receive() {
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

		try {
			unicastSocket.receive(receivePacket);
		}
		catch (IOException e) {
			System.out.println("IOException while receive the message\n");
			return null;
		}

		String response = new String(receivePacket.getData(), 0, receivePacket.getLength());
		response.trim();
		return response;
	}

	private int transmit(String message, InetAddress ip, int port) {
		byte[] messageBytes = message.getBytes();

		try {
			DatagramPacket packet = new DatagramPacket(messageBytes, messageBytes.length, ip, port);
			unicastSocket.send(packet);
		}
		catch (IOException e){
			System.out.println("Error while sending the message");
		}
		return messageBytes.length;
	}
}
