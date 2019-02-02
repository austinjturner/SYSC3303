package src.main.scheduler;

/**
 * Used to simulate time for elevator door to close for transition between states as a thread.
 * 
 * @author austinjturner
 */
public class DoorWaitTimer extends Thread {
	public final int WAIT_TIME_TO_EXIT_ELEVATOR = 10000;
	
	private StateMachine stateMachine;
	/**
	 * Constructor for the DoorWaitTimer.
	 * 
	 * @param stateMachine StateMachine object used to manipulate current state of elevator.
	 */
	public DoorWaitTimer(StateMachine stateMachine) {
		this.stateMachine = stateMachine;
	}
	/**
	 * Run method for threads of DoorWaitTimer class.
	 * Waits a specified amount of time  to simulate door close before transition to next state
	 */
	public void run() {
		try {
			Thread.sleep(this.WAIT_TIME_TO_EXIT_ELEVATOR);
			stateMachine.doorTimerEvent();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
