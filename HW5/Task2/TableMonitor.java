

public class TableMonitor{
    int numClients;
    Fork[] forks;

    public TableMonitor(int numClients , Fork[] forks){
        this.numClients = numClients;
        this.forks = forks;

    }


    public synchronized int forkDistribution(int philosopherID) throws InterruptedException {
        int  leftForkID = philosopherID;
        int rightForkID = (philosopherID % numClients) + 1; // Philosopher 1 gets fork 1 and 2, Philosopher 5 gets fork 5 and 1
        System.out.println("TableMonitor: Philosopher " + philosopherID + " requests fork " + leftForkID + " and " + rightForkID);
        pickUpFork(forks[leftForkID]);
        pickUpFork(forks[rightForkID]);


        
        putDownFork(forks[leftForkID]);
        putDownFork(forks[rightForkID]);

        return rightForkID;
    }

    
    void pickUpFork(Fork fork){
        fork.pickUp();
    }
    void putDownFork(Fork fork){
        fork.putDown();
    }   

}