package src.main.gui;

import src.main.settings.Settings;

public class ElevatorModel {

	private int[] currentFloor;
	private int[] direction;
	private String[] error;

	public ElevatorModel() {
		currentFloor = new int[Settings.NUMBER_OF_ELEVATORS];
		direction = new int[Settings.NUMBER_OF_ELEVATORS];
		error = new String[Settings.NUMBER_OF_ELEVATORS];
	}

	public void setCurrentFloor(int index, int value){
		currentFloor[index] = value;
	}
	
	public void setDirection(int index, int value) {
		direction[index] = value;
	}
	
	public void setError(int index, String value) {
		error[index] = value;
	}
}
