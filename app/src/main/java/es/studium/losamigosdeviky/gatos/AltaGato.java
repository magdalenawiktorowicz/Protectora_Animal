package es.studium.losamigosdeviky.gatos;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.SwitchCompat;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    Spinner spinnerVeterinarioFKGato, spinnerColoniaFKGato;
    private List<Veterinario> veterinarios = new ArrayList<>();
    private List<Colonia> colonias = new ArrayList<>();
    private Uri imageUri = null;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_alta_gato, null);
        Context context = v.getContext();
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
        spinnerVeterinarioFKGato = v.findViewById(R.id.spinnerAltaVeterinarioFKGato);
        spinnerColoniaFKGato = v.findViewById(R.id.spinnerAltaColoniaFKGato);

        // Set up Veterinario Spinner
        BDConexion.consultarVeterinarios(new VeterinarioCallback() {
            @Override
            public void onResult(ArrayList<Veterinario> vets) {
                if (vets != null && getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        veterinarios.addAll(vets);
                        List<String> spinnerArrayVeterinarios = new ArrayList<>();
                        spinnerArrayVeterinarios.add("Selecciona el veterinario...");
                        for (Veterinario v : veterinarios) {
                            spinnerArrayVeterinarios.add(v.getNombreVeterinario() + " " + v.getApellidosVeterinario());
                        }
                        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, spinnerArrayVeterinarios);
                        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                        spinnerVeterinarioFKGato.setAdapter(spinnerArrayAdapter);
                    });
                }
            }
        });

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

        spinnerVeterinarioFKGato.setOnItemSelectedListener(this);
        spinnerColoniaFKGato.setOnItemSelectedListener(this);

        builder.setView(v)
                .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!editTextNombreGato.getText().toString().isBlank() && !editTextSexoGato.getText().toString().isBlank() && !editTextDescripcionGato.getText().toString().isBlank() && !editTextFechaNacimientoGato.getText().toString().isBlank() && !editTextChipGato.getText().toString().isBlank() && spinnerVeterinarioFKGato.getSelectedItemPosition() != 0 && spinnerColoniaFKGato.getSelectedItemPosition() != 0) {
                            String nombreGato = editTextNombreGato.getText().toString();
                            String sexoGato = editTextSexoGato.getText().toString();
                            String descripcionGato = editTextDescripcionGato.getText().toString();
                            int esEsterilizado = (switchEsterilizadoGato.isChecked() ? 1 : 0);
                            //byte[] fotoGato = ????

                        }
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(R.color.background);
        return alertDialog;
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
                    pickImageCamera(); // método para hacer una foto
                } else if (item.getItemId() == 2) {
                    pickImageGallery(); // método para seleccionar una imagen
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}