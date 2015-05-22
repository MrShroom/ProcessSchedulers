
import java.util.Comparator;

public class ProcessArrivalTimeComparator implements Comparator<Process>
{
    @Override
    public int compare(Process x, Process y)
    {
        if (x.getArrivalTime() < y.getArrivalTime())
        {
            return -1;
        }
        if (x.getArrivalTime() > y.getArrivalTime())
        {
            return 1;
        }
		if(x.getArrivalTime() == y.getArrivalTime())
		{
			return x.compareTo(y);
		}
        return 0;
    }
}