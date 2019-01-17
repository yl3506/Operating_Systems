import java.util.*;
import java.io.*;

public class Page{
	// data field
	private int num; // page number
	private Process p; // corresponding process stored
	private Frame frame = null;
	private int loadTime = Integer.MAX_VALUE;
	private int evictTime = Integer.MAX_VALUE;
	private int residencyTime = 0;

	// constructor
	public Page(int i, Process pro){
		num = i;
		p = pro;
		frame = null;
	}
	public Page(){}

	// getter
	public int getPageNum(){return num;}
	public Process getProcess(){return p;}
	public Frame getFrame(){return frame;}
	public int getLoadTime(){return loadTime;}
	public int getEvictTime(){return evictTime;}

	// functions
	public void setFrame(Frame f){frame = f;}
	public void setProcess(Process p) {this.p = p;}
	public boolean isPageFault(){
		if(frame == null){
			return true;
		} else {
			return false;
		}
	}
	public void setLoadTime(int i){loadTime = i;}
	public void setEvictTime(int i){
		evictTime = i;
		residencyTime += (evictTime - loadTime);
	}
	public int getResidencyTime(){return residencyTime;}
}