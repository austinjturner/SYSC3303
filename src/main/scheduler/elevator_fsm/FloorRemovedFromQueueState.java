package src.main.scheduler.elevator_fsm;

public class FloorRemovedFromQueueState extends State {

	public FloorRemovedFromQueueState(StateMachine stateMachine) {
		super(stateMachine);
	}
	
	@Override
	public State next() {
		this.stateMachine.schedulerSubsystem.sendCloseDoorMessage(this.stateMachine.elevatorID);
		return new DoorClosedState(this.stateMachine);
	}

}
