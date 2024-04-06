import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Random;

public class Sensor implements Runnable{
    Random random = new Random();
    List<TempReading> sharedReadings;
    public Sensor(List<TempReading> sharedReadings){
        this.sharedReadings = sharedReadings;
    }

    // within 10 min time intervals have highest temps & lowest

    public int collectTemp(){
        // creates random number for temperature recordings between -100 and 70
        return random.nextInt(70 + 100) - 100;
    }


    public void run(){
        final var temp = collectTemp();
        System.out.println(temp + "");
        final var reading = new TempReading(temp, Timestamp.from(Instant.now()));
        sharedReadings.add(reading);
    }
}
