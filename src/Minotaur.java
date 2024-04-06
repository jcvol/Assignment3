import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Minotaur implements Runnable {
    final Servant[] servants;
    final Random random = new Random();
    final int numGifts;
    final ConcurrentLinkedQueue<Present> bag;
    final PresentChain chain;

    public Minotaur(Servant[] servants, int numGifts, ConcurrentLinkedQueue<Present> bag, PresentChain chain){
        // pass the array of the servants to the minotaur so he knows who to pick
        this.servants = servants;

        // let the minotaur know how many gifts there are so he can randomly pick an appropriate number
        // to search for
        this.numGifts = numGifts;

        // have the minotaur be aware of the bag and the chain of gifts so he knows when to stop asking
        // his servants to search for gifts
        this.bag = bag;
        this.chain = chain;
    }

    public void run(){
        do{
            // chooses a random gift tag
            final var tag = random.nextInt(numGifts);

            // chooses random servant to boss around
            final var chosenServant = random.nextInt(servants.length);
            final var servant = servants[chosenServant];
            System.out.println("The minotaur demands that " + servant.name + " finds gift number " + tag + "!");

            // the servant goes to search the requested gift number, when they can
            servant.findGift(tag);
            try {
                // the minotaur thinks about who he wants to harass next
                Thread.sleep(30);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }while(!bag.isEmpty() || !chain.isEmpty());
    }
}
