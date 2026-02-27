import java.io.*;
import java.net.*;


public class Philosopher implements Runnable{
    private int plateID;
    private fork leftFork;
    private fork rightFork;
    private int port;

    public Philosopher(int plateID, fork leftFork, fork rightFork, int port){
        this.plateID = plateID;
        this.leftFork = leftFork;
        this.rightFork = rightFork;
        this.port = port;
    }
    void think(){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {System.out.println("Sleep error: " + e.getMessage());}
    }
    void eat(){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {System.out.println("Sleep error: " + e.getMessage());}
    }
    void pickUpLeftFork(){
        leftFork.pickUp();
    }
    void pickUpRightFork(){
        rightFork.pickUp();
    }
    void putDownLeftFork(){
        leftFork.putDown();
    }   
    void putDownRightFork(){
        rightFork.putDown();
    }


    public void run(){
        int clientPartner = -1;
        try {
            Socket clientSocket = new Socket("localhost", port);

            DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
            out.writeInt(plateID);

            DataInputStream in = new DataInputStream(clientSocket.getInputStream());
            clientPartner = in.readInt();

        } catch (Exception e) {System.out.println("client err: " + e.getMessage());}
        //System.out.println("Philosopher: " + plateID + " Partner: " + clientPartner);
    }
}