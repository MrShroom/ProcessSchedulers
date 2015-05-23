
/**
 * Shortest remaining time first scheduler
 */
 
import java.util.*;
import java.io.*;
 
public class SRTFScheduler implements Scheduler 
{
	private PriorityQueue<Process> arrivalQueue, readyQueue;
	private Queue<Process> finishedQueue;
	private ProcessRemainingTimeComparator timeComparator;
	private int systemTime, totalWaitTime, totalTurnaroundTime;

	public SRTFScheduler()
	{
		arrivalQueue = new PriorityQueue<Process>(10, new ProcessArrivalTimeComparator());
		timeComparator = new ProcessRemainingTimeComparator();
		readyQueue = new PriorityQueue<Process>(10, timeComparator);
		finishedQueue = new LinkedList<Process>();
		systemTime = 0;
		totalWaitTime = 0;
		totalTurnaroundTime = 0;
	}
	

	private void fillArrivalQueue(String inputFile) throws IOException
	{
	                System.out.println("file: " + inputFile);
			 BufferedReader input = new BufferedReader(new FileReader(inputFile));
			while(input.ready())
				arrivalQueue.add(new Process(input.readLine()));
			input.close();		
	}
	
	private Process nextProcess()
	{
	     	if (arrivalQueue.isEmpty())
	                return readyQueue.poll();
	                
	        if  (readyQueue.isEmpty() || 
	                (arrivalQueue.peek().getArrivalTime() <= systemTime 
	                && timeComparator.compare(arrivalQueue.peek(), readyQueue.peek()) < 0))
	        {
	                int arrived = arrivalQueue.peek().getArrivalTime();	                
	                while(!arrivalQueue.isEmpty() && arrived == arrivalQueue.peek().getArrivalTime())
	                {  	                    
	                        readyQueue.add(arrivalQueue.poll());
	                }
	        }
	        
         
	     return readyQueue.poll();	       
	}
	
	private int figureOutRunTime(Process currentProcess)
	{
	        int minTime = currentProcess.getRemainingBurstTime();
	        System.out.println(currentProcess.output());
	                
	        if (!readyQueue.isEmpty() 
	                && minTime >  (arrivalQueue.peek().getArrivalTime() - systemTime))
	                minTime = arrivalQueue.peek().getArrivalTime() - systemTime;
	                	  	
	        return minTime;
	}
	
	private void runSimulation()
	{
		Process currentProcess;
		int runtime;		

		while(!arrivalQueue.isEmpty() || !readyQueue.isEmpty())
		{	        
		        currentProcess = nextProcess();
		        
		        if(systemTime < currentProcess.getArrivalTime())
				systemTime  = currentProcess.getArrivalTime();
		        runtime = figureOutRunTime(currentProcess);
		        
			
				
			systemTime = currentProcess.runFor(systemTime, runtime);
			
			if(currentProcess.isFinished())
			{
				finishedQueue.add(currentProcess);
				totalWaitTime += currentProcess.getCurrentWaitTime();
				totalTurnaroundTime += currentProcess.getTurnAroundTime();
							
			}
			else
			{
			        readyQueue.add(currentProcess);
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
