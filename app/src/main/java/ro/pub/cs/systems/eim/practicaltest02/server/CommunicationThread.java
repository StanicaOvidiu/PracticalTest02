package ro.pub.cs.systems.eim.practicaltest02.server;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.BasicResponseHandler;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.protocol.BasicHttpContext;
import ro.pub.cs.systems.eim.practicaltest02.utils.Constants;
import ro.pub.cs.systems.eim.practicaltest02.utils.Utils;


class CurrencyCache {
    public String rateValue;
    public String updatedTime;

    public CurrencyCache(String rateValue, String updatedTime) {
        this.rateValue = rateValue;
        this.updatedTime = updatedTime;
    }
}

class CommunicationThread extends Thread {
    private final Socket socket;
    private static CurrencyCache usdCache = null;
    private static CurrencyCache eurCache = null;

    public CommunicationThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            Log.v(Constants.TAG, "Started new connection with:  " + socket.getLocalAddress());
            BufferedReader reader = Utils.getReader(socket);
            PrintWriter writer = Utils.getWriter(socket);

            String currencyType = reader.readLine();

            Log.v(Constants.TAG, "Received a request for " + currencyType + " currency");

            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet("https://api.coindesk.com/v1/bpi/currentprice/EUR.json");
            String jsonContent = httpClient.execute(httpGet, new BasicResponseHandler());

            try {
                String result;
                JSONObject jsonObject = new JSONObject(jsonContent);

                String updateTime = jsonObject.getJSONObject("time").getString("updated");

                Date date;
                try {
                    SimpleDateFormat format = new SimpleDateFormat("MM dd, yyyy HH:mm:ss");
                    updateTime = updateTime.substring(0, updateTime.length() - 4);

                    date = format.parse(updateTime);
                    Log.v(Constants.TAG, "Date " + date);
                } catch (ParseException e) {
                }

                if (currencyType.equals("EUR")) {
                    result = jsonObject.getJSONObject("bpi").getJSONObject("EUR").getString("rate");
                } else {
                    result = jsonObject.getJSONObject("bpi").getJSONObject("USD").getString("rate");
                }
                writer.println(result);
            } catch (JSONException e) {
                e.printStackTrace();
                writer.println("Error on Json parsing");
            }

            socket.close();
            Log.v(Constants.TAG, "Closed connection with: " + socket.getLocalAddress());
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "Communication Error: " + ioException.getMessage());
        }
    }
}