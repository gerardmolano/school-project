
import java.util.ArrayList;

public class Fcfs implements CpuAlgorithm{
	
	private ArrayList<Job> arlistJob;
	private ArrayList<Job> arlistTemp;
	private ArrayList<Job> arlistTrack;
	
	/*control the process of the jobs*/
	public void run(ArrayList<Job> arlistJob, double dblTotalBurstTime){
		
		this.arlistJob = arlistJob;
		
		/*make a copy of the jobs*/
		arlistTemp = new ArrayList<Job>();
		arlistTemp.addAll(arlistJob);
		
		/*holds the track of the process for creating a Gantt Chart*/
		arlistTrack = new ArrayList<Job>();
		
		double dblProcessTime = 0.0;
		while(dblProcessTime < dblTotalBurstTime){
			
			Job objJob = getNextJob();

			if(objJob.getArrivalTime() > dblProcessTime){
				
				/*Idle representation*/
				Job objIdle = new Job();
				objIdle.setJobNumber(-1);
				objIdle.setBurstTime(objJob.getArrivalTime() - dblProcessTime);
				
				dblTotalBurstTime += objJob.getArrivalTime() - dblProcessTime;
				dblProcessTime += (objJob.getArrivalTime() - dblProcessTime);
				
				/*add the idle to the tracklist*/
				arlistTrack.add(objIdle);
			}//close if
		
			dblProcessTime = processJobs(objJob, dblProcessTime);
			
		}//close for
		
		return;
	}//close runFcFs()
	
	/*get the next jobs to process*/
	private Job getNextJob(){
		
		Job objJobSmallest = arlistTemp.get(0);
		
		for(Job objJob: arlistTemp){
			
			if(objJob.getArrivalTime() < objJobSmallest.getArrivalTime()){
				
				objJobSmallest = objJob;
				
			}else if(objJob.getArrivalTime() == objJobSmallest.getArrivalTime()){
				
				if(objJob.getJobNumber() < objJobSmallest.getJobNumber()){
					
					objJobSmallest = objJob;
				
				}//close if
			}//close if
		}//clsoe for
		
		arlistTemp.remove(objJobSmallest);
		
		return (objJobSmallest);
	}//close getNextJobs()
	
	/*processing jobs*/
	private double processJobs(Job objJob, double dblProcessTime){
		
		objJob.setTurnAroundTime((objJob.getBurstTime() + dblProcessTime) - objJob.getArrivalTime());
		objJob.setWaitingTime(objJob.getTurnAroundTime() - objJob.getBurstTime());
			
		/*add the job the track for the gantt char*/
		arlistTrack.add(objJob);
			
		/*update process time*/
		dblProcessTime += objJob.getBurstTime();
		
		return(dblProcessTime);
	}//close processJobs()
	
	/*return the results of the computation*/
	public ArrayList<Job> getResultCompute(){
		
		return(arlistJob);
	}//close for

	/*return the track of the result*/
	public ArrayList<Job> getResultTrack(){
		
		return(arlistTrack);
	}//close getResulTrack()
	
	/*this method is applicable only in rr and rro*/
	public void quantumOverhead(double dblQuantum, double dblOverhead){
		
		return;
	}//close quantumOverhead
}//close Fcfs