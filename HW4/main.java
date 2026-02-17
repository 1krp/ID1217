



public static void main(String[] args){
    final int V = 6;            // #Docks
    final int mN = 500;         // MaxCapacity
    final int mQ = 500;         // MaxCapacity
    final int N = 300;          // StraringAmount
    final int Q = 300;          // StraringAmount
    final AtomicInteger nShip = new AtomicInteger(10);       // #Ships

    refuelStationMonitor refeulStation = new refuelStationMonitor(mN,mQ,N,Q,V,nShip); // mN,mQ,N,Q,slots
    
    Thread supplyShip = new Thread(new supplyShipObj(refeulStation,99999,100,100));
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