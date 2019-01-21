package floor;
import java.util.*;

import common.Constants;
import net.*;

public class FloorSubsystem {
	private ArrayList<Floor> floorList;
	private Responder responder;
	
	public FloorSubsystem(int numFloors) {
		System.out.println("Floor subsystem started");
		this.responder = new Responder(Constants.FLOOR_SUBSYSTEM_PORT);
		this.floorList = new ArrayList<Floor>();
		
		for (int i = 0; i < numFloors; i++) {
			Floor f = new Floor(i);
			this.floorList.add(f);
			f.run();
		}
		
		for (;;) {
			Request response = responder.receive();
			
			switch (response.GetMessage().getRequestType()){
			case MessageAPI.MSG_BOOTSTRAP_FLOOR_SUBSYSTEM:
				this.bootstrap(response); 
				break;
			default:
				System.out.println("Error here");
			}
		}
	}
	
	private void bootstrap(Request response) {
		Map<Integer,Integer> portMap = new HashMap<Integer,Integer>();
		for (Floor f : this.floorList) {
			portMap.put(f.getFloorID(), f.getPort());
		}
		Message responseMessage = new MapIntToIntMessage(MessageAPI.MSG_EMPTY_RESPONSE, portMap);
		response.sendResponse(responseMessage);
		
		System.out.println("Floor subsystem bootstrap completed successfully");
	}
	
	public static void main(String[] args) {
		new FloorSubsystem(Constants.NUM_FLOORS);
	}	
}
