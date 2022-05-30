package ro.pub.cs.systems.eim.practicaltest02.client;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import ro.pub.cs.systems.eim.practicaltest02.utils.Utils;

public class ClientAsyncTask extends AsyncTask<String, String, Void> {
    public ClientAsyncTask() {
    }

    @Override
    protected Void doInBackground(String... params) {
        String serverAddress = params[0];
        String portAddress = params[1];

        try {
            Socket socket = new Socket(serverAddress, Integer.parseInt(portAddress));
            PrintWriter write = Utils.getWriter(socket);
            write.println("Hello world");
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onProgressUpdate(String... progress) { ;
    }

    @Override
    protected void onPostExecute(Void result) {
    }

}
