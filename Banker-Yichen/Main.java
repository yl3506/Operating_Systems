// FIFO and banker algorithm main document
import java.io.*;
import java.util.*;
import java.io.FileReader; 
import java.io.IOException; 
import java.nio.file.Files; 
import java.nio.file.Paths;

public class Main{

	public static void main(String[] args){

        // read argument 
        String inputString = "";
        Scanner sc = null;
        String filePath = args[args.length - 1];
        try {
        inputString = new String(Files.readAllBytes(Paths.get(filePath)));
        sc = new Scanner(inputString);
        }
        catch (Exception e) {
            System.out.println(e);
            System.out.println("wtf");
        	System.exit(1);
        }

        // process initialization
        int nProcess = sc.nextInt(); // number of processes
        int nResource = sc.nextInt(); // number of resources
        FIFO f = new FIFO(nProcess, nResource); // initialize fifo algorithm
        Banker b = new Banker(nProcess, nResource); // initialize banker algorithm
        // process resource capacity
        for (int i = 1; i <= nResource; i++){
            int capacity = sc.nextInt();
            f.resources.set(i, capacity); // add to fifo
            b.resources.set(i, capacity); // add to banker
        }

        // process instructions
        while(sc.hasNext()){
            String instruction = sc.next();
            int process = sc.nextInt();
            int resource = sc.nextInt();
            int amount = sc.nextInt();
            f.processes.get(process).ins.offer(new Instruction(instruction, process, resource, amount));
            b.processes.get(process).ins.offer(new Instruction(instruction, process, resource, amount));
        }

        // start running
        sc.close();
        f.run();
        b.run();
    }
}

