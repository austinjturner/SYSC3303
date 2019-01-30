package src.main.scheduler.elevator_fsm;

public class DoorOpenedState extends State {

	public DoorOpenedState(StateMachine stateMachine) {
		super(stateMachine);
	}
	
	public State next() {
		if (this.stateMachine.floorQueue.get(0).destinationType == Destination.DestinationType.PICKUP ||
				this.stateMachine.floorQueue.get(0).destinationType == Destination.DestinationType.PICKUP_AND_DROPOFF) {
			return new WaitForElevatorButtonState(this.stateMachine);
		} else {
			return this;
		}
	}

	
	public State doorTimer() {
		this.stateMachine.floorQueue.remove(0);
		return new FloorRemovedFromQueueState(this.stateMachine);
	}
}
