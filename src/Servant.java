import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Servant implements Runnable {
    public final String name;
    final ConcurrentLinkedQueue<Present> bag;
    final PresentChain chain;
    private ServantMode mode;
    private int tagQuery;
    private Object lock = new Object();

    public Servant(ConcurrentLinkedQueue<Present> bag, PresentChain chain, ServantMode mode, String name) {
        // have the servant be aware of the bag and the chain of gifts so they know when to stop
        // doing their jobs
        this.bag = bag;
        this.chain = chain;

        // the servants have different modes they alternate between while they are working for different jobs
        this.mode = mode;

        this.name = name;
    }

    public void findGift(int tag) {
        // look through present chain to see if the gift is sorted into the chain of gifts
        // by request of the minotaur
        tagQuery = tag;

        synchronized (lock) {
            mode = ServantMode.Searching;
        }
    }

    private void insertGift() {
        // take gift out of bag and insert gift into chain
        // outputs message indicating that the gift has been sorted
        final var present = bag.poll();
        if (present != null) {
            chain.insert(present);
            System.out.println(name + " has inserted a gift from guest number " + present.tag);
        }
        synchronized (lock) {
            // if mode is not set to "searching" then the servant will move to thanking
            if (mode != ServantMode.Searching) {
                mode = ServantMode.Thanking;
            }
        }
    }

    private void thankGuest() {
        // removes the first gift from the chain
        // indicates that the servant has thanked the guest
        final var removed = chain.remove();
        if (removed != null) {
            System.out.println(name + " has thanked guest number " + removed.tag);
        }
        synchronized (lock) {
            // if mode is not set to "searching" then the servant will move to inserting
            if (mode != ServantMode.Searching) {
                mode = ServantMode.Inserting;
            }
        }
    }

    public void run() {
        do {
            // depending on the mode of working that the servant is in, they will perform different jobs
            // the servant will keep doing this until the bag and chain are both empty
            switch (mode) {
                case Inserting:
                    insertGift();
                    break;
                case Searching:
                    final var isInChain = chain.searchChain(tagQuery);
                    if (isInChain) {
                        System.out.println(name + " has located the minotaur's requested gift number " + tagQuery + "!");
                    } else {
                        System.out.println(name + " was unable to locate the minotaur's requested gift number " + tagQuery + ".");
                    }
                    synchronized (lock){
                        mode = ServantMode.Inserting;
                    }
                    break;
                case Thanking:
                    thankGuest();
                    break;
            }
        } while (!bag.isEmpty() || !chain.isEmpty() || mode == ServantMode.Searching);
    }
}
