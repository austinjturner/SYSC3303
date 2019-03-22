package src.test.integration;


import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import src.main.settings.Settings;

public class TestFileGenerator {
	
	
	/*
	 * Testing settings
	 */
	public static final int THROUGHPUT_TEST_NUMBER_OF_PASSENGERS = 30;
	public static final int THROUGHPUT_TEST_PASSENGER_FREQUENCY_PER_MINUTE = 3;
	
	
	public static void main(String[] args) {
		String testFilePath = "src//main//text//random_30_file_3_input.txt";
		generateTestInputFile(testFilePath, LocalDateTime.now().plusHours(5));
	}
	
	public static void generateTestInputFile(String filename, LocalDateTime startTime) {
		Random random = new Random();
		PrintWriter out = null;
		try {
			out = new PrintWriter(filename);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		// Distribution of simulated passengers
		double timeDeltaMean = 1000 * 60 / THROUGHPUT_TEST_PASSENGER_FREQUENCY_PER_MINUTE;
		double timeDeltaSigma = timeDeltaMean / 3;
		
		for (int i = 0; i < THROUGHPUT_TEST_NUMBER_OF_PASSENGERS; i++) {
			
			/*
			 * Step 1: Get timestamp
			 */
			int timeDelta = (int) (random.nextGaussian() * timeDeltaSigma + timeDeltaMean);
			timeDelta = timeDelta < 0 ? 0 : timeDelta;		// make sure we don't have negative time
			
			startTime = startTime.plusNanos(timeDelta * 1000000l);		// convert millis to nanos
			String testLine = DateTimeFormatter.ofPattern("HH:mm:ss.SSS").format(startTime);
			
			
			/*
			 * Step 2: Get pick up floor
			 */
			int pickUpFloor = 1;
			if (random.nextBoolean()) {
				pickUpFloor = random.nextInt(Settings.NUMBER_OF_FLOORS - 1) + 2;
			} else if (random.nextBoolean()){
				pickUpFloor = 2;	// required by document
			}
			
			
			/*
			 * Step 3: Get drop off floor
			 */
			int dropOffFloor = pickUpFloor;
			if (pickUpFloor == 1 || pickUpFloor == 2) {
				// Go up
				dropOffFloor = pickUpFloor + random.nextInt(Settings.NUMBER_OF_FLOORS - 1) + 1;
				dropOffFloor = dropOffFloor > Settings.NUMBER_OF_FLOORS ? Settings.NUMBER_OF_FLOORS : dropOffFloor;

			} else if (pickUpFloor == Settings.NUMBER_OF_FLOORS) {
				// Go down
				dropOffFloor = random.nextInt(Settings.NUMBER_OF_FLOORS - 1) + 1;
			} else if (random.nextInt(4) == 0) {
				// Go up ==> 1 in 4 chance
				while (dropOffFloor == pickUpFloor) {
					dropOffFloor = random.nextInt(Settings.NUMBER_OF_FLOORS - pickUpFloor) + 1 + pickUpFloor;
				}
			} else {		
				// Go down ==> 3 in 4 chance
				while (dropOffFloor == pickUpFloor) {
					dropOffFloor = random.nextInt(pickUpFloor - 1) + 1;
				}
			}
			
			/*
			 * Step 4: Compile string and write to file
			 */
			testLine += " " + pickUpFloor +
						" " + (pickUpFloor < dropOffFloor ? "up" : "down") + 
						" " + dropOffFloor;
			
			out.println(testLine);
			//System.out.println(testLine);
		}
		out.close();
	}
}
