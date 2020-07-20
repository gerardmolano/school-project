
import java.util.ArrayList;

public class Deadline implements CpuAlgorithm{
	
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
			
			Job objJob = getNextJob(dblProcessTime);
			
			/*for idle time*/
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
			
			dblProcessTime = processJob(objJob, dblProcessTime);
		
		}//close while
	}//close run
	
	/*selecte job to be processed*/
	private Job getNextJob(double dblProcessTime){
		
		Job objJobProcess = null;
		
		/*contains the list the jobs that available for processing*/
		ArrayList<Job> arlistForProcess = new ArrayList<Job>();
			
		/*get the list of jobs that are available to process*/
		
		for(Job objJob: arlistTemp){
			
			if(objJob.getArrivalTime() <= dblProcessTime){
				
				arlistForProcess.add(objJob);
			
			}//close if
		}//close for
		
		/*get the earliest deadline job*/
		if(arlistForProcess.size() != 0){
			
			objJobProcess = arlistForProcess.get(0);
			
			/*select the earliest deadline job*/
			for(Job objJob: arlistForProcess){
				
				if((objJob.getPriorityDeadline() < objJobProcess.getPriorityDeadline() && (objJob.getPriorityDeadline() != 0d)) || objJobProcess.getPriorityDeadline() == 0d){
				
					objJobProcess = objJob;
				/*if the has same deadline, base arrival time*/
				}else if(objJob.getPriorityDeadline() == objJobProcess.getPriorityDeadline()){
					
					if(objJob.getArrivalTime() < objJobProcess.getArrivalTime()){
						
						objJobProcess = objJob;
					}else if(objJob.getArrivalTime() == objJobProcess.getArrivalTime()){
						
						if(objJob.getJobNumber() < objJobProcess.getJobNumber()){
							
							objJobProcess = objJob;
						}//close if
					}//close if
				}//close if
			}//close for
			
		/*if there's no job arrived basis to find the next job is the arrival time*/
		}else{
			
			objJobProcess = arlistTemp.get(0);
			
			for(Job objJob: arlistTemp){
				
				if(objJob.getArrivalTime() < objJobProcess.getArrivalTime()){
					
					objJobProcess = objJob;
				}else if (objJob.getArrivalTime() == objJobProcess.getArrivalTime()){
					
					if(objJob.getPriorityDeadline() == objJobProcess.getPriorityDeadline()){
						
						if(objJob.getArrivalTime() < objJobProcess.getArrivalTime()){
							
							objJobProcess = objJob;
						}else if(objJob.getArrivalTime() == objJobProcess.getArrivalTime()){
							
							if(objJob.getJobNumber() < objJobProcess.getJobNumber()){
								
								objJobProcess = objJob;
							}//close if
						}//close if
						
					}else if((objJob.getPriorityDeadline() < objJobProcess.getPriorityDeadline() && (objJob.getPriorityDeadline() != 0d)) || objJobProcess.getPriorityDeadline() == 0d){
						
						objJobProcess = objJob;
					}//close if
				}//close if
			}//close for
		}//close else
			
		/*remove the smallest job*/
		arlistTemp.remove(objJobProcess);
		
		return(objJobProcess);
	}//close getNextJob()
	
	/*process the selected job*/
	private double processJob(Job objJob, double dblProcessTime){
		
		objJob.setTurnAroundTime((objJob.getBurstTime() + dblProcessTime) - objJob.getArrivalTime());
		objJob.setWaitingTime(objJob.getTurnAroundTime() - objJob.getBurstTime());
		arlistTrack.add(objJob);
		
		/*update process time*/
		return(dblProcessTime += objJob.getBurstTime());
	}//close processJob()
	
	/*return the results of the computation*/
	public ArrayList<Job> getResultCompute(){
		
		return(arlistJob);
	}//close getResultCompute()
	
	/*return the track of the result*/
	public ArrayList<Job> getResultTrack(){
		
		return(arlistTrack);
	}//close getResulTrack()
	
	/*this method is applicable only in rr and rro*/
	public void quantumOverhead(double dblQuantum, double dblOverhead){
		
		return;
	}//close quantumOverhead
}//close Deadline