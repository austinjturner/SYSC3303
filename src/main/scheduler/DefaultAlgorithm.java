package src.main.scheduler;

import java.util.*;

import src.main.scheduler.Destination.DestinationType;

public class DefaultAlgorithm extends Algorithm {

	public DefaultAlgorithm(Map<Integer, StateMachine> stateMachineMap) {
		super(stateMachineMap);
	}

	@Override
	public void handleFloorButtonEvent(int pickUpFloorNumber, int dropOffFloorNumber, boolean goingUp) {
		
		int prefElevQueue = 20;
		int fastestFSM = 0;
		
		boolean passDir; 
		if (pickUpFloorNumber < dropOffFloorNumber) {
			passDir = true;
		} else {
			passDir = false;
		}
		
		// Iterating over the hashmap of statemachines to find a suitable elevator
		for (Map.Entry<Integer, StateMachine> entry : stateMachineMap.entrySet()) {
			
			if (entry.getValue().goingUp == passDir) {
				if (entry.getValue().floorQueue.size() < prefElevQueue) {
					prefElevQueue = entry.getValue().floorQueue.size();
					fastestFSM = entry.getKey();
				}
			}
		}
		
		// Adding the floors to be visited to the fastest elevator's floorQueue
		for (Map.Entry<Integer, StateMachine> entry : stateMachineMap.entrySet()) {
			if (entry.getKey() == fastestFSM) {
				entry.getValue().floorQueue.add(new Destination(pickUpFloorNumber, DestinationType.PICKUP));
				entry.getValue().floorQueue.add(new Destination(dropOffFloorNumber, DestinationType.DROPOFF));
			}
		}
		
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
