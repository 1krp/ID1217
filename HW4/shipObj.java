
import java.util.Random;

public class shipObj implements Runnable{
    refuelStationMonitor station;
    
    public int id;
    public int nitrogenCapacity;
    public int quantumCapacity;
    public long timeInQ;
    private int rounds;
    public int refuelingCounter;

    Random r = new Random();
    
    public shipObj(refuelStationMonitor station, int id, int rounds){
        this.station = station;
        this.id = id;
        this.nitrogenCapacity = r.nextInt(10);
        this.quantumCapacity = r.nextInt(10);
        this.refuelingCounter = 0;
        this.timeInQ = 0;
        this.rounds = rounds;
    }

    @Override
    public void run() {
        while(refuelingCounter < rounds){       
            try {
                int sleepDuration = r.nextInt(1000)+1000;
                //System.out.println("ship: " + this.id + " sleeps for: " + sleepDuration/1000 + " seconds");
                Thread.sleep(sleepDuration);
                station.getFuel(this);
            } catch (InterruptedException  e) {
                System.out.println(e.getMessage());
            }
        }
        int left = station.shipsLeft.get() - 1;
        System.out.println("Ship: " + this.id + " is done, ships left: " + left);
        station.shipsLeft.getAndDecrement();
    }
}