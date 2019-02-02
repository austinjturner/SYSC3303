package src.main.scheduler;

public class WaitForElevatorButtonState extends State {

	public WaitForElevatorButtonState(StateMachine stateMachine) {
		super(stateMachine);
	}

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
