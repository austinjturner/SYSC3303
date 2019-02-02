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
	public State defaultEvent() {	
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
