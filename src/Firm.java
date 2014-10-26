import java.util.ArrayList;
import java.util.List;

/**
 * Firm Class
 * 
 * Work on: John
 */

public class Firm {
	private List<Meeting> meetings;
	private SoftwareProjectManager suPremeManager;
	//private List<Developer> developers;
	private List<TeamLead> leads;
	private List<Thread> threadDevs;
	private List<Thread> threadLeads;
	private long startTime;
	private boolean conferenceRoomFree;
	private List<TeamLead> roomQ;
	private long timeInQ;
	private long lastStart;

	/**
	 * @param suPremeManager
	 *        SoftwareProjectManager that's head of the office
	 */
	public Firm(SoftwareProjectManager suPremeManager) {
		timeInQ = 0;
		lastStart = -15;
		this.roomQ = new ArrayList<TeamLead>();
		this.suPremeManager = suPremeManager;
		threadDevs.add(suPremeManager);
		this.leads = suPremeManager.getTeamLeaders();
		//developers = new ArrayList<Developer>();
		
		ArrayList<Thread> staff1 = new ArrayList<Thread>();
		ArrayList<Thread> staff2 = new ArrayList<Thread>();
		ArrayList<Thread> staff3 = new ArrayList<Thread>();
		int count = 1;
		for (TeamLead lead : this.leads) {
			if ( count == 1 ) {
				staff1.add(lead);
				staff1.addAll(lead.getDevelopers());
			}
			if ( count == 2 ) {
				staff2.add(lead);
				staff2.addAll(lead.getDevelopers());
			}
			if ( count == 3 ) {
				staff3.add(lead);
				staff3.addAll(lead.getDevelopers());
			}
			count ++;
			//developers.addAll(lead.getDevelopers());
			//developers.add((Developer) lead);
			threadDevs.add(lead);
			threadDevs.addAll(lead.getDevelopers());
			threadLeads.add(lead);
		}

		// Team1
		Meeting meeting1 = new Meeting(0, 300, 150, staff1);
		meetings.add(meeting1);

		// Team2
		Meeting meeting2 = new Meeting(1200, 0, 600, staff2);
		meetings.add(meeting2);

		// Team3
		Meeting meeting3 = new Meeting(3600, 0, 600, staff3);
		meetings.add(meeting3);

		// End of day meeting, 4PM
//		ArrayList<Thread> staff4 = new ArrayList<Thread>(threadDevs);
//		staff4.add(suPremeManager);
//		Meeting meeting4 = new Meeting(0, 300, 150, staff4);
//		meetings.add(meeting4);

	}
	
	private void addAllFirms() {
		for ( Thread dev : this.threadDevs ) {
			try {
				((Developer)dev).setFirm(this);
			}catch(Exception e){}
			try {
				((TeamLead)dev).setFirm(this);
			}catch(Exception e){}
			try {
				((SoftwareProjectManager)dev).setFirm(this);
			}catch(Exception e){}
		}
	}

	/**
	 * Starts the simulation
	 */
	public void startDay() {
		addAllFirms();
		startTime = System.currentTimeMillis();

	}
	


