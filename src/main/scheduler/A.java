package src.main.scheduler;

public class A extends State {
	
	public void on() {
		System.out.println("A + on  = C"); 
		// TO-DO Needs proper functionality when overriding State
	}

	public void off() {
		System.out.println("A + off = B");
		// TO-DO Needs proper functionality when overriding State
	}

	public void ack() {
		System.out.println("A + ack = A");
		// TO-DO Needs proper functionality when overriding State
	}

}
