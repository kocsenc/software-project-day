import java.util.ArrayList;
import java.util.List;

/**
 * SPM Class
 *
 * Work on: Geoff
 */

/* TODO: verify I need to extend thread, pretty sure Firm is going to manage me */
public class SoftwareProjectManager extends Thread {

	/* TODO: I think this will be a map but I need to verify that
	         I will need access to the threads via a lexical key. */
	private List<TeamLead> teamLeaders;
	private boolean awaitingQuestions = true;

	/**
	 * No args constructor
	 */
	public SoftwareProjectManager() {

	}

	/**
	 * A method that allows team leaders to knock on the door and initiate
	 * a meeting with the SoftwareProjectManager.
	 * 
	 * @param tl the TeamLeader who is knocking
	 */
	public void knock(TeamLead tl) {
		// TODO: Not sure this is needed... need to determine who controls this
		// Lazy instantiation
		if (teamLeaders == null) {
			teamLeaders = new ArrayList<TeamLead>();
		}

		// Add team lead to a list/map to keep track.
		teamLeaders.add(tl);
	}

	/**
	 * A method that allows team leaders to arrive at the door and initiate
	 * a meeting with the SoftwareProjectManager.
	 * 
	 * @param tl the TeamLeader who is knocking
	 */
	public void arrives(TeamLead tl) {
		// Lazy instantiation
		if (teamLeaders == null) {
			teamLeaders = new ArrayList<TeamLead>();
		}

		// Add team lead to a list/map to keep track.
		teamLeaders.add(tl);

		if (teamLeaders.size()>=3) {
			// Notify that all three are here???
			/* Who's in charge of "knocking" on my door? */
		}
	}

	/**
	 * @see Thread.java
	 */
	public void run() {
		// Arrive at Firm.java at 8AM

		// Do my general daily activities...

		// Do my administrative stuff, waiting until EVERY team lead arrives.
		// while not knock() : wait();
		/* TODO: Someone needs to notify me to check. */

		// Enter 15 minute stand-up
		performStandup();
		
		// wait for questions, break if end of day meeting
		while(awaitingQuestions) {
			// If someone asks a question : awaitingQuestions = false;
			answerQuestion();
			// If no question : look for deals on Woot!;
		}
		
		performStandup();
		
		// Leave at 5PM
	}


	private void answerQuestion() {
		// TODO: implement this method
	}
	
	private void performStandup() {
		// TODO: implement this method
	}
	
	/**
	 * Set this object to be at lunch or not
	 * @param atLunch is this object at lunch or not
	 */
	public void setAtLunch(boolean atLunch) {
		// set available
		if (atLunch) {
			// set unavailable
		}
		// TODO: Implement
	}

	/**
	 * Set this object to be in a meeting or not
	 * @param inMeeting is this object in a meeting or not
	 */
	public void setInMeeting(boolean inMeeting) { 
		// set available
		if (inMeeting) {
			// set unavailable
		}
		// TODO: Implement
	}

}
