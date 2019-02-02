package src.main.scheduler;

public class FailedState extends State {

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
