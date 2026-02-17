public class refuleStationMonitor {
    private int nitrogenLevel;
    private int quantumLevel;
    private final int dockingSlots = 6;
    private int occupiedSlots = 0;

    public synchronized void getFule(int N, int Q) throws InterruptedException {
        while (occupiedSlots == dockingSlots || nitrogenLevel < N || quantumLevel <= Q){
            wait();
        }
        
        occupiedSlots++;


    }
    public synchronized void depositFule() {

    }



}   


public static void main(String[] args){

}