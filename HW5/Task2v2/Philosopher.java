import java.io.*;
import java.net.*;
import java.util.Random;

public class Philosopher implements Runnable{
    private int philosopherID;
    private int timesEaten;
    Random r = new Random();
    private int port;

    public Philosopher(int philosopherID, int port){
        this.philosopherID = philosopherID;
        this.port = port;
        this.timesEaten = 0;
    }

    public void run(){
        while (true) { 
            boolean hasEaten = false;
            try {
                Socket clientSocket = new Socket("localhost", port);
                DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
                System.out.println("Philosopher: " + philosopherID + " wants to eat");
                out.writeInt(philosopherID);

                DataInputStream in = new DataInputStream(clientSocket.getInputStream());
                hasEaten = in.readBoolean();
            } catch (Exception e) {System.out.println("client err: " + e.getMessage());}
            if(hasEaten) timesEaten++;
            System.out.println("Philosopher: " + philosopherID + " has eaten: " + timesEaten + " times");
            int sleepDuration = r.nextInt(5000)+2000;
            System.out.println("Phiosopher now thinks for " + sleepDuration/1000 + " seconds");
            //try { Thread.sleep(sleepDuration); } catch (InterruptedException e) {;}
        }
    }
}