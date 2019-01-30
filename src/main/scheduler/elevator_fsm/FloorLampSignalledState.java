package src.main.scheduler.elevator_fsm;

class FloorLampSignalledState extends State {

	public FloorLampSignalledState(StateMachine stateMachine) {
		super(stateMachine);
	}
	
	public State next() {
		this.stateMachine.schedulerSubsystem.sendClearElevatorButtonMessage(
				this.stateMachine.elevatorID,
				this.stateMachine.currentFloor);
		return new ElevatorLampSignalledState(this.stateMachine);
	}

}
