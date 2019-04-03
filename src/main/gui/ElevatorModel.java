package src.main.gui;

import src.main.settings.Settings;

public class ElevatorModel {

	private int[] currentFloor;
	private boolean[] goingUp;
	private boolean[] moving;
	private String[] error;

	public ElevatorModel() {
		currentFloor = new int[Settings.NUMBER_OF_ELEVATORS];
		goingUp = new boolean[Settings.NUMBER_OF_ELEVATORS];
		moving = new boolean[Settings.NUMBER_OF_ELEVATORS];
		error = new String[Settings.NUMBER_OF_ELEVATORS];
	}
	
	public void setCurrentFloor(int index, int value){
		currentFloor[index] = value;
	}
	
	public void setDirection(int index, boolean value) {
		goingUp[index] = value;
	}
	
	public void setMoving(int index, boolean value) {
		moving[index] = value;
	}
	
	public void setError(int index, String value) {
		error[index] = value;
	}
	
	public int getCurrentFloor(int index) {
		return currentFloor[index];
	}
	
	public boolean isGoingUp(int index) {
		return goingUp[index];
	}
	
	public boolean getMoving(int index) {
		return moving[index];
	}
	
	public String getError(int index) {
		return error[index];
	}
}
