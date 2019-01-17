public class Process{
	// data fields
	private int processNum; // process number, from 1 to 4
	private int S; // size of process in virtual address
	private int N; // the number of references
	private double A;
	private double B;
	private double C;
	private double random;
	private int faultNum = 0;
	private int evictNum = 0;
	private int w = -1;

	// constructor
	public Process(int num, int s, int n, double a, double b, double c, double ran){
		processNum = num;
		S = s;
		N = n;
		A = a;
		B = b;
		C = c;
		random = ran;
	}
	public Process(){}

	// getters
	public int getProcessNum(){return processNum;}
	public int getN(){return N;}
	public int getS(){return S;}
	public double getA(){return A;}
	public double getB(){return B;}
	public double getC(){return C;}
	public double getRandom(){return random;}
	public int getEvictNum(){return evictNum;}
	public int getFaultNum(){return faultNum;}
	public int getW(){return w;}

	// functions
	// check if this process is terminated
	public boolean isTerminated(){
		if(N == 0){
			return true;
		} else{
			return false;
		}
	} // end isTerminated
	public void decreaseN(){N--;}
	public void increaseFault(){faultNum++;}
	public int updateW(RandomNum ran){
		// generate a double between 0 and 1
	  	double x = ran.nextRan() / (Integer.MAX_VALUE + 1d);
	  	if (x < A) {
	   		w = (w + 1) % S;
	  	} else if (x < A + B) {
	   		w = (w - 5 + S) % S;
	  	} else if (x < A + B + C) {
	   		w = (w + 4) % S;
	  	} else {
			w = ran.nextRan() % S;
		}
		return w;
	}
	public int initializeW(){
		if(w == -1){
			w = (111 * processNum) % S;
		} 
		return w;
	}
	public void increaseEvictNum(){evictNum++;}

}