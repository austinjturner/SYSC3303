package src.main.scheduler.states;

import src.main.scheduler.Destination;
import src.main.scheduler.StateMachine;

/**
 * State that checks if current floor corresponds to a destination floor,
 * opens doors if true. 
 * 
 * @author austinjturner
 */
public class WaitingState extends State {
	
	/**
	 * Constructor for the WaitingState.
	 * 
	 * @param stateMachine StateMachine object used to manipulate current state of elevator.
	 */
	public WaitingState(StateMachine stateMachine) {
		super(stateMachine);
	}

	/**
	 * This state moves to DoorOpenedState if current floor is the destination floor after signaling doors open,
	 * otherwise transitions to GotNextFloorState to continue moving to next floor.
	 * 
	 * @return State Returns next state for the elevator.
	 */
	@Override
	public State enqueueFloorEvent() {
		Destination dest = this.stateMachine.getQueueFront();;
		int targetFloor = dest.floorNum;
		int currentFloor = this.stateMachine.currentFloor;
		
		if (currentFloor == targetFloor) {
			/*
			 * Detect if a fault should be simulated.
			 * If detected, send message to elevator
			 */
			if (dest.hasFault()) {
				this.stateMachine.schedulerSubsystem.sendSimulateFaultMessage(
						this.stateMachine.elevatorID, dest.faultFloorNumber, dest.faultType);
			}
			
			this.stateMachine.schedulerSubsystem.sendOpenDoorMessage(
					this.stateMachine.elevatorID);
			return new DoorOpenedState(this.stateMachine);
		} 

		return new GotNextFloorState(this.stateMachine);
	}
}
