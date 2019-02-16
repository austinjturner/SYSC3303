package src.test.integration;

import org.junit.Test;

import src.main.elevator.ElevatorSubsystem;
import src.main.floor.FloorSubsystem;
import src.main.scheduler.SchedulerSubsystem;

public class TestSystemFromFile {
	
	private void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	
	@Test
	public void testFullSystem() {
		SchedulerSubsystem schedulerSubsystem = new SchedulerSubsystem();
		
		schedulerSubsystem.start();
		sleep(100);	
		new ElevatorSubsystem();	// This will create many threads
		
		// Wait for Elevator and Scheduler threads to stabilize
		sleep(100);
		
		FloorSubsystem floor = null;
		try {
			floor = new FloorSubsystem();
			floor.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		for (;;) {
			sleep(10000);
		}
	}
}
