import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.*;

public class Rover {

    public static void main(String[] args) {
        final var readings = Collections.synchronizedList(new ArrayList<TempReading>());
        Sensor sensor = new Sensor(readings);
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(8);
        scheduler.scheduleAtFixedRate(sensor, 0, 100, TimeUnit.MILLISECONDS);
        scheduler.scheduleAtFixedRate(sensor, 0, 100, TimeUnit.MILLISECONDS);
        scheduler.scheduleAtFixedRate(sensor, 0, 100, TimeUnit.MILLISECONDS);
        scheduler.scheduleAtFixedRate(sensor, 0, 100, TimeUnit.MILLISECONDS);
        scheduler.scheduleAtFixedRate(sensor, 0, 100, TimeUnit.MILLISECONDS);
        scheduler.scheduleAtFixedRate(sensor, 0, 100, TimeUnit.MILLISECONDS);
        scheduler.scheduleAtFixedRate(sensor, 0, 100, TimeUnit.MILLISECONDS);
        scheduler.scheduleAtFixedRate(sensor, 0, 100, TimeUnit.MILLISECONDS);

        // reports highest and lowest temperatures at given intervals
        var reporter = new Runnable(){
            public void run(){
                final var readingsList = new ArrayList<>(readings);
                readings.clear();
                readingsList.sort((a,b)->Integer.compare(a.temperature, b.temperature));
                System.out.println("Lowest temperatures: ");
                for (int i=0; i < 5 && i < readingsList.size(); i++) {
                    System.out.print(readingsList.get(i).temperature + " ");
                }
                System.out.println();
                System.out.println("Highest temperatures: ");
                for (int i=0; i < 5 && readingsList.size() - i - 1 >= 0; i++) {
                    System.out.print(readingsList.get(readingsList.size() - i - 1).temperature + " ");
                }
            }
        };

        // we have potentially two threads that handle reporting. if the first takes too long,
        // the second will be created
        ScheduledExecutorService reportScheduler = Executors.newScheduledThreadPool(2);
        reportScheduler.scheduleAtFixedRate(reporter, 700, 600, TimeUnit.MILLISECONDS);
    }
}
