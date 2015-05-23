
/**
 * First comes first served scheduler.
 */
 
import java.util.*;
import java.io.*;

public class FCFSScheduler implements Scheduler {

	private PriorityQueue<Process> arrivalQueue;
	private Queue<Process> finishedQueue;
	private int systemTime, totalWaitTime, totalTurnaroundTime;

	
	public FCFSScheduler()
	{
		arrivalQueue = new PriorityQueue<Process>(10, new ProcessArrivalTimeComparator());
		finishedQueue = new LinkedList<Process>();
		systemTime = 0;
		totalWaitTime = 0;
		totalTurnaroundTime = 0;
	}
	
	private void fillArrivalQueue(String inputFile) throws IOException
	{
			 BufferedReader input = new BufferedReader(new FileReader(inputFile));
			while(input.ready())
				arrivalQueue.add(new Process(input.readLine()));
			input.close();		
	}
	
	private void runSimulation()
	{
		Process currentProcess;
		while((currentProcess = arrivalQueue.poll()) != null)
		{	
			if(systemTime < currentProcess.getArrivalTime())
				systemTime  = currentProcess.getArrivalTime();
			systemTime = currentProcess.runFor(systemTime, currentProcess.getRemainingBurstTime());
			if(currentProcess.isFinished())
			{
				finishedQueue.add(currentProcess);
				totalWaitTime += currentProcess.getCurrentWaitTime();
				totalTurnaroundTime += currentProcess.getTurnAroundTime();				
			}
				
		}
	}

	private void writeResults(String outputFile) throws IOException
	{
		int aveWaitTime = totalWaitTime/finishedQueue.size();
		int aveTurnaroundTime = totalTurnaroundTime/finishedQueue.size();
		StringBuilder builder = new StringBuilder();
		Process currentProcess;
		
		PrintWriter outputter = new PrintWriter(outputFile); 
		while((currentProcess = finishedQueue.poll()) != null)
		{
			builder.append(currentProcess.output());
		}
		builder.append(new String(aveWaitTime + " " + aveTurnaroundTime));

		outputter.print(builder.toString());
		outputter.close();
	}

  @Override
  public void schedule(String inputFile, String outputFile) 
  {
    // implement your scheduler here.
    // write the scheduler output to {@code outputFile}
	try
	{
		fillArrivalQueue(inputFile);
		runSimulation();
		writeResults(outputFile);
	}	 		
	catch (Exception e) 
	{
		System.out.println(e.getMessage());
	}

  }
}
