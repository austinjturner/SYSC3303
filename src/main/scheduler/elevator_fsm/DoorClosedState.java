package src.main.scheduler.elevator_fsm;

class DoorClosedState extends State {

	public DoorClosedState(StateMachine stateMachine) {
		super(stateMachine);
	}
	
	@Override
	public State next() {
		if (this.stateMachine.floorQueue.size() == 0) {
			return new WaitingState(this.stateMachine);
		} else {
			return new GotNextFloorState(this.stateMachine);
		}
	}
}
