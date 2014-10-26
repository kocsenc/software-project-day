import java.util.ArrayList;
import java.util.List;

/**
 * Main Class
 * Entry point of the software project day program
 */

public class Main {

    public static void main(String[] args){
        // TODO Stub fillout

        // Make a new Firm

    	// Ugly but I have to do it this way since there is a cyclic dependency
    	List<TeamLead> leads = new ArrayList<TeamLead>();
    	List<Developer> devs =  new ArrayList<Developer>();
    	List<Developer> devs1 =  new ArrayList<Developer>();
    	List<Developer> devs2 =  new ArrayList<Developer>();
    	List<Developer> devs3 =  new ArrayList<Developer>();

    	// Create Software Project Manager
    	SoftwareProjectManager spm = new SoftwareProjectManager();

    	// Create Team Leaders
    	for (int i=0; i < 3; i++) {
    		TeamLead l = new TeamLead("L" + i);
    		l.setManager(spm);
    		leads.add(l);

    		// Create developers and set everything
    		for (int j=0; j<3; j++) {
    			Developer d = new Developer("D" + (i+j) + 3*i);
    			d.setTeamLead(l);
    			l.addDeveloper(d);
    		}

    	}

        // Set team leads
    	spm.setTeamLeaders(leads);

        // Create a firm
        Firm firm = new Firm(spm);

    	// Start the application
    	firm.startDay();

    }
}
