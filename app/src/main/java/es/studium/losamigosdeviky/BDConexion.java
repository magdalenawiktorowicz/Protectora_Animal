package es.studium.losamigosdeviky;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.IOException;

import okhttp3.*;

import org.json.JSONException;
import org.json.JSONObject;

public class BDConexion {

    public interface LoginCallback {
        void onLoginResult(boolean success, int tipoUsuario, String nombreUsuario);
    }

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

}
