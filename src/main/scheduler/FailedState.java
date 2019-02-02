package src.main.scheduler;

/**
 * Subclass of State to represent when a failed transition occurs. 
 * Remains in FailedState indefinitely if reached.
 * 
 * @author austinjturner
 */
public class FailedState extends State {

	/**
	 * Constructor for the FailedState.
	 * 
	 * @param stateMachine StateMachine object used to manipulate current state of elevator.
	 */
	public FailedState(StateMachine stateMachine) {
		super(stateMachine);
	}
	
	private void printErrorMessage() {
		System.out.println("ERROR: State machine in an unrecoverable state");
	}

	public State defaultEvent() {
		printErrorMessage();
		return this;
	}
	
	public State elevatorReachedFloorEvent() {
		printErrorMessage();
		return this;
	}
	
	public State elevatorButtonPressedEvent() {
		printErrorMessage();
		return this;
	}
	
	public State enqueueFloorEvent() {
		printErrorMessage();
		return this;
	}
	
	public State doorTimerEvent() {
		printErrorMessage();
		return this;
	}
}
