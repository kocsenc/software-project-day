public class Condition {
	private boolean isMet(){
		return true;
	}

	public void waitUntilMet(long delay){
		while(!isMet()){
			try {
				wait(delay);
			} catch (InterruptedException e) {
				
			}
		}
	}
}
