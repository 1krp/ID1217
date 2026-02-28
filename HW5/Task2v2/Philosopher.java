import java.io.*;
import java.net.*;
import java.util.Random;

public class Philosopher implements Runnable{
    private int philosopherID;
    private int timesEaten;
    private long totTimeInQ;
    Random r = new Random();
    private int port;

    public Philosopher(int philosopherID, int port){
        this.philosopherID = philosopherID;
        this.port = port;
        this.timesEaten = 0;
        this.totTimeInQ = 0;
    }

    public void run(){
        for(int i = 0; i < 4; i++){
            while (timesEaten < 500) { 
                long timeInQ = Long.MAX_VALUE;
                try {
                    Socket clientSocket = new Socket("localhost", port);
                    DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
                    //System.out.println("Philosopher: " + philosopherID + " wants to eat");
                    out.writeInt(philosopherID);

                    DataInputStream in = new DataInputStream(clientSocket.getInputStream());
                    timeInQ = in.readLong();
                } catch (Exception e) {System.out.println("client err: " + e.getMessage());}
                timesEaten++;
                totTimeInQ += timeInQ;
                if(timesEaten == 500) {
                    //System.out.println("Philosopher: " + philosopherID + " times eaten: 1000");
                    System.out.println("Philosopher: " + philosopherID + " avg time in Q: " + totTimeInQ);
                    timesEaten = 0;
                    totTimeInQ = 0;
                    try { Thread.sleep(2000); } catch (InterruptedException e) {;}
                }else{//System.out.println("Philosopher: " + philosopherID + " time in Q: " + timeInQ);}
                
                
                int sleepDuration = r.nextInt(5000)+2000;
                //System.out.println("Phiosopher now thinks for " + sleepDuration/1000 + " seconds");
                //try { Thread.sleep(sleepDuration); } catch (InterruptedException e) {;}
                }
            }
        }
        
    }
}