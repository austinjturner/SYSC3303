package src.main.scheduler;

public abstract class State {
	// gives default behaviour (if transition not viable for the current state)
	
	public void on() {
        System.out.println("error");
    }

    public void off() {
        System.out.println("error");
    }

    public void ack() {
        System.out.println("error");
    }
}

