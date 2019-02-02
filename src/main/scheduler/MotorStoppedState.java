package src.main.scheduler;

class MotorStoppedState extends State {
	
	public MotorStoppedState(StateMachine stateMachine) {
		super(stateMachine);
	}

	private boolean isFloorWait() {
		return Destination.DestinationType.WAIT == this.stateMachine.getQueueFront().destinationType;
	}
	
	@Override
	public State defaultEvent() {
		if (isFloorWait()) {
			// No need to open door. Move back to waiting state.
			this.stateMachine.dequeue();
			return new WaitingState(this.stateMachine);
		} else {
			// Let the person off the elevator
			this.stateMachine.schedulerSubsystem.sendClearFloorButtonMessage(
					this.stateMachine.elevatorID,
					this.stateMachine.currentFloor,
					this.stateMachine.goingUp);
			
			this.stateMachine.schedulerSubsystem.sendClearElevatorButtonMessage(
					this.stateMachine.elevatorID,
					this.stateMachine.currentFloor);
			
			return new LampsSignaledState(this.stateMachine);
		}
	}
}
