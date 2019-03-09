package src.main.scheduler.states;

import src.main.scheduler.StateMachine;
import src.main.settings.Settings;
import src.main.net.MessageAPI;

/**
 * State representing the elevator with door opened.
 * 
 * @author austinjturner
 */
public class DoorOpenedState extends State {

	/**
	 * Constructor for the DoorOpenedState.
	 * 
	 * @param stateMachine StateMachine object used to manipulate current state of elevator.
	 */
	public DoorOpenedState(StateMachine stateMachine) {
		super(stateMachine);
		
		/*
		 * We start this timer to ensure the door represent the door opening
		 * and allow people to leave.
		 */
		startDoorWaitTimer();
	}
	
	private void startDoorWaitTimer() {
		new DoorWaitTimer(this.stateMachine, Settings.WAIT_TIME_OPEN_DOOR_AND_EXIT_ELEVATOR).start();
	}


	/**
	 * Moves elevator state into FloorDequeuedState.
	 * 
	 * @return State Returns next state for the elevator.
	 */
	public State doorTimerEvent() {
		while (this.stateMachine.schedulerSubsystem.areElevatorDoorsClosed(this.stateMachine.elevatorID)) {
			if (this.stateMachine.faultMessage == null) {
				this.stateMachine.setFault(MessageAPI.FaultType.ElevatorFailedToOpenDoors);
				this.stateMachine.schedulerSubsystem.sendOpenDoorMessage(
						this.stateMachine.elevatorID);
			} else {
				this.stateMachine.schedulerSubsystem.sendCloseDoorMessage(
						this.stateMachine.elevatorID);
			}
		} 
		
		if (this.stateMachine.faultMessage != null) {
			this.stateMachine.clearFault();
		}
		
		this.stateMachine.dequeue();
		this.stateMachine.schedulerSubsystem.debug("This is transition state: " + getStateName());
		return new FloorDequeuedState(this.stateMachine);
		}
}
