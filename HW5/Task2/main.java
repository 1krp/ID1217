    import java.util.concurrent.*;
    
    public class main{
    public static void main(String[] args) {
        int clientThreadPoolSize = 5, port =9999;
        ExecutorService executor = Executors.newFixedThreadPool(clientThreadPoolSize);
        Fork[] forks = new Fork[clientThreadPoolSize];
        for (int i = 0; i < clientThreadPoolSize; i++){
            forks[i] = new Fork(i+1);
        }
        for (int i = 0; i < clientThreadPoolSize; i++){
            executor.execute(new Philosopher(i+1, port, forks[i], forks[(i+1)%clientThreadPoolSize])); // Philosopher 1 gets fork 1 and 2, Philosopher 5 gets fork 5 and 1
        }
        executor.shutdown();
    }
}