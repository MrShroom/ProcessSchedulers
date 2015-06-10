/***
*	@author Mr.Shroom
*	Created by: Shaun McThomas
*	Last Modified:     06/10/15
*
 * Proportional share scheduler
 * Take total shares to be 100.
 * A process will not run unless it is completely given the requested share.
 */
 
import java.util.*;
import java.io.*;
 
public class PSScheduler implements Scheduler 
{
        private static final int TOTAL_SHARES = 100;
        private static final boolean DEBUG_MODE = false;
        
        private PriorityQueue<Process> arrivalQueue,finishedQueue;
        private PriorityQueue<Process> readyQueue;
        private LinkedList<Process> runningList;
        private LinkedList<Integer> startSystemTime;
        
        private int systemTime, totalWaitTime, totalTurnaroundTime, curentSharesLeft;
        
        public PSScheduler()
	{
		arrivalQueue = new PriorityQueue<Process>(10, new ProcessArrivalTimeComparator());
		finishedQueue =  new PriorityQueue<Process>(10, new ProcessIDComparator());
		readyQueue =  new PriorityQueue<Process>(10, new ProcessIDComparator());
		runningList = new LinkedList<Process>();
		startSystemTime = new LinkedList<Integer>();
		systemTime = 0;
		totalWaitTime = 0;
		totalTurnaroundTime = 0;
		curentSharesLeft = TOTAL_SHARES;
	}
	
	private void fillArrivalQueue(String inputFile) throws IOException
	{
	       if (DEBUG_MODE)
                        System.out.println("file: " + inputFile + "\n***************************************");
                        
		BufferedReader input = new BufferedReader(new FileReader(inputFile));
		
		while(input.ready())
			arrivalQueue.add(new Process(input.readLine()));
		
		input.close();		
	}
	
	private void fillReadyQueue(int tempSystemTime)
	{
	        while(!arrivalQueue.isEmpty() && (readyQueue.isEmpty() || (arrivalQueue.peek().getArrivalTime() <= tempSystemTime )))
	                readyQueue.add(arrivalQueue.poll()); 
	}
	
	private void fillRunningList(int tempSystemTime)
	{
	        while(!readyQueue.isEmpty() && readyQueue.peek().getShare() <= curentSharesLeft) 
	        {
	                curentSharesLeft -= readyQueue.peek().getShare();
	                runningList.add(readyQueue.poll());
	                startSystemTime.add(tempSystemTime);
	        }
	}
	
	private void runSimulation()
	{
		Process currentProcess;
		int runtime, temp;
	        int currentPos = 0 ;		
                
                fillReadyQueue(systemTime);
		fillRunningList(systemTime);
		
		while(!arrivalQueue.isEmpty() || !runningList.isEmpty()|| !readyQueue.isEmpty())
		{ 
                        for(int itr = 0; itr < runningList.size();itr++)
                        {                                                             
                                currentProcess = runningList.get(itr);
                                
                                if (DEBUG_MODE)
                                {                                        
                                        System.out.println(currentProcess.toString());
                                        System.out.println("System Time:  " + startSystemTime.get(itr));
                                        pressAnyKeyToContinue();
                                }
                                
                                runtime =  currentProcess.getRemainingBurstTime();                                                
                                temp = currentProcess.runFor(startSystemTime.get(itr), runtime);
                                
                                if (DEBUG_MODE)
                                        System.out.println("temp : " + temp + "\n");
                                
                                if(temp > systemTime)
                                        systemTime = temp; 
                                        
			        if(currentProcess.isFinished())
			        { 		          
				        finishedQueue.add(currentProcess);
				        totalWaitTime += currentProcess.getCurrentWaitTime();
				        totalTurnaroundTime += currentProcess.getTurnAroundTime();
				        curentSharesLeft += currentProcess.getShare();
				        runningList.remove(itr);
				        startSystemTime.remove(itr);
				        itr--;				      
			        }
			        
			        fillReadyQueue(temp);  
			        fillRunningList(temp);	  
			}		
				
		}
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
		        System.out.println(e.toString());
		        e.printStackTrace();
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
