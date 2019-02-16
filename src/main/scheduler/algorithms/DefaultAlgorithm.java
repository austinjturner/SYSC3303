package src.main.scheduler.algorithms;

import java.util.*;

import src.main.scheduler.Destination;
import src.main.scheduler.StateMachine;
import src.main.scheduler.Destination.DestinationType;

public class DefaultAlgorithm extends Algorithm {

	private Random random;
	
	public DefaultAlgorithm(Map<Integer, StateMachine> stateMachineMap) {
		super(stateMachineMap);
		random = new Random();
	}

	
	@Override
	public void handleFloorButtonEvent(int pickUpFloorNumber, int dropOffFloorNumber, boolean goingUp) {	
		int prefElevQueue = stateMachineMap.size() * 100;
		int fastestFSM = random.nextInt(stateMachineMap.size()) + 1;
		
		boolean passDir; 
		if (pickUpFloorNumber < dropOffFloorNumber) {
			passDir = true;
		} else {
			passDir = false;
		}
		
		// Iterating over the hashmap of statemachines to find a suitable elevator
		for (Map.Entry<Integer, StateMachine> entry : stateMachineMap.entrySet()) {
			int queueSize = entry.getValue().floorQueue.size();
			if (queueSize == 0) {
				fastestFSM = entry.getKey();
				break;
			} else if (queueSize > 1) {
				int nextFloor = entry.getValue().floorQueue.get(0).floorNum;
				int nextNextFloor = entry.getValue().floorQueue.get(1).floorNum;
				boolean elevatorIsGoingUp = nextFloor < nextNextFloor;
				
				if (passDir == elevatorIsGoingUp && entry.getValue().floorQueue.size() < prefElevQueue) {
					// Elevator is going is our direction
					prefElevQueue = entry.getValue().floorQueue.size();
					fastestFSM = entry.getKey();
				}
			}

		}
		
		// Adding the floors to be visited to the fastest elevator's floorQueue
		StateMachine fsm = stateMachineMap.get(fastestFSM);
		fsm.floorQueue.add(new Destination(pickUpFloorNumber, DestinationType.PICKUP));
		fsm.floorQueue.add(new Destination(dropOffFloorNumber, DestinationType.DROPOFF));
		fsm.enqueueFloorEvent();
	}

	
	/**
	 * This method might not need to do anything else.
	 * For now just passes the floor info to the appropriate state machines
	 */
	@Override
	public void handleFloorSensorEvent(int floorNumber, int elevatorID) {
		this.stateMachineMap.get(elevatorID).elevatorReachedFloorEvent(floorNumber);
	}

}
