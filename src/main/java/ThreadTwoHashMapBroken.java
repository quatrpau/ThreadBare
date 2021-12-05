import java.util.HashMap;
import java.util.Set;

public class ThreadTwoHashMapBroken extends Thread {
    HashMap<String, Thread> threadMap;

    public ThreadTwoHashMapBroken(String name) {
        super(name);
        this.threadMap = new HashMap<>();
    }

    @Override
    public void run() {
        System.out.println("ThreadTwoHashMapB - START "+Thread.currentThread().getName());
        try {
            Thread.sleep(1000);
            //Get database connection, delete unused data from DB
            doDBProcessing();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("ThreadTwoHashMapB - END "+Thread.currentThread().getName());
    }

    private void doDBProcessing() throws InterruptedException {
        Thread.sleep(5000);
    }


    // Run this
    public static void main(String[] args){
        ThreadTwoHashMapBroken tm = new ThreadTwoHashMapBroken(""+10);
        //1 run
        // What's wrong with this idea??...
        //these will create threads inside of the threads which may lead to unforseen issues
        // my guess is that because the threads are abstracted inside of the bigger threads the
        //exception is contained inside of the parent class 
        new Thread("Run of " + 6){
            public void run(){
                tm.runMapOfSize(6);
            }
        }.start();
        new Thread("Run of " + 8){
            public void run(){
                tm.runMapOfSize(8);
            }
        }.start();

    }

    private void runMapOfSize(int size) {
        System.out.println("Constructing HashMap of Size "+size);
        Integer threadCount = size;

        for (int i = 0; i < threadCount; i++) {
            this.threadMap.put("T"+ i, new ThreadTwoHashMapBroken("T"+ i));
        }
        System.out.println("Starting Threads in HashMap");
        Set<String> names = this.threadMap.keySet();
        for (String name : names) {
            this.threadMap.get(name).start();
        }
        System.out.println("Thread HashMap, all have been started");
    }
}
