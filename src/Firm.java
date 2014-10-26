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
	private List<Developer> developers;
	private List<TeamLead> leads;
	private List<Thread> threadDevs;
	private List<Thread> threadLeads;
	private long startTime;

	/**
	 * @param suPremeManager
	 *        SoftwareProjectManager that's head of the office
	 */
	public Firm(SoftwareProjectManager suPremeManager) {
		this.suPremeManager = suPremeManager;
		threadDevs.add(suPremeManager);
		this.leads = suPremeManager.getTeamLeaders();
		developers = new ArrayList<Developer>();
		for (TeamLead lead : this.leads) {
			developers.addAll(lead.getDevelopers());
			developers.add((Developer) lead);
			threadDevs.add(lead);
			threadDevs.addAll(lead.getDevelopers());
			threadLeads.add(lead);
		}

		// Daily standup, 8AM
		ArrayList<Thread> staff1 = new ArrayList<Thread>(threadLeads);
		staff1.add(suPremeManager);
		Meeting meeting1 = new Meeting(0, 300, 150, staff1);
		meetings.add(meeting1);

		// Corporate meeting, 10AM
		ArrayList<Thread> staff2 = new ArrayList<Thread>();
		staff2.add(suPremeManager);
		Meeting meeting2 = new Meeting(1200, 0, 600, staff2);
		meetings.add(meeting2);

		// Corporate meeting, 1PM
		ArrayList<Thread> staff3 = new ArrayList<Thread>();
		staff3.add(suPremeManager);
		Meeting meeting3 = new Meeting(3600, 0, 600, staff3);
		meetings.add(meeting3);

		// End of day meeting, 4PM
		ArrayList<Thread> staff4 = new ArrayList<Thread>(threadDevs);
		staff4.add(suPremeManager);
		Meeting meeting4 = new Meeting(0, 300, 150, staff4);
		meetings.add(meeting4);

	}

	/**
	 * Starts the simulation
	 */
	public void startDay() {
		startTime = System.currentTimeMillis();

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
	 * Attempts to join a meeting in progress.  Returns false if there is no current meeting the
	 * thread is in.  True otherwise.  
	 * @return False if there is no current meeting the thread is in.  True otherwise.  
	 */
	public synchronized boolean attemptJoin() {
		Meeting meeting = currentMeeting();
		return meeting.attemptJoin();
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
	 * Returns time since start in millliseconds.
	 * @return Time since start in millliseconds.
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
