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
	
	int elevatorID = 666;
	int targetFloor1 = 10;
	int targetFloor2 = 5;
	int targetFloor3 = 2;
	int targetFloor4 = 3;
	int targetFloor5 = 7;
	int targetFloor6 = 8;
	
	
	/* 
	 * Test to ensure that the floorQueue is initially empty
	 */
	@Test
	public void emptyQueue_testWaitingState(StateMachine machine) {
		assertTrue(machine.floorQueue.isEmpty());
	}
	
	/*
	 * Test to show overall state sequence, note transition states shown via embedded print statements not assertEquals().
	 */
	@Test
	public void transition_testOverallStateTransition(StateMachine machine) {
		
		machine.floorQueue.add(new Destination(targetFloor1, Destination.DestinationType.PICKUP));
		
		machine.enqueueFloorEvent();
		
		assertEquals(machine.getState().getClass(), MotorStartedState.class);
		for (int i = 2; i <= targetFloor1; i++) {
			machine.elevatorReachedFloorEvent(i);
		}
		
		assertEquals(machine.getState().getClass(), WaitForElevatorButtonState.class);
		
		machine.floorQueue.add(new Destination(targetFloor2, Destination.DestinationType.DROPOFF));
		machine.elevatorButtonPressedEvent();
		
		assertEquals(machine.getState().getClass(), WaitingState.class);
		
	}
	
	
	

	
	
	
	
	// main program to set up the SchedulerSubsystem and statemachine objects for testing.
	public static void main(String[] args) {
		SchedulerSubsystem ss = new SchedulerSubsystem(new MockRequester(), new Responder());
		ss.start();
		

		StateMachine fsm  = ss.getStateMachine();
		
	}
	
}
