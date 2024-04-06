import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.*;

public class Rover {

    public static void main(String[] args) {
        final var readings = Collections.synchronizedList(new ArrayList<TempReading>());
        Sensor sensor = new Sensor(readings);
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(8);
        scheduler.scheduleAtFixedRate(sensor, 0, 10, TimeUnit.MILLISECONDS);

        var reporter = new Runnable(){
            public void run(){
                final var readingsList = new ArrayList<>(readings);
                readingsList.sort((a,b)->Integer.compare(a.temperature, b.temperature));
                System.out.println("Highest temperatures: ");
                System.out.println();
                System.out.println("Lowest temperatures: ");

            }
        };

        scheduler.scheduleAtFixedRate(reporter, 0, 60, TimeUnit.MILLISECONDS);

        while (true){

        }
    }
}
