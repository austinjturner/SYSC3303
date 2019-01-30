package src.main.scheduler.elevator_fsm;

class MotorStoppedState extends State {
	
	public MotorStoppedState(StateMachine stateMachine) {
		super(stateMachine);
	}

	
	@Override
	public State next() {
		if (Destination.DestinationType.WAIT == this.stateMachine.floorQueue.get(0).destinationType) {
			this.stateMachine.floorQueue.remove(0);
			return new WaitingState(this.stateMachine);
		}
		
		// Let the person off the elevator
		this.stateMachine.schedulerSubsystem.sendClearFloorButtonMessage(
				this.stateMachine.elevatorID,
				this.stateMachine.currentFloor,
				this.stateMachine.goingUp);
		
		return new FloorLampSignalledState(this.stateMachine);
	}
}
