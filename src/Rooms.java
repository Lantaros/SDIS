class Rooms {
	private int id_room;
	private int[] client_id = new int[4];
	private int nClients = 0;

	public Rooms(int id_room) {
		this.id_room = id_room;
    }

    public void setClientId(int id_client) {
    	this.nClients++;
    	client_id[this.nClients] = id_client;
    }

    public int getRoomId() {
    	return this.id_room;
    }

}