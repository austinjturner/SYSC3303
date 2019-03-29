package src.main.gui;

public class ElevatorModel {

	private int[] currentFloor;
	private int[] direction;
	private String[] error;

	public ElevatorModel() {
		currentFloor = new int[4];
		direction = new int[4];
		error = new String[4];
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
