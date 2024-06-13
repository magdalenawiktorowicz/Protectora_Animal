package es.studium.losamigosdeviky;

import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import es.studium.losamigosdeviky.ayuntamientos.Ayuntamiento;
import es.studium.losamigosdeviky.ayuntamientos.AyuntamientoCallback;
import es.studium.losamigosdeviky.colonias.Colonia;
import es.studium.losamigosdeviky.colonias.ColoniaCallback;
import es.studium.losamigosdeviky.cuidados.Cuidado;
import es.studium.losamigosdeviky.cuidados.CuidadoCallback;
import es.studium.losamigosdeviky.gatos.Gato;
import es.studium.losamigosdeviky.gatos.GatoCallback;
import es.studium.losamigosdeviky.protectoras.Protectora;
import es.studium.losamigosdeviky.protectoras.ProtectoraCallback;
import es.studium.losamigosdeviky.veterinarios.Veterinario;
import es.studium.losamigosdeviky.veterinarios.VeterinarioCallback;
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

    // Ayuntamientos - Consulta (todos o por id)
    public static void consultarAyuntamientos(final AyuntamientoCallback callback) {
        consultarAyuntamientos(null, callback);
    }

    public static void consultarAyuntamientos(Integer idAyuntamiento, final AyuntamientoCallback callback) {
        OkHttpClient client = new OkHttpClient();
        String url = "http://192.168.1.131/ApiProtectora/ayuntamientos.php";
        if (idAyuntamiento != null) {
            url += "?idAyuntamiento=" + idAyuntamiento;
        }

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("BDConexion", "Network error: " + e.getMessage());
                callback.onResult(new ArrayList<>());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ArrayList<Ayuntamiento> ayuntamientos = new ArrayList<>();
                if (response.isSuccessful()) {
                    try {
                        if (idAyuntamiento == null) {
                            JSONArray result = new JSONArray(response.body().string());
                            for (int i = 0; i < result.length(); i++) {
                                JSONObject jsonObject = result.getJSONObject(i);
                                int id = jsonObject.getInt("idAyuntamiento");
                                String nombre = jsonObject.getString("nombreAyuntamiento");
                                int telefono = jsonObject.getInt("telefonoAyuntamiento");
                                String responsable = jsonObject.getString("responsableAyuntamiento");
                                String direccion = jsonObject.getString("direccionAyuntamiento");
                                int cp = jsonObject.getInt("cpAyuntamiento");

                                ayuntamientos.add(new Ayuntamiento(id, nombre, telefono, responsable, direccion, cp));
                            }
                        } else {
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            int id = jsonObject.getInt("idAyuntamiento");
                            String nombre = jsonObject.getString("nombreAyuntamiento");
                            int telefono = jsonObject.getInt("telefonoAyuntamiento");
                            String responsable = jsonObject.getString("responsableAyuntamiento");
                            String direccion = jsonObject.getString("direccionAyuntamiento");
                            int cp = jsonObject.getInt("cpAyuntamiento");

                            ayuntamientos.add(new Ayuntamiento(id, nombre, telefono, responsable, direccion, cp));
                        }
                    } catch (JSONException e) {
                        Log.e("BDConexion", "JSON parsing error: " + e.getMessage());
                    }
                } else {
                    Log.e("BDConexion", "Response not successful: " + response.message());
                }
                callback.onResult(ayuntamientos);
            }
        });
    }

    // Ayuntamiento - Alta
    public static void anadirAyuntamiento(Ayuntamiento ayuntamiento, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("idAyuntamiento", "")
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
                callback.onFailure(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callback.onResponse(call, response);
            }
        });
    }

    // Ayuntamiento - Modificacion
    public static void modificarAyuntamiento(Ayuntamiento ayuntamiento, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder queryUrlBuilder = HttpUrl.parse("http://192.168.1.131/ApiProtectora/ayuntamientos.php").newBuilder();
        queryUrlBuilder.addQueryParameter("idAyuntamiento", String.valueOf(ayuntamiento.getIdAyuntamiento()));
        queryUrlBuilder.addQueryParameter("nombreAyuntamiento", ayuntamiento.getNombreAyuntamiento());
        queryUrlBuilder.addQueryParameter("telefonoAyuntamiento", String.valueOf(ayuntamiento.getTelefonoAyuntamiento()));
        queryUrlBuilder.addQueryParameter("responsableAyuntamiento", ayuntamiento.getResponsableAyuntamiento());
        queryUrlBuilder.addQueryParameter("direccionAyuntamiento", ayuntamiento.getDireccionAyuntamiento());
        queryUrlBuilder.addQueryParameter("cpAyuntamiento", String.valueOf(ayuntamiento.getCpAyuntamiento()));

        RequestBody requestBody = new FormBody.Builder()
                .build();

        Request request = new Request.Builder()
                .url(queryUrlBuilder.build())
                .put(requestBody)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callback.onResponse(call, response);
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
                callback.onFailure(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callback.onResponse(call, response);
            }
        });
    }

    // Protectoras - Consulta (todas o por id)
    public static void consultarProtectoras(final ProtectoraCallback callback) {
        consultarProtectoras(null, callback);
    }

    public static void consultarProtectoras(Integer idProtectora, final ProtectoraCallback callback) {
        OkHttpClient client = new OkHttpClient();
        String url = "http://192.168.1.131/ApiProtectora/protectoras.php";
        if (idProtectora != null) {
            url += "?idProtectora=" + idProtectora;
        }

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("BDConexion", "Network error: " + e.getMessage());
                callback.onResult(new ArrayList<>());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ArrayList<Protectora> protectoras = new ArrayList<>();
                if (response.isSuccessful()) {
                    try {
                        if (idProtectora == null) {
                            JSONArray result = new JSONArray(response.body().string());
                            for (int i = 0; i < result.length(); i++) {
                                JSONObject jsonObject = result.getJSONObject(i);
                                int idProtectora = jsonObject.getInt("idProtectora");
                                String nombreProtectora = jsonObject.getString("nombreProtectora");
                                String direccionProtectora = jsonObject.getString("direccionProtectora");
                                String localidadProtectora = jsonObject.getString("localidadProtectora");
                                int telefonoProtectora = jsonObject.getInt("telefonoProtectora");
                                String correoProtectora = jsonObject.getString("correoProtectora");

                                protectoras.add(new Protectora(idProtectora, nombreProtectora, direccionProtectora, localidadProtectora, telefonoProtectora, correoProtectora));
                            }
                        } else {
                                JSONObject jsonObject = new JSONObject(response.body().string());
                                int idProtectora = jsonObject.getInt("idProtectora");
                                String nombreProtectora = jsonObject.getString("nombreProtectora");
                                String direccionProtectora = jsonObject.getString("direccionProtectora");
                                String localidadProtectora = jsonObject.getString("localidadProtectora");
                                int telefonoProtectora = jsonObject.getInt("telefonoProtectora");
                                String correoProtectora = jsonObject.getString("correoProtectora");

                                protectoras.add(new Protectora(idProtectora, nombreProtectora, direccionProtectora, localidadProtectora, telefonoProtectora, correoProtectora));
                        }
                    } catch (JSONException e) {
                        Log.e("BDConexion", "JSON parsing error: " + e.getMessage());
                    }
                } else {
                    Log.e("BDConexion", "Response not successful: " + response.message());
                }
                callback.onResult(protectoras);
            }
        });
    }

    // Protectora - Alta
    public static void anadirProtectora(Protectora protectora, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("idProtectora", "")
                .add("nombreProtectora", protectora.getNombreProtectora())
                .add("direccionProtectora", protectora.getDireccionProtectora())
                .add("localidadProtectora", protectora.getLocalidadProtectora())
                .add("telefonoProtectora", String.valueOf(protectora.getTelefonoProtectora()))
                .add("correoProtectora", protectora.getCorreoProtectora())
                .build();

        Request request = new Request.Builder()
                .url("http://192.168.1.131/ApiProtectora/protectoras.php")
                .post(formBody)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callback.onResponse(call, response);
            }
        });
    }

    // Protectora - Modificacion
    public static void modificarProtectora(Protectora protectora, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder queryUrlBuilder = HttpUrl.parse("http://192.168.1.131/ApiProtectora/protectoras.php").newBuilder();
        queryUrlBuilder.addQueryParameter("idProtectora", String.valueOf(protectora.getIdProtectora()));
        queryUrlBuilder.addQueryParameter("nombreProtectora", protectora.getNombreProtectora());
        queryUrlBuilder.addQueryParameter("direccionProtectora", protectora.getDireccionProtectora());
        queryUrlBuilder.addQueryParameter("localidadProtectora", protectora.getLocalidadProtectora());
        queryUrlBuilder.addQueryParameter("telefonoProtectora", String.valueOf(protectora.getTelefonoProtectora()));
        queryUrlBuilder.addQueryParameter("correoProtectora", protectora.getCorreoProtectora());

        RequestBody requestBody = new FormBody.Builder()
                .build();

        Request request = new Request.Builder()
                .url(queryUrlBuilder.build())
                .put(requestBody)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callback.onResponse(call, response);
            }
        });
    }

    // Protectora - Borrado
    public static void borrarProtectora(Protectora protectora, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://192.168.1.131/ApiProtectora/protectoras.php?idProtectora=" + protectora.getIdProtectora())
                .delete()
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callback.onResponse(call, response);
            }
        });
    }

    // Colonias - Consulta (todas o por id)
    public static void consultarColonias(final ColoniaCallback callback) {
        consultarColonias(null, callback);
    }

    public static void consultarColonias(Integer idColonia, final ColoniaCallback callback) {
        OkHttpClient client = new OkHttpClient();
        String url = "http://192.168.1.131/ApiProtectora/colonias.php";
        if (idColonia != null) {
            url += "?idColonia=" + idColonia;
        }

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("BDConexion", "Network error: " + e.getMessage());
                callback.onResult(new ArrayList<>());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ArrayList<Colonia> colonias = new ArrayList<>();
                if (response.isSuccessful()) {
                    try {
                        if (idColonia == null) {
                            JSONArray result = new JSONArray(response.body().string());
                            for (int i = 0; i < result.length(); i++) {
                                JSONObject jsonObject = result.getJSONObject(i);
                                int id = jsonObject.getInt("idColonia");
                                String nombre = jsonObject.getString("nombreColonia");
                                int cp = jsonObject.getInt("cpColonia");
                                String latitud = jsonObject.getString("latitudColonia");
                                String longitud = jsonObject.getString("longitudColonia");
                                String direccion = jsonObject.getString("direccionColonia");
                                int idAyuntamientoFK = jsonObject.getInt("idAyuntamientoFK1");
                                int idProtectoraFK = jsonObject.getInt("idProtectoraFK2");
                                colonias.add(new Colonia(id, nombre, cp, latitud, longitud, direccion, idAyuntamientoFK, idProtectoraFK));
                            }
                        } else {
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            int id = jsonObject.getInt("idColonia");
                            String nombre = jsonObject.getString("nombreColonia");
                            int cp = jsonObject.getInt("cpColonia");
                            String latitud = jsonObject.getString("latitudColonia");
                            String longitud = jsonObject.getString("longitudColonia");
                            String direccion = jsonObject.getString("direccionColonia");
                            int idAyuntamientoFK = jsonObject.getInt("idAyuntamientoFK1");
                            int idProtectoraFK = jsonObject.getInt("idProtectoraFK2");
                            colonias.add(new Colonia(id, nombre, cp, latitud, longitud, direccion, idAyuntamientoFK, idProtectoraFK));
                        }
                    } catch (JSONException e) {
                        Log.e("BDConexion", "JSON parsing error: " + e.getMessage());
                    }
                } else {
                    Log.e("BDConexion", "Response not successful: " + response.message());
                }
                callback.onResult(colonias);
            }
        });
    }

    // Colonia - Alta
    public static void anadirColonia(Colonia colonia, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("idColonia", "")
                .add("nombreColonia", colonia.getNombreColonia())
                .add("cpColonia", String.valueOf(colonia.getCpColonia()))
                .add("latitudColonia", colonia.getLatitudColonia())
                .add("longitudColonia", colonia.getLongitudColonia())
                .add("direccionColonia", colonia.getDireccionColonia())
                .add("idAyuntamientoFK1", String.valueOf(colonia.getIdAyuntamientoFK1()))
                .add("idProtectoraFK2", String.valueOf(colonia.getIdProtectoraFK2()))
                .build();

        Request request = new Request.Builder()
                .url("http://192.168.1.131/ApiProtectora/colonias.php")
                .post(formBody)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callback.onResponse(call, response);
            }
        });
    }

    // Colonia - Modificacion
    public static void modificarColonia(Colonia colonia, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder queryUrlBuilder = HttpUrl.parse("http://192.168.1.131/ApiProtectora/colonias.php").newBuilder();

        queryUrlBuilder.addQueryParameter("idColonia", String.valueOf(colonia.getIdColonia()));
        queryUrlBuilder.addQueryParameter("nombreColonia", colonia.getNombreColonia());
        queryUrlBuilder.addQueryParameter("cpColonia", String.valueOf(colonia.getCpColonia()));
        queryUrlBuilder.addQueryParameter("latitudColonia", colonia.getLatitudColonia());
        queryUrlBuilder.addQueryParameter("longitudColonia", colonia.getLongitudColonia());
        queryUrlBuilder.addQueryParameter("direccionColonia", colonia.getDireccionColonia());
        queryUrlBuilder.addQueryParameter("idAyuntamientoFK1", String.valueOf(colonia.getIdAyuntamientoFK1()));
        queryUrlBuilder.addQueryParameter("idProtectoraFK2", String.valueOf(colonia.getIdProtectoraFK2()));

        RequestBody requestBody = new FormBody.Builder()
                .build();

        Request request = new Request.Builder()
                .url(queryUrlBuilder.build())
                .put(requestBody)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callback.onResponse(call, response);
            }
        });
    }

    // Colonia - Borrado
    public static void borrarColonia(Colonia colonia, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://192.168.1.131/ApiProtectora/colonias.php?idColonia=" + colonia.getIdColonia())
                .delete()
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callback.onResponse(call, response);
            }
        });
    }

    // Veterinarios - Consulta (todos o por id)
    public static void consultarVeterinarios(final VeterinarioCallback callback) {
        consultarVeterinarios(null, callback);
    }

    public static void consultarVeterinarios(Integer idVeterinario, final VeterinarioCallback callback) {
        OkHttpClient client = new OkHttpClient();
        String url = "http://192.168.1.131/ApiProtectora/veterinarios.php";
        if (idVeterinario != null) {
            url += "?idVeterinario=" + idVeterinario;
        }

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("BDConexion", "Network error: " + e.getMessage());
                callback.onResult(new ArrayList<>());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ArrayList<Veterinario> veterinarios = new ArrayList<>();
                if (response.isSuccessful()) {
                    try {
                        if (idVeterinario == null) {
                            JSONArray result = new JSONArray(response.body().string());
                            for (int i = 0; i < result.length(); i++) {
                                JSONObject jsonObject = result.getJSONObject(i);
                                int id = jsonObject.getInt("idVeterinario");
                                String nombre = jsonObject.getString("nombreVeterinario");
                                String apellidos = jsonObject.getString("apellidosVeterinario");
                                int telefono = jsonObject.getInt("telefonoVeterinario");
                                String especialidad = jsonObject.getString("especialidadVeterinario");

                                veterinarios.add(new Veterinario(id, nombre, apellidos, telefono, especialidad));
                            }
                        } else {
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            int id = jsonObject.getInt("idVeterinario");
                            String nombre = jsonObject.getString("nombreVeterinario");
                            String apellidos = jsonObject.getString("apellidosVeterinario");
                            int telefono = jsonObject.getInt("telefonoVeterinario");
                            String especialidad = jsonObject.getString("especialidadVeterinario");

                            veterinarios.add(new Veterinario(id, nombre, apellidos, telefono, especialidad));
                        }
                    } catch (JSONException e) {
                        Log.e("BDConexion", "JSON parsing error: " + e.getMessage());
                    }
                } else {
                    Log.e("BDConexion", "Response not successful: " + response.message());
                }
                callback.onResult(veterinarios);
            }
        });
    }

    // Veterinario - Alta
    public static void anadirVeterinario(Veterinario veterinario, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("idAyuntamiento", "")
                .add("nombreVeterinario", veterinario.getNombreVeterinario())
                .add("apellidosVeterinario", veterinario.getApellidosVeterinario())
                .add("telefonoVeterinario", String.valueOf(veterinario.getTelefonoVeterinario()))
                .add("especialidadVeterinario", veterinario.getEspecialidadVeterinario())
                .build();

        Request request = new Request.Builder()
                .url("http://192.168.1.131/ApiProtectora/veterinarios.php")
                .post(formBody)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callback.onResponse(call, response);
            }
        });
    }

    // Veterinario - Modificacion
    public static void modificarVeterinario(Veterinario veterinario, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder queryUrlBuilder = HttpUrl.parse("http://192.168.1.131/ApiProtectora/veterinarios.php").newBuilder();
        queryUrlBuilder.addQueryParameter("idVeterinario", String.valueOf(veterinario.getIdVeterinario()));
        queryUrlBuilder.addQueryParameter("nombreVeterinario", veterinario.getNombreVeterinario());
        queryUrlBuilder.addQueryParameter("apellidosVeterinario", veterinario.getApellidosVeterinario());
        queryUrlBuilder.addQueryParameter("telefonoVeterinario", String.valueOf(veterinario.getTelefonoVeterinario()));
        queryUrlBuilder.addQueryParameter("especialidadVeterinario", veterinario.getEspecialidadVeterinario());

        RequestBody requestBody = new FormBody.Builder()
                .build();

        Request request = new Request.Builder()
                .url(queryUrlBuilder.build())
                .put(requestBody)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callback.onResponse(call, response);
            }
        });
    }

    // Veterinario - Borrado
    public static void borrarVeterinario(Veterinario veterinario, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://192.168.1.131/ApiProtectora/veterinarios.php?idVeterinario=" + veterinario.getIdVeterinario())
                .delete()
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callback.onResponse(call, response);
            }
        });
    }

    // Gatos - Consulta (todos o por id)
    public static void consultarGatos(final GatoCallback callback) {
        consultarGatos(null, callback);
    }

    public static void consultarGatos(Integer idGato, final GatoCallback callback) {
        OkHttpClient client = new OkHttpClient();
        String url = "http://192.168.1.131/ApiProtectora/gatos.php";
        if (idGato != null) {
            url += "?idGato=" + idGato;
        }

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("BDConexion", "Network error: " + e.getMessage());
                callback.onResult(new ArrayList<>());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ArrayList<Gato> gatos = new ArrayList<>();
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    Log.d("BDConexion", "Server Response: " + responseBody);
                    try {
                        if (idGato == null) {
                            JSONArray result = new JSONArray(responseBody);
                            for (int i = 0; i < result.length(); i++) {
                                JSONObject jsonObject = result.getJSONObject(i);
                                int id = jsonObject.getInt("idGato");
                                String nombre = jsonObject.getString("nombreGato");
                                String sexo = jsonObject.getString("sexoGato");
                                String descripcion = jsonObject.getString("descripcionGato");
                                int esEsterilizado = jsonObject.getInt("esEsterilizado");
                                String fotoGato = jsonObject.getString("fotoGato");
                                String[] fn = jsonObject.getString("fechaNacimientoGato").split("-");
                                LocalDate fechaNacimientoGato = LocalDate.of(Integer.parseInt(fn[0]), Integer.parseInt(fn[1]), Integer.parseInt(fn[2]));
                                String chipGato = jsonObject.getString("chipGato");
                                int idColoniaFK = jsonObject.getInt("idColoniaFK4");
                                gatos.add(new Gato(id, nombre, sexo, descripcion, esEsterilizado, fotoGato, fechaNacimientoGato, chipGato, idColoniaFK));
                            }
                        } else {
                            JSONObject jsonObject = new JSONObject(responseBody);
                            int id = jsonObject.getInt("idGato");
                            String nombre = jsonObject.getString("nombreGato");
                            String sexo = jsonObject.getString("sexoGato");
                            String descripcion = jsonObject.getString("descripcionGato");
                            int esEsterilizado = jsonObject.getInt("esEsterilizado");
                            String fotoGato = jsonObject.getString("fotoGato");
                            String[] fn = jsonObject.getString("fechaNacimientoGato").split("-");
                            LocalDate fechaNacimientoGato = LocalDate.of(Integer.parseInt(fn[0]), Integer.parseInt(fn[1]), Integer.parseInt(fn[2]));
                            String chipGato = jsonObject.getString("chipGato");
                            int idColoniaFK = jsonObject.getInt("idColoniaFK4");
                            gatos.add(new Gato(id, nombre, sexo, descripcion, esEsterilizado, fotoGato, fechaNacimientoGato, chipGato, idColoniaFK));
                        }
                    } catch (JSONException e) {
                        Log.e("BDConexion", "JSON parsing error: " + e.getMessage());
                    }
                } else {
                    Log.e("BDConexion", "Response not successful: " + response.message());
                }
                callback.onResult(gatos);
            }
        });
    }

    // Gato - Alta
    public static void anadirGato(Gato gato, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("idGato", "")
                .add("nombreGato", gato.getNombreGato())
                .add("sexoGato", gato.getSexoGato())
                .add("descripcionGato", gato.getDescripcionGato())
                .add("esEsterilizado", String.valueOf(gato.getEsEsterilizado()))
                .add("fotoGato", gato.getFotoGato())
                .add("fechaNacimientoGato", gato.getFechaNacimientoGato().toString())
                .add("chipGato", gato.getChipGato())
                .add("idColoniaFK4", String.valueOf(gato.getIdColoniaFK4()))
                .build();

        Request request = new Request.Builder()
                .url("http://192.168.1.131/ApiProtectora/gatos.php")
                .post(formBody)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();
                Log.d("API Response", responseBody);
                callback.onResponse(call, response);
            }
        });
    }

    // Gato - Modificacion
    public static void modificarGato(Gato gato, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder queryUrlBuilder = HttpUrl.parse("http://192.168.1.131/ApiProtectora/gatos.php").newBuilder();
        queryUrlBuilder.addQueryParameter("idGato", String.valueOf(gato.getIdGato()));
        queryUrlBuilder.addQueryParameter("nombreGato", gato.getNombreGato());
        queryUrlBuilder.addQueryParameter("sexoGato", gato.getSexoGato());
        queryUrlBuilder.addQueryParameter("descripcionGato", gato.getDescripcionGato());
        queryUrlBuilder.addQueryParameter("esEsterilizado", String.valueOf(gato.getEsEsterilizado()));
        queryUrlBuilder.addQueryParameter("fotoGato", gato.getFotoGato());
        queryUrlBuilder.addQueryParameter("fechaNacimientoGato", gato.getFechaNacimientoGato().toString());
        queryUrlBuilder.addQueryParameter("chipGato", gato.getChipGato());
        queryUrlBuilder.addQueryParameter("idColoniaFK4", String.valueOf(gato.getIdColoniaFK4()));

        RequestBody requestBody = new FormBody.Builder()
                .build();

        Request request = new Request.Builder()
                .url(queryUrlBuilder.build())
                .put(requestBody)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callback.onResponse(call, response);
            }
        });
    }

    // Gato - Borrado
    public static void borrarGato(Gato gato, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://192.168.1.131/ApiProtectora/gatos.php?idGato=" + gato.getIdGato())
                .delete()
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callback.onResponse(call, response);
            }
        });
    }

    // Cuidados - Consulta (todos o por id)
    public static void consultarCuidados(final CuidadoCallback callback) {
        consultarCuidados(null, callback);
    }

    public static void consultarCuidados(Integer idCuidado, final CuidadoCallback callback) {
        OkHttpClient client = new OkHttpClient();
        String url = "http://192.168.1.131/ApiProtectora/cuidados.php";
        if (idCuidado != null) {
            url += "?idCuidado=" + idCuidado;
        }

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("BDConexion", "Network error: " + e.getMessage());
                callback.onResult(new ArrayList<>());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ArrayList<Cuidado> cuidados = new ArrayList<>();
                if (response.isSuccessful()) {
                    try {
                        if (idCuidado == null) {
                            JSONArray result = new JSONArray(response.body().string());
                            for (int i = 0; i < result.length(); i++) {
                                JSONObject jsonObject = result.getJSONObject(i);
                                int id = jsonObject.getInt("idCuida");
                                String[] fi = jsonObject.getString("fechaInicioCuidado").split("-");
                                LocalDate fechaInicio = LocalDate.of(Integer.parseInt(fi[0]), Integer.parseInt(fi[1]), Integer.parseInt(fi[2]));
                                String[] ff = jsonObject.getString("fechaFinCuidado").split("-");
                                LocalDate fechaFin = LocalDate.of(Integer.parseInt(ff[0]), Integer.parseInt(ff[1]), Integer.parseInt(ff[2]));
                                String descripcion = jsonObject.getString("descripcionCuidado");
                                String posologia = jsonObject.getString("posologiaCuidado");
                                int idGatoFK = jsonObject.getInt("idGatoFK5");
                                int idVeterinarioFK = jsonObject.getInt("idVeterinarioFK6");
                                cuidados.add(new Cuidado(id, fechaInicio, fechaFin, descripcion, posologia, idGatoFK, idVeterinarioFK));
                            }
                        } else {
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            int id = jsonObject.getInt("idCuida");
                            String[] fi = jsonObject.getString("fechaInicioCuidado").split("-");
                            LocalDate fechaInicio = LocalDate.of(Integer.parseInt(fi[0]), Integer.parseInt(fi[1]), Integer.parseInt(fi[2]));
                            String[] ff = jsonObject.getString("fechaFinCuidado").split("-");
                            LocalDate fechaFin = LocalDate.of(Integer.parseInt(ff[0]), Integer.parseInt(ff[1]), Integer.parseInt(ff[2]));
                            String descripcion = jsonObject.getString("descripcionCuidado");
                            String posologia = jsonObject.getString("posologiaCuidado");
                            int idGatoFK = jsonObject.getInt("idGatoFK5");
                            int idVeterinarioFK = jsonObject.getInt("idVeterinarioFK6");
                            cuidados.add(new Cuidado(id, fechaInicio, fechaFin, descripcion, posologia, idGatoFK, idVeterinarioFK));
                        }
                    } catch (JSONException e) {
                        Log.e("BDConexion", "JSON parsing error: " + e.getMessage());
                    }
                } else {
                    Log.e("BDConexion", "Response not successful: " + response.message());
                }
                callback.onResult(cuidados);
            }
        });
    }

    // Cuidado - Alta
    public static void anadirCuidado(Cuidado cuidado, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("idCuidado", "")
                .add("fechaInicioCuidado", cuidado.getFechaInicioCuidado().toString())
                .add("fechaFinCuidado", cuidado.getFechaFinCuidado().toString())
                .add("descripcionCuidado", cuidado.getDescripcionCuidado())
                .add("posologiaCuidado", cuidado.getPosologiaCuidado())
                .add("idGatoFK5", String.valueOf(cuidado.getIdGatoFK()))
                .add("idVeterinarioFK6", String.valueOf(cuidado.getIdVeterinarioFK()))
                .build();

        Request request = new Request.Builder()
                .url("http://192.168.1.131/ApiProtectora/cuidados.php")
                .post(formBody)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();
                Log.d("API Response", responseBody);
                callback.onResponse(call, response);
            }
        });
    }

    // Cuidado - Modificacion
    public static void modificarCuidado(Cuidado cuidado, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder queryUrlBuilder = HttpUrl.parse("http://192.168.1.131/ApiProtectora/cuidados.php").newBuilder();
        queryUrlBuilder.addQueryParameter("idCuidado", String.valueOf(cuidado.getIdCuidado()));
        queryUrlBuilder.addQueryParameter("fechaInicioCuidado", cuidado.getFechaInicioCuidado().toString());
        queryUrlBuilder.addQueryParameter("fechaFinCuidado", cuidado.getFechaFinCuidado().toString());
        queryUrlBuilder.addQueryParameter("descripcionCuidado", cuidado.getDescripcionCuidado());
        queryUrlBuilder.addQueryParameter("posologiaCuidado", cuidado.getPosologiaCuidado());
        queryUrlBuilder.addQueryParameter("idGatoFK5", String.valueOf(cuidado.getIdGatoFK()));
        queryUrlBuilder.addQueryParameter("idVeterinarioFK6", String.valueOf(cuidado.getIdVeterinarioFK()));

        RequestBody requestBody = new FormBody.Builder()
                .build();

        Request request = new Request.Builder()
                .url(queryUrlBuilder.build())
                .put(requestBody)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callback.onResponse(call, response);
            }
        });
    }

    // Cuidado - Borrado
    public static void borrarCuidado(Cuidado cuidado, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://192.168.1.131/ApiProtectora/cuidados.php?idCuida=" + cuidado.getIdCuidado())
                .delete()
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callback.onResponse(call, response);
            }
        });
    }
}
