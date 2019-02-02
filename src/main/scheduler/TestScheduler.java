package src.main.scheduler;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import java.net.*;
import src.main.net.*;

/*
 * @author Nic Howes
 */
public class TestScheduler {
	
	
	@Test
	public void emptyQueue_testWaitingState() {
		
	}
	
	@Test
	public void transition_testOverallStateTransition() {
		
	}
	
	@Test
	public void doorClosedToWait_testTransition() {
		
	}
	
	
	
	
	
	
	
	// main program to set up the SchedulerSubsystem and statemachine objects for testing.
	public static void main(String[] args) {
		SchedulerSubsystem ss = new SchedulerSubsystem(new MockRequester(), new Responder());
		ss.start();
		
		int elevatorID = 666;
		int targetFloor1 = 10;
		int targetFloor2 = 5;
		int targetFloor3 = 2;
		int targetFloor4 = 3;
		int targetFloor5 = 7;
		int targetFloor6 = 8;

		StateMachine fsm  = ss.getStateMachine();
		
	}
	
}
