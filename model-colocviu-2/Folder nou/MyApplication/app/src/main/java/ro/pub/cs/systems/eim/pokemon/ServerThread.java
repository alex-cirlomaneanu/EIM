package ro.pub.cs.systems.eim.pokemon;

import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class ServerThread extends Thread {
    ServerSocket serverSocket = null;

    private HashMap<String, PokemonInfo> data;

    public synchronized void setData(String pokemon, PokemonInfo pokemonInfo) {
        this.data.put(pokemon, pokemonInfo);
    }

    public synchronized HashMap<String, PokemonInfo> getData() {
        return data;
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public ServerThread(int port) {
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (Exception e) {
            Log.e(Constants.TAG, "An exception has occurred: " + e.getMessage());
            if (Constants.DEBUG)
                e.printStackTrace();
        }
        this.data = new HashMap<>();
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Log.i(Constants.TAG, "[SERVER THREAD] Waiting for a client invocation...");
                Socket socket = serverSocket.accept();
                Log.i(Constants.TAG, "[SERVER THREAD] A connection request was received from " + socket.getInetAddress() + ":" + socket.getLocalPort());

                CommunicationThread communicationThread = new CommunicationThread(this, socket);
                communicationThread.start();
            }
        } catch (Exception e) {
            Log.e(Constants.TAG, "[SERVER THREAD] An exception has occurred: " + e.getMessage());
            if (Constants.DEBUG)
                e.printStackTrace();
        }
    }

    public void stopThread() {
        interrupt();
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException ioException) {
                Log.e(Constants.TAG, "[SERVER THREAD] An exception has occurred: " + ioException.getMessage());
                if (Constants.DEBUG) {
                    ioException.printStackTrace();
                }
            }
        }
    }
}
