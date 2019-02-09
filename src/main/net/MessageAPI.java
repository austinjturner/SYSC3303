package src.main.net;

/**
 * Contains the definitions for each requestType to be sent
 * 
 * @author austinjturner
 *
 */
public class MessageAPI {
	
	
	/*
	 * Special Messages
	 */
	public static final int MSG_EMPTY_RESPONSE = 0;
	
	
	/*
	 * Testing Messages
	 */
	public static final int MSG_TEST_SEND_REQUEST = 1000;
	public static final int MSG_TEST_SEND_RESPONSE = 1001;
	
	
	/*
	 * Elevator Messages
	 */
	public static final int MSG_CLOSE_DOORS = 2001; 
	public static final int MSG_OPEN_DOORS = 2002; 
	public static final int MSG_MOTOR_STOP = 2003; 
	public static final int MSG_MOTOR_UP = 2004; 
	public static final int MSG_MOTOR_DOWN = 2005; 
	public static final int MSG_PRESS_ELEVATOR_BUTTON = 2006;
	public static final int MSG_CLEAR_ELEVATOR_BUTTON = 2007;
	public static final int MSG_CURRENT_FLOOR = 2008;
	public static final int MSG_FLOOR_SENSOR = 2009;
	public static final int MSG_TURN_ON_ELEVATOR_LAMP = 2010;
	public static final int MSG_TURN_OFF_ELEVATOR_LAMP = 2011;

	
	/*
	 * Floor Messages
	 */
	public static final int MSG_ELEVATOR_BUTTON_PRESSED = 3001;
	public static final int MSG_FLOOR_BUTTON_PRESSED = 3002;
	public static final int MSG_CLEAR_FLOOR_BUTTON = 3003;
}
