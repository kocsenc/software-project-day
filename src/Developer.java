/**
 * Developer Class
 *
 * Work on: Kocsen
 */

public class Developer extends Thread {

    public boolean locked = false;

    public long entered; // Time entered
    public boolean arrived = false;
    public boolean askedQuestion = false;
    public boolean askingQuestion = false;
    public String name;
    private Firm firm;
    private TeamLead teamLead;



    public Developer(String name, Firm firm, TeamLead teamLead) {
        this.name = name;
        this.firm = firm;
        this.teamLead = teamLead;
        arrived = false;
        askingQuestion = false;

    }

    public void run() {
        try {
            // Wait for up to 30 mins.
            Thread.sleep(Util.randomInBetween(0, FirmTime.HALF_HOUR.ms()));
            // Arrive
            this.entered = firm.getTime();
            this.arrived = true;
            System.out.printf("Developer %s has arrived to the workplace at %d%n", name, entered);


            // Randomly asking a question
            this.askQuestion();



        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void askQuestion() {
        if(!askedQuestion){
            this.teamLead.askedQuestion();
        }
    }

    /**
     * Getter for whether the dev has arrived in the morning
     *
     * @return true if arrived, false otherwise
     */
    public boolean getArrived() {
        return this.arrived;
    }
    // Waits
    protected Condition waitForUnlock = new Condition(){
    	private boolean isMet(){
			return !locked;
		}
	};
}
