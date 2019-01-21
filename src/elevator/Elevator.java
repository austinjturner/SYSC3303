package elevator;
import net.Requester;
import net.Responder;

public class Elevator extends Thread {
	private int elevatorID;
	private Requester requester;
	private Responder responder;

	public Elevator(int elevatorID) {
		this.elevatorID = elevatorID;
		this.responder = new Responder();
		this.requester = new Requester();
	}

	public int getElevatorID() {
		return this.elevatorID;
	}

	public int getPort() {
		return this.responder.GetPort();
	}

	public void run(int elevatorID) {
		Elevator elevator = new Elevator(elevatorID);
	}
}
