package src.main.scheduler;

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
	}
	// Note changed to floorQueue.isEmpty(), doesn't change functionality.

	/**
	 * Conditional check for the following state transition.
	 * 
	 * @return State Returns next state for the elevator.
	 */
	@Override
	public State defaultEvent() {
		String s = new String(getStateName());
		System.out.println("This is transition state: " + s);
		
		if (this.stateMachine.floorQueue.isEmpty()) {
			return new WaitingState(this.stateMachine);
		} else {
			return new GotNextFloorState(this.stateMachine);
		}
	}
}
