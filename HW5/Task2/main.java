    import java.util.concurrent.*;
    
    public class main{
    public static void main(String[] args) {
        int clientThreadPoolSize = 5, port =9999;
        ExecutorService executor = Executors.newFixedThreadPool(clientThreadPoolSize);
        for (int i = 0; i < clientThreadPoolSize; i++){
            executor.execute(new Client(i+1, port));
        }
        executor.shutdown();
    }
}