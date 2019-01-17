// main class for execution
import java.util.*;
import java.io.*;

public class Main{

	public static void main(String[] args){
		
		// data fields
		int M = Integer.parseInt(args[0]); // machine size in words
		int P = Integer.parseInt(args[1]); // page size in words
		int S = Integer.parseInt(args[2]); // size of each process, from 0 to S-1
		int J = Integer.parseInt(args[3]); // job mix, determines A, B, and C
		int N = Integer.parseInt(args[4]);// number of references for each process
		String R = args[5]; // replacement algorithm, LIFO, RANDOM, or LRU
		int Debug = Integer.parseInt(args[6]); // level of debugging
		// note that debugging output is not supported

		// ArrayList to save all processes
		ArrayList<Process> allProcesses = new ArrayList<>();
		allProcesses.add(new Process()); // dummy head
		// ArrayList of all pages
		ArrayList<Page> allPages = new ArrayList<>();
		// ArrayList of all frames
		ArrayList<Frame> allFrames = new ArrayList<>();
		// initialize all frames
		for(int i = 0; i < M/P; i++){
			allFrames.add(new Frame(i, null));
		}

		// probabilities
		double A = 0;
		double B = 0;
		double C = 0;
		double random;
		// number of process
		int D = 0;
		// round robin scheduling quantum
		int q = 3;
		// number of pages each process takes
		int processPage = S/P;
		RandomNum ran = new RandomNum();


		// decides the combination
		if(J == 1){
			A = 1;
			B = 0;
			C = 0;
			random = 1 - A - B - C; 
			D = 1;
			// initialize processes, pagetable, and pages
			// creates four processes each with size N
			for(int i = 0; i < D; i++){
				allProcesses.add(new Process(i + 1, S, N, A, B, C, random));
			}
			// initialize all pages
			for(int i = 0; i < D; i++){
				for(int j = 0; j < S/P; j++){
					allPages.add(new Page(i * (S/P) + j, allProcesses.get(i+1)));
				}
			}
		} else if (J == 2){
			A = 1;
			B = 0;
			C = 0;
			random = 1 - A - B - C;
			D = 4; // four processes
			// initialize processes, pagetable, and pages
			// creates four processes each with size N
			for(int i = 0; i < D; i++){
				allProcesses.add(new Process(i + 1, S, N, A, B, C, random));
			}
			// initialize all pages
			for(int i = 0; i < D; i++){
				for(int j = 0; j < S/P; j++){
					allPages.add(new Page(i * (S/P) + j, allProcesses.get(i+1)));
				}
			}
		} else if (J == 3){
			A = 0;
			B = 0;
			C = 0;
			random = 1 - A - B - C;
			D = 4; // four processes
			// initialize processes, pagetable, and pages
			// creates four processes each with size N
			for(int i = 0; i < D; i++){
				allProcesses.add(new Process(i + 1, S, N, A, B, C, random));
			}
			// initialize all pages
			for(int i = 0; i < D; i++){
				for(int j = 0; j < S/P; j++){
					allPages.add(new Page(i * (S/P) + j, allProcesses.get(i+1)));
				}
			}
		} else if (J == 4){
			D = 4; // one process
			// initialize processes, pagetable, and pages
			// creates four processes each with size N
			random = 0;
			A = 0.75; B = 0.25; C = 0; random = 1 - A - B - C;
			allProcesses.add(new Process(1, S, N, A, B, C, random));
			A = 0.75; B = 0; C = 0.25; random = 1 - A - B - C;
			allProcesses.add(new Process(2, S, N, A, B, C, random));
			A = 0.75; B = 0.125; C = 0.125; random = 1 - A - B - C;
			allProcesses.add(new Process(3, S, N, A, B, C, random));
			A = 0.5; B = 0.125; C = 0.125; random = 1 - A - B - C;
			allProcesses.add(new Process(4, S, N, A, B, C, random));
			// initialize all pages
			for(int i = 0; i < D; i++) {
				for(int j = 0; j < S/P; j++){
					allPages.add(new Page(i * (S/P) + j, allProcesses.get(i+1)));
				}
			}
		}


		// absolute page address pointer
		int curPageNum = 0;
		Page curPage = null;
		int w = 0;
		int cycle = 0;
		int curProcessNum = 0;
		int rr = 0;

		// enter round robin cycling
		while(!allTerminated(allProcesses)){
			cycle ++;
			//System.out.println("cycle begin " + cycle);
			// change to another process once reaches the quantum
			if(rr % q == 0){ // change to another process
				//System.out.println("current");
				curProcessNum = (curProcessNum + 1) % (D + 1);
				if(curProcessNum == 0){curProcessNum += 1;}
				rr = 0;
				// re-initialize page pointer with respect to current process address
				// w = allProcesses.get(curProcessNum).updateW(ran);
			} else { // still inside the same process
				// w = allProcesses.get(curProcessNum).updateW(ran);
			}

			while(allProcesses.get(curProcessNum).isTerminated()){
				curProcessNum = (curProcessNum + 1) % (D + 1);
				if(curProcessNum == 0){curProcessNum += 1;}
				//System.out.println("stuck");
				// re-initialize page pointer with respect to current process address
				// w = allProcesses.get(curProcessNum).updateW(ran);
				// curPageNum = (w + S * (curProcessNum - 1)) / P;
				// extract current page
				// curPage = allPages.get(curPageNum);
				rr = 0;
				//System.out.println("changed to process:" + curProcessNum);
			}
			// determines if this is a page fault

			if(allProcesses.get(curProcessNum).getW() == -1){
				allProcesses.get(curProcessNum).initializeW();
			}
			
			w = allProcesses.get(curProcessNum).getW();
			//System.out.println(cycle + ": " + w);
			// System.out.println("cycle: " + cycle + ", curprocessnum" + curProcessNum);
			curPageNum = (w + S * (curProcessNum - 1)) / P;
			// extract current page
			curPage = allPages.get(curPageNum);
			//System.out.println(curProcessNum+ " at page " + (curPage.getPageNum() % (S / P)) );

			if(curPage.isPageFault()){
				//System.out.println("fault in cycle "+ cycle);
				allProcesses.get(curProcessNum).increaseFault();
				curPage.setLoadTime(cycle);
				
				if(existsEmptyFrame(allFrames)){ // if still exists empty frame
					// choose an empty frame to load
					Frame chosedFrame = chooseEmptyFrame(allFrames);
					// load current page into the frame
					curPage.setFrame(chosedFrame);
					// update the frame reference as well
					chosedFrame.setPage(curPage);
					// update frame load time
					chosedFrame.setLoadTime(cycle);
					// update recent use
					chosedFrame.setRecentUse(cycle);
					// update page load time
					curPage.setLoadTime(cycle);
					// what else should be updated?
				} else { // if does not exist any empty frame
					// have to choose one frame to evict
					Frame frameToEvict = null;
					Page pageToEvict = null;
					Process processToEvict = null;
					// decide which replacement/eviction algorithm to use				
					if(R.equals("LIFO") || R.equals("lifo")){
						// choose a evict program according to lifo
						frameToEvict = LIFO(allFrames);
						pageToEvict = frameToEvict.getPage();
						processToEvict = pageToEvict.getProcess();
					} else if(R.equals("RANDOM") || R.equals("random")){
						// choose a evict program according to random
						frameToEvict = RANDOM(allFrames, ran, M/P);
						pageToEvict = frameToEvict.getPage();
						processToEvict = pageToEvict.getProcess();
					} else if(R.equals("LRU") || R.equals("lru")){
						// choose a evict program according to lru
						frameToEvict = LRU(allFrames);
						pageToEvict = frameToEvict.getPage();
						processToEvict = pageToEvict.getProcess();
					}
					// unlink page and frame
					pageToEvict.setFrame(null);
					pageToEvict.setEvictTime(cycle);
					pageToEvict.setLoadTime(Integer.MAX_VALUE);
					frameToEvict.setPage(null);
					frameToEvict.setLoadTime(Integer.MAX_VALUE);
					processToEvict.increaseEvictNum();
					// load page and process to this frame
					frameToEvict.setPage(curPage);
					frameToEvict.setLoadTime(cycle);
					frameToEvict.setRecentUse(cycle);
					curPage.setFrame(frameToEvict);
					curPage.setLoadTime(cycle);

				}
				curPage.getFrame().setLoadTime(cycle);



			} else{ // if current reference is a page hit
				Frame curFrame = curPage.getFrame();
				curFrame.setRecentUse(cycle);
			}
			rr ++;

			// decrease reference time for current visited process
			allProcesses.get(curProcessNum).decreaseN();
			allProcesses.get(curProcessNum).updateW(ran);

		} // end while


		// print results
		System.out.println("The machine size is " + M + ".");
		System.out.println("The page size is " + P + ".");
		System.out.println("The process size is " + S + ".");
		System.out.println("The job mix number is " + J + ".");
		System.out.println("The number of references per process is " + N + ".");
		System.out.println("The replacement algorithm is " + R + ".");
		System.out.println("The level of debugging output is " + Debug);
		
		int totFault = 0, totRes = 0, totEvict = 0;
		System.out.println();
		for(int i = 1; i <= D; i++){
			Process curP = allProcesses.get(i);
			// collect residency time for each process
			int resSum = 0;
			for(int k = 0; k < S/P; k++){
				resSum += allPages.get((i-1) * (S/P) + k).getResidencyTime();
			}
			// calculate average residency time

			double aveRes = (double)resSum / (curP.getEvictNum());
			// System.out.print("resSum = " + (double)resSum + ", evictNum = " + (double)curP.getEvictNum());

			if (curP.getEvictNum() != 0) {

				System.out.println("Process " + i + " had " + curP.getFaultNum() 
					+ " faults and " + aveRes + " average residency." );
			}
			else {
				System.out.println("Process " + i + " had " + curP.getFaultNum() + " faults.\n" + 
    			 	"	With no evictions, the average residence is undefined.");		
			}
			totFault += curP.getFaultNum();
			totRes += resSum;
			totEvict += curP.getEvictNum();
		}

		System.out.println();
		if (totEvict == 0) {
			System.out.println("The total number of faults is " + totFault + ".\n" +
     			"	With no evictions, the overall average residence is undefined.");
		}
		else {
			double aveRes = (double) totRes / (totEvict);
			System.out.println("The total number of faults is " + totFault +
			" and the overall average residency is " + aveRes + ".");
		}


	} // end main


