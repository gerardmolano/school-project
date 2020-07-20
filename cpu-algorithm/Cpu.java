
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Font;
import java.awt.Dimension;

import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.UIManager;
import javax.swing.SwingConstants;

import java.util.ArrayList;

public class Cpu extends JFrame{
	
	private JComboBox<String> cmbCpuAlgo;
	private JSpinner spnJobs;
	private JTextField tfQuantum;
	private JTextField tfOverhead;
	private JPanel pnlJobs;
	private JPanel pnlGanttChart;
	
	/*hold the all text field per column*/
	ArrayList<JTextField> arlistArrTime;
	ArrayList<JTextField> arlistBurstTime;
	ArrayList<JTextField> arlistPrioDeadline;
	ArrayList<JTextField> arlistTurnArTime;
	ArrayList<JTextField> arlistWaitingTime;
	ArrayList<JTextField> arlistAve;
	
	/*contains the the all jobs information*/
	ArrayList<Job> arlistJob = new ArrayList<Job>();
	
	/*contains the object of the cpu algorithm class*/
	CpuAlgorithm[] objCpu = new CpuAlgorithm[8];
	
	/*object that draw a gantt chart*/
	GanttChart objGanttChart = new GanttChart();
	
	private static final int FIELDCOLUMN = 10;
	private static final int MAXJOB = 10;
	private static final int WIDTH = 1120;
	private static final int HEIGHT = 310;
	
	public static void main(String[] args){
		
		SwingUtilities.invokeLater(new Runnable(){
			
			public void run(){
				
				try{
					
					UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
					//UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				}catch(Exception objException){
					
					objException.printStackTrace();
				}//close catch
				
				new Cpu();
			}//close run()
		});
	}//clse main
	
	public Cpu(){
		
		getCpuAlgo();
		
		this.add(commandPanel(), BorderLayout.WEST);
		this.add(pnlInput(), BorderLayout.CENTER);
		
		/*for gantt char*/
		pnlGanttChart = new JPanel();
		pnlGanttChart.setLayout(new BoxLayout(pnlGanttChart, BoxLayout.X_AXIS));
		this.add(new JScrollPane(pnlGanttChart), BorderLayout.SOUTH);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(WIDTH, HEIGHT);
		this.setLocationRelativeTo(null );
		this.setVisible(true );
		
	}//clse Cpu constructor
	
	/*get the Cpu Algorithm object*/
	public void getCpuAlgo(){
		
		objCpu[0] = new Fcfs();
		objCpu[1] = new Sjf();
		objCpu[2] = new Priority();
		objCpu[3] = new Deadline();
		objCpu[4] = new PPrio();
		objCpu[5] = new Srtf();
		objCpu[6] = new RoundRobin();
		objCpu[7] = new RoundRobinOverhead();
		
		return;
	}//close getCpuAlgo()
	
	/*ui for command panel*/
	public JPanel commandPanel(){
		

		String[] arrCpuAlgo = new String[] {"FCFS", "SJF", "Priority", "Earliest Deadline", "P-Prio", "SRTF", "RR", "RRo"};
		JPanel pnlCommand = new JPanel();
		JButton btnProcess = new JButton("Process");
		
		btnProcess.addActionListener(new ProcessHandler());	
		
		pnlCommand.setLayout(new BoxLayout(pnlCommand, BoxLayout.Y_AXIS));
		
		cmbCpuAlgo = new JComboBox<String>(arrCpuAlgo); //list of cpu algorithm
		cmbCpuAlgo.addItemListener(new DisplayHandler());
		spnJobs = new JSpinner(new SpinnerNumberModel(1, 1, MAXJOB, 1)); //number of jobs
		tfQuantum = new JTextField(); //number of quantum
		tfOverhead = new JTextField(); //number of overhead
		
		tfQuantum.setEnabled(false );
		tfOverhead.setEnabled(false );
		
		spnJobs.addChangeListener(new NumberJobsHandler());
	
		JPanel pnlOtherCom = new JPanel(new GridLayout(3, 2, 3, 3));
		pnlOtherCom.add(new JLabel("Jobs "));
		pnlOtherCom.add(spnJobs);
		pnlOtherCom.add(new JLabel("Quantum "));
		pnlOtherCom.add(tfQuantum);
		pnlOtherCom.add(new JLabel("Overhead"));
		pnlOtherCom.add(tfOverhead);
		
		JPanel pnlProcess = new JPanel();
		pnlProcess.add(btnProcess);
		
		pnlCommand.add(cmbCpuAlgo);
		pnlCommand.add(pnlOtherCom);
		pnlCommand.add(pnlProcess);
		
		JPanel pnlCommand2 = new JPanel();
		pnlCommand2.setBorder(new TitledBorder(""));
		pnlCommand2.add(pnlCommand);
		
		return(pnlCommand2);
	}//close commandPanel()

