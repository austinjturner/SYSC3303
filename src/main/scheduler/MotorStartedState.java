package src.main.scheduler;

public class MotorStartedState extends State {
	
	public MotorStartedState(StateMachine stateMachine) {
		super(stateMachine);
	}

	/**
	 * This state only moves to MotorStoppedState once it receives
	 * a signal from the elevator that it has reached the correct floors
	 */
	@Override
	public State elevatorReachedFloorEvent() {
		int targetFloor = this.stateMachine.getQueueFront().floorNum;
		
		if (this.stateMachine.currentFloor == targetFloor) {
			this.stateMachine.schedulerSubsystem.sendMotorStopMessage(
					this.stateMachine.elevatorID);
			return new MotorStoppedState(this.stateMachine);
		} else {
			return this;
		}
	}
	
	
}
