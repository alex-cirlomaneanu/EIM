package ro.pub.cs.systems.eim.pokemon;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.util.EntityUtils;
import ro.pub.cs.systems.eim.pokemon.Constants;
import ro.pub.cs.systems.eim.pokemon.PokemonInfo;
import ro.pub.cs.systems.eim.pokemon.ServerThread;
import ro.pub.cs.systems.eim.pokemon.Utilities;

public class CommunicationThread extends Thread {

    private final ServerThread serverThread;
    private final Socket socket;

    // Constructor of the thread, which takes a ServerThread and a Socket as parameters
    public CommunicationThread(ServerThread serverThread, Socket socket) {
        this.serverThread = serverThread;
        this.socket = socket;
    }

    @Override
    public void run() {
        if (socket == null) {
            Log.e(Constants.TAG, "[COMMUNICATION THREAD] Socket is null!");
            return;
        }
        try {
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);
            Log.i(Constants.TAG, "[COMMUNICATION THREAD] Waiting for parameters from client pokemon!");

            String pokemon = bufferedReader.readLine();

            if (pokemon == null || pokemon.isEmpty()) {
                Log.e(Constants.TAG, "[COMMUNICATION THREAD] Error receiving parameters from client (pokemon) ");
                return;
            }

            HashMap<String, PokemonInfo> data = serverThread.getData();
            PokemonInfo pokemonInfo;

            if (data.containsKey(pokemon)) {
                Log.i(Constants.TAG, "[COMMUNICATION THREAD] Getting the information from the cache...");
                pokemonInfo = data.get(pokemon);
            } else {
                Log.i(Constants.TAG, "[COMMUNICATION THREAD] Getting the information from the webservice...");
                HttpClient httpClient = new DefaultHttpClient();
                String pageSourceCode = "";

                HttpGet httpGet = new HttpGet(Constants.WEB_SERVICE_ADDRESS + pokemon);
                HttpResponse httpGetResponse = httpClient.execute(httpGet);
                HttpEntity httpGetEntity = httpGetResponse.getEntity();
                if (httpGetEntity != null) {
                    pageSourceCode = EntityUtils.toString(httpGetEntity);
                }
                if (pageSourceCode == null) {
                    Log.e(Constants.TAG, "[COMMUNICATION THREAD] Error getting the information from the webservice!");
                    return;
                } else Log.i(Constants.TAG, pageSourceCode);
                JSONObject content = new JSONObject(pageSourceCode);
                JSONArray arrAbilities = content.getJSONArray(Constants.ABILITIES);
                JSONObject ability;
                StringBuilder abilities = new StringBuilder();
                for (int i = 0; i < arrAbilities.length(); i++) {
                    ability = arrAbilities.getJSONObject(i);
                    JSONObject abilityDetails = ability.getJSONObject(Constants.ABILITY);
                    abilities.append(abilityDetails.getString(Constants.NAME));
                    abilities.append(" ");
                }

                String image = content.getJSONObject("sprites").getString("front_default");
                JSONArray types = content.getJSONArray(Constants.TYPES);
                JSONObject type;
                StringBuilder arrTypes = new StringBuilder();
                for (int i = 0; i < types.length(); i++) {
                    type = types.getJSONObject(i);
                    JSONObject abilityDetails = type.getJSONObject(Constants.TYPE);
                    arrTypes.append(abilityDetails.getString(Constants.NAME));
                    arrTypes.append(" ");
                }

                pokemonInfo = new PokemonInfo(abilities.toString(), arrTypes.toString(),  image);
                serverThread.setData(pokemon, pokemonInfo);
            }

            printWriter.println(pokemonInfo.getAbilities());
            printWriter.flush();
            printWriter.println(pokemonInfo.getTypes());
            printWriter.flush();
        } catch (IOException | JSONException ioException) {
            Log.e(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        } finally {
            try {
                socket.close();
            } catch (IOException ioException) {
                Log.e(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
                if (Constants.DEBUG) {
                    ioException.printStackTrace();
                }
            }
        }
    }
}
