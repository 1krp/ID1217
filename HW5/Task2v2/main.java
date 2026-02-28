    import java.util.concurrent.*;
    
    public class main{
    public static void main(String[] args) {
        int clientThreadPoolSize = 5, port =9999;
        
        try {
            if (args.length >1) clientThreadPoolSize = Integer.parseInt(args[1]);
            if (args.length >0) port = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.out.println("USAGE: [port] [numClients]");
            System.exit(1);
        }
        
        ExecutorService executor = Executors.newFixedThreadPool(clientThreadPoolSize);
        for (int i = 0; i < clientThreadPoolSize; i++){
            executor.execute(new Philosopher(i+1, port)); 
        }
        executor.shutdown();
    }
}