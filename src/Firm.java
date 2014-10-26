import java.util.ArrayList;
import java.util.List;

/**
 * Firm Class
 * 
 * Work on: John
 */

public class Firm {
	private SoftwareProjectManager suPremeManager;
	//private List<Developer> developers;
	private List<TeamLead> leads;
	private List<Thread> threadDevs;
	private List<Thread> threadLeads;
	private long startTime;
	private long firstStart;
	private long lastStart;
	private List<Thread> devsInBigMeeting;

	/**
	 * @param suPremeManager
	 *        SoftwareProjectManager that's head of the office
	 */
	public Firm(SoftwareProjectManager suPremeManager) {
		devsInBigMeeting = new ArrayList<Thread>();
		threadDevs = new ArrayList<Thread>();
		threadLeads = new ArrayList<Thread>();
		firstStart = -15;
		lastStart = -1;
		this.suPremeManager = suPremeManager;
		threadDevs.add(suPremeManager);
		this.leads = suPremeManager.getTeamLeaders();
	}
	
	/**
	 * Starts the simulation
	 */
	public void startDay() {
		addAllFirms();
		startTime = System.currentTimeMillis();
		
		addAllFirms();
		for ( Thread thread : threadDevs ) {
			thread.start();
		}
	}
	
	/**
	 * Sets all the dev threads's Firms to this Firm.
	 */
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
	 * Attempts to join a meeting in progress.  Returns false if there is no current meeting the
	 * thread is in.  True otherwise.  
	 * @return False if there is no current meeting the thread is in.  True otherwise.  
	 */
	public void attemptJoin() {
		if ( getTime() < FirmTime.HOUR.ms() * 8 - 10) { //if it is the first meeting of the day for developers
			synchronized(this){
				while ( getTime() - firstStart < FirmTime.MINUTE.ms() * 15)
				{
					try {
						wait(FirmTime.MINUTE.ms() * 15 - (getTime() - firstStart));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				firstStart = getTime();
			}
		}
		
		else { //if it is the final meeting of the day for developers
			synchronized(this) {
				this.devsInBigMeeting.add(Thread.currentThread());
				if ( devsInBigMeeting.size() == threadDevs.size() ) {
					lastStart = getTime();
					try {
						wait(FirmTime.MINUTE.ms() * 15);
					} catch (InterruptedException e) {
					}
					notifyAll();
				}
				else{
					try {
						//System.out.println("attending: "+Thread.currentThread().getName());
						wait();
					} catch (InterruptedException e) {
					}
				}
			}
		}
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
	 * Returns true if the last meeting of the done has been initiated and finished.
	 * @return True if the last meeting of the done has been initiated and finished.
	 */
	public synchronized boolean isLastMeetingDone() {
		if ( lastStart == -1 || getTime() - lastStart < FirmTime.MINUTE.ms() * 15 ) {
			return false;
		}
		return true;
	}
}
