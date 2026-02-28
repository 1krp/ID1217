import java.io.*;
import java.net.*;


public class Client implements Runnable{
    private int clientID;
    private int port;
    private int[] partnerCounterArr;

    public Client(int clientID, int port, int numClients){
        this.clientID = clientID;
        this.port = port;
        this.partnerCounterArr = new int[numClients + 1]; // there is no client 0
    }

    public void run(){
        for(int i = 0; i < 1000; i++){
            int clientPartner = -1;
            try {
                Socket clientSocket = new Socket("localhost", port);

                DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
                out.writeInt(clientID);

                DataInputStream in = new DataInputStream(clientSocket.getInputStream());
                clientPartner = in.readInt();

            } catch (Exception e) {System.out.println("client err: " + e.getMessage());}
            System.out.println("Client: " + clientID + " Partner: " + clientPartner);
            partnerCounterArr[clientPartner]++;
        }

        for(int i = 1; i < partnerCounterArr.length; i++){
            System.out.println("Client: " + clientID + " got paired with " + i + " " + partnerCounterArr[i] + " times");
        }
    }
}