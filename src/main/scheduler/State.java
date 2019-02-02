package src.main.scheduler;

public abstract class State {
	
	protected StateMachine stateMachine;
	
	public State(StateMachine stateMachine) {
		this.stateMachine = stateMachine;
	}
	
	public static State start(StateMachine stateMachine) {
		return new WaitingState(stateMachine);
	}
	
	public String getStateName() {
		String name = this.getClass().getName();
		return name.substring(name.lastIndexOf('.') + 1);
	}
	
	public State defaultEvent() {
		return this;
	}
	
	public State elevatorReachedFloorEvent() {
		return this;
	}
	
	public State elevatorButtonPressedEvent() {
		return this;
	}
	
	public State enqueueFloorEvent() {
		return this;
	}
	
	public State doorTimerEvent() {
		return this;
	}
}
