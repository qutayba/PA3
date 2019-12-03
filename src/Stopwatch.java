public class Stopwatch {

    private long startTime;
    private long endTime;

    public void start() {
        startTime = System.nanoTime();
    }

    public void stop() {
        endTime =  System.nanoTime();
    }

    public long getDuration() {
        return (endTime - startTime) / 1000000;
    }
}
