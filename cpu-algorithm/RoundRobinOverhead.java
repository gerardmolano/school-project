
import java.util.ArrayList;
import java.util.ArrayDeque;

public class RoundRobinOverhead implements CpuAlgorithm{
	
	/*contains the list of jobs*/
	ArrayList<Job> arlistJob;
	
	/*copy of the original list of jobs*/
	ArrayList<Job> arlistTemp;
	
	/*contains the track of jobs*/
	ArrayList<Job> arlistTrack;
	
	/*contains the timeline of the process*/
	ArrayDeque<Job> queueJob;
	
	/*contains the number of quantum*/
	double dblQuantum;
	
	/*contains  the number of overhead*/
	double dblOverhead;
	
	/*contains the the total burst time*/
	double dblTotalBurstTime;
	
	/*this method is applicable only in rr and rro*/
	public void quantumOverhead(double dblQuantum, double dblOverhead){
		
		this.dblQuantum = dblQuantum;
		this.dblOverhead = dblOverhead;
		
		return;
	}//close quantumOverhead
	
	/*controls the processing*/
	public void run(ArrayList<Job> arlistJob, double dblTotalBurstTime){
		
		this.arlistJob = arlistJob;
		
		this.dblTotalBurstTime = dblTotalBurstTime;
		
		/*track of the jobs*/
		arlistTrack = new ArrayList<Job>();
		
		queueJob = new ArrayDeque<Job>(20);
		
		/*make a copy*/
		arlistTemp = new ArrayList<Job>();
		
		for(Job objJob: arlistJob){
			
			Job objCopy = new Job();
			
			objCopy.setJobNumber(objJob.getJobNumber());
			objCopy.setArrivalTime(objJob.getArrivalTime());
			objCopy.setBurstTime(objJob.getBurstTime());
			
			arlistTemp.add(objCopy);
		}//close for
		
		double dblProcessTime = 0d;
		
		while(dblProcessTime < this.dblTotalBurstTime){
			
			Job objJob = getNextJob(dblProcessTime);
			
			/*idle*/
			if(objJob.getArrivalTime() > dblProcessTime){
				
				/*Idle representation*/
				Job objIdle = new Job();
				objIdle.setJobNumber(-1);
				objIdle.setBurstTime(objJob.getArrivalTime() - dblProcessTime);
				this.dblTotalBurstTime += objJob.getArrivalTime() - dblProcessTime;
				dblProcessTime += (objJob.getArrivalTime() - dblProcessTime);
				
				/*add the idle to the track list*/
				arlistTrack.add(objIdle);
			}//close if
			
			dblProcessTime = processJob(objJob, dblProcessTime);
			
		}//close while
		
		return;
	}//close run
	
	private Job getNextJob(double dblProcessTime){
		
		Job objNextJob = null;
		
		/*put the jobs to the queue, according to their arrival time*/
		for(Job objJob: arlistTemp){
			
			/*if the job is already in the queue, no need to enqueue the job*/
			if(!(queueJob.contains(objJob)) && objJob.getArrivalTime() <= dblProcessTime){
			
				queueJob.add(objJob);
			}//close if
		}//close for
		
		objNextJob = queueJob.poll();
		
		/*if there's no job in the queue but there's remaining job, find the next job by arrival time*/
		if(objNextJob == null){
			
			Job objSmallest = arlistTemp.get(0);
			for(Job objJob: arlistTemp){
				
				if(objJob.getArrivalTime() < objSmallest.getArrivalTime()){
					
					objSmallest = objJob;
				}else if(objJob.getJobNumber() < objSmallest.getJobNumber()){
					
					objSmallest  = objJob;
				}//close if
			}//close for
			
			objNextJob = objSmallest;
		}//close if
		
		return(objNextJob);
	}//close getNextJob()
	
	private double processJob(Job objJob, double dblProcessTime){
		
		double dblBurst = 0d;	
		
		if(objJob.getBurstTime() <= dblQuantum){
			
			dblBurst = objJob.getBurstTime();
			objJob.setBurstTime(0d);
		
		}else{
			
			dblBurst = dblQuantum;
			objJob.setBurstTime(objJob.getBurstTime() - dblQuantum);
		
		}//close else		
			
		/*update the process time*/
		dblProcessTime += dblBurst;
			
		if(objJob.getBurstTime() == 0d){
			
			/*remove the job*/
			arlistTemp.remove(objJob);
			
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
		Job objClone = new Job();
		objClone.setJobNumber(objJob.getJobNumber());
		objClone.setBurstTime(dblBurst);
		arlistTrack.add(objClone);
		
		int intPreviousSize = queueJob.size();
		
		/*get jobs arrived, without the presence of the overhead*/
		getPossibleNextJobs(objJob, dblProcessTime);
		
		/*put the job again to the queue if it has a remaing burst time*/
		if(!(objJob.getBurstTime() == 0d)){
	
			queueJob.add(objJob);
	
		}//close if
			
		/*add overhead if the overhead is not equalst 0d*/
		if(!arlistTemp.isEmpty()){
			
			/*Overhead representation*/
			Job objOverhead = new Job();
			objOverhead.setJobNumber(-2);
			objOverhead.setBurstTime(dblOverhead);
			arlistTrack.add(objOverhead);
			this.dblTotalBurstTime += dblOverhead;
			dblProcessTime += dblOverhead;
			
		}//close if
		getPossibleNextJobs(objJob, dblProcessTime);
		
		return(dblProcessTime);
	}//close processJob()
	
	
	/*put the possible next jobs to the queue*/
	public void getPossibleNextJobs(Job objJob, double dblProcessTime){
		
		for(Job objNextJob: arlistTemp){
			
			if(objNextJob != objJob && objNextJob.getArrivalTime() <= dblProcessTime && !(queueJob.contains(objNextJob))){
				
				queueJob.add(objNextJob);
			
			}//close if
		}//close for
		
		return;
	}//close getPossibleNextJob()
	
	/*return the result of the computation*/
	public ArrayList<Job> getResultCompute(){
		
		return(arlistJob);
	}//close getResultCompute()
	
	/*return the track of the result*/
	public ArrayList<Job> getResultTrack(){
		
		return(arlistTrack);
	}//close getResulTrack()
}//close RoundRobin