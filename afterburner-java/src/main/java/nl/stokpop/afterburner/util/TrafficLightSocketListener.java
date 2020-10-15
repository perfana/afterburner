package nl.stokpop.afterburner.util;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TrafficLightSocketListener implements Runnable {

    private static final int SOCKET_TIMEOUT_MILLIS = 1000;
    private static final Logger log = LoggerFactory.getLogger(TrafficLightSocketListener.class);

    private final ServerSocket serverSocket;
    private final Executor executor;

    private volatile boolean continueServing = true;

    public TrafficLightSocketListener(int port, Executor executor) throws IOException {
        this.serverSocket = new ServerSocket(port);
        // prevent the listener to get stuck when no traffic arrives
        this.serverSocket.setSoTimeout(SOCKET_TIMEOUT_MILLIS);
        this.executor = executor;
    }

    public void stopServing() { this.continueServing = false; }

    @Override
    public void run() {
        try {
            while (continueServing) {
                try {
                    Socket accept = serverSocket.accept();
                    if (continueServing) {
                        executor.execute(new RequestHandler(accept));
                    }
                } catch (SocketTimeoutException e) {
                    // continue, but give space to stop serving
                } catch (IOException e) {
                    log.error("Cannot start server socket", e);
                }
            }
        }
        finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                log.error("Cannot close server socket on " + serverSocket);
            }
        }
    }

    private static class RequestHandler implements Runnable {

        private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

        private final Socket socket;

        public RequestHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                     BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                    String inputLine;
                    boolean isHttp = false;
                    while ((inputLine = in.readLine()) != null) {
                        if (inputLine.contains("HTTP")) { isHttp = true; }
                        if (inputLine.length() == 0) { break; }
                    }
                    String color = (System.currentTimeMillis() % 100) < 25 ? "red" : "green";
                    String reply = "The traffic light is "+ color + "!";
                    if (isHttp) {
                        out.println(minimalHttpTextReply(reply));
                    } else {
                        out.println(reply);
                    }
                }
            } catch (IOException e) {
                log.error("Issue handling socket input.", e);
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    log.error("Issue closing socket.", e);
                }
            }
        }

        @NotNull
        private String minimalHttpTextReply(String reply) {
            return "HTTP/1.1 200 OK\n" +
                "Content-Length: " + reply.length() + "\n" +
                "Content-Type: text/plain; charset=utf-8\n" +
                "\n" +
                reply;
        }
    }

    public static void main(String[] args) throws IOException {
        ExecutorService executor = Executors.newFixedThreadPool(3);
        TrafficLightSocketListener socketListener = new TrafficLightSocketListener(3344, executor);
        executor.execute(socketListener);

        System.out.println("Open for business...");

        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(5));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Closing for business...");
        socketListener.stopServing();
        executor.shutdownNow();
    }
}
