
import java.util.ArrayList;
import java.util.List;

/**
 * Team Lead Class
 *
 * Work on: Conor
 */

public class TeamLead extends Developer {
    private final SoftwareProjectManager manager;
    private final ArrayList<Developer> developers;
    private final Firm firm;
    private boolean locked = false;
    
    // Waits
    private Condition waitForDevs = new Condition(){
    	private boolean isMet(){
			for(Developer dev : developers){
				if(!dev.getArrived()){
					return false;
				}
			}
			return true;
		}
	};
    private Condition waitForUnlock = new Condition(){
    	private boolean isMet(){
			return !locked;
		}
	};

    public TeamLead(String name, SoftwareProjectManager manager, Firm firm, List<Developer> developers) {
        super(name, firm, this); //TODO: this won't work out
        this.manager = manager;
        this.firm = firm;
        this.developers = (ArrayList<Developer>) developers;
    }
    
    public void run(){
    	this.entered = firm.getTime();
    	manager.arrives(this);
    	locked = true;
    	waitForUnlock.waitUntilMet(100);
    	waitForDevs.waitUntilMet(100);
    	for(Developer dev : developers){
            // TODO: Merge notes, we cant access varaibles, we need getters & setters.
    		dev.locked = true;
    	}
    	this.locked = true;
    	try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {
		}
    	for(Developer dev : developers){
    		dev.locked = false;
    	}
    	this.locked = false;
    	
    	
    }
    
    public void unlock(){
    	locked = false;
    }

    /**
     * Developer calls this method when asking a question
     */
    public void askedQuestion(){
        // 50% chance of coming up with the answer
        boolean canIAnswer = Util.randomInBetween(0,1) == 1;
        try {
            // 10 minute wait
            Thread.sleep(FirmTime.MINUTE.ms() * 10);
            // See if we need to talk to the SPM incase we don't know the answer
            if (!canIAnswer){
                this.askQuestion();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void askQuestion(){
        // TODO: This is what runs when we need to ask a question to the SPM
    }

    /**
     * Gets list of developers under this team lead.
     * @return list of developers under this team lead.
     */
	public ArrayList<Developer> getDevelopers() {
		return developers;
	}
}