	/*ui for input or display*/
	public JPanel pnlInput(){
		
		String[] arrLabel = new String[] {"Job", "Arrival Time", "Burst Time", "Priority or Deadline", "Turnaround Time", "Waiting Time"};
		JPanel pnlMain = new JPanel(new BorderLayout());
		
		pnlJobs = new JPanel(new GridLayout(4, 6, 10, 5));
		
		/*label*/
		JPanel pnlLabel = new JPanel();
		pnlLabel.setBorder(new TitledBorder(""));
		for(String strLabel: arrLabel){
			
			JLabel lblCpu = new JLabel(strLabel, SwingConstants.CENTER);
			lblCpu.setFont(new Font(lblCpu.getFont().getFontName(), Font.BOLD, 15));
			pnlJobs.add(lblCpu);
		}//close for
		
		/*jobs field*/
		JPanel pnlContainJob = new JPanel();
		arlistArrTime = new ArrayList<JTextField>();
		arlistBurstTime = new ArrayList<JTextField>();
		arlistPrioDeadline = new ArrayList<JTextField>();
		arlistTurnArTime = new ArrayList<JTextField>();
		arlistWaitingTime = new ArrayList<JTextField>();
		
		for(int intCounter = 0; intCounter < 1; intCounter++){
			
			pnlJobs.add(new JLabel("Job " + (intCounter + 1)));
			
			JTextField tfArrTime = new JTextField(FIELDCOLUMN);
			JTextField tfBurstTime = new JTextField(FIELDCOLUMN);
			JTextField tfPrioDeadline = new JTextField(FIELDCOLUMN);
			JTextField tfTurnArTime = new JTextField(FIELDCOLUMN);
			JTextField tfWaitingTime = new JTextField(FIELDCOLUMN);
			
			tfPrioDeadline.setEnabled(false );
			tfTurnArTime.setEditable(false );
			tfWaitingTime.setEditable(false );

			arlistArrTime.add(tfArrTime);
			arlistBurstTime.add(tfBurstTime);
			arlistPrioDeadline.add(tfPrioDeadline);
			arlistTurnArTime.add(tfTurnArTime);
			arlistWaitingTime.add(tfWaitingTime);
			
			pnlJobs.add(tfArrTime);
			pnlJobs.add(tfBurstTime);
			pnlJobs.add(tfPrioDeadline);
			pnlJobs.add(tfTurnArTime);
			pnlJobs.add(tfWaitingTime);
		}//close for
		
		/*display the tt ave and wt ave*/
		arlistAve = new ArrayList<JTextField>();
		for(int intCounter = 0; intCounter < 4; intCounter++){
			
			pnlJobs.add(new JLabel(""));
		}//close for
		JLabel lblTtAve = new JLabel("Turnaround Time Average");
		lblTtAve.setFont(new Font(lblTtAve.getFont().getFontName(), Font.BOLD, 12));
		JLabel lblWtAve = new JLabel("Waiting Time Average");
		lblWtAve.setFont(new Font(lblTtAve.getFont().getFontName(), Font.BOLD, 12));
		pnlJobs.add(lblTtAve);
		pnlJobs.add(lblWtAve);
		for(int intCounter = 0; intCounter < 4; intCounter++){
			
			pnlJobs.add(new JLabel(""));
		}//close for
		JTextField tfTurnarround = new JTextField();
		JTextField tfWaiting = new JTextField();
		tfTurnarround.setEditable(false );
		tfWaiting.setEditable(false );
		arlistAve.add(tfTurnarround);
		arlistAve.add(tfWaiting);
		pnlJobs.add(arlistAve.get(0));
		pnlJobs.add(arlistAve.get(1));
		
		pnlContainJob.add(pnlJobs);
		
		pnlMain.add(pnlLabel, BorderLayout.NORTH);
		pnlMain.add(pnlContainJob, BorderLayout.CENTER);
		
		return (pnlMain);
	}//close pnlInput()
	
