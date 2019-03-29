package src.main.gui;

public class ElevatorModel {

	private int[] currentFloor;
	private boolean[] goingUp;
	private String[] error;

	public ElevatorModel() {
		currentFloor = new int[4];
		goingUp = new boolean[4];
		error = new String[4];
	}

	public void setCurrentFloor(int index, int value){
		currentFloor[index] = value;
	}
	
	public void setDirection(int index, boolean value) {
		goingUp[index] = value;
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
	
	public String getError(int index) {
		return error[index];
	}
}
