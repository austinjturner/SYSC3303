package src.main.scheduler;

import java.util.*;

import src.main.net.*;
import src.main.scheduler.states.State;

/**
 * This class represents the schedulers view of a Elevator state machine.
 * The state machine processes a list of floors from a queue.
 * 
 * The state machine will remove floors once visited, but is NOT
 * Responsible for managing the queue.
 * 
 * @author austinjturner
 *
 */
public class StateMachine {
	public List<Destination> floorQueue;
	public int currentFloor;

	public SchedulerSubsystem schedulerSubsystem;
	public State state;
	public int elevatorID;
	public boolean goingUp;
	
	public StateMachine(int elevatorID, SchedulerSubsystem schedulerSubsystem) {
		this.elevatorID = elevatorID;
		this.schedulerSubsystem = schedulerSubsystem;
		
		this.floorQueue = new LinkedList<Destination>();
		this.currentFloor = 1;
		this.state = State.start(this);
		this.goingUp = true;
	}
	
	public State getState() {
		return this.state;
	}
	
	// dequeue operations to be used by states
	public void dequeue() {
		this.floorQueue.remove(0);
	}
	
	// getFront operation to be used by states
	public Destination getQueueFront() {
		return this.floorQueue.get(0);
	}
	
	private void printStateChange(State nextState) {
		//schedulerSubsystem.debug("Leaving state:      " + this.state.getStateName());
		//schedulerSubsystem.debug("Entering state:     " + nextState.getStateName());
	}
	
	public synchronized void elevatorReachedFloorEvent(int currentFloor) {
		this.currentFloor = currentFloor;
		State nextState = this.state.elevatorReachedFloorEvent();
		this.state = nextState;
		go();
	}
	
	public synchronized void elevatorButtonPressedEvent() {
		State nextState = this.state.elevatorButtonPressedEvent();
		printStateChange(nextState);
		this.state = nextState;
		go();
	}
	
	public synchronized void enqueueFloorEvent() {
		State nextState = this.state.enqueueFloorEvent();
		printStateChange(nextState);
		this.state = nextState;
		go();
	}
	
	public synchronized void doorTimerEvent() {
		State nextState = this.state.doorTimerEvent();
		printStateChange(nextState);
		this.state = nextState;
		go();
	}
	
	/**
	 * Go until we reach a stable state
	 */
	private void go() {
		for (;;) {
			State currentState = this.state;
			State nextState = this.state.defaultEvent();
			
			if (currentState.getClass() == nextState.getClass()) {
				//schedulerSubsystem.debug("Remaining in state: " + currentState.getStateName());
				break;
			} else {
				//schedulerSubsystem.debug("Leaving state:      " + currentState.getStateName());
				//schedulerSubsystem.debug("Entering state:     " + nextState.getStateName());
				this.state = nextState;
			}
		}
	}
	
	
	public static void main(String[] args) {
		SchedulerSubsystem ss = new SchedulerSubsystem(new Requester(), new Responder());
		ss.start();
	}
}
