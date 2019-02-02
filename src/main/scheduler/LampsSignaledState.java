package src.main.scheduler;

class LampsSignaledState extends State {

	public LampsSignaledState(StateMachine stateMachine) {
		super(stateMachine);
	}
	
	public State defaultEvent() {
		this.stateMachine.schedulerSubsystem.sendOpenDoorMessage(
				this.stateMachine.elevatorID);
		String s = new String(getStateName());
		System.out.println("This is transition state: " + s);
		
		return new DoorOpenedState(this.stateMachine);
	}

}
