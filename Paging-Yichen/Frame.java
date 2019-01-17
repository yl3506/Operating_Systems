public class Frame{
	private int frameNum;
	private Page page = null;
	private int loadTime = Integer.MAX_VALUE;
	private int recentUse = Integer.MAX_VALUE;

	// constructor
	public Frame(int n, Page p){
		frameNum = n;
		page = p;
	}
	public Frame(){}

	// getter
	public int getFrameNum(){return frameNum;}
	public Page getPage(){return page;}
	public int getLoadTime(){return loadTime;}
	public int getRecentUse(){return recentUse;}

	// functions
	public void setPage(Page p){page = p;}
	public void setLoadTime(int t){loadTime = t;}
	public void setRecentUse(int i){recentUse = i;}
}