package src.test.integration;

import org.junit.Test;

import src.main.elevator.ElevatorSubsystem;
import src.main.floor.FloorSubsystem;
import src.main.scheduler.SchedulerSubsystem;
import src.main.scheduler.SchedulerSubsystemProfiler;

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
		
		//String testFilePath = "src//main//text//fault_simulation_input.txt";
		String testFilePath = "src//main//text//parallel_elevators_input.txt";
		
		String csvFilePath = "src//main//text//"+System.currentTimeMillis()+"_profile_output.csv";
		
		//SchedulerSubsystem schedulerSubsystem = new SchedulerSubsystem();
		SchedulerSubsystemProfiler schedulerSubsystem = new SchedulerSubsystemProfiler();
		
		schedulerSubsystem.start();
		sleep(100);	
		new ElevatorSubsystem();	// This will create many threads
		
		// Wait for Elevator and Scheduler threads to stabilize
		sleep(100);
		
		FloorSubsystem floor = null;
		try {
			floor = new FloorSubsystem(testFilePath);
			floor.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		for (;;) {
			sleep(10000);
			schedulerSubsystem.generateCSV(csvFilePath);
		}
	}
}
