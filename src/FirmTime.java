/**
 * Created by kocsen on 10/20/14.
 * <p/>
 * Firm Time used to convert Real time to Simulation time
 * 10ms simulation = 1min real time
 */
public enum FirmTime {


    MINUTE(10),
    HOUR(600),
    HALF_HOUR(300),
    FULL_DAY(4800);

    private final int msTime;

    FirmTime(int msTime) {
        this.msTime = msTime;
    }

    /**
     * USAGE:
     *
     * To get the value of an hour do this:
     *
     ****** FirmTime.MINUTE.ms() *******s
     *
     * @return - the time in ms (as an int) that is represented in the firm
     */
    public int ms() {
        return msTime;
    }

}
