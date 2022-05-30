package ro.pub.cs.systems.eim.practicaltest02.server;

import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import ro.pub.cs.systems.eim.practicaltest02.utils.Constants;


public class ServerThread extends Thread {
    private ServerSocket serverSocket;
    private boolean isRunning;
    private final int port;

    public ServerThread(int port) {
        this.port = port;
    }

    public void startServer() {
        isRunning = true;
        start();
        Log.v(Constants.TAG, "Server running...");
    }

    public void stopServer() {
        isRunning = false;
        try {
            serverSocket.close();
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "Server Thread Error: " + ioException.getMessage());
        }
        Log.v(Constants.TAG, "Server stopped");
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            while (isRunning) {
                Socket socket = serverSocket.accept();
                new CommunicationThread(socket).start();
            }
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "Server Thread Error: " + ioException.getMessage());
        }
    }
}
