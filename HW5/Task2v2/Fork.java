import java.util.concurrent.atomic.AtomicBoolean;

public class Fork {

    private AtomicBoolean isAvalible;

    public Fork(){
        this.isAvalible = new AtomicBoolean(true);
    }
    
    public void pickUp(){
        this.isAvalible.set(false);
    }
    public void putDown(){
        this.isAvalible.set(true);
    }
    public boolean tryPickUp(){
        return this.isAvalible.get();
    }
}   
