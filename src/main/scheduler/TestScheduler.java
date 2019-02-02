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
	
	public static int elevatorID = 666;
	public static int targetFloor1 = 10;
	public static int targetFloor2 = 5;
	public static int targetFloor3 = 2;
	
	
	
	
	/* 
	 * Test to ensure that the floorQueue is initially empty
	 */
	@Test
	public static void emptyQueue_testWaitingState(StateMachine machine) {
		assertTrue(machine.floorQueue.isEmpty());
	}
	
	/*
	 * Test to show overall state sequence, note transition states shown via embedded print statements not assertEquals().
	 * Simulates someone getting picked up at a floor and pressing a button in the elevator then going to that floor.
	 */
	@Test
	public static void transition_testOverallStateTransition(StateMachine machine) {
		
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
	
	/*
	 * Test to show the MotorStoppedState goes to the WaitingState when destinationType is wait.
	 */
	@Test 
	public static void motorStoppedState_testNextState(StateMachine machine) {
		
		machine.floorQueue.add(new Destination(targetFloor3, Destination.DestinationType.WAIT));
		machine.enqueueFloorEvent();
		
		for (int i = 2; i <= targetFloor3; i++) {
			machine.elevatorReachedFloorEvent(i);
		}
		
		assertEquals(machine.getState().getClass(), WaitingState.class);
		
	}
	
	
	
	
	// main program to set up the SchedulerSubsystem and statemachine objects for testing.
	public static void main(String[] args) {
		SchedulerSubsystem ss = new SchedulerSubsystem(new MockRequester(), new Responder());
		ss.start();
		StateMachine fsm  = ss.getStateMachine();
		
		emptyQueue_testWaitingState(fsm);
		
		transition_testOverallStateTransition(fsm);
		
		motorStoppedState_testNextState(fsm);
		
	}
	
}
