
class Room {
	private int id_room;
	private int[] client_id = new int[4];
	private int nClients = 0;
    private Hangman game;
    private boolean owner = false;

	public Room(int id_room) {
		this.id_room = id_room;
    }

    public void setClientId(int id_client) {
    	this.nClients++;
    	client_id[this.nClients] = id_client;
    }

    public int getRoomId() {
    	return this.id_room;
    }

    public int[] getClients() {
        return this.client_id;
    }

    public int getnClients() {
        return this.nClients;
    }

    public void addGame(Hangman game) {
        this.game = game;
    }

    public Hangman getGame() {
        return this.game;
    }

    public void setOwner(boolean yes) {
        this.owner = yes;
    }

    public boolean getOwner() {
        return this.owner;
    }

}