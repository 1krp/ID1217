import java.util.Random;

public class TableMonitor{
    int numClients;
    Fork[] forks;

    Random r = new Random();

    public TableMonitor(int numClients , Fork[] forks){
        this.numClients = numClients;
        this.forks = forks;
    }
    public boolean forkDistribution(int philosopherID) throws InterruptedException {
        int leftForkID = philosopherID;
        int rightForkID = (philosopherID % numClients) + 1; // Philosopher 1 gets fork 1 and 2, Philosopher 5 gets fork 5 and 1

        synchronized (this) {
            while (!(forks[leftForkID].tryPickUp() && forks[rightForkID].tryPickUp())) { 
                // The philosophers that cant aquire its 2 forks wait here.
                wait();
            }
            pickUpFork(forks[leftForkID]);
            pickUpFork(forks[rightForkID]);

            System.out.println("TableMonitor: Philosopher " + philosopherID + " picked up: " + leftForkID + " and " + rightForkID);

        }

        int sleepDuration = r.nextInt(1000)+1000;
        Thread.sleep(sleepDuration);

        synchronized (this) {
            putDownFork(forks[leftForkID]);
            putDownFork(forks[rightForkID]);

            System.out.println("TableMonitor: Philosopher " + philosopherID + " put down: " + leftForkID + " and " + rightForkID);

            notifyAll();
            return true;
        }

    }


    private void pickUpFork(Fork fork){
        fork.pickUp();
    }
    private void putDownFork(Fork fork){
        fork.putDown();
    }   

}