	/**
	 * Attempts to join a meeting in progress.  Returns false if there is no current meeting the
	 * thread is in.  True otherwise.  
	 * @return False if there is no current meeting the thread is in.  True otherwise.  
	 */
	public boolean attemptJoin() {
		synchronized(this){
			while ( getTime() - lastStart < FirmTime.MINUTE.ms() * 15)
			{
				try {
					Thread.currentThread().wait(FirmTime.MINUTE.ms() * 15 - (getTime() - lastStart));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			lastStart = getTime();
		}
		return true;
//		Meeting meeting = currentMeeting();
//		return meeting.attemptJoin();
	}

	/**
	 * Returns time until the soonest the next meeting starts for the current thread in milliseconds.
	 * @return Time until the soonest the next meeting starts for the current thread in milliseconds.
	 */
	public synchronized long getTimeUntilNextMeetingSuggestedStart() {
		long timeUntil = 5400;
		for (Meeting meeting : meetings) {
			if (timeUntil > meeting.timeUntilScheduledStart()) {
				timeUntil = meeting.timeUntilScheduledStart();
			}
		}
		return timeUntil;
	}

	/**
	 * Returns time until the latest the next meeting starts for the current thread in milliseconds.
	 * @return Time until the latest the next meeting starts for the current thread in milliseconds.
	 */
	public synchronized long getTimeUntilNextMeetingMustStart() {
		long timeUntil = 5400;
		for (Meeting meeting : meetings) {
			if (timeUntil > meeting.timeUntilMustStart()) {
				timeUntil = meeting.timeUntilMustStart();
			}
		}
		return timeUntil;
	}

	/**
	 * Returns true if the current thread currently is (or should be) in a meeting.
	 * @return True if the current thread currently is (or should be) in a meeting.
	 */
	public synchronized boolean inMeeting() {
		if (currentMeeting() == null) {
			return false;
		}
		return true;
	}

	/**
	 * Returns time left in the current meeting that the thread is in. 0 if there is no meeting.  
	 * Returns the meeting's duration, if the meeting has not actually started yet.  
	 * @return Time left in the current meeting that the thread is in, 0 if there is no meeting,
	 *         and the meeting's duration, if the meeting has not actually started yet.  
	 */
	public synchronized long timeLeft() {
		Meeting meeting = currentMeeting();
		if (meeting == null) {
			return 0;
		}
		return meeting.estimatedTimeUntilEnd();
	}

	/**
	 * Returns the meeting that the current thread should be in.  Null if no meeting fulfills that criterion.
	 * @return The meeting that the current thread should be in.  Null if no meeting fulfills that criterion.
	 */
	private synchronized Meeting currentMeeting() {
		Thread dev = Thread.currentThread();
		for (Meeting meeting : meetings) {
			if (meeting.currentlyRunning()
					&& meeting.devsRequired.contains(dev)) {
				return meeting;
			}
		}
		return null;
	}

	/**
	 * Returns time since start in milliseconds.
	 * @return Time since start in milliseconds.
	 */
	public synchronized long getTime() {
		long currentTime = System.currentTimeMillis() - startTime;
		return currentTime;
	}

	/**
	 * Representation of a meeting.
	 */
	private class Meeting {

		/** When the meeting is scheduled to start in milliseconds from start */
		public long scheduledStart;
		/** 
		 * How late people are allowed to be, e.g., meeting is at 4:00, but people can arrive 
		 * as late as 4:15, so the leeway would be 150 (15 minutes in milliseconds) */
		public long leeway;
		/** When everyone arrived and the meeting started.  -1 if that has not happened yet. */
		public long actualStart;
		/** How long the meeting is in milliseconds */
		public long duration;
		/** List of developers, team leads, and SPMs that need to be there. */
		public List<Thread> devsRequired;
		/** List of developers, team leads, and SPMs that have arrived. */
		public List<Thread> devsPresent;

		
		/**
		 * Constructor for Meeting
		 * @param time 
		 *        Time the meeting starts
		 * @param leeway
		 *        How late people can be
		 * @param duration
		 *        How long the meeting is
		 * @param devsRequired
		 *        List of developers, team leads, and SPMs required for the meeting to start
		 */
		public Meeting(long time, long leeway, long duration,
				ArrayList<Thread> devsRequired) {
			this.scheduledStart = time;
			this.leeway = leeway;
			this.duration = duration;
			this.devsRequired = devsRequired;
			this.devsPresent = new ArrayList<Thread>();
			this.actualStart = -1;
		}

		/**
		 * Returns time until the meeting should start.
		 * @return Time until the meeting should start.
		 */
		public synchronized long timeUntilScheduledStart() {
			long timeUntil = scheduledStart - getTime();
			return timeUntil;
		}

		/**
		 * Returns time until the meeting MUST start.
		 * @return Time until the meeting MUST start.
		 */
		public synchronized long timeUntilMustStart() {
			long timeUntil = scheduledStart + leeway - getTime();
			return timeUntil;
		}

		/**
		 * Current thread attempts to join this meeting.
		 * @return True if current thread is a member of this meeting and meeting is currently running.
		 */
		public synchronized boolean attemptJoin() {
			Thread dev = Thread.currentThread();
			if (devsRequired.contains(dev)) {
				if (!devsPresent.contains(dev)) {
					devsPresent.add(dev);
					if (devsPresent.size() == devsRequired.size()) {
						this.actualStart = getTime();
						notifyAll();
					}
				}
				return true;
			}
			return false;
		}

		/**
		 * Returns true if the meeting is currently running. 
		 * @return True if the meeting is currently running. 
		 */
		public synchronized boolean currentlyRunning() {
			if (getTime() > scheduledStart) { // scheduled start is before the
												// current time
				if (actualStart == -1) { // meeting should be starting, but not
											// everyone's here
					return true;
				}
				if (actualStart + duration < getTime()) { // meeting started and
															// has not yet
															// reached duration
					return true;
				}
			}
			return false;
		}

		/**
		 * How long until the meeting ends.  Returns duration, if it has not started yet. 
		 * @return How long until the meeting ends.  Returns duration, if it has not started yet. 
		 */
		public synchronized long estimatedTimeUntilEnd() {
			if (actualStart != -1) {
				long ete = (actualStart + duration) - getTime();
				return ete;
			} else {
				return duration;
			}
		}
	}

	public void doneWithRoom() {
		// TODO Auto-generated method stub
		
	}
}
