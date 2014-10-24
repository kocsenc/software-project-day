import java.util.ArrayList;
import java.util.List;

/**
 * SoftwareProjectManager.java
 * 
 * Overview:
 * This class represents a software project manager (SPM), the SPM is responsible
 * for three teams of developers.  The SPM arrives at the firm at 8AM and 
 * leaves the firm at 5PM. For the hours that the SPM is at the firm he has
 * a 15 minute stand-up with team leads while also attending two executive 
 * meetings at 10AM and 2PM.  The SPM goes to lunch at the earliest convenience 
 * after 12PM. The SPM also attends a project status meeting in the conference 
 * room at 4:15PM to give a fifteen minute status update.  For the times when
 * the SPM is not in meetings or at lunch the SPM is able to answer questions
 * that the team lead asks which will take 10 minutes to answer. 
 * 
 * 
 * Details from a developers standpoint:
 * The Firm 
 * 	Opens at 8:00 
 * 	Closes at 5:00 
 * 
 * Software Project Manager (SPM)
 * 	In charge of 3 teams of developers (12 total employees) (irrelevant)
 * 	Process:
 * 		Arrives at 8:00 each day
 * 		Engages in daily planning activities
 * 		<waits> for knock (meanwhile performing administrative duties)
 * 		Performs daily 15 minute stand-up
 * 		<waits> for 
 * 			Question 			(at any time)	: unavailable(10 min)
 * 			Executive Meeting	(10:00)			: unavailable(60 min)
 * 			Lunch				(>= 12:00)		: unavailable(60 min)
 * 			Executive Meeting	(2:00)			: unavailable(60 min)
 * 			Project Status Meet	(4:15)			: unavailable(15 min)
 * 			Departure			(5:00)			: END
 * 
 * Additional Info:
 * 	One minute of simulated time takes 10ms of real time (thus 8 hours takes 4800ms or 4.8 seconds).
 * 
 * @author: Geoff Berl
 * 
 */

/* TODO: verify I need to extend thread, pretty sure Firm is going to manage me */
public class SoftwareProjectManager extends Thread {

	private List<TeamLead> teamLeaders;
	private boolean available = true;
	private final Object knockLock = new Object();

	/**
	 * No args constructor
	 */
	public SoftwareProjectManager() {
		super();
	}

	/**
	 * A method that allows team leaders to knock on the door and initiate
	 * a meeting with the SoftwareProjectManager.
	 */
	public void knock() {
		synchronized(knockLock) {
			knockLock.notify();
		}
	}

	/* Method to wait for team leads to arrive at door */
	private void dailyPlanning() throws InterruptedException {
		// Tell the lock to wait, once notified, we'll continue
		synchronized(knockLock) {
			knockLock.wait();
		}
	}
	
	/* Perform the standup meeting */
	private void performStandup() {
		goUnavailable(FirmTime.MINUTE.ms()*15);
	}
	
	/* Answer a question */
	private void answerQuestion() {
		goUnavailable(FirmTime.MINUTE.ms()*10);
	}
	
	private void goUnavailable(long time) {
		available = false;
		try {
			Thread.sleep(time);
		} catch (InterruptedException ignore) { }
		available = true;
	}
	
	/**
	 * @see Thread.java
	 */
	public void run() {
		// Arrive at Firm.java at 8AM (by default, nothing to do here)
		// I'm available
		available = true;

		// Wait for team leads to arrive
		try {
			dailyPlanning();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// Start stand-up meeting
		performStandup();

		while(true) { // TODO: I thought FirmTime was going to keep track of actual time (Like, FirmTime.currentTime() return 5 for 5PM or whatever)
			
		}
		
		// Leave at 5PM
	}
	
	public synchronized void askQuestion() {
		// TODO: need to keep order.
		while(!available) {
			try {
				wait();
			} catch (InterruptedException ignore) { }
		}
		available = false;
		notify();
		answerQuestion();
	}

	
	/**
	 * Returns list of team leaders under the SPM.
	 * @return list of team leaders under the SPM.
	 */
	public List<TeamLead> getTeamLeaders() {
		return teamLeaders;
	}

	/**
	 * Sets list of team leaders under the SPM.
	 * @param teamLeaders
	 *        List of team leaders under the SPM.  
	 */
	public void setTeamLeaders(List<TeamLead> teamLeaders) {
		this.teamLeaders = teamLeaders;
	}
	
	/**
	 * Adds a team leader to the SPM
	 * @param lead the team leader to add
	 */
	public void addTeamLeader(TeamLead lead) {
		if (teamLeaders == null) {
			teamLeaders = new ArrayList<TeamLead>();
		}
		this.teamLeaders.add(lead);
	}
	


}
