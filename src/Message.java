public class Message {

	public static String CRLF = "\r\n";
	public static String TAB = "\t";

	public static byte[] convertToBytes(String msg) {
		byte[] bytes = msg.getBytes();
		return bytes;
	}

	
}