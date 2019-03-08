/*
 *	Project Title:	Elevator System
 *	Authored by:	Devon Daley
 *	Made For: 		SYSC 3303
 *	Iteration:		1
 * 	Last modified:	March 8th by Devon Daley
*/

package src.main.floor;

import src.main.net.MessageAPI.FaultType;

public class inputVar {
	
	public int hh;		//hours
	public int mm;		//minutes
	public int ss;		//seconds
	public int mmm;	//milliseconds
	public int floor;
	public String direction;
	public int destFloor;
	public int length;
	
	// For faults
	public int faultFloor;
	public FaultType faultType;
	
	public inputVar() {
		hh = 0;
		mm = 0;
		ss = 0;
		mmm = 0;
		floor = 0;
		direction = "none";
		destFloor = 0;
		length = 0;
		faultFloor = -1;
		faultType = null;
	}
	
	public void setTime(String time) {
		int length = time.length();
		// The length of millisecond is variable, so we use the length of string as an index
		
		hh = Integer.parseInt(time.substring(0,2));
		mm = Integer.parseInt(time.substring(3,5));		//a semicolon is @ index 2, so ignore it
		ss = Integer.parseInt(time.substring(6,8));		//a semicolon is @ index 5, so ignore it
		mmm = Integer.parseInt(time.substring(9, length));		//a dot is @ index 8, so ignore it
	}
	
	public void setFloor(int f) {
		floor = f;
	}
	
	public void setDirection(String fb) {
		direction = fb;
	}
	
	public void setDestFloor(int cb) {
		destFloor = cb;
	}
	
	public void setLength(int len) {
		length = len;
	}
	
	public void setFaultType(FaultType ft) {
		faultType = ft;
	}
	
	public void setFaultFloor(int ff) {
		faultFloor = ff;
	}
}
