import java.util.ArrayList;

/**
 * Created by Kocsen & Conor on 9/19/14.
 * Woolie crossing the bridge
 */

public class Woolie extends Thread {
    private String name;
    private int speed;
    private String destinationCity;
    private Bridge bridge;

    Woolie(String name, int speed, String destinationCity, Bridge bridge) {
        this.name = name;
        this.speed = speed;
        this.destinationCity = destinationCity;
        this.bridge = bridge;
    }

    public void run() {
        int start = 0;
        System.out.println(this.name + " has arrived at the bridge.");
        try {
            bridge.enterBridge(this);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

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
        bridge.leaveBridge(this);
    }

    public static void main(String[] args) {
        ArrayList<Woolie> woolies = new ArrayList<Woolie>();
        Bridge b = new Bridge(3);
        for (int i = 0; i < 5; i++) {
            woolies.add(new Woolie("Woolie-" + i, 10 - i, "DatCity", b));
            woolies.get(i).start();
        }
    }

}

