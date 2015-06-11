/***
*	@author Mr.Shroom
*	Created by: Shaun McThomas
*	Last Modified:     06/10/15
*	This a simple class to represent a process in a scheduler algorithm
*	It will be used in simulations 
****/

import java.util.*;

public class Process
{
        private Long processId; //the process id
        private int arrivalTime; //the time the process arrived
        private int lastTime; //the last time the process had the CPU
        private int turnAroundTime;//the process turn around time
        private int burstTime; //the total burst time
        private int share; //the share used only for PS scheduler
        private int remainingBurstTime; //the remaining burst
        private int currentWaitTime; //the current amount of time the process has been waiting

        /**
        *constructor with needed parameters
        *
        **/
        public Process(String vals)
        {
                Scanner scanner =  new Scanner(vals);
                Long processId = new Long(scanner.nextLong());
                int arrivalTime = scanner.nextInt();
                int burstTime = scanner.nextInt();
                int share = (scanner.hasNextInt() ? scanner.nextInt() : 0);
                setAtributes(processId,arrivalTime, burstTime, share, burstTime, arrivalTime,0,0);
                scanner.close();
        }

        /**
        *constructor with needed parameters
        *
        **/
        public Process(Long processId,int arrivalTime, int burstTime, int share)
        {
                setAtributes(processId,arrivalTime, burstTime, share, burstTime, arrivalTime,0,0);
        }

        /**
        *constructor with needed parameters
        *
        **/
        public Process(Long processId,int arrivalTime, int burstTime)
        {
                setAtributes(processId,arrivalTime, burstTime, 0, burstTime, arrivalTime,0,0);
        }

        /**
        * copy constructor
        *
        **/
        public Process(Process toCopy)
        {
                setAtributes(toCopy.processId,toCopy.arrivalTime, 
                        toCopy.burstTime,toCopy.share, toCopy.remainingBurstTime, toCopy.lastTime,
                        toCopy.currentWaitTime, toCopy.turnAroundTime );
        }

        /**
        *setter for process Attributes
        *
        **/
        private void setAtributes(Long processId, int arrivalTime, int burstTime,
                int share, int remainingBurstTime, int lastTime, int currentWaitTime, int turnAroundTime )
        {
                this.processId =new Long(processId);
                this.arrivalTime = arrivalTime;
                this.burstTime = burstTime;
                this.share = share;
                this.remainingBurstTime = remainingBurstTime;
                this.lastTime = lastTime;
                this.currentWaitTime = currentWaitTime;
                this.turnAroundTime = turnAroundTime;
        }
        
        /**
        *getter
        **/
        public Long getProcessId()
        {
                return processId;
        }

        /**
        *getter
        **/
        public int getArrivalTime()
        {
                return arrivalTime;
        }

        /**
        *getter
        **/
        public int getBurstTime()
        {
                return burstTime;
        }

        /**
        *getter
        **/
        public int getShare()
        {
                return share;
        }
        
        /**
        *getter
        **/
        public int getRemainingBurstTime()
        {
                return remainingBurstTime;
        }
        
        /**
        *getter
        **/
        public int getCurrentWaitTime()
        {
                return currentWaitTime;
        }
        
        /**
        *getter
        **/
        public int getTurnAroundTime()
        {
                return turnAroundTime;
        }

        /**
        *check if process is finished running
        **/
        public boolean isFinished ()
        {
                return (remainingBurstTime <= 0);
        }
	
        /**
        *	This function handles the running a process for a set amount of time.
        *	Where it finishes it returns what the new system time should be
        *
        *	@param systemTime	The system time before running the process
        *	@param runAmount	The amount of time allocated for the process to run.
        *
        *	@return				What the new system time should be.
        **/
        public int runFor (int systemTime, int runAmount)
        {
                if(!isFinished())                               //if the process is finished, we don't run it
	        {
                        if(remainingBurstTime < runAmount)      //make sure we don't run past the end 
                                runAmount = remainingBurstTime;
                        
                        remainingBurstTime -= runAmount;		//logic involved in the running of a process
                        currentWaitTime += (systemTime - lastTime);	//bookkeeping so we can easily know wait time 
                        lastTime = systemTime + runAmount;		//needed to figure out wait and turn Around Time

                        if(isFinished())
                                turnAroundTime = lastTime - arrivalTime; //now that we're done we can calculate turn Around Time
                        
                        return lastTime;
                }
                return systemTime;
        }

        /**
        *	This function will output process as a string in the format request by the assignment
        *	<process-id> <finish-time> <wait-time> <turnaround-time>
        *
        *	@return			The requested string.
        **/
        public String output()
        {
                return(new String(processId + " " + lastTime + " " + currentWaitTime + " " + turnAroundTime + "\n"));
        }

        @Override 
        public String toString()
        {
                return(new String("[Process Id: " +             this.processId +
                                  "\nArrival Time: " +          this.arrivalTime+
                                  "\nInitail Burst time: " + this.burstTime  +
                                  "\nShare: " +                 this.share +
                                  "\nRemaining Burst time: " +  this.remainingBurstTime +
                                  "\nLast time it used CPU: "+  this.lastTime  +
                                  "\nCurrent wait time: " +	this.currentWaitTime  +
                                  "\nCurrent turnaround time(0 if unfinished): " + this.turnAroundTime +
                                  "]\n"));
        }
}
