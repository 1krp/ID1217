



public static void main(String[] args) throws InterruptedException{
    final int rounds = 8;       // #Refuling rounds
    final int V = 4;            // #Docks
    final int mN = 500;         // MaxCapacity
    final int mQ = 500;         // MaxCapacity
    final int N = 300;          // StraringAmount
    final int Q = 300;          // StraringAmount
    final AtomicInteger nShip = new AtomicInteger(16);       // #Ships

    refuelStationMonitor refeulStation = new refuelStationMonitor(mN,mQ,N,Q,V,nShip); // mN,mQ,N,Q,slots
    
    Thread supplyShip = new Thread(new supplyShipObj(refeulStation,99999,100,100));
    supplyShip.start();
    System.out.println("supplyShip has been started");

    Thread[] shipThreads = new Thread[nShip.get()];
    shipObj[] shipObjs = new shipObj[nShip.get()];
    for(int i = 0; i < nShip.get(); i++){
        shipObjs[i] = new shipObj(refeulStation, i, rounds);
        shipThreads[i] = new Thread(shipObjs[i]);
    }

    for (Thread t : shipThreads) t.start();
    System.out.println("ships have been created");
    for (Thread t : shipThreads) t.join();
    
    supplyShip.interrupt(); 
    supplyShip.join();

    for(shipObj s : shipObjs) System.out.println("ship: " + s.id + " avg Time in Queue (ms): " + s.timeInQ/rounds);
}