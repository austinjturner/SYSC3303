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
	
	public void elevatorReachedFloor(int currentFloor) {
		this.currentFloor = currentFloor;
		this.state = this.state.elevatorReacherFloor();
		go();
	}
	
	public void floorButtonPressed() {
		this.state = this.state.elevatorButtonPressed();
		go();
	}
	
	public void newItemInQueue() {
		this.state = this.state.newItemInQueue();
		go();
	}
	
	public void doorTimer() {
		this.state = this.state.doorTimer();
		go();
	}
	
	/**
	 * Go until we reach a stable state
	 */
	public void go() {
		State nextState = this.state.next();
		System.out.println("Starting State: " + this.state.getClass().getName());
		while (nextState != this.state) {
			this.state = nextState;
			System.out.println("Entering new State: " + nextState.getClass().getName());
			nextState = this.state.next();
			
			//System.out.println("DEBUG: this.state = : " + this.state.getClass().getName());
			//System.out.println("DEBUG: nextState = : " + nextState.getClass().getName());
		}
		//this.state = nextState;
	}
	
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
	}
}
