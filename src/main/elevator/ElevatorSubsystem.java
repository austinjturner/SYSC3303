package src.main.elevator;

import src.main.net.Requester;
import src.main.net.Responder;
import src.main.settings.Settings;

/**
 * Elevator Subsystem that creates and starts required number of elevators in separate threads.
 * 
 * @author Samuel English
 *
 */
public class ElevatorSubsystem {
	private Thread[] elevatorThreads = new Thread[Settings.NUMBER_OF_ELEVATORS];
	
	public ElevatorSubsystem() {
		createElevators();
	}
	
	private void createElevators() {
		 for (int i = 1; i <= elevatorThreads.length; i++) {
			 elevatorThreads[i] = new Thread(new Elevator(i, 0, Settings.NUMBER_OF_FLOORS,  new Requester(), new Responder()));
			 elevatorThreads[i].start();
		 }
	}
}
