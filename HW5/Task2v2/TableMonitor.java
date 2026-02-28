import java.util.Random;

public class TableMonitor{
    int numClients;
    Fork[] forks;
    int[] timesEaten;
    Random r = new Random();

    public TableMonitor(int numClients , Fork[] forks){
        this.numClients = numClients;
        this.forks = forks;
        this.timesEaten = new int[numClients + 1];
    }

    private boolean canEat(int philosopherID, int left, int right) {
        int minEaten = 999999;
        boolean forksAvalible = forks[left].tryPickUp() && forks[right].tryPickUp();
    
        if (forksAvalible) {
            for(int i = 1; i < timesEaten.length; i++) {
                if(minEaten > timesEaten[i]){
                    minEaten = timesEaten[i];
                }
            }
            if(timesEaten[philosopherID] == minEaten) {
                return true;
            }
        } 
        return false;
    }

    public long forkDistribution(int philosopherID) throws InterruptedException {
        int leftForkID = philosopherID;
        int rightForkID = (philosopherID % numClients) + 1; // Philosopher 1 gets fork 1 and 2, Philosopher 5 gets fork 5 and 1
        long timeInQ;

        synchronized (this) {
            long start = System.nanoTime();
            while (!canEat(philosopherID, leftForkID, rightForkID)) { 
                //can eats returns true if a philosopher can aquire its forks and is the 
                //philosopher that has eaten the least amount of times among all the philosophers.
                //Hence, here is where the other philosopher waits, that are not allowed to eat.
                wait();
            }
            long stop = System.nanoTime();
            timeInQ = (stop - start)/1000;

            pickUpFork(forks[leftForkID]);
            pickUpFork(forks[rightForkID]);

            System.out.println("TableMonitor: Philosopher " + philosopherID + " picked up: " + leftForkID + " and " + rightForkID);

        }

        int sleepDuration = r.nextInt(1000)+1000;
        //Thread.sleep(sleepDuration);

        synchronized (this) {
            timesEaten[philosopherID]++;
            putDownFork(forks[leftForkID]);
            putDownFork(forks[rightForkID]);

            System.out.println("TableMonitor: Philosopher " + philosopherID + " put down: " + leftForkID + " and " + rightForkID);

            notifyAll();
            return timeInQ;
        }

    }

    private void pickUpFork(Fork fork){
        fork.pickUp();
    }
    private void putDownFork(Fork fork){
        fork.putDown();
    }   

}