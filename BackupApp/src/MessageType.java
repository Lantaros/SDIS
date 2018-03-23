public enum MessageTypeEnum {
    BACKUP,
    RESTORE,
    DELETE,
    MANAGE_STORAGE,
    RETRIEVE_INFO
}

public class MessageType {
    private MessageTypeEnum type;

    public EnumTest(MessageTypeEnum type) {
        this.type = type;
    }

    @Override
    public String toString() {
        switch (type) {
            case BACKUP:
                return "BACKUP";
                break;

            case RESTORE:
                return "RESTORE";
                break;

            case DELETE:
                return "DELETE";
                break;

            case MANAGE_STORAGE:
                return "MANAGE_STORAGE";
                break;

            case RETRIEVE_INFO:
                return "RETRIEVE_INFO";
                break;

            default:
                return "FAIL";
                break;
        }
}