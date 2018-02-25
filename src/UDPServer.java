import java.io.*; 
import java.net.*;
import java.util.HashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class UDPServer implements Runnable {
	
	private byte[] receiveData = new byte[1024];
	private DatagramSocket unicastSocket;
	private MulticastSocket mcastSocket;
	private InetAddress mcastAddress;
	private int mcastPort;

	
    public UDPServer(int uniPort, String mcastAddress, int mcastPort) {
		try {
			System.out.println("Unicast Port: " + uniPort);
			unicastSocket = new DatagramSocket(uniPort);
		}
		catch(SocketException e){
			System.out.println("Error creating unicast socket, with port " + uniPort);
		}

		try {
			mcastSocket = new MulticastSocket(mcastPort);
		}
		catch(IOException e){
			System.out.println("Error creating multicast socket, with port " + mcastPort);
		}

		try {
			this.mcastAddress = InetAddress.getByName(mcastAddress);
		} catch (UnknownHostException e) {
			System.out.println("Couldn't find " + mcastAddress + " multicast address");
		}
		this.mcastPort = mcastPort;
    }

	@Override
	public void run() {
		String message = null;

    	try {
			 message = unicastSocket.getLocalAddress().getHostAddress() + " " + unicastSocket.getLocalPort();
		}catch (Exception e){
			System.out.println("Exception thrown");
			e.printStackTrace();
		}
		//message = mcastAddress.getHostAddress() + " " + mcastPort;

		System.out.println("Message: \"" + message + "\"");
		System.out.println("Started multicast advertisement at " + mcastAddress.getHostAddress() + " " + mcastPort);

		DatagramPacket packet = new DatagramPacket(message.getBytes(), message.length(), mcastAddress, mcastPort);

		try {
			mcastSocket.setTimeToLive(1);
		} catch (IOException e){}

		try {
			mcastSocket.send(packet);
		} catch (IOException e) {
			System.out.println("Error occured while multicasting message");
		}
	}
    
	public static void main(String[] args) {
		if(args.length != 3) {
			System.out.println("Wrong number of arguments!!\nExpected <service_port> <mcast_addr> <mcast_port>");
			System.exit(1);
		}

    	HashMap<String, String> hashtable = new HashMap<>();
		UDPServer server =  new UDPServer(Integer.parseInt(args[0]), args[1],Integer.parseInt(args[2]));


		//Start multicasting  unicast address and port advertisement
		//new Thread(server).start();

		new ScheduledThreadPoolExecutor(1).scheduleWithFixedDelay(server, 0, 1, TimeUnit.SECONDS);
		System.out.println("Successfully scheduled Multicast thread");

		String receivedMessage;
		
		while(true) {
            DatagramPacket receivedPacket = server.receive();
			receivedMessage = new String(receivedPacket.getData(), 0, receivedPacket.getLength()); //or String(receivedPacket.getData())
			System.out.println("Server Received\n\t\"" + receivedMessage + "\"");
			if(receivedMessage.contains("REGISTER")) {
				String[] info = receivedMessage.split(" ");

				if(hashtable.put(info[1], info[2]) != null) {
					System.out.println("License plate already in the system");
					server.transmit("-1", receivedPacket.getAddress(), receivedPacket.getPort());
				}
				else 
					server.transmit(Integer.toString(hashtable.size()), receivedPacket.getAddress(), receivedPacket.getPort());
			}
			else if(receivedMessage.contains("LOOKUP")) {
				String[] info = receivedMessage.split("" +
						" ");
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
