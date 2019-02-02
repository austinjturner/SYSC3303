package src.main.scheduler;

class GotNextFloorState extends State {
	
	public GotNextFloorState(StateMachine stateMachine) {
		super(stateMachine);
	}

	/**
	 * Start moving motor in the appropriate direction.
	 * Move the MotorStartedState
	 */
	@Override
	// Note floorQueue.get(0) changed to .remove(size-1) removing the last element and adding new floors to FRONT of queue.
	public State next() {	
		int currentFloor = this.stateMachine.currentFloor;
		int targetFloor = this.stateMachine.floorQueue.get(0).floorNum;
		
		if (currentFloor < targetFloor) {
			this.stateMachine.goingUp = true;
			this.stateMachine.schedulerSubsystem.sendMotorUpMessage(
					this.stateMachine.elevatorID);
		} else {
			this.stateMachine.goingUp = false;
			this.stateMachine.schedulerSubsystem.sendMotorDownMessage(
					this.stateMachine.elevatorID);			
		}
		
		return new MotorStartedState(this.stateMachine);
	}
}
