package src.main.scheduler;

import java.io.*;
import java.util.*;

import src.main.net.MessageAPI;
import src.main.net.messages.RequestMessage;

/**
 * 
 * This class extends the SchedulerSubsystem with the addition abilities to 
 * record how long method calls into the subsystem take
 * 
 * @author austinjturner
 *
 */
public class SchedulerSubsystemProfiler extends SchedulerSubsystem {
	
	private ArrayList<Long> floorButtonPressedRequestTimes = new ArrayList<>();
	private ArrayList<Long> floorReachedRequestTimes = new ArrayList<>();
	private ArrayList<Long> floorTimerRequestTimes = new ArrayList<>();
	private ArrayList<Long> doorTimerRequestTimes = new ArrayList<>();
	
	@Override
	protected void messageHandle(RequestMessage rm) {
		long startTime = System.nanoTime();
		
		
		/*
		 * Call to super class
		 */
		super.messageHandle(rm);
		
		synchronized(this) {
			switch (rm.getRequestType()) {
			case MessageAPI.MSG_FLOOR_BUTTON_PRESSED:
				floorButtonPressedRequestTimes.add(System.nanoTime() - startTime);
				break;
			case MessageAPI.MSG_CURRENT_FLOOR:
				floorReachedRequestTimes.add(System.nanoTime() - startTime);
				break;
			}
		}

	}
	
	@Override
	protected StateMachine buildNewStateMachine(int elevatorID) {
		return new StateMachineProfiler(elevatorID, this);
	}
	
	public synchronized void addFloorTimerRequestTime(long l) {
		floorTimerRequestTimes.add(l);
	}
	
	public synchronized void addDoorTimerRequestTime(long l) {
		doorTimerRequestTimes.add(l);
	}
	
	
	@SuppressWarnings("unchecked")
	public void generateCSV(String filename) {
	    BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(filename));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	    
		ArrayList<Long> floorButtonPressedRequestTimesCopy;
		ArrayList<Long> floorReachedRequestTimesCopy;
		ArrayList<Long> floorTimerRequestTimesCopy;
		ArrayList<Long> doorTimerRequestTimesCopy;
		
		synchronized(this) {
			floorButtonPressedRequestTimesCopy = (ArrayList<Long>) floorButtonPressedRequestTimes.clone();
			floorReachedRequestTimesCopy = (ArrayList<Long>) floorReachedRequestTimes.clone();
			floorTimerRequestTimesCopy = (ArrayList<Long>) floorTimerRequestTimes.clone();
			doorTimerRequestTimesCopy = (ArrayList<Long>) doorTimerRequestTimes.clone();
		}
		
		String line1 = ", ,floorButtonPressed, , ,";
		for (Long l : floorButtonPressedRequestTimesCopy) {
			line1 += l.toString() + ", ";
		}
		line1 += "\n";
		
		String line2 = ", ,floorReached, , ,";
		for (Long l : floorReachedRequestTimesCopy) {
			line2 += l.toString() + ", ";
		}
		line2 += "\n";
		
		String line3 = ", ,floorTimer, , ,";
		for (Long l : floorTimerRequestTimesCopy) {
			line3 += l.toString() + ", ";
		}
		line3 += "\n";
		
		String line4 = ", ,doorTimer, , ,";
		for (Long l : doorTimerRequestTimesCopy) {
			line4 += l.toString() + ", ";
		}
		line4 += "\n";
		
		
		
	    try {
			writer.write(line1);
			writer.write(line2);
			writer.write(line3);
			writer.write(line4);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
