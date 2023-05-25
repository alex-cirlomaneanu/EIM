package ro.pub.cs.systems.eim.pokemon;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.BreakIterator;

public class MainActivity extends AppCompatActivity {

    private EditText serverPortEditText = null;
    private EditText clientAddressEditText = null;
    private EditText clientPortEditText = null;
    private EditText pokemonName = null;

    private ServerThread serverThread = null;

    private final ConnectButtonClickListener connectButtonClickListener = new ConnectButtonClickListener();
    private final GetInfoClickListener getInfoClickListener = new GetInfoClickListener();
    private TextView textViewAilities= null;
    private TextView textViewPokemonTypes = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        serverPortEditText = findViewById(R.id.server_port_edit_text);
        Button connectButton = findViewById(R.id.connect_button);
        connectButton.setOnClickListener(connectButtonClickListener);

        clientAddressEditText = findViewById(R.id.client_address_edit_text);
        clientPortEditText = findViewById(R.id.client_port_edit_text);
        pokemonName = findViewById(R.id.pokemon_edit_text);

        Button getInfo = findViewById(R.id.get_info_button);
        getInfo.setOnClickListener(getInfoClickListener);
        textViewAilities = findViewById(R.id.pokemon_text_view);
        textViewPokemonTypes = findViewById(R.id.type_pokemon_text_view);
    }

    private class ConnectButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            // Retrieves the server port. Checks if it is empty or not
            // Creates a new server thread with the port and starts it
            String serverPort = serverPortEditText.getText().toString();
            if (serverPort.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Server port should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            serverThread = new ServerThread(Integer.parseInt(serverPort));
            if (serverThread.getServerSocket() == null) {
                Log.e(Constants.TAG, "[MAIN ACTIVITY] Could not create server thread!");
                return;
            }
            serverThread.start();
        }
    }

    private class GetInfoClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            // Retrieves the client address and port. Checks if they are empty or not
            //  Checks if the server thread is alive. Then creates a new client thread with the address, port, city and information type
            //  and starts it
            String clientAddress = clientAddressEditText.getText().toString();
            String clientPort = clientPortEditText.getText().toString();
            if (clientAddress.isEmpty() || clientPort.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Client connection parameters should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (serverThread == null || !serverThread.isAlive()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] There is no server to connect to!", Toast.LENGTH_SHORT).show();
                return;
            }
            String pokemon = pokemonName.getText().toString();;
            if (pokemon.isEmpty() ) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Parameters from client (pokemon) should be filled", Toast.LENGTH_SHORT).show();
                return;
            }
            Log.i(Constants.TAG, "[MAIN ACTIVITY] Parameters from client (pokemon): " + pokemon);
            textViewAilities.setText(Constants.EMPTY_STRING);
            textViewPokemonTypes.setText(Constants.EMPTY_STRING);
            ClientThread clientThread = new ClientThread(clientAddress, Integer.parseInt(clientPort), pokemon, textViewAilities, textViewPokemonTypes);
            clientThread.start();
        }
    }
}