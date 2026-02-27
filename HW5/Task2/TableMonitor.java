

public class TableMonitor{
    int pairedClients = 0;
    int numClients;
    int[] clientPair = new int[2];

    public TableMonitor(int numClients){
        this.numClients = numClients;
    }

    private void clearArr(int[] arr){
        for (int i = 0; i < arr.length; i++) {
            arr[i] = 0;
        }
    }

    public synchronized int forkDistribution(int plateID) throws InterruptedException {
        
    }
}