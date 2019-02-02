package src.main.scheduler;

class LampsSignaledState extends State {

	public LampsSignaledState(StateMachine stateMachine) {
		super(stateMachine);
	}
	
	public State defaultEvent() {
		this.stateMachine.schedulerSubsystem.sendOpenDoorMessage(
				this.stateMachine.elevatorID);
		
		return new DoorOpenedState(this.stateMachine);
	}

}
