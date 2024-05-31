package es.studium.losamigosdeviky.colonias;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.studium.losamigosdeviky.R;
import es.studium.losamigosdeviky.RecyclerViewOnItemClickListener;

public class ColoniasAdapter extends RecyclerView.Adapter<ColoniasViewHolder> {
    private List<Colonia> data;
    private final RecyclerViewOnItemClickListener recyclerViewOnItemClickListener;

    public ColoniasAdapter(@NonNull List<Colonia> data, @NonNull RecyclerViewOnItemClickListener recyclerViewOnItemClickListener) {
        this.data = data;
        this.recyclerViewOnItemClickListener = recyclerViewOnItemClickListener;
    }

    @NonNull
    @Override
    public ColoniasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_colonias, parent, false);
        return new ColoniasViewHolder(row, recyclerViewOnItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ColoniasViewHolder holder, int position) {
        holder.bindRow(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
