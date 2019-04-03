package src.test.integration;

import org.junit.Test;

import src.main.elevator.ElevatorSubsystem;
import src.main.floor.FloorSubsystem;
import src.main.scheduler.*;

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
		//String testFilePath = "src//main//text//random_100_file_1_input.txt";
		String testFilePath = "src//main//text//fault_simulation_for_gui_input.txt";
		//String testFilePath = "src//main//text//parallel_elevators_input.txt";
		
		//SchedulerSubsystem schedulerSubsystem = new SchedulerSubsystem();
		SchedulerSubsystem schedulerSubsystem = new SchedulerSubsystem();
		
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
			sleep(100000);
		}
	}
	
	
	@Test
	public void testFullSystemWithProfiling() {
		
		//String testFilePath = "src//main//text//fault_simulation_input.txt";
		//String testFilePath = "src//main//text//parallel_elevators_input.txt";
		
		String rootName = "random_30_file_3";
		String trialNumber = "trial_2";
		
		String testFilePath = "src//main//text//"+rootName+"_input.txt";
		
		String csvFilePath = "src//main//text//"+rootName+"_"+trialNumber+"_"+System.currentTimeMillis()+"_profile_output.csv";
		
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
			int seconds = 1000;
			int minutes = 60 * seconds;
			sleep(1 * minutes);
			schedulerSubsystem.generateCSV(csvFilePath);
			break;
		}
	}
}
