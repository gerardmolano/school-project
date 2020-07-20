
import java.util.ArrayList;

public interface CpuAlgorithm{
	
	/*for processing*/
	public void run(ArrayList<Job> arlistJob, double dblTotalBurstTime);
	
	/*for round robin*/
	public void quantumOverhead(double dblQuantu, double dblOverhead);
	
	/*return the result of the process*/
	public ArrayList<Job> getResultCompute();
	
	/*return the track of the process for Gantt Chart*/
	public ArrayList<Job> getResultTrack();
	
}//close CpuAlgorithm