package src.main.scheduler;

import java.util.*;

public class DefaultAlgorithm extends Algorithm {

	public DefaultAlgorithm(Map<Integer, StateMachine> stateMachineMap) {
		super(stateMachineMap);
	}

	@Override
	public void handleFloorButtonEvent(int pickUpFloorNumber, int dropOffFloorNumber, boolean goingUp) {
		// TODO Auto-generated method stub
		
	}

	
	/**
	 * This method might not need to do anything else.
	 * For now just passes the floor info the the appropriate state machines
	 */
	@Override
	public void handleFloorSensorEvent(int floorNumber, int elevatorID) {
		this.stateMachineMap.get(elevatorID).elevatorReachedFloorEvent(floorNumber);
	}

}
