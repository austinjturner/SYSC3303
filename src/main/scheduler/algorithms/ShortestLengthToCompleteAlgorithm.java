package src.main.scheduler.algorithms;

import java.util.*;

import src.main.net.MessageAPI.FaultType;
import src.main.scheduler.Destination;
import src.main.scheduler.StateMachine;
import src.main.scheduler.Destination.DestinationType;

public class ShortestLengthToCompleteAlgorithm extends Algorithm {

	private int FLOOR_BUFFER = 1;
	
	public ShortestLengthToCompleteAlgorithm(Map<Integer, StateMachine> stateMachineMap) {
		super(stateMachineMap);
	}

	
	@Override
	public void handleFloorButtonEvent(int pickUpFloorNumber, int dropOffFloorNumber, boolean goingUp,
			FaultType faultType, int faultFloorNumber) {
		
		boolean passDir = pickUpFloorNumber < dropOffFloorNumber; 
		
		/*
		 * Hold information about each possible elevator choice
		 * NOTE: elevator IDs start at 1 --> So in this array index 0 designates elevator ID 1
		 */
		int[] distanceToComplete = new int[stateMachineMap.size()];
		int[] stopsToComplete = new int[stateMachineMap.size()];
		int[] startIndex = new int[stateMachineMap.size()];
		int[] endIndex = new int[stateMachineMap.size()];
		
		// Iterating over the hashmap of statemachines to find a suitable elevator
		for (Map.Entry<Integer, StateMachine> entry : stateMachineMap.entrySet()) {
			
			/*
			 * 3 Cases based off current state of elevator and passenger start floor and direction
			 */
			
			StateMachine fsm = entry.getValue();
			List<Destination> queue = fsm.floorQueue;
			int i = fsm.elevatorID - 1;		// IDs are 1-indexed
			
			/*
			 * Case 1: 	Queue is empty
			 */
			if (queue.size() == 0) {
				distanceToComplete[i] = 
						Math.abs(fsm.currentFloor - pickUpFloorNumber) + 
						Math.abs(pickUpFloorNumber - dropOffFloorNumber);
				
				stopsToComplete[i] = 2;
				startIndex[i] = 0;
				endIndex[i] = 0;
				continue;
			}
			
			/*
			 * Not sure what to do in the case. Just skip for now.
			 */
			if (queue.size() == 1) {
				distanceToComplete[i] = -1;
				stopsToComplete[i] = -1;
				startIndex[i] = -1;
				endIndex[i] = -1;
				continue;
			}
			
			int nextFloor = queue.get(0).floorNum;
			int nextNextFloor = queue.get(1).floorNum;
			boolean elevatorIsGoingUp = nextFloor < nextNextFloor;
			
			
			/*
			 * Case 2: 	Elevator is going in the same direction as passenger
			 * 				AND
			 * 			Elevator has NOT passed passengers floor yet
			 */
			if (passDir == elevatorIsGoingUp && (
					passDir && fsm.currentFloor < pickUpFloorNumber + FLOOR_BUFFER ||
					!passDir && fsm.currentFloor > pickUpFloorNumber - FLOOR_BUFFER)) {
				
				distanceToComplete[i] = 
						Math.abs(fsm.currentFloor - pickUpFloorNumber) + 
						Math.abs(pickUpFloorNumber - dropOffFloorNumber);
				
				// Default case -- Holds until proven otherwise
				startIndex[i] = 0;
				endIndex[i] = 0;
				int queueIndex;
				
				if (passDir) {
					for (
							queueIndex = 0; 
							queueIndex < queue.size() && queue.get(queueIndex).floorNum < dropOffFloorNumber; 
							queueIndex++) {
						
						if (queue.get(queueIndex).floorNum < pickUpFloorNumber) {
							startIndex[i] = queueIndex + 1;
						}
						endIndex[i] = queueIndex + 1;
						stopsToComplete[i] = queueIndex + 2;	// +2 for added stops
					}
				} else {
					for (
							queueIndex = 0; 
							queueIndex < queue.size() && queue.get(queueIndex).floorNum > dropOffFloorNumber; 
							queueIndex++) {
						
						if (queue.get(queueIndex).floorNum > pickUpFloorNumber) {
							startIndex[i] = queueIndex + 1;
						}
						endIndex[i] = queueIndex + 1;
						stopsToComplete[i] = queueIndex + 2;	// +2 for added stops
					}
				}
			
				
			/*
			 * Case 3: Everything else for now
			 */
			} else {
				distanceToComplete[i] = 
						Math.abs(queue.get(queue.size() - 1).floorNum - pickUpFloorNumber) + 
						Math.abs(pickUpFloorNumber - dropOffFloorNumber);
				stopsToComplete[i] = 2;		// For added stops
				
				for(int queueIndex = 1; queueIndex < queue.size(); queueIndex++) {
					distanceToComplete[i] += Math.abs(queue.get(queueIndex).floorNum - queue.get(queueIndex - 1).floorNum);
					stopsToComplete[i]++;
				}
				
				// Will be scheduled at the end
				startIndex[i] = queue.size();
				endIndex[i] = queue.size();
			}
		}
		
		
		
		/*
		 * Determine best elevator to add request to.
		 * CURRENTLY --> just find min distance
		 */
		int bestIndex = 0;
		for (int i = 1; i < distanceToComplete.length; i++) {
			
			// Check if state machine is schedulable
			if (!stateMachineMap.get(i + 1).isSchedulable()) {
				continue;
			}
			
			if (distanceToComplete[i] >= 0 && (
					distanceToComplete[bestIndex] < 0 || 
					distanceToComplete[i] < distanceToComplete[bestIndex])) {
				bestIndex = i;
			}
		}
		
		
		// Adding the floors to be visited to the fastest elevator's floorQueue
		StateMachine fsm = stateMachineMap.get(bestIndex + 1);	// elevatorID 1-indexed
		
		fsm.floorQueue.add(startIndex[bestIndex], new Destination(
				pickUpFloorNumber, DestinationType.PICKUP, faultType, faultFloorNumber));
		fsm.floorQueue.add(endIndex[bestIndex] + 1, new Destination(
				dropOffFloorNumber, DestinationType.DROPOFF));
		
		consolidateQueue(fsm.floorQueue);
		fsm.enqueueFloorEvent();
	}

	
	private void consolidateQueue(List<Destination> list) {
		for (int i = 0; i < list.size() - 1; i++) {
			Destination nextFloor = list.get(i);
			Destination nextNextFloor = list.get(i + 1);
			if (nextFloor.floorNum == nextNextFloor.floorNum) {
				// Need to consolidate
				DestinationType dt;
				if (nextFloor.destinationType == nextNextFloor.destinationType) {
					dt = nextFloor.destinationType;		// Pick either one, they're the same
				} else {
					dt = Destination.DestinationType.PICKUP_AND_DROPOFF;	// Must be both
				}
				
				list.get(i).destinationType = dt;
				list.remove(i + 1);
				
				i--;	// Need to check this index again after node was removed
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
