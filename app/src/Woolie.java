/**
 * Created by kocsenc on 9/19/14.
 */

public class Woolie extends Thread {
    private String name;
    private int speed;
    private String destinationCity;

    Woolie(String name, int speed, String destinationCity){
        this.name = name;
        this.speed = speed;
        this.destinationCity = destinationCity;
    }


}

