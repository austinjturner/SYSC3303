package src.main.elevator;

public class ElevatorSubsystem {

	public ElevatorSubsystem() {
		
	}
	
	private void createElevators(int numberOfElevators, int numberOfFloors) {
		Thread[] elevatorThreads = new Thread[numberOfElevators];
		 for (int i = 1; i <= elevatorThreads.length; i++) {
			 elevatorThreads[i] = new Thread(new Elevator(i, 0, numberOfFloors));
			
			 elevatorThreads[i].start();
		 }
	}

}
