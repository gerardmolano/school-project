
import java.util.ArrayList;

public class PPrio implements CpuAlgorithm{
	
	/*contains the original list of jobs*/
	ArrayList<Job> arlistJob;
	
	/*copy of the original list*/
	ArrayList<Job> arlistJobTemp;
	
	/*contains the track of executions of the job*/
	ArrayList<Job> arlistTrack;
	
	public void run(ArrayList<Job> arlistJob, double dblTotalBurstTime){
		
		this.arlistJob = arlistJob;
		
		arlistTrack = new ArrayList<Job>();
		
		/*make a list of new job object based on the original job list*/
		arlistJobTemp = new ArrayList<Job>();
		for(Job objOriginalJob: arlistJob){
			
			Job objNewJob = new Job();
			
			objNewJob.setJobNumber(objOriginalJob.getJobNumber());
			objNewJob.setArrivalTime(objOriginalJob.getArrivalTime());
			objNewJob.setBurstTime(objOriginalJob.getBurstTime());
			objNewJob.setPriorityDeadline(objOriginalJob.getPriorityDeadline());
			
			arlistJobTemp.add(objNewJob);
		}//close for
		
		double dblProcessTime = 0d;
		
		while(dblProcessTime < dblTotalBurstTime){
			
			Job objJob = getNextJob(dblProcessTime);
			
			/*idle*/
			if(objJob.getArrivalTime() > dblProcessTime){
				
				/*Idle representation*/
				Job objIdle = new Job();
				objIdle.setJobNumber(-1);
				objIdle.setBurstTime(objJob.getArrivalTime() - dblProcessTime);
				dblTotalBurstTime += objJob.getArrivalTime() - dblProcessTime;
				dblProcessTime += (objJob.getArrivalTime() - dblProcessTime);
				
				/*add the idle to the track list*/
				arlistTrack.add(objIdle);
			}//close if
			
			dblProcessTime = processedJob(objJob, dblProcessTime);
			
		}//close while
	
		return;
	}//close run()
	
	private Job getNextJob(double dblProcessTime){
		
		Job objPriority = null;
		
		/*contains the the jobs the available to process*/
		ArrayList<Job> arlistProcess = new ArrayList<Job>();
		
		for(Job objJob: arlistJobTemp){
			
			if(objJob.getArrivalTime() <= dblProcessTime){
				
				arlistProcess.add(objJob);
			}//close if
		}//close for
		
		/*select a job that has a greater priority*/
		if(!arlistProcess.isEmpty()){
			
			objPriority = arlistProcess.get(0);
			
			for(Job objJob: arlistProcess){
				
				if(objJob.getPriorityDeadline() < objPriority.getPriorityDeadline()){
					
					objPriority = objJob;
				}else if(objJob.getPriorityDeadline() == objPriority.getPriorityDeadline()){
					
					if(objJob.getArrivalTime() < objPriority.getArrivalTime()){
						
						objPriority = objJob;
					}else if(objJob.getArrivalTime() == objPriority.getArrivalTime()){
						
						if(objJob.getJobNumber() < objPriority.getJobNumber()){
							
							objPriority = objJob;
						}//close if
					}//close if
				}//close if
			}//close for
		
		/*if the there's no job arrive, select job by arrival time*/
		}else{
			
			objPriority = arlistJobTemp.get(0);
			
			for(Job objJob: arlistJobTemp){
				
				if(objJob.getArrivalTime() < objPriority.getArrivalTime()){
					
					objPriority = objJob;
				}else if(objJob.getArrivalTime() == objPriority.getArrivalTime()){
					
					if(objJob.getPriorityDeadline() < objPriority.getPriorityDeadline()){
						
						objPriority = objJob;
					}else if(objJob.getPriorityDeadline() == objPriority.getPriorityDeadline()){
						
						if(objJob.getJobNumber() < objPriority.getJobNumber()){
							
							objPriority = objJob;
						}//close if
					}//close if
				}//close if
			}//close for
		}//close else
			
		return(objPriority);
	}//close getNextJob()
	
	private double processedJob(Job objJob, double dblProcessTime){
		
		double dblBurst = 0d;
		
		try{
			
			/*get the next possible job*/
			Job objPossibleNextJob = getNextJobTime(objJob, dblProcessTime);
			
			if((objPossibleNextJob.getArrivalTime() - dblProcessTime) >= objJob.getBurstTime()){
			
				dblProcessTime += objJob.getBurstTime();
				dblBurst = objJob.getBurstTime();
				objJob.setBurstTime(0d);
				arlistJobTemp.remove(objJob);
			
			}else{
				
				
				objJob.setBurstTime(objJob.getBurstTime() - (objPossibleNextJob.getArrivalTime() - dblProcessTime));
				dblBurst =  objPossibleNextJob.getArrivalTime() - dblProcessTime;
				dblProcessTime += dblBurst;
				
			}//close else
		}catch(IndexOutOfBoundsException objIndexException){
			
			dblProcessTime += objJob.getBurstTime();
			dblBurst =  objJob.getBurstTime();
			objJob.setBurstTime(0d);
			arlistJobTemp.remove(objJob);
			
		}//close catch
		
		if(objJob.getBurstTime() == 0d){
			
			/*if the job burst time is equal to 0, compute the turnaround and waiting time*/
			for(Job objOriginalJob: arlistJob){
				
				/*find the job*/
				if(objOriginalJob.getJobNumber() == objJob.getJobNumber()){
					
					/*compute the turn around and waiting time*/
					objOriginalJob.setTurnAroundTime(dblProcessTime - objOriginalJob.getArrivalTime());
					objOriginalJob.setWaitingTime(objOriginalJob.getTurnAroundTime() - objOriginalJob.getBurstTime());
					
					break;
				}//close if
			}//close for
		}//close if
		
		/*add to the track list*/
		try{
				
			if(objJob.getJobNumber() == arlistTrack.get(arlistTrack.size() - 1).getJobNumber()){
				
				Job objPrevious = arlistTrack.get(arlistTrack.size() - 1);
				objPrevious.setBurstTime(objPrevious.getBurstTime() + dblBurst);
			
			}else{
				
				Job objCloneJob = new Job();
				objCloneJob.setJobNumber(objJob.getJobNumber());
				objCloneJob.setBurstTime(dblBurst);
				arlistTrack.add(objCloneJob);

			}//close else
		}catch(IndexOutOfBoundsException objIndexException){
			
			Job objCloneJob = new Job();
			objCloneJob.setJobNumber(objJob.getJobNumber());
			objCloneJob.setBurstTime(dblBurst);
			arlistTrack.add(objCloneJob);
		}//close catch

		return(dblProcessTime);
	}//close processedJob
	
	private Job getNextJobTime(Job objJob, double dblProcessTime)throws IndexOutOfBoundsException{
		
		/*get the possible next job*/
		ArrayList<Job> arlistPossible = new ArrayList<Job>();
		
		for(Job objPossibleJob: arlistJobTemp){
			
			if(objPossibleJob != objJob && objPossibleJob.getArrivalTime() > dblProcessTime){
				
				arlistPossible.add(objPossibleJob);
			}//close if
		}//close for
		
		Job objSmallest = arlistPossible.get(0);
		
		/*select the job by arrival time*/
		for(Job objNextJob: arlistPossible){
			
			if(objNextJob.getArrivalTime() < objSmallest.getArrivalTime()){
				
				objSmallest = objNextJob;
			}//close if
		}//close for
		
		return(objSmallest);
	}//close getNextJobTime()
	
	/*return the result of the computation*/
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
}//close PPrio