package protocol;

public class InvalidMessage extends Throwable {
    String type;

    public InvalidMessage(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Invalid protocol.Message Type " + type;
    }
}
