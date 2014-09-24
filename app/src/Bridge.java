import java.util.LinkedList;

/**
 * Created by Kocsen & Conor on 9/19/14.
 * Woolie crossing the bridge
 */


public class Bridge {
    private final int MAX_CAPACITY;

    private int wooliesOnBridge;
    private LinkedList<Woolie> woolieQueue;

    /**
     * @param capacity - number of woolies that a single bridge can hold
     */
    Bridge(int capacity) {
        this.MAX_CAPACITY = capacity;
        this.wooliesOnBridge = 0;
        this.woolieQueue = new LinkedList<>();
    }

    /**
     * @param woolie - the woolie entering
     */
    public synchronized void enterBridge(Woolie woolie) throws InterruptedException {
        woolieQueue.addLast(woolie);

        // While woolie is not first, woolie must wait
        while (!woolieQueue.peek().equals(woolie)) {
            wait();
        }
        // While bridge is full, woolie must wait
        while (wooliesOnBridge >= MAX_CAPACITY) {
            //System.out.println("\t\t\t" + woolie.name + "has to wait, bridge is full!");
            wait();
        }

        // pop it from the stack and woolie to bridge
        woolieQueue.remove();
        wooliesOnBridge++;

        notifyAll();
    }

    /**
     * Removes the woolie from the bridge array
     */
    public synchronized void leaveBridge() {
        wooliesOnBridge--;
        notifyAll();
    }

}
