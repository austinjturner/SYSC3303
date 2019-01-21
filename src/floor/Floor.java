package floor;

import net.*;

public class Floor extends Thread {
	private int floorID;
	private Requester requester;
	private Responder responder;
	
	public Floor(int floorID) {
		this.floorID = floorID;
		this.responder = new Responder();
		this.requester = new Requester();
	}
	
	public int getFloorID() {
		return this.floorID;
	}
	
	public int getPort() {
		// This port is exposed to the system
		return this.responder.GetPort();
	}
	
	public void run(int floorID) {
		Floor floor = new Floor(floorID);
	}
}
