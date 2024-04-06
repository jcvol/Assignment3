import java.util.Random;

public class Sensor {


    // take temp readings every one min
    // rolling 10 min time interval?
    // within 10 min time intervals have highest temps & lowest
    // keep rolling interval recorded?

    public static int collectTemp(){
        // random number
        Random random = new Random();
        return random.nextInt(70 + 100) - 100;
    }
}
