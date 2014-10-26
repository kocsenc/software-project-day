import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;


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

public class SoftwareProjectManager extends Thread {

	private List<TeamLead> teamLeaders;
	private CyclicBarrier standupBarrier;
	private Firm firm;
	private List<Thread> awaitingAnswers;
	private final Object wakeUp;
	private Timer alarmClock;
	private AlarmClockTask alarm;
	public static final int STANDUP_LENGTH_MINS = 15;
	public static final int EXEC_MEETING_LENGTH_MINS = 60;
	public static final int ANSWER_QUESTION_LENGTH_MINS = 10;
	public static final int TEN_AM_MEETING = 1200;
	public static final int TWO_PM_MEETING = 3600;
	public static final int FOUR_PM_MEETING = 4800;

	/**
	 * No args constructor
	 */
	public SoftwareProjectManager() {
		super();

		wakeUp = new Object();
		alarmClock = new Timer(false);
		awaitingAnswers = new ArrayList<Thread>();
	}

	public void setFirm(Firm firm) {
		this.firm = firm;
	}

	/**
	 * A method that allows team leaders to knock on the door, when all
	 * team leads arrive, the wait for the SPM will be released a meeting 
	 * with the SoftwareProjectManager.
	 */
	public void knock() {
		// Add to the barrier
		System.out.println("A TeamLead knocked on SPM's door");
		try {
			standupBarrier.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (BrokenBarrierException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see Thread.java
	 */
	public void run() {
		// Arrive at Firm.java at 8AM (by default, nothing to do here)


		// Wait for team leads to arrive
		try {
			System.out.println(Util.timeToString(firm.getTime())+": SPM engages in daily planning activities");
			standupBarrier.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (BrokenBarrierException e) {
			e.printStackTrace();
		}
		System.out.println(Util.timeToString(firm.getTime())+": SPM is out of morning stand-up");

		//
		//		// Wait for question OR next meeting (10:00AM or 2 "Hours")
		//		do {
		//			// Do I need to Answer Question?
		//			synchronized(speakingToken) {
		//				speakingToken.notify();
		//			}
		//
		//
		//			// Set an alarm until the meeting... or someone asks a question
		//			if (alarm != null) {
		//				alarm.turnOff();
		//			}
		//			// Set an alarm
		//			alarm = new AlarmClock(wakeUp, (TEN_AM_MEETING - (firm.getTime()/FirmTime.HOUR.ms())));
		//			alarm.start();
		//			try {
		//				synchronized(wakeUp) {
		//					System.out.println("Wait " + alarm.getDuration() + "ms 'till 10AM meeting");
		//					wakeUp.wait();
		//				}
		//			} catch (InterruptedException e) {
		//				e.printStackTrace();
		//			}
		//
		//			// Leave as soon as we've reached the meeting time or later
		//			// Ignore anyone currently waiting for answers in this case
		//		} while(firm.getTime()/FirmTime.HOUR.ms() < TEN_AM_MEETING);
		//		// No more questions



		//		// Start stand-up meeting
		//		performStandup();
		//
		//		while(firm.getTime()/FirmTime.HOUR.ms() < 9) { // TODO: I thought FirmTime was going to keep track of actual time (Like, FirmTime.currentTime() return 5 for 5PM or whatever)
		//
		//		}
		//
		//		long time = firm.getTime();
		//		// Leave at 5PM


		// Wait until 4PM meeting
		do {
			//			System.out.println("Set an alarm for " + FOUR_PM_MEETING + " - " + firm.getTime() + "/" + FirmTime.HOUR.ms() + "ms");

			try {

				synchronized(wakeUp) {
					// Schedule a new alarm
					alarmClock.schedule(alarm = new AlarmClockTask(wakeUp),
							FOUR_PM_MEETING - (firm.getTime()/FirmTime.HOUR.ms()));

					System.out.println("Now wait for the alarm");
					wakeUp.wait(FOUR_PM_MEETING - (firm.getTime()/FirmTime.HOUR.ms()));
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//			System.out.println("I woke up");

			// Answer questions while there are still more or until it's time for a meeting
			while (awaitingAnswers.size() != 0 && firm.getTime() < FOUR_PM_MEETING) {
				TeamLead tl = (TeamLead) awaitingAnswers.remove(0);
				// Lock the team leader object while we wait for the answer
				try {
					Thread.sleep(ANSWER_QUESTION_LENGTH_MINS);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				// Unlock the teamLeader so they can continue their business
				tl.unlock();
			}

			// Cancel the current alarm and create a new AlarmClock
			if (alarm != null) {
				alarm.cancel();
			}

		} while(firm.getTime() < FOUR_PM_MEETING);

		// Join 4PM Meeting
		System.out.println(Util.timeToString(firm.getTime())+": Manager attempting to join 4PM meeting");
		firm.attemptJoin();
		
		// TODO: This is only test
		for (Thread tl : awaitingAnswers) {
			((TeamLead)tl).unlock();
		}
		
		while(firm.getTime()<5400){
			try {
				synchronized (this){
					wait(10);
				}
			} catch (InterruptedException e) {
			}
		}
		System.out.println(Util.timeToString(firm.getTime())+": Manager is going home");
		System.exit(0);
	}

	public void askQuestion() {
		// Current Thread
		Developer teamLead = (Developer) Thread.currentThread();

		// If someone is already asking...
		if (awaitingAnswers.size() >= 1) {
			// Add teamLead to the list
			awaitingAnswers.add(teamLead);
			// Lock them so they have to wait
			teamLead.lock();
			synchronized (this) {
				try {
					wait();
				} catch (InterruptedException e) {
				}
			}
		}

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
		// Now we can set our barrier (allows dynamic # of TeamLeads)
		// +1 because the SPM is part of the barrier
		standupBarrier = new CyclicBarrier(
				this.teamLeaders.size()+1, new Runnable() {
					@Override
					public void run() {
						// Have stand-up
						try {
							System.out.println(Util.timeToString(firm.getTime())+": We're in a meeting");
							Thread.sleep(FirmTime.MINUTE.ms()*STANDUP_LENGTH_MINS);
						} catch (InterruptedException ignore) { }
					}
				});
	}


	class AlarmClockTask extends TimerTask {
		private Object lock;

		public AlarmClockTask(Object lock) {
			this.lock = lock;
		}

		@Override
		public void run() {
			synchronized(lock) {
				lock.notify();
			}
		}
	}
}
