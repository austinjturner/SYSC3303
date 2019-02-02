package src.main.scheduler;

public class FloorDequeuedState extends State {

	public FloorDequeuedState(StateMachine stateMachine) {
		super(stateMachine);
	}
	
	@Override
	public State defaultEvent() {
		this.stateMachine.schedulerSubsystem.sendCloseDoorMessage(this.stateMachine.elevatorID);
		
		String s = new String(getStateName());
		System.out.println("This is transition state: " + s);
		
		return new DoorClosedState(this.stateMachine);
	}

}
