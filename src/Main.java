import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        // ordered chain of presents, arranged by tag number in ascending order
        PresentChain chain = new PresentChain();

        // the minotaur has a bag of 500,000 presents that he wants his servants to organize into the chain
        final var numberOfPresents = 500000;
        final var presents = new Present[numberOfPresents];

        // this initializes our 500k gifts with a unique tag
        Arrays.parallelSetAll(presents, Present::new);

        // we now take all the presents and throw them into a bag
        final var shuffledPresents = Arrays.asList(presents);

        // and shuffle them around in the bag
        Collections.shuffle(shuffledPresents);

        final var bag = new ConcurrentLinkedQueue<>(shuffledPresents);

        // create all 4 of the minotaur's servants
        // starting all servants ready to insert gifts into the chain
        // since there is nothing to search for or remove at the beginning
        final var servants = new Servant[]{
                new Servant(bag, chain, ServantMode.Inserting, "John One"),
                new Servant(bag, chain, ServantMode.Inserting, "Mrs. Two"),
                new Servant(bag, chain, ServantMode.Inserting, "Detective Three"),
                new Servant(bag, chain, ServantMode.Inserting, "Madame Four")
        };

        // create the minotaur himself who will demand that a random servant search for specific tag numbers
        // in the chain of presents
        final var minotaur = new Minotaur(servants, numberOfPresents, bag, chain);

        // create threads of servants AND minotaur
        final var threads = new Thread[]{
                new Thread(servants[0]),
                new Thread(servants[1]),
                new Thread(servants[2]),
                new Thread(servants[3]),
                new Thread(minotaur)
        };

        // start all servants' and minotaur's threads
        for (int i = 0; i < 5; i++){
            threads[i].start();
        }

        // and join them at the end
        for (int i = 0; i < 5; i++){
            threads[i].join();
        }

    }
}