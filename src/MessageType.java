public enum MessageType {
    PUTCHUNK, //BACKUP chunk request
    STORED,   //Successfully stored chunk notification

    GETCHUNK, //Restore chunk request
    CHUNK,

    DELETE,   //Delete file request
    REMOVED,

    MANAGE_STORAGE,
    RETRIEVE_INFO;

    @Override
    public String toString() {
        switch (this) {
            case PUTCHUNK:
                return "PUTCHUNK";

            case STORED:
                return "STORED";

            case GETCHUNK:
                return "GETCHUNK";

            case CHUNK:
                return "CHUNK";

            case DELETE:
                return "DELETE";

            case REMOVED:
                return "REMOVED";

            case MANAGE_STORAGE:
                return "MANAGE_STORAGE";

            case RETRIEVE_INFO:
                return "RETRIEVE_INFO";

            default:
                return "FAIL";
        }
    }
}