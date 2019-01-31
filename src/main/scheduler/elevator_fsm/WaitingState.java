package src.main.scheduler.elevator_fsm;

public class WaitingState extends State {
	
	public WaitingState(StateMachine stateMachine) {
		super(stateMachine);
	}

	@Override
	public State newItemInQueue() {
		int currentFloor = this.stateMachine.currentFloor;
		int targetFloor = this.stateMachine.floorQueue.get(0).floorNum;
		
		if (currentFloor == targetFloor) {
			this.stateMachine.schedulerSubsystem.sendOpenDoorMessage(
					this.stateMachine.elevatorID);
			return new DoorOpenedState(this.stateMachine);
		} 

		return new GotNextFloorState(this.stateMachine);
	}
}
