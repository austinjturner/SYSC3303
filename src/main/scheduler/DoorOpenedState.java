package src.main.scheduler;

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
		 * We start this timer to ensure the door is closed again after
		 * a period of time.
		 */
		new DoorWaitTimer(this.stateMachine).start();
	}
	/**
	 * Checks if state object at front of queue has destinationType matching up with certain types.
	 * 
	 * @return boolean Returns boolean of destinationType comparison
	 */
	private boolean floorIsPickup() {
		return this.stateMachine.getQueueFront().destinationType == Destination.DestinationType.PICKUP ||
				this.stateMachine.getQueueFront().destinationType == Destination.DestinationType.PICKUP_AND_DROPOFF;
	}
	
	/**
	 * Conditional check for the following state transition.
	 * 
	 * @return State Returns next state for the elevator.
	 */
	public State defaultEvent() {
		if (floorIsPickup()) {
			return new WaitForElevatorButtonState(this.stateMachine);
		} else {
			return this;
		}
	}

	/**
	 * Moves elevator state into FloorDequeuedState.
	 * 
	 * @return State Returns next state for the elevator.
	 */
	public State doorTimerEvent() {
		this.stateMachine.dequeue();
		return new FloorDequeuedState(this.stateMachine);
	}
}
