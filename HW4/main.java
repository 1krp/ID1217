
public static void main(String[] args){
    refuelStationMonitor refeulStation = new refuelStationMonitor(100,100,100,100,4);
    shipObj ship1 = new shipObj(1,10,5);
    supplyShipObj supplyship1 = new supplyShipObj(2,10,10,50,50);
    
    try {
            refeulStation.getFuel(ship1);
    } catch (InterruptedException  e) {
            Thread.currentThread().interrupt();
        }
    try {
            refeulStation.getFuel(ship1);
    } catch (InterruptedException  e) {
            Thread.currentThread().interrupt();
        }
    try {
            refeulStation.getFuel(ship1);
    } catch (InterruptedException  e) {
            Thread.currentThread().interrupt();
        }
}