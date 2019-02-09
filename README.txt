-----------------------------------Iteration 1-----------------------------------

Instructions to run program:

	- File -> Open projects from file system
	- Open the project file that you extracted
	- Expand the project files in eclipse's package explorer view
	- Go to src -> test -> integration
	- Right click on TestSystem.java and run as JUnit test
	- View the console to see the test working
	
File Structure:
	
	In the package "main" is where you will find all of the classes used to make the program run. The package structure is as follows:
		- elevator: this package contains all files associated with the elevator subsystem
		- floor: this package contains all files associated with the floor subsystem
		- scheduler: this package contains all files associated with the scheduler subsystem and state machine, along with all of the states.
		- net: this package contains all of the files that are commonly shared between the three subsystems such as the messaging system. It also contains some mock classes
			   that were created for testing purposes.
	 	- text: this package contains the input file
	
	In the package "test", we have all of our Junit test classes. The package structure is the same is in "main".

Responsibilities:

	Sam - Did the elevator subsystem
	Nikola - Helped with elevator subsystem, wrote elevator test cases, made UML
	Austin - Created the messaging system, worked on scheduler subsystem, wrote integration tests, made state machine diagram
	Nic - Worked on scheduler subsystem and wrote test cases
	Devon - Worked on floor subsystem and wrote test cases


-----------------------------------Iteration 2-----------------------------------

Instructions to run the program are the same as iteration 1

File Structure:

	Changes made after Iteration 1:
		- New package "settings" has been added. This is used for any configurable constants such as number of floors and number of elevators.
		
Responsibilities:

	Sam - Updated elevator for running as a thread. Created ElevatorSubsystem to handle elevator creation.
	Nikola - Updated elevator to use ElevatorMessage. Wrote Handlers for toggling the lamps, UML.
	Austin - Write what you did
	Nic - Write what you did
	Devon - Write what you did

