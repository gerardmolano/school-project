
import java.util.ArrayList;

public class Srtf implements CpuAlgorithm{
	
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
		
		Job objJobSmallest = null;
		
		ArrayList<Job> arlistProcessJob = new ArrayList<Job>();
		
		/*get the job the available to process*/
		for(Job objJob: arlistJobTemp){
			
			if(objJob.getArrivalTime() <= dblProcessTime){
				
				arlistProcessJob.add(objJob);
			}//close if
		}//close for
		
		/*select the job that has a smallest burst*/
		if(!arlistProcessJob.isEmpty()){
			
			objJobSmallest = arlistProcessJob.get(0);
			
			for(Job objJob: arlistProcessJob){
				
				if(objJob.getBurstTime() < objJobSmallest.getBurstTime()){
					
					objJobSmallest = objJob;
				}else if(objJob.getBurstTime() == objJobSmallest.getBurstTime()){
					
					try{
						
						if(objJobSmallest != arlistTrack.get(arlistTrack.size() - 1)){
							
							if(objJob.getArrivalTime() < objJobSmallest.getArrivalTime()){
								
								objJobSmallest = objJob;
							}else if(objJob.getArrivalTime() == objJobSmallest.getArrivalTime()){
								
								if(objJob.getJobNumber() < objJobSmallest.getJobNumber()){
									
									objJobSmallest = objJob;
								}//if
							}//clsoe if
						}//close if
					}catch(IndexOutOfBoundsException objIndexException){
						
						if(objJob.getArrivalTime() < objJobSmallest.getArrivalTime()){
							
							objJobSmallest = objJob;
						}else if(objJob.getArrivalTime() == objJob.getArrivalTime()){
							
							if(objJob.getJobNumber() < objJobSmallest.getJobNumber()){
								
								objJobSmallest = objJob;
							}//close if
						}//close if
					}//close catch
				}//close if
			}//close for
			
		/*if there's no job arrived, select a job by arrival time*/
		}else{
			
			objJobSmallest = arlistJobTemp.get(0);
			
			for(Job objJob: arlistJobTemp){
				
				if(objJob.getArrivalTime() < objJobSmallest.getArrivalTime()){
					
					objJobSmallest = objJob;
				}else if(objJob.getArrivalTime() == objJobSmallest.getArrivalTime()){
					
					if(objJob.getBurstTime() < objJobSmallest.getBurstTime()){
						
						objJobSmallest = objJob;
					}else if(objJob.getBurstTime() == objJobSmallest.getBurstTime()){
						
						if(objJob.getArrivalTime() < objJobSmallest.getArrivalTime()){
							
							objJobSmallest  = objJob;
						}//close if
					}//close if
				}//close if
			}//close for
		}//close else
			
		return(objJobSmallest);
	}//close getNextJob()

	private double processedJob(Job objJob, double dblProcessTime){
		
		double dblBurst = 0d;
		
		try{
			
			Job objNextJob = getNextJobTime(objJob, dblProcessTime);
			
			if((objNextJob.getArrivalTime() - dblProcessTime) >= objJob.getBurstTime()){
			
				dblProcessTime += objJob.getBurstTime();
				dblBurst = objJob.getBurstTime();
				objJob.setBurstTime(0d);
				arlistJobTemp.remove(objJob);
			
			}else{
			
				objJob.setBurstTime(objJob.getBurstTime() - (objNextJob.getArrivalTime() - dblProcessTime));
				dblBurst =  objNextJob.getArrivalTime() - dblProcessTime;
				dblProcessTime += (objNextJob.getArrivalTime() - dblProcessTime);
				
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
		
		/*add to the tracklist*/
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
	}//clse processedJob
	
	/*get the next possible job arrival time (but greater than the process)*/
	private Job getNextJobTime(Job objJob, double dblProcessTime)throws IndexOutOfBoundsException{
		
		/*get the possible jobs that not equals the passed job*/
		ArrayList<Job> arlistPossibleJobs = new ArrayList<Job>();
		
		for(Job objJobPossible: arlistJobTemp){
			
			if(objJobPossible != objJob && objJobPossible.getArrivalTime() > dblProcessTime){
				
				arlistPossibleJobs.add(objJobPossible);
			}//close if
		}//close for
	
		
		/*get the possible next job time*/
		Job objSmallest = arlistPossibleJobs.get(0);
		
		for(Job objJobPossible: arlistPossibleJobs){
			
			if(objJobPossible.getArrivalTime() < objSmallest.getArrivalTime()){
				
				objSmallest = objJobPossible;
			}//close if
		}//close for
		
		return (objSmallest);
	}//close getNextJobTime
	
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
}//close Srtf