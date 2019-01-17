import java.util.*;

public class Banker{

	int nProcess;
	int nResource;
	int r;
	ArrayList<Process> processes = new ArrayList<>();
	ArrayList<Integer> resources = new ArrayList<Integer>();

	// constructor
	public Banker(int p, int r) {
		this.nProcess = p;
		this.nResource = r;
		for (int i = 0; i <= p; i++) {
			processes.add(new Process(r));
		}
		processes.get(0).runningTime = 1;
		for (int i = 0; i <= r; i ++) {
			resources.add(0);
		}
	}

	// run
	public void run() {
		System.out.println("\nBanker's");
		int cycle = 0;

		while (!allFinish()) {

			// resources going to be handled next cycle
			ArrayList<Integer> toReturn = new ArrayList<Integer>();
			for (int i = 0; i <= this.nResource; i ++) {
				toReturn.add(0);
			}

			// resources going to be handled this cycle
			LinkedList<Integer> requestList = new LinkedList<Integer>();
			// number of non-terminating Process
			int numNT = 0;
			// iterate through all processes
			for (int i = 1; i <= this.nProcess; i ++) {

				//already terminated
				if (processes.get(i).ins.size() == 0){
					continue;
				} 
				// terminate instruction
				if (processes.get(i).ins.peek().c.equals("terminate")) {
					for (int j = 1; j <= r; j ++) {
						toReturn.set(j, toReturn.get(j) + processes.get(i).rs.get(j));
					}
					processes.get(i).runningTime = cycle; 
					continue;
				}

				// // compute instruction
				// if (processes.get(i).ins.peek().c.equals("compute")) {
				// 	processes.get(i).runningTime ++;
				// 	processes.get(i).ins.peek().r --;
				// 	continue;
				// }

				// initiate instruction
				if (processes.get(i).ins.peek().c.equals("initiate")) {

					// make claim
					if (resources.get(processes.get(i).ins.peek().r) >= processes.get(i).ins.peek().n) {
						processes.get(i).ini.set(processes.get(i).ins.peek().r, processes.get(i).ins.peek().n);
					}
					// if the claim exceed the system resources, abort it
					else {

						processes.get(i).runningTime = -1;
						processes.get(i).waitingTime = -1;
						System.out.println("Process " + i + " aborted before cycle " + cycle + "-" + (cycle + 1) + " due to claim exceeding system resources.");
						processes.get(i).ins.clear();
						continue;
					}
				}

				// release instruction
				if (processes.get(i).ins.peek().c.equals("release")) {
					toReturn.set(processes.get(i).ins.peek().r, toReturn.get(processes.get(i).ins.peek().r)  + processes.get(i).ins.peek().n);
					processes.get(i).rs.set(processes.get(i).ins.peek().r, processes.get(i).rs.get(processes.get(i).ins.peek().r) - processes.get(i).ins.peek().n);
				}
	
				// request instruction
				if (processes.get(i).ins.peek().c.equals("request")) {
					int r = processes.get(i).ins.peek().r, num = processes.get(i).ins.peek().n;

					//valid request
					if (processes.get(i).ini.get(r) >= processes.get(i).rs.get(r) + num) {
						requestList.add(i);
					}

					//it request exceeds its claim, abort it
					else {
						processes.get(i).runningTime = -1;
						processes.get(i).waitingTime = -1;
						System.out.println("Process " + i + " aborted before cycle " + cycle + "-" + (cycle + 1) + " due to request exceeding its claim.");
						processes.get(i).ins.clear();
						for (int j = 1; j <= r; j ++) {
							toReturn.set(j, toReturn.get(j) + processes.get(i).rs.get(j));
						}
					}
				}
			}

			//sort the current waiting time for requests
			for (int i = 0; i < requestList.size() - 1; i ++) {
				for (int j = 1; j < requestList.size() - i; j ++) {
					if (processes.get(requestList.get(j - 1)).curWaitingTime < processes.get(requestList.get(j)).curWaitingTime) {
						int tmp = requestList.get(j - 1);
						requestList.set(j - 1, requestList.get(j));
						requestList.set(j, tmp);
					}
				}
			}

			// iterate request list
			for (int i = 0; i < requestList.size(); i ++ ) {
				Process t = processes.get(requestList.get(i));

				// if a safe state is guaranteed, process it
				if (checksafe(requestList.get(i), t.ins.peek().r, t.ins.peek().n)) {
					// System.out.println("Process " + requestList.get(i) + " successfully at cycle " + cycle);
					t.curWaitingTime = 0;
					resources.set(t.ins.peek().r, resources.get(t.ins.peek().r) - t.ins.peek().n);
					t.rs.set(t.ins.peek().r, t.ins.peek().n + t.rs.get(t.ins.peek().r));
				}

				// otherwise, hold it
				else {
					t.curWaitingTime ++;
					t.waitingTime ++;
				}
			}

			for (int i = 1; i <= this.nResource; i++) {
				resources.set(i, toReturn.get(i) + resources.get(i));
				// System.out.println("Resource " + i + " has " + resources.get(i) + " units at cycle " + cycle);
			}

			// pop used instruction
			for (int i = 1; i <= this.nProcess; i ++) {
				if (processes.get(i).ins.size() == 0) continue;
				else if (processes.get(i).curWaitingTime == 0 && !processes.get(i).ins.peek().c.equals("compute")) {
					processes.get(i).ins.poll();
				}
				else if (processes.get(i).ins.peek().c.equals("compute")){
					// processes.get(i).runningTime ++;
					processes.get(i).ins.peek().r --;
					if (processes.get(i).ins.peek().r == 0) 
						processes.get(i).ins.poll();
				}
			}

			cycle ++;
			
		}

		result();
	}

