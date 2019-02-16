package src.test.scheduler;
import src.main.scheduler.*;
import src.main.scheduler.states.*;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import java.util.Map;

import src.main.net.*;

/*
 * @author Nic Howes
 */
public class TestScheduler {
	
	public static int elevatorID = 666;
	public static int targetFloor1 = 10;
	public static int targetFloor2 = 5;
	public static int targetFloor3 = 2;
	
	public StateMachine fsm;
	
	
	@Before
	public void setUp() {
		SchedulerSubsystem ss = new SchedulerSubsystem(new MockRequester(), new Responder());
		ss.start();
		for (Map.Entry<Integer, StateMachine> entry : ss.getStateMachineMap().entrySet()) {
			this.fsm = entry.getValue();
		}
	}
	
	/* 
	 * Test to ensure that the floorQueue is initially empty
	 */
	@Test
	public void emptyQueue_testWaitingState() {
		assertTrue(fsm.floorQueue.isEmpty());
	}
	
	/*
	 * Test to show overall state sequence, note transition states shown via embedded print statements not assertEquals().
	 * Simulates someone getting picked up at a floor and pressing a button in the elevator then going to that floor.
	 */
	@Test
	public void transition_testOverallStateTransition() {
		
		fsm.floorQueue.add(new Destination(targetFloor1, Destination.DestinationType.PICKUP));
		fsm.floorQueue.add(1, new Destination(targetFloor2, Destination.DestinationType.DROPOFF));
		
		fsm.enqueueFloorEvent();
		
		assertEquals(fsm.getState().getClass(), MotorStartedState.class);
		for (int i = 2; i <= targetFloor1; i++) {
			fsm.elevatorReachedFloorEvent(i);
		}
		
		assertEquals(fsm.getState().getClass(), DoorOpenedState.class);
		fsm.doorTimerEvent();
		assertEquals(fsm.getState().getClass(), FloorDequeuedState.class);
		fsm.doorTimerEvent();
		
		for (int i = targetFloor1; i >= targetFloor2; i--) {
			fsm.elevatorReachedFloorEvent(i);
		}
		
		assertEquals(fsm.getState().getClass(), DoorOpenedState.class);
		fsm.doorTimerEvent();
		assertEquals(fsm.getState().getClass(), FloorDequeuedState.class);
		fsm.doorTimerEvent();
		
		assertEquals(fsm.getState().getClass(), WaitingState.class);
		
	}
	
	/*
	 * Test to show the MotorStoppedState goes to the WaitingState when destinationType is wait.
	 */
	@Test 
	public void motorStoppedState_testNextState() {
		
		fsm.floorQueue.add(new Destination(targetFloor3, Destination.DestinationType.WAIT));
		fsm.enqueueFloorEvent();
		
		for (int i = 2; i <= targetFloor3; i++) {
			fsm.elevatorReachedFloorEvent(i);
		}
		
		assertEquals(fsm.getState().getClass(), WaitingState.class);
		
	}
}
