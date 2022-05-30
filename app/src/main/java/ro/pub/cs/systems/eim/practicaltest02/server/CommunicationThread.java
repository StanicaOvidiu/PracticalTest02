package ro.pub.cs.systems.eim.practicaltest02.server;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;

import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.BasicResponseHandler;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import ro.pub.cs.systems.eim.practicaltest02.utils.Constants;
import ro.pub.cs.systems.eim.practicaltest02.utils.Utils;

class CommunicationThread extends Thread {
    private final Socket socket;

    public CommunicationThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            Log.v(Constants.TAG, "Started new connection with:  " + socket.getLocalAddress());
            BufferedReader reader = Utils.getReader(socket);
            Log.v(Constants.TAG, reader.readLine());
            socket.close();
            Log.v(Constants.TAG, "Closed connection with: " + socket.getLocalAddress());

            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet("https://www.google.com");
            Log.v(Constants.TAG, httpClient.execute(httpGet, new BasicResponseHandler()));
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "Communication Error: " + ioException.getMessage());
        }
    }
}