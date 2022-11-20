package info.digitalproject.vedirect2mqtt4j.sample;

import java.util.LinkedList;
import java.util.Queue;

public class SimpleMovingAverage {
    // queue used to store list so that we get the average
    public final Queue<Integer> dataset = new LinkedList<>();
    private final int period;
    private int sum;

    // constructor to initialize period
    public SimpleMovingAverage(int period)
    {
        this.period = period;
    }

    // function to add new data in the
    // list and update the sum so that
    // we get the new mean
    public void addData(int num)
    {
        sum += num;
        dataset.add(num);

        // Updating size so that length
        // of data set should be equal
        // to period as a normal mean has
        if (dataset.size() > period) {
            sum -= dataset.remove();
        }
    }

    // function to calculate mean
    public int getMean() { return sum / dataset.size(); }

}
