package es.studium.losamigosdeviky;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private static final int MY_PERMISSIONS_REQUEST_INTERNET = 1001;
    EditText editTextUsuario, editTextClave;
    Button btnAcceder;
    SwitchCompat switchGuardarCredenciales;

    // nombre de las preferencias compartidas
    public static final String LoginCredenciales = "LoginCredenciales";
    // nombres de los claves en las preferencias compartidas
    public static final String Usuario = "usuarioKey";
    public static final String Clave = "claveKey";
    public static final String Tipo = "tipoKey";
    public static final String Creds = "credencialesGuardadas";
    SharedPreferences sharedpreferences;
    String usuarioInput, contrasenaInput;
    Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            // establecer el color del fondo de la barra superior
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.main_lighter)));
            // establecer el título de la barra superior
            getSupportActionBar().setTitle(R.string.app_name);

        // obtener una referencia a la colección de preferencias compartidas
        sharedpreferences = getSharedPreferences(LoginCredenciales, Context.MODE_PRIVATE);
        // obtener los valores del usuario y la clave
        String isSharedUsuario = sharedpreferences.getString(Usuario, "");
        String isSharedClave = sharedpreferences.getString(Clave, "");

        // si el nombre y la clave ya están guardadas en las preferencias compartidas
        if (!(isSharedUsuario.isEmpty()) && !(isSharedClave.isEmpty())) {
            // pasar a la ventana principal a través del Intent
            Intent intentMain = new Intent(this, MainActivity.class);
            intentMain.putExtra("nombreUsuario", sharedpreferences.getString("nombreUsuario", ""));
            intentMain.putExtra("tipoUsuario", sharedpreferences.getInt("tipoKey", -1));
            startActivity(intentMain);
            finish(); // deshabilitar el botón 'back'
        }
        else {
            // establecer el contenido de la vista de login
            setContentView(R.layout.activity_login);
            // enlazar los elementos del código con los de la vista
            editTextUsuario = findViewById(R.id.username);
            editTextClave = findViewById(R.id.contrasena);
            switchGuardarCredenciales = findViewById(R.id.switchSaveCredentials);
            switchGuardarCredenciales.setChecked(false);
            btnAcceder = findViewById(R.id.btnAcceder);
            btnAcceder.setEnabled(false);
            btnAcceder.setOnClickListener(this);
            // objeto textWatcher nos permite supervisar cuando el contenido de los editTexts cambia
            TextWatcher textWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                // si ocurre un cambio dentro de los editText
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    // si los los editTexts no están vacíos - se habita el botón 'Acceder'
                    if (!(editTextUsuario.getText().toString().isBlank()) && !(editTextClave.getText().toString().isBlank())) {
                        btnAcceder.setEnabled(true);
                    } else {
                        btnAcceder.setEnabled(false);
                    }
                }
                @Override
                public void afterTextChanged(Editable s) {
                }
            };
            editTextUsuario.addTextChangedListener(textWatcher);
            editTextClave.addTextChangedListener(textWatcher);
            // método para comprobar permisos
            checkPermissions();
        }
    }

    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.INTERNET)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.INTERNET},
                        MY_PERMISSIONS_REQUEST_INTERNET);
            } else {
                // esperar hasta un click
            }
        } else {
            // esperar hasta un click
        }
    }

    private void comprobarCreds() {
        // llamar al método de BDConexion para comprobar las credenciales del usuario de la app
        BDConexion.comprobarCredenciales(usuarioInput, contrasenaInput, new BDConexion.LoginCallback() {
            @Override
            public void onLoginResult(boolean success, int tipoUsuario, String nombreUsuario) {
                try {
                    if (success) {
                        // si el usuario existe en la tabla 'usuarios' de BD 'protectora', seguir a entrar en sesión
                        performLogin(usuarioInput, contrasenaInput, tipoUsuario);
                    } else {
                        toast = Toast.makeText(getApplicationContext(), "Credenciales incorrectas.", Toast.LENGTH_SHORT);
                        makeToast();
                    }
                } catch (Exception e) {}
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_INTERNET) {
            // Comprobar si la app tiene los permisos
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // en caso positivo, seguir a comprobar las credenciales
                comprobarCreds();
            } else {
                // caso negativo
                toast = Toast.makeText(this, "Internet permission denied", Toast.LENGTH_SHORT);
                makeToast();
            }
        }
    }

    private void performLogin(String usuario, String contrasena, int tipo) {
        MainActivity.tipoUsuario = tipo;
        if (switchGuardarCredenciales.isChecked()) {
            // guardar las credenciales en sharedPreferences
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString(Usuario, usuario);
            editor.putString(Clave, contrasena);
            editor.putInt(Tipo, MainActivity.tipoUsuario);
            editor.putBoolean(Creds, true);
            editor.commit();
        }
        // pasar a la ventana principal
        Intent intentMain = new Intent(this, MainActivity.class);
        intentMain.putExtra("nombreUsuario", usuarioInput);
        intentMain.putExtra("contrasenaUsuario", contrasenaInput);
        intentMain.putExtra("tipoUsuario", MainActivity.tipoUsuario);
        intentMain.putExtra("sharedPrefsClicked", switchGuardarCredenciales.isChecked());
        startActivity(intentMain);
        finish();
    }

    @Override
    public void onClick(View v) {
        // obtener el nombre y la contraseña del usuario cuando pulsa el botón Acceder
        if (v.getId() == R.id.btnAcceder) {
            usuarioInput = editTextUsuario.getText().toString();
            contrasenaInput = editTextClave.getText().toString();
            if (!usuarioInput.isEmpty() && !contrasenaInput.isEmpty()) {
                comprobarCreds();
            } else {
                toast = Toast.makeText(this, "Por favor, introduce el nombre del usuario y contraseña", Toast.LENGTH_SHORT);
                makeToast();
            }
        }
    }

    private void makeToast() {
        View toastView = toast.getView();
        TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
        toastMessage.setTextAppearance(R.style.ToastStyle);
        toastView.setBackground(getResources().getDrawable(R.drawable.toast_shape));
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}