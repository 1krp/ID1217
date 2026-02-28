import java.util.concurrent.Semaphore;

public class Fork {
    Semaphore forkSemaphore;
    public Fork(int philosopherID){
        forkSemaphore = new Semaphore(1, true);
    }   
    public boolean pickUp(){
        try {
            forkSemaphore.acquire();
            return true;
        } catch (InterruptedException e) {System.out.println("pickUp error: " + e.getMessage()); return false;}
    }
    public void putDown(){
        forkSemaphore.release();
    }
}   
