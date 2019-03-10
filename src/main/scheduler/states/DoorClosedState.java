package src.main.scheduler.states;

import src.main.net.MessageAPI;
import src.main.scheduler.StateMachine;
import src.main.settings.Settings;

/**
 * State representing the elevator with door closed.
 * 
 * @author austinjturner
 *
 */
class DoorClosedState extends State {

	/**
	 * Constructor for the ClosedDoorState.
	 * 
	 * @param stateMachine StateMachine object used to manipulate current state of elevator.
	 */
	public DoorClosedState(StateMachine stateMachine) {
		super(stateMachine);
		
		startDoorWaitTimer();
	}
	
	private void startDoorWaitTimer() {
		new DoorWaitTimer(this.stateMachine, Settings.WAIT_TIME_CLOSE_DOOR).start();
	}

	/**
	 * Conditional check for the following state transition.
	 * 
	 * @return State Returns next state for the elevator.
	 */
	public State doorTimerEvent() {
		String s = new String(getStateName());
		this.stateMachine.schedulerSubsystem.debug("This is transition state: " + s);
		
		
		if (!this.stateMachine.schedulerSubsystem.areElevatorDoorsClosed(this.stateMachine.elevatorID)) {
			this.stateMachine.setFault(MessageAPI.FaultType.ElevatorFailedToCloseDoors);
			this.stateMachine.schedulerSubsystem.sendCloseDoorMessage(
					this.stateMachine.elevatorID);
			return new DoorClosedState(this.stateMachine);
		}
		
		this.stateMachine.clearFault();
		if (this.stateMachine.floorQueue.isEmpty()) {
			return new WaitingState(this.stateMachine);
		} else {
			return new GotNextFloorState(this.stateMachine);
		}
	}
}
