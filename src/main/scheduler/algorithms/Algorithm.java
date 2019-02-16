package src.main.scheduler.algorithms;

import java.util.*;

import src.main.scheduler.SchedulerSubsystem;
import src.main.scheduler.StateMachine;

/**
 * This Algorithm interface is package scoped.
 * 
 * 
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
			int pickUpFloorNumber, int dropOffFloorNumber, boolean goingUp);
	
	public abstract void handleFloorSensorEvent(
			int floorNumber, int elevatorID);
	
	SchedulerSubsystem getSchedulerSubsystem(){
		// Assumes 1 elevator in map
		return stateMachineMap.get(0).schedulerSubsystem;
	}	
}
