
public class Job{
	
	private double dblArrivalTime;
	private double dblBurstTime;
	private double dblPriorityDeadline;
	private double dblTurnAroundTime;
	private double dblWaitingTime;
	
	private int intJobNum;
	
	public double getArrivalTime(){
		
		return(dblArrivalTime);
	}//close getArrivalTime()
	
	public double getBurstTime(){
		
		return(dblBurstTime);
	}//close getBurstTime()
	
	public double getPriorityDeadline(){
		
		return(dblPriorityDeadline);
	}//close getPriorityDeadline()
	
	public double getTurnAroundTime(){
		
		return(dblTurnAroundTime);
	}//close getTurnAroundTime()
	
	public double getWaitingTime(){
		
		return(dblWaitingTime);
	}//close getWaitingTime()
	
	public int getJobNumber(){
		
		return(intJobNum);
	}//close getJobNumber
	
	public void setArrivalTime(double dblArrivalTime){
		
		this.dblArrivalTime = dblArrivalTime;
		
		return;
	}//close setArrivalTime()
	
	public void setBurstTime(double dblBurstTime){
		
		this.dblBurstTime = dblBurstTime;
	
		return;
	}//close setBurstTime()
	
	public void setPriorityDeadline(double dblPriorityDeadline){
		
		this.dblPriorityDeadline = dblPriorityDeadline;
		
		return;
	}//close setPriorityDeadline()
	
	public void setTurnAroundTime(double dblTurnAroundTime){
		
		this.dblTurnAroundTime = dblTurnAroundTime;
		
		return;
	}//close setTurnAroundTime()
	
	public void setWaitingTime(double dblWaitingTime){
		
		this.dblWaitingTime = dblWaitingTime;
		
		return;
	}//close setWaitingTime()
	
	public void setJobNumber(int intJobNum){
		
		this.intJobNum = intJobNum;
		
		return;
	}//close setJobNumber()
}//close Job