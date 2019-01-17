import java.util.*;

public class Process {
	ArrayList<Integer> ini;
	int waitingTime; // total waiting
	int runningTime; // total running
	int curWaitingTime;
	LinkedList<Instruction> ins; // instructions
	ArrayList<Integer> rs;

	public Process(int numR) {
		rs = new ArrayList<Integer>();
		ins = new LinkedList<Instruction>();
		ini = new ArrayList<Integer>();
		for (int i = 0; i <= numR; i ++)  {
			rs.add(0);
			ini.add(0);
		}
	}
} // end Task class
