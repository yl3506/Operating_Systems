import java.util.*;
import java.io.*;
public class RandomNum{

	// all number storage
	private ArrayList<Integer> allNum = new ArrayList<>();
	
	// constructor
	public RandomNum(){
		// read all random numbers from file
		try{
			Scanner sc = new Scanner(new File("AllRandom.txt"));
			while(sc.hasNext()){
				allNum.add(sc.nextInt());
			}
			// close scanner when finished reading
			sc.close();
		} catch (FileNotFoundException e){
			System.out.println("Cannot find file named AllRandom.txt");
			System.exit(1);
		}
	} // end consructor

	// get next random number in list
	public int nextRan(){
		// System.out.println("now random " + allNum.get(0));
		return allNum.remove(0);
	}

	// check if file has end
	public boolean hasNextRan(){
		return !allNum.isEmpty();
	}

}