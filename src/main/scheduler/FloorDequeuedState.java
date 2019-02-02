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
	}
	
	/**
	 *Moves current state into the DoorClosedState after signaling for doors to close.
	 * 
	 * @return State Returns next state for the elevator.
	 */
	@Override
	public State defaultEvent() {
		this.stateMachine.schedulerSubsystem.sendCloseDoorMessage(this.stateMachine.elevatorID);
		return new DoorClosedState(this.stateMachine);
	}

}
