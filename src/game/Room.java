package game;

public class Room {
	private int id_room;
	private int[] client_id = new int[5];
	private int nClients = 0;
	private String name = "";
	private Hangman game = null;
	private boolean owner = false;
	private boolean[] isReady = { false, false, false, false, false };

	public Room(int id_room) {
		this.id_room = id_room;
	}

	public void addClientId(int id_client) {
		this.nClients++;
		client_id[this.nClients] = id_client;
	}

	public void setClientId(int id_client, int id_general) {
		client_id[id_client] = id_general;
	}

	public int getRoomId() {
		return this.id_room;
	}

	public int[] getClients() {
		return this.client_id;
	}

	public int getNClients() {
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

	public String getName(){
	    return name;
    }

	public boolean isReady(int idClient) {
		for (int i = 0; i <= nClients; i++)
			if (client_id[i] == idClient)
				return true;
		return false;
	}

	public void setAllReadyToFalse() {
		for (int i = 0; i <= nClients; i++)
			isReady[i] = false;

	}

	public void setReady(int idClient) {
		for (int i = 0; i <= nClients; i++)
			if (client_id[i] == idClient)
				isReady[i] = true;
	}
	
	public boolean isEveryoneReady() {
		for (int i = 1; i <= nClients; i++)
			if (isReady[i] == false)
				return false;
		return true;
	}

    public void setName(String roomName) {
		this.name = roomName;
    }

    public void setNClients(int nClients) {
	    this.nClients = nClients;
    }
}