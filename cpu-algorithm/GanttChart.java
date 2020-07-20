
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.Color;

import java.util.ArrayList;

import javax.swing.Box;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

public class GanttChart{
	
	private ArrayList<Job> arlistJob;
	private JPanel pnlGanttChart;
	private double dblProcessTime;
	
	public void drawGanttChart(ArrayList<Job> arlistJob){
		
		this.arlistJob = arlistJob;
		
		dblProcessTime = 0d;
		
		pnlGanttChart = new JPanel(new GridLayout(1, arlistJob.size(), 0, 0));
	
		for(Job objJob: arlistJob){
			
			pnlGanttChart.add(pnlPerJob(objJob));
		}//close for
		
		return;
	}//close drawGanttChart()
	
	/*make a box per job*/
	private Box pnlPerJob(Job objJob){
		
		Box bxJob = Box.createVerticalBox();
		
		/*for time proces*/
		JPanel pnlTime = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		
		dblProcessTime += objJob.getBurstTime();
		pnlTime.add(new JLabel(Double.toString(dblProcessTime), SwingConstants.RIGHT));
		
		/*for job*/
		Box bxPerJob = Box.createVerticalBox();
		
		/*job number, idle, and overhead*/
		JPanel pnlJobNumber = new JPanel(new FlowLayout(FlowLayout.LEFT));
		if(objJob.getJobNumber() != -1 && objJob.getJobNumber() != -2){
			
			pnlJobNumber.add(new JLabel("Job " + Integer.toString(objJob.getJobNumber()), SwingConstants.RIGHT));
		}else if(objJob.getJobNumber() == -1){
			
			pnlJobNumber.add(new JLabel("Idle", SwingConstants.RIGHT));
			bxPerJob.setBackground(Color.RED);
			
		}else if(objJob.getJobNumber() == -2){
			
			pnlJobNumber.add(new JLabel("Overhead", SwingConstants.RIGHT));
			bxPerJob.setBackground(Color.YELLOW);
		}//close if
		
		bxPerJob.add(pnlTime);
		
		/*burst time*/
		JPanel pnlBurstTime = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		pnlBurstTime.add(new JLabel(Double.toString(objJob.getBurstTime()), SwingConstants.RIGHT));
		
		bxPerJob.add(pnlJobNumber);
		bxPerJob.add(pnlBurstTime);
		bxPerJob.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		
		bxJob.add(bxPerJob);
		bxJob.add(pnlTime);
		
		return(bxJob);
	}//close pnlPerJob()
	
	/*return the gantt chart ui*/
	public JPanel getGanttChart(){
		
		return(pnlGanttChart);
	}//close getGanttChart()
}//close GanttChart