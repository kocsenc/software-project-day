
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
    private long entered;
    
    // Waits
    private Condition waitForDevs = new Condition(){
		@Override
		private void isMet(){
			for(Developer dev : developers){
				if(dev.entered==null){
					return false;
				}
			}
			return true;
		}
	}

    public TeamLead(SoftwareProjectManager manager, Firm firm, List<Developer> developers) {
        this.manager = manager;
        this.firm = firm;
        this.developers = developers;
    }
    
    public void run(){
    	entered = time;
    	waitForDevs.waitUntilMet(100);
    	
    }
}
