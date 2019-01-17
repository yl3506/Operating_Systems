import java.io.*;
import java.util.*;
import java.lang.Math;

public class LCFS{
	private ArrayList<Process> allProcesses = null;
	private boolean verbose = false;
	private int cycle = 0;
	//private ArrayList<Process> sortedList = new ArrayList<>();
	private ArrayList<Process> blockList = new ArrayList<>();
	private ArrayList<Process> runningList = new ArrayList<>();
	private ArrayList<Process> readyList = new ArrayList<>();
	private RandomNumber random = new RandomNumber();
	private int totalRunningTime = 0;
	private int totalBlockTime = 0;
	private int totalWaitingTime = 0;
	private int totalTurnAround = 0;
	public LCFS(ArrayList<Process> p, ArrayList<Process> s, boolean v){
		allProcesses = s; //already sorted
		//sortedList = s;
		verbose = v;
	}
	public void run(){
		while(!allTerminated()){
			if(verbose){
				System.out.print("\nBefore cycle:\t" + Integer.toString(cycle) + ":");
				for(int i = 0; i < allProcesses.size(); i++){
					Process cur = allProcesses.get(i);
					String status = cur.getStatus();
					System.out.print("\t" + status + "  ");
					if(status == "unstarted" || status == "ready" || status == "terminated"){
						System.out.print("0");
					} else if(status == "running"){
						System.out.print(Integer.toString(cur.getPrintRemainC()));
					} else if(status == "blocked"){
						System.out.print(Integer.toString(cur.getCurRemainB()));
					}
				}
				System.out.print(".");
			}
			DoBlockedProcesses();
			DoRunningProcesses();
			DoArrivingProcesses();
			DoReadyProcesses();
			cycle++;
		}
		cycle--;
		System.out.println("\nThe scheduling algorithm used was Last Come First Served");
		for(int i = 0; i < allProcesses.size(); i++){
			Process p = allProcesses.get(i);
			System.out.print("\nProcess " + Integer.toString(i) + ":\n");
			System.out.print("\t(A, B, C, M) = (" + Integer.toString(p.getA()) + 
				", " + Integer.toString(p.getB()) + ", " + Integer.toString(p.getC())
				 + ", " + Integer.toString(p.getM()) + ")\n");
			System.out.print("\tFinishing time: " + Integer.toString(p.getFinishTime()) + "\n");
			System.out.print("\tTurnaround time: " + Integer.toString(p.getFinishTime() - p.getA()) + "\n");
			totalTurnAround += p.getFinishTime() - p.getA();
			System.out.print("\tI/O time: " + Integer.toString(p.getCumBlockTime()) + "\n");
			System.out.println("\tWaiting time: " + Integer.toString(p.getWaitingTime()));
			totalWaitingTime += p.getWaitingTime();
		}
		System.out.print("\nSummary Data:\n");
		System.out.print("\tFinishing time: " + Integer.toString(cycle));
		float cpuUtilization = totalRunningTime * 1.0f / cycle;
		System.out.printf("\n\tCPU Utilization: %.6f", cpuUtilization);
		float ioUtilization = totalBlockTime * 1.0f / cycle;
		System.out.printf("\n\tI/O Utiliztion: %.6f", ioUtilization);
		float throughput = 100f * allProcesses.size() / cycle;
		System.out.printf("\n\tThroughput: %.6f processes per hundred cycles", throughput);
		float turnaround = totalTurnAround * 1.0f / allProcesses.size();
		System.out.printf("\n\tAverage turnaround time: %.6f", turnaround);
		float aveWaiting = totalWaitingTime * 1.0f / allProcesses.size();
		System.out.printf("\n\tAverage waiting time: %.6f", aveWaiting);
		System.out.println();
	}
	private boolean allTerminated(){
		for (int i = 0; i < allProcesses.size(); i++){
			Process cur = allProcesses.get(i);
			if (cur.getStatus() != "terminated"){
				return false;
			}
		}
		return true;
	}
	private void DoBlockedProcesses(){
		if (blockList.isEmpty()){return;}
		for(int i = 0; i < allProcesses.size(); i++){
			Process p = allProcesses.get(i);
			if (blockList.contains(p)){
				p.setCurRemainB(p.getCurRemainB() - 1);
				p.setCumBlockTime(p.getCumBlockTime() + 1);
				if(p.getCurRemainB() == 0){
					p.setStatus("ready");
					blockList.remove(p);
					readyList.add(p);
					p.setReadyTime(cycle);
				}
			}
		}
		totalBlockTime++;
	}
	private void DoRunningProcesses(){
		if (runningList.isEmpty()){return;}
		Process p = runningList.get(0);
		p.setCurRemainC(p.getCurRemainC() - 1);
		p.setTotalRemainC(p.getTotalRemainC() - 1);
		p.setPrintRemainC(p.getPrintRemainC() - 1);
		totalRunningTime++;
		if(p.getCurRemainC() == 0){
			if(p.getTotalRemainC() == 0){
				p.setStatus("terminated");
				p.setFinishTime(cycle);
				runningList.remove(p);
			} else {
				p.setStatus("blocked");
				int curB = p.getPrevC() * p.getM();
				p.setCurRemainB(curB);
				//System.out.print("\nblock for " + Integer.toString(curB));
				runningList.remove(p);
				blockList.add(p);
			} 
		}
	}
	private void DoArrivingProcesses(){
		for(int i = 0; i < allProcesses.size(); i++){
			Process p = allProcesses.get(i);
			if(p.getA() == cycle){
				readyList.add(p);
				p.setStatus("ready");
				p.setReadyTime(cycle);
			}
		}
	}
	private void DoReadyProcesses(){
		if(readyList.isEmpty()){return;}
		if(runningList.isEmpty()){
			Collections.sort(readyList, new Comparator<Process>(){
				@Override
				public int compare(Process one, Process two){
					int A1 = one.getReadyTime();
					int arrivingOrder1 = allProcesses.indexOf(one);
					int A2 = two.getReadyTime();
					int arrivingOrder2 = allProcesses.indexOf(two);
					if(A1 < A2){return 1;}
					if(A1 > A2){return -1;}
					return arrivingOrder1 < arrivingOrder2? -1:1;
				}
			});
			Process p = readyList.remove(0);
			p.setReadyTime(Integer.MAX_VALUE);
			p.setStatus("running");
			int nextRun = random.randomOS(p.getB());
			p.setPrintRemainC(nextRun);
			if(nextRun > p.getTotalRemainC()){
				nextRun = p.getTotalRemainC();
			}
			//System.out.print("\nnextRun: " + Integer.toString(nextRun));
			p.setPrevC(nextRun);
			p.setCurRemainC(nextRun);
			runningList.add(p);
		}
		for(int i = 0; i < readyList.size(); i++){
			Process p = readyList.get(i);
			p.setWaitingTime(p.getWaitingTime() + 1);
		}
	}
}