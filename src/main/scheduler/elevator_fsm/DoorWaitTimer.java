package src.main.scheduler.elevator_fsm;

public class DoorWaitTimer extends Thread {
	public final int WAIT_TIME_TO_EXIT_ELEVATOR = 10000;
	
	private StateMachine stateMachine;
	
	public DoorWaitTimer(StateMachine stateMachine) {
		this.stateMachine = stateMachine;
	}
	
	public void run() {
		try {
			Thread.sleep(this.WAIT_TIME_TO_EXIT_ELEVATOR);
			stateMachine.doorTimer();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
