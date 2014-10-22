/**
 * Developer Class
 *
 * Work on: Kocsen
 */

public class Developer extends Thread {
	protected long entered;
    protected boolean locked = false;

    // Waits
    protected Condition waitForUnlock = new Condition(){
    	private boolean isMet(){
			return !locked;
		}
	};
}
