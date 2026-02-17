public class refuelStationMonitor {
    private int maxN;
    private int maxQ;
    private int stationNitrogenLevel;
    private int stationQuantumLevel;
    private final int dockingSlots;
    private int occupiedSlots;

    public refuelStationMonitor(int mN, int mQ, int N, int Q, int D){
        this.maxN = mN;
        this.maxQ = mQ;
        this.stationNitrogenLevel = N;
        this.stationQuantumLevel = Q;
        this.dockingSlots = D;
        this.occupiedSlots = 0;
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
            occupiedSlots--;
            System.out.println("Slots now avalible: " + (dockingSlots - occupiedSlots));
            notifyAll();
        }


    }
    public synchronized void depositFuel() {
        while()
    }

}   
