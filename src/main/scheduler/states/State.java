package src.main.scheduler.states;

import src.main.scheduler.StateMachine;

/**
 * Abstract class for all states to inherit from. 
 * 
 * @author austinjturner
 */
public abstract class State {
	
	protected StateMachine stateMachine;
	
	/**
	 * Constructor for a State for initializing stateMachine for the state.
	 * 
	 * @param stateMachine StateMachine object used to manipulate current state of elevator.
	 */
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
	
	public State floorTimerEvent(int previousFloor) {
		return this;
	}
}
