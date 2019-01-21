package elevator;

import java.util.*;

import common.Constants;
import net.MapIntToIntMessage;
import net.Message;
import net.MessageAPI;
import net.Responder;
import net.Request;

public class ElevatorSubsystem {
	
	private List<Elevator> elevatorList;
	private Responder responder;
	
	public ElevatorSubsystem(int numFloors) {
		System.out.println("Elevator subsystem started");
		this.responder = new Responder(Constants.ELEVATOR_SUBSYSTEM_PORT);
		this.elevatorList = new ArrayList<Elevator>();
		
		for (int i = 0; i < numFloors; i++) {
			Elevator e = new Elevator(i);
			this.elevatorList.add(e);
			e.run();
		}
		
		for (;;) {
			Request response = responder.receive();
			
			switch (response.GetMessage().getRequestType()){
			case MessageAPI.MSG_BOOTSTRAP_ELEVATOR_SUBSYSTEM:
				this.bootstrap(response);
				break;
			default:
				System.out.println("Error here");
			}
		}
	}
	
	private void bootstrap(Request response) {
		Map<Integer,Integer> portMap = new HashMap<Integer,Integer>();
		for (Elevator e : this.elevatorList) {
			portMap.put(e.getElevatorID(), e.getPort());
		}
		Message responseMessage = new MapIntToIntMessage(MessageAPI.MSG_EMPTY_RESPONSE, portMap);
		response.sendResponse(responseMessage);
		
		System.out.println("Elevator subsystem bootstrap completed successfully");
	}
	
	public static void main(String[] args) {
		new ElevatorSubsystem(Constants.NUM_ELEVATORS);
	}	
}