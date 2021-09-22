import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.sql.Time;
import java.time.LocalTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Client {
    private static String name;
    public static void main(String[] args){
        name = args[0];
        try {
            Socket socket = new Socket();
            int port = 5001;
            InetSocketAddress IP = new InetSocketAddress("x.x.x.x", port); // commented out IP
            socket.connect(IP);
            BufferedReader in
                    = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            doCommunications(in, out, name);
        } catch (IOException e) {
            System.out.println("I/O Failed");
            System.exit(-1);
        }
    }

    private static void doCommunications(BufferedReader in, PrintWriter out, String name){
        Runnable communicationRunnable = () -> {
            try {
                if (in.ready()){
                    System.out.println("Message from server: " + in.readLine());
                    out.println(name + ": " + Time.valueOf(LocalTime.now())); // TODO FIX, this won't always work
                    // as locations aren't sent this way
                }
            } catch (IOException e) {
                System.out.println("I/O Failed");
                System.exit(-1);
                // TODO Raise Error on Server
            }
        };
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);
        executorService.scheduleAtFixedRate(communicationRunnable, 0, 500, TimeUnit.MILLISECONDS);
        // socket close
    }
}
