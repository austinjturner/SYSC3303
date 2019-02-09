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
	List<StateMachine> stateMachineList;
	
	public Algorithm(List<StateMachine> stateMachineList) {
		this.stateMachineList = stateMachineList;
	}
	
	public abstract void handleFloorButtonEvent(
			int pickUpFloorNumber, int dropOffFloorNumber, boolean goingUp);
	
	public abstract void handleFloorSensorEvent(
			int floorNumber, int ElevatorNumber);
}
