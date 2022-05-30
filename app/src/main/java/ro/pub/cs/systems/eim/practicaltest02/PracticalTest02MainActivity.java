package ro.pub.cs.systems.eim.practicaltest02;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ro.pub.cs.systems.eim.practicaltest02.client.ClientAsyncTask;
import ro.pub.cs.systems.eim.practicaltest02.server.ServerThread;
import ro.pub.cs.systems.eim.practicaltest02.utils.Constants;

public class PracticalTest02MainActivity extends AppCompatActivity {
    private ServerThread serverThread = null;

    private EditText serverPortEditText;
    private Button startServerButton;
    private EditText clientAddressEditText;
    private EditText clientPortEditText;
    private EditText currencyEditText;
    private Button submitButton;
    private TextView currencyValueTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02_main);

        setupViews();
        setupServer();
        setupClient();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (serverThread != null) {
            serverThread.stopServer();
        }
    }

    private void setupViews() {
        serverPortEditText = (EditText) findViewById(R.id.server_port_edit_text);
        startServerButton = (Button) findViewById(R.id.start_server_button);
        clientAddressEditText = (EditText) findViewById(R.id.client_address_edit_text);
        clientPortEditText = (EditText) findViewById(R.id.client_port_edit_text);
        currencyEditText = (EditText) findViewById(R.id.currency_edit_text);
        submitButton = (Button) findViewById(R.id.submit_button);
        currencyValueTextView = (TextView) findViewById(R.id.currency_value_text_view);
    }

    private void setupClient() {
        submitButton.setOnClickListener(view -> {
            String address, port, currencyType;
            try {
                address = clientAddressEditText.getText().toString();
                port = clientPortEditText.getText().toString();
                currencyType = currencyEditText.getText().toString();
                if (!currencyType.equals("EUR") && !currencyType.equals("USD")) {
                    throw new Exception();
                }
            } catch (Exception e) {
                Log.e(Constants.TAG, "Invalid Client input");
                return;
            }

            new ClientAsyncTask(currencyValueTextView).execute(
                    address, port, currencyType
            );
        });
    }

    private void setupServer() {
        startServerButton.setOnClickListener(view -> {
            if (serverThread == null) {
                try {
                    int port = Integer.parseInt(serverPortEditText.getText().toString());
                    serverThread = new ServerThread(port);
                } catch (Exception ex) {
                    Log.v(Constants.TAG, "Invalid Server Port");
                    return;
                }
                serverThread.startServer();
                startServerButton.setText("Stop Running");
            } else {
                serverThread.stopServer();
                serverThread = null;
                startServerButton.setText("Start Running");
            }
        });
    }
}
