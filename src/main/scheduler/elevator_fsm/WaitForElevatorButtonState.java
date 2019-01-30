package src.main.scheduler.elevator_fsm;

public class WaitForElevatorButtonState extends State {

	public WaitForElevatorButtonState(StateMachine stateMachine) {
		super(stateMachine);
	}

	@Override
	public State elevatorButtonPressed() {
		this.stateMachine.floorQueue.remove(0);
		return new FloorRemovedFromQueueState(this.stateMachine);
	}
	
	@Override
	public State doorTimer() {
		return new FailedState(this.stateMachine);
	}
}
