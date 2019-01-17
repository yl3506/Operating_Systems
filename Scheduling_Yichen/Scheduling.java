import java.io.*;
import java.util.*;
import java.io.FileReader; 
import java.io.IOException; 
import java.nio.file.Files; 
import java.nio.file.Paths;

public class Scheduling{
	public static void main(String[] args){
		// verbose flag
		boolean verbose = false;
        if (args.length > 1) {
            if (args[0].equals("--verbose")) {
                verbose = true;
            }
        }
        // read argument
        String inputString = "";
        Scanner sc = null;
        String filepath = args[args.length - 1];
        try {
        inputString = new String(Files.readAllBytes(Paths.get(filepath)));
        inputString = inputString.replaceAll("[^\\d.]", " ");
        sc = new Scanner(inputString);
        }
        catch (Exception e) {
        	System.exit(1);
        }
        // process input 
        int numProcess = sc.nextInt();
        ArrayList<Process> allProcesses1 = new ArrayList<>();
        ArrayList<Process> allProcesses2 = new ArrayList<>();
        ArrayList<Process> allProcesses3 = new ArrayList<>();
        ArrayList<Process> allProcesses4 = new ArrayList<>();
        ArrayList<Process> sortedProcesses1 = new ArrayList<>();
        ArrayList<Process> sortedProcesses2 = new ArrayList<>();
        ArrayList<Process> sortedProcesses3 = new ArrayList<>();
        ArrayList<Process> sortedProcesses4 = new ArrayList<>();
        for(int i = 0; i < numProcess; i++){ // initialize processes
        	int A = sc.skip("[^\\d]").nextInt();
        	int B = sc.skip("[^\\d]").nextInt();
        	int C = sc.skip("[^\\d]").nextInt();
        	int M = sc.skip("[^\\d]").nextInt();
        	allProcesses1.add(new Process(A, B, C, M));
            allProcesses2.add(new Process(A, B, C, M));
            allProcesses3.add(new Process(A, B, C, M));
            allProcesses4.add(new Process(A, B, C, M));
            sortedProcesses1.add(new Process(A, B, C, M));
            sortedProcesses2.add(new Process(A, B, C, M));
            sortedProcesses3.add(new Process(A, B, C, M));
            sortedProcesses4.add(new Process(A, B, C, M));
         }
        // sort input
        Collections.sort(sortedProcesses1, new Comparator<Process>(){
            @Override
            public int compare(Process one, Process two){
                int A1 = one.getA();
                int arrivingOrder1 = sortedProcesses1.indexOf(one);
                int A2 = two.getA();
                int arrivingOrder2 = sortedProcesses1.indexOf(two);
                if(A1 < A2){return -1;}
                if(A1 > A2){return 1;}
                return arrivingOrder1 < arrivingOrder2? -1:1;
            }
        });
        Collections.sort(sortedProcesses2, new Comparator<Process>(){
            @Override
            public int compare(Process one, Process two){
                int A1 = one.getA();
                int arrivingOrder1 = sortedProcesses2.indexOf(one);
                int A2 = two.getA();
                int arrivingOrder2 = sortedProcesses2.indexOf(two);
                if(A1 < A2){return -1;}
                if(A1 > A2){return 1;}
                return arrivingOrder1 < arrivingOrder2? -1:1;
            }
        });
        Collections.sort(sortedProcesses3, new Comparator<Process>(){
            @Override
            public int compare(Process one, Process two){
                int A1 = one.getA();
                int arrivingOrder1 = sortedProcesses3.indexOf(one);
                int A2 = two.getA();
                int arrivingOrder2 = sortedProcesses3.indexOf(two);
                if(A1 < A2){return -1;}
                if(A1 > A2){return 1;}
                return arrivingOrder1 < arrivingOrder2? -1:1;
            }
        });
        Collections.sort(sortedProcesses4, new Comparator<Process>(){
            @Override
            public int compare(Process one, Process two){
                int A1 = one.getA();
                int arrivingOrder1 = sortedProcesses4.indexOf(one);
                int A2 = two.getA();
                int arrivingOrder2 = sortedProcesses4.indexOf(two);
                if(A1 < A2){return -1;}
                if(A1 > A2){return 1;}
                return arrivingOrder1 < arrivingOrder2? -1:1;
            }
        });
        System.out.print("The original input was: " + Integer.toString(numProcess));
        for (int i = 0; i < allProcesses1.size(); i++){
            Process p = allProcesses1.get(i);
            System.out.print(" (" + Integer.toString(p.getA()) + " " 
                + Integer.toString(p.getB()) + " " + Integer.toString(p.getC())
                + " " + Integer.toString(p.getM()) + ")");
        }
        System.out.print("\nThe (sorted) input is: " + Integer.toString(numProcess));
        for (int i = 0; i < sortedProcesses1.size(); i++){
            Process p = sortedProcesses1.get(i);
            System.out.print(" (" + Integer.toString(p.getA()) + " " 
                + Integer.toString(p.getB()) + " " + Integer.toString(p.getC())
                + " " + Integer.toString(p.getM()) + ")");
        }
        System.out.println();
        // run
        FCFS fcfs = new FCFS(allProcesses1, sortedProcesses1, verbose);
        fcfs.run();
        RR rr = new RR(allProcesses2, sortedProcesses2, verbose);
        rr.run();
        LCFS lcfs = new LCFS(allProcesses3, sortedProcesses3, verbose);
        lcfs.run();
        HPRN hprn = new HPRN(allProcesses4, sortedProcesses4, verbose);
        hprn.run();
     }
}