package src.main.scheduler.algorithms;

import java.util.*;

import src.main.net.MessageAPI.FaultType;
import src.main.scheduler.SchedulerSubsystem;
import src.main.scheduler.StateMachine;

/**
 * This class represents an algorithm that the scheduler subsystem
 * will use to schedule all elevators.
 * 
 * This class is abstract, and must be implemented by a concrete class
 * to supply the logic for the system.
 * 
 * @author austinjturner
 *
 */
public abstract class Algorithm {
	Map<Integer, StateMachine> stateMachineMap;
	
	public Algorithm(Map<Integer, StateMachine> stateMachineMap) {
		this.stateMachineMap = stateMachineMap;
	}
	
	public abstract void handleFloorButtonEvent(
			int pickUpFloorNumber, int dropOffFloorNumber, boolean goingUp,
			FaultType faultType, int faultFloorNumber);
	
	public abstract void handleFloorSensorEvent(
			int floorNumber, int elevatorID);
	
	
	public void handleElevatorWaiting() {
		
	}
	
	SchedulerSubsystem getSchedulerSubsystem(){
		// Assumes 1 elevator in map
		return stateMachineMap.get(1).schedulerSubsystem;
	}	
}
