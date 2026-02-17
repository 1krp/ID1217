



public static void main(String[] args){
    final int V = 6;            // #Docks
    final int mN = 1000;         // MaxCapacity
    final int mQ = 1000;         // MaxCapacity
    final int N = 1000;          // StraringAmount
    final int Q = 1000;          // StraringAmount
    final AtomicInteger nShip = new AtomicInteger(100);       // #Ships

    refuelStationMonitor refeulStation = new refuelStationMonitor(mN,mQ,N,Q,V,nShip); // mN,mQ,N,Q,slots
    
    Thread supplyShip = new Thread(new supplyShipObj(refeulStation,69,600,600));
    supplyShip.start();
    System.out.println(" supplyShip has been started");

    Thread[] ships = new Thread[nShip.get()];
    for(int i = 0; i < nShip.get(); i++){
        ships[i] = new Thread(new shipObj(refeulStation, i));
    }
    System.out.println("ships have been created");
    for(int i = 0; i < nShip.get(); i++){
        ships[i].start();
        if (i == nShip.get()-1){ 
            System.out.println("ships have been started");
        }
    }
    
}