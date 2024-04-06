import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.*;
import java.sql.Timestamp;

// Uses eight sensors son the Rover to read current temperature, then each puts those values in memory
// along with the others' readings. Then outputs highest and lowest temperatures, and the time
// interval in which the greatest difference in temperature was.
public class Rover {
    // takes the minimum and maximum temperature's timestamps and returns the
    // time interval between them
    public static Timestamp[] difference(ArrayList<TempReading> readings){
        int maxTemp = -200;
        int minTemp = 100;
        Timestamp maxTime = Timestamp.from(Instant.now());
        Timestamp minTime = Timestamp.from(Instant.now());

        for (TempReading reading : readings){
            if (reading.temperature > maxTemp){
                maxTemp = reading.temperature;
                maxTime = reading.time;
            } else if (reading.temperature < minTemp){
                minTemp = reading.temperature;
                minTime = reading.time;
            };
        }

        // orders the output to ensure it makes sense chronologically
        if(maxTime.after(minTime)){
            return new Timestamp[]{minTime, maxTime};
        }else {
            return new Timestamp[]{maxTime, minTime};
        }
    }

    public static void main(String[] args) {
        final var readings = Collections.synchronizedList(new ArrayList<TempReading>());

        // schedule eight sensors to take temperature readings every simulated minute
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(8);
        scheduler.scheduleAtFixedRate(new Sensor(readings), 0, 50, TimeUnit.MILLISECONDS);
        scheduler.scheduleAtFixedRate(new Sensor(readings), 0, 50, TimeUnit.MILLISECONDS);
        scheduler.scheduleAtFixedRate(new Sensor(readings), 0, 50, TimeUnit.MILLISECONDS);
        scheduler.scheduleAtFixedRate(new Sensor(readings), 0, 50, TimeUnit.MILLISECONDS);
        scheduler.scheduleAtFixedRate(new Sensor(readings), 0, 50, TimeUnit.MILLISECONDS);
        scheduler.scheduleAtFixedRate(new Sensor(readings), 0, 50, TimeUnit.MILLISECONDS);
        scheduler.scheduleAtFixedRate(new Sensor(readings), 0, 50, TimeUnit.MILLISECONDS);
        scheduler.scheduleAtFixedRate(new Sensor(readings), 0, 50, TimeUnit.MILLISECONDS);

        // reports highest and lowest temperatures at given intervals
        var reporter = new Runnable(){
            public void run(){
                // copy all readings from the last hour
                final var readingsList = new ArrayList<>(readings);

                // clear the list of readings from the last hour from the shared memory pool
                readings.clear();

                // sort list to easily get highest and lowest temperatures, then output 5 highest and lowest
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

                // formats the date to hour:minute:second:millisecond format
                String formattedDate1 = new SimpleDateFormat("H:m:s:S").format(difference(readingsList)[0]);
                String formattedDate2 = new SimpleDateFormat("H:m:s:S").format(difference(readingsList)[1]);
                System.out.println();
                System.out.println("Time interval of largest temperature difference: ");
                System.out.println(formattedDate1 + " to " + formattedDate2);
            }
        };

        // reports the temperature readings every simulated hour
        ScheduledExecutorService reportScheduler = Executors.newScheduledThreadPool(1);
        reportScheduler.scheduleAtFixedRate(reporter, 700, 1000, TimeUnit.MILLISECONDS);
    }
}
