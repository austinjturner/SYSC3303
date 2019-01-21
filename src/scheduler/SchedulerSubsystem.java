package scheduler;
import java.net.*;
import java.util.*;

import common.Constants;
import net.MapIntToIntMessage;
import net.Message;
import net.MessageAPI;
import net.Requester;

public class SchedulerSubsystem {
	
	private Requester requester;
	private Map<Integer,Integer> elevatorPortMap, floorPortMap;

	public SchedulerSubsystem() {
		System.out.println("Scheduler subsystem started");
		this.requester = new Requester();
	}
	
	public void bootstrap() {
		/*
		 * Bootstrap ElevatorSubsystesm
		 */
		Message msg = new Message(MessageAPI.MSG_BOOTSTRAP_ELEVATOR_SUBSYSTEM);
		Message response = null;
		try {
			response = this.requester.sendRequest(InetAddress.getLocalHost(),
					Constants.ELEVATOR_SUBSYSTEM_PORT, msg);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

		this.elevatorPortMap = new MapIntToIntMessage(response).getDataObject();
		System.out.println("[Elevator Port Map] elevatorID:port");
		System.out.println(this.elevatorPortMap);
		
		/*
		 * Use elevatorPortMap to bootstrap FloorSubsystem
		 */
		msg = new MapIntToIntMessage(MessageAPI.MSG_BOOTSTRAP_FLOOR_SUBSYSTEM, this.elevatorPortMap);
		try {
			response = this.requester.sendRequest(InetAddress.getLocalHost(),
					Constants.FLOOR_SUBSYSTEM_PORT, msg);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		this.elevatorPortMap = new MapIntToIntMessage(response).getDataObject();
		System.out.println("[Floor Port Map] floorID:port");
		System.out.println(this.elevatorPortMap);
		
		System.out.println("Scheduler subsystem bootstrap completed successfully");
		System.out.println("System bootstrap completed successfully");
	}
	
	public static void main(String[] args) {	
		SchedulerSubsystem schedulerSubsystem = new SchedulerSubsystem();
		schedulerSubsystem.bootstrap();
	}
}
