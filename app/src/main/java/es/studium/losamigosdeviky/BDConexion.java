package es.studium.losamigosdeviky;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

import es.studium.losamigosdeviky.ayuntamientos.Ayuntamiento;
import es.studium.losamigosdeviky.ayuntamientos.AyuntamientoCallback;
import okhttp3.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BDConexion {

    public interface LoginCallback {
        void onLoginResult(boolean success, int tipoUsuario, String nombreUsuario);
    }

    // Login
    public static void comprobarCredenciales(String nombreUsuario, String contrasenaUsuario, final LoginCallback callback) {

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("usuario", nombreUsuario)
                .add("contrasena", contrasenaUsuario)
                .build();
        Request request = new Request.Builder()
                .url("http://192.168.1.131/ApiProtectora/login.php")
                .post(requestBody)
                .build();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = client.newCall(request).execute();
                    boolean loginSuccess = false;
                    int tipoUsuario = -1;
                    if (response.isSuccessful()) {
                        // Parse JSON response
                        String jsonResponse = response.body().string();
                        try {
                            JSONObject jsonObject = new JSONObject(jsonResponse);
                            loginSuccess = jsonObject.getBoolean("success");
                            if (loginSuccess) {
                                tipoUsuario = jsonObject.getInt("tipoUsuario");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // Post result back to the main thread
                        final boolean finalLoginSuccess = loginSuccess;
                        MainActivity.tipoUsuario = tipoUsuario;
                        final String finalNombreUsuario = nombreUsuario;
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                callback.onLoginResult(finalLoginSuccess, MainActivity.tipoUsuario, finalNombreUsuario);
                            }
                        });
                    }
                } catch (IOException e) {
                    // Handle failure
                    Log.e("BDConexion", "Error during login request", e);
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onLoginResult(false, -1, nombreUsuario);
                        }
                    });
                }
            }
        }).start();
    }

    // Ayuntamientos - Consulta
    public static void consultarAyuntamientos(final AyuntamientoCallback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://192.168.1.131/ApiProtectora/ayuntamientos.php")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("BDConexion", "Network error: " + e.getMessage());
                // Handle the error appropriately
                callback.onResult(new ArrayList<>()); // Return empty list on failure
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ArrayList<Ayuntamiento> ayuntamientos = new ArrayList<>();
                if (response.isSuccessful()) {
                    try {
                        JSONArray result = new JSONArray(response.body().string());
                        for (int i = 0; i < result.length(); i++) {
                            JSONObject jsonObject = result.getJSONObject(i);
                            int idAyuntamiento = jsonObject.getInt("idAyuntamiento");
                            String nombreAyuntamiento = jsonObject.getString("nombreAyuntamiento");
                            int telefonoAyuntamiento = jsonObject.getInt("telefonoAyuntamiento");
                            String responsableAyuntamiento = jsonObject.getString("responsableAyuntamiento");
                            String direccionAyuntamiento = jsonObject.getString("direccionAyuntamiento");
                            int cpAyuntamiento = jsonObject.getInt("cpAyuntamiento");

                            ayuntamientos.add(new Ayuntamiento(idAyuntamiento, nombreAyuntamiento, telefonoAyuntamiento, responsableAyuntamiento, direccionAyuntamiento, cpAyuntamiento));
                        }
                    } catch (JSONException e) {
                        Log.e("BDConexion", "JSON parsing error: " + e.getMessage());
                    }
                } else {
                    Log.e("BDConexion", "Response not successful: " + response.message());
                }
                // Pass the result to the callback
                callback.onResult(ayuntamientos);
            }
        });
    }

    // Ayuntamiento - Alta
    public static void anadirAyuntamiento(Ayuntamiento ayuntamiento, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("idAyuntamiento", "3")
                .add("nombreAyuntamiento", ayuntamiento.getNombreAyuntamiento())
                .add("telefonoAyuntamiento", String.valueOf(ayuntamiento.getTelefonoAyuntamiento()))
                .add("responsableAyuntamiento", ayuntamiento.getResponsableAyuntamiento())
                .add("direccionAyuntamiento", ayuntamiento.getDireccionAyuntamiento())
                .add("cpAyuntamiento", String.valueOf(ayuntamiento.getCpAyuntamiento()))
                .build();

        Request request = new Request.Builder()
                .url("http://192.168.1.131/ApiProtectora/ayuntamientos.php")
                .post(formBody)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(call, e); // Forward the failure to the provided callback
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                } else {
                }
                callback.onResponse(call, response); // Forward the response to the provided callback
            }
        });
    }

    // Ayuntamiento - Modificacion
    public static void modificarAyuntamiento(Ayuntamiento ayuntamiento, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder queryUrlBuilder = HttpUrl.parse("http://192.168.1.131/ApiProtectora/ayuntamientos.php").newBuilder();
        // Add query parameters
        queryUrlBuilder.addQueryParameter("idAyuntamiento", String.valueOf(ayuntamiento.getIdAyuntamiento()));
        queryUrlBuilder.addQueryParameter("nombreAyuntamiento", ayuntamiento.getNombreAyuntamiento());
        queryUrlBuilder.addQueryParameter("telefonoAyuntamiento", String.valueOf(ayuntamiento.getTelefonoAyuntamiento()));
        queryUrlBuilder.addQueryParameter("responsableAyuntamiento", ayuntamiento.getResponsableAyuntamiento());
        queryUrlBuilder.addQueryParameter("direccionAyuntamiento", ayuntamiento.getDireccionAyuntamiento());
        queryUrlBuilder.addQueryParameter("cpAyuntamiento", String.valueOf(ayuntamiento.getCpAyuntamiento()));

        // Create request body (empty for PUT requests)
        RequestBody requestBody = new FormBody.Builder()
                .build();

        // Build the request
        Request request = new Request.Builder()
                .url(queryUrlBuilder.build())
                .put(requestBody)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(call, e); // Forward the failure to the provided callback
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                } else {
                }
                callback.onResponse(call, response); // Forward the response to the provided callback
            }
        });
    }

    // Ayuntamiento - Borrado
    public static void borrarAyuntamiento(Ayuntamiento ayuntamiento, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://192.168.1.131/ApiProtectora/ayuntamientos.php?idAyuntamiento=" + ayuntamiento.getIdAyuntamiento())
                .delete()
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(call, e); // Forward the failure to the provided callback
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                } else {
                }
                callback.onResponse(call, response); // Forward the response to the provided callback
            }
        });
    }
}
