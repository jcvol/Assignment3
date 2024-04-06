import java.sql.Timestamp;

public class TempReading {
    public final int temperature;
    public final Timestamp time;

    // get temperature readiing with current timestamp
    public TempReading(int temperature, Timestamp time){
        this.temperature = temperature;
        this.time = time;
    }
}
