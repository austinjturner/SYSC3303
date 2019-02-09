package src.main.scheduler;

/**
 * State to signal motor to move elevator upwards or downwards towards destination floor.
 * 
 * @author austinjturner
 *
 */
class GotNextFloorState extends State {
	
	/**
	 * Constructor for the FloorDequeuedState.
	 * 
	 * @param stateMachine StateMachine object used to manipulate current state of elevator.
	 */
	public GotNextFloorState(StateMachine stateMachine) {
		super(stateMachine);
	}

	/**
	 * Start moving motor in the appropriate direction.
	 * Move the MotorStartedState
	 * 
	 * @return State Returns next state for the elevator.
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
		
		this.stateMachine.schedulerSubsystem.sendSetElevatorButtonMessage(0, targetFloor);
		
		this.stateMachine.schedulerSubsystem.debug("This is transition state: " + getStateName());
		return new MotorStartedState(this.stateMachine);
	}
}