	// function to check if the next state is safe
	private boolean checksafe(int k, int r, int n) {

		//virtual resources and virtual processes used only in safe state check
		ArrayList<Boolean> ter = new ArrayList<Boolean>();
		ArrayList<Process> vt = new ArrayList<Process>();
		ArrayList<Integer> vr = new ArrayList<Integer>();
		
		for (int i = 0; i <= this.nResource; i ++) {
			vr.add(resources.get(i));
		}
		
		//create virtual processes
		for (int i = 0; i <= this.nProcess; i ++) {
			Process tmp = new Process(this.nResource);
			vt.add(tmp);
			for (int j = 0; j <= this.nResource; j ++) {
				tmp.rs.set(j, processes.get(i).rs.get(j));
				tmp.ini.set(j, processes.get(i).ini.get(j));
			}

			// -1 means terminated
			if (processes.get(i).runningTime != 0) {
				tmp.runningTime = -1;
			}
		}

		// pretend to grant
		vt.get(k).rs.set(r, vt.get(k).rs.get(r) + n);
		vr.set(r, vr.get(r) - n);

		// until all terminated
		while (checkAll(vt)) {
			int count = 0;
			for (Process t : vt) {

				// check non-terminated processes
				if (t.runningTime == 0) {
					boolean b = true;
					for (int i = 0; i <= this.nResource; i ++) {
						if (t.ini.get(i) - t.rs.get(i) > vr.get(i)){
							b = false;
							break;
						}
					}

					//terminate if possible
					if (b) {
						count ++;
						for (int i = 0; i <= this.nResource; i ++) {
							vr.set(i, vr.get(i) + t.rs.get(i));
							t.rs.set(i, 0);
						}
						t.runningTime = -1;
					}
				}
			}

			//if no processes are terminated in this round, the state is not safe
			if (count == 0)
				return false;
		}


		return true;
	
	}

	//check if all processes are terminated in checksafe()
	private boolean checkAll(ArrayList<Process> vt) {
		for (Process t : vt) {
			if (t.runningTime == 0) return true;
		}
		return false;
	}

	// check if all processes are finished
	private boolean allFinish() {
		for (int i = 1; i <= this.nProcess; i ++) {
			if (processes.get(i).ins.size() > 0)
				return false;
		}
		return true;
	}

	// count time taken, waiting time, and percentage of time spending for each Process
	private void result(){
		int tot = 0, tw = 0;
		for (int i = 1; i <= this.nProcess; i ++) {
			if (processes.get(i).runningTime != -1) {
				System.out.println(i + " " + processes.get(i).runningTime + " " + processes.get(i).waitingTime + " " + (100 *processes.get(i).waitingTime /(float) processes.get(i).runningTime) + "%");
				tot += processes.get(i).runningTime;
				tw += processes.get(i).waitingTime;
			}
			else {
				System.out.println(i + " aborted");
			}
		}
		System.out.println("total " + tot + " " + tw + " " + (tw * 100 / (float) tot) + "%");
	}
}

