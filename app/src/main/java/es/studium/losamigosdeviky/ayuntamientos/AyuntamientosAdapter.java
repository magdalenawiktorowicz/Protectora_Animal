package es.studium.losamigosdeviky.ayuntamientos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.studium.losamigosdeviky.R;
import es.studium.losamigosdeviky.RecyclerViewOnItemClickListener;

public class AyuntamientosAdapter extends RecyclerView.Adapter<AyuntamientosViewHolder> {
    private List<Ayuntamiento> data;
    private final RecyclerViewOnItemClickListener recyclerViewOnItemClickListener;

    public AyuntamientosAdapter(@NonNull List<Ayuntamiento> data, @NonNull RecyclerViewOnItemClickListener recyclerViewOnItemClickListener) {
        this.data = data;
        this.recyclerViewOnItemClickListener = recyclerViewOnItemClickListener;
    }

    @NonNull
    @Override
    public AyuntamientosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_ayuntamientos, parent, false);
        return new AyuntamientosViewHolder(row, recyclerViewOnItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AyuntamientosViewHolder holder, int position) {
        holder.bindRow(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
