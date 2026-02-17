
import java.util.Random;

public class shipObj implements Runnable{
    refuelStationMonitor station;
    
    public int id;
    public int nitrogenCapacity;
    public int quantumCapacity;
    public int refulingCounter;

    Random r = new Random();
    
    public shipObj(refuelStationMonitor station, int id){
        this.station = station;
        this.id = id;
        this.nitrogenCapacity = r.nextInt(10);
        this.quantumCapacity = r.nextInt(10);
        this.refulingCounter = 0;
    }

    @Override
    public void run() {
        while(refulingCounter < 5){       
            try {
                int sleepDuration = r.nextInt(5000)+3000;
                //System.out.println("ship: " + this.id + " sleeps for: " + sleepDuration/1000 + " seconds");
                Thread.sleep(sleepDuration);
                station.getFuel(this);
            } catch (InterruptedException  e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println("ship: " + this.id + " is done");
        station.doneRefulingCounter.getAndDecrement();
    }
}