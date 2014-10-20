import java.util.Random;

/**
 * Created by kocsen on 10/20/14
 * Util class meant for helping out with random utilities like getting a random number range
 */
public class Util {

    /**
     * Get random integer between number min and max [INCLUSIVE]
     *
     * @param min - min
     * @param max - max
     * @return int to put into your wait (most likely)
     */
    public static int randomInBetween(int min, int max) {
        // NOTE: Usually this should be a field rather than a method
        // variable so that it is not re-seeded every call.
        // @Credit from
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        return rand.nextInt((max - min) + 1) + min;
    }
    
    
    /**
     * Return the string value of a time, given how many milliseconds it is into the day (e.g. 9:52 A.M.)
     * 
     * @param time Time of day, represented in miliseconds from start of day.
     * @return
     */
    public static String timeToString(long time) {
    	String tooMuchGoodStuff; //AM or PM
    	String hourString;
    	String minuteString;
    	
    	long hour = (time / 600) + 8;    	
    	if (hour > 11) {
    		tooMuchGoodStuff = "P.M.";
    	}
    	else {
    		tooMuchGoodStuff = "A.M.";
    	}
    	if (hour > 12) {
    		hour -= 12;
    	}

    	hourString = Long.toString(hour);
    	
    	int minute = (int)(time % 600) / 10;
    	minuteString = String.format("%02d", minute);
    	
    	return hourString + ":" + minuteString + " " + tooMuchGoodStuff;
    }


}
