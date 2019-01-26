package src.main.net;

/**
 * Contains the definitions for each requestType to be sent
 * 
 * @author austinjturner
 *
 */
public class MessageAPI {
	public static final int MSG_EMPTY_RESPONSE					= 0;
	
	
	public static final int MSG_TEST_SEND_REQUEST = 1000;
	public static final int MSG_TEST_SEND_RESPONSE = 1001;
	
	
	public static final int MSG_CLOSE_DOORS = 2001; 
	public static final int MSG_OPEN_DOORS = 2002; 
	public static final int MSG_MOTOR_STOP = 2003; 
	public static final int MSG_MOTOR_UP = 2004; 
	public static final int MSG_MOTOR_DOWN = 2005; 
	public static final int MSG_TOGGLE_ELEVATOR_LAMP = 2006; 
	public static final int MSG_PRESS_ELEVATOR_BUTTON = 2007;
	public static final int MSG_CLEAR_ELEVATOR_BUTTON = 2008;
	public static final int MSG_CURRENT_FLOOR = 2009;
	
}
