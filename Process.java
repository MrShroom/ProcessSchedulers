
public class Process
{
	private Long processId; //the process id
	private Integer arrivalTime; //the time the process arrived
	private int burstTime; //the total burst time
	private int share; //the share used only for PS schedular
	private int remainingBurstTime; //the remaining burst
	private int currentWaitTime; //the current amount of time the process has been waiting
	
	/**
	*contructor with needed parameters
	*
	**/
	public Process(Long processId,Integer arrivalTime, int bustTime, int share)
	{
		setAtributes(processId,arrivalTime, bustTime, share);
	}

	/**
	*contructor with needed parameters
	*
	**/
	public Process(Long processId,Integer arrivalTime, int bustTime)
	{
		setAtributes(processId,arrivalTime, bustTime, 0);
	}

	private void setAtributes(Long processId,Integer arrivalTime, int bustTime, int share)
	{
		this.processId = processId;
		this.arrivalTime = arrivalTime;
		this.burstTime = burstTime;
		this.share = share;
		remainingBurstTime = bustTime;
	}
	
	public Long getProcessId()
	{
		return processId;
	}

	public Integer getArrivalTime()
	{
		return arrivalTime;
	}
	
	public int getBurstTime()
	{
		return burstTime;
	}
	
	public int getshare()
	{
		return share;
	}
	
	public int getRemainingBurstTime()
	{
		return remainingBurstTime;
	}
	
}
	

