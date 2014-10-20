public class Condition {
	private boolean isMet(){
		return true;
	}

	public boolean waitUntilMet(long delay){
		while(!isMet()){
			wait(delay);
		}
	}
}
