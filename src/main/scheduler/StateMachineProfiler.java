package src.main.scheduler;

/**
 * 
 * This class extends the StateMachine with the addition abilities to 
 * record how long method calls into the state transitions take
 * 
 * @author austinjturner
 *
 */
public class StateMachineProfiler extends StateMachine {

	private SchedulerSubsystemProfiler ssp;
	
	public StateMachineProfiler(int elevatorID, SchedulerSubsystemProfiler schedulerSubsystem) {
		super(elevatorID, schedulerSubsystem);
		ssp = schedulerSubsystem;
	}
	
	@Override	
	public synchronized void doorTimerEvent() {
		long startTime = System.nanoTime();
		super.doorTimerEvent();
		this.ssp.addDoorTimerRequestTime(System.nanoTime() - startTime);
	}
	
	@Override	
	public synchronized void floorTimerEvent(int previousFloor) {
		long startTime = System.nanoTime();
		super.floorTimerEvent(previousFloor);
		this.ssp.addFloorTimerRequestTime(System.nanoTime() - startTime);
	}
}
