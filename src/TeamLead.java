
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
				if(dev.entered==null){
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

    public TeamLead(SoftwareProjectManager manager, Firm firm, List<Developer> developers) {
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
    	
    }
    
    public void unlock(){
    	locked = false;
    }

    /**
     * Gets list of developers under this team lead.
     * @return list of developers under this team lead.
     */
	public ArrayList<Developer> getDevelopers() {
		return developers;
	}
}
