import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Kocsen & Conor on 9/19/14.
 * Woolie crossing the bridge
 */


public class Bridge {
    private final int MAX_CAPACITY;

    private ArrayList<Woolie> wooliesOnBridge;
    private LinkedList<Woolie> woolieQueue;

    /**
     * @param capacity - number of woolies that a single bridge can hold
     */
    Bridge(int capacity) {
        this.MAX_CAPACITY = capacity;
        this.wooliesOnBridge = new ArrayList<>();
        this.woolieQueue = new LinkedList<>();
    }

    /**
     * @param woolie - the woolie entering
     * @return True or False depending if woolie on bridge or not
     */
    public synchronized boolean enterBridge(Woolie woolie) throws InterruptedException {
        woolieQueue.addLast(woolie);

        // While bridge is full & not first, tell woolie to wait
        while (wooliesOnBridge.size() == MAX_CAPACITY && !woolieQueue.peek().equals(woolie)) {
            woolie.wait();
        }

        // pop it from the stack and woolie to bridge
        woolieQueue.remove();
        wooliesOnBridge.add(woolie);
        woolie.notify();
        return true;
    }

    /**
     * Removes the woolie from the bridge array
     * @param woolie - the woolie leaving
     */
    public void leaveBridge(Woolie woolie) {
        wooliesOnBridge.remove(woolie);
        notify();
    }

}
