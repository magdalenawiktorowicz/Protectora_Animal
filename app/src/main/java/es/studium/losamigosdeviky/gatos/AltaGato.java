package es.studium.losamigosdeviky.gatos;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import es.studium.losamigosdeviky.BDConexion;
import es.studium.losamigosdeviky.R;
import es.studium.losamigosdeviky.colonias.Colonia;
import es.studium.losamigosdeviky.colonias.ColoniaCallback;
import es.studium.losamigosdeviky.veterinarios.Veterinario;
import es.studium.losamigosdeviky.veterinarios.VeterinarioCallback;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AltaGato extends DialogFragment implements AdapterView.OnItemSelectedListener {

    ImageView imageViewFotoGato;
    EditText editTextNombreGato, editTextSexoGato, editTextDescripcionGato, editTextFechaNacimientoGato, editTextChipGato;
    SwitchCompat switchEsterilizadoGato;
    Spinner spinnerColoniaFKGato;
    private List<Colonia> colonias = new ArrayList<>();
    private Uri imageUri = null;
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 101;
    private String[] cameraPermissions;
    private String[] storagePermissions;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_alta_gato, null);
        Context context = v.getContext();
        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        imageViewFotoGato = v.findViewById(R.id.imageViewAltaFotoGato);
        imageViewFotoGato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputImageDialog();
            }
        });
        editTextNombreGato = v.findViewById(R.id.editTextAltaNombreGato);
        editTextSexoGato = v.findViewById(R.id.editTextAltaSexoGato);
        editTextDescripcionGato = v.findViewById(R.id.editTextAltaDescripcionGato);
        editTextFechaNacimientoGato = v.findViewById(R.id.editTextAltaFechaNacimientoGato);
        editTextChipGato = v.findViewById(R.id.editTextAltaChipGato);
        switchEsterilizadoGato = v.findViewById(R.id.switchAltaEsterlizadoGato);
        spinnerColoniaFKGato = v.findViewById(R.id.spinnerAltaColoniaFKGato);

        // Set up Colonia Spinner
        BDConexion.consultarColonias(new ColoniaCallback() {
            @Override
            public void onResult(ArrayList<Colonia> cols) {
                if (cols != null && getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        colonias.addAll(cols);
                        List<String> spinnerArrayColonias = new ArrayList<>();
                        spinnerArrayColonias.add("Selecciona la colonia...");
                        for (Colonia c : colonias) {
                            spinnerArrayColonias.add(c.getNombreColonia());
                        }
                        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, spinnerArrayColonias);
                        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                        spinnerColoniaFKGato.setAdapter(spinnerArrayAdapter);
                    });
                }
            }
        });

        spinnerColoniaFKGato.setOnItemSelectedListener(this);

        builder.setView(v)
                .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!editTextNombreGato.getText().toString().isBlank() && !editTextSexoGato.getText().toString().isBlank() && !editTextDescripcionGato.getText().toString().isBlank() && !editTextFechaNacimientoGato.getText().toString().isBlank() && !editTextChipGato.getText().toString().isBlank() && spinnerColoniaFKGato.getSelectedItemPosition() != 0) {
                            String nombreGato = editTextNombreGato.getText().toString();
                            String sexoGato = editTextSexoGato.getText().toString();
                            String descripcionGato = editTextDescripcionGato.getText().toString();
                            int esEsterilizado = (switchEsterilizadoGato.isChecked() ? 1 : 0);
                            String fotoGato = String.valueOf(imageUri);
                            LocalDate fechaNacimientoGato = null;
                            if (comprobarFecha(editTextFechaNacimientoGato.getText().toString())) {
                                String[] fn = (editTextFechaNacimientoGato.getText().toString()).split("-");
                                fechaNacimientoGato = LocalDate.of(Integer.parseInt(fn[0]), Integer.parseInt(fn[1]), Integer.parseInt(fn[2]));
                            }
                            String chipGato = editTextChipGato.getText().toString();
                            String coloniaNombre = spinnerColoniaFKGato.getSelectedItem().toString();
                            int coloniaFKGato = colonias.stream()
                                    .filter(c -> (c.getNombreColonia()).equals(coloniaNombre))
                                    .map(Colonia::getIdColonia)
                                    .findFirst()
                                    .orElse(-1);

                            Gato newGato = new Gato(nombreGato, sexoGato, descripcionGato, esEsterilizado, fotoGato, fechaNacimientoGato, chipGato, coloniaFKGato);
                            // DAR DE ALTA + INFORMAR SOBRE EL RESULTADO
                            BDConexion.anadirGato(newGato, new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    new Handler(Looper.getMainLooper()).post(() -> {
                                        Toast.makeText(context, "Error: la operación no se ha realizado.", Toast.LENGTH_SHORT).show();
                                        // Send result
                                        if (isAdded()) {
                                            sendResult(false);
                                        }
                                    });
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    new Handler(Looper.getMainLooper()).post(() -> {
                                        if (response.code() == 200) {
                                            if (isAdded()) {
                                                sendResult(true);
                                            }
                                            Toast.makeText(context, "La operación se ha realizado correctamente.", Toast.LENGTH_SHORT).show();
                                        } else {
                                            // Send result
                                            if (isAdded()) {
                                                sendResult(false);
                                            }
                                            Toast.makeText(context, "Error: la operación no se ha realizado.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });
                            dialog.dismiss();
                        } else {
                            Toast.makeText(context, "Rellena todos los campos.", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(R.color.background);
        return alertDialog;
    }

    private void sendResult(boolean success) {
        Bundle result = new Bundle();
        result.putBoolean("operationSuccess", success);
        if (isAdded()) {
            getParentFragmentManager().setFragmentResult("altaGatoRequestKey", result);
        }
    }

    private boolean comprobarFecha(String string) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        dateFormat.setLenient(false); // Ensures strict parsing
        try {
            dateFormat.parse(string);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private void showInputImageDialog() {
        PopupMenu popupMenu = new PopupMenu(getContext(), imageViewFotoGato);
        popupMenu.getMenu().add(Menu.NONE, 1, 1, "Cámara"); // hacer una foto
        popupMenu.getMenu().add(Menu.NONE, 2, 2, "Galeria"); // seleccionar una imagén del móvil
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == 1) {
                    // comprobar los permisos de la cámara
                    if (checkCameraPermissions()) {
                        pickImageCamera(); // método para hacer una foto
                    } else {
                        requestCameraPermissions();
                    }
                } else if (item.getItemId() == 2) {
                    // comprobar los permisos a los archivos del móvil
                    if (checkStoragePermission()) {
                        pickImageGallery(); // método para seleccionar una imagen
                    } else {
                        requestStoragePermission();
                    }
                }
                return false;
            }
        });
    }

    // método para seleccionar una foto del móvil
    private void pickImageGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        galleryActivityResultLauncher.launch(intent);
    }

    private ActivityResultLauncher<Intent> galleryActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // al seleccionar la foto del móvil
                        imageUri = result.getData().getData(); // obtener su Uri
                        // asignarle al producto
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imageUri);
                            imageViewFotoGato.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
    );

    // método para hacer una foto
    private void pickImageCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Sample Title");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Sample Description");
        imageUri = requireContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        cameraActivityResultLauncher.launch(intent);
    }

    private ActivityResultLauncher<Intent> cameraActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    // al hacer una foto, recibir la imagen
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // asignar la Uri de la foto al producto
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imageUri);
                            imageViewFotoGato.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
    );

    // comprobar los permisos a los archivos del móvil
    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    // pedir los permisos a los archivos del móvil
    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(getActivity(), storagePermissions, STORAGE_REQUEST_CODE);
    }

    // comprobar los permisos a la cámara
    private boolean checkCameraPermissions() {
        boolean cameraResult = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean storageResult = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return cameraResult && storageResult;
    }

    // pedir los permisos a la cámara
    private void requestCameraPermissions() {
        ActivityCompat.requestPermissions(getActivity(), cameraPermissions, CAMERA_REQUEST_CODE);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {}

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}
}