import java.io.*;
import java.net.*;
import java.util.concurrent.*;
import semaphores.*;
public class Server {
    private int port;
    private int numClients;
    private int serverThreadPoolSize;

    public Server (int port, int serverThreadPoolSize, int numClients){
        this.port = port;
        this.serverThreadPoolSize = serverThreadPoolSize;
        this.numClients = numClients;
    }

    public void runServer(TableMonitor tableMonitor) throws IOException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println("Can not listen on port: " + port);
            System.exit(1);
        }
        System.out.println("Server started at port: " + port + " with " + serverThreadPoolSize + " threads and houses " + numClients + " philosophers");
        ExecutorService executor = Executors.newFixedThreadPool(serverThreadPoolSize);
        while(true){
            try{
            Socket socket = serverSocket.accept();
            executor.execute( new Handler(socket, tableMonitor) );
            
            } catch (SocketException SocketE){ SocketE.printStackTrace();}
        }
    }
    public static void main(String[] args) {
        // args: <portNr> <serverThreads> <clients>
        int port = 9999;
        int serverThreadPoolSize = 4;
        int numClients = 5;
        Fork[] forks = new Fork[numClients];
            for (int i = 0; i < numClients; i++){
            forks[i] = new Fork(i+1);
        }
        try {
            if (args.length >1) serverThreadPoolSize = Integer.parseInt(args[1]);
            if (args.length >0) port = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.out.println("USAGE: <portNr> <serverThreads>");
            System.exit(1);
        }
        Server server = new Server(port, serverThreadPoolSize, numClients);
        TableMonitor tableMonitor = new TableMonitor(numClients, forks);
        try {
          server.runServer(tableMonitor);  
        } catch (IOException e) {System.out.println("runServer error " + e.getMessage());}
    }
}