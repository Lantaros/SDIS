class Message {

	public static String CRLF = "\r\n";
	public static String TAB = "\t";

	public static byte[] convertToBytes(String msg) {
        return msg.getBytes();
	}

	public static byte[] handler(String msg) {
		byte[] bytes = msg.getBytes();
		return bytes;
	}
}