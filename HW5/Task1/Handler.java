import java.io.*;
import java.net.*;
public class Handler implements Runnable {
    private Socket socket;
    private paringMonitor paringMonitor;
    public Handler(Socket socket, paringMonitor paringMonitor) { this.socket = socket; this.paringMonitor = paringMonitor;}
    public void run() {
        try {
            
            DataInputStream in = new DataInputStream(socket.getInputStream());
            int clientID = in.readInt();
            int pairedClient = -1;
            try{ pairedClient = paringMonitor.pairClients(clientID); } 
            catch( InterruptedException e) {System.out.println("paring error" + e.getMessage()); }
            
            System.out.println("Handler: Client " + clientID + " Partner: " + pairedClient);

            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            out.writeInt(pairedClient);

            socket.close();
        } catch ( IOException e ) {;}
    }
}
