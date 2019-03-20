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
	
	private List<Long> floorButtonPressedRequestTimes = new ArrayList<>();
	private List<Long> floorReachedRequestTimes = new ArrayList<>();
	
	@Override
	protected synchronized void messageHandle(RequestMessage rm) {
		long startTime = System.nanoTime();
		
		
		/*
		 * Call to super class
		 */
		super.messageHandle(rm);
		
		switch (rm.getRequestType()) {
		case MessageAPI.MSG_FLOOR_BUTTON_PRESSED:
			floorButtonPressedRequestTimes.add(System.nanoTime() - startTime);
			break;
		case MessageAPI.MSG_CURRENT_FLOOR:
			floorReachedRequestTimes.add(System.nanoTime() - startTime);
			break;
		}

	}
	
	public synchronized void generateCSV(String filename) {
	    BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(filename));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	    
		String line1 = "";
		for (Long l : floorButtonPressedRequestTimes) {
			line1 += l.toString() + ", ";
		}
		line1 += "\n";
		
		String line2 = "";
		for (Long l : floorReachedRequestTimes) {
			line2 += l.toString() + ", ";
		}
		line2 += "\n";
		
		
	    try {
			writer.write(line1);
			writer.write(line2);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
