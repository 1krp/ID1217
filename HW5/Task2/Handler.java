import java.io.*;
import java.net.*;
public class Handler implements Runnable {
    private Socket socket;
    private TableMonitor tableMonitor;
    public Handler(Socket socket, TableMonitor tableMonitor) { this.socket = socket; this.tableMonitor = tableMonitor;}
    public void run() {
        try {
            
            DataInputStream in = new DataInputStream(socket.getInputStream());
            int philosopherID = in.readInt();
            int pairedClient = -1;
            try{ pairedClient = tableMonitor.forkDistribution(philosopherID); } 
            catch( InterruptedException e) {System.out.println("tableMonitor error" + e.getMessage()); }
            
            System.out.println("Handler: Philosopher " + philosopherID + " Partner: " + pairedClient);

            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            out.writeInt(pairedClient);

            socket.close();
        } catch ( IOException e ) {;}
    }
}
