package es.studium.losamigosdeviky.gatos;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import es.studium.losamigosdeviky.BDConexion;
import es.studium.losamigosdeviky.R;
import es.studium.losamigosdeviky.RecyclerViewOnItemClickListener;
import es.studium.losamigosdeviky.colonias.Colonia;
import es.studium.losamigosdeviky.colonias.ColoniaCallback;
import es.studium.losamigosdeviky.veterinarios.Veterinario;
import es.studium.losamigosdeviky.veterinarios.VeterinarioCallback;

public class GatosViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private final TextView nombreGato;
    private final ImageView imagenGato;
    private final TextView fechaNacimientoGato;
    private final TextView chipGato;
    private final TextView sexoGato;
    private final TextView esterilizadoGato;
    private final TextView descripcionGato;
    private final TextView coloniaFKGato;

    private final RecyclerViewOnItemClickListener listener;
    private Context contexto;

    public GatosViewHolder(@NonNull View itemView, @NonNull RecyclerViewOnItemClickListener listener) {
        super(itemView);
        contexto = itemView.getContext();
        nombreGato = itemView.findViewById(R.id.textViewNombreGato);
        imagenGato = itemView.findViewById(R.id.imageViewImagenGato);
        fechaNacimientoGato = itemView.findViewById(R.id.textViewFechaNacimientoGato);
        chipGato = itemView.findViewById(R.id.textViewChipGato);
        sexoGato = itemView.findViewById(R.id.textViewSexoGato);
        esterilizadoGato = itemView.findViewById(R.id.textViewEsterilizadoGato);
        descripcionGato = itemView.findViewById(R.id.textViewDescripcionGato);
        coloniaFKGato = itemView.findViewById(R.id.textViewColoniaFKGato);
        this.listener = listener;
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                listener.onLongClick(v, getAbsoluteAdapterPosition());
                return true;
            }
        });
    }

    public void bindRow(@NonNull Gato gato) {
        final Gato currentGato = gato;

        String imageUri = gato.getFotoGato();
        if (imageUri != null && !imageUri.equals("null") && !imageUri.isBlank()) {
            Glide.with(contexto)
                    .load(Uri.parse(imageUri))
                    .placeholder(R.drawable.no_photo) // en caso de que no hay acceso a la imagen seleccionada
                    .error(R.drawable.no_photo)
                    .into(imagenGato);
        }

        nombreGato.setText(gato.getNombreGato());
        fechaNacimientoGato.setText(contexto.getResources().getString(R.string.fecha_nacimiento) + " " +gato.getFechaNacimientoGato().toString());
        chipGato.setText(contexto.getResources().getString(R.string.chip) + " " +gato.getChipGato());
        sexoGato.setText(contexto.getResources().getString(R.string.sexo) + " " +gato.getSexoGato());
        esterilizadoGato.setText(contexto.getResources().getString(R.string.esterilizado) + " " + (gato.getEsEsterilizado() == 1 ? "sí" : "no"));
        descripcionGato.setText(contexto.getResources().getString(R.string.descripcion) + " " + gato.getDescripcionGato());
        coloniaFKGato.setText(contexto.getResources().getString(R.string.coloniaFK));

        BDConexion.consultarColonias(gato.getIdColoniaFK4(), new ColoniaCallback() {
            @Override
            public void onResult(ArrayList<Colonia> colonias) {
                if (!colonias.isEmpty()) {
                    Colonia colonia = colonias.get(0);
                    String coloniaData = colonia.getNombreColonia() + "\n\t\t\t" + colonia.getDireccionColonia();

                    // Update the UI with the Colonia data
                    updateUIWithColoniaData(currentGato, coloniaData);
                }
            }
        });
    }


    private void updateUIWithColoniaData(Gato gato, String coloniaData) {
        coloniaFKGato.setText(contexto.getResources().getString(R.string.coloniaFK) + " " + coloniaData);
    }

    @Override
    public void onClick(View v) {
        listener.onClick(v, getAbsoluteAdapterPosition());
    }
}
