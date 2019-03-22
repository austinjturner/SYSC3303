package src.main.settings;

public class Settings {
	
	/*
	 * System settings
	 */
	public static final int NUMBER_OF_ELEVATORS = 4;
	public static final int NUMBER_OF_FLOORS = 22;
	
	public static final float TIME_FACTOR = 1;		// 1/X times faster
	
	public static final String INPUT_FILE_PATH = "src//main//text//parallel_elevators_input.txt";
	
	
	/*
	 * Elevator settings
	 */
	public static final int TIME_BETWEEN_FLOORS = (int) (1500 * TIME_FACTOR);
	public static final int WAIT_TIME_OPEN_DOOR_AND_EXIT_ELEVATOR = (int) (10000 * TIME_FACTOR);
	public static final int WAIT_TIME_CLOSE_DOOR = (int) (5000 * TIME_FACTOR);
	
	
	/*
	 * Scheduler settings
	 */
	public static final int MAX_TIME_BEFORE_DOOR_FAULT = (int) (1.25 * TIME_BETWEEN_FLOORS); // 25% after expected time
	
	
	/*
	 * Debug settings
	 */
	public static final boolean DEBUG_SCHEDULER = false;

}