	/*get the inputs make it a Job object*/
	public void getJobs(){
		
		arlistJob.clear();
		for(int intCounter = 0; intCounter < (Integer) spnJobs.getValue(); intCounter++){
			
			Job objNewJob = new Job();
			
			objNewJob.setJobNumber(intCounter + 1);
			objNewJob.setArrivalTime(Double.parseDouble(arlistArrTime.get(intCounter).getText()));
			objNewJob.setBurstTime(Double.parseDouble(arlistBurstTime.get(intCounter).getText()));
			
			/*if the deadline field is empty, make the deadline of the job to be 0*/
			try{
				
				objNewJob.setPriorityDeadline(Double.parseDouble(arlistPrioDeadline.get(intCounter).getText()));
				
			}catch(NumberFormatException objNumberException){
				
				objNewJob.setPriorityDeadline(0d);
			}//close catch
			
			arlistJob.add(objNewJob);
		}//close for
		
		return;
	}//close getJobs
	
	/*return the total burst time of all jobs entered*/
	public double getTotalBurstTime(){
		
		double dblTotalBurstTime = 0d;
		for(Job objJob: arlistJob){
			
			dblTotalBurstTime += objJob.getBurstTime();
		}//close for
		
		return(dblTotalBurstTime);
	}//close getTotalBurstTime()
	
	/*check if all text field has a input*/
	public void checkInputs()throws VerifyError{
		
		for(JTextField textfield: arlistArrTime){
			
			if(textfield.getText().isEmpty()){
				
				throw new VerifyError();
			}//close if
		}//close for
		
		for(JTextField textfield: arlistBurstTime){
			
			if(textfield.getText().isEmpty()){
				
				throw new VerifyError();
			}//close if
		}//close for
		
		if(((String ) cmbCpuAlgo.getSelectedItem()).equals("RR") || ((String ) cmbCpuAlgo.getSelectedItem()).equals("RRo")){
			
			if(tfQuantum.getText().isEmpty()){
				
				throw new VerifyError();
			}//close if
			
			if(tfOverhead.getText().isEmpty() && ((String ) cmbCpuAlgo.getSelectedItem()).equals("RRo")){
				
				throw new VerifyError();
			}//close if
		}//close if
		
		return;
	}//close checkInputs()
	
	/*process event handler*/
	private class ProcessHandler implements ActionListener{
		
