/**
 * Developer Class
 * <p/>
 * Work on: Kocsen
 */

public class Developer extends Thread {

	private boolean locked = false;
    public long entered; // Time entered
    public boolean arrived = false;
    public boolean askedQuestion = false;
    public boolean askingQuestion = false;
    public String name;
    private Firm firm;
    private TeamLead teamLead;

    public Developer(String name) {
        this.name = name;
        arrived = false;
        askingQuestion = false;
    }

    public void setTeamLead(TeamLead tl) {
        this.teamLead = tl;
    }

    /**
     * Setter for the firm
     *
     * @param firm
     */
    public void setFirm(Firm firm) {
        this.firm = firm;
    }

    public void run() {
        try {
            // Wait for up to 30 mins.
            Thread.sleep(Util.randomInBetween(0, FirmTime.HALF_HOUR.ms()));
            // Arrive
            this.entered = firm.getTime();
            this.arrived = true;
            System.out.printf("Developer %s has arrived to the workplace at %d%n", name, entered);

            System.out.printf("Developer %s has knocked%n", name);
            teamLead.knock(); //Wait until meeting, release when meeting over
            while(locked){
            	wait();
            }
            System.out.printf("Developer %s is leaves standup%n", name);


            // Randomly asking a question
            //this.askQuestion();

            System.out.printf("Developer %s will begin waiting until 4pm meeting%n", name);
            while (firm.getTime() < 8 * FirmTime.HOUR.ms()) {
                Thread.sleep(FirmTime.MINUTE.ms());
            }
            firm.attemptJoin();
            System.out.printf("Developer %s done for the day%n", name);




        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void unlock() {
        locked = false;
    }
    

    private Condition waitForUnlock = new Condition() {
        @SuppressWarnings("unused")
        private boolean isMet() {
            return !locked;
        }
    };

    public synchronized void lock() {
        waitForUnlock.waitUntilMet(100);
        locked = true;
    }

    private void askQuestion() {
        if (!askedQuestion) {
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
}
