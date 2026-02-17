import java.util.Random;

public class supplyShipObj implements Runnable{
    refuelStationMonitor station;

    public int id;
    public int nitrogenCapacity;
    public int quantumCapacity;
    public int TransportNitrogenCapacity;
    public int TransportQuantumCapacity;
    public int depositingCounter;

    Random r = new Random();
    
    public supplyShipObj(refuelStationMonitor station, int id, int tN, int tQ){

        this.station = station;
        this.id = id;
        this.nitrogenCapacity = r.nextInt(10);
        this.quantumCapacity = r.nextInt(10);
        this.TransportNitrogenCapacity = tN;
        this.TransportQuantumCapacity = tQ;
        this.depositingCounter = 0;
    }

    @Override
    public void run() {
        while(station.doneRefulingCounter.get() != 0){       
            try {
                int sleepDuration = r.nextInt(5000)+1000;
                //System.out.println("ship: " + this.id + " sleeps for: " + sleepDuration/1000 + " seconds");
                Thread.sleep(sleepDuration);
                station.depositFuel(this);
            } catch (InterruptedException  e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println("ALL DONE");
    }
}