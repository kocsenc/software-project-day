/**
 * Created by kocsenc on 9/19/14.
 */
public class Woolie { 
	
    public void run(){
    	int start = 0;
    	System.out.println(this.name + " has arrived at the bridge.");
    	System.out.println(this.name + " is starting to cross.");
    	while(start!=this.speed){
    		start+=1;
    		System.out.println("\t"+this.name + " "+start+" seconds.");
    		Thread.sleep(1000);
    	}
    	System.out.println(this.name + " leaves at "+this.destinationCity+".");
    }
}
