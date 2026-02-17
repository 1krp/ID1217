import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class refuelStationMonitor {
    private int maxN;
    private int maxQ;
    private int stationNitrogenLevel;
    private int stationQuantumLevel;
    private final int dockingSlots;
    private int occupiedSlots;
    public AtomicInteger doneRefulingCounter;

    Random r = new Random();

    public refuelStationMonitor(int mN, int mQ, int N, int Q, int D, AtomicInteger nShips){
        this.maxN = mN;
        this.maxQ = mQ;
        this.stationNitrogenLevel = N;
        this.stationQuantumLevel = Q;
        this.dockingSlots = D;
        this.occupiedSlots = 0;
        this.doneRefulingCounter = nShips;
    }


    public synchronized void getFuel(shipObj ship) throws InterruptedException {

        while (occupiedSlots == dockingSlots || 
        stationNitrogenLevel < ship.nitrogenCapacity || 
        stationQuantumLevel < ship.quantumCapacity){
            System.out.println("ship id: " + ship.id + " checks status");
            wait();
        }

        

        occupiedSlots++;
        System.out.println("ship id: " + ship.id + " got slot: " + occupiedSlots);

        try {
            stationNitrogenLevel -= ship.nitrogenCapacity;
            stationQuantumLevel -= ship.quantumCapacity;
            System.out.println("Station N capacity: " + stationNitrogenLevel);
            System.out.println("Station Q capacity: " + stationQuantumLevel);

        } finally {
            ship.refulingCounter++;
            int sleepDuration = r.nextInt(1000)+1000;
            Thread.sleep(sleepDuration);
            occupiedSlots--;
            System.out.println("Slots now avalible: " + (dockingSlots - occupiedSlots));
            notifyAll();
        }

    }
    public synchronized void depositFuel(supplyShipObj supplyShip) throws InterruptedException {
        while(occupiedSlots == dockingSlots){

            System.out.println("ship id: " + supplyShip.id + " checks status for docks");
            wait();
        }

        occupiedSlots++;
        System.out.println("ship id: " + supplyShip.id + " got slot: " + occupiedSlots);

        while(true){
            if(stationNitrogenLevel + supplyShip.TransportNitrogenCapacity < maxN || 
            stationQuantumLevel + supplyShip.TransportQuantumCapacity < maxQ){
                break;
            }
            wait();
        }

        try {
            System.out.println("depositing");
            if(stationNitrogenLevel + supplyShip.TransportNitrogenCapacity < maxN){
                stationNitrogenLevel += supplyShip.TransportNitrogenCapacity;
            } else {
                System.out.println("N full");
            }
            if(stationQuantumLevel + supplyShip.TransportQuantumCapacity < maxQ){
                stationQuantumLevel += supplyShip.TransportQuantumCapacity;
            } else {
                 System.out.println("Q full");
            }
            System.out.println("Station N capacity: " + stationNitrogenLevel);
            System.out.println("Station Q capacity: " + stationQuantumLevel);

            System.out.println("refuling");
            stationNitrogenLevel -= supplyShip.nitrogenCapacity;
            stationQuantumLevel -= supplyShip.quantumCapacity;
            System.out.println("Station N capacity: " + stationNitrogenLevel);
            System.out.println("Station Q capacity: " + stationQuantumLevel);

        } finally {
            supplyShip.depositingCounter++;
            int sleepDuration = r.nextInt(1000)+1000;
            Thread.sleep(sleepDuration);
            occupiedSlots--;
            System.out.println("Slots now avalible: " + (dockingSlots - occupiedSlots));
            notifyAll();
        }
        
    }

}   
