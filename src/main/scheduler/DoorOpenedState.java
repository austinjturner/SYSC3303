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
		 * We start this timer to ensure the door represent the door opening
		 * and allow people to leave.
		 */
		startDoorWaitTimer();
	}
	
	private void startDoorWaitTimer() {
		new DoorWaitTimer(this.stateMachine).start();
	}


	/**
	 * Moves elevator state into FloorDequeuedState.
	 * 
	 * @return State Returns next state for the elevator.
	 */
	public State doorTimerEvent() {
		this.stateMachine.dequeue();
		this.stateMachine.schedulerSubsystem.debug("This is transition state: " + getStateName());
		return new FloorDequeuedState(this.stateMachine);
	}
}
