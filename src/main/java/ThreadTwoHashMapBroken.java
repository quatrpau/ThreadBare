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
        //hashmap is not thread safe and it has the potential to get concurrently modified
        //specifically the hashmap get concurrently modified with the put method
        //**the put method expects a certain value for it to change but if that value gets modified
        //it raises the concurrent modification exception**
        //also since hashmap has class scope and not local scope it can get modified by a thread from either parent

        new Thread("Run of " + 6){
            public void run(){
                tm.runMapOfSize(6,0);
            }
        }.start();
        new Thread("Run of " + 8){
            public void run(){
                tm.runMapOfSize(8,6);
            }
        }.start();

    }

    private void runMapOfSize(int size,int start) {
        System.out.println("Constructing HashMap of Size "+size);
        Integer threadCount = size;
        for (int i = start; i < start + threadCount; i++) {
            this.threadMap.put("T"+ i, new ThreadTwoHashMapBroken("T"+ i));
        }
        System.out.println("Starting Threads in HashMap");
        Set<String> names = this.threadMap.keySet();
        for (int i = start; i < start + threadCount ; i++) {
            this.threadMap.get("T" + i).start();
        }
        System.out.println("Thread HashMap, all have been started");
    }
}
