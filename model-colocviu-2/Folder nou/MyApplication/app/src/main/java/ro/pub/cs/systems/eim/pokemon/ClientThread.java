package ro.pub.cs.systems.eim.pokemon;

import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread extends Thread {
    private String address;
    private int port;
    private String pokemonName;
    private TextView textViewAilities;
    private TextView textViewPokemonTypes;

    private Socket socket;

    public ClientThread(String address, int port, String pokemonName, TextView textViewAilities, TextView textViewPokemonTypes) {
        this.address = address;
        this.port = port;
        this.pokemonName = pokemonName;
        this.textViewAilities = textViewAilities;
        this.textViewPokemonTypes = textViewPokemonTypes;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(address, port);
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);
            printWriter.println(pokemonName);
            printWriter.flush();

            String abilities;
            String types;
            while ((abilities = bufferedReader.readLine()) != null &&
                    (types = bufferedReader.readLine()) != null) {
                final String finalizedAbilities = abilities;
                final String finalizedTypes = types;
                textViewAilities.post(() -> textViewAilities.setText(finalizedAbilities + "\n" + finalizedTypes));
            }
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        } finally {
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch(IOException ioException) {
                Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
                if (Constants.DEBUG) {
                    ioException.printStackTrace();
                }
            }
        }
    }
}
