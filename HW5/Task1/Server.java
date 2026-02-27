import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class Server {
    private int port;
    private int serverThreadPoolSize;

    public Server (int port, int serverThreadPoolSize){
        this.port = port;
        this.serverThreadPoolSize = serverThreadPoolSize;
    }

    public void runServer(paringMonitor paringMonitor) throws IOException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println("Can not listen on port: " + port);
            System.exit(1);
        }
        System.out.println("Server started at port: " + port + " with " + serverThreadPoolSize + " threads");
        ExecutorService executor = Executors.newFixedThreadPool(serverThreadPoolSize);
        while(true){
            try{
            Socket socket = serverSocket.accept();
            executor.execute( new Handler(socket, paringMonitor) );
            
            } catch (SocketException SocketE){ SocketE.printStackTrace();}

        }
    }
    public static void main(String[] args) {
        int serverThreadPoolSize = 4, port = 9999;
        try {
            if (args.length >1) serverThreadPoolSize = Integer.parseInt(args[1]);
            if (args.length >0) port = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.out.println("USAGE: java ReverseServer [port] [poolSize]");
            System.exit(1);
        }
        Server server = new Server(port, serverThreadPoolSize);
        paringMonitor paringMonitor = new paringMonitor(serverThreadPoolSize);
        try {
          server.runServer(paringMonitor);  
        } catch (IOException e) {System.out.println("runServer error " + e.getMessage());}
    }
}