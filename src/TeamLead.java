import java.util.ArrayList;
import java.util.List;

/**
 * Team Lead Class
 * <p/>
 * Work on: Conor
 */

public class TeamLead {
    private final SoftwareProjectManager manager;
    private final ArrayList<Developer> developers;
    private int devArrived = 0;
    public long entered;
    private Firm firm;
    private boolean locked = false;
    
    // Waits
    private Condition waitForDevs = new Condition() {
        private boolean isMet() {
            return devArrived==3;
        }
    };
    private Condition waitForUnlock = new Condition() {
        private boolean isMet() {
            return !locked;
        }
    };

    public TeamLead(String name, SoftwareProjectManager manager, Firm firm, List<Developer> developers) {
        this.manager = manager;
        this.firm = firm;
        this.developers = (ArrayList<Developer>) developers;
    }

    public synchronized void setFirm(Firm firm) {
        this.firm = firm;
    }
    
    public synchronized void knock(){
    	try {
			Thread.currentThread().wait();
		} catch (InterruptedException e) {
		}
		this.devArrived+=1;
    }

    public void run() {
    	try {
			Thread.sleep(Util.randomInBetween(0, FirmTime.HALF_HOUR.ms()));
		} catch (InterruptedException e1) {
		}
        this.entered = firm.getTime();
        manager.knock();
        
        waitForDevs.waitUntilMet(100);
        firm.attemptJoin();
        try {
            Thread.sleep(15*FirmTime.MINUTE.ms());
        } catch (InterruptedException e) {
        }
        firm.doneWithRoom();
        for (Developer dev : developers) {
            dev.notify();
        }
    }

    public synchronized void unlock() {
        locked = false;
    }

    public synchronized void lock() {
        locked = true;
    }

    /**
     * Developer calls this method when asking a question
     */
    public synchronized void askedQuestion() {
        // 50% chance of coming up with the answer
        boolean canIAnswer = Util.randomInBetween(0, 1) == 1;
        try {
            // 10 minute wait
            Thread.sleep(FirmTime.MINUTE.ms() * 10);
            // See if we need to talk to the SPM incase we don't know the answer
            if (!canIAnswer) {
                this.askQuestion();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public synchronized void askQuestion() {
        // TODO: This is what runs when we need to ask a question to the SPM
    	this.lock();
    	this.manager.askQuestion();
    	this.waitForUnlock.waitUntilMet(100);
    }

    /**
     * Gets list of developers under this team lead.
     *
     * @return list of developers under this team lead.
     */
    public ArrayList<Developer> getDevelopers() {
        return developers;
    }
}
