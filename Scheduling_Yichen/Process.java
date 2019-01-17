import java.io.*;
import java.util.*;

public class Process{
	private int A;
	private int B;
	private int C;
	private int M;
	private String status = "unstarted";
	private boolean preempted = false;
	private int totalRemainC;
	private int contRun = 0;
	private int curRemainB = 0;
	private int curRemainC = 0;
	private int prevC = 0;
	private int printRemainC = 0;
	private int cumBlockTime = 0; // total IO time
	private int finishTime = 0;
	private int waitingTime = 0;
	private int readyTime = Integer.MAX_VALUE;
	private int t = 0; // running time
	public Process(int a, int b, int c, int m){
		A = a;
		B = b;
		C = c;
		totalRemainC = c;
		M = m;
	}
	public int getA(){return A;}
	public int getB(){return B;}
	public int getC(){return C;}
	public int getM(){return M;}
	public int getTotalRemainC(){return totalRemainC;}
	public int getCurRemainB(){return curRemainB;}
	public int getCurRemainC(){return curRemainC;}
	public int getPrevC(){return prevC;}
	public int getPrintRemainC(){return printRemainC;}
	public int getCumBlockTime(){return cumBlockTime;}
	public int getFinishTime(){return finishTime;}
	public int getWaitingTime(){return waitingTime;}
	public int getReadyTime(){return readyTime;}
	public int getT(){return t;}
	public String getStatus(){return status;}
	public boolean getPreempted(){return preempted;}
	public int getContRun(){return contRun;}
	public void setStatus(String s){status = s;}
	public void setTotalRemainC(int t){totalRemainC = t;}
	public void setCurRemainB(int c){curRemainB = c;}
	public void setCurRemainC(int c){curRemainC = c;}
	public void setPrevC(int c){prevC = c;}
	public void setPrintRemainC(int c){printRemainC = c;}
	public void setCumBlockTime(int c){cumBlockTime = c;}
	public void setFinishTime(int c){finishTime = c;}
	public void setWaitingTime(int c){waitingTime = c;}
	public void setPreempted(boolean b){preempted = b;}
	public void setContRun(int c){contRun = c;}
	public void setReadyTime(int c){readyTime = c;}
	public void setT(int c){t = c;}
}