		public void actionPerformed(ActionEvent objEvent){
			
			double dblTotalTurnAround = 0.0;
			double dblTotalWaitingTime = 0.0;
			
			
			try{
				
				/*check if the all required inputs has a value*/
				checkInputs();
				
				getJobs();
			
				if(((String) cmbCpuAlgo.getSelectedItem()).equals("RR") || ((String) cmbCpuAlgo.getSelectedItem()).equals("RRo")){
					
					try{
					
						objCpu[cmbCpuAlgo.getSelectedIndex()].quantumOverhead(Double.parseDouble(tfQuantum.getText()), Double.parseDouble(tfOverhead.getText()));
				
					}catch(NumberFormatException objFormatException){
					
						objCpu[cmbCpuAlgo.getSelectedIndex()].quantumOverhead(Double.parseDouble(tfQuantum.getText()), 0);
				
					}//close catch
				}//closeif
			
			
				objCpu[cmbCpuAlgo.getSelectedIndex()].run(arlistJob, getTotalBurstTime());
			
				/*display the turnaround time of each job and waiting time*/
				int intCounter = 0;
				for(Job objJob: objCpu[cmbCpuAlgo.getSelectedIndex()].getResultCompute()){
				
					arlistTurnArTime.get(intCounter).setText(Double.toString(objJob.getTurnAroundTime()));
					dblTotalTurnAround += objJob.getTurnAroundTime();
					arlistWaitingTime.get(intCounter).setText(Double.toString(objJob.getWaitingTime()));
					dblTotalWaitingTime += objJob.getWaitingTime();
				
					intCounter++;
				}//close for
			
				arlistAve.get(0).setText(Double.toString(dblTotalTurnAround / ((Integer) spnJobs.getValue()).doubleValue())); //display the average of turn around time
				arlistAve.get(1).setText(Double.toString(dblTotalWaitingTime / ((Integer) spnJobs.getValue()).doubleValue())); //display the average of waiting time
			
				pnlGanttChart.removeAll();
				objGanttChart.drawGanttChart(objCpu[cmbCpuAlgo.getSelectedIndex()].getResultTrack());
			
				pnlGanttChart.add(objGanttChart.getGanttChart());
			
				pnlGanttChart.repaint();
				pnlGanttChart.revalidate();
			
				Cpu.this.revalidate();
			
				
			}catch(VerifyError objVerifyError){
				
				JOptionPane.showMessageDialog(Cpu.this, "Complete the required inputs", "Cpu", JOptionPane.WARNING_MESSAGE);
			}//close catch
			
			return;
		}//close actionPerformed()
	}//close ProcessHandler
	
	/*handle the number of jobs to display*/
	private class NumberJobsHandler implements ChangeListener{
		
