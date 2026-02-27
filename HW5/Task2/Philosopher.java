import java.io.*;
import java.net.*;


public class Philosopher implements Runnable{
    private int plateID;
    private int port;

    public Philosopher(int plateID, int port){
        this.plateID = plateID;
        this.port = port;
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