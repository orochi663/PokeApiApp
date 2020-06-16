package com.example.pokeapiapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

/**
 * Created by RTanweer on 11/3/2019.
 */
public class MoveAdapter extends RecyclerView.Adapter<MoveAdapter.ViewHolder>{

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;

        ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.move_name);
        }
    }

    private List<PokemonInfo.Move> data;

    MoveAdapter() {
        data = new ArrayList<>();
    }

    void setData(List<PokemonInfo.Move> newData) {
        data.clear();
        data.addAll(newData);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MoveAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View rootView = inflater.inflate(R.layout.row_layout_moves, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull MoveAdapter.ViewHolder holder, int position) {
        PokemonInfo.Move value = data.get(position);
        TextView name = holder.name;
        name.setText(value.name);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
