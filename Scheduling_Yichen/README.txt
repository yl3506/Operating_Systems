Project: Scheduling
Author: Yichen Li
OS202 FALL 2018

A simulating program of operating system process scheduling algorithms.
This project includes four types of scheduling algorithms:
1. FCFS: First Come First Served.
2. RR: Round Robin with quantum of 2 cycles.
3. LCFS: Last Come First Served.
4. HPRN: Highest Penalty Ratio Next.

-----------------------------------------------------------------
How to run the program: 

Unzip the file in a directory and execute under a command line shell.

1. Go to the "Scheduling_Yichen" directory.

2. Compile: enter commmand "javac *.java"

3. Execution: 
	Use command
		java Scheduling <input-file>
					or
		java Scheduling --verbose <input-file>
	to execute the program.

	The first command gives the general result. 
	The second command gives the debug information, including the record for each cycle and random numbers used each time.
	All four algorithms will be used and printed seperately in each execution.
	
	Example command: 
	javac *.java
	java Scheduling input01.txt
	java Scheduling --verbose input01.txt

4. Optional: You can show random numbers on each call in script RandomNumber.java, 
Uncomment line 19 to let the program print random numbers.

5. All random numbers are stored in the file Number.txt, 
The numbers will be automatically read when you execute the program.


