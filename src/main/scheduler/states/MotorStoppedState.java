package src.main.scheduler.states;

import src.main.scheduler.Destination;
import src.main.scheduler.StateMachine;

/**
 * State indicating the elevator motor has stopped.
 * 
 * @author austinjturner
 */
class MotorStoppedState extends State {
	
	public MotorStoppedState(StateMachine stateMachine) {
		super(stateMachine);
	}

	/**
	 * Conditional check for destinationType of state at from of the stateMachine queue.
	 * 
	 * @return boolean Boolean of destinationType conditional check 
	 */
	private boolean isFloorWait() {
		return Destination.DestinationType.WAIT == this.stateMachine.getQueueFront().destinationType;
	}
	
	/**
	 * This state only moves to LampsSignaledState if elevator has arrived at a destination floor.
	 * If it has, it signals to clear floor and elevator buttons first.
	 * 
	 * @return State Returns next state for the elevator.
	 */
	@Override
	public State defaultEvent() {
		if (isFloorWait()) {
			// No need to open door. Move back to waiting state.
			this.stateMachine.dequeue();
			this.stateMachine.schedulerSubsystem.debug("This is transition state: " + getStateName());
			return new WaitingState(this.stateMachine);
		} else {
			// Let the person off the elevator
			this.stateMachine.schedulerSubsystem.sendClearFloorButtonMessage(
					this.stateMachine.currentFloor,
					this.stateMachine.goingUp);
			
			this.stateMachine.schedulerSubsystem.sendClearElevatorButtonMessage(
					this.stateMachine.elevatorID,
					this.stateMachine.currentFloor);
			
			return new LampsSignaledState(this.stateMachine);
		}
	}
}