	// helper function check if all processes are terminated
	public static boolean allTerminated(ArrayList<Process> allProcesses){
		for(int i = 1; i < allProcesses.size(); i++){
			// if exists unterminated process
			if(!allProcesses.get(i).isTerminated()){
				return false;
			}
		}
		return true;
	}

	// check if exists empty frame
	public static boolean existsEmptyFrame(ArrayList<Frame> allF){
		for(int i = 0; i < allF.size(); i++){
			Frame curFrame = allF.get(i);
			if(curFrame.getPage() == null){ // if any of the frame is empty
				return true;
			}
		}
		// if does not exist any empty frame
		return false;
	}

	// choose an empty frame according to the highest numbered frame
	public static Frame chooseEmptyFrame(ArrayList<Frame> allF){
		for(int i = allF.size() - 1; i >= 0; i--){ // traver all frames
			Frame curF = allF.get(i); // current traversing frame
			if(curF.getPage() == null){ // if current frame is empty
				return curF;
			}
		}
		// if does not find empty frame after traversing
		return null;
	}


	// replacement algorithm: LIFO
	public static Frame LIFO(ArrayList<Frame> allF){
		Frame max = allF.get(allF.size() - 1);
		for(Frame f : allF){
			if(f.getLoadTime() > max.getLoadTime()){
				max = f;
			}
		}
		return max;
	}

	// replacement algorithm: RANDOM
	public static Frame RANDOM(ArrayList<Frame> allF, RandomNum ran, int frameSize){
		return allF.get(ran.nextRan() % frameSize);	
	}

	// replacement algorithm: LRU
	public static Frame LRU(ArrayList<Frame> allF){
		Frame min = allF.get(0);
		for(Frame f : allF){
			if(f.getPage() != null && f.getRecentUse() < min.getRecentUse()){
				min = f;
			}
		}
		return min;
	}

}