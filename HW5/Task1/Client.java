import java.io.*;
import java.net.*;


public class Client implements Runnable{
    private int clientID;
    private int port;

    public Client(int clientID, int port){
        this.clientID = clientID;
        this.port = port;
    }

    public void run(){
        System.out.println("Im client: " + clientID);
        int clientPartner = -1;
        
        try {
            Socket clientSocket = new Socket("localhost", port);

            DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
            out.writeInt(clientID);

            DataInputStream in = new DataInputStream(clientSocket.getInputStream());
            clientPartner = in.readInt();

        } catch (Exception e) {System.out.println("client err: " + e.getMessage());}
        System.out.println("Client: " + clientID + " Partner: " + clientPartner);
    }
}