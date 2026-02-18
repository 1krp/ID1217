import java.util.Random;
import java.util.concurrent.atomic.*;

public class refuelStationMonitor {
    private int maxN;
    private int maxQ;
    private int stationNitrogenLevel;
    private int stationQuantumLevel;
    private final int dockingSlots;
    private int occupiedSlots;
    public AtomicInteger shipsLeft;

    Random r = new Random();

    public refuelStationMonitor(int mN, int mQ, int N, int Q, int D, AtomicInteger nShips){
        this.maxN = mN;
        this.maxQ = mQ;
        this.stationNitrogenLevel = N;
        this.stationQuantumLevel = Q;
        this.dockingSlots = D;
        this.occupiedSlots = 0;
        this.shipsLeft = nShips;
    }
    private void printCapacity(){
        System.out.println("    N = " + stationNitrogenLevel);
        System.out.println("    Q = " + stationQuantumLevel);
    }
    private void withdrawFuel(int N, int Q){
        stationNitrogenLevel -= N;
        stationQuantumLevel -= Q;
    }
    private void depositFuel(int N, int Q){
        stationNitrogenLevel += N;
        stationQuantumLevel += Q;        
    }

    public void getFuel(shipObj ship) throws InterruptedException {
        synchronized (this) {
            long start = System.nanoTime();
            while (occupiedSlots == dockingSlots ||
                stationNitrogenLevel < ship.nitrogenCapacity ||
                stationQuantumLevel < ship.quantumCapacity) {
                    //System.out.println("ship id: " + ship.id + " checks status");
                    wait();
            }
            long stop = System.nanoTime();
            ship.timeInQ += (stop - start)/1000000;
            occupiedSlots++;
            withdrawFuel(ship.nitrogenCapacity, ship.quantumCapacity);
        }
        
        ship.refuelingCounter++;
        System.out.println("Ship: "+ship.id + " is Refueling for the " + ship.refuelingCounter + "'th time");
        //printCapacity();

        int sleepDuration = r.nextInt(1000)+1000;
        Thread.sleep(sleepDuration);
        synchronized (this) {
            occupiedSlots--;
            //System.out.println("Free docks: " + (dockingSlots - occupiedSlots));
            notifyAll();
        }
    }

    public void depositFuel(supplyShipObj supplyShip) throws InterruptedException {
        synchronized (this) {
            while (occupiedSlots == dockingSlots && shipsLeft.get() > 1) {
            //System.out.println("ship id: " + ship.id + " checks status");
            wait();
            }
            occupiedSlots++;
            System.out.println("SupplyShip: "+ supplyShip.id + " has docked, and is waiting to refuel");
            
            while ((maxN - supplyShip.TransportNitrogenCapacity < stationNitrogenLevel || 
            maxQ - supplyShip.TransportQuantumCapacity < stationQuantumLevel) && shipsLeft.get() > 1) { 
                wait();
            }
            if(shipsLeft.get() > 1){
                printCapacity();
                System.out.println("depositing");
                depositFuel(supplyShip.TransportNitrogenCapacity, supplyShip.TransportQuantumCapacity);
                printCapacity();

                System.out.println("refueling");
                withdrawFuel(supplyShip.nitrogenCapacity, supplyShip.quantumCapacity);
                //printCapacity();
            }
        }    
        supplyShip.depositingCounter++;
        int sleepDuration = r.nextInt(2000)+2000;
        Thread.sleep(sleepDuration);

        synchronized (this) {
            occupiedSlots--;
            //System.out.println("Slots now avalible: " + (dockingSlots - occupiedSlots));
            notifyAll();
        }
                
    }

}   
