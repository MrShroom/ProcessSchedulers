/***
*	@author Mr.Shroom
*	Created by: Shaun McThomas
*	Last Modified:     06/10/15
*
 * Shortest remaining time first scheduler
 */
 
import java.util.*;
import java.io.*;
 
public class SRTFScheduler implements Scheduler 
{
        private static final boolean DEBUG_MODE = false;
        private PriorityQueue<Process> arrivalQueue, readyQueue,finishedQueue;
        private int systemTime, totalWaitTime, totalTurnaroundTime;

        public SRTFScheduler()
        {
                arrivalQueue = new PriorityQueue<Process>(10, new ProcessArrivalTimeComparator());
	        readyQueue = new PriorityQueue<Process>(10, new ProcessRemainingTimeComparator());
	        finishedQueue =  new PriorityQueue<Process>(10, new ProcessIDComparator());
		systemTime = 0;
		totalWaitTime = 0;
		totalTurnaroundTime = 0;
	}
	
	private void fillArrivalQueue(String inputFile) throws IOException
	{
                if (DEBUG_MODE)
                        System.out.println("file: " + inputFile);
                        
		BufferedReader input = new BufferedReader(new FileReader(inputFile));
		while(input.ready())
        		arrivalQueue.add(new Process(input.readLine()));
		input.close();		
	}
	
	private Process nextProcess()
	{        
	        if  (readyQueue.isEmpty() || 
	                (!arrivalQueue.isEmpty() && arrivalQueue.peek().getArrivalTime() <= systemTime ))
	        {
	                int arrived = arrivalQueue.peek().getArrivalTime();	                
	                while(!arrivalQueue.isEmpty() && arrived == arrivalQueue.peek().getArrivalTime())	               
	                        readyQueue.add(arrivalQueue.poll());	                
	        }     
	        
	        return readyQueue.poll();	       
	}
	
	private int figureOutRunTime(Process currentProcess)
	{
	        int minTime = currentProcess.getRemainingBurstTime();	        
	                
	        if (!arrivalQueue.isEmpty())
	                minTime = Math.min(minTime, (arrivalQueue.peek().getArrivalTime() - systemTime));
	                	  	
	        return minTime;
	}
	
	private void runSimulation()
	{
		Process currentProcess;
		int runtime;		

		while(!arrivalQueue.isEmpty() || !readyQueue.isEmpty())
		{	        
		        currentProcess = nextProcess();
		        
		        if (DEBUG_MODE)
		        {
		                System.out.println("\n*********************************************************************");
        		        System.out.println(currentProcess.toString());
	                }
	                	        
		        if(systemTime < currentProcess.getArrivalTime())
				systemTime  = currentProcess.getArrivalTime();
				
		        runtime = figureOutRunTime(currentProcess);
		        
		        if(DEBUG_MODE)
		        {
        		        System.out.println("will run: " + runtime);
        		        pressAnyKeyToContinue();
			}
				
			systemTime = currentProcess.runFor(systemTime, runtime);
			
			if(currentProcess.isFinished())
			{
				finishedQueue.add(currentProcess);
				totalWaitTime += currentProcess.getCurrentWaitTime();
				totalTurnaroundTime += currentProcess.getTurnAroundTime();
							
			}
			else
			        readyQueue.add(currentProcess);			
				
		}
	}
	
	private void writeResults(String outputFile) throws IOException
	{
		long aveWaitTime = Math.round((double)totalWaitTime/finishedQueue.size());
		long aveTurnaroundTime = Math.round((double)totalTurnaroundTime/finishedQueue.size());
		Process currentProcess;
		
		BufferedWriter outputter = new BufferedWriter (new FileWriter(outputFile)); 
		while((currentProcess = finishedQueue.poll()) != null)
		{
			outputter.write(currentProcess.output());
		}
		outputter.write(new String(aveWaitTime + " " + aveTurnaroundTime));

		outputter.close();
	}

        @Override
  	public void schedule(String inputFile, String outputFile) 
	{

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
        
        private void pressAnyKeyToContinue()
        { 
                System.out.println("Press any key to continue...");
                try
                {
                        System.in.read();
                }         
                catch(Exception e)
                {
                }
        }
}
