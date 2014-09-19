/**
 * Created by Kocsen & Conor on 9/19/14.
 * Woolie crossing the bridge
 */

public class Woolie extends Thread {
    private String name;
    private int speed;
    private String destinationCity;

    Woolie(String name, int speed, String destinationCity) {
        this.name = name;
        this.speed = speed;
        this.destinationCity = destinationCity;
    }

    public void run() {
        int start = 0;
        System.out.println(this.name + " has arrived at the bridge.");
        System.out.println(this.name + " is starting to cross.");
        while (start != this.speed) {
            start += 1;
            System.out.println("\t" + this.name + " " + start + " seconds.");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(this.name + " leaves at " + this.destinationCity + ".");
    }


}

