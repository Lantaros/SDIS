import java.util.Scanner;

class Message {
	//messages types!
	public static final int ROOM_AVAILABLE = 0;
	public static final int ROOM_CONNECT = 1;
	public static final int ROOM_CREATE = 2;

	public static String CRLF = "\r\n";
	public static String TAB = "\t";

	public static byte[] convertToBytes(String msg) {
        return msg.getBytes();
	}

	public static byte[] handler(String type) {
		byte[] bytes = type.getBytes();
		switch (type) {
			case "ROOM_AVAILABLE":
                break;
            case "ROOM_CONNECT":
                return roomConnect();
            case "ROOM_CREATE":
                break;		
		}
		return bytes;
	}
	public static byte[] roomConnect() {
		Scanner sc = new Scanner(System.in);
		System.out.println("Qual é a sala que te queres ligar?");
		int i = sc.nextInt();
		String msg = Integer.toString(ROOM_CONNECT) + " ROOM_CONNECT " + Integer.toString(i);
		System.out.println(msg);
		return convertToBytes(msg);
	}
}