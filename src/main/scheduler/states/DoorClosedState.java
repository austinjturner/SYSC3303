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
		new DoorWaitTimer(this.stateMachine, Settings.WAIT_TIME_OPEN_DOOR_AND_EXIT_ELEVATOR).start();
	}

	/**
	 * Conditional check for the following state transition.
	 * 
	 * @return State Returns next state for the elevator.
	 */
	public State doorTimerEvent() {
		String s = new String(getStateName());
		this.stateMachine.schedulerSubsystem.debug("This is transition state: " + s);
		
		while (!(this.stateMachine.schedulerSubsystem.areElevatorDoorsClosed(this.stateMachine.elevatorID))) {
			if (this.stateMachine.faultMessage == null) {
				this.stateMachine.setFault(MessageAPI.FaultType.ElevatorFailedToCloseDoors);
				this.stateMachine.schedulerSubsystem.sendOpenDoorMessage(
						this.stateMachine.elevatorID);
			} else {
				// keep looping until our doors are closed
			}
		} 
		
		if (this.stateMachine.floorQueue.isEmpty()) {
			return new WaitingState(this.stateMachine);
		} else {
			return new GotNextFloorState(this.stateMachine);
		}
	}
}
