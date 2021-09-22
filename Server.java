import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Server {

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private static ArrayList<Thread> threadQueue = new ArrayList<>();
    private static ArrayList<Socket> sockets = new ArrayList<>();
    private static VariableChange variableChanger = new VariableChange();
    private static boolean canTalk = false;

    public static void main(String[] args){
        new Server();
        canTalk = true; // cantalk will always be true once constructor is complete
    }

    public Server() {
        int port = 5001;
        System.out.println("Waiting on connections");
        try {
            serverSocket = new ServerSocket(port); // creating socket for server
            while (threadQueue.size() < 2){
                clientSocket = serverSocket.accept();
                sockets.add(clientSocket);
                // create a thread to pass new clients to
                Thread clientThread = (new Thread(() -> new ConnectionHandler(clientSocket)));
                threadQueue.add(clientThread);
                clientThread.start();
                System.out.println("Waiting on " + String.valueOf(2 - threadQueue.size()) + " connections");
            }
        } catch (IOException e) {
            System.out.println("No I/O");
            //System.exit(-1);
        }
    }

    public static void writeSocket(PrintWriter printWriter){
        VariableChange instance = variableChanger.getInstance(); // TODO: SEND START SIGNAL
        Runnable writeRunnable = () -> {
            if (canTalk) {
                printWriter.println("(" + instance.getX() +
                        ", " + instance.getY() + ", " + instance.getZ() + ")");
            }
            //System.out.println("(" + instance.getX() +
            // ", " + instance.getY() + ", " + instance.getZ() + ")");
        };
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        executorService.scheduleAtFixedRate(writeRunnable, 0, 500, TimeUnit.MILLISECONDS);
    }

    public static void readComms(BufferedReader in){
        Runnable communicationRunnable = () -> {
            if (canTalk) {
                try {
                    if (in.ready()) {
                        System.out.println(in.readLine());
                    }
                } catch (IOException e) {
                    System.out.println("I/O Failed");
                    System.exit(-1);
                    // TODO Raise Error on Server
                }
            }
        };
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);
        executorService.scheduleAtFixedRate(communicationRunnable, 0, 500, TimeUnit.MILLISECONDS);
    }


}

