package src.main.scheduler;

import java.util.*;

public class DefaultAlgorithm extends Algorithm {

	public DefaultAlgorithm(Map<Integer, StateMachine> stateMachineMap) {
		super(stateMachineMap);
	}

	@Override
	public void handleFloorButtonEvent(int pickUpFloorNumber, int dropOffFloorNumber, boolean goingUp) {
		// TODO Auto-generated method stub
		
		// Iterating over the hashmap of statemachines to find a suitable elevator
		for (Map.Entry<Integer, StateMachine> entry : stateMachineMap.entrySet()) {
			if (entry.getValue().goingUp == goingUp) {
				// TO_DO if statement body needs to be finished, if statement for choosing elevators going in direction of floor
				// Note may need to look at goingUp from floorButtonPressMessage
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
