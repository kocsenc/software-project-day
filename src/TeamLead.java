import java.util.ArrayList;

/**
 * Team Lead Class
 * <p/>
 * Work on: Conor
 */

public class TeamLead extends Thread {
    private static int leads = 0;
    private final ArrayList<Developer> developers = new ArrayList<Developer>();
    private final int id;
    public long entered;
    private SoftwareProjectManager manager;
    private int devArrived = 0;
    private Firm firm;
    private boolean locked = false;
    
    // Waits
    private Condition waitForDevs = new Condition() {
        @SuppressWarnings("unused")
        private boolean isMet() {
            return devArrived == 3;
        }
    };
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

    /**
     * Adds a developer to the TL's team
     * @param dev - developer to add
     */
    public synchronized void addDeveloper(Developer dev) {
        developers.add(dev);
    }

    /**
     * Sets the Team Lead's manager
     * @param man - manager to set to
     */
    public synchronized void setManager(SoftwareProjectManager man) {
        this.manager = man;
    }

    /**
     * Sets the Team Lead's firm
     * @param firm - firm to set to
     */
    public synchronized void setFirm(Firm firm) {
        this.firm = firm;
    }

    /**
     * Knocks on the PM's door for the morning meeting
     */
    public synchronized void knock() {
        try {
            wait();
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
            System.out.println(Util.timeToString(firm.getTime())+": TeamLead #" + this.id + " arrives.");
            manager.knock(); // Locks until released from meeting
            
            // Wait for the developers and start standup
            waitForDevs.waitUntilMet(10);
            firm.attemptJoin(); // Lock until we have room empty
            System.out.println(Util.timeToString(firm.getTime())+": TeamLead #" + this.id + " begins standup.");
            Thread.sleep(15 * FirmTime.MINUTE.ms());
            System.out.println(Util.timeToString(firm.getTime())+": TeamLead #" + this.id + " ends standup.");
            
            // End Standup
            for (Developer dev : developers) {
                synchronized (dev) {
                	dev.unlock();
                	synchronized (this) {
                		notifyAll();
                	}
                }
            }

            // Wait until lunch
            while (firm.getTime() < FirmTime.HOUR.ms() * 4) {
                synchronized (this) {
                    wait(10);

                }
            }
            
            // Go to lunch
            lock();
            System.out.println(Util.timeToString(firm.getTime())+": TeamLead #" + this.id + " goes on lunch.");
            Thread.sleep(FirmTime.MINUTE.ms() * (30 + (int) Math.random() * 30));
            System.out.println(Util.timeToString(firm.getTime())+": TeamLead #" + this.id + " ends lunch.");
            unlock();

            // Wait until end of the day meeting
            while (firm.getTime() < FirmTime.HOUR.ms() * 8) {
                synchronized (this) {
                    wait(10);
                }
            }
            
            // Attend Meeting
            lock();
            System.out.println(Util.timeToString(firm.getTime())+": TeamLead #" + this.id + " goes to meeting.");
            firm.attemptJoin();
            unlock();

            // Finish up and leave
            while (firm.getTime() - this.entered < 8 * FirmTime.HOUR.ms()) {
                synchronized (this) {
                    wait(10);
                }
            }
            lock();
            System.out.println(Util.timeToString(firm.getTime())+": TeamLead #" + this.id + " goes home.");
        } catch (InterruptedException e1) {
        }
    }

    /**
     * Unlocks the teamlead
     */
    public synchronized void unlock() {
        locked = false;
    }

    /**
     * Locks the teamlead
     */
    public synchronized void lock() {
        waitForUnlock.waitUntilMet(10);
        locked = true;
    }

    /**
     * Developer calls this method when asking a question
     */
    public synchronized void askedQuestion() {
        // 50% chance of coming up with the answer
        this.waitForUnlock.waitUntilMet(10);
        boolean canIAnswer = Util.randomInBetween(0, 1) != 1;

        // 10 minute wait only applies for the manager
        //Thread.sleep(FirmTime.MINUTE.ms() * 10);
        // See if we need to talk to the SPM incase we don't know the answer
        if (!canIAnswer) {
            this.askQuestion();
        }
    }

    /**
     * Asks the Project Manager a question
     */
    private synchronized void askQuestion() {
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
