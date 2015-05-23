
/**
 * First comes first served scheduler.
 */
 
import java.util.*;

public class FCFSScheduler implements Scheduler {

	private PriorityQueue<Process> arrivalQueue;
	private Queue finishedQueue;
	
	public FCFSScheduler()
	{
		arrivalQueue = new PriorityQueue<Process>(10, new ProcessArrivalTimeComparator());
		finishedQueue = new LinkedList();
	}
	
	private void fillArrivalQueue(String inputFile)
	{
		
		
	}
	
  @Override
  public void schedule(String inputFile, String outputFile) 
  {
    // implement your scheduler here.
    // write the scheduler output to {@code outputFile}
  }
}
