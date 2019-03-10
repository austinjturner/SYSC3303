package src.main.scheduler.states;

import src.main.scheduler.Destination;
import src.main.scheduler.Destination.DestinationType;
import src.main.scheduler.StateMachine;

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
		Destination dest = this.stateMachine.getQueueFront();
		int targetFloor = dest.floorNum;
		
		/*
		 * Detect if a fault should be simulated.
		 * If detected, send message to elevator
		 */
		if (dest.hasFault()) {
			this.stateMachine.schedulerSubsystem.sendSimulateFaultMessage(
					this.stateMachine.elevatorID, dest.faultFloorNumber, dest.faultType);
		}
		
		if (currentFloor < targetFloor) {
			this.stateMachine.goingUp = true;
			this.stateMachine.schedulerSubsystem.sendMotorUpMessage(
					this.stateMachine.elevatorID);
		} else {
			this.stateMachine.goingUp = false;
			this.stateMachine.schedulerSubsystem.sendMotorDownMessage(
					this.stateMachine.elevatorID);			
		}
		
		// Set elevator lamp ONLY if someone is being dropped off.
		// This represents them pressing the button inside the elevator.
		if (dest.destinationType == DestinationType.DROPOFF || dest.destinationType == DestinationType.PICKUP_AND_DROPOFF) {
			this.stateMachine.schedulerSubsystem.sendSetElevatorButtonMessage(this.stateMachine.elevatorID, targetFloor);
		}
		
		

		
		this.stateMachine.schedulerSubsystem.debug("This is transition state: " + getStateName());
		return new MotorStartedState(this.stateMachine);
	}
	
}
