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
	
	public State next() {
		return this;
	}
	
	public State elevatorReacherFloor() {
		return this;
	}
	
	public State elevatorButtonPressed() {
		return this;
	}
	
	public State newItemInQueue() {
		return this;
	}
	
	public State doorTimer() {
		return this;
	}
}
