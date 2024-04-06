import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.*;

public class Rover {

    public static void main(String[] args) {
        final var readings = Collections.synchronizedList(new ArrayList<TempReading>());
        Sensor sensor = new Sensor(readings);
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(8);
        scheduler.scheduleAtFixedRate(sensor, 0, 100, TimeUnit.MILLISECONDS);

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

        scheduler.scheduleAtFixedRate(reporter, 700, 600, TimeUnit.MILLISECONDS);

        while (true){

        }
    }
}
