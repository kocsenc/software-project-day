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


}
