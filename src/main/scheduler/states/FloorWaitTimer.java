package src.main.scheduler.states;

import src.main.scheduler.StateMachine;

/**
 * Used to simulate time for elevator to reach the next floor.
 * 
 * @author austinjturner
 */
public class FloorWaitTimer extends Thread {
	
	private int waitTime, previousFloor;
	private StateMachine stateMachine;
	
	/**
	 * Constructor for the FloorWaitTimer.
	 * 
	 * @param stateMachine StateMachine object used to manipulate current state of elevator.
	 */
	public FloorWaitTimer(StateMachine stateMachine, int waitTime, int previousFloor) {
		this.waitTime = waitTime;
		this.stateMachine = stateMachine;
		this.previousFloor = previousFloor;
	}
	
	
	/**
	 * Run method for threads of FloorWaitTimer class.
	 * Waits a specified amount of time  to simulate door close before transition to next state
	 */
	public void run() {
		try {
			Thread.sleep(this.waitTime);
			stateMachine.floorTimerEvent(previousFloor);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
