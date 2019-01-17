import java.util.*;

public class FIFO {

	// data initialize
	int nProcess;
	int nResource;
	ArrayList<Process> processes = new ArrayList<>();
	ArrayList<Integer> resources = new ArrayList<>();

	// constructor
	public FIFO(int p, int r) {
		this.nProcess = p;
		this.nResource = r;
		for (int i = 0; i <= p; i++) {
			processes.add(new Process(r));
		}
		for (int i = 0; i <= r; i ++) {
			resources.add(0);
		}
	}

	// run
	public void run() {

		System.out.println("FIFO");
		int cycle = 0;

		while (!allFinish()) {
			
			// instructions to be handled this cycle
			LinkedList<Integer> requestList = new LinkedList<>(); 
			int numNT = 0; // number of non-terminating processes
			int granted = 0; // granted resource
			// amount of resources to be released next cycle
			ArrayList<Integer> toRelease = new ArrayList<>();
			for (int i = 0; i <= this.nResource; i ++) {
				toRelease.add(0);
			}

			// check each process for instruction
			for (int i = 1; i <= this.nProcess; i ++) {
				//already terminated
				if (processes.get(i).ins.size() == 0){
					continue;
				} 
				// terminate instruction
				if (processes.get(i).ins.peek().c.equals("terminate")) {
					for (int j = 1; j <= this.nResource; j ++) { // release all resources
						toRelease.set(j, toRelease.get(j) + processes.get(i).rs.get(j));
					}
					// total running time
					processes.get(i).runningTime = cycle; 
					continue;
				}
				// compute instruction
				if (processes.get(i).ins.peek().c.equals("compute")) {
					processes.get(i).ins.peek().r --;
					continue;
				}

				// non-terminated process
				numNT ++;
				// release instruction
				if (processes.get(i).ins.peek().c.equals("release")) {
					toRelease.set(processes.get(i).ins.peek().r, 
								toRelease.get(processes.get(i).ins.peek().r)  
								+ processes.get(i).ins.peek().n);
					processes.get(i).rs.set(processes.get(i).ins.peek().r, 
										processes.get(i).rs.get(processes.get(i).ins.peek().r) 
										- processes.get(i).ins.peek().n);
				}
				// request instruction
				if (processes.get(i).ins.peek().c.equals("request")) {
					requestList.add(i);
				}
			}

			// sort the current waiting time for requests
			for (int i = 0; i < requestList.size() - 1; i ++) {
				for (int j = 1; j < requestList.size() - i; j ++) {
					if (processes.get(requestList.get(j - 1)).curWaitingTime < processes.get(requestList.get(j)).curWaitingTime) {
						int tmp = requestList.get(j - 1);
						requestList.set(j - 1, requestList.get(j));
						requestList.set(j, tmp);
					}
				}
			}

			// do request instruction
			for (int i = 0; i < requestList.size(); i ++) {
				Process t = processes.get(requestList.get(i));

				if (resources.get(t.ins.peek().r) >= t.ins.peek().n) {
					//grant it when there is enough resources
					resources.set(t.ins.peek().r, resources.get(t.ins.peek().r) - t.ins.peek().n);
					t.rs.set(t.ins.peek().r, t.ins.peek().n + t.rs.get(t.ins.peek().r));
					t.curWaitingTime = 0;

					granted ++;
				}

				//otherwise hold it
				else {
					t.curWaitingTime ++;
					t.waitingTime ++;
				}
			}

			//return resource for the next cycle (also for deadlock check)
			for (int i = 1; i <= this.nResource; i++) {
				resources.set(i, toRelease.get(i) + resources.get(i));
			}


			//reorder it based on task number
			for (int i = 0; i < requestList.size() - 1; i ++) {
				for (int j = i + 1; j < requestList.size(); j ++) {
					if (requestList.get(i) > requestList.get(j)) {
						int tmp = requestList.get(i);
						requestList.set(i, requestList.get(j));
						requestList.set(j, tmp);
					}
				}
			}


			// deadlock check 
			// (till something can be granted)
			while (granted == 0 && requestList.size() == numNT && requestList.size() > 0) {

				numNT --;
				int tmp = 0;
				for (int i = requestList.size() - 1; i >= 0 ; i --) {
					int t = requestList.get(i);
					if (resources.get(processes.get(t).ins.peek().r) >= processes.get(t).ins.peek().n) {
						granted ++;
						break;
					}

				}

				//nothing can be granted => deadlock
				//abort the first(lowest number) task
				if (granted == 0) {
					int t = requestList.poll();

					processes.get(t).ins.clear();
					processes.get(t).waitingTime = -1;
					processes.get(t).runningTime = -1;
					for (int i = 1; i <= this.nResource; i ++) {
						resources.set(i, resources.get(i) + processes.get(t).rs.get(i));
					}
				}
			}

			//excuete the previous command or decrease delay
			for (int i = 1; i <= this.nProcess; i ++) {
				if (processes.get(i).ins.size() == 0) continue;
				else if (processes.get(i).curWaitingTime == 0 && !processes.get(i).ins.peek().c.equals("compute")) {
					processes.get(i).ins.poll();
				}
				else if (processes.get(i).ins.peek().c.equals("compute") && processes.get(i).ins.peek().r == 0){
					processes.get(i).ins.poll();
				}
			}

			cycle ++;	
		}

		// print result
		result();
	}




	// check if all process are terninated
	private boolean allFinish() {
		for (int i = 1; i <= this.nProcess; i ++) {
			if (processes.get(i).ins.size() > 0)
				return false;
		}
		return true;
	}

	// count time taken, waiting time, and percentage of waiting
	private void result(){
		int tot = 0, tw = 0;
		for (int i = 1; i <= this.nProcess; i ++) {
			if (processes.get(i).runningTime != -1) {
				System.out.println(i + " " + processes.get(i).runningTime + " " 
								+ processes.get(i).waitingTime + " " 
								+ (100 * processes.get(i).waitingTime / (float)processes.get(i).runningTime) + "%");
				tot += processes.get(i).runningTime;
				tw += processes.get(i).waitingTime;
			}
			else {
				System.out.println(i + " aborted");
			}
		}
		System.out.println("total " + tot + " " + tw + " " + (tw * 100/ (float) tot) + "%");
	}
} 


