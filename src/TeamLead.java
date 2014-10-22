
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
    
    // Waits
    private Condition waitForDevs = new Condition(){
    	private boolean isMet(){
			for(Developer dev : developers){
				if(dev.entered==0){
					return false;
				}
			}
			return true;
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
    	for(Developer dev : developers){
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
     * Gets list of developers under this team lead.
     * @return list of developers under this team lead.
     */
	public ArrayList<Developer> getDevelopers() {
		return developers;
	}
}
