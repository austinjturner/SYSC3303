package src.main.scheduler.elevator_fsm;

public class ElevatorLampSignalledState extends State {

	public ElevatorLampSignalledState(StateMachine stateMachine) {
		super(stateMachine);
	}

	public State next() {
		this.stateMachine.schedulerSubsystem.sendOpenDoorMessage(
				this.stateMachine.elevatorID);
		
		return new DoorOpenedState(this.stateMachine);
	}
	
}
