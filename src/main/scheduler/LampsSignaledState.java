package src.main.scheduler;

/**
 * State indicating lamps in elevator have been signaled.
 * 
 * @author austinjturner
 */
class LampsSignaledState extends State {

	/**
	 * Constructor for the LampsSignaledState.
	 * 
	 * @param stateMachine StateMachine object used to manipulate current state of elevator.
	 */
	public LampsSignaledState(StateMachine stateMachine) {
		super(stateMachine);
	}
	
	/**
	 * Moves elevator state into DoorOpenedState after signaling for doors to open.
	 * 
	 * @return State Returns next state for the elevator.
	 */
	public State defaultEvent() {
		this.stateMachine.schedulerSubsystem.sendOpenDoorMessage(
				this.stateMachine.elevatorID);
		String s = new String(getStateName());
		System.out.println("This is transition state: " + s);
		
		return new DoorOpenedState(this.stateMachine);
	}

}
