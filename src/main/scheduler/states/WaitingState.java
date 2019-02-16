package src.main.scheduler.states;

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
		int currentFloor = this.stateMachine.currentFloor;
		int targetFloor = this.stateMachine.getQueueFront().floorNum;
		
		if (currentFloor == targetFloor) {
			this.stateMachine.schedulerSubsystem.sendOpenDoorMessage(
					this.stateMachine.elevatorID);
			return new DoorOpenedState(this.stateMachine);
		} 

		return new GotNextFloorState(this.stateMachine);
	}
}
