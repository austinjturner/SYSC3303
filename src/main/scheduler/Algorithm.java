package src.main.scheduler;

import java.util.*;

/**
 * This Algorithm interface is package scoped.
 * 
 * 
 * 
 * @author austinjturner
 *
 */
abstract class Algorithm {
	Map<Integer, StateMachine> stateMachineMap;
	
	public Algorithm(Map<Integer, StateMachine> stateMachineMap) {
		this.stateMachineMap = stateMachineMap;
	}
	
	public abstract void handleFloorButtonEvent(
			int pickUpFloorNumber, int dropOffFloorNumber, boolean goingUp);
	
	public abstract void handleFloorSensorEvent(
			int floorNumber, int elevatorID);
	
}
