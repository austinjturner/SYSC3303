package src.test.integration;

import java.util.*;

import org.junit.*;

import src.main.elevator.Elevator;
import src.main.net.Common;
import src.main.scheduler.*;

public class TestSystem {
	
	int elevatorID = 666;
	int numFloors = 10;
	
	
	@Test
	public void testSystemBasicUseCase() {
	
		Elevator elevator = new Elevator(elevatorID, Common.PORT_ELEVATOR_SUBSYSTEM, numFloors);
		Thread elevatorThread = new Thread(elevator);
		elevatorThread.start();
			
		SchedulerSubsystem schedulerSubsystem = new SchedulerSubsystem();
		schedulerSubsystem.start();
			
		// Wait for Elevator and Scheduler threads to stabilize
		try {
			Thread.sleep(100);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		StateMachine fsm = schedulerSubsystem.getStateMachine();
		
		/*
		 * Starting part 1 of test
		 */
		
		class TestPerson {
			Destination startDest, destDest;
			public TestPerson(Destination startDest, Destination destDest) {
				this.startDest = startDest;
				this.destDest = destDest;
			}
		}
		
		List<TestPerson> testPeople = new ArrayList<TestPerson>();
		testPeople.add(new TestPerson(
				new Destination(5, Destination.DestinationType.PICKUP),
				new Destination(10, Destination.DestinationType.DROPOFF)));
		testPeople.add(new TestPerson(
				new Destination(8, Destination.DestinationType.PICKUP),
				new Destination(1, Destination.DestinationType.DROPOFF)));
		testPeople.add(new TestPerson(
				new Destination(3, Destination.DestinationType.PICKUP),
				new Destination(4, Destination.DestinationType.DROPOFF)));
		testPeople.add(new TestPerson(
				new Destination(10, Destination.DestinationType.PICKUP),
				new Destination(2, Destination.DestinationType.DROPOFF)));;
		
		// Create a queue of people 
		// NOTE - Currently these are NOT treated as concurrent requests
		// They will be serviced in sequence letting each person off one-at-a-time
		for (TestPerson tp : testPeople) {
			fsm.floorQueue.add(tp.startDest);
		}
		
		fsm.enqueueFloorEvent();
		
		// Simulate each person getting into the elevator and pressing a button
		for (TestPerson tp: testPeople) {
			
			// Simulate button state once WaitForElevatorButtonState reached
			while (! (fsm.getState() instanceof WaitForElevatorButtonState)) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			// button press
			fsm.floorQueue.add(1, tp.destDest);
			fsm.elevatorButtonPressedEvent();
		}
		
		// Wait for final request
		while (! (fsm.getState() instanceof WaitingState)) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("Part 1 Complete!");
		
		
		/*
		 * Starting part 2 of test
		 */
		
		fsm.floorQueue.add(new Destination(6, Destination.DestinationType.PICKUP));
		fsm.enqueueFloorEvent();
		
		// Wait for arrival
		while (! (fsm.getState() instanceof WaitForElevatorButtonState)) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		// Drop person off at floor 1 AND pickup next person
		fsm.floorQueue.add(1, new Destination(1, Destination.DestinationType.PICKUP_AND_DROPOFF));
		fsm.elevatorButtonPressedEvent();
		
		while (! (fsm.getState() instanceof WaitForElevatorButtonState)) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		fsm.floorQueue.add(1, new Destination(3, Destination.DestinationType.DROPOFF));
		fsm.elevatorButtonPressedEvent();
		
		while (! (fsm.getState() instanceof WaitingState)) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		System.out.println("Part 2 Complete!");		
	}	
}
