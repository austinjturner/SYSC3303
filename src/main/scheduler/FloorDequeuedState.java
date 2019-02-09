package src.main.scheduler;

/**
 * State representing the elevator door closing.
 * 
 * @author austinjturner
 *
 */
public class FloorDequeuedState extends State {

	/**
	 * Constructor for the FloorDequeuedState.
	 * 
	 * @param stateMachine StateMachine object used to manipulate current state of elevator.
	 */
	public FloorDequeuedState(StateMachine stateMachine) {
		super(stateMachine);
		
		/*
		 * We start this timer to represent the time it takes to close the door
		 */
		startDoorWaitTimer();
	}
	
	private void startDoorWaitTimer() {
		new DoorWaitTimer(this.stateMachine).start();
	}
	
	/**
	 *Moves current state into the DoorClosedState after signaling for doors to close.
	 * 
	 * @return State Returns next state for the elevator.
	 */
	@Override
	public State doorTimerEvent() {
		this.stateMachine.schedulerSubsystem.sendCloseDoorMessage(this.stateMachine.elevatorID);
		this.stateMachine.schedulerSubsystem.debug("This is transition state: " + getStateName());
		return new DoorClosedState(this.stateMachine);
	}

}
