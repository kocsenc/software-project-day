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

	public Firm(SoftwareProjectManager suPremeManager) {
		this.suPremeManager = suPremeManager;
		threadDevs.add(suPremeManager);
		this.leads = suPremeManager.getTeamLeaders();
		developers = new ArrayList<Developer>();
		for (TeamLead lead : this.leads) {
			developers.addAll(lead.getDevelopers());
			developers.add((Developer)lead);
			threadDevs.add(lead);
			threadDevs.addAll(lead.getDevelopers());
			threadLeads.add(lead);
		}
		
		//Daily standup, 8AM
		ArrayList<Thread> staff1 = new ArrayList<Thread>(threadLeads);
		staff1.add(suPremeManager);
		Meeting meeting1 = new Meeting(0, 300, 150, staff1);
		meetings.add(meeting1);
		
		//Corporate meeting, 10AM
		ArrayList<Thread> staff2 = new ArrayList<Thread>();
		staff2.add(suPremeManager);
		Meeting meeting2 = new Meeting(1200, 0, 600, staff2);
		meetings.add(meeting2);
		
		//Corporate meeting, 1PM
		ArrayList<Thread> staff3 = new ArrayList<Thread>();
		staff3.add(suPremeManager);
		Meeting meeting3 = new Meeting(3600, 0, 600, staff3);
		meetings.add(meeting3);
		
		//End of day meeting, 4PM
		ArrayList<Thread> staff4 = new ArrayList<Thread>(threadDevs);
		staff4.add(suPremeManager);
		Meeting meeting4 = new Meeting(0, 300, 150, staff4);
		meetings.add(meeting4);
		
	}
	
	public void startDay() {
		startTime = System.currentTimeMillis();
		
	}
	
	public long getTimeUntilNextMeetingSuggestedStart() {
		long timeUntil = 5400;
		for ( Meeting meeting : meetings ) {
			if ( timeUntil > meeting.timeUntilScheduledStart() )
			{
				timeUntil = meeting.timeUntilScheduledStart();
			}
		}
		return timeUntil;
	}
	
	public long getTimeUntilNextMeetingMustStart() {
		long timeUntil = 5400;
		for ( Meeting meeting : meetings ) {
			if ( timeUntil > meeting.timeUntilMustStart() )
			{
				timeUntil = meeting.timeUntilMustStart();
			}
		}
		return timeUntil;
	}
	
	public boolean inMeeting() {
		if ( currentMeeting() == null ) {
			return false;
		}
		return true;
	}
	
	public long timeLeft() { 
		Meeting meeting = currentMeeting();
		if ( meeting == null ) { 
			return 0;
		}
		return meeting.estimatedTimeUntilEnd();
	}
	
	public boolean attemptJoin() {
		Meeting meeting = currentMeeting();
		return meeting.attemptJoin();
	}
	
	private Meeting currentMeeting() {
		Thread dev = Thread.currentThread();
		for ( Meeting meeting : meetings ) {
			if ( meeting.currentlyRunning() && meeting.devsRequired.contains(dev) ) {
				return meeting;
			}
		}
		return null;
	}
	
	public long getTime() {
		long currentTime = System.currentTimeMillis() - startTime;
		return currentTime;
	}
	
	private class Meeting {
		
		public long scheduledStart;
		public long leeway;
		public long actualStart;
		public long duration;
		public List<Thread> devsRequired;
		public List<Thread> devsPresent;
		
		public Meeting(long time, long leeway, long duration, ArrayList<Thread> devsRequired) {
			this.scheduledStart = time;
			this.leeway = leeway;
			this.duration = duration;
			this.devsRequired = devsRequired;
			this.devsPresent = new ArrayList<Thread>();
			this.actualStart = -1;
		}
		
		public long timeUntilScheduledStart() { 
			long timeUntil = scheduledStart - getTime();
			return timeUntil;
		}
		
		public long timeUntilMustStart() {
			long timeUntil = scheduledStart + leeway - getTime();
			return timeUntil;
		}
		
		public boolean attemptJoin() {
			Thread dev = Thread.currentThread();
			if ( devsRequired.contains(dev) ) {
				if ( !devsPresent.contains(dev) ) {
					devsPresent.add(dev);
					if ( devsPresent.size() == devsRequired.size() ) {
						this.actualStart = getTime();
						notifyAll();
					}
				}
				return true;
			}
			return false;
		}
		
		public boolean currentlyRunning() {
			if ( getTime() > scheduledStart ) { //scheduled start is before the current time
				if ( actualStart == -1 ) { //meeting should be starting, but not everyone's here
					return true;
				}
				if ( actualStart + duration < getTime() ) { //meeting started and has not yet reached duration
					return true;
				}
			}
			return false;
		}
		
		public synchronized long estimatedTimeUntilEnd() {
			if ( actualStart != -1 ) {
				long ete = (actualStart + duration) - getTime();
				return ete;
			}
			else {
				return duration;
			}
		}
	}
}
