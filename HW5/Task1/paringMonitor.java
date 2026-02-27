

public class paringMonitor{
    int pairedClients = 0;
    int numClients;
    int[] clientPair = new int[2];

    public paringMonitor(int numClients){
        this.numClients = numClients;
    }

    private void clearArr(int[] arr){
        for (int i = 0; i < arr.length; i++) {
            arr[i] = 0;
        }
    }

    public synchronized int pairClients(int clientID) throws InterruptedException {
        while (clientPair[0] != 0 && clientPair[1] != 0) { 
            wait();
        }
        pairedClients++;

        if(clientPair[0] == 0){
            // first client in Pair
            clientPair[0] = clientID;

            if(numClients % 2 != 0 && pairedClients == numClients){
                // If this is the last client and numClients is odd
                clientPair[1] = clientID;
                clearArr(clientPair);
                pairedClients = 0;
                
                notifyAll();
                return clientID;
            } else {
                // return the pair to the first client
                while (clientPair[1] == 0) { 
                    wait();
                }

                int secondClient = clientPair[1];
                clearArr(clientPair);
                
                notifyAll();
                return secondClient;
            }
        } 
        // One client is already waiting, set second client in pair and return to second client
        else {
            clientPair[1] = clientID;
            int firstClient = clientPair[0];

            notifyAll();

            return firstClient;
        }
    }
}