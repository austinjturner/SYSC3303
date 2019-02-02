package src.main.scheduler;

public class DoorOpenedState extends State {

	public DoorOpenedState(StateMachine stateMachine) {
		super(stateMachine);
		/*
		 * We start this timer to ensure the door is closed again after
		 * a period of time.
		 */
		new DoorWaitTimer(this.stateMachine).start();
	}
	
	private boolean floorIsPickup() {
		return this.stateMachine.getQueueFront().destinationType == Destination.DestinationType.PICKUP ||
				this.stateMachine.getQueueFront().destinationType == Destination.DestinationType.PICKUP_AND_DROPOFF;
	}
	
	public State defaultEvent() {
		if (floorIsPickup()) {
			return new WaitForElevatorButtonState(this.stateMachine);
		} else {
			return this;
		}
	}

	
	public State doorTimerEvent() {
		this.stateMachine.dequeue();
		return new FloorDequeuedState(this.stateMachine);
	}
}
