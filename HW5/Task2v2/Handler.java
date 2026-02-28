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
            long timeInQ = Long.MAX_VALUE;
            try{ timeInQ = tableMonitor.forkDistribution(philosopherID); } 
            catch( InterruptedException e) {System.out.println("tableMonitor error" + e.getMessage()); }
            
            //System.out.println("Handler: Philosopher " + philosopherID + " has eaten");

            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            out.writeLong(timeInQ);

            socket.close();
        } catch ( IOException e ) {;}
    }
}
