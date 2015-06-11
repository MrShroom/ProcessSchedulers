/***
*	@author Mr.Shroom
*	Created by: Shaun McThomas
*	Last Modified:     06/10/15
*
* First comes first served scheduler.
****/
 
import java.util.*;
import java.io.*;

public class FCFSScheduler implements Scheduler 
{
        private PriorityQueue<Process> arrivalQueue,finishedQueue;
        private int systemTime, totalWaitTime, totalTurnaroundTime;

        public FCFSScheduler()
        {
                arrivalQueue = new PriorityQueue<Process>(10, new ProcessArrivalTimeComparator());
                finishedQueue =  new PriorityQueue<Process>(10, new ProcessIDComparator());
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
                        finishedQueue.add(currentProcess);
                        totalWaitTime += currentProcess.getCurrentWaitTime();
                        totalTurnaroundTime += currentProcess.getTurnAroundTime();						
                }
        }

        private void writeResults(String outputFile) throws IOException
        {
                long aveWaitTime = Math.round((double)totalWaitTime/finishedQueue.size());
                long aveTurnaroundTime = Math.round((double)totalTurnaroundTime/finishedQueue.size());
                Process currentProcess;

                BufferedWriter outputter = new BufferedWriter (new FileWriter(outputFile)); 
                
                while((currentProcess = finishedQueue.poll()) != null)
                        outputter.write(currentProcess.output());
                        
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
}
