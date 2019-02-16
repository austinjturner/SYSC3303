package src.main.settings;

public class Settings {
	
	/*
	 * System settings
	 */
	public static final int NUMBER_OF_ELEVATORS = 4;
	public static final int NUMBER_OF_FLOORS = 10;
	
	public static final float TIME_FACTOR = 1;		// 1/X times faster
	
	public static final String INPUT_FILE_PATH = "src//main//text//austin_input.txt";
	
	
	/*
	 * Elevator settings
	 */
	public static final int TIME_BETWEEN_FLOORS = (int) (1500 * TIME_FACTOR);
	public static final int WAIT_TIME_OPEN_DOOR_AND_EXIT_ELEVATOR = (int) (10000 * TIME_FACTOR);
	public static final int WAIT_TIME_CLOSE_DOOR = (int) (5000 * TIME_FACTOR);
	
	/*
	 * Debug settings
	 */
	public static final boolean DEBUG_SCHEDULER = false;
	
	
	/*
	 * Testing settings
	 */
	public static final int THROUGHPUT_TEST_NUMBER_OF_PASSENGERS = 100;
	public static final int THROUGHPUT_TEST_PASSENGER_FREQUENCY_PER_MINUTE = 3;

}
