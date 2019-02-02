package src.main.scheduler;

/**
 * State to wait for an elevator button to be pressed.
 * 
 * @author austinjturner
 */
public class WaitForElevatorButtonState extends State {

	/**
	 * Constructor for the WaitForElevatorButtonState.
	 * 
	 * @param stateMachine StateMachine object used to manipulate current state of elevator.
	 */
	public WaitForElevatorButtonState(StateMachine stateMachine) {
		super(stateMachine);
	}

	/**
	 * Transitions to FloorDequeuedState after an elevator button is pressed.
	 * 
	 * @return State Returns next state for the elevator.
	 */
	@Override
	public State elevatorButtonPressedEvent() {
		this.stateMachine.dequeue();
		return new FloorDequeuedState(this.stateMachine);
	}
	
	@Override
	public State doorTimerEvent() {
		return new FailedState(this.stateMachine);
	}
}
