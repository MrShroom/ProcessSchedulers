
public class Process
{
	private Long processId; //the process id
	private Integer arrivalTime; //the time the process arrived
	private Integer lastTime; //the last time the process had the CPU
	private Integer turnAroundTime;//the process turn around time
	private int burstTime; //the total burst time
	private int share; //the share used only for PS scheduler
	private int remainingBurstTime; //the remaining burst
	private int currentWaitTime; //the current amount of time the process has been waiting
	
	/**
	*constructor with needed parameters
	*
	**/
	public Process(Long processId,Integer arrivalTime, int bustTime, int share)
	{
		setAtributes(processId,arrivalTime, bustTime, share);
	}

	/**
	*constructor with needed parameters
	*
	**/
	public Process(Long processId,Integer arrivalTime, int bustTime)
	{
		setAtributes(processId,arrivalTime, bustTime, 0);
	}

	/**
	*setter for process Attributes
	*
	**/
	private void setAtributes(Long processId, Integer arrivalTime, int bustTime, int share)
	{
		this.processId =new Integer(processId);
		this.arrivalTime = new Integer( arrivalTime);
		this.burstTime = burstTime;
		this.share = share;
		this.remainingBurstTime = bustTime;
		this.lastTime = new (arrivalTime);
		this.currentWaitTime = 0;
		this.turnAroundTime = 0;
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
	public Integer getArrivalTime()
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
	public int getshare()
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
	public int getcurrentWaitTime()
	{
		return currentWaitTime;
	}
	
	public boolean isFinished ()
	{
		return (remainingBurstTime <= 0);
	}
	
	public void runFor (int sytemTime, int runAmount)
	{
		if(!isFinished())
		{
			remainingBurstTime -= runAmount;
			currentWaitTime += (sytemTime - lastTime);
			lastTime = sytemTime + runAmount;
		}
		
	}
}
	

