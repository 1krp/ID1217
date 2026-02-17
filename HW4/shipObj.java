public class shipObj{
    public int id;
    public int nitrogenCapacity;
    public int quantumCapacity;
    
    public shipObj(int id, int N, int Q){
        this.id = id;
        this.nitrogenCapacity = N;
        this.quantumCapacity = Q;
    }

    public void ship() {
        /*
        try {
            refuelStationMonitor.getFuel(this);
        } catch (InterruptedException  e) {
            Thread.currentThread().interrupt();
        }
        */
    }
}