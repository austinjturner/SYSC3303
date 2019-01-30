package src.main.scheduler.elevator_fsm;

public class FailedState extends State {

	public FailedState(StateMachine stateMachine) {
		super(stateMachine);
	}

	private void printErrorMessage() {
		System.out.println("ERROR: State machine in an unrecoverable state");
	}
	
	public State next() {
		printErrorMessage();
		return this;
	}
	
	public State elevatorReacherFloor() {
		printErrorMessage();
		return this;
	}
	
	public State elevatorButtonPressed() {
		printErrorMessage();
		return this;
	}
	
	public State newItemInQueue() {
		printErrorMessage();
		return this;
	}
	
	public State doorTimer() {
		printErrorMessage();
		return this;
	}
}
