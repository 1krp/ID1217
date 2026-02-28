

public class TableMonitor{
    int numClients;
    Fork[] forks;
    int[] clientPair = new int[2];

    public TableMonitor(int numClients , Fork[] forks){
        this.numClients = numClients;
        this.forks = forks;
    }

    private void clearArr(int[] arr){
        for (int i = 0; i < arr.length; i++) {
            arr[i] = 0;
        }
    }

    public synchronized int forkDistribution(int plateID) throws InterruptedException {



        return forkDistribution(plateID, 0);
    }

    
    void pickUpLeftFork(Fork leftFork){
        leftFork.pickUp();
    }
    void pickUpRightFork(Fork rightFork){
        rightFork.pickUp();
    }
    void putDownLeftFork(Fork leftFork){
        leftFork.putDown();
    }   
    void putDownRightFork(Fork rightFork){
        rightFork.putDown();
    }
}