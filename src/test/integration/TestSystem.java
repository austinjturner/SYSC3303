package src.test.integration;

import org.junit.*;

import src.main.elevator.Elevator;
import src.main.net.Common;
import src.main.scheduler.*;

public class TestSystem {
	
	int elevatorID = 666;
	int numFloors = 10;
	
	@Test
	public void testSystem() {

		int targetFloor1 = 10;
		int targetFloor2 = 5;
		
		Elevator elevator = new Elevator(elevatorID, Common.PORT_ELEVATOR_SUBSYSTEM, numFloors);
		Thread elevatorThread = new Thread(elevator);
		elevatorThread.start();
		
		SchedulerSubsystem ss = new SchedulerSubsystem();
		ss.start();
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		StateMachine fsm = ss.getStateMachine();
		//fsm.go();
		
		fsm.floorQueue.add(0, new Destination(targetFloor1, Destination.DestinationType.PICKUP));
		fsm.newItemInQueue();
		
		while (! (fsm.getState() instanceof WaitForElevatorButtonState)) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		fsm.floorQueue.add(1, new Destination(targetFloor2, Destination.DestinationType.DROPOFF));
		fsm.floorButtonPressed();
		
		while (! (fsm.getState() instanceof WaitingState)) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("Nice!");
	}
}
