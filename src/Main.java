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

    	// Create Software Project Manager
    	SoftwareProjectManager spm = new SoftwareProjectManager();

    	// Create Team Leaders
    	for (int i=0; i < 3; i++) {
    		// TeamLead (Team, EmployeeNumber)
    		TeamLead l = new TeamLead("TeamLead " + (i+1) + 1);
    		l.setManager(spm);
    		leads.add(l);

    		// Create developers and set everything
    		for (int j=0; j<3; j++) {
    			// Developer (Team, EmployeeNumber)
    			Developer d = new Developer("Developer " + (i+1) + (j+2));
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
