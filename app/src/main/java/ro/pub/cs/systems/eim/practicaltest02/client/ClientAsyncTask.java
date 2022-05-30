package ro.pub.cs.systems.eim.practicaltest02.client;

import android.os.AsyncTask;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import ro.pub.cs.systems.eim.practicaltest02.utils.Utils;

public class ClientAsyncTask extends AsyncTask<String, Void, String> {
    private TextView currencyValueTextView;
    public ClientAsyncTask(TextView currencyValueTextView) {
        this.currencyValueTextView = currencyValueTextView;
    }

    @Override
    protected String doInBackground(String... params) {
        String serverAddress = params[0];
        String portAddress = params[1];
        String currencyType = params[2];

        try {
            Socket socket = new Socket(serverAddress, Integer.parseInt(portAddress));
            BufferedReader reader = Utils.getReader(socket);
            PrintWriter writer = Utils.getWriter(socket);

            writer.println(currencyType);
            String result = reader.readLine();
            socket.close();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        if (result == null) {
            result = "Error on server communication";
        }
        currencyValueTextView.setText(result);
    }

}