		public void stateChanged(ChangeEvent objEvent){
			
			String[] arrLabel = new String[] {"Job", "Arrival Time", "Burst Time", "Priority or Deadline", "Turnaround Time", "Waiting Time"};
			
			pnlJobs.removeAll();
			pnlJobs.setLayout(new GridLayout((Integer) spnJobs.getValue() + 3, 6, 10, 5));
			
			/*display again the label*/
			for(String strLabel: arrLabel){
			
				JLabel lblCpu = new JLabel(strLabel, SwingConstants.CENTER);
				lblCpu.setFont(new Font(lblCpu.getFont().getFontName(), Font.BOLD, 15));
				pnlJobs.add(lblCpu);
			}//close for
			
			/*remove other text fields*/
			for(int intCounter = arlistArrTime.size(); intCounter > (Integer) spnJobs.getValue(); intCounter--){
				
				arlistArrTime.remove(intCounter - 1);
				arlistBurstTime.remove(intCounter - 1);
				arlistPrioDeadline.remove(intCounter - 1);
				arlistTurnArTime.remove(intCounter - 1);
				arlistWaitingTime.remove(intCounter - 1);
				
			}//close for
			
			/*remain the other text fields if necessary*/
			for(int intCounter = 0; intCounter < arlistArrTime.size(); intCounter++){
				
				pnlJobs.add(new JLabel("Job" + (intCounter + 1)));
				pnlJobs.add(arlistArrTime.get(intCounter));
				pnlJobs.add(arlistBurstTime.get(intCounter));
				pnlJobs.add(arlistPrioDeadline.get(intCounter));
				pnlJobs.add(arlistTurnArTime.get(intCounter));
				pnlJobs.add(arlistWaitingTime.get(intCounter));
				
			}//close for
			
			/*display the new field depend on the number of job*/
			for(int intCounter = 0; intCounter < (Integer) spnJobs.getValue() - arlistArrTime.size(); intCounter++){
				
				pnlJobs.add(new JLabel("Job " + (arlistArrTime.size() + intCounter + 1)));
			
				JTextField tfArrTime = new JTextField(FIELDCOLUMN);
				JTextField tfBurstTime = new JTextField(FIELDCOLUMN);
				JTextField tfPrioDeadline = new JTextField(FIELDCOLUMN);
				JTextField tfTurnArTime = new JTextField(FIELDCOLUMN);
				JTextField tfWaitingTime = new JTextField(FIELDCOLUMN);
				
				if(!(arlistPrioDeadline.get(0).isEnabled())){
					
					tfPrioDeadline.setEnabled(false );
				}//close if
				
				tfTurnArTime.setEditable(false );
				tfWaitingTime.setEditable(false );
			
				arlistArrTime.add(tfArrTime);
				arlistBurstTime.add(tfBurstTime);
				arlistPrioDeadline.add(tfPrioDeadline);
				arlistTurnArTime.add(tfTurnArTime);
				arlistWaitingTime.add(tfWaitingTime);
			
				pnlJobs.add(tfArrTime);
				pnlJobs.add(tfBurstTime);
				pnlJobs.add(tfPrioDeadline);
				pnlJobs.add(tfTurnArTime);
				pnlJobs.add(tfWaitingTime);
			}//close for
			
			/*display again the tt ave field and wt ave field*/
			for(int intCounter = 0; intCounter < 4; intCounter++){
			
				pnlJobs.add(new JLabel(""));
			}//close for
			JLabel lblTtAve = new JLabel("Turnaround Time Average");
			lblTtAve.setFont(new Font(lblTtAve.getFont().getFontName(), Font.BOLD, 12));
			JLabel lblWtAve = new JLabel("Waiting Time Average");
			lblWtAve.setFont(new Font(lblTtAve.getFont().getFontName(), Font.BOLD, 12));
			pnlJobs.add(lblTtAve);
			pnlJobs.add(lblWtAve);
			for(int intCounter = 0; intCounter < 4; intCounter++){
			
				pnlJobs.add(new JLabel(""));
			}//close for
			pnlJobs.add(arlistAve.get(0)); //tt ave
			pnlJobs.add(arlistAve.get(1)); //wt ave
			
			pnlJobs.repaint();
			pnlJobs.revalidate();
			/*resize the screen*/
			setSize(WIDTH, ((Double) pnlJobs.getPreferredSize().getHeight()).intValue() + 170);
		
			return;
		}//close stateChange()
	}//close NumberJobsHandler

	/*handle the changes of the cpu algorithm combo box*/
	private class DisplayHandler implements ItemListener{
		
		public void itemStateChanged(ItemEvent objEvent){
			
			if(objEvent.getStateChange() == ItemEvent.SELECTED){
				
				String strCpuAlgo = (String )((JComboBox) objEvent.getSource()).getSelectedItem();
				
				if(strCpuAlgo.equals("Priority") || strCpuAlgo.equals("Earliest Deadline") || strCpuAlgo.equals("P-Prio")){
					
					for(JTextField textfield: arlistPrioDeadline){
						
						textfield.setEnabled(true );
					}//close for
				}else{
					
					for(JTextField textfield: arlistPrioDeadline){
						
						textfield.setEnabled(false );
					}//close for
					
				}//close else
					
				/*prompt the user*/
				if(strCpuAlgo.equals("Earliest Deadline")){
					
					JOptionPane.showMessageDialog(Cpu.this, "To the Job that has no Deadline just leave it blank");
					
				}//close if
					
				if(strCpuAlgo.equals("RR") || strCpuAlgo.equals("RRo")){
					
					tfQuantum.setEnabled(true );
					tfOverhead.setEnabled(true );
				}else{
					
					tfQuantum.setEnabled(false );
					tfOverhead.setEnabled(false );
				}//close else
			}//close if
		}//close itemStateChanged()
	}//close DisplayHandler
}//close Cpu