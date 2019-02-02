package src.main.scheduler;

public class WaitingState extends State {
	
	public WaitingState(StateMachine stateMachine) {
		super(stateMachine);
	}

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
