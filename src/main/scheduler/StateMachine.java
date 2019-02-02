package src.main.scheduler;

import java.util.*;

import src.main.net.*;
import src.main.scheduler.*;

public class StateMachine {
	public List<Destination> floorQueue;
	public int currentFloor;

	SchedulerSubsystem schedulerSubsystem;
	State state;
	int elevatorID;
	boolean goingUp;
	
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
		schedulerSubsystem.debug("Leaving state:      " + this.state.getStateName());
		schedulerSubsystem.debug("Entering state:     " + nextState.getStateName());
	}
	
	public void elevatorReachedFloorEvent(int currentFloor) {
		this.currentFloor = currentFloor;
		State nextState = this.state.elevatorReachedFloorEvent();
		this.state = nextState;
		go();
	}
	
	public void floorButtonPressedEvent() {
		State nextState = this.state.elevatorButtonPressedEvent();
		printStateChange(nextState);
		this.state = nextState;
		go();
	}
	
	public void enqueueFloorEvent() {
		State nextState = this.state.enqueueFloorEvent();
		printStateChange(nextState);
		this.state = nextState;
		go();
	}
	
	public void doorTimerEvent() {
		State nextState = this.state.doorTimerEvent();
		printStateChange(nextState);
		this.state = nextState;
		go();
	}
	
	/**
	 * Go until we reach a stable state
	 */
	public void go() {
		for (;;) {
			State currentState = this.state;
			State nextState = this.state.defaultEvent();
			
			if (currentState.getClass() == nextState.getClass()) {
				schedulerSubsystem.debug("Remaining in state: " + currentState.getStateName());
				break;
			} else {
				schedulerSubsystem.debug("Leaving state:      " + currentState.getStateName());
				schedulerSubsystem.debug("Entering state:     " + nextState.getStateName());
				this.state = nextState;
			}
		}
	}
	
	
	public static void main(String[] args) {
		SchedulerSubsystem ss = new SchedulerSubsystem(new MockRequester(), new Responder());
		ss.start();
		
		int elevatorID = 666;
		int targetFloor1 = 10;
		int targetFloor2 = 5;

		StateMachine fsm  = ss.getStateMachine();
		//StateMachine fsm = new StateMachine(elevatorID, ss);
		//fsm.go();
		
		fsm.floorQueue.add(0, new Destination(targetFloor1, Destination.DestinationType.PICKUP));
		fsm.enqueueFloorEvent();
		for (int i = 2; i <= targetFloor1; i++) {
			//assertEqual(fsm.getState().getClass(), MotorStartedSTate.class);
			fsm.elevatorReachedFloorEvent(i);
		}
		
		//assertEqual(fsm.getState().getClass(), WaitForElevatorButtonState.class);
		
		fsm.floorQueue.add(0, new Destination(targetFloor2, Destination.DestinationType.DROPOFF));
		fsm.floorButtonPressedEvent();
		
		for (int i = 9; i >= targetFloor2; i--) {
			fsm.currentFloor = 8;
			fsm.elevatorReachedFloorEvent(i);
		}
	}
}
