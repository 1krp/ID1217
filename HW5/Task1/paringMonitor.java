

public class paringMonitor{
    private int numClients;
    private int pairedClients = 0;
    private int waitingClientID = -1;
    private boolean watingClient;
    private boolean noPair;

    public paringMonitor(int numClients){
        this.numClients = numClients;
    }

    public synchronized int pairClient(int clientID) throws InterruptedException {
        while (noPair) { 
            wait();
        }
        pairedClients++;
        if(!watingClient){
            if(numClients % 2 != 0 && pairedClients == numClients){
                System.out.println("Im the odd client: " + clientID);
                return -1;
            }
            
            watingClient = true;
            waitingClientID = clientID;

            while(watingClient){ 
                System.out.println(clientID + " is wating for partner");
                wait();
            }

            return waitingClientID;
        }
        if(watingClient){}
            System.out.println("Im " + clientID + " and " + waitingClientID + " is wating for me");
            int partnerID = waitingClientID;
            waitingClientID = clientID;
            watingClient = false; 
            notifyAll();

        return partnerID;
    }
}
