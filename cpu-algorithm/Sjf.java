
import java.util.ArrayList;

public class Sjf implements CpuAlgorithm{
	
	/*contains the original list of job*/
	private ArrayList<Job> arlistJob; 
	private ArrayList<Job> arlistTrack;
	
	/*contains the temporary list of job for processing*/
	private ArrayList<Job> arlistJobTemp;
	
	public void run(ArrayList<Job> arlistJob, double dblTotalBurstTime){
		
		this.arlistJob = arlistJob;
		
		/*holds the track of the process for creating a Gantt Chart*/
		arlistTrack = new ArrayList<Job>();
		
		/*make a copy*/
		arlistJobTemp = new ArrayList<Job>();
		arlistJobTemp.addAll(arlistJob);
		
		double dblProcessTime = 0d;
		
		while(dblProcessTime < dblTotalBurstTime){
			
			Job objJob = getNextJob(dblProcessTime);
			
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
			
		}//close for
		
		return;
	}//close run()
	
	/*select only one job to process*/
	private Job getNextJob(double dblProcessTime){
		
		Job objJobProcess = null;
		
		/*contains the list the jobs that available for processing*/
		ArrayList<Job> arlistForProcess = new ArrayList<Job>();
			
		/*get the list of jobs that are available to process*/
		for(Job objJob: arlistJobTemp){
			
			if(objJob.getArrivalTime() <= dblProcessTime){
			
				arlistForProcess.add(objJob);
			
			}//close if
		}//close for
		
		/*get the smallest job in the list of jobs that are available to process*/
		if(arlistForProcess.size() != 0){
			
			objJobProcess = arlistForProcess.get(0);
			for(Job objJob: arlistForProcess){
				
				/*test if the smallest job selected is greater than to the someone of job in list*/
				if(objJob.getBurstTime() < objJobProcess.getBurstTime()){

					objJobProcess = objJob;
				
				/*test if smallest job is equal to to the selected job in the list*/
				}else if(objJob.getBurstTime() == objJobProcess.getBurstTime()){
					
					/*test if smallest arrival time is equal to the selected job arrival time in the list
					  if equal basis to find the next job is the job number
					  */
					if(objJob.getArrivalTime() == objJobProcess.getArrivalTime()){
						
						if(objJob.getJobNumber() < objJobProcess.getJobNumber()){
							
							objJobProcess = objJob;
						}//close if
					}else if(objJob.getArrivalTime() < objJob.getArrivalTime()){
						
						objJobProcess = objJob;
				
					}//close if
				}//close if
			}//close for
		
		/*if there's no job arrived, select a job by arrival time*/
		}else{
			
			objJobProcess = arlistJobTemp.get(0);
			for(Job objJob: arlistJobTemp){
				
				if(objJob.getArrivalTime() < objJobProcess.getArrivalTime()){
					
					objJobProcess = objJob;
				}else if(objJob.getArrivalTime() == objJobProcess.getArrivalTime()){
					
					if(objJob.getBurstTime() == objJobProcess.getBurstTime()){
						
						if(objJob.getJobNumber() < objJobProcess.getJobNumber()){
							
							objJobProcess = objJob;
						}//close if
					}else if(objJob.getBurstTime() < objJobProcess.getBurstTime()){
						
						objJobProcess = objJob;
					}//close if
				}//close if
			}//close for
		}//close else
			
		/*remove the smallest job*/
		arlistJobTemp.remove(objJobProcess);
		
		return(objJobProcess);
	}//close if

	/*process job*/
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
}//close Sjf