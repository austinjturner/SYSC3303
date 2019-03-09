===============================================================================
================================= Iteration 3 =================================
===============================================================================

Instructions to run program:

	- File -> Open projects from file system
	- Open the project file that you extracted
	- Expand the project files in eclipse's package explorer view
	- Go to src -> test -> integration
	- Right click on TestSystemFromFile.java and run as JUnit test
	- View the console to see the test working
	
File Structure:
	
	In the package "main" is where you will find all of the classes used to make the program run. The package structure is as follows:
		- elevator: this package contains all files associated with the elevator subsystem
		- floor: this package contains all files associated with the floor subsystem
		- scheduler: this package contains all files associated with the scheduler subsystem and state machine
			- scheduler.algorithms: this package contains class definitions for different algorithms the scheduler may use
			- scheduler.states: this package contains class definitions for all states used by the StateMachine
		- net: this package contains files that are commonly shared between the three subsystems for messaging. It also contains some mock classes.
			- net.messages: this packages contains class definitions for different Message types that are sent between subsystems
	 	- text: this package contains the input file
	 	- settings: this package contains common settings for the system all in one place
	
	In the package "test", we have all of our Junit test classes. The package structure is the same is in "main".
		
Responsibilities:

	Sam 	- Update elevator to accept fault simulation messages and perform that simulation.
	Nikola 	- Update elevator to accept fault simulation messages and perform that simulation. UML.
	Austin 	- Updated scheduler to handle message for fault simulation and door sensor fault. Sequence diagrams.
	Nic 	- Updated scheduler state machine to handle doors stuck open and closed.
	Devon 	- Enhanced floor text parsing to support simulated faults.



===============================================================================
================================= Iteration 2 =================================
===============================================================================

Instructions to run program:

	- File -> Open projects from file system
	- Open the project file that you extracted
	- Expand the project files in eclipse's package explorer view
	- Go to src -> test -> integration
	- Right click on TestSystemFromFile.java and run as JUnit test
	- View the console to see the test working
	
File Structure:
	
	In the package "main" is where you will find all of the classes used to make the program run. The package structure is as follows:
		- elevator: this package contains all files associated with the elevator subsystem
		- floor: this package contains all files associated with the floor subsystem
		- scheduler: this package contains all files associated with the scheduler subsystem and state machine
			- scheduler.algorithms: this package contains class definitions for different algorithms the scheduler may use
			- scheduler.states: this package contains class definitions for all states used by the StateMachine
		- net: this package contains files that are commonly shared between the three subsystems for messaging. It also contains some mock classes.
			- net.messages: this packages contains class definitions for different Message types that are sent between subsystems
	 	- text: this package contains the input file
	 	- settings: this package contains common settings for the system all in one place
	
	In the package "test", we have all of our Junit test classes. The package structure is the same is in "main".
		
Responsibilities:

	Sam 	- Updated elevator for running as a thread. Created ElevatorSubsystem to handle elevator creation.
	Nikola 	- Updated elevator to use ElevatorMessage. Wrote Handlers for toggling the lamps, UML.
	Austin 	- Updated scheduler messaging. Integrated subsystems and create demo test case.
	Nic 	- Created scheduler algorithm for many parallel elevators.
	Devon 	- Updated floor subsystem to support simulating button presses and managing floor lamps concurrently.



===============================================================================
================================= Iteration 1 =================================
===============================================================================

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

	Sam 	- Did the elevator subsystem
	Nikola 	- Helped with elevator subsystem, wrote elevator test cases, made UML
	Austin 	- Created the messaging system, worked on scheduler subsystem, wrote integration tests, made state machine diagram
	Nic 	- Worked on scheduler subsystem and wrote test cases
	Devon 	- Worked on floor subsystem and wrote test cases



