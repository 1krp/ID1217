import java.io.*;
import java.net.*;
public class Handler implements Runnable {
    private Socket socket;
    private pairingMonitor pairingMonitor;
    public Handler(Socket socket, pairingMonitor pairingMonitor) { this.socket = socket; this.pairingMonitor = pairingMonitor;}
    public void run() {
        try {
            DataInputStream in = new DataInputStream(socket.getInputStream());
            int clientID = in.readInt();
            int pairedClient = -1;
            try{ pairedClient = pairingMonitor.pairClients(clientID); } 
            catch( InterruptedException e) {System.out.println("pairing error: " + e.getMessage()); }
            
            System.out.println("Handler: Client " + clientID + " Partner: " + pairedClient);

            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            out.writeInt(pairedClient);

            socket.close();
        } catch ( IOException e ) {;}
    }
}
