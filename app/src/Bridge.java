/**
 * Created by kocsenc on 9/23/14.
 */
public class Bridge {
    private static boolean woolieOnBridge = false;

    public static synchronized boolean enterBridge() {
        if (Bridge.woolieOnBridge) {
            return false;
        } else {
            Bridge.woolieOnBridge = true;
            return true;
        }
    }

    public static void leaveBridge() {
        Bridge.woolieOnBridge = false;
    }
}
