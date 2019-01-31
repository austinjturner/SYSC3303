package src.main.scheduler.elevator_fsm;

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
	
	private void printStateChange(State nextState) {
		//System.out.println("Leaving state: " + this.state.getClass().getName());
		//System.out.println("Entering state: " + nextState.getClass().getName());
	}
	
	public void elevatorReachedFloor(int currentFloor) {
		this.currentFloor = currentFloor;
		State nextState = this.state.elevatorReacherFloor();
		this.state = nextState;
		go();
	}
	
	public void floorButtonPressed() {
		State nextState = this.state.elevatorButtonPressed();
		printStateChange(nextState);
		this.state = nextState;
		go();
	}
	
	public void newItemInQueue() {
		State nextState = this.state.newItemInQueue();
		printStateChange(nextState);
		this.state = nextState;
		go();
	}
	
	public void doorTimer() {
		State nextState = this.state.doorTimer();
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
			State nextState = this.state.next();
			
			if (currentState.getClass() == nextState.getClass()) {
				//System.out.println("Remaining in state: " + currentState.getClass().getName());
				break;
			} else {
				//System.out.println("Leaving state: " + currentState.getClass().getName());
				//System.out.println("Entering state: " + nextState.getClass().getName());
				this.state = nextState;
			}
		}
	}
	
	/*
	public static void main(String[] args) {
		SchedulerSubsystem ss = new SchedulerSubsystem(new MockRequester(), new Responder());
		ss.start();
		
		int elevatorID = 666;
		int targetFloor1 = 10;
		int targetFloor2 = 5;

		StateMachine fsm = new StateMachine(elevatorID, ss);
		fsm.go();
		
		fsm.floorQueue.add(0, new Destination(targetFloor1, Destination.DestinationType.PICKUP));
		fsm.newItemInQueue();
		for (int i = 2; i <= targetFloor1; i++) {
			fsm.elevatorReachedFloor(i);
		}
		
		fsm.floorQueue.add(0, new Destination(targetFloor2, Destination.DestinationType.DROPOFF));
		fsm.floorButtonPressed();
		
		for (int i = 9; i >= targetFloor2; i--) {
			fsm.currentFloor = 8;
			fsm.elevatorReachedFloor(i);
		}
	}*/
}
