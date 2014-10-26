import java.util.ArrayList;
import java.util.List;

/**
 * Team Lead Class
 * <p/>
 * Work on: Conor
 */

public class TeamLead extends Thread {
    private static int leads = 0;
    private SoftwareProjectManager manager;
    private final ArrayList<Developer> developers = new ArrayList<Developer>();
    private final int id;
    public long entered;
    private int devArrived = 0;
    // Waits
    private Condition waitForDevs = new Condition() {
        @SuppressWarnings("unused")
        private boolean isMet() {
            return devArrived == 3;
        }
    };
    private Firm firm;
    private boolean locked = false;
    private Condition waitForUnlock = new Condition() {
        @SuppressWarnings("unused")
        private boolean isMet() {
            return !locked;
        }
    };

    public TeamLead(String name) {
        this.id = TeamLead.leads;
        TeamLead.leads += 1;
    }
    
    public synchronized void addDeveloper(Developer dev){
    	developers.add(dev);
    }
    
    public synchronized void setManager(SoftwareProjectManager man){
    	this.manager = man;
    }

    public synchronized void setFirm(Firm firm) {
        this.firm = firm;
    }

    public synchronized void knock() {
        try {
            Thread.currentThread().wait();
        } catch (InterruptedException e) {
        }
        this.devArrived += 1;
    }

    public void run() {
        try {
            // Wait up to 30 mins
            Thread.sleep(Util.randomInBetween(0, FirmTime.HALF_HOUR.ms()));

            // Arrive & knock on manager's door
            this.entered = firm.getTime();
            System.out.println(firm.getTime() + ": TeamLead #" + this.id + " arrives.");
            manager.knock(); // Locks until released from meeting

            waitForDevs.waitUntilMet(100);
            firm.attemptJoin(); // Lock until we have room empty
            Thread.sleep(15 * FirmTime.MINUTE.ms());
            firm.doneWithRoom();
            for (Developer dev : developers) {
                dev.notify();
            }
            while (firm.getTime() < FirmTime.HOUR.ms() * 4) {
            	Thread.currentThread().wait(100);
            }
            lock();
            System.out.println(firm.getTime() + ": TeamLead #" + this.id + " goes on lunch.");
            Thread.sleep(FirmTime.MINUTE.ms() * (30 + (int) Math.random() * 30));
            System.out.println(firm.getTime() + ": TeamLead #" + this.id + " ends lunch.");
            unlock();
            while (firm.getTime() < FirmTime.HOUR.ms() * 8) {
                Thread.currentThread().wait(100);
            }
            // Alert manager?
            firm.attemptJoin();
            while (firm.getTime() - this.entered < 8 * FirmTime.HOUR.ms()) {
            	Thread.currentThread().wait(100);
            }
            lock();
            System.out.println(firm.getTime() + ": TeamLead #" + this.id + " goes home.");
        } catch (InterruptedException e1) {
        }
    }

    public synchronized void unlock() {
        locked = false;
    }

    public synchronized void lock() {
        waitForUnlock.waitUntilMet(100);
        locked = true;
    }

    /**
     * Developer calls this method when asking a question
     */
    public synchronized void askedQuestion() {
        // 50% chance of coming up with the answer
        this.waitForUnlock.waitUntilMet(100);
        boolean canIAnswer = Util.randomInBetween(0, 1) == 1;

        // 10 minute wait only applies for the manager
        //Thread.sleep(FirmTime.MINUTE.ms() * 10);
        // See if we need to talk to the SPM incase we don't know the answer
        if (!canIAnswer) {
            this.lock();
            this.askQuestion();
            this.unlock();
        }
    }

    public synchronized void askQuestion() {
        this.lock();
        this.manager.askQuestion();
        unlock();
    }

    /**
     * Gets list of developers under this team lead.
     *
     * @return list of developers under this team lead.
     */
    public synchronized ArrayList<Developer> getDevelopers() {
        return developers;
    }
}
