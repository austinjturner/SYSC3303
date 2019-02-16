package src.main.scheduler.states;

import src.main.scheduler.StateMachine;

/**
 * State indicating elevator motor is running, and continues until a destination floor is reached and signal is received.
 * 
 * @author austinjturner
 */
public class MotorStartedState extends State {
	
	/**
	 * Constructor for the MotorStartedState.
	 * 
	 * @param stateMachine StateMachine object used to manipulate current state of elevator.
	 */
	public MotorStartedState(StateMachine stateMachine) {
		super(stateMachine);
	}

	/**
	 * This state only moves to MotorStoppedState once it receives
	 * a signal from the elevator that it has reached the correct floor.
	 * 
	 * @return State Returns next state for the elevator.
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
