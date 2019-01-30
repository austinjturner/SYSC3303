package src.main.scheduler;

public class FSM {
	
	// States, note we can add more, each is implemented in a class of it's own
	private State[] states = {new A(), new B(), new C()};
	
	// Transitions
	private int[][] transition = {{0,1,2}, {0,1,2}, {0,1,2}};
	
	// Current state
	private int current = 0;
	
	private void next(int msg) {
		current = transition[current][msg];
	}
	
	// All client requests are simply delegated to the current state object, 
	// note on, off, and ack need to be changed to proper behaviour.
	public void on() {
		states[current].on();
		next(0);
	}

	public void off() {
		states[current].off();
		next(1);
	}

	public void ack() {
		states[current].ack();
		next(2);
	}
}
	

