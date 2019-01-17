import java.io.*;
import java.util.*;
import java.io.File;

public class RandomNumber{
	String f = "Number.txt";
	Scanner sc = new Scanner(System.in);
	
	public RandomNumber(){
		try{
			sc = new Scanner(new File(f));
		} catch(FileNotFoundException e){
			System.exit(1);
		}
	}

	public int randomOS(int u){
		int x = sc.nextInt();
		//System.out.print("\nFind burst when choosing ready process to run " + Integer.toString(x));
		return 1 + (x % u);
	}
} 