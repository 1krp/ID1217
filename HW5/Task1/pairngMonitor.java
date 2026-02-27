

public class pairngMonitor{
    private int pairedClients = 0;
    private int numClients;

    private int first = 0;
    private int second = 0;

    public pairngMonitor(int numClients){
        this.numClients = numClients;
    }


    public synchronized int pairClients(int clientID) throws InterruptedException {
        //catches other clients so that they wait for the first two clients to pair.
        while (first != 0 && second != 0) { 
            wait();
        }
        pairedClients++;

        if(first == 0){
            // first client in Pair, waits for its partner
            first = clientID;

            if(numClients % 2 != 0 && pairedClients == numClients){
                // If this is the last client and numClients is odd, return its partner as itself
                pairedClients = 0;
                first = 0;
                second = 0;
                
                notifyAll();
                return clientID;
            } else {
                // wait for a second partner to pair with
                while (second == 0) { 
                    wait();
                }
                int copySecond = second;
                first = 0;
                second = 0;

                notifyAll();
                return copySecond; // return second as partner to the first client
            }
        } 
        // One client is already waiting, set second client in pair and return to second client
        else {
            //second client, gives first client its pair and returns first to itself
            second = clientID;

            notifyAll();
            return first;
        }
    }